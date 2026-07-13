/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin Module
 * File              : leave.js
 * Path              : frontend/modules/store-admin/leave/leave.js
 * Purpose           : Controller component for Store Leave approvals UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-admin/leave/leave.html
 * Related CSS       : frontend/modules/store-admin/leave/leave.css
 * Related APIs      : GET /leaves/pending
 *                     GET /leaves/my
 *                     GET /leaves/reports/summary
 *                     GET /leaves/holidays
 *                     GET /leaves/types
 *                     PUT /leaves/:id/approve
 *                     PUT /leaves/:id/reject
 *                     PUT /leaves/:id/approve-cancellation
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in leave.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../../store/authStore.js';
import { notificationStore } from '../../../../../store/notificationStore.js';
import { logger } from '../../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the leave HTML template */
const TEMPLATE_URL = 'modules/store-admin/pages/leave/leave.html';

export default class StoreAdminLeave {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

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

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreAdminLeave component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreAdminLeave', 'Mounting store admin leave management...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout with loading state
    this._renderLoading(container);

    // 2. Fetch all telemetry and policies data from database
    await this.loadData();

    // 3. Inject full layout
    await this._loadTemplate(container);

    // 4. Render details
    this.render(container);

    // 5. Bind event listeners
    this.bindEvents(container, lifecycle);
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
      if (pendingRes?.success) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      }
      this.summary = summaryRes?.success ? summaryRes.data || {} : {};
      this.holidays = holidayRes?.success ? holidayRes.data || [] : [];
      this.leaveTypes = typesRes?.success ? typesRes.data || [] : [];
    } catch (err) {
      logger.error('StoreAdminLeave', 'Failed to load data', err);
    }
  }

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): loadAndRender
  // ---------------------------------------------------------------------------

  async loadAndRender(container, lifecycle) {
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    const { totalPending = 0, totalApproved = 0, totalRejected = 0, totalRequests = 0 } = this.summary;
    const cancellationCount = this.cancellationRequests.length;

    // 1. Sync counts badges
    const badgePending = container.querySelector('#badge-pending-count');
    const badgeCancellations = container.querySelector('#badge-cancellations-count');
    if (badgePending) badgePending.textContent = String(this.pendingLeaves.length);
    if (badgeCancellations) badgeCancellations.textContent = String(cancellationCount);

    // 2. Sync KPI block grid cards
    const kpiGrid = container.querySelector('#leave-kpi-grid');
    if (kpiGrid) {
      const kpis = [
        { label: 'Pending Approvals', value: totalPending, icon: 'clock', color: 'var(--status-warning)' },
        { label: 'Cancellation Requests', value: cancellationCount, icon: 'x-circle', color: 'var(--status-danger)' },
        { label: 'Approved This Year', value: totalApproved, icon: 'check-circle', color: 'var(--status-success)' },
        { label: 'Total Requests', value: totalRequests, icon: 'calendar', color: 'var(--accent-primary)' }
      ];
      kpiGrid.replaceChildren();
      kpis.forEach(k => {
        const div = document.createElement('div');
        div.className = 'kpi-block-card';
        div.innerHTML = `
          <div class="kpi-block-header">
            <span class="kpi-block-title">${k.label}</span>
            <i data-lucide="${k.icon}" class="kpi-block-icon" style="color: ${k.color};" aria-hidden="true"></i>
          </div>
          <div class="kpi-block-value">${k.value}</div>
        `;
        kpiGrid.appendChild(div);
      });
    }

    // 3. Render Approvals List
    this._renderPendingQueue(container);

    // 4. Render Cancellation Requests
    this._renderCancellations(container);

    // 5. Render Yearly Summaries
    this._renderSummary(container);

    // 6. Render Policy details
    this._renderPolicies(container);

    // 7. Render French Public Holidays
    this._renderHolidays(container);

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    // 1. Approve leave listeners
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

    // 2. Reject leave click -> open modal prompt
    const rejectBtns = container.querySelectorAll('.btn-reject-leave');
    const modal = container.querySelector('#reject-modal');
    const modalSubmit = container.querySelector('#reject-modal-submit');
    const modalClose = container.querySelector('#reject-modal-close');
    const rejectReasonInput = container.querySelector('#reject-reason-input');
    const charCountEl = container.querySelector('#reject-char-count');

    rejectBtns.forEach(btn => {
      const handleOpenReject = () => {
        if (modalSubmit) modalSubmit.dataset.leaveId = btn.dataset.id;
        if (rejectReasonInput) rejectReasonInput.value = '';
        if (charCountEl) charCountEl.textContent = '0 / 500';
        if (modal) {
          modal.style.display = 'flex';
          modal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenReject);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenReject));
    });

    // Char count update listener
    if (rejectReasonInput) {
      const handleCharCount = (e) => {
        if (charCountEl) charCountEl.textContent = `${e.target.value.length} / 500`;
      };
      rejectReasonInput.addEventListener('input', handleCharCount);
      lifecycle.onCleanup(() => rejectReasonInput.removeEventListener('input', handleCharCount));
    }

    // Modal reject confirm submit
    if (modalSubmit) {
      const handleConfirmReject = async () => {
        const leaveId = modalSubmit.dataset.leaveId;
        const reason = rejectReasonInput?.value?.trim();
        if (!reason || reason.length < 10) {
          notificationStore.danger('Rejection reason must be at least 10 characters.');
          return;
        }
        if (reason.length > 500) {
          notificationStore.danger('Rejection reason cannot exceed 500 characters.');
          return;
        }

        try {
          const response = await apiClient.put(`/leaves/${leaveId}/reject`, { rejectionReason: reason });
          if (response?.success) {
            notificationStore.success('Leave request rejected.');
            if (modal) {
              modal.style.display = 'none';
              modal.setAttribute('aria-hidden', 'true');
            }
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Rejection failed.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Rejection failed.');
        }
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

    // Modal background click dismiss
    if (modal) {
      const handleModalBgClick = (e) => {
        if (e.target === modal) {
          modal.style.display = 'none';
          modal.setAttribute('aria-hidden', 'true');
        }
      };
      modal.addEventListener('click', handleModalBgClick);
      lifecycle.onCleanup(() => modal.removeEventListener('click', handleModalBgClick));
    }

    // 3. Approve cancellation requests
    const cancellationBtns = container.querySelectorAll('.btn-approve-cancellation');
    cancellationBtns.forEach(btn => {
      const handleApproveCancel = async () => {
        try {
          const response = await apiClient.put(`/leaves/${btn.dataset.id}/approve-cancellation`, {});
          if (response?.success) {
            notificationStore.success('Leave cancellation approved. Balance restored.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to approve cancellation.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to approve cancellation.');
        }
      };
      btn.addEventListener('click', handleApproveCancel);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleApproveCancel));
    });
  }

  async doApprove(leaveId, comment, container, lifecycle) {
    try {
      const response = await apiClient.put(`/leaves/${leaveId}/approve`, { comment });
      if (response?.success) {
        notificationStore.success('Leave approved. Balance updated.');
        await this.loadAndRender(container, lifecycle);
      } else {
        notificationStore.danger(response?.message || 'Approval failed.');
      }
    } catch (err) {
      notificationStore.danger(err.message || 'Approval failed.');
    }
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

    this.pendingLeaves.forEach(l => {
      if (!cardTpl) return;
      const clone = cardTpl.content.cloneNode(true);

      const name = clone.querySelector('.employee-name');
      const subline = clone.querySelector('.leave-details-subline');
      const dates = clone.querySelector('.dates-subline');
      const reason = clone.querySelector('.reason-quote-box');
      const commentInput = clone.querySelector('.admin-comment-input');
      const approveBtn = clone.querySelector('.btn-approve-leave');
      const rejectBtn = clone.querySelector('.btn-reject-leave');
      const warningNote = clone.querySelector('.document-warning-note');
      
      const slaSpan = clone.querySelector('.sla-warning-span');
      const protectedBadge = clone.querySelector('.protected-badge');
      const docBadge = clone.querySelector('.document-badge');

      if (name) name.textContent = l.employeeName || 'Employee';
      if (subline) {
        const sessionStr = l.session === 'FULL_DAY' ? 'Full Day' : l.session === 'HALF_DAY_AM' ? '½ Morning' : '½ Afternoon';
        subline.innerHTML = `${l.leaveType} &nbsp;·&nbsp; ${sessionStr} &nbsp;·&nbsp; <strong>${l.totalDays}d</strong>`;
      }

      // SLA warning settings
      if (slaSpan && l.approvalDueAt) {
        const due = new Date(l.approvalDueAt);
        const now = new Date();
        const hoursLeft = Math.floor((due - now) / 3600000);
        if (hoursLeft < 0) {
          slaSpan.textContent = '⏰ SLA OVERDUE';
          slaSpan.className = 'sla-warning-span sla-warning-span--overdue';
        } else if (hoursLeft < 6) {
          slaSpan.textContent = `⏳ ${hoursLeft}h left`;
          slaSpan.className = 'sla-warning-span sla-warning-span--near';
        } else {
          slaSpan.textContent = `Due ${due.toLocaleDateString()}`;
          slaSpan.className = 'sla-warning-span sla-warning-span--normal';
        }
      }

      if (protectedBadge && !l.isProtected) protectedBadge.style.display = 'none';
      if (docBadge && !(!l.hasDocument && l.requiresDocument)) docBadge.style.display = 'none';

      if (dates) dates.innerHTML = `📅 ${l.startDate} → ${l.endDate}`;
      
      if (reason) {
        if (l.reason) {
          reason.textContent = `"${l.reason}"`;
        } else {
          reason.style.display = 'none';
        }
      }

      if (commentInput) commentInput.setAttribute('data-id', String(l.id));

      const requiresDoc = !l.hasDocument && l.requiresDocument;
      if (approveBtn) {
        approveBtn.setAttribute('data-id', String(l.id));
        if (requiresDoc) {
          approveBtn.disabled = true;
          approveBtn.style.opacity = '0.4';
          approveBtn.style.pointerEvents = 'none';
        }
      }

      if (rejectBtn) {
        rejectBtn.setAttribute('data-id', String(l.id));
        if (l.isProtected) {
          rejectBtn.disabled = true;
          rejectBtn.style.opacity = '0.4';
          rejectBtn.style.pointerEvents = 'none';
        }
      }

      if (warningNote && !requiresDoc) {
        warningNote.style.display = 'none';
      }

      listContainer.appendChild(clone);
    });
  }

  _renderCancellations(container) {
    const listContainer = container.querySelector('#cancellation-queue-list');
    const emptyTpl = container.querySelector('#leave-empty-cancellations-tpl');
    const cardTpl = container.querySelector('#leave-cancellation-card-tpl');

    if (!listContainer) return;

    listContainer.replaceChildren();

    if (this.cancellationRequests.length === 0) {
      if (emptyTpl) {
        listContainer.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    this.cancellationRequests.forEach(l => {
      if (!cardTpl) return;
      const clone = cardTpl.content.cloneNode(true);

      const name = clone.querySelector('.employee-name');
      const subline = clone.querySelector('.leave-details-subline');
      const reason = clone.querySelector('.reason-quote-box');
      const approveBtn = clone.querySelector('.btn-approve-cancellation');

      if (name) name.textContent = l.employeeName;
      if (subline) {
        subline.innerHTML = `Requesting cancellation of <strong>${l.leaveType}</strong> (${l.startDate} – ${l.endDate})`;
      }
      if (reason) reason.textContent = `Reason: "${l.cancellationReason || 'Not specified'}"`;
      if (approveBtn) approveBtn.setAttribute('data-id', String(l.id));

      listContainer.appendChild(clone);
    });
  }

  _renderSummary(container) {
    const title = container.querySelector('#summary-year-title');
    const list = container.querySelector('#summary-details-container');

    if (title && this.summary.year) {
      title.textContent = `Leave Summary ${this.summary.year}`;
    }

    if (list) {
      list.replaceChildren();
      const stats = [
        { label: 'Total Requests', value: this.summary.totalRequests || 0, color: 'var(--accent-primary)' },
        { label: 'Approved', value: this.summary.totalApproved || 0, color: 'var(--status-success)' },
        { label: 'Pending', value: this.summary.totalPending || 0, color: 'var(--status-warning)' },
        { label: 'Rejected', value: this.summary.totalRejected || 0, color: 'var(--status-danger)' },
        { label: 'Cancelled', value: this.summary.totalCancelled || 0, color: 'var(--text-muted)' }
      ];

      stats.forEach(s => {
        const div = document.createElement('div');
        div.className = 'summary-row';
        div.innerHTML = `
          <span class="label">${s.label}</span>
          <span class="val" style="color: ${s.color};">${s.value}</span>
        `;
        list.appendChild(div);
      });
    }
  }

  _renderPolicies(container) {
    const list = container.querySelector('#policy-details-container');
    if (!list) return;

    list.replaceChildren();

    this.leaveTypes.forEach(lt => {
      const div = document.createElement('div');
      div.className = 'policy-row';
      div.innerHTML = `
        <span class="label">
          ${lt.protected ? '🛡️ ' : ''}${lt.name}
        </span>
        <div class="policy-badge-group">
          <span class="level-badge">${lt.approvalLevel}</span>
          ${lt.annualLimit ? `<span class="limit-val">${lt.annualLimit}d</span>` : '<span class="limit-val limit-val--open">Open</span>'}
        </div>
      `;
      list.appendChild(div);
    });
  }

  _renderHolidays(container) {
    const title = container.querySelector('#holidays-title');
    const list = container.querySelector('#holidays-details-container');

    if (title) {
      title.textContent = `🇫🇷 Public Holidays ${new Date().getFullYear()}`;
    }

    if (list) {
      list.replaceChildren();
      this.holidays.slice(0, 6).forEach(h => {
        const div = document.createElement('div');
        div.className = 'holiday-row';
        div.innerHTML = `
          <span class="label">🇫🇷 ${h.name}</span>
          <span class="val">${h.date}</span>
        `;
        list.appendChild(div);
      });
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'store-admin-leave-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/leave/leave.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreAdminLeave };
