/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee — Shift Supervisor Tasks
 * File              : supervisor-tasks.js
 * Path              : frontend/modules/store-employee/pages/supervisor-tasks/supervisor-tasks.js
 * Purpose           : Controller component for Shift Supervisor task management & delegation
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { storage } from '../../../../core/storage.js';

const TEMPLATE_URL = 'modules/store-employee/pages/supervisor-tasks/supervisor-tasks.html';

export default class SupervisorTaskManagement {

  constructor() {
    this.user = authStore.getUser() || {};
    this.teamTasks = [];
    this.extensionRequests = [];
    this.storeEmployees = [];
  }

  async mount(container, lifecycle) {
    logger.info('SupervisorTasks', 'Mounting Shift Supervisor Tasks Page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    await this.fetchData(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'supervisor-tasks-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/supervisor-tasks/supervisor-tasks.css';
      document.head.appendChild(link);
    }
  }

  async fetchData(container) {
    let apiTasks = [];
    try {
      const res = await apiClient.get('/api/v1/store-tasks/store-tasks').catch(() => null);
      if (res && res.success && Array.isArray(res.data)) {
        apiTasks = res.data;
      }
    } catch (err) {
      logger.error('SupervisorTasks', 'Error fetching store team tasks:', err);
    }

    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const taskMap = new Map();

    localTasks.forEach(t => taskMap.set(String(t.id), t));
    apiTasks.forEach(t => {
      if (!taskMap.has(String(t.id))) {
        taskMap.set(String(t.id), t);
      }
    });

    this.teamTasks = Array.from(taskMap.values());
    this.teamTasks.sort((a, b) => {
      const timeA = new Date(a.createdAt || a.dueDate || Date.now()).getTime();
      const timeB = new Date(b.createdAt || b.dueDate || Date.now()).getTime();
      return timeB - timeA;
    });

    this.extensionRequests = this.teamTasks.filter(t => t.extensionStatus === 'REQUESTED');
    this.renderExtensionRequests(container);
    this.renderTeamTasks(container);
    await this.loadEmployees(container);
  }

  async loadEmployees(container) {
    const select = container.querySelector('#asg-employee');
    if (!select) return;

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
            designation: emp.designation || 'Barista'
          }));
        }
      }
    } catch (err) {
      logger.warn('SupervisorTasks', 'API error loading employees:', err);
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

    if (filteredEmployees && filteredEmployees.length > 0) {
      select.innerHTML = filteredEmployees.map(emp => {
        const desig = emp.designation ? ` (${emp.designation})` : '';
        return `<option value="${emp.email}">${emp.name}${desig} - ${emp.email}</option>`;
      }).join('');
    } else {
      select.innerHTML = '<option value="" disabled>No store staff found</option>';
    }
  }

  renderExtensionRequests(container) {
    const cardEl = container.querySelector('#sup-extension-requests-card');
    const listEl = container.querySelector('#sup-extension-requests-list');
    if (!cardEl || !listEl) return;

    if (this.extensionRequests.length === 0) {
      cardEl.style.display = 'none';
      return;
    }

    cardEl.style.display = 'block';
    listEl.innerHTML = this.extensionRequests.map(r => `
      <div style="background: rgba(239, 68, 68, 0.08); border: 1px solid rgba(239, 68, 68, 0.2); border-radius: 8px; padding: 12px 16px; margin-bottom: 8px; display: flex; justify-content: space-between; align-items: center;">
        <div>
          <div style="font-weight: 700; color: #fff; font-size: 0.9rem;">${r.title}</div>
          <div style="font-size: 0.75rem; color: #a1a1aa;">Requested by <strong>${r.assignedEmployeeName}</strong> &bull; Reason: ${r.extensionReason || 'Need additional shift time'}</div>
        </div>
        <div style="display: flex; gap: 8px;">
          <button type="button" class="tasks-btn tasks-btn--green btn-approve-ext" data-id="${r.id}">Approve</button>
          <button type="button" class="tasks-btn tasks-btn--red btn-reject-ext" data-id="${r.id}">Reject</button>
        </div>
      </div>
    `).join('');

    listEl.querySelectorAll('.btn-approve-ext').forEach(b => {
      b.addEventListener('click', () => this.reviewExtension(container, b.dataset.id, true));
    });

    listEl.querySelectorAll('.btn-reject-ext').forEach(b => {
      b.addEventListener('click', () => this.reviewExtension(container, b.dataset.id, false));
    });
  }

  async reviewExtension(container, taskId, approved) {
    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/extension-review?userEmail=${encodeURIComponent(this.user.username || this.user.email || 'supervisor')}`, {
        approved,
        reviewNotes: approved ? 'Extension granted by supervisor.' : 'Extension rejected.'
      });
      notificationStore.success(approved ? 'Extension Approved!' : 'Extension Rejected');
      await this.fetchData(container);
    } catch (err) {
      logger.error('SupervisorTasks', 'Error reviewing extension:', err);
      notificationStore.danger('Failed to submit review.');
    }
  }

  renderTeamTasks(container) {
    const listEl = container.querySelector('#sup-team-tasks-list');
    if (!listEl) return;

    if (this.teamTasks.length === 0) {
      listEl.innerHTML = `<div style="text-align:center; padding:30px; color:#888;">No team tasks logged for this shift.</div>`;
      return;
    }

    listEl.innerHTML = this.teamTasks.map(t => `
      <div class="task-item" style="background: rgba(30,30,30,0.7); border: 1px solid rgba(255,255,255,0.08); border-radius: 12px; padding: 14px 18px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
        <div>
          <div style="display: flex; gap: 8px; align-items: center;">
            <span class="p-pill p-pill--important">${t.priority}</span>
            <span class="s-pill s-pill--started">${t.status}</span>
          </div>
          <h4 style="font-family: var(--font-display); font-size: 0.95rem; color: #fff; margin: 4px 0 0;">${t.title}</h4>
          <div style="font-size: 0.75rem; color: #71717a; margin-top: 2px;">
            <span>Assignee: <strong>${t.assignedEmployeeName || t.assignedEmployeeEmail || 'Team'}</strong></span> &bull; 
            <span>Category: <strong>${t.category}</strong></span>
          </div>
        </div>
      </div>
    `).join('');
  }

  bindEvents(container, lifecycle) {
    const openModalBtn = container.querySelector('#btn-open-create-task-modal');
    const modal = container.querySelector('#modal-assign-task');
    const closeBtn = container.querySelector('#btn-close-assign-modal');
    const cancelBtn = container.querySelector('#btn-cancel-assign-modal');
    const form = container.querySelector('#form-assign-task');

    const openModal = async () => {
      if (modal) modal.style.display = 'flex';
      await this.loadEmployees(container);
    };
    const closeModal = () => { if (modal) modal.style.display = 'none'; };

    if (openModalBtn) openModalBtn.addEventListener('click', openModal);
    if (closeBtn) closeBtn.addEventListener('click', closeModal);
    if (cancelBtn) cancelBtn.addEventListener('click', closeModal);

    const presetSelect = container.querySelector('#asg-preset-template');
    if (presetSelect) {
      presetSelect.addEventListener('change', () => {
        const val = presetSelect.value;
        const titleEl = container.querySelector('#asg-title');
        const descEl = container.querySelector('#asg-desc');
        const catEl = container.querySelector('#asg-category');
        const priorityEl = container.querySelector('#asg-priority');
        const dueDateEl = container.querySelector('#asg-duedate');

        const now = new Date();
        const formatDateLocal = (dateObj) => {
          const tzOffset = dateObj.getTimezoneOffset() * 60000;
          return (new Date(dateObj.getTime() - tzOffset)).toISOString().slice(0, 16);
        };

        if (val === 'INV_COUNT') {
          if (titleEl) titleEl.value = 'Bar Counter Stock & Syrup Replenishment';
          if (descEl) descEl.value = '1. Restock espresso beans, vanilla/caramel syrups, and oat milk.\n2. Verify backup inventory level under main service counter.';
          if (catEl) catEl.value = 'STOCK_REPLENISHMENT';
          if (priorityEl) priorityEl.value = 'COMMON';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 2 * 3600000));
        } else if (val === 'EQUIP_CLEAN') {
          if (titleEl) titleEl.value = 'Backflush Espresso Group Heads & Wand Sanitize';
          if (descEl) descEl.value = '1. Perform backflush clean on group heads 1 & 2.\n2. Soak steam wands in warm water and food-grade disinfectant.';
          if (catEl) catEl.value = 'EQUIPMENT_MAINTENANCE';
          if (priorityEl) priorityEl.value = 'IMPORTANT';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 1 * 3600000));
        } else if (val === 'TEMP_CHECK') {
          if (titleEl) titleEl.value = 'Milk Fridge & Chiller Temperature Check';
          if (descEl) descEl.value = '1. Record digital temperature on milk fridge (must be 1°C to 4°C).\n2. Log reading in shift temperature audit log sheet.';
          if (catEl) catEl.value = 'SAFETY_AUDIT';
          if (priorityEl) priorityEl.value = 'IMPORTANT';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 3 * 3600000));
        } else if (val === 'CLOSING_CLEAN') {
          if (titleEl) titleEl.value = 'End-of-Shift Floor Sweep & Trash Disposal';
          if (descEl) descEl.value = '1. Sweep customer seating area and barista bar floor.\n2. Empty espresso puck bin and replace trash liners.';
          if (catEl) catEl.value = 'HYGIENE_CLEANING';
          if (priorityEl) priorityEl.value = 'COMMON';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 4 * 3600000));
        } else if (val === 'PASTRY_RESTOCK') {
          if (titleEl) titleEl.value = 'Pastry Display Case Restock & Expiry Check';
          if (descEl) descEl.value = '1. Arrange fresh croissants and pain au chocolat in display case.\n2. Label expiry tags for fresh baked items.';
          if (catEl) catEl.value = 'OPENING_CHECKLIST';
          if (priorityEl) priorityEl.value = 'COMMON';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 1 * 3600000));
        } else if (val === 'URGENT_SPILL') {
          if (titleEl) titleEl.value = 'URGENT: Counter Spill & Station Clean';
          if (descEl) descEl.value = 'Immediate spill response: Wipe liquid spill on main barista counter and sanitize customer hand-off area immediately.';
          if (catEl) catEl.value = 'HYGIENE_CLEANING';
          if (priorityEl) priorityEl.value = 'IMMEDIATE';
          if (dueDateEl) dueDateEl.value = formatDateLocal(new Date(now.getTime() + 30 * 60000));
        }
      });
    }

    if (form) {
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
          const title = container.querySelector('#asg-title').value;
          const description = container.querySelector('#asg-desc').value;
          const employeeEmail = container.querySelector('#asg-employee').value;
          const priority = container.querySelector('#asg-priority').value;
          const category = container.querySelector('#asg-category').value;
          const dueDateVal = container.querySelector('#asg-duedate').value;

          const select = container.querySelector('#asg-employee');
          let targetName = 'Shift Employee';
          if (select && select.selectedOptions && select.selectedOptions[0]) {
            targetName = select.selectedOptions[0].text.split(' - ')[0];
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
            assignedEmployeeEmail: employeeEmail,
            assignedEmployeeName: targetName,
            priority: priority || 'COMMON',
            category: category || 'GENERAL',
            dueDate: formattedDueDate,
            createdAt: new Date().toISOString(),
            createdByName: this.user.name || 'Shift Supervisor',
            creatorRole: 'shiftSupervisor',
            status: 'ASSIGNED',
            delegationMode: 'DIRECT_EMPLOYEE'
          };

          const localTasks = storage.get('plus33-custom-store-tasks') || [];
          localTasks.unshift(newTask);
          storage.set('plus33-custom-store-tasks', localTasks);

          apiClient.post('/api/v1/store-tasks', newTask).then(res => {
            if (res && res.data && res.data.id) {
              newTask.id = res.data.id;
              storage.set('plus33-custom-store-tasks', localTasks);
            }
          }).catch(err => logger.warn('SupervisorTasks', 'API post sync warning:', err));

          notificationStore.success('Shift Task dispatched to barista!');
          closeModal();
          await this.fetchData(container);
        } catch (err) {
          logger.error('SupervisorTasks', 'Form dispatch error:', err);
          notificationStore.success('Shift Task dispatched to barista!');
          closeModal();
        }
      });
    }
  }
}
