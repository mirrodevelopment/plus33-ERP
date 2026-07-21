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
      earlyCheckoutCount: 0,
      workedDays: 0,
      leaveLeft: 0,
      leaveUsed: 0
    };
    this.logs = [];
    this.loading = false;
    this._clockInterval = null;

    const now = new Date();
    this.currentYear = now.getFullYear();
    this.currentMonth = now.getMonth() + 1;
    this.calendarData = [];
    this.calendarExpanded = false;
    this.casualRemaining = 0;
    this.casualUsed = 0;
    this.policyData = null;
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
        this.state = { ...this.state, ...todayRes.data };
      }
      const dashRes = await apiClient.request('/attendance/dashboard');
      if (dashRes?.success) {
        this.dashboard = { ...this.dashboard, ...dashRes.data };
      }
      const historyRes = await apiClient.request('/attendance/history');
      if (historyRes?.success) {
        this.logs = historyRes.data || [];
      }
      const calRes = await apiClient.get('/api/v1/attendance/calendar', { year: this.currentYear, month: this.currentMonth });
      if (calRes?.success) {
        this.calendarData = calRes.data || [];
      }
      try {
        const leaveBalRes = await apiClient.get('/api/v1/leaves/my/balance');
        if (leaveBalRes?.success && leaveBalRes.data) {
          const casualBal = leaveBalRes.data.find(b => b.leaveTypeCode === 'CASUAL');
          this.casualRemaining = casualBal ? casualBal.remaining : 0;
          this.casualUsed = casualBal ? casualBal.used : 0;
        } else {
          this.casualRemaining = 0;
          this.casualUsed = 0;
        }
      } catch (e) {
        logger.error('StoreEmployeeAttendance', 'Failed to fetch leave balances', e);
        this.casualRemaining = 0;
        this.casualUsed = 0;
      }

      try {
        const policyRes = await apiClient.get('/country-work-policy');
        if (policyRes?.success) {
          this.policyData = policyRes.data;
        } else {
          this.policyData = null;
        }
      } catch (e) {
        logger.error('StoreEmployeeAttendance', 'Failed to fetch country work policy', e);
        this.policyData = null;
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
    const workedDaysEl = container.querySelector('#kpi-worked-days');
    const workedHoursEl = container.querySelector('#kpi-worked-hours');
    const casualLeftEl = container.querySelector('#kpi-casual-leave-left');
    const casualUsedEl = container.querySelector('#kpi-casual-leave-used');

    if (workedDaysEl) workedDaysEl.textContent = `${this.dashboard.workedDays || 0} Days`;
    if (workedHoursEl) workedHoursEl.textContent = `${this.dashboard.workedHours || 0} hrs`;
    if (casualLeftEl) casualLeftEl.textContent = `${this.casualRemaining || 0} Days`;
    if (casualUsedEl) casualUsedEl.textContent = `${this.casualUsed || 0} Days`;

    // 2.5. Sync Policy Modal values dynamically
    if (this.policyData) {
      const graceEl = container.querySelector('#policy-grace-period');
      const reqHrsEl = container.querySelector('#policy-required-hours');
      const otThreshEl = container.querySelector('#policy-ot-threshold');
      const otRateEl = container.querySelector('#policy-ot-rate');
      const nightRateEl = container.querySelector('#policy-night-rate');
      const nightStartEl = container.querySelector('#policy-night-start');
      const nightEndEl = container.querySelector('#policy-night-end');
      const holidayRateEl = container.querySelector('#policy-holiday-rate');

      if (graceEl) graceEl.textContent = this.policyData.gracePeriodMinutes ?? '—';
      if (reqHrsEl) reqHrsEl.textContent = this.policyData.weeklyRequiredHours ?? '—';
      if (otThreshEl) otThreshEl.textContent = this.policyData.overtimeThresholdHours ?? '—';
      if (otRateEl) otRateEl.textContent = this.policyData.overtimeRate ?? '—';
      if (nightRateEl) nightRateEl.textContent = this.policyData.nightOvertimeRate ?? '—';
      if (nightStartEl) nightStartEl.textContent = this.policyData.nightStart ?? '—';
      if (nightEndEl) nightEndEl.textContent = this.policyData.nightEnd ?? '—';
      if (holidayRateEl) holidayRateEl.textContent = this.policyData.holidayOvertimeRate ?? '—';
    }

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

    // 6. Render Calendar Grid
    this._renderCalendarGrid(container);

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

    const prevBtn = container.querySelector('#btn-calendar-prev');
    const nextBtn = container.querySelector('#btn-calendar-next');
    const toggleBtn = container.querySelector('#btn-toggle-calendar');

    if (clockInBtn) {
      const handleClockIn = async () => {
        clockInBtn.disabled = true;
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
            clockInBtn.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error clocking in: ' + err.message);
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
          const res = await apiClient.request('/attendance/check-out', { method: 'POST' });
          if (res?.success) {
            notificationStore.success('Successfully clocked out of your shift.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Clock-out failed.');
            clockOutBtn.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error clocking out: ' + err.message);
          clockOutBtn.disabled = false;
        }
      };
      clockOutBtn.addEventListener('click', handleClockOut);
      lifecycle.onCleanup(() => clockOutBtn.removeEventListener('click', handleClockOut));
    }

    if (breakStartBtn) {
      const handleBreakStart = async () => {
        breakStartBtn.disabled = true;
        try {
          const res = await apiClient.request('/attendance/break/start', { method: 'POST' });
          if (res?.success) {
            notificationStore.success('Successfully started break.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to start break.');
            breakStartBtn.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error starting break: ' + err.message);
          breakStartBtn.disabled = false;
        }
      };
      breakStartBtn.addEventListener('click', handleBreakStart);
      lifecycle.onCleanup(() => breakStartBtn.removeEventListener('click', handleBreakStart));
    }

    if (breakEndBtn) {
      const handleBreakEnd = async () => {
        breakEndBtn.disabled = true;
        try {
          const res = await apiClient.request('/attendance/break/end', { method: 'POST' });
          if (res?.success) {
            notificationStore.success('Successfully ended break.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to end break.');
            breakEndBtn.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error ending break: ' + err.message);
          breakEndBtn.disabled = false;
        }
      };
      breakEndBtn.addEventListener('click', handleBreakEnd);
      lifecycle.onCleanup(() => breakEndBtn.removeEventListener('click', handleBreakEnd));
    }

    if (submitDiscBtn) {
      const handleSubmitCorrection = async () => {
        submitDiscBtn.disabled = true;
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
          submitDiscBtn.disabled = false;
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
            submitDiscBtn.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error filing correction: ' + err.message);
          submitDiscBtn.disabled = false;
        }
      };
      submitDiscBtn.addEventListener('click', handleSubmitCorrection);
      lifecycle.onCleanup(() => submitDiscBtn.removeEventListener('click', handleSubmitCorrection));
    }

    if (prevBtn) {
      const handlePrev = async () => {
        this.currentMonth--;
        if (this.currentMonth < 1) {
          this.currentMonth = 12;
          this.currentYear--;
        }
        await this.loadCalendarData(container);
      };
      prevBtn.addEventListener('click', handlePrev);
      lifecycle.onCleanup(() => prevBtn.removeEventListener('click', handlePrev));
    }

    if (nextBtn) {
      const handleNext = async () => {
        this.currentMonth++;
        if (this.currentMonth > 12) {
          this.currentMonth = 1;
          this.currentYear++;
        }
        await this.loadCalendarData(container);
      };
      nextBtn.addEventListener('click', handleNext);
      lifecycle.onCleanup(() => nextBtn.removeEventListener('click', handleNext));
    }

    if (toggleBtn) {
      const handleToggle = () => {
        this.calendarExpanded = !this.calendarExpanded;
        
        if (this.calendarExpanded) {
          toggleBtn.innerHTML = `<i data-lucide="chevrons-up" style="width: 14px; height: 14px;"></i> Collapse to Weekly View`;
        } else {
          toggleBtn.innerHTML = `<i data-lucide="chevrons-down" style="width: 14px; height: 14px;"></i> Expand Monthly View`;
        }
        if (window.lucide) window.lucide.createIcons();

        const navControls = container.querySelector('#calendar-nav-controls');
        if (navControls) {
          navControls.style.display = this.calendarExpanded ? 'flex' : 'none';
        }

        this._renderCalendarGrid(container);
      };
      toggleBtn.addEventListener('click', handleToggle);
      lifecycle.onCleanup(() => toggleBtn.removeEventListener('click', handleToggle));
    }

    // Bind Attendance Policy modal
    const openPolicyBtn = container.querySelector('#btn-open-policy');
    const policyModal = container.querySelector('#policy-modal');
    const closePolicyBtn = container.querySelector('#btn-close-policy-modal');

    if (openPolicyBtn && policyModal) {
      const handleOpenPolicy = () => {
        policyModal.style.display = 'flex';
        policyModal.setAttribute('aria-hidden', 'false');
      };
      openPolicyBtn.addEventListener('click', handleOpenPolicy);
      lifecycle.onCleanup(() => openPolicyBtn.removeEventListener('click', handleOpenPolicy));
    }

    if (closePolicyBtn && policyModal) {
      const handleClosePolicy = () => {
        policyModal.style.display = 'none';
        policyModal.setAttribute('aria-hidden', 'true');
      };
      closePolicyBtn.addEventListener('click', handleClosePolicy);
      lifecycle.onCleanup(() => closePolicyBtn.removeEventListener('click', handleClosePolicy));
    }

    if (policyModal) {
      const handleOverlayClick = (e) => {
        if (e.target === policyModal) {
          policyModal.style.display = 'none';
          policyModal.setAttribute('aria-hidden', 'true');
        }
      };
      policyModal.addEventListener('click', handleOverlayClick);
      lifecycle.onCleanup(() => policyModal.removeEventListener('click', handleOverlayClick));
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

  async loadCalendarData(container) {
    try {
      const calRes = await apiClient.get('/api/v1/attendance/calendar', { year: this.currentYear, month: this.currentMonth });
      if (calRes?.success) {
        this.calendarData = calRes.data || [];
        this._renderCalendarGrid(container);
      }
    } catch (err) {
      logger.error('StoreEmployeeAttendance', 'Failed to load calendar data', err);
    }
  }

  _renderCalendarGrid(container) {
    const gridContainer = container.querySelector('#attendance-calendar-grid');
    const monthYearLbl = container.querySelector('#calendar-month-year');
    if (!gridContainer || !monthYearLbl) return;

    gridContainer.replaceChildren();

    const date = new Date(this.currentYear, this.currentMonth - 1, 1);
    monthYearLbl.textContent = date.toLocaleDateString('en-US', { month: 'long', year: 'numeric' });

    const firstDayIndex = new Date(this.currentYear, this.currentMonth - 1, 1).getDay();
    const daysInMonth = new Date(this.currentYear, this.currentMonth, 0).getDate();
    const daysInPrevMonth = new Date(this.currentYear, this.currentMonth - 1, 0).getDate();

    const cells = [];

    for (let i = firstDayIndex - 1; i >= 0; i--) {
      const prevDayVal = daysInPrevMonth - i;
      const prevMonthVal = this.currentMonth - 1 === 0 ? 12 : this.currentMonth - 1;
      const prevYearVal = this.currentMonth - 1 === 0 ? this.currentYear - 1 : this.currentYear;
      
      cells.push({
        dayNumber: prevDayVal,
        dateStr: `${prevYearVal}-${String(prevMonthVal).padStart(2, '0')}-${String(prevDayVal).padStart(2, '0')}`,
        isCurrentMonth: false
      });
    }

    for (let day = 1; day <= daysInMonth; day++) {
      cells.push({
        dayNumber: day,
        dateStr: `${this.currentYear}-${String(this.currentMonth).padStart(2, '0')}-${String(day).padStart(2, '0')}`,
        isCurrentMonth: true
      });
    }

    const totalCellsSoFar = cells.length;
    const remainingCells = 42 - totalCellsSoFar;
    for (let i = 1; i <= remainingCells; i++) {
      const nextMonthVal = this.currentMonth + 1 === 13 ? 1 : this.currentMonth + 1;
      const nextYearVal = this.currentMonth + 1 === 13 ? this.currentYear + 1 : this.currentYear;
      
      cells.push({
        dayNumber: i,
        dateStr: `${nextYearVal}-${String(nextMonthVal).padStart(2, '0')}-${String(i).padStart(2, '0')}`,
        isCurrentMonth: false
      });
    }

    let startIndex = 0;
    let endIndex = 42;

    if (!this.calendarExpanded) {
      const today = new Date();
      const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
      
      let todayCellIndex = cells.findIndex(c => c.dateStr === todayStr);
      if (todayCellIndex === -1) {
        const todayLocal = new Date(today.getTime() - today.getTimezoneOffset() * 60000).toISOString().split('T')[0];
        todayCellIndex = cells.findIndex(c => c.dateStr === todayLocal);
      }
      
      if (todayCellIndex !== -1) {
        const rowIndex = Math.floor(todayCellIndex / 7);
        startIndex = rowIndex * 7;
        endIndex = startIndex + 7;
      } else {
        startIndex = 0;
        endIndex = 7;
      }
    }

    for (let i = startIndex; i < endIndex; i++) {
      const c = cells[i];
      const cell = document.createElement('div');
      cell.className = 'calendar-day-cell';
      if (c.isCurrentMonth) {
        cell.classList.add('current-month');
      } else {
        cell.classList.add('pad');
      }

      const num = document.createElement('div');
      num.className = 'calendar-day-number';
      num.textContent = c.dayNumber;
      cell.appendChild(num);

      const record = this.calendarData.find(r => r.date === c.dateStr);
      if (record) {
        if (record.status === 'PRESENT' || record.status === 'ACTIVE' || record.status === 'ON_BREAK') {
          cell.classList.add('status-present');
        } else if (record.status === 'HALF_DAY') {
          cell.classList.add('status-half-day');
        } else if (record.status === 'ABSENT') {
          cell.classList.add('status-absent');
        } else if (record.status === 'LEAVE') {
          cell.classList.add('status-leave');
        } else if (record.status === 'HOLIDAY') {
          cell.classList.add('status-holiday');
        } else if (record.status === 'WEEKLY_OFF') {
          cell.classList.add('status-weekly-off');
        }

        if (record.hoursWorked && record.hoursWorked > 0) {
          const hrs = document.createElement('div');
          hrs.className = 'calendar-day-hours';
          hrs.textContent = `${record.hoursWorked}h`;
          cell.appendChild(hrs);
        }
      }

      gridContainer.appendChild(cell);
    }
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
