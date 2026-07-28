/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : my-tasks.js
 * Path              : frontend/modules/store-employee/pages/tasks/my-tasks.js
 * Purpose           : Controller component for Barista personal "My Tasks" page
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { storage } from '../../../../core/storage.js';

const TEMPLATE_URL = 'modules/store-employee/pages/tasks/my-tasks.html';

export default class StoreEmployeeMyTasks {

  constructor() {
    this.user = authStore.getUser() || {};
    this.tasks = [];
    this.activeFilter = 'all';
    this.kpis = { todayTasks: 0, completedMonthTasks: 0, uncompletedMonthTasks: 0 };
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeMyTasks', 'Mounting Barista My Tasks Page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    await this.fetchData(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'store-employee-my-tasks-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/tasks/my-tasks.css';
      document.head.appendChild(link);
    }
  }

  async fetchData(container) {
    const myEmail = this.user.username || this.user.email || '';
    let apiTasks = [];

    try {
      const [kpiRes, taskRes] = await Promise.all([
        apiClient.get(`/api/v1/store-tasks/kpis?employeeEmail=${encodeURIComponent(myEmail)}`).catch(() => null),
        apiClient.get(`/api/v1/store-tasks/my-tasks?employeeEmail=${encodeURIComponent(myEmail)}`).catch(() => null)
      ]);

      if (kpiRes && kpiRes.success && kpiRes.data) {
        this.kpis = kpiRes.data;
      }
      if (taskRes && taskRes.success && Array.isArray(taskRes.data)) {
        apiTasks = taskRes.data;
      }
    } catch (err) {
      logger.error('StoreEmployeeMyTasks', 'Error fetching tasks from backend API:', err);
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
  }

  renderKpis(container) {
    const todayEl = container.querySelector('#kpi-today-tasks');
    const compEl = container.querySelector('#kpi-completed-month');
    const uncompEl = container.querySelector('#kpi-uncompleted-month');

    if (todayEl) todayEl.textContent = this.kpis.todayTasks || this.tasks.length;
    if (compEl) compEl.textContent = this.kpis.completedMonthTasks || this.tasks.filter(t => t.status === 'COMPLETED').length;
    if (uncompEl) uncompEl.textContent = this.kpis.uncompletedMonthTasks || this.tasks.filter(t => t.status !== 'COMPLETED').length;
  }

  renderTaskList(container) {
    const listEl = container.querySelector('#my-tasks-list');
    const preemptionBanner = container.querySelector('#preemption-alert-banner');
    if (!listEl) return;

    // Check if an immediate preemptive task is active
    const hasImmediate = this.tasks.some(t => (t.priority === 'IMMEDIATE' || t.priority === 'CRITICAL') && t.status !== 'COMPLETED');
    if (preemptionBanner) {
      preemptionBanner.style.display = hasImmediate ? 'flex' : 'none';
    }

    // Filter tasks
    let filtered = this.tasks;
    if (this.activeFilter !== 'all') {
      if (this.activeFilter === 'IN_PROGRESS') {
        filtered = this.tasks.filter(t => t.status === 'IN_PROGRESS' || t.status === 'STARTED');
      } else if (this.activeFilter === 'PENDING') {
        filtered = this.tasks.filter(t => t.status === 'PENDING' || t.status === 'ASSIGNED');
      } else {
        filtered = this.tasks.filter(t => t.status === this.activeFilter);
      }
    }

    // Update Counts
    const updateCount = (id, count) => {
      const el = container.querySelector(id);
      if (el) el.textContent = count;
    };
    updateCount('#count-all', this.tasks.length);
    updateCount('#count-pending', this.tasks.filter(t => t.status === 'PENDING' || t.status === 'ASSIGNED').length);
    updateCount('#count-started', this.tasks.filter(t => t.status === 'STARTED').length);
    updateCount('#count-ongoing', this.tasks.filter(t => t.status === 'IN_PROGRESS' || t.status === 'STARTED').length);
    updateCount('#count-completed', this.tasks.filter(t => t.status === 'COMPLETED').length);
    updateCount('#count-blocked', this.tasks.filter(t => t.status === 'BLOCKED').length);

    if (filtered.length === 0) {
      listEl.innerHTML = `<div style="text-align:center; padding:40px; color:#888;">No tasks found matching active filter.</div>`;
      return;
    }

    listEl.innerHTML = filtered.map(t => {
      const isPreemptive = (t.priority === 'IMMEDIATE' || t.priority === 'CRITICAL') && t.status !== 'COMPLETED';
      const isPaused = t.status === 'PAUSED_FOR_IMMEDIATE';

      const priorityBadge = this._getPriorityBadge(t.priority);
      const statusBadge = this._getStatusBadge(t.status);
      const formattedDue = t.dueDate ? String(t.dueDate).replace('T', ' ').substring(0, 16) : 'End of Shift';

      return `
        <div class="task-item ${isPreemptive ? 'task-item--preemptive' : ''} ${isPaused ? 'task-item--paused' : ''}">
          <div class="task-item__left">
            <div class="task-item__badges">
              ${priorityBadge}
              ${statusBadge}
              ${t.extensionStatus === 'REQUESTED' ? '<span class="s-pill s-pill--paused">⏳ EXTENSION REQUESTED</span>' : ''}
              ${t.extensionStatus === 'APPROVED' ? '<span class="s-pill s-pill--completed">✓ EXTENDED</span>' : ''}
            </div>
            <h4 class="task-item__title">${t.title}</h4>
            <p class="task-item__desc">${t.description || ''}</p>
            <div class="task-item__meta">
              <span><strong>Category:</strong> ${t.category}</span> &bull; 
              <span><strong>Due:</strong> ${formattedDue}</span> &bull; 
              <span><strong>Assigned To:</strong> ${t.assignedEmployeeName || 'Store Team'}</span>
            </div>
          </div>

          <div class="task-item__actions">
            ${t.status === 'ASSIGNED' || t.status === 'PENDING' ? `
              <button type="button" class="tasks-btn tasks-btn--green btn-start-task" data-id="${t.id}">Start Task</button>
            ` : ''}

            ${t.status === 'STARTED' || t.status === 'IN_PROGRESS' ? `
              <button type="button" class="tasks-btn tasks-btn--gold btn-complete-task" data-id="${t.id}">Complete Task</button>
            ` : ''}

            ${t.status === 'COMPLETED' ? `
              <span style="color: #10b981; font-weight: 700; font-size: 0.85rem;">✓ Verified Complete</span>
            ` : ''}
          </div>
        </div>
      `;
    }).join('');

    // Bind action buttons
    listEl.querySelectorAll('.btn-start-task').forEach(b => {
      b.addEventListener('click', () => this.updateStatus(container, b.dataset.id, 'STARTED'));
    });
    listEl.querySelectorAll('.btn-complete-task').forEach(b => {
      b.addEventListener('click', () => this.updateStatus(container, b.dataset.id, 'COMPLETED'));
    });
  }

  async updateStatus(container, taskId, status) {
    const myEmail = this.user.username || this.user.email || '';
    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/status?status=${status}&userEmail=${encodeURIComponent(myEmail)}`);
    } catch (err) {
      logger.warn('MyTasks', 'API status update warning:', err);
    }

    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const task = localTasks.find(t => String(t.id) === String(taskId));
    if (task) {
      task.status = status;
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memoryTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memoryTask) {
      memoryTask.status = status;
    }

    notificationStore.success(`Task status updated to ${status}!`);
    await this.fetchData(container);
  }

  _getPriorityBadge(priority) {
    switch ((priority || '').toUpperCase()) {
      case 'IMMEDIATE':
      case 'CRITICAL':
        return '<span class="p-pill p-pill--immediate">🔴 IMMEDIATE</span>';
      case 'IMPORTANT':
      case 'HIGH':
        return '<span class="p-pill p-pill--important">🟠 IMPORTANT</span>';
      case 'ROUTINE':
      case 'LOW':
        return '<span class="p-pill p-pill--routine">🟢 ROUTINE</span>';
      default:
        return '<span class="p-pill p-pill--common">🔵 COMMON</span>';
    }
  }

  _getStatusBadge(status) {
    switch ((status || '').toUpperCase()) {
      case 'ASSIGNED':
      case 'PENDING':
        return '<span class="s-pill s-pill--pending">ASSIGNED</span>';
      case 'STARTED':
      case 'IN_PROGRESS':
        return '<span class="s-pill s-pill--started">IN PROGRESS</span>';
      case 'COMPLETED':
        return '<span class="s-pill s-pill--completed">COMPLETED</span>';
      case 'PAUSED_FOR_IMMEDIATE':
        return '<span class="s-pill s-pill--paused">PAUSED (IMMEDIATE TASK ACTIVE)</span>';
      default:
        return `<span class="s-pill">${status}</span>`;
    }
  }

  bindEvents(container, lifecycle) {
    const refreshBtn = container.querySelector('#btn-refresh-my-tasks');
    if (refreshBtn) {
      refreshBtn.addEventListener('click', async () => {
        notificationStore.info('Refreshing My Tasks workspace...');
        await this.fetchData(container);
        notificationStore.success('Tasks updated!');
      });
    }

    const tabs = container.querySelectorAll('.tab-pill');
    tabs.forEach(t => {
      t.addEventListener('click', () => {
        tabs.forEach(btn => btn.classList.remove('active'));
        t.classList.add('active');
        this.activeFilter = t.dataset.status;
        this.renderTaskList(container);
      });
    });
  }
}
