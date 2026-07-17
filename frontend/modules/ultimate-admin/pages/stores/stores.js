/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin — Stores Management
 * File              : stores.js
 * Purpose           : Controller component for Stores Management UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/ultimate-admin/stores/stores.html
 * Related CSS       : frontend/modules/ultimate-admin/stores/stores.css
 * Related APIs      : GET /api/v1/stores
 *                     POST /api/v1/stores
 *                     DELETE /api/v1/stores/:id
 *                     PATCH /api/v1/stores/:id/activate
 *                     PATCH /api/v1/stores/:id/deactivate
 *                     GET /api/v1/regions
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in stores.html — this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor → mount → loadTemplate → loadData → render → bindEvents → destroy
 ******************************************************************************/

import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { dashboardService } from '../../../../services/dashboard/DashboardService.js';
import { authStore } from '../../../../store/authStore.js';

/** Path to the stores HTML template */
const TEMPLATE_URL = 'modules/ultimate-admin/pages/stores/stores.html';

export default class StoresPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.stores = [];
    this.regions = [];
    this.selectedRegionFilter = 'all';
    this.selectedTypeFilter = 'all';
    this.searchQuery = '';
    this.container = null;
    this.lifecycle = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the stores page component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoresPage', 'Syncing stores workspace with backend database...');
    this.container = container;
    this.lifecycle = lifecycle;

    // Dynamically load stores CSS
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
      // 1. Fetch raw stores from GET /api/v1/stores
      const storesRes = await apiClient.get('/api/v1/stores', { size: 100 });
      if (storesRes && storesRes.success && storesRes.data && storesRes.data.content) {
        this.stores = storesRes.data.content;
      }
      
      // 2. Fetch raw regions from GET /api/v1/regions
      const regionsRes = await apiClient.get('/api/v1/regions', { size: 100 });
      if (regionsRes && regionsRes.success && regionsRes.data && regionsRes.data.content) {
        this.regions = regionsRes.data.content;
      }

      // Restrict stores and regions if user is a nationalAdmin
      const meRes = await apiClient.get('/api/v1/auth/me');
      const currentUser = (meRes && meRes.success) ? meRes.data : null;
      const userCountry = currentUser ? currentUser.country : null;
      const activeRole = authStore.getRole();

      if (activeRole === 'nationalAdmin' && userCountry) {
        const allCountries = this.regions.filter(r => r.parentId === null || r.parentId === undefined);
        const userCountryObj = allCountries.find(c => c.name && userCountry && (c.name.toLowerCase().includes(userCountry.toLowerCase()) || userCountry.toLowerCase().includes(c.name.toLowerCase())));
        if (userCountryObj) {
          // Filter regions to only include sub-regions of user's country
          this.regions = this.regions.filter(r => r.parentId === userCountryObj.id);
          const regionIds = new Set(this.regions.map(r => r.id));
          // Filter stores to only include locations within those regions
          this.stores = this.stores.filter(s => regionIds.has(s.regionId));
        }
      }
    } catch (err) {
      logger.error('StoresPage', 'Failed to synchronize data with server:', err);
      notificationStore.danger('Database connection failed. Displaying local cache.');
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  /**
   * Render dynamic fields, KPI cards, filter list items, store cards.
   * @param {HTMLElement} container
   */
  _render(container) {
    this._populateKpiSummary(container);
    this._populateFilterDropdowns(container);
    this._renderStoreCardsList(container);
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
    const searchInput = container.querySelector('#input-search-stores');
    const regionFilter = container.querySelector('#select-filter-region');
    const btnAddStore = container.querySelector('#btn-add-store');
    const modal = container.querySelector('#add-store-modal');
    const btnCloseModal = container.querySelector('#btn-close-store-modal');
    const formAddStore = container.querySelector('#form-add-store');

    // Restore filter values in UI fields
    const typeFilter = container.querySelector('#select-filter-type');
    if (searchInput) searchInput.value = this.searchQuery;
    if (regionFilter) regionFilter.value = this.selectedRegionFilter;
    if (typeFilter) typeFilter.value = this.selectedTypeFilter;

    // 1. Text Search Input
    if (searchInput) {
      const handleSearch = (e) => {
        this.searchQuery = e.target.value;
        this._renderStoreCardsList(container, lifecycle);
      };
      searchInput.addEventListener('input', handleSearch);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', handleSearch));
    }

    // 2. Region Dropdown Filter
    if (regionFilter) {
      const handleRegionFilter = (e) => {
        this.selectedRegionFilter = e.target.value;
        this._renderStoreCardsList(container, lifecycle);
      };
      regionFilter.addEventListener('change', handleRegionFilter);
      lifecycle.onCleanup(() => regionFilter.removeEventListener('change', handleRegionFilter));
    }

    // 2b. Store Type Dropdown Filter
    if (typeFilter) {
      const handleTypeFilter = (e) => {
        this.selectedTypeFilter = e.target.value;
        this._renderStoreCardsList(container, lifecycle);
      };
      typeFilter.addEventListener('change', handleTypeFilter);
      lifecycle.onCleanup(() => typeFilter.removeEventListener('change', handleTypeFilter));
    }

    // 3. Open Add Store Modal
    if (btnAddStore && modal) {
      const openModal = () => {
        modal.removeAttribute('hidden');
        // Auto-set opening date to today's date format (YYYY-MM-DD)
        const dateInput = container.querySelector('#store-modal-date');
        if (dateInput) dateInput.value = new Date().toISOString().substring(0, 10);
      };
      btnAddStore.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddStore.removeEventListener('click', openModal));
    }

    // 4. Close Add Store Modal
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

    // 5. Form Submit connection to POST /api/v1/stores
    if (formAddStore && modal) {
      const submitBtn = container.querySelector('#btn-store-submit');
      const handleSubmit = async (e) => {
        e.preventDefault();

        const name = container.querySelector('#store-modal-name').value.trim();
        const regionId = Number(container.querySelector('#store-modal-region').value);
        const address = container.querySelector('#store-modal-address').value.trim();
        const phone = container.querySelector('#store-modal-phone').value.trim();
        const email = container.querySelector('#store-modal-email').value.trim();
        const timezone = container.querySelector('#store-modal-timezone').value.trim();
        const openingDate = container.querySelector('#store-modal-date').value;
        const type = container.querySelector('#store-modal-type').value;

        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.textContent = 'Registering…';
        }

        try {
          // Generate a unique store code from the name
          const cleanCode = 'ST_' + name.toUpperCase().replace(/[^A-Z0-9]/g, '_').substring(0, 20) + '_' + Date.now();
          
          const response = await apiClient.post('/api/v1/stores', {
            code: cleanCode,
            name: name,
            address: address,
            phone: phone,
            email: email,
            timezone: timezone,
            openingDate: openingDate,
            regionId: regionId,
            warehouseId: 1, // mapping default central warehouse
            active: true,
            type: type
          });

          if (response && response.success) {
            // Force dynamic metrics updates
            await dashboardService.getDashboardOverview();
            
            // Hide modal and reset form
            modal.setAttribute('hidden', '');
            formAddStore.reset();

            // Refresh UI components
            await this.loadAndRender(container, lifecycle);

            notificationStore.success(`Franchise store '${name}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by backend API.');
          }

        } catch (err) {
          logger.error('StoresPage', 'Failed to register store:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Store';
          }
        }
      };

      formAddStore.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddStore.removeEventListener('submit', handleSubmit));
    }

    // Bind delete and toggle actions initially
    this._bindGridActions(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    logger.debug('StoresPage', 'Workspace destroyed.');
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
    const activeStores = this.stores.filter(s => s.active).length;
    const inactiveStores = this.stores.length - activeStores;
    const totalRegions = this.regions.length;

    const kpiTotalStores = container.querySelector('#kpi-total-stores');
    const kpiActiveStores = container.querySelector('#kpi-active-stores');
    const kpiInactiveStores = container.querySelector('#kpi-inactive-stores');
    const kpiRepresentedRegions = container.querySelector('#kpi-represented-regions');

    if (kpiTotalStores) kpiTotalStores.textContent = this.stores.length;
    if (kpiActiveStores) kpiActiveStores.textContent = activeStores;
    if (kpiInactiveStores) kpiInactiveStores.textContent = inactiveStores;
    if (kpiRepresentedRegions) kpiRepresentedRegions.textContent = totalRegions;
  }

  /**
   * Populate dropdown options from dynamic list.
   * @param {HTMLElement} container
   */
  _populateFilterDropdowns(container) {
    const regionFilter = container.querySelector('#select-filter-region');
    const modalRegionSelect = container.querySelector('#store-modal-region');

    if (regionFilter) {
      // Clear except default option
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
  _renderStoreCardsList(container, lifecycle = this.lifecycle) {
    const cardsGrid = container.querySelector('#stores-cards-grid');
    if (!cardsGrid) return;

    let filtered = [...this.stores];

    // 1. Text Search query filter
    const query = this.searchQuery.toLowerCase().trim();
    if (query) {
      filtered = filtered.filter(s => 
        (s.name && s.name.toLowerCase().includes(query)) || 
        (s.code && s.code.toLowerCase().includes(query))
      );
    }

    // 2. Region selection filter
    if (this.selectedRegionFilter !== 'all') {
      filtered = filtered.filter(s => s.regionId === Number(this.selectedRegionFilter));
    }

    // 2b. Store Type selection filter
    if (this.selectedTypeFilter !== 'all') {
      filtered = filtered.filter(s => (s.type || 'COMPACT_CAFE') === this.selectedTypeFilter);
    }

    cardsGrid.replaceChildren();

    if (filtered.length === 0) {
      const emptyTpl = container.querySelector('#stores-empty-tpl');
      if (emptyTpl) {
        cardsGrid.appendChild(emptyTpl.content.cloneNode(true));
      }
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    const cardTpl = container.querySelector('#store-card-tpl');
    if (!cardTpl) return;

    filtered.forEach(s => {
      const clone = cardTpl.content.cloneNode(true);

      const card = clone.querySelector('.stores-card');
      const storeCode = clone.querySelector('.store-code');
      const storeName = clone.querySelector('.store-name');
      const regionCode = clone.querySelector('.region-code');
      const contactAddress = clone.querySelector('.contact-address');
      const contactPhone = clone.querySelector('.contact-phone');
      const contactEmail = clone.querySelector('.contact-email');
      const statusBadge = clone.querySelector('.store-status-badge');
      const openingDate = clone.querySelector('.store-opening-date');
      const deleteBtn = clone.querySelector('.btn-delete-store');
      const toggleBtn = clone.querySelector('.btn-toggle-store-state');

      const storeAvatar = clone.querySelector('.store-avatar-badge');
      const employeeCountEl = clone.querySelector('.store-employee-count');
      const stockValueEl = clone.querySelector('.store-stock-value');
      const contactWarehouse = clone.querySelector('.contact-warehouse');
      const typeBadge = clone.querySelector('.store-type-badge');

      const activeState = s.active;
      const statusText = activeState ? 'ACTIVE' : 'INACTIVE';
      const cleanName = this.sanitizeText(s.name);
      const cleanAddress = this.sanitizeText(s.address);

      if (storeCode) storeCode.textContent = `CODE: ${s.code}`;
      if (storeName) {
        storeName.textContent = cleanName;
        storeName.setAttribute('title', cleanName);
      }
      if (regionCode) regionCode.textContent = this.sanitizeText(s.regionCode || 'IDF');
      if (typeBadge) {
        const typeVal = s.type || 'COMPACT_CAFE';
        let displayType = 'COMPACT CAFÉ';
        let modifierClass = 'store-type-badge--compact';
        
        if (typeVal === 'KIOSK') {
          displayType = 'KIOSK';
          modifierClass = 'store-type-badge--kiosk';
        } else if (typeVal === 'FLAGSHIP_CAFE') {
          displayType = 'FLAGSHIP CAFÉ';
          modifierClass = 'store-type-badge--flagship';
        }
        
        const typeLabel = typeBadge.querySelector('.type-code') || typeBadge.querySelector('span');
        if (typeLabel) {
          typeLabel.textContent = displayType;
        }
        typeBadge.className = `store-type-badge ${modifierClass}`;
      }
      if (contactAddress) {
        contactAddress.textContent = cleanAddress;
        contactAddress.setAttribute('title', cleanAddress);
      }
      if (contactPhone) contactPhone.textContent = s.phone || 'No phone';
      if (contactEmail) {
        contactEmail.textContent = s.email || 'No email';
        contactEmail.setAttribute('title', s.email || '');
      }
      if (openingDate) openingDate.textContent = `Opened: ${s.openingDate || 'Unknown'}`;

      if (storeAvatar) {
        const initials = s.name ? s.name.split(' ').filter(word => word.length > 0).map(n => n[0]).join('').substring(0, 2).toUpperCase() : 'ST';
        storeAvatar.textContent = initials;
      }
      if (employeeCountEl) {
        employeeCountEl.textContent = s.employeeCount !== undefined ? s.employeeCount : 0;
      }
      if (stockValueEl) {
        const val = s.stockValue !== undefined ? s.stockValue : 0.0;
        stockValueEl.textContent = new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(val);
      }
      if (contactWarehouse) {
        contactWarehouse.textContent = `Linked WH: ${s.warehouseCode || 'None'}`;
        contactWarehouse.setAttribute('title', s.warehouseCode || 'None');
      }

      if (statusBadge) {
        statusBadge.textContent = statusText;
        statusBadge.className = `store-status-badge store-status-badge--${activeState ? 'active' : 'inactive'}`;
      }

      // Check access permission to display delete button
      if (authStore.getRole() !== 'nationalAdmin' && deleteBtn) {
        deleteBtn.removeAttribute('hidden');
        deleteBtn.setAttribute('data-id', s.id);
        deleteBtn.setAttribute('data-name', cleanName);
      }

      if (toggleBtn) {
        toggleBtn.setAttribute('data-id', s.id);
        toggleBtn.setAttribute('data-active', String(activeState));
        toggleBtn.title = activeState ? 'Deactivate location' : 'Activate location';
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
    // 1. Soft-Delete Store
    const deleteButtons = container.querySelectorAll('.btn-delete-store');
    deleteButtons.forEach(btn => {
      if (btn.dataset.hasDeleteListener) return;
      btn.dataset.hasDeleteListener = 'true';

      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const name = btn.getAttribute('data-name');
        
        if (confirm(`Are you sure you want to delete the store "${name}"?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/stores/${id}`);
            if (res && res.success) {
              notificationStore.success(`Store "${name}" deleted successfully.`);
              await dashboardService.getDashboardOverview();
              await this.loadAndRender(container, lifecycle);
            } else {
              throw new Error(res.message || 'Operation rejected by backend API.');
            }
          } catch (err) {
            logger.error('StoresPage', `Failed to delete store ${name}:`, err);
            notificationStore.danger(`Deletion failed: ${err.message}`);
          }
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });

    // 2. Activate/Deactivate Toggle Switcher
    const toggleButtons = container.querySelectorAll('.btn-toggle-store-state');
    toggleButtons.forEach(btn => {
      if (btn.dataset.hasToggleListener) return;
      btn.dataset.hasToggleListener = 'true';

      const handleToggle = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const isActive = btn.getAttribute('data-active') === 'true';
        const actionText = isActive ? 'deactivate' : 'activate';
        
        try {
          const endpoint = `/api/v1/stores/${id}/${actionText}`;
          const res = await apiClient.patch(endpoint);
          
          if (res && res.success) {
            notificationStore.success(`Location state changed to ${actionText}d successfully.`);
            await dashboardService.getDashboardOverview();
            await this.loadAndRender(container, lifecycle);
          } else {
            throw new Error(res.message || 'State modification rejected by API.');
          }
        } catch (err) {
          logger.error('StoresPage', `Failed to toggle store state:`, err);
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
    const cssId = 'stores-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/ultimate-admin/pages/stores/stores.css';
      document.head.appendChild(link);
    }
  }
}
export { StoresPage };
