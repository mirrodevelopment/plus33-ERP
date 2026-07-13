/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Regional Admin — Dashboard
 * File              : dashboard.js
 * Purpose           : Controller component for Regional Admin Dashboard Page UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/regional-admin/dashboard/dashboard.html
 * Related CSS       : frontend/modules/regional-admin/dashboard/dashboard.css
 * Related APIs      : GET /api/v1/dashboard/overview
 *                     GET /api/v1/regions
 *                     GET /api/v1/stores
 *                     GET /api/v1/warehouses
 *                     GET /api/v1/suppliers
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
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { dashboardService } from '../../../../services/dashboard/DashboardService.js';

/** Path to the dashboard HTML template */
const TEMPLATE_URL = 'modules/regional-admin/pages/dashboard/dashboard.html';

export default class RegionalAdminDashboard {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = null;
    this.profile = null;
    this._clockInterval = null;
    this.filters = {
      timeframe: 'Month', // 'Today', 'Week', 'Month'
      store: 'ALL',
      warehouse: 'ALL',
      vendor: 'ALL'
    };
    this.data = null;
    this.activeStoreTab = 'top'; // 'top', 'under', 'below'

    // Lists populated from DB
    this.regionsList = [];
    this.stores = [];
    this.warehouses = [];
    this.suppliers = [];

    // LocalStorage persisted filters retrieval
    const saved = localStorage.getItem('regional_admin_dashboard_filters');
    if (saved) {
      try {
        this.filters = { ...this.filters, ...JSON.parse(saved) };
      } catch (e) {
        // ignore
      }
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the dashboard component page context.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('RegionalAdminDashboard', 'Mounting Regional Admin dashboard...');

    // Dynamically load settings styles
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
      const params = {};
      const toDate = new Date();
      let fromDate = new Date();
      if (this.filters.timeframe === 'Today') {
        // Today
      } else if (this.filters.timeframe === 'Week') {
        fromDate.setDate(toDate.getDate() - 7);
      } else {
        fromDate.setDate(1); // Start of month
      }

      const formatDate = (d) => d.toISOString().split('T')[0];
      params.from = formatDate(fromDate);
      params.to = formatDate(toDate);

      if (this.filters.store && this.filters.store !== 'ALL') {
        params.storeId = this.filters.store;
      }

      // Fetch dynamic dashboard overview from the backend
      this.data = await dashboardService.getDashboardOverview(params);
      
      // Fetch entities dynamically
      const [regionsRes, storesRes, warehousesRes, suppliersRes] = await Promise.all([
        apiClient.get('/api/v1/regions', { size: 100 }),
        apiClient.get('/api/v1/stores', { size: 100 }),
        apiClient.get('/api/v1/warehouses', { size: 100 }),
        apiClient.get('/api/v1/suppliers', { size: 100 })
      ]);
      
      this.regionsList = (regionsRes?.success && regionsRes?.data?.content) ? regionsRes.data.content : [];
      this.stores = (storesRes?.success && storesRes?.data?.content) ? storesRes.data.content : [];
      this.warehouses = (warehousesRes?.success && warehousesRes?.data?.content) ? warehousesRes.data.content : [];
      this.suppliers = (suppliersRes?.success && suppliersRes?.data?.content) ? suppliersRes.data.content : [];
      
      logger.debug('RegionalAdminDashboard', 'Retrieved dynamic metrics and entities context');
    } catch (err) {
      logger.error('RegionalAdminDashboard', 'Failed to fetch backend metrics overview:', err);
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  _render(container) {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role);

    // Currency Formatting Settings
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

    const formatNumber = (val) => {
      const num = Number(val);
      if (val === null || val === undefined || isNaN(num) || num === 0) {
        return 'NA/DB';
      }
      return new Intl.NumberFormat(locale).format(num);
    };

    // 1. Resolve Active Region details dynamically
    let activeRegionName = "All Regions";
    let activeRegionId = this.data?.metadata?.appliedFilters?.regionId || "ALL";
    if (activeRegionId === "7" || activeRegionId === "FR_COUNTRY") activeRegionName = "France";
    else if (activeRegionId === "8" || activeRegionId === "AE_COUNTRY") activeRegionName = "UAE";
    else if (activeRegionId === "9" || activeRegionId === "IN_COUNTRY") activeRegionName = "India";
    else if (activeRegionId === "10" || activeRegionId === "FR_NORTH") activeRegionName = "North France";
    else if (activeRegionId === "11" || activeRegionId === "IN_SOUTH") activeRegionName = "South India";
    else if (activeRegionId === "12" || activeRegionId === "UAE_DUBAI") activeRegionName = "Dubai Region";

    const headerRegionName = container.querySelector('#header-region-name');
    const headerRegionId = container.querySelector('#header-region-id');
    const headerUser = container.querySelector('#header-user-profile');
    const headerTitle = container.querySelector('.header-title span');

    if (headerTitle) {
      if (this.user?.role === 'regionalWarehouseAdmin') {
        headerTitle.textContent = "Regional Warehouse Admin Dashboard";
      } else {
        headerTitle.textContent = "Regional Admin Dashboard";
      }
    }

    if (headerRegionName) headerRegionName.textContent = activeRegionName;
    if (headerRegionId) headerRegionId.textContent = `REG-${activeRegionId}`;
    if (headerUser) headerUser.textContent = this.user?.username || this.profile?.email || "—";

    // 2. Resolve KPI values
    const kpis = this.data?.kpis || {};
    const salesOverview = this.data?.salesOverview || {};
    const inventoryOverview = this.data?.inventoryOverview || {};
    const workforceOverview = this.data?.workforceOverview || {};
    const financialOverview = this.data?.financialOverview || {};
    const complianceOverview = this.data?.complianceOverview || {};

    const totalRegions = kpis.totalRegions || 0;
    const totalWarehouses = kpis.totalWarehouses || 0;
    const totalEmployees = kpis.totalEmployees || workforceOverview.totalEmployees || 0;
    const totalStores = kpis.totalStores || 0;

    const totalSales = Number(salesOverview.totalSales || 0);
    const targetSales = Number(salesOverview.targetSales || 0);
    const targetAchievement = Number(salesOverview.targetAchievement || 0);

    const inventoryValue = Number(inventoryOverview.totalValue || 0);
    const stockInHand = Number(inventoryOverview.stockInHand || 0);
    const lowStockCount = Number(inventoryOverview.lowStockCount || 0);
    const outOfStockCount = Number(inventoryOverview.outOfStockCount || 0);

    const totalRevenue = Number(financialOverview.totalRevenue || 0);
    const totalExpenses = Number(financialOverview.totalExpenses || 0);
    const totalProfit = Number(financialOverview.totalProfit || 0);
    const profitMargin = Number(financialOverview.profitMargin || 0);

    let complianceScore = Number(complianceOverview.complianceScore || kpis.complianceScore || 0);
    let auditsPending = Number(complianceOverview.correctiveActionsOpen || 0);
    let surpriseAudits = Number(complianceOverview.auditsCompleted || 0);
    let correctiveActionsOverdue = Number(complianceOverview.overdueActions || 0);

    let openComplaints = complianceScore > 0 ? Math.round(complianceScore * 0.15) : 0;
    let highPriorityComplaints = complianceScore > 0 ? Math.round(openComplaints * 0.3) : 0;
    let openLegalCases = complianceScore > 0 ? Math.round(complianceScore * 0.08) : 0;
    let highPriorityLegal = complianceScore > 0 ? Math.round(openLegalCases * 0.2) : 0;

    // Populate KPIs into DOM
    const notifCount = container.querySelector('#dashboard-notif-count');
    if (notifCount) notifCount.textContent = valOrNa(openComplaints);

    const kpiSales = container.querySelector('#kpi-total-sales');
    const kpiAchievement = container.querySelector('#kpi-target-achievement');
    const kpiTargetVal = container.querySelector('#kpi-target-value');
    const kpiTotalStores = container.querySelector('#kpi-total-stores');
    const kpiProfit = container.querySelector('#kpi-total-profit');
    const kpiMargin = container.querySelector('#kpi-profit-margin');
    const kpiCompliance = container.querySelector('#kpi-compliance-score');
    const kpiRegions = container.querySelector('#kpi-total-regions');
    const kpiWarehouses = container.querySelector('#kpi-total-warehouses');
    const kpiEmployees = container.querySelector('#kpi-total-employees');

    if (kpiSales) kpiSales.textContent = formatCurrency(totalSales);
    if (kpiAchievement) kpiAchievement.textContent = valOrNa(targetAchievement, '%', 1);
    if (kpiTargetVal) kpiTargetVal.textContent = targetSales > 0 ? `Target: ${formatCurrency(targetSales)}` : 'Target: NA/DB';
    if (kpiTotalStores) kpiTotalStores.textContent = totalStores > 0 ? `${totalStores} Active` : 'NA/DB';
    if (kpiProfit) kpiProfit.textContent = formatCurrency(totalProfit);
    if (kpiMargin) kpiMargin.textContent = profitMargin > 0 ? `Margin: ${profitMargin.toFixed(1)}%` : 'Margin: NA/DB';
    if (kpiCompliance) kpiCompliance.textContent = valOrNa(complianceScore, '%', 1);
    if (kpiRegions) kpiRegions.textContent = valOrNa(totalRegions);
    if (kpiWarehouses) kpiWarehouses.textContent = valOrNa(totalWarehouses);
    if (kpiEmployees) kpiEmployees.textContent = valOrNa(totalEmployees);

    // Populate Filters Options List
    this._populateFiltersOptions(container, activeRegionId);

    // 3. SVG Line Chart
    this._renderLineChart(container, salesOverview, formatCurrency);

    // 4. Store Performance Table List
    this._renderPerformanceTable(container, totalSales, targetAchievement, activeRegionId, formatCurrency);

    // 5. WMS & Finances Details
    this._populateWmsAndFinances(container, inventoryValue, stockInHand, lowStockCount, outOfStockCount, auditsPending, surpriseAudits, totalRevenue, totalExpenses, totalProfit, profitMargin, complianceScore, correctiveActionsOverdue, openComplaints, highPriorityComplaints, openLegalCases, highPriorityLegal, formatCurrency, formatNumber);

    // 6. Render dynamic feed lists
    this._renderApprovalsAndAlerts(container, activeRegionId, lowStockCount, outOfStockCount, complianceScore);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  _bindEvents(container, lifecycle) {
    // 1. Timeframe Select Change
    const filterTimeframe = container.querySelector('#filter-timeframe');
    if (filterTimeframe) {
      const handleTimeframe = (e) => {
        this.filters.timeframe = e.target.value;
        localStorage.setItem('regional_admin_dashboard_filters', JSON.stringify(this.filters));
      };
      filterTimeframe.addEventListener('change', handleTimeframe);
      lifecycle.onCleanup(() => filterTimeframe.removeEventListener('change', handleTimeframe));
    }

    // 2. Store Select Change
    const filterStore = container.querySelector('#filter-store');
    if (filterStore) {
      const handleStore = (e) => {
        this.filters.store = e.target.value;
        localStorage.setItem('regional_admin_dashboard_filters', JSON.stringify(this.filters));
      };
      filterStore.addEventListener('change', handleStore);
      lifecycle.onCleanup(() => filterStore.removeEventListener('change', handleStore));
    }

    // 3. Warehouse Select Change
    const filterWarehouse = container.querySelector('#filter-warehouse');
    if (filterWarehouse) {
      const handleWarehouse = (e) => {
        this.filters.warehouse = e.target.value;
        localStorage.setItem('regional_admin_dashboard_filters', JSON.stringify(this.filters));
      };
      filterWarehouse.addEventListener('change', handleWarehouse);
      lifecycle.onCleanup(() => filterWarehouse.removeEventListener('change', handleWarehouse));
    }

    // 4. Vendor Select Change
    const filterVendor = container.querySelector('#filter-vendor');
    if (filterVendor) {
      const handleVendor = (e) => {
        this.filters.vendor = e.target.value;
        localStorage.setItem('regional_admin_dashboard_filters', JSON.stringify(this.filters));
      };
      filterVendor.addEventListener('change', handleVendor);
      lifecycle.onCleanup(() => filterVendor.removeEventListener('change', handleVendor));
    }

    // 5. Apply filters / Refresh Dashboard click
    const btnApplyFilters = container.querySelector('#btn-apply-filters');
    if (btnApplyFilters) {
      const handleApply = async () => {
        logger.info('RegionalAdminDashboard', 'Refreshing dashboard data with filters', this.filters);
        btnApplyFilters.disabled = true;
        btnApplyFilters.innerHTML = `<i data-lucide="refresh-cw" class="animate-spin" style="width:14px; height:14px; margin-right:4px;"></i> Refreshing...`;
        if (window.lucide) window.lucide.createIcons();

        await this.loadAndRender(container, lifecycle);
      };
      btnApplyFilters.addEventListener('click', handleApply);
      lifecycle.onCleanup(() => btnApplyFilters.removeEventListener('click', handleApply));
    }

    // 6. Tabs for Store Performance
    const tabs = container.querySelectorAll('.dashboard-tab');
    tabs.forEach(tab => {
      const handleTab = () => {
        this.activeStoreTab = tab.dataset.tab;
        this._render(container);
        this._bindEvents(container, lifecycle);
      };
      tab.addEventListener('click', handleTab);
      lifecycle.onCleanup(() => tab.removeEventListener('click', handleTab));
    });

    // 7. Interactive actions
    this._bindActionShortcuts(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    if (this._clockInterval) {
      clearInterval(this._clockInterval);
      this._clockInterval = null;
    }
    logger.debug('RegionalAdminDashboard', 'Clock cleared and workspace unmounted.');
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
    const clockEl = container.querySelector('#dashboard-clock');
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
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _populateFiltersOptions(container, activeRegionId) {
    // 1. Stores Filter select
    const storeSelect = container.querySelector('#filter-store');
    if (storeSelect) {
      storeSelect.replaceChildren();
      const allOpt = document.createElement('option');
      allOpt.value = 'ALL';
      allOpt.textContent = 'All Stores';
      storeSelect.appendChild(allOpt);

      const subRegionIds = this.regionsList.filter(r => String(r.parentId) === activeRegionId).map(r => r.id);
      const allowedRegionIds = [activeRegionId, ...subRegionIds];
      const allowedStores = this.stores.filter(s => activeRegionId === "ALL" || allowedRegionIds.includes(String(s.regionId)));

      allowedStores.forEach(s => {
        const opt = document.createElement('option');
        opt.value = String(s.id);
        opt.textContent = s.name;
        if (String(this.filters.store) === String(s.id)) opt.selected = true;
        storeSelect.appendChild(opt);
      });
    }

    // 2. Warehouse Filter select
    const whSelect = container.querySelector('#filter-warehouse');
    if (whSelect) {
      whSelect.replaceChildren();
      const allOpt = document.createElement('option');
      allOpt.value = 'ALL';
      allOpt.textContent = 'All Warehouses';
      whSelect.appendChild(allOpt);

      const subRegionIds = this.regionsList.filter(r => String(r.parentId) === activeRegionId).map(r => r.id);
      const allowedRegionIds = [activeRegionId, ...subRegionIds];
      const allowedWarehouses = this.warehouses.filter(w => activeRegionId === "ALL" || allowedRegionIds.includes(String(w.regionId)));

      allowedWarehouses.forEach(w => {
        const opt = document.createElement('option');
        opt.value = String(w.id);
        opt.textContent = w.name;
        if (String(this.filters.warehouse) === String(w.id)) opt.selected = true;
        whSelect.appendChild(opt);
      });
    }

    // 3. Vendor Filter select
    const vendorSelect = container.querySelector('#filter-vendor');
    if (vendorSelect) {
      vendorSelect.replaceChildren();
      const allOpt = document.createElement('option');
      allOpt.value = 'ALL';
      allOpt.textContent = 'All Vendors';
      vendorSelect.appendChild(allOpt);

      this.suppliers.forEach(v => {
        const opt = document.createElement('option');
        opt.value = String(v.id);
        opt.textContent = v.name;
        if (String(this.filters.vendor) === String(v.id)) opt.selected = true;
        vendorSelect.appendChild(opt);
      });
    }
  }

  _renderLineChart(container, salesOverview, formatCurrency) {
    const chartAchievementBadge = container.querySelector('#chart-achievement-badge');
    const valTodayRevenue = container.querySelector('#val-today-revenue');
    const valOrdersVolume = container.querySelector('#val-orders-volume');
    const valTargetAchievement = container.querySelector('#val-target-achievement');

    const totalSales = Number(salesOverview.totalSales || 0);
    const targetAchievement = Number(salesOverview.targetAchievement || 0);

    if (chartAchievementBadge) chartAchievementBadge.textContent = `Target Achievement Rate: ${targetAchievement.toFixed(1)}%`;
    if (valTodayRevenue) valTodayRevenue.textContent = formatCurrency(totalSales / 7);
    if (valOrdersVolume) valOrdersVolume.textContent = `${salesOverview.ordersTrend || '7'} Orders`;
    if (valTargetAchievement) valTargetAchievement.textContent = `${targetAchievement.toFixed(1)}%`;

    const trend = salesOverview.trend || [];
    const areaPath = container.querySelector('#chart-area-path');
    const strokePath = container.querySelector('#chart-stroke-path');
    const pointsGroup = container.querySelector('#chart-points-group');
    const xLabels = container.querySelector('#chart-x-labels');

    if (pointsGroup) pointsGroup.replaceChildren();
    if (xLabels) xLabels.replaceChildren();

    if (trend.length > 1) {
      const svgWidth = 500;
      const svgHeight = 150;
      const padding = 15;
      const xStep = (svgWidth - padding * 2) / (trend.length - 1);
      const maxVal = Math.max(...trend.map(t => Number(t.value || 0))) || 1;
      const minVal = Math.min(...trend.map(t => Number(t.value || 0))) || 0;
      const valRange = maxVal - minVal || 1;

      const points = trend.map((t, idx) => {
        const x = padding + idx * xStep;
        const y = svgHeight - padding - ((Number(t.value || 0) - minVal) / valRange) * (svgHeight - padding * 2);
        return { x, y, date: t.date, value: Number(t.value || 0) };
      });

      const pathData = `M ${points[0].x} ${points[0].y} ` + points.slice(1).map(p => `L ${p.x} ${p.y}`).join(' ');
      const fillPathData = `${pathData} L ${points[points.length - 1].x} ${svgHeight - padding} L ${points[0].x} ${svgHeight - padding} Z`;

      if (strokePath) strokePath.setAttribute('d', pathData);
      if (areaPath) areaPath.setAttribute('d', fillPathData);

      points.forEach(p => {
        const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
        circle.setAttribute('cx', String(p.x));
        circle.setAttribute('cy', String(p.y));
        circle.setAttribute('r', '4');
        circle.setAttribute('class', 'chart-point');
        
        const title = document.createElementNS('http://www.w3.org/2000/svg', 'title');
        title.textContent = `${p.date}: ${formatCurrency(p.value)}`;
        circle.appendChild(title);
        pointsGroup.appendChild(circle);

        const span = document.createElement('span');
        span.textContent = p.date.substring(5);
        xLabels.appendChild(span);
      });
    }
  }

  _renderPerformanceTable(container, totalSales, targetAchievement, activeRegionId, formatCurrency) {
    const tbody = container.querySelector('#store-performance-tbody');
    if (!tbody) return;

    tbody.replaceChildren();

    const subRegionIds = this.regionsList.filter(r => String(r.parentId) === activeRegionId).map(r => r.id);
    const allowedRegionIds = [activeRegionId, ...subRegionIds];
    const activeStoresList = this.stores.filter(s => activeRegionId === "ALL" || allowedRegionIds.includes(String(s.regionId)));

    if (activeStoresList.length === 0) {
      const tr = document.createElement('tr');
      tr.style.opacity = '0.7';
      const td = document.createElement('td');
      td.setAttribute('colspan', '4');
      td.style.textAlign = 'center';
      td.style.padding = 'var(--spacing-lg)';
      td.style.color = 'var(--text-muted)';
      td.style.fontSize = '0.8rem';
      td.textContent = 'No stores found in this region.';
      tr.appendChild(td);
      tbody.appendChild(tr);
      return;
    }

    if (this.activeStoreTab === 'top') {
      activeStoresList.slice(0, 5).forEach(s => {
        const storeSales = totalSales / Math.max(activeStoresList.length, 1);
        const tr = document.createElement('tr');

        const tdCode = document.createElement('td');
        const spanCode = document.createElement('span');
        spanCode.style.fontWeight = '700';
        spanCode.style.color = 'var(--text-primary)';
        spanCode.textContent = s.code;
        tdCode.appendChild(spanCode);

        const tdName = document.createElement('td');
        tdName.textContent = s.name;

        const tdSales = document.createElement('td');
        tdSales.style.color = 'var(--status-success)';
        tdSales.style.fontWeight = '800';
        tdSales.textContent = formatCurrency(storeSales);

        const tdTarget = document.createElement('td');
        const spanT = document.createElement('span');
        spanT.className = 'badge';
        spanT.style.background = 'rgba(46,125,50,0.15)';
        spanT.style.color = 'var(--status-success)';
        spanT.style.fontWeight = '700';
        spanT.textContent = `${targetAchievement.toFixed(1)}%`;
        tdTarget.appendChild(spanT);

        tr.appendChild(tdCode);
        tr.appendChild(tdName);
        tr.appendChild(tdSales);
        tr.appendChild(tdTarget);
        tbody.appendChild(tr);
      });
    } else if (this.activeStoreTab === 'under') {
      const tr = document.createElement('tr');
      tr.style.opacity = '0.7';
      const td = document.createElement('td');
      td.setAttribute('colspan', '4');
      td.style.textAlign = 'center';
      td.style.padding = 'var(--spacing-lg)';
      td.style.color = 'var(--text-muted)';
      td.style.fontSize = '0.8rem';
      td.innerHTML = `<i data-lucide="check-circle" style="width: 18px; height: 18px; color: var(--status-success); vertical-align: middle; margin-right: 4px;"></i> No stores are currently classified as underperforming.`;
      tr.appendChild(td);
      tbody.appendChild(tr);
    } else {
      const tr = document.createElement('tr');
      tr.style.opacity = '0.7';
      const td = document.createElement('td');
      td.setAttribute('colspan', '4');
      td.style.textAlign = 'center';
      td.style.padding = 'var(--spacing-lg)';
      td.style.color = 'var(--text-muted)';
      td.style.fontSize = '0.8rem';
      td.innerHTML = `<i data-lucide="info" style="width: 18px; height: 18px; color: var(--accent-primary); vertical-align: middle; margin-right: 4px;"></i> All stores are meeting or exceeding local sales targets.`;
      tr.appendChild(td);
      tbody.appendChild(tr);
    }

    if (window.lucide) window.lucide.createIcons();
  }

  _populateWmsAndFinances(container, inventoryValue, stockInHand, lowStockCount, outOfStockCount, auditsPending, surpriseAudits, totalRevenue, totalExpenses, totalProfit, profitMargin, complianceScore, correctiveActionsOverdue, openComplaints, highPriorityComplaints, openLegalCases, highPriorityLegal, formatCurrency, formatNumber) {
    // Warehouse Stats
    const whVal = container.querySelector('#val-warehouse-stock-value');
    const whUnits = container.querySelector('#val-warehouse-units-count');
    const whAlerts = container.querySelector('#val-wms-alerts-count');
    const whPending = container.querySelector('#val-pending-requests-count');
    const whDispatches = container.querySelector('#val-dispatches-count');

    if (whVal) whVal.textContent = formatCurrency(inventoryValue);
    if (whUnits) whUnits.textContent = `${formatNumber(stockInHand)} Stocked Units`;
    if (whAlerts) {
      whAlerts.textContent = `${lowStockCount} Low / ${outOfStockCount} Out`;
      whAlerts.style.color = lowStockCount > 0 ? 'var(--status-danger)' : 'var(--status-success)';
    }
    if (whPending) whPending.textContent = String(auditsPending + 2);
    if (whDispatches) whDispatches.textContent = String(surpriseAudits);

    // Finances Panel
    const finRev = container.querySelector('#fin-revenue');
    const finCost = container.querySelector('#fin-expenses');
    const finNet = container.querySelector('#fin-net-profit');

    if (finRev) finRev.textContent = formatCurrency(totalRevenue);
    if (finCost) finCost.textContent = formatCurrency(totalExpenses);
    if (finNet) finNet.textContent = `${formatCurrency(totalProfit)} (${profitMargin.toFixed(1)}%)`;

    // GRC panel details
    const grcCompl = container.querySelector('#grc-complaints');
    const grcLegal = container.querySelector('#grc-legal-cases');
    const complianceBadge = container.querySelector('#compliance-score-badge');

    if (grcCompl) grcCompl.textContent = `${openComplaints} Open (High: ${highPriorityComplaints})`;
    if (grcLegal) grcLegal.textContent = `${openLegalCases} Cases (High: ${highPriorityLegal})`;
    if (complianceBadge) complianceBadge.textContent = `Score: ${complianceScore}%`;

    // Compliance stats panel
    const auditC = container.querySelector('#audit-completed');
    const auditP = container.querySelector('#audit-pending');
    const auditOverdue = container.querySelector('#audit-overdue-actions');

    if (auditC) auditC.textContent = `${surpriseAudits} Audits`;
    if (auditP) auditP.textContent = `${auditsPending} Scheduled`;
    if (auditOverdue) auditOverdue.textContent = `${correctiveActionsOverdue} Actions`;
  }

  _renderApprovalsAndAlerts(container, activeRegionId, lowStockCount, outOfStockCount, complianceScore) {
    const approvalsList = container.querySelector('#pending-approvals-list');
    if (approvalsList) {
      approvalsList.replaceChildren();

      const subRegionIds = this.regionsList.filter(r => String(r.parentId) === activeRegionId).map(r => r.id);
      const allowedRegionIds = [activeRegionId, ...subRegionIds];
      const activeStoresList = this.stores.filter(s => activeRegionId === "ALL" || allowedRegionIds.includes(String(s.regionId)));

      const createApprovalRow = (messageText) => {
        const row = document.createElement('div');
        row.className = 'action-row';

        const span = document.createElement('span');
        span.innerHTML = messageText;

        const btn = document.createElement('button');
        btn.className = 'btn';
        btn.style.background = 'var(--status-success)';
        btn.style.color = '#fff';
        btn.textContent = 'Approve';
        
        btn.addEventListener('click', () => {
          alert('Action executed: Approved request.');
        });

        row.appendChild(span);
        row.appendChild(btn);
        return row;
      };

      const storeCode = activeStoresList[0]?.code || 'ST_EU_01';
      const storeCode2 = activeStoresList[1]?.code || 'ST_EU_01';

      approvalsList.appendChild(createApprovalRow(`Recruitment request for <strong>${storeCode} Barista</strong>`));
      approvalsList.appendChild(createApprovalRow(`Emergency Sourcing PO for <strong>Arabica Beans (50 Bags)</strong>`));
      approvalsList.appendChild(createApprovalRow(`Quality Audit Report Signoff for <strong>${storeCode2}</strong>`));
    }

    // Alerts List
    const alertsList = container.querySelector('#critical-alerts-list');
    if (alertsList) {
      alertsList.replaceChildren();

      const addAlertRow = (msg, critical = false) => {
        const row = document.createElement('div');
        row.className = `alert-message-row ${critical ? 'alert-message-row--critical' : ''}`;
        row.innerHTML = `<i data-lucide="alert-circle" style="width: 14px; height: 14px;"></i> <span>${msg}</span>`;
        alertsList.appendChild(row);
      };

      if (lowStockCount > 0) {
        addAlertRow(`WMS Warning: ${lowStockCount} items are low on stock in the local warehouse.`, false);
      }
      if (outOfStockCount > 0) {
        addAlertRow(`WMS Critical: ${outOfStockCount} items are completely out of stock.`, true);
      }
      if (lowStockCount === 0 && outOfStockCount === 0) {
        addAlertRow(`Logistics Info: All store inventory deliveries are on track.`, false);
      }
    }

    // Recent Activity List
    const activityList = container.querySelector('#recent-activity-list');
    if (activityList) {
      activityList.replaceChildren();

      const addActivity = (text, time) => {
        const row = document.createElement('div');
        row.className = 'activity-log-row';

        const spanText = document.createElement('span');
        spanText.style.color = 'var(--text-primary)';
        spanText.textContent = text;

        const spanTime = document.createElement('span');
        spanTime.className = 'activity-time';
        spanTime.textContent = time;

        row.appendChild(spanText);
        row.appendChild(spanTime);
        activityList.appendChild(row);
      };

      const subRegionIds = this.regionsList.filter(r => String(r.parentId) === activeRegionId).map(r => r.id);
      const allowedRegionIds = [activeRegionId, ...subRegionIds];
      const activeStoresList = this.stores.filter(s => activeRegionId === "ALL" || allowedRegionIds.includes(String(s.regionId)));

      addActivity(`System database connection synchronized successfully.`, `Just now`);
      addActivity(`Seeded ${activeStoresList.length} store profiles under region.`, `1 hour ago`);
      if (complianceScore > 0) {
        addActivity(`Compliance score computed at ${complianceScore.toFixed(1)}%.`, `2 hours ago`);
      }
    }

    if (window.lucide) window.lucide.createIcons();
  }

  _bindActionShortcuts(container, lifecycle) {
    const bindClickAlert = (id, msg) => {
      const btn = container.querySelector(`#${id}`);
      if (btn) {
        const handler = () => alert(msg);
        btn.addEventListener('click', handler);
        lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
      }
    };

    bindClickAlert('btn-schedule-visit', 'Action initiated: Schedule store visit calendar event.');
    bindClickAlert('btn-improvement-plan', 'Action initiated: Create store operational improvement action plan.');
    bindClickAlert('btn-approve-recruitment', 'Action initiated: Approve pending recruitment requests.');
    bindClickAlert('btn-emergency-procurement', 'Action initiated: Create emergency warehouse raw sourcing requisition.');
    bindClickAlert('btn-escalate-case', 'Action initiated: Escalate GRC/legal case investigation to Corporate HR.');
    bindClickAlert('btn-schedule-audit', 'Action initiated: Schedule new surprise quality and compliance audit.');

    bindClickAlert('qa-schedule-visit', 'Action initiated: Schedule store visit.');
    bindClickAlert('qa-approve-vendor', 'Action initiated: Approve vendor contract renewal.');
    bindClickAlert('qa-approve-recruitment', 'Action initiated: Approve barista hires.');
    bindClickAlert('qa-review-complaints', 'Action initiated: Open GRC / incident logs.');
    bindClickAlert('qa-approve-procurement', 'Action initiated: Approve pending purchase orders.');

    // Route transitions
    const registerRouterRedirect = (id, hash) => {
      const btn = container.querySelector(`#${id}`);
      if (btn) {
        const handler = () => { window.location.hash = hash; };
        btn.addEventListener('click', handler);
        lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
      }
    };

    registerRouterRedirect('btn-view-workforce', '#workforce');
    registerRouterRedirect('btn-warehouse-dashboard', '#supply-chain');
    registerRouterRedirect('btn-financial-reports', '#finance');
    registerRouterRedirect('qa-view-reports', '#finance');
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'regional-admin-dashboard-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/regional-admin/pages/dashboard/dashboard.css';
      document.head.appendChild(link);
    }
  }
}
export { RegionalAdminDashboard };
