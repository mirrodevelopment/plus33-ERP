/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin — Task Governance & Report Approval
 * File              : store-tasks.js
 * Path              : frontend/modules/store-admin/pages/tasks/store-tasks.js
 * Purpose           : Controller component for Store Admin task management, preset dispatch, & report verification
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { storage } from '../../../../core/storage.js';
import { openExportModal } from '../../../../core/exportUtils.js';

const TEMPLATE_URL = 'modules/store-admin/pages/tasks/store-tasks.html';

export default class StoreTasks {

  constructor() {
    this.user = authStore.getUser() || {};
    this.tasks = [];
    this.kpis = {};
    this.employees = [];
  }

  async mount(container, lifecycle) {
    logger.info('StoreTasks', 'Mounting Store Admin Tasks Governance Page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    await this.fetchData(container);
    await this.loadEmployees(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'store-tasks-css';
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
      const res = await apiClient.get('/api/v1/store-tasks').catch(() => null);
      if (res && res.success && Array.isArray(res.data)) {
        apiTasks = res.data;
      }
    } catch (err) {
      logger.error('StoreTasks', 'Error fetching store tasks:', err);
    }

    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const taskMap = new Map();

    localTasks.forEach(t => {
      const key = `${(t.title || '').trim().toLowerCase()}_${t.createdAt || t.id}`;
      if (!taskMap.has(key)) taskMap.set(key, t);
    });

    apiTasks.forEach(t => {
      const key = `${(t.title || '').trim().toLowerCase()}_${t.createdAt || t.id}`;
      if (!taskMap.has(key)) taskMap.set(key, t);
    });

    this.tasks = Array.from(taskMap.values());
    this.tasks.sort((a, b) => {
      const timeA = new Date(a.createdAt || a.dueDate || Date.now()).getTime();
      const timeB = new Date(b.createdAt || b.dueDate || Date.now()).getTime();
      return timeB - timeA;
    });

    this.renderKpis(container);
    this.renderTaskList(container);
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
            email: emp.email || `${(emp.firstName || 'user').toLowerCase()}.${(emp.lastName || '').toLowerCase()}@plus33coffee.fr`,
            designation: emp.designation || 'Barista'
          }));
        }
      }
    } catch (err) {
      logger.warn('StoreTasks', 'API error loading employees:', err);
    }

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

    this.employees = filteredEmployees;

    let options = '<option value="all@plus33.com">👥 All Store Employees (Storewide Broadcast)</option>';
    if (this.employees && this.employees.length > 0) {
      options += this.employees.map(emp => {
        const desig = emp.designation ? ` (${emp.designation})` : '';
        return `<option value="${emp.email}">${emp.name}${desig} - ${emp.email}</option>`;
      }).join('');
    }

    if (select) select.innerHTML = options;
    cardSelects.forEach(sel => sel.innerHTML = options);
  }

  renderKpis(container) {
    const todayEl = container.querySelector('#admin-kpi-today');
    const compEl = container.querySelector('#admin-kpi-completed');
    const uncompEl = container.querySelector('#admin-kpi-uncompleted');

    if (todayEl) todayEl.textContent = this.tasks.length;
    if (compEl) compEl.textContent = this.tasks.filter(t => t.status === 'COMPLETED').length;
    if (uncompEl) uncompEl.textContent = this.tasks.filter(t => t.status !== 'COMPLETED').length;
  }

  formatTimeRemaining(dueDateStr) {
    if (!dueDateStr) return 'End of Shift';
    const due = new Date(dueDateStr).getTime();
    if (isNaN(due)) return 'End of Shift';

    const now = Date.now();
    const diffMs = due - now;

    if (diffMs <= 0) {
      const overdueMs = Math.abs(diffMs);
      const overdueMins = Math.floor(overdueMs / 60000);
      const hours = Math.floor(overdueMins / 60);
      const mins = overdueMins % 60;
      if (hours > 0) return `⚠️ Overdue by ${hours}H ${mins}M`;
      return `⚠️ Overdue by ${mins}M`;
    }

    const totalMins = Math.floor(diffMs / 60000);
    const hours = Math.floor(totalMins / 60);
    const mins = totalMins % 60;

    if (hours > 0) {
      return `⏳ ${hours}H ${mins}M Left`;
    }
    return `⏳ ${mins}M Left`;
  }

  renderTaskList(container) {
    const listEl = container.querySelector('#admin-store-tasks-list');
    if (!listEl) return;

    if (this.tasks.length === 0) {
      listEl.innerHTML = `<div style="text-align:center; padding:40px; color:#888;">No storewide tasks logged.</div>`;
      return;
    }

    listEl.innerHTML = this.tasks.map(t => {
      const isCompleted = t.status === 'COMPLETED';
      const isReviewPending = t.status === 'SUBMITTED_FOR_REVIEW';
      const isRejected = t.status === 'REJECTED';
      const isBlocked = t.status === 'BLOCKED';
      const hasReport = t.completionReport && (t.completionReport.notes || t.completionReport.fileName);

      let statusBadgeText = t.status;
      let statusStyle = 'background: rgba(59,130,246,0.15); color: #60a5fa; border: 1px solid rgba(59,130,246,0.3);';

      if (t.status === 'ASSIGNED' || t.status === 'PENDING') {
        statusBadgeText = 'Pending (Assigned)';
        statusStyle = 'background: rgba(245,158,11,0.15); color: #fbbf24; border: 1px solid rgba(245,158,11,0.3);';
      } else if (t.status === 'STARTED') {
        statusBadgeText = 'Started';
        statusStyle = 'background: rgba(59,130,246,0.15); color: #60a5fa; border: 1px solid rgba(59,130,246,0.3);';
      } else if (t.status === 'SUBMITTED_FOR_REVIEW') {
        statusBadgeText = '⏳ SUBMITTED FOR REVIEW';
        statusStyle = 'background: rgba(245,158,11,0.25); color: #fbbf24; border: 1px solid #f59e0b; font-weight: 800; box-shadow: 0 0 10px rgba(245,158,11,0.3);';
      } else if (t.status === 'COMPLETED') {
        statusBadgeText = '✓ Completed';
        statusStyle = 'background: rgba(16,185,129,0.15); color: #34d399; border: 1px solid rgba(16,185,129,0.3);';
      } else if (t.status === 'REJECTED') {
        statusBadgeText = '❌ Rejected by Barista';
        statusStyle = 'background: rgba(239,68,68,0.2); color: #f87171; border: 1px solid rgba(239,68,68,0.4);';
      } else if (t.status === 'BLOCKED') {
        statusBadgeText = '🚫 Blocked';
        statusStyle = 'background: rgba(239,68,68,0.15); color: #f87171; border: 1px solid rgba(239,68,68,0.3);';
      }

      const timeRemainingStr = this.formatTimeRemaining(t.dueDate);
      const isOverdue = timeRemainingStr.includes('Overdue');

      return `
        <div class="task-item" style="background: rgba(30,30,30,0.7); border: 1px solid rgba(255,255,255,0.08); border-radius: 12px; padding: 16px 20px; margin-bottom: 12px; display: flex; justify-content: space-between; align-items: center; gap: 16px;">
          <div>
            <div style="display: flex; gap: 8px; align-items: center; flex-wrap: wrap;">
              <span class="p-pill p-pill--important">${t.priority}</span>
              <span style="font-size: 0.72rem; padding: 3px 8px; border-radius: 6px; font-weight: 700; ${statusStyle}">${statusBadgeText}</span>
              <span style="font-size: 0.65rem; background: rgba(201,164,106,0.15); color: #c9a46a; padding: 2px 6px; border-radius: 4px;">${t.delegationMode || 'DIRECT'}</span>
              
              <span style="font-size: 0.72rem; font-weight: 700; padding: 3px 10px; border-radius: 6px; background: ${isCompleted ? 'rgba(16,185,129,0.15)' : isOverdue ? 'rgba(239,68,68,0.2)' : 'rgba(245,158,11,0.18)'}; color: ${isCompleted ? '#34d399' : isOverdue ? '#f87171' : '#fbbf24'}; border: 1px solid ${isCompleted ? 'rgba(16,185,129,0.3)' : isOverdue ? 'rgba(239,68,68,0.4)' : 'rgba(245,158,11,0.35)'};">
                ${isCompleted ? '✓ Done' : timeRemainingStr}
              </span>
            </div>
            <h4 style="font-family: var(--font-display); font-size: 0.98rem; color: #fff; margin: 6px 0 2px;">${t.title}</h4>
            ${t.rejectReason ? `<div style="font-size: 0.78rem; color: #ef4444; background: rgba(239,68,68,0.12); border: 1px solid rgba(239,68,68,0.3); padding: 4px 8px; border-radius: 6px; margin: 4px 0;">❌ <strong>Rejection Reason:</strong> ${t.rejectReason}</div>` : ''}
            ${t.extensionStatus === 'REQUESTED' ? `<div style="font-size: 0.78rem; color: #fbbf24; background: rgba(245,158,11,0.15); border: 1px solid rgba(245,158,11,0.35); padding: 6px 10px; border-radius: 6px; margin: 4px 0;">⏳ <strong>Extra Time Requested:</strong> "${t.extensionReason || 'Need additional time'}" (New Target: ${t.requestedDueDate ? String(t.requestedDueDate).replace('T',' ').substring(0,16) : 'N/A'})</div>` : ''}
            <div style="font-size: 0.75rem; color: #71717a; margin-top: 4px;">
              <span><strong>Assignee:</strong> ${t.assignedEmployeeName || 'Store Team'}</span> &bull; 
              <span><strong>Due:</strong> ${t.dueDate ? String(t.dueDate).replace('T', ' ').substring(0, 16) : 'End of Month'}</span>
            </div>
          </div>

          <!-- Direct Action Buttons: Always allow Manager to Approve & Complete -->
          <div style="display: flex; gap: 10px; align-items: center; flex-shrink: 0; flex-wrap: wrap;">
            ${t.extensionStatus === 'REQUESTED' ? `
              <button type="button" class="tasks-btn tasks-btn--gold btn-admin-approve-ext" data-id="${t.id}">✅ Approve Extra Time</button>
              <button type="button" class="tasks-btn tasks-btn--glass btn-admin-reject-ext" data-id="${t.id}" style="color: #f87171; border-color: rgba(239,68,68,0.4);">❌ Reject Extension</button>
            ` : ''}

            ${isReviewPending ? `
              <button type="button" class="tasks-btn tasks-btn--gold btn-admin-review-report" data-id="${t.id}" style="box-shadow: 0 0 12px rgba(201,164,106,0.4);">🔍 Review Report &amp; Approve</button>
            ` : ''}
            
            ${!isCompleted && !isReviewPending ? `
              <button type="button" class="tasks-btn tasks-btn--green btn-admin-approve-direct" data-id="${t.id}">✅ Approve &amp; Complete</button>
            ` : ''}

            ${isCompleted && hasReport ? `
              <button type="button" class="tasks-btn tasks-btn--glass btn-admin-view-report" data-id="${t.id}">📄 View Approved Report</button>
            ` : ''}

            ${isCompleted && !hasReport ? `
              <span style="font-size: 0.78rem; color: #34d399; font-weight: 700;">✓ Approved</span>
            ` : ''}

            ${(t.status === 'ASSIGNED' || t.status === 'PENDING') ? `
              <button type="button" class="tasks-btn tasks-btn--red btn-delete-admin-task" data-id="${t.id}">Remove</button>
            ` : `
              <span style="font-size: 0.72rem; color: #71717a; background: rgba(255,255,255,0.04); padding: 4px 8px; border-radius: 6px; border: 1px solid rgba(255,255,255,0.08);" title="Accepted, active, or completed directives cannot be deleted for store governance compliance">🔒 Cannot Delete</span>
            `}
          </div>
        </div>
      `;
    }).join('');

    this.bindItemEvents(container);
  }

  bindEvents(container, lifecycle) {
    const clearBtn = container.querySelector('#btn-clear-all-store-tasks');
    if (clearBtn) {
      clearBtn.addEventListener('click', () => {
        storage.set('plus33-custom-store-tasks', []);
        this.tasks = [];
        this.renderKpis(container);
        this.renderTaskList(container);
        notificationStore.success('All task records cleared cleanly!');
      });
    }

    const purgeBtn = container.querySelector('#btn-purge-all-db-data');
    if (purgeBtn) {
      purgeBtn.addEventListener('click', async () => {
        if (!confirm('⚠️ Are you sure you want to PURGE ALL tasks across the entire store database? This will clear tasks for all employees, shift supervisors, and staff under you.')) {
          return;
        }

        storage.set('plus33-custom-store-tasks', []);

        try {
          await apiClient.delete('/api/v1/store-tasks/purge-all');
        } catch (e) {
          logger.warn('StoreTasks', 'API purge all tasks error:', e);
        }

        this.tasks = [];
        this.renderKpis(container);
        this.renderTaskList(container);
        notificationStore.success('All store tasks purged cleanly across Database and Local Storage for all staff!');
      });
    }

    const pdfBtn = container.querySelector('#btn-export-task-pdf');
    if (pdfBtn) {
      pdfBtn.addEventListener('click', () => {
        openExportModal(this.tasks, 'PDF', 'Store Admin Task Status Report', 'store-admin-task-status-report.png');
      });
    }

    const imgBtn = container.querySelector('#btn-export-task-img');
    if (imgBtn) {
      imgBtn.addEventListener('click', () => {
        openExportModal(this.tasks, 'IMAGE', 'Store Admin Task Status Report', 'store-admin-task-status-report.png');
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

    // Admin Rejection Modal Handlers
    const adminRejModal = container.querySelector('#modal-admin-reject-report');
    const closeAdminRejBtn = container.querySelector('#btn-close-admin-reject-modal');
    const cancelAdminRejBtn = container.querySelector('#btn-cancel-admin-reject');
    const formAdminRej = container.querySelector('#form-admin-reject-report');

    const closeAdminRejModal = () => { if (adminRejModal) adminRejModal.style.display = 'none'; };
    if (closeAdminRejBtn) closeAdminRejBtn.addEventListener('click', closeAdminRejModal);
    if (cancelAdminRejBtn) cancelAdminRejBtn.addEventListener('click', closeAdminRejModal);

    if (formAdminRej) {
      formAdminRej.addEventListener('submit', async (e) => {
        e.preventDefault();
        const taskId = container.querySelector('#admin-rej-task-id').value;
        const rejectReason = container.querySelector('#admin-rej-reason').value;

        await this.rejectTaskWithReason(container, taskId, rejectReason);
        closeAdminRejModal();
      });
    }

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
          targetName = 'All Store Employees (Storewide Broadcast)';
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
            description: 'Immediate spill response: Wipe liquid spill on main barista counter and sanitize customer hand-off area immediately.',
            priority: 'IMMEDIATE',
            dueDate: new Date(now.getTime() + 1 * 3600000).toISOString()
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
        }).catch(err => logger.warn('StoreTasks', 'API post sync warning:', err));

        notificationStore.success(`Dispatched "${presetData.title}" to ${targetName}!`);
        await this.fetchData(container);
      });
    });

    if (formAdmin) {
      formAdmin.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
          const title = container.querySelector('#adm-title').value;
          const description = container.querySelector('#adm-desc').value;
          const assignedEmail = container.querySelector('#adm-assignee').value;
          const priority = container.querySelector('#adm-priority').value;
          const dueInput = container.querySelector('#adm-duedate').value;

          let targetName = 'Store Team';
          let delegationMode = 'DIRECT_EMPLOYEE';

          if (assignedEmail === 'all@plus33.com') {
            delegationMode = 'BROADCAST_ALL_EMPLOYEES';
            targetName = 'All Store Employees (Storewide Broadcast)';
          } else {
            const select = container.querySelector('#adm-assignee');
            if (select && select.selectedOptions && select.selectedOptions[0]) {
              targetName = select.selectedOptions[0].text.split(' - ')[0];
            }
          }

          const taskId = Date.now();
          const newTask = {
            id: taskId,
            title,
            description,
            category: 'GENERAL',
            delegationMode,
            assignedEmployeeEmail: assignedEmail,
            assignedEmployeeName: targetName,
            priority: priority || 'COMMON',
            dueDate: dueInput ? new Date(dueInput).toISOString() : new Date(Date.now() + 86400000).toISOString(),
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
          }).catch(err => logger.warn('StoreTasks', 'API post sync warning:', err));

          notificationStore.success(`Dispatched "${title}" to ${targetName}!`);
          closeModal();
          await this.fetchData(container);
        } catch (err) {
          logger.error('StoreTasks', 'Create task error:', err);
          closeModal();
        }
      });
    }

    // Modal elements for Report View
    const viewModal = container.querySelector('#modal-admin-view-report');
    const closeViewBtn = container.querySelector('#btn-close-admin-view-report-modal');
    const cancelViewBtn = container.querySelector('#btn-cancel-admin-view-report');

    const closeViewModal = () => { if (viewModal) viewModal.style.display = 'none'; };
    if (closeViewBtn) closeViewBtn.addEventListener('click', closeViewModal);
    if (cancelViewBtn) cancelViewBtn.addEventListener('click', closeViewModal);
  }

  bindItemEvents(container) {
    container.querySelectorAll('.btn-delete-admin-task').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        const targetTask = this.tasks.find(x => String(x.id) === String(id));
        if (targetTask && targetTask.status !== 'ASSIGNED' && targetTask.status !== 'PENDING') {
          notificationStore.error('Directives that have been accepted, started, or completed cannot be deleted.');
          return;
        }

        let localTasks = storage.get('plus33-custom-store-tasks') || [];
        localTasks = localTasks.filter(t => String(t.id) !== String(id));
        storage.set('plus33-custom-store-tasks', localTasks);

        try {
          await apiClient.delete(`/api/v1/store-tasks/${id}`);
        } catch (e) { }

        notificationStore.info(`Unaccepted task directive #${id} removed.`);
        await this.fetchData(container);
      });
    });

    container.querySelectorAll('.btn-admin-approve-ext').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        await this.reviewExtension(container, id, true);
      });
    });

    container.querySelectorAll('.btn-admin-reject-ext').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        await this.reviewExtension(container, id, false);
      });
    });

    container.querySelectorAll('.btn-admin-approve-direct').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        await this.approveTask(container, id);
      });
    });

    container.querySelectorAll('.btn-admin-review-report, .btn-admin-view-report').forEach(b => {
      b.addEventListener('click', () => {
        const id = b.dataset.id;
        const t = this.tasks.find(x => String(x.id) === String(id));
        if (!t || !t.completionReport) return;

        const rep = t.completionReport;
        const viewModal = container.querySelector('#modal-admin-view-report');
        const contentEl = container.querySelector('#admin-view-report-content');

        let imagePreviewHtml = '';
        if (rep.dataUrl && (rep.fileType || '').startsWith('image/')) {
          imagePreviewHtml = `
            <div style="margin-top: 10px;">
              <strong style="font-size: 0.78rem; color: #a1a1aa; display: block; margin-bottom: 6px;">ATTACHED PROOF PHOTO:</strong>
              <img src="${rep.dataUrl}" alt="Proof Image" style="max-width: 100%; max-height: 250px; border-radius: 8px; border: 1px solid rgba(255,255,255,0.15);" />
            </div>
          `;
        } else if (rep.fileName) {
          imagePreviewHtml = `
            <div style="margin-top: 10px; font-size: 0.82rem; color: #34d399;">
              📎 <strong>Attached Document:</strong> ${rep.fileName} (${rep.fileSize || ''})
            </div>
          `;
        }

        const isReviewPending = t.status === 'SUBMITTED_FOR_REVIEW';

        if (contentEl) {
          contentEl.innerHTML = `
            <div style="background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); padding: 14px; border-radius: 8px;">
              <strong style="color: #c9a46a; font-size: 0.82rem;">STORE DIRECTIVE:</strong>
              <h5 style="color: #fff; font-size: 0.95rem; margin: 4px 0 0;">${t.title}</h5>
            </div>

            <div style="background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); padding: 14px; border-radius: 8px;">
              <strong style="color: #f59e0b; font-size: 0.82rem;">SUBMITTED WORK NOTES &amp; PROOF:</strong>
              <p style="color: #e2e8f0; font-size: 0.88rem; margin: 6px 0 0; white-space: pre-line; line-height: 1.4;">${rep.notes || 'No detailed notes provided.'}</p>
            </div>

            <div style="display: flex; gap: 14px; flex-wrap: wrap;">
              <div style="flex: 1; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); padding: 10px 14px; border-radius: 8px;">
                <span style="font-size: 0.75rem; color: #a1a1aa;">AUDIT METRIC / READINGS</span>
                <div style="color: #fbbf24; font-weight: 700; font-size: 0.9rem; margin-top: 2px;">${rep.metric || 'N/A'}</div>
              </div>
              <div style="flex: 1; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); padding: 10px 14px; border-radius: 8px;">
                <span style="font-size: 0.75rem; color: #a1a1aa;">SUBMITTED BY &amp; TIME</span>
                <div style="color: #fff; font-size: 0.82rem; margin-top: 2px;">${rep.submittedBy || 'Store Staff'} &bull; ${rep.submittedAt ? String(rep.submittedAt).replace('T', ' ').substring(0, 16) : 'Just Now'}</div>
              </div>
            </div>

            ${imagePreviewHtml}

            ${isReviewPending ? `
              <div style="border-top: 1px solid rgba(255,255,255,0.1); padding-top: 16px; margin-top: 8px; display: flex; gap: 12px; justify-content: flex-end;">
                <button type="button" class="tasks-btn tasks-btn--glass btn-open-admin-reject-modal" data-id="${t.id}" data-title="${t.title.replace(/"/g, '&quot;')}" style="color: #f87171; border-color: rgba(239,68,68,0.4);">❌ Request Revision</button>
                <button type="button" class="tasks-btn tasks-btn--gold btn-admin-approve-report">✅ Approve &amp; Mark Completed</button>
              </div>
            ` : ''}
          `;

          if (isReviewPending) {
            contentEl.querySelector('.btn-admin-approve-report').addEventListener('click', async () => {
              await this.approveTask(container, t.id);
              if (viewModal) viewModal.style.display = 'none';
            });

            contentEl.querySelector('.btn-open-admin-reject-modal').addEventListener('click', (e) => {
              const rejId = e.target.dataset.id;
              const rejTitle = e.target.dataset.title;

              const rejModal = container.querySelector('#modal-admin-reject-report');
              const rejIdInput = container.querySelector('#admin-rej-task-id');
              const rejTitleInput = container.querySelector('#admin-rej-task-title');
              const rejReasonInput = container.querySelector('#admin-rej-reason');

              if (rejIdInput) rejIdInput.value = rejId;
              if (rejTitleInput) rejTitleInput.value = rejTitle;
              if (rejReasonInput) rejReasonInput.value = '';

              if (viewModal) viewModal.style.display = 'none';
              if (rejModal) rejModal.style.display = 'flex';
            });
          }
        }

        if (viewModal) viewModal.style.display = 'flex';
      });
    });
  }

  async approveTask(container, taskId) {
    const myEmail = this.user.username || this.user.email || '';

    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const localTask = localTasks.find(t => String(t.id) === String(taskId));
    if (localTask) {
      localTask.status = 'COMPLETED';
      if (localTask.completionReport) localTask.completionReport.approvalStatus = 'APPROVED';
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memTask) {
      memTask.status = 'COMPLETED';
      if (memTask.completionReport) memTask.completionReport.approvalStatus = 'APPROVED';
    }

    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/status?status=COMPLETED&userEmail=${encodeURIComponent(myEmail)}`);
    } catch (err) {
      logger.warn('StoreTasks', 'API status update warning:', err);
    }

    notificationStore.success(`Task #${taskId} verified & approved as COMPLETED!`);
    await this.fetchData(container);
  }

  async rejectTaskWithReason(container, taskId, rejectReason) {
    const myEmail = this.user.username || this.user.email || '';

    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const localTask = localTasks.find(t => String(t.id) === String(taskId));
    if (localTask) {
      localTask.status = 'IN_PROGRESS';
      localTask.supervisorRejectReason = rejectReason;
      if (localTask.completionReport) localTask.completionReport.approvalStatus = 'REVISION_REQUESTED';
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memTask) {
      memTask.status = 'IN_PROGRESS';
      memTask.supervisorRejectReason = rejectReason;
      if (memTask.completionReport) memTask.completionReport.approvalStatus = 'REVISION_REQUESTED';
    }

    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/status?status=IN_PROGRESS&userEmail=${encodeURIComponent(myEmail)}`);
    } catch (err) {
      logger.warn('StoreTasks', 'API status update warning:', err);
    }

    notificationStore.info(`Revision requested for Task #${taskId} with reason: "${rejectReason}"`);
    await this.fetchData(container);
  }

  async reviewExtension(container, taskId, approved) {
    const myEmail = this.user.username || this.user.email || '';
    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const localTask = localTasks.find(t => String(t.id) === String(taskId));
    if (localTask) {
      localTask.extensionStatus = approved ? 'APPROVED' : 'REJECTED';
      if (approved && localTask.requestedDueDate) {
        localTask.dueDate = localTask.requestedDueDate;
      }
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memTask) {
      memTask.extensionStatus = approved ? 'APPROVED' : 'REJECTED';
      if (approved && memTask.requestedDueDate) {
        memTask.dueDate = memTask.requestedDueDate;
      }
    }

    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/extension-review?userEmail=${encodeURIComponent(myEmail)}`, {
        approved,
        reviewNotes: approved ? 'Extra time extension approved by assigner' : 'Extra time request declined'
      });
    } catch (err) {
      logger.warn('StoreTasks', 'API extension review warning:', err);
    }

    if (approved) {
      notificationStore.success(`Approved extra time extension for Task #${taskId}!`);
    } else {
      notificationStore.info(`Declined extra time extension for Task #${taskId}.`);
    }
    await this.fetchData(container);
  }
}


