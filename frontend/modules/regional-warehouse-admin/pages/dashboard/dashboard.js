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
const TEMPLATE_URL = 'modules/regional-warehouse-admin/pages/dashboard/dashboard.html';

export default class RegionalWarehouseAdminDashboard {

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

    const formatCurrency = (val) => {
      const num = Number(val);
      if (val === null || val === undefined || isNaN(num) || num === 0) {
        return '₹ 84,75,250'; // Default mockup value
      }
      return new Intl.NumberFormat(locale, { style: 'currency', currency: systemCurrency, maximumFractionDigits: 0 }).format(num);
    };

    // 1. Resolve Active Region details dynamically
    let activeRegionName = "All Regions";
    let activeRegionId = this.data?.metadata?.appliedFilters?.regionId || "ALL";
    if (activeRegionId === "7" || activeRegionId === "FR_COUNTRY" || activeRegionId === "FR_NORTH") activeRegionName = "France Region";
    else if (activeRegionId === "8" || activeRegionId === "AE_COUNTRY" || activeRegionId === "UAE_DUBAI") activeRegionName = "UAE Region";
    else if (activeRegionId === "9" || activeRegionId === "IN_COUNTRY" || activeRegionId === "IN_SOUTH") activeRegionName = "India Region";
    else if (activeRegionId === "10") activeRegionName = "North France Region";
    else if (activeRegionId === "11") activeRegionName = "South India Region";
    else if (activeRegionId === "12") activeRegionName = "Dubai Region";
    else if (activeRegionId === "19") activeRegionName = "West India Region";
    else if (activeRegionId === "20") activeRegionName = "North India Region";
    else if (activeRegionId === "21") activeRegionName = "East India Region";

    const headerRegionName = container.querySelector('#header-region-name');
    const headerRegionId = container.querySelector('#header-region-id');
    const headerUser = container.querySelector('#header-user-profile');
    const headerWarehouseSelect = container.querySelector('#header-warehouse-select');
    const widgetProfileName = container.querySelector('.user-profile-widget .profile-name');
    const widgetProfileRole = container.querySelector('.user-profile-widget .profile-role');

    if (headerRegionName) headerRegionName.textContent = activeRegionName;
    if (headerRegionId) headerRegionId.textContent = `REG-${activeRegionId}`;
    if (headerUser) headerUser.textContent = this.user?.username || this.profile?.email || "—";
    if (widgetProfileName) widgetProfileName.textContent = this.profile?.name || "Rohit Sharma";
    if (widgetProfileRole) widgetProfileRole.textContent = this.profile?.designation || "Warehouse Admin";

    // Populate dynamic warehouse options
    if (headerWarehouseSelect) {
      headerWarehouseSelect.replaceChildren();
      if (this.warehouses && this.warehouses.length > 0) {
        this.warehouses.forEach(wh => {
          const opt = document.createElement('option');
          opt.value = wh.code;
          opt.textContent = wh.name;
          headerWarehouseSelect.appendChild(opt);
        });
      } else {
        const opt = document.createElement('option');
        opt.value = "WH-EU-01";
        opt.textContent = "North Region Warehouse";
        headerWarehouseSelect.appendChild(opt);
      }
    }

    // 2. Resolve KPI values and populate
    const kpis = this.data?.kpis || {};
    const inventoryOverview = this.data?.inventoryOverview || {};

    const inventoryValue = Number(inventoryOverview.totalValue || 0);
    const stockInHand = Number(inventoryOverview.stockInHand || 0);
    const lowStockCount = Number(inventoryOverview.lowStockCount || 0);
    const inboundToday = Number(inventoryOverview.inboundDeliveriesToday || 0);
    const outboundToday = Number(inventoryOverview.outboundDeliveriesToday || 0);
    const pendingRequests = Number(inventoryOverview.pendingRequests || 0);
    const pendingDispatches = Number(inventoryOverview.pendingDispatches || 0);
    const expiringSoon = Number(inventoryOverview.expiryAlerts?.within30 || 0);

    const kpiVal = container.querySelector('#kpi-inventory-val');
    const kpiStock = container.querySelector('#kpi-stock-hand');
    const kpiInbound = container.querySelector('#kpi-inbound-today');
    const kpiOutbound = container.querySelector('#kpi-outbound-today');
    const kpiPendingReq = container.querySelector('#kpi-pending-req');
    const kpiPendingDisp = container.querySelector('#kpi-pending-disp');
    const kpiLow = container.querySelector('#kpi-low-stock');
    const kpiExpiring = container.querySelector('#kpi-expiring');

    if (kpiVal) kpiVal.textContent = formatCurrency(inventoryValue);
    if (kpiStock) kpiStock.textContent = stockInHand.toLocaleString(locale);
    if (kpiInbound) kpiInbound.textContent = `${inboundToday} Receipts`;
    if (kpiOutbound) kpiOutbound.textContent = `${outboundToday} Dispatches`;
    if (kpiPendingReq) kpiPendingReq.textContent = `${pendingRequests} Requests`;
    if (kpiPendingDisp) kpiPendingDisp.textContent = `${pendingDispatches} Orders`;
    if (kpiLow) kpiLow.textContent = `${lowStockCount} Items`;
    if (kpiExpiring) kpiExpiring.textContent = `${expiringSoon} Items`;

    // 3. Render Donut Inventory Overview
    this._renderInventoryDonut(container);

    if (window.lucide) window.lucide.createIcons();
  }

  _renderInventoryDonut(container) {
    const inventoryOverview = this.data?.inventoryOverview || {};
    const stockInHand = Number(inventoryOverview.stockInHand || 2568);
    const lowStockCount = Number(inventoryOverview.lowStockCount || 157);
    const outOfStockCount = Number(inventoryOverview.outOfStockCount || 73);

    // Compute dynamic layout stats proportionally
    const reservedVal = Math.round(stockInHand * 0.13) || 342;
    const transitVal = Math.round(stockInHand * 0.08) || 212;
    const availableVal = stockInHand - reservedVal - transitVal - lowStockCount - outOfStockCount;

    const stats = [
      { name: 'Available', value: availableVal > 0 ? availableVal : 1784, color: 'var(--status-success)', selector: '#inv-available' },
      { name: 'Reserved', value: reservedVal, color: 'var(--status-warning)', selector: '#inv-reserved' },
      { name: 'In Transit', value: transitVal, color: 'var(--status-info)', selector: '#inv-transit' },
      { name: 'Low Stock', value: lowStockCount, color: 'var(--accent-secondary)', selector: '#inv-low' },
      { name: 'Out of Stock', value: outOfStockCount, color: 'var(--status-danger)', selector: '#inv-out' }
    ];

    const total = stats.reduce((acc, s) => acc + s.value, 0) || 2568;

    const totalEl = container.querySelector('#inv-total-items');
    if (totalEl) totalEl.textContent = String(total);

    stats.forEach(s => {
      const el = container.querySelector(s.selector);
      if (el) el.textContent = String(s.value);
    });

    const segmentsGroup = container.querySelector('#inv-donut-segments');
    if (segmentsGroup) {
      segmentsGroup.replaceChildren();

      let currentOffset = 25;
      stats.forEach(s => {
        const pct = (s.value / total) * 100;
        const dash = `${pct.toFixed(1)} ${(100 - pct).toFixed(1)}`;
        const offset = currentOffset;
        currentOffset = (currentOffset - pct + 100) % 100;

        const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
        circle.setAttribute('cx', '21');
        circle.setAttribute('cy', '21');
        circle.setAttribute('r', '15.915');
        circle.setAttribute('class', 'donut-segment-circle');
        circle.setAttribute('stroke', s.color);
        circle.setAttribute('stroke-dasharray', dash);
        circle.setAttribute('stroke-dashoffset', String(offset));

        segmentsGroup.appendChild(circle);
      });
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  _bindEvents(container, lifecycle) {
    const filterTimeframe = container.querySelector('#filter-timeframe');
    if (filterTimeframe) {
      const handleTimeframe = (e) => {
        this.filters.timeframe = e.target.value;
      };
      filterTimeframe.addEventListener('change', handleTimeframe);
      lifecycle.onCleanup(() => filterTimeframe.removeEventListener('change', handleTimeframe));
    }

    const filterZone = container.querySelector('#filter-zone');
    if (filterZone) {
      const handleZone = (e) => {
        this.filters.zone = e.target.value;
      };
      filterZone.addEventListener('change', handleZone);
      lifecycle.onCleanup(() => filterZone.removeEventListener('change', handleZone));
    }

    // Interactive actions
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
    logger.debug('RegionalWarehouseAdminDashboard', 'Clock cleared and workspace unmounted.');
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

  _bindActionShortcuts(container, lifecycle) {
    const bindClickAlert = (id, msg) => {
      const btn = container.querySelector(`#${id}`);
      if (btn) {
        const handler = () => alert(msg);
        btn.addEventListener('click', handler);
        lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
      }
    };

    bindClickAlert('btn-create-transfer', 'Action initiated: Create stock transfer request.');
    bindClickAlert('btn-adjust-inv', 'Action initiated: Adjust warehouse inventory.');
    bindClickAlert('btn-start-count', 'Action initiated: Start stock count process.');
    bindClickAlert('btn-approve-requests', 'Action initiated: Approve store supply requests.');
    bindClickAlert('btn-allocate-inv', 'Action initiated: Allocate inventory allocation table.');
    bindClickAlert('btn-sched-dispatch', 'Action initiated: Schedule store dispatch route.');
    bindClickAlert('btn-assign-vehicle', 'Action initiated: Assign delivery vehicle to route.');
    bindClickAlert('btn-assign-driver', 'Action initiated: Assign driver.');
    bindClickAlert('btn-track-deliv', 'Action initiated: Opening live delivery tracker.');
    bindClickAlert('btn-create-pr', 'Action initiated: Create vendor purchase requisition.');
    bindClickAlert('btn-view-vendors', 'Action initiated: Displaying vendors log.');
    bindClickAlert('btn-review-invoices', 'Action initiated: Open invoices review screen.');
    bindClickAlert('btn-create-emerg-req', 'Action initiated: Create emergency purchasing request.');
    bindClickAlert('btn-approve-emerg', 'Action initiated: Approve emergency store procurement.');
    bindClickAlert('btn-assign-tasks', 'Action initiated: Assign workforce duties.');
    bindClickAlert('btn-view-schedules', 'Action initiated: Open workforce schedules calendar.');

    bindClickAlert('btn-view-low-stock-details', 'Action initiated: Displaying low stock items list.');
    bindClickAlert('btn-view-out-stock-details', 'Action initiated: Displaying out of stock items list.');
    bindClickAlert('btn-view-exp7-details', 'Action initiated: Displaying items expiring in 7 days.');
    bindClickAlert('btn-view-exp30-details', 'Action initiated: Displaying items expiring in 30 days.');

    bindClickAlert('qa-approve-req', 'Action initiated: Approve supply requests.');
    bindClickAlert('qa-create-pr', 'Action initiated: Create Purchase Requisition.');
    bindClickAlert('qa-stock-transfer', 'Action initiated: Create Stock Transfer.');
    bindClickAlert('qa-assign-vehicle', 'Action initiated: Assign vehicle to route.');
    bindClickAlert('qa-start-count', 'Action initiated: Start Stock Count.');

    // Route transitions
    const registerRouterRedirect = (id, hash) => {
      const btn = container.querySelector(`#${id}`);
      if (btn) {
        const handler = () => { window.location.hash = hash; };
        btn.addEventListener('click', handler);
        lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
      }
    };

    registerRouterRedirect('btn-view-all-supply', '#supply-requests');

    registerRouterRedirect('qa-view-reports', '#docs');
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'regional-warehouse-admin-dashboard-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/regional-warehouse-admin/pages/dashboard/dashboard.css';
      document.head.appendChild(link);
    }
  }
}
export { RegionalWarehouseAdminDashboard };
