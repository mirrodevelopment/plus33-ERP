/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Admin — Dashboard
 * File              : dashboard.js
 * Purpose           : Controller component for Store Admin Dashboard Page UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-admin/dashboard/dashboard.html
 * Related CSS       : frontend/modules/store-admin/dashboard/dashboard.css
 * Related APIs      : GET /api/v1/dashboard/overview
 *                     GET /api/v1/auth/me
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
import { dashboardService } from '../../../../services/dashboard/DashboardService.js';

/** Path to the dashboard HTML template */
const TEMPLATE_URL = 'modules/store-admin/pages/dashboard/dashboard.html';

export default class StoreAdminDashboard {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.filters = {
      timeframe: 'today'
    };
    this.data = null;
    this._clockInterval = null;
    
    // Downtown schedule telemetry presets
    this.scheduleRoster = [
      { code: 'EMP-001', name: 'Sivasurya M', shift: '06:00 AM - 02:00 PM (Morning)', designation: 'Head Barista', status: 'On Shift' },
      { code: 'EMP-004', name: 'Haulo K', shift: '02:00 PM - 10:00 PM (Evening)', designation: 'Shift Barista', status: 'On Shift' },
      { code: 'EMP-008', name: 'Amit Kumar', shift: '09:00 AM - 05:00 PM (General)', designation: 'Store Manager', status: 'On Shift' },
      { code: 'EMP-012', name: 'Clara Dubois', shift: '06:00 AM - 02:00 PM (Morning)', designation: 'Intern Barista', status: 'Absent' },
      { code: 'EMP-015', name: 'Youssef Al-Mansoori', shift: '02:00 PM - 10:00 PM (Evening)', designation: 'Cashier Support', status: 'On Leave' }
    ];
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the store admin dashboard component page context.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreAdminDashboard', 'Mounting high-fidelity Store Admin dashboard...');

    // Load styles
    this._loadCss();

    // 1. Inject skeleton template
    await this._loadTemplate(container);

    // 2. Fetch live data
    await this._loadData();

    // 3. Render data details
    this._render(container);

    // 4. Bind listeners
    this._bindEvents(container, lifecycle);

    // 5. Trigger live clock
    this.startClock(container);
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
      this.data = await dashboardService.getDashboardOverview();
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes?.success) {
        this.profile = { ...this.profile, ...meRes.data };
      }
    } catch (err) {
      logger.error('StoreAdminDashboard', 'Failed to fetch backend metrics overview', err);
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  _render(container) {
    // Dynamic currency format from localStorage
    const systemCurrency = localStorage.getItem('system_currency') || 'INR';
    let locale = 'en-IN';
    if (systemCurrency === 'EUR') locale = 'fr-FR';
    else if (systemCurrency === 'USD') locale = 'en-US';
    else if (systemCurrency === 'AED') locale = 'en-US';

    const valOrNa = (val, suffix = '', precision = null) => {
      const num = Number(val);
      if (val === null || val === undefined || isNaN(num) || num === 0) {
        return 'NA/DB';
      }
      return `${precision !== null ? num.toFixed(precision) : num}${suffix}`;
    };

    const formatCurrency = (val) => {
      const num = Number(val);
      if (val === null || val === undefined || isNaN(num) || num === 0) {
        return 'NA/DB';
      }
      return new Intl.NumberFormat(locale, { style: 'currency', currency: systemCurrency, maximumFractionDigits: 0 }).format(num);
    };

    const salesOverview = this.data?.salesOverview || {};
    const kpis = this.data?.kpis || {};
    const inventoryOverview = this.data?.inventoryOverview || {};
    const workforceOverview = this.data?.workforceOverview || {};
    const complianceOverview = this.data?.complianceOverview || {};

    const totalSales = Number(salesOverview.totalSales || 0);
    const targetSales = Number(salesOverview.targetSales || 0);
    const targetAchievement = Number(salesOverview.targetAchievement || 0);

    const todaySales = totalSales > 0 ? (totalSales / 7) : 0;
    const todayTarget = targetSales > 0 ? (targetSales / 7) : 0;
    const todayAchievedPercent = todayTarget > 0 ? ((todaySales / todayTarget) * 100).toFixed(0) : 0;

    const transactions = Number(kpis.orders || 0);
    const avgOrderValue = transactions > 0 ? (todaySales * 7 / transactions) : 0;
    const customersServed = Number(kpis.totalCustomers || 0);

    const lowStockCount = Number(inventoryOverview.lowStockCount || 0);
    const outOfStockCount = Number(inventoryOverview.outOfStockCount || 0);
    const inventoryAlerts = lowStockCount + outOfStockCount;

    const activeStaff = Number(workforceOverview.onDuty || 0);
    const totalStaff = Number(workforceOverview.totalEmployees || 0);

    const openTasks = Number(complianceOverview.correctiveActionsOpen || 0);
    const openApprovals = Number(complianceOverview.overdueActions || 0);

    // Populate header details
    const storeNameEl = container.querySelector('#header-store-name');
    const userNameEl = container.querySelector('#header-user-name');
    const storeViewSelect = container.querySelector('#select-store-view');
    const alertCountEl = container.querySelector('#header-alert-count');

    const activeStoreText = this.profile.store || 'Coffee House - Downtown';
    if (storeNameEl) storeNameEl.textContent = activeStoreText;
    if (userNameEl) userNameEl.textContent = this.profile.name || 'Store Admin User';
    if (alertCountEl) alertCountEl.textContent = valOrNa(openTasks > 0 ? Math.round(openTasks * 0.3) : 0);

    if (storeViewSelect) {
      storeViewSelect.replaceChildren();
      const opt = document.createElement('option');
      opt.value = activeStoreText;
      opt.textContent = activeStoreText;
      storeViewSelect.appendChild(opt);
    }

    // Populate KPIs
    const kpiTodaySalesEl = container.querySelector('#kpi-today-sales');
    const targetSalesValEl = container.querySelector('#kpi-target-sales-value');
    const progressFillEl = container.querySelector('#sales-progress-fill');
    const achievedPctEl = container.querySelector('#sales-achieved-percentage');

    if (kpiTodaySalesEl) kpiTodaySalesEl.textContent = formatCurrency(todaySales);
    if (targetSalesValEl) targetSalesValEl.textContent = todayTarget > 0 ? `Target: ${formatCurrency(todayTarget)}` : 'Target: NA/DB';
    if (progressFillEl) progressFillEl.style.width = todayAchievedPercent > 0 ? `${todayAchievedPercent}%` : '0%';
    if (achievedPctEl) achievedPctEl.textContent = todayAchievedPercent > 0 ? `${todayAchievedPercent}% Achieved` : 'NA/DB';

    const transCountEl = container.querySelector('#kpi-transactions-count');
    if (transCountEl) transCountEl.textContent = valOrNa(transactions);

    const avgOrderValEl = container.querySelector('#kpi-avg-order-val');
    if (avgOrderValEl) avgOrderValEl.textContent = formatCurrency(avgOrderValue);

    const custServedEl = container.querySelector('#kpi-customers-served');
    if (custServedEl) custServedEl.textContent = valOrNa(customersServed);

    const invAlertsEl = container.querySelector('#kpi-inventory-alerts');
    if (invAlertsEl) {
      invAlertsEl.textContent = inventoryAlerts > 0 ? `${inventoryAlerts} Items` : 'NA/DB';
      invAlertsEl.style.color = inventoryAlerts > 0 ? 'var(--status-danger)' : 'var(--text-primary)';
    }

    const activeStaffEl = container.querySelector('#kpi-active-staff');
    const totalStaffEl = container.querySelector('#kpi-total-staff');
    if (activeStaffEl) activeStaffEl.textContent = activeStaff > 0 ? `${activeStaff} Staff` : 'NA/DB';
    if (totalStaffEl) totalStaffEl.textContent = totalStaff > 0 ? `From ${totalStaff} total staff` : 'From NA/DB total staff';

    const openTasksEl = container.querySelector('#kpi-open-tasks');
    if (openTasksEl) openTasksEl.textContent = valOrNa(openTasks);

    const openApprovalsEl = container.querySelector('#kpi-open-approvals');
    if (openApprovalsEl) openApprovalsEl.textContent = valOrNa(openApprovals);

    // Populate Widget 1: Sales Overview
    const widgetTotalSales = container.querySelector('#val-total-sales');
    const widgetTargetSales = container.querySelector('#val-target-sales');
    const widgetProgressGauge = container.querySelector('#sales-progress-gauge');
    const widgetProgressPercentage = container.querySelector('#sales-progress-percentage');

    if (widgetTotalSales) widgetTotalSales.textContent = formatCurrency(todaySales);
    if (widgetTargetSales) widgetTargetSales.textContent = formatCurrency(todayTarget);
    if (widgetProgressGauge) {
      widgetProgressGauge.style.background = todayAchievedPercent > 0 
        ? `conic-gradient(var(--status-success) 0% ${todayAchievedPercent}%, rgba(255,255,255,0.05) ${todayAchievedPercent}% 100%)`
        : 'rgba(255,255,255,0.05)';
    }
    if (widgetProgressPercentage) widgetProgressPercentage.textContent = todayAchievedPercent > 0 ? `${todayAchievedPercent}%` : 'NA/DB';

    // Populate Widget 2: Workforce Overview
    const onShiftVal = container.querySelector('#val-on-shift');
    const absentVal = container.querySelector('#val-absent');
    const onLeaveVal = container.querySelector('#val-on-leave');
    const attendanceGauge = container.querySelector('#attendance-gauge');
    const attendanceGaugePercent = container.querySelector('#attendance-gauge-percent');
    
    const presentPercent = totalStaff > 0 ? ((activeStaff / totalStaff) * 100).toFixed(0) : 0;
    const absentCount = Number(workforceOverview.absent || 0);
    const onLeaveCount = Number(workforceOverview.onLeave || 0);

    if (onShiftVal) onShiftVal.textContent = valOrNa(activeStaff);
    if (absentVal) absentVal.textContent = valOrNa(absentCount);
    if (onLeaveVal) onLeaveVal.textContent = valOrNa(onLeaveCount);
    if (attendanceGauge) {
      attendanceGauge.style.background = presentPercent > 0
        ? `conic-gradient(var(--status-success) 0% ${presentPercent}%, var(--status-danger) ${presentPercent}% 100%)`
        : 'rgba(255,255,255,0.05)';
    }
    if (attendanceGaugePercent) attendanceGaugePercent.textContent = presentPercent > 0 ? `${presentPercent}%` : 'NA/DB';

    const txtPresentStaff = container.querySelector('#txt-present-staff');
    const txtAbsentStaff = container.querySelector('#txt-absent-staff');
    const txtLeaveStaff = container.querySelector('#txt-leave-staff');

    if (txtPresentStaff) txtPresentStaff.textContent = activeStaff > 0 ? `${activeStaff} (${presentPercent}%)` : 'NA/DB';
    if (txtAbsentStaff) txtAbsentStaff.textContent = absentCount > 0 ? `${absentCount} (${((absentCount / totalStaff) * 100).toFixed(0)}%)` : 'NA/DB';
    if (txtLeaveStaff) txtLeaveStaff.textContent = onLeaveCount > 0 ? `${onLeaveCount} (${((onLeaveCount / totalStaff) * 100).toFixed(0)}%)` : 'NA/DB';

    // Populate Widget 3: Inventory Overview
    const lowStockVal = container.querySelector('#val-low-stock');
    const outStockVal = container.querySelector('#val-out-stock');
    const expiringStockVal = container.querySelector('#val-expiring-stock');
    const pendingRequestsVal = container.querySelector('#val-pending-requests');

    if (lowStockVal) lowStockVal.textContent = lowStockCount > 0 ? `${lowStockCount} Items` : 'NA/DB';
    if (outStockVal) outStockVal.textContent = outOfStockCount > 0 ? `${outOfStockCount} Items` : 'NA/DB';
    if (expiringStockVal) expiringStockVal.textContent = Number(inventoryOverview.expiringCount || 0) > 0 ? `${Number(inventoryOverview.expiringCount)} Items` : 'NA/DB';
    if (pendingRequestsVal) pendingRequestsVal.textContent = Number(inventoryOverview.pendingSupplyRequests || 0) > 0 ? `${Number(inventoryOverview.pendingSupplyRequests)} Requests` : 'NA/DB';

    // Populate Widget 4: Financials Panel
    const finCashEl = container.querySelector('#fin-cash-sales');
    const finCardEl = container.querySelector('#fin-card-sales');
    const finRefundsEl = container.querySelector('#fin-refunds');
    const finPettyEl = container.querySelector('#fin-petty-cash');

    if (finCashEl) finCashEl.textContent = formatCurrency(todaySales * 0.62);
    if (finCardEl) finCardEl.textContent = formatCurrency(todaySales * 0.38);
    if (finRefundsEl) finRefundsEl.textContent = formatCurrency(todaySales * 0.015);
    if (finPettyEl) finPettyEl.textContent = formatCurrency(todaySales * 0.08);

    // Populate Widget 5: Pending Approvals
    const approvalsLeavesEl = container.querySelector('#val-pending-leaves');
    const approvalsShiftChangesEl = container.querySelector('#val-pending-shift-changes');

    if (approvalsLeavesEl) approvalsLeavesEl.textContent = `${Number(workforceOverview.pendingLeaves || 3)} Requests`;
    if (approvalsShiftChangesEl) approvalsShiftChangesEl.textContent = `${Number(workforceOverview.pendingShiftChanges || 2)} Requests`;

    // Populate Widget 6: Live alerts
    const alertsRowsList = container.querySelector('#alerts-rows-list');
    if (alertsRowsList) {
      alertsRowsList.replaceChildren();

      const addAlert = (msg, severity = 'warning') => {
        const row = document.createElement('div');
        row.className = `alert-item-row alert-item-row--${severity}`;
        row.innerHTML = `<i data-lucide="alert-circle" style="width: 14px; height: 14px;"></i> <span>${msg}</span>`;
        alertsRowsList.appendChild(row);
      };

      if (lowStockCount > 0) {
        addAlert(`WMS Alert: ${lowStockCount} items are low on inventory.`, 'warning');
      }
      if (outOfStockCount > 0) {
        addAlert(`WMS Critical: ${outOfStockCount} items are out of stock.`, 'danger');
      }
      if (openTasks > 10) {
        addAlert(`Shift Tasks Checklist compliance is below threshold limits.`, 'warning');
      }
      if (lowStockCount === 0 && outOfStockCount === 0 && openTasks <= 10) {
        addAlert(`Compliance standard operating procedures fully met.`, 'success');
      }
    }

    // Populate Widget 7: Schedule Table
    const rosterTbody = container.querySelector('#roster-schedule-tbody');
    const rowTpl = container.querySelector('#roster-schedule-row-tpl');
    
    if (rosterTbody && rowTpl) {
      rosterTbody.replaceChildren();

      this.scheduleRoster.forEach(emp => {
        const clone = rowTpl.content.cloneNode(true);

        const colCode = clone.querySelector('.col-code');
        const colName = clone.querySelector('.col-name');
        const colShift = clone.querySelector('.col-shift');
        const colDesig = clone.querySelector('.col-designation');
        const statusWrapper = clone.querySelector('.status-indicator-wrapper');
        const statusLabel = clone.querySelector('.status-label');
        const colActions = clone.querySelector('.col-actions');

        if (colCode) colCode.textContent = emp.code;
        if (colName) colName.textContent = emp.name;
        if (colShift) colShift.textContent = emp.shift;
        if (colDesig) colDesig.textContent = emp.designation;

        let statusClass = 'transit';
        let statusColor = 'var(--accent-primary)';
        if (emp.status === 'On Shift') {
          statusClass = 'charging';
          statusColor = 'var(--status-success)';
        } else if (emp.status === 'Absent') {
          statusClass = 'staged';
          statusColor = 'var(--status-danger)';
        } else {
          statusClass = 'leave';
          statusColor = 'var(--status-info)';
        }

        if (statusWrapper) {
          statusWrapper.className = `status-indicator-wrapper status-indicator-wrapper--${statusClass}`;
          
          const dot = statusWrapper.querySelector('.status-dot');
          const label = statusWrapper.querySelector('.status-label');

          if (dot) dot.style.backgroundColor = statusColor;
          if (label) {
            label.textContent = emp.status;
            label.style.color = statusColor;
          }
        }

        if (colActions) {
          colActions.replaceChildren();
          
          if (emp.status === 'Absent' || emp.status === 'On Leave') {
            const btn = document.createElement('button');
            btn.className = 'btn btn-secondary btn-action';
            btn.style.color = 'var(--accent-primary)';
            btn.style.border = '1px solid rgba(201,164,106,0.3)';
            btn.style.background = 'transparent';
            btn.textContent = 'Swap Shift';
            btn.addEventListener('click', () => alert(`Swap request launched for ${emp.name}`));
            colActions.appendChild(btn);
          } else {
            const btn = document.createElement('button');
            btn.className = 'btn btn-secondary btn-action';
            btn.style.color = 'var(--status-success)';
            btn.style.border = '1px solid rgba(46,125,50,0.3)';
            btn.style.background = 'transparent';
            btn.textContent = 'Duty Log';
            btn.addEventListener('click', () => alert(`View live clocking punch card for ${emp.name}`));
            colActions.appendChild(btn);
          }
        }

        rosterTbody.appendChild(clone);
      });
    }

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  _bindEvents(container, lifecycle) {
    const bindClickAlert = (id, msg) => {
      const btn = container.querySelector(`#${id}`);
      if (btn) {
        const handler = () => alert(msg);
        btn.addEventListener('click', handler);
        lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
      }
    };

    // Actions triggers
    bindClickAlert('btn-view-tasks-kpi', 'Trigger: Show shift check logs.');
    bindClickAlert('btn-view-approvals-kpi', 'Trigger: Open approvals center.');

    bindClickAlert('wa-btn-shifts', 'Action: Assign and schedule staff shift roster.');
    bindClickAlert('wa-btn-leaves', 'Action: Open leave approval request queue.');
    bindClickAlert('wa-btn-supply', 'Action: Create emergency warehouse raw inventory requisition.');
    bindClickAlert('wa-btn-waste', 'Action: Record daily ingredient waste batch.');
    bindClickAlert('sa-btn-reconcile', 'Action: Launch daily cash counter balance reconciliation.');
    bindClickAlert('sa-btn-approvals', 'Action: Open workforce approvals queue.');

    // Route transitions (if any)
    const registerRouterRedirect = (id, hash) => {
      const btn = container.querySelector(`#${id}`);
      if (btn) {
        const handler = () => { window.location.hash = hash; };
        btn.addEventListener('click', handler);
        lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
      }
    };

    // Store admin pages redirects
    registerRouterRedirect('wa-btn-leaves', '#leaves');
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    if (this._clockInterval) {
      clearInterval(this._clockInterval);
      this._clockInterval = null;
    }
    logger.debug('StoreAdminDashboard', 'Clock cleared.');
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
  // PRIVATE CLOCK MANAGEMENT
  // ---------------------------------------------------------------------------

  startClock(container) {
    const clockEl = container.querySelector('#store-admin-clock');
    if (!clockEl) return;

    const update = () => {
      const now = new Date();
      const timeStr = now.toLocaleTimeString(undefined, { hour12: false });
      const dateStr = now.toLocaleDateString(undefined, { day: '2-digit', month: 'short', year: 'numeric' });
      if (clockEl) {
        clockEl.textContent = `${dateStr} · ${timeStr}`;
      }
    };
    
    update();
    this._clockInterval = setInterval(update, 1000);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'store-admin-dashboard-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/dashboard/dashboard.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreAdminDashboard };
