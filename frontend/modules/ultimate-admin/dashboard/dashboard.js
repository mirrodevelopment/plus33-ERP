/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : dashboard.js
 * Path              : frontend/pages/dashboard/ultimate-admin/dashboard.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/regions?size=100, GET /api/v1/stores?size=100
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : services/dashboard/DashboardService, widgets/widget-engine.js?v=10, api/client, core/logger
 * Depends On        : services/dashboard/DashboardService, widgets/widget-engine.js?v=10, api/client, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { dashboardService } from '../../../services/dashboard/DashboardService.js';
import { WidgetEngine } from '../../../widgets/widget-engine.js?v=10';
import { apiClient } from '../../../api/client.js';
import { logger } from '../../../core/logger.js';

export default class UltimateAdminDashboard {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.filters = {
      from: '',
      to: '',
      regionId: '',
      storeId: '',
      rangeType: 'thisMonth'
    };
    this.regions = [];
    this.stores = [];
    this._clockInterval = null;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('UltimateAdminDashboard', 'Mounting Ultimate Admin dashboard...');

    // Load persisted filters
    const savedFilters = localStorage.getItem('dashboard_filters');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (savedFilters) {
      try {
        this.filters = { ...this.filters, ...JSON.parse(savedFilters) };
      } catch (e) {
        logger.warn('UltimateAdminDashboard', 'Failed to parse stored filters', e);
      }
    }

    // Resolve date bounds
    this.resolveDates();

    // Fetch Regions & Stores options in parallel
    try {
      const [regRes, storeRes] = await Promise.all([
        apiClient.get('/api/v1/regions?size=100'),
        apiClient.get('/api/v1/stores?size=100')
      ]);
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (regRes?.success && regRes?.data?.content) {
        this.regions = regRes.data.content;
      }
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (storeRes?.success && storeRes?.data?.content) {
        this.stores = storeRes.data.content;
      }
    } catch (err) {
      logger.error('UltimateAdminDashboard', 'Failed to fetch dropdown filter options', err);
    }

    // Render page structural layout
    container.innerHTML = `
      <!-- Page Header -->
      <div class="dashboard-header flex justify-between align-center mb-lg" style="flex-wrap: wrap; gap: var(--spacing-md);">
        <div>
          <h2 class="m-0" style="font-family: var(--font-display); font-weight: 800; font-size: 1.65rem; letter-spacing: -0.02em;">
            Ultimate Admin Dashboard
          </h2>
          <p class="m-0" style="color: var(--text-muted); font-size: 0.82rem; margin-top: 2px;">
            Enterprise Analytics &amp; Operational Intelligence &nbsp;·&nbsp;
            <span id="live-clock" style="color: var(--accent-primary); font-weight: 600; font-variant-numeric: tabular-nums;"></span>
          </p>
        </div>
        <div style="display:flex; align-items:center; gap: var(--spacing-md);">
          <!-- Data Sync Status Pill -->
          <div id="sync-status-badge" style="display:flex; align-items:center; gap:6px; background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.75rem; font-weight: 600; color: var(--status-success);">
            <span style="width:7px; height:7px; border-radius:50%; background: var(--status-success); display:inline-block; animation: pulse-dot 2s infinite;"></span>
            Live
          </div>
          <!-- Refresh Button -->
          <button id="btn-refresh-dashboard" style="display:flex; align-items:center; gap:6px; background: rgba(201,164,106,0.08); border: 1px solid rgba(201,164,106,0.3); border-radius: var(--radius-md); padding: 6px 14px; color: var(--accent-primary); font-size: 0.78rem; font-weight: 700; cursor:pointer; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(201,164,106,0.16)'" onmouseout="this.style.background='rgba(201,164,106,0.08)'">
            <i data-lucide="refresh-cw" style="width:14px; height:14px;"></i>
            Refresh
          </button>
        </div>
      </div>

      <!-- Filters Ribbon Control -->
      <div class="card mb-lg glass flex gap-md align-center flex-wrap" style="padding: var(--spacing-sm) var(--spacing-md); border-color: rgba(255,255,255,0.06); background: rgba(255,255,255,0.02);">
        <div class="flex flex-col">
          <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; letter-spacing:0.07em; margin-bottom: 4px;">Date Range</label>
          <select id="filter-range-type" style="background: rgba(0,0,0,0.2); color: var(--text-primary); border: 1px solid rgba(255,255,255,0.1); border-radius: var(--radius-sm); padding: 5px 10px; font-size: 0.75rem; font-weight: 600; outline:none; cursor:pointer;">
            <option value="today">Today</option>
            <option value="yesterday">Yesterday</option>
            <option value="thisWeek">This Week</option>
            <option value="lastWeek">Last Week</option>
            <option value="thisMonth">This Month</option>
            <option value="lastMonth">Last Month</option>
            <option value="quarter">This Quarter</option>
            <option value="year">This Year</option>
            <option value="custom">Custom Range...</option>
          </select>
        </div>

        <div id="custom-date-inputs" class="flex gap-xs" style="display: none;">
          <div class="flex flex-col">
            <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; letter-spacing:0.07em; margin-bottom: 4px;">From</label>
            <input type="date" id="filter-date-from" style="background: rgba(0,0,0,0.2); color: var(--text-primary); border: 1px solid rgba(255,255,255,0.1); border-radius: var(--radius-sm); padding: 4px 8px; font-size: 0.75rem; outline:none;">
          </div>
          <div class="flex flex-col">
            <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; letter-spacing:0.07em; margin-bottom: 4px;">To</label>
            <input type="date" id="filter-date-to" style="background: rgba(0,0,0,0.2); color: var(--text-primary); border: 1px solid rgba(255,255,255,0.1); border-radius: var(--radius-sm); padding: 4px 8px; font-size: 0.75rem; outline:none;">
          </div>
        </div>

        <div class="flex flex-col">
          <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; letter-spacing:0.07em; margin-bottom: 4px;">Region</label>
          <select id="filter-region" style="background: rgba(0,0,0,0.2); color: var(--text-primary); border: 1px solid rgba(255,255,255,0.1); border-radius: var(--radius-sm); padding: 5px 10px; font-size: 0.75rem; font-weight: 600; min-width: 140px; outline:none; cursor:pointer;">
            <option value="">All Regions</option>
            <optgroup label="Countries" style="background: #1e1e1e; color: var(--accent-primary);">
              ${this.regions.filter(r => r.parentId === null || r.parentId === undefined).map(r => `<option value="${r.id}">${r.name}</option>`).join('')}
            </optgroup>
            <optgroup label="Sub-Regions" style="background: #1e1e1e; color: var(--text-primary);">
              ${this.regions.filter(r => r.parentId !== null && r.parentId !== undefined).map(r => `<option value="${r.id}">${r.name}</option>`).join('')}
            </optgroup>
          </select>
        </div>

        <div class="flex flex-col">
          <label style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; letter-spacing:0.07em; margin-bottom: 4px;">Store</label>
          <select id="filter-store" style="background: rgba(0,0,0,0.2); color: var(--text-primary); border: 1px solid rgba(255,255,255,0.1); border-radius: var(--radius-sm); padding: 5px 10px; font-size: 0.75rem; font-weight: 600; min-width: 160px; outline:none; cursor:pointer;">
            <option value="">All Stores</option>
            ${this.stores.map(s => `<option value="${s.id}">${s.name}</option>`).join('')}
          </select>
        </div>

        <div style="display:flex; gap: var(--spacing-sm); margin-top: 14px;">
          <button id="btn-apply-filters" class="btn btn-primary" style="font-size: 0.75rem; padding: 5px var(--spacing-md); font-weight: 700; display:flex; align-items:center; gap:5px;">
            <i data-lucide="filter" style="width:13px;height:13px;"></i>
            Apply
          </button>
          <button id="btn-reset-filters" style="font-size: 0.75rem; padding: 5px var(--spacing-md); font-weight: 600; background: transparent; border: 1px solid rgba(255,255,255,0.12); border-radius: var(--radius-sm); color: var(--text-muted); cursor:pointer; display:flex; align-items:center; gap:5px; transition: var(--transition-fast);" onmouseover="this.style.color='var(--text-primary)'" onmouseout="this.style.color='var(--text-muted)'">
            <i data-lucide="x-circle" style="width:13px;height:13px;"></i>
            Reset
          </button>
        </div>
      </div>

      <!-- Responsive Grid for KPIs -->
      <div id="dashboard-kpis-mount" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: var(--spacing-md); width: 100%; margin-bottom: var(--spacing-lg);">
        <!-- KPI Cards dynamically appended here -->
      </div>

      <!-- 12-Column Responsive Grid -->
      <div class="grid grid-cols-12 gap-lg" id="dashboard-grid-mount">
        <!-- Dynamic content or loader goes here -->
      </div>
    `;

    // Bind element refs
    const rangeSelect = container.querySelector('#filter-range-type');
    const customDateContainer = container.querySelector('#custom-date-inputs');
    const dateFromInput = container.querySelector('#filter-date-from');
    const dateToInput = container.querySelector('#filter-date-to');
    const regionSelect = container.querySelector('#filter-region');
    const storeSelect = container.querySelector('#filter-store');
    const applyBtn = container.querySelector('#btn-apply-filters');
    const resetBtn = container.querySelector('#btn-reset-filters');
    const refreshBtn = container.querySelector('#btn-refresh-dashboard');
    const kpisContainer = container.querySelector('#dashboard-kpis-mount');
    const gridContainer = container.querySelector('#dashboard-grid-mount');

    // Restore filter values
    rangeSelect.value = this.filters.rangeType;
    regionSelect.value = this.filters.regionId;
    
    // Synchronize store dropdown to initial region filter value
    const updateStoreDropdown = () => {
      const selectedRegionId = regionSelect.value;
      if (!selectedRegionId) {
        storeSelect.innerHTML = `<option value="">All Stores</option>` + 
          this.stores.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
      } else {
        const childRegionIds = this.regions
          .filter(r => String(r.parentId) === String(selectedRegionId) || String(r.id) === String(selectedRegionId))
          .map(r => String(r.id));
        const filteredStores = this.stores.filter(s => childRegionIds.includes(String(s.regionId)));
        storeSelect.innerHTML = `<option value="">All Stores (${filteredStores.length})</option>` +
          filteredStores.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
      }
    };
    updateStoreDropdown();
    storeSelect.value = this.filters.storeId;
    regionSelect.addEventListener('change', () => {
      updateStoreDropdown();
      storeSelect.value = ''; // Reset selected store when region changes
    });

    dateFromInput.value = this.filters.from;
    dateToInput.value = this.filters.to;

    /**
     * Performs the toggleCustomDates operation in this module.
     * @memberof Pages Module
     */
    const toggleCustomDates = () => {
      customDateContainer.style.display = rangeSelect.value === 'custom' ? 'flex' : 'none';
    };
    toggleCustomDates();
    rangeSelect.addEventListener('change', () => {
      toggleCustomDates();
      this.filters.rangeType = rangeSelect.value;
      this.resolveDates();
      dateFromInput.value = this.filters.from;
      dateToInput.value = this.filters.to;
    });

    // Skeleton loader helper
    /**
     * Performs the showSkeleton operation in this module.
     * @memberof Pages Module
     */
    const showSkeleton = () => {
      kpisContainer.innerHTML = Array(8).fill(0).map(() => `
        <div style="height:90px; border-radius:var(--radius-lg); background: linear-gradient(90deg, rgba(255,255,255,0.04) 25%, rgba(255,255,255,0.08) 50%, rgba(255,255,255,0.04) 75%); background-size: 200% 100%; animation: skeleton-shimmer 1.5s infinite; border: 1px solid rgba(255,255,255,0.05);"></div>
      `).join('');
      gridContainer.innerHTML = `
        <div class="col-12" style="display:grid; grid-template-columns: 8fr 4fr; gap: var(--spacing-lg);">
          ${[220, 280].map(h => `
            <div style="height:${h}px; border-radius:var(--radius-lg); background: linear-gradient(90deg, rgba(255,255,255,0.04) 25%, rgba(255,255,255,0.08) 50%, rgba(255,255,255,0.04) 75%); background-size: 200% 100%; animation: skeleton-shimmer 1.5s infinite; border: 1px solid rgba(255,255,255,0.05);"></div>
          `).join('')}
        </div>
        <div class="col-12" style="display:grid; grid-template-columns: 1fr 1fr 1fr; gap: var(--spacing-lg);">
          ${[200, 200, 200].map(h => `
            <div style="height:${h}px; border-radius:var(--radius-lg); background: linear-gradient(90deg, rgba(255,255,255,0.04) 25%, rgba(255,255,255,0.08) 50%, rgba(255,255,255,0.04) 75%); background-size: 200% 100%; animation: skeleton-shimmer 1.5s infinite; border: 1px solid rgba(255,255,255,0.05);"></div>
          `).join('')}
        </div>
      `;
    };

    /**
     * Performs the refreshDashboard operation in this module.
     * @memberof Pages Module
     */
    const refreshDashboard = async () => {
      showSkeleton();
      // Spin the refresh icon
      const refreshIcon = refreshBtn?.querySelector('i[data-lucide="refresh-cw"]');
      if (refreshIcon) refreshIcon.style.animation = 'spin 1s linear infinite';

      try {
        const queryParams = {
          from: this.filters.from,
          to: this.filters.to
        };
        if (this.filters.regionId) queryParams.regionId = this.filters.regionId;
        if (this.filters.storeId) queryParams.storeId = this.filters.storeId;

        const overview = await dashboardService.getDashboardOverview(queryParams);
        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
        if (!overview) {
          throw new Error('Failed to retrieve server analytics overview.');
        }

        const dashboardRes = await fetch('roles/ultimate-admin/dashboard.json');
        const layoutRes = await fetch('roles/ultimate-admin/layout.json');

        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
        if (!dashboardRes.ok || !layoutRes.ok) {
          throw new Error('Failed to load JSON configurations.');
        }

        const { widgets } = await dashboardRes.json();
        const { layout } = await layoutRes.json();

        gridContainer.innerHTML = '';
        kpisContainer.innerHTML = '';

        /**
         * Performs the widget operation in this module.
         * @memberof Pages Module
         */
        for (const widget of widgets) {
          const positioning = layout.find(l => l.id === widget.id);
          const targetContainer = widget.type === 'kpi' ? kpisContainer : gridContainer;
          await WidgetEngine.loadWidget(widget, positioning, targetContainer, overview, lifecycle);
        }
      } catch (err) {
        logger.error('UltimateAdminDashboard', 'Failed to render dashboard overview:', err);
        kpisContainer.innerHTML = '';
        gridContainer.innerHTML = `
          <div class="card col-12" style="display:flex; flex-direction:column; gap:var(--spacing-sm); background: rgba(201, 92, 92, 0.06); border: 1px solid var(--status-danger); padding: var(--spacing-xl);">
            <div style="display:flex; align-items:center; gap:10px; color: var(--status-danger);">
              <i data-lucide="wifi-off" style="width:22px;height:22px;"></i>
              <h4 class="m-0">Enterprise Analytics Unavailable</h4>
            </div>
            <p class="m-0" style="color: var(--text-muted); font-size:0.85rem;">${err.message}</p>
            <button onclick="window.location.reload()" style="margin-top:8px; width:fit-content; padding: 6px 16px; font-size:0.8rem; background: rgba(201,164,106,0.1); border:1px solid var(--accent-primary); border-radius:var(--radius-sm); color:var(--accent-primary); cursor:pointer; display:flex; align-items:center; gap:6px;">
              <i data-lucide="refresh-cw" style="width:13px;height:13px;"></i> Retry Connection
            </button>
          </div>
        `;
        if (window.lucide) window.lucide.createIcons();
      } finally {
        // Stop spinning refresh icon
        if (refreshIcon) refreshIcon.style.animation = '';
        if (window.lucide) window.lucide.createIcons();
      }
    };

    // Apply filters
    applyBtn.addEventListener('click', () => {
      this.filters.rangeType = rangeSelect.value;
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (this.filters.rangeType === 'custom') {
        this.filters.from = dateFromInput.value;
        this.filters.to = dateToInput.value;
      } else {
        this.resolveDates();
      }
      this.filters.regionId = regionSelect.value;
      this.filters.storeId = storeSelect.value;
      localStorage.setItem('dashboard_filters', JSON.stringify(this.filters));
      refreshDashboard();
    });

    // Reset filters
    resetBtn.addEventListener('click', () => {
      this.filters = { from: '', to: '', regionId: '', storeId: '', rangeType: 'thisMonth' };
      this.resolveDates();
      rangeSelect.value = this.filters.rangeType;
      regionSelect.value = '';
      storeSelect.value = '';
      dateFromInput.value = '';
      dateToInput.value = '';
      toggleCustomDates();
      localStorage.removeItem('dashboard_filters');
      refreshDashboard();
    });

    // Refresh button
    refreshBtn.addEventListener('click', refreshDashboard);

    // Live clock
    const clockEl = container.querySelector('#live-clock');
    /**
     * Performs the tickClock operation in this module.
     * @memberof Pages Module
     */
    const tickClock = () => {
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (clockEl) {
        try {
          const now = new Date();
          let timeFormat = '24h';
          let timezone = 'Europe/Paris';
          
          const storedGeneral = localStorage.getItem('plus33-settings-general');
          if (storedGeneral) {
            try {
              const parsed = JSON.parse(storedGeneral);
              if (parsed.timeFormat) timeFormat = parsed.timeFormat;
              if (parsed.defaultTimezone) timezone = parsed.defaultTimezone;
            } catch (e) {
              // ignore
            }
          }
          
          const options = {
            timeZone: timezone,
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: timeFormat === '12h'
          };
          
          const timeStr = now.toLocaleTimeString(timeFormat === '12h' ? 'en-US' : 'fr-FR', options);
          
          let friendlyTimezone = timezone;
          if (timezone === 'Europe/Paris') friendlyTimezone = 'Europe/Paris (CET)';
          else if (timezone === 'Asia/Dubai') friendlyTimezone = 'Asia/Dubai (GST)';
          else if (timezone === 'Asia/Kolkata') friendlyTimezone = 'Asia/Kolkata (IST)';
          else if (timezone === 'UTC') friendlyTimezone = 'UTC (GMT)';
          
          clockEl.textContent = `${friendlyTimezone} · ${timeStr}`;
        } catch (err) {
          const now = new Date();
          clockEl.textContent = now.toLocaleTimeString();
        }
      }
    };
    tickClock();
    this._clockInterval = setInterval(tickClock, 1000);

    // Cleanup on navigation away
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (lifecycle?.onDestroy) {
      lifecycle.onDestroy(() => clearInterval(this._clockInterval));
    }

    // Inject skeleton shimmer keyframe if not already present
    if (!document.getElementById('skeleton-shimmer-style')) {
      const style = document.createElement('style');
      style.id = 'skeleton-shimmer-style';
      style.textContent = `
        @keyframes skeleton-shimmer {
          0%   { background-position: 200% 0; }
          100% { background-position: -200% 0; }
        }
        @keyframes pulse-dot {
          0%, 100% { opacity: 1; }
          50% { opacity: 0.3; }
        }
      `;
      document.head.appendChild(style);
    }

    // Initial load
    if (window.lucide) window.lucide.createIcons();
    await refreshDashboard();
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  resolveDates() {
    const today = new Date();
    let fromDate = new Date();
    let toDate = new Date();

    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    switch (this.filters.rangeType) {
      case 'today':
        break;
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
        toDate = new Date(today.getFullYear(), today.getMonth(), 0);
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

    /**
     * Performs the fmt operation in this module.
     * @memberof Pages Module
     */
    const fmt = (d) => {
      const y = d.getFullYear();
      const m = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${y}-${m}-${day}`;
    };

    this.filters.from = fmt(fromDate);
    this.filters.to = fmt(toDate);
  }
}
export { UltimateAdminDashboard };



