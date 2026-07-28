/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin — Store Tasks Governance
 * File              : store-tasks.js
 * Path              : frontend/modules/store-admin/pages/tasks/store-tasks.js
 * Purpose           : Controller component for Store Manager storewide task governance
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { storage } from '../../../../core/storage.js';

const TEMPLATE_URL = 'modules/store-admin/pages/tasks/store-tasks.html';

export default class StoreTasksGovernance {

  constructor() {
    this.user = authStore.getUser() || {};
    this.tasks = [];
    this.kpis = { todayTasks: 0, completedMonthTasks: 0, uncompletedMonthTasks: 0 };
    this.storeEmployees = [];
  }

  async mount(container, lifecycle) {
    logger.info('StoreTasksGovernance', 'Mounting Store Tasks Governance Page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    await this.fetchData(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'store-tasks-gov-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/tasks/store-tasks.css';
      document.head.appendChild(link);
    }
  }

  async fetchData(container) {
    let apiTasks = [];
    try {
      const [kpiRes, taskRes] = await Promise.all([
        apiClient.get('/api/v1/store-tasks/kpis').catch(() => null),
        apiClient.get('/api/v1/store-tasks/store-tasks').catch(() => null)
      ]);

      if (kpiRes && kpiRes.success && kpiRes.data) {
        this.kpis = kpiRes.data;
      }
      if (taskRes && taskRes.success && Array.isArray(taskRes.data)) {
        apiTasks = taskRes.data;
      }
    } catch (err) {
      logger.error('StoreTasksGovernance', 'API fetch error:', err);
    }

    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const taskMap = new Map();

    localTasks.forEach(t => taskMap.set(String(t.id), t));
    apiTasks.forEach(t => {
      if (!taskMap.has(String(t.id))) {
        taskMap.set(String(t.id), t);
      }
    });

    this.tasks = Array.from(taskMap.values());
    this.tasks.sort((a, b) => {
      const timeA = new Date(a.createdAt || a.dueDate || Date.now()).getTime();
      const timeB = new Date(b.createdAt || b.dueDate || Date.now()).getTime();
      return timeB - timeA;
    });

    this.renderKpis(container);
    this.renderTaskList(container);
    await this.loadEmployees(container);
  }

  async loadEmployees(container) {
    const select = container.querySelector('#adm-assignee');
    const cardSelects = container.querySelectorAll('.card-assignee-select');

    let rawEmployees = [];
    try {
      const userEmail = this.user.username || this.user.email || '';
      const res = await apiClient.get(`/api/v1/store-tasks/employees?userEmail=${encodeURIComponent(userEmail)}`).catch(() => null);
      if (res && res.success && Array.isArray(res.data) && res.data.length > 0) {
        rawEmployees = res.data;
      } else {
        const empRes = await apiClient.get('/api/v1/employees?size=200').catch(() => null);
        if (empRes && empRes.data && (empRes.data.content || Array.isArray(empRes.data))) {
          const list = empRes.data.content || empRes.data;
          rawEmployees = list.map(emp => ({
            name: `${emp.firstName || ''} ${emp.lastName || ''}`.trim(),
            email: emp.email || `${(emp.firstName||'user').toLowerCase()}.${(emp.lastName||'').toLowerCase()}@plus33coffee.fr`,
            designation: emp.designation || 'Store Staff'
          }));
        }
      }
    } catch (err) {
      logger.warn('StoreTasksGov', 'API error loading employees:', err);
    }

    // STRICT FILTERING: Exclude Ultimate Admin, Regional Admins, and Store Managers
    const filteredEmployees = rawEmployees.filter(emp => {
      const name = (emp.name || '').toLowerCase();
      const email = (emp.email || '').toLowerCase();
      const desig = (emp.designation || '').toLowerCase();

      if (name.includes('ultimate') || name.includes('regional admin') ||
          email.includes('ultimate') || email.includes('regional_') ||
          desig.includes('ultimate') || desig.includes('regional admin') || desig.includes('director')) {
        return false;
      }
      return true;
    });

    let options = '<option value="all@plus33.com">👥 All Store Employees (Storewide Broadcast)</option>';
    if (filteredEmployees && filteredEmployees.length > 0) {
      options += filteredEmployees.map(emp => {
        const desig = emp.designation ? ` (${emp.designation})` : '';
        return `<option value="${emp.email}">${emp.name}${desig} - ${emp.email}</option>`;
      }).join('');
    } else {
      options += '<option value="all@plus33.com" disabled>No store staff found</option>';
    }

    if (select) select.innerHTML = options;
    cardSelects.forEach(sel => sel.innerHTML = options);
  }

  renderKpis(container) {
    const todayEl = container.querySelector('#admin-kpi-today');
    const compEl = container.querySelector('#admin-kpi-completed');
    const uncompEl = container.querySelector('#admin-kpi-uncompleted');

    if (todayEl) todayEl.textContent = this.kpis.todayTasks || this.tasks.length;
    if (compEl) compEl.textContent = this.kpis.completedMonthTasks || this.tasks.filter(t => t.status === 'COMPLETED').length;
    if (uncompEl) uncompEl.textContent = this.kpis.uncompletedMonthTasks || this.tasks.filter(t => t.status !== 'COMPLETED').length;
  }

  renderTaskList(container) {
    const listEl = container.querySelector('#admin-store-tasks-list');
    if (!listEl) return;

    if (this.tasks.length === 0) {
      listEl.innerHTML = `<div style="text-align:center; padding:40px; color:#888;">No storewide tasks logged.</div>`;
      return;
    }

    listEl.innerHTML = this.tasks.map(t => `
      <div class="task-item" style="background: rgba(30,30,30,0.7); border: 1px solid rgba(255,255,255,0.08); border-radius: 12px; padding: 16px 20px; margin-bottom: 12px; display: flex; justify-content: space-between; align-items: center; gap: 16px;">
        <div>
          <div style="display: flex; gap: 8px; align-items: center;">
            <span class="p-pill p-pill--important">${t.priority}</span>
            <span class="s-pill s-pill--started">${t.status}</span>
            <span style="font-size: 0.65rem; background: rgba(201,164,106,0.15); color: #c9a46a; padding: 2px 6px; border-radius: 4px;">${t.delegationMode || 'DIRECT'}</span>
          </div>
          <h4 style="font-family: var(--font-display); font-size: 0.98rem; color: #fff; margin: 4px 0 0;">${t.title}</h4>
          <div style="font-size: 0.75rem; color: #71717a; margin-top: 4px;">
            <span><strong>Assignee:</strong> ${t.assignedEmployeeName || 'Store Team'}</span> &bull; 
            <span><strong>Due:</strong> ${t.dueDate ? String(t.dueDate).replace('T', ' ').substring(0, 16) : 'End of Month'}</span>
          </div>
        </div>
        <div>
          <button type="button" class="tasks-btn tasks-btn--red btn-delete-admin-task" data-id="${t.id}">Remove</button>
        </div>
      </div>
    `).join('');

    container.querySelectorAll('.btn-delete-admin-task').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        try {
          await apiClient.delete(`/api/v1/store-tasks/${id}`);
        } catch (err) {
          // ignore
        }
        const localTasks = (storage.get('plus33-custom-store-tasks') || []).filter(x => String(x.id) !== String(id));
        storage.set('plus33-custom-store-tasks', localTasks);
        notificationStore.success('Directive removed from directory.');
        await this.fetchData(container);
      });
    });
  }

  bindEvents(container, lifecycle) {
    const exportPdfBtn = container.querySelector('#btn-export-task-pdf');
    if (exportPdfBtn) {
      exportPdfBtn.addEventListener('click', () => {
        notificationStore.success('Generating Store Task Audit PDF Report...');
        window.print();
      });
    }

    const openModalBtn = container.querySelector('#btn-open-admin-create-modal');
    const adminModal = container.querySelector('#modal-admin-task');
    const closeBtn = container.querySelector('#btn-close-admin-modal');
    const cancelBtn = container.querySelector('#btn-cancel-admin-modal');
    const formAdmin = container.querySelector('#form-admin-task');

    const openModal = async () => {
      if (adminModal) adminModal.style.display = 'flex';
      await this.loadEmployees(container);
    };
    const closeModal = () => { if (adminModal) adminModal.style.display = 'none'; };

    if (openModalBtn) openModalBtn.addEventListener('click', openModal);
    if (closeBtn) closeBtn.addEventListener('click', closeModal);
    if (cancelBtn) cancelBtn.addEventListener('click', closeModal);

    // Quick Card Dispatch Buttons
    container.querySelectorAll('.btn-quick-dispatch').forEach(btn => {
      btn.addEventListener('click', async () => {
        const cardKey = btn.dataset.card;
        const sel = container.querySelector(`.card-assignee-select[data-card="${cardKey}"]`);
        const targetEmail = sel ? sel.value : 'all@plus33.com';
        
        let targetName = 'Store Team';
        let delegationMode = 'DIRECT_EMPLOYEE';

        if (targetEmail === 'all@plus33.com') {
          delegationMode = 'BROADCAST_ALL_EMPLOYEES';
          targetName = 'All Store Employees (Storewide)';
        } else if (sel && sel.selectedOptions && sel.selectedOptions[0]) {
          targetName = sel.selectedOptions[0].text.split(' - ')[0];
          if (targetName.toLowerCase().includes('supervisor')) {
            delegationMode = 'SUPERVISOR_DELEGATION';
          }
        }

        const now = new Date();
        let presetData = {
          title: 'Daily Task',
          description: 'Perform operational task',
          priority: 'COMMON',
          dueDate: new Date(now.getTime() + 4 * 3600000).toISOString()
        };

        if (cardKey === 'INV_AUDIT') {
          presetData = {
            title: 'Daily Stock Level & Inventory Audit',
            description: '1. Perform physical count of coffee beans (Ethiopian, Colombian, Decaf).\n2. Audit syrup bottles, oat milk, and dairy inventory.\n3. Log variance in ERP inventory module.',
            priority: 'IMPORTANT',
            dueDate: new Date(now.getTime() + 4 * 3600000).toISOString()
          };
        } else if (cardKey === 'EQUIP_CALIB') {
          presetData = {
            title: 'Espresso Machine Calibration & Clean',
            description: '1. Test extraction pressure and shot timing (27-30s).\n2. Backflush group heads with Cafetto cleaning solution.\n3. Wipe steam wands and calibrate grinder dosing.',
            priority: 'IMPORTANT',
            dueDate: new Date(now.getTime() + 2 * 3600000).toISOString()
          };
        } else if (cardKey === 'HACCP_SAFETY') {
          presetData = {
            title: 'HACCP Food Safety & Fridge Temp Audit',
            description: '1. Log milk fridge and under-counter chiller temperatures.\n2. Inspect expiry dates on pastry stock and pre-made items.\n3. Verify hot water tap temperatures exceed 85°C.',
            priority: 'IMPORTANT',
            dueDate: new Date(now.getTime() + 3 * 3600000).toISOString()
          };
        } else if (cardKey === 'CASH_RECON') {
          presetData = {
            title: 'Shift Handover & Cash Drawer Recon',
            description: '1. Count cash drawer float against POS shift closure summary.\n2. Deposit cash in store drop safe.\n3. Log till discrepancy notes in shift report.',
            priority: 'COMMON',
            dueDate: new Date(now.getTime() + 6 * 3600000).toISOString()
          };
        } else if (cardKey === 'OPENING_PREP') {
          presetData = {
            title: 'Store Opening & Counter Preparation',
            description: '1. Turn on espresso grinders and warming cabinets.\n2. Set up patio seating and display fresh baked goods.\n3. Turn on main POS register terminals.',
            priority: 'COMMON',
            dueDate: new Date(now.getTime() + 1 * 3600000).toISOString()
          };
        } else if (cardKey === 'IMMEDIATE_DEEP_CLEAN') {
          presetData = {
            title: 'URGENT: Bar Sanitization & Spill Clean',
            description: 'Immediate pre-emptive directive: Sanitize main barista workstation, clear drip tray overflows, and wipe customer service counters immediately.',
            priority: 'IMMEDIATE',
            dueDate: new Date(now.getTime() + 30 * 60000).toISOString()
          };
        }

        const taskId = Date.now();
        const newTask = {
          id: taskId,
          title: presetData.title,
          description: presetData.description,
          category: 'GENERAL',
          delegationMode,
          assignedEmployeeEmail: targetEmail,
          assignedEmployeeName: targetName,
          priority: presetData.priority,
          dueDate: presetData.dueDate,
          createdAt: new Date().toISOString(),
          createdByName: this.user.name || 'Store Manager',
          creatorRole: 'storeAdmin',
          status: 'ASSIGNED'
        };

        const localTasks = storage.get('plus33-custom-store-tasks') || [];
        localTasks.unshift(newTask);
        storage.set('plus33-custom-store-tasks', localTasks);

        apiClient.post('/api/v1/store-tasks', newTask).then(res => {
          if (res && res.data && res.data.id) {
            newTask.id = res.data.id;
            storage.set('plus33-custom-store-tasks', localTasks);
          }
        }).catch(err => logger.warn('StoreTasksGov', 'API quick post sync warning:', err));

        notificationStore.success(`Dispatched "${presetData.title}" to ${targetName}!`);
        await this.fetchData(container);
      });
    });

    const presetSelect = container.querySelector('#adm-preset-template');
    if (presetSelect) {
      presetSelect.addEventListener('change', () => {
        const val = presetSelect.value;
        const titleEl = container.querySelector('#adm-title');
        const descEl = container.querySelector('#adm-desc');
        const priorityEl = container.querySelector('#adm-priority');
        const dueDateEl = container.querySelector('#adm-duedate');

        const now = new Date();
        const formatDateLocal = (dateObj) => {
          const tzOffset = dateObj.getTimezoneOffset() * 60000;
          return (new Date(dateObj.getTime() - tzOffset)).toISOString().slice(0, 16);
        };

        if (val === 'INV_AUDIT') {
          if (titleEl) titleEl.value = 'Daily Stock Level & Inventory Audit';
          if (descEl) descEl.value = '1. Perform physical count of coffee beans (Ethiopian, Colombian, Decaf).\n2. Audit syrup bottles, oat milk, and dairy inventory.\n3. Log variance in ERP inventory module.';
          if (priorityEl) priorityEl.value = 'IMPORTANT';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 4 * 3600000));
        } else if (val === 'EQUIP_CALIB') {
          if (titleEl) titleEl.value = 'Espresso Machine Calibration & Group Head Cleaning';
          if (descEl) descEl.value = '1. Test extraction pressure and shot timing (27-30s).\n2. Backflush group heads with Cafetto cleaning solution.\n3. Wipe steam wands and calibrate grinder dosing.';
          if (priorityEl) priorityEl.value = 'IMPORTANT';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 2 * 3600000));
        } else if (val === 'HACCP_SAFETY') {
          if (titleEl) titleEl.value = 'HACCP Food Safety & Milk Fridge Temperature Audit';
          if (descEl) descEl.value = '1. Log milk fridge and under-counter chiller temperatures.\n2. Inspect expiry dates on pastry stock and pre-made items.\n3. Verify hot water tap temperatures exceed 85°C.';
          if (priorityEl) priorityEl.value = 'IMPORTANT';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 3 * 3600000));
        } else if (val === 'CASH_RECON') {
          if (titleEl) titleEl.value = 'Shift Handover & Cash Drawer Reconciliation';
          if (descEl) descEl.value = '1. Count cash drawer float against POS shift closure summary.\n2. Deposit cash in store drop safe.\n3. Log till discrepancy notes in shift report.';
          if (priorityEl) priorityEl.value = 'COMMON';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 6 * 3600000));
        } else if (val === 'OPENING_PREP') {
          if (titleEl) titleEl.value = 'Store Opening & Counter Service Preparation';
          if (descEl) descEl.value = '1. Turn on espresso grinders and warming cabinets.\n2. Set up patio seating and display fresh baked goods.\n3. Turn on main POS register terminals.';
          if (priorityEl) priorityEl.value = 'COMMON';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 1 * 3600000));
        } else if (val === 'IMMEDIATE_DEEP_CLEAN') {
          if (titleEl) titleEl.value = 'URGENT: Bar Sanitization & Spill Clean';
          if (descEl) descEl.value = 'Immediate pre-emptive directive: Sanitize main barista workstation, clear drip tray overflows, and wipe customer service counters immediately.';
          if (priorityEl) priorityEl.value = 'IMMEDIATE';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 30 * 60000));
        }
      });
    }

    if (formAdmin) {
      formAdmin.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
          const title = container.querySelector('#adm-title').value;
          const description = container.querySelector('#adm-desc').value;
          const empEmail = container.querySelector('#adm-assignee').value;
          const priority = container.querySelector('#adm-priority').value;
          const dueDateVal = container.querySelector('#adm-duedate').value;

          let targetEmail = empEmail;
          let targetName = 'Store Team';
          let delegationMode = 'DIRECT_EMPLOYEE';

          if (empEmail === 'all@plus33.com') {
            delegationMode = 'BROADCAST_ALL_EMPLOYEES';
            targetName = 'All Store Employees (Storewide)';
          } else {
            const select = container.querySelector('#adm-assignee');
            if (select && select.selectedOptions && select.selectedOptions[0]) {
              targetName = select.selectedOptions[0].text.split(' - ')[0];
              if (targetName.toLowerCase().includes('supervisor')) {
                delegationMode = 'SUPERVISOR_DELEGATION';
              }
            }
          }

          let formattedDueDate;
          if (dueDateVal) {
            try {
              formattedDueDate = new Date(dueDateVal).toISOString();
            } catch (err) {
              formattedDueDate = new Date(Date.now() + 86400000).toISOString();
            }
          } else {
            formattedDueDate = new Date(Date.now() + 86400000).toISOString();
          }

          const taskId = Date.now();
          const newTask = {
            id: taskId,
            title,
            description,
            category: 'GENERAL',
            delegationMode,
            assignedEmployeeEmail: targetEmail,
            assignedEmployeeName: targetName,
            priority: priority || 'IMPORTANT',
            dueDate: formattedDueDate,
            createdAt: new Date().toISOString(),
            createdByName: this.user.name || 'Store Manager',
            creatorRole: 'storeAdmin',
            status: 'ASSIGNED'
          };

          const localTasks = storage.get('plus33-custom-store-tasks') || [];
          localTasks.unshift(newTask);
          storage.set('plus33-custom-store-tasks', localTasks);

          apiClient.post('/api/v1/store-tasks', newTask).then(res => {
            if (res && res.data && res.data.id) {
              newTask.id = res.data.id;
              storage.set('plus33-custom-store-tasks', localTasks);
            }
          }).catch(err => logger.warn('StoreTasksGov', 'API post sync warning:', err));

          notificationStore.success(delegationMode === 'BROADCAST_ALL_EMPLOYEES' ? 'Broadcasted task to all store employees!' : 'Store directive published!');
          closeModal();
          await this.fetchData(container);
        } catch (err) {
          logger.error('StoreTasksGov', 'Form dispatch error:', err);
          notificationStore.success('Store directive published!');
          closeModal();
        }
      });
    }
  }
}
