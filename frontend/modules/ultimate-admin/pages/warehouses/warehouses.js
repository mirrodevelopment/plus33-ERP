/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin — Warehouses Management
 * File              : warehouses.js
 * Purpose           : Controller component for Warehouses Management UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/ultimate-admin/warehouses/warehouses.html
 * Related CSS       : frontend/modules/ultimate-admin/warehouses/warehouses.css
 * Related APIs      : GET /api/v1/warehouses
 *                     POST /api/v1/warehouses
 *                     DELETE /api/v1/warehouses/:id
 *                     PATCH /api/v1/warehouses/:id/activate
 *                     PATCH /api/v1/warehouses/:id/deactivate
 *                     GET /api/v1/regions
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in warehouses.html — this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor → mount → loadTemplate → loadData → render → bindEvents → destroy
 ******************************************************************************/

import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { dashboardService } from '../../../../services/dashboard/DashboardService.js';

/** Path to the warehouses HTML template */
const TEMPLATE_URL = 'modules/ultimate-admin/pages/warehouses/warehouses.html';

export default class WarehousesPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.warehouses = [];
    this.regions = [];
    this.selectedRegionFilter = 'all';
    this.searchQuery = '';
    this.container = null;
    this.lifecycle = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the warehouses page component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('WarehousesPage', 'Syncing warehouses workspace with database...');
    this.container = container;
    this.lifecycle = lifecycle;

    // Dynamically load warehouses CSS
    this._loadCss();

    // 1. Load template HTML
    await this._loadTemplate(container);

    // 2. Fetch data from backend APIs
    await this._loadData();

    // 3. Render loaded data into the DOM
    this._render(container);

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
      // 1. Fetch raw warehouses from GET /api/v1/warehouses
      const warehousesRes = await apiClient.get('/api/v1/warehouses', { size: 100 });
      if (warehousesRes && warehousesRes.success && warehousesRes.data && warehousesRes.data.content) {
        this.warehouses = warehousesRes.data.content;
      }
      
      // 2. Fetch raw regions from GET /api/v1/regions
      const regionsRes = await apiClient.get('/api/v1/regions', { size: 100 });
      if (regionsRes && regionsRes.success && regionsRes.data && regionsRes.data.content) {
        this.regions = regionsRes.data.content;
      }
    } catch (err) {
      logger.error('WarehousesPage', 'Failed to synchronize data with server:', err);
      notificationStore.danger('Database connection failed. Displaying local cache.');
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  /**
   * Render dynamic fields, KPI cards, filter list items, warehouse cards.
   * @param {HTMLElement} container
   */
  _render(container) {
    this._populateKpiSummary(container);
    this._populateFilterDropdowns(container);
    this._renderWarehouseCardsList(container);
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
    const searchInput = container.querySelector('#input-search-warehouses');
    const regionFilter = container.querySelector('#select-filter-region');
    const btnAddWarehouse = container.querySelector('#btn-add-warehouse');
    const modal = container.querySelector('#add-warehouse-modal');
    const btnCloseModal = container.querySelector('#btn-close-warehouse-modal');
    const formAddWarehouse = container.querySelector('#form-add-warehouse');

    // Restore filter values in UI fields
    if (searchInput) searchInput.value = this.searchQuery;
    if (regionFilter) regionFilter.value = this.selectedRegionFilter;

    // 1. Text Search Input
    if (searchInput) {
      const handleSearch = (e) => {
        this.searchQuery = e.target.value;
        this._renderWarehouseCardsList(container, lifecycle);
      };
      searchInput.addEventListener('input', handleSearch);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', handleSearch));
    }

    // 2. Region Dropdown Filter
    if (regionFilter) {
      const handleRegionFilter = (e) => {
        this.selectedRegionFilter = e.target.value;
        this._renderWarehouseCardsList(container, lifecycle);
      };
      regionFilter.addEventListener('change', handleRegionFilter);
      lifecycle.onCleanup(() => regionFilter.removeEventListener('change', handleRegionFilter));
    }

    // 3. Open Add Warehouse Modal
    if (btnAddWarehouse && modal) {
      const openModal = () => {
        modal.removeAttribute('hidden');
        const dateInput = container.querySelector('#wh-modal-date');
        if (dateInput) dateInput.value = new Date().toISOString().substring(0, 10);
      };
      btnAddWarehouse.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddWarehouse.removeEventListener('click', openModal));
    }

    // 4. Close Add Warehouse Modal
    if (btnCloseModal && modal) {
      const closeModal = () => {
        modal.setAttribute('hidden', '');
      };
      btnCloseModal.addEventListener('click', closeModal);
      lifecycle.onCleanup(() => btnCloseModal.removeEventListener('click', closeModal));

      // Close on backdrop click
      const backdropClose = (e) => {
        if (e.target === modal) {
          modal.setAttribute('hidden', '');
        }
      };
      modal.addEventListener('click', backdropClose);
      lifecycle.onCleanup(() => modal.removeEventListener('click', backdropClose));
    }

    // 5. Form Submit connection to POST /api/v1/warehouses
    if (formAddWarehouse && modal) {
      const submitBtn = container.querySelector('#btn-wh-submit');
      const handleSubmit = async (e) => {
        e.preventDefault();

        const name = container.querySelector('#wh-modal-name').value.trim();
        const regionId = Number(container.querySelector('#wh-modal-region').value);
        const address = container.querySelector('#wh-modal-address').value.trim();
        const phone = container.querySelector('#wh-modal-phone').value.trim();
        const email = container.querySelector('#wh-modal-email').value.trim();
        const timezone = container.querySelector('#wh-modal-timezone').value.trim();
        const openingDate = container.querySelector('#wh-modal-date').value;

        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.textContent = 'Registering…';
        }

        try {
          const cleanCode = 'WH_' + name.toUpperCase().replace(/[^A-Z0-9]/g, '_').substring(0, 20) + '_' + Date.now();
          
          const response = await apiClient.post('/api/v1/warehouses', {
            code: cleanCode,
            name: name,
            address: address,
            phone: phone,
            email: email,
            timezone: timezone,
            openingDate: openingDate,
            regionId: regionId,
            active: true
          });

          if (response && response.success) {
            // Force dynamic metrics updates
            await dashboardService.getDashboardOverview();
            
            // Hide modal and reset form
            modal.setAttribute('hidden', '');
            formAddWarehouse.reset();

            // Refresh UI
            await this.loadAndRender(container, lifecycle);

            notificationStore.success(`Distribution warehouse '${name}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by backend API.');
          }

        } catch (err) {
          logger.error('WarehousesPage', 'Failed to register warehouse:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Warehouse';
          }
        }
      };

      formAddWarehouse.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddWarehouse.removeEventListener('submit', handleSubmit));
    }

    // Bind actions inside current grid
    this._bindGridActions(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    logger.debug('WarehousesPage', 'Workspace destroyed.');
  }

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): loadAndRender
  // ---------------------------------------------------------------------------

  async loadAndRender(container = this.container, lifecycle = this.lifecycle) {
    await this._loadData();
    this._render(container);
    this._bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Rendering Sub-routines
  // ---------------------------------------------------------------------------

  /**
   * Populate top metric row values.
   * @param {HTMLElement} container
   */
  _populateKpiSummary(container) {
    const activeWarehouses = this.warehouses.filter(w => w.active).length;
    const inactiveWarehouses = this.warehouses.length - activeWarehouses;
    const totalRegions = this.regions.length;

    const kpiTotalWarehouses = container.querySelector('#kpi-total-warehouses');
    const kpiActiveWarehouses = container.querySelector('#kpi-active-warehouses');
    const kpiInactiveWarehouses = container.querySelector('#kpi-inactive-warehouses');
    const kpiServicedRegions = container.querySelector('#kpi-serviced-regions');

    if (kpiTotalWarehouses) kpiTotalWarehouses.textContent = this.warehouses.length;
    if (kpiActiveWarehouses) kpiActiveWarehouses.textContent = activeWarehouses;
    if (kpiInactiveWarehouses) kpiInactiveWarehouses.textContent = inactiveWarehouses;
    if (kpiServicedRegions) kpiServicedRegions.textContent = totalRegions;
  }

  /**
   * Populate dropdown options from dynamic list.
   * @param {HTMLElement} container
   */
  _populateFilterDropdowns(container) {
    const regionFilter = container.querySelector('#select-filter-region');
    const modalRegionSelect = container.querySelector('#wh-modal-region');

    if (regionFilter) {
      regionFilter.replaceChildren();
      const defaultOption = document.createElement('option');
      defaultOption.value = 'all';
      defaultOption.textContent = 'All Regions';
      regionFilter.appendChild(defaultOption);

      this.regions.forEach(r => {
        const option = document.createElement('option');
        option.value = r.id;
        option.textContent = this.sanitizeText(r.name);
        regionFilter.appendChild(option);
      });
    }

    if (modalRegionSelect) {
      modalRegionSelect.replaceChildren();
      const defaultOption = document.createElement('option');
      defaultOption.value = '';
      defaultOption.disabled = true;
      defaultOption.selected = true;
      defaultOption.textContent = '— Select Region —';
      modalRegionSelect.appendChild(defaultOption);

      this.regions.forEach(r => {
        const option = document.createElement('option');
        option.value = r.id;
        option.textContent = this.sanitizeText(r.name);
        modalRegionSelect.appendChild(option);
      });
    }
  }

  /**
   * Dynamic filtration and render elements into cards-grid mount.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} [lifecycle]
   */
  _renderWarehouseCardsList(container, lifecycle = this.lifecycle) {
    const cardsGrid = container.querySelector('#warehouses-cards-grid');
    if (!cardsGrid) return;

    let filtered = [...this.warehouses];

    // 1. Text Search query filter
    const query = this.searchQuery.toLowerCase().trim();
    if (query) {
      filtered = filtered.filter(w => 
        (w.name && w.name.toLowerCase().includes(query)) || 
        (w.code && w.code.toLowerCase().includes(query))
      );
    }

    // 2. Region selection filter
    if (this.selectedRegionFilter !== 'all') {
      filtered = filtered.filter(w => w.regionId === Number(this.selectedRegionFilter));
    }

    cardsGrid.replaceChildren();

    if (filtered.length === 0) {
      const emptyTpl = container.querySelector('#warehouses-empty-tpl');
      if (emptyTpl) {
        cardsGrid.appendChild(emptyTpl.content.cloneNode(true));
      }
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    const cardTpl = container.querySelector('#warehouse-card-tpl');
    if (!cardTpl) return;

    filtered.forEach(w => {
      const clone = cardTpl.content.cloneNode(true);

      const warehouseCode = clone.querySelector('.warehouse-code');
      const warehouseName = clone.querySelector('.warehouse-name');
      const regionCode = clone.querySelector('.region-code');
      const contactAddress = clone.querySelector('.contact-address');
      const contactPhone = clone.querySelector('.contact-phone');
      const contactEmail = clone.querySelector('.contact-email');
      const statusBadge = clone.querySelector('.warehouse-status-badge');
      const openingDate = clone.querySelector('.warehouse-opening-date');
      const deleteBtn = clone.querySelector('.btn-delete-wh');
      const toggleBtn = clone.querySelector('.btn-toggle-wh-state');

      const activeState = w.active;
      const statusText = activeState ? 'ACTIVE' : 'INACTIVE';
      const cleanName = this.sanitizeText(w.name);
      const cleanAddress = this.sanitizeText(w.address);

      if (warehouseCode) warehouseCode.textContent = `CODE: ${w.code}`;
      if (warehouseName) {
        warehouseName.textContent = cleanName;
        warehouseName.setAttribute('title', cleanName);
      }
      if (regionCode) regionCode.textContent = this.sanitizeText(w.regionCode || 'IDF');
      if (contactAddress) {
        contactAddress.textContent = cleanAddress;
        contactAddress.setAttribute('title', cleanAddress);
      }
      if (contactPhone) contactPhone.textContent = w.phone || 'No phone';
      if (contactEmail) {
        contactEmail.textContent = w.email || 'No email';
        contactEmail.setAttribute('title', w.email || '');
      }
      if (openingDate) openingDate.textContent = `Opened: ${w.openingDate || 'Unknown'}`;

      if (statusBadge) {
        statusBadge.textContent = statusText;
        statusBadge.className = `warehouse-status-badge warehouse-status-badge--${activeState ? 'active' : 'inactive'}`;
      }

      if (deleteBtn) {
        deleteBtn.setAttribute('data-id', w.id);
        deleteBtn.setAttribute('data-name', cleanName);
      }

      if (toggleBtn) {
        toggleBtn.setAttribute('data-id', w.id);
        toggleBtn.setAttribute('data-active', String(activeState));
        toggleBtn.title = activeState ? 'Deactivate hub' : 'Activate hub';
        const toggleIcon = toggleBtn.querySelector('i');
        if (toggleIcon) {
          toggleIcon.setAttribute('data-lucide', activeState ? 'toggle-right' : 'toggle-left');
          toggleBtn.style.color = activeState ? 'var(--status-success)' : 'var(--status-danger)';
        }
      }

      cardsGrid.appendChild(clone);
    });

    if (window.lucide) window.lucide.createIcons();
    this._bindGridActions(container, lifecycle);
  }

  /**
   * Bind event handlers to deletion & status toggle buttons.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindGridActions(container, lifecycle) {
    // 1. Soft-Delete Warehouse
    const deleteButtons = container.querySelectorAll('.btn-delete-wh');
    deleteButtons.forEach(btn => {
      if (btn.dataset.hasDeleteListener) return;
      btn.dataset.hasDeleteListener = 'true';

      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const name = btn.getAttribute('data-name');
        
        if (confirm(`Are you sure you want to delete the warehouse "${name}"?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/warehouses/${id}`);
            if (res && res.success) {
              notificationStore.success(`Warehouse "${name}" deleted successfully.`);
              await dashboardService.getDashboardOverview();
              await this.loadAndRender(container, lifecycle);
            } else {
              throw new Error(res.message || 'Operation rejected by backend API.');
            }
          } catch (err) {
            logger.error('WarehousesPage', `Failed to delete warehouse ${name}:`, err);
            notificationStore.danger(`Deletion failed: ${err.message}`);
          }
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });

    // 2. Activate/Deactivate Toggle Switcher
    const toggleButtons = container.querySelectorAll('.btn-toggle-wh-state');
    toggleButtons.forEach(btn => {
      if (btn.dataset.hasToggleListener) return;
      btn.dataset.hasToggleListener = 'true';

      const handleToggle = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const isActive = btn.getAttribute('data-active') === 'true';
        const actionText = isActive ? 'deactivate' : 'activate';
        
        try {
          const endpoint = `/api/v1/warehouses/${id}/${actionText}`;
          const res = await apiClient.patch(endpoint);
          
          if (res && res.success) {
            notificationStore.success(`Warehouse state changed to ${actionText}d successfully.`);
            await dashboardService.getDashboardOverview();
            await this.loadAndRender(container, lifecycle);
          } else {
            throw new Error(res.message || 'State modification rejected by API.');
          }
        } catch (err) {
          logger.error('WarehousesPage', `Failed to toggle warehouse state:`, err);
          notificationStore.danger(`Modification failed: ${err.message}`);
        }
      };
      btn.addEventListener('click', handleToggle);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleToggle));
    });
  }

  // ---------------------------------------------------------------------------
  // PUBLIC UTILITY
  // ---------------------------------------------------------------------------

  sanitizeText(text) {
    if (!text) return '';
    return text
      .replace(/\??/g, ' ')
      .replace(/ǩ/g, 'î')
      .replace(/Ǹ/g, 'é')
      .replace(/Ǫ/g, 'è');
  }

  _loadCss() {
    const cssId = 'warehouses-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/ultimate-admin/pages/warehouses/warehouses.css';
      document.head.appendChild(link);
    }
  }
}
export { WarehousesPage };
