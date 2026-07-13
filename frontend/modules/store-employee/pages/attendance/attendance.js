/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : attendance.js
 * Path              : frontend/modules/store-employee/attendance/attendance.js
 * Purpose           : Controller component for Barista clocking system UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/attendance/attendance.html
 * Related CSS       : frontend/modules/store-employee/attendance/attendance.css
 * Related APIs      : GET /attendance/today
 *                     GET /attendance/dashboard
 *                     GET /attendance/history
 *                     POST /attendance/check-in
 *                     POST /attendance/check-out
 *                     POST /attendance/break/start
 *                     POST /attendance/break/end
 *                     PUT /attendance/correction
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in attendance.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the attendance HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/attendance/attendance.html';

export default class StoreEmployeeAttendance {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.state = {
      clockedIn: false,
      status: 'ABSENT',
      checkInTime: null,
      checkOutTime: null,
      breakActive: false
    };
    this.dashboard = {
      presentDays: 0,
      absentDays: 0,
      leaveDays: 0,
      holidayCount: 0,
      weeklyOff: 0,
      attendanceRate: 100,
      requiredHours: 0,
      workedHours: 0,
      remainingHours: 0,
      overtime: 0,
      nightOvertime: 0,
      holidayOvertime: 0,
      currentShift: 'Morning Shift',
      currentStatus: 'Inactive',
      averageHours: 0,
      lateCount: 0,
      earlyCheckoutCount: 0
    };
    this.logs = [];
    this.loading = false;
    this._clockInterval = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeeAttendance component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeAttendance', 'Mounting Barista Attendance Page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout template with loading state
    this._renderLoading(container);

    // 2. Fetch all telemetry and logs data from database
    await this.loadData();

    // 3. Inject full layout
    await this._loadTemplate(container);

    // 4. Render details
    this.render(container);

    // 5. Bind event listeners
    this.bindEvents(container, lifecycle);

    // 6. Start running clock
    this.startClock(container);

    lifecycle.onCleanup(() => {
      this.destroy();
    });
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // DATA TELEMETRY SUB-ROUTINES
  // ---------------------------------------------------------------------------

  async loadData() {
    this.loading = true;
    try {
      const todayRes = await apiClient.request('/attendance/today');
      if (todayRes?.success) {
        this.state = todayRes.data;
      }
      const dashRes = await apiClient.request('/attendance/dashboard');
      if (dashRes?.success) {
        this.dashboard = dashRes.data;
      }
      const historyRes = await apiClient.request('/attendance/history');
      if (historyRes?.success) {
        this.logs = historyRes.data;
      }
    } catch (err) {
      logger.error('StoreEmployeeAttendance', 'Failed to load attendance data', err);
      notificationStore.danger('Error connecting to attendance service.');
    } finally {
      this.loading = false;
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
    // 1. Sync header text
    const nameEl = container.querySelector('#lbl-employee-name');
    const shiftEl = container.querySelector('#lbl-current-shift');

    if (nameEl) nameEl.textContent = this.user?.name || this.user?.email || 'Neha Sharma';
    if (shiftEl) shiftEl.textContent = this.dashboard.currentShift;

    // 2. Sync KPIs values
    const loggedHoursEl = container.querySelector('#kpi-logged-hours');
    const shiftsAttendedEl = container.querySelector('#kpi-shifts-attended');
    const rateEl = container.querySelector('#kpi-attendance-rate');
    const lateEl = container.querySelector('#kpi-late-arrivals');

    if (loggedHoursEl) loggedHoursEl.textContent = `${this.dashboard.workedHours}h / ${this.dashboard.requiredHours}h`;
    if (shiftsAttendedEl) shiftsAttendedEl.textContent = `${this.dashboard.presentDays} Days`;
    if (rateEl) rateEl.textContent = `${this.dashboard.attendanceRate}%`;
    if (lateEl) lateEl.textContent = `${this.dashboard.lateCount} Late`;

    // 3. Render Duty Status details
    const checkInTimeStr = this.state.checkInTime 
      ? new Date(this.state.checkInTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) 
      : '--';

    const dutyStatusWrapper = container.querySelector('#lbl-duty-status');
    if (dutyStatusWrapper) {
      if (this.state.clockedIn) {
        dutyStatusWrapper.className = 'duty-status-wrapper color-active';
        dutyStatusWrapper.innerHTML = `
          <span class="status-dot status-dot--active"></span>
          <span>Clocked In since ${checkInTimeStr}</span>
        `;
      } else {
        dutyStatusWrapper.className = 'duty-status-wrapper color-inactive';
        dutyStatusWrapper.innerHTML = `
          <span class="status-dot status-dot--inactive"></span>
          <span>Clocked Out</span>
        `;
      }
    }

    // 4. Sync clock buttons enabled states
    const btnClockIn = container.querySelector('#btn-att-clock-in');
    const btnBreakStart = container.querySelector('#btn-att-break-start');
    const btnBreakEnd = container.querySelector('#btn-att-break-end');
    const btnClockOut = container.querySelector('#btn-att-clock-out');

    if (btnClockIn) {
      btnClockIn.disabled = this.state.clockedIn;
    }
    if (btnBreakStart) {
      btnBreakStart.disabled = !this.state.clockedIn || this.state.breakActive;
    }
    if (btnBreakEnd) {
      btnBreakEnd.disabled = !this.state.clockedIn || !this.state.breakActive;
    }
    if (btnClockOut) {
      btnClockOut.disabled = !this.state.clockedIn;
    }

    // 5. Render Time logs list rows
    this._renderLogsTable(container);

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const clockInBtn = container.querySelector('#btn-att-clock-in');
    const clockOutBtn = container.querySelector('#btn-att-clock-out');
    const breakStartBtn = container.querySelector('#btn-att-break-start');
    const breakEndBtn = container.querySelector('#btn-att-break-end');
    const submitDiscBtn = container.querySelector('#btn-submit-discrepancy');

    if (clockInBtn) {
      const handleClockIn = async () => {
        let gps = null;
        try {
          const res = await apiClient.request('/attendance/check-in', {
            method: 'POST',
            body: JSON.stringify({ gps, device: navigator.userAgent })
          });
          if (res?.success) {
            notificationStore.success('Successfully clocked in for your shift.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Clock-in failed.');
          }
        } catch (err) {
          notificationStore.danger('Error clocking in: ' + err.message);
        }
      };
      clockInBtn.addEventListener('click', handleClockIn);
      lifecycle.onCleanup(() => clockInBtn.removeEventListener('click', handleClockIn));
    }

    if (clockOutBtn) {
      const handleClockOut = async () => {
        try {
          const res = await apiClient.request('/attendance/check-out', { method: 'POST' });
          if (res?.success) {
            notificationStore.success('Successfully clocked out of your shift.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Clock-out failed.');
          }
        } catch (err) {
          notificationStore.danger('Error clocking out: ' + err.message);
        }
      };
      clockOutBtn.addEventListener('click', handleClockOut);
      lifecycle.onCleanup(() => clockOutBtn.removeEventListener('click', handleClockOut));
    }

    if (breakStartBtn) {
      const handleBreakStart = async () => {
        try {
          const res = await apiClient.request('/attendance/break/start', { method: 'POST' });
          if (res?.success) {
            notificationStore.success('Successfully started break.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to start break.');
          }
        } catch (err) {
          notificationStore.danger('Error starting break: ' + err.message);
        }
      };
      breakStartBtn.addEventListener('click', handleBreakStart);
      lifecycle.onCleanup(() => breakStartBtn.removeEventListener('click', handleBreakStart));
    }

    if (breakEndBtn) {
      const handleBreakEnd = async () => {
        try {
          const res = await apiClient.request('/attendance/break/end', { method: 'POST' });
          if (res?.success) {
            notificationStore.success('Successfully ended break.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to end break.');
          }
        } catch (err) {
          notificationStore.danger('Error ending break: ' + err.message);
        }
      };
      breakEndBtn.addEventListener('click', handleBreakEnd);
      lifecycle.onCleanup(() => breakEndBtn.removeEventListener('click', handleBreakEnd));
    }

    if (submitDiscBtn) {
      const handleSubmitCorrection = async () => {
        const dateInput = container.querySelector('#discrepancy-date');
        const inInput = container.querySelector('#discrepancy-in');
        const outInput = container.querySelector('#discrepancy-out');
        const reasonInput = container.querySelector('#discrepancy-reason');

        const date = dateInput.value.trim();
        const punchIn = inInput.value.trim();
        const punchOut = outInput.value.trim();
        const reason = reasonInput.value.trim();

        if (!date || !punchIn || !punchOut || !reason) {
          notificationStore.danger('Please fill out all discrepancy details.');
          return;
        }

        try {
          const res = await apiClient.request('/attendance/correction', {
            method: 'PUT',
            body: JSON.stringify({ date, reason, checkIn: punchIn, checkOut: punchOut })
          });
          if (res?.success) {
            notificationStore.success('Discrepancy correction filed for store manager review.');
            dateInput.value = '';
            inInput.value = '';
            outInput.value = '';
            reasonInput.value = '';
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Correction submission failed.');
          }
        } catch (err) {
          notificationStore.danger('Error filing correction: ' + err.message);
        }
      };
      submitDiscBtn.addEventListener('click', handleSubmitCorrection);
      lifecycle.onCleanup(() => submitDiscBtn.removeEventListener('click', handleSubmitCorrection));
    }
  }

  startClock(container) {
    const updateTime = () => {
      const clockEl = container.querySelector('#attendance-clock-run');
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

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderLoading(container) {
    container.innerHTML = `
      <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
        <i data-lucide="loader-2" class="animate-spin" style="width:32px;height:32px;color:var(--accent-primary);"></i>
        <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading timecard data...</span>
      </div>`;
    if (window.lucide) window.lucide.createIcons();
  }

  _renderLogsTable(container) {
    const tbody = container.querySelector('#attendance-log-tbody');
    const emptyTpl = container.querySelector('#attendance-empty-row-tpl');
    const rowTpl = container.querySelector('#attendance-row-tpl');

    if (!tbody) return;

    tbody.replaceChildren();

    if (this.logs.length === 0) {
      if (emptyTpl) {
        tbody.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    this.logs.forEach(log => {
      if (!rowTpl) return;
      const clone = rowTpl.content.cloneNode(true);

      const dateEl = clone.querySelector('.log-date-cell');
      const shiftEl = clone.querySelector('.log-shift-cell');
      const checkInEl = clone.querySelector('.log-check-in-cell');
      const checkOutEl = clone.querySelector('.log-check-out-cell');
      const hoursEl = clone.querySelector('.log-hours-cell');
      const statusEl = clone.querySelector('.log-status-cell');

      if (dateEl) dateEl.textContent = log.date;
      if (shiftEl) shiftEl.textContent = log.shift;
      if (checkInEl) checkInEl.textContent = log.checkIn;
      if (checkOutEl) checkOutEl.textContent = log.checkOut;
      if (hoursEl) hoursEl.textContent = log.hours;

      if (statusEl) {
        statusEl.textContent = log.status;
        if (log.status === 'ACTIVE' || log.status === 'ON_BREAK') {
          statusEl.classList.add('log-status-cell--active');
        } else if (log.status === 'PRESENT') {
          statusEl.classList.add('log-status-cell--present');
        } else if (log.status === 'LEAVE') {
          statusEl.classList.add('log-status-cell--leave');
        } else {
          statusEl.classList.add('log-status-cell--absent');
        }
      }

      tbody.appendChild(clone);
    });
  }

  _loadCss() {
    const cssId = 'store-employee-attendance-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/attendance/attendance.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeeAttendance };
