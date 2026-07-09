/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin — Dashboard
 * File              : dashboard.js
 * Path              : frontend/modules/ultimate-admin/dashboard/dashboard.js
 * Purpose           : Dashboard page controller
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/ultimate-admin/dashboard/dashboard.html
 * Related CSS       : frontend/modules/ultimate-admin/dashboard/dashboard.css
 * Related APIs      : GET /api/v1/dashboard/overview
 *                     GET /api/v1/regions?size=100
 *                     GET /api/v1/stores?size=100
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in dashboard.html — this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor → mount → loadTemplate → loadData → render → bindEvents → destroy
 ******************************************************************************/

import { htmlLoader } from '../../../core/htmlLoader.js';
import { dashboardService } from '../../../services/dashboard/DashboardService.js';
import { WidgetEngine } from '../../../widgets/widget-engine.js?v=10';
import { apiClient } from '../../../api/client.js';
import { logger } from '../../../core/logger.js';

/** Path to the dashboard HTML template */
const TEMPLATE_URL = 'modules/ultimate-admin/dashboard/dashboard.html';

/** Friendly timezone label map */
const TIMEZONE_LABELS = {
  'Europe/Paris':  'Europe/Paris (CET)',
  'Asia/Dubai':    'Asia/Dubai (GST)',
  'Asia/Kolkata':  'Asia/Kolkata (IST)',
  'UTC':           'UTC (GMT)'
};

export default class UltimateAdminDashboard {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.filters = {
      from: '',
      to: '',
      regionId: '',
      storeId: '',
      rangeType: 'thisMonth'
    };
    this.regions = [];
    this.stores  = [];
    /** @type {number|null} setInterval ID for live clock */
    this._clockInterval = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the dashboard into the container.
   * @param {HTMLElement} container
   * @param {{ onCleanup?: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('UltimateAdminDashboard', 'Mounting...');

    // Restore persisted filters
    this._restoreFilters();

    // Resolve date bounds for the selected range type
    this.resolveDates();

    // 1. Load HTML template
    await this._loadTemplate(container);

    // 2. Load data (regions, stores)
    await this._loadData();

    // 3. Render dynamic content into DOM
    this._render(container);

    // 4. Bind event listeners
    this._bindEvents(container, lifecycle);

    // 5. Initial dashboard data load
    await this._refreshDashboard(container);
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
      const [regRes, storeRes] = await Promise.all([
        apiClient.get('/api/v1/regions?size=100'),
        apiClient.get('/api/v1/stores?size=100')
      ]);
      if (regRes?.success && regRes?.data?.content)     this.regions = regRes.data.content;
      if (storeRes?.success && storeRes?.data?.content) this.stores  = storeRes.data.content;
    } catch (err) {
      logger.error('UltimateAdminDashboard', 'Failed to fetch filter dropdown data', err);
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  /**
   * Populate all dynamic content into the already-injected DOM.
   * @param {HTMLElement} container
   */
  _render(container) {
    this._populateRegionDropdown(container);
    this._updateStoreDropdown(container);
    this._restoreFilterValues(container);
    this._toggleCustomDates(container);
    this._startClock(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  /**
   * Attach all event listeners.
   * @param {HTMLElement} container
   * @param {{ onCleanup?: Function, onDestroy?: Function }} lifecycle
   */
  _bindEvents(container, lifecycle) {
    const rangeSelect  = container.querySelector('#filter-range-type');
    const regionSelect = container.querySelector('#filter-region');
    const storeSelect  = container.querySelector('#filter-store');
    const dateFrom     = container.querySelector('#filter-date-from');
    const dateTo       = container.querySelector('#filter-date-to');
    const applyBtn     = container.querySelector('#btn-apply-filters');
    const resetBtn     = container.querySelector('#btn-reset-filters');
    const refreshBtn   = container.querySelector('#btn-refresh-dashboard');

    // Date range type change
    if (rangeSelect) {
      rangeSelect.addEventListener('change', () => {
        this.filters.rangeType = rangeSelect.value;
        this.resolveDates();
        this._toggleCustomDates(container);
        const fromEl = container.querySelector('#filter-date-from');
        const toEl   = container.querySelector('#filter-date-to');
        if (fromEl) fromEl.value = this.filters.from;
        if (toEl)   toEl.value   = this.filters.to;
      });
    }

    // Region change → update store dropdown
    if (regionSelect) {
      regionSelect.addEventListener('change', () => {
        this._updateStoreDropdown(container);
        if (storeSelect) storeSelect.value = '';
      });
    }

    // Apply filters
    if (applyBtn) {
      applyBtn.addEventListener('click', () => {
        this.filters.rangeType = rangeSelect?.value || 'thisMonth';
        if (this.filters.rangeType === 'custom') {
          this.filters.from = dateFrom?.value || '';
          this.filters.to   = dateTo?.value   || '';
        } else {
          this.resolveDates();
        }
        this.filters.regionId = regionSelect?.value || '';
        this.filters.storeId  = storeSelect?.value  || '';
        localStorage.setItem('dashboard_filters', JSON.stringify(this.filters));
        this._refreshDashboard(container);
      });
    }

    // Reset filters
    if (resetBtn) {
      resetBtn.addEventListener('click', () => {
        this.filters = { from: '', to: '', regionId: '', storeId: '', rangeType: 'thisMonth' };
        this.resolveDates();
        if (rangeSelect)  rangeSelect.value  = this.filters.rangeType;
        if (regionSelect) regionSelect.value = '';
        if (storeSelect)  storeSelect.value  = '';
        if (dateFrom)     dateFrom.value     = '';
        if (dateTo)       dateTo.value       = '';
        this._updateStoreDropdown(container);
        this._toggleCustomDates(container);
        localStorage.removeItem('dashboard_filters');
        this._refreshDashboard(container);
      });
    }

    // Refresh button
    if (refreshBtn) {
      refreshBtn.addEventListener('click', () => this._refreshDashboard(container));
    }

    // Destroy: stop clock interval
    if (lifecycle?.onDestroy) {
      lifecycle.onDestroy(() => this.destroy());
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    if (this._clockInterval) {
      clearInterval(this._clockInterval);
      this._clockInterval = null;
    }
    logger.debug('UltimateAdminDashboard', 'Destroyed.');
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Dashboard Data Refresh
  // ---------------------------------------------------------------------------

  /**
   * Fetch dashboard overview and render widgets.
   * @param {HTMLElement} container
   */
  async _refreshDashboard(container) {
    const kpisContainer = container.querySelector('#dashboard-kpis-mount');
    const gridContainer = container.querySelector('#dashboard-grid-mount');
    const refreshBtn    = container.querySelector('#btn-refresh-dashboard');
    const refreshIcon   = refreshBtn?.querySelector('[data-lucide="refresh-cw"]');

    // Show skeleton loaders
    this._showSkeleton(kpisContainer, gridContainer);

    // Spin refresh icon
    if (refreshIcon) refreshIcon.classList.add('icon-spinning');

    try {
      const queryParams = { from: this.filters.from, to: this.filters.to };
      if (this.filters.regionId) queryParams.regionId = this.filters.regionId;
      if (this.filters.storeId)  queryParams.storeId  = this.filters.storeId;

      const overview = await dashboardService.getDashboardOverview(queryParams);
      if (!overview) throw new Error('Failed to retrieve server analytics overview.');

      const [dashboardRes, layoutRes] = await Promise.all([
        fetch('roles/ultimate-admin/dashboard.json'),
        fetch('roles/ultimate-admin/layout.json')
      ]);

      if (!dashboardRes.ok || !layoutRes.ok) {
        throw new Error('Failed to load widget configuration files.');
      }

      const { widgets } = await dashboardRes.json();
      const { layout }  = await layoutRes.json();

      kpisContainer.replaceChildren();
      gridContainer.replaceChildren();

      for (const widget of widgets) {
        const positioning = layout.find(l => l.id === widget.id);
        const targetEl    = widget.type === 'kpi' ? kpisContainer : gridContainer;
        await WidgetEngine.loadWidget(widget, positioning, targetEl, overview, {});
      }

      if (window.lucide) window.lucide.createIcons();

    } catch (err) {
      logger.error('UltimateAdminDashboard', 'Failed to render dashboard overview', err);
      this._showError(container, kpisContainer, gridContainer, err.message);
    } finally {
      if (refreshIcon) refreshIcon.classList.remove('icon-spinning');
      if (window.lucide) window.lucide.createIcons();
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: DOM Helpers
  // ---------------------------------------------------------------------------

  /**
   * Populate region dropdown from this.regions data.
   * @param {HTMLElement} container
   */
  _populateRegionDropdown(container) {
    const countriesGroup  = container.querySelector('#filter-region-countries');
    const subRegionGroup  = container.querySelector('#filter-region-subregions');
    if (!countriesGroup || !subRegionGroup) return;

    countriesGroup.replaceChildren();
    subRegionGroup.replaceChildren();

    this.regions
      .filter(r => r.parentId === null || r.parentId === undefined)
      .forEach(r => {
        const opt = document.createElement('option');
        opt.value       = r.id;
        opt.textContent = r.name;
        countriesGroup.appendChild(opt);
      });

    this.regions
      .filter(r => r.parentId !== null && r.parentId !== undefined)
      .forEach(r => {
        const opt = document.createElement('option');
        opt.value       = r.id;
        opt.textContent = r.name;
        subRegionGroup.appendChild(opt);
      });
  }

  /**
   * Update store dropdown based on currently selected region.
   * @param {HTMLElement} container
   */
  _updateStoreDropdown(container) {
    const regionSelect = container.querySelector('#filter-region');
    const storeSelect  = container.querySelector('#filter-store');
    if (!storeSelect) return;

    const selectedRegionId = regionSelect?.value || '';
    storeSelect.replaceChildren();

    let filteredStores = this.stores;
    let label = 'All Stores';

    if (selectedRegionId) {
      const childRegionIds = this.regions
        .filter(r => String(r.parentId) === String(selectedRegionId) || String(r.id) === String(selectedRegionId))
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
      opt.textContent = s.name;
      storeSelect.appendChild(opt);
    });
  }

  /**
   * Restore filter form values from this.filters.
   * @param {HTMLElement} container
   */
  _restoreFilterValues(container) {
    const rangeSelect  = container.querySelector('#filter-range-type');
    const regionSelect = container.querySelector('#filter-region');
    const storeSelect  = container.querySelector('#filter-store');
    const dateFrom     = container.querySelector('#filter-date-from');
    const dateTo       = container.querySelector('#filter-date-to');

    if (rangeSelect)  rangeSelect.value  = this.filters.rangeType;
    if (regionSelect) regionSelect.value = this.filters.regionId;
    if (storeSelect)  storeSelect.value  = this.filters.storeId;
    if (dateFrom)     dateFrom.value     = this.filters.from;
    if (dateTo)       dateTo.value       = this.filters.to;
  }

  /**
   * Show/hide the custom date inputs based on rangeType.
   * @param {HTMLElement} container
   */
  _toggleCustomDates(container) {
    const rangeSelect   = container.querySelector('#filter-range-type');
    const customInputs  = container.querySelector('#custom-date-inputs');
    if (customInputs) {
      customInputs.hidden = rangeSelect?.value !== 'custom';
    }
  }

  /**
   * Start the live clock interval.
   * @param {HTMLElement} container
   */
  _startClock(container) {
    const clockEl = container.querySelector('#live-clock');
    if (!clockEl) return;

    const tick = () => {
      try {
        const stored   = localStorage.getItem('plus33-settings-general');
        let timeFormat = '24h', timezone = 'Europe/Paris';
        if (stored) {
          const parsed = JSON.parse(stored);
          if (parsed.timeFormat)      timeFormat = parsed.timeFormat;
          if (parsed.defaultTimezone) timezone   = parsed.defaultTimezone;
        }
        const timeStr       = new Date().toLocaleTimeString(
          timeFormat === '12h' ? 'en-US' : 'fr-FR',
          { timeZone: timezone, hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: timeFormat === '12h' }
        );
        const friendlyTz    = TIMEZONE_LABELS[timezone] || timezone;
        clockEl.textContent = `${friendlyTz} · ${timeStr}`;
      } catch {
        clockEl.textContent = new Date().toLocaleTimeString();
      }
    };

    tick();
    this._clockInterval = setInterval(tick, 1000);
  }

  /**
   * Show skeleton placeholder cards while data loads.
   */
  _showSkeleton(kpisContainer, gridContainer) {
    if (!kpisContainer || !gridContainer) return;

    kpisContainer.replaceChildren();
    for (let i = 0; i < 8; i++) {
      const div = document.createElement('div');
      div.className = 'skeleton-card';
      kpisContainer.appendChild(div);
    }

    gridContainer.replaceChildren();
    const row1 = document.createElement('div');
    row1.className = 'col-12 skeleton-row skeleton-row--2col';
    const row2 = document.createElement('div');
    row2.className = 'col-12 skeleton-row skeleton-row--3col';
    [row1, row2].forEach(row => {
      gridContainer.appendChild(row);
    });
  }

  /**
   * Show an error card when data fetch fails.
   * @param {HTMLElement} container
   * @param {HTMLElement} kpisContainer
   * @param {HTMLElement} gridContainer
   * @param {string} message
   */
  _showError(container, kpisContainer, gridContainer, message) {
    if (kpisContainer) kpisContainer.replaceChildren();
    if (!gridContainer) return;

    const tpl = container.querySelector('#dashboard-error-tpl');
    if (tpl) {
      const clone = tpl.content.cloneNode(true);
      const msgEl = clone.querySelector('.error-message');
      if (msgEl) msgEl.textContent = message;

      const retryBtn = clone.querySelector('.error-retry-btn');
      if (retryBtn) {
        retryBtn.addEventListener('click', () => window.location.reload());
      }
      gridContainer.replaceChildren(clone);
    } else {
      // Fallback if template not found
      const div = document.createElement('div');
      div.className = 'card col-12';
      div.textContent = `Analytics Unavailable: ${message}`;
      gridContainer.replaceChildren(div);
    }
    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Filter Persistence
  // ---------------------------------------------------------------------------

  /**
   * Load persisted filters from localStorage.
   */
  _restoreFilters() {
    try {
      const saved = localStorage.getItem('dashboard_filters');
      if (saved) this.filters = { ...this.filters, ...JSON.parse(saved) };
    } catch (e) {
      logger.warn('UltimateAdminDashboard', 'Failed to parse stored filters', e);
    }
  }

  // ---------------------------------------------------------------------------
  // PUBLIC: Date Resolver (preserved from original)
  // ---------------------------------------------------------------------------

  /**
   * Resolve from/to date strings based on current rangeType.
   * Called both on mount and on dropdown changes.
   */
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
    this.filters.to   = fmt(toDate);
  }
}

export { UltimateAdminDashboard };
