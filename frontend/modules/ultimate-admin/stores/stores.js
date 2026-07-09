/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/stores/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/stores, GET /api/v1/regions, POST /api/v1/stores
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client, core/logger, store/notificationStore, services/dashboard/DashboardService
 * Depends On        : api/client, core/logger, store/notificationStore, services/dashboard/DashboardService
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../../api/client.js';
import { logger } from '../../../core/logger.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { dashboardService } from '../../../services/dashboard/DashboardService.js';
import { authStore } from '../../../store/authStore.js';

export default class StoresPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.stores = [];
    this.regions = [];
    this.selectedRegionFilter = 'all';
    this.searchQuery = '';
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('StoresPage', 'Syncing stores workspace with backend database...');

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

    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  sanitizeText(text) {
    if (!text) return '';
    return text
      .replace(/\??/g, ' ')
      .replace(/ǩ/g, 'î')
      .replace(/Ǹ/g, 'é')
      .replace(/Ǫ/g, 'è');
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  render(container) {
    const activeStores = this.stores.filter(s => s.active).length;
    const inactiveStores = this.stores.length - activeStores;
    const totalRegions = this.regions.length;

    container.innerHTML = `
      <div style="display:flex; flex-direction:column; gap: var(--spacing-lg); max-width: 1200px; margin: 0 auto; padding-bottom: var(--spacing-xxl);">
        
        <!-- Header row -->
        <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
          <div>
            <div class="flex align-center gap-xs" style="color: var(--accent-primary); font-weight: 700; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px;">
              <i data-lucide="coffee" style="width: 14px; height: 14px;"></i> Operations
            </div>
            <h1 style="font-family: var(--font-display); font-weight: 800; font-size: 1.8rem; margin: 0; color: var(--text-primary); letter-spacing: -0.01em;">Stores Management</h1>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0; margin-top: 4px;">Configure local coffee house franchise locations, operational state, and regional mappings.</p>
          </div>
          <button id="btn-add-store" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 18px; border-radius: var(--radius-md); border:none; display:flex; align-items:center; gap:6px; cursor:pointer;">
            <i data-lucide="plus" style="width:16px; height:16px;"></i> Add Store
          </button>
        </div>

        <!-- KPI Summary row -->
        <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap: var(--spacing-md);">
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-primary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Franchise Stores</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${this.stores.length}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-success);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Active Locations</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-success); margin-top: 6px;">${activeStores}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-danger);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Inactive/Suspended</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-danger); margin-top: 6px;">${inactiveStores}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-secondary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Represented Regions</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalRegions}</span>
          </div>
        </div>

        <!-- Filter bar -->
        <div class="card glass flex align-center justify-between" style="padding: var(--spacing-md); border-color: rgba(255,255,255,0.03); gap:var(--spacing-md); flex-wrap:wrap;">
          
          <!-- Search box -->
          <div style="display:flex; align-items:center; gap: var(--spacing-md); width: 100%; max-width: 320px; background: rgba(0,0,0,0.15); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 8px 12px;">
            <i data-lucide="search" style="width:16px; height:16px; color: var(--text-muted);"></i>
            <input id="input-search-stores" type="text" placeholder="Search store name or code..." style="background:none; border:none; outline:none; color:var(--text-primary); font-size:0.8rem; width:100%;" value="${this.searchQuery}" />
          </div>

          <!-- Region selection dropdown filter -->
          <div class="flex align-center gap-xs" style="font-size: 0.8rem;">
            <span style="color:var(--text-muted); font-weight:600;">Filter Territory:</span>
            <select id="select-filter-region" style="background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-secondary); padding: 6px 12px; border-radius:var(--radius-md); outline:none; cursor:pointer;">
              <option value="all" ${this.selectedRegionFilter === 'all' ? 'selected' : ''}>All Regions</option>
              ${this.regions.map(r => `<option value="${r.id}" ${Number(this.selectedRegionFilter) === r.id ? 'selected' : ''}>${this.sanitizeText(r.name)}</option>`).join('')}
            </select>
          </div>
        </div>

        <!-- Stores list cards grid -->
        <div id="stores-cards-grid" style="display:grid; grid-template-columns: repeat(3, 1fr); gap: var(--spacing-md);">
          ${this.renderStoreCards()}
        </div>

        <!-- Add Store Modal -->
        <div id="add-store-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.7); z-index:99999; display:none; align-items:center; justify-content:center; padding: var(--spacing-lg); backdrop-filter: blur(4px);">
          <div class="card glass" style="width:100%; max-width: 450px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.5);">
            <div class="flex justify-between align-center mb-md" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.15rem; margin:0; color:var(--text-primary);">Create New Store</h3>
              <button id="btn-close-store-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.25rem; line-height:1;">&times;</button>
            </div>
            
            <form id="form-add-store" style="display:flex; flex-direction:column; gap: var(--spacing-sm); text-align:left;">
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Store Name</label>
                <input id="store-modal-name" type="text" placeholder="e.g. PLUS33 Coffee - Versailles" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>
              
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Territory Region</label>
                <select id="store-modal-region" required style="padding:8px 10px; border-radius:var(--radius-md); background:var(--bg-card); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; cursor:pointer;">
                  <option value="" disabled selected>-- Select Region --</option>
                  ${this.regions.map(r => `<option value="${r.id}">${this.sanitizeText(r.name)}</option>`).join('')}
                </select>
              </div>

              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Address</label>
                <input id="store-modal-address" type="text" placeholder="e.g. 5 Place d'Armes, 78000 Versailles" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>

              <div style="display:grid; grid-template-columns: 1fr 1fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Phone Number</label>
                  <input id="store-modal-phone" type="text" placeholder="+33-1-000-00" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Store Email</label>
                  <input id="store-modal-email" type="email" placeholder="store@plus33.com" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
              </div>

              <div style="display:grid; grid-template-columns: 1.2fr 0.8fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Timezone</label>
                  <input id="store-modal-timezone" type="text" value="Europe/Paris" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Opening Date</label>
                  <input id="store-modal-date" type="date" required style="padding:7px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
              </div>
              
              <button id="btn-store-submit" type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.8rem; padding:12px; margin-top:var(--spacing-sm); border:none; border-radius:var(--radius-md); cursor:pointer;">
                Register Store
              </button>
            </form>
          </div>
        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderStoreCards() {
    let filtered = [...this.stores];

    // 1. Text Search query filter
    const query = this.searchQuery.toLowerCase().trim();
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (query) {
      filtered = filtered.filter(s => 
        s.name.toLowerCase().includes(query) || 
        s.code.toLowerCase().includes(query)
      );
    }

    // 2. Region selection filter
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (this.selectedRegionFilter !== 'all') {
      filtered = filtered.filter(s => s.regionId === Number(this.selectedRegionFilter));
    }

    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (filtered.length === 0) {
      return `
        <div class="card glass col-12 text-center" style="grid-column: span 3; padding: var(--spacing-xl); color: var(--text-muted); font-size: 0.85rem;">
          <i data-lucide="info" style="width:24px; height:24px; margin: 0 auto 10px auto; color: var(--accent-primary);"></i>
          No matching store locations found.
        </div>
      `;
    }

    return filtered.map(s => {
      const activeState = s.active;
      const statusText = activeState ? 'ACTIVE' : 'INACTIVE';
      const statusColor = activeState ? 'var(--status-success)' : 'var(--status-danger)';
      const statusBg = activeState ? 'rgba(16,185,129,0.06)' : 'rgba(239,68,68,0.06)';
      const cleanName = this.sanitizeText(s.name);
      const cleanAddress = this.sanitizeText(s.address);

      return `
        <div class="card glass animate-slide-up flex flex-col justify-between" style="gap: var(--spacing-md); position:relative; min-height: 180px;">
          
          <!-- Delete and State Action Buttons (top-right) -->
          <div style="position:absolute; top: 12px; right: 12px; display:flex; align-items:center; gap:4px;">
            <!-- Active State Toggle Switcher -->
            <button class="btn-toggle-store-state" data-id="${s.id}" data-active="${activeState}" title="${activeState ? 'Deactivate location' : 'Activate location'}" style="background:none; border:none; color:${statusColor}; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 5px; border-radius:var(--radius-sm);">
              <i data-lucide="${activeState ? 'toggle-right' : 'toggle-left'}" style="width:18px; height:18px; stroke-width:2.5;"></i>
            </button>
            <!-- Delete Soft-Deactivate Button -->
            ${authStore.getRole() !== 'nationalAdmin' ? `
              <button class="btn-delete-store" data-id="${s.id}" data-name="${cleanName}" title="Delete Store location" style="background:none; border:none; color:var(--status-danger); opacity:0.6; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 5px; border-radius:var(--radius-sm);" onmouseover="this.style.opacity='1'; this.style.background='rgba(239,68,68,0.1)'" onmouseout="this.style.opacity='0.6'; this.style.background='none'">
                <i data-lucide="trash-2" style="width:13px; height:13px; stroke-width:2.5;"></i>
              </button>
            ` : ''}
          </div>

          <!-- Top row Info -->
          <div style="padding-right: 48px;">
            <div style="font-size:0.62rem; color: var(--text-muted); font-weight:700; letter-spacing:0.04em;">CODE: ${s.code}</div>
            <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 0.95rem; margin: 4px 0 0 0; color: var(--text-primary); line-height: 1.25;" title="${cleanName}">${cleanName}</h3>
            
            <!-- Region Badge -->
            <div class="flex align-center gap-xs mt-sm" style="display:inline-flex; background:rgba(255,255,255,0.03); border:1px solid var(--border-color); border-radius:4px; padding: 2px 8px; font-size: 0.65rem; font-weight:700; color: var(--text-secondary);">
              <i data-lucide="map-pin" style="width:10px; height:10px; color: var(--accent-secondary);"></i> ${this.sanitizeText(s.regionCode || 'IDF')}
            </div>
          </div>

          <!-- Contact details block -->
          <div style="display:flex; flex-direction:column; gap: var(--spacing-xs); font-size: 0.7rem; border-top:1px solid rgba(255,255,255,0.05); padding-top:var(--spacing-sm); margin-top: auto;">
            <div class="flex align-center gap-xs" style="color:var(--text-secondary);">
              <i data-lucide="navigation" style="width:11px; height:11px; color:var(--text-muted); flex-shrink:0;"></i>
              <span style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="${cleanAddress}">${cleanAddress}</span>
            </div>
            <div class="flex align-center gap-xs" style="color:var(--text-secondary);">
              <i data-lucide="phone" style="width:11px; height:11px; color:var(--text-muted); flex-shrink:0;"></i>
              <span>${s.phone || 'No phone'}</span>
            </div>
            <div class="flex align-center gap-xs" style="color:var(--text-secondary);">
              <i data-lucide="mail" style="width:11px; height:11px; color:var(--text-muted); flex-shrink:0;"></i>
              <span style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="${s.email}">${s.email || 'No email'}</span>
            </div>
          </div>

          <!-- State & Opening Date Row -->
          <div class="flex justify-between align-center mt-sm" style="border-top:1px solid rgba(255,255,255,0.05); padding-top:var(--spacing-sm); font-size: 0.65rem;">
            <div style="background:${statusBg}; border: 1px solid ${statusColor}; color:${statusColor}; font-weight:700; border-radius:3px; padding: 2px 6px; letter-spacing:0.04em;">
              ${statusText}
            </div>
            <div style="color:var(--text-muted);">
              Opened: ${s.openingDate || 'Unknown'}
            </div>
          </div>

        </div>
      `;
    }).join('');
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#input-search-stores');
    const regionFilter = container.querySelector('#select-filter-region');
    const cardsGrid = container.querySelector('#stores-cards-grid');
    const btnAddStore = container.querySelector('#btn-add-store');
    const modal = container.querySelector('#add-store-modal');
    const btnCloseModal = container.querySelector('#btn-close-store-modal');
    const formAddStore = container.querySelector('#form-add-store');
    const submitBtn = container.querySelector('#btn-store-submit');

    // 1. Text Search Input
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (searchInput && cardsGrid) {
      /**
       * Handles the handle search event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleSearch = (e) => {
        this.searchQuery = e.target.value;
        this.refreshGrid(container, lifecycle);
      };
      searchInput.addEventListener('input', handleSearch);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', handleSearch));
    }

    // 2. Region Dropdown Filter
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (regionFilter && cardsGrid) {
      /**
       * Handles the handle region filter event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleRegionFilter = (e) => {
        this.selectedRegionFilter = e.target.value;
        this.refreshGrid(container, lifecycle);
      };
      regionFilter.addEventListener('change', handleRegionFilter);
      lifecycle.onCleanup(() => regionFilter.removeEventListener('change', handleRegionFilter));
    }

    // 3. Open Add Store Modal
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnAddStore && modal) {
      /**
       * Performs the openModal operation in this module.
       * @memberof Pages Module
       */
      const openModal = () => {
        modal.style.display = 'flex';
        // Auto-set opening date to today's date format (YYYY-MM-DD)
        const dateInput = container.querySelector('#store-modal-date');
        if (dateInput) dateInput.value = new Date().toISOString().substring(0, 10);
      };
      btnAddStore.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddStore.removeEventListener('click', openModal));
    }

    // 4. Close Add Store Modal
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnCloseModal && modal) {
      /**
       * Completes the close modal workflow and finalizes the record status.
       * @memberof Pages Module
       */
      const closeModal = () => {
        modal.style.display = 'none';
      };
      btnCloseModal.addEventListener('click', closeModal);
      lifecycle.onCleanup(() => btnCloseModal.removeEventListener('click', closeModal));

      // Close on backdrop click
      /**
       * Performs the backdropClose operation in this module.
       * @memberof Pages Module
       */
      const backdropClose = (e) => {
        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
        if (e.target === modal) {
          modal.style.display = 'none';
        }
      };
      modal.addEventListener('click', backdropClose);
      lifecycle.onCleanup(() => modal.removeEventListener('click', backdropClose));
    }

    // 5. Form Submit connection to POST /api/v1/stores
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (formAddStore && modal) {
      /**
       * Handles the handle submit event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleSubmit = async (e) => {
        e.preventDefault();

        const name = container.querySelector('#store-modal-name').value.trim();
        const regionId = Number(container.querySelector('#store-modal-region').value);
        const address = container.querySelector('#store-modal-address').value.trim();
        const phone = container.querySelector('#store-modal-phone').value.trim();
        const email = container.querySelector('#store-modal-email').value.trim();
        const timezone = container.querySelector('#store-modal-timezone').value.trim();
        const openingDate = container.querySelector('#store-modal-date').value;

        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
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
            active: true
          });

          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (response && response.success) {
            // Force dynamic metrics updates
            await dashboardService.getDashboardOverview();
            
            // Hide modal and reset form
            modal.style.display = 'none';
            formAddStore.reset();

            // Refresh UI components
            await this.mount(container, lifecycle);

            notificationStore.success(`Franchise store '${name}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by backend API.');
          }

        } catch (err) {
          logger.error('StoresPage', 'Failed to register store:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Store';
          }
        }
      };

      formAddStore.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddStore.removeEventListener('submit', handleSubmit));
    }

    // 6. Bind Actions inside current grid (Deactivate & Delete)
    this.bindGridActions(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  refreshGrid(container, lifecycle) {
    const cardsGrid = container.querySelector('#stores-cards-grid');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (cardsGrid) {
      cardsGrid.innerHTML = this.renderStoreCards();
      if (window.lucide) window.lucide.createIcons();
      this.bindGridActions(container, lifecycle); // Re-bind grid actions for filtered items
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindGridActions(container, lifecycle) {
    // 1. Soft-Delete Store
    const deleteButtons = container.querySelectorAll('.btn-delete-store');
    deleteButtons.forEach(btn => {
      /**
       * Handles the handle delete event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const name = btn.getAttribute('data-name');
        
        if (confirm(`Are you sure you want to delete the store "${name}"?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/stores/${id}`);
            /**
             * Performs the fn operation in this module.
             * @memberof Pages Module
             */
            if (res && res.success) {
              notificationStore.success(`Store "${name}" deleted successfully.`);
              
              // Force dashboard metrics update and reload page
              await dashboardService.getDashboardOverview();
              await this.mount(container, lifecycle);
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
      /**
       * Handles the handle toggle event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleToggle = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const isActive = btn.getAttribute('data-active') === 'true';
        const actionText = isActive ? 'deactivate' : 'activate';
        
        try {
          const endpoint = `/api/v1/stores/${id}/${actionText}`;
          const res = await apiClient.patch(endpoint);
          
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (res && res.success) {
            notificationStore.success(`Location state changed to ${actionText}d successfully.`);
            
            // Force dashboard metrics update and reload page
            await dashboardService.getDashboardOverview();
            await this.mount(container, lifecycle);
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
}



