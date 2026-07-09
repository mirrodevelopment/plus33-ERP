/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : national.js
 * Purpose           : Frontend page component for National Management UI (countries)
 * Version           : 0.0.4
 ******************************************************************************/

import { dashboardService } from '../../../services/dashboard/DashboardService.js';
import { apiClient } from '../../../api/client.js';
import { logger } from '../../../core/logger.js';
import { notificationStore } from '../../../store/notificationStore.js';

export default class NationalPage {
  constructor() {
    this.countries = [];
    this.allStores = [];
    this.regionsData = [];
    this.allRegionsPerformance = [];
    this.filters = {
      search: '',
      from: '',
      to: '',
      sortBy: 'sales-desc',
      nation: 'all'
    };
  }

  async mount(container, lifecycle) {
    logger.info('NationalPage', 'Loading national management workspace...');
    this.container = container;
    this.lifecycle = lifecycle;

    // Initialize default dates if empty (default to this month to date)
    if (!this.filters.from || !this.filters.to) {
      const today = new Date();
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
      
      const fmt = (d) => {
        const y = d.getFullYear();
        const m = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        return `${y}-${m}-${day}`;
      };
      
      this.filters.from = fmt(firstDay);
      this.filters.to = fmt(today);
    }

    await this.loadAndRender();
  }

  async loadAndRender() {
    try {
      const queryParams = {};
      if (this.filters.from) queryParams.from = this.filters.from;
      if (this.filters.to) queryParams.to = this.filters.to;

      const overview = await dashboardService.getDashboardOverview(queryParams);
      
      // Query raw database list from GET /api/v1/regions
      const res = await apiClient.get('/api/v1/regions', { size: 100 });
      let backendRegions = [];
      if (res && res.success && res.data && res.data.content) {
        backendRegions = res.data.content;
      }
      this.regionsData = backendRegions;

      // Query raw database stores list
      const storesRes = await apiClient.get('/api/v1/stores', { size: 200 });
      let backendStores = [];
      if (storesRes && storesRes.success && storesRes.data && storesRes.data.content) {
        backendStores = storesRes.data.content;
      }
      this.allStores = backendStores;

      // Filter only parent countries (where parentId is null/undefined)
      const parentRegions = backendRegions.filter(br => br.parentId === null || br.parentId === undefined);
      const rawCountriesPerf = (overview && overview.regionalPerformance) ? overview.regionalPerformance : [];
      const rawSubRegions = (overview && overview.subRegionalPerformance) ? overview.subRegionalPerformance : [];
      this.allRegionsPerformance = rawSubRegions;

      this.countries = parentRegions.map(br => {
        const perf = rawCountriesPerf.find(or => or.region.toLowerCase() === br.name.toLowerCase());
        const subRegionsCount = backendRegions.filter(sub => sub.parentId === br.id).length;

        return {
          id: br.id,
          country: br.name,
          code: br.code,
          description: br.description || '',
          subRegionsCount: subRegionsCount,
          sales: perf ? Number(perf.sales || 0) : 0,
          target: perf ? Number(perf.target || 0) : 10000,
          achievement: perf ? Number(perf.achievement || 0) : 0,
          stores: perf ? Number(perf.stores || 0) : 0,
          employees: perf ? Number(perf.employees || 0) : 0,
          orders: perf ? Number(perf.orders || 0) : 0
        };
      });

      this.overview = overview;
      this.render(this.container);
      this.bindEvents(this.container, this.lifecycle);
    } catch (err) {
      logger.error('NationalPage', 'Failed to load and render national data:', err);
      notificationStore.danger('Failed to sync with database countries.');
    }
  }

  render(container) {
    const totalCountries = this.countries.length;
    const totalSales = this.countries.reduce((acc, c) => acc + Number(c.sales || 0), 0);
    const avgAchievement = totalCountries > 0 
      ? (this.countries.reduce((acc, c) => acc + Number(c.achievement || 0), 0) / totalCountries).toFixed(1)
      : '0.0';
    const totalStores = this.countries.reduce((acc, c) => acc + Number(c.stores || 0), 0);

    const totalRegions = this.overview?.kpis?.totalRegions || this.regionsData.filter(r => r.parentId !== null && r.parentId !== undefined).length;
    const totalWarehouses = this.overview?.kpis?.totalWarehouses || 0;
    const totalEmployees = this.overview?.kpis?.totalEmployees || this.countries.reduce((acc, c) => acc + Number(c.employees || 0), 0);
    const totalOrders = this.overview?.kpis?.orders || this.countries.reduce((acc, c) => acc + Number(c.orders || 0), 0);

    let currencyCode = 'EUR';
    let locale = 'fr-FR';
    let symbol = '€';
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      try {
        const parsed = JSON.parse(storedGeneral);
        if (parsed.defaultCurrency) {
          currencyCode = parsed.defaultCurrency;
          if (currencyCode === 'USD') { symbol = '$'; locale = 'en-US'; }
          else if (currencyCode === 'INR') { symbol = '₹'; locale = 'en-IN'; }
          else if (currencyCode === 'AED') { symbol = 'AED '; locale = 'en-US'; }
        }
      } catch (e) {
        // ignore
      }
    }

    this.currencyCode = currencyCode;
    this.currencyLocale = locale;
    this.currencySymbol = symbol;

    const formattedSales = new Intl.NumberFormat(locale, {
      style: 'currency', currency: currencyCode, maximumFractionDigits: 0
    }).format(totalSales);

    const colors = [
      'var(--accent-primary)',
      'var(--accent-secondary)',
      'var(--status-info)',
      'var(--status-success)',
      'var(--status-warning)',
      '#a855f7',
      '#ec4899',
      '#f43f5e'
    ];

    let currentOffset = 25;
    const segmentsSvg = this.countries.map((c, i) => {
      const pct = totalSales > 0 ? (Number(c.sales || 0) / totalSales) * 100 : 0;
      if (pct <= 0) return '';
      const stroke = colors[i % colors.length];
      const dash = `${pct.toFixed(1)} ${(100 - pct).toFixed(1)}`;
      const offset = currentOffset;
      currentOffset = (currentOffset - pct + 100) % 100;
      return `<circle class="donut-segment" cx="21" cy="21" r="15.915" fill="transparent" stroke="${stroke}" stroke-width="4.2" stroke-dasharray="${dash}" stroke-dashoffset="${offset}" style="transition: stroke-dasharray 0.3s ease;"></circle>`;
    }).join('');

    const formatTotalCompact = (val) => {
      if (val >= 1_000_000) return `${symbol}${(val / 1_000_000).toFixed(1)}M`;
      if (val >= 1_000) return `${symbol}${(val / 1_000).toFixed(1)}K`;
      return `${symbol}${val.toFixed(0)}`;
    };

    const legendListHtml = this.countries.map((c, i) => {
      const pct = totalSales > 0 ? ((Number(c.sales || 0) / totalSales) * 100).toFixed(1) : '0.0';
      const stroke = colors[i % colors.length];
      
      const salesFormatted = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(c.sales || 0);

      return `
        <div class="flex justify-between align-center" style="background: rgba(255,255,255,0.01); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 5px 8px;">
          <div class="flex align-center gap-xs" style="min-width:0; flex-grow:1;">
            <span style="width: 8px; height: 8px; border-radius: 50%; background: ${stroke}; flex-shrink:0;"></span>
            <span style="font-weight: 600; color: var(--text-primary); overflow:hidden; text-overflow:ellipsis; white-space:nowrap; font-size:0.7rem;">${c.country}</span>
          </div>
          <span style="font-weight: 700; color: var(--text-secondary); flex-shrink:0; margin-left: 8px; font-size:0.68rem;">${salesFormatted} (${pct}%)</span>
        </div>
      `;
    }).join('');

    container.innerHTML = `
      <div style="display:flex; flex-direction:column; gap: var(--spacing-lg); max-width: 1200px; margin: 0 auto; padding-bottom: var(--spacing-xxl);">
        
        <!-- Header row -->
        <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
          <div>
            <div class="flex align-center gap-xs" style="color: var(--accent-primary); font-weight: 700; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px;">
              <i data-lucide="globe" style="width: 14px; height: 14px;"></i> Administration
            </div>
            <h1 style="font-family: var(--font-display); font-weight: 800; font-size: 1.8rem; margin: 0; color: var(--text-primary); letter-spacing: -0.01em;">National Management</h1>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0; margin-top: 4px;">Manage global countries, macro regions targets, and operational metrics.</p>
          </div>
          <button id="btn-add-country" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 18px; border-radius: var(--radius-md); border:none; display:flex; align-items:center; gap:6px; cursor:pointer;">
            <i data-lucide="plus" style="width:16px; height:16px;"></i> Add Country
          </button>
        </div>

        <!-- KPI summary grid -->
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: var(--spacing-md); width: 100%;">
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-primary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Countries</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalCountries}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-secondary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Combined Country Revenue</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${formattedSales}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-success);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Avg Country Achievement</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-success); margin-top: 6px;">${avgAchievement}%</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-warning);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Global Stores</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalStores}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-info);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Regions</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalRegions}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: #8b5cf6;"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Warehouses</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalWarehouses}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: #ec4899;"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Employees</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalEmployees}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: #f59e0b;"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Orders</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalOrders}</span>
          </div>
        </div>

        <!-- 2 Column Workspace -->
        <div style="display:grid; grid-template-columns: minmax(0, 7.8fr) minmax(0, 4.2fr); gap: var(--spacing-lg);">
          
          <!-- Left Column: Search & Filter Ribbon + Countries Cards Grid -->
          <div style="display:flex; flex-direction:column; gap: var(--spacing-md); min-width:0;">
            <!-- Filter Bar -->
            <div class="card glass flex flex-col gap-md" style="padding: var(--spacing-md); border-color: rgba(255,255,255,0.03); background: rgba(255,255,255,0.01);">
              <!-- Top Row: Search Name & Sort By -->
              <div class="flex gap-md align-center flex-wrap" style="width: 100%;">
                <div style="display:flex; align-items:center; gap: var(--spacing-xs); flex-grow: 1; background: rgba(0,0,0,0.15); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 6px 12px;">
                  <i data-lucide="search" style="width:14px; height:14px; color: var(--text-muted);"></i>
                  <input id="input-search-countries" type="text" placeholder="Search country names..." style="background:none; border:none; outline:none; color:var(--text-primary); font-size:0.78rem; width:100%;" />
                </div>
                <div class="flex align-center gap-xs" style="flex-shrink:0;">
                  <label style="font-size: 0.68rem; color: var(--text-muted); font-weight:700; white-space:nowrap; text-transform:uppercase; letter-spacing:0.04em;">Nation</label>
                  <select id="filter-nation" style="background: rgba(0,0,0,0.25); color: var(--text-primary); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 5px 8px; font-size: 0.75rem; font-weight: 600; outline:none; cursor:pointer; min-width: 120px;">
                    <option value="all">All Nations</option>
                    ${this.countries.map(c => `<option value="${c.country.toLowerCase()}">${c.country}</option>`).join('')}
                  </select>
                </div>
                <div class="flex align-center gap-xs" style="flex-shrink:0;">
                  <label style="font-size: 0.68rem; color: var(--text-muted); font-weight:700; white-space:nowrap; text-transform:uppercase; letter-spacing:0.04em;">Sort By</label>
                  <select id="filter-sort-by" style="background: rgba(0,0,0,0.25); color: var(--text-primary); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 5px 8px; font-size: 0.75rem; font-weight: 600; outline:none; cursor:pointer; min-width: 135px;">
                    <option value="sales-desc">Highest Revenue</option>
                    <option value="sales-asc">Lowest Revenue</option>
                    <option value="stores-desc">Most Stores</option>
                    <option value="regions-desc">Most Regions</option>
                    <option value="name-asc">Name (A-Z)</option>
                  </select>
                </div>
              </div>

              <!-- Bottom Row: Dates & Apply Button -->
              <div class="flex gap-md align-center flex-wrap" style="border-top: 1px solid rgba(255,255,255,0.03); padding-top: var(--spacing-sm); width: 100%;">
                <div class="flex align-center gap-xs" style="flex-shrink:0;">
                  <label style="font-size: 0.68rem; color: var(--text-muted); font-weight:700; white-space:nowrap; text-transform:uppercase; letter-spacing:0.04em;">From</label>
                  <input id="filter-date-from" type="date" style="background: rgba(0,0,0,0.2); color: var(--text-primary); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 4px 8px; font-size: 0.75rem; outline:none;" />
                  <label style="font-size: 0.68rem; color: var(--text-muted); font-weight:700; white-space:nowrap; text-transform:uppercase; letter-spacing:0.04em; margin-left:6px;">To</label>
                  <input id="filter-date-to" type="date" style="background: rgba(0,0,0,0.2); color: var(--text-primary); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 4px 8px; font-size: 0.75rem; outline:none;" />
                </div>

                <button id="btn-apply-page-filters" class="btn btn-primary" style="font-size: 0.75rem; padding: 5px 12px; font-weight: 700; display:flex; align-items:center; gap:4px; height: 28px; margin-left:auto;">
                  <i data-lucide="filter" style="width:12px; height:12px;"></i> Apply
                </button>
              </div>
            </div>
            
            <!-- Grid list of country cards -->
            <div id="countries-cards-grid" style="display:grid; grid-template-columns: repeat(2, 1fr); gap: var(--spacing-md);">
              ${this.renderCountryCards()}
            </div>
          </div>

          <!-- Right Column: Income Analytics & Donut Circle Graph -->
          <div style="display:flex; flex-direction:column; gap: var(--spacing-md); min-width:0;">
            <div class="card glass" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05); display:flex; flex-direction:column; gap: var(--spacing-md); position: sticky; top: var(--spacing-lg);">
              <h3 style="font-family: var(--font-display); font-weight:800; font-size:0.95rem; margin:0; color:var(--text-primary); border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:10px;">National Income Share</h3>
              
              <div class="flex justify-center align-center" style="padding: var(--spacing-md) 0;">
                <div style="position: relative; width: 140px; height: 140px; display: flex; align-items: center; justify-content: center;">
                  <svg width="140" height="140" viewBox="0 0 42 42" style="transform: rotate(0deg); overflow: visible;">
                    <circle cx="21" cy="21" r="15.915" fill="transparent"></circle>
                    <circle cx="21" cy="21" r="15.915" fill="transparent" stroke="rgba(255,255,255,0.03)" stroke-width="4.2"></circle>
                    ${segmentsSvg}
                  </svg>
                  <div style="position: absolute; display: flex; flex-direction: column; align-items: center; justify-content: center; line-height: 1.15; text-align: center;">
                    <span style="font-family: var(--font-display); font-size: 0.95rem; font-weight: 800; color: var(--text-primary);">${formatTotalCompact(totalSales)}</span>
                    <span style="font-size: 0.5rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; letter-spacing: 0.05em; margin-top:2px;">Combined Sales</span>
                  </div>
                </div>
              </div>

              <!-- Color-coded legends list -->
              <div style="display:flex; flex-direction:column; gap: var(--spacing-xs); max-height: 240px; overflow-y: auto; padding-right: 4px;">
                ${legendListHtml}
              </div>
            </div>
          </div>

        </div>

        <!-- Add Country Modal -->
        <div id="add-country-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.7); z-index:99999; align-items:center; justify-content:center; padding: var(--spacing-lg); backdrop-filter: blur(4px);">
          <div class="card glass" style="width:100%; max-width: 450px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.5);">
            <div class="flex justify-between align-center mb-md" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.15rem; margin:0; color:var(--text-primary);">Create New Country</h3>
              <button id="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.25rem; line-height:1;">&times;</button>
            </div>
            
            <form id="form-add-country" style="display:flex; flex-direction:column; gap: var(--spacing-md); text-align:left;">
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.7rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Country Name</label>
                <input id="modal-input-name" type="text" placeholder="e.g. Germany" required style="padding:10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.7rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Target Sales (€)</label>
                <input id="modal-input-target" type="number" placeholder="e.g. 100000" required style="padding:10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>
              
              <button id="btn-submit-country" type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.8rem; padding:12px; margin-top:var(--spacing-sm); border:none; border-radius:var(--radius-md); cursor:pointer;">
                Register Country
              </button>
            </form>
          </div>
        </div>

        <!-- Details Modal -->
        <div id="details-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.85); z-index:99999; align-items:center; justify-content:center; padding: var(--spacing-xl); backdrop-filter: blur(8px);">
          <div class="card glass" style="width:100%; max-width: 900px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.6); display:flex; flex-direction:column; gap: var(--spacing-lg); max-height: 90vh; overflow:hidden;">
            <div class="flex justify-between align-center" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <div class="flex align-center gap-xs">
                <i data-lucide="globe" style="width:20px; height:20px; color:var(--accent-primary);"></i>
                <h3 id="details-modal-title" style="font-family:var(--font-display); font-weight:800; font-size:1.3rem; margin:0; color:var(--text-primary);">Country Operations Hub</h3>
              </div>
              <button id="btn-close-details-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.5rem; line-height:1; padding:5px;">&times;</button>
            </div>
            
            <!-- Modal Body scrollable -->
            <div id="details-modal-body" style="overflow-y:auto; padding-right:10px; display:flex; flex-direction:column; gap: var(--spacing-xl);">
              <!-- Inside content injected dynamically -->
            </div>
          </div>
        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  renderCountryCards() {
    const query = (this.filters.search || '').toLowerCase().trim();
    const selectedNation = this.filters.nation || 'all';
    let filtered = this.countries.filter(c => {
      const matchesSearch = c.country.toLowerCase().includes(query);
      const matchesNation = selectedNation === 'all' || c.country.toLowerCase() === selectedNation;
      return matchesSearch && matchesNation;
    });

    // Apply sorting selection
    const sortBy = this.filters.sortBy;
    if (sortBy === 'sales-desc') {
      filtered.sort((a, b) => b.sales - a.sales);
    } else if (sortBy === 'sales-asc') {
      filtered.sort((a, b) => a.sales - b.sales);
    } else if (sortBy === 'stores-desc') {
      filtered.sort((a, b) => b.stores - a.stores);
    } else if (sortBy === 'regions-desc') {
      filtered.sort((a, b) => b.subRegionsCount - a.subRegionsCount);
    } else if (sortBy === 'name-asc') {
      filtered.sort((a, b) => a.country.localeCompare(b.country));
    }

    if (filtered.length === 0) {
      return `
        <div class="card glass col-12 text-center" style="grid-column: span 2; padding: var(--spacing-xl); color: var(--text-muted); font-size: 0.85rem;">
          <i data-lucide="info" style="width:24px; height:24px; margin: 0 auto 10px auto; color: var(--accent-primary);"></i>
          No matching countries found.
        </div>
      `;
    }

    return filtered.map(c => {
      const salesVal = Number(c.sales || 0);
      const targetVal = Number(c.target || salesVal * 1.1 || 1);
      const ach = Number(c.achievement || ((salesVal / targetVal) * 100)).toFixed(1);
      
      const salesFormatted = new Intl.NumberFormat(this.currencyLocale, {
        style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
      }).format(salesVal);
      
      const targetFormatted = new Intl.NumberFormat(this.currencyLocale, {
        style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
      }).format(targetVal);

      const statusColor = Number(ach) >= 90
        ? 'var(--status-success)'
        : (Number(ach) >= 80 ? 'var(--status-warning)' : 'var(--status-danger)');

      return `
        <div class="card glass animate-slide-up" style="display:flex; flex-direction:column; gap: var(--spacing-md); transition: var(--transition-normal); position:relative; overflow:visible;">
          
          <!-- Delete Country database button (top right) -->
          ${c.id ? `
            <button class="btn-delete-country" data-id="${c.id}" data-name="${c.country}" title="Delete Country from Database" style="position:absolute; top: 12px; right: 12px; background:none; border:none; color:var(--status-danger); opacity:0.6; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 5px; border-radius:var(--radius-sm);" onmouseover="this.style.opacity='1'; this.style.background='rgba(239,68,68,0.1)'" onmouseout="this.style.opacity='0.6'; this.style.background='none'">
              <i data-lucide="trash-2" style="width:13px; height:13px; stroke-width:2.5;"></i>
            </button>
          ` : ''}

          <!-- Top row -->
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm); padding-right: 20px;">
            <div>
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 0.92rem; margin: 0; color: var(--text-primary); overflow:hidden; text-overflow:ellipsis; white-space:nowrap; max-width:140px;" title="${c.country}">${c.country}</h3>
              <div style="font-size:0.62rem; color: var(--text-muted); margin-top:2px; font-weight:700; letter-spacing:0.04em;">CODE: ${c.code || 'NATION'}</div>
            </div>
            <div style="display:flex; align-items:center; gap:6px; flex-shrink:0;">
              <div style="display:flex; align-items:center; gap:3px; background: rgba(255,255,255,0.03); border:1px solid var(--border-color); border-radius:4px; padding: 2px 6px; font-size: 0.65rem; font-weight:700; color: var(--text-secondary);">
                <i data-lucide="map" style="width:10px; height:10px; color: var(--accent-primary);"></i> ${c.subRegionsCount} Regions
              </div>
              <div style="display:flex; align-items:center; gap:3px; background: rgba(255,255,255,0.03); border:1px solid var(--border-color); border-radius:4px; padding: 2px 6px; font-size: 0.65rem; font-weight:700; color: var(--text-secondary);">
                <i data-lucide="coffee" style="width:10px; height:10px; color: var(--accent-secondary);"></i> ${c.stores} Stores
              </div>
            </div>
          </div>

          <!-- Mid metrics -->
          <div style="display:grid; grid-template-columns: repeat(2, 1fr); gap: var(--spacing-sm);">
            <div>
              <div style="font-size: 0.65rem; color: var(--text-muted);">Current Sales</div>
              <div style="font-size: 0.95rem; font-weight: 800; color: var(--text-primary); margin-top:2px;">${salesFormatted}</div>
            </div>
            <div>
              <div style="font-size: 0.65rem; color: var(--text-muted);">MTD Target</div>
              <div style="font-size: 0.95rem; font-weight: 800; color: var(--text-muted); margin-top:2px;">${targetFormatted}</div>
            </div>
          </div>

          <!-- Bottom details -->
          <div style="display:flex; flex-direction:column; gap: 6px; font-size: 0.7rem; padding-bottom: var(--spacing-xs);">
            <div class="flex justify-between align-center">
              <span style="color:var(--text-muted);">Combined Orders</span>
              <span style="font-weight:700; color:var(--text-primary);">${c.orders || 0} orders</span>
            </div>
            <div class="flex justify-between align-center">
              <span style="color:var(--text-muted);">Corporate Staff</span>
              <span style="font-weight:700; color:var(--text-primary);">${c.employees || 0} staff</span>
            </div>
          </div>

          <!-- Progress bar -->
          <div>
            <div class="flex justify-between align-center mb-xs" style="font-size: 0.65rem; font-weight:600;">
              <span style="color: var(--text-muted);">Target Achievement</span>
              <span style="color: ${statusColor}; font-weight:700;">${ach}%</span>
            </div>
            <div style="background: rgba(255,255,255,0.05); border-radius: var(--radius-full); height: 6px; overflow:hidden;">
              <div style="width: ${Math.min(100, ach)}%; height: 100%; background: ${statusColor}; border-radius: var(--radius-full); transition: width 0.6s ease;"></div>
            </div>
          </div>

          <!-- View Details Button Footer -->
          <div style="display:flex; justify-content:space-between; align-items:center; border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); margin-top:2px;">
            <button class="btn-view-details" data-id="${c.id}" style="background:none; border:none; color:var(--accent-primary); font-size:0.72rem; font-weight:700; cursor:pointer; text-transform:uppercase; letter-spacing:0.02em; display:flex; align-items:center; gap:3px; padding:0; transition: color var(--transition-fast);" onmouseover="this.style.color='var(--text-primary)'" onmouseout="this.style.color='var(--accent-primary)'">
              View Details <i data-lucide="arrow-right" style="width:12px; height:12px;"></i>
            </button>
            <span style="font-size: 0.62rem; color: var(--text-muted); font-weight:700; font-family:var(--font-mono);">${c.code || 'NATION'}</span>
          </div>

        </div>
      `;
    }).join('');
  }

  showCountryDetails(c) {
    const modal = this.container.querySelector('#details-modal');
    const title = this.container.querySelector('#details-modal-title');
    const body = this.container.querySelector('#details-modal-body');
    
    if (!modal || !body) return;

    title.innerHTML = `
      <div class="flex align-center gap-xs">
        <i data-lucide="globe" style="width:20px; height:20px; color:var(--accent-primary);"></i>
        <span>${c.country} Operations Hub</span>
      </div>
    `;

    // Filter sub-regions under this country
    const subRegions = this.regionsData.filter(sr => sr.parentId === c.id);
    
    // Filter stores under this country or its sub-regions
    const subRegionIds = subRegions.map(sr => sr.id);
    const countryStores = this.allStores.filter(store => store.regionId === c.id || subRegionIds.includes(store.regionId));

    // Sales Formatting
    const salesFormatted = new Intl.NumberFormat(this.currencyLocale, {
      style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
    }).format(c.sales);
    
    const targetFormatted = new Intl.NumberFormat(this.currencyLocale, {
      style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
    }).format(c.target);

    // Regions list HTML
    const subRegionsHtml = subRegions.length === 0
      ? `<div style="font-size:0.8rem; color:var(--text-muted); padding:10px 0; text-align:center;">No active sub-regions registered under this country.</div>`
      : `
        <table style="width:100%; border-collapse:collapse; text-align:left; font-size:0.78rem;">
          <thead>
            <tr style="border-bottom:1px solid rgba(255,255,255,0.08); color:var(--text-muted); font-weight:700;">
              <th style="padding:8px 4px;">Region Name</th>
              <th style="padding:8px 4px;">Code</th>
              <th style="padding:8px 4px; text-align:right;">MTD Sales</th>
            </tr>
          </thead>
          <tbody>
            ${subRegions.map(sr => {
              // Find matching sub-region sales from regions
              const match = this.allRegionsPerformance.find(p => p.region.toLowerCase() === sr.name.toLowerCase());
              const srSales = match ? Number(match.sales || 0) : 0;
              const srSalesFmt = new Intl.NumberFormat(this.currencyLocale, {
                style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
              }).format(srSales);
              return `
                <tr style="border-bottom:1px solid rgba(255,255,255,0.03); color:var(--text-primary);">
                  <td style="padding:8px 4px; font-weight:600;">${sr.name}</td>
                  <td style="padding:8px 4px; color:var(--text-secondary); font-family:var(--font-mono); font-size:0.7rem;">${sr.code}</td>
                  <td style="padding:8px 4px; text-align:right; font-weight:700; color:var(--accent-secondary);">${srSalesFmt}</td>
                </tr>
              `;
            }).join('')}
          </tbody>
        </table>
      `;

    // Stores list HTML
    const storesHtml = countryStores.length === 0
      ? `<div style="font-size:0.8rem; color:var(--text-muted); padding:10px 0; text-align:center;">No stores registered under this country or its sub-regions.</div>`
      : `
        <table style="width:100%; border-collapse:collapse; text-align:left; font-size:0.78rem;">
          <thead>
            <tr style="border-bottom:1px solid rgba(255,255,255,0.08); color:var(--text-muted); font-weight:700;">
              <th style="padding:8px 4px;">Store Name</th>
              <th style="padding:8px 4px;">Code</th>
              <th style="padding:8px 4px;">Assigned Region</th>
              <th style="padding:8px 4px;">Contact Email</th>
            </tr>
          </thead>
          <tbody>
            ${countryStores.map(st => {
              // Find region name
              const parentReg = this.regionsData.find(r => r.id === st.regionId);
              const regName = parentReg ? parentReg.name : 'Direct Country';
              return `
                <tr style="border-bottom:1px solid rgba(255,255,255,0.03); color:var(--text-primary);">
                  <td style="padding:8px 4px; font-weight:600;">${st.name}</td>
                  <td style="padding:8px 4px; color:var(--text-secondary); font-family:var(--font-mono); font-size:0.7rem;">${st.code}</td>
                  <td style="padding:8px 4px; color:var(--text-muted); font-size:0.72rem;">${regName}</td>
                  <td style="padding:8px 4px; color:var(--text-secondary);">${st.email || 'N/A'}</td>
                </tr>
              `;
            }).join('')}
          </tbody>
        </table>
      `;

    body.innerHTML = `
      <!-- KPI stats row -->
      <div style="display:grid; grid-template-columns: repeat(3, 1fr); gap: var(--spacing-md); background: rgba(255,255,255,0.02); border:1px solid var(--border-color); border-radius: var(--radius-md); padding: var(--spacing-md);">
        <div>
          <span style="font-size:0.6rem; color:var(--text-muted); text-transform:uppercase; font-weight:700; letter-spacing:0.04em;">MTD Income</span>
          <div style="font-size:1.15rem; font-weight:800; color:var(--accent-primary); margin-top:2px;">${salesFormatted}</div>
        </div>
        <div>
          <span style="font-size:0.6rem; color:var(--text-muted); text-transform:uppercase; font-weight:700; letter-spacing:0.04em;">Sales Target</span>
          <div style="font-size:1.15rem; font-weight:800; color:var(--text-primary); margin-top:2px;">${targetFormatted}</div>
        </div>
        <div>
          <span style="font-size:0.6rem; color:var(--text-muted); text-transform:uppercase; font-weight:700; letter-spacing:0.04em;">Orders / Headcount</span>
          <div style="font-size:0.95rem; font-weight:800; color:var(--text-secondary); margin-top:4px;">${c.orders} orders · ${c.employees} staff</div>
        </div>
      </div>

      <!-- 2 column list details -->
      <div style="display:grid; grid-template-columns: repeat(2, 1fr); gap: var(--spacing-lg);">
        
        <!-- Left: Regions list -->
        <div class="card glass" style="padding: var(--spacing-md); display:flex; flex-direction:column; gap: var(--spacing-sm); border-color: rgba(255,255,255,0.03);">
          <div class="flex justify-between align-center" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:6px;">
            <h4 style="margin:0; font-size:0.85rem; font-weight:800; color:var(--text-primary); display:flex; align-items:center; gap:4px;">
              <i data-lucide="map" style="width:12px; height:12px; color:var(--accent-primary);"></i> Sub-Regions (${subRegions.length})
            </h4>
          </div>
          <div style="max-height:220px; overflow-y:auto; padding-right:4px;">
            ${subRegionsHtml}
          </div>
        </div>

        <!-- Right: Stores list -->
        <div class="card glass" style="padding: var(--spacing-md); display:flex; flex-direction:column; gap: var(--spacing-sm); border-color: rgba(255,255,255,0.03);">
          <div class="flex justify-between align-center" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:6px;">
            <h4 style="margin:0; font-size:0.85rem; font-weight:800; color:var(--text-primary); display:flex; align-items:center; gap:4px;">
              <i data-lucide="coffee" style="width:12px; height:12px; color:var(--accent-secondary);"></i> Physical Stores (${countryStores.length})
            </h4>
          </div>
          <div style="max-height:220px; overflow-y:auto; padding-right:4px;">
            ${storesHtml}
          </div>
        </div>

      </div>
    `;

    modal.style.display = 'flex';
    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#input-search-countries');
    const dateFromInput = container.querySelector('#filter-date-from');
    const dateToInput = container.querySelector('#filter-date-to');
    const sortBySelect = container.querySelector('#filter-sort-by');
    const applyFiltersBtn = container.querySelector('#btn-apply-page-filters');
    const cardsGrid = container.querySelector('#countries-cards-grid');

    const btnAddCountry = container.querySelector('#btn-add-country');
    const modal = container.querySelector('#add-country-modal');
    const btnCloseModal = container.querySelector('#btn-close-modal');
    const formAddCountry = container.querySelector('#form-add-country');
    const submitBtn = container.querySelector('#btn-submit-country');

    // Restore filter values in UI fields
    const filterNation = container.querySelector('#filter-nation');
    if (searchInput) searchInput.value = this.filters.search;
    if (dateFromInput) dateFromInput.value = this.filters.from;
    if (dateToInput) dateToInput.value = this.filters.to;
    if (sortBySelect) sortBySelect.value = this.filters.sortBy;
    if (filterNation) filterNation.value = this.filters.nation;

    // 1. Live Client-side Filters
    const updateClientFilters = () => {
      this.filters.search = searchInput ? searchInput.value : '';
      this.filters.sortBy = sortBySelect ? sortBySelect.value : 'sales-desc';
      this.filters.nation = filterNation ? filterNation.value : 'all';
      if (cardsGrid) {
        cardsGrid.innerHTML = this.renderCountryCards();
        if (window.lucide) window.lucide.createIcons();
        this.bindCardActionEvents(container, lifecycle);
      }
    };

    if (searchInput) {
      searchInput.addEventListener('input', updateClientFilters);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', updateClientFilters));
    }
    if (sortBySelect) {
      sortBySelect.addEventListener('change', updateClientFilters);
      lifecycle.onCleanup(() => sortBySelect.removeEventListener('change', updateClientFilters));
    }
    if (filterNation) {
      filterNation.addEventListener('change', updateClientFilters);
      lifecycle.onCleanup(() => filterNation.removeEventListener('change', updateClientFilters));
    }

    // 2. Server-side Date Filters Apply
    if (applyFiltersBtn) {
      const applyFilters = async () => {
        this.filters.from = dateFromInput ? dateFromInput.value : '';
        this.filters.to = dateToInput ? dateToInput.value : '';
        applyFiltersBtn.disabled = true;
        applyFiltersBtn.innerHTML = '<span style="animation: spin 1s linear infinite; display: inline-block;">↺</span> Applying';
        try {
          await this.loadAndRender();
        } finally {
          applyFiltersBtn.disabled = false;
          applyFiltersBtn.innerHTML = '<i data-lucide="filter" style="width:12px; height:12px;"></i> Apply';
          if (window.lucide) window.lucide.createIcons();
        }
      };
      applyFiltersBtn.addEventListener('click', applyFilters);
      lifecycle.onCleanup(() => applyFiltersBtn.removeEventListener('click', applyFilters));
    }

    // 3. Open Add Country Modal
    if (btnAddCountry && modal) {
      const openModal = () => {
        modal.style.display = 'flex';
      };
      btnAddCountry.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddCountry.removeEventListener('click', openModal));
    }

    // 4. Close Add Country Modal
    if (btnCloseModal && modal) {
      const closeModal = () => {
        modal.style.display = 'none';
      };
      btnCloseModal.addEventListener('click', closeModal);
      lifecycle.onCleanup(() => btnCloseModal.removeEventListener('click', closeModal));

      const backdropClose = (e) => {
        if (e.target === modal) {
          modal.style.display = 'none';
        }
      };
      modal.addEventListener('click', backdropClose);
      lifecycle.onCleanup(() => modal.removeEventListener('click', backdropClose));
    }

    // 5. Create Country Form Submit
    if (formAddCountry && modal) {
      const handleSubmit = async (e) => {
        e.preventDefault();

        const name = container.querySelector('#modal-input-name').value.trim();
        const target = Number(container.querySelector('#modal-input-target').value);

        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.textContent = 'Registering…';
        }

        try {
          const cleanCode = name.toUpperCase().replace(/[^A-Z0-9]/g, '_') + '_' + Date.now();
          
          const response = await apiClient.post('/api/v1/regions', {
            code: cleanCode,
            name: name,
            description: `National country operations for ${name}`,
            companyId: 1,
            parentId: null
          });

          if (response && response.success) {
            await dashboardService.getDashboardOverview();
            modal.style.display = 'none';
            formAddCountry.reset();
            await this.loadAndRender();
            notificationStore.success(`Country '${name}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by backend API.');
          }
        } catch (err) {
          logger.error('NationalPage', 'Failed to register country:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Country';
          }
        }
      };

      formAddCountry.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddCountry.removeEventListener('submit', handleSubmit));
    }

    // 6. Close Details Modal & Backdrop
    const detailsModal = container.querySelector('#details-modal');
    const btnCloseDetails = container.querySelector('#btn-close-details-modal');
    if (btnCloseDetails && detailsModal) {
      const closeDetails = () => { detailsModal.style.display = 'none'; };
      btnCloseDetails.addEventListener('click', closeDetails);
      lifecycle.onCleanup(() => btnCloseDetails.removeEventListener('click', closeDetails));

      const backdropCloseDetails = (e) => {
        if (e.target === detailsModal) {
          detailsModal.style.display = 'none';
        }
      };
      detailsModal.addEventListener('click', backdropCloseDetails);
      lifecycle.onCleanup(() => detailsModal.removeEventListener('click', backdropCloseDetails));
    }

    this.bindCardActionEvents(container, lifecycle);
  }

  bindCardActionEvents(container, lifecycle) {
    // 1. Delete Country Action
    const deleteButtons = container.querySelectorAll('.btn-delete-country');
    deleteButtons.forEach(btn => {
      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const name = btn.getAttribute('data-name');
        
        if (confirm(`Are you sure you want to delete the country "${name}" from the database?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/regions/${id}`);
            if (res && res.success) {
              notificationStore.success(`Country "${name}" deleted from database.`);
              await dashboardService.getDashboardOverview();
              await this.loadAndRender();
            } else {
              throw new Error(res.message || 'Operation rejected by backend API.');
            }
          } catch (err) {
            logger.error('NationalPage', `Failed to delete country ${name}:`, err);
            notificationStore.danger(`Deletion failed: ${err.message}`);
          }
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });

    // 2. View Country Details Action
    const viewDetailBtns = container.querySelectorAll('.btn-view-details');
    viewDetailBtns.forEach(btn => {
      const id = Number(btn.getAttribute('data-id'));
      const c = this.countries.find(item => item.id === id);
      if (c) {
        const handleView = (e) => {
          e.stopPropagation();
          this.showCountryDetails(c);
        };
        btn.addEventListener('click', handleView);
        lifecycle.onCleanup(() => btn.removeEventListener('click', handleView));
      }
    });
  }
}
