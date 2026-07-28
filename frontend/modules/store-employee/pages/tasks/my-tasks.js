/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee — My Tasks Workspace
 * File              : my-tasks.js
 * Path              : frontend/modules/store-employee/pages/tasks/my-tasks.js
 * Purpose           : Barista task execution controller with Deadline Extension Requests (Extra Time), Employee Task Rejection, SOP Policy Modal, Action Options, Proof submission, & Time Countdown
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { storage } from '../../../../core/storage.js';
import { openExportModal } from '../../../../core/exportUtils.js';

const TEMPLATE_URL = 'modules/store-employee/pages/tasks/my-tasks.html';

export default class MyTasks {

  constructor() {
    this.user = authStore.getUser() || {};
    this.tasks = [];
    this.currentFilter = 'all';
    this.activePreemption = false;
  }

  async mount(container, lifecycle) {
    logger.info('MyTasks', 'Mounting Barista My Tasks Workspace Page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    await this.fetchData(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'my-tasks-css';
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
      const res = await apiClient.get(`/api/v1/store-tasks/my-tasks?employeeEmail=${encodeURIComponent(myEmail)}`).catch(() => null);
      if (res && res.success && Array.isArray(res.data)) {
        apiTasks = res.data;
      }
    } catch (err) {
      logger.error('MyTasks', 'Error fetching barista tasks:', err);
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

    this.checkPreemptionBanner(container);
    this.updateKpiCounters(container);
    this.renderTaskList(container);
  }

  checkPreemptionBanner(container) {
    const banner = container.querySelector('#preemption-alert-banner');
    if (!banner) return;

    this.activePreemption = this.tasks.some(t => 
      (t.priority === 'IMMEDIATE' || t.priority === 'CRITICAL') && 
      (t.status === 'ASSIGNED' || t.status === 'STARTED' || t.status === 'PENDING')
    );

    banner.style.display = this.activePreemption ? 'flex' : 'none';
  }

  updateKpiCounters(container) {
    const todayCountEl = container.querySelector('#kpi-today-tasks');
    const completedMonthEl = container.querySelector('#kpi-completed-month');
    const uncompletedMonthEl = container.querySelector('#kpi-uncompleted-month');

    const countAllEl = container.querySelector('#count-all');
    const countPendingEl = container.querySelector('#count-pending');
    const countStartedEl = container.querySelector('#count-started');
    const countOngoingEl = container.querySelector('#count-ongoing');
    const countCompletedEl = container.querySelector('#count-completed');
    const countBlockedEl = container.querySelector('#count-blocked');

    const pending = this.tasks.filter(t => t.status === 'ASSIGNED' || t.status === 'PENDING').length;
    const started = this.tasks.filter(t => t.status === 'STARTED').length;
    const ongoing = this.tasks.filter(t => t.status === 'IN_PROGRESS' || t.status === 'ONGOING' || t.status === 'SUBMITTED_FOR_REVIEW').length;
    const completed = this.tasks.filter(t => t.status === 'COMPLETED').length;
    const blocked = this.tasks.filter(t => t.status === 'BLOCKED' || t.status === 'REJECTED').length;

    if (todayCountEl) todayCountEl.textContent = this.tasks.length;
    if (completedMonthEl) completedMonthEl.textContent = completed;
    if (uncompletedMonthEl) uncompletedMonthEl.textContent = pending + started + ongoing + blocked;

    if (countAllEl) countAllEl.textContent = this.tasks.length;
    if (countPendingEl) countPendingEl.textContent = pending;
    if (countStartedEl) countStartedEl.textContent = started;
    if (countOngoingEl) countOngoingEl.textContent = ongoing;
    if (countCompletedEl) countCompletedEl.textContent = completed;
    if (countBlockedEl) countBlockedEl.textContent = blocked;
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
    const listEl = container.querySelector('#my-tasks-list');
    if (!listEl) return;

    let filtered = this.tasks;
    if (this.currentFilter === 'PENDING') {
      filtered = this.tasks.filter(t => t.status === 'PENDING' || t.status === 'ASSIGNED');
    } else if (this.currentFilter === 'STARTED') {
      filtered = this.tasks.filter(t => t.status === 'STARTED');
    } else if (this.currentFilter === 'ONGOING') {
      filtered = this.tasks.filter(t => t.status === 'IN_PROGRESS' || t.status === 'ONGOING' || t.status === 'SUBMITTED_FOR_REVIEW');
    } else if (this.currentFilter === 'COMPLETED') {
      filtered = this.tasks.filter(t => t.status === 'COMPLETED');
    } else if (this.currentFilter === 'BLOCKED') {
      filtered = this.tasks.filter(t => t.status === 'BLOCKED' || t.status === 'REJECTED');
    }

    if (filtered.length === 0) {
      listEl.innerHTML = `<div style="text-align:center; padding:45px; color:#888;">No tasks found for status "${this.currentFilter}".</div>`;
      return;
    }

    listEl.innerHTML = filtered.map(t => {
      let displayStatus = 'Pending';
      let pillClass = 's-pill--pending';

      if (t.status === 'STARTED') {
        displayStatus = 'Started';
        pillClass = 's-pill--started';
      } else if (t.status === 'IN_PROGRESS' || t.status === 'ONGOING') {
        displayStatus = 'Ongoing';
        pillClass = 's-pill--ongoing';
      } else if (t.status === 'SUBMITTED_FOR_REVIEW') {
        displayStatus = 'Ongoing (Submitted)';
        pillClass = 's-pill--ongoing';
      } else if (t.status === 'COMPLETED') {
        displayStatus = 'Completed';
        pillClass = 's-pill--completed';
      } else if (t.status === 'BLOCKED') {
        displayStatus = 'Blocked';
        pillClass = 's-pill--pending';
      } else if (t.status === 'REJECTED') {
        displayStatus = 'Rejected';
        pillClass = 's-pill--pending';
      }

      const isCompleted = t.status === 'COMPLETED';
      const isReviewPending = t.status === 'SUBMITTED_FOR_REVIEW';
      const isStarted = t.status === 'STARTED' || t.status === 'IN_PROGRESS' || t.status === 'ONGOING';
      const isPending = t.status === 'ASSIGNED' || t.status === 'PENDING';
      const isBlocked = t.status === 'BLOCKED';
      const isRejected = t.status === 'REJECTED';
      const formattedDue = t.dueDate ? String(t.dueDate).replace('T', ' ').substring(0, 16) : 'Shift End';
      const timeRemainingStr = this.formatTimeRemaining(t.dueDate);
      const isOverdue = timeRemainingStr.includes('Overdue');

      const isExtRequested = t.extensionStatus === 'REQUESTED';
      const isExtApproved = t.extensionStatus === 'APPROVED';

      const report = t.completionReport || {};
      const hasReport = !!report.notes || !!report.fileName;

      return `
        <div class="task-item" style="background: rgba(20,20,20,0.6); border: 1px solid rgba(255,255,255,0.08); border-radius: 12px; padding: 18px 22px; margin-bottom: 14px; display: flex; flex-direction: column; gap: 10px;">
          <div style="display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; width: 100%;">
            <div style="display: flex; flex-direction: column; gap: 6px; flex: 1;">
              <div style="display: flex; gap: 8px; align-items: center; flex-wrap: wrap;">
                <span class="p-pill p-pill--important">${t.priority}</span>
                <span class="s-pill ${pillClass}">${displayStatus}</span>
                <span style="font-size: 0.65rem; background: rgba(201,164,106,0.15); color: #c9a46a; padding: 2px 8px; border-radius: 4px;">${t.category || 'GENERAL'}</span>
                
                <!-- Live Countdown Time Remaining Badge -->
                <span style="font-size: 0.72rem; font-weight: 700; padding: 3px 10px; border-radius: 6px; background: ${isCompleted ? 'rgba(16,185,129,0.15)' : isOverdue ? 'rgba(239,68,68,0.2)' : 'rgba(245,158,11,0.18)'}; color: ${isCompleted ? '#34d399' : isOverdue ? '#f87171' : '#fbbf24'}; border: 1px solid ${isCompleted ? 'rgba(16,185,129,0.3)' : isOverdue ? 'rgba(239,68,68,0.4)' : 'rgba(245,158,11,0.35)'};">
                  ${isCompleted ? '✓ Done' : timeRemainingStr}
                </span>

                ${isExtRequested ? `<span style="font-size: 0.68rem; background: rgba(245,158,11,0.2); color: #fbbf24; border: 1px solid rgba(245,158,11,0.4); padding: 2px 8px; border-radius: 4px; font-weight: 700;">⏳ Extra Time Requested</span>` : ''}
                ${isExtApproved ? `<span style="font-size: 0.68rem; background: rgba(16,185,129,0.2); color: #34d399; border: 1px solid rgba(16,185,129,0.4); padding: 2px 8px; border-radius: 4px; font-weight: 700;">✓ Extra Time Approved</span>` : ''}
              </div>
              <h4 style="font-family: var(--font-display); font-size: 1.05rem; font-weight: 700; color: #fff; margin: 4px 0 2px;">${t.title}</h4>
              <p style="font-size: 0.82rem; color: #a1a1aa; margin: 0; line-height: 1.4;">${t.description || ''}</p>
              ${t.blockReason ? `<div style="font-size: 0.78rem; color: #f87171; background: rgba(239,68,68,0.1); border: 1px solid rgba(239,68,68,0.25); padding: 4px 8px; border-radius: 6px; margin-top: 4px;">🚫 <strong>Block Reason:</strong> ${t.blockReason}</div>` : ''}
              ${t.rejectReason ? `<div style="font-size: 0.78rem; color: #ef4444; background: rgba(239,68,68,0.12); border: 1px solid rgba(239,68,68,0.3); padding: 4px 8px; border-radius: 6px; margin-top: 4px;">❌ <strong>Employee Rejection Reason:</strong> ${t.rejectReason}</div>` : ''}
              ${t.supervisorRejectReason ? `<div style="font-size: 0.78rem; color: #fbbf24; background: rgba(245,158,11,0.15); border: 1px solid rgba(245,158,11,0.35); padding: 6px 10px; border-radius: 6px; margin-top: 4px;">⚠️ <strong>Revision Requested by Supervisor:</strong> ${t.supervisorRejectReason}</div>` : ''}
              <div style="font-size: 0.75rem; color: #71717a; margin-top: 4px; display: flex; gap: 12px; flex-wrap: wrap;">
                <span><strong>Assigned By:</strong> ${t.createdByName || 'Store Manager'}</span> &bull; 
                <span><strong>Due:</strong> ${formattedDue}</span>
              </div>
            </div>

            <!-- Action Options rendered on task card -->
            <div style="display: flex; gap: 10px; align-items: center; flex-shrink: 0; margin-top: 4px; flex-wrap: wrap;">
              ${!isCompleted ? `
                <button type="button" class="tasks-btn tasks-btn--glass btn-open-ext-modal" data-id="${t.id}" data-title="${t.title.replace(/"/g, '&quot;')}" style="color: #fbbf24; border-color: rgba(245,158,11,0.4);">⏳ Request Extra Time</button>
              ` : ''}

              ${isPending ? `
                <button type="button" class="tasks-btn tasks-btn--green btn-start-task" data-id="${t.id}">▶ Start Task</button>
                <button type="button" class="tasks-btn tasks-btn--red btn-open-reject-modal" data-id="${t.id}" data-title="${t.title.replace(/"/g, '&quot;')}">❌ Reject Task</button>
                <button type="button" class="tasks-btn tasks-btn--glass btn-open-block-modal" data-id="${t.id}" data-title="${t.title.replace(/"/g, '&quot;')}" style="color: #f87171; border-color: rgba(239,68,68,0.4);">🚫 Mark Blocked</button>
              ` : ''}

              ${isStarted ? `
                <button type="button" class="tasks-btn tasks-btn--gold btn-open-report-modal" data-id="${t.id}" data-title="${t.title.replace(/"/g, '&quot;')}">📋 Submit Completion Report &amp; Proof</button>
                <button type="button" class="tasks-btn tasks-btn--glass btn-open-block-modal" data-id="${t.id}" data-title="${t.title.replace(/"/g, '&quot;')}" style="color: #f87171; border-color: rgba(239,68,68,0.4);">🚫 Mark Blocked</button>
              ` : ''}

              ${isBlocked ? `
                <button type="button" class="tasks-btn tasks-btn--green btn-start-task" data-id="${t.id}">▶ Resume Task</button>
                <span style="font-size: 0.78rem; color: #f87171; background: rgba(239,68,68,0.1); border: 1px solid rgba(239,68,68,0.25); padding: 6px 12px; border-radius: 8px; font-weight: 700;">🚫 Blocked</span>
              ` : ''}

              ${isRejected ? `
                <span style="font-size: 0.78rem; color: #ef4444; background: rgba(239,68,68,0.15); border: 1px solid rgba(239,68,68,0.3); padding: 6px 12px; border-radius: 8px; font-weight: 700;">❌ Rejected by Employee</span>
              ` : ''}

              ${isReviewPending ? `
                <div style="display: flex; gap: 8px; align-items: center;">
                  <span style="font-size: 0.78rem; color: #f59e0b; background: rgba(245,158,11,0.1); border: 1px solid rgba(245,158,11,0.25); padding: 6px 12px; border-radius: 8px; font-weight: 700;">⏳ Ongoing (Awaiting Approval)</span>
                  ${hasReport ? `<button type="button" class="tasks-btn tasks-btn--glass btn-view-report" data-id="${t.id}">📄 View Submitted Report</button>` : ''}
                </div>
              ` : ''}

              ${isCompleted ? `
                <div style="display: flex; gap: 8px; align-items: center;">
                  <span style="font-size: 0.78rem; color: #10b981; background: rgba(16,185,129,0.1); border: 1px solid rgba(16,185,129,0.25); padding: 6px 12px; border-radius: 8px; font-weight: 700;">✓ Completed</span>
                  ${hasReport ? `<button type="button" class="tasks-btn tasks-btn--glass btn-view-report" data-id="${t.id}">📄 View Approved Report</button>` : ''}
                </div>
              ` : ''}
            </div>
          </div>
        </div>
      `;
    }).join('');

    this.bindItemEvents(container);
  }

  bindEvents(container, lifecycle) {
    // Policy Modal Handlers
    const policyCard = container.querySelector('#btn-open-task-policy-modal');
    const policyModal = container.querySelector('#modal-task-policy-guidelines');
    const closePolicyBtn = container.querySelector('#btn-close-policy-modal');
    const cancelPolicyBtn = container.querySelector('#btn-cancel-policy-modal');

    const openPolicyModal = () => { if (policyModal) policyModal.style.display = 'flex'; };
    const closePolicyModal = () => { if (policyModal) policyModal.style.display = 'none'; };

    if (policyCard) policyCard.addEventListener('click', openPolicyModal);
    if (closePolicyBtn) closePolicyBtn.addEventListener('click', closePolicyModal);
    if (cancelPolicyBtn) cancelPolicyBtn.addEventListener('click', closePolicyModal);

    const pdfBtn = container.querySelector('#btn-export-my-pdf');
    if (pdfBtn) {
      pdfBtn.addEventListener('click', () => {
        openExportModal(this.tasks, 'PDF', 'Barista Personal Shift Task Status Report', 'barista-shift-task-status-report.png');
      });
    }

    const imgBtn = container.querySelector('#btn-export-my-img');
    if (imgBtn) {
      imgBtn.addEventListener('click', () => {
        openExportModal(this.tasks, 'IMAGE', 'Barista Personal Shift Task Status Report', 'barista-shift-task-status-report.png');
      });
    }

    const purgeBtn = container.querySelector('#btn-purge-my-tasks-data');
    if (purgeBtn) {
      purgeBtn.addEventListener('click', async () => {
        if (!confirm('⚠️ Are you sure you want to PURGE ALL tasks? This will clear task records across the entire store database.')) {
          return;
        }

        storage.set('plus33-custom-store-tasks', []);

        try {
          await apiClient.delete('/api/v1/store-tasks/purge-all');
        } catch (e) {
          logger.warn('MyTasks', 'API purge all tasks error:', e);
        }

        this.tasks = [];
        this.updateKpiCounters(container);
        this.renderTaskList(container);
        notificationStore.success('All store task records purged cleanly across Database and Local Storage!');
      });
    }

    const refreshBtn = container.querySelector('#btn-refresh-my-tasks');
    if (refreshBtn) {
      refreshBtn.addEventListener('click', async () => {
        notificationStore.info('Refreshing task workspace...');
        await this.fetchData(container);
        notificationStore.success('Tasks updated!');
      });
    }

    // Filter tab buttons
    container.querySelectorAll('.tab-pill').forEach(btn => {
      btn.addEventListener('click', () => {
        container.querySelectorAll('.tab-pill').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        this.currentFilter = btn.dataset.status;
        this.renderTaskList(container);
      });
    });

    // Deadline Extension Request Modal Handlers
    const extModal = container.querySelector('#modal-request-extension');
    const closeExtBtn = container.querySelector('#btn-close-ext-modal');
    const cancelExtBtn = container.querySelector('#btn-cancel-ext');
    const formExt = container.querySelector('#form-request-extension');

    const closeExtModal = () => { if (extModal) extModal.style.display = 'none'; };
    if (closeExtBtn) closeExtBtn.addEventListener('click', closeExtModal);
    if (cancelExtBtn) cancelExtBtn.addEventListener('click', closeExtModal);

    if (formExt) {
      formExt.addEventListener('submit', async (e) => {
        e.preventDefault();
        const taskId = container.querySelector('#ext-task-id').value;
        const requestedDate = container.querySelector('#ext-requested-date').value;
        const reason = container.querySelector('#ext-reason').value;

        await this.requestTaskExtension(container, taskId, requestedDate, reason);
        closeExtModal();
      });
    }

    // Task Rejection Modal Handlers
    const rejectModal = container.querySelector('#modal-reject-task');
    const closeRejectBtn = container.querySelector('#btn-close-reject-modal');
    const cancelRejectBtn = container.querySelector('#btn-cancel-reject');
    const formReject = container.querySelector('#form-reject-task');

    const closeRejectModal = () => { if (rejectModal) rejectModal.style.display = 'none'; };
    if (closeRejectBtn) closeRejectBtn.addEventListener('click', closeRejectModal);
    if (cancelRejectBtn) cancelRejectBtn.addEventListener('click', closeRejectModal);

    if (formReject) {
      formReject.addEventListener('submit', async (e) => {
        e.preventDefault();
        const taskId = container.querySelector('#rej-task-id').value;
        const rejectReason = container.querySelector('#rej-reason').value;

        await this.markTaskRejected(container, taskId, rejectReason);
        closeRejectModal();
      });
    }

    // Mark Blocked Modal elements
    const blockModal = container.querySelector('#modal-block-task');
    const closeBlockBtn = container.querySelector('#btn-close-block-modal');
    const cancelBlockBtn = container.querySelector('#btn-cancel-block');
    const formBlock = container.querySelector('#form-block-task');

    const closeBlockModal = () => { if (blockModal) blockModal.style.display = 'none'; };
    if (closeBlockBtn) closeBlockBtn.addEventListener('click', closeBlockModal);
    if (cancelBlockBtn) cancelBlockBtn.addEventListener('click', closeBlockModal);

    if (formBlock) {
      formBlock.addEventListener('submit', async (e) => {
        e.preventDefault();
        const taskId = container.querySelector('#block-task-id').value;
        const blockReason = container.querySelector('#block-reason').value;

        await this.markTaskBlocked(container, taskId, blockReason);
        closeBlockModal();
      });
    }

    // Work Report Modal elements
    const reportModal = container.querySelector('#modal-work-report');
    const closeReportBtn = container.querySelector('#btn-close-report-modal');
    const cancelReportBtn = container.querySelector('#btn-cancel-report');
    const formReport = container.querySelector('#form-work-report');
    const fileInput = container.querySelector('#rep-file-input');
    const filePreview = container.querySelector('#rep-file-preview');
    const hiddenFileData = container.querySelector('#rep-attachment-data');

    const closeReportModal = () => { if (reportModal) reportModal.style.display = 'none'; };
    if (closeReportBtn) closeReportBtn.addEventListener('click', closeReportModal);
    if (cancelReportBtn) cancelReportBtn.addEventListener('click', closeReportModal);

    if (fileInput) {
      fileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
          const reader = new FileReader();
          reader.onload = (evt) => {
            const dataUrl = evt.target.result;
            const fileMeta = {
              fileName: file.name,
              fileSize: (file.size / 1024).toFixed(1) + ' KB',
              fileType: file.type,
              dataUrl
            };
            if (hiddenFileData) hiddenFileData.value = JSON.stringify(fileMeta);
            if (filePreview) {
              filePreview.innerHTML = `✓ Attached File: <strong>${file.name}</strong> (${fileMeta.fileSize})`;
              filePreview.style.display = 'block';
            }
          };
          reader.readAsDataURL(file);
        }
      });
    }

    if (formReport) {
      formReport.addEventListener('submit', async (e) => {
        e.preventDefault();
        const taskId = container.querySelector('#rep-task-id').value;
        const notes = container.querySelector('#rep-notes').value;
        const metric = container.querySelector('#rep-metric').value;
        
        let attachmentMeta = null;
        if (hiddenFileData && hiddenFileData.value) {
          try { attachmentMeta = JSON.parse(hiddenFileData.value); } catch(err) {}
        }

        const reportObj = {
          notes,
          metric: metric || 'N/A',
          fileName: attachmentMeta ? attachmentMeta.fileName : null,
          fileSize: attachmentMeta ? attachmentMeta.fileSize : null,
          fileType: attachmentMeta ? attachmentMeta.fileType : null,
          dataUrl: attachmentMeta ? attachmentMeta.dataUrl : null,
          submittedAt: new Date().toISOString(),
          submittedBy: this.user.name || this.user.username || 'Barista',
          approvalStatus: 'PENDING_APPROVAL'
        };

        await this.completeTaskWithReport(container, taskId, reportObj);
        closeReportModal();
      });
    }

    // View Report Modal elements
    const viewModal = container.querySelector('#modal-view-report');
    const closeViewBtn = container.querySelector('#btn-close-view-report-modal');
    const cancelViewBtn = container.querySelector('#btn-cancel-view-report');

    const closeViewModal = () => { if (viewModal) viewModal.style.display = 'none'; };
    if (closeViewBtn) closeViewBtn.addEventListener('click', closeViewModal);
    if (cancelViewBtn) cancelViewBtn.addEventListener('click', closeViewModal);
  }

  bindItemEvents(container) {
    container.querySelectorAll('.btn-start-task').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        await this.updateStatus(container, id, 'STARTED');
      });
    });

    container.querySelectorAll('.btn-open-ext-modal').forEach(b => {
      b.addEventListener('click', (e) => {
        e.preventDefault();
        const id = b.dataset.id;
        const title = b.dataset.title;

        const extModal = container.querySelector('#modal-request-extension');
        const taskIdInput = container.querySelector('#ext-task-id');
        const taskTitleInput = container.querySelector('#ext-task-title');
        const dateInput = container.querySelector('#ext-requested-date');
        const reasonInput = container.querySelector('#ext-reason');

        if (taskIdInput) taskIdInput.value = id;
        if (taskTitleInput) taskTitleInput.value = title;
        if (reasonInput) reasonInput.value = '';
        
        // Default requested date: now + 4 hours formatted for datetime-local
        if (dateInput) {
          const dt = new Date(Date.now() + 4 * 3600000);
          dt.setMinutes(dt.getMinutes() - dt.getTimezoneOffset());
          dateInput.value = dt.toISOString().slice(0, 16);
        }

        if (extModal) extModal.style.display = 'flex';
      });
    });

    container.querySelectorAll('.btn-open-reject-modal').forEach(b => {
      b.addEventListener('click', (e) => {
        e.preventDefault();
        const id = b.dataset.id;
        const title = b.dataset.title;

        const rejectModal = container.querySelector('#modal-reject-task');
        const taskIdInput = container.querySelector('#rej-task-id');
        const taskTitleInput = container.querySelector('#rej-task-title');
        const reasonInput = container.querySelector('#rej-reason');

        if (taskIdInput) taskIdInput.value = id;
        if (taskTitleInput) taskTitleInput.value = title;
        if (reasonInput) reasonInput.value = '';

        if (rejectModal) rejectModal.style.display = 'flex';
      });
    });

    container.querySelectorAll('.btn-open-block-modal').forEach(b => {
      b.addEventListener('click', (e) => {
        e.preventDefault();
        const id = b.dataset.id;
        const title = b.dataset.title;

        const blockModal = container.querySelector('#modal-block-task');
        const taskIdInput = container.querySelector('#block-task-id');
        const taskTitleInput = container.querySelector('#block-task-title');
        const reasonInput = container.querySelector('#block-reason');

        if (taskIdInput) taskIdInput.value = id;
        if (taskTitleInput) taskTitleInput.value = title;
        if (reasonInput) reasonInput.value = '';

        if (blockModal) blockModal.style.display = 'flex';
      });
    });

    container.querySelectorAll('.btn-open-report-modal').forEach(b => {
      b.addEventListener('click', (e) => {
        e.preventDefault();
        const id = b.dataset.id;
        const title = b.dataset.title;

        const reportModal = container.querySelector('#modal-work-report');
        const taskIdInput = container.querySelector('#rep-task-id');
        const taskTitleInput = container.querySelector('#rep-task-title');
        const notesInput = container.querySelector('#rep-notes');
        const fileInput = container.querySelector('#rep-file-input');
        const filePreview = container.querySelector('#rep-file-preview');
        const hiddenFileData = container.querySelector('#rep-attachment-data');

        if (taskIdInput) taskIdInput.value = id;
        if (taskTitleInput) taskTitleInput.value = title;
        if (notesInput) notesInput.value = '';
        if (fileInput) fileInput.value = '';
        if (hiddenFileData) hiddenFileData.value = '';
        if (filePreview) filePreview.style.display = 'none';

        if (reportModal) reportModal.style.display = 'flex';
      });
    });

    container.querySelectorAll('.btn-view-report').forEach(b => {
      b.addEventListener('click', () => {
        const id = b.dataset.id;
        const t = this.tasks.find(x => String(x.id) === String(id));
        if (!t || !t.completionReport) return;

        const rep = t.completionReport;
        const viewModal = container.querySelector('#modal-view-report');
        const contentEl = container.querySelector('#view-report-content');

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

        if (contentEl) {
          contentEl.innerHTML = `
            <div style="background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); padding: 14px; border-radius: 8px;">
              <strong style="color: #c9a46a; font-size: 0.82rem;">TASK DIRECTIVE:</strong>
              <h5 style="color: #fff; font-size: 0.95rem; margin: 4px 0 0;">${t.title}</h5>
            </div>

            <div style="background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); padding: 14px; border-radius: 8px;">
              <strong style="color: #34d399; font-size: 0.82rem;">COMPLETION SUMMARY / WORK NOTES:</strong>
              <p style="color: #e2e8f0; font-size: 0.88rem; margin: 6px 0 0; white-space: pre-line; line-height: 1.4;">${rep.notes || 'No detailed notes provided.'}</p>
            </div>

            <div style="display: flex; gap: 14px; flex-wrap: wrap;">
              <div style="flex: 1; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); padding: 10px 14px; border-radius: 8px;">
                <span style="font-size: 0.75rem; color: #a1a1aa;">AUDIT METRIC / READINGS</span>
                <div style="color: #fbbf24; font-weight: 700; font-size: 0.9rem; margin-top: 2px;">${rep.metric || 'N/A'}</div>
              </div>
              <div style="flex: 1; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); padding: 10px 14px; border-radius: 8px;">
                <span style="font-size: 0.75rem; color: #a1a1aa;">APPROVAL STATUS</span>
                <div style="color: ${t.status === 'COMPLETED' ? '#10b981' : '#f59e0b'}; font-weight: 700; font-size: 0.88rem; margin-top: 2px;">${t.status === 'COMPLETED' ? '✓ APPROVED & COMPLETED' : '⏳ PENDING SUPERVISOR VERIFICATION'}</div>
              </div>
            </div>

            ${imagePreviewHtml}
          `;
        }

        if (viewModal) viewModal.style.display = 'flex';
      });
    });
  }

  async requestTaskExtension(container, taskId, requestedDateStr, extensionReason) {
    const myEmail = this.user.username || this.user.email || '';
    const isoDueDate = new Date(requestedDateStr).toISOString();

    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const localTask = localTasks.find(t => String(t.id) === String(taskId));
    if (localTask) {
      localTask.extensionStatus = 'REQUESTED';
      localTask.requestedDueDate = isoDueDate;
      localTask.extensionReason = extensionReason;
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memTask) {
      memTask.extensionStatus = 'REQUESTED';
      memTask.requestedDueDate = isoDueDate;
      memTask.extensionReason = extensionReason;
    }

    try {
      await apiClient.post(`/api/v1/store-tasks/${taskId}/extension-request?userEmail=${encodeURIComponent(myEmail)}`, {
        requestedDueDate: isoDueDate,
        reason: extensionReason
      });
    } catch (err) {
      logger.warn('MyTasks', 'API extension request warning:', err);
    }

    notificationStore.success('Extra time extension request submitted to assigner!');
    await this.fetchData(container);
  }

  async markTaskRejected(container, taskId, rejectReason) {
    const myEmail = this.user.username || this.user.email || '';
    
    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const localTask = localTasks.find(t => String(t.id) === String(taskId));
    if (localTask) {
      localTask.status = 'REJECTED';
      localTask.rejectReason = rejectReason;
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memTask) {
      memTask.status = 'REJECTED';
      memTask.rejectReason = rejectReason;
    }

    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/status?status=REJECTED&userEmail=${encodeURIComponent(myEmail)}`);
    } catch (err) {
      logger.warn('MyTasks', 'API status update warning:', err);
    }

    notificationStore.info(`Task directive rejected with written reason: "${rejectReason}"`);
    await this.fetchData(container);
  }

  async markTaskBlocked(container, taskId, blockReason) {
    const myEmail = this.user.username || this.user.email || '';
    
    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const localTask = localTasks.find(t => String(t.id) === String(taskId));
    if (localTask) {
      localTask.status = 'BLOCKED';
      localTask.blockReason = blockReason;
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memTask) {
      memTask.status = 'BLOCKED';
      memTask.blockReason = blockReason;
    }

    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/status?status=BLOCKED&userEmail=${encodeURIComponent(myEmail)}`);
    } catch (err) {
      logger.warn('MyTasks', 'API status update warning:', err);
    }

    notificationStore.info(`Task status set to BLOCKED with reason: "${blockReason}"`);
    await this.fetchData(container);
  }

  async completeTaskWithReport(container, taskId, reportObj) {
    const myEmail = this.user.username || this.user.email || '';
    
    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const localTask = localTasks.find(t => String(t.id) === String(taskId));
    if (localTask) {
      localTask.status = 'SUBMITTED_FOR_REVIEW';
      localTask.completionReport = reportObj;
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memTask) {
      memTask.status = 'SUBMITTED_FOR_REVIEW';
      memTask.completionReport = reportObj;
    }

    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/status?status=SUBMITTED_FOR_REVIEW&userEmail=${encodeURIComponent(myEmail)}`);
    } catch (err) {
      logger.warn('MyTasks', 'API status update warning:', err);
    }

    notificationStore.success('Completion Report submitted! Sent to Shift Supervisor / Store Admin for verification & approval.');
    await this.fetchData(container);
  }

  async updateStatus(container, taskId, status) {
    const myEmail = this.user.username || this.user.email || '';
    
    const localTasks = storage.get('plus33-custom-store-tasks') || [];
    const task = localTasks.find(t => String(t.id) === String(taskId));
    if (task) {
      task.status = status;
      if (status !== 'BLOCKED') task.blockReason = null;
      if (status !== 'REJECTED') task.rejectReason = null;
      storage.set('plus33-custom-store-tasks', localTasks);
    }

    const memoryTask = this.tasks.find(t => String(t.id) === String(taskId));
    if (memoryTask) {
      memoryTask.status = status;
      if (status !== 'BLOCKED') memoryTask.blockReason = null;
      if (status !== 'REJECTED') memoryTask.rejectReason = null;
    }

    try {
      await apiClient.put(`/api/v1/store-tasks/${taskId}/status?status=${status}&userEmail=${encodeURIComponent(myEmail)}`);
    } catch (err) {
      logger.warn('MyTasks', 'API status update warning:', err);
    }

    notificationStore.success(`Task status updated to ${status}!`);
    await this.fetchData(container);
  }
}
