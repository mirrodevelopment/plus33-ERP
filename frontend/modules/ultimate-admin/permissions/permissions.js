/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/permissions/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger, store/notificationStore, assets/js/constants/permissions
 * Depends On        : core/logger, store/notificationStore, assets/js/constants/permissions
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../../core/logger.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { Permissions } from '../../../shared/constants/permissions.js';

export default class PermissionsPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.roles = [];
    this.selectedRoleCode = 'ultimateAdmin';
    this.allPermissions = Object.keys(Permissions);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('PermissionsPage', 'Loading system roles & permission matrix workspace...');
    
    // Load persisted configurations from localStorage or seed default matrix
    const stored = localStorage.getItem('plus33-roles-state');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (stored) {
      this.roles = JSON.parse(stored);
    } else {
      this.roles = [
        {
          code: 'ultimateAdmin',
          name: 'Ultimate Administrator',
          description: 'Global superuser credentials. Full system scope access to all tables, ledgers, and configurations.',
          permissions: [...this.allPermissions]
        },
        {
          code: 'warehouse',
          name: 'Warehouse Administrator',
          description: 'Logistics scope credentials. Access to inventory adjustment tables, stock ledgers, and physical topology.',
          permissions: ['INVENTORY_VIEW', 'INVENTORY_EDIT']
        },
        {
          code: 'store',
          name: 'Store Manager',
          description: 'Retail site operations credentials. Management of store details, local staff, and customer orders.',
          permissions: ['STORE_VIEW', 'STORE_CREATE', 'INVENTORY_VIEW', 'WORKFORCE_VIEW']
        },
        {
          code: 'barista',
          name: 'Barista Operator',
          description: 'Daily counter retail access. Viewing local stock logs, logging sales, and processing receipts.',
          permissions: ['INVENTORY_VIEW']
        }
      ];
      localStorage.setItem('plus33-roles-state', JSON.stringify(this.roles));
    }

    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  saveState() {
    localStorage.setItem('plus33-roles-state', JSON.stringify(this.roles));
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  render(container) {
    const totalRoles = this.roles.length;
    const totalPermissions = this.allPermissions.length;
    const totalMappings = this.roles.reduce((acc, r) => acc + r.permissions.length, 0);

    const activeRole = this.roles.find(r => r.code === this.selectedRoleCode) || this.roles[0];

    container.innerHTML = `
      <div style="display:flex; flex-direction:column; gap: var(--spacing-lg); max-width: 1200px; margin: 0 auto; padding-bottom: var(--spacing-xxl);">
        
        <!-- Header row -->
        <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
          <div>
            <div class="flex align-center gap-xs" style="color: var(--accent-primary); font-weight: 700; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px;">
              <i data-lucide="shield-check" style="width: 14px; height: 14px;"></i> Administration
            </div>
            <h1 style="font-family: var(--font-display); font-weight: 800; font-size: 1.8rem; margin: 0; color: var(--text-primary); letter-spacing: -0.01em;">Roles & Permissions</h1>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0; margin-top: 4px;">Configure enterprise-grade role-based access control (RBAC) rules and authority matrix mappings.</p>
          </div>
          <button id="btn-add-role" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 18px; border-radius: var(--radius-md); border:none; display:flex; align-items:center; gap:6px; cursor:pointer;">
            <i data-lucide="plus" style="width:16px; height:16px;"></i> Create Custom Role
          </button>
        </div>

        <!-- KPI summary grid -->
        <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap: var(--spacing-md);">
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-primary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Security Roles</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalRoles}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-secondary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Access Authorities</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${totalPermissions}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-success);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Active Mappings</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-success); margin-top: 6px;">${totalMappings}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-info);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Security Grade</span>
            <span style="font-family: var(--font-display); font-size: 1.1rem; font-weight: 800; color: var(--status-info); margin-top: 10px;">ENTERPRISE</span>
          </div>
        </div>

        <!-- Split Layout Panel -->
        <div style="display:grid; grid-template-columns: 4.2fr 7.8fr; gap: var(--spacing-lg); align-items: start;">
          
          <!-- Left Panel: Roles List -->
          <div class="card glass flex flex-col gap-md" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
            <div style="font-size: 0.8rem; font-weight: 700; color: var(--text-primary); text-transform: uppercase; letter-spacing: 0.05em; border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 8px;">System Roles</div>
            
            <div style="display:flex; flex-direction:column; gap: var(--spacing-sm);">
              ${this.roles.map(r => {
                const isSelected = r.code === activeRole.code;
                const borderStyle = isSelected ? 'border-color: var(--accent-primary); background: rgba(255,255,255,0.03);' : 'border-color: var(--border-color);';
                const textStyle = isSelected ? 'color: var(--accent-primary); font-weight: 700;' : 'color: var(--text-primary); font-weight: 600;';

                return `
                  <div class="role-list-item flex flex-col justify-between" data-code="${r.code}" style="border: 1px solid transparent; border-radius: var(--radius-md); padding: var(--spacing-md); cursor:pointer; transition: all var(--transition-fast); position:relative; ${borderStyle}" onmouseover="this.style.borderColor='var(--border-color-hover)'" onmouseout="if(this.getAttribute('data-code')!=='${activeRole.code}') this.style.borderColor='var(--border-color)'">
                    
                    <!-- Deletion action button for custom roles (Ultimate admin can never be deleted) -->
                    ${r.code !== 'ultimateAdmin' && r.code !== 'warehouse' && r.code !== 'store' ? `
                      <button class="btn-delete-role" data-code="${r.code}" title="Delete Custom Role" style="position:absolute; top: 12px; right: 12px; background:none; border:none; color:var(--status-danger); opacity:0.6; cursor:pointer; transition: all var(--transition-fast); display:flex; align-items:center; justify-content:center; padding: 4px; border-radius:var(--radius-sm);" onmouseover="this.style.opacity='1'; this.style.background='rgba(239,68,68,0.1)'" onmouseout="this.style.opacity='0.6'; this.style.background='none'">
                        <i data-lucide="trash-2" style="width:12px; height:12px; stroke-width:2.5;"></i>
                      </button>
                    ` : ''}

                    <div style="${textStyle} font-size: 0.85rem;">${r.name}</div>
                    <div style="font-size:0.62rem; color:var(--text-muted); font-weight:700; margin-top:2px;">CODE: ${r.code}</div>
                    <div style="font-size: 0.7rem; color: var(--text-muted); margin-top: 6px; line-height: 1.35; overflow:hidden; text-overflow:ellipsis; display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical;">${r.description}</div>
                  </div>
                `;
              }).join('')}
            </div>
          </div>

          <!-- Right Panel: Permissions Matrix -->
          <div class="card glass flex flex-col gap-md" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
            <div class="flex justify-between align-center mb-xs" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 10px;">
              <div>
                <h3 style="font-family: var(--font-display); font-weight:800; font-size: 1rem; margin:0; color:var(--text-primary);">${activeRole.name}</h3>
                <span style="font-size:0.65rem; color:var(--text-muted); font-weight:700; text-transform:uppercase;">Authority Management Scope</span>
              </div>
              <div style="font-size: 0.72rem; color: var(--text-secondary); background:rgba(255,255,255,0.03); border:1px solid var(--border-color); padding: 4px 10px; border-radius: 4px; font-weight:700;">
                ${activeRole.permissions.length} Mapped Keys
              </div>
            </div>

            <p style="color:var(--text-secondary); font-size:0.75rem; line-height:1.45; margin:0; padding-bottom: var(--spacing-sm); border-bottom: 1px dashed var(--border-color);">${activeRole.description}</p>

            <div style="display:flex; flex-direction:column; gap: var(--spacing-md); margin-top:var(--spacing-sm);">
              <div style="font-size:0.7rem; font-weight:700; color:var(--text-muted); text-transform:uppercase; letter-spacing:0.04em;">Granular Permissions Matrix</div>
              
              <div style="display:grid; grid-template-columns: 1fr; gap:var(--spacing-xs);">
                ${this.allPermissions.map(perm => {
                  const hasIt = activeRole.permissions.includes(perm);
                  
                  // Label styling
                  const cleanName = perm.replace(/_/g, ' ')
                                        .toLowerCase()
                                        .split(' ')
                                        .map(w => w.charAt(0).toUpperCase() + w.slice(1))
                                        .join(' ');

                  return `
                    <div class="flex justify-between align-center" style="background: rgba(255,255,255,0.01); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 8px 12px; transition: var(--transition-fast);">
                      <div class="flex flex-col">
                        <span style="font-weight:600; font-size:0.75rem; color:var(--text-primary);">${cleanName}</span>
                        <span style="font-size:0.62rem; color:var(--text-muted); font-family: monospace; font-weight:700; margin-top:2px;">${perm}</span>
                      </div>
                      
                      <!-- Toggle switch -->
                      <label style="position:relative; display:inline-block; width: 34px; height: 20px;">
                        <input class="checkbox-toggle-permission" type="checkbox" data-permission="${perm}" ${hasIt ? 'checked' : ''} style="opacity:0; width:0; height:0;">
                        <span style="position:absolute; cursor:pointer; inset:0; background:rgba(255,255,255,0.06); border:1px solid var(--border-color); border-radius:20px; transition:0.3s; display:block;"></span>
                        <span class="toggle-slider-knob" style="position:absolute; content:''; height:12px; width:12px; left:3px; bottom:3px; background:var(--text-muted); border-radius:50%; transition:0.3s; display:block; transform:${hasIt ? 'translateX(14px)' : 'none'}; ${hasIt ? 'background:var(--accent-primary);' : ''}"></span>
                      </label>
                    </div>
                  `;
                }).join('')}
              </div>
            </div>

          </div>

        </div>

        <!-- Add Custom Role Modal -->
        <div id="add-role-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.7); z-index:99999; display:none; align-items:center; justify-content:center; padding: var(--spacing-lg); backdrop-filter: blur(4px);">
          <div class="card glass" style="width:100%; max-width: 450px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.5);">
            <div class="flex justify-between align-center mb-md" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.15rem; margin:0; color:var(--text-primary);">Create Custom Security Role</h3>
              <button id="btn-close-role-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.25rem; line-height:1;">&times;</button>
            </div>
            
            <form id="form-add-role" style="display:flex; flex-direction:column; gap: var(--spacing-md); text-align:left;">
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Role Name</label>
                <input id="role-modal-name" type="text" placeholder="e.g. Compliance Auditor" required style="padding:10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Role Description</label>
                <textarea id="role-modal-description" placeholder="Provide operational scope description..." required style="padding:10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; height:80px; resize:none; font-family:var(--font-sans);"></textarea>
              </div>
              
              <button type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.8rem; padding:12px; margin-top:var(--spacing-sm); border:none; border-radius:var(--radius-md); cursor:pointer;">
                Register Security Role
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
  bindEvents(container, lifecycle) {
    const listItems = container.querySelectorAll('.role-list-item');
    const btnAddRole = container.querySelector('#btn-add-role');
    const modal = container.querySelector('#add-role-modal');
    const btnCloseModal = container.querySelector('#btn-close-role-modal');
    const formAddRole = container.querySelector('#form-add-role');
    const permissionToggles = container.querySelectorAll('.checkbox-toggle-permission');

    // 1. Role Selection Switching
    listItems.forEach(item => {
      /**
       * Handles the handle select event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleSelect = (e) => {
        // Prevent trigger if clicking on delete custom role button
        if (e.target.closest('.btn-delete-role')) return;

        this.selectedRoleCode = item.getAttribute('data-code');
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      item.addEventListener('click', handleSelect);
      lifecycle.onCleanup(() => item.removeEventListener('click', handleSelect));
    });

    // 2. Open Modal
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnAddRole && modal) {
      /**
       * Performs the openModal operation in this module.
       * @memberof Pages Module
       */
      const openModal = () => {
        modal.style.display = 'flex';
      };
      btnAddRole.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddRole.removeEventListener('click', openModal));
    }

    // 3. Close Modal
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

    // 4. Form Submit (add custom role)
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (formAddRole && modal) {
      /**
       * Handles the handle submit event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleSubmit = (e) => {
        e.preventDefault();

        const name = container.querySelector('#role-modal-name').value.trim();
        const description = container.querySelector('#role-modal-description').value.trim();
        const code = name.toLowerCase().replace(/[^a-z0-9]/g, '-');

        if (this.roles.some(r => r.code === code)) {
          notificationStore.danger('Role code already exists.');
          return;
        }

        const newRole = {
          code: code,
          name: name,
          description: description,
          permissions: [] // initial empty permissions
        };

        this.roles.push(newRole);
        this.saveState();

        this.selectedRoleCode = code;
        modal.style.display = 'none';
        formAddRole.reset();

        this.render(container);
        this.bindEvents(container, lifecycle);

        notificationStore.success(`Security role '${name}' registered successfully!`);
      };

      formAddRole.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddRole.removeEventListener('submit', handleSubmit));
    }

    // 5. Deletion of Custom Roles
    const deleteButtons = container.querySelectorAll('.btn-delete-role');
    deleteButtons.forEach(btn => {
      /**
       * Handles the handle delete event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleDelete = (e) => {
        e.stopPropagation();
        const code = btn.getAttribute('data-code');
        const role = this.roles.find(r => r.code === code);
        if (!role) return;

        if (confirm(`Are you sure you want to delete the security role "${role.name}"?`)) {
          this.roles = this.roles.filter(r => r.code !== code);
          this.saveState();
          
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (this.selectedRoleCode === code) {
            this.selectedRoleCode = 'ultimateAdmin';
          }

          this.render(container);
          this.bindEvents(container, lifecycle);

          notificationStore.success(`Security role "${role.name}" deleted.`);
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });

    // 6. Permission Matrix Toggle Checkboxes
    permissionToggles.forEach(toggle => {
      /**
       * Handles the handle toggle event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleToggle = (e) => {
        const perm = toggle.getAttribute('data-permission');
        const role = this.roles.find(r => r.code === this.selectedRoleCode);
        if (!role) return;

        const isChecked = toggle.checked;

        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
        if (isChecked) {
          if (!role.permissions.includes(perm)) {
            role.permissions.push(perm);
          }
        } else {
          role.permissions = role.permissions.filter(p => p !== perm);
        }

        this.saveState();
        
        // Visual knob translation helper
        const knob = toggle.closest('label').querySelector('.toggle-slider-knob');
        const bg = toggle.closest('label').querySelector('span');
        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
        if (knob) {
          knob.style.transform = isChecked ? 'translateX(14px)' : 'none';
          knob.style.background = isChecked ? 'var(--accent-primary)' : 'var(--text-muted)';
        }

        // Update badge count
        const badge = container.querySelector('.header-right span');
        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
        if (badge) {
          badge.textContent = `${role.permissions.length} Mapped Keys`;
        }

        notificationStore.success(`Role authority '${perm}' ${isChecked ? 'mapped' : 'removed'}.`);
      };
      toggle.addEventListener('change', handleToggle);
      lifecycle.onCleanup(() => toggle.removeEventListener('change', handleToggle));
    });
  }
}



