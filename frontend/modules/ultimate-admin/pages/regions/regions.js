/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : regions.js
 * Purpose           : Frontend page component for Regions Management UI
 * Version           : 0.0.4
 ******************************************************************************/

import { dashboardService } from '../../../../services/dashboard/DashboardService.js';
import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { authStore } from '../../../../store/authStore.js';

export default class RegionsPage {
  constructor() {
    this.regions = [];
    this.countries = [];
    this.filters = {
      search: '',
      countryId: '',
      regionId: '',
      from: '',
      to: '',
      sortBy: 'sales-desc'
    };
  }

  async mount(container, lifecycle) {
    logger.info('RegionsPage', 'Loading regions management workspace...');
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

      // Fetch current logged in user details to filter by country if the role is nationalAdmin
      const meRes = await apiClient.get('/api/v1/auth/me');
      const currentUser = (meRes && meRes.success) ? meRes.data : null;
      const userCountry = currentUser ? currentUser.country : null;
      const activeRole = authStore.getRole();

      // Filter countries and sub-regions
      const allCountries = backendRegions.filter(br => br.parentId === null || br.parentId === undefined);
      let childRegions = backendRegions.filter(br => br.parentId !== null && br.parentId !== undefined);

      if (activeRole === 'nationalAdmin' && userCountry) {
        this.countries = allCountries.filter(c => c.name && userCountry && (c.name.toLowerCase().includes(userCountry.toLowerCase()) || userCountry.toLowerCase().includes(c.name.toLowerCase())));
        const userCountryObj = allCountries.find(c => c.name && userCountry && (c.name.toLowerCase().includes(userCountry.toLowerCase()) || userCountry.toLowerCase().includes(c.name.toLowerCase())));
        if (userCountryObj) {
          childRegions = childRegions.filter(r => r.parentId === userCountryObj.id);
        }
      } else {
        this.countries = allCountries;
      }
      
      const rawSubRegions = (overview && overview.subRegionalPerformance) ? overview.subRegionalPerformance : [];

      this.regions = childRegions.map(br => {
        const perf = rawSubRegions.find(or => or.region && br.name && or.region.toLowerCase() === br.name.toLowerCase());
        return {
          id: br.id,
          region: br.name,
          code: br.code,
          parentId: br.parentId,
          parentName: br.parentName || 'Unknown Country',
          description: br.description || '',
          sales: perf ? Number(perf.sales || 0) : 0,
          target: perf ? Number(perf.target || 0) : 0,
          achievement: perf ? Number(perf.achievement || 0) : 0,
          stores: perf ? Number(perf.stores || 0) : 0,
          employees: perf ? Number(perf.employees || 0) : 0,
          orders: perf ? Number(perf.orders || 0) : 0
        };
      });

      this.render(this.container);
      this.bindEvents(this.container, this.lifecycle);
    } catch (err) {
      logger.error('RegionsPage', 'Failed to load and render regions data:', err);
      notificationStore.danger('Failed to sync with database regions.');
    }
  }

  render(container) {
    const totalRegions = this.regions.length;
    const totalSales = this.regions.reduce((acc, r) => acc + Number(r.sales || 0), 0);
    
    let avgAchievement = '0.0';
    const regionsWithSales = this.regions.filter(r => r.sales > 0);
    if (regionsWithSales.length > 0) {
      avgAchievement = (regionsWithSales.reduce((acc, r) => acc + Number(r.achievement || 0), 0) / regionsWithSales.length).toFixed(1);
    }
    const totalStores = this.regions.reduce((acc, r) => acc + Number(r.stores || 0), 0);

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

    const formattedSales = totalSales > 0
      ? new Intl.NumberFormat(locale, {
          style: 'currency', currency: currencyCode, maximumFractionDigits: 0
        }).format(totalSales)
      : 'NA/DB';

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
    const segmentsSvg = this.regions.map((r, i) => {
      const pct = totalSales > 0 ? (Number(r.sales || 0) / totalSales) * 100 : 0;
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

    const legendListHtml = this.regions.map((r, i) => {
      const pct = totalSales > 0 ? ((Number(r.sales || 0) / totalSales) * 100).toFixed(1) : '0.0';
      const stroke = colors[i % colors.length];
      
      const salesVal = Number(r.sales || 0);
      const salesFormatted = salesVal > 0
        ? new Intl.NumberFormat(locale, {
            style: 'currency', currency: currencyCode, maximumFractionDigits: 0
          }).format(salesVal)
        : 'NA/DB';

      return `
        <div class="flex justify-between align-center" style="background: rgba(255,255,255,0.01); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 5px 8px;">
          <div class="flex align-center gap-xs" style="min-width:0; flex-grow:1;">
            <span style="width: 8px; height: 8px; border-radius: 50%; background: ${stroke}; flex-shrink:0;"></span>
            <span style="font-weight: 600; color: var(--text-primary); overflow:hidden; text-overflow:ellipsis; white-space:nowrap; font-size:0.7rem;">${r.region}</span>
          </div>
          <span style="font-weight: 700; color: var(--text-secondary); flex-shrink:0; margin-left: 8px; font-size:0.68rem;">${salesFormatted}${totalSales > 0 && salesVal > 0 ? ` (${pct}%)` : ''}</span>
        </div>
      `;
    }).join('');

    container.innerHTML = `
      <div style="display:flex; flex-direction:column; gap: var(--spacing-lg); max-width: 1200px; margin: 0 auto; padding-bottom: var(--spacing-xxl);">
        
        <!-- Header row -->
        <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
          <div>
            <div class="flex align-center gap-xs" style="color: var(--accent-primary); font-weight: 700; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px;">
              <i data-lucide="map-pin" style="width: 14px; height: 14px;"></i> Administration
            </div>
            <h1 style="font-family: var(--font-display); font-weight: 800; font-size: 1.8rem; margin: 0; color: var(--text-primary); letter-spacing: -0.01em;">Regions Management</h1>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0; margin-top: 4px;">Manage territory definitions, franchise targets, and operational metrics.</p>
          </div>
          <button id="btn-add-region" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 18px; border-radius: var(--radius-md); border:none; display:flex; align-items:center; gap:6px; cursor:pointer;">
            <i data-lucide="plus" style="width:16px; height:16px;"></i> Add Region
          </button>
        </div>

        <!-- KPI summary grid -->
        <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap: var(--spacing-md);">
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-primary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Territories</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalRegions > 0 ? totalRegions : 'NA/DB'}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-secondary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Combined Revenue</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${formattedSales}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-success);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Avg Achievement</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: ${Number(avgAchievement) > 0 ? 'var(--status-success)' : 'var(--text-muted)'}; margin-top: 6px;">${Number(avgAchievement) > 0 ? `${avgAchievement}%` : 'NA/DB'}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-warning);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Franchise Stores</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalStores > 0 ? totalStores : 'NA/DB'}</span>
          </div>
        </div>

        <!-- 2 Column Workspace -->
        <div style="display:grid; grid-template-columns: minmax(0, 7.8fr) minmax(0, 4.2fr); gap: var(--spacing-lg);">
          
          <!-- Left Column: Search & Filter Ribbon + Regions Cards Grid -->
          <div style="display:flex; flex-direction:column; gap: var(--spacing-md); min-width:0;">
            <!-- Filter Bar -->
            <div class="card glass flex flex-col gap-md" style="padding: var(--spacing-md); border-color: rgba(255,255,255,0.03); background: rgba(255,255,255,0.01);">
              <!-- Top Row: Search Name, Country Select & Sort By -->
              <div class="flex gap-md align-center flex-wrap" style="width: 100%;">
                <div style="display:flex; align-items:center; gap: var(--spacing-xs); flex-grow: 1; background: rgba(0,0,0,0.15); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 6px 12px;">
                  <i data-lucide="search" style="width:14px; height:14px; color: var(--text-muted);"></i>
                  <input id="input-search-regions" type="text" placeholder="Search region names..." style="background:none; border:none; outline:none; color:var(--text-primary); font-size:0.78rem; width:100%;" />
                </div>
                <div class="flex align-center gap-xs" style="flex-shrink:0;">
                  <label style="font-size: 0.68rem; color: var(--text-muted); font-weight:700; white-space:nowrap; text-transform:uppercase; letter-spacing:0.04em;">Country</label>
                  <select id="filter-parent-country" style="background: rgba(0,0,0,0.25); color: var(--text-primary); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 5px 8px; font-size: 0.75rem; font-weight: 600; outline:none; cursor:pointer; min-width: 130px;">
                    <option value="">All Countries</option>
                    ${this.countries.map(c => `<option value="${c.id}">${c.name}</option>`).join('')}
                  </select>
                </div>
                <div class="flex align-center gap-xs" style="flex-shrink:0;">
                  <label style="font-size: 0.68rem; color: var(--text-muted); font-weight:700; white-space:nowrap; text-transform:uppercase; letter-spacing:0.04em;">Region</label>
                  <select id="filter-region-id" style="background: rgba(0,0,0,0.25); color: var(--text-primary); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 5px 8px; font-size: 0.75rem; font-weight: 600; outline:none; cursor:pointer; min-width: 130px;">
                    <option value="">All Regions</option>
                    ${this.regions.map(r => `<option value="${r.id}">${r.region}</option>`).join('')}
                  </select>
                </div>
                <div class="flex align-center gap-xs" style="flex-shrink:0;">
                  <label style="font-size: 0.68rem; color: var(--text-muted); font-weight:700; white-space:nowrap; text-transform:uppercase; letter-spacing:0.04em;">Sort By</label>
                  <select id="filter-sort-by" style="background: rgba(0,0,0,0.25); color: var(--text-primary); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 5px 8px; font-size: 0.75rem; font-weight: 600; outline:none; cursor:pointer; min-width: 135px;">
                    <option value="sales-desc">Highest Revenue</option>
                    <option value="sales-asc">Lowest Revenue</option>
                    <option value="stores-desc">Most Stores</option>
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
            
            <!-- Grid list of region cards -->
            <div id="regions-cards-grid" style="display:grid; grid-template-columns: repeat(2, 1fr); gap: var(--spacing-md);">
              ${this.renderRegionCards()}
            </div>
          </div>

          <!-- Right Column: Income Analytics & Donut Circle Graph -->
          <div style="display:flex; flex-direction:column; gap: var(--spacing-md); min-width:0;">
            <div class="card glass" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05); display:flex; flex-direction:column; gap: var(--spacing-md); position: sticky; top: var(--spacing-lg);">
              <h3 style="font-family: var(--font-display); font-weight:800; font-size:0.95rem; margin:0; color:var(--text-primary); border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:10px;">Territory Income Share</h3>
              
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

        <!-- Add Region Modal -->
        <div id="add-region-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.7); z-index:99999; align-items:center; justify-content:center; padding: var(--spacing-lg); backdrop-filter: blur(4px);">
          <div class="card glass" style="width:100%; max-width: 450px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.5);">
            <div class="flex justify-between align-center mb-md" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.15rem; margin:0; color:var(--text-primary);">Create New Region</h3>
              <button id="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.25rem; line-height:1;">&times;</button>
            </div>
            
            <form id="form-add-region" style="display:flex; flex-direction:column; gap: var(--spacing-md); text-align:left;">
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.7rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Country</label>
                <select id="modal-input-country" required style="padding:10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.25); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; cursor:pointer;">
                  <option value="">-- Select Country --</option>
                  ${this.countries.map(c => `<option value="${c.id}">${c.name}</option>`).join('')}
                </select>
              </div>
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.7rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Region Name</label>
                <input id="modal-input-name" type="text" placeholder="e.g. Occitanie" required style="padding:10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.7rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Target Sales (â‚¬)</label>
                <input id="modal-input-target" type="number" placeholder="e.g. 50000" required style="padding:10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>
              <div class="form-group flex flex-col gap-xs" style="display:none;">
                <input id="modal-input-stores" type="number" value="0" />
                <input id="modal-input-employees" type="number" value="0" />
              </div>
              
              <button id="btn-submit-region" type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.8rem; padding:12px; margin-top:var(--spacing-sm); border:none; border-radius:var(--radius-md); cursor:pointer;">
                Register Region
              </button>
            </form>
          </div>
        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  renderRegionCards() {
    const query = (this.filters.search || '').toLowerCase().trim();
    const countryFilter = this.filters.countryId;
    const regionFilter = this.filters.regionId;

    let filtered = this.regions.filter(r => r.region && r.region.toLowerCase().includes(query));

    if (countryFilter) {
      filtered = filtered.filter(r => String(r.parentId) === String(countryFilter));
    }

    if (regionFilter) {
      filtered = filtered.filter(r => String(r.id) === String(regionFilter));
    }

    // Apply sorting
    const sortBy = this.filters.sortBy;
    if (sortBy === 'sales-desc') {
      filtered.sort((a, b) => b.sales - a.sales);
    } else if (sortBy === 'sales-asc') {
      filtered.sort((a, b) => a.sales - b.sales);
    } else if (sortBy === 'stores-desc') {
      filtered.sort((a, b) => b.stores - a.stores);
    } else if (sortBy === 'name-asc') {
      filtered.sort((a, b) => (a.region || '').localeCompare(b.region || ''));
    }

    if (filtered.length === 0) {
      return `
        <div class="card glass col-12 text-center" style="grid-column: span 2; padding: var(--spacing-xl); color: var(--text-muted); font-size: 0.85rem;">
          <i data-lucide="info" style="width:24px; height:24px; margin: 0 auto 10px auto; color: var(--accent-primary);"></i>
          No matching regions found.
        </div>
      `;
    }

    return filtered.map(r => {
      const salesVal = Number(r.sales || 0);
      const targetVal = Number(r.target || 0);
      const ach = (salesVal > 0 && targetVal > 0) ? ((salesVal / targetVal) * 100).toFixed(1) : 'NA/DB';
      
      const salesFormatted = salesVal > 0
        ? new Intl.NumberFormat(this.currencyLocale, {
            style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
          }).format(salesVal)
        : 'NA/DB';
      
      const targetFormatted = targetVal > 0
        ? new Intl.NumberFormat(this.currencyLocale, {
            style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
          }).format(targetVal)
        : 'NA/DB';

      const statusColor = ach === 'NA/DB'
        ? 'var(--text-muted)'
        : (Number(ach) >= 90
          ? 'var(--status-success)'
          : (Number(ach) >= 80 ? 'var(--status-warning)' : 'var(--status-danger)'));

      return `
        <div class="card glass animate-slide-up" style="display:flex; flex-direction:column; gap: var(--spacing-md); transition: var(--transition-normal); position:relative; overflow:visible;">
          
          <!-- Delete Region database button (top right) -->
          ${(r.id && authStore.getRole() !== 'nationalAdmin') ? `
            <button class="btn-delete-region" data-id="${r.id}" data-name="${r.region}" title="Delete Region from Database" style="position:absolute; top: 12px; right: 12px; background:none; border:none; color:var(--status-danger); opacity:0.6; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 5px; border-radius:var(--radius-sm);" onmouseover="this.style.opacity='1'; this.style.background='rgba(239,68,68,0.1)'" onmouseout="this.style.opacity='0.6'; this.style.background='none'">
              <i data-lucide="trash-2" style="width:13px; height:13px; stroke-width:2.5;"></i>
            </button>
          ` : ''}

          <!-- Top row -->
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm); padding-right: 20px;">
            <div>
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 0.92rem; margin: 0; color: var(--text-primary); overflow:hidden; text-overflow:ellipsis; white-space:nowrap; max-width:140px;" title="${r.region}">${r.region}</h3>
              <div style="font-size:0.62rem; color: var(--text-muted); margin-top:2px; font-weight:700; letter-spacing:0.04em;">COUNTRY: ${r.parentName.toUpperCase()} · CODE: ${r.code || 'ZONE'}</div>
            </div>
            <div style="display:flex; align-items:center; gap:4px; background: rgba(255,255,255,0.03); border:1px solid var(--border-color); border-radius:4px; padding: 2px 8px; font-size: 0.65rem; font-weight:700; color: var(--text-secondary); flex-shrink:0;">
              <i data-lucide="coffee" style="width:11px; height:11px; color: var(--accent-secondary);"></i> ${r.stores > 0 ? `${r.stores} Stores` : '0 Stores'}
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
          <div style="display:flex; flex-direction:column; gap: 6px; font-size: 0.7rem; border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm);">
            <div class="flex justify-between align-center">
              <span style="color:var(--text-muted);">Orders Volume</span>
              <span style="font-weight:700; color:var(--text-primary);">${r.orders > 0 ? `${r.orders} orders` : 'NA/DB'}</span>
            </div>
            <div class="flex justify-between align-center">
              <span style="color:var(--text-muted);">Active Staff</span>
              <span style="font-weight:700; color:var(--text-primary);">${r.employees > 0 ? `${r.employees} staff` : 'NA/DB'}</span>
            </div>
          </div>

          <!-- Progress bar -->
          <div>
            <div class="flex justify-between align-center mb-xs" style="font-size: 0.65rem; font-weight:600;">
              <span style="color: var(--text-muted);">Target Achievement</span>
              <span style="color: ${statusColor}; font-weight:700;">${ach === 'NA/DB' ? 'NA/DB' : `${ach}%`}</span>
            </div>
            <div style="background: rgba(255,255,255,0.05); border-radius: var(--radius-full); height: 6px; overflow:hidden;">
              <div style="width: ${ach === 'NA/DB' ? '0%' : `${Math.min(100, Number(ach))}%`}; height: 100%; background: ${statusColor}; border-radius: var(--radius-full); transition: width 0.6s ease;"></div>
            </div>
          </div>

        </div>
      `;
    }).join('');
  }

  bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#input-search-regions');
    const countryFilter = container.querySelector('#filter-parent-country');
    const regionFilterSelect = container.querySelector('#filter-region-id');
    const sortBySelect = container.querySelector('#filter-sort-by');
    const dateFromInput = container.querySelector('#filter-date-from');
    const dateToInput = container.querySelector('#filter-date-to');
    const applyFiltersBtn = container.querySelector('#btn-apply-page-filters');
    const cardsGrid = container.querySelector('#regions-cards-grid');

    const btnAddRegion = container.querySelector('#btn-add-region');
    const modal = container.querySelector('#add-region-modal');
    const btnCloseModal = container.querySelector('#btn-close-modal');
    const formAddRegion = container.querySelector('#form-add-region');
    const submitBtn = container.querySelector('#btn-submit-region');

    // Restore filter values in UI fields
    if (searchInput) searchInput.value = this.filters.search;
    if (countryFilter) countryFilter.value = this.filters.countryId;
    if (regionFilterSelect) regionFilterSelect.value = this.filters.regionId;
    if (dateFromInput) dateFromInput.value = this.filters.from;
    if (dateToInput) dateToInput.value = this.filters.to;
    if (sortBySelect) sortBySelect.value = this.filters.sortBy;

    // Helper to dynamically sync region dropdown options when country filter changes
    const updateRegionOptions = () => {
      if (!regionFilterSelect) return;
      const selectedCountryId = countryFilter ? countryFilter.value : '';
      
      // Prevent redundant rebuilds if the country context did not change
      if (regionFilterSelect.dataset.lastCountryId === selectedCountryId) {
        return;
      }
      regionFilterSelect.dataset.lastCountryId = selectedCountryId;
      
      let filteredRegionsForDropdown = this.regions;
      if (selectedCountryId) {
        filteredRegionsForDropdown = this.regions.filter(r => String(r.parentId) === String(selectedCountryId));
      }
      regionFilterSelect.innerHTML = `<option value="">All Regions</option>` + 
        filteredRegionsForDropdown.map(r => `<option value="${r.id}">${r.region}</option>`).join('');
      
      if (this.filters.regionId && !filteredRegionsForDropdown.some(r => String(r.id) === String(this.filters.regionId))) {
        this.filters.regionId = '';
        regionFilterSelect.value = '';
      } else {
        regionFilterSelect.value = this.filters.regionId;
      }
    };

    // Initialize region options mapping
    updateRegionOptions();

    // 1. Live Client-side Filters
    const updateClientFilters = () => {
      this.filters.search = searchInput ? searchInput.value : '';
      this.filters.countryId = countryFilter ? countryFilter.value : '';
      
      updateRegionOptions();
      
      this.filters.regionId = regionFilterSelect ? regionFilterSelect.value : '';
      this.filters.sortBy = sortBySelect ? sortBySelect.value : 'sales-desc';
      
      if (cardsGrid) {
        cardsGrid.innerHTML = this.renderRegionCards();
        if (window.lucide) window.lucide.createIcons();
        this.bindDeleteEvents(container, lifecycle);
      }
    };

    if (searchInput) {
      searchInput.addEventListener('input', updateClientFilters);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', updateClientFilters));
    }
    if (countryFilter) {
      countryFilter.addEventListener('change', updateClientFilters);
      lifecycle.onCleanup(() => countryFilter.removeEventListener('change', updateClientFilters));
    }
    if (regionFilterSelect) {
      regionFilterSelect.addEventListener('change', updateClientFilters);
      lifecycle.onCleanup(() => regionFilterSelect.removeEventListener('change', updateClientFilters));
    }
    if (sortBySelect) {
      sortBySelect.addEventListener('change', updateClientFilters);
      lifecycle.onCleanup(() => sortBySelect.removeEventListener('change', updateClientFilters));
    }

    // 2. Server-side Date Filters Apply
    if (applyFiltersBtn) {
      const applyFilters = async () => {
        this.filters.from = dateFromInput ? dateFromInput.value : '';
        this.filters.to = dateToInput ? dateToInput.value : '';
        applyFiltersBtn.disabled = true;
        applyFiltersBtn.innerHTML = '<span style="animation: spin 1s linear infinite; display: inline-block;">â†º</span> Applying';
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

    // 3. Open Add Region Modal
    if (btnAddRegion && modal) {
      const openModal = () => {
        modal.style.display = 'flex';
      };
      btnAddRegion.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddRegion.removeEventListener('click', openModal));
    }

    // 4. Close Add Region Modal
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

    // 5. Create Region Form Submit
    if (formAddRegion && modal) {
      const handleSubmit = async (e) => {
        e.preventDefault();

        const name = container.querySelector('#modal-input-name').value.trim();
        const target = Number(container.querySelector('#modal-input-target').value);
        const countryId = container.querySelector('#modal-input-country').value;

        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.textContent = 'Registeringâ€¦';
        }

        try {
          const cleanCode = name.toUpperCase().replace(/[^A-Z0-9]/g, '_') + '_' + Date.now();
          
          const response = await apiClient.post('/api/v1/regions', {
            code: cleanCode,
            name: name,
            description: `Franchise territory operations for ${name}`,
            companyId: 1,
            parentId: countryId ? Number(countryId) : null
          });

          if (response && response.success) {
            await dashboardService.getDashboardOverview();
            modal.style.display = 'none';
            formAddRegion.reset();
            await this.loadAndRender();
            notificationStore.success(`Territory region '${name}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by backend API.');
          }
        } catch (err) {
          logger.error('RegionsPage', 'Failed to register region:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Region';
          }
        }
      };

      formAddRegion.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddRegion.removeEventListener('submit', handleSubmit));
    }

    this.bindDeleteEvents(container, lifecycle);
  }

  bindDeleteEvents(container, lifecycle) {
    const deleteButtons = container.querySelectorAll('.btn-delete-region');
    deleteButtons.forEach(btn => {
      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const name = btn.getAttribute('data-name');
        
        if (confirm(`Are you sure you want to delete the region "${name}" from the database?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/regions/${id}`);
            if (res && res.success) {
              notificationStore.success(`Region "${name}" deleted from database.`);
              await dashboardService.getDashboardOverview();
              await this.loadAndRender();
            } else {
              throw new Error(res.message || 'Operation rejected by backend API.');
            }
          } catch (err) {
            logger.error('RegionsPage', `Failed to delete region ${name}:`, err);
            notificationStore.danger(`Deletion failed: ${err.message}`);
          }
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });
  }
}
