/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : attendance.js
 * Purpose           : Barista clocking system, timecard logs, and attendance discrepancy requests
 * Version           : 2.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

export default class StoreEmployeeAttendance {
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

  async loadData() {
    this.loading = true;
    try {
      const todayRes = await apiClient.request('/attendance/today');
      if (todayRes && todayRes.success) {
        this.state = todayRes.data;
      }
      const dashRes = await apiClient.request('/attendance/dashboard');
      if (dashRes && dashRes.success) {
        this.dashboard = dashRes.data;
      }
      const historyRes = await apiClient.request('/attendance/history');
      if (historyRes && historyRes.success) {
        this.logs = historyRes.data;
      }
    } catch (err) {
      logger.error('StoreEmployeeAttendance', 'Failed to load attendance data', err);
      notificationStore.danger('Error connecting to attendance service.');
    } finally {
      this.loading = false;
    }
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeAttendance', 'Mounting Barista Attendance Page...');
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
    this.startClock(container);
    
    lifecycle.onCleanup(() => {
      this.destroy();
    });
  }

  render(container) {
    if (this.loading) {
      container.innerHTML = `
        <div style="display: flex; justify-content: center; align-items: center; height: 400px; color: var(--text-muted);">
          <div class="spinner animate-spin" style="width: 24px; height: 24px; border: 2px solid var(--accent-primary); border-top-color: transparent; border-radius: 50%;"></div>
          <span style="margin-left: var(--spacing-sm); font-weight: 600;">Loading timecard data...</span>
        </div>
      `;
      return;
    }

    const checkInTimeStr = this.state.checkInTime 
      ? new Date(this.state.checkInTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) 
      : '--';

    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header banner -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0;">
              Attendance & Timecard
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              Employee: <span style="color: var(--accent-primary); font-weight: 700;">${this.user?.name || this.user?.email || 'Neha Sharma'}</span> &nbsp;·&nbsp; Current Shift: <span style="color: var(--accent-primary); font-weight: 700;">${this.dashboard.currentShift}</span>
            </p>
          </div>
          <div style="background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.72rem; font-weight: 600; color: var(--status-success); display: flex; align-items: center; gap: 6px;">
            <span style="width:6px; height:6px; border-radius:50%; background: var(--status-success); display:inline-block;"></span> Database Connected
          </div>
        </div>

        <!-- 4 Attendance Stats Cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: var(--spacing-md); width: 100%;">
          
          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
              <i data-lucide="clock" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.dashboard.workedHours}h / ${this.dashboard.requiredHours}h</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Hours Logged (Month)</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
              <i data-lucide="user-check" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.dashboard.presentDays} Days</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Shifts Attended</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(74,222,128,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
              <i data-lucide="activity" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.dashboard.attendanceRate}%</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Attendance Rate</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(239,68,68,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-danger); display:flex; align-items:center;">
              <i data-lucide="alert-circle" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.dashboard.lateCount} Late</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Late Arrivals Logged</div>
            </div>
          </div>

        </div>

        <!-- Main layout columns -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Column Left: Timecard clocker & history -->
          <div style="display: flex; flex-direction: column; gap: var(--spacing-lg); flex: 1.8;">
            
            <!-- Clock card -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Digital Punch Timecard</h3>
                <i data-lucide="clock" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>

              <div style="display: flex; align-items: center; justify-content: space-between; background: rgba(0,0,0,0.15); padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-md); border: 1px solid rgba(255,255,255,0.02); flex-wrap: wrap; gap: var(--spacing-sm);">
                <div>
                  <span style="font-size: 0.6rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; display: block; margin-bottom: 4px;">Digital Roster Time</span>
                  <div id="attendance-clock-run" style="font-size: 1.6rem; font-weight: 800; color: var(--text-primary); font-variant-numeric: tabular-nums; font-family: monospace; text-shadow: 0 0 10px rgba(201,164,106,0.2);">
                    --:--:--
                  </div>
                </div>
                <div style="text-align: right;">
                  <span style="font-size: 0.6rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; display: block; margin-bottom: 4px;">Duty Status</span>
                  <span style="font-size: 0.8rem; font-weight: 800; color: ${this.state.clockedIn ? 'var(--status-success)' : 'var(--status-danger)'}; display: inline-flex; align-items: center; gap: 4px;">
                    <span style="width: 6px; height: 6px; border-radius: 50%; background: ${this.state.clockedIn ? 'var(--status-success)' : 'var(--status-danger)'}; display: inline-block;"></span>
                    ${this.state.clockedIn ? `Clocked In since ${checkInTimeStr}` : 'Clocked Out'}
                  </span>
                </div>
              </div>

              <div style="display: flex; gap: var(--spacing-sm); margin-top: 4px; flex-wrap: wrap;">
                <button class="btn" id="btn-att-clock-in" style="flex: 1; min-width: 120px; font-weight: 700; padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; font-size: 0.8rem; background: var(--status-success); color: #fff; box-shadow: 0 4px 10px rgba(74,222,128,0.2); transition: var(--transition-fast); ${this.state.clockedIn ? 'opacity:0.4; cursor:not-allowed; box-shadow:none;' : ''}" ${this.state.clockedIn ? 'disabled' : ''}>
                  <i data-lucide="play" style="width: 14px; height: 14px;"></i> Clock In
                </button>

                <button class="btn" id="btn-att-break-start" style="flex: 1; min-width: 120px; font-weight: 700; padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; font-size: 0.8rem; background: var(--accent-primary); color: #000; transition: var(--transition-fast); ${!this.state.clockedIn || this.state.breakActive ? 'opacity:0.4; cursor:not-allowed;' : ''}" ${!this.state.clockedIn || this.state.breakActive ? 'disabled' : ''}>
                  <i data-lucide="coffee" style="width: 14px; height: 14px;"></i> Break Start
                </button>

                <button class="btn" id="btn-att-break-end" style="flex: 1; min-width: 120px; font-weight: 700; padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; font-size: 0.8rem; background: #6366f1; color: #fff; transition: var(--transition-fast); ${!this.state.clockedIn || !this.state.breakActive ? 'opacity:0.4; cursor:not-allowed;' : ''}" ${!this.state.clockedIn || !this.state.breakActive ? 'disabled' : ''}>
                  <i data-lucide="user-check" style="width: 14px; height: 14px;"></i> Break End
                </button>

                <button class="btn" id="btn-att-clock-out" style="flex: 1; min-width: 120px; font-weight: 700; padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; font-size: 0.8rem; background: var(--status-danger); color: #fff; box-shadow: 0 4px 10px rgba(239,68,68,0.2); transition: var(--transition-fast); ${!this.state.clockedIn ? 'opacity:0.4; cursor:not-allowed; box-shadow:none;' : ''}" ${!this.state.clockedIn ? 'disabled' : ''}>
                  <i data-lucide="square" style="width: 14px; height: 14px;"></i> Clock Out
                </button>
              </div>
            </div>

            <!-- Attendance History -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Timecard Logs</h3>
                <i data-lucide="history" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>

              <div style="overflow-x: auto; width: 100%;">
                <table style="width: 100%; border-collapse: collapse; font-size: 0.76rem; text-align: left;">
                  <thead>
                    <tr style="border-bottom: 1.5px solid var(--border-color); color: var(--text-muted); text-transform: uppercase; font-weight: 700;">
                      <th style="padding: var(--spacing-sm) 6px;">Date</th>
                      <th style="padding: var(--spacing-sm) 6px;">Shift Slot</th>
                      <th style="padding: var(--spacing-sm) 6px;">Punch In</th>
                      <th style="padding: var(--spacing-sm) 6px;">Punch Out</th>
                      <th style="padding: var(--spacing-sm) 6px;">Logged Hours</th>
                      <th style="padding: var(--spacing-sm) 6px;">Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    ${this.logs.length === 0 ? `
                      <tr>
                        <td colspan="6" style="padding: var(--spacing-md); text-align: center; color: var(--text-muted);">No attendance records found in DB.</td>
                      </tr>
                    ` : this.logs.map(log => `
                      <tr style="border-bottom: 1px solid rgba(255,255,255,0.05); hover: background: rgba(255,255,255,0.02);">
                        <td style="padding: var(--spacing-sm) 6px; font-weight:600; color:var(--text-primary);">${log.date}</td>
                        <td style="padding: var(--spacing-sm) 6px; color:var(--text-muted);">${log.shift}</td>
                        <td style="padding: var(--spacing-sm) 6px; font-variant-numeric: tabular-nums;">${log.checkIn}</td>
                        <td style="padding: var(--spacing-sm) 6px; font-variant-numeric: tabular-nums;">${log.checkOut}</td>
                        <td style="padding: var(--spacing-sm) 6px; font-weight:700;">${log.hours}</td>
                        <td style="padding: var(--spacing-sm) 6px; font-weight: 800; color: ${
                          log.status === 'ACTIVE' || log.status === 'ON_BREAK' ? 'var(--status-info)' : 
                          log.status === 'PRESENT' ? 'var(--status-success)' : 
                          log.status === 'LEAVE' ? 'var(--accent-primary)' : 'var(--status-danger)'
                        };">${log.status}</td>
                      </tr>
                    `).join('')}
                  </tbody>
                </table>
              </div>
            </div>

          </div>

          <!-- Column Right: File Attendance Discrepancy -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; height: fit-content; flex: 1;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">File Discrepancy</h3>
              <i data-lucide="alert-triangle" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); font-size: 0.76rem;">
              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Shift Date</label>
                <input type="date" id="discrepancy-date" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
              </div>

              <div style="display: grid; grid-template-columns: 1fr 1fr; gap: var(--spacing-sm);">
                <div style="display: flex; flex-direction: column; gap: 4px;">
                  <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Corrected Punch In</label>
                  <input type="text" id="discrepancy-in" placeholder="e.g. 08:00 AM" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
                </div>
                <div style="display: flex; flex-direction: column; gap: 4px;">
                  <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Corrected Punch Out</label>
                  <input type="text" id="discrepancy-out" placeholder="e.g. 04:00 PM" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
                </div>
              </div>

              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Correction Reason</label>
                <textarea id="discrepancy-reason" placeholder="Explain the timecard correction..." rows="3" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none; font-family: inherit; resize: none;"></textarea>
              </div>

              <button class="btn" id="btn-submit-discrepancy" style="background: var(--accent-primary); color: #000; font-weight: 800; border: none; border-radius: var(--radius-md); padding: var(--spacing-sm); cursor: pointer; transition: var(--transition-fast); margin-top: 4px;">
                Submit Timecard Correction
              </button>
            </div>
          </div>

        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const clockInBtn = container.querySelector('#btn-att-clock-in');
    const clockOutBtn = container.querySelector('#btn-att-clock-out');
    const breakStartBtn = container.querySelector('#btn-att-break-start');
    const breakEndBtn = container.querySelector('#btn-att-break-end');

    if (clockInBtn) {
      clockInBtn.addEventListener('click', async () => {
        let gps = null;
        try {
          const res = await apiClient.request('/attendance/check-in', {
            method: 'POST',
            body: JSON.stringify({ gps, device: navigator.userAgent })
          });
          if (res && res.success) {
            notificationStore.success('Successfully clocked in for your shift.');
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Clock-in failed.');
          }
        } catch (err) {
          notificationStore.danger('Error clocking in: ' + err.message);
        }
      });
    }

    if (clockOutBtn) {
      clockOutBtn.addEventListener('click', async () => {
        try {
          const res = await apiClient.request('/attendance/check-out', {
            method: 'POST'
          });
          if (res && res.success) {
            notificationStore.success('Successfully clocked out of your shift.');
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Clock-out failed.');
          }
        } catch (err) {
          notificationStore.danger('Error clocking out: ' + err.message);
        }
      });
    }

    if (breakStartBtn) {
      breakStartBtn.addEventListener('click', async () => {
        try {
          const res = await apiClient.request('/attendance/break/start', {
            method: 'POST'
          });
          if (res && res.success) {
            notificationStore.success('Successfully started break.');
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to start break.');
          }
        } catch (err) {
          notificationStore.danger('Error starting break: ' + err.message);
        }
      });
    }

    if (breakEndBtn) {
      breakEndBtn.addEventListener('click', async () => {
        try {
          const res = await apiClient.request('/attendance/break/end', {
            method: 'POST'
          });
          if (res && res.success) {
            notificationStore.success('Successfully ended break.');
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to end break.');
          }
        } catch (err) {
          notificationStore.danger('Error ending break: ' + err.message);
        }
      });
    }

    // Discrepancy Form submit
    const submitDiscBtn = container.querySelector('#btn-submit-discrepancy');
    if (submitDiscBtn) {
      submitDiscBtn.addEventListener('click', async () => {
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
          if (res && res.success) {
            notificationStore.success('Discrepancy correction filed for store manager review.');
            dateInput.value = '';
            inInput.value = '';
            outInput.value = '';
            reasonInput.value = '';
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Correction submission failed.');
          }
        } catch (err) {
          notificationStore.danger('Error filing correction: ' + err.message);
        }
      });
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
}
