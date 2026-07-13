/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin — Roles & Permissions Matrix
 * File              : permissions.js
 * Purpose           : Controller component for Roles & Permissions UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/ultimate-admin/permissions/permissions.html
 * Related CSS       : frontend/modules/ultimate-admin/permissions/permissions.css
 * Related Constants : shared/constants/permissions.js
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in permissions.html — this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor → mount → loadTemplate → loadData → render → bindEvents → destroy
 ******************************************************************************/

import { htmlLoader } from '../../../../core/htmlLoader.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { Permissions } from '../../../shared/constants/permissions.js';

/** Path to the permissions HTML template */
const TEMPLATE_URL = 'modules/ultimate-admin/pages/permissions/permissions.html';

export default class PermissionsPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.roles = [];
    this.selectedRoleCode = 'ultimateAdmin';
    this.allPermissions = Object.keys(Permissions);
    this.container = null;
    this.lifecycle = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the permissions page component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('PermissionsPage', 'Loading system roles & permission matrix workspace...');
    this.container = container;
    this.lifecycle = lifecycle;

    // Dynamically load permissions CSS
    this._loadCss();

    // 1. Load template HTML
    await this._loadTemplate(container);

    // 2. Fetch configurations from localStorage or seed defaults
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
    const stored = localStorage.getItem('plus33-roles-state');
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
          code: 'nationalWarehouseAdmin',
          name: 'National Warehouse Admin',
          description: 'Logistics scope credentials. Access to inventory adjustment tables, stock ledgers, and physical topology.',
          permissions: ['INVENTORY_VIEW', 'INVENTORY_EDIT']
        },
        {
          code: 'regionalWarehouseAdmin',
          name: 'Regional Warehouse Admin',
          description: 'Logistics scope credentials for a specific region. Access to regional inventory and physical topology.',
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
      this._saveState();
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  /**
   * Render dynamic fields, KPI cards, role list buttons, permission toggles.
   * @param {HTMLElement} container
   */
  _render(container) {
    this._populateKpiSummary(container);
    this._renderRolesList(container);
    this._renderMatrixToggles(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  /**
   * Bind event listeners for selects, buttons and modal forms.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindEvents(container, lifecycle) {
    const btnAddRole = container.querySelector('#btn-add-role');
    const modal = container.querySelector('#add-role-modal');
    const btnCloseModal = container.querySelector('#btn-close-role-modal');
    const formAddRole = container.querySelector('#form-add-role');

    // 1. Open Modal
    if (btnAddRole && modal) {
      const openModal = () => {
        modal.removeAttribute('hidden');
      };
      btnAddRole.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddRole.removeEventListener('click', openModal));
    }

    // 2. Close Modal
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

    // 3. Form Submit (add custom role)
    if (formAddRole && modal) {
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
        this._saveState();

        this.selectedRoleCode = code;
        modal.setAttribute('hidden', '');
        formAddRole.reset();

        this.loadAndRender(container, lifecycle);

        notificationStore.success(`Security role '${name}' registered successfully!`);
      };

      formAddRole.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddRole.removeEventListener('submit', handleSubmit));
    }

    // Bind item action events (switching role, toggling switches, custom deletes)
    this._bindMatrixActionEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    logger.debug('PermissionsPage', 'Workspace destroyed.');
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
    const totalRoles = this.roles.length;
    const totalPermissions = this.allPermissions.length;
    const totalMappings = this.roles.reduce((acc, r) => acc + r.permissions.length, 0);

    const kpiTotalRoles = container.querySelector('#kpi-total-roles');
    const kpiTotalPermissions = container.querySelector('#kpi-total-permissions');
    const kpiTotalMappings = container.querySelector('#kpi-total-mappings');

    if (kpiTotalRoles) kpiTotalRoles.textContent = totalRoles;
    if (kpiTotalPermissions) kpiTotalPermissions.textContent = totalPermissions;
    if (kpiTotalMappings) kpiTotalMappings.textContent = totalMappings;
  }

  /**
   * Render left panel roles menu list.
   * @param {HTMLElement} container
   */
  _renderRolesList(container) {
    const listContainer = container.querySelector('#roles-list-container');
    const itemTpl = container.querySelector('#role-item-tpl');
    
    if (!listContainer || !itemTpl) return;
    listContainer.replaceChildren();

    this.roles.forEach(r => {
      const clone = itemTpl.content.cloneNode(true);
      const itemEl = clone.querySelector('.role-list-item');
      const nameEl = clone.querySelector('.role-item-name');
      const codeEl = clone.querySelector('.role-item-code');
      const descEl = clone.querySelector('.role-item-desc');
      const deleteBtn = clone.querySelector('.btn-delete-role');

      const isSelected = r.code === this.selectedRoleCode;
      
      if (itemEl) {
        itemEl.setAttribute('data-code', r.code);
        if (isSelected) {
          itemEl.classList.add('role-list-item--selected');
          itemEl.setAttribute('aria-selected', 'true');
        }
      }

      if (nameEl) nameEl.textContent = r.name;
      if (codeEl) codeEl.textContent = `CODE: ${r.code}`;
      if (descEl) descEl.textContent = r.description;

      // Allow deleting only custom roles
      if (r.code !== 'ultimateAdmin' && r.code !== 'nationalWarehouseAdmin' && r.code !== 'regionalWarehouseAdmin' && r.code !== 'store' && deleteBtn) {
        deleteBtn.removeAttribute('hidden');
        deleteBtn.setAttribute('data-code', r.code);
      }

      listContainer.appendChild(clone);
    });
  }

  /**
   * Render active role metadata information and toggles checklist.
   * @param {HTMLElement} container
   */
  _renderMatrixToggles(container) {
    const activeRole = this.roles.find(r => r.code === this.selectedRoleCode) || this.roles[0];

    const titleEl = container.querySelector('#active-role-title');
    const mappedCountEl = container.querySelector('#active-role-mapped-count');
    const descEl = container.querySelector('#active-role-desc');
    const togglesContainer = container.querySelector('#matrix-toggles-container');
    const toggleTpl = container.querySelector('#perm-toggle-tpl');

    if (titleEl) titleEl.textContent = activeRole.name;
    if (mappedCountEl) mappedCountEl.textContent = `${activeRole.permissions.length} Mapped Keys`;
    if (descEl) descEl.textContent = activeRole.description;

    if (!togglesContainer || !toggleTpl) return;
    togglesContainer.replaceChildren();

    this.allPermissions.forEach(perm => {
      const clone = toggleTpl.content.cloneNode(true);

      const card = clone.querySelector('.perm-toggle-card');
      const friendlyName = clone.querySelector('.perm-friendly-name');
      const constantCode = clone.querySelector('.perm-constant-code');
      const checkbox = clone.querySelector('.checkbox-toggle-permission');

      const hasIt = activeRole.permissions.includes(perm);
      
      const cleanName = perm.replace(/_/g, ' ')
                            .toLowerCase()
                            .split(' ')
                            .map(w => w.charAt(0).toUpperCase() + w.slice(1))
                            .join(' ');

      if (friendlyName) friendlyName.textContent = cleanName;
      if (constantCode) constantCode.textContent = perm;
      
      if (checkbox) {
        checkbox.setAttribute('data-permission', perm);
        checkbox.checked = hasIt;
      }

      togglesContainer.appendChild(clone);
    });
  }

  /**
   * Bind event actions inside current layout cards matrix.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindMatrixActionEvents(container, lifecycle) {
    const listItems = container.querySelectorAll('.role-list-item');
    const deleteButtons = container.querySelectorAll('.btn-delete-role');
    const permissionToggles = container.querySelectorAll('.checkbox-toggle-permission');

    // 1. Role Selection Switching
    listItems.forEach(item => {
      const handleSelect = (e) => {
        if (e.target.closest('.btn-delete-role')) return;

        this.selectedRoleCode = item.getAttribute('data-code');
        this._render(container);
        this._bindEvents(container, lifecycle);
      };
      item.addEventListener('click', handleSelect);
      lifecycle.onCleanup(() => item.removeEventListener('click', handleSelect));
    });

    // 2. Deletion of Custom Roles
    deleteButtons.forEach(btn => {
      const handleDelete = (e) => {
        e.stopPropagation();
        const code = btn.getAttribute('data-code');
        const role = this.roles.find(r => r.code === code);
        if (!role) return;

        if (confirm(`Are you sure you want to delete the security role "${role.name}"?`)) {
          this.roles = this.roles.filter(r => r.code !== code);
          this._saveState();
          
          if (this.selectedRoleCode === code) {
            this.selectedRoleCode = 'ultimateAdmin';
          }

          this.loadAndRender(container, lifecycle);
          notificationStore.success(`Security role "${role.name}" deleted.`);
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });

    // 3. Permission Matrix Toggle Checkboxes
    permissionToggles.forEach(toggle => {
      const handleToggle = (e) => {
        const perm = toggle.getAttribute('data-permission');
        const role = this.roles.find(r => r.code === this.selectedRoleCode);
        if (!role) return;

        const isChecked = toggle.checked;

        if (isChecked) {
          if (!role.permissions.includes(perm)) {
            role.permissions.push(perm);
          }
        } else {
          role.permissions = role.permissions.filter(p => p !== perm);
        }

        this._saveState();
        
        // Update badge count
        const badge = container.querySelector('#active-role-mapped-count');
        if (badge) {
          badge.textContent = `${role.permissions.length} Mapped Keys`;
        }

        // Re-render components without rebuild inputs to avoid focus loss
        this._populateKpiSummary(container);

        notificationStore.success(`Role authority '${perm}' ${isChecked ? 'mapped' : 'removed'}.`);
      };
      toggle.addEventListener('change', handleToggle);
      lifecycle.onCleanup(() => toggle.removeEventListener('change', handleToggle));
    });
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE PERSISTENCE
  // ---------------------------------------------------------------------------

  _saveState() {
    localStorage.setItem('plus33-roles-state', JSON.stringify(this.roles));
  }

  _loadCss() {
    const cssId = 'permissions-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/ultimate-admin/pages/permissions/permissions.css';
      document.head.appendChild(link);
    }
  }
}
export { PermissionsPage };
