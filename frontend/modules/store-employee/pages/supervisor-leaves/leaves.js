/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module - Shift Supervisor Sub-Role
 * File              : leaves.js
 * Path              : frontend/modules/store-employee/supervisor-leaves/leaves.js
 * Purpose           : Controller component for Shift Supervisor leave approvals hub UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/supervisor-leaves/leaves.html
 * Related CSS       : frontend/modules/store-employee/supervisor-leaves/leaves.css
 * Related APIs      : GET /leaves/pending
 *                     PUT /leaves/:id/approve
 *                     PUT /leaves/:id/reject
 *                     PUT /leaves/:id/approve-cancellation
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in leaves.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the supervisor leaves HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/supervisor-leaves/leaves.html';

export default class StoreSupervisorLeaves {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.pendingLeaves = [];
    this.cancellationRequests = [];
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreSupervisorLeaves component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreSupervisorLeaves', 'Mounting supervisor leave approvals...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout template
    await this._loadTemplate(container);

    // 2. Render loading indicator
    this.renderLoading(container);

    // 3. Fetch requests data details
    await this.loadData();

    // 4. Render main layout
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
      const pendingRes = await safeGet('/leaves/pending');
      if (pendingRes?.success) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      } else {
        this.pendingLeaves = [];
        this.cancellationRequests = [];
      }
    } catch (err) {
      logger.error('StoreSupervisorLeaves', 'Failed to load pending leaves', err);
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

  renderLoading(container) {
    const gridContainer = container.querySelector('#requests-grid-container');
    const loadingTpl = container.querySelector('#leaves-loading-tpl');

    if (gridContainer && loadingTpl) {
      gridContainer.replaceChildren(loadingTpl.content.cloneNode(true));
      if (window.lucide) window.lucide.createIcons();
    }
  }

  render(container) {
    // 1. Sync header text counter
    const countEl = container.querySelector('#lbl-pending-count');
    const totalCount = this.pendingLeaves.length + this.cancellationRequests.length;
    if (countEl) countEl.textContent = `${totalCount} Pending`;

    // 2. Render Requests list grid
    this._renderRequests(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const modal = container.querySelector('#reject-modal');
    const modalSubmit = container.querySelector('#reject-modal-submit');
    const modalClose = container.querySelector('#reject-modal-close');

    // 1. Approve Leave buttons listener inside table cards
    container.querySelectorAll('.btn-approve-leave').forEach(btn => {
      const handleApprove = async () => {
        const leaveId = btn.getAttribute('data-id');
        const commentInput = container.querySelector(`.supervisor-comment-input[data-id="${leaveId}"]`);
        const comment = commentInput?.value?.trim() || '';
        try {
          const response = await apiClient.put(`/leaves/${leaveId}/approve`, { comment });
          if (response?.success) {
            notificationStore.success('Time-off application approved successfully.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to approve leave request.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to approve leave.');
        }
      };
      btn.addEventListener('click', handleApprove);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleApprove));
    });

    // 2. Reject Leave triggers modal popup menu
    container.querySelectorAll('.btn-reject-leave').forEach(btn => {
      const handleOpenReject = () => {
        const leaveId = btn.getAttribute('data-id');
        if (modalSubmit) modalSubmit.dataset.leaveId = leaveId;
        
        const reasonInput = container.querySelector('#reject-reason-input');
        if (reasonInput) reasonInput.value = '';

        if (modal) {
          modal.style.display = 'flex';
          modal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenReject);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenReject));
    });

    // 3. Confirm Reject Leave submit button listener
    if (modalSubmit) {
      const handleConfirmReject = async () => {
        const leaveId = modalSubmit.dataset.leaveId;
        const reasonInput = container.querySelector('#reject-reason-input');
        const reason = reasonInput?.value?.trim();
        
        if (!reason || reason.length < 10) {
          notificationStore.danger('Rejection reason must be at least 10 characters.');
          return;
        }

        try {
          const response = await apiClient.put(`/leaves/${leaveId}/reject`, { rejectionReason: reason });
          if (response?.success) {
            notificationStore.success('Time-off application rejected.');
            if (modal) {
              modal.style.display = 'none';
              modal.setAttribute('aria-hidden', 'true');
            }
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to reject leave request.');
          }
        } catch (err) {
          notificationStore.danger('Network error rejecting leave.');
        }
      };
      modalSubmit.addEventListener('click', handleConfirmReject);
      lifecycle.onCleanup(() => modalSubmit.removeEventListener('click', handleConfirmReject));
    }

    // 4. Cancel Reject Leave modal close triggers
    if (modalClose) {
      const handleCloseModal = () => {
        if (modal) {
          modal.style.display = 'none';
          modal.setAttribute('aria-hidden', 'true');
        }
      };
      modalClose.addEventListener('click', handleCloseModal);
      lifecycle.onCleanup(() => modalClose.removeEventListener('click', handleCloseModal));
    }

    // 5. Approve Cancellation request triggers
    container.querySelectorAll('.btn-approve-cancel').forEach(btn => {
      const handleApproveCancel = async () => {
        const leaveId = btn.getAttribute('data-id');
        try {
          const response = await apiClient.put(`/leaves/${leaveId}/approve-cancellation`, {});
          if (response?.success) {
            notificationStore.success('Cancellation request approved successfully.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to approve cancellation request.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to approve cancellation.');
        }
      };
      btn.addEventListener('click', handleApproveCancel);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleApproveCancel));
    });
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderRequests(container) {
    const gridContainer = container.querySelector('#requests-grid-container');
    const emptyTpl = container.querySelector('#leaves-empty-list-tpl');
    const leaveTpl = container.querySelector('#leave-request-card-tpl');
    const cancelTpl = container.querySelector('#cancellation-request-card-tpl');

    if (!gridContainer) return;

    gridContainer.replaceChildren();

    if (this.pendingLeaves.length === 0 && this.cancellationRequests.length === 0) {
      if (emptyTpl) {
        gridContainer.appendChild(emptyTpl.content.cloneNode(true));
      }
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    // Render leaves card list
    this.pendingLeaves.forEach(req => {
      if (!leaveTpl) return;
      const clone = leaveTpl.content.cloneNode(true);

      const nameEl = clone.querySelector('.employee-name-lbl');
      const badge = clone.querySelector('.leave-type-badge');
      const durationEl = clone.querySelector('.duration-val');
      const reasonEl = clone.querySelector('.reason-detail');
      const commentInput = clone.querySelector('.supervisor-comment-input');
      const approveBtn = clone.querySelector('.btn-approve-leave');
      const rejectBtn = clone.querySelector('.btn-reject-leave');
      const warningEl = clone.querySelector('.medical-cert-warning');

      if (nameEl) nameEl.textContent = req.employeeName;
      if (badge) {
        badge.textContent = req.leaveTypeCode;
      }
      if (durationEl) {
        durationEl.textContent = `${req.totalDays} day(s) (${req.startDate} to ${req.endDate})`;
      }
      if (reasonEl) {
        reasonEl.textContent = `"${req.reason || 'No reason specified'}"`;
      }
      if (commentInput) {
        commentInput.setAttribute('data-id', String(req.id));
      }

      const showWarning = !req.hasDocument && req.requiresDocument;
      if (warningEl && showWarning) {
        warningEl.classList.add('visible');
      }

      if (approveBtn) {
        approveBtn.setAttribute('data-id', String(req.id));
        if (showWarning) {
          approveBtn.classList.add('disabled');
        }
      }

      if (rejectBtn) {
        rejectBtn.setAttribute('data-id', String(req.id));
        if (req.isProtected) {
          rejectBtn.classList.add('disabled');
          rejectBtn.setAttribute('disabled', 'true');
        }
      }

      gridContainer.appendChild(clone);
    });

    // Render cancellations card list
    this.cancellationRequests.forEach(req => {
      if (!cancelTpl) return;
      const clone = cancelTpl.content.cloneNode(true);

      const nameEl = clone.querySelector('.employee-name-lbl');
      const badge = clone.querySelector('.leave-type-badge');
      const durationEl = clone.querySelector('.duration-val');
      const reasonEl = clone.querySelector('.reason-detail');
      const approveCancelBtn = clone.querySelector('.btn-approve-cancel');

      if (nameEl) nameEl.textContent = req.employeeName;
      if (badge) {
        badge.textContent = `Cancel: ${req.leaveTypeCode}`;
      }
      if (durationEl) {
        durationEl.textContent = `${req.totalDays} day(s) (${req.startDate} to ${req.endDate})`;
      }
      if (reasonEl) {
        reasonEl.textContent = `Reason: "${req.cancellationReason || 'No reason specified'}"`;
      }
      if (approveCancelBtn) {
        approveCancelBtn.setAttribute('data-id', String(req.id));
      }

      gridContainer.appendChild(clone);
    });

    if (window.lucide) window.lucide.createIcons();
  }

  _loadCss() {
    const cssId = 'store-employee-supervisor-leaves-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/supervisor-leaves/leaves.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreSupervisorLeaves };
