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
import { eventBus } from '../../../../core/eventBus.js';
import { StoreAdminDashboardCharts } from './dashboard.charts.js';

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
    this.storeId = null;
    this.employees = [];
    this.storeDetails = null;
    this.pendingLeaves = [];
    this.awayPermissions = [];
    this.ongoingAwayPermissions = [];
    
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

    // 3b. After render, broadcast pending document count so layout updates sidebar dot + bell
    const workforceOverview = this.data?.workforceOverview || {};
    const pendingDocs = Number(workforceOverview.pendingDocuments || 0);
    eventBus.emit('workforce:pending-docs', { count: pendingDocs });

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
        this.storeId = meRes.data.storeId || null;
      }

      // Fetch store details to get timezone
      if (this.storeId) {
        const storeRes = await apiClient.get(`/api/v1/stores/${this.storeId}`);
        this.storeDetails = storeRes?.success ? storeRes.data : null;
      } else {
        this.storeDetails = null;
      }

      // Fetch store employees for the worker roster
      if (this.storeId) {
        const empRes = await apiClient.get(`/api/v1/employees?storeId=${this.storeId}&size=50&status=ACTIVE`);
        this.employees = empRes?.success ? (empRes.data.content || []) : [];
      } else {
        this.employees = [];
      }

      // Fetch pending leaves, away permissions, and ongoing away permissions for approvals card
      const [pendingRes, awayRes, ongoingAwayRes] = await Promise.all([
        apiClient.get('/leaves/pending').catch(() => null),
        apiClient.get('/api/v1/away-permission/pending').catch(() => null),
        apiClient.get('/api/v1/away-permission/ongoing').catch(() => null)
      ]);
      if (pendingRes?.success) {
        this.pendingLeaves = pendingRes.data?.pending || [];
      } else {
        this.pendingLeaves = [];
      }
      this.awayPermissions = awayRes?.success ? (awayRes.data || []) : [];
      this.ongoingAwayPermissions = ongoingAwayRes?.success ? (ongoingAwayRes.data || []) : [];
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
    const storeTypeEl = container.querySelector('#header-store-type');
    const storeIdEl = container.querySelector('#header-store-id');
    const userNameEl = container.querySelector('#header-user-name');
    const storeViewSelect = container.querySelector('#select-store-view');
    const alertCountEl = container.querySelector('#header-alert-count');

    const activeStoreText = this.profile.store || 'Coffee House - Downtown';
    if (storeNameEl) storeNameEl.textContent = activeStoreText;
    if (storeIdEl) storeIdEl.textContent = this.storeId || '—';
    if (userNameEl) userNameEl.textContent = this.profile.name || 'Store Admin User';

    if (storeTypeEl) {
      const typeVal = this.storeDetails?.type;
      const friendlyType = typeVal === 'COMPACT_CAFE' ? 'COMPACT CAFÉ' : typeVal === 'FLAGSHIP_CAFE' ? 'FLAGSHIP CAFÉ' : (typeVal || 'COMPACT CAFÉ');
      storeTypeEl.textContent = friendlyType;
    }
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
    
    // Render Sales Progress Gauge using helper
    StoreAdminDashboardCharts.renderSalesProgress(container, todayAchievedPercent);

    // Populate Widget 2: Workforce Overview
    const onShiftVal = container.querySelector('#val-on-shift');
    const absentVal = container.querySelector('#val-absent');
    const onLeaveVal = container.querySelector('#val-on-leave');
    
    const presentPercent = totalStaff > 0 ? ((activeStaff / totalStaff) * 100).toFixed(0) : 0;
    const absentCount = Number(workforceOverview.absent || 0);
    const onLeaveCount = Number(workforceOverview.onLeave || 0);

    if (onShiftVal) onShiftVal.textContent = valOrNa(activeStaff);
    if (absentVal) absentVal.textContent = valOrNa(absentCount);
    if (onLeaveVal) onLeaveVal.textContent = valOrNa(onLeaveCount);
    
    // Render Attendance Gauge using helper
    StoreAdminDashboardCharts.renderAttendanceGauge(container, presentPercent);

    const txtPresentStaff = container.querySelector('#txt-present-staff');
    const txtAbsentStaff = container.querySelector('#txt-absent-staff');
    const txtLeaveStaff = container.querySelector('#txt-leave-staff');

    if (txtPresentStaff) txtPresentStaff.textContent = activeStaff > 0 ? `${activeStaff} (${presentPercent}%)` : 'NA/DB';
    if (txtAbsentStaff) txtAbsentStaff.textContent = absentCount > 0 ? `${absentCount} (${((absentCount / totalStaff) * 100).toFixed(0)}%)` : 'NA/DB';
    if (txtLeaveStaff) txtLeaveStaff.textContent = onLeaveCount > 0 ? `${onLeaveCount} (${((onLeaveCount / totalStaff) * 100).toFixed(0)}%)` : 'NA/DB';

    // Populate Widget 3: Supply Chain & Inventory Redesigned
    const valTotalStock = container.querySelector('#val-total-stock-items');
    const valLowStock = container.querySelector('#val-low-stock-items');
    const valOutStock = container.querySelector('#val-out-stock-items');
    const exp30 = container.querySelector('#exp-30-days');
    const exp60 = container.querySelector('#exp-60-days');
    const exp90 = container.querySelector('#exp-90-days');

    const totalStockVal = Number(inventoryOverview.stockInHand || 0);
    if (valTotalStock) valTotalStock.textContent = totalStockVal.toLocaleString();
    if (valLowStock) valLowStock.textContent = valOrNa(lowStockCount);
    if (valOutStock) valOutStock.textContent = valOrNa(outOfStockCount);

    const expiryAlerts = inventoryOverview.expiryAlerts || {};
    if (exp30) exp30.textContent = valOrNa(expiryAlerts.within30);
    if (exp60) exp60.textContent = valOrNa(expiryAlerts.within60);
    if (exp90) exp90.textContent = valOrNa(expiryAlerts.within90);

    // Draw inventory donut chart using helper
    StoreAdminDashboardCharts.renderInventoryDonut(container, inventoryOverview);

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
    const approvalsDocumentsEl = container.querySelector('#val-pending-documents');
    const approvalsExpensesEl = container.querySelector('#val-pending-expenses');

    if (approvalsLeavesEl) approvalsLeavesEl.textContent = Number(workforceOverview.pendingLeaves !== undefined ? workforceOverview.pendingLeaves : 0);
    if (approvalsShiftChangesEl) approvalsShiftChangesEl.textContent = Number(workforceOverview.pendingShiftChanges !== undefined ? workforceOverview.pendingShiftChanges : 0);
    if (approvalsDocumentsEl) approvalsDocumentsEl.textContent = Number(workforceOverview.pendingDocuments !== undefined ? workforceOverview.pendingDocuments : 0);
    if (approvalsExpensesEl) approvalsExpensesEl.textContent = Number(workforceOverview.pendingExpenses !== undefined ? workforceOverview.pendingExpenses : 0);

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

      let hasAlerts = false;

      // Render database-driven alerts
      if (this.data?.alerts && Array.isArray(this.data.alerts)) {
        this.data.alerts.forEach(alert => {
          let sev = 'warning';
          const type = String(alert.type || '').toUpperCase();
          if (type === 'DANGER' || type === 'CRITICAL' || type === 'ERROR') {
            sev = 'danger';
          } else if (type === 'SUCCESS') {
            sev = 'success';
          }
          addAlert(alert.message, sev);
          hasAlerts = true;
        });
      }

      if (lowStockCount > 0) {
        addAlert(`WMS Alert: ${lowStockCount} items are low on inventory.`, 'warning');
        hasAlerts = true;
      }
      if (outOfStockCount > 0) {
        addAlert(`WMS Critical: ${outOfStockCount} items are out of stock.`, 'danger');
        hasAlerts = true;
      }
      if (openTasks > 10) {
        addAlert(`Shift Tasks Checklist compliance is below threshold limits.`, 'warning');
        hasAlerts = true;
      }
      if (!hasAlerts) {
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

    // Render worker roster list
    this._renderWorkerRoster(container);
    this._renderApprovalsHub(container);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Worker Roster Render
  // ---------------------------------------------------------------------------

  /**
   * Renders the mini worker list inside the Workforce Overview card.
   * Shows avatar, full name, designation, and today's attendance status.
   */
  _renderWorkerRoster(container) {
    const listEl = container.querySelector('#wf-worker-list');
    if (!listEl) return;

    listEl.replaceChildren();

    const employees = this.employees || [];

    if (employees.length === 0) {
      const empty = document.createElement('div');
      empty.className = 'wf-worker-empty';
      empty.textContent = 'No active workers found for this store.';
      listEl.appendChild(empty);
      return;
    }

    employees.slice(0, 5).forEach(emp => {
      const fullName = `${emp.firstName || ''} ${emp.lastName || ''}`.trim() || 'Unknown';
      const initials = `${emp.firstName ? emp.firstName[0] : ''}${emp.lastName ? emp.lastName[0] : ''}`.toUpperCase() || 'EE';
      const designation = emp.designation || emp.workerType || 'Staff';

      // Determine status
      const rawStatus = (emp.todayStatus || '').toUpperCase();
      let statusLabel = 'Unknown';
      let statusClass = 'wf-status--unknown';
      if (rawStatus.startsWith('PRESENT') || rawStatus === 'LATE' || rawStatus === 'PRESENT_HALF') {
        statusLabel = 'On Shift';
        statusClass = 'wf-status--present';
      } else if (rawStatus === 'ON_LEAVE') {
        statusLabel = 'On Leave';
        statusClass = 'wf-status--leave';
      } else if (rawStatus === 'ABSENT' || rawStatus === '') {
        statusLabel = 'Absent';
        statusClass = 'wf-status--absent';
      }

      const row = document.createElement('div');
      row.className = 'wf-worker-row';

      // Avatar
      const avatarEl = document.createElement('div');
      avatarEl.className = 'wf-worker-avatar';
      if (emp.avatarUrl) {
        const img = document.createElement('img');
        img.src = emp.avatarUrl;
        img.alt = fullName;
        img.onerror = () => { avatarEl.textContent = initials; };
        avatarEl.appendChild(img);
      } else {
        avatarEl.textContent = initials;
      }

      // Info
      const infoEl = document.createElement('div');
      infoEl.className = 'wf-worker-info';
      infoEl.innerHTML = `
        <span class="wf-worker-name">${fullName}</span>
        <span class="wf-worker-role">${designation}</span>
      `;

      // Status badge
      const statusEl = document.createElement('span');
      statusEl.className = `wf-worker-status ${statusClass}`;
      statusEl.textContent = statusLabel;

      row.appendChild(avatarEl);
      row.appendChild(infoEl);
      row.appendChild(statusEl);
      listEl.appendChild(row);
    });
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

    // Bind Approvals Hub actions
    this._bindApprovalsHubEvents(container, lifecycle);
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
    const nationEl = container.querySelector('#store-admin-nation');
    if (!clockEl) return;

    // Timezone mapping to 3-letter nation code
    const TIMEZONE_TO_NATION = {
      'Europe/Paris': 'FRA',
      'Asia/Kolkata': 'IND',
      'America/New_York': 'USA',
      'America/Chicago': 'USA',
      'America/Denver': 'USA',
      'America/Los_Angeles': 'USA',
      'Asia/Dubai': 'UAE',
      'Asia/Singapore': 'SGP',
      'Europe/London': 'GBR',
      'UTC': 'UTC',
      'GMT': 'GMT'
    };

    // Determine target timezone and nation abbreviation
    let timezone = this.storeDetails?.timezone || 'Europe/Paris';
    let nationCode = TIMEZONE_TO_NATION[timezone] || 'FRA';

    // If timezone is not standard, try to guess or use name
    if (nationEl) {
      nationEl.textContent = nationCode;
      nationEl.title = `Timezone: ${timezone}`;
    }

    const update = () => {
      try {
        const now = new Date();
        const timeStr = now.toLocaleTimeString(undefined, { 
          timeZone: timezone,
          hour12: false,
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        });
        const dateStr = now.toLocaleDateString(undefined, { 
          timeZone: timezone,
          day: '2-digit',
          month: 'short',
          year: 'numeric'
        });
        if (clockEl) {
          clockEl.textContent = `${dateStr} · ${timeStr}`;
        }
      } catch (e) {
        // Fallback if timezone string is invalid
        const now = new Date();
        const timeStr = now.toLocaleTimeString(undefined, { hour12: false });
        const dateStr = now.toLocaleDateString(undefined, { day: '2-digit', month: 'short', year: 'numeric' });
        if (clockEl) {
          clockEl.textContent = `${dateStr} · ${timeStr}`;
        }
      }
    };
    
    update();
    this._clockInterval = setInterval(update, 1000);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _renderApprovalsHub(container) {
    const leavesList = container.querySelector('#sa-pending-leaves-list');
    const awayList = container.querySelector('#sa-pending-away-list');

    // 1. Render Leaves
    if (leavesList) {
      leavesList.replaceChildren();
      if (this.pendingLeaves.length === 0) {
        leavesList.innerHTML = `
          <div class="empty-approvals-banner" style="text-align: center; padding: 24px; color: var(--text-muted); font-size: 0.8rem;">
            <i data-lucide="check-square" style="width: 24px; height: 24px; margin-bottom: 8px; color: var(--accent-primary); display: block; margin-left: auto; margin-right: auto;"></i>
            <div>All leaves processed</div>
            <div style="font-size: 0.7rem; color: var(--text-muted); margin-top: 2px;">No pending leave applications.</div>
          </div>
        `;
      } else {
        this.pendingLeaves.forEach(req => {
          const item = document.createElement('div');
          item.className = 'approval-item-card';
          item.style.cssText = 'background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 12px; display: flex; flex-direction: column; gap: 8px; margin-bottom: 8px;';
          
          const startDate = req.startDate ? new Date(req.startDate).toLocaleDateString() : '';
          const endDate = req.endDate ? new Date(req.endDate).toLocaleDateString() : '';
          
          item.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: flex-start;">
              <div>
                <div style="font-weight: 700; font-size: 0.82rem; color: var(--text-primary);">${req.employeeName || 'Employee'}</div>
                <div style="font-size: 0.7rem; color: var(--text-muted);">${req.leaveTypeName || 'Leave'} · ${req.durationDays || 0} days</div>
              </div>
              <span style="font-size: 0.65rem; background: rgba(201,164,106,0.12); color: var(--accent-primary); border: 1px solid rgba(201,164,106,0.2); padding: 2px 6px; border-radius: 4px; font-weight: 600;">PENDING</span>
            </div>
            <div style="font-size: 0.74rem; color: var(--text-secondary);">
              <strong>Period:</strong> ${startDate} - ${endDate} (${req.session || 'Full Day'})
            </div>
            <div style="font-size: 0.74rem; color: var(--text-secondary);">
              <strong>Reason:</strong> <em>${req.reason || 'No reason provided'}</em>
            </div>
            <div class="form-group" style="margin-top: 4px;">
              <input type="text" class="form-input sa-leave-comment-input" data-id="${req.id}" placeholder="Optional approval comment..." style="font-size: 0.7rem; padding: 4px 8px; background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); border-radius: 6px;">
            </div>
            <div style="display: flex; gap: 8px; margin-top: 4px;">
              <button type="button" class="btn btn-primary btn-sa-leave-approve" data-id="${req.id}" style="flex: 1; padding: 6px; font-size: 0.74rem;">✓ Approve</button>
              <button type="button" class="btn btn-danger btn-sa-leave-reject" data-id="${req.id}" style="flex: 1; padding: 6px; font-size: 0.74rem;">✕ Reject</button>
            </div>
          `;
          leavesList.appendChild(item);
        });
      }
    }

    // 2. Render Away Passes
    if (awayList) {
      awayList.replaceChildren();
      if (this.awayPermissions.length === 0) {
        awayList.innerHTML = `
          <div class="empty-approvals-banner" style="text-align: center; padding: 24px; color: var(--text-muted); font-size: 0.8rem;">
            <i data-lucide="map-pin" style="width: 24px; height: 24px; margin-bottom: 8px; color: var(--accent-warning, #f59e0b); display: block; margin-left: auto; margin-right: auto;"></i>
            <div>All employees on site</div>
            <div style="font-size: 0.7rem; color: var(--text-muted); margin-top: 2px;">No pending away pass requests.</div>
          </div>
        `;
      } else {
        this.awayPermissions.forEach(req => {
          const item = document.createElement('div');
          item.className = 'approval-item-card';
          item.style.cssText = 'background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 12px; display: flex; flex-direction: column; gap: 8px; margin-bottom: 8px;';
          
          const statusColor = req.status === 'EXTENSION_REQUESTED' ? '#f59e0b' : 'var(--accent-primary)';
          const tag = req.status === 'EXTENSION_REQUESTED' ? '🔁 Extension' : '⏸ Away Pass';
          const since = req.requestedAt ? new Date(req.requestedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--';

          item.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: flex-start;">
              <div>
                <div style="font-weight: 700; font-size: 0.82rem; color: var(--text-primary);">${req.employeeName || 'Employee'}</div>
                <div style="font-size: 0.7rem; color: var(--text-muted);">Requested at ${since}</div>
              </div>
              <span style="font-size: 0.65rem; background: rgba(245,158,11,0.12); color: ${statusColor}; border: 1px solid rgba(245,158,11,0.2); padding: 2px 6px; border-radius: 4px; font-weight: 600;">${tag}</span>
            </div>
            <div style="font-size: 0.74rem; color: var(--text-secondary); display: flex; flex-direction: column; gap: 2px;">
              ${req.reason ? `<div><strong>Original Reason:</strong> <em>${req.reason}</em></div>` : ''}
              ${req.status === 'EXTENSION_REQUESTED' && req.extensionReason ? `<div><strong>Extension Request:</strong> <em style="color: var(--accent-warning, #f59e0b);">${req.extensionReason}</em></div>` : ''}
            </div>
            <div style="display: flex; gap: 8px; margin-top: 4px;">
              <button type="button" class="btn btn-primary btn-sa-away-approve" data-id="${req.id}" style="flex: 1; padding: 6px; font-size: 0.74rem;">✓ Approve</button>
              <button type="button" class="btn btn-danger btn-sa-away-reject" data-id="${req.id}" style="flex: 1; padding: 6px; font-size: 0.74rem;">✕ Deny</button>
            </div>
          `;
          awayList.appendChild(item);
        });
      }
    }

    // 3. Render Ongoing Away Passes
    const ongoingList = container.querySelector('#sa-ongoing-away-list');
    if (ongoingList) {
      ongoingList.replaceChildren();
      if (this.ongoingAwayPermissions.length === 0) {
        ongoingList.innerHTML = `
          <div class="empty-approvals-banner" style="text-align: center; padding: 24px; color: var(--text-muted); font-size: 0.8rem;">
            <i data-lucide="door-open" style="width: 24px; height: 24px; margin-bottom: 8px; color: var(--status-success, #4caf50); display: block; margin-left: auto; margin-right: auto;"></i>
            <div>No ongoing away passes</div>
            <div style="font-size: 0.7rem; color: var(--text-muted); margin-top: 2px;">All staff currently inside geofence.</div>
          </div>
        `;
      } else {
        this.ongoingAwayPermissions.forEach(req => {
          const item = document.createElement('div');
          item.className = 'approval-item-card';
          item.style.cssText = 'background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 12px; display: flex; flex-direction: column; gap: 8px; margin-bottom: 8px;';
          
          const until = req.approvedUntil ? new Date(req.approvedUntil).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--:--';
          const duration = req.approvedDurationMins || 0;

          item.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: flex-start;">
              <div>
                <div style="font-weight: 700; font-size: 0.82rem; color: var(--text-primary);">${req.employeeName || 'Employee'}</div>
                <div style="font-size: 0.7rem; color: var(--text-muted);">Away for ${duration} mins</div>
              </div>
              <span style="font-size: 0.65rem; background: rgba(76,175,80,0.12); color: #4caf50; border: 1px solid rgba(76,175,80,0.2); padding: 2px 6px; border-radius: 4px; font-weight: 600;">ACTIVE</span>
            </div>
            <div style="font-size: 0.74rem; color: var(--text-secondary);">
              <strong>Approved Until:</strong> ${until}
            </div>
            <div style="font-size: 0.74rem; color: var(--text-secondary);">
              <strong>Reason:</strong> <em>${req.reason || 'No reason provided'}</em>
            </div>
          `;
          ongoingList.appendChild(item);
        });
      }
    }

    if (window.lucide) window.lucide.createIcons();
  }

  _bindApprovalsHubEvents(container, lifecycle) {
    // 1. Approve Leave
    const leaveApproveBtns = container.querySelectorAll('.btn-sa-leave-approve');
    leaveApproveBtns.forEach(btn => {
      const leaveId = btn.getAttribute('data-id');
      const commentInput = container.querySelector(`.sa-leave-comment-input[data-id="${leaveId}"]`);
      const handleApprove = async () => {
        const comment = commentInput?.value?.trim() || '';
        btn.disabled = true;
        try {
          const res = await apiClient.put(`/leaves/${leaveId}/approve`, { comment });
          if (res?.success) {
            notificationStore.success('Leave request approved successfully.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to approve leave request.');
            btn.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error approving leave: ' + err.message);
          btn.disabled = false;
        }
      };
      btn.addEventListener('click', handleApprove);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleApprove));
    });

    // 2. Reject Leave
    const leaveRejectBtns = container.querySelectorAll('.btn-sa-leave-reject');
    const modal = container.querySelector('#sa-reject-modal');
    const modalSubmit = container.querySelector('#sa-reject-modal-submit');
    const modalClose = container.querySelector('#sa-reject-modal-close');
    const modalCancel = container.querySelector('#sa-reject-modal-cancel');
    const rejectReasonInput = container.querySelector('#sa-reject-reason-input');

    leaveRejectBtns.forEach(btn => {
      const leaveId = btn.getAttribute('data-id');
      const handleOpenReject = () => {
        if (modalSubmit) modalSubmit.dataset.leaveId = leaveId;
        if (rejectReasonInput) rejectReasonInput.value = '';
        if (modal) {
          modal.style.display = 'flex';
          modal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenReject);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenReject));
    });

    const closeModal = () => {
      if (modal) {
        modal.style.display = 'none';
        modal.setAttribute('aria-hidden', 'true');
      }
    };

    if (modalClose) {
      modalClose.addEventListener('click', closeModal);
      lifecycle.onCleanup(() => modalClose.removeEventListener('click', closeModal));
    }
    if (modalCancel) {
      modalCancel.addEventListener('click', closeModal);
      lifecycle.onCleanup(() => modalCancel.removeEventListener('click', closeModal));
    }

    if (modalSubmit) {
      const handleConfirmReject = async () => {
        const leaveId = modalSubmit.dataset.leaveId;
        const reason = rejectReasonInput?.value?.trim() || '';
        if (!reason) {
          notificationStore.danger('Rejection reason is required.');
          return;
        }
        modalSubmit.disabled = true;
        try {
          const res = await apiClient.put(`/leaves/${leaveId}/reject`, { rejectionReason: reason });
          if (res?.success) {
            notificationStore.success('Leave request rejected.');
            closeModal();
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to reject leave request.');
            modalSubmit.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error rejecting leave: ' + err.message);
          modalSubmit.disabled = false;
        }
      };
      modalSubmit.addEventListener('click', handleConfirmReject);
      lifecycle.onCleanup(() => modalSubmit.removeEventListener('click', handleConfirmReject));
    }

    // 3. Approve Away Pass
    const awayApproveBtns = container.querySelectorAll('.btn-sa-away-approve');
    const awayOverlay = container.querySelector('#sa-away-approve-overlay');
    const awayConfirm = container.querySelector('#sa-away-confirm');
    const awayClose = container.querySelector('#sa-away-close');
    const awayCancel = container.querySelector('#sa-away-cancel');
    const awayDurationInput = container.querySelector('#sa-away-duration');
    const awayHint = container.querySelector('#sa-away-hint');

    awayApproveBtns.forEach(btn => {
      const awayId = btn.getAttribute('data-id');
      const handleOpenAwayApprove = () => {
        if (awayConfirm) awayConfirm.dataset.awayId = awayId;
        if (awayDurationInput) awayDurationInput.value = '30';
        if (awayHint) awayHint.textContent = 'Employee will have 30 min + 10 min grace = 40 min total.';
        if (awayOverlay) {
          awayOverlay.style.display = 'flex';
          awayOverlay.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenAwayApprove);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenAwayApprove));
    });

    const closeAwayOverlay = () => {
      if (awayOverlay) {
        awayOverlay.style.display = 'none';
        awayOverlay.setAttribute('aria-hidden', 'true');
      }
    };

    if (awayClose) {
      awayClose.addEventListener('click', closeAwayOverlay);
      lifecycle.onCleanup(() => awayClose.removeEventListener('click', closeAwayOverlay));
    }
    if (awayCancel) {
      awayCancel.addEventListener('click', closeAwayOverlay);
      lifecycle.onCleanup(() => awayCancel.removeEventListener('click', closeAwayOverlay));
    }

    if (awayDurationInput) {
      const handleDurationInput = () => {
        const val = parseInt(awayDurationInput.value || '30');
        if (awayHint) {
          awayHint.textContent = `Employee will have ${val} min + 10 min grace = ${val + 10} min total.`;
        }
      };
      awayDurationInput.addEventListener('input', handleDurationInput);
      lifecycle.onCleanup(() => awayDurationInput.removeEventListener('input', handleDurationInput));
    }

    if (awayConfirm) {
      const handleAwayConfirm = async () => {
        const awayId = awayConfirm.dataset.awayId;
        const duration = parseInt(awayDurationInput?.value || '30');
        if (!duration || duration < 1) {
          notificationStore.danger('Please enter a valid duration.');
          return;
        }
        awayConfirm.disabled = true;
        try {
          const res = await apiClient.put(`/api/v1/away-permission/${awayId}/approve`, { durationMins: duration });
          if (res?.success) {
            notificationStore.success(`Away pass approved for ${duration} minutes.`);
            closeAwayOverlay();
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to approve away pass.');
            awayConfirm.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error approving away pass: ' + err.message);
          awayConfirm.disabled = false;
        }
      };
      awayConfirm.addEventListener('click', handleAwayConfirm);
      lifecycle.onCleanup(() => awayConfirm.removeEventListener('click', handleAwayConfirm));
    }

    // 4. Deny Away Pass
    const awayRejectBtns = container.querySelectorAll('.btn-sa-away-reject');
    awayRejectBtns.forEach(btn => {
      const awayId = btn.getAttribute('data-id');
      const handleAwayReject = async () => {
        btn.disabled = true;
        try {
          const res = await apiClient.put(`/api/v1/away-permission/${awayId}/deny`, {});
          if (res?.success) {
            notificationStore.success('Away pass request denied.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to deny away pass.');
            btn.disabled = false;
          }
        } catch (err) {
          notificationStore.danger('Error denying away pass: ' + err.message);
          btn.disabled = false;
        }
      };
      btn.addEventListener('click', handleAwayReject);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleAwayReject));
    });
  }

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
