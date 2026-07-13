/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module - Shift Supervisor Sub-Role
 * File              : dashboard.js
 * Path              : frontend/modules/store-employee/supervisor-dashboard/dashboard.js
 * Purpose           : Controller component for Shift Supervisor Dashboard Page UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/supervisor-dashboard/dashboard.html
 * Related CSS       : frontend/modules/store-employee/supervisor-dashboard/dashboard.css
 * Related APIs      : GET /api/v1/auth/me
 *                     GET /leaves/pending
 *                     PUT /leaves/:id/approve
 *                     PUT /leaves/:id/reject
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in dashboard.html — this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor → mount → loadTemplate → loadData → render → bindEvents → destroy
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

/** Path to the dashboard HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/supervisor-dashboard/dashboard.html';

export default class ShiftSupervisorDashboard {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `supervisor_dashboard_state_${this.user?.username || 'rohan'}`;
    this.pendingLeaves = [];
    this.cancellationRequests = [];
    
    this.loadState();
  }

  loadState() {
    const cachedState = localStorage.getItem(this.stateKey);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
      } catch (err) {
        logger.error('ShiftSupervisorDashboard', 'Error parsing cached state, resetting', err);
        this.initDefaultState();
      }
    } else {
      this.initDefaultState();
    }
  }

  initDefaultState() {
    this.state = {
      name: this.profile.name || 'Rohan Sharma',
      id: 'SUP40212',
      store: 'Green Park Café, City Center',
      shiftName: 'Morning Roster',
      shiftHours: '08:00 AM - 04:00 PM',
      fulfillmentSpeed: 2.4,
      totalOrders: 142,
      netSales: 28400,
      activeTeam: [
        { name: 'Neha Sharma', role: 'Espresso Bar Lead', checkIn: '08:02 AM', progress: 75, status: 'On Duty' },
        { name: 'Amit Singh', role: 'Customer Service & Till', checkIn: '07:58 AM', progress: 50, status: 'On Duty' },
        { name: 'Siddharth Mishra', role: 'Cleaning & Prep', checkIn: '08:05 AM', progress: 100, status: 'On Duty' }
      ]
    };
    this.saveState();
  }

  saveState() {
    localStorage.setItem(this.stateKey, JSON.stringify(this.state));
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the shift supervisor dashboard component page context.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('ShiftSupervisorDashboard', 'Mounting Shift Supervisor Dashboard...');
    this.loadState();

    // Dynamically load supervisor CSS
    this._loadCss();

    // 1. Inject skeleton template with loading view
    this._renderLoading(container);

    // 2. Fetch live session information to sync supervisor profile
    await this._loadSession();

    // 3. Inject full layout
    await this._loadTemplate(container);

    // 4. Fetch dynamic dashboard approvals from backend
    await this._loadData();

    // 5. Render details
    this._render(container);

    // 6. Bind listeners
    this._bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadTemplate
  // ---------------------------------------------------------------------------

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadData
  // ---------------------------------------------------------------------------

  async _loadData() {
    try {
      const pendingRes = await apiClient.get('/leaves/pending');
      if (pendingRes?.success) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      } else {
        logger.error('ShiftSupervisorDashboard', 'Failed to fetch pending leaves');
      }
    } catch (err) {
      logger.error('ShiftSupervisorDashboard', 'Network error fetching pending leaves', err);
    }
  }

  async _loadSession() {
    try {
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes?.success) {
        this.profile = { ...this.profile, ...meRes.data };
        this.state.name = this.profile.name || this.state.name;
        this.state.store = this.profile.store || this.state.store;
        this.state.id = 'SUP' + (this.profile.id || '40212');
        this.saveState();
      }
    } catch (err) {
      logger.error('ShiftSupervisorDashboard', 'Failed to retrieve session info', err);
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  _render(container) {
    // Welcome Banner details
    const welcomeName = container.querySelector('#welcome-name');
    const storeName = container.querySelector('#meta-store-name');
    const shiftDetails = container.querySelector('#meta-shift-details');

    if (welcomeName) welcomeName.textContent = this.state.name;
    if (storeName) storeName.textContent = this.state.store;
    if (shiftDetails) {
      shiftDetails.textContent = `${this.state.shiftName} (${this.state.shiftHours})`;
    }

    // KPIs Details
    const activeBaristasCount = container.querySelector('#kpi-active-baristas-count');
    const shiftOrders = container.querySelector('#kpi-shift-orders');
    const prepSpeed = container.querySelector('#kpi-prep-speed');
    const pendingLeavesCount = container.querySelector('#kpi-pending-leaves');

    if (activeBaristasCount) activeBaristasCount.textContent = `${this.state.activeTeam.length} Baristas`;
    if (shiftOrders) shiftOrders.textContent = `${this.state.totalOrders} Orders`;
    if (prepSpeed) prepSpeed.textContent = `${this.state.fulfillmentSpeed} min`;
    if (pendingLeavesCount) pendingLeavesCount.textContent = `${this.pendingLeaves.length} Pending`;

    // Render active team members monitor
    this._renderTeamMonitor(container);

    // Render leave approvals hub list
    this._renderApprovalsList(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  _bindEvents(container, lifecycle) {
    // 1. Ping Barista triggers
    const pingBtns = container.querySelectorAll('.btn-ping-barista');
    pingBtns.forEach(btn => {
      const name = btn.getAttribute('data-name');
      const handlePing = () => {
        notificationStore.info(`Roster notification dispatched to ${name}.`);
      };
      btn.addEventListener('click', handlePing);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handlePing));
    });

    // 2. Approve Leave (Real API integration)
    const approveBtns = container.querySelectorAll('.btn-approve-leave');
    approveBtns.forEach(btn => {
      const leaveId = btn.getAttribute('data-id');
      const commentInput = container.querySelector(`.supervisor-comment-input[data-id="${leaveId}"]`);
      
      const handleApprove = async () => {
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

    // 3. Reject Leave Open Modal
    const rejectBtns = container.querySelectorAll('.btn-reject-leave');
    const modal = container.querySelector('#reject-modal');
    const modalSubmit = container.querySelector('#reject-modal-submit');
    const modalClose = container.querySelector('#reject-modal-close');
    const rejectReasonInput = container.querySelector('#reject-reason-input');

    rejectBtns.forEach(btn => {
      const leaveId = btn.getAttribute('data-id');
      const handleOpenReject = () => {
        if (modalSubmit) modalSubmit.dataset.leaveId = leaveId;
        if (rejectReasonInput) rejectReasonInput.value = '';
        if (modal) {
          modal.style.display = 'flex';
          modal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenReject);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenReject));
    });

    // Reject Modal confirm submit
    if (modalSubmit) {
      const handleConfirmReject = async () => {
        const leaveId = modalSubmit.dataset.leaveId;
        const reason = rejectReasonInput?.value?.trim();
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
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    logger.debug('ShiftSupervisorDashboard', 'Shift supervisor control hub unmounted.');
  }

  unmount() {
    this.destroy();
  }

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): loadAndRender
  // ---------------------------------------------------------------------------

  async loadAndRender(container, lifecycle) {
    await this._loadData();
    this._render(container);
    this._bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE LOADING & RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderLoading(container) {
    container.innerHTML = `
      <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
        <i data-lucide="loader-2" class="animate-spin" style="width:32px;height:32px;color:var(--accent-primary);"></i>
        <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading supervisor control hub...</span>
      </div>`;
    if (window.lucide) window.lucide.createIcons();
  }

  _renderTeamMonitor(container) {
    const activeTeamList = container.querySelector('#active-team-list');
    const teamRowTpl = container.querySelector('#team-row-tpl');

    if (!activeTeamList || !teamRowTpl) return;

    activeTeamList.replaceChildren();

    this.state.activeTeam.forEach(barista => {
      const clone = teamRowTpl.content.cloneNode(true);

      const nameEl = clone.querySelector('.barista-name');
      const stationInfoEl = clone.querySelector('.barista-station-info');
      const progressFillEl = clone.querySelector('.progress-fill');
      const progressPercentageEl = clone.querySelector('.progress-percentage-text');
      const pingBtn = clone.querySelector('.btn-ping-barista');

      if (nameEl) nameEl.textContent = barista.name;
      if (stationInfoEl) {
        stationInfoEl.innerHTML = `Station: ${barista.role} &nbsp;·&nbsp; Punch In: ${barista.checkIn}`;
      }
      if (progressFillEl) progressFillEl.style.width = `${barista.progress}%`;
      if (progressPercentageEl) progressPercentageEl.textContent = `${barista.progress}%`;
      if (pingBtn) pingBtn.setAttribute('data-name', barista.name);

      activeTeamList.appendChild(clone);
    });

    if (window.lucide) window.lucide.createIcons();
  }

  _renderApprovalsList(container) {
    const pendingApprovalsList = container.querySelector('#pending-approvals-list');
    const approvalCardTpl = container.querySelector('#leave-approval-tpl');

    if (!pendingApprovalsList || !approvalCardTpl) return;

    pendingApprovalsList.replaceChildren();

    if (!this.pendingLeaves || this.pendingLeaves.length === 0) {
      const emptyDiv = document.createElement('div');
      emptyDiv.className = 'empty-approvals-banner';
      emptyDiv.innerHTML = `
        <i data-lucide="check-circle" aria-hidden="true"></i>
        <span class="empty-approvals-title">All requests signed off</span>
        <span class="empty-approvals-subtitle">No pending barista time-off applications to process.</span>
      `;
      pendingApprovalsList.appendChild(emptyDiv);
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    this.pendingLeaves.forEach(req => {
      const clone = approvalCardTpl.content.cloneNode(true);

      const empNameEl = clone.querySelector('.employee-name');
      const typeCodeEl = clone.querySelector('.leave-type-code-badge');
      const daysValEl = clone.querySelector('.days-val');
      const datesValEl = clone.querySelector('.dates-val');
      const reasonEl = clone.querySelector('.reason-quote');
      const docWarningEl = clone.querySelector('.document-warning-alert');
      const commentInput = clone.querySelector('.supervisor-comment-input');
      const approveBtn = clone.querySelector('.btn-approve-leave');
      const rejectBtn = clone.querySelector('.btn-reject-leave');

      if (empNameEl) empNameEl.textContent = req.employeeName;
      if (typeCodeEl) typeCodeEl.textContent = req.leaveTypeCode;
      if (daysValEl) daysValEl.textContent = String(req.totalDays);
      if (datesValEl) datesValEl.textContent = `${req.startDate} to ${req.endDate}`;
      if (reasonEl) reasonEl.textContent = `"${req.reason || 'No reason specified'}"`;

      const requiresDoc = !req.hasDocument && req.requiresDocument;
      if (docWarningEl) {
        docWarningEl.style.display = requiresDoc ? 'block' : 'none';
      }

      if (commentInput) {
        commentInput.setAttribute('data-id', String(req.id));
      }

      if (approveBtn) {
        approveBtn.setAttribute('data-id', String(req.id));
        if (requiresDoc) {
          approveBtn.disabled = true;
          approveBtn.style.opacity = '0.4';
          approveBtn.style.pointerEvents = 'none';
        }
      }

      if (rejectBtn) {
        rejectBtn.setAttribute('data-id', String(req.id));
        if (req.isProtected) {
          rejectBtn.disabled = true;
          rejectBtn.style.opacity = '0.4';
          rejectBtn.style.pointerEvents = 'none';
        }
      }

      pendingApprovalsList.appendChild(clone);
    });

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'shift-supervisor-dashboard-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/supervisor-dashboard/dashboard.css';
      document.head.appendChild(link);
    }
  }
}
export { ShiftSupervisorDashboard };
