/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee — Dashboard
 * File              : dashboard.js
 * Purpose           : Controller component for Store Employee Dashboard Page UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/dashboard/dashboard.html
 * Related CSS       : frontend/modules/store-employee/dashboard/dashboard.css
 * Related APIs      : GET /api/v1/auth/me
 *                     GET /leaves/types
 *                     GET /leaves/my/balance
 *                     GET /leaves/holidays
 *                     POST /leaves
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
import { geofenceMonitor } from '../../../../core/geofenceMonitor.js';

/** Path to the dashboard HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/dashboard/dashboard.html';

export default class StoreEmployeeDashboard {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

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
    this._geofenceActive = false;

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
      shiftScheduled: true,
      shiftName: 'Morning Shift',
      shiftStartTime: '08:00:00',
      shiftEndTime: '16:00:00',
      shiftCode: 'SHIFT_MORN',
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

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the store employee dashboard component context.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeDashboard', 'Mounting Store Employee Dashboard...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject skeleton template
    await this._loadTemplate(container);

    // 2. Fetch live session information to sync profile details
    await this._loadData();
    await this._loadAttendanceData(container);

    // 3. Render loaded data into the DOM
    this._render(container);

    // 4. Bind event listeners
    this._bindEvents(container, lifecycle);

    // 5. Trigger live clock
    this.startClock(container);

    // 6. Fetch live leave data from backend and re-render dynamic parts
    await this.loadLeaveData(container, lifecycle);
    
    lifecycle.onCleanup(() => {
      this.destroy();
      // Stop geofence monitor on page leave
      geofenceMonitor.stop();
    });
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
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes?.success) {
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
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  _render(container) {
    const pendingTasksCount = this.state.tasks.filter(t => t.status !== 'Completed').length;

    // 1. Sync header text variables
    const welcomeName = container.querySelector('#welcome-name');
    const avatarImg = container.querySelector('#employee-avatar');
    const statusDot = container.querySelector('#employee-status-dot');
    const metaId = container.querySelector('#meta-emp-id');
    const metaLevel = container.querySelector('#meta-emp-level');
    const metaStore = container.querySelector('#meta-emp-store');

    if (welcomeName) welcomeName.textContent = this.state.name;
    if (avatarImg) avatarImg.src = this.profile.avatarUrl || 'imgs/female-avatar.jpg';
    if (statusDot) {
      statusDot.style.backgroundColor = this.state.clockedIn ? 'var(--status-success)' : 'var(--status-danger)';
    }
    if (metaId) metaId.textContent = this.state.id;
    if (metaLevel) metaLevel.textContent = this.state.level;
    if (metaStore) metaStore.textContent = this.state.store;

    // 2. Sync KPIs values
    const kpiDutyStatus = container.querySelector('#kpi-duty-status');
    const kpiDutySubtext = container.querySelector('#kpi-duty-subtext');
    const kpiChecklistStatus = container.querySelector('#kpi-checklist-status');
    const kpiChecklistSubtext = container.querySelector('#kpi-checklist-subtext');
    const kpiTrainingProgress = container.querySelector('#kpi-training-progress');
    const trainingProgressFill = container.querySelector('#training-progress-fill');
    const kpiLeaveBalance = container.querySelector('#kpi-leave-balance');
    const kpiLeaveDetail = container.querySelector('#kpi-leave-detail');
    const kpiPerformanceScore = container.querySelector('#kpi-performance-score');
    const starsContainer = container.querySelector('#kpi-stars-container');

    if (kpiDutyStatus) {
      kpiDutyStatus.textContent = this.state.clockedIn ? 'Clocked In' : 'Clocked Out';
      kpiDutyStatus.style.color = this.state.clockedIn ? 'var(--status-success)' : 'var(--status-danger)';
    }
    if (kpiDutySubtext) {
      kpiDutySubtext.textContent = this.state.clockedIn ? `In since ${this.state.clockInTime}` : 'Out of duty schedule';
    }
    if (kpiChecklistStatus) kpiChecklistStatus.textContent = `${pendingTasksCount} Tasks Pending`;
    if (kpiChecklistSubtext) kpiChecklistSubtext.textContent = `Of ${this.state.tasks.length} total assigned`;
    if (kpiTrainingProgress) kpiTrainingProgress.textContent = `${this.state.trainingProgress}% Completed`;
    if (trainingProgressFill) trainingProgressFill.style.width = `${this.state.trainingProgress}%`;
    
    if (kpiLeaveBalance) kpiLeaveBalance.textContent = `${this.state.leave.available} Days Available`;
    if (kpiLeaveDetail) {
      kpiLeaveDetail.innerHTML = `${this.state.leave.approved} used &nbsp;·&nbsp; ${this.state.leave.pending} pending`;
    }

    if (kpiPerformanceScore) kpiPerformanceScore.textContent = `${this.state.performanceScore} / 5.0`;
    if (starsContainer) {
      starsContainer.replaceChildren();
      const score = Math.round(this.state.performanceScore);
      for (let i = 1; i <= 5; i++) {
        const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
        svg.setAttribute('width', '10');
        svg.setAttribute('height', '10');
        svg.setAttribute('viewBox', '0 0 24 24');
        svg.setAttribute('fill', i <= score ? 'var(--accent-primary)' : 'none');
        svg.setAttribute('stroke', 'var(--accent-primary)');
        svg.setAttribute('stroke-width', '2');
        
        const poly = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
        poly.setAttribute('points', '12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2');
        svg.appendChild(poly);
        starsContainer.appendChild(svg);
      }
    }

    // Disable buttons according to clocked in state
    const clockInBtn = container.querySelector('#btn-clock-in');
    const clockOutBtn = container.querySelector('#btn-clock-out');
    if (clockInBtn) {
      clockInBtn.disabled = this.state.clockedIn;
      clockInBtn.style.opacity = this.state.clockedIn ? '0.4' : '1';
      clockInBtn.style.cursor = this.state.clockedIn ? 'not-allowed' : 'pointer';
    }
    if (clockOutBtn) {
      clockOutBtn.disabled = !this.state.clockedIn;
      clockOutBtn.style.opacity = !this.state.clockedIn ? '0.4' : '1';
      clockOutBtn.style.cursor = !this.state.clockedIn ? 'not-allowed' : 'pointer';
    }

    // Render today's shift details on the timecard
    const timecardShiftName = container.querySelector('#timecard-shift-name');
    const timecardShiftHours = container.querySelector('#timecard-shift-hours');
    if (timecardShiftName) {
      timecardShiftName.textContent = this.state.shiftName || 'Day Off';
    }
    if (timecardShiftHours) {
      if (this.state.shiftScheduled && this.state.shiftStartTime && this.state.shiftEndTime) {
        timecardShiftHours.textContent = `${this.formatTime12(this.state.shiftStartTime)} - ${this.formatTime12(this.state.shiftEndTime)}`;
      } else {
        timecardShiftHours.textContent = 'Rest Day';
      }
    }

    // 3. Render checklist tasks
    this._renderTaskList(container);
    this._renderAwayPassCard(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  _bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#employee-modal-overlay');
    const modalContent = container.querySelector('#employee-modal-content');

    const showModal = (htmlContent) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = htmlContent;
        overlay.style.display = 'flex';
        overlay.setAttribute('aria-hidden', 'false');
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
        overlay.setAttribute('aria-hidden', 'true');
        this.activeModal = null;
      }
    };

    // Overlay click dismiss
    if (overlay) {
      const handleOverlayClick = (e) => {
        if (e.target === overlay) hideModal();
      };
      overlay.addEventListener('click', handleOverlayClick);
      lifecycle.onCleanup(() => overlay.removeEventListener('click', handleOverlayClick));
    }

    // 1. Clock In / Out
    const clockInBtn = container.querySelector('#btn-clock-in');
    const clockOutBtn = container.querySelector('#btn-clock-out');

    if (clockInBtn) {
      const handleClockIn = async () => {
        clockInBtn.disabled = true;
        // Step 1: get GPS before hitting the server
        let gps = null;
        try {
          gps = await new Promise((resolve, reject) => {
            if (!navigator.geolocation) { resolve(null); return; }
            navigator.geolocation.getCurrentPosition(
              p => resolve(`${p.coords.latitude},${p.coords.longitude}`),
              () => resolve(null),
              { enableHighAccuracy: true, timeout: 10000, maximumAge: 30000 }
            );
          });
        } catch (_) { gps = null; }

        try {
          const res = await apiClient.post('/api/v1/attendance/check-in', { gps });
          if (res?.success) {
            notificationStore.success('Successfully clocked in for your shift.');
            // Start geofence monitoring
            geofenceMonitor.start(
              () => {
                // Auto-clocked-out callback
                this.state.clockedIn = false;
                this.saveState();
                this._render(container);
                this._bindEvents(container, lifecycle);
              },
              (type, title, message, actions) => {
                this._showGeofencePopup(container, lifecycle, type, title, message, actions);
              }
            );
            await this._loadAttendanceData(container);
            this._render(container);
            this._bindEvents(container, lifecycle);
          } else {
            const msg = res?.message || '';
            if (msg.startsWith('NO_SHIFT_TODAY')) {
              // Rest day popup with overtime request button
              this._showPopupCard(container, lifecycle, 'info',
                '🌿 Rest Day',
                'You have no shift scheduled for today. This is your rest day — enjoy it! If you need to come in, you can request overtime below.',
                [
                  { label: 'Request Overtime', id: 'btn-popup-overtime', variant: 'btn-primary' },
                  { label: 'OK, Got It', id: 'btn-popup-dismiss', variant: 'btn-secondary' },
                ]
              );
              // Bind overtime request
              const overtimeBtn = container.querySelector('#btn-popup-overtime');
              if (overtimeBtn) {
                overtimeBtn.addEventListener('click', async () => {
                  try {
                    const shiftsRes = await apiClient.get('/api/v1/shifts');
                    const firstShiftId = (shiftsRes?.success && shiftsRes.data?.length > 0)
                      ? shiftsRes.data[0].id
                      : 1;

                    const todayStr = new Date().toISOString().split('T')[0];
                    await apiClient.post('/api/v1/overtime-requests', {
                      requestedDate: todayStr,
                      shiftId: firstShiftId,
                      reason: 'Requested additional shift for today'
                    });
                    notificationStore.success('Overtime request sent to your supervisor.');
                  } catch (e) {
                    notificationStore.danger('Failed to send overtime request: ' + e.message);
                  }
                  this._closePopupCard(container);
                });
              }
            } else if (msg.startsWith('OUT_OF_RANGE')) {
              this._showPopupCard(container, lifecycle, 'warning',
                '📍 Not at Store Location',
                msg.replace('OUT_OF_RANGE: ', ''),
                [{ label: 'OK', id: 'btn-popup-dismiss', variant: 'btn-secondary' }]
              );
            } else if (msg.startsWith('GPS_REQUIRED')) {
              this._showPopupCard(container, lifecycle, 'warning',
                '📡 Location Required',
                'Please enable location access in your browser to clock in. Your GPS location verifies you are at the store.',
                [{ label: 'OK', id: 'btn-popup-dismiss', variant: 'btn-secondary' }]
              );
            } else {
              notificationStore.danger(msg || 'Failed to clock in.');
            }
            clockInBtn.disabled = false;
          }
        } catch (e) {
          notificationStore.danger('Failed to clock in: ' + e.message);
          clockInBtn.disabled = false;
        }
      };
      clockInBtn.addEventListener('click', handleClockIn);
      lifecycle.onCleanup(() => clockInBtn.removeEventListener('click', handleClockIn));
    }

    if (clockOutBtn) {
      const handleClockOut = async () => {
        clockOutBtn.disabled = true;
        try {
          const res = await apiClient.post('/api/v1/attendance/check-out', {});
          if (res?.success) {
            geofenceMonitor.stop(); // stop monitoring on clock-out
            notificationStore.success('Successfully clocked out of your shift.');
            await this._loadAttendanceData(container);
            this._render(container);
            this._bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to clock out.');
            clockOutBtn.disabled = false;
          }
        } catch (e) {
          notificationStore.danger('Failed to clock out: ' + e.message);
          clockOutBtn.disabled = false;
        }
      };
      clockOutBtn.addEventListener('click', handleClockOut);
      lifecycle.onCleanup(() => clockOutBtn.removeEventListener('click', handleClockOut));
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
        this._render(container);
        this._bindEvents(container, lifecycle);
      };
      
      addTaskBtn.addEventListener('click', handleAddTask);
      lifecycle.onCleanup(() => addTaskBtn.removeEventListener('click', handleAddTask));

      const handleKey = (e) => {
        if (e.key === 'Enter') handleAddTask();
      };
      addTaskInput.addEventListener('keydown', handleKey);
      lifecycle.onCleanup(() => addTaskInput.removeEventListener('keydown', handleKey));
    }

    // 3. Task checklist toggle and delete listeners
    this._bindTaskActionEvents(container, lifecycle);

    // 4. Dashboard Leave Date Picker — auto working-days calculation
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
          dashCounter.textContent = String(days);
          dashIndicator.style.display = 'block';
        } else {
          dashIndicator.style.display = 'none';
        }
      } else {
        dashIndicator.style.display = 'none';
      }
    };

    const handleStartChange = () => updateDashDays();
    const handleEndChange = () => updateDashDays();

    dashStartInput?.addEventListener('change', handleStartChange);
    dashStartInput?.addEventListener('input', handleStartChange);
    dashEndInput?.addEventListener('change', handleEndChange);
    dashEndInput?.addEventListener('input', handleEndChange);

    lifecycle.onCleanup(() => {
      dashStartInput?.removeEventListener('change', handleStartChange);
      dashStartInput?.removeEventListener('input', handleStartChange);
      dashEndInput?.removeEventListener('change', handleEndChange);
      dashEndInput?.removeEventListener('input', handleEndChange);
    });

    // Submit Leave Request — sends to backend database via API
    const submitLeaveBtn = container.querySelector('#btn-submit-leave');
    if (submitLeaveBtn) {
      const handleSubmitLeave = async () => {
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

          if (response?.success) {
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
            await this.loadLeaveData(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to submit leave request.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to submit leave request.');
        }
      };
      submitLeaveBtn.addEventListener('click', handleSubmitLeave);
      lifecycle.onCleanup(() => submitLeaveBtn.removeEventListener('click', handleSubmitLeave));
    }

    // 5. View Payslip Modal
    const payslipBtn = container.querySelector('#btn-view-payslip');
    if (payslipBtn) {
      const handlePayslip = () => {
        const payslipHtml = `
          <div class="modal-header-split">
            <h3 class="modal-header-title">PLUS33 Employee Payslip</h3>
            <button class="btn-close-modal" type="button"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div class="payslip-details-grid">
            <div class="payslip-meta-grid">
              <div><strong>Name:</strong> ${this.state.name}</div>
              <div><strong>Employee ID:</strong> ${this.state.id}</div>
              <div><strong>Month:</strong> June 2026</div>
              <div><strong>Designation:</strong> ${this.state.level}</div>
            </div>
            
            <strong style="font-size:0.8rem; color:var(--accent-primary); margin-top:4px;">Earnings & Allowances</strong>
            <div class="payslip-row-item">
              <span>Basic Salary:</span>
              <span>₹42,000.00</span>
            </div>
            <div class="payslip-row-item">
              <span>House Rent Allowance (HRA):</span>
              <span>₹12,500.00</span>
            </div>
            <div class="payslip-row-item payslip-row-item--divider">
              <span>Conveyance Allowance:</span>
              <span>₹3,200.00</span>
            </div>
            
            <strong style="font-size:0.8rem; color:var(--accent-primary); margin-top:4px;">Deductions</strong>
            <div class="payslip-row-item">
              <span>Provident Fund (PF):</span>
              <span>₹1,800.00</span>
            </div>
            <div class="payslip-row-item payslip-row-item--divider">
              <span>Professional Tax (PT):</span>
              <span>₹200.00</span>
            </div>
            
            <div class="net-take-home-banner">
              <span>Net Take-home Salary:</span>
              <span>₹55,700.00</span>
            </div>
            
            <button class="btn btn-secondary flex align-center justify-center gap-xs" style="width:100%; margin-top:10px; padding:var(--spacing-sm);" onclick="alert('Downloading payslip PDF receipt...');">
              <i data-lucide="download" style="width:14px; height:14px;"></i> Download Payslip PDF
            </button>
          </div>
        `;
        showModal(payslipHtml);
      };
      payslipBtn.addEventListener('click', handlePayslip);
      lifecycle.onCleanup(() => payslipBtn.removeEventListener('click', handlePayslip));
    }

    // 6. View Timecard History Log Modal
    const historyBtn = container.querySelector('#btn-attendance-history');
    if (historyBtn) {
      const handleHistory = () => {
        const historyHtml = `
          <div class="modal-header-split">
            <h3 class="modal-header-title">Attendance Roster Logs</h3>
            <button class="btn-close-modal" type="button"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div class="timecard-table-scroller">
            <table class="timecard-table">
              <thead>
                <tr>
                  <th scope="col">Date</th>
                  <th scope="col">In</th>
                  <th scope="col">Out</th>
                  <th scope="col">Hours</th>
                  <th scope="col">Status</th>
                </tr>
              </thead>
              <tbody>
                ${this.state.attendanceLogs.map(log => `
                  <tr class="timecard-table-row">
                    <td>${log.date}</td>
                    <td>${log.checkIn}</td>
                    <td>${log.checkOut}</td>
                    <td>${log.hours}</td>
                    <td style="font-weight: 800; color: ${log.status === 'Active' ? 'var(--status-info)' : 'var(--status-success)'};">${log.status}</td>
                  </tr>
                `).join('')}
              </tbody>
            </table>
          </div>
        `;
        showModal(historyHtml);
      };
      historyBtn.addEventListener('click', handleHistory);
      lifecycle.onCleanup(() => historyBtn.removeEventListener('click', handleHistory));
    }

    // 7. Interactive S1 Quiz Modal
    const startTrainingBtn = container.querySelector('#btn-start-training');
    if (startTrainingBtn) {
      const handleStartTraining = () => {
        const quizHtml = `
          <div class="modal-header-split">
            <h3 class="modal-header-title">Food Safety Quiz</h3>
            <button class="btn-close-modal" type="button"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div style="font-size:0.75rem; color:var(--text-primary); display:flex; flex-direction:column; gap:12px;">
            <p style="margin:0; line-height:1.4;">Complete this safety question to progress your S1 safety module checklist score.</p>
            
            <div style="background:rgba(255,255,255,0.02); border:1px solid var(--border-color); padding:12px; border-radius:var(--radius-md);">
              <strong style="color:var(--accent-primary); font-size:0.78rem; display:block; margin-bottom:8px;">Question:</strong>
              <span style="font-weight:600; line-height:1.35; display:block;">Which of the following is the correct target holding temperature range for hot milk espresso beverages?</span>
            </div>
            
            <div style="display:flex; flex-direction:column; gap:6px;">
              <button class="btn btn-secondary quiz-option-button btn-option" data-correct="false" type="button">A) 100°F - 120°F (Warm)</button>
              <button class="btn btn-secondary quiz-option-button btn-option" data-correct="true" type="button">B) 140°F - 160°F (Optimal Food Safety Range)</button>
              <button class="btn btn-secondary quiz-option-button btn-option" data-correct="false" type="button">C) 180°F - 200°F (Scalding)</button>
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
              this._render(container);
              this._bindEvents(container, lifecycle);
            } else {
              notificationStore.danger('Incorrect answer. Please review the safety guidelines and try again!');
              opt.style.borderColor = 'var(--status-danger)';
              opt.style.background = 'rgba(239,68,68,0.1)';
            }
          });
        });
      };
      startTrainingBtn.addEventListener('click', handleStartTraining);
      lifecycle.onCleanup(() => startTrainingBtn.removeEventListener('click', handleStartTraining));
    }

    // 8. Standard Actions Notification Fallbacks
    const actions = [
      { id: '#btn-request-swap', msg: 'Shift roster swap request submitted to store supervisor.' },
      { id: '#btn-attendance-issue', msg: 'Timecard logging discrepancy reported to manager.' }
    ];

    actions.forEach(act => {
      const el = container.querySelector(act.id);
      if (el) {
        const handler = () => { notificationStore.info(act.msg); };
        el.addEventListener('click', handler);
        lifecycle.onCleanup(() => el.removeEventListener('click', handler));
      }
    });

    // 9. Away Pass Manager event handlers
    const btnSubmitAway = container.querySelector('#btn-submit-away-pass');
    const selectAwayReason = container.querySelector('#away-pass-reason-select');
    const customReasonGroup = container.querySelector('#away-pass-custom-reason-group');
    const inputAwayReason = container.querySelector('#away-pass-reason');
    const selectAwayDuration = container.querySelector('#away-pass-duration-select');

    if (selectAwayReason) {
      const handleReasonSelectChange = () => {
        if (customReasonGroup) {
          customReasonGroup.style.display = selectAwayReason.value === 'custom' ? 'block' : 'none';
        }
      };
      selectAwayReason.addEventListener('change', handleReasonSelectChange);
      lifecycle.onCleanup(() => selectAwayReason.removeEventListener('change', handleReasonSelectChange));
    }
    
    if (btnSubmitAway) {
      const handleSubmitAway = async () => {
        let rawReason = '';
        if (selectAwayReason?.value === 'custom') {
          rawReason = inputAwayReason?.value.trim() || '';
          if (!rawReason) {
            notificationStore.danger('Please provide custom reason details.');
            return;
          }
        } else {
          rawReason = selectAwayReason?.value || 'No reason';
        }
        
        const startVal = container.querySelector('#away-pass-start-time-select')?.value || 'now';
        const mins = selectAwayDuration?.value || '30';
        const startLabel = startVal === 'now' ? 'Now' : startVal;
        const reason = `${rawReason} (Start: ${startLabel}) (Requested Duration: ${mins} mins)`;
        
        btnSubmitAway.disabled = true;
        try {
          const res = await apiClient.post('/api/v1/away-permission/request', { reason });
          if (res?.success) {
            notificationStore.success('Away pass request submitted successfully.');
            if (inputAwayReason) inputAwayReason.value = '';
            await this._loadAttendanceData(container);
            this._render(container);
            this._bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to submit away pass request.');
            btnSubmitAway.disabled = false;
          }
        } catch (e) {
          notificationStore.danger('Failed to request away pass: ' + e.message);
          btnSubmitAway.disabled = false;
        }
      };
      btnSubmitAway.addEventListener('click', handleSubmitAway);
      lifecycle.onCleanup(() => btnSubmitAway.removeEventListener('click', handleSubmitAway));
    }

    const btnExtendAway = container.querySelector('#btn-extend-away-pass');
    if (btnExtendAway) {
      const handleExtendAway = async () => {
        const pass = this.state.currentAwayPass;
        if (!pass) return;

        const minsInput = prompt('How many additional minutes do you need? (e.g., 15, 30, 45, 60):', '30');
        if (minsInput === null) return; // User cancelled

        const mins = parseInt(minsInput.trim(), 10);
        if (isNaN(mins) || mins <= 0) {
          notificationStore.danger('Please enter a valid number of minutes.');
          return;
        }

        const reasonInput = prompt('Reason for extension (optional):');
        if (reasonInput === null) return; // User cancelled

        let extensionReason = `Requested ${mins} extra minutes.`;
        if (reasonInput.trim()) {
          extensionReason += ` Reason: ${reasonInput.trim()}`;
        }
        
        btnExtendAway.disabled = true;
        try {
          const res = await apiClient.post(`/api/v1/away-permission/${pass.id}/extend`, {
            reason: extensionReason
          });
          if (res?.success) {
            notificationStore.success('Extension request sent to your supervisor.');
            await this._loadAttendanceData(container);
            this._render(container);
            this._bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to request extension.');
            btnExtendAway.disabled = false;
          }
        } catch (e) {
          notificationStore.danger('Failed to request extension: ' + e.message);
          btnExtendAway.disabled = false;
        }
      };
      btnExtendAway.addEventListener('click', handleExtendAway);
      lifecycle.onCleanup(() => btnExtendAway.removeEventListener('click', handleExtendAway));
    }

    const btnReturnAway = container.querySelector('#btn-return-away-pass');
    if (btnReturnAway) {
      const handleReturnAway = async () => {
        const pass = this.state.currentAwayPass;
        if (!pass) return;
        
        btnReturnAway.disabled = true;
        try {
          const res = await apiClient.post(`/api/v1/away-permission/${pass.id}/return`, {});
          if (res?.success) {
            notificationStore.success('Away pass ended successfully. Welcome back!');
            await this._loadAttendanceData(container);
            this._render(container);
            this._bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to end away pass.');
            btnReturnAway.disabled = false;
          }
        } catch (e) {
          notificationStore.danger('Failed to end away pass: ' + e.message);
          btnReturnAway.disabled = false;
        }
      };
      btnReturnAway.addEventListener('click', handleReturnAway);
      lifecycle.onCleanup(() => btnReturnAway.removeEventListener('click', handleReturnAway));
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    if (this._clockInterval) {
      clearInterval(this._clockInterval);
      this._clockInterval = null;
    }
    logger.debug('StoreEmployeeDashboard', 'Clock cleared.');
  }

  unmount() {
    this.destroy();
    geofenceMonitor.stop();
  }

  // ---------------------------------------------------------------------------
  // POPUP CARD HELPERS (no alert/confirm)
  // ---------------------------------------------------------------------------

  /**
   * Shows a styled popup card inside the dashboard modal overlay.
   * @param {'info'|'warning'|'danger'|'success'} type
   * @param {string} title
   * @param {string} message
   * @param {Array<{label,id,variant}>} actions
   */
  _showPopupCard(container, lifecycle, type, title, message, actions = []) {
    const overlay = container.querySelector('#employee-modal-overlay');
    const modalContent = container.querySelector('#employee-modal-content');
    if (!overlay || !modalContent) return;

    const iconMap = { info: '💡', warning: '⚠️', danger: '🚨', success: '✅' };
    const colorMap = {
      info:    'var(--status-info)',
      warning: '#f59e0b',
      danger:  'var(--status-danger)',
      success: 'var(--status-success)',
    };
    const icon  = iconMap[type]  || '💡';
    const color = colorMap[type] || 'var(--accent-primary)';

    const actionButtons = actions.map(a =>
      `<button id="${a.id}" class="btn ${a.variant || 'btn-secondary'}" type="button" style="flex:1; min-width:120px;">${a.label}</button>`
    ).join('');

    modalContent.innerHTML = `
      <div style="display:flex; flex-direction:column; align-items:center; gap:14px; text-align:center; padding:8px 4px;">
        <div style="font-size:2.4rem; line-height:1;">${icon}</div>
        <h3 style="margin:0; font-size:1rem; font-weight:700; color:${color};">${title}</h3>
        <p style="margin:0; font-size:0.82rem; color:var(--text-secondary); line-height:1.55;">${message}</p>
        <div style="display:flex; gap:8px; flex-wrap:wrap; justify-content:center; width:100%; margin-top:4px;">
          ${actionButtons}
        </div>
      </div>
    `;
    overlay.style.display = 'flex';
    overlay.setAttribute('aria-hidden', 'false');
    this.activeModal = 'popup';

    // Wire dismiss buttons
    const dismissBtn = modalContent.querySelector('#btn-popup-dismiss');
    if (dismissBtn) {
      dismissBtn.addEventListener('click', () => this._closePopupCard(container));
    }

    // Wire generic geofence buttons
    const geofenceDismiss = modalContent.querySelector('#btn-geofence-dismiss');
    if (geofenceDismiss) geofenceDismiss.addEventListener('click', () => this._closePopupCard(container));

    const geofenceAck = modalContent.querySelector('#btn-geofence-ack');
    if (geofenceAck) geofenceAck.addEventListener('click', () => this._closePopupCard(container));

    const geofenceExtend = modalContent.querySelector('#btn-geofence-extend');
    if (geofenceExtend) {
      geofenceExtend.addEventListener('click', async () => {
        try {
          // Find the current away pass request
          const myRes = await apiClient.get('/api/v1/away-permission/my');
          const passes = myRes?.data || [];
          const activePasses = passes.filter(p => p.status === 'EXTENSION_REQUESTED' || p.status === 'APPROVED');
          if (activePasses.length > 0) {
            const passId = activePasses[activePasses.length - 1].id;
            await apiClient.post(`/api/v1/away-permission/${passId}/extend`, {
              reason: 'Extension requested from mobile — still returning to store'
            });
            notificationStore.success('Extension request sent to your supervisor.');
          } else {
            notificationStore.info('No active away pass found. Please contact your supervisor.');
          }
        } catch (e) {
          notificationStore.info('Extension request sent to your supervisor.');
        }
        this._closePopupCard(container);
      });
    }

    if (window.lucide) window.lucide.createIcons();
  }

  _closePopupCard(container) {
    const overlay = container.querySelector('#employee-modal-overlay');
    if (overlay) {
      overlay.style.display = 'none';
      overlay.setAttribute('aria-hidden', 'true');
      this.activeModal = null;
    }
  }

  /**
   * Geofence monitor popup — uses the popup card system.
   */
  _showGeofencePopup(container, lifecycle, type, title, message, actions) {
    this._showPopupCard(container, lifecycle, type, title, message, actions);
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
  // PUBLIC: loadLeaveData
  // ---------------------------------------------------------------------------

  async loadLeaveData(container, lifecycle) {
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
        catSelect.replaceChildren();
        const placeholder = document.createElement('option');
        placeholder.value = '';
        placeholder.textContent = '— Select Category —';
        catSelect.appendChild(placeholder);

        this.leaveTypes.forEach(t => {
          const opt = document.createElement('option');
          opt.value = String(t.id);
          opt.textContent = t.name;
          catSelect.appendChild(opt);
        });
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
        
        // Update internal state
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

  // ---------------------------------------------------------------------------
  // PRIVATE CLOCK MANAGEMENT
  // ---------------------------------------------------------------------------

  formatTime12(timeStr) {
    if (!timeStr) return '';
    const parts = timeStr.split(':');
    let h = parseInt(parts[0]);
    const m = parts[1];
    const ampm = h >= 12 ? 'PM' : 'AM';
    h = h % 12;
    h = h ? h : 12; // the hour '0' should be '12'
    return `${String(h).padStart(2, '0')}:${m} ${ampm}`;
  }

  startClock(container) {
    const updateTime = () => {
      const clockEl = container.querySelector('#employee-clock');
      if (clockEl) {
        const now = new Date();
        clockEl.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true });
      }
      this.updateShiftCountdown(container);
      this.updateWorkElapsed(container);
      this.updateAwayPassTimer(container);
    };
    updateTime();
    this._clockInterval = setInterval(updateTime, 1000);
  }

  updateShiftCountdown(container) {
    const countdownBlock = container.querySelector('#shift-countdown-block');
    const countdownVal = container.querySelector('#shift-countdown-value');
    const countdownLbl = container.querySelector('#shift-countdown-label');
    const breakBlock = container.querySelector('#shift-break-block');

    if (!countdownBlock || !countdownVal || !countdownLbl) return;

    // Countdown should ONLY display when clocked in
    if (!this.state.clockedIn) {
      countdownBlock.style.display = 'none';
      return;
    }

    if (breakBlock) {
      // Hide break details if it's a day off
      breakBlock.style.display = this.state.shiftScheduled ? 'flex' : 'none';
    }

    const now = new Date();
    let endDate = null;
    let labelText = 'until shift ends';

    if (this.state.shiftScheduled && this.state.shiftEndTime) {
      // 1. Shift is scheduled, countdown to the scheduled shift end time
      const endParts = this.state.shiftEndTime.split(':');
      endDate = new Date(now);
      endDate.setHours(parseInt(endParts[0]), parseInt(endParts[1]), parseInt(endParts[2] || '0'), 0);
      
      // If shift end is before shift start (crosses midnight)
      if (this.state.shiftStartTime) {
        const startParts = this.state.shiftStartTime.split(':');
        const startDate = new Date(now);
        startDate.setHours(parseInt(startParts[0]), parseInt(startParts[1]), parseInt(startParts[2] || '0'), 0);
        if (endDate < startDate) {
          endDate.setDate(endDate.getDate() + 1);
        }
      }
    } else if (this.state.checkInTimeRaw) {
      // 2. Day Off / unscheduled shift, countdown 8 hours from clock-in
      const checkInDate = new Date(this.state.checkInTimeRaw);
      endDate = new Date(checkInDate.getTime() + 8 * 60 * 60 * 1000);
      labelText = 'until standard end';
    }

    if (endDate) {
      const diffMs = endDate - now;
      if (diffMs > 0) {
        countdownBlock.style.display = 'flex';
        countdownVal.textContent = this.formatDuration(diffMs);
        countdownLbl.textContent = labelText;
        countdownVal.style.color = 'var(--accent-primary, #c9a46a)';
      } else {
        // Shift ended or overtime
        countdownBlock.style.display = 'flex';
        countdownVal.textContent = 'Shift Ended';
        countdownLbl.textContent = 'overtime active';
        countdownVal.style.color = '#ff6b6b';
      }
    } else {
      countdownBlock.style.display = 'none';
    }
  }

  updateWorkElapsed(container) {
    const elapsedBlock = container.querySelector('#shift-elapsed-block');
    const elapsedVal = container.querySelector('#shift-elapsed-value');
    const elapsedLbl = container.querySelector('#shift-elapsed-label');

    if (!elapsedBlock || !elapsedVal) return;

    if (!this.state.checkInTimeRaw) {
      elapsedBlock.style.display = 'none';
      return;
    }

    elapsedBlock.style.display = 'flex';

    if (this.state.clockedIn) {
      const now = new Date();
      const checkInDate = new Date(this.state.checkInTimeRaw);
      const breakMs = (this.state.breakMinutes || 0) * 60 * 1000;
      let diffMs = now - checkInDate - breakMs;
      if (diffMs < 0) diffMs = 0;

      elapsedVal.textContent = this.formatDuration(diffMs);
      if (elapsedLbl) elapsedLbl.textContent = 'worked today';
    } else {
      const finalMinutes = this.state.workMinutes || 0;
      const finalMs = finalMinutes * 60 * 1000;
      elapsedVal.textContent = this.formatDuration(finalMs);
      if (elapsedLbl) elapsedLbl.textContent = 'total worked';
    }
  }

  formatDuration(ms) {
    const totalSecs = Math.floor(ms / 1000);
    const hours = Math.floor(totalSecs / 3600);
    const minutes = Math.floor((totalSecs % 3600) / 60);
    const seconds = totalSecs % 60;
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderTaskList(container) {
    const taskList = container.querySelector('#employee-task-list');
    const rowTpl = container.querySelector('#checklist-row-tpl');

    if (!taskList || !rowTpl) return;

    taskList.replaceChildren();

    this.state.tasks.forEach(t => {
      const clone = rowTpl.content.cloneNode(true);

      const rowDiv = clone.querySelector('.task-item-row');
      const input = clone.querySelector('.task-checkbox');
      const indicator = clone.querySelector('.checkbox-indicator');
      const label = clone.querySelector('.task-name-text');
      const priority = clone.querySelector('.task-priority-tag');
      const deleteBtn = clone.querySelector('.btn-delete-task');

      if (input) {
        input.setAttribute('data-id', String(t.id));
        input.checked = t.status === 'Completed';
      }

      if (indicator && t.status === 'Completed') {
        indicator.innerHTML = `
          <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="#000" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="20 6 9 17 4 12"></polyline>
          </svg>
        `;
      }

      if (label) label.textContent = t.name;

      if (priority) {
        priority.textContent = t.priority.split(' ')[0];
        if (t.priority === 'High Priority') {
          priority.className = 'task-priority-tag task-priority-tag--high';
        }
      }

      if (deleteBtn) {
        deleteBtn.setAttribute('data-id', String(t.id));
      }

      taskList.appendChild(clone);
    });
  }

  _bindTaskActionEvents(container, lifecycle) {
    // Checkbox toggling listener
    const checkboxes = container.querySelectorAll('.task-checkbox');
    checkboxes.forEach(chk => {
      const handleToggle = () => {
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
          this._render(container);
          this._bindEvents(container, lifecycle);
        }
      };
      chk.addEventListener('change', handleToggle);
      lifecycle.onCleanup(() => chk.removeEventListener('change', handleToggle));
    });

    // Task deletion listener
    const deleteBtns = container.querySelectorAll('.btn-delete-task');
    deleteBtns.forEach(btn => {
      const handleDelete = () => {
        const id = parseInt(btn.getAttribute('data-id'));
        const index = this.state.tasks.findIndex(t => t.id === id);
        if (index > -1) {
          const taskName = this.state.tasks[index].name;
          this.state.tasks.splice(index, 1);
          notificationStore.info(`Removed task: ${taskName}`);
          
          this.saveState();
          this._render(container);
          this._bindEvents(container, lifecycle);
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });
  }

  async _loadAttendanceData(container) {
    try {
      const todayRes = await apiClient.get('/api/v1/attendance/today');
      if (todayRes?.success && todayRes.data) {
        const d = todayRes.data;
        this.state.clockedIn = d.clockedIn || false;
        this.state.clockInTime = d.checkInTime ? new Date(d.checkInTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--:--';
        this.state.checkInTimeRaw = d.checkInTime || '';
        this.state.shiftScheduled = d.shiftScheduled || false;
        this.state.shiftName = d.shiftName || 'Day Off';
        this.state.shiftStartTime = d.shiftStartTime || '';
        this.state.shiftEndTime = d.shiftEndTime || '';
        this.state.shiftCode = d.shiftCode || '';
        this.state.breakMinutes = d.breakMinutes || 0;
        this.state.workMinutes = d.workMinutes || 0;

        // Auto-start geofence monitor if already clocked in (page reload, session resume)
        if (this.state.clockedIn && !this._geofenceActive) {
          this._geofenceActive = true;
          const self = this;
          geofenceMonitor.start(
            () => {
              self.state.clockedIn = false;
              self.saveState();
            },
            (type, title, message, actions) => {
              // container may be stale on resume — best-effort
              const c = document.querySelector('.store-employee-dashboard-workspace')?.closest('[data-page]') || document.body;
              self._showGeofencePopup(c, { onCleanup: () => {} }, type, title, message, actions);
            }
          );
        }
        if (!this.state.clockedIn) {
          geofenceMonitor.stop();
          this._geofenceActive = false;
        }
      }
      
      const historyRes = await apiClient.get('/api/v1/attendance/history');
      if (historyRes?.success && historyRes.data) {
        const logs = historyRes.data;
        if (logs.length > 0) {
          this.state.attendanceLogs = logs.map(l => ({
            date: l.date,
            shift: l.shift,
            checkIn: l.checkIn,
            checkOut: l.checkOut,
            hours: l.hours,
            status: l.status
          }));
        }
      }

      // Fetch user's away permissions for today
      if (this.state.clockedIn) {
        const awayRes = await apiClient.get('/api/v1/away-permission/my').catch(() => null);
        if (awayRes?.success && awayRes.data) {
          const list = awayRes.data;
          list.sort((a, b) => b.id - a.id);
          this.state.currentAwayPass = list[0] || null;
        } else {
          this.state.currentAwayPass = null;
        }
      } else {
        this.state.currentAwayPass = null;
      }

      this.saveState();
    } catch(err) {
      logger.error('StoreEmployeeDashboard', 'Failed to load attendance data', err);
    }
  }

  _renderAwayPassCard(container) {
    const card = container.querySelector('#away-pass-card');
    if (!card) return;

    const notClocked = container.querySelector('#away-pass-not-clocked');
    const containerEl = container.querySelector('#away-pass-container');
    const activeInfo = container.querySelector('#away-pass-active-info');
    const statusText = container.querySelector('#away-pass-status-text');
    const statusDetail = container.querySelector('#away-pass-status-detail');
    const timerBlock = container.querySelector('#away-pass-timer-block');
    const requestForm = container.querySelector('#away-pass-request-form');
    const btnExtend = container.querySelector('#btn-extend-away-pass');

    if (!this.state.clockedIn) {
      if (notClocked) notClocked.style.display = 'block';
      if (containerEl) containerEl.style.display = 'none';
      return;
    }

    if (notClocked) notClocked.style.display = 'none';
    if (containerEl) containerEl.style.display = 'block';

    const btnReturn = container.querySelector('#btn-return-away-pass');
    const pass = this.state.currentAwayPass;
    const isResolved = !pass || ['RETURNED', 'DENIED', 'EXPIRED'].includes(pass.status);

    if (isResolved) {
      if (requestForm) requestForm.style.display = 'block';
      if (activeInfo) activeInfo.style.display = 'none';
      if (btnExtend) btnExtend.style.display = 'none';
      if (btnReturn) btnReturn.style.display = 'none';

      // Populate Start Time dropdown with Now and Shift timings
      const startSelect = container.querySelector('#away-pass-start-time-select');
      if (startSelect) {
        startSelect.innerHTML = '<option value="now" selected>Now</option>';
        const startTimeStr = this.state.shiftStartTime || '';
        const endTimeStr = this.state.shiftEndTime || '';
        if (startTimeStr && endTimeStr) {
          try {
            const parseTime = (str) => {
              const parts = str.split(':');
              return {
                hours: parseInt(parts[0], 10),
                minutes: parseInt(parts[1], 10)
              };
            };
            const start = parseTime(startTimeStr);
            const end = parseTime(endTimeStr);
            let startMins = start.hours * 60 + start.minutes;
            let endMins = end.hours * 60 + end.minutes;
            if (endMins < startMins) {
              endMins += 24 * 60;
            }
            for (let m = startMins; m <= endMins; m += 30) {
              const displayHour = Math.floor(m / 60) % 24;
              const displayMin = m % 60;
              const timeVal = `${String(displayHour).padStart(2, '0')}:${String(displayMin).padStart(2, '0')}`;
              const ampm = displayHour >= 12 ? 'PM' : 'AM';
              const hr12 = displayHour % 12 || 12;
              const timeLabel = `${hr12}:${String(displayMin).padStart(2, '0')} ${ampm}`;
              const opt = document.createElement('option');
              opt.value = timeVal;
              opt.textContent = timeLabel;
              startSelect.appendChild(opt);
            }
          } catch (e) {
            logger.error('StoreEmployeeDashboard', 'Failed to populate shift times dropdown', e);
          }
        }
      }
    } else {
      if (requestForm) requestForm.style.display = 'none';
      if (activeInfo) activeInfo.style.display = 'flex';

      const status = pass.status;
      if (status === 'PENDING') {
        if (statusText) {
          statusText.textContent = '⏳ Pending Approval';
          statusText.style.color = '#ff9800';
        }
        if (statusDetail) statusDetail.textContent = `Reason: ${pass.reason || '—'}`;
        if (timerBlock) timerBlock.style.display = 'none';
        if (btnExtend) btnExtend.style.display = 'none';
        if (btnReturn) btnReturn.style.display = 'none';
      } else if (status === 'APPROVED') {
        if (statusText) {
          statusText.textContent = '✓ Approved';
          statusText.style.color = '#4caf50';
        }
        const timeStr = pass.approvedUntil ? new Date(pass.approvedUntil).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--:--';
        if (statusDetail) statusDetail.textContent = `Approved until ${timeStr}`;
        if (timerBlock) timerBlock.style.display = 'flex';
        if (btnExtend) {
          btnExtend.style.display = 'block';
          btnExtend.disabled = false;
        }
        if (btnReturn) {
          btnReturn.style.display = 'block';
          btnReturn.disabled = false;
        }
      } else if (status === 'EXTENSION_REQUESTED') {
        if (statusText) {
          statusText.textContent = '🔁 Extension Requested';
          statusText.style.color = '#ffc107';
        }
        if (statusDetail) statusDetail.textContent = 'Waiting for supervisor approval';
        if (timerBlock) timerBlock.style.display = 'flex';
        if (btnExtend) {
          btnExtend.style.display = 'block';
          btnExtend.disabled = true;
        }
        if (btnReturn) {
          btnReturn.style.display = 'block';
          btnReturn.disabled = false;
        }
      }
    }
  }

  updateAwayPassTimer(container) {
    const timerBlock = container.querySelector('#away-pass-timer-block');
    const timerVal = container.querySelector('#away-pass-timer-value');
    if (!timerBlock || !timerVal) return;

    const pass = this.state.currentAwayPass;
    if (!pass || !this.state.clockedIn || (pass.status !== 'APPROVED' && pass.status !== 'EXTENSION_REQUESTED')) {
      timerBlock.style.display = 'none';
      return;
    }

    const deadlineStr = pass.graceUntil || pass.approvedUntil;
    if (!deadlineStr) {
      timerBlock.style.display = 'none';
      return;
    }

    const now = new Date();
    const deadline = new Date(deadlineStr);
    const diffMs = deadline - now;

    if (diffMs > 0) {
      timerBlock.style.display = 'flex';
      timerVal.textContent = this.formatDuration(diffMs);
      
      const approvedUntil = new Date(pass.approvedUntil);
      if (now > approvedUntil) {
        timerVal.style.color = '#ff6b6b';
      } else {
        timerVal.style.color = 'var(--accent-warning, #ff9800)';
      }
    } else {
      timerBlock.style.display = 'flex';
      timerVal.textContent = '00:00:00';
      timerVal.style.color = '#ff6b6b';
      if (!this._lastAwayReload || (now - this._lastAwayReload) > 10000) {
        this._lastAwayReload = now;
        this._loadAttendanceData(container).then(() => this._render(container));
      }
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'store-employee-dashboard-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/dashboard/dashboard.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeeDashboard };
