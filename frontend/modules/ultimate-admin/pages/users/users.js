/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin — Users & Roles
 * File              : users.js
 * Purpose           : Controller component for Users & Roles UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/ultimate-admin/users/users.html
 * Related CSS       : frontend/modules/ultimate-admin/users/users.css
 * Related APIs      : GET /api/v1/employees
 *                     POST /api/v1/employees
 *                     DELETE /api/v1/employees/:id
 *                     PATCH /api/v1/employees/:id/activate
 *                     PATCH /api/v1/employees/:id/deactivate
 *                     GET /api/v1/regions
 *                     GET /api/v1/stores
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in users.html — this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor → mount → loadTemplate → loadData → render → bindEvents → destroy
 ******************************************************************************/

import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { dashboardService } from '../../../../services/dashboard/DashboardService.js';

/** Path to the users HTML template */
const TEMPLATE_URL = 'modules/ultimate-admin/pages/users/users.html';

export default class UsersPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.users = [];
    this.regions = [];
    this.stores = [];
    this.searchQuery = '';
    this.selectedRegionFilter = 'all';
    this.selectedRoleFilter = 'all';
    this.container = null;
    this.lifecycle = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the users page component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('UsersPage', 'Syncing workforce workspace with database...');
    this.container = container;
    this.lifecycle = lifecycle;

    // Dynamically load users CSS
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
      // 1. Fetch raw employees from GET /api/v1/employees
      const employeesRes = await apiClient.get('/api/v1/employees', { size: 100 });
      if (employeesRes && employeesRes.success && employeesRes.data && employeesRes.data.content) {
        this.users = employeesRes.data.content;
      }
      
      // 2. Fetch raw regions from GET /api/v1/regions
      const regionsRes = await apiClient.get('/api/v1/regions', { size: 100 });
      if (regionsRes && regionsRes.success && regionsRes.data && regionsRes.data.content) {
        this.regions = regionsRes.data.content;
      }

      // 3. Fetch raw stores from GET /api/v1/stores
      const storesRes = await apiClient.get('/api/v1/stores', { size: 100 });
      if (storesRes && storesRes.success && storesRes.data && storesRes.data.content) {
        this.stores = storesRes.data.content;
      }
    } catch (err) {
      logger.error('UsersPage', 'Failed to synchronize data with server:', err);
      notificationStore.danger('Database connection failed. Displaying local cache.');
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  /**
   * Render dynamic fields, KPI cards, filter list items, user cards.
   * @param {HTMLElement} container
   */
  _render(container) {
    this._populateKpiSummary(container);
    this._populateFilterDropdowns(container);
    this._renderUserCardsList(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  /**
   * Bind event listeners for search input, dropdown filters, and modal triggers.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#input-search-users');
    const regionFilter = container.querySelector('#select-filter-region');
    const roleFilter = container.querySelector('#select-filter-role');
    const btnAddUser = container.querySelector('#btn-add-user');
    const modal = container.querySelector('#add-user-modal');
    const btnCloseModal = container.querySelector('#btn-close-user-modal');
    const formAddUser = container.querySelector('#form-add-user');

    // Restore filter values in UI fields
    if (searchInput) searchInput.value = this.searchQuery;
    if (regionFilter) regionFilter.value = this.selectedRegionFilter;
    if (roleFilter) roleFilter.value = this.selectedRoleFilter;

    // 1. Text Search Input
    if (searchInput) {
      const handleSearch = (e) => {
        this.searchQuery = e.target.value;
        this._renderUserCardsList(container, lifecycle);
      };
      searchInput.addEventListener('input', handleSearch);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', handleSearch));
    }

    // 2. Region Dropdown Filter
    if (regionFilter) {
      const handleRegionFilter = (e) => {
        this.selectedRegionFilter = e.target.value;
        this._renderUserCardsList(container, lifecycle);
      };
      regionFilter.addEventListener('change', handleRegionFilter);
      lifecycle.onCleanup(() => regionFilter.removeEventListener('change', handleRegionFilter));
    }

    // 3. Designation Role Dropdown Filter
    if (roleFilter) {
      const handleRoleFilter = (e) => {
        this.selectedRoleFilter = e.target.value;
        this._renderUserCardsList(container, lifecycle);
      };
      roleFilter.addEventListener('change', handleRoleFilter);
      lifecycle.onCleanup(() => roleFilter.removeEventListener('change', handleRoleFilter));
    }

    // 4. Open Add User Modal
    if (btnAddUser && modal) {
      const openModal = () => {
        modal.removeAttribute('hidden');
        const dateInput = container.querySelector('#user-modal-date');
        if (dateInput) dateInput.value = new Date().toISOString().substring(0, 10);
      };
      btnAddUser.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddUser.removeEventListener('click', openModal));
    }

    // 5. Close Add User Modal
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

    // 6. Form Submit connection to POST /api/v1/employees
    if (formAddUser && modal) {
      const submitBtn = container.querySelector('#btn-user-submit');
      const handleSubmit = async (e) => {
        e.preventDefault();

        const firstName = container.querySelector('#user-modal-firstname').value.trim();
        const lastName = container.querySelector('#user-modal-lastname').value.trim();
        const email = container.querySelector('#user-modal-email').value.trim();
        const designation = container.querySelector('#user-modal-designation').value;
        const employmentType = container.querySelector('#user-modal-emptype').value;
        const regionId = container.querySelector('#user-modal-region').value;
        const storeId = container.querySelector('#user-modal-store').value;
        const phone = container.querySelector('#user-modal-phone').value.trim();
        const hireDate = container.querySelector('#user-modal-date').value;

        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.textContent = 'Registering…';
        }

        try {
          const cleanCode = 'EMP-' + Math.floor(1000 + Math.random() * 9000);
          
          const response = await apiClient.post('/api/v1/employees', {
            employeeCode: cleanCode,
            firstName: firstName,
            lastName: lastName,
            email: email,
            phone: phone,
            companyId: 1, // default company
            regionId: regionId ? Number(regionId) : null,
            storeId: storeId ? Number(storeId) : null,
            designation: designation,
            department: 'Operations',
            employmentType: employmentType,
            hireDate: hireDate,
            active: true
          });

          if (response && response.success) {
            // Force dynamic metrics updates
            await dashboardService.getDashboardOverview();
            
            // Hide modal and reset form
            modal.setAttribute('hidden', '');
            formAddUser.reset();

            // Refresh UI
            await this.loadAndRender(container, lifecycle);

            notificationStore.success(`User profile '${firstName} ${lastName}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by backend API.');
          }

        } catch (err) {
          logger.error('UsersPage', 'Failed to register user:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register User Profile';
          }
        }
      };

      formAddUser.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddUser.removeEventListener('submit', handleSubmit));
    }

    // Bind actions inside current grid
    this._bindGridActions(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    logger.debug('UsersPage', 'Workspace destroyed.');
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
   * Populate top metric grid row.
   * @param {HTMLElement} container
   */
  _populateKpiSummary(container) {
    const totalUsers = this.users.length;
    const activeUsers = this.users.filter(u => u.active).length;
    const permanentUsers = this.users.filter(u => u.employmentType === 'PERMANENT').length;
    const uniqueRoles = [...new Set(this.users.map(u => u.designation))].filter(Boolean);

    const kpiTotalStaff = container.querySelector('#kpi-total-staff');
    const kpiActiveOperators = container.querySelector('#kpi-active-operators');
    const kpiPermanentContracts = container.querySelector('#kpi-permanent-contracts');
    const kpiMappedRoles = container.querySelector('#kpi-mapped-roles');

    if (kpiTotalStaff) kpiTotalStaff.textContent = totalUsers;
    if (kpiActiveOperators) kpiActiveOperators.textContent = activeUsers;
    if (kpiPermanentContracts) kpiPermanentContracts.textContent = permanentUsers;
    if (kpiMappedRoles) kpiMappedRoles.textContent = uniqueRoles.length;
  }

  /**
   * Populate filter selects and create modal fields.
   * @param {HTMLElement} container
   */
  _populateFilterDropdowns(container) {
    const regionFilter = container.querySelector('#select-filter-region');
    const roleFilter = container.querySelector('#select-filter-role');
    const modalRegionSelect = container.querySelector('#user-modal-region');
    const modalStoreSelect = container.querySelector('#user-modal-store');

    const uniqueRoles = [...new Set(this.users.map(u => u.designation))].filter(Boolean);

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

    if (roleFilter) {
      roleFilter.replaceChildren();
      const defaultOption = document.createElement('option');
      defaultOption.value = 'all';
      defaultOption.textContent = 'All Roles';
      roleFilter.appendChild(defaultOption);

      uniqueRoles.forEach(role => {
        const option = document.createElement('option');
        option.value = role;
        option.textContent = role;
        roleFilter.appendChild(option);
      });
    }

    if (modalRegionSelect) {
      modalRegionSelect.replaceChildren();
      const defaultOption = document.createElement('option');
      defaultOption.value = '';
      defaultOption.textContent = '— No Region —';
      modalRegionSelect.appendChild(defaultOption);

      this.regions.forEach(r => {
        const option = document.createElement('option');
        option.value = r.id;
        option.textContent = this.sanitizeText(r.name);
        modalRegionSelect.appendChild(option);
      });
    }

    if (modalStoreSelect) {
      modalStoreSelect.replaceChildren();
      const defaultOption = document.createElement('option');
      defaultOption.value = '';
      defaultOption.textContent = '— No Store —';
      modalStoreSelect.appendChild(defaultOption);

      this.stores.forEach(s => {
        const option = document.createElement('option');
        option.value = s.id;
        option.textContent = this.sanitizeText(s.name);
        modalStoreSelect.appendChild(option);
      });
    }
  }

  /**
   * Filter and render list of operator profile cards.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} [lifecycle]
   */
  _renderUserCardsList(container, lifecycle = this.lifecycle) {
    const cardsGrid = container.querySelector('#users-cards-grid');
    if (!cardsGrid) return;

    let filtered = [...this.users];

    // 1. Text Search Filter
    const query = this.searchQuery.toLowerCase().trim();
    if (query) {
      filtered = filtered.filter(u => 
        (u.firstName && u.firstName.toLowerCase().includes(query)) || 
        (u.lastName && u.lastName.toLowerCase().includes(query)) || 
        (u.email && u.email.toLowerCase().includes(query)) ||
        (u.designation && u.designation.toLowerCase().includes(query))
      );
    }

    // 2. Region selection filter
    if (this.selectedRegionFilter !== 'all') {
      filtered = filtered.filter(u => u.regionId === Number(this.selectedRegionFilter));
    }

    // 3. Designation role filter
    if (this.selectedRoleFilter !== 'all') {
      filtered = filtered.filter(u => u.designation === this.selectedRoleFilter);
    }

    cardsGrid.replaceChildren();

    if (filtered.length === 0) {
      const emptyTpl = container.querySelector('#users-empty-tpl');
      if (emptyTpl) {
        cardsGrid.appendChild(emptyTpl.content.cloneNode(true));
      }
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    const cardTpl = container.querySelector('#user-card-tpl');
    if (!cardTpl) return;

    filtered.forEach(u => {
      const clone = cardTpl.content.cloneNode(true);

      const avatarInitial = clone.querySelector('.user-avatar-initial');
      const userCode = clone.querySelector('.user-code');
      const userFullName = clone.querySelector('.user-fullname');
      const designationBadge = clone.querySelector('.user-designation-badge');
      const regionDomain = clone.querySelector('.region-domain');
      const assignedStore = clone.querySelector('.assigned-store');
      const contactEmail = clone.querySelector('.contact-email');
      const contactPhone = clone.querySelector('.contact-phone');
      const statusBadge = clone.querySelector('.user-status-badge');
      const hireDate = clone.querySelector('.user-hire-date');
      const deleteBtn = clone.querySelector('.btn-delete-user');
      const toggleBtn = clone.querySelector('.btn-toggle-user-state');

      const activeState = u.active;
      const statusText = activeState ? 'ACTIVE' : 'INACTIVE';
      
      const cleanFirstName = this.sanitizeText(u.firstName);
      const cleanLastName = this.sanitizeText(u.lastName);
      const cleanFullName = `${cleanFirstName} ${cleanLastName}`;
      const cleanRegion = u.regionName ? this.sanitizeText(u.regionName) : 'Central Hub';
      const cleanStore = u.storeName ? this.sanitizeText(u.storeName) : 'All Locations';
      const initial = cleanFirstName ? cleanFirstName.charAt(0).toUpperCase() : 'U';

      if (avatarInitial) avatarInitial.textContent = initial;
      if (userCode) userCode.textContent = `CODE: ${u.employeeCode}`;
      if (userFullName) {
        userFullName.textContent = cleanFullName;
        userFullName.setAttribute('title', cleanFullName);
      }

      if (designationBadge) {
        const isDirector = u.designation && u.designation.includes('Director');
        const isManager = u.designation && u.designation.includes('Manager');
        designationBadge.textContent = u.designation || 'Barista';
        designationBadge.className = `user-designation-badge user-designation-badge--${isDirector ? 'director' : (isManager ? 'manager' : 'default')}`;
      }

      if (regionDomain) {
        regionDomain.textContent = cleanRegion;
        regionDomain.setAttribute('title', cleanRegion);
      }
      if (assignedStore) {
        assignedStore.textContent = cleanStore;
        assignedStore.setAttribute('title', cleanStore);
      }

      if (contactEmail) {
        contactEmail.textContent = u.email || 'No email';
        contactEmail.setAttribute('title', u.email || '');
      }
      if (contactPhone) contactPhone.textContent = u.phone || 'No phone';
      if (hireDate) hireDate.textContent = `Joined: ${u.hireDate || 'Unknown'}`;

      if (statusBadge) {
        statusBadge.textContent = statusText;
        statusBadge.className = `user-status-badge user-status-badge--${activeState ? 'active' : 'inactive'}`;
      }

      if (deleteBtn) {
        deleteBtn.setAttribute('data-id', u.id);
        deleteBtn.setAttribute('data-name', cleanFullName);
      }

      if (toggleBtn) {
        toggleBtn.setAttribute('data-id', u.id);
        toggleBtn.setAttribute('data-active', String(activeState));
        toggleBtn.title = activeState ? 'Deactivate operator access' : 'Activate operator access';
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
    // 1. Soft-Delete Employee User
    const deleteButtons = container.querySelectorAll('.btn-delete-user');
    deleteButtons.forEach(btn => {
      if (btn.dataset.hasDeleteListener) return;
      btn.dataset.hasDeleteListener = 'true';

      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const name = btn.getAttribute('data-name');
        
        if (confirm(`Are you sure you want to delete the user operator profile for "${name}"?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/employees/${id}`);
            if (res && res.success) {
              notificationStore.success(`User "${name}" deleted successfully.`);
              await dashboardService.getDashboardOverview();
              await this.loadAndRender(container, lifecycle);
            } else {
              throw new Error(res.message || 'Operation rejected by backend API.');
            }
          } catch (err) {
            logger.error('UsersPage', `Failed to delete user ${name}:`, err);
            notificationStore.danger(`Deletion failed: ${err.message}`);
          }
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });

    // 2. Activate/Deactivate Toggle Switcher
    const toggleButtons = container.querySelectorAll('.btn-toggle-user-state');
    toggleButtons.forEach(btn => {
      if (btn.dataset.hasToggleListener) return;
      btn.dataset.hasToggleListener = 'true';

      const handleToggle = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const isActive = btn.getAttribute('data-active') === 'true';
        const actionText = isActive ? 'deactivate' : 'activate';
        
        try {
          const endpoint = `/api/v1/employees/${id}/${actionText}`;
          const res = await apiClient.patch(endpoint);
          
          if (res && res.success) {
            notificationStore.success(`User access ${actionText}d successfully.`);
            await dashboardService.getDashboardOverview();
            await this.loadAndRender(container, lifecycle);
          } else {
            throw new Error(res.message || 'State modification rejected by API.');
          }
        } catch (err) {
          logger.error('UsersPage', `Failed to toggle user state:`, err);
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
    const cssId = 'users-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/ultimate-admin/pages/users/users.css';
      document.head.appendChild(link);
    }
  }
}
export { UsersPage };
