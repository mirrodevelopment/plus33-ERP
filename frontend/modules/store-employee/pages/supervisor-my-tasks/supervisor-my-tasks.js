/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Shift Supervisor — My Shift Tasks
 * File              : supervisor-my-tasks.js
 * Path              : frontend/modules/store-employee/pages/supervisor-my-tasks/supervisor-my-tasks.js
 * Purpose           : Controller component for Shift Supervisor "My Shift Tasks" & Report Verification
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { storage } from '../../../../core/storage.js';
import { openExportModal } from '../../../../core/exportUtils.js';

const TEMPLATE_URL = 'modules/store-employee/pages/supervisor-my-tasks/supervisor-my-tasks.html';

export default class SupervisorMyTasks {

  constructor() {
    this.user = authStore.getUser() || {};
    this.tasks = [];
    this.rosteredEmployees = [];
  }

  async mount(container, lifecycle) {
    logger.info('SupervisorMyTasks', 'Mounting Supervisor My Shift Tasks Page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    await this.fetchData(container);
    await this.loadEmployees(container);
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

  async loadEmployees(container) {
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
      logger.warn('SupervisorMyTasks', 'API error loading employees:', err);
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

    if (filteredEmployees && filteredEmployees.length > 0) {
      this.rosteredEmployees = filteredEmployees;
    }

    const shiftAssigneeSelect = container.querySelector('#shift-assignee');
    if (shiftAssigneeSelect) {
      let options = '<option value="all@plus33.com">👥 All Shift Baristas (Broadcast)</option>';
      if (this.rosteredEmployees.length > 0) {
        options += this.rosteredEmployees.map(emp => {
          const desig = emp.designation ? ` (${emp.designation})` : '';
          return `<option value="${emp.email}">${emp.name}${desig} - ${emp.email}</option>`;
        }).join('');
      }
      shiftAssigneeSelect.innerHTML = options;
    }
  }

  async fetchData(container) {
    const myEmail = this.user.username || this.user.email || '';
    let apiTasks = [];

    try {
      const res = await apiClient.get('/api/v1/store-tasks').catch(() => null);
      if (res && res.success && Array.isArray(res.data)) {
        apiTasks = res.data;
      }
    } catch (err) {
      logger.error('SupervisorMyTasks', 'Error fetching supervisor tasks:', err);
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

    this.renderTaskList(container);
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
    const listEl = container.querySelector('#sup-my-tasks-list');
    if (!listEl) return;

    const uniqueMap = new Map();
    this.tasks.forEach(t => {
      const key = `${(t.title || '').trim().toLowerCase()}_${t.createdAt || t.id}`;
      if (!uniqueMap.has(key)) {
        uniqueMap.set(key, t);
      }
    });

    const displayTasks = Array.from(uniqueMap.values());

    if (displayTasks.length === 0) {
      listEl.innerHTML = `<div style="text-align:center; padding:45px; color:#888;">No shift directives currently pending. Click '+ Assign Shift Directive' to dispatch tasks to your shift team.</div>`;
      return;
    }

    listEl.innerHTML = displayTasks.map(t => {
      const isSplit = t.status === 'SPLIT';
      const isCompleted = t.status === 'COMPLETED';
      const isReviewPending = t.status === 'SUBMITTED_FOR_REVIEW';
      const isAssigned = t.status === 'ASSIGNED' || t.status === 'PENDING';
      const isStarted = t.status === 'STARTED' || t.status === 'IN_PROGRESS';
      const timeRemainingStr = this.formatTimeRemaining(t.dueDate);
      const isOverdue = timeRemainingStr.includes('Overdue');

      const report = t.completionReport || {};
      const hasReport = !!report.notes || !!report.fileName;

      const subTasksForParent = this.tasks.filter(sub => String(sub.parentTaskId) === String(t.id));
      const subTasksHtml = subTasksForParent.length > 0 ? `
        <div style="margin-top: 10px; padding-top: 10px; border-top: 1px dashed rgba(255,255,255,0.12); font-size: 0.76rem;">
          <strong style="color: #c9a46a; margin-bottom: 6px; display: block;">Delegated Sub-Tasks (${subTasksForParent.length}):</strong>
          <div style="display: flex; flex-wrap: wrap; gap: 8px;">
            ${subTasksForParent.map(s => {
              const isSubReview = s.status === 'SUBMITTED_FOR_REVIEW';
              const isSubDone = s.status === 'COMPLETED';
              return `
                <span style="background: rgba(201,164,106,0.12); border: 1px solid rgba(201,164,106,0.25); color: #e2e8f0; padding: 4px 10px; border-radius: 6px; display: inline-flex; align-items: center; gap: 6px;">
                  📌 <strong>${s.title}</strong> → ${s.assignedEmployeeName || s.assignedEmployeeEmail} 
                  <span style="font-size:0.65rem; background:rgba(0,0,0,0.3); padding:1px 6px; border-radius:4px; color:${isSubDone ? '#10b981' : isSubReview ? '#f59e0b' : '#3b82f6'};">${isSubReview ? 'PENDING APPROVAL' : s.status}</span>
                  ${isSubReview ? `<button type="button" class="btn-review-subtask" data-id="${s.id}" style="background:#c9a46a; color:#000; border:none; padding:1px 6px; border-radius:4px; font-weight:700; cursor:pointer;">🔍 Review</button>` : ''}
                </span>
              `;
            }).join('')}
          </div>
        </div>
      ` : '';

      return `
        <div class="task-item" style="background: rgba(20,20,20,0.6); border: 1px solid rgba(255,255,255,0.08); border-radius: 12px; padding: 18px 22px; margin-bottom: 14px; display: flex; flex-direction: column; gap: 12px;">
          <div style="display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; width: 100%;">
            <div style="display: flex; flex-direction: column; gap: 6px; flex: 1;">
              <div style="display: flex; gap: 8px; align-items: center; flex-wrap: wrap;">
                <span class="p-pill p-pill--important">${t.priority}</span>
                <span class="s-pill ${isCompleted ? 's-pill--completed' : isReviewPending ? 's-pill--ongoing' : 's-pill--started'}">${isReviewPending ? 'SUBMITTED FOR REVIEW' : t.status}</span>
                <span style="font-size: 0.65rem; background: rgba(201,164,106,0.15); color: #c9a46a; padding: 2px 8px; border-radius: 4px; border: 1px solid rgba(201,164,106,0.2);">${t.delegationMode || 'DIRECT'}</span>
                
                <span style="font-size: 0.72rem; font-weight: 700; padding: 3px 10px; border-radius: 6px; background: ${isCompleted ? 'rgba(16,185,129,0.15)' : isOverdue ? 'rgba(239,68,68,0.2)' : 'rgba(245,158,11,0.18)'}; color: ${isCompleted ? '#34d399' : isOverdue ? '#f87171' : '#fbbf24'}; border: 1px solid ${isCompleted ? 'rgba(16,185,129,0.3)' : isOverdue ? 'rgba(239,68,68,0.4)' : 'rgba(245,158,11,0.35)'};">
                  ${isCompleted ? '✓ Done' : timeRemainingStr}
                </span>
              </div>
              <h4 style="font-family: var(--font-display); font-size: 1.05rem; font-weight: 700; color: #fff; margin: 4px 0 2px;">${t.title}</h4>
              <p style="font-size: 0.82rem; color: #a1a1aa; line-height: 1.4; margin: 0;">${t.description || ''}</p>
              ${t.rejectReason ? `<div style="font-size: 0.78rem; color: #ef4444; background: rgba(239,68,68,0.12); border: 1px solid rgba(239,68,68,0.3); padding: 6px 10px; border-radius: 6px; margin-top: 4px;">❌ <strong>Employee Rejection Reason:</strong> ${t.rejectReason}</div>` : ''}
              ${t.extensionStatus === 'REQUESTED' ? `<div style="font-size: 0.78rem; color: #fbbf24; background: rgba(245,158,11,0.15); border: 1px solid rgba(245,158,11,0.35); padding: 6px 10px; border-radius: 6px; margin-top: 4px;">⏳ <strong>Extra Time Requested:</strong> "${t.extensionReason || 'Need additional time'}" (New Target: ${t.requestedDueDate ? String(t.requestedDueDate).replace('T',' ').substring(0,16) : 'N/A'})</div>` : ''}
              <div style="font-size: 0.75rem; color: #71717a; margin-top: 4px; display: flex; gap: 12px; flex-wrap: wrap;">
                <span><strong>Assigned By:</strong> ${t.createdByName || 'Store Manager'}</span> &bull; 
                <span><strong>Assigned To:</strong> ${t.assignedEmployeeName || 'Shift Team'}</span> &bull; 
                <span><strong>Due:</strong> ${t.dueDate ? String(t.dueDate).replace('T', ' ').substring(0, 16) : 'Shift End'}</span>
              </div>
            </div>
            <div style="display: flex; gap: 10px; align-items: center; flex-shrink: 0; margin-top: 4px; flex-wrap: wrap;">
              ${t.extensionStatus === 'REQUESTED' ? `
                <button type="button" class="tasks-btn tasks-btn--gold btn-sup-approve-ext" data-id="${t.id}">✅ Approve Extra Time</button>
                <button type="button" class="tasks-btn tasks-btn--glass btn-sup-reject-ext" data-id="${t.id}" style="color: #f87171; border-color: rgba(239,68,68,0.4);">❌ Reject Extension</button>
              ` : ''}

              ${isReviewPending ? `<button type="button" class="tasks-btn tasks-btn--gold btn-review-report" data-id="${t.id}" style="box-shadow: 0 0 12px rgba(201,164,106,0.4);">🔍 Review Report &amp; Approve</button>` : ''}
              ${!isCompleted && !isReviewPending ? `<button type="button" class="tasks-btn tasks-btn--green btn-sup-approve-direct" data-id="${t.id}">✅ Approve &amp; Complete</button>` : ''}
              ${!isSplit && !isCompleted && !isReviewPending ? `<button type="button" class="tasks-btn tasks-btn--gold btn-split-task" data-id="${t.id}" data-title="${t.title.replace(/"/g, '&quot;')}">⚡ Split &amp; Delegate</button>` : ''}
              ${isSplit ? '<span style="font-size: 0.78rem; color: #34d399; background: rgba(52,211,153,0.1); border: 1px solid rgba(52,211,153,0.25); padding: 6px 12px; border-radius: 8px; font-weight: 700;">✓ Delegated to Team</span>' : ''}
              ${isCompleted ? `
                <div style="display: flex; gap: 8px; align-items: center;">
                  <span style="font-size: 0.78rem; color: #10b981; background: rgba(16,185,129,0.1); border: 1px solid rgba(16,185,129,0.25); padding: 6px 12px; border-radius: 8px; font-weight: 700;">✓ Approved &amp; Completed</span>
                  ${hasReport ? `<button type="button" class="tasks-btn tasks-btn--glass btn-view-sup-report" data-id="${t.id}">📄 View Report &amp; Proof</button>` : ''}
                </div>
              ` : ''}

              ${(t.status === 'ASSIGNED' || t.status === 'PENDING') ? `
                <button type="button" class="tasks-btn tasks-btn--red btn-delete-sup-task" data-id="${t.id}">Remove</button>
              ` : `
                <span style="font-size: 0.72rem; color: #71717a; background: rgba(255,255,255,0.04); padding: 4px 8px; border-radius: 6px; border: 1px solid rgba(255,255,255,0.08);" title="Accepted, active, or completed directives cannot be deleted for store governance compliance">🔒 Cannot Delete</span>
              `}
            </div>
          </div>
          ${subTasksHtml}
        </div>
      `;
    }).join('');

    this.bindItemEvents(container);
  }

  bindEvents(container, lifecycle) {
    const pdfBtn = container.querySelector('#btn-export-sup-pdf');
    if (pdfBtn) {
      pdfBtn.addEventListener('click', () => {
        openExportModal(this.tasks, 'PDF', 'Shift Supervisor Task Status Report', 'supervisor-shift-task-status-report.png');
      });
    }

    const imgBtn = container.querySelector('#btn-export-sup-img');
    if (imgBtn) {
      imgBtn.addEventListener('click', () => {
        openExportModal(this.tasks, 'IMAGE', 'Shift Supervisor Task Status Report', 'supervisor-shift-task-status-report.png');
      });
    }

    const refreshBtn = container.querySelector('#btn-refresh-sup-my-tasks');
    if (refreshBtn) {
      refreshBtn.addEventListener('click', async () => {
        notificationStore.info('Refreshing supervisor tasks...');
        await this.fetchData(container);
        notificationStore.success('Shift tasks refreshed!');
      });
    }

    // Modal elements for Split Task
    const splitModal = container.querySelector('#modal-split-task');
    const closeBtn = container.querySelector('#btn-close-split-modal');
    const cancelBtn = container.querySelector('#btn-cancel-split');
    const addRowBtn = container.querySelector('#btn-add-subtask-row');
    const formSplit = container.querySelector('#form-split-task');

    const closeModal = () => { if (splitModal) splitModal.style.display = 'none'; };
    if (closeBtn) closeBtn.addEventListener('click', closeModal);
    if (cancelBtn) cancelBtn.addEventListener('click', closeModal);

    const createModal = container.querySelector('#modal-sup-task');
    const openCreateBtn = container.querySelector('#btn-open-create-shift-task-modal');
    const closeCreateBtn = container.querySelector('#btn-close-sup-modal');
    const cancelCreateBtn = container.querySelector('#btn-cancel-sup-modal');
    const formCreate = container.querySelector('#form-sup-task');

    const openCreateModal = async () => {
      if (createModal) createModal.style.display = 'flex';
      await this.loadEmployees(container);
    };
    const closeCreateModal = () => { if (createModal) createModal.style.display = 'none'; };

    if (openCreateBtn) openCreateBtn.addEventListener('click', openCreateModal);
    if (closeCreateBtn) closeCreateBtn.addEventListener('click', closeCreateModal);
    if (cancelCreateBtn) cancelCreateBtn.addEventListener('click', closeCreateModal);

    // Supervisor Rejection Modal Handlers
    const supRejModal = container.querySelector('#modal-sup-reject-report');
    const closeSupRejBtn = container.querySelector('#btn-close-sup-reject-modal');
    const cancelSupRejBtn = container.querySelector('#btn-cancel-sup-reject');
    const formSupRej = container.querySelector('#form-sup-reject-report');

    const closeSupRejModal = () => { if (supRejModal) supRejModal.style.display = 'none'; };
    if (closeSupRejBtn) closeSupRejBtn.addEventListener('click', closeSupRejModal);
    if (cancelSupRejBtn) cancelSupRejBtn.addEventListener('click', closeSupRejModal);

    if (formSupRej) {
      formSupRej.addEventListener('submit', async (e) => {
        e.preventDefault();
        const taskId = container.querySelector('#sup-rej-task-id').value;
        const rejectReason = container.querySelector('#sup-rej-reason').value;

        await this.rejectTaskWithReason(container, taskId, rejectReason);
        closeSupRejModal();
      });
    }

    // Modal elements for Completion Report Submission
    const reportModal = container.querySelector('#modal-sup-work-report');
    const closeReportBtn = container.querySelector('#btn-close-sup-report-modal');
    const cancelReportBtn = container.querySelector('#btn-cancel-sup-report');
    const formReport = container.querySelector('#form-sup-work-report');
    const fileInput = container.querySelector('#sup-rep-file-input');
    const filePreview = container.querySelector('#sup-rep-file-preview');
    const hiddenFileData = container.querySelector('#sup-rep-attachment-data');

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
        const taskId = container.querySelector('#sup-rep-task-id').value;
        const notes = container.querySelector('#sup-rep-notes').value;
        const metric = container.querySelector('#sup-rep-metric').value;
        
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
          submittedBy: this.user.name || this.user.username || 'Shift Supervisor',
          approvalStatus: 'PENDING_APPROVAL'
        };

        await this.submitReportPendingApproval(container, taskId, reportObj);
        closeReportModal();
      });
    }

    // View Report Modal elements
    const viewModal = container.querySelector('#modal-sup-view-report');
    const closeViewBtn = container.querySelector('#btn-close-sup-view-report-modal');
    const cancelViewBtn = container.querySelector('#btn-cancel-sup-view-report');

    const closeViewModal = () => { if (viewModal) viewModal.style.display = 'none'; };
    if (closeViewBtn) closeViewBtn.addEventListener('click', closeViewModal);
    if (cancelViewBtn) cancelViewBtn.addEventListener('click', closeViewModal);

    // Preset selector in create modal
    const shiftPresetSelect = container.querySelector('#shift-preset-template');
    if (shiftPresetSelect) {
      shiftPresetSelect.addEventListener('change', () => {
        const val = shiftPresetSelect.value;
        const titleEl = container.querySelector('#shift-title');
        const descEl = container.querySelector('#shift-desc');
        const priorityEl = container.querySelector('#shift-priority');

        if (val === 'INV_COUNT') {
          if (titleEl) titleEl.value = 'Bar Stock & Syrup Replenishment';
          if (descEl) descEl.value = '1. Restock espresso beans, vanilla/caramel syrups, and oat milk.\n2. Verify backup inventory level under main service counter.';
          if (priorityEl) priorityEl.value = 'COMMON';
        } else if (val === 'EQUIP_CLEAN') {
          if (titleEl) titleEl.value = 'Backflush Espresso Group Heads & Wand Sanitize';
          if (descEl) descEl.value = '1. Perform backflush clean on group heads 1 & 2.\n2. Soak steam wands in warm water and food-grade disinfectant.';
          if (priorityEl) priorityEl.value = 'IMPORTANT';
        } else if (val === 'TEMP_CHECK') {
          if (titleEl) titleEl.value = 'Milk Fridge & Chiller Temperature Check';
          if (descEl) descEl.value = '1. Record digital temperature on milk fridge (must be 1°C to 4°C).\n2. Log reading in shift temperature audit log sheet.';
          if (priorityEl) priorityEl.value = 'IMPORTANT';
        } else if (val === 'CLOSING_CLEAN') {
          if (titleEl) titleEl.value = 'End-of-Shift Floor Sweep & Trash Disposal';
          if (descEl) descEl.value = '1. Sweep customer seating area and barista bar floor.\n2. Empty espresso puck bin and replace trash liners.';
          if (priorityEl) priorityEl.value = 'COMMON';
        } else if (val === 'PASTRY_RESTOCK') {
          if (titleEl) titleEl.value = 'Pastry Display Case Restock & Expiry Check';
          if (descEl) descEl.value = '1. Arrange fresh croissants and pain au chocolat in display case.\n2. Label expiry tags for fresh baked items.';
          if (priorityEl) priorityEl.value = 'COMMON';
        } else if (val === 'URGENT_SPILL') {
          if (titleEl) titleEl.value = 'URGENT: Counter Spill & Station Clean';
          if (descEl) descEl.value = 'Immediate spill response: Wipe liquid spill on main barista counter and sanitize customer hand-off area immediately.';
          if (priorityEl) priorityEl.value = 'IMMEDIATE';
        }
      });
    }

    if (formCreate) {
      formCreate.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
          const title = container.querySelector('#shift-title').value;
          const description = container.querySelector('#shift-desc').value;
          const assignedEmail = container.querySelector('#shift-assignee').value;
          const priority = container.querySelector('#shift-priority').value;

          let targetName = 'Shift Team';
          let delegationMode = 'DIRECT_EMPLOYEE';

          if (assignedEmail === 'all@plus33.com') {
            delegationMode = 'BROADCAST_ALL_EMPLOYEES';
            targetName = 'All Shift Baristas (Broadcast)';
          } else {
            const select = container.querySelector('#shift-assignee');
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
            dueDate: new Date(Date.now() + 86400000).toISOString(),
            createdAt: new Date().toISOString(),
            createdByName: this.user.name || 'Supervisor1 ST_FR_REG_1_01',
            creatorRole: 'Shift Lead',
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
          }).catch(err => logger.warn('SupervisorMyTasks', 'API post sync warning:', err));

          notificationStore.success(`Dispatched "${title}" to ${targetName}!`);
          closeCreateModal();
          await this.fetchData(container);
        } catch (err) {
          logger.error('SupervisorMyTasks', 'Create shift task error:', err);
          closeCreateModal();
        }
      });
    }

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
        const localTasks = storage.get('plus33-custom-store-tasks') || [];

        // Mark parent task as SPLIT locally
        const parentLoc = localTasks.find(x => String(x.id) === String(parentId));
        if (parentLoc) parentLoc.status = 'SPLIT';
        const parentMem = this.tasks.find(x => String(x.id) === String(parentId));
        if (parentMem) parentMem.status = 'SPLIT';

        subtaskRows.forEach(row => {
          const title = row.querySelector('.sub-title').value;
          const assignedEmail = row.querySelector('.sub-emp').value;
          const empMatch = this.rosteredEmployees.find(e => e.email === assignedEmail);
          const empName = empMatch ? empMatch.name : (assignedEmail.split('@')[0]);

          if (title) {
            const subTaskId = Date.now() + Math.floor(Math.random() * 1000);
            const subTaskObj = {
              id: subTaskId,
              parentTaskId: parentId,
              title,
              description: `Sub-task delegated from parent directive #${parentId}`,
              category: 'GENERAL',
              delegationMode: 'SPLIT_SUBTASK',
              assignedEmployeeEmail: assignedEmail,
              assignedEmployeeName: empName,
              priority: 'COMMON',
              dueDate: new Date(Date.now() + 86400000).toISOString(),
              createdAt: new Date().toISOString(),
              createdByName: this.user.name || 'Shift Supervisor',
              creatorRole: 'shiftSupervisor',
              status: 'ASSIGNED'
            };

            subTasks.push(subTaskObj);
            localTasks.unshift(subTaskObj);
          }
        });

        storage.set('plus33-custom-store-tasks', localTasks);

        try {
          await apiClient.post(`/api/v1/store-tasks/${parentId}/split`, subTasks);
          notificationStore.success(`Successfully split task into ${subTasks.length} sub-tasks!`);
        } catch (err) {
          logger.warn('SupervisorMyTasks', 'API task split fallback:', err);
          notificationStore.success(`Sub-tasks dispatched to ${subTasks.length} team members!`);
        }

        closeModal();
        await this.fetchData(container);
      });
    }
  }

  appendSubtaskRow(container) {
    const subContainer = container.querySelector('#split-subtasks-container');
    if (!subContainer) return;

    let empOptions = '<option value="all@plus33.com">👥 All Shift Baristas</option>';
    if (this.rosteredEmployees.length > 0) {
      empOptions = this.rosteredEmployees.map(e => `<option value="${e.email}">${e.name} (${e.email})</option>`).join('');
    }

    const div = document.createElement('div');
    div.className = 'subtask-row';
    div.style.cssText = 'display: flex; gap: 10px; margin-bottom: 10px; width: 100%;';
    div.innerHTML = `
      <input type="text" class="form-input sub-title" placeholder="Sub-task title (e.g. Grinder Dial-In)" style="flex: 1;" required />
      <select class="form-input sub-emp" style="width: 260px;">
        ${empOptions}
      </select>
    `;
    subContainer.appendChild(div);
  }

  bindItemEvents(container) {
    container.querySelectorAll('.btn-split-task').forEach(b => {
      b.addEventListener('click', async (e) => {
        e.preventDefault();
        const id = b.dataset.id;
        const title = b.dataset.title;

        const splitModal = container.querySelector('#modal-split-task');
        const parentIdInput = container.querySelector('#split-parent-id');
        const parentTitleInput = container.querySelector('#split-parent-title');

        if (parentIdInput) parentIdInput.value = id;
        if (parentTitleInput) parentTitleInput.value = title;

        if (splitModal) splitModal.style.display = 'flex';

        await this.loadEmployees(container);

        const subContainer = container.querySelector('#split-subtasks-container');
        if (subContainer) {
          subContainer.innerHTML = '';
          this.appendSubtaskRow(container);
          this.appendSubtaskRow(container);
        }
      });
    });

    // Review Report & Approve button handler
    container.querySelectorAll('.btn-review-report, .btn-review-subtask').forEach(b => {
      b.addEventListener('click', () => {
        const id = b.dataset.id;
        const t = this.tasks.find(x => String(x.id) === String(id));
        if (!t || !t.completionReport) return;

        const rep = t.completionReport;
        const viewModal = container.querySelector('#modal-sup-view-report');
        const contentEl = container.querySelector('#sup-view-report-content');

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
                <div style="color: #fff; font-size: 0.82rem; margin-top: 2px;">${rep.submittedBy || 'Barista'} &bull; ${rep.submittedAt ? String(rep.submittedAt).replace('T', ' ').substring(0, 16) : 'Just Now'}</div>
              </div>
            </div>

            ${imagePreviewHtml}

            <div style="border-top: 1px solid rgba(255,255,255,0.1); padding-top: 16px; margin-top: 8px; display: flex; gap: 12px; justify-content: flex-end;">
              <button type="button" class="tasks-btn tasks-btn--glass btn-reject-task-report" data-id="${t.id}" style="color: #f87171; border-color: rgba(239,68,68,0.4);">❌ Request Revision</button>
              <button type="button" class="tasks-btn tasks-btn--gold btn-approve-task-report" data-id="${t.id}">✅ Approve &amp; Mark Completed</button>
            </div>
          `;

          // Bind approval buttons
          contentEl.querySelector('.btn-approve-task-report').addEventListener('click', async () => {
            await this.approveTask(container, t.id);
            if (viewModal) viewModal.style.display = 'none';
          });

          contentEl.querySelector('.btn-reject-task-report').addEventListener('click', async () => {
            await this.rejectTask(container, t.id);
            if (viewModal) viewModal.style.display = 'none';
          });
        }

        if (viewModal) viewModal.style.display = 'flex';
      });
    });

    container.querySelectorAll('.btn-delete-sup-task').forEach(b => {
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

        notificationStore.info(`Shift directive #${id} removed.`);
        await this.fetchData(container);
      });
    });

    container.querySelectorAll('.btn-start-sup-task').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        try {
          await apiClient.put(`/api/v1/store-tasks/${id}/status?status=STARTED`);
        } catch (err) {
          // fallback
        }

        const localTasks = storage.get('plus33-custom-store-tasks') || [];
        const tLoc = localTasks.find(x => String(x.id) === String(id));
        if (tLoc) tLoc.status = 'STARTED';
        storage.set('plus33-custom-store-tasks', localTasks);

        const t = this.tasks.find(x => String(x.id) === String(id));
        if (t) t.status = 'STARTED';

        notificationStore.info('Supervisor task set to STARTED.');
        this.renderTaskList(container);
      });
    });

    container.querySelectorAll('.btn-sup-approve-ext').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        await this.reviewExtension(container, id, true);
      });
    });

    container.querySelectorAll('.btn-sup-reject-ext').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        await this.reviewExtension(container, id, false);
      });
    });

    container.querySelectorAll('.btn-sup-approve-direct').forEach(b => {
      b.addEventListener('click', async () => {
        const id = b.dataset.id;
        await this.approveTask(container, id);
      });
    });

    container.querySelectorAll('.btn-open-sup-report-modal').forEach(b => {
      b.addEventListener('click', (e) => {
        e.preventDefault();
        const id = b.dataset.id;
        const title = b.dataset.title;

        const reportModal = container.querySelector('#modal-sup-work-report');
        const taskIdInput = container.querySelector('#sup-rep-task-id');
        const taskTitleInput = container.querySelector('#sup-rep-task-title');
        const notesInput = container.querySelector('#sup-rep-notes');
        const fileInput = container.querySelector('#sup-rep-file-input');
        const filePreview = container.querySelector('#sup-rep-file-preview');
        const hiddenFileData = container.querySelector('#sup-rep-attachment-data');

        if (taskIdInput) taskIdInput.value = id;
        if (taskTitleInput) taskTitleInput.value = title;
        if (notesInput) notesInput.value = '';
        if (fileInput) fileInput.value = '';
        if (hiddenFileData) hiddenFileData.value = '';
        if (filePreview) filePreview.style.display = 'none';

        if (reportModal) reportModal.style.display = 'flex';
      });
    });

    container.querySelectorAll('.btn-view-sup-report').forEach(b => {
      b.addEventListener('click', () => {
        const id = b.dataset.id;
        const t = this.tasks.find(x => String(x.id) === String(id));
        if (!t || !t.completionReport) return;

        const rep = t.completionReport;
        const viewModal = container.querySelector('#modal-sup-view-report');
        const contentEl = container.querySelector('#sup-view-report-content');

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
                <span style="font-size: 0.75rem; color: #a1a1aa;">SUBMITTED BY &amp; TIME</span>
                <div style="color: #fff; font-size: 0.82rem; margin-top: 2px;">${rep.submittedBy || 'Shift Supervisor'} &bull; ${rep.submittedAt ? String(rep.submittedAt).replace('T', ' ').substring(0, 16) : 'Just Now'}</div>
              </div>
            </div>

            ${imagePreviewHtml}

            ${t.status === 'SUBMITTED_FOR_REVIEW' ? `
              <div style="border-top: 1px solid rgba(255,255,255,0.1); padding-top: 16px; margin-top: 8px; display: flex; gap: 12px; justify-content: flex-end;">
                <button type="button" class="tasks-btn tasks-btn--glass btn-open-sup-reject-modal" data-id="${t.id}" data-title="${t.title.replace(/"/g, '&quot;')}" style="color: #f87171; border-color: rgba(239,68,68,0.4);">❌ Request Revision</button>
                <button type="button" class="tasks-btn tasks-btn--gold btn-sup-approve-modal-btn">✅ Approve &amp; Mark Completed</button>
              </div>
            ` : ''}
          `;

          if (t.status === 'SUBMITTED_FOR_REVIEW') {
            const approveBtn = contentEl.querySelector('.btn-sup-approve-modal-btn');
            if (approveBtn) {
              approveBtn.addEventListener('click', async () => {
                await this.approveTask(container, t.id);
                if (viewModal) viewModal.style.display = 'none';
              });
            }

            const rejectBtn = contentEl.querySelector('.btn-open-sup-reject-modal');
            if (rejectBtn) {
              rejectBtn.addEventListener('click', (e) => {
                const rejId = e.target.dataset.id;
                const rejTitle = e.target.dataset.title;

                const rejModal = container.querySelector('#modal-sup-reject-report');
                const rejIdInput = container.querySelector('#sup-rej-task-id');
                const rejTitleInput = container.querySelector('#sup-rej-task-title');
                const rejReasonInput = container.querySelector('#sup-rej-reason');

                if (rejIdInput) rejIdInput.value = rejId;
                if (rejTitleInput) rejTitleInput.value = rejTitle;
                if (rejReasonInput) rejReasonInput.value = '';

                if (viewModal) viewModal.style.display = 'none';
                if (rejModal) rejModal.style.display = 'flex';
              });
            }
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
      logger.warn('SupervisorMyTasks', 'API status update warning:', err);
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
      logger.warn('SupervisorMyTasks', 'API status update warning:', err);
    }

    notificationStore.info(`Revision requested for Task #${taskId} with reason: "${rejectReason}"`);
    await this.fetchData(container);
  }

  async submitReportPendingApproval(container, taskId, reportObj) {
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
      logger.warn('SupervisorMyTasks', 'API status update warning:', err);
    }

    notificationStore.success('Completion Report submitted! Sent for Store Admin / Supervisor verification & approval.');
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
      logger.warn('SupervisorMyTasks', 'API extension review warning:', err);
    }

    if (approved) {
      notificationStore.success(`Approved extra time extension for Task #${taskId}!`);
    } else {
      notificationStore.info(`Declined extra time extension for Task #${taskId}.`);
    }
    await this.fetchData(container);
  }
}
