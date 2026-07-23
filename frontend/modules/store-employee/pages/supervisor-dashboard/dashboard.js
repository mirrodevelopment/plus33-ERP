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
    this.awayPermissions = []; // pending away pass requests
    this.ongoingAwayPermissions = []; // active/ongoing away pass requests
    this.overtimeRequests = []; // pending overtime requests
    
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
      const [pendingRes, awayRes, ongoingAwayRes, otRes] = await Promise.all([
        apiClient.get('/leaves/pending').catch(() => null),
        apiClient.get('/api/v1/away-permission/pending').catch(() => null),
        apiClient.get('/api/v1/away-permission/ongoing').catch(() => null),
        apiClient.get('/api/v1/overtime-requests/pending').catch(() => null)
      ]);
      if (pendingRes?.success && pendingRes.data) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      } else {
        this.pendingLeaves = [];
        this.cancellationRequests = [];
      }

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
      this.awayPermissions = awayRes?.success ? (awayRes.data || []) : [];
      this.ongoingAwayPermissions = ongoingAwayRes?.success ? (ongoingAwayRes.data || []) : [];
      this.overtimeRequests = otRes?.success ? (otRes.data || []) : [];
    } catch (err) {
      logger.error('ShiftSupervisorDashboard', 'Network error fetching dashboard data', err);
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

    // Render away permission requests
    this._renderAwayPermissions(container);

    // Render overtime requests
    this._renderOvertimeRequests(container);
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

            // Clear from shared_pending_leaves
            const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
            const idx = sharedLeaves.findIndex(s => String(s.id) === String(leaveId));
            if (idx !== -1) {
              sharedLeaves.splice(idx, 1);
              localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
            }

            // Update status to APPROVED in user_leaves_* localStorage arrays
            for (let i = 0; i < localStorage.length; i++) {
              const key = localStorage.key(i);
              if (key && key.startsWith('user_leaves_')) {
                const list = JSON.parse(localStorage.getItem(key) || '[]');
                const foundIdx = list.findIndex(l => String(l.id) === String(leaveId));
                if (foundIdx !== -1) {
                  list[foundIdx].status = 'APPROVED';
                  localStorage.setItem(key, JSON.stringify(list));
                }
              }
            }

            eventBus.emit('leave:updated', { leaveId, decision: 'APPROVED' });
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

            // Clear from shared_pending_leaves
            const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
            const idx = sharedLeaves.findIndex(s => String(s.id) === String(leaveId));
            if (idx !== -1) {
              sharedLeaves.splice(idx, 1);
              localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
            }

            // Update status to REJECTED in user_leaves_* localStorage arrays
            for (let i = 0; i < localStorage.length; i++) {
              const key = localStorage.key(i);
              if (key && key.startsWith('user_leaves_')) {
                const list = JSON.parse(localStorage.getItem(key) || '[]');
                const foundIdx = list.findIndex(l => String(l.id) === String(leaveId));
                if (foundIdx !== -1) {
                  list[foundIdx].status = 'REJECTED';
                  list[foundIdx].rejectedBySupervisor = true;
                  list[foundIdx].rejectionReason = reason;
                  localStorage.setItem(key, JSON.stringify(list));
                }
              }
            }

            eventBus.emit('leave:updated', { leaveId, decision: 'REJECTED' });

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

    // 4. Away Permission Buttons
    this._bindAwayPermissionButtons(container, lifecycle);

    // 5. Overtime Request Buttons
    this._bindOvertimeRequestButtons(container, lifecycle);
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

  // ---------------------------------------------------------------------------
  // AWAY PERMISSION RENDERING
  // ---------------------------------------------------------------------------

  _renderAwayPermissions(container) {
    const section = container.querySelector('#sup-away-permissions-section');
    if (!section) return;

    const count = this.awayPermissions.length;
    const badge = section.querySelector('#sup-away-badge');
    if (badge) {
      badge.textContent = count;
      badge.style.display = count > 0 ? 'inline-flex' : 'none';
    }

    const list = section.querySelector('#sup-away-permissions-list');
    if (list) {
      if (count === 0) {
        list.innerHTML = `
          <div class="empty-approvals-banner">
            <i data-lucide="map-pin" aria-hidden="true"></i>
            <span class="empty-approvals-title">All employees on site</span>
            <span class="empty-approvals-subtitle">No pending away pass requests from your team.</span>
          </div>`;
      } else {
        list.innerHTML = this.awayPermissions.map(req => {
          const statusColor = req.status === 'EXTENSION_REQUESTED' ? '#f59e0b' : 'var(--accent-primary)';
          const tag = req.status === 'EXTENSION_REQUESTED' ? '🔁 Extension Requested' : '⏸ Away Pass Requested';
          const since = req.requestedAt ? new Date(req.requestedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--';
          return `
            <div style="
              background:rgba(255,255,255,0.03);
              border:1px solid var(--border-color);
              border-left:3px solid ${statusColor};
              border-radius:var(--radius-md,8px);
              padding:12px 16px;
              display:flex; flex-direction:column; gap:8px;
              margin-bottom:8px;
            ">
              <div style="display:flex; justify-content:space-between; align-items:center;">
                <span style="font-weight:700; font-size:0.82rem; color:var(--text-primary);">${req.employeeName || 'Employee'}</span>
                <span style="font-size:0.68rem; color:${statusColor}; font-weight:600;">${tag}</span>
              </div>
              <div style="font-size:0.74rem; color:var(--text-secondary); display:flex; flex-direction:column; gap:2px;">
                ${req.reason ? `<div><strong>Original Reason:</strong> <em>${req.reason}</em></div>` : ''}
                ${req.status === 'EXTENSION_REQUESTED' && req.extensionReason ? `<div><strong>Extension Request:</strong> <em style="color:var(--accent-warning, #ff9800);">${req.extensionReason}</em></div>` : ''}
              </div>
              <div style="font-size:0.68rem; color:var(--text-muted);">Requested at ${since}</div>
              <div style="display:flex; gap:8px; margin-top:4px;">
                <button type="button" class="btn btn-primary btn-away-approve" data-away-id="${req.id}"
                  style="flex:1; padding:6px; font-size:0.74rem;">✓ Approve Pass</button>
                <button type="button" class="btn btn-secondary btn-away-deny" data-away-id="${req.id}"
                  style="flex:1; padding:6px; font-size:0.74rem;">✕ Deny</button>
              </div>
            </div>
          `;
        }).join('');
      }
    }

    const ongoingSection = section.querySelector('#sup-ongoing-away-section');
    const ongoingList = section.querySelector('#sup-ongoing-away-list');
    if (ongoingSection && ongoingList) {
      const ongoingCount = this.ongoingAwayPermissions.length;
      if (ongoingCount > 0) {
        ongoingSection.style.display = 'block';
        ongoingList.innerHTML = this.ongoingAwayPermissions.map(req => {
          const until = req.approvedUntil ? new Date(req.approvedUntil).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--:--';
          const duration = req.approvedDurationMins || 0;
          return `
            <div style="
              background:rgba(255,255,255,0.03);
              border:1px solid var(--border-color);
              border-left:3px solid var(--status-success, #4caf50);
              border-radius:var(--radius-md,8px);
              padding:12px 16px;
              display:flex; flex-direction:column; gap:6px;
              margin-bottom:8px;
            ">
              <div style="display:flex; justify-content:space-between; align-items:center;">
                <span style="font-weight:700; font-size:0.82rem; color:var(--text-primary);">${req.employeeName || 'Employee'}</span>
                <span style="font-size:0.65rem; background:rgba(76,175,80,0.12); color:#4caf50; border:1px solid rgba(76,175,80,0.2); padding:2px 6px; border-radius:4px; font-weight:600;">ACTIVE</span>
              </div>
              <div style="font-size:0.74rem; color:var(--text-secondary); display:flex; flex-direction:column; gap:2px;">
                <div><strong>Approved:</strong> ${duration} mins (until ${until})</div>
                ${req.reason ? `<div><strong>Reason:</strong> <em>${req.reason}</em></div>` : ''}
              </div>
            </div>
          `;
        }).join('');
      } else {
        ongoingSection.style.display = 'none';
        ongoingList.innerHTML = '';
      }
    }

    if (window.lucide) window.lucide.createIcons();
  }

  _bindAwayPermissionButtons(container, lifecycle) {
    container.querySelectorAll('.btn-away-approve').forEach(btn => {
      const handler = () => this._showSupervisorAwayApproveCard(container, lifecycle, btn.getAttribute('data-away-id'));
      btn.addEventListener('click', handler);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });

    container.querySelectorAll('.btn-away-deny').forEach(btn => {
      const handler = async () => {
        btn.disabled = true; btn.textContent = 'Denying…';
        try {
          const res = await apiClient.put(`/api/v1/away-permission/${btn.getAttribute('data-away-id')}/deny`, {});
          if (res?.success) {
            notificationStore.success('Away pass denied.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to deny.');
            btn.disabled = false; btn.textContent = '✕ Deny';
          }
        } catch (e) {
          notificationStore.danger('Error: ' + e.message);
          btn.disabled = false; btn.textContent = '✕ Deny';
        }
      };
      btn.addEventListener('click', handler);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });
  }

  _showSupervisorAwayApproveCard(container, lifecycle, awayId) {
    let overlay = document.getElementById('sup-away-approve-overlay');
    if (!overlay) {
      overlay = document.createElement('div');
      overlay.id = 'sup-away-approve-overlay';
      overlay.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.65);backdrop-filter:blur(4px);display:flex;align-items:center;justify-content:center;z-index:9999;';
      document.body.appendChild(overlay);
    }
    overlay.innerHTML = `
      <div style="background:var(--bg-card,#1e1e2e);border:1px solid var(--border-color);border-radius:14px;padding:28px 24px;width:340px;max-width:95vw;display:flex;flex-direction:column;gap:16px;box-shadow:0 8px 40px rgba(0,0,0,0.5);">
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <h3 style="margin:0;font-size:0.95rem;font-weight:700;color:var(--text-primary);">⏱ Approve Away Pass</h3>
          <button id="btn-sup-away-close" type="button" style="background:none;border:none;color:var(--text-muted);cursor:pointer;font-size:1.2rem;">✕</button>
        </div>
        <p style="margin:0;font-size:0.8rem;color:var(--text-secondary);line-height:1.5;">Set how many minutes the employee is allowed away. A <strong>10-minute grace buffer</strong> is added automatically.</p>
        <div style="display:flex;flex-direction:column;gap:6px;">
          <label style="font-size:0.75rem;color:var(--text-muted);font-weight:600;">Duration (minutes)</label>
          <input id="sup-away-duration" type="number" min="5" max="240" value="30" style="background:rgba(255,255,255,0.05);border:1px solid var(--border-color);border-radius:8px;color:var(--text-primary);padding:8px 12px;font-size:0.85rem;width:100%;box-sizing:border-box;">
          <span id="sup-away-hint" style="font-size:0.68rem;color:var(--text-muted);">Employee will have 30 min + 10 min grace = 40 min total.</span>
        </div>
        <div style="display:flex;gap:10px;">
          <button id="btn-sup-away-confirm" type="button" class="btn btn-primary" style="flex:1;padding:9px;">✓ Confirm</button>
          <button id="btn-sup-away-cancel" type="button" class="btn btn-secondary" style="flex:1;padding:9px;">Cancel</button>
        </div>
      </div>`;
    overlay.style.display = 'flex';

    const close = () => { overlay.style.display = 'none'; };
    const dInput = overlay.querySelector('#sup-away-duration');
    const hint = overlay.querySelector('#sup-away-hint');
    dInput?.addEventListener('input', () => {
      const d = parseInt(dInput.value || '30');
      if (hint) hint.textContent = `Employee will have ${d} min + 10 min grace = ${d + 10} min total.`;
    });
    overlay.querySelector('#btn-sup-away-close')?.addEventListener('click', close);
    overlay.querySelector('#btn-sup-away-cancel')?.addEventListener('click', close);
    overlay.addEventListener('click', e => { if (e.target === overlay) close(); });

    const confirmBtn = overlay.querySelector('#btn-sup-away-confirm');
    confirmBtn?.addEventListener('click', async () => {
      const dMins = parseInt(dInput?.value || '30');
      if (!dMins || dMins < 1) { notificationStore.danger('Please enter a valid duration.'); return; }
      confirmBtn.disabled = true; confirmBtn.textContent = 'Approving…';
      try {
        const res = await apiClient.put(`/api/v1/away-permission/${awayId}/approve`, { durationMins: dMins });
        if (res?.success) {
          close();
          notificationStore.success(`Away pass approved for ${dMins} minutes (+10 min grace).`);
          await this.loadAndRender(container, lifecycle);
        } else {
          notificationStore.danger(res?.message || 'Failed to approve.');
          confirmBtn.disabled = false; confirmBtn.textContent = '✓ Confirm';
        }
      } catch (e) {
        notificationStore.danger('Error: ' + e.message);
        confirmBtn.disabled = false; confirmBtn.textContent = '✓ Confirm';
      }
    });
  }

  // ---------------------------------------------------------------------------
  // OVERTIME REQUEST RENDERING & ACTIONS
  // ---------------------------------------------------------------------------

  _renderOvertimeRequests(container) {
    const section = container.querySelector('#sup-overtime-requests-section');
    if (!section) return;

    const count = this.overtimeRequests.length;
    const badge = section.querySelector('#sup-overtime-badge');
    if (badge) {
      badge.textContent = count;
      badge.style.display = count > 0 ? 'inline-flex' : 'none';
    }

    const list = section.querySelector('#sup-overtime-requests-list');
    if (!list) return;

    if (count === 0) {
      list.innerHTML = `
        <div class="empty-approvals-banner">
          <i data-lucide="clock" aria-hidden="true"></i>
          <span class="empty-approvals-title">No Overtime Requests</span>
          <span class="empty-approvals-subtitle">No baristas have requested overtime shifts today.</span>
        </div>`;
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    list.innerHTML = this.overtimeRequests.map(req => {
      const since = req.requestedDateDisplay || req.requestedDate || '';
      return `
        <div style="
          background:rgba(255,255,255,0.03);
          border:1px solid var(--border-color);
          border-left:3px solid var(--accent-primary);
          border-radius:var(--radius-md,8px);
          padding:12px 16px;
          display:flex; flex-direction:column; gap:8px;
          margin-bottom:8px;
        ">
          <div style="display:flex; justify-content:space-between; align-items:center;">
            <span style="font-weight:700; font-size:0.82rem; color:var(--text-primary);">${req.employeeName || 'Employee'}</span>
            <span style="font-size:0.68rem; color:var(--accent-primary); font-weight:600;">⏳ Overtime Requested</span>
          </div>
          <div style="font-size:0.74rem; color:var(--text-secondary);">
            Requested date: <strong>${since}</strong> &nbsp;·&nbsp; Shift: <strong>${req.shiftName || ''}</strong>
          </div>
          <div style="font-size:0.74rem; color:var(--text-secondary);">
            ${req.reason ? `Reason: <em>${req.reason}</em>` : '<span style="color:var(--text-muted);font-style:italic;">No reason provided</span>'}
          </div>
          <div style="display:flex; gap:8px; margin-top:4px;">
            <button type="button" class="btn btn-primary btn-ot-approve" data-ot-id="${req.id}"
              style="flex:1; padding:6px; font-size:0.74rem;">✓ Approve Request</button>
            <button type="button" class="btn btn-secondary btn-ot-deny" data-ot-id="${req.id}"
              style="flex:1; padding:6px; font-size:0.74rem;">✕ Deny</button>
          </div>
        </div>
      `;
    }).join('');
    if (window.lucide) window.lucide.createIcons();
  }

  _bindOvertimeRequestButtons(container, lifecycle) {
    container.querySelectorAll('.btn-ot-approve').forEach(btn => {
      const handler = async () => {
        btn.disabled = true; btn.textContent = 'Approving…';
        try {
          const res = await apiClient.put(`/api/v1/overtime-requests/${btn.getAttribute('data-ot-id')}/approve`, {});
          if (res?.success) {
            notificationStore.success('Overtime shift approved and roster updated.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to approve request.');
            btn.disabled = false; btn.textContent = '✓ Approve Request';
          }
        } catch (e) {
          notificationStore.danger('Error: ' + e.message);
          btn.disabled = false; btn.textContent = '✓ Approve Request';
        }
      };
      btn.addEventListener('click', handler);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });

    container.querySelectorAll('.btn-ot-deny').forEach(btn => {
      const handler = async () => {
        btn.disabled = true; btn.textContent = 'Denying…';
        try {
          const res = await apiClient.put(`/api/v1/overtime-requests/${btn.getAttribute('data-ot-id')}/deny`, {});
          if (res?.success) {
            notificationStore.success('Overtime shift denied.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to deny.');
            btn.disabled = false; btn.textContent = '✕ Deny';
          }
        } catch (e) {
          notificationStore.danger('Error: ' + e.message);
          btn.disabled = false; btn.textContent = '✕ Deny';
        }
      };
      btn.addEventListener('click', handler);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });
  }
}
export { ShiftSupervisorDashboard };
