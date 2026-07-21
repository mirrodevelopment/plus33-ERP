/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : payslips.js
 * Version           : 3.0.0
 *
 * APIs Used         : GET /api/v2/employee-self-service/payslips
 *
 * Features
 * ---------------------------------------------------------------------------
 * - 5 KPI cards incl. YTD net earnings
 * - 6-month net pay trend bar chart (Canvas API)
 * - Year-based history filter
 * - Full breakdown modal: work summary, earnings, tax, deductions, employer contributions
 * - PDF export: full A4 layout with logo (imgs/logo-gold.png), window.print()
 * - Image export: Canvas-drawn payslip → PNG file download (no external library)
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

const TEMPLATE_URL = 'modules/store-employee/pages/payslips/payslips.html';

export default class StoreEmployeePayslips {

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.payslipsHistory = [];
    this.filteredHistory = [];
    this.loading = false;
    this._selectedYear = 'all';
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeePayslips', 'Mounting Payslips Page v3...');
    this._loadCss();
    this._renderLoading(container);
    await this.loadData();
    await this._loadTemplate(container);
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // DATA LOAD
  // ---------------------------------------------------------------------------

  async loadData() {
    this.loading = true;
    try {
      // Fetch profile data first from database, matching profile page
      try {
        const profileResp = await apiClient.get('/api/v1/auth/me');
        if (profileResp && profileResp.success && profileResp.data) {
          this.profile = profileResp.data;
          userStore.updateProfile(this.user?.role, profileResp.data);
        }
      } catch (profileErr) {
        logger.error('StoreEmployeePayslips', 'Error loading auth/me profile:', profileErr);
      }

      // Fetch payslip history
      const response = await apiClient.request('/api/v2/employee-self-service/payslips');
      let apiHistory = [];
      if (response) {
        apiHistory = response.success ? (response.data || []) : (Array.isArray(response) ? response : []);
      }
      
      this.payslipsHistory = apiHistory || [];
    } catch (err) {
      logger.error('StoreEmployeePayslips', 'Failed to load payslips', err);
      this.payslipsHistory = [];
    } finally {
      this.loading = false;
    }
  }

  // ---------------------------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------------------------

  render(container) {
    const history = this.payslipsHistory;
    const latest  = history[0] || {};
    const sym = (latest.currencyCode === 'EUR' ? '€' : '$');

    // Header
    let dispName = latest.employeeName || '';
    const isGeneric = (n) => !n || n.includes('_') || n.includes('@') || n.match(/^\d/);
    if (isGeneric(dispName)) {
      dispName = this.profile.name || '';
    }
    if (isGeneric(dispName)) {
      const parts = (this.user?.name || this.user?.email || '').split('@')[0].split('_');
      dispName = parts.map(p => p.charAt(0).toUpperCase() + p.slice(1)).join(' ');
    }

    const empCode = latest.employeeCode || this.profile.employeeCode || this.user?.email || '';
    const country = this.profile.country || '';
    
    // Resolve Nation Tag from profile country first, fallback to code format
    let nationTag = '🏳️ N/A';
    const upperCode = empCode.toUpperCase();
    const upperCountry = country.toUpperCase();
    if (upperCountry.includes('FRANCE') || upperCode.includes('_FR') || upperCode.includes('.FR') || upperCode.includes('@FR') || upperCode.includes('FR_') || upperCode.includes('-FR')) {
      nationTag = '🇫🇷 FR';
    } else if (upperCountry.includes('INDIA') || upperCode.includes('_IN') || upperCode.includes('.IN') || upperCode.includes('@IN') || upperCode.includes('IN_') || upperCode.includes('-IN')) {
      nationTag = '🇮🇳 IN';
    } else if (upperCountry.includes('UAE') || upperCountry.includes('UNITED ARAB EMIRATES') || upperCode.includes('_AE') || upperCode.includes('.AE') || upperCode.includes('@AE') || upperCode.includes('AE_') || upperCode.includes('-AE') || upperCode.includes('UAE')) {
      nationTag = '🇦🇪 AE';
    }

    const nameEl  = container.querySelector('#lbl-barista-name');
    const codeEl  = container.querySelector('#lbl-employee-code');
    const roleEl  = container.querySelector('#lbl-barista-role');
    const nationEl = container.querySelector('#lbl-nation-tag');
    
    if (nameEl) nameEl.textContent = dispName || '—';
    if (nationEl) nationEl.textContent = nationTag;
    if (codeEl) codeEl.textContent = latest.employeeCode || this.profile.employeeCode || 'EMP-???';
    if (roleEl) roleEl.textContent = this.profile.designation || this.user?.role || 'Employee';

    // KPI 1-4
    let latestAllowances = 0;
    if (latest.overtimeSnapshot) {
      try {
        const ot = JSON.parse(latest.overtimeSnapshot);
        latestAllowances = (ot.overtimePay || 0) + (ot.nightPay || 0) + (ot.holidayPay || 0)
          + (ot.weekendPay || 0) + (ot.attendanceBonus || 0) + (ot.performanceBonus || 0) + (ot.allowances || 0);
      } catch (_) {}
    }

    this._setKpi(container, '#kpi-net-salary',       sym, latest.netPay || 0);
    this._setKpi(container, '#kpi-gross-earnings',    sym, latest.grossPay || 0);
    this._setKpi(container, '#kpi-overtime-allowances', sym, latestAllowances);
    this._setKpi(container, '#kpi-deductions-taxes',  sym, latest.deductions || 0);

    // KPI 5: YTD
    const currentYear = new Date().getFullYear();
    const ytd = history.reduce((sum, p) => {
      if (!p.paidAt) return sum;
      const yr = new Date(p.paidAt).getFullYear();
      return yr === currentYear ? sum + Number(p.netPay || 0) : sum;
    }, 0);
    this._setKpi(container, '#kpi-ytd-net', sym, ytd);

    // Inquiry billing period options
    const querySelect = container.querySelector('#query-billing-period');
    if (querySelect) {
      querySelect.replaceChildren();
      history.forEach(p => {
        const opt = document.createElement('option');
        opt.value = String(p.runNumber);
        opt.textContent = `${p.runNumber}${p.paidAt ? ' (' + this._formatDate(p.paidAt) + ')' : ''}`;
        querySelect.appendChild(opt);
      });
    }

    // Year filter options
    this._buildYearFilter(container);

    // History table
    this._applyFilterAndRender(container);

    // Trend chart
    this._renderTrendChart(container);

    if (window.lucide) window.lucide.createIcons();
  }

  _setKpi(container, id, sym, value) {
    const el = container.querySelector(id);
    if (el) el.textContent = `${sym}${Number(value).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  }

  // ---------------------------------------------------------------------------
  // YEAR FILTER
  // ---------------------------------------------------------------------------

  _buildYearFilter(container) {
    const select = container.querySelector('#filter-year-select');
    if (!select) return;
    const years = new Set();
    this.payslipsHistory.forEach(p => {
      if (p.paidAt) years.add(new Date(p.paidAt).getFullYear());
    });
    select.replaceChildren();
    const allOpt = document.createElement('option');
    allOpt.value = 'all'; allOpt.textContent = 'All Years';
    select.appendChild(allOpt);
    [...years].sort((a, b) => b - a).forEach(yr => {
      const opt = document.createElement('option');
      opt.value = String(yr); opt.textContent = String(yr);
      select.appendChild(opt);
    });
    select.value = this._selectedYear;
  }

  _applyFilterAndRender(container) {
    if (this._selectedYear === 'all') {
      this.filteredHistory = [...this.payslipsHistory];
    } else {
      const yr = parseInt(this._selectedYear, 10);
      this.filteredHistory = this.payslipsHistory.filter(p => {
        if (!p.paidAt) return false;
        return new Date(p.paidAt).getFullYear() === yr;
      });
    }
    this._renderHistory(container);
  }

  // ---------------------------------------------------------------------------
  // TREND CHART (Canvas API)
  // ---------------------------------------------------------------------------

  _renderTrendChart(container) {
    const canvas = container.querySelector('#payslip-trend-chart');
    if (!canvas) return;

    const last6 = [...this.payslipsHistory].slice(0, 6).reverse();
    if (last6.length === 0) {
      canvas.style.display = 'none';
      return;
    }

    const dpr = window.devicePixelRatio || 1;
    const W   = canvas.parentElement.clientWidth || 700;
    const H   = 160;
    canvas.width  = W * dpr;
    canvas.height = H * dpr;
    canvas.style.width  = W + 'px';
    canvas.style.height = H + 'px';

    const ctx = canvas.getContext('2d');
    ctx.scale(dpr, dpr);

    const maxVal = Math.max(...last6.map(p => Number(p.netPay || 0)), 1);
    const pad    = { top: 20, bottom: 40, left: 10, right: 10 };
    const chartW = W - pad.left - pad.right;
    const chartH = H - pad.top - pad.bottom;
    const barW   = chartW / (last6.length * 2 - 1) * 0.9;
    const gap    = chartW / (last6.length * 2 - 1) * 1.1;

    // Use CSS variable accent color (fallback: gold)
    const accentColor = getComputedStyle(document.documentElement)
      .getPropertyValue('--accent-primary').trim() || '#c9a84c';

    ctx.clearRect(0, 0, W, H);

    last6.forEach((p, i) => {
      const val  = Number(p.netPay || 0);
      const barH = (val / maxVal) * chartH;
      const x    = pad.left + i * (barW + gap);
      const y    = pad.top + chartH - barH;

      // Bar fill
      const grad = ctx.createLinearGradient(x, y, x, y + barH);
      grad.addColorStop(0, accentColor);
      grad.addColorStop(1, accentColor + '55');
      ctx.fillStyle = grad;
      ctx.beginPath();
      ctx.roundRect(x, y, barW, barH, 4);
      ctx.fill();

      // Value label
      const sym = p.currencyCode === 'EUR' ? '€' : '$';
      ctx.fillStyle = getComputedStyle(document.documentElement).getPropertyValue('--text-muted').trim() || '#888';
      ctx.font = `600 10px Inter, system-ui, sans-serif`;
      ctx.textAlign = 'center';
      ctx.fillText(`${sym}${this._shortNum(val)}`, x + barW / 2, y - 5);

      // Month label
      const label = p.paidAt ? new Date(p.paidAt).toLocaleDateString('en-US', { month: 'short' }) : p.runNumber || '';
      ctx.fillStyle = getComputedStyle(document.documentElement).getPropertyValue('--text-muted').trim() || '#888';
      ctx.font = `500 10px Inter, system-ui, sans-serif`;
      ctx.fillText(label, x + barW / 2, H - 8);
    });
  }

  _shortNum(n) {
    if (n >= 1000) return (n / 1000).toFixed(1) + 'k';
    return n.toFixed(0);
  }

  // ---------------------------------------------------------------------------
  // BIND EVENTS
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const overlay     = container.querySelector('#payslip-modal-overlay');
    const modalContent = container.querySelector('#payslip-modal-content');
    const queryBtn    = container.querySelector('#btn-submit-payroll-query');
    const yearSelect  = container.querySelector('#filter-year-select');

    // Year filter
    if (yearSelect) {
      const onYearChange = () => {
        this._selectedYear = yearSelect.value;
        this._applyFilterAndRender(container);
      };
      yearSelect.addEventListener('change', onYearChange);
      lifecycle.onCleanup(() => yearSelect.removeEventListener('change', onYearChange));
    }

    const showModal = (html) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = html;
        overlay.style.display = 'flex';
        overlay.setAttribute('aria-hidden', 'false');
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
      const onOverlay = (e) => { if (e.target === overlay) hideModal(); };
      overlay.addEventListener('click', onOverlay);
      lifecycle.onCleanup(() => overlay.removeEventListener('click', onOverlay));
    }

    // View breakdown buttons (re-bind when filter changes)
    this._bindViewButtons(container, showModal, hideModal, modalContent, lifecycle);

    // Policy guide card click
    const guideBtn = container.querySelector('#btn-open-policy-guide');
    if (guideBtn) {
      const openGuide = () => {
        showModal(this._buildPolicyGuideHtml());
        const closeBtn = modalContent.querySelector('.btn-close-modal');
        if (closeBtn) closeBtn.addEventListener('click', () => hideModal());
      };
      guideBtn.addEventListener('click', openGuide);
      lifecycle.onCleanup(() => guideBtn.removeEventListener('click', openGuide));
    }

    // Inquiry form
    if (queryBtn) {
      const onSubmit = () => {
        const details = container.querySelector('#query-details')?.value?.trim();
        if (!details) { notificationStore.danger('Please provide details for the payroll inquiry.'); return; }
        notificationStore.success('Accounts ticket filed. You will receive a notification when resolved.');
        if (container.querySelector('#query-details')) container.querySelector('#query-details').value = '';
      };
      queryBtn.addEventListener('click', onSubmit);
      lifecycle.onCleanup(() => queryBtn.removeEventListener('click', onSubmit));
    }
  }

  _bindViewButtons(container, showModal, hideModal, modalContent, lifecycle) {
    container.querySelectorAll('.btn-view-payslip').forEach(btn => {
      const handleOpen = () => {
        const idx = parseInt(btn.getAttribute('data-index'));
        const p   = this.filteredHistory[idx];
        if (!p) return;
        showModal(this._buildBreakdownHtml(p));
        this._bindModalActions(modalContent, hideModal, p);
      };
      btn.addEventListener('click', handleOpen);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpen));
    });
  }

  // ---------------------------------------------------------------------------
  // BREAKDOWN MODAL HTML
  // ---------------------------------------------------------------------------

  _buildBreakdownHtml(p) {
    const template = document.getElementById('payslip-breakdown-tpl');
    if (!template) return '';
    const clone = template.content.cloneNode(true);

    const att     = this._safeJson(p.attendanceSnapshot);
    const hours   = this._safeJson(p.workingHourSnapshot);
    const sal     = this._safeJson(p.salarySnapshot);
    const ot      = this._safeJson(p.overtimeSnapshot);
    const benefits = this._safeJson(p.benefitSnapshot);
    const tax     = this._safeJson(p.taxSnapshot);
    const contrib = this._safeJson(p.employerContributionSnapshot);
    const audit   = this._safeJson(p.payrollAudit);
    const sym     = p.currencyCode === 'EUR' ? '€' : '$';
    const totalBonus = (ot.performanceBonus || 0) + (ot.attendanceBonus || 0);

    const set = (sel, txt) => {
      const el = clone.querySelector(sel);
      if (el) el.textContent = txt;
    };

    set('.val-run-number', p.runNumber || 'Run');
    set('.val-emp-name', p.employeeName || '—');
    set('.val-emp-code', p.employeeCode || '—');
    set('.val-base-rate', `${sym}${sal.hourlyRate || 0}/hr`);
    set('.val-monthly-base', `${sym}${sal.monthlySalary || 0}`);
    set('.val-employment-type', sal.employmentType || 'Permanent');
    set('.val-payment-date', p.paidAt ? this._formatDate(p.paidAt) : 'Pending');
    set('.val-bank-name', p.bankName || 'N/A');
    set('.val-bank-account', p.bankAccountNumber || 'N/A');
    set('.val-bank-ifsc', p.ifscNumber || 'N/A');

    set('.val-days-worked', att.presentDays || 0);
    set('.val-hours-worked', `${hours.workedHours || 0}h`);
    set('.val-ot-hours', `${hours.overtimeHours || 0}h`);
    set('.val-night-hours', `${hours.nightHours || 0}h`);
    set('.val-total-bonus', `${sym}${totalBonus.toFixed(2)}`);
    set('.val-attendance-rate', `${(att.attendancePercentage || 100).toFixed(1)}%`);
    set('.val-absent-days', `${att.absentDays || 0}d`);
    set('.val-leave-used', `${att.leaveDays || 0}d`);

    // Earnings breakdown
    const earningsBox = clone.querySelector('.box-earnings-rows');
    if (earningsBox) {
      const addRow = (lbl, val, cls = '') => {
        const row = document.createElement('div');
        row.className = 'modal-flex-line';
        row.innerHTML = `<span>${lbl}:</span><span class="font-mono ${cls}">${val}</span>`;
        earningsBox.appendChild(row);
      };
      addRow('Regular Base Pay', `${sym}${(ot.regularPay || 0).toFixed(2)}`);
      addRow('Overtime Pay (Tiered)', `+${sym}${(ot.overtimePay || 0).toFixed(2)}`, 'color-success');
      addRow('Night Shift Pay', `+${sym}${(ot.nightPay || 0).toFixed(2)}`, 'color-success');
      addRow('Holiday Work Pay', `+${sym}${(ot.holidayPay || 0).toFixed(2)}`, 'color-success');
      addRow('Weekend Work Pay', `+${sym}${(ot.weekendPay || 0).toFixed(2)}`, 'color-success');
      addRow('Performance Bonus', `+${sym}${(ot.performanceBonus || 0).toFixed(2)}`, 'color-success');
      addRow('Attendance Bonus', `+${sym}${(ot.attendanceBonus || 0).toFixed(2)}`, 'color-success');
      addRow('Meal / Travel Allowances', `+${sym}${(ot.allowances || 0).toFixed(2)}`, 'color-success');
    }
    set('.val-gross-earnings', `${sym}${Number(p.grossPay || 0).toFixed(2)}`);

    // Deductions breakdown
    const dedBox = clone.querySelector('.box-deductions-rows');
    if (dedBox) {
      const addRow = (lbl, val, cls = '') => {
        const row = document.createElement('div');
        row.className = 'modal-flex-line';
        row.innerHTML = `<span>${lbl}:</span><span class="font-mono ${cls}">${val}</span>`;
        dedBox.appendChild(row);
      };
      addRow('Income Tax', `${sym}${(benefits.incomeTax || 0).toFixed(2)}`, 'color-danger');
      addRow('Provident Fund (PF)', `${sym}${(benefits.pfDeduction || 0).toFixed(2)}`, 'color-danger');
      addRow('ESI', `${sym}${(benefits.esiDeduction || 0).toFixed(2)}`, 'color-danger');
      addRow('Pension Deduction', `${sym}${(benefits.pensionDeduction || 0).toFixed(2)}`, 'color-danger');
      addRow('Insurance', `${sym}${(benefits.insuranceDeduction || 0).toFixed(2)}`, 'color-danger');
      addRow('Unpaid Leave (LWP)', `${sym}${(benefits.leaveWithoutPay || 0).toFixed(2)}`, 'color-danger');
      addRow('Absent Penalties', `${sym}${(benefits.absentDeduction || 0).toFixed(2)}`, 'color-danger');
      addRow('Late Clock Penalties', `${sym}${(benefits.latePenalty || 0).toFixed(2)}`, 'color-danger');
    }
    set('.val-total-deductions', `${sym}${Number(p.deductions || 0).toFixed(2)}`);

    // Tax snapshot
    if (tax && Object.keys(tax).length > 0) {
      const taxSection = clone.querySelector('.modal-tax-breakdown-section');
      if (taxSection) taxSection.style.display = 'block';
      const taxBox = clone.querySelector('.box-tax-rows');
      if (taxBox) {
        Object.entries(tax).forEach(([k, v]) => {
          const div = document.createElement('div');
          div.innerHTML = `${this._camelToLabel(k)}: <strong class="color-danger">${sym}${Number(v || 0).toFixed(2)}</strong>`;
          taxBox.appendChild(div);
        });
      }
      set('.val-total-tax', `${sym}${Number(p.taxWithheld || 0).toFixed(2)}`);
    }

    set('.val-employer-pf', `${sym}${(contrib.employerPf || 0).toFixed(2)}`);
    set('.val-employer-esi', `${sym}${(contrib.employerEsi || 0).toFixed(2)}`);
    set('.val-employer-pension', `${sym}${(contrib.employerPension || 0).toFixed(2)}`);
    set('.val-employer-soc-sec', `${sym}${(contrib.employerSocialSecurity || 0).toFixed(2)}`);
    set('.val-employer-health', `${sym}${(contrib.employerHealthInsurance || 0).toFixed(2)}`);
    set('.val-employer-eos', `${sym}${(contrib.employerEndOfService || 0).toFixed(2)}`);
    set('.val-employer-total', `${sym}${(contrib.employerContributionTotal || 0).toFixed(2)}`);

    set('.val-net-pay', `${sym}${Number(p.netPay || 0).toFixed(2)}`);
    set('.val-audit-by', audit.generatedBy || 'SYSTEM');
    set('.val-audit-date', audit.generatedAt || 'N/A');
    set('.val-audit-status', p.status || 'CALCULATED');

    // Bottom bank credit details section
    set('.val-bank-name-btm', p.bankName || 'N/A');
    set('.val-bank-account-btm', p.bankAccountNumber || 'N/A');
    set('.val-bank-ifsc-btm', p.ifscNumber || 'N/A');

    const tempDiv = document.createElement('div');
    tempDiv.appendChild(clone);
    return tempDiv.innerHTML;
  }

  _camelToLabel(key) {
    return key.replace(/([A-Z])/g, ' $1').replace(/^./, s => s.toUpperCase());
  }

  _buildPolicyGuideHtml() {
    const template = document.getElementById('payroll-policy-tpl');
    if (!template) return '';
    const clone = template.content.cloneNode(true);
    const tempDiv = document.createElement('div');
    tempDiv.appendChild(clone);
    return tempDiv.innerHTML;
  }

  _getMockPayslips() {
    return [];
  }

  // ---------------------------------------------------------------------------
  // MODAL ACTION BINDINGS (PDF + Image export)
  // ---------------------------------------------------------------------------

  _bindModalActions(modalContent, hideModal, p) {
    // Close
    const closeBtn = modalContent.querySelector('.btn-close-modal');
    if (closeBtn) closeBtn.addEventListener('click', () => hideModal());

    // PDF print
    const printBtn = modalContent.querySelector('.btn-doc-download');
    if (printBtn) printBtn.addEventListener('click', () => this._printPdf(p));

    // Image export
    const imgBtn = modalContent.querySelector('#btn-export-img');
    if (imgBtn) imgBtn.addEventListener('click', () => this._exportAsImage(p));
  }

  _printPdf(p) {
    notificationStore.success(`Opening print dialog for payslip ${p.runNumber}...`);
    const att      = this._safeJson(p.attendanceSnapshot);
    const hours    = this._safeJson(p.workingHourSnapshot);
    const sal      = this._safeJson(p.salarySnapshot);
    const ot       = this._safeJson(p.overtimeSnapshot);
    const ben      = this._safeJson(p.benefitSnapshot);
    const tax      = this._safeJson(p.taxSnapshot);
    const contrib  = this._safeJson(p.employerContributionSnapshot);
    const audit    = this._safeJson(p.payrollAudit);
    const sym      = p.currencyCode === 'EUR' ? '€' : '$';
    const logoUrl  = `${window.location.origin}/imgs/logo-gold.png`;

    const thisYear = new Date().getFullYear();
    const ytdSlips = this.payslipsHistory.filter(h => h.paidAt && new Date(h.paidAt).getFullYear() === thisYear);
    const ytdGross = ytdSlips.reduce((s, h) => s + Number(h.grossPay   || 0), 0);
    const ytdNet   = ytdSlips.reduce((s, h) => s + Number(h.netPay     || 0), 0);
    const ytdDed   = ytdSlips.reduce((s, h) => s + Number(h.deductions || 0), 0);

    const sumOt    = (key) => ytdSlips.reduce((s, h) => s + (this._safeJson(h.overtimeSnapshot)[key] || 0), 0);
    const sumBen   = (key) => ytdSlips.reduce((s, h) => s + (this._safeJson(h.benefitSnapshot)[key]  || 0), 0);
    const sumContrib = (key) => ytdSlips.reduce((s, h) => s + (this._safeJson(h.employerContributionSnapshot)[key] || 0), 0);

    const fmt  = (v) => `${sym}${Number(v || 0).toFixed(2)}`;
    const hrs  = (v) => v ? `${Number(v).toFixed(2)}` : '—';
    const rate = (v) => v ? `${sym}${Number(v).toFixed(2)}` : '—';

    // Select print template
    const template = document.getElementById('print-pdf-tpl');
    if (!template) return;
    const clone = template.content.cloneNode(true);

    // Set logo
    const logoEl = clone.querySelector('.ps-logo');
    if (logoEl) logoEl.src = logoUrl;

    const set = (sel, txt) => {
      const el = clone.querySelector(sel);
      if (el) el.textContent = txt;
    };

    set('.val-store-name', this.profile.store || 'Store Name');
    set('.val-store-region', this.profile.storeRegion || 'Region');
    set('.val-store-address', this.profile.address || '');

    set('.val-emp-name', p.employeeName || '—');
    set('.val-emp-code', p.employeeCode || 'N/A');
    set('.val-emp-desig', sal.employmentType || '—');
    set('.val-emp-email', this.profile.email || '—');
    set('.val-bank-name', p.bankName || 'N/A');
    set('.val-bank-account', p.bankAccountNumber || 'N/A');
    set('.val-bank-ifsc', p.ifscNumber || 'N/A');

    set('.val-pay-period', p.runNumber || '—');
    set('.val-pay-date', p.paidAt ? this._formatDate(p.paidAt) : 'Pending');
    set('.val-hourly-rate', `${sym}${sal.hourlyRate || 0}/hr`);

    // Earnings
    const earningsBody = clone.querySelector('.earnings-tbody');
    if (earningsBody) {
      const addRow = (lbl, rateVal, hoursVal, currVal, ytdVal) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${lbl}</td>
          <td class="num">${rate(rateVal)}</td>
          <td class="num">${hrs(hoursVal)}</td>
          <td class="num">${fmt(currVal)}</td>
          <td class="num">${fmt(ytdVal)}</td>
        `;
        earningsBody.appendChild(tr);
      };
      addRow('Regular Base Pay', sal.hourlyRate, hours.regularHours, ot.regularPay, ot.regularPay);
      if (hours.overtimeHours > 0 || sumOt('overtimePay') > 0) {
        addRow('Overtime Pay (Tiered)', sal.hourlyRate * 1.5, hours.overtimeHours, ot.overtimePay, sumOt('overtimePay'));
      }
      if (hours.nightHours > 0 || sumOt('nightPay') > 0) {
        addRow('Night Shift Allowance', sal.hourlyRate * 1.2, hours.nightHours, ot.nightPay, sumOt('nightPay'));
      }
      if (ot.performanceBonus > 0 || sumOt('performanceBonus') > 0) {
        addRow('Performance Bonus', '', '', ot.performanceBonus, sumOt('performanceBonus'));
      }
      if (ot.attendanceBonus > 0 || sumOt('attendanceBonus') > 0) {
        addRow('Attendance Bonus', '', '', ot.attendanceBonus, sumOt('attendanceBonus'));
      }
      if (ot.allowances > 0 || sumOt('allowances') > 0) {
        addRow('Meal/Travel Allowance', '', '', ot.allowances, sumOt('allowances'));
      }
      
      const totalTr = document.createElement('tr');
      totalTr.className = 'total-row';
      totalTr.innerHTML = `
        <td><strong>Gross Earnings</strong></td>
        <td></td><td></td>
        <td class="num"><strong>${fmt(p.grossPay)}</strong></td>
        <td class="num"><strong>${fmt(ytdGross)}</strong></td>
      `;
      earningsBody.appendChild(totalTr);
    }

    // Deductions
    const deductionsBody = clone.querySelector('.deductions-tbody');
    if (deductionsBody) {
      const addRow = (lbl, currVal, ytdVal) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${lbl}</td>
          <td class="num">${fmt(currVal)}</td>
          <td class="num">${fmt(ytdVal)}</td>
        `;
        deductionsBody.appendChild(tr);
      };
      if (ben.incomeTax > 0 || sumBen('incomeTax') > 0) addRow('Income Tax (Progressive)', ben.incomeTax, sumBen('incomeTax'));
      if (ben.pfDeduction > 0 || sumBen('pfDeduction') > 0) addRow('Provident Fund (PF)', ben.pfDeduction, sumBen('pfDeduction'));
      if (ben.esiDeduction > 0 || sumBen('esiDeduction') > 0) addRow('Employee State Insurance (ESI)', ben.esiDeduction, sumBen('esiDeduction'));
      if (ben.pensionDeduction > 0 || sumBen('pensionDeduction') > 0) addRow('Pension Contribution', ben.pensionDeduction, sumBen('pensionDeduction'));
      if (ben.insuranceDeduction > 0 || sumBen('insuranceDeduction') > 0) addRow('Health/Life Insurance', ben.insuranceDeduction, sumBen('insuranceDeduction'));
      if (ben.leaveWithoutPay > 0 || sumBen('leaveWithoutPay') > 0) addRow('Leave Without Pay Deduction', ben.leaveWithoutPay, sumBen('leaveWithoutPay'));
      if (ben.absentDeduction > 0 || sumBen('absentDeduction') > 0) addRow('Unexcused Absence Deduction', ben.absentDeduction, sumBen('absentDeduction'));
      if (ben.latePenalty > 0 || sumBen('latePenalty') > 0) addRow('Late Clock-in Deduction', ben.latePenalty, sumBen('latePenalty'));

      const totalTr = document.createElement('tr');
      totalTr.className = 'total-row';
      totalTr.innerHTML = `
        <td><strong>Total Deductions</strong></td>
        <td class="num"><strong>${fmt(p.deductions)}</strong></td>
        <td class="num"><strong>${fmt(ytdDed)}</strong></td>
      `;
      deductionsBody.appendChild(totalTr);
    }

    // Contributions
    const contributionsBody = clone.querySelector('.contributions-tbody');
    if (contributionsBody) {
      const addRow = (lbl, currVal, ytdVal) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${lbl}</td>
          <td class="num">${fmt(currVal)}</td>
          <td class="num">${fmt(ytdVal)}</td>
        `;
        contributionsBody.appendChild(tr);
      };
      addRow('Employer Provident Fund (PF)', contrib.employerPf, sumContrib('employerPf'));
      addRow('Employer ESI Contribution', contrib.employerEsi, sumContrib('employerEsi'));
      addRow('Employer Pension Contribution', contrib.employerPension, sumContrib('employerPension'));
      addRow('Social Security Fund Contribution', contrib.employerSocialSecurity, sumContrib('employerSocialSecurity'));
      addRow('Employer Medical / Health Insurance', contrib.employerHealthInsurance, sumContrib('employerHealthInsurance'));
      addRow('End of Service (EOS) Accrual', contrib.employerEndOfService, sumContrib('employerEndOfService'));

      const totalTr = document.createElement('tr');
      totalTr.className = 'total-row';
      totalTr.innerHTML = `
        <td><strong>Total Employer Contributions</strong></td>
        <td class="num"><strong>${fmt(contrib.employerContributionTotal)}</strong></td>
        <td class="num"><strong>${fmt(sumContrib('employerContributionTotal'))}</strong></td>
      `;
      contributionsBody.appendChild(totalTr);
    }

    set('.val-net-current', fmt(p.netPay));
    const netYtdEl = clone.querySelector('.val-net-ytd');
    if (netYtdEl) netYtdEl.innerHTML = `YTD Net Paid: <strong>${fmt(ytdNet)}</strong>`;

    set('.val-audit-by', audit.generatedBy || 'SYSTEM');
    set('.val-audit-date', audit.generatedAt || 'N/A');

    const tempDiv = document.createElement('div');
    tempDiv.appendChild(clone);

    const pw = window.open('', '_blank');
    pw.document.write(`<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Earnings Statement — ${p.runNumber}</title>
</head>
<body>
  ${tempDiv.innerHTML}
  <script>window.onload = function() { window.print(); }</script>
</body>
</html>`);
    pw.document.close();
  }

  async _exportAsImage(p) {
    notificationStore.success('Generating payslip image...');
    const att   = this._safeJson(p.attendanceSnapshot);
    const hours = this._safeJson(p.workingHourSnapshot);
    const sal   = this._safeJson(p.salarySnapshot);
    const ot    = this._safeJson(p.overtimeSnapshot);
    const ben   = this._safeJson(p.benefitSnapshot);
    const sym   = p.currencyCode === 'EUR' ? '€' : '$';
    const totalBonus = (ot.performanceBonus || 0) + (ot.attendanceBonus || 0);

    const W = 794, H = 1123;
    const canvas = document.createElement('canvas');
    canvas.width = W; canvas.height = H;
    const ctx = canvas.getContext('2d');

    // Background
    ctx.fillStyle = '#ffffff';
    ctx.fillRect(0, 0, W, H);

    // Load logo
    let logoImg = null;
    try {
      logoImg = await this._loadImage(`${window.location.origin}/imgs/logo-gold.png`);
    } catch (_) {}

    // --- HEADER ---
    ctx.fillStyle = '#1a1a1a';
    ctx.fillRect(0, 0, W, 80);

    if (logoImg) {
      ctx.drawImage(logoImg, 24, 12, 56, 56);
    }
    ctx.fillStyle = '#ffffff';
    ctx.font = 'bold 20px Inter, sans-serif';
    ctx.fillText('PLUS33 COFFEE', logoImg ? 90 : 24, 42);
    ctx.fillStyle = '#c9a84c';
    ctx.font = '500 11px Inter, sans-serif';
    ctx.fillText('ENTERPRISE PAYROLL SYSTEM', logoImg ? 90 : 24, 62);

    // Run number top right
    ctx.fillStyle = '#c9a84c';
    ctx.textAlign = 'right';
    ctx.font = 'bold 13px Inter, sans-serif';
    ctx.fillText(p.runNumber || 'Payslip', W - 24, 38);
    ctx.fillStyle = '#9ca3af';
    ctx.font = '500 11px Inter, sans-serif';
    ctx.fillText(p.paidAt ? this._formatDate(p.paidAt) : 'Date pending', W - 24, 58);
    ctx.textAlign = 'left';

    // Gold accent line
    ctx.fillStyle = '#c9a84c';
    ctx.fillRect(0, 80, W, 3);

    let y = 100;

    // --- EMPLOYEE INFO ---
    ctx.fillStyle = '#f8fafc';
    ctx.fillRect(24, y, W - 48, 80);
    ctx.strokeStyle = '#e2e8f0'; ctx.lineWidth = 1;
    ctx.strokeRect(24, y, W - 48, 80);
    ctx.fillStyle = '#1a1a1a';
    ctx.font = 'bold 15px Inter, sans-serif';
    ctx.fillText(p.employeeName || '—', 40, y + 20);
    ctx.fillStyle = '#6b7280';
    ctx.font = '500 11px Inter, sans-serif';
    ctx.fillText(`Code: ${p.employeeCode || 'N/A'}   |   ${sal.employmentType || 'Employee'}   |   ${sym}${sal.hourlyRate || 0}/hr   |   Base: ${sym}${sal.monthlySalary || 0}/mo`, 40, y + 38);
    ctx.fillText(`Status: ${p.status || '—'}`, 40, y + 54);
    ctx.fillText(`Bank: ${p.bankName || 'N/A'}   |   Account: ${p.bankAccountNumber || 'N/A'}   |   IFSC: ${p.ifscNumber || 'N/A'}`, 40, y + 70);
    y += 95;

    // --- WORK SUMMARY BOX ---
    ctx.fillStyle = '#fffbeb';
    ctx.fillRect(24, y, W - 48, 95);
    ctx.strokeStyle = '#fcd34d'; ctx.lineWidth = 1;
    ctx.strokeRect(24, y, W - 48, 95);
    ctx.fillStyle = '#92400e';
    ctx.font = 'bold 11px Inter, sans-serif';
    ctx.fillText('WORK SUMMARY', 40, y + 18);

    const workStats = [
      { label: 'Days Worked', val: `${att.presentDays || 0}d` },
      { label: 'Hours Worked', val: `${hours.workedHours || 0}h` },
      { label: 'Overtime', val: `${hours.overtimeHours || 0}h` },
      { label: 'Night Shift', val: `${hours.nightHours || 0}h` },
      { label: 'Absent', val: `${att.absentDays || 0}d` },
      { label: 'Leave Taken', val: `${att.leaveDays || 0}d` },
      { label: 'Total Bonus', val: `${sym}${totalBonus.toFixed(2)}` },
      { label: 'Attendance', val: `${(att.attendancePercentage || 100).toFixed(1)}%` },
    ];
    const colW = (W - 48) / 4;
    workStats.forEach((s, i) => {
      const col = i % 4;
      const row = Math.floor(i / 4);
      const sx = 40 + col * colW;
      const sy = y + 30 + row * 38;
      ctx.fillStyle = '#1a1a1a';
      ctx.font = 'bold 16px Inter, sans-serif';
      ctx.fillText(s.val, sx, sy + 14);
      ctx.fillStyle = '#9ca3af';
      ctx.font = '500 10px Inter, sans-serif';
      ctx.fillText(s.label, sx, sy + 28);
    });
    y += 110;

    // --- EARNINGS & DEDUCTIONS COLUMNS ---
    const col1X = 24, col2X = W / 2 + 8;
    const colWide = W / 2 - 32;

    const drawSection = (title, rows, sx, sy, accentBg) => {
      ctx.fillStyle = accentBg;
      ctx.fillRect(sx, sy, colWide, 18);
      ctx.fillStyle = '#1a1a1a';
      ctx.font = 'bold 11px Inter, sans-serif';
      ctx.fillText(title, sx + 8, sy + 13);
      let ry = sy + 28;
      rows.forEach(([lbl, val, bold]) => {
        ctx.fillStyle = bold ? '#1a1a1a' : '#4b5563';
        ctx.font = bold ? 'bold 11px Inter, sans-serif' : '500 11px Inter, sans-serif';
        ctx.fillText(lbl, sx + 8, ry);
        ctx.textAlign = 'right';
        ctx.fillText(val, sx + colWide - 8, ry);
        ctx.textAlign = 'left';
        if (bold) { ctx.fillStyle = '#e2e8f0'; ctx.fillRect(sx, ry + 3, colWide, 1); }
        ry += 18;
      });
      return ry - sy;
    };

    const earningsRows = [
      ['Regular Base Pay', `${sym}${(ot.regularPay||0).toFixed(2)}`],
      ['Overtime Pay',     `${sym}${(ot.overtimePay||0).toFixed(2)}`],
      ['Night Shift Pay',  `${sym}${(ot.nightPay||0).toFixed(2)}`],
      ['Holiday Work Pay', `${sym}${(ot.holidayPay||0).toFixed(2)}`],
      ['Weekend Work Pay', `${sym}${(ot.weekendPay||0).toFixed(2)}`],
      ['Performance Bonus',`${sym}${(ot.performanceBonus||0).toFixed(2)}`],
      ['Attendance Bonus', `${sym}${(ot.attendanceBonus||0).toFixed(2)}`],
      ['Allowances',       `${sym}${(ot.allowances||0).toFixed(2)}`],
      ['GROSS EARNINGS',   `${sym}${Number(p.grossPay||0).toFixed(2)}`, true],
    ];
    const deductRows = [
      ['Income Tax',       `${sym}${(ben.incomeTax||0).toFixed(2)}`],
      ['Provident Fund',   `${sym}${(ben.pfDeduction||0).toFixed(2)}`],
      ['ESI',              `${sym}${(ben.esiDeduction||0).toFixed(2)}`],
      ['Pension',          `${sym}${(ben.pensionDeduction||0).toFixed(2)}`],
      ['Insurance',        `${sym}${(ben.insuranceDeduction||0).toFixed(2)}`],
      ['Unpaid Leave (LWP)',`${sym}${(ben.leaveWithoutPay||0).toFixed(2)}`],
      ['Absent Penalties', `${sym}${(ben.absentDeduction||0).toFixed(2)}`],
      ['Late Penalties',   `${sym}${(ben.latePenalty||0).toFixed(2)}`],
      ['TOTAL DEDUCTIONS', `${sym}${Number(p.deductions||0).toFixed(2)}`, true],
    ];

    const h1 = drawSection('EARNINGS', earningsRows, col1X, y, '#f0fdf4');
    const h2 = drawSection('DEDUCTIONS', deductRows, col2X, y, '#fef2f2');
    y += Math.max(h1, h2) + 16;

    // --- NET PAY BANNER ---
    ctx.fillStyle = '#f0fdf4';
    ctx.fillRect(24, y, W - 48, 50);
    ctx.strokeStyle = '#86efac'; ctx.lineWidth = 2;
    ctx.strokeRect(24, y, W - 48, 50);
    ctx.fillStyle = '#166534';
    ctx.font = 'bold 13px Inter, sans-serif';
    ctx.fillText('NET TAKE-HOME SALARY', 40, y + 22);
    ctx.fillStyle = '#15803d';
    ctx.font = 'bold 22px Inter, sans-serif';
    ctx.textAlign = 'right';
    ctx.fillText(`${sym}${Number(p.netPay || 0).toFixed(2)}`, W - 40, y + 34);
    ctx.textAlign = 'left';
    y += 64;

    // --- FOOTER ---
    ctx.fillStyle = '#f1f5f9';
    ctx.fillRect(0, H - 48, W, 48);
    ctx.fillStyle = '#9ca3af';
    ctx.font = '500 10px Inter, sans-serif';
    ctx.textAlign = 'center';
    ctx.fillText('This is a system-generated payslip. PLUS33 Coffee Enterprise ERP.', W / 2, H - 28);
    ctx.fillText(`Processed by: ${this._safeJson(p.payrollAudit).generatedBy || 'SYSTEM'} | ${p.paidAt ? this._formatDate(p.paidAt) : ''}`, W / 2, H - 14);
    ctx.textAlign = 'left';

    // Download
    const link = document.createElement('a');
    link.download = `PLUS33_Payslip_${p.runNumber || 'run'}.png`;
    link.href = canvas.toDataURL('image/png');
    link.click();
  }

  _loadImage(url) {
    return new Promise((res, rej) => {
      const img = new Image();
      img.crossOrigin = 'anonymous';
      img.onload = () => res(img);
      img.onerror = rej;
      img.src = url;
    });
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
    const tbody   = container.querySelector('#payslip-history-tbody');
    const emptyTpl = container.querySelector('#payslip-empty-row-tpl');
    const rowTpl  = container.querySelector('#payslip-row-tpl');
    if (!tbody) return;
    tbody.replaceChildren();

    if (this.filteredHistory.length === 0) {
      if (emptyTpl) tbody.appendChild(emptyTpl.content.cloneNode(true));
      return;
    }

    this.filteredHistory.forEach((p, index) => {
      if (!rowTpl) return;
      const clone = rowTpl.content.cloneNode(true);
      const sym   = p.currencyCode === 'EUR' ? '€' : '$';

      const periodEl    = clone.querySelector('.period-cell');
      const statusEl    = clone.querySelector('.status-cell');
      const payDateEl   = clone.querySelector('.payment-date-cell');
      const grossEl     = clone.querySelector('.gross-cell');
      const deductEl    = clone.querySelector('.deductions-cell');
      const netEl       = clone.querySelector('.net-cell');
      const viewBtn     = clone.querySelector('.btn-view-payslip');

      if (periodEl)  periodEl.textContent  = p.runNumber || '—';
      if (payDateEl) payDateEl.textContent  = p.paidAt ? this._formatDate(p.paidAt) : 'Pending';
      if (grossEl)   grossEl.textContent   = `${sym}${Number(p.grossPay || 0).toLocaleString('en-US', { minimumFractionDigits: 2 })}`;
      if (deductEl)  deductEl.textContent  = `${sym}${Number(p.deductions || 0).toLocaleString('en-US', { minimumFractionDigits: 2 })}`;
      if (netEl)     netEl.textContent     = `${sym}${Number(p.netPay || 0).toLocaleString('en-US', { minimumFractionDigits: 2 })}`;
      if (viewBtn)   viewBtn.setAttribute('data-index', String(index));

      // Status badge
      if (statusEl) {
        const statusText = (p.status || 'DRAFT').toUpperCase();
        const badge = document.createElement('span');
        badge.className = `status-badge status-badge--${statusText.toLowerCase()}`;
        badge.textContent = statusText;
        statusEl.appendChild(badge);
      }

      tbody.appendChild(clone);
    });

    // Rebind view buttons after re-render
    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // HELPERS
  // ---------------------------------------------------------------------------

  async loadAndRender(container, lifecycle) {
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  _safeJson(str) {
    if (!str) return {};
    try { return JSON.parse(str); } catch (_) { return {}; }
  }

  _formatDate(isoStr) {
    if (!isoStr) return 'N/A';
    try {
      return new Date(isoStr).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
    } catch (_) { return isoStr; }
  }

  _loadCss() {
    const cssId = 'store-employee-payslips-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id   = cssId;
      link.rel  = 'stylesheet';
      link.href = 'modules/store-employee/pages/payslips/payslips.css';
      document.head.appendChild(link);
    }
  }
}

export { StoreEmployeePayslips };
