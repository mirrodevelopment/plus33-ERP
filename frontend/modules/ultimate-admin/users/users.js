/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/users/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/employees, GET /api/v1/regions, GET /api/v1/stores, POST /api/v1/employees
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

export default class UsersPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.users = [];
    this.regions = [];
    this.stores = [];
    this.searchQuery = '';
    this.selectedRegionFilter = 'all';
    this.selectedRoleFilter = 'all';
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('UsersPage', 'Syncing workforce workspace with backend database...');

    try {
      // 1. Fetch raw employees from GET /api/v1/employees
      const employeesRes = await apiClient.get('/api/v1/employees', { size: 100 });
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (employeesRes && employeesRes.success && employeesRes.data && employeesRes.data.content) {
        this.users = employeesRes.data.content;
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

      // 3. Fetch raw stores from GET /api/v1/stores
      const storesRes = await apiClient.get('/api/v1/stores', { size: 100 });
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (storesRes && storesRes.success && storesRes.data && storesRes.data.content) {
        this.stores = storesRes.data.content;
      }
    } catch (err) {
      logger.error('UsersPage', 'Failed to synchronize data with server:', err);
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
    const totalUsers = this.users.length;
    const activeUsers = this.users.filter(u => u.active).length;
    const permanentUsers = this.users.filter(u => u.employmentType === 'PERMANENT').length;
    
    // Get unique designations/roles
    const uniqueRoles = [...new Set(this.users.map(u => u.designation))].filter(Boolean);

    container.innerHTML = `
      <div style="display:flex; flex-direction:column; gap: var(--spacing-lg); max-width: 1200px; margin: 0 auto; padding-bottom: var(--spacing-xxl);">
        
        <!-- Header row -->
        <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
          <div>
            <div class="flex align-center gap-xs" style="color: var(--accent-primary); font-weight: 700; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px;">
              <i data-lucide="users" style="width: 14px; height: 14px;"></i> Administration
            </div>
            <h1 style="font-family: var(--font-display); font-weight: 800; font-size: 1.8rem; margin: 0; color: var(--text-primary); letter-spacing: -0.01em;">Users & Roles</h1>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0; margin-top: 4px;">Manage operator login access, system security profiles, and regional/store workforce scoping.</p>
          </div>
          <button id="btn-add-user" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 18px; border-radius: var(--radius-md); border:none; display:flex; align-items:center; gap:6px; cursor:pointer;">
            <i data-lucide="plus" style="width:16px; height:16px;"></i> Add User
          </button>
        </div>

        <!-- KPI Summary row -->
        <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap: var(--spacing-md);">
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-primary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Total Enterprise Staff</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalUsers}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-success);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Active Operators</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-success); margin-top: 6px;">${activeUsers}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-secondary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Permanent Contracts</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${permanentUsers}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-info);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Mapped Designations</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-info); margin-top: 6px;">${uniqueRoles.length}</span>
          </div>
        </div>

        <!-- Filter bar -->
        <div class="card glass flex align-center justify-between" style="padding: var(--spacing-md); border-color: rgba(255,255,255,0.03); gap:var(--spacing-md); flex-wrap:wrap;">
          
          <!-- Search box -->
          <div style="display:flex; align-items:center; gap: var(--spacing-md); width: 100%; max-width: 300px; background: rgba(0,0,0,0.15); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 8px 12px;">
            <i data-lucide="search" style="width:16px; height:16px; color: var(--text-muted);"></i>
            <input id="input-search-users" type="text" placeholder="Search user name, email..." style="background:none; border:none; outline:none; color:var(--text-primary); font-size:0.8rem; width:100%;" value="${this.searchQuery}" />
          </div>

          <!-- Region selection dropdown filter -->
          <div style="display:flex; align-items:center; gap:var(--spacing-md); flex-wrap:wrap;">
            <div class="flex align-center gap-xs" style="font-size: 0.8rem;">
              <span style="color:var(--text-muted); font-weight:600;">Territory:</span>
              <select id="select-filter-region" style="background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-secondary); padding: 6px 12px; border-radius:var(--radius-md); outline:none; cursor:pointer;">
                <option value="all" ${this.selectedRegionFilter === 'all' ? 'selected' : ''}>All Regions</option>
                ${this.regions.map(r => `<option value="${r.id}" ${Number(this.selectedRegionFilter) === r.id ? 'selected' : ''}>${this.sanitizeText(r.name)}</option>`).join('')}
              </select>
            </div>

            <!-- Role filter -->
            <div class="flex align-center gap-xs" style="font-size: 0.8rem;">
              <span style="color:var(--text-muted); font-weight:600;">System Role:</span>
              <select id="select-filter-role" style="background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-secondary); padding: 6px 12px; border-radius:var(--radius-md); outline:none; cursor:pointer;">
                <option value="all" ${this.selectedRoleFilter === 'all' ? 'selected' : ''}>All Roles</option>
                ${uniqueRoles.map(r => `<option value="${r}" ${this.selectedRoleFilter === r ? 'selected' : ''}>${r}</option>`).join('')}
              </select>
            </div>
          </div>

        </div>

        <!-- Users grid -->
        <div id="users-cards-grid" style="display:grid; grid-template-columns: repeat(3, 1fr); gap: var(--spacing-md);">
          ${this.renderUserCards()}
        </div>

        <!-- Add User Modal -->
        <div id="add-user-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.7); z-index:99999; display:none; align-items:center; justify-content:center; padding: var(--spacing-lg); backdrop-filter: blur(4px);">
          <div class="card glass" style="width:100%; max-width: 480px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.5);">
            <div class="flex justify-between align-center mb-md" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.15rem; margin:0; color:var(--text-primary);">Register New User Profile</h3>
              <button id="btn-close-user-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.25rem; line-height:1;">&times;</button>
            </div>
            
            <form id="form-add-user" style="display:flex; flex-direction:column; gap: var(--spacing-sm); text-align:left;">
              <div style="display:grid; grid-template-columns: 1fr 1fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">First Name</label>
                  <input id="user-modal-firstname" type="text" placeholder="John" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Last Name</label>
                  <input id="user-modal-lastname" type="text" placeholder="Doe" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
              </div>

              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">User Email</label>
                <input id="user-modal-email" type="email" placeholder="john.doe@plus33coffee.fr" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>

              <div style="display:grid; grid-template-columns: 1.1fr 0.9fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">System Role / Designation</label>
                  <select id="user-modal-designation" required style="padding:8px 10px; border-radius:var(--radius-md); background:var(--bg-card); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; cursor:pointer;">
                    <option value="Regional Director">Regional Director</option>
                    <option value="Store Manager">Store Manager</option>
                    <option value="Warehouse Admin">Warehouse Admin</option>
                    <option value="Barista">Barista</option>
                    <option value="Operations Manager">Operations Manager</option>
                  </select>
                </div>
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Employment Type</label>
                  <select id="user-modal-emptype" required style="padding:8px 10px; border-radius:var(--radius-md); background:var(--bg-card); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; cursor:pointer;">
                    <option value="PERMANENT">PERMANENT</option>
                    <option value="FULL_TIME">FULL_TIME</option>
                    <option value="PART_TIME">PART_TIME</option>
                    <option value="CONTRACTOR">CONTRACTOR</option>
                  </select>
                </div>
              </div>

              <div style="display:grid; grid-template-columns: 1fr 1fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Assigned Region</label>
                  <select id="user-modal-region" style="padding:8px 10px; border-radius:var(--radius-md); background:var(--bg-card); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; cursor:pointer;">
                    <option value="">-- No Region --</option>
                    ${this.regions.map(r => `<option value="${r.id}">${this.sanitizeText(r.name)}</option>`).join('')}
                  </select>
                </div>
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Assigned Store</label>
                  <select id="user-modal-store" style="padding:8px 10px; border-radius:var(--radius-md); background:var(--bg-card); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; cursor:pointer;">
                    <option value="">-- No Store --</option>
                    ${this.stores.map(s => `<option value="${s.id}">${this.sanitizeText(s.name)}</option>`).join('')}
                  </select>
                </div>
              </div>

              <div style="display:grid; grid-template-columns: 1fr 1fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Phone Number</label>
                  <input id="user-modal-phone" type="text" placeholder="+33-1-000-00" style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Hire Date</label>
                  <input id="user-modal-date" type="date" required style="padding:7px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
              </div>

              <button id="btn-user-submit" type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.8rem; padding:12px; margin-top:var(--spacing-sm); border:none; border-radius:var(--radius-md); cursor:pointer;">
                Register User Profile
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
  renderUserCards() {
    let filtered = [...this.users];

    // 1. Text Search Filter
    const query = this.searchQuery.toLowerCase().trim();
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (query) {
      filtered = filtered.filter(u => 
        u.firstName.toLowerCase().includes(query) || 
        u.lastName.toLowerCase().includes(query) || 
        u.email.toLowerCase().includes(query) ||
        (u.designation && u.designation.toLowerCase().includes(query))
      );
    }

    // 2. Region selection filter
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (this.selectedRegionFilter !== 'all') {
      filtered = filtered.filter(u => u.regionId === Number(this.selectedRegionFilter));
    }

    // 3. Designation role filter
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (this.selectedRoleFilter !== 'all') {
      filtered = filtered.filter(u => u.designation === this.selectedRoleFilter);
    }

    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (filtered.length === 0) {
      return `
        <div class="card glass col-12 text-center" style="grid-column: span 3; padding: var(--spacing-xl); color: var(--text-muted); font-size: 0.85rem;">
          <i data-lucide="info" style="width:24px; height:24px; margin: 0 auto 10px auto; color: var(--accent-primary);"></i>
          No matching operator user profiles found.
        </div>
      `;
    }

    return filtered.map(u => {
      const activeState = u.active;
      const statusText = activeState ? 'ACTIVE' : 'INACTIVE';
      const statusColor = activeState ? 'var(--status-success)' : 'var(--status-danger)';
      const statusBg = activeState ? 'rgba(16,185,129,0.06)' : 'rgba(239,68,68,0.06)';
      
      const cleanFirstName = this.sanitizeText(u.firstName);
      const cleanLastName = this.sanitizeText(u.lastName);
      const cleanFullName = `${cleanFirstName} ${cleanLastName}`;
      const cleanRegion = u.regionName ? this.sanitizeText(u.regionName) : 'Central Hub';
      const cleanStore = u.storeName ? this.sanitizeText(u.storeName) : 'All Locations';

      // Avatar styling based on first initials
      const initial = cleanFirstName ? cleanFirstName.charAt(0).toUpperCase() : 'U';

      // Dynamic color styling for role badges
      const isDirector = u.designation && u.designation.includes('Director');
      const isManager = u.designation && u.designation.includes('Manager');
      const roleColor = isDirector ? 'var(--accent-secondary)' : (isManager ? 'var(--status-info)' : 'var(--text-muted)');
      const roleBg = isDirector ? 'rgba(201,164,106,0.08)' : (isManager ? 'rgba(56,189,248,0.08)' : 'rgba(255,255,255,0.02)');

      return `
        <div class="card glass animate-slide-up flex flex-col justify-between" style="gap: var(--spacing-md); position:relative; min-height: 200px;">
          
          <!-- Delete and State Action Buttons (top-right) -->
          <div style="position:absolute; top: 12px; right: 12px; display:flex; align-items:center; gap:4px;">
            <!-- Active State Toggle Switcher -->
            <button class="btn-toggle-user-state" data-id="${u.id}" data-active="${activeState}" title="${activeState ? 'Deactivate operator access' : 'Activate operator access'}" style="background:none; border:none; color:${statusColor}; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 5px; border-radius:var(--radius-sm);">
              <i data-lucide="${activeState ? 'toggle-right' : 'toggle-left'}" style="width:18px; height:18px; stroke-width:2.5;"></i>
            </button>
            <!-- Delete Soft-Deactivate Button -->
            <button class="btn-delete-user" data-id="${u.id}" data-name="${cleanFullName}" title="Delete User account" style="background:none; border:none; color:var(--status-danger); opacity:0.6; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 5px; border-radius:var(--radius-sm);" onmouseover="this.style.opacity='1'; this.style.background='rgba(239,68,68,0.1)'" onmouseout="this.style.opacity='0.6'; this.style.background='none'">
              <i data-lucide="trash-2" style="width:13px; height:13px; stroke-width:2.5;"></i>
            </button>
          </div>

          <!-- Top row operator profile card details -->
          <div class="flex align-center gap-md" style="padding-right: 48px;">
            <!-- Initials avatar -->
            <div style="width: 44px; height: 44px; border-radius: 50%; background: rgba(255,255,255,0.04); border:1px solid var(--border-color); display:flex; align-items:center; justify-content:center; font-family:var(--font-display); font-weight:800; font-size:1.1rem; color:var(--accent-primary); flex-shrink:0;">
              ${initial}
            </div>
            <div style="min-width:0;">
              <div style="font-size:0.6rem; color: var(--text-muted); font-weight:700; letter-spacing:0.04em;">CODE: ${u.employeeCode}</div>
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 0.95rem; margin: 2px 0 0 0; color: var(--text-primary); overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="${cleanFullName}">${cleanFullName}</h3>
              
              <!-- Role designation badge -->
              <span style="display:inline-block; font-size:0.65rem; font-weight:700; color:${roleColor}; background:${roleBg}; border:1px solid ${roleColor}25; border-radius:4px; padding:2px 8px; margin-top:4px;">
                ${u.designation || 'Barista'}
              </span>
            </div>
          </div>

          <!-- Mid assignments -->
          <div style="display:flex; flex-direction:column; gap:4px; font-size: 0.7rem; border-top:1px solid rgba(255,255,255,0.04); padding-top:var(--spacing-sm); margin-top:4px;">
            <div class="flex justify-between align-center">
              <span style="color:var(--text-muted);">Region Domain</span>
              <span style="font-weight:700; color:var(--text-secondary); overflow:hidden; text-overflow:ellipsis; white-space:nowrap; max-width:130px;" title="${cleanRegion}">${cleanRegion}</span>
            </div>
            <div class="flex justify-between align-center">
              <span style="color:var(--text-muted);">Assigned Store</span>
              <span style="font-weight:700; color:var(--text-secondary); overflow:hidden; text-overflow:ellipsis; white-space:nowrap; max-width:130px;" title="${cleanStore}">${cleanStore}</span>
            </div>
          </div>

          <!-- Bottom Contact Info -->
          <div style="display:flex; flex-direction:column; gap: 4px; font-size: 0.68rem; border-top:1px solid rgba(255,255,255,0.05); padding-top:var(--spacing-sm); margin-top: auto;">
            <div class="flex align-center gap-xs" style="color:var(--text-secondary);">
              <i data-lucide="mail" style="width:10px; height:10px; color:var(--text-muted); flex-shrink:0;"></i>
              <span style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="${u.email}">${u.email || 'No email'}</span>
            </div>
            <div class="flex align-center gap-xs" style="color:var(--text-secondary);">
              <i data-lucide="phone" style="width:10px; height:10px; color:var(--text-muted); flex-shrink:0;"></i>
              <span>${u.phone || 'No phone'}</span>
            </div>
          </div>

          <!-- State & Opening Date Row -->
          <div class="flex justify-between align-center mt-sm" style="border-top:1px solid rgba(255,255,255,0.05); padding-top:var(--spacing-sm); font-size: 0.65rem;">
            <div style="background:${statusBg}; border: 1px solid ${statusColor}; color:${statusColor}; font-weight:700; border-radius:3px; padding: 2px 6px; letter-spacing:0.04em;">
              ${statusText}
            </div>
            <div style="color:var(--text-muted);">
              Joined: ${u.hireDate || 'Unknown'}
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
    const searchInput = container.querySelector('#input-search-users');
    const regionFilter = container.querySelector('#select-filter-region');
    const roleFilter = container.querySelector('#select-filter-role');
    const btnAddUser = container.querySelector('#btn-add-user');
    const modal = container.querySelector('#add-user-modal');
    const btnCloseModal = container.querySelector('#btn-close-user-modal');
    const formAddUser = container.querySelector('#form-add-user');
    const submitBtn = container.querySelector('#btn-user-submit');

    // 1. Text Search Input
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (searchInput) {
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
    if (regionFilter) {
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

    // 3. Designation Role Dropdown Filter
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (roleFilter) {
      /**
       * Handles the handle role filter event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleRoleFilter = (e) => {
        this.selectedRoleFilter = e.target.value;
        this.refreshGrid(container, lifecycle);
      };
      roleFilter.addEventListener('change', handleRoleFilter);
      lifecycle.onCleanup(() => roleFilter.removeEventListener('change', handleRoleFilter));
    }

    // 4. Open Add User Modal
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnAddUser && modal) {
      /**
       * Performs the openModal operation in this module.
       * @memberof Pages Module
       */
      const openModal = () => {
        modal.style.display = 'flex';
        const dateInput = container.querySelector('#user-modal-date');
        if (dateInput) dateInput.value = new Date().toISOString().substring(0, 10);
      };
      btnAddUser.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddUser.removeEventListener('click', openModal));
    }

    // 5. Close Add User Modal
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

    // 6. Form Submit connection to POST /api/v1/employees
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (formAddUser && modal) {
      /**
       * Handles the handle submit event or exception in the business workflow.
       * @memberof Pages Module
       */
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

        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
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

          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (response && response.success) {
            // Force dynamic metrics updates
            await dashboardService.getDashboardOverview();
            
            // Hide modal and reset form
            modal.style.display = 'none';
            formAddUser.reset();

            // Refresh UI
            await this.mount(container, lifecycle);

            notificationStore.success(`User profile '${firstName} ${lastName}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by backend API.');
          }

        } catch (err) {
          logger.error('UsersPage', 'Failed to register user:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register User Profile';
          }
        }
      };

      formAddUser.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddUser.removeEventListener('submit', handleSubmit));
    }

    // 7. Bind Actions (Deactivate & Delete)
    this.bindGridActions(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  refreshGrid(container, lifecycle) {
    const cardsGrid = container.querySelector('#users-cards-grid');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (cardsGrid) {
      cardsGrid.innerHTML = this.renderUserCards();
      if (window.lucide) window.lucide.createIcons();
      this.bindGridActions(container, lifecycle);
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindGridActions(container, lifecycle) {
    // 1. Soft-Delete Employee User
    const deleteButtons = container.querySelectorAll('.btn-delete-user');
    deleteButtons.forEach(btn => {
      /**
       * Handles the handle delete event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const name = btn.getAttribute('data-name');
        
        if (confirm(`Are you sure you want to delete the user operator profile for "${name}"?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/employees/${id}`);
            /**
             * Performs the fn operation in this module.
             * @memberof Pages Module
             */
            if (res && res.success) {
              notificationStore.success(`User "${name}" deleted successfully.`);
              
              await dashboardService.getDashboardOverview();
              await this.mount(container, lifecycle);
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
          const endpoint = `/api/v1/employees/${id}/${actionText}`;
          const res = await apiClient.patch(endpoint);
          
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (res && res.success) {
            notificationStore.success(`User access ${actionText}d successfully.`);
            
            await dashboardService.getDashboardOverview();
            await this.mount(container, lifecycle);
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
}



