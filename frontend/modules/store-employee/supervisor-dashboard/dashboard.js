/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module - Shift Supervisor Sub-Role
 * File              : dashboard.js
 * Path              : frontend/modules/store-employee/supervisor-dashboard/dashboard.js
 * Purpose           : Shift supervisor control hub, active roster tracking, and leave approvals
 * Version           : 2.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

const API = '/api/v1';

export default class ShiftSupervisorDashboard {
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

  async loadData() {
    try {
      const pendingRes = await apiClient.get('/leaves/pending');
      if (pendingRes && pendingRes.success) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      } else {
        logger.error('ShiftSupervisorDashboard', 'Failed to fetch pending leaves');
      }
    } catch (err) {
      logger.error('ShiftSupervisorDashboard', 'Network error fetching pending leaves', err);
    }
  }

  async mount(container, lifecycle) {
    logger.info('ShiftSupervisorDashboard', 'Mounting Shift Supervisor Dashboard...');
    this.loadState();
    
    // Fetch live session details to sync supervisor profile info
    try {
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes && meRes.success) {
        this.profile = { ...this.profile, ...meRes.data };
        this.state.name = this.profile.name || this.state.name;
        this.state.store = this.profile.store || this.state.store;
        this.state.id = 'SUP' + (this.profile.id || '40212');
        this.saveState();
      }
    } catch (err) {
      logger.error('ShiftSupervisorDashboard', 'Failed to retrieve session info', err);
    }

    this.render(container, true);
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  render(container, loading = false) {
    if (loading) {
      container.innerHTML = `
        <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
          <i data-lucide="loader-2" style="width:32px;height:32px;color:var(--accent-primary);animation:spin 1s linear infinite;"></i>
          <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading supervisor control hub...</span>
        </div>`;
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    container.innerHTML = `
      <style>
        .team-row {
          background: rgba(0,0,0,0.15);
          border: 1px solid var(--border-color);
          border-radius: var(--radius-md);
          padding: var(--spacing-md);
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: var(--spacing-md);
          transition: all 0.2s ease-out;
        }
        .team-row:hover {
          border-color: rgba(255,255,255,0.08);
          background: rgba(255,255,255,0.02);
        }
        .leave-approval-card {
          background: rgba(201,164,106,0.04);
          border: 1px solid rgba(201,164,106,0.15);
          border-radius: var(--radius-md);
          padding: var(--spacing-md);
          display: flex;
          flex-direction: column;
          gap: var(--spacing-sm);
          text-align: left;
        }
      </style>

      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header welcome banner -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left; position: relative; overflow: hidden;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0;">
              Welcome back, Supervisor ${this.state.name}!
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              Store: <span style="color: var(--accent-primary); font-weight: 700;">${this.state.store}</span> &nbsp;·&nbsp; Active Shift: <span style="color: var(--accent-primary); font-weight: 700;">${this.state.shiftName} (${this.state.shiftHours})</span>
            </p>
          </div>
          <div style="background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.72rem; font-weight: 600; color: var(--status-success); display: flex; align-items: center; gap: 6px;">
            <span style="width:6px; height:6px; border-radius:50%; background: var(--status-success); display:inline-block; animation: pulse 1.5s infinite;"></span> Shift Running
          </div>
        </div>

        <!-- 4 Supervisor KPI Cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: var(--spacing-md); width: 100%;">
          
          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
              <i data-lucide="users" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.state.activeTeam.length} Baristas</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Clocked-in Team</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
              <i data-lucide="shopping-cart" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.state.totalOrders} Orders</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Shift Orders Fulfillment</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(74,222,128,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
              <i data-lucide="zap" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.state.fulfillmentSpeed} min</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Avg Beverage Prep Speed</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(239,68,68,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-danger); display:flex; align-items:center;">
              <i data-lucide="calendar" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.pendingLeaves.length} Pending</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Leave Requests Awaiting</div>
            </div>
          </div>

        </div>

        <!-- Main Workspace splits -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Column Left: Team Status Board -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; flex: 1.6;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Shift Team Monitor</h3>
              <i data-lucide="activity" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <div style="display: flex; flex-direction: column; gap: var(--spacing-sm);">
              ${this.state.activeTeam.map(barista => `
                <div class="team-row">
                  <div style="display: flex; align-items: center; gap: var(--spacing-md); flex: 1;">
                    <div style="background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); padding: 8px; border-radius: 50%; display: flex; align-items: center;">
                      <i data-lucide="user" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
                    </div>
                    <div style="flex:1;">
                      <strong style="color:var(--text-primary); font-size:0.85rem; display:block;">${barista.name}</strong>
                      <span style="font-size:0.62rem; color:var(--text-muted);">Station: ${barista.role} &nbsp;·&nbsp; Punch In: ${barista.checkIn}</span>
                      
                      <!-- Checklist progress bar -->
                      <div style="display:flex; align-items:center; gap:8px; margin-top:6px;">
                        <span style="font-size:0.58rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Checklist Progress:</span>
                        <div style="flex:1; max-width: 100px; background:rgba(255,255,255,0.05); height:4px; border-radius:2px; overflow:hidden;">
                          <div style="width:${barista.progress}%; height:100%; background:var(--accent-primary); border-radius:2px;"></div>
                        </div>
                        <span style="font-size:0.62rem; font-weight:800; color:var(--text-primary);">${barista.progress}%</span>
                      </div>
                    </div>
                  </div>

                  <button class="btn btn-secondary btn-ping-barista" data-name="${barista.name}" style="padding: 4px 10px; font-size: 0.65rem; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; cursor: pointer;">
                    <i data-lucide="bell" style="width: 11px; height: 11px; color:var(--accent-primary);"></i> Ping
                  </button>
                </div>
              `).join('')}
            </div>
          </div>

          <!-- Column Right: Integrated Leave Approvals Center -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; flex: 1.2;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Leave Approvals Hub</h3>
              <i data-lucide="shield-check" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <div style="display: flex; flex-direction: column; gap: var(--spacing-md); max-height:480px; overflow-y:auto;">
              ${this.pendingLeaves.length === 0 ? `
                <div style="display:flex; flex-direction:column; align-items:center; justify-content:center; padding:var(--spacing-lg); color:var(--text-muted); border: 1.5px dashed rgba(255,255,255,0.05); border-radius:var(--radius-md); text-align:center;">
                  <i data-lucide="check-circle" style="width:28px; height:28px; margin-bottom:6px; opacity:0.3; color:var(--status-success);"></i>
                  <span style="font-weight:600; font-size:0.78rem;">All requests signed off</span>
                  <span style="font-size:0.62rem; margin-top:2px;">No pending barista time-off applications to process.</span>
                </div>
              ` : this.pendingLeaves.map(req => `
                <div class="leave-approval-card" style="position:relative;">
                  <div style="display:flex; justify-content:space-between; align-items:center; gap: 8px;">
                    <strong style="color:var(--text-primary); font-size:0.8rem;">${req.employeeName}</strong>
                    <span style="font-size:0.58rem; font-weight:700; background:rgba(255,255,255,0.05); padding:2px 8px; border-radius:4px; color:var(--accent-primary); text-transform:uppercase;">
                      ${req.leaveTypeCode}
                    </span>
                  </div>

                  <div style="font-size:0.72rem; color:var(--text-muted);">
                    <div><strong>Duration:</strong> ${req.totalDays} day(s) (${req.startDate} to ${req.endDate})</div>
                    <div style="margin-top:2px; font-style:italic;">"${req.reason || 'No reason specified'}"</div>
                    ${!req.hasDocument && req.requiresDocument ? `<div style="margin-top:4px; color:var(--status-danger); font-weight:700;">⚠ Medical Certificate Required</div>` : ''}
                  </div>

                  <div style="display:flex; flex-direction:column; gap:4px; margin-top:4px;">
                    <input type="text" class="supervisor-comment-input" data-id="${req.id}" placeholder="Optional approval comment..." style="background:rgba(0,0,0,0.3); border:1px solid var(--border-color); border-radius:var(--radius-sm); color:var(--text-primary); padding:4px 6px; font-size:0.68rem; outline:none; font-family:inherit;">
                  </div>

                  <div style="display:flex; gap:8px; margin-top:4px;">
                    <button class="btn btn-approve-leave" data-id="${req.id}" style="flex:1; background:var(--status-success); color:#000; font-weight:800; border:none; padding:6px; border-radius:4px; font-size:0.65rem; cursor:pointer; ${(!req.hasDocument && req.requiresDocument) ? 'opacity:0.4; pointer-events:none;' : ''}">
                      Approve
                    </button>
                    <button class="btn btn-reject-leave" data-id="${req.id}" style="flex:1; background:var(--status-danger); color:#fff; font-weight:800; border:none; padding:6px; border-radius:4px; font-size:0.65rem; cursor:pointer; ${req.isProtected ? 'opacity:0.4; pointer-events:none;' : ''}" ${req.isProtected ? 'disabled' : ''}>
                      Reject
                    </button>
                  </div>
                </div>
              `).join('')}
            </div>
          </div>

        </div>

      </div>

      <!-- Reject Modal -->
      <div id="reject-modal" style="display:none;position:fixed;inset:0;background:rgba(0,0,0,0.7);z-index:9999;align-items:center;justify-content:center;">
        <div style="background:var(--bg-card);border:1px solid var(--border-color);border-radius:var(--radius-lg);padding:var(--spacing-lg);width:100%;max-width:400px;display:flex;flex-direction:column;gap:var(--spacing-md);text-align:left;">
          <h3 style="font-family:var(--font-display);font-weight:800;font-size:1.0rem;margin:0;color:var(--status-danger);">Reject Barista Leave</h3>
          <label style="font-size:0.72rem;font-weight:700;color:var(--text-muted);">Rejection Reason <span style="color:var(--status-danger);">*</span> (min 10 chars)</label>
          <textarea id="reject-reason-input" rows="3" placeholder="State reason for rejection..." style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-md);color:var(--text-primary);padding:var(--spacing-sm);outline:none;font-family:inherit;resize:none;"></textarea>
          <div style="display:flex;gap:var(--spacing-sm);">
            <button id="reject-modal-submit" data-leave-id="" style="flex:1;background:var(--status-danger);color:#fff;font-weight:800;border:none;padding:var(--spacing-sm);border-radius:var(--radius-md);cursor:pointer;">Confirm Rejection</button>
            <button id="reject-modal-close" style="flex:1;background:rgba(255,255,255,0.06);color:var(--text-primary);font-weight:700;border:1px solid var(--border-color);padding:var(--spacing-sm);border-radius:var(--radius-md);cursor:pointer;">Cancel</button>
          </div>
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    // 1. Ping Barista
    const pingBtns = container.querySelectorAll('.btn-ping-barista');
    pingBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const name = btn.getAttribute('data-name');
        notificationStore.info(`Roster notification dispatched to ${name}.`);
      });
    });

    // 2. Approve Leave (Real API integration)
    const approveBtns = container.querySelectorAll('.btn-approve-leave');
    approveBtns.forEach(btn => {
      btn.addEventListener('click', async () => {
        const leaveId = btn.getAttribute('data-id');
        const commentInput = container.querySelector(`.supervisor-comment-input[data-id="${leaveId}"]`);
        const comment = commentInput?.value?.trim() || '';
        
        try {
          const response = await apiClient.put(`/leaves/${leaveId}/approve`, { comment });
          if (response && response.success) {
            notificationStore.success('Time-off application approved successfully.');
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to approve leave request.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to approve leave.');
        }
      });
    });

    // 3. Reject Leave Open Modal
    const rejectBtns = container.querySelectorAll('.btn-reject-leave');
    rejectBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const leaveId = btn.getAttribute('data-id');
        const modal = container.querySelector('#reject-modal');
        container.querySelector('#reject-modal-submit').dataset.leaveId = leaveId;
        container.querySelector('#reject-reason-input').value = '';
        modal.style.display = 'flex';
      });
    });

    // Reject Modal submit
    container.querySelector('#reject-modal-submit')?.addEventListener('click', async () => {
      const leaveId = container.querySelector('#reject-modal-submit').dataset.leaveId;
      const reason = container.querySelector('#reject-reason-input')?.value?.trim();
      if (!reason || reason.length < 10) {
        notificationStore.danger('Rejection reason must be at least 10 characters.');
        return;
      }

      try {
        const response = await apiClient.put(`/leaves/${leaveId}/reject`, { rejectionReason: reason });
        if (response && response.success) {
          notificationStore.success('Time-off application rejected.');
          container.querySelector('#reject-modal').style.display = 'none';
          await this.loadData();
          this.render(container);
          this.bindEvents(container, lifecycle);
        } else {
          notificationStore.danger(response?.message || 'Failed to reject leave request.');
        }
      } catch (err) {
        notificationStore.danger('Network error rejecting leave.');
      }
    });

    container.querySelector('#reject-modal-close')?.addEventListener('click', () => {
      container.querySelector('#reject-modal').style.display = 'none';
    });
  }
}
