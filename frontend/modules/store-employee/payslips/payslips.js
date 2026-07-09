/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : payslips.js
 * Path              : frontend/modules/store-employee/payslips/payslips.js
 * Purpose           : Barista payroll records, detailed payslips breakups, and queries from DB snapshots
 * Version           : 2.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

export default class StoreEmployeePayslips {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.payslipsHistory = [];
    this.loading = false;
  }

  async loadData() {
    this.loading = true;
    try {
      const response = await apiClient.request('/api/v2/employee-self-service/payslips');
      if (response) {
        // The API returns the list directly or wrapped in standard ApiResponse envelope
        this.payslipsHistory = response.success ? (response.data || []) : response;
      }
    } catch (err) {
      logger.error('StoreEmployeePayslips', 'Failed to load payslips from database', err);
      notificationStore.danger('Error connecting to payroll service.');
    } finally {
      this.loading = false;
    }
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeePayslips', 'Mounting Barista Payslips Page...');
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  render(container) {
    if (this.loading) {
      container.innerHTML = `
        <div style="display: flex; justify-content: center; align-items: center; height: 400px; color: var(--text-muted);">
          <div class="spinner animate-spin" style="width: 24px; height: 24px; border: 2px solid var(--accent-primary); border-top-color: transparent; border-radius: 50%;"></div>
          <span style="margin-left: var(--spacing-sm); font-weight: 600;">Loading paycheck data...</span>
        </div>
      `;
      return;
    }

    const latest = this.payslipsHistory[0] || { grossPay: 0, netPay: 0, deductions: 0, currencyCode: 'EUR' };
    const currencySym = latest.currencyCode === 'EUR' ? '€' : '$';

    // Parse latest allowances from snapshot if available
    let latestAllowances = 0;
    if (latest.overtimeSnapshot) {
      try {
        const snap = JSON.parse(latest.overtimeSnapshot);
        latestAllowances = snap.overtimePay + snap.nightPay + snap.holidayPay + snap.weekendPay + snap.attendanceBonus + snap.performanceBonus + snap.allowances;
      } catch (e) {
        latestAllowances = 0;
      }
    }

    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header ribbon -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0;">
              Earnings Statement
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              Employee: <span style="color: var(--accent-primary); font-weight: 700;">${this.profile.name || this.user?.username || 'Neha Sharma'}</span> &nbsp;·&nbsp; Role: <span style="color: var(--accent-primary); font-weight: 700;">${this.user?.role || 'Barista'}</span>
            </p>
          </div>
          <div style="background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.72rem; font-weight: 600; color: var(--status-success); display: flex; align-items: center; gap: 6px;">
            <i data-lucide="check" style="width: 14px; height: 14px;"></i> Salary Account Active (France FR Region)
          </div>
        </div>

        <!-- 4 Payroll KPI Cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: var(--spacing-md); width: 100%;">
          
          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(130,163,125,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
              <i data-lucide="banknote" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${currencySym}${latest.netPay.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Net Salary (Latest)</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(255,255,255,0.05); border-radius: var(--radius-md); padding: 10px; color: var(--text-primary); display:flex; align-items:center;">
              <i data-lucide="wallet" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${currencySym}${latest.grossPay.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Gross Earnings</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
              <i data-lucide="plus-circle" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${currencySym}${latestAllowances.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Overtime &amp; Allowances</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(239,68,68,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-danger); display:flex; align-items:center;">
              <i data-lucide="minus-circle" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${currencySym}${latest.deductions.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Deductions &amp; Taxes</div>
            </div>
          </div>

        </div>

        <!-- Main layout grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Column Left: Payslip releases table logs -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; flex: 1.6;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Payroll Release History</h3>
              <i data-lucide="history" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <div style="overflow-x: auto; width: 100%;">
              <table style="width: 100%; border-collapse: collapse; font-size: 0.76rem; text-align: left;">
                <thead>
                  <tr style="border-bottom: 1.5px solid var(--border-color); color: var(--text-muted); text-transform: uppercase; font-weight: 700;">
                    <th style="padding: var(--spacing-sm) 6px;">Billing Period</th>
                    <th style="padding: var(--spacing-sm) 6px;">Gross Pay</th>
                    <th style="padding: var(--spacing-sm) 6px;">Deductions</th>
                    <th style="padding: var(--spacing-sm) 6px;">Net Paid</th>
                    <th style="padding: var(--spacing-sm) 6px;">Run #</th>
                    <th style="padding: var(--spacing-sm) 6px; text-align: center;">Action</th>
                  </tr>
                </thead>
                <tbody>
                  ${this.payslipsHistory.length === 0 ? `
                    <tr>
                      <td colspan="6" style="padding: var(--spacing-md); text-align: center; color: var(--text-muted);">
                        No paycheck releases found in system database.
                      </td>
                    </tr>
                  ` : this.payslipsHistory.map((p, index) => {
                    const rowSym = p.currencyCode === 'EUR' ? '€' : '$';
                    return `
                      <tr style="border-bottom: 1px solid rgba(255,255,255,0.05); hover: background: rgba(255,255,255,0.02);">
                        <td style="padding: var(--spacing-sm) 6px; font-weight:600; color:var(--text-primary);">Period #${p.runNumber}</td>
                        <td style="padding: var(--spacing-sm) 6px; font-variant-numeric: tabular-nums;">${rowSym}${p.grossPay.toLocaleString('en-US', {minimumFractionDigits: 2})}</td>
                        <td style="padding: var(--spacing-sm) 6px; font-variant-numeric: tabular-nums; color:var(--status-danger);">${rowSym}${p.deductions.toLocaleString('en-US', {minimumFractionDigits: 2})}</td>
                        <td style="padding: var(--spacing-sm) 6px; font-weight:800; font-variant-numeric: tabular-nums; color:var(--accent-primary);">${rowSym}${p.netPay.toLocaleString('en-US', {minimumFractionDigits: 2})}</td>
                        <td style="padding: var(--spacing-sm) 6px; font-weight:600; color:var(--text-muted);">${p.runNumber}</td>
                        <td style="padding: var(--spacing-sm) 6px; text-align: center;">
                          <button class="btn btn-secondary btn-view-payslip" data-index="${index}" style="padding: 2px 8px; font-size: 0.62rem; font-weight: 700; cursor: pointer;">
                            View Breakdown
                          </button>
                        </td>
                      </tr>
                    `;
                  }).join('')}
                </tbody>
              </table>
            </div>
          </div>

          <!-- Column Right: File payroll query ticket -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; height: fit-content; flex: 1;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Payroll Inquiry</h3>
              <i data-lucide="help-circle" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); font-size: 0.76rem;">
              <p style="margin: 0; color: var(--text-muted); font-size: 0.72rem; line-height: 1.4;">
                File a ticket directly with store accounts and HR regarding shift counts, salary credits, or benefit discrepancies.
              </p>

              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Select billing Period</label>
                <select id="query-billing-period" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none; cursor: pointer;">
                  ${this.payslipsHistory.map(p => `
                    <option value="${p.runNumber}">Period #${p.runNumber}</option>
                  `).join('')}
                </select>
              </div>

              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Inquiry Topic</label>
                <select id="query-topic" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none; cursor: pointer;">
                  <option value="overtime">Incorrect Overtime Hours Calculation</option>
                  <option value="tax">Tax/PF Deduction Discrepancy</option>
                  <option value="bank">Delay in Bank Remittance</option>
                  <option value="other">Other Issues</option>
                </select>
              </div>

              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Details & Evidence</label>
                <textarea id="query-details" placeholder="Describe the paycheck calculation dispute..." rows="3" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none; font-family: inherit; resize: none;"></textarea>
              </div>

              <button class="btn" id="btn-submit-payroll-query" style="background: var(--accent-primary); color: #000; font-weight: 800; border: none; border-radius: var(--radius-md); padding: var(--spacing-sm); cursor: pointer; transition: var(--transition-fast); margin-top: 4px;">
                Submit Accounts Ticket
              </button>
            </div>
          </div>

        </div>

      </div>

      <!-- Payslips Modal Mount Point Overlay -->
      <div id="payslip-modal-overlay" style="position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.65); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 10000; padding: var(--spacing-md);">
        <div id="payslip-modal-content" class="card glass animate-slide-up" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); width: 100%; max-width: 650px; padding: var(--spacing-lg); max-height: 90vh; overflow-y: auto; text-align: left;">
          <!-- Modal content injected here -->
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#payslip-modal-overlay');
    const modalContent = container.querySelector('#payslip-modal-content');

    const showModal = (htmlContent) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = htmlContent;
        overlay.style.display = 'flex';
        
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
      }
    };

    if (overlay) {
      overlay.addEventListener('click', (e) => {
        if (e.target === overlay) hideModal();
      });
    }

    // 1. View Breakdown modal
    const viewBtns = container.querySelectorAll('.btn-view-payslip');
    viewBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const index = parseInt(btn.getAttribute('data-index'));
        const p = this.payslipsHistory[index];
        if (!p) return;

        // Parse JSON snapshots
        const att = p.attendanceSnapshot ? JSON.parse(p.attendanceSnapshot) : {};
        const hours = p.workingHourSnapshot ? JSON.parse(p.workingHourSnapshot) : {};
        const sal = p.salarySnapshot ? JSON.parse(p.salarySnapshot) : {};
        const leaves = p.leaveSnapshot ? JSON.parse(p.leaveSnapshot) : {};
        const ot = p.overtimeSnapshot ? JSON.parse(p.overtimeSnapshot) : {};
        const benefits = p.benefitSnapshot ? JSON.parse(p.benefitSnapshot) : {};
        const contrib = p.employerContributionSnapshot ? JSON.parse(p.employerContributionSnapshot) : {};
        const audit = p.payrollAudit ? JSON.parse(p.payrollAudit) : {};

        const sym = sal.currency === 'EUR' ? '€' : '$';

        const breakupHtml = `
          <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1.5px solid var(--accent-primary); padding-bottom:var(--spacing-xs); margin-bottom:var(--spacing-md);">
            <h3 style="margin:0; font-weight:800; color:var(--accent-primary); font-family:var(--font-display);">Payslip Breakdown: Run #${p.runNumber}</h3>
            <button class="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer;"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          
          <div style="font-size:0.72rem; color:var(--text-primary); display:flex; flex-direction:column; gap:16px;">
            
            <!-- Salary / Info Summary -->
            <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; background: rgba(255,255,255,0.03); padding: 8px; border-radius: var(--radius-md);">
              <div>
                <div style="color: var(--text-muted); font-size: 0.6rem; text-transform: uppercase;">Hourly Base Rate</div>
                <div style="font-weight: 700; font-size: 0.85rem;">${sym}${sal.hourlyRate || 0}/hr</div>
              </div>
              <div>
                <div style="color: var(--text-muted); font-size: 0.6rem; text-transform: uppercase;">Employment Type</div>
                <div style="font-weight: 700; font-size: 0.85rem;">${sal.employmentType || 'Permanent'}</div>
              </div>
              <div>
                <div style="color: var(--text-muted); font-size: 0.6rem; text-transform: uppercase;">Monthly Base</div>
                <div style="font-weight: 700; font-size: 0.85rem;">${sym}${sal.monthlySalary || 0}</div>
              </div>
            </div>

            <!-- Attendance & Hours side-by-side -->
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
              
              <!-- Attendance Snapshot -->
              <div style="border: 1px solid var(--border-color); padding: 8px; border-radius: var(--radius-md);">
                <div style="font-weight: 800; text-transform: uppercase; font-size: 0.65rem; color: var(--accent-primary); border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 4px; margin-bottom: 6px;">Attendance Summary</div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Present Days:</span><strong>${att.presentDays || 0}d</strong></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Absent Days:</span><strong>${att.absentDays || 0}d</strong></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Approved Leave:</span><strong>${att.leaveDays || 0}d</strong></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Weekly Off / Holday:</span><strong>${(att.weeklyOff || 0) + (att.publicHolidays || 0)}d</strong></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Late / Early Out:</span><strong>${att.lateDays || 0} / ${att.earlyCheckout || 0}</strong></div>
                <div style="display:flex; justify-content:space-between; color: var(--status-success);"><span>Attendance Rate:</span><strong>${(att.attendancePercentage || 100).toFixed(1)}%</strong></div>
              </div>

              <!-- Working Hours Snapshot -->
              <div style="border: 1px solid var(--border-color); padding: 8px; border-radius: var(--radius-md);">
                <div style="font-weight: 800; text-transform: uppercase; font-size: 0.65rem; color: var(--accent-primary); border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 4px; margin-bottom: 6px;">Working Hour Summary</div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Required Hours:</span><strong>${hours.requiredHours || 0}h</strong></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Worked Hours:</span><strong>${hours.workedHours || 0}h</strong></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Regular Hours:</span><strong>${hours.regularHours || 0}h</strong></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Overtime Hours:</span><strong>${hours.overtimeHours || 0}h</strong></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Night / Holiday Hours:</span><strong>${hours.nightHours || 0}h / ${hours.holidayHours || 0}h</strong></div>
                <div style="display:flex; justify-content:space-between; color: var(--status-danger);"><span>Missing / Breaks:</span><strong>${hours.missingHours || 0}h / ${hours.breakHours || 0}h</strong></div>
              </div>
            </div>

            <!-- Detailed Pay Calculation Breakdowns -->
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
              
              <!-- Earnings -->
              <div style="border: 1px solid var(--border-color); padding: 8px; border-radius: var(--radius-md);">
                <div style="font-weight: 800; text-transform: uppercase; font-size: 0.65rem; color: var(--status-success); border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 4px; margin-bottom: 6px;">Earning Components</div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Regular Salary:</span><span style="font-variant-numeric:tabular-nums;">${sym}${(ot.regularPay || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Overtime Pay (Tiered):</span><span style="font-variant-numeric:tabular-nums; color: var(--status-success);">+${sym}${(ot.overtimePay || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Night Shift Pay:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-success);">+${sym}${(ot.nightPay || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Holiday Work Pay:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-success);">+${sym}${(ot.holidayPay || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Weekend Work Pay:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-success);">+${sym}${(ot.weekendPay || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Attendance Bonus:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-success);">+${sym}${(ot.attendanceBonus || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Performance Bonus:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-success);">+${sym}${(ot.performanceBonus || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Allowances (Meal/Travel):</span><span style="font-variant-numeric:tabular-nums; color: var(--status-success);">+${sym}${(ot.allowances || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; font-weight:800; border-top: 1px solid rgba(255,255,255,0.05); padding-top: 4px; margin-top: 4px;">
                  <span>Gross Earnings:</span>
                  <span style="font-variant-numeric:tabular-nums; color: var(--text-primary);">${sym}${(p.grossPay || 0).toFixed(2)}</span>
                </div>
              </div>

              <!-- Deductions -->
              <div style="border: 1px solid var(--border-color); padding: 8px; border-radius: var(--radius-md);">
                <div style="font-weight: 800; text-transform: uppercase; font-size: 0.65rem; color: var(--status-danger); border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 4px; margin-bottom: 6px;">Deductions &amp; Penalties</div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Income Tax:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(benefits.incomeTax || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Provident Fund (PF):</span><span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(benefits.pfDeduction || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>ESI:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(benefits.esiDeduction || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Pension Deduction:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(benefits.pensionDeduction || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Insurance:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(benefits.insuranceDeduction || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Unpaid Leave (LWP):</span><span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(benefits.leaveWithoutPay || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Absent Penalties:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(benefits.absentDeduction || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; margin-bottom: 4px;"><span>Late Clock Penalties:</span><span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(benefits.latePenalty || 0).toFixed(2)}</span></div>
                <div style="display:flex; justify-content:space-between; font-weight:800; border-top: 1px solid rgba(255,255,255,0.05); padding-top: 4px; margin-top: 4px;">
                  <span>Total Deductions:</span>
                  <span style="font-variant-numeric:tabular-nums; color: var(--status-danger);">${sym}${(p.deductions || 0).toFixed(2)}</span>
                </div>
              </div>
            </div>

            <!-- Employer Contributions -->
            <div style="border: 1px solid var(--border-color); padding: 8px; border-radius: var(--radius-md);">
              <div style="font-weight: 800; text-transform: uppercase; font-size: 0.65rem; color: #3b82f6; border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 4px; margin-bottom: 6px;">Employer Statutory Contributions</div>
              <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 6px;">
                <div>Employer PF: <strong>${sym}${(contrib.employerPf || 0).toFixed(2)}</strong></div>
                <div>Employer ESI: <strong>${sym}${(contrib.employerEsi || 0).toFixed(2)}</strong></div>
                <div>Employer Pension: <strong>${sym}${(contrib.employerPension || 0).toFixed(2)}</strong></div>
                <div>Employer SocSec: <strong>${sym}${(contrib.employerSocialSecurity || 0).toFixed(2)}</strong></div>
                <div>Health Insur: <strong>${sym}${(contrib.employerHealthInsurance || 0).toFixed(2)}</strong></div>
                <div>End of Service: <strong>${sym}${(contrib.employerEndOfService || 0).toFixed(2)}</strong></div>
              </div>
              <div style="display:flex; justify-content:space-between; font-weight:800; border-top: 1px solid rgba(255,255,255,0.05); padding-top: 4px; margin-top: 6px;">
                <span>Total Employer Contribution:</span>
                <span style="font-variant-numeric:tabular-nums; color: #3b82f6;">${sym}${(contrib.employerContributionTotal || 0).toFixed(2)}</span>
              </div>
            </div>

            <!-- Net take-home summary banner -->
            <div style="display:flex; justify-content:space-between; background:rgba(130,163,125,0.12); padding:10px; border-radius:var(--radius-md); border:1px solid rgba(130,163,125,0.3); font-size:0.85rem; font-weight:800; color:var(--accent-primary);">
              <span>Net Take-Home Salary (Auto-Calculated):</span>
              <span style="font-size: 1rem; color: var(--accent-primary);">${sym}${(p.netPay || 0).toFixed(2)}</span>
            </div>

            <!-- Audit details -->
            <div style="font-size: 0.58rem; color: var(--text-muted); text-align: center; border-top: 1px dashed rgba(255,255,255,0.08); padding-top: 6px;">
              Audit Certificate &nbsp;·&nbsp; Processed by: ${audit.generatedBy || 'SYSTEM'} &nbsp;·&nbsp; Date: ${audit.generatedAt || 'N/A'} &nbsp;·&nbsp; Status: ${audit.status || 'CALCULATED'}
            </div>

            <div style="text-align: center; margin-top: 4px;">
              <button class="btn btn-doc-download" data-name="PLUS33_Payslip_Run_${p.runNumber}.pdf" style="background:transparent; border:1px solid var(--accent-primary); color:var(--accent-primary); width:100%; font-weight:800;">
                <i data-lucide="download" style="width:14px; height:14px; vertical-align:middle; margin-right:4px;"></i> Download PDF Receipt
              </button>
            </div>
          </div>
        `;
        showModal(breakupHtml);

        // Bind inner closing btn
        const innerClose = modalContent.querySelector('.btn-close-modal');
        if (innerClose) {
          innerClose.addEventListener('click', () => hideModal());
        }

        // Bind inner download btn
        const innerDl = modalContent.querySelector('.btn-doc-download');
        if (innerDl) {
          innerDl.addEventListener('click', () => {
            notificationStore.success(`Initiated secure download of payslip PDF for Period #${p.runNumber}.`);
          });
        }
      });
    });

    // 2. Submit payroll query ticket
    const queryBtn = container.querySelector('#btn-submit-payroll-query');
    if (queryBtn) {
      queryBtn.addEventListener('click', () => {
        const periodSelect = container.querySelector('#query-billing-period');
        const topicSelect = container.querySelector('#query-topic');
        const detailsText = container.querySelector('#query-details');

        const period = periodSelect.value;
        const topic = topicSelect.value;
        const details = detailsText.value.trim();

        if (!details) {
          notificationStore.danger('Please provide details for the payroll inquiry.');
          return;
        }

        notificationStore.success('Accounts ticket filed. You will receive a notification when resolved.');
        detailsText.value = '';
      });
    }
  }
}
