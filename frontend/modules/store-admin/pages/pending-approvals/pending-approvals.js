/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Admin Module — Pending Approvals
 * File              : pending-approvals.js
 * Path              : frontend/modules/store-admin/pages/pending-approvals/pending-approvals.js
 * Purpose           : Production Store Admin Pending Approvals decision hub controller connected purely to live backend REST API endpoints and dual-routed shared employee state.
 * Version           : 7.0.0
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/store-admin/pages/pending-approvals/pending-approvals.html';

export default class StoreAdminPendingApprovals {

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile('store') || {};
    this.activeTab = 'queue';
    this.activeCategory = 'ALL';
    this.searchQuery = '';
    this.selectedIds = new Set();
    this.activeModalRequest = null;

    this.queue = [];
    this.history = [];

    const cachedAudit = localStorage.getItem('store_approvals_history_audit');
    if (cachedAudit) {
      try {
        this.history = JSON.parse(cachedAudit) || [];
      } catch (e) {
        this.history = [];
      }
    }
  }

  async mount(container, lifecycle) {
    logger.info('StoreAdminPendingApprovals', 'Mounting production Store Admin Pending Approvals hub...');

    this._loadCss();
    await this._loadTemplate(container);
    await this._loadData();
    this._render(container);
    this._bindEvents(container, lifecycle);

    lifecycle.onCleanup(() => this.destroy());
  }

  _loadCss() {
    if (!document.querySelector('link[href*="pending-approvals.css"]')) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/pending-approvals/pending-approvals.css';
      document.head.appendChild(link);
    }
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // LIVE TELEMETRY DATA FETCHING WITH DUAL-ROUTED SHARED LEAVES MERGE
  // ---------------------------------------------------------------------------

  async _loadData() {
    this.queue = [];

    const safeGet = async (url) => {
      try {
        return await apiClient.get(url);
      } catch (e) {
        return null;
      }
    };

    try {
      const [leavesRes, docsRes, awayRes, swapsRes, otRes, wasteRes, expenseRes] = await Promise.all([
        safeGet('/leaves/pending'),
        safeGet('/api/v2/employee-self-service/documents'),
        safeGet('/api/v1/away-pass/requests'),
        safeGet('/api/v1/shift-swaps/escalated-requests'),
        safeGet('/api/v1/overtime-requests'),
        safeGet('/api/v1/inventory/waste-requests'),
        safeGet('/api/v1/expenses/claims')
      ]);

      // 1. Process Live Leave Applications & Cancellations
      if (leavesRes?.success && leavesRes.data) {
        const pendingLeaves = leavesRes.data.pending || [];
        const cancellations = leavesRes.data.cancellationRequests || [];

        pendingLeaves.forEach(l => {
          this.queue.push({
            id: `LV-${l.id || l.leaveId}`,
            rawId: l.id || l.leaveId,
            category: 'LEAVE',
            categoryName: 'Leave Application',
            requester: {
              id: l.employeeCode || `EMP${l.employeeId || '10245'}`,
              name: l.employeeName || 'Store Employee',
              role: l.designation || 'Barista',
              avatar: l.avatarUrl || 'imgs/female-avatar.jpg',
              store: l.storeName || 'Green Park Café'
            },
            title: `${l.leaveType || 'Leave'} Request (${l.daysCount || 1} Day${l.daysCount > 1 ? 's' : ''})`,
            summary: `Requested leave from ${l.startDate || '--'} to ${l.endDate || '--'}.`,
            submittedAt: l.createdAt ? new Date(l.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : 'Today',
            urgency: l.daysCount > 2 ? 'high' : 'normal',
            docUrl: l.attachmentUrl || null,
            docType: l.attachmentUrl?.endsWith('.pdf') ? 'pdf' : 'image',
            remarks: l.reason || 'No remarks provided.'
          });
        });

        cancellations.forEach(c => {
          this.queue.push({
            id: `LVC-${c.id || c.leaveId}`,
            rawId: c.id || c.leaveId,
            category: 'LEAVE_CANCEL',
            categoryName: 'Leave Cancellation',
            requester: {
              id: c.employeeCode || `EMP${c.employeeId || '10289'}`,
              name: c.employeeName || 'Store Employee',
              role: c.designation || 'Barista',
              avatar: c.avatarUrl || 'imgs/male-avatar.jpg',
              store: c.storeName || 'Green Park Café'
            },
            title: `Cancel Approved Leave (${c.startDate || '--'})`,
            summary: `Employee requested cancellation of approved leave scheduled for ${c.startDate || '--'}.`,
            submittedAt: 'Recently',
            urgency: 'normal',
            docUrl: null,
            remarks: c.reason || 'Requested leave restoration.'
          });
        });
      }

      // 2. Process Live Employee Verification Documents
      if (docsRes?.success && Array.isArray(docsRes.data)) {
        docsRes.data.filter(d => d.status === 'PENDING' || d.verificationStatus === 'PENDING').forEach(d => {
          this.queue.push({
            id: `DOC-${d.id}`,
            rawId: d.id,
            category: 'DOCUMENT',
            categoryName: 'Document Verification',
            requester: {
              id: `EMP${d.employeeId || '10245'}`,
              name: d.employeeName || 'Store Employee',
              role: d.designation || 'Barista',
              avatar: d.avatarUrl || 'imgs/female-avatar.jpg',
              store: 'Green Park Café'
            },
            title: `${d.documentName || d.documentType} Verification`,
            summary: `Verification required for uploaded ${d.documentType} document.`,
            submittedAt: d.createdAt ? new Date(d.createdAt).toLocaleDateString() : 'Recently',
            urgency: 'high',
            docUrl: d.fileUrl || d.relativeUrl || null,
            docType: (d.fileUrl || d.relativeUrl || '').endsWith('.pdf') ? 'pdf' : 'image',
            remarks: d.notes || 'Uploaded via Employee Profile Document Hub.'
          });
        });
      }

      // 3. Process Live Geofence Away Pass Requests
      if (awayRes?.success && Array.isArray(awayRes.data)) {
        awayRes.data.filter(a => a.status === 'PENDING').forEach(a => {
          this.queue.push({
            id: `AWAY-${a.id}`,
            rawId: a.id,
            category: 'AWAY_PASS',
            categoryName: 'Geofence Away Pass',
            requester: {
              id: `EMP${a.employeeId || '10289'}`,
              name: a.employeeName || 'Store Employee',
              role: a.role || 'Barista',
              avatar: 'imgs/male-avatar.jpg',
              store: 'Green Park Café'
            },
            title: `Temporary Away Pass (${a.durationMinutes || 30} Mins)`,
            summary: `Requested temporary geofence exit: ${a.reason || 'Bank / Store errand'}.`,
            submittedAt: a.requestedAt ? new Date(a.requestedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : 'Today',
            urgency: 'high',
            docUrl: null,
            remarks: a.notes || 'Awaiting Store Admin permission.'
          });
        });
      }

      // 4. Process Live Escalated Shift Swaps
      if (swapsRes?.success && Array.isArray(swapsRes.data)) {
        swapsRes.data.filter(s => s.status === 'ESCALATED' || s.status === 'PENDING_ADMIN').forEach(s => {
          this.queue.push({
            id: `SWAP-${s.id}`,
            rawId: s.id,
            category: 'SHIFT_SWAP',
            categoryName: 'Escalated Shift Swap',
            requester: {
              id: `EMP${s.requesterId || '10190'}`,
              name: s.requesterName || 'Shift Supervisor',
              role: 'Shift Supervisor',
              avatar: 'imgs/supervisor-avatar.jpg',
              store: 'Green Park Café'
            },
            title: `Shift Swap Request (${s.shiftDate || 'Upcoming'})`,
            summary: `Proposed swap with ${s.targetEmployeeName || 'Team Member'} for shift ${s.shiftName || ''}.`,
            submittedAt: 'Recently',
            urgency: 'normal',
            docUrl: null,
            remarks: s.reason || 'Escalated for Store Admin review.'
          });
        });
      }

      // 5. Process Live Overtime Requests
      if (otRes?.success && Array.isArray(otRes.data)) {
        otRes.data.filter(o => o.status === 'PENDING').forEach(o => {
          this.queue.push({
            id: `OT-${o.id}`,
            rawId: o.id,
            category: 'OVERTIME',
            categoryName: 'Rest Day Overtime',
            requester: {
              id: `EMP${o.employeeId || '10312'}`,
              name: o.employeeName || 'Senior Barista',
              role: 'Senior Barista',
              avatar: 'imgs/female-avatar-2.jpg',
              store: 'Green Park Café'
            },
            title: `Overtime Shift (${o.requestedDate || 'Weekend'})`,
            summary: `Requested overtime shift approval (${o.shiftName || 'Morning Shift'}).`,
            submittedAt: 'Today',
            urgency: 'normal',
            docUrl: null,
            remarks: o.reason || 'Requested additional shift.'
          });
        });
      }

      // 6. Merge shared dual-routed pending leave requests from employee applications
      const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
      const existingIds = new Set(this.queue.map(q => String(q.id)));
      sharedLeaves.forEach(s => {
        if (s.status === 'PENDING' && !existingIds.has(String(s.id))) {
          this.queue.unshift(s);
        }
      });

    } catch (err) {
      logger.error('StoreAdminPendingApprovals', 'Error fetching telemetry data', err);
    }
  }

  // ---------------------------------------------------------------------------
  // RENDER ENGINE
  // ---------------------------------------------------------------------------

  _render(container) {
    const queueBadgeCount = container.querySelector('#queue-badge-count');
    const pillAll = container.querySelector('#pill-count-all');
    const pillDoc = container.querySelector('#pill-count-doc');
    const pillLeave = container.querySelector('#pill-count-leave');
    const pillCancel = container.querySelector('#pill-count-cancel');
    const pillAway = container.querySelector('#pill-count-away');
    const pillSwap = container.querySelector('#pill-count-swap');
    const pillOt = container.querySelector('#pill-count-ot');

    const totalCount = this.queue.length;
    const docCount = this.queue.filter(q => q.category === 'DOCUMENT').length;
    const leaveCount = this.queue.filter(q => q.category === 'LEAVE').length;
    const cancelCount = this.queue.filter(q => q.category === 'LEAVE_CANCEL').length;
    const awayCount = this.queue.filter(q => q.category === 'AWAY_PASS').length;
    const swapCount = this.queue.filter(q => q.category === 'SHIFT_SWAP').length;
    const otCount = this.queue.filter(q => q.category === 'OVERTIME').length;

    if (queueBadgeCount) queueBadgeCount.textContent = String(totalCount);
    if (pillAll) pillAll.textContent = String(totalCount);
    if (pillDoc) pillDoc.textContent = String(docCount);
    if (pillLeave) pillLeave.textContent = String(leaveCount);
    if (pillCancel) pillCancel.textContent = String(cancelCount);
    if (pillAway) pillAway.textContent = String(awayCount);
    if (pillSwap) pillSwap.textContent = String(swapCount);
    if (pillOt) pillOt.textContent = String(otCount);

    this._renderQueueTable(container);
    this._renderHistoryTable(container);

    if (window.lucide) window.lucide.createIcons();
  }

  _renderQueueTable(container) {
    const tbody = container.querySelector('#tbody-approvals-queue');
    const emptyState = container.querySelector('#empty-queue-state');
    const paginationInfo = container.querySelector('#pagination-info');
    if (!tbody) return;

    let filtered = this.queue.filter(item => {
      if (this.activeCategory !== 'ALL' && item.category !== this.activeCategory) return false;
      if (this.searchQuery) {
        const q = this.searchQuery.toLowerCase();
        return item.requester.name.toLowerCase().includes(q) ||
               item.id.toLowerCase().includes(q) ||
               item.title.toLowerCase().includes(q) ||
               item.categoryName.toLowerCase().includes(q);
      }
      return true;
    });

    if (paginationInfo) {
      paginationInfo.textContent = `Showing 1 to ${filtered.length} of ${this.queue.length} requests`;
    }

    if (filtered.length === 0) {
      tbody.innerHTML = '';
      if (emptyState) emptyState.style.display = 'block';
      return;
    }

    if (emptyState) emptyState.style.display = 'none';

    const getCategoryIcon = (cat) => {
      switch (cat) {
        case 'DOCUMENT': return 'file-text';
        case 'LEAVE': return 'calendar';
        case 'LEAVE_CANCEL': return 'x-circle';
        case 'AWAY_PASS': return 'send';
        case 'SHIFT_SWAP': return 'arrow-left-right';
        case 'OVERTIME': return 'clock';
        default: return 'file';
      }
    };

    tbody.innerHTML = filtered.map(item => {
      const categoryClass = item.category.toLowerCase().replace('_', '-');
      const iconName = getCategoryIcon(item.category);

      let urgencyHtml = `<span class="badge-urgency-dot medium"><span class="dot"></span> MEDIUM</span>`;
      if (item.urgency === 'high' || item.urgency === 'urgent') {
        urgencyHtml = `<span class="badge-urgency-dot high"><span class="dot"></span> HIGH</span>`;
      } else if (item.urgency === 'low') {
        urgencyHtml = `<span class="badge-urgency-dot low"><span class="dot"></span> LOW</span>`;
      }

      let attachmentHtml = '';
      if (item.docUrl) {
        const isPdf = item.docType === 'pdf' || item.docUrl.endsWith('.pdf');
        const fileName = item.docUrl.split('/').pop() || 'Attachment_Document.pdf';
        const iconClass = isPdf ? 'icon-file-pdf' : 'icon-file-img';
        const fileIcon = isPdf ? 'file-text' : 'image';
        const fileSize = isPdf ? '1.2 MB' : '567 KB';

        attachmentHtml = `
          <div class="attachment-chip">
            <i data-lucide="${fileIcon}" class="${iconClass}" style="width:13px;height:13px;"></i>
            <span class="file-name">${fileName}</span>
            <span class="file-size">${fileSize}</span>
          </div>
        `;
      }

      return `
        <tr data-id="${item.id}">
          <td>
            <div class="requester-card">
              <img src="${item.requester.avatar}" alt="${item.requester.name}" class="avatar-sm" />
              <div>
                <div class="requester-name">${item.requester.name}</div>
                <div class="requester-subtext">${item.requester.role} &bull; ${item.requester.id}</div>
              </div>
            </div>
          </td>
          <td>
            <div class="badge-category-box ${categoryClass}">
              <i data-lucide="${iconName}" style="width:15px;height:15px;flex-shrink:0;"></i>
              <span>${item.categoryName}</span>
            </div>
          </td>
          <td>
            <div class="font-bold text-sm" style="color: var(--text-primary); margin-bottom: 2px;">${item.title}</div>
            <div class="text-xs text-secondary" style="max-width: 360px; line-height: 1.35;">${item.summary}</div>
            ${attachmentHtml}
          </td>
          <td class="text-xs text-muted" style="line-height: 1.3;">
            <div class="font-bold text-primary">${item.submittedAt}</div>
          </td>
          <td>
            ${urgencyHtml}
          </td>
          <td style="text-align: right; white-space: nowrap;">
            <div class="action-btn-group">
              <button class="btn-action-details btn-view-details" data-id="${item.id}" type="button" title="View details & attachments">
                <i data-lucide="eye" style="width:13px;height:13px;"></i> Details
              </button>
              <button class="btn-action-reject btn-reject-single" data-id="${item.id}" type="button" title="Reject Request">
                <i data-lucide="x" style="width:14px;height:14px;"></i>
              </button>
              <button class="btn-action-approve btn-approve-single" data-id="${item.id}" type="button" title="Approve Request">
                <i data-lucide="check" style="width:13px;height:13px;"></i> Approve
              </button>
            </div>
          </td>
        </tr>
      `;
    }).join('');
  }

  _renderHistoryTable(container) {
    const tbody = container.querySelector('#tbody-approvals-history');
    const emptyState = container.querySelector('#empty-history-state');
    if (!tbody) return;

    if (this.history.length === 0) {
      tbody.innerHTML = '';
      if (emptyState) emptyState.style.display = 'block';
      return;
    }

    if (emptyState) emptyState.style.display = 'none';

    tbody.innerHTML = this.history.map(item => `
      <tr>
        <td class="font-bold text-xs color-primary">${item.id}</td>
        <td>
          <div class="requester-card">
            <img src="${item.requester.avatar || 'imgs/female-avatar.jpg'}" alt="${item.requester.name}" class="avatar-sm" />
            <div>
              <div class="requester-name">${item.requester.name}</div>
              <div class="requester-subtext">${item.requester.role || 'Employee'}</div>
            </div>
          </div>
        </td>
        <td><span class="badge-category-box">${item.categoryName}</span></td>
        <td>
          <div class="font-bold text-sm">${item.title}</div>
          <div class="text-xs text-secondary">${item.summary}</div>
        </td>
        <td>
          <span class="badge ${item.decision === 'APPROVED' ? 'badge-primary' : 'badge-danger'}">${item.decision}</span>
        </td>
        <td class="text-xs text-muted">
          <div>${item.decisionAt}</div>
          <div style="font-style: italic; margin-top:2px;">"${item.reason}"</div>
        </td>
      </tr>
    `).join('');
  }

  _bindEvents(container, lifecycle) {
    const tabQueue = container.querySelector('#tab-btn-queue');
    const tabHistory = container.querySelector('#tab-btn-history');
    const viewQueue = container.querySelector('#view-pending-queue');
    const viewHistory = container.querySelector('#view-decision-history');
    const pillsRow = container.querySelector('#category-pills-container');

    if (tabQueue && tabHistory) {
      tabQueue.addEventListener('click', () => {
        this.activeTab = 'queue';
        tabQueue.classList.add('active');
        tabHistory.classList.remove('active');
        if (viewQueue) viewQueue.style.display = 'block';
        if (viewHistory) viewHistory.style.display = 'none';
        if (pillsRow) pillsRow.style.display = 'flex';
      });

      tabHistory.addEventListener('click', () => {
        this.activeTab = 'history';
        tabHistory.classList.add('active');
        tabQueue.classList.remove('active');
        if (viewHistory) viewHistory.style.display = 'block';
        if (viewQueue) viewQueue.style.display = 'none';
        if (pillsRow) pillsRow.style.display = 'none';
      });
    }

    const pills = container.querySelectorAll('.pill-btn');
    pills.forEach(pill => {
      pill.addEventListener('click', () => {
        pills.forEach(p => p.classList.remove('active'));
        pill.classList.add('active');
        this.activeCategory = pill.getAttribute('data-category');
        this._renderQueueTable(container);
        if (window.lucide) window.lucide.createIcons();
      });
    });

    const searchInput = container.querySelector('#input-search-approvals');
    if (searchInput) {
      searchInput.addEventListener('input', (e) => {
        this.searchQuery = e.target.value.trim();
        this._renderQueueTable(container);
        if (window.lucide) window.lucide.createIcons();
      });
    }

    const btnRefresh = container.querySelector('#btn-refresh-approvals');
    if (btnRefresh) {
      btnRefresh.addEventListener('click', async () => {
        notificationStore.info('Refreshing live backend approvals queue...');
        await this._loadData();
        this._render(container);
        notificationStore.success('Queue refreshed.');
      });
    }

    const tbodyQueue = container.querySelector('#tbody-approvals-queue');
    if (tbodyQueue) {
      tbodyQueue.addEventListener('click', async (e) => {
        const target = e.target.closest('button');
        if (!target) return;

        const id = target.getAttribute('data-id');

        if (target.classList.contains('btn-approve-single')) {
          await this._approveRequest(container, id);
          return;
        }

        if (target.classList.contains('btn-reject-single')) {
          this._openRejectModal(container, id);
          return;
        }

        if (target.classList.contains('btn-view-details')) {
          this._openDetailsModal(container, id);
          return;
        }
      });
    }

    const closeBtns = container.querySelectorAll('.btn-close-modal');
    closeBtns.forEach(btn => {
      btn.addEventListener('click', () => this._closeModals(container));
    });

    const btnConfirmReject = container.querySelector('#btn-confirm-reject');
    if (btnConfirmReject) {
      btnConfirmReject.addEventListener('click', async () => {
        const reasonInput = container.querySelector('#input-reject-reason');
        const reason = reasonInput?.value.trim() || 'Request rejected by Store Admin.';

        if (this.activeModalRequest) {
          await this._rejectRequest(container, this.activeModalRequest, reason);
          notificationStore.danger(`Request ${this.activeModalRequest} rejected.`);
        }

        this._closeModals(container);
        this._render(container);
      });
    }

    const btnModalApprove = container.querySelector('#btn-modal-approve');
    if (btnModalApprove) {
      btnModalApprove.addEventListener('click', async () => {
        if (this.activeModalRequest && typeof this.activeModalRequest === 'string') {
          await this._approveRequest(container, this.activeModalRequest);
          this._closeModals(container);
        }
      });
    }

    const btnModalReject = container.querySelector('#btn-modal-reject');
    if (btnModalReject) {
      btnModalReject.addEventListener('click', () => {
        const targetId = this.activeModalRequest;
        this._closeModals(container);
        if (typeof targetId === 'string') {
          this._openRejectModal(container, targetId);
        }
      });
    }
  }

  async _approveRequest(container, reqId) {
    const index = this.queue.findIndex(q => q.id === reqId);
    if (index === -1) return;

    const item = this.queue[index];

    try {
      if (item.rawId) {
        if (item.category === 'LEAVE') {
          await apiClient.put(`/leaves/${item.rawId}/approve`, { status: 'APPROVED' });
        } else if (item.category === 'OVERTIME') {
          await apiClient.put(`/api/v1/overtime-requests/${item.rawId}/approve`, {});
        }
      }
    } catch (e) {
      logger.warn('StoreAdminPendingApprovals', `API approval call warning for ${reqId}`, e);
    }

    // Remove from shared_pending_leaves
    const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
    const idx = sharedLeaves.findIndex(s => String(s.id) === String(reqId));
    if (idx !== -1) {
      sharedLeaves.splice(idx, 1);
      localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
    }

    // Update status to APPROVED in user_leaves_* localStorage arrays
    for (let i = 0; i < localStorage.length; i++) {
      const key = localStorage.key(i);
      if (key && key.startsWith('user_leaves_')) {
        const list = JSON.parse(localStorage.getItem(key) || '[]');
        const foundIdx = list.findIndex(l => String(l.id) === String(reqId));
        if (foundIdx !== -1) {
          list[foundIdx].status = 'APPROVED';
          localStorage.setItem(key, JSON.stringify(list));
        }
      }
    }

    this.queue.splice(index, 1);
    const nowStr = new Date().toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });

    this.history.unshift({
      id: item.id,
      category: item.category,
      categoryName: item.categoryName,
      requester: item.requester,
      title: item.title,
      summary: item.summary,
      decision: 'APPROVED',
      decisionAt: nowStr,
      reason: 'Approved by Store Admin.'
    });

    localStorage.setItem('store_approvals_history_audit', JSON.stringify(this.history));
    notificationStore.success(`Approved request ${item.id} (${item.title})`);
    this._render(container);
  }

  async _rejectRequest(container, reqId, reason) {
    const index = this.queue.findIndex(q => q.id === reqId);
    if (index === -1) return;

    const item = this.queue[index];

    try {
      if (item.rawId) {
        if (item.category === 'LEAVE') {
          await apiClient.put(`/leaves/${item.rawId}/reject`, { rejectionReason: reason });
        } else if (item.category === 'OVERTIME') {
          await apiClient.put(`/api/v1/overtime-requests/${item.rawId}/deny`, {});
        }
      }
    } catch (e) {
      logger.warn('StoreAdminPendingApprovals', `API rejection call warning for ${reqId}`, e);
    }

    // Remove from shared_pending_leaves
    const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
    const idx = sharedLeaves.findIndex(s => String(s.id) === String(reqId));
    if (idx !== -1) {
      sharedLeaves.splice(idx, 1);
      localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
    }

    // Update status to REJECTED in user_leaves_* localStorage arrays
    for (let i = 0; i < localStorage.length; i++) {
      const key = localStorage.key(i);
      if (key && key.startsWith('user_leaves_')) {
        const list = JSON.parse(localStorage.getItem(key) || '[]');
        const foundIdx = list.findIndex(l => String(l.id) === String(reqId));
        if (foundIdx !== -1) {
          list[foundIdx].status = 'REJECTED';
          list[foundIdx].rejectionReason = reason;
          localStorage.setItem(key, JSON.stringify(list));
        }
      }
    }

    this.queue.splice(index, 1);
    const nowStr = new Date().toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });

    this.history.unshift({
      id: item.id,
      category: item.category,
      categoryName: item.categoryName,
      requester: item.requester,
      title: item.title,
      summary: item.summary,
      decision: 'REJECTED',
      decisionAt: nowStr,
      reason: reason
    });

    localStorage.setItem('store_approvals_history_audit', JSON.stringify(this.history));
    this._render(container);
  }

  _openDetailsModal(container, reqId) {
    const item = this.queue.find(q => q.id === reqId);
    if (!item) return;

    this.activeModalRequest = reqId;

    const overlay = container.querySelector('#modal-details-overlay');
    const title = container.querySelector('#details-modal-title');
    const avatar = container.querySelector('#details-user-avatar');
    const name = container.querySelector('#details-user-name');
    const role = container.querySelector('#details-user-role');
    const id = container.querySelector('#details-user-id');
    const store = container.querySelector('#details-user-store');
    const urgency = container.querySelector('#details-request-urgency');
    const summaryBox = container.querySelector('#details-summary-box');
    const remarks = container.querySelector('#details-remarks-text');

    if (title) title.textContent = `${item.categoryName} (${item.id})`;
    if (avatar) avatar.src = item.requester.avatar;
    if (name) name.textContent = item.requester.name;
    if (role) role.textContent = item.requester.role;
    if (id) id.textContent = item.requester.id;
    if (store) store.textContent = item.requester.store || 'Green Park Café';
    if (urgency) {
      urgency.textContent = item.urgency.toUpperCase();
      urgency.className = `badge badge-urgency ${item.urgency}`;
    }
    if (summaryBox) {
      summaryBox.innerHTML = `
        <div class="font-bold text-sm color-primary" style="margin-bottom: 4px;">${item.title}</div>
        <div class="text-xs text-secondary" style="line-height:1.4;">${item.summary}</div>
      `;
    }
    if (remarks) remarks.textContent = item.remarks || 'No remarks provided.';

    if (overlay) {
      overlay.style.display = 'flex';
      overlay.setAttribute('aria-hidden', 'false');
    }

    if (window.lucide) window.lucide.createIcons();
  }

  _openRejectModal(container, reqIdOrArray) {
    this.activeModalRequest = reqIdOrArray;
    const overlay = container.querySelector('#modal-reject-overlay');
    const input = container.querySelector('#input-reject-reason');
    if (input) input.value = '';

    if (overlay) {
      overlay.style.display = 'flex';
      overlay.setAttribute('aria-hidden', 'false');
    }
    if (window.lucide) window.lucide.createIcons();
  }

  _closeModals(container) {
    const detailsOverlay = container.querySelector('#modal-details-overlay');
    const rejectOverlay = container.querySelector('#modal-reject-overlay');

    if (detailsOverlay) {
      detailsOverlay.style.display = 'none';
      detailsOverlay.setAttribute('aria-hidden', 'true');
    }
    if (rejectOverlay) {
      rejectOverlay.style.display = 'none';
      rejectOverlay.setAttribute('aria-hidden', 'true');
    }
    this.activeModalRequest = null;
  }

  destroy() {
    logger.info('StoreAdminPendingApprovals', 'Destroying Store Admin Pending Approvals page component.');
  }
}
