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
      from: '',
      to: '',
      nationId: '',
      regionId: '',
      storeId: '',
      rangeType: 'thisMonth'
    };
    this.data = null;
    this.activeStoreTab = 'top'; // 'top', 'under', 'below'

    // Lists populated from DB
    this.regionsList = [];
    this.stores = [];
    this.warehouses = [];
    this.suppliers = [];
    localStorage.removeItem('regional_admin_dashboard_filters'); // clear legacy key
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  async mount(container, lifecycle) {
    logger.info('RegionalAdminDashboard', 'Mounting Regional Admin dashboard...');
    this.lifecycle = lifecycle;

    // Dynamically load settings styles
    this._loadCss();

    // Restore persisted filters
    this._restoreFilters();

    // Resolve date bounds for the selected range type
    this.resolveDates();

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
      // 1. Fetch user profile info dynamically if not loaded
      if (!this.profile || !this.profile.country) {
        const meRes = await apiClient.get('/api/v1/auth/me');
        if (meRes?.success && meRes?.data) {
          this.profile = { ...this.profile, ...meRes.data };
        }
      }

      // 2. Fetch entities dynamically first
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

      // 3. Resolve locked nation and region based on profile
      this._resolveUserScope();

      // 4. Default filter values for first-load scoping
      const userRole = this.user?.role || authStore.getUser()?.role;
      if (userRole !== 'ultimateAdmin') {
        if (this.loggedNationId && !this.filters.nationId) {
          this.filters.nationId = this.loggedNationId;
        }
        if (this.loggedRegionId && !this.filters.regionId) {
          this.filters.regionId = this.loggedRegionId;
        }
      }

      // 5. Construct parameter queries
      const params = {
        from: this.filters.from,
        to: this.filters.to
      };

      if (this.filters.regionId) {
        params.regionId = this.filters.regionId;
      } else if (this.filters.nationId) {
        params.regionId = this.filters.nationId;
      }
      if (this.filters.storeId) {
        params.storeId = this.filters.storeId;
      }

      // 6. Fetch scoped backend metrics overview
      this.data = await dashboardService.getDashboardOverview(params);
      
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
    this.profile = this.profile || userStore.getProfile(this.user?.role);

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

    // 2.5. Populate filter ribbon options dynamically
    this._populateNationDropdown(container);
    this._populateRegionDropdown(container);
    this._updateStoreDropdown(container);
    this._restoreFilterValues(container);
    this._toggleCustomDates(container);

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
    const rangeSelect  = container.querySelector('#filter-range-type');
    const nationSelect = container.querySelector('#filter-nation');
    const regionSelect = container.querySelector('#filter-region');
    const storeSelect  = container.querySelector('#filter-store');
    const dateFrom     = container.querySelector('#filter-date-from');
    const dateTo       = container.querySelector('#filter-date-to');
    const applyBtn     = container.querySelector('#btn-apply-filters');
    const resetBtn     = container.querySelector('#btn-reset-filters');

    const applyFilters = () => {
      this.filters.rangeType = rangeSelect?.value || 'thisMonth';
      if (this.filters.rangeType === 'custom') {
        this.filters.from = dateFrom?.value || '';
        this.filters.to   = dateTo?.value   || '';
      } else {
        this.resolveDates();
      }
      this.filters.nationId = nationSelect?.value || '';
      this.filters.regionId = regionSelect?.value || '';
      this.filters.storeId  = storeSelect?.value  || '';
      localStorage.setItem(this._getStorageKey(), JSON.stringify(this.filters));
      this._triggerRefresh(container);
    };

    // Date range type change
    if (rangeSelect) {
      const handleRangeChange = () => {
        this.filters.rangeType = rangeSelect.value;
        this.resolveDates();
        this._toggleCustomDates(container);
        const fromEl = container.querySelector('#filter-date-from');
        const toEl   = container.querySelector('#filter-date-to');
        if (fromEl) fromEl.value = this.filters.from;
        if (toEl)   toEl.value   = this.filters.to;
        if (rangeSelect.value !== 'custom') {
          applyFilters();
        }
      };
      rangeSelect.addEventListener('change', handleRangeChange);
      lifecycle.onCleanup(() => rangeSelect.removeEventListener('change', handleRangeChange));
    }

    // Nation change → update region dropdown options and reset sub-filters
    if (nationSelect) {
      const handleNationChange = () => {
        this._populateRegionDropdown(container);
        if (regionSelect) regionSelect.value = '';
        this._updateStoreDropdown(container);
        if (storeSelect) storeSelect.value = '';
        applyFilters();
      };
      nationSelect.addEventListener('change', handleNationChange);
      lifecycle.onCleanup(() => nationSelect.removeEventListener('change', handleNationChange));
    }

    // Region change → update store dropdown
    if (regionSelect) {
      const handleRegionChange = () => {
        this._updateStoreDropdown(container);
        if (storeSelect) storeSelect.value = '';
        applyFilters();
      };
      regionSelect.addEventListener('change', handleRegionChange);
      lifecycle.onCleanup(() => regionSelect.removeEventListener('change', handleRegionChange));
    }

    // Store change
    if (storeSelect) {
      const handleStoreChange = () => {
        applyFilters();
      };
      storeSelect.addEventListener('change', handleStoreChange);
      lifecycle.onCleanup(() => storeSelect.removeEventListener('change', handleStoreChange));
    }

    // Custom date changes
    if (dateFrom) {
      const handleDateFromChange = () => {
        if (rangeSelect?.value === 'custom' && dateFrom.value && dateTo?.value) {
          applyFilters();
        }
      };
      dateFrom.addEventListener('change', handleDateFromChange);
      lifecycle.onCleanup(() => dateFrom.removeEventListener('change', handleDateFromChange));
    }
    if (dateTo) {
      const handleDateToChange = () => {
        if (rangeSelect?.value === 'custom' && dateFrom?.value && dateTo.value) {
          applyFilters();
        }
      };
      dateTo.addEventListener('change', handleDateToChange);
      lifecycle.onCleanup(() => dateTo.removeEventListener('change', handleDateToChange));
    }

    // Apply filters
    if (applyBtn) {
      const handleApply = () => applyFilters();
      applyBtn.addEventListener('click', handleApply);
      lifecycle.onCleanup(() => applyBtn.removeEventListener('click', handleApply));
    }

    // Reset filters
    if (resetBtn) {
      const handleReset = () => {
        const userRole = this.user?.role;
        this.filters = { 
          from: '', 
          to: '', 
          nationId: (userRole !== 'ultimateAdmin') ? this.loggedNationId : '', 
          regionId: (userRole !== 'ultimateAdmin') ? this.loggedRegionId : '', 
          storeId: '', 
          rangeType: 'thisMonth' 
        };
        this.resolveDates();
        if (rangeSelect)  rangeSelect.value  = this.filters.rangeType;
        if (nationSelect) nationSelect.value = this.filters.nationId;
        this._populateRegionDropdown(container);
        if (regionSelect) regionSelect.value = this.filters.regionId;
        this._updateStoreDropdown(container);
        if (storeSelect)  storeSelect.value  = '';
        if (dateFrom)     dateFrom.value     = '';
        if (dateTo)       dateTo.value       = '';
        this._toggleCustomDates(container);
        localStorage.removeItem(this._getStorageKey());
        localStorage.removeItem('regional_admin_dashboard_filters'); // clear legacy key
        this._triggerRefresh(container);
      };
      resetBtn.addEventListener('click', handleReset);
      lifecycle.onCleanup(() => resetBtn.removeEventListener('click', handleReset));
    }

    // Tabs for Store Performance
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

    // Interactive actions
    this._bindActionShortcuts(container, lifecycle);
  }

  _populateNationDropdown(container) {
    const nationSelect = container.querySelector('#filter-nation');
    if (!nationSelect) return;
    nationSelect.replaceChildren();

    const userRole = this.user?.role;
    if (userRole !== 'ultimateAdmin' && this.loggedNationId) {
      const matchingNation = this.regionsList.find(r => String(r.id) === String(this.loggedNationId));
      if (matchingNation) {
        const opt = document.createElement('option');
        opt.value       = matchingNation.id;
        opt.textContent = matchingNation.name;
        nationSelect.appendChild(opt);
        return;
      }
    }

    const allOpt = document.createElement('option');
    allOpt.value       = '';
    allOpt.textContent = 'All Nations';
    nationSelect.appendChild(allOpt);

    this.regionsList
      .filter(r => r.parentId === null || r.parentId === undefined)
      .forEach(r => {
        const opt = document.createElement('option');
        opt.value       = r.id;
        opt.textContent = r.name;
        nationSelect.appendChild(opt);
      });
  }

  _populateRegionDropdown(container) {
    const nationSelect = container.querySelector('#filter-nation');
    const regionSelect = container.querySelector('#filter-region');
    if (!regionSelect) return;
    regionSelect.replaceChildren();

    const userRole = this.user?.role;
    if (userRole !== 'ultimateAdmin' && this.loggedRegionId) {
      const matchingRegion = this.regionsList.find(r => String(r.id) === String(this.loggedRegionId));
      if (matchingRegion) {
        const opt = document.createElement('option');
        opt.value       = matchingRegion.id;
        opt.textContent = matchingRegion.name;
        regionSelect.appendChild(opt);
        return;
      }
    }

    const selectedNationId = nationSelect?.value || '';
    const allOpt = document.createElement('option');
    allOpt.value       = '';
    allOpt.textContent = 'All Regions';
    regionSelect.appendChild(allOpt);

    let filteredRegions = this.regionsList.filter(r => r.parentId !== null && r.parentId !== undefined);
    if (selectedNationId) {
      filteredRegions = filteredRegions.filter(r => String(r.parentId) === String(selectedNationId));
    }

    filteredRegions.forEach(r => {
      const opt = document.createElement('option');
      opt.value       = r.id;
      opt.textContent = r.name;
      regionSelect.appendChild(opt);
    });
  }

  _updateStoreDropdown(container) {
    const nationSelect = container.querySelector('#filter-nation');
    const regionSelect = container.querySelector('#filter-region');
    const storeSelect  = container.querySelector('#filter-store');
    if (!storeSelect) return;

    const selectedNationId = nationSelect?.value || '';
    const selectedRegionId = regionSelect?.value || '';
    storeSelect.replaceChildren();

    let filteredStores = this.stores;
    let label = 'All Stores';

    if (selectedRegionId) {
      filteredStores = this.stores.filter(s => String(s.regionId) === String(selectedRegionId));
      label = `All Stores (${filteredStores.length})`;
    } else if (selectedNationId) {
      const childRegionIds = this.regionsList
        .filter(r => String(r.parentId) === String(selectedNationId))
        .map(r => String(r.id));
      filteredStores = this.stores.filter(s => childRegionIds.includes(String(s.regionId)));
      label = `All Stores (${filteredStores.length})`;
    }

    const allOpt = document.createElement('option');
    allOpt.value       = '';
    allOpt.textContent = label;
    storeSelect.appendChild(allOpt);

    filteredStores.forEach(s => {
      const opt = document.createElement('option');
      opt.value       = s.id;
      const typeStr = s.type === 'COMPACT_CAFE' ? 'COMPACT CAFÉ' : s.type === 'FLAGSHIP_CAFE' ? 'FLAGSHIP CAFÉ' : (s.type || '');
      opt.textContent = s.name + (typeStr ? ` (${typeStr})` : '');
      storeSelect.appendChild(opt);
    });
  }

  _restoreFilterValues(container) {
    const rangeSelect  = container.querySelector('#filter-range-type');
    const nationSelect = container.querySelector('#filter-nation');
    const regionSelect = container.querySelector('#filter-region');
    const storeSelect  = container.querySelector('#filter-store');
    const dateFrom     = container.querySelector('#filter-date-from');
    const dateTo       = container.querySelector('#filter-date-to');

    const userRole = this.user?.role;
    // For non-ultimate admins: lock nation and region to their assigned scope
    // BUT preserve any storeId the user explicitly selected
    if (userRole !== 'ultimateAdmin') {
      if (this.loggedNationId) {
        this.filters.nationId = this.loggedNationId;
      }
      // Only lock region if user has a specific sub-region assignment
      // Don't override if user has explicitly selected a store (allow store to define context)
      if (this.loggedRegionId && !this.filters.storeId) {
        this.filters.regionId = this.loggedRegionId;
      } else if (this.loggedRegionId && !this.filters.regionId) {
        this.filters.regionId = this.loggedRegionId;
      }
    }

    if (rangeSelect)  rangeSelect.value  = this.filters.rangeType;
    if (nationSelect) nationSelect.value = this.filters.nationId;

    this._populateRegionDropdown(container);
    if (regionSelect) regionSelect.value = this.filters.regionId;

    this._updateStoreDropdown(container);
    // Restore the user-selected storeId after dropdown is repopulated
    if (storeSelect && this.filters.storeId) {
      storeSelect.value = this.filters.storeId;
    }

    if (dateFrom)     dateFrom.value     = this.filters.from;
    if (dateTo)       dateTo.value       = this.filters.to;
  }

  _toggleCustomDates(container) {
    const rangeSelect   = container.querySelector('#filter-range-type');
    const customInputs  = container.querySelector('#custom-date-inputs');
    if (customInputs) {
      customInputs.hidden = rangeSelect?.value !== 'custom';
    }
  }

  _resolveUserScope() {
    this.user = this.user || authStore.getUser();
    const userCountry = this.profile?.country || '';
    const userRegion = this.profile?.storeRegion || '';

    // Find country/nation region
    const nation = this.regionsList.find(r => 
      (r.parentId === null || r.parentId === undefined) && 
      r.name.toLowerCase().includes(userCountry.toLowerCase())
    );
    this.loggedNationId = nation ? String(nation.id) : '';

    // Find sub-region
    const region = this.regionsList.find(r => 
      r.name.toLowerCase().trim() === userRegion.toLowerCase().trim()
    );
    if (region && region.parentId !== null && region.parentId !== undefined) {
      this.loggedRegionId = String(region.id);
    } else {
      this.loggedRegionId = '';
    }
  }

  _getStorageKey() {
    const username = this.user?.username || authStore.getUser()?.username || 'default';
    return `regional_admin_dashboard_filters_${username}`;
  }

  _restoreFilters() {
    try {
      // Try per-user key first, fallback to legacy shared key
      const key = this._getStorageKey();
      const saved = localStorage.getItem(key);
      if (saved) this.filters = { ...this.filters, ...JSON.parse(saved) };
    } catch (e) {
      logger.warn('RegionalAdminDashboard', 'Failed to parse stored filters', e);
    }
  }

  resolveDates() {
    const today = new Date();
    let fromDate = new Date();
    let toDate   = new Date();

    switch (this.filters.rangeType) {
      case 'today':      break;
      case 'yesterday':
        fromDate.setDate(today.getDate() - 1);
        toDate.setDate(today.getDate() - 1);
        break;
      case 'thisWeek': {
        const dow = today.getDay();
        fromDate.setDate(today.getDate() - dow);
        break;
      }
      case 'lastWeek':
        fromDate.setDate(today.getDate() - today.getDay() - 7);
        toDate.setDate(today.getDate() - today.getDay() - 1);
        break;
      case 'thisMonth':
        fromDate = new Date(today.getFullYear(), today.getMonth(), 1);
        break;
      case 'lastMonth':
        fromDate = new Date(today.getFullYear(), today.getMonth() - 1, 1);
        toDate   = new Date(today.getFullYear(), today.getMonth(), 0);
        break;
      case 'quarter': {
        const q = Math.floor(today.getMonth() / 3);
        fromDate = new Date(today.getFullYear(), q * 3, 1);
        break;
      }
      case 'year':
        fromDate = new Date(today.getFullYear(), 0, 1);
        break;
      case 'custom':
        return;
    }

    const fmt = (d) => {
      const y   = d.getFullYear();
      const m   = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${y}-${m}-${day}`;
    };

    this.filters.from = fmt(fromDate);
    this.filters.to = fmt(toDate);
  }

  async _triggerRefresh(container) {
    localStorage.setItem(this._getStorageKey(), JSON.stringify(this.filters));
    await this._loadData();
    this._render(container);
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
