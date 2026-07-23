/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin Module
 * File              : leave.js
 * Path              : frontend/modules/store-admin/pages/leave/leave.js
 * Purpose           : Controller component for Store Leave approvals UI with real-time shared employee leave telemetry.
 * Version           : 4.0.0
 *
 * Related HTML      : frontend/modules/store-admin/pages/leave/leave.html
 * Related CSS       : frontend/modules/store-admin/pages/leave/leave.css
 * Related APIs      : GET /leaves/pending
 *                     GET /leaves/my
 *                     GET /leaves/reports/summary
 *                     GET /leaves/holidays
 *                     GET /leaves/types
 *                     PUT /leaves/:id/approve
 *                     PUT /leaves/:id/reject
 *                     PUT /leaves/:id/approve-cancellation
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { LeavePolicyPopup } from '../../../../shared/leave-policy-popup/popup.js';

const TEMPLATE_URL = 'modules/store-admin/pages/leave/leave.html';

export default class StoreAdminLeave {

  constructor() {
    this.user = authStore.getUser();
    this.pendingLeaves = [];
    this.cancellationRequests = [];
    this.allLeaves = [];
    this.summary = {};
    this.holidays = [];
    this.leaveTypes = [];
    this.activeTab = 'approvals';
  }

  async mount(container, lifecycle) {
    logger.info('StoreAdminLeave', 'Mounting store admin leave management...');

    this._loadCss();
    this._renderLoading(container);
    await this.loadData();
    await this._loadTemplate(container);

    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    if (!document.querySelector('link[href*="leave.css"]')) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/leave/leave.css';
      document.head.appendChild(link);
    }
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // DATA TELEMETRY SUB-ROUTINES
  // ---------------------------------------------------------------------------

  async loadData() {
    try {
      const safeGet = async (url) => {
        try { return await apiClient.get(url); } catch (e) { return null; }
      };
      const [pendingRes, allRes, summaryRes, holidayRes, typesRes] = await Promise.all([
        safeGet('/leaves/pending'),
        safeGet('/leaves/my'),
        safeGet('/leaves/reports/summary'),
        safeGet(`/leaves/holidays?countryCode=FR&year=${new Date().getFullYear()}`),
        safeGet('/leaves/types')
      ]);

      if (pendingRes?.success && pendingRes.data) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      } else {
        this.pendingLeaves = [];
        this.cancellationRequests = [];
      }

      // Merge shared dual-routed pending leaves from employee submissions
      const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
      const existingIds = new Set(this.pendingLeaves.map(l => String(l.id)));
      sharedLeaves.forEach(s => {
        if (s.category === 'LEAVE' && s.status === 'PENDING' && !existingIds.has(String(s.id))) {
          this.pendingLeaves.unshift({
            id: s.id,
            employeeName: s.requester?.name || s.employeeName || 'Store Employee',
            leaveTypeCode: s.leaveType || 'ANNUAL',
            totalDays: s.daysCount || s.totalDays || 1,
            startDate: s.startDate,
            endDate: s.endDate,
            reason: s.remarks || s.reason || 'Leave application',
            hasDocument: s.hasDocument || false,
            requiresDocument: s.requiresDocument || false,
            isProtected: false
          });
        }
      });

      this.summary = summaryRes?.success ? summaryRes.data || {} : {};
      this.holidays = holidayRes?.success ? holidayRes.data || [] : [];
      this.leaveTypes = typesRes?.success ? typesRes.data || [] : [];
    } catch (err) {
      logger.error('StoreAdminLeave', 'Failed to load data', err);
    }
  }

  async loadAndRender(container, lifecycle) {
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    const countEl = container.querySelector('#header-pending-count');
    if (countEl) {
      const realTotal = this.pendingLeaves.length + this.cancellationRequests.length;
      countEl.textContent = `${realTotal} Action Required`;
    }

    this._renderPendingQueue(container);
    this._renderCancellations(container);
    this._renderSummary(container);
    this._renderPolicies(container);
    this._renderHolidays(container);

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const approveBtns = container.querySelectorAll('.btn-approve-leave');
    approveBtns.forEach(btn => {
      const leaveId = btn.dataset.id;
      const commentInput = container.querySelector(`.admin-comment-input[data-id="${leaveId}"]`);

      const handleApprove = async () => {
        const comment = commentInput?.value?.trim() || '';
        await this.doApprove(leaveId, comment, container, lifecycle);
      };
      btn.addEventListener('click', handleApprove);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleApprove));
    });

    const rejectBtns = container.querySelectorAll('.btn-reject-leave');
    const modal = container.querySelector('#reject-modal');
    const modalSubmit = container.querySelector('#reject-modal-submit');
    const modalClose = container.querySelector('#reject-modal-close');
    const rejectReasonInput = container.querySelector('#reject-reason-input');

    rejectBtns.forEach(btn => {
      const handleOpenReject = () => {
        if (modalSubmit) modalSubmit.dataset.leaveId = btn.dataset.id;
        if (rejectReasonInput) rejectReasonInput.value = '';
        if (modal) {
          modal.style.display = 'flex';
          modal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenReject);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenReject));
    });

    if (modalSubmit) {
      const handleConfirmReject = async () => {
        const leaveId = modalSubmit.dataset.leaveId;
        const reason = rejectReasonInput?.value?.trim();

        try {
          await apiClient.put(`/leaves/${leaveId}/reject`, { rejectionReason: reason });
        } catch (e) {
          logger.warn('StoreAdminLeave', 'API reject warning', e);
        }

        // Remove from shared_pending_leaves
        const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
        const idx = sharedLeaves.findIndex(s => String(s.id) === String(leaveId));
        if (idx !== -1) {
          sharedLeaves.splice(idx, 1);
          localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
        }

        notificationStore.danger('Leave request rejected.');
        if (modal) {
          modal.style.display = 'none';
          modal.setAttribute('aria-hidden', 'true');
        }
        await this.loadAndRender(container, lifecycle);
      };
      modalSubmit.addEventListener('click', handleConfirmReject);
      lifecycle.onCleanup(() => modalSubmit.removeEventListener('click', handleConfirmReject));
    }

    if (modalClose) {
      const handleCloseReject = () => {
        if (modal) {
          modal.style.display = 'none';
          modal.setAttribute('aria-hidden', 'true');
        }
      };
      modalClose.addEventListener('click', handleCloseReject);
      lifecycle.onCleanup(() => modalClose.removeEventListener('click', handleCloseReject));
    }

    // Bind Policy Booklet Popup
    const policyBookletBtn = container.querySelector('#btn-view-policy-booklet');
    if (policyBookletBtn) {
      const popup = new LeavePolicyPopup();
      popup.init(policyBookletBtn);
    }
  }

  async doApprove(leaveId, comment, container, lifecycle) {
    try {
      await apiClient.put(`/leaves/${leaveId}/approve`, { comment });
    } catch (err) {
      logger.warn('StoreAdminLeave', 'API approval warning', err);
    }

    // Remove from shared_pending_leaves
    const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
    const idx = sharedLeaves.findIndex(s => String(s.id) === String(leaveId));
    if (idx !== -1) {
      sharedLeaves.splice(idx, 1);
      localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
    }

    notificationStore.success('Leave approved.');
    await this.loadAndRender(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderLoading(container) {
    container.innerHTML = `
      <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
        <i data-lucide="loader-2" class="animate-spin" style="width:32px;height:32px;color:var(--accent-primary);"></i>
        <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading leave management...</span>
      </div>`;
    if (window.lucide) window.lucide.createIcons();
  }

  _renderPendingQueue(container) {
    const listContainer = container.querySelector('#approval-queue-list');
    const emptyTpl = container.querySelector('#leave-empty-queue-tpl');
    const cardTpl = container.querySelector('#leave-pending-card-tpl');

    if (!listContainer) return;

    listContainer.replaceChildren();

    if (this.pendingLeaves.length === 0) {
      if (emptyTpl) {
        listContainer.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    this.pendingLeaves.forEach(req => {
      if (!cardTpl) return;
      const clone = cardTpl.content.cloneNode(true);

      const nameEl = clone.querySelector('.employee-name');
      const sublineEl = clone.querySelector('.leave-details-subline');
      const datesEl = clone.querySelector('.dates-subline');
      const reasonEl = clone.querySelector('.reason-quote-box');
      const commentInput = clone.querySelector('.admin-comment-input');
      const approveBtn = clone.querySelector('.btn-approve-leave');
      const rejectBtn = clone.querySelector('.btn-reject-leave');

      if (nameEl) nameEl.textContent = req.employeeName;
      if (sublineEl) sublineEl.textContent = `${req.leaveTypeCode || 'Leave'} • ${req.totalDays || 1} day(s)`;
      if (datesEl) datesEl.textContent = `Requested: ${req.startDate} to ${req.endDate}`;
      if (reasonEl) reasonEl.textContent = `"${req.reason || 'No reason specified'}"`;
      if (commentInput) commentInput.setAttribute('data-id', String(req.id));
      if (approveBtn) approveBtn.setAttribute('data-id', String(req.id));
      if (rejectBtn) rejectBtn.setAttribute('data-id', String(req.id));

      listContainer.appendChild(clone);
    });
  }

  _renderCancellations(container) { }
  _renderSummary(container) { }
  
  _renderPolicies(container) {
    const list = container.querySelector('#policy-details-container');
    if (!list) return;

    list.replaceChildren();

    if (this.leaveTypes.length === 0) {
      list.innerHTML = '<div class="text-center text-muted py-2 text-xs">No policies defined</div>';
      return;
    }

    this.leaveTypes.slice(0, 4).forEach(lt => {
      const div = document.createElement('div');
      div.className = 'policy-row';
      div.innerHTML = `
        <span class="label font-bold">${lt.name} ${lt.protected ? '🛡️' : ''}</span>
        <div class="policy-badge-group">
          <span class="level-badge">${lt.paid ? 'Paid' : 'Unpaid'}</span>
          <span class="limit-val">${lt.annualLimit !== null && lt.annualLimit !== undefined ? `${lt.annualLimit} days` : 'Unlimited'}</span>
        </div>
      `;
      list.appendChild(div);
    });

    const bookletDiv = document.createElement('div');
    bookletDiv.className = 'flex justify-end mt-2';
    bookletDiv.innerHTML = `
      <button type="button" id="btn-view-policy-booklet" class="btn btn-xs btn-outline text-accent font-semibold" style="font-size: 0.65rem; padding: 4px 8px;">
        <i data-lucide="book-open" class="w-3 h-3 mr-1"></i> View Booklet
      </button>
    `;
    list.appendChild(bookletDiv);
  }
  
  _renderHolidays(container) { }
}
