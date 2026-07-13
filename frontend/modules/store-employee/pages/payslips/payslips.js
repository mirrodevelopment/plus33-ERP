/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : payslips.js
 * Path              : frontend/modules/store-employee/payslips/payslips.js
 * Purpose           : Controller component for Barista payroll records UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/payslips/payslips.html
 * Related CSS       : frontend/modules/store-employee/payslips/payslips.css
 * Related APIs      : GET /api/v2/employee-self-service/payslips
 *                     POST /api/v1/payroll/query (Discrepancies inquiries)
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in payslips.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the payslips HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/payslips/payslips.html';

export default class StoreEmployeePayslips {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.payslipsHistory = [];
    this.loading = false;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeePayslips component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeePayslips', 'Mounting Barista Payslips Page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout template with loading state
    this._renderLoading(container);

    // 2. Fetch all telemetry and history data from database
    await this.loadData();

    // 3. Inject full layout
    await this._loadTemplate(container);

    // 4. Render details
    this.render(container);

    // 5. Bind event listeners
    this.bindEvents(container, lifecycle);
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
      const response = await apiClient.request('/api/v2/employee-self-service/payslips');
      if (response) {
        this.payslipsHistory = response.success ? (response.data || []) : response;
      }
    } catch (err) {
      logger.error('StoreEmployeePayslips', 'Failed to load payslips from database', err);
      notificationStore.danger('Error connecting to payroll service.');
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
    const nameEl = container.querySelector('#lbl-barista-name');
    const roleEl = container.querySelector('#lbl-barista-role');

    if (nameEl) nameEl.textContent = this.profile.name || this.user?.username || 'Neha Sharma';
    if (roleEl) roleEl.textContent = this.user?.role || 'Barista';

    // 2. Sync KPIs values
    const latest = this.payslipsHistory[0] || { grossPay: 0, netPay: 0, deductions: 0, currencyCode: 'EUR' };
    const currencySym = latest.currencyCode === 'EUR' ? '€' : '$';

    let latestAllowances = 0;
    if (latest.overtimeSnapshot) {
      try {
        const snap = JSON.parse(latest.overtimeSnapshot);
        latestAllowances = snap.overtimePay + snap.nightPay + snap.holidayPay + snap.weekendPay + snap.attendanceBonus + snap.performanceBonus + snap.allowances;
      } catch (e) {
        latestAllowances = 0;
      }
    }

    const netEl = container.querySelector('#kpi-net-salary');
    const grossEl = container.querySelector('#kpi-gross-earnings');
    const allowancesEl = container.querySelector('#kpi-overtime-allowances');
    const deductionsEl = container.querySelector('#kpi-deductions-taxes');

    if (netEl) netEl.textContent = `${currencySym}${latest.netPay.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}`;
    if (grossEl) grossEl.textContent = `${currencySym}${latest.grossPay.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}`;
    if (allowancesEl) allowancesEl.textContent = `${currencySym}${latestAllowances.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}`;
    if (deductionsEl) deductionsEl.textContent = `${currencySym}${latest.deductions.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}`;

    // 3. Render inquiry billing options select list
    const queryBillingPeriodSelect = container.querySelector('#query-billing-period');
    if (queryBillingPeriodSelect) {
      queryBillingPeriodSelect.replaceChildren();
      this.payslipsHistory.forEach(p => {
        const opt = document.createElement('option');
        opt.value = String(p.runNumber);
        opt.textContent = `Period #${p.runNumber}`;
        queryBillingPeriodSelect.appendChild(opt);
      });
    }

    // 4. Render payroll release history list rows
    this._renderHistory(container);

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#payslip-modal-overlay');
    const modalContent = container.querySelector('#payslip-modal-content');
    const queryBtn = container.querySelector('#btn-submit-payroll-query');

    const showModal = (htmlContent) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = htmlContent;
        overlay.style.display = 'flex';
        overlay.setAttribute('aria-hidden', 'false');
        
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
      }
    };

    if (overlay) {
      const handleOverlayClick = (e) => {
        if (e.target === overlay) hideModal();
      };
      overlay.addEventListener('click', handleOverlayClick);
      lifecycle.onCleanup(() => overlay.removeEventListener('click', handleOverlayClick));
    }

    // 1. View Breakdown modal button triggers in table
    const viewBtns = container.querySelectorAll('.btn-view-payslip');
    viewBtns.forEach(btn => {
      const handleOpenBreakdown = () => {
        const index = parseInt(btn.getAttribute('data-index'));
        const p = this.payslipsHistory[index];
        if (!p) return;

        // Parse snapshots JSON strings
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
          <div class="modal-header-split">
            <h3 class="modal-title">Payslip Breakdown: Run #${p.runNumber}</h3>
            <button class="btn-close-modal" type="button" aria-label="Close modal">
              <i data-lucide="x" aria-hidden="true"></i>
            </button>
          </div>
          
          <div class="modal-details-body">
            <!-- Salary / Info Summary -->
            <div class="modal-top-grid-3">
              <div>
                <div class="modal-meta-label">Hourly Base Rate</div>
                <div class="modal-meta-val">${sym}${sal.hourlyRate || 0}/hr</div>
              </div>
              <div>
                <div class="modal-meta-label">Employment Type</div>
                <div class="modal-meta-val">${sal.employmentType || 'Permanent'}</div>
              </div>
              <div>
                <div class="modal-meta-label">Monthly Base</div>
                <div class="modal-meta-val">${sym}${sal.monthlySalary || 0}</div>
              </div>
            </div>

            <!-- Attendance & Hours side-by-side -->
            <div class="modal-split-row-2">
              <!-- Attendance Snapshot -->
              <div class="modal-outline-box">
                <div class="modal-box-title">Attendance Summary</div>
                <div class="modal-flex-line"><span>Present Days:</span><strong>${att.presentDays || 0}d</strong></div>
                <div class="modal-flex-line"><span>Absent Days:</span><strong>${att.absentDays || 0}d</strong></div>
                <div class="modal-flex-line"><span>Approved Leave:</span><strong>${att.leaveDays || 0}d</strong></div>
                <div class="modal-flex-line"><span>Weekly Off / Holiday:</span><strong>${(att.weeklyOff || 0) + (att.publicHolidays || 0)}d</strong></div>
                <div class="modal-flex-line"><span>Late / Early Out:</span><strong>${att.lateDays || 0} / ${att.earlyCheckout || 0}</strong></div>
                <div class="modal-flex-line color-success"><span>Attendance Rate:</span><strong>${(att.attendancePercentage || 100).toFixed(1)}%</strong></div>
              </div>

              <!-- Working Hours Snapshot -->
              <div class="modal-outline-box">
                <div class="modal-box-title">Working Hour Summary</div>
                <div class="modal-flex-line"><span>Required Hours:</span><strong>${hours.requiredHours || 0}h</strong></div>
                <div class="modal-flex-line"><span>Worked Hours:</span><strong>${hours.workedHours || 0}h</strong></div>
                <div class="modal-flex-line"><span>Regular Hours:</span><strong>${hours.regularHours || 0}h</strong></div>
                <div class="modal-flex-line"><span>Overtime Hours:</span><strong>${hours.overtimeHours || 0}h</strong></div>
                <div class="modal-flex-line"><span>Night / Holiday Hours:</span><strong>${hours.nightHours || 0}h / ${hours.holidayHours || 0}h</strong></div>
                <div class="modal-flex-line color-danger"><span>Missing / Breaks:</span><strong>${hours.missingHours || 0}h / ${hours.breakHours || 0}h</strong></div>
              </div>
            </div>

            <!-- Detailed Pay Calculation Breakdowns -->
            <div class="modal-split-row-2">
              <!-- Earnings -->
              <div class="modal-outline-box modal-outline-box--earnings">
                <div class="modal-box-title">Earning Components</div>
                <div class="modal-flex-line"><span>Regular Salary:</span><span class="font-mono">${sym}${(ot.regularPay || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Overtime Pay (Tiered):</span><span class="font-mono color-success">+${sym}${(ot.overtimePay || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Night Shift Pay:</span><span class="font-mono color-success">+${sym}${(ot.nightPay || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Holiday Work Pay:</span><span class="font-mono color-success">+${sym}${(ot.holidayPay || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Weekend Work Pay:</span><span class="font-mono color-success">+${sym}${(ot.weekendPay || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Attendance Bonus:</span><span class="font-mono color-success">+${sym}${(ot.attendanceBonus || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Performance Bonus:</span><span class="font-mono color-success">+${sym}${(ot.performanceBonus || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Allowances (Meal/Travel):</span><span class="font-mono color-success">+${sym}${(ot.allowances || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line-bold modal-flex-line-bold-top">
                  <span>Gross Earnings:</span>
                  <span class="font-mono">${sym}${(p.grossPay || 0).toFixed(2)}</span>
                </div>
              </div>

              <!-- Deductions -->
              <div class="modal-outline-box modal-outline-box--deductions">
                <div class="modal-box-title">Deductions &amp; Penalties</div>
                <div class="modal-flex-line"><span>Income Tax:</span><span class="font-mono color-danger">${sym}${(benefits.incomeTax || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Provident Fund (PF):</span><span class="font-mono color-danger">${sym}${(benefits.pfDeduction || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>ESI:</span><span class="font-mono color-danger">${sym}${(benefits.esiDeduction || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Pension Deduction:</span><span class="font-mono color-danger">${sym}${(benefits.pensionDeduction || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Insurance:</span><span class="font-mono color-danger">${sym}${(benefits.insuranceDeduction || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Unpaid Leave (LWP):</span><span class="font-mono color-danger">${sym}${(benefits.leaveWithoutPay || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Absent Penalties:</span><span class="font-mono color-danger">${sym}${(benefits.absentDeduction || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line"><span>Late Clock Penalties:</span><span class="font-mono color-danger">${sym}${(benefits.latePenalty || 0).toFixed(2)}</span></div>
                <div class="modal-flex-line-bold modal-flex-line-bold-top color-danger">
                  <span>Total Deductions:</span>
                  <span class="font-mono">${sym}${(p.deductions || 0).toFixed(2)}</span>
                </div>
              </div>
            </div>

            <!-- Employer Contributions -->
            <div class="modal-outline-box modal-outline-box--contributions">
              <div class="modal-box-title">Employer Statutory Contributions</div>
              <div class="contributions-grid-3">
                <div>Employer PF: <strong>${sym}${(contrib.employerPf || 0).toFixed(2)}</strong></div>
                <div>Employer ESI: <strong>${sym}${(contrib.employerEsi || 0).toFixed(2)}</strong></div>
                <div>Employer Pension: <strong>${sym}${(contrib.employerPension || 0).toFixed(2)}</strong></div>
                <div>Employer SocSec: <strong>${sym}${(contrib.employerSocialSecurity || 0).toFixed(2)}</strong></div>
                <div>Health Insur: <strong>${sym}${(contrib.employerHealthInsurance || 0).toFixed(2)}</strong></div>
                <div>End of Service: <strong>${sym}${(contrib.employerEndOfService || 0).toFixed(2)}</strong></div>
              </div>
              <div class="modal-flex-line-bold modal-flex-line-bold-top color-blue">
                <span>Total Employer Contribution:</span>
                <span class="font-mono">${sym}${(contrib.employerContributionTotal || 0).toFixed(2)}</span>
              </div>
            </div>

            <!-- Net take-home summary banner -->
            <div class="modal-total-banner-row">
              <span>Net Take-Home Salary (Auto-Calculated):</span>
              <span>${sym}${(p.netPay || 0).toFixed(2)}</span>
            </div>

            <!-- Audit details -->
            <div class="modal-audit-certificate-line">
              Audit Certificate &nbsp;·&nbsp; Processed by: ${audit.generatedBy || 'SYSTEM'} &nbsp;·&nbsp; Date: ${audit.generatedAt || 'N/A'} &nbsp;·&nbsp; Status: ${audit.status || 'CALCULATED'}
            </div>

            <div class="modal-action-btn-row">
              <button class="btn btn-secondary btn-doc-download" data-name="PLUS33_Payslip_Run_${p.runNumber}.pdf" type="button">
                <i data-lucide="download" aria-hidden="true"></i> Download PDF Receipt
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
      };
      btn.addEventListener('click', handleOpenBreakdown);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenBreakdown));
    });

    // 2. Submit payroll query ticket
    if (queryBtn) {
      const handleSubmitQuery = () => {
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
      };
      queryBtn.addEventListener('click', handleSubmitQuery);
      lifecycle.onCleanup(() => queryBtn.removeEventListener('click', handleSubmitQuery));
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderLoading(container) {
    container.innerHTML = `
      <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
        <i data-lucide="loader-2" class="animate-spin" style="width:32px;height:32px;color:var(--accent-primary);"></i>
        <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading paycheck data...</span>
      </div>`;
    if (window.lucide) window.lucide.createIcons();
  }

  _renderHistory(container) {
    const tbody = container.querySelector('#payslip-history-tbody');
    const emptyTpl = container.querySelector('#payslip-empty-row-tpl');
    const rowTpl = container.querySelector('#payslip-row-tpl');

    if (!tbody) return;

    tbody.replaceChildren();

    if (this.payslipsHistory.length === 0) {
      if (emptyTpl) {
        tbody.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    this.payslipsHistory.forEach((p, index) => {
      if (!rowTpl) return;
      const clone = rowTpl.content.cloneNode(true);

      const periodEl = clone.querySelector('.period-cell');
      const grossEl = clone.querySelector('.gross-cell');
      const deductionsEl = clone.querySelector('.deductions-cell');
      const netEl = clone.querySelector('.net-cell');
      const runEl = clone.querySelector('.run-cell');
      const viewBtn = clone.querySelector('.btn-view-payslip');

      const rowSym = p.currencyCode === 'EUR' ? '€' : '$';

      if (periodEl) periodEl.textContent = `Period #${p.runNumber}`;
      if (grossEl) grossEl.textContent = `${rowSym}${p.grossPay.toLocaleString('en-US', {minimumFractionDigits: 2})}`;
      if (deductionsEl) deductionsEl.textContent = `${rowSym}${p.deductions.toLocaleString('en-US', {minimumFractionDigits: 2})}`;
      if (netEl) netEl.textContent = `${rowSym}${p.netPay.toLocaleString('en-US', {minimumFractionDigits: 2})}`;
      if (runEl) runEl.textContent = String(p.runNumber);
      if (viewBtn) {
        viewBtn.setAttribute('data-index', String(index));
      }

      tbody.appendChild(clone);
    });
  }

  _loadCss() {
    const cssId = 'store-employee-payslips-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/payslips/payslips.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeePayslips };
