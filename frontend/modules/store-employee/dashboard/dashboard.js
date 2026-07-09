/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : dashboard.js
 * Path              : frontend/modules/store-employee/dashboard/dashboard.js
 * Purpose           : Interactive Store Employee Dashboard
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

export default class StoreEmployeeDashboard {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    
    // Load state from localStorage or use default mock values
    const cachedState = localStorage.getItem(`emp_dashboard_state_${this.user?.username || 'neha'}`);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
      } catch (err) {
        logger.error('StoreEmployeeDashboard', 'Error parsing cached state, resetting', err);
        this.initDefaultState();
      }
    } else {
      this.initDefaultState();
    }
    
    this._clockInterval = null;
    this.activeModal = null;

    // Live backend data (populated by loadLeaveData)
    this.leaveTypes = [];
    this.leaveBalances = [];
    this.holidays = [];
  }

  initDefaultState() {
    this.state = {
      name: this.profile.name || 'Neha Sharma',
      id: 'EMP10245',
      level: 'Senior Barista',
      store: 'Green Park Café, City Center',
      clockedIn: true,
      clockInTime: '08:02 AM',
      trainingProgress: 72,
      performanceScore: 4.6,
      tasks: [
        { id: 1, name: 'Prepare espresso bar and grinders', priority: 'High Priority', status: 'In Progress' },
        { id: 2, name: 'Restock milk cartons and syrups', priority: 'Medium Priority', status: 'Not Started' },
        { id: 3, name: 'Sanitize customer tables and counters', priority: 'Medium Priority', status: 'Not Started' },
        { id: 4, name: 'Verify cold brew keg pressures', priority: 'Low Priority', status: 'In Progress' },
        { id: 5, name: 'Log temperature check logs', priority: 'High Priority', status: 'Not Started' }
      ],
      leave: {
        available: 12.5,
        pending: 1,
        approved: 3
      },
      attendanceLogs: [
        { date: 'Today', shift: 'Morning Shift', checkIn: '08:02 AM', checkOut: '--:--', hours: '6.2h', status: 'Active' },
        { date: 'Yesterday', shift: 'Morning Shift', checkIn: '08:00 AM', checkOut: '04:00 PM', hours: '8.0h', status: 'Present' },
        { date: '04 July 2026', shift: 'Morning Shift', checkIn: '07:58 AM', checkOut: '04:03 PM', hours: '8.1h', status: 'Present' },
        { date: '03 July 2026', shift: 'Morning Shift', checkIn: '08:01 AM', checkOut: '04:00 PM', hours: '8.0h', status: 'Present' },
        { date: '02 July 2026', shift: 'Morning Shift', checkIn: '07:55 AM', checkOut: '04:00 PM', hours: '8.1h', status: 'Present' }
      ],
      activities: [
        { text: 'Clocked in at 08:02 AM', time: 'Today, 08:02 AM' },
        { text: 'Completed espresso calibration checklist', time: 'Today, 08:15 AM' },
        { text: 'Completed safety module training', time: 'Yesterday' }
      ]
    };
    this.saveState();
  }

  saveState() {
    localStorage.setItem(`emp_dashboard_state_${this.user?.username || 'neha'}`, JSON.stringify(this.state));
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeDashboard', 'Mounting Store Employee Dashboard...');
    
    // Fetch live session information to remove placeholders
    try {
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes && meRes.success) {
        this.profile = { ...this.profile, ...meRes.data };
        this.state.name = this.profile.name || this.state.name;
        this.state.store = this.profile.store || this.state.store;
        const rawRole = this.profile.role || 'SENIOR_EMPLOYEE';
        this.state.level = rawRole.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
        this.state.id = 'EMP' + (this.profile.id || '10245');
        this.saveState();
      }
    } catch (err) {
      logger.error('StoreEmployeeDashboard', 'Failed to fetch dynamic user info', err);
    }

    this.render(container);
    this.bindEvents(container, lifecycle);
    this.startClock(container);

    // Fetch live leave data from backend and re-render dynamic parts
    await this.loadLeaveData(container);
    
    lifecycle.onCleanup(() => {
      this.destroy();
    });
  }

  /** Fetch leave types, balances, and holidays from the database */
  async loadLeaveData(container) {
    const safeGet = async (url) => {
      try { return await apiClient.get(url); } catch (e) { return null; }
    };

    try {
      const [typesRes, balanceRes, holidaysRes] = await Promise.all([
        safeGet('/leaves/types'),
        safeGet('/leaves/my/balance'),
        safeGet('/leaves/holidays?countryCode=FR&year=' + new Date().getFullYear())
      ]);

      this.leaveTypes = typesRes?.data || [];
      this.leaveBalances = balanceRes?.data || [];
      this.holidays = (holidaysRes?.data || []).map(h => h.date);

      // --- Update Leave Category dropdown ---
      const catSelect = container.querySelector('#dash-leave-category');
      if (catSelect && this.leaveTypes.length > 0) {
        catSelect.innerHTML = '<option value="">— Select Category —</option>' +
          this.leaveTypes.map(t =>
            `<option value="${t.id}">${t.name}</option>`
          ).join('');
      }

      // --- Update Leave Balance KPI card ---
      const balKpiValue = container.querySelector('#kpi-leave-balance');
      const balKpiDetail = container.querySelector('#kpi-leave-detail');
      if (balKpiValue && this.leaveBalances.length > 0) {
        const totalRemaining = this.leaveBalances.reduce((s, b) => s + (b.remaining || 0), 0);
        const totalUsed = this.leaveBalances.reduce((s, b) => s + (b.used || 0), 0);
        const totalPending = this.leaveBalances.reduce((s, b) => s + (b.pending || 0), 0);
        balKpiValue.textContent = `${totalRemaining} Days Available`;
        if (balKpiDetail) {
          balKpiDetail.innerHTML = `${totalUsed} used &nbsp;·&nbsp; ${totalPending} pending`;
        }
        // Update internal state for submit validation
        this.state.leave.available = totalRemaining;
        this.state.leave.pending = totalPending;
        this.state.leave.approved = totalUsed;
        this.saveState();
      }

      logger.info('StoreEmployeeDashboard', `Loaded ${this.leaveTypes.length} leave types, ${this.leaveBalances.length} balances`);
    } catch (err) {
      logger.error('StoreEmployeeDashboard', 'Failed to load leave data', err);
    }
  }

  render(container) {
    const pendingTasksCount = this.state.tasks.filter(t => t.status !== 'Completed').length;
    
    container.innerHTML = `
      <style>
        @keyframes wave-animation {
          0% { transform: rotate( 0.0deg) }
          10% { transform: rotate(14.0deg) }
          20% { transform: rotate(-8.0deg) }
          30% { transform: rotate(14.0deg) }
          40% { transform: rotate(-4.0deg) }
          50% { transform: rotate(10.0deg) }
          60% { transform: rotate( 0.0deg) }
          100% { transform: rotate( 0.0deg) }
        }
      </style>
      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header Banner Card -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left; box-shadow: var(--shadow-md);">
          <div style="display: flex; align-items: center; gap: var(--spacing-md);">
            <div style="position: relative;">
              <img src="${this.profile.avatarUrl || 'imgs/female-avatar.jpg'}" alt="Employee Avatar" style="width: 56px; height: 56px; border-radius: 50%; border: 2.5px solid var(--accent-primary); object-fit: cover; box-shadow: 0 4px 10px rgba(0,0,0,0.3);">
              <span style="position: absolute; bottom: 0; right: 0; width: 14px; height: 14px; border-radius: 50%; background: ${this.state.clockedIn ? 'var(--status-success)' : 'var(--status-danger)'}; border: 2px solid var(--bg-card);"></span>
            </div>
            <div>
              <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0; display: flex; align-items: center; gap: 8px;">
                Welcome Back, ${this.state.name}
                <svg class="wave-hand animate-bounce" xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="var(--accent-primary)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" style="display: inline-block; animation: wave-animation 2.5s infinite; transform-origin: 70% 70%;">
                  <path d="M18 8a2 2 0 1 1 4 0v6a8 8 0 0 1-8 8h-2c-2.8 0-4.5-.86-5.99-2.34l-3.6-3.6a2 2 0 0 1 2.83-2.82L9 16V9a2 2 0 1 1 4 0v3a1 1 0 1 0 2 0V5a2 2 0 1 1 4 0v3a1 1 0 1 0 2 0V8z"/>
                </svg>
              </h2>
              <span style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px;">
                ID: <span style="color: var(--accent-primary); font-weight: 700;">${this.state.id}</span> &nbsp;·&nbsp; Role: <span style="color: var(--accent-primary); font-weight: 700;">${this.state.level}</span> &nbsp;·&nbsp; Location: <span style="color: var(--accent-primary); font-weight: 700;">${this.state.store}</span>
              </span>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: var(--spacing-md); margin-left: auto; flex-wrap: wrap;">
            <div style="text-align: right; display: flex; flex-direction: column; justify-content: center;">
              <span style="font-size: 0.6rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; letter-spacing: 0.5px; margin-bottom: 2px;">Digital Timecard</span>
              <div id="employee-clock" style="font-size: 1rem; font-weight: 800; color: var(--text-primary); background: rgba(0,0,0,0.3); padding: 5px 12px; border-radius: var(--radius-md); border: 1px solid var(--border-color); font-variant-numeric: tabular-nums; font-family: monospace; text-shadow: 0 0 10px rgba(201,164,106,0.2);">
                --:--:--
              </div>
            </div>
          </div>
        </div>

        <!-- 6 KPI Information Cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: var(--spacing-md); width: 100%;">
          
          <!-- Card 1: Today's Shift -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 100px; text-align: left; box-shadow: var(--shadow-sm); transition: var(--transition-fast);" onmouseover="this.style.transform='translateY(-2px)'" onmouseout="this.style.transform='none'">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted);">
              <span style="font-size: 0.65rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Today's Shift</span>
              <i data-lucide="calendar" style="width: 16px; height: 16px; color: var(--status-success);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.15rem; font-weight: 800; color: var(--text-primary);">08:00 AM - 04:00 PM</h4>
              <span style="font-size: 0.6rem; color: var(--status-success); font-weight: 700; display: flex; align-items: center; gap: 4px; margin-top: 3px;">
                <span style="width: 6px; height: 6px; border-radius: 50%; background: var(--status-success); display: inline-block;"></span> Active Duty
              </span>
            </div>
          </div>

          <!-- Card 2: Attendance Status -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 100px; text-align: left; box-shadow: var(--shadow-sm); transition: var(--transition-fast);" onmouseover="this.style.transform='translateY(-2px)'" onmouseout="this.style.transform='none'">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted);">
              <span style="font-size: 0.65rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Duty Status</span>
              <i data-lucide="user-check" style="width: 16px; height: 16px; color: var(--status-info);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.25rem; font-weight: 800; color: ${this.state.clockedIn ? 'var(--status-success)' : 'var(--status-danger)'};">
                ${this.state.clockedIn ? 'Clocked In' : 'Clocked Out'}
              </h4>
              <span style="font-size: 0.6rem; color: var(--text-muted);">
                ${this.state.clockedIn ? `In since ${this.state.clockInTime}` : 'Out of duty schedule'}
              </span>
            </div>
          </div>

          <!-- Card 3: Checklist Tasks -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 100px; text-align: left; box-shadow: var(--shadow-sm); transition: var(--transition-fast);" onmouseover="this.style.transform='translateY(-2px)'" onmouseout="this.style.transform='none'">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted);">
              <span style="font-size: 0.65rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Shift Checklist</span>
              <i data-lucide="list-todo" style="width: 16px; height: 16px; color: var(--status-warning);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">
                ${pendingTasksCount} Tasks Pending
              </h4>
              <span style="font-size: 0.6rem; color: var(--text-muted);">
                Of ${this.state.tasks.length} total assigned
              </span>
            </div>
          </div>

          <!-- Card 4: Training & Safety Progress -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 100px; text-align: left; box-shadow: var(--shadow-sm); transition: var(--transition-fast);" onmouseover="this.style.transform='translateY(-2px)'" onmouseout="this.style.transform='none'">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted);">
              <span style="font-size: 0.65rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Training Completion</span>
              <i data-lucide="graduation-cap" style="width: 16px; height: 16px; color: var(--status-info);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">${this.state.trainingProgress}% Completed</h4>
              <div style="width: 100%; height: 6px; background: rgba(255,255,255,0.1); border-radius: 3px; margin-top: 6px; overflow:hidden;">
                <div style="width: ${this.state.trainingProgress}%; height: 100%; background: linear-gradient(90deg, var(--accent-primary), var(--status-info)); border-radius: 3px; transition: width 0.5s ease-out;"></div>
              </div>
            </div>
          </div>

          <!-- Card 5: Leave balance (live from DB) -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 100px; text-align: left; box-shadow: var(--shadow-sm); transition: var(--transition-fast);" onmouseover="this.style.transform='translateY(-2px)'" onmouseout="this.style.transform='none'">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted);">
              <span style="font-size: 0.65rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Leave Balance</span>
              <i data-lucide="plane" style="width: 16px; height: 16px; color: var(--accent-primary);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 id="kpi-leave-balance" style="margin: 0; font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">${this.state.leave.available} Days Available</h4>
              <span id="kpi-leave-detail" style="font-size: 0.6rem; color: var(--text-muted);">
                ${this.state.leave.approved} used &nbsp;·&nbsp; ${this.state.leave.pending} pending
              </span>
            </div>
          </div>

          <!-- Card 6: Performance Rating -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 100px; text-align: left; box-shadow: var(--shadow-sm); transition: var(--transition-fast);" onmouseover="this.style.transform='translateY(-2px)'" onmouseout="this.style.transform='none'">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted);">
              <span style="font-size: 0.65rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Performance Score</span>
              <i data-lucide="star" style="width: 16px; height: 16px; color: var(--accent-primary);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">${this.state.performanceScore} / 5.0</h4>
              <div style="display: flex; align-items: center; gap: 2px; margin-top: 3px;">
                ${[1, 2, 3, 4, 5].map(i => `
                  <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="${i <= Math.round(this.state.performanceScore) ? 'var(--accent-primary)' : 'none'}" stroke="var(--accent-primary)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>
                  </svg>
                `).join('')}
              </div>
            </div>
          </div>

        </div>

        <!-- Main Workspace Grid layout -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Column Left: Duty Actions & Active Shifts -->
          <div style="display: flex; flex-direction: column; gap: var(--spacing-lg);">
            
            <!-- Duty Check-In Card -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Duty shift Timecard</h3>
                <i data-lucide="clock" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>
              
              <div style="display: grid; grid-template-columns: 1fr 1.2fr; gap: var(--spacing-md); background: rgba(0,0,0,0.15); padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid rgba(255,255,255,0.02);">
                <div>
                  <span style="font-size: 0.6rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700;">Shift Schedule</span>
                  <div style="font-weight: 800; font-size: 0.95rem; color: var(--text-primary); margin-top: 4px;">Morning Shift</div>
                  <div style="font-size: 0.65rem; color: var(--text-muted); margin-top: 2px;">08:00 AM - 04:00 PM</div>
                </div>
                <div>
                  <span style="font-size: 0.6rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700;">Next Shift Break</span>
                  <div style="font-weight: 700; font-size: 0.85rem; color: var(--accent-primary); margin-top: 4px;">12:00 PM - 12:30 PM</div>
                  <div style="font-size: 0.65rem; color: var(--text-muted); margin-top: 2px;">Lunch Break (Paid)</div>
                </div>
              </div>

              <div style="display: flex; gap: var(--spacing-sm); margin-top: var(--spacing-sm);">
                <button class="btn" id="btn-clock-in" style="flex: 1; font-weight: 700; padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; font-size: 0.8rem; background: var(--status-success); color: #fff; box-shadow: 0 4px 10px rgba(74,222,128,0.2); transition: var(--transition-fast); ${this.state.clockedIn ? 'opacity:0.4; cursor:not-allowed; box-shadow:none;' : ''}" ${this.state.clockedIn ? 'disabled' : ''}>
                  <i data-lucide="play" style="width: 14px; height: 14px;"></i> Clock In
                </button>
                <button class="btn" id="btn-clock-out" style="flex: 1; font-weight: 700; padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; font-size: 0.8rem; background: var(--status-danger); color: #fff; box-shadow: 0 4px 10px rgba(239,68,68,0.2); transition: var(--transition-fast); ${!this.state.clockedIn ? 'opacity:0.4; cursor:not-allowed; box-shadow:none;' : ''}" ${!this.state.clockedIn ? 'disabled' : ''}>
                  <i data-lucide="square" style="width: 14px; height: 14px;"></i> Clock Out
                </button>
              </div>
            </div>

            <!-- Quick Actions Hub -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Quick Actions & Documents</h3>
                <i data-lucide="files" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>
              
              <div style="display: grid; grid-template-columns: 1fr 1fr; gap: var(--spacing-sm);">
                <button class="btn btn-secondary flex align-center justify-center gap-xs" id="btn-view-payslip" style="padding: var(--spacing-md); border-radius: var(--radius-md); font-size: 0.75rem; font-weight: 700; text-align: center;">
                  <i data-lucide="file-text" style="width: 14px; height: 14px; color: var(--accent-primary);"></i> View Payslip
                </button>
                <button class="btn btn-secondary flex align-center justify-center gap-xs" id="btn-attendance-history" style="padding: var(--spacing-md); border-radius: var(--radius-md); font-size: 0.75rem; font-weight: 700; text-align: center;">
                  <i data-lucide="history" style="width: 14px; height: 14px; color: var(--accent-primary);"></i> Timecard Log
                </button>
                <button class="btn btn-secondary flex align-center justify-center gap-xs" id="btn-request-swap" style="padding: var(--spacing-md); border-radius: var(--radius-md); font-size: 0.75rem; font-weight: 700; text-align: center;">
                  <i data-lucide="refresh-cw" style="width: 14px; height: 14px; color: var(--accent-primary);"></i> Swap Roster
                </button>
                <button class="btn btn-secondary flex align-center justify-center gap-xs" id="btn-attendance-issue" style="padding: var(--spacing-md); border-radius: var(--radius-md); font-size: 0.75rem; font-weight: 700; text-align: center;">
                  <i data-lucide="alert-circle" style="width: 14px; height: 14px; color: var(--accent-primary);"></i> File Discrepancy
                </button>
              </div>
            </div>

            <!-- Leave Application Form -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Request Time Off</h3>
                <i data-lucide="plane" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>
              
              <div style="display: flex; flex-direction: column; gap: var(--spacing-sm);">
                <!-- Leave Category -->
                <div>
                  <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block; margin-bottom: 4px;">Leave Category</label>
                  <select id="dash-leave-category" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); font-size: 0.76rem; padding: var(--spacing-sm); outline: none; cursor: pointer;">
                    <option value="">— Select Category —</option>
                    <option value="annual">Annual Leave</option>
                    <option value="sick">Sick Leave</option>
                    <option value="personal">Personal Leave</option>
                    <option value="emergency">Emergency Leave</option>
                  </select>
                </div>

                <!-- Date Pickers Row -->
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: var(--spacing-sm);">
                  <div>
                    <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block; margin-bottom: 4px;">Start Date</label>
                    <input type="date" id="dash-leave-start" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); font-size: 0.76rem; padding: var(--spacing-sm); outline: none; color-scheme: dark;">
                  </div>
                  <div>
                    <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block; margin-bottom: 4px;">End Date</label>
                    <input type="date" id="dash-leave-end" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); font-size: 0.76rem; padding: var(--spacing-sm); outline: none; color-scheme: dark;">
                  </div>
                </div>

                <!-- Working Days Indicator -->
                <div id="dash-working-days" style="display:none; background: rgba(130,163,125,0.08); border: 1px solid rgba(130,163,125,0.2); border-radius: var(--radius-sm); padding: 6px 10px; font-size: 0.7rem; color: var(--accent-primary); font-weight: 700;">
                  📅 Estimated Working Days: <span id="dash-days-count">0</span>
                </div>

                <!-- Reason -->
                <div>
                  <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block; margin-bottom: 4px;">Reason</label>
                  <input type="text" id="dash-leave-reason" placeholder="e.g. Annual Vacation, Family Event..." style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); font-size: 0.76rem; padding: var(--spacing-sm); outline: none;">
                </div>

                <button class="btn" id="btn-submit-leave" style="background: var(--accent-primary); color: #000; font-weight: 800; border: none; border-radius: var(--radius-md); padding: var(--spacing-sm); font-size: 0.8rem; cursor: pointer; transition: var(--transition-fast); margin-top: 4px; display: flex; align-items: center; justify-content: center; gap: 6px;" onmouseover="this.style.filter='brightness(1.1)';" onmouseout="this.style.filter='none';">
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M22 2L11 13"/><path d="M22 2l-7 20-4-9-9-4 20-7z"/></svg>
                  Submit Leave Request
                </button>
              </div>
            </div>

          </div>

          <!-- Column Right: Active Checklist & Training Hub -->
          <div style="display: flex; flex-direction: column; gap: var(--spacing-lg);">
            
            <!-- Checklist Card -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Shift Checklist Tasks</h3>
                <i data-lucide="check-square" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>
              
              <!-- Task List Mount -->
              <div style="display: flex; flex-direction: column; gap: 8px; overflow-y: auto; max-height: 240px; padding-right: 4px;" id="employee-task-list">
                ${this.state.tasks.map(t => `
                  <div class="task-row flex align-center justify-between" style="background: rgba(0,0,0,0.2); border: 1px solid rgba(255,255,255,0.02); border-radius: var(--radius-md); padding: 8px var(--spacing-md); font-size: 0.76rem; gap: 8px;">
                    <div style="display: flex; align-items: center; gap: var(--spacing-sm); flex: 1;">
                      <label style="display: flex; align-items: center; gap: 8px; cursor: pointer; margin: 0; width: 100%;">
                        <input type="checkbox" class="task-checkbox" data-id="${t.id}" ${t.status === 'Completed' ? 'checked' : ''} style="display: none;">
                        <span style="width: 16px; height: 16px; border-radius: 4px; border: 1.5px solid ${t.status === 'Completed' ? 'var(--accent-primary)' : 'var(--text-muted)'}; display: inline-flex; align-items: center; justify-content: center; background: ${t.status === 'Completed' ? 'var(--accent-primary)' : 'transparent'}; transition: all 0.2s;">
                          ${t.status === 'Completed' ? `
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="#000" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round">
                              <polyline points="20 6 9 17 4 12"></polyline>
                            </svg>
                          ` : ''}
                        </span>
                        <span style="font-weight: 600; color: ${t.status === 'Completed' ? 'var(--text-muted)' : 'var(--text-primary)'}; text-decoration: ${t.status === 'Completed' ? 'line-through' : 'none'}; text-align: left;">${t.name}</span>
                      </label>
                    </div>
                    <div style="display: flex; align-items: center; gap: 6px;">
                      <span style="font-size: 0.58rem; font-weight: 700; padding: 2px 6px; border-radius: 4px; background: ${t.priority === 'High Priority' ? 'rgba(239,68,68,0.1)' : 'rgba(255,255,255,0.04)'}; color: ${t.priority === 'High Priority' ? 'var(--status-danger)' : 'var(--text-muted)'}; text-transform: uppercase;">
                        ${t.priority.split(' ')[0]}
                      </span>
                      <button class="btn-delete-task" data-id="${t.id}" style="background: none; border: none; color: var(--text-muted); cursor: pointer; display: flex; align-items: center;" onmouseover="this.style.color='var(--status-danger)'" onmouseout="this.style.color='var(--text-muted)'">
                        <i data-lucide="trash-2" style="width: 12px; height: 12px;"></i>
                      </button>
                    </div>
                  </div>
                `).join('')}
              </div>

              <!-- Add custom task form -->
              <div style="display: flex; gap: var(--spacing-xs); margin-top: 4px; border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm);">
                <input type="text" id="add-task-input" placeholder="Create a custom task..." style="flex: 1; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); font-size: 0.76rem; padding: 6px var(--spacing-sm); outline: none;">
                <button class="btn btn-secondary" id="btn-add-task" style="padding: 6px 12px; font-weight: 700; font-size: 0.74rem;">Add</button>
              </div>
            </div>

            <!-- Interactive Training Hub -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Training Modules & S1 Quiz</h3>
                <i data-lucide="graduation-cap" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>
              
              <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); font-size: 0.75rem;">
                <div style="background: rgba(0,0,0,0.1); border: 1px solid rgba(255,255,255,0.01); padding: var(--spacing-sm); border-radius: var(--radius-md); display: flex; flex-direction: column; gap: 4px;">
                  <div style="display: flex; justify-content: space-between; font-weight: 700;">
                    <span>Food Safety Certification</span>
                    <span style="color: var(--accent-primary);">S1 Module</span>
                  </div>
                  <span style="font-size: 0.65rem; color: var(--text-muted);">Prerequisite course for junior and senior baristas.</span>
                </div>
              </div>
              
              <button class="btn" id="btn-start-training" style="background: transparent; border: 1px solid var(--accent-primary); color: var(--accent-primary); font-weight: 800; border-radius: var(--radius-md); padding: var(--spacing-sm); font-size: 0.8rem; cursor: pointer; transition: var(--transition-fast); text-align: center; display: flex; align-items: center; justify-content: center; gap: 6px; width: 100%;" onmouseover="this.style.background='rgba(201,164,106,0.15)';" onmouseout="this.style.background='transparent';">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M12 20h9"></path>
                  <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"></path>
                </svg>
                Start S1 Training Quiz
              </button>
            </div>

            <!-- Announcements Board -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Announcements Board</h3>
                <i data-lucide="megaphone" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>
              
              <div style="display: flex; flex-direction: column; gap: var(--spacing-md); font-size: 0.74rem;">
                <div style="border-left: 3.5px solid var(--status-danger); padding-left: var(--spacing-sm);">
                  <strong style="color: var(--text-primary); font-size: 0.8rem; display: block;">Upcoming Store Schedule Updates</strong>
                  <span style="color: var(--text-muted); font-size: 0.7rem; line-height: 1.35; display: block; margin-top: 2px;">
                    Please check shift rosters for the upcoming week. Stores will operate on Sunday holiday hours (09:00 AM - 05:00 PM).
                  </span>
                </div>
                <div style="border-left: 3.5px solid var(--accent-primary); padding-left: var(--spacing-sm);">
                  <strong style="color: var(--text-primary); font-size: 0.8rem; display: block;">Barista Safety Standards v2</strong>
                  <span style="color: var(--text-muted); font-size: 0.7rem; line-height: 1.35; display: block; margin-top: 2px;">
                    Remember to record machine safety calibration checklists on every morning shift before opening grinders.
                  </span>
                </div>
              </div>
            </div>

          </div>

        </div>

      </div>

      <!-- Modal Mount Point Overlay -->
      <div id="employee-modal-overlay" style="position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.65); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 10000; padding: var(--spacing-md);">
        <div id="employee-modal-content" class="card glass animate-slide-up" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); width: 100%; max-width: 500px; padding: var(--spacing-lg); max-height: 90vh; overflow-y: auto; text-align: left; box-shadow: 0 10px 30px rgba(0,0,0,0.5);">
          <!-- Modal content injected here -->
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#employee-modal-overlay');
    const modalContent = container.querySelector('#employee-modal-content');

    const showModal = (htmlContent) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = htmlContent;
        overlay.style.display = 'flex';
        this.activeModal = 'open';
        
        // Modal close hooks
        const closeBtn = modalContent.querySelector('.btn-close-modal');
        if (closeBtn) {
          closeBtn.addEventListener('click', () => hideModal());
        }
        if (window.lucide) window.lucide.createIcons();
      }
    };

    const hideModal = () => {
      if (overlay) {
        overlay.style.display = 'none';
        this.activeModal = null;
      }
    };

    // Overlay click dismiss
    if (overlay) {
      overlay.addEventListener('click', (e) => {
        if (e.target === overlay) {
          hideModal();
        }
      });
    }

    // 1. Clock In / Out
    const clockInBtn = container.querySelector('#btn-clock-in');
    const clockOutBtn = container.querySelector('#btn-clock-out');

    if (clockInBtn) {
      clockInBtn.addEventListener('click', () => {
        this.state.clockedIn = true;
        const nowStr = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        this.state.clockInTime = nowStr;
        this.state.attendanceLogs.unshift({
          date: 'Today',
          shift: 'Morning Shift',
          checkIn: nowStr,
          checkOut: '--:--',
          hours: '--',
          status: 'Active'
        });
        
        this.state.activities.unshift({ text: `Clocked in for shift at ${nowStr}`, time: 'Just now' });
        notificationStore.success('Successfully clocked in for your shift.');
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    }

    if (clockOutBtn) {
      clockOutBtn.addEventListener('click', () => {
        this.state.clockedIn = false;
        const nowStr = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        
        // Update today's log
        if (this.state.attendanceLogs[0] && this.state.attendanceLogs[0].date === 'Today') {
          this.state.attendanceLogs[0].checkOut = nowStr;
          this.state.attendanceLogs[0].status = 'Present';
          this.state.attendanceLogs[0].hours = '8.0h';
        }
        
        this.state.activities.unshift({ text: `Clocked out of shift at ${nowStr}`, time: 'Just now' });
        notificationStore.success('Successfully clocked out of your shift.');
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    }

    // 2. Add custom task
    const addTaskBtn = container.querySelector('#btn-add-task');
    const addTaskInput = container.querySelector('#add-task-input');
    
    if (addTaskBtn && addTaskInput) {
      const handleAddTask = () => {
        const text = addTaskInput.value.trim();
        if (!text) return;
        
        const newId = this.state.tasks.length ? Math.max(...this.state.tasks.map(t => t.id)) + 1 : 1;
        this.state.tasks.push({
          id: newId,
          name: text,
          priority: 'Low Priority',
          status: 'Not Started'
        });
        
        this.state.activities.unshift({ text: `Added custom task "${text}"`, time: 'Just now' });
        notificationStore.success('Added new task to checklist.');
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      
      addTaskBtn.addEventListener('click', handleAddTask);
      addTaskInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') handleAddTask();
      });
    }

    // 3. Task checklist toggle
    const checkboxes = container.querySelectorAll('.task-checkbox');
    checkboxes.forEach(chk => {
      chk.addEventListener('change', () => {
        const id = parseInt(chk.getAttribute('data-id'));
        const task = this.state.tasks.find(t => t.id === id);
        if (task) {
          task.status = chk.checked ? 'Completed' : 'In Progress';
          
          if (chk.checked) {
            this.state.activities.unshift({ text: `Completed checklist item "${task.name}"`, time: 'Just now' });
            notificationStore.info(`Completed task: ${task.name}`);
          } else {
            this.state.activities.unshift({ text: `Unchecked checklist item "${task.name}"`, time: 'Just now' });
          }
          
          this.saveState();
          this.render(container);
          this.bindEvents(container, lifecycle);
        }
      });
    });

    // 4. Delete checklist task
    const deleteBtns = container.querySelectorAll('.btn-delete-task');
    deleteBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const id = parseInt(btn.getAttribute('data-id'));
        const index = this.state.tasks.findIndex(t => t.id === id);
        if (index > -1) {
          const taskName = this.state.tasks[index].name;
          this.state.tasks.splice(index, 1);
          notificationStore.info(`Removed task: ${taskName}`);
          
          this.saveState();
          this.render(container);
          this.bindEvents(container, lifecycle);
        }
      });
    });

    // 5. Dashboard Leave Date Picker — auto working-days calculation
    const dashStartInput = container.querySelector('#dash-leave-start');
    const dashEndInput = container.querySelector('#dash-leave-end');
    const dashIndicator = container.querySelector('#dash-working-days');
    const dashCounter = container.querySelector('#dash-days-count');

    const calcWorkingDays = (startDate, endDate) => {
      const holidaySet = new Set(this.holidays);
      let count = 0;
      const cur = new Date(startDate);
      while (cur <= endDate) {
        const d = cur.getUTCDay();
        const iso = cur.getUTCFullYear() + '-' +
                    String(cur.getUTCMonth() + 1).padStart(2, '0') + '-' +
                    String(cur.getUTCDate()).padStart(2, '0');
        if (d !== 0 && d !== 6 && !holidaySet.has(iso)) count++;
        cur.setUTCDate(cur.getUTCDate() + 1);
      }
      return count;
    };

    const updateDashDays = () => {
      if (dashStartInput?.value && dashEndInput?.value) {
        const start = new Date(dashStartInput.value);
        const end = new Date(dashEndInput.value);
        if (end >= start) {
          const days = calcWorkingDays(start, end);
          dashCounter.textContent = days;
          dashIndicator.style.display = 'block';
        } else {
          dashIndicator.style.display = 'none';
        }
      } else {
        dashIndicator.style.display = 'none';
      }
    };

    dashStartInput?.addEventListener('change', updateDashDays);
    dashStartInput?.addEventListener('input', updateDashDays);
    dashEndInput?.addEventListener('change', updateDashDays);
    dashEndInput?.addEventListener('input', updateDashDays);

    // Submit Leave Request — sends to backend database via API
    const submitLeaveBtn = container.querySelector('#btn-submit-leave');
    if (submitLeaveBtn) {
      submitLeaveBtn.addEventListener('click', async () => {
        const leaveTypeId = container.querySelector('#dash-leave-category')?.value;
        const startVal = dashStartInput?.value;
        const endVal = dashEndInput?.value;
        const reason = container.querySelector('#dash-leave-reason')?.value?.trim();

        if (!leaveTypeId) { notificationStore.danger('Please select a leave category.'); return; }
        if (!startVal || !endVal) { notificationStore.danger('Please specify both start and end dates.'); return; }

        const start = new Date(startVal);
        const end = new Date(endVal);
        if (end < start) { notificationStore.danger('End date cannot be before start date.'); return; }

        if (!reason) { notificationStore.danger('Please specify a reason for leave request.'); return; }

        const days = calcWorkingDays(start, end);

        try {
          const response = await apiClient.post('/leaves', {
            leaveTypeId: leaveTypeId,
            session: days === 0.5 ? 'HALF_DAY_AM' : 'FULL_DAY',
            startDate: startVal,
            endDate: endVal,
            reason
          });

          if (response && response.success) {
            const status = response.data?.status;
            if (status === 'APPROVED') {
              notificationStore.success('✅ Leave auto-approved — legally protected leave type.');
            } else {
              notificationStore.success('Leave request submitted successfully. Pending approval.');
            }

            this.state.activities.unshift({ text: `Applied for ${days} day(s) leave: "${reason}"`, time: 'Just now' });
            this.saveState();

            // Reset form
            container.querySelector('#dash-leave-category').value = '';
            dashStartInput.value = '';
            dashEndInput.value = '';
            container.querySelector('#dash-leave-reason').value = '';
            dashIndicator.style.display = 'none';

            // Reload live data from backend to reflect updated balances
            await this.loadLeaveData(container);
          } else {
            notificationStore.danger(response?.message || 'Failed to submit leave request.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to submit leave request.');
        }
      });
    }

    // 6. View Payslip Modal
    const payslipBtn = container.querySelector('#btn-view-payslip');
    if (payslipBtn) {
      payslipBtn.addEventListener('click', () => {
        const payslipHtml = `
          <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1.5px solid var(--accent-primary); padding-bottom:var(--spacing-xs); margin-bottom:var(--spacing-md);">
            <h3 style="margin:0; font-weight:800; color:var(--accent-primary); font-family:var(--font-display);">PLUS33 Employee Payslip</h3>
            <button class="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer;"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div style="font-size:0.75rem; color:var(--text-primary); display:flex; flex-direction:column; gap:10px;">
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:8px; border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:8px;">
              <div><strong>Name:</strong> ${this.state.name}</div>
              <div><strong>Employee ID:</strong> ${this.state.id}</div>
              <div><strong>Month:</strong> June 2026</div>
              <div><strong>Designation:</strong> ${this.state.level}</div>
            </div>
            
            <strong style="font-size:0.8rem; color:var(--accent-primary); margin-top:4px;">Earnings & Allowances</strong>
            <div style="display:flex; justify-content:space-between; padding:2px 0;">
              <span>Basic Salary:</span>
              <span>₹42,000.00</span>
            </div>
            <div style="display:flex; justify-content:space-between; padding:2px 0;">
              <span>House Rent Allowance (HRA):</span>
              <span>₹12,500.00</span>
            </div>
            <div style="display:flex; justify-content:space-between; padding:2px 0; border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:6px;">
              <span>Conveyance Allowance:</span>
              <span>₹3,200.00</span>
            </div>
            
            <strong style="font-size:0.8rem; color:var(--accent-primary); margin-top:4px;">Deductions</strong>
            <div style="display:flex; justify-content:space-between; padding:2px 0;">
              <span>Provident Fund (PF):</span>
              <span>₹1,800.00</span>
            </div>
            <div style="display:flex; justify-content:space-between; padding:2px 0; border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:6px;">
              <span>Professional Tax (PT):</span>
              <span>₹200.00</span>
            </div>
            
            <div style="background:rgba(201,164,106,0.1); border:1px solid var(--accent-primary); padding:10px; border-radius:var(--radius-md); display:flex; justify-content:space-between; font-weight:800; font-size:0.85rem; margin-top:6px; color:var(--accent-primary);">
              <span>Net Take-home Salary:</span>
              <span>₹55,700.00</span>
            </div>
            
            <button class="btn btn-secondary flex align-center justify-center gap-xs" style="width:100%; margin-top:10px; padding:var(--spacing-sm);" onclick="alert('Downloading payslip PDF receipt...');">
              <i data-lucide="download" style="width:14px; height:14px;"></i> Download Payslip PDF
            </button>
          </div>
        `;
        showModal(payslipHtml);
      });
    }

    // 7. View Timecard History Log Modal
    const historyBtn = container.querySelector('#btn-attendance-history');
    if (historyBtn) {
      historyBtn.addEventListener('click', () => {
        const historyHtml = `
          <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1.5px solid var(--accent-primary); padding-bottom:var(--spacing-xs); margin-bottom:var(--spacing-md);">
            <h3 style="margin:0; font-weight:800; color:var(--accent-primary); font-family:var(--font-display);">Attendance Roster Logs</h3>
            <button class="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer;"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div style="max-height: 250px; overflow-y: auto;">
            <table style="width: 100%; border-collapse: collapse; font-size: 0.72rem; text-align: left;">
              <thead>
                <tr style="border-bottom: 1.5px solid var(--border-color); color: var(--text-muted); text-transform: uppercase; font-weight: 700;">
                  <th style="padding: 6px;">Date</th>
                  <th style="padding: 6px;">In</th>
                  <th style="padding: 6px;">Out</th>
                  <th style="padding: 6px;">Hours</th>
                  <th style="padding: 6px;">Status</th>
                </tr>
              </thead>
              <tbody>
                ${this.state.attendanceLogs.map(log => `
                  <tr style="border-bottom: 1px solid rgba(255,255,255,0.05); hover: background: rgba(255,255,255,0.02);">
                    <td style="padding: 8px 6px; font-weight:600;">${log.date}</td>
                    <td style="padding: 8px 6px;">${log.checkIn}</td>
                    <td style="padding: 8px 6px;">${log.checkOut}</td>
                    <td style="padding: 8px 6px;">${log.hours}</td>
                    <td style="padding: 8px 6px; font-weight: 800; color: ${log.status === 'Active' ? 'var(--status-info)' : 'var(--status-success)'};">${log.status}</td>
                  </tr>
                `).join('')}
              </tbody>
            </table>
          </div>
        `;
        showModal(historyHtml);
      });
    }

    // 8. Interactive S1 Quiz Modal
    const startTrainingBtn = container.querySelector('#btn-start-training');
    if (startTrainingBtn) {
      startTrainingBtn.addEventListener('click', () => {
        const quizHtml = `
          <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1.5px solid var(--accent-primary); padding-bottom:var(--spacing-xs); margin-bottom:var(--spacing-md);">
            <h3 style="margin:0; font-weight:800; color:var(--accent-primary); font-family:var(--font-display);">Food Safety Certification (S1) Quiz</h3>
            <button class="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer;"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div style="font-size:0.75rem; color:var(--text-primary); display:flex; flex-direction:column; gap:12px;">
            <p style="margin:0; line-height:1.4;">Complete this safety question to progress your S1 safety module checklist score.</p>
            
            <div style="background:rgba(255,255,255,0.02); border:1px solid var(--border-color); padding:12px; border-radius:var(--radius-md);">
              <strong style="color:var(--accent-primary); font-size:0.78rem; display:block; margin-bottom:8px;">Question:</strong>
              <span style="font-weight:600; line-height:1.35; display:block;">Which of the following is the correct target holding temperature range for hot milk espresso beverages?</span>
            </div>
            
            <div style="display:flex; flex-direction:column; gap:6px;">
              <button class="btn btn-secondary btn-option" data-correct="false" style="padding:10px; border-radius:var(--radius-md); text-align:left; font-weight:600; font-size:0.72rem;">A) 100°F - 120°F (Warm)</button>
              <button class="btn btn-secondary btn-option" data-correct="true" style="padding:10px; border-radius:var(--radius-md); text-align:left; font-weight:600; font-size:0.72rem;">B) 140°F - 160°F (Optimal Food Safety Range)</button>
              <button class="btn btn-secondary btn-option" data-correct="false" style="padding:10px; border-radius:var(--radius-md); text-align:left; font-weight:600; font-size:0.72rem;">C) 180°F - 200°F (Scalding)</button>
            </div>
          </div>
        `;
        showModal(quizHtml);

        // Bind quiz option clicks
        const options = modalContent.querySelectorAll('.btn-option');
        options.forEach(opt => {
          opt.addEventListener('click', () => {
            const isCorrect = opt.getAttribute('data-correct') === 'true';
            if (isCorrect) {
              notificationStore.success('Correct answer! +5% Training Module Progress.');
              this.state.trainingProgress = Math.min(this.state.trainingProgress + 5, 100);
              this.state.activities.unshift({ text: 'Answered S1 safety quiz correctly', time: 'Just now' });
              this.saveState();
              hideModal();
              this.render(container);
              this.bindEvents(container, lifecycle);
            } else {
              notificationStore.danger('Incorrect answer. Please review the safety guidelines and try again!');
              opt.style.borderColor = 'var(--status-danger)';
              opt.style.background = 'rgba(239,68,68,0.1)';
            }
          });
        });
      });
    }

    // 9. Standard Actions Notification Fallbacks
    const actions = [
      { id: '#btn-request-swap', msg: 'Shift roster swap request submitted to store supervisor.' },
      { id: '#btn-attendance-issue', msg: 'Timecard logging discrepancy reported to manager.' }
    ];

    actions.forEach(act => {
      const el = container.querySelector(act.id);
      if (el) {
        el.addEventListener('click', () => {
          notificationStore.info(act.msg);
        });
      }
    });
  }

  startClock(container) {
    const updateTime = () => {
      const clockEl = container.querySelector('#employee-clock');
      if (clockEl) {
        const now = new Date();
        clockEl.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true });
      }
    };
    updateTime();
    this._clockInterval = setInterval(updateTime, 1000);
  }

  destroy() {
    if (this._clockInterval) {
      clearInterval(this._clockInterval);
    }
  }
}
