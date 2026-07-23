/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module - Shift Supervisor Sub-Role
 * File              : leaves.js
 * Path              : frontend/modules/store-employee/supervisor-leaves/leaves.js
 * Purpose           : Controller component for Shift Supervisor leave approvals hub UI with dual-routed employee leave request support.
 * Version           : 4.0.0
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { eventBus } from '../../../../core/eventBus.js';

const TEMPLATE_URL = 'modules/store-employee/pages/supervisor-leaves/leaves.html';

export default class StoreSupervisorLeaves {

  constructor() {
    this.user = authStore.getUser();
    this.pendingLeaves = [];
    this.cancellationRequests = [];
  }

  async mount(container, lifecycle) {
    logger.info('StoreSupervisorLeaves', 'Mounting supervisor leave approvals...');
    
    this._loadCss();
    await this._loadTemplate(container);
    this.renderLoading(container);
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);

    // Live EventBus refresh
    const handleRefresh = () => this.loadAndRender(container, lifecycle);
    eventBus.on('leave:submitted', handleRefresh);
    lifecycle.onCleanup(() => eventBus.off('leave:submitted', handleRefresh));
  }

  _loadCss() {
    if (!document.querySelector('link[href*="leaves.css"]')) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/supervisor-leaves/leaves.css';
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
      const pendingRes = await safeGet('/leaves/pending');
      if (pendingRes?.success && pendingRes.data) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      } else {
        this.pendingLeaves = [];
        this.cancellationRequests = [];
      }

      // Merge shared dual-routed pending leaves for Shift Supervisors
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
    } catch (err) {
      logger.error('StoreSupervisorLeaves', 'Failed to load pending leaves', err);
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

  renderLoading(container) {
    const gridContainer = container.querySelector('#requests-grid-container');
    const loadingTpl = container.querySelector('#leaves-loading-tpl');

    if (gridContainer && loadingTpl) {
      gridContainer.replaceChildren(loadingTpl.content.cloneNode(true));
      if (window.lucide) window.lucide.createIcons();
    }
  }

  render(container) {
    const countEl = container.querySelector('#lbl-pending-count-stat');
    const totalCount = this.pendingLeaves.length + this.cancellationRequests.length;
    if (countEl) countEl.textContent = totalCount;

    this._renderRequests(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const modal = container.querySelector('#reject-modal');
    const modalSubmit = container.querySelector('#reject-modal-submit');
    const modalClose = container.querySelector('#reject-modal-close');
    const reasonInput = container.querySelector('#reject-reason-input');

    container.querySelectorAll('.btn-approve-leave').forEach(btn => {
      const handleApprove = async () => {
        const leaveId = btn.getAttribute('data-id');
        const commentInput = container.querySelector(`.supervisor-comment-input-new[data-id="${leaveId}"]`);
        const comment = commentInput?.value?.trim() || '';

        try {
          await apiClient.put(`/leaves/${leaveId}/approve`, { comment });
        } catch (e) {
          logger.warn('StoreSupervisorLeaves', 'API approve call warning', e);
        }

        const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
        const idx = sharedLeaves.findIndex(s => String(s.id) === String(leaveId));
        if (idx !== -1) {
          sharedLeaves.splice(idx, 1);
          localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
        }

        notificationStore.success('Leave request approved.');
        eventBus.emit('leave:updated', { leaveId, decision: 'APPROVED' });
        await this.loadAndRender(container, lifecycle);
      };
      btn.addEventListener('click', handleApprove);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleApprove));
    });

    container.querySelectorAll('.btn-reject-leave').forEach(btn => {
      const handleOpenReject = () => {
        const leaveId = btn.getAttribute('data-id');
        if (modalSubmit) modalSubmit.dataset.leaveId = leaveId;
        if (reasonInput) reasonInput.value = '';
        if (modal) {
          modal.style.display = 'flex';
          modal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenReject);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenReject));
    });

    if (modalClose) {
      const handleClose = () => {
        if (modal) {
          modal.style.display = 'none';
          modal.setAttribute('aria-hidden', 'true');
        }
      };
      modalClose.addEventListener('click', handleClose);
      lifecycle.onCleanup(() => modalClose.removeEventListener('click', handleClose));
    }

    if (modalSubmit) {
      const handleConfirmReject = async () => {
        const leaveId = modalSubmit.dataset.leaveId;
        const reason = reasonInput?.value?.trim();

        if (!reason) {
          notificationStore.danger('Please provide a rejection reason.');
          return;
        }

        try {
          await apiClient.put(`/leaves/${leaveId}/reject`, { rejectionReason: reason });
        } catch (e) {
          logger.warn('StoreSupervisorLeaves', 'API reject call warning', e);
        }

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
        eventBus.emit('leave:updated', { leaveId, decision: 'REJECTED' });
        await this.loadAndRender(container, lifecycle);
      };
      modalSubmit.addEventListener('click', handleConfirmReject);
      lifecycle.onCleanup(() => modalSubmit.removeEventListener('click', handleConfirmReject));
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderRequests(container) {
    const gridContainer = container.querySelector('#requests-grid-container');
    const emptyTpl = container.querySelector('#leaves-empty-list-tpl');
    const leaveTpl = container.querySelector('#leave-request-card-tpl');

    if (!gridContainer) return;

    gridContainer.replaceChildren();

    if (this.pendingLeaves.length === 0 && this.cancellationRequests.length === 0) {
      if (emptyTpl) {
        gridContainer.appendChild(emptyTpl.content.cloneNode(true));
      }
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    this.pendingLeaves.forEach(req => {
      if (!leaveTpl) return;
      const clone = leaveTpl.content.cloneNode(true);

      const nameEl = clone.querySelector('.employee-email-lbl');
      const avatarText = clone.querySelector('.avatar-text-new');
      const avatarCircle = clone.querySelector('.avatar-circle-new');
      const badge = clone.querySelector('.leave-type-badge-new');
      
      const durationDays = clone.querySelector('.duration-days-val');
      const durationDates = clone.querySelector('.duration-dates-val');
      const requestedDate = clone.querySelector('.requested-date-val');
      const requestedTime = clone.querySelector('.requested-time-val');
      const reasonEl = clone.querySelector('.reason-text-val');

      const commentInput = clone.querySelector('.supervisor-comment-input-new');
      const approveBtn = clone.querySelector('.btn-approve-leave');
      const rejectBtn = clone.querySelector('.btn-reject-leave');
      const warningEl = clone.querySelector('.medical-cert-warning');

      if (nameEl) nameEl.textContent = req.employeeName;
      if (avatarText) {
        avatarText.textContent = (req.employeeName || 'SE').slice(0, 2).toUpperCase();
      }
      if (avatarCircle) {
        const colors = ['#6d28d9', '#1d4ed8', '#047857', '#a16207', '#b91c1c'];
        const hash = (req.employeeName || '').split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
        avatarCircle.style.backgroundColor = colors[hash % colors.length];
      }

      if (badge) {
        const typeNames = {
          'ANNUAL': 'ANNUAL PAID LEAVE',
          'SICK': 'MEDICAL SICK LEAVE',
          'PERSONAL': 'PERSONAL UNPAID LEAVE',
          'MATERNITY': 'MATERNITY LEAVE',
          'PATERNITY': 'PATERNITY LEAVE',
          'BEREAVEMENT': 'BEREAVEMENT LEAVE'
        };
        badge.textContent = typeNames[req.leaveTypeCode] || (req.leaveTypeCode + ' LEAVE').toUpperCase();
      }

      if (durationDays) durationDays.textContent = `${req.totalDays} day(s)`;
      if (durationDates) durationDates.textContent = `${req.startDate} – ${req.endDate}`;

      let dateStr = 'Jul 21, 2026';
      let timeStr = '09:15 AM';
      if (req.createdAt) {
        const d = new Date(req.createdAt);
        if (!isNaN(d.getTime())) {
          dateStr = d.toLocaleDateString('en-US', { month: 'short', day: '2-digit', year: 'numeric' });
          timeStr = d.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
        }
      }
      if (requestedDate) requestedDate.textContent = dateStr;
      if (requestedTime) requestedTime.textContent = timeStr;

      if (reasonEl) reasonEl.textContent = `"${req.reason || 'No reason specified'}"`;
      if (commentInput) commentInput.setAttribute('data-id', String(req.id));
      if (approveBtn) approveBtn.setAttribute('data-id', String(req.id));
      if (rejectBtn) rejectBtn.setAttribute('data-id', String(req.id));

      const missingDoc = req.requiresDocument && !req.hasDocument;
      if (warningEl) warningEl.style.display = missingDoc ? 'flex' : 'none';

      gridContainer.appendChild(clone);
    });

    this.cancellationRequests.forEach(req => {
      const cancelTpl = container.querySelector('#cancellation-request-card-tpl');
      if (!cancelTpl) return;
      const clone = cancelTpl.content.cloneNode(true);

      const nameEl = clone.querySelector('.employee-email-lbl');
      const avatarText = clone.querySelector('.avatar-text-new');
      const avatarCircle = clone.querySelector('.avatar-circle-new');
      
      const durationDays = clone.querySelector('.duration-days-val');
      const durationDates = clone.querySelector('.duration-dates-val');
      const requestedDate = clone.querySelector('.requested-date-val');
      const requestedTime = clone.querySelector('.requested-time-val');
      const reasonEl = clone.querySelector('.reason-text-val');
      const approveCancelBtn = clone.querySelector('.btn-approve-cancel');

      if (nameEl) nameEl.textContent = req.employeeName;
      if (avatarText) {
        avatarText.textContent = (req.employeeName || 'SE').slice(0, 2).toUpperCase();
      }
      if (avatarCircle) {
        const colors = ['#6d28d9', '#1d4ed8', '#047857', '#a16207', '#b91c1c'];
        const hash = (req.employeeName || '').split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
        avatarCircle.style.backgroundColor = colors[hash % colors.length];
      }

      if (durationDays) durationDays.textContent = `${req.totalDays} day(s)`;
      if (durationDates) durationDates.textContent = `${req.startDate} – ${req.endDate}`;

      let dateStr = 'Jul 21, 2026';
      let timeStr = '09:15 AM';
      if (req.createdAt) {
        const d = new Date(req.createdAt);
        if (!isNaN(d.getTime())) {
          dateStr = d.toLocaleDateString('en-US', { month: 'short', day: '2-digit', year: 'numeric' });
          timeStr = d.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
        }
      }
      if (requestedDate) requestedDate.textContent = dateStr;
      if (requestedTime) requestedTime.textContent = timeStr;

      if (reasonEl) reasonEl.textContent = `"${req.reason || 'Requested leave restoration.'}"`;

      if (approveCancelBtn) {
        approveCancelBtn.setAttribute('data-id', String(req.id));
        const handleApproveCancel = async () => {
          try {
            await apiClient.put(`/leaves/${req.id}/approve-cancellation`);
            notificationStore.success('Cancellation request approved.');
          } catch (e) {
            logger.warn('StoreSupervisorLeaves', 'Approve cancellation error', e);
          }
          await this.loadAndRender(container, lifecycle);
        };
        approveCancelBtn.addEventListener('click', handleApproveCancel);
        lifecycle.onCleanup(() => approveCancelBtn.removeEventListener('click', handleApproveCancel));
      }

      gridContainer.appendChild(clone);
    });

    if (window.lucide) window.lucide.createIcons();
  }
}
