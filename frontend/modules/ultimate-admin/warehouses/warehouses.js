/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/warehouses/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/warehouses, GET /api/v1/regions, POST /api/v1/warehouses
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

export default class WarehousesPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.warehouses = [];
    this.regions = [];
    this.selectedRegionFilter = 'all';
    this.searchQuery = '';
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('WarehousesPage', 'Syncing warehouses workspace with backend database...');

    try {
      // 1. Fetch raw warehouses from GET /api/v1/warehouses
      const warehousesRes = await apiClient.get('/api/v1/warehouses', { size: 100 });
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (warehousesRes && warehousesRes.success && warehousesRes.data && warehousesRes.data.content) {
        this.warehouses = warehousesRes.data.content;
      }
      
      // 2. Fetch raw regions from GET /api/v1/regions
      const regionsRes = await apiClient.get('/api/v1/regions', { size: 100 });
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (regionsRes && regionsRes.success && regionsRes.data && regionsRes.data.content) {
        this.regions = regionsRes.data.content;
      }
    } catch (err) {
      logger.error('WarehousesPage', 'Failed to synchronize data with server:', err);
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
    const activeWarehouses = this.warehouses.filter(w => w.active).length;
    const inactiveWarehouses = this.warehouses.length - activeWarehouses;
    const totalRegions = this.regions.length;

    container.innerHTML = `
      <div style="display:flex; flex-direction:column; gap: var(--spacing-lg); max-width: 1200px; margin: 0 auto; padding-bottom: var(--spacing-xxl);">
        
        <!-- Header row -->
        <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
          <div>
            <div class="flex align-center gap-xs" style="color: var(--accent-primary); font-weight: 700; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px;">
              <i data-lucide="archive" style="width: 14px; height: 14px;"></i> Logistics
            </div>
            <h1 style="font-family: var(--font-display); font-weight: 800; font-size: 1.8rem; margin: 0; color: var(--text-primary); letter-spacing: -0.01em;">Warehouses Management</h1>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0; margin-top: 4px;">Configure distribution hubs, central warehouses, physical topology mappings, and logistics status.</p>
          </div>
          <button id="btn-add-warehouse" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 18px; border-radius: var(--radius-md); border:none; display:flex; align-items:center; gap:6px; cursor:pointer;">
            <i data-lucide="plus" style="width:16px; height:16px;"></i> Add Warehouse
          </button>
        </div>

        <!-- KPI Summary row -->
        <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap: var(--spacing-md);">
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-primary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Warehouses</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${this.warehouses.length}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-success);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Active Logistics Hubs</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-success); margin-top: 6px;">${activeWarehouses}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-danger);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Suspended Hubs</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-danger); margin-top: 6px;">${inactiveWarehouses}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-secondary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Serviced Territories</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalRegions}</span>
          </div>
        </div>

        <!-- Filter bar -->
        <div class="card glass flex align-center justify-between" style="padding: var(--spacing-md); border-color: rgba(255,255,255,0.03); gap:var(--spacing-md); flex-wrap:wrap;">
          
          <!-- Search box -->
          <div style="display:flex; align-items:center; gap: var(--spacing-md); width: 100%; max-width: 320px; background: rgba(0,0,0,0.15); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 8px 12px;">
            <i data-lucide="search" style="width:16px; height:16px; color: var(--text-muted);"></i>
            <input id="input-search-warehouses" type="text" placeholder="Search hub name or code..." style="background:none; border:none; outline:none; color:var(--text-primary); font-size:0.8rem; width:100%;" value="${this.searchQuery}" />
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

        <!-- Warehouses list cards grid -->
        <div id="warehouses-cards-grid" style="display:grid; grid-template-columns: repeat(3, 1fr); gap: var(--spacing-md);">
          ${this.renderWarehouseCards()}
        </div>

        <!-- Add Warehouse Modal -->
        <div id="add-warehouse-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.7); z-index:99999; display:none; align-items:center; justify-content:center; padding: var(--spacing-lg); backdrop-filter: blur(4px);">
          <div class="card glass" style="width:100%; max-width: 450px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.5);">
            <div class="flex justify-between align-center mb-md" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.15rem; margin:0; color:var(--text-primary);">Create New Warehouse</h3>
              <button id="btn-close-warehouse-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.25rem; line-height:1;">&times;</button>
            </div>
            
            <form id="form-add-warehouse" style="display:flex; flex-direction:column; gap: var(--spacing-sm); text-align:left;">
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Warehouse Name</label>
                <input id="wh-modal-name" type="text" placeholder="e.g. Marseille Logistics Hub" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>
              
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Territory Region</label>
                <select id="wh-modal-region" required style="padding:8px 10px; border-radius:var(--radius-md); background:var(--bg-card); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; cursor:pointer;">
                  <option value="" disabled selected>-- Select Region --</option>
                  ${this.regions.map(r => `<option value="${r.id}">${this.sanitizeText(r.name)}</option>`).join('')}
                </select>
              </div>

              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Address</label>
                <input id="wh-modal-address" type="text" placeholder="e.g. 10 Avenue du Fret, 13000 Marseille" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>

              <div style="display:grid; grid-template-columns: 1fr 1fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Phone Number</label>
                  <input id="wh-modal-phone" type="text" placeholder="+33-4-000-00" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Warehouse Email</label>
                  <input id="wh-modal-email" type="email" placeholder="warehouse@plus33.com" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
              </div>

              <div style="display:grid; grid-template-columns: 1.2fr 0.8fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Timezone</label>
                  <input id="wh-modal-timezone" type="text" value="Europe/Paris" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Opening Date</label>
                  <input id="wh-modal-date" type="date" required style="padding:7px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
              </div>
              
              <button id="btn-wh-submit" type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.8rem; padding:12px; margin-top:var(--spacing-sm); border:none; border-radius:var(--radius-md); cursor:pointer;">
                Register Warehouse
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
  renderWarehouseCards() {
    let filtered = [...this.warehouses];

    // 1. Text Search Filter
    const query = this.searchQuery.toLowerCase().trim();
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (query) {
      filtered = filtered.filter(w => 
        w.name.toLowerCase().includes(query) || 
        w.code.toLowerCase().includes(query)
      );
    }

    // 2. Region selection filter
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (this.selectedRegionFilter !== 'all') {
      filtered = filtered.filter(w => w.regionId === Number(this.selectedRegionFilter));
    }

    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (filtered.length === 0) {
      return `
        <div class="card glass col-12 text-center" style="grid-column: span 3; padding: var(--spacing-xl); color: var(--text-muted); font-size: 0.85rem;">
          <i data-lucide="info" style="width:24px; height:24px; margin: 0 auto 10px auto; color: var(--accent-primary);"></i>
          No matching logistics locations found.
        </div>
      `;
    }

    return filtered.map(w => {
      const activeState = w.active;
      const statusText = activeState ? 'ACTIVE' : 'INACTIVE';
      const statusColor = activeState ? 'var(--status-success)' : 'var(--status-danger)';
      const statusBg = activeState ? 'rgba(16,185,129,0.06)' : 'rgba(239,68,68,0.06)';
      const cleanName = this.sanitizeText(w.name);
      const cleanAddress = this.sanitizeText(w.address);

      return `
        <div class="card glass animate-slide-up flex flex-col justify-between" style="gap: var(--spacing-md); position:relative; min-height: 180px;">
          
          <!-- Delete and State Action Buttons (top-right) -->
          <div style="position:absolute; top: 12px; right: 12px; display:flex; align-items:center; gap:4px;">
            <!-- Active State Toggle Switcher -->
            <button class="btn-toggle-wh-state" data-id="${w.id}" data-active="${activeState}" title="${activeState ? 'Deactivate hub' : 'Activate hub'}" style="background:none; border:none; color:${statusColor}; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 5px; border-radius:var(--radius-sm);">
              <i data-lucide="${activeState ? 'toggle-right' : 'toggle-left'}" style="width:18px; height:18px; stroke-width:2.5;"></i>
            </button>
            <!-- Delete Soft-Deactivate Button -->
            <button class="btn-delete-wh" data-id="${w.id}" data-name="${cleanName}" title="Delete Warehouse hub" style="background:none; border:none; color:var(--status-danger); opacity:0.6; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 5px; border-radius:var(--radius-sm);" onmouseover="this.style.opacity='1'; this.style.background='rgba(239,68,68,0.1)'" onmouseout="this.style.opacity='0.6'; this.style.background='none'">
              <i data-lucide="trash-2" style="width:13px; height:13px; stroke-width:2.5;"></i>
            </button>
          </div>

          <!-- Top row Info -->
          <div style="padding-right: 48px;">
            <div style="font-size:0.62rem; color: var(--text-muted); font-weight:700; letter-spacing:0.04em;">CODE: ${w.code}</div>
            <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 0.95rem; margin: 4px 0 0 0; color: var(--text-primary); line-height: 1.25;" title="${cleanName}">${cleanName}</h3>
            
            <!-- Region Badge -->
            <div class="flex align-center gap-xs mt-sm" style="display:inline-flex; background:rgba(255,255,255,0.03); border:1px solid var(--border-color); border-radius:4px; padding: 2px 8px; font-size: 0.65rem; font-weight:700; color: var(--text-secondary);">
              <i data-lucide="map-pin" style="width:10px; height:10px; color: var(--accent-secondary);"></i> ${this.sanitizeText(w.regionCode || 'IDF')}
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
              <span>${w.phone || 'No phone'}</span>
            </div>
            <div class="flex align-center gap-xs" style="color:var(--text-secondary);">
              <i data-lucide="mail" style="width:11px; height:11px; color:var(--text-muted); flex-shrink:0;"></i>
              <span style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="${w.email}">${w.email || 'No email'}</span>
            </div>
          </div>

          <!-- State & Opening Date Row -->
          <div class="flex justify-between align-center mt-sm" style="border-top:1px solid rgba(255,255,255,0.05); padding-top:var(--spacing-sm); font-size: 0.65rem;">
            <div style="background:${statusBg}; border: 1px solid ${statusColor}; color:${statusColor}; font-weight:700; border-radius:3px; padding: 2px 6px; letter-spacing:0.04em;">
              ${statusText}
            </div>
            <div style="color:var(--text-muted);">
              Opened: ${w.openingDate || 'Unknown'}
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
    const searchInput = container.querySelector('#input-search-warehouses');
    const regionFilter = container.querySelector('#select-filter-region');
    const cardsGrid = container.querySelector('#warehouses-cards-grid');
    const btnAddWarehouse = container.querySelector('#btn-add-warehouse');
    const modal = container.querySelector('#add-warehouse-modal');
    const btnCloseModal = container.querySelector('#btn-close-warehouse-modal');
    const formAddWarehouse = container.querySelector('#form-add-warehouse');
    const submitBtn = container.querySelector('#btn-wh-submit');

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

    // 3. Open Add Warehouse Modal
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnAddWarehouse && modal) {
      /**
       * Performs the openModal operation in this module.
       * @memberof Pages Module
       */
      const openModal = () => {
        modal.style.display = 'flex';
        const dateInput = container.querySelector('#wh-modal-date');
        if (dateInput) dateInput.value = new Date().toISOString().substring(0, 10);
      };
      btnAddWarehouse.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddWarehouse.removeEventListener('click', openModal));
    }

    // 4. Close Add Warehouse Modal
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

    // 5. Form Submit connection to POST /api/v1/warehouses
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (formAddWarehouse && modal) {
      /**
       * Handles the handle submit event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleSubmit = async (e) => {
        e.preventDefault();

        const name = container.querySelector('#wh-modal-name').value.trim();
        const regionId = Number(container.querySelector('#wh-modal-region').value);
        const address = container.querySelector('#wh-modal-address').value.trim();
        const phone = container.querySelector('#wh-modal-phone').value.trim();
        const email = container.querySelector('#wh-modal-email').value.trim();
        const timezone = container.querySelector('#wh-modal-timezone').value.trim();
        const openingDate = container.querySelector('#wh-modal-date').value;

        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
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

          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (response && response.success) {
            // Force dynamic metrics updates
            await dashboardService.getDashboardOverview();
            
            // Hide modal and reset form
            modal.style.display = 'none';
            formAddWarehouse.reset();

            // Refresh UI
            await this.mount(container, lifecycle);

            notificationStore.success(`Distribution warehouse '${name}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by backend API.');
          }

        } catch (err) {
          logger.error('WarehousesPage', 'Failed to register warehouse:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Warehouse';
          }
        }
      };

      formAddWarehouse.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddWarehouse.removeEventListener('submit', handleSubmit));
    }

    // 6. Bind Actions (Deactivate & Delete)
    this.bindGridActions(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  refreshGrid(container, lifecycle) {
    const cardsGrid = container.querySelector('#warehouses-cards-grid');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (cardsGrid) {
      cardsGrid.innerHTML = this.renderWarehouseCards();
      if (window.lucide) window.lucide.createIcons();
      this.bindGridActions(container, lifecycle);
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindGridActions(container, lifecycle) {
    // 1. Soft-Delete Warehouse
    const deleteButtons = container.querySelectorAll('.btn-delete-wh');
    deleteButtons.forEach(btn => {
      /**
       * Handles the handle delete event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const name = btn.getAttribute('data-name');
        
        if (confirm(`Are you sure you want to delete the warehouse "${name}"?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/warehouses/${id}`);
            /**
             * Performs the fn operation in this module.
             * @memberof Pages Module
             */
            if (res && res.success) {
              notificationStore.success(`Warehouse "${name}" deleted successfully.`);
              
              // Force metrics update and reload
              await dashboardService.getDashboardOverview();
              await this.mount(container, lifecycle);
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
          const endpoint = `/api/v1/warehouses/${id}/${actionText}`;
          const res = await apiClient.patch(endpoint);
          
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (res && res.success) {
            notificationStore.success(`Warehouse state changed to ${actionText}d successfully.`);
            
            // Force metrics update and reload
            await dashboardService.getDashboardOverview();
            await this.mount(container, lifecycle);
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
}



