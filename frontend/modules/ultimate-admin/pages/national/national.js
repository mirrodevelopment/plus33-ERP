/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin â€” National Management
 * File              : national.js
 * Purpose           : Controller component for National Management UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/ultimate-admin/national/national.html
 * Related CSS       : frontend/modules/ultimate-admin/national/national.css
 * Related APIs      : GET /api/v1/regions
 *                     POST /api/v1/regions
 *                     DELETE /api/v1/regions/:id
 *                     GET /api/v1/stores
 *                     GET /api/v1/dashboard/overview
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in national.html â€” this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor â†’ mount â†’ loadTemplate â†’ loadData â†’ render â†’ bindEvents â†’ destroy
 ******************************************************************************/

import { htmlLoader } from '../../../../core/htmlLoader.js';
import { dashboardService } from '../../../../services/dashboard/DashboardService.js';
import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';

/** Path to the national HTML template */
const TEMPLATE_URL = 'modules/ultimate-admin/pages/national/national.html';

/** Colors for revenue donut segments */
const DONUT_COLORS = [
  'var(--accent-primary)',
  'var(--accent-secondary)',
  'var(--status-info)',
  'var(--status-success)',
  'var(--status-warning)',
  '#a855f7',
  '#ec4899',
  '#f43f5e'
];

export default class NationalPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

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
      nation: 'all',
      dateRangePreset: 'this-month'
    };
    
    // Currency configuration defaults
    this.currencyCode = 'EUR';
    this.currencyLocale = 'fr-FR';
    this.currencySymbol = 'â‚¬';
    
    this.overview = null;
    this.container = null;
    this.lifecycle = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the national page component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('NationalPage', 'Loading national management workspace...');
    this.container = container;
    this.lifecycle = lifecycle;

    // Dynamically load national CSS
    this._loadCss();

    // Initialize default dates if empty (default to this month to date)
    this._initDefaultDates();

    // 1. Load template HTML
    await this._loadTemplate(container);

    // 2. Fetch data from backend APIs
    await this._loadData();

    // 3. Render loaded data into the DOM
    await this._render(container);

    // 4. Bind event listeners
    this._bindEvents(container, lifecycle);
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
          target: perf ? Number(perf.target || 0) : 0,
          achievement: perf ? Number(perf.achievement || 0) : 0,
          stores: perf ? Number(perf.stores || 0) : 0,
          employees: perf ? Number(perf.employees || 0) : 0,
          orders: perf ? Number(perf.orders || 0) : 0
        };
      });

      this.overview = overview;
      this._resolveCurrencySettings();

    } catch (err) {
      logger.error('NationalPage', 'Failed to load national data:', err);
      notificationStore.danger('Failed to sync with database countries.');
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  /**
   * Render dynamic fields, KPI cards, filter list items, donut charts and legend lists.
   * @param {HTMLElement} container
   */
  async _render(container) {
    this._populateKpiSummary(container);
    this._populateFilterDropdowns(container);
    this._renderCountryCardsList(container);
    this._renderRevenueChart(container);
    await this._renderFinancialOverviewWidget(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  /**
   * Bind event listeners for search input, dropdown filter, and modal triggers.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindEvents(container, lifecycle) {
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

    // Restore filter values in UI fields
    const filterNation = container.querySelector('#filter-nation');
    const rangeSelect = container.querySelector('#filter-date-range');
    const customElements = container.querySelectorAll('.filter-date-custom');

    if (searchInput) searchInput.value = this.filters.search;
    if (dateFromInput) dateFromInput.value = this.filters.from;
    if (dateToInput) dateToInput.value = this.filters.to;
    if (sortBySelect) sortBySelect.value = this.filters.sortBy;
    if (filterNation) filterNation.value = this.filters.nation;
    if (rangeSelect) rangeSelect.value = this.filters.dateRangePreset;

    // Toggle custom date range fields visibility on load based on active preset
    const isCustomInit = this.filters.dateRangePreset === 'custom';
    customElements.forEach(el => el.classList.toggle('hidden', !isCustomInit));

    // 1. Live Client-side Filters
    const updateClientFilters = () => {
      this.filters.search = searchInput ? searchInput.value : '';
      this.filters.sortBy = sortBySelect ? sortBySelect.value : 'sales-desc';
      this.filters.nation = filterNation ? filterNation.value : 'all';
      this._renderCountryCardsList(container, lifecycle);
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

    // 2. Date Preset Selector Handler
    if (rangeSelect) {
      const handleRangeChange = async () => {
        this.filters.dateRangePreset = rangeSelect.value;
        const isCustom = rangeSelect.value === 'custom';
        customElements.forEach(el => el.classList.toggle('hidden', !isCustom));
        this._updateDatesFromPreset();

        if (dateFromInput) dateFromInput.value = this.filters.from;
        if (dateToInput) dateToInput.value = this.filters.to;

        if (!isCustom) {
          await this.loadAndRender(container, lifecycle);
        }
      };
      rangeSelect.addEventListener('change', handleRangeChange);
      lifecycle.onCleanup(() => rangeSelect.removeEventListener('change', handleRangeChange));
    }

    // 3. Server-side Custom Date Filters Apply
    if (applyFiltersBtn) {
      const applyFilters = async () => {
        this.filters.from = dateFromInput ? dateFromInput.value : '';
        this.filters.to = dateToInput ? dateToInput.value : '';
        applyFiltersBtn.disabled = true;
        applyFiltersBtn.innerHTML = '<span style="animation: spin 1s linear infinite; display: inline-block;">â†º</span> Applying';
        try {
          await this.loadAndRender(container, lifecycle);
        } finally {
          applyFiltersBtn.disabled = false;
          applyFiltersBtn.innerHTML = '<i data-lucide="filter" style="width:12px; height:12px;"></i> Apply';
          if (window.lucide) window.lucide.createIcons();
        }
      };
      applyFiltersBtn.addEventListener('click', applyFilters);
      lifecycle.onCleanup(() => applyFiltersBtn.removeEventListener('click', applyFilters));
    }

    // 4. Refresh Button â€” re-fetch data and re-render cards
    const refreshBtn = container.querySelector('#btn-refresh-cards');
    if (refreshBtn) {
      const handleRefresh = async () => {
        if (refreshBtn.disabled) return;
        refreshBtn.disabled = true;
        refreshBtn.classList.add('btn-refresh-cards--spinning');
        try {
          await this.loadAndRender(container, lifecycle);
        } finally {
          refreshBtn.disabled = false;
          refreshBtn.classList.remove('btn-refresh-cards--spinning');
          if (window.lucide) window.lucide.createIcons();
        }
      };
      refreshBtn.addEventListener('click', handleRefresh);
      lifecycle.onCleanup(() => refreshBtn.removeEventListener('click', handleRefresh));
    }

    if (btnAddCountry && modal) {
      const openModal = () => {
        modal.removeAttribute('hidden');
      };
      btnAddCountry.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddCountry.removeEventListener('click', openModal));
    }

    // 4. Close Add Country Modal
    if (btnCloseModal && modal) {
      const closeModal = () => {
        modal.setAttribute('hidden', '');
      };
      btnCloseModal.addEventListener('click', closeModal);
      lifecycle.onCleanup(() => btnCloseModal.removeEventListener('click', closeModal));

      const backdropClose = (e) => {
        if (e.target === modal) {
          modal.setAttribute('hidden', '');
        }
      };
      modal.addEventListener('click', backdropClose);
      lifecycle.onCleanup(() => modal.removeEventListener('click', backdropClose));
    }

    // 5. Create Country Form Submit
    if (formAddCountry && modal) {
      const submitBtn = container.querySelector('#btn-submit-country');
      const handleSubmit = async (e) => {
        e.preventDefault();

        const name = container.querySelector('#modal-input-name').value.trim();
        const target = Number(container.querySelector('#modal-input-target').value);

        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.textContent = 'Registeringâ€¦';
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
            modal.setAttribute('hidden', '');
            formAddCountry.reset();
            await this.loadAndRender(container, lifecycle);
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
      const closeDetails = () => { detailsModal.setAttribute('hidden', ''); };
      btnCloseDetails.addEventListener('click', closeDetails);
      lifecycle.onCleanup(() => btnCloseDetails.removeEventListener('click', closeDetails));

      const backdropCloseDetails = (e) => {
        if (e.target === detailsModal) {
          detailsModal.setAttribute('hidden', '');
        }
      };
      detailsModal.addEventListener('click', backdropCloseDetails);
      lifecycle.onCleanup(() => detailsModal.removeEventListener('click', backdropCloseDetails));
    }

    // Bind action buttons inside list items
    this._bindCardActionEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    logger.debug('NationalPage', 'Workspace destroyed.');
  }

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): loadAndRender
  // ---------------------------------------------------------------------------

  async loadAndRender(container = this.container, lifecycle = this.lifecycle) {
    await this._loadData();
    await this._render(container);
    this._bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Rendering Sub-routines
  // ---------------------------------------------------------------------------

  /**
   * Populate top metric grid row.
   * @param {HTMLElement} container
   */
  _populateKpiSummary(container) {
    const kpis = this.overview?.kpis || {};
    const totalCountriesVal = kpis.totalCountries !== undefined && kpis.totalCountries !== null ? kpis.totalCountries : this.countries.length;
    const totalSalesVal = kpis.totalSales !== undefined && kpis.totalSales !== null ? Number(kpis.totalSales) : 0;
    const totalStoresVal = kpis.totalStores !== undefined && kpis.totalStores !== null ? Number(kpis.totalStores) : 0;
    const totalRegionsVal = kpis.totalRegions !== undefined && kpis.totalRegions !== null ? Number(kpis.totalRegions) : 0;
    const totalWarehousesVal = kpis.totalWarehouses !== undefined && kpis.totalWarehouses !== null ? Number(kpis.totalWarehouses) : 0;
    const totalEmployeesVal = kpis.totalEmployees !== undefined && kpis.totalEmployees !== null ? Number(kpis.totalEmployees) : 0;
    const totalOrdersVal = kpis.orders !== undefined && kpis.orders !== null ? Number(kpis.orders) : 0;

    let avgAchievementVal = 0;
    if (this.countries.length > 0) {
      const activeCountriesWithSales = this.countries.filter(c => c.sales > 0);
      if (activeCountriesWithSales.length > 0) {
        const sumAch = activeCountriesWithSales.reduce((acc, c) => acc + Number(c.achievement || 0), 0);
        avgAchievementVal = (sumAch / activeCountriesWithSales.length).toFixed(1);
      }
    }

    const formattedSales = totalSalesVal > 0
      ? new Intl.NumberFormat(this.currencyLocale, {
          style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
        }).format(totalSalesVal)
      : 'NA/DB';

    const kpiTotalCountries = container.querySelector('#kpi-total-countries');
    const kpiTotalSales = container.querySelector('#kpi-total-sales');
    const kpiAvgAchievement = container.querySelector('#kpi-avg-achievement');
    const kpiTotalStores = container.querySelector('#kpi-total-stores');
    const kpiTotalRegions = container.querySelector('#kpi-total-regions');
    const kpiTotalWarehouses = container.querySelector('#kpi-total-warehouses');
    const kpiTotalEmployees = container.querySelector('#kpi-total-employees');
    const kpiTotalOrders = container.querySelector('#kpi-total-orders');

    if (kpiTotalCountries) kpiTotalCountries.textContent = totalCountriesVal > 0 ? String(totalCountriesVal) : 'NA/DB';
    if (kpiTotalSales) kpiTotalSales.textContent = formattedSales;
    if (kpiAvgAchievement) kpiAvgAchievement.textContent = Number(avgAchievementVal) > 0 ? `${avgAchievementVal}%` : 'NA/DB';
    if (kpiTotalStores) kpiTotalStores.textContent = totalStoresVal > 0 ? String(totalStoresVal) : 'NA/DB';
    if (kpiTotalRegions) kpiTotalRegions.textContent = totalRegionsVal > 0 ? String(totalRegionsVal) : 'NA/DB';
    if (kpiTotalWarehouses) kpiTotalWarehouses.textContent = totalWarehousesVal > 0 ? String(totalWarehousesVal) : 'NA/DB';
    if (kpiTotalEmployees) kpiTotalEmployees.textContent = totalEmployeesVal > 0 ? String(totalEmployeesVal) : 'NA/DB';
    if (kpiTotalOrders) kpiTotalOrders.textContent = totalOrdersVal > 0 ? String(totalOrdersVal) : 'NA/DB';
  }

  /**
   * Populates country filter options dropdown list.
   * @param {HTMLElement} container
   */
  _populateFilterDropdowns(container) {
    const filterNation = container.querySelector('#filter-nation');
    if (filterNation) {
      filterNation.replaceChildren();
      const defaultOption = document.createElement('option');
      defaultOption.value = 'all';
      defaultOption.textContent = 'All Nations';
      filterNation.appendChild(defaultOption);

      this.countries.forEach(c => {
        const option = document.createElement('option');
        option.value = c.country.toLowerCase();
        option.textContent = c.country;
        filterNation.appendChild(option);
      });
    }
  }

  /**
   * Filter and render elements into cards-grid mount.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} [lifecycle]
   */
  _renderCountryCardsList(container, lifecycle = this.lifecycle) {
    const cardsGrid = container.querySelector('#countries-cards-grid');
    if (!cardsGrid) return;

    const query = (this.filters.search || '').toLowerCase().trim();
    const selectedNation = this.filters.nation || 'all';

    let filtered = this.countries.filter(c => {
      const matchesSearch = c.country.toLowerCase().includes(query);
      const matchesNation = selectedNation === 'all' || c.country.toLowerCase() === selectedNation;
      return matchesSearch && matchesNation;
    });

    // Apply sorting
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

    cardsGrid.replaceChildren();

    if (filtered.length === 0) {
      const emptyTpl = container.querySelector('#national-empty-tpl');
      if (emptyTpl) {
        cardsGrid.appendChild(emptyTpl.content.cloneNode(true));
      }
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    const cardTpl = container.querySelector('#country-card-tpl');
    if (!cardTpl) return;

    filtered.forEach(c => {
      const clone = cardTpl.content.cloneNode(true);

      const nameEl = clone.querySelector('.national-card-name');
      const codeEl = clone.querySelector('.national-card-code');
      const regionsCount = clone.querySelector('.regions-count');
      const storesCount = clone.querySelector('.stores-count');
      const salesValEl = clone.querySelector('.metric-value--primary');
      const targetValEl = clone.querySelector('.metric-value--muted');
      const ordersVolume = clone.querySelector('.orders-volume');
      const staffHeadcount = clone.querySelector('.staff-headcount');
      const progressPct = clone.querySelector('.progress-pct');
      const progressFill = clone.querySelector('.progress-fill');
      const deleteBtn = clone.querySelector('.btn-delete-country');
      const viewDetailsBtn = clone.querySelector('.btn-view-details');
      const footerCode = clone.querySelector('.footer-code');

      const salesVal = Number(c.sales || 0);
      const targetVal = Number(c.target || 0);
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

      if (nameEl) {
        nameEl.textContent = c.country;
        nameEl.setAttribute('title', c.country);
      }
      if (codeEl) codeEl.textContent = `CODE: ${c.code || 'NATION'}`;
      if (regionsCount) regionsCount.textContent = c.subRegionsCount > 0 ? String(c.subRegionsCount) : 'NA/DB';
      if (storesCount) storesCount.textContent = c.stores > 0 ? String(c.stores) : 'NA/DB';
      if (salesValEl) salesValEl.textContent = salesFormatted;
      if (targetValEl) targetValEl.textContent = targetFormatted;
      if (ordersVolume) ordersVolume.textContent = c.orders > 0 ? `${c.orders} orders` : 'NA/DB';
      if (staffHeadcount) staffHeadcount.textContent = c.employees > 0 ? `${c.employees} staff` : 'NA/DB';

      if (progressPct) {
        progressPct.textContent = ach === 'NA/DB' ? 'NA/DB' : `${ach}%`;
        progressPct.style.color = statusColor;
      }
      if (progressFill) {
        progressFill.style.width = ach === 'NA/DB' ? '0%' : `${Math.min(100, Number(ach))}%`;
        progressFill.style.backgroundColor = statusColor;
      }

      if (footerCode) footerCode.textContent = c.code || 'NATION';

      if (c.id && deleteBtn) {
        deleteBtn.removeAttribute('hidden');
        deleteBtn.setAttribute('data-id', c.id);
        deleteBtn.setAttribute('data-name', c.country);
      }

      if (viewDetailsBtn) {
        viewDetailsBtn.setAttribute('data-id', c.id);
      }

      cardsGrid.appendChild(clone);
    });

    if (window.lucide) window.lucide.createIcons();
    this._bindCardActionEvents(container, lifecycle);
  }

  /**
   * Bind event handlers to deletion & view detail buttons.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindCardActionEvents(container, lifecycle) {
    // 1. Delete Country Action
    const deleteButtons = container.querySelectorAll('.btn-delete-country');
    deleteButtons.forEach(btn => {
      if (btn.dataset.hasDeleteListener) return;
      btn.dataset.hasDeleteListener = 'true';

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
              await this.loadAndRender(container, lifecycle);
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
      if (btn.dataset.hasViewListener) return;
      btn.dataset.hasViewListener = 'true';

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

  /**
   * Populate details inside overlay modal dialog box.
   * @param {Object} c
   */
  showCountryDetails(c) {
    const modal = this.container.querySelector('#details-modal');
    const title = this.container.querySelector('#details-modal-title');
    const body = this.container.querySelector('#details-modal-body');
    
    if (!modal || !body) return;

    title.replaceChildren();
    const headingDiv = document.createElement('div');
    headingDiv.className = 'flex align-center gap-xs';
    const headingIcon = document.createElement('i');
    headingIcon.setAttribute('data-lucide', 'globe');
    headingIcon.setAttribute('aria-hidden', 'true');
    const headingText = document.createElement('span');
    headingText.textContent = `${c.country} Operations Hub`;
    headingDiv.appendChild(headingIcon);
    headingDiv.appendChild(headingText);
    title.appendChild(headingDiv);

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

    body.replaceChildren();

    // Stats Section
    const statsRow = document.createElement('div');
    statsRow.className = 'details-stats-row';

    const buildStatCol = (label, val, isPrimary = false) => {
      const col = document.createElement('div');
      col.className = 'details-stats-col';
      const lblSpan = document.createElement('span');
      lblSpan.className = 'details-stats-label';
      lblSpan.textContent = label;

      const valDiv = document.createElement('div');
      valDiv.className = isPrimary ? 'details-stats-value details-stats-value--primary' : 'details-stats-value';
      valDiv.textContent = val;

      col.appendChild(lblSpan);
      col.appendChild(valDiv);
      return col;
    };

    statsRow.appendChild(buildStatCol('MTD Income', salesFormatted, true));
    statsRow.appendChild(buildStatCol('Sales Target', targetFormatted));
    statsRow.appendChild(buildStatCol('Orders / Headcount', `${c.orders} orders Â· ${c.employees} staff`));
    body.appendChild(statsRow);

    // Columns wrapper
    const colsWrapper = document.createElement('div');
    colsWrapper.className = 'details-cols-wrapper';

    // Regions Column
    const regCard = document.createElement('div');
    regCard.className = 'card glass details-sub-card';

    const regHeader = document.createElement('div');
    regHeader.className = 'details-sub-header';
    const regTitle = document.createElement('h4');
    regTitle.className = 'details-sub-title';
    
    const regTitleIcon = document.createElement('i');
    regTitleIcon.setAttribute('data-lucide', 'map');
    const regTitleText = document.createElement('span');
    regTitleText.textContent = `Sub-Regions (${subRegions.length})`;
    regTitle.appendChild(regTitleIcon);
    regTitle.appendChild(regTitleText);
    regHeader.appendChild(regTitle);
    regCard.appendChild(regHeader);

    const regListScroll = document.createElement('div');
    regListScroll.className = 'details-list-scroll';

    if (subRegions.length === 0) {
      const noReg = document.createElement('div');
      noReg.className = 'no-data';
      noReg.textContent = 'No active sub-regions registered under this country.';
      regListScroll.appendChild(noReg);
    } else {
      const regTable = document.createElement('table');
      const regThead = document.createElement('thead');
      const regTr = document.createElement('tr');
      
      const buildTh = (text, align = 'left') => {
        const th = document.createElement('th');
        th.style.textAlign = align;
        th.textContent = text;
        return th;
      };
      regTr.appendChild(buildTh('Region Name'));
      regTr.appendChild(buildTh('Code'));
      regTr.appendChild(buildTh('MTD Sales', 'right'));
      regThead.appendChild(regTr);
      regTable.appendChild(regThead);

      const regTbody = document.createElement('tbody');
      subRegions.forEach(sr => {
        const match = this.allRegionsPerformance.find(p => p.region.toLowerCase() === sr.name.toLowerCase());
        const srSales = match ? Number(match.sales || 0) : 0;
        const srSalesFmt = new Intl.NumberFormat(this.currencyLocale, {
          style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
        }).format(srSales);

        const tr = document.createElement('tr');

        const tdName = document.createElement('td');
        tdName.className = 'bold-val';
        tdName.textContent = sr.name;

        const tdCode = document.createElement('td');
        tdCode.style.color = 'var(--text-secondary)';
        tdCode.style.fontFamily = 'var(--font-mono)';
        tdCode.style.fontSize = '0.7rem';
        tdCode.textContent = sr.code;

        const tdSales = document.createElement('td');
        tdSales.style.textAlign = 'right';
        tdSales.className = 'accent-val';
        tdSales.textContent = srSalesFmt;

        tr.appendChild(tdName);
        tr.appendChild(tdCode);
        tr.appendChild(tdSales);
        regTbody.appendChild(tr);
      });
      regTable.appendChild(regTbody);
      regListScroll.appendChild(regTable);
    }
    regCard.appendChild(regListScroll);
    colsWrapper.appendChild(regCard);

    // Stores Column
    const storesCard = document.createElement('div');
    storesCard.className = 'card glass details-sub-card';

    const storeHeader = document.createElement('div');
    storeHeader.className = 'details-sub-header';
    const storeTitle = document.createElement('h4');
    storeTitle.className = 'details-sub-title';

    const storeTitleIcon = document.createElement('i');
    storeTitleIcon.setAttribute('data-lucide', 'coffee');
    const storeTitleText = document.createElement('span');
    storeTitleText.textContent = `Physical Stores (${countryStores.length})`;
    storeTitle.appendChild(storeTitleIcon);
    storeTitle.appendChild(storeTitleText);
    storeHeader.appendChild(storeTitle);
    storesCard.appendChild(storeHeader);

    const storeListScroll = document.createElement('div');
    storeListScroll.className = 'details-list-scroll';

    if (countryStores.length === 0) {
      const noStore = document.createElement('div');
      noStore.className = 'no-data';
      noStore.textContent = 'No stores registered under this country or its sub-regions.';
      storeListScroll.appendChild(noStore);
    } else {
      const stTable = document.createElement('table');
      const stThead = document.createElement('thead');
      const stTr = document.createElement('tr');

      const buildThSt = (text) => {
        const th = document.createElement('th');
        th.textContent = text;
        return th;
      };
      stTr.appendChild(buildThSt('Store Name'));
      stTr.appendChild(buildThSt('Code'));
      stTr.appendChild(buildThSt('Assigned Region'));
      stTr.appendChild(buildThSt('Contact Email'));
      stThead.appendChild(stTr);
      stTable.appendChild(stThead);

      const stTbody = document.createElement('tbody');
      countryStores.forEach(st => {
        const parentReg = this.regionsData.find(r => r.id === st.regionId);
        const regName = parentReg ? parentReg.name : 'Direct Country';

        const tr = document.createElement('tr');

        const tdName = document.createElement('td');
        tdName.className = 'bold-val';
        tdName.textContent = st.name;

        const tdCode = document.createElement('td');
        tdCode.style.color = 'var(--text-secondary)';
        tdCode.style.fontFamily = 'var(--font-mono)';
        tdCode.style.fontSize = '0.7rem';
        tdCode.textContent = st.code;

        const tdReg = document.createElement('td');
        tdReg.style.color = 'var(--text-muted)';
        tdReg.style.fontSize = '0.72rem';
        tdReg.textContent = regName;

        const tdMail = document.createElement('td');
        tdMail.style.color = 'var(--text-secondary)';
        tdMail.textContent = st.email || 'N/A';

        tr.appendChild(tdName);
        tr.appendChild(tdCode);
        tr.appendChild(tdReg);
        tr.appendChild(tdMail);
        stTbody.appendChild(tr);
      });
      stTable.appendChild(stTbody);
      storeListScroll.appendChild(stTable);
    }
    storesCard.appendChild(storeListScroll);
    colsWrapper.appendChild(storesCard);
    
    body.appendChild(colsWrapper);

    modal.removeAttribute('hidden');
    if (window.lucide) window.lucide.createIcons();
  }

  /**
   * Generates donut SVGs segments and legend lists in the right sidebar component.
   * @param {HTMLElement} container
   */
  _renderRevenueChart(container) {
    const svgEl = container.querySelector('#donut-chart-svg');
    const legendList = container.querySelector('#national-legend-list');
    const totalValEl = container.querySelector('#donut-total-value');
    
    if (!svgEl || !legendList || !totalValEl) return;

    const totalSales = this.countries.reduce((acc, c) => acc + Number(c.sales || 0), 0);

    // Format total compact
    const formatTotalCompact = (val) => {
      if (val >= 1_000_000) return `${this.currencySymbol}${(val / 1_000_000).toFixed(1)}M`;
      if (val >= 1_000) return `${this.currencySymbol}${(val / 1_000).toFixed(1)}K`;
      return `${this.currencySymbol}${val.toFixed(0)}`;
    };

    totalValEl.textContent = totalSales > 0 ? formatTotalCompact(totalSales) : 'NA/DB';

    // Clear segments except base tracks
    const existingSegments = svgEl.querySelectorAll('.donut-segment');
    existingSegments.forEach(el => el.remove());

    let currentOffset = 25;
    this.countries.forEach((c, i) => {
      const pct = totalSales > 0 ? (Number(c.sales || 0) / totalSales) * 100 : 0;
      if (pct <= 0) return;

      const strokeColor = DONUT_COLORS[i % DONUT_COLORS.length];
      const dash = `${pct.toFixed(1)} ${(100 - pct).toFixed(1)}`;
      const offset = currentOffset;
      currentOffset = (currentOffset - pct + 100) % 100;

      const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
      circle.setAttribute('class', 'donut-segment');
      circle.setAttribute('cx', '21');
      circle.setAttribute('cy', '21');
      circle.setAttribute('r', '15.915');
      circle.setAttribute('fill', 'transparent');
      circle.setAttribute('stroke', strokeColor);
      circle.setAttribute('stroke-width', '4.2');
      circle.setAttribute('stroke-dasharray', dash);
      circle.setAttribute('stroke-dashoffset', String(offset));
      circle.style.transition = 'stroke-dasharray 0.3s ease';

      svgEl.appendChild(circle);
    });

    // Populate Legend List
    legendList.replaceChildren();
    const legendTpl = container.querySelector('#legend-item-tpl');
    if (!legendTpl) return;

    this.countries.forEach((c, i) => {
      const pct = totalSales > 0 ? ((Number(c.sales || 0) / totalSales) * 100).toFixed(1) : '0.0';
      const strokeColor = DONUT_COLORS[i % DONUT_COLORS.length];
      
      const salesVal = Number(c.sales || 0);
      const salesFormatted = salesVal > 0
        ? new Intl.NumberFormat(this.currencyLocale, {
            style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
          }).format(salesVal)
        : 'NA/DB';

      const clone = legendTpl.content.cloneNode(true);
      const dot = clone.querySelector('.legend-dot');
      const name = clone.querySelector('.legend-name');
      const income = clone.querySelector('.legend-income');
      const share = clone.querySelector('.legend-share');
      const regionsEl = clone.querySelector('.legend-regions');
      const storesEl = clone.querySelector('.legend-stores');
      const achEl = clone.querySelector('.legend-ach');

      // Count sub-regions and stores belonging to this country
      const storeCount = this.allStores.filter(s => s.regionId === c.id || s.countryId === c.id).length;
      const regionCount = c.subRegionsCount || 0;

      if (dot) dot.style.backgroundColor = strokeColor;
      if (name) name.textContent = c.country;
      if (income) income.textContent = salesFormatted;
      if (share) share.textContent = totalSales > 0 && salesVal > 0 ? `${pct}%` : 'NA/DB';
      if (regionsEl) regionsEl.textContent = regionCount > 0 ? `${regionCount}` : 'NA/DB';
      if (storesEl) storesEl.textContent = storeCount > 0 ? `${storeCount}` : 'NA/DB';

      // Calculate achievement dynamically
      const targetVal = Number(c.target || 0);
      const achVal = (salesVal > 0 && targetVal > 0) ? (salesVal / targetVal) * 100 : NaN;
      const achText = isNaN(achVal) ? 'NA/DB' : `${achVal.toFixed(1)}%`;

      if (achEl) {
        achEl.textContent = achText;
        if (isNaN(achVal)) {
          achEl.style.color = 'var(--text-muted)';
        } else {
          achEl.style.color = achVal >= 90
            ? 'var(--status-success)'
            : (achVal >= 80 ? 'var(--status-warning)' : 'var(--status-danger)');
        }
      }

      legendList.appendChild(clone);
    });
  }

  /**
   * Dynamically instantiates and renders the Financial Overview widget.
   * @param {HTMLElement} container
   */
  async _renderFinancialOverviewWidget(container) {
    const widgetContainer = container.querySelector('#national-financial-widget-container');
    if (!widgetContainer) return;

    // Clear previous widget card if any
    widgetContainer.innerHTML = '';

    // Create wrapper card with styling
    const cardEl = document.createElement('div');
    cardEl.className = 'card col-12 glass animate-slide-up';
    cardEl.id = 'national-financial-card-wrapper';
    widgetContainer.appendChild(cardEl);

    try {
      const { FinancialChart } = await import('../../../../widgets/charts/financial-chart/financial-chart.js?v=' + Date.now());
      const config = { id: 'national-financial-overview', title: 'Financial Overview (MTD)' };
      const financialOverview = this.overview ? this.overview.financialOverview : null;
      const instance = new FinancialChart(config, financialOverview);
      await instance.mount(cardEl, this.lifecycle);
    } catch (err) {
      logger.error('NationalPage', 'Failed to load FinancialChart widget on National page', err);
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Initializer Helpers
  // ---------------------------------------------------------------------------

  _updateDatesFromPreset() {
    const today = new Date();
    const fmt = (d) => {
      const y = d.getFullYear();
      const m = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${y}-${m}-${day}`;
    };

    switch (this.filters.dateRangePreset) {
      case 'today':
        this.filters.from = fmt(today);
        this.filters.to = fmt(today);
        break;
      case 'yesterday':
        const yesterday = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
        this.filters.from = fmt(yesterday);
        this.filters.to = fmt(yesterday);
        break;
      case 'this-week': {
        const startOfWeek = new Date(today);
        const day = startOfWeek.getDay();
        const diff = startOfWeek.getDate() - day + (day === 0 ? -6 : 1);
        startOfWeek.setDate(diff);
        this.filters.from = fmt(startOfWeek);
        this.filters.to = fmt(today);
        break;
      }
      case 'last-week': {
        const mondayLastWeek = new Date();
        const day = mondayLastWeek.getDay();
        const diff = mondayLastWeek.getDate() - day + (day === 0 ? -6 : 1) - 7;
        mondayLastWeek.setDate(diff);
        const sundayLastWeek = new Date(mondayLastWeek);
        sundayLastWeek.setDate(mondayLastWeek.getDate() + 6);
        this.filters.from = fmt(mondayLastWeek);
        this.filters.to = fmt(sundayLastWeek);
        break;
      }
      case 'this-month':
        const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
        this.filters.from = fmt(firstDay);
        this.filters.to = fmt(today);
        break;
      case 'last-month': {
        const firstOfLastMonth = new Date(today.getFullYear(), today.getMonth() - 1, 1);
        const lastOfLastMonth = new Date(today.getFullYear(), today.getMonth(), 0);
        this.filters.from = fmt(firstOfLastMonth);
        this.filters.to = fmt(lastOfLastMonth);
        break;
      }
      case 'this-quarter': {
        const quarterStartMonth = Math.floor(today.getMonth() / 3) * 3;
        const firstOfQuarter = new Date(today.getFullYear(), quarterStartMonth, 1);
        this.filters.from = fmt(firstOfQuarter);
        this.filters.to = fmt(today);
        break;
      }
      case 'this-year':
        const firstOfYear = new Date(today.getFullYear(), 0, 1);
        this.filters.from = fmt(firstOfYear);
        this.filters.to = fmt(today);
        break;
      case 'custom':
        if (!this.filters.from || !this.filters.to) {
          const firstDayCustom = new Date(today.getFullYear(), today.getMonth(), 1);
          this.filters.from = fmt(firstDayCustom);
          this.filters.to = fmt(today);
        }
        break;
    }
  }

  _initDefaultDates() {
    this._updateDatesFromPreset();
  }

  _resolveCurrencySettings() {
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      try {
        const parsed = JSON.parse(storedGeneral);
        if (parsed.defaultCurrency) {
          this.currencyCode = parsed.defaultCurrency;
          if (this.currencyCode === 'USD') { this.currencySymbol = '$'; this.currencyLocale = 'en-US'; }
          else if (this.currencyCode === 'INR') { this.currencySymbol = 'â‚¹'; this.currencyLocale = 'en-IN'; }
          else if (this.currencyCode === 'AED') { this.currencySymbol = 'AED '; this.currencyLocale = 'en-US'; }
          else { this.currencySymbol = 'â‚¬'; this.currencyLocale = 'fr-FR'; }
        }
      } catch (e) {
        // ignore
      }
    }
  }

  _loadCss() {
    const cssId = 'national-page-css';
    let link = document.getElementById(cssId);
    if (!link) {
      link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      document.head.appendChild(link);
    }
    link.href = '/modules/ultimate-admin/pages/national/national.css?v=' + Date.now();
  }
}
export { NationalPage };
