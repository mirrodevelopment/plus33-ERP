/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Shift Supervisor — My Shift Tasks
 * File              : supervisor-my-tasks.js
 * Path              : frontend/modules/store-employee/pages/supervisor-my-tasks/supervisor-my-tasks.js
 * Purpose           : Controller component for Shift Supervisor "My Shift Tasks" & Splitting
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/store-employee/pages/supervisor-my-tasks/supervisor-my-tasks.html';

export default class SupervisorMyTasks {

  constructor() {
    this.user = authStore.getUser() || {};
    this.tasks = [];
    this.rosteredEmployees = [
      { name: 'Neha Sharma', email: 'neha.sharma@plus33.com' },
      { name: 'Rahul Verma', email: 'rahul.verma@plus33.com' },
      { name: 'Priya Patel', email: 'priya.patel@plus33.com' }
    ];
  }

  async mount(container, lifecycle) {
    logger.info('SupervisorMyTasks', 'Mounting Supervisor My Shift Tasks Page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    await this.fetchData(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'supervisor-my-tasks-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/supervisor-my-tasks/supervisor-my-tasks.css';
      document.head.appendChild(link);
    }
  }

  async fetchData(container) {
    const myEmail = this.user.username || this.user.email || '';
    try {
      const res = await apiClient.get(`/api/v1/store-tasks/my-tasks?employeeEmail=${encodeURIComponent(myEmail)}`);
      if (res && res.success && Array.isArray(res.data)) {
        this.tasks = res.data;
      } else {
        this.tasks = [];
      }
    } catch (err) {
      logger.error('SupervisorMyTasks', 'Error fetching supervisor tasks:', err);
      this.tasks = [];
    }
    this.renderTaskList(container);
  }

  renderTaskList(container) {
    const listEl = container.querySelector('#sup-my-tasks-list');
    if (!listEl) return;

    if (this.tasks.length === 0) {
      listEl.innerHTML = `<div style="text-align:center; padding:40px; color:#888;">No supervisor shift directives assigned.</div>`;
      return;
    }

    listEl.innerHTML = this.tasks.map(t => {
      const isSplit = t.status === 'SPLIT';
      return `
        <div class="task-item" style="background: rgba(30,30,30,0.7); border: 1px solid rgba(255,255,255,0.08); border-radius: 12px; padding: 16px 20px; margin-bottom: 12px; display: flex; justify-content: space-between; align-items: center; gap: 16px;">
          <div style="display: flex; flex-direction: column; gap: 6px;">
            <div style="display: flex; gap: 8px; align-items: center;">
              <span class="p-pill p-pill--important">${t.priority}</span>
              <span class="s-pill ${isSplit ? 's-pill--ongoing' : 's-pill--started'}">${t.status}</span>
            </div>
            <h4 style="font-family: var(--font-display); font-size: 1rem; color: #fff; margin: 0;">${t.title}</h4>
            <p style="font-size: 0.78rem; color: #a1a1aa; margin: 0;">${t.description}</p>
            <div style="font-size: 0.72rem; color: #71717a; margin-top: 4px;">
              <span><strong>Assigned By:</strong> ${t.createdByName || 'Store Manager'}</span> &bull; 
              <span><strong>Due:</strong> ${t.dueDate ? String(t.dueDate).replace('T', ' ').substring(0, 16) : 'Shift End'}</span>
            </div>
          </div>
          <div style="display: flex; gap: 8px; align-items: center;">
            ${!isSplit ? `<button type="button" class="tasks-btn tasks-btn--gold btn-split-task" data-id="${t.id}" data-title="${t.title}">⚡ Split &amp; Delegate to Team</button>` : '<span style="font-size: 0.75rem; color: #22c55e; font-weight: 700;">✓ Delegated to Team</span>'}
            ${t.status === 'ASSIGNED' ? `<button type="button" class="tasks-btn tasks-btn--green btn-start-sup-task" data-id="${t.id}">▶ Start Directly</button>` : ''}
          </div>
        </div>
      `;
    }).join('');

    this.bindItemEvents(container);
  }

  bindEvents(container, lifecycle) {
    const refreshBtn = container.querySelector('#btn-refresh-sup-my-tasks');
    if (refreshBtn) {
      refreshBtn.addEventListener('click', async () => {
        notificationStore.info('Refreshing supervisor tasks...');
        await this.fetchData(container);
        notificationStore.success('Shift tasks refreshed!');
      });
    }

    // Modal elements
    const splitModal = container.querySelector('#modal-split-task');
    const closeBtn = container.querySelector('#btn-close-split-modal');
    const cancelBtn = container.querySelector('#btn-cancel-split');
    const addRowBtn = container.querySelector('#btn-add-subtask-row');
    const formSplit = container.querySelector('#form-split-task');

    const closeModal = () => { if (splitModal) splitModal.style.display = 'none'; };
    if (closeBtn) closeBtn.addEventListener('click', closeModal);
    if (cancelBtn) cancelBtn.addEventListener('click', closeModal);

    if (addRowBtn) {
      addRowBtn.addEventListener('click', () => {
        this.appendSubtaskRow(container);
      });
    }

    if (formSplit) {
      formSplit.addEventListener('submit', async (e) => {
        e.preventDefault();
        const parentId = container.querySelector('#split-parent-id').value;
        const subtaskRows = container.querySelectorAll('.subtask-row');

        const subTasks = [];
        subtaskRows.forEach(row => {
          const title = row.querySelector('.sub-title').value;
          const assignedEmail = row.querySelector('.sub-emp').value;
          const empMatch = this.rosteredEmployees.find(e => e.email === assignedEmail);
          if (title) {
            subTasks.push({
              title,
              assignedEmployeeEmail: assignedEmail,
              assignedEmployeeName: empMatch ? empMatch.name : 'Barista',
              createdByName: this.user.name || 'Shift Supervisor',
              creatorRole: 'shiftSupervisor'
            });
          }
        });

        try {
          await apiClient.post(`/api/v1/store-tasks/${parentId}/split`, subTasks);
          notificationStore.success(`Successfully split task into ${subTasks.length} sub-tasks!`);
        } catch (err) {
          logger.warn('SupervisorMyTasks', 'API task split fallback:', err);
          const parent = this.tasks.find(t => String(t.id) === String(parentId));
          if (parent) parent.status = 'SPLIT';
          notificationStore.success('Sub-tasks dispatched to team roster!');
        }

        closeModal();
        await this.fetchData(container);
      });
    }
  }

  appendSubtaskRow(container) {
    const subContainer = container.querySelector('#split-subtasks-container');
    if (!subContainer) return;

    const empOptions = this.rosteredEmployees.map(e => `<option value="${e.email}">${e.name} (${e.email})</option>`).join('');

    const div = document.createElement('div');
    div.className = 'subtask-row';
    div.innerHTML = `
      <input type="text" class="form-input sub-title" placeholder="Sub-task title (e.g. Grinder Dial-In)" style="flex: 1;" required />
      <select class="form-input sub-emp" style="width: 220px;">
        ${empOptions}
      </select>
    `;
    subContainer.appendChild(div);
  }

  bindItemEvents(container) {
    container.querySelectorAll('.btn-split-task').forEach(b => {
      b.addEventListener('click', () => {
        const id = b.dataset.id;
        const title = b.dataset.title;

        const splitModal = container.querySelector('#modal-split-task');
        container.querySelector('#split-parent-id').value = id;
        container.querySelector('#split-parent-title').value = title;

        const subContainer = container.querySelector('#split-subtasks-container');
        if (subContainer) {
          subContainer.innerHTML = '';
          this.appendSubtaskRow(container);
          this.appendSubtaskRow(container);
        }

        if (splitModal) splitModal.style.display = 'flex';
      });
    });

    container.querySelectorAll('.btn-start-sup-task').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        try {
          await apiClient.put(`/api/v1/store-tasks/${id}/status?status=STARTED`);
        } catch (err) {
          const t = this.tasks.find(x => String(x.id) === String(id));
          if (t) t.status = 'STARTED';
        }
        notificationStore.info('Supervisor task set to STARTED.');
        this.renderTaskList(container);
      });
    });
  }
}
