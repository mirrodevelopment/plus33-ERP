/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : RegionalAdmin — Stores
 * File              : stores.js
 * Purpose           : Controller component for Stores Management page. Supports store listing, creation, updates, status toggles, and document management.
 * Version           : 1.0.0
 *
 * Related HTML      : frontend/modules/regional-admin/pages/stores/stores.html
 * Related CSS       : frontend/modules/regional-admin/pages/stores/stores.css
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/regional-admin/pages/stores/stores.html';

export default class RegionalAdminStores {

  constructor() {
    this.user = authStore.getUser() || {};
    this.stores = [];
    this.regions = [];
    this.warehouses = [];
    this.activeStore = null; 
  }

  async mount(container, lifecycle) {
    logger.info('RegionalAdminStores', 'Mounting Stores Management page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    await this.fetchRegions();
    await this.fetchWarehouses();
    await this.fetchStores(container);

    this._bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'regional-stores-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/regional-admin/pages/stores/stores.css';
      document.head.appendChild(link);
    }
  }

  async fetchRegions() {
    try {
      const res = await apiClient.get('/api/v1/regions');
      if (res && res.success && res.data) {
        this.regions = Array.isArray(res.data) ? res.data : (res.data.content || []);
      }
    } catch (e) {
      logger.error('RegionalAdminStores', 'Error fetching regions:', e);
    }
  }

  async fetchWarehouses() {
    try {
      const res = await apiClient.get('/api/v1/warehouses');
      if (res && res.success && res.data) {
        this.warehouses = Array.isArray(res.data) ? res.data : (res.data.content || []);
      }
    } catch (e) {
      logger.error('RegionalAdminStores', 'Error fetching warehouses:', e);
    }
  }

  async fetchStores(container) {
    try {
      let url = '/api/v1/stores?page=0&size=100';
      if (this.user.regionId) {
        url += '&regionId=' + this.user.regionId;
      }
      const res = await apiClient.get(url);
      if (res && res.success && res.data) {
        this.stores = res.data.content || [];
        this.renderTable(container);
      }
    } catch (e) {
      logger.error('RegionalAdminStores', 'Error fetching stores:', e);
      notificationStore.show('Error loading stores database', 'error');
    }
  }

  renderTable(container) {
    const tbody = container.querySelector('#stores-table-body');
    if (!tbody) return;

    const query = container.querySelector('#search-stores-input')?.value.toLowerCase() || '';
    const typeFilter = container.querySelector('#filter-store-type')?.value || '';
    const statusFilter = container.querySelector('#filter-store-status')?.value || '';

    const filtered = this.stores.filter(s => {
      const matchesSearch = !query || 
        s.name.toLowerCase().includes(query) || 
        s.code.toLowerCase().includes(query) || 
        (s.address && s.address.toLowerCase().includes(query));
      
      const matchesType = !typeFilter || s.type === typeFilter;
      const matchesStatus = !statusFilter || String(s.active) === statusFilter;

      return matchesSearch && matchesType && matchesStatus;
    });

    if (filtered.length === 0) {
      tbody.innerHTML = `
        <tr>
          <td colspan="9" style="text-align:center; padding: 40px; color: rgba(255,255,255,0.4);">
            <span>No stores match the active search filters.</span>
          </td>
        </tr>
      `;
      return;
    }

    tbody.innerHTML = filtered.map(s => {
      const typeBadge = s.type === 'COMPACT_CAFE' ? 'badge-cafe' : (s.type === 'FLAGSHIP_STORE' ? 'badge-flagship' : 'badge-store');
      const typeLabel = s.type.replace(/_/g, ' ');
      const docCount = s.documents ? s.documents.length : 0;
      
      return `
        <tr data-id="${s.id}">
          <td>
            <div style="font-weight:700;">#${s.id}</div>
            <div style="font-size:0.75rem; color:#9ca3af;">${s.code}</div>
          </td>
          <td style="font-weight:600; color:#fff;">${s.name}</td>
          <td><span class="badge ${typeBadge}">${typeLabel}</span></td>
          <td>
            <div style="max-width:180px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" title="${s.address || ''}">
              ${s.address || '—'}
            </div>
            <div style="font-size:0.7rem; color:#9ca3af;">
              ${s.latitude ? s.latitude + ', ' + s.longitude : 'No GPS set'} 
              ${s.geofenceRadiusMeters ? ' (' + s.geofenceRadiusMeters + 'm)' : ''}
            </div>
          </td>
          <td>
            <div style="font-weight:600;">${s.phone || '—'}</div>
            <div style="font-size:0.75rem; color:#9ca3af;">${s.email || '—'}</div>
          </td>
          <td>
            <div style="font-weight:600; color:#c9a46a;">${s.adminName || '—'}</div>
            <div style="font-size:0.75rem; color:#9ca3af;">${s.adminNumber || '—'} / ${s.adminMobile || '—'}</div>
          </td>
          <td>
            <button class="btn-action btn-action--docs" data-action="docs" style="color: #c9a46a; font-weight:700;" title="Manage Attached Compliance Documents">
              📄 ${docCount} Records
            </button>
          </td>
          <td>
            <span class="badge badge-status ${s.active ? 'badge-status--active' : 'badge-status--inactive'}">
              ${s.active ? 'Active' : 'Inactive'}
            </span>
          </td>
          <td>
            <div class="action-btn-group">
              <button class="btn-action btn-action--edit" data-action="edit" title="Edit Franchise Details">✏️</button>
              <button class="btn-action" data-action="toggle-status" title="${s.active ? 'Deactivate Franchise' : 'Activate Franchise'}">
                ${s.active ? '🛑' : '✅'}
              </button>
            </div>
          </td>
        </tr>
      `;
    }).join('');
  }

  populateDropdowns(container) {
    const regionSelect = container.querySelector('#form-store-region');
    const warehouseSelect = container.querySelector('#form-store-warehouse');

    if (regionSelect) {
      regionSelect.innerHTML = this.regions.map(r => `<option value="${r.id}">${r.name} (${r.code})</option>`).join('');
      if (this.user.regionId) {
        regionSelect.value = this.user.regionId;
      }
    }

    if (warehouseSelect) {
      warehouseSelect.innerHTML = '<option value="">No Default Warehouse</option>' + 
        this.warehouses.map(w => `<option value="${w.id}">${w.name} (${w.code})</option>`).join('');
    }
  }

  openStoreModal(container, store = null) {
    const modal = container.querySelector('#store-form-modal');
    const title = container.querySelector('#store-modal-title');
    const form = container.querySelector('#form-store-management');
    
    if (!modal || !form) return;

    this.populateDropdowns(container);

    if (store) {
      title.textContent = 'Edit Store Location';
      form.querySelector('#form-store-id').value = store.id;
      form.querySelector('#form-store-code').value = store.code;
      form.querySelector('#form-store-name').value = store.name;
      form.querySelector('#form-store-type').value = store.type;
      form.querySelector('#form-store-timezone').value = store.timezone || 'Europe/Paris';
      form.querySelector('#form-store-phone').value = store.phone || '';
      form.querySelector('#form-store-email').value = store.email || '';
      form.querySelector('#form-store-opening').value = store.openingDate || '';
      form.querySelector('#form-store-active').value = String(store.active);
      form.querySelector('#form-store-region').value = store.regionId;
      form.querySelector('#form-store-warehouse').value = store.warehouseId || '';
      form.querySelector('#form-store-address').value = store.address || '';
      form.querySelector('#form-store-latitude').value = store.latitude || '';
      form.querySelector('#form-store-longitude').value = store.longitude || '';
      form.querySelector('#form-store-radius').value = store.geofenceRadiusMeters || 200;
      form.querySelector('#form-store-admin-name').value = store.adminName || 'giri';
      form.querySelector('#form-store-admin-number').value = store.adminNumber || 'EMP10245';
      form.querySelector('#form-store-admin-mobile').value = store.adminMobile || '+919999999999';
    } else {
      title.textContent = 'Create Store Location';
      form.reset();
      form.querySelector('#form-store-id').value = '';
      if (this.user.regionId) {
        form.querySelector('#form-store-region').value = this.user.regionId;
      }
      form.querySelector('#form-store-timezone').value = 'Europe/Paris';
      form.querySelector('#form-store-active').value = 'true';
      form.querySelector('#form-store-radius').value = 200;
      form.querySelector('#form-store-admin-name').value = 'giri';
      form.querySelector('#form-store-admin-number').value = 'EMP10245';
      form.querySelector('#form-store-admin-mobile').value = '+919999999999';
    }

    modal.style.display = 'flex';
  }

  closeStoreModal(container) {
    const modal = container.querySelector('#store-form-modal');
    if (modal) modal.style.display = 'none';
  }

  async saveStoreForm(container, form) {
    const id = form.querySelector('#form-store-id').value;
    const payload = {
      code: form.querySelector('#form-store-code').value,
      name: form.querySelector('#form-store-name').value,
      type: form.querySelector('#form-store-type').value,
      timezone: form.querySelector('#form-store-timezone').value,
      phone: form.querySelector('#form-store-phone').value,
      email: form.querySelector('#form-store-email').value,
      openingDate: form.querySelector('#form-store-opening').value || null,
      active: form.querySelector('#form-store-active').value === 'true',
      regionId: parseInt(form.querySelector('#form-store-region').value),
      warehouseId: form.querySelector('#form-store-warehouse').value ? parseInt(form.querySelector('#form-store-warehouse').value) : null,
      address: form.querySelector('#form-store-address').value,
      adminName: form.querySelector('#form-store-admin-name').value,
      adminNumber: form.querySelector('#form-store-admin-number').value,
      adminMobile: form.querySelector('#form-store-admin-mobile').value
    };

    const lat = form.querySelector('#form-store-latitude').value;
    const lng = form.querySelector('#form-store-longitude').value;
    const radius = form.querySelector('#form-store-radius').value;

    try {
      let res;
      if (id) {
        res = await apiClient.put(`/api/v1/stores/${id}`, payload);
      } else {
        res = await apiClient.post('/api/v1/stores', payload);
      }

      if (res && res.success) {
        const savedStore = res.data;
        
        if (lat || lng || radius) {
          await apiClient.put(`/api/v1/stores/${savedStore.id}/location`, {
            latitude: lat ? parseFloat(lat) : null,
            longitude: lng ? parseFloat(lng) : null,
            geofenceRadiusMeters: radius ? parseInt(radius) : 200
          });
        }

        notificationStore.show(id ? 'Franchise updated successfully' : 'Franchise created successfully', 'success');
        this.closeStoreModal(container);
        await this.fetchStores(container);
      }
    } catch (e) {
      logger.error('RegionalAdminStores', 'Error saving store:', e);
      notificationStore.show(e.message || 'Error saving franchise details', 'error');
    }
  }

  async toggleStoreStatus(container, storeId) {
    const store = this.stores.find(s => s.id === storeId);
    if (!store) return;

    try {
      const endpoint = store.active ? `/api/v1/stores/${storeId}/deactivate` : `/api/v1/stores/${storeId}/activate`;
      const res = await apiClient.patch(endpoint);
      if (res && res.success) {
        notificationStore.show(store.active ? 'Franchise deactivated' : 'Franchise activated', 'success');
        await this.fetchStores(container);
      }
    } catch (e) {
      logger.error('RegionalAdminStores', 'Error toggling store status:', e);
      notificationStore.show('Error updating store status', 'error');
    }
  }

  openDocsModal(container, storeId) {
    const store = this.stores.find(s => s.id === storeId);
    if (!store) return;

    this.activeStore = store;
    const modal = container.querySelector('#store-docs-modal');
    const storeTitle = container.querySelector('#modal-docs-store-name');
    
    if (storeTitle) storeTitle.textContent = `${store.name} (${store.code})`;
    
    this.renderDocsList(container);
    modal.style.display = 'flex';
  }

  closeDocsModal(container) {
    const modal = container.querySelector('#store-docs-modal');
    if (modal) modal.style.display = 'none';
    this.activeStore = null;
  }

  renderDocsList(container) {
    const box = container.querySelector('#store-docs-list-container');
    if (!box || !this.activeStore) return;

    const docs = this.activeStore.documents || [];
    if (docs.length === 0) {
      box.innerHTML = '<div style="color:rgba(255,255,255,0.4); text-align:center; padding:16px; font-size:0.8rem;">No compliance documents uploaded yet.</div>';
      return;
    }

    box.innerHTML = docs.map(d => {
      const typeLabel = d.documentType.replace(/_/g, ' ');
      return `
        <div class="doc-item-row" data-doc-id="${d.id}">
          <div class="doc-info">
            <a href="${d.filePath}" target="_blank" class="doc-title-link" title="Download Document">${d.documentName}</a>
            <span class="doc-meta">${typeLabel} • Uploaded ${new Date(d.uploadedAt).toLocaleDateString()}</span>
          </div>
          <button type="button" class="btn-delete-doc" data-action="delete-doc" title="Remove Document">🗑️</button>
        </div>
      `;
    }).join('');
  }

  async uploadStoreFile(container, form) {
    if (!this.activeStore) return;

    const fileInput = form.querySelector('#form-doc-file');
    const file = fileInput?.files[0];
    const docType = form.querySelector('#form-doc-type').value;

    if (!file) {
      notificationStore.show('Please select a file to upload.', 'error');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('documentType', docType);

    try {
      const res = await apiClient.postMultipart(`/api/v1/stores/${this.activeStore.id}/documents`, formData);
      if (res && res.success) {
        notificationStore.show('Document uploaded successfully', 'success');
        fileInput.value = '';
        
        await this.fetchStores(container);
        this.activeStore = this.stores.find(s => s.id === this.activeStore.id);
        this.renderDocsList(container);
      }
    } catch (e) {
      logger.error('RegionalAdminStores', 'Error uploading document:', e);
      notificationStore.show('Failed to upload document file', 'error');
    }
  }

  async removeStoreFile(container, docId) {
    if (!this.activeStore) return;
    if (!confirm('Are you sure you want to delete this compliance record?')) return;

    try {
      const res = await apiClient.delete(`/api/v1/stores/${this.activeStore.id}/documents/${docId}`);
      if (res && res.success) {
        notificationStore.show('Document deleted successfully', 'success');
        
        await this.fetchStores(container);
        this.activeStore = this.stores.find(s => s.id === this.activeStore.id);
        this.renderDocsList(container);
      }
    } catch (e) {
      logger.error('RegionalAdminStores', 'Error removing document:', e);
      notificationStore.show('Failed to remove compliance document', 'error');
    }
  }

  _bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#search-stores-input');
    const typeFilter = container.querySelector('#filter-store-type');
    const statusFilter = container.querySelector('#filter-store-status');

    const triggerFilter = () => this.renderTable(container);
    if (searchInput) { searchInput.addEventListener('input', triggerFilter); lifecycle.onCleanup(() => searchInput.removeEventListener('input', triggerFilter)); }
    if (typeFilter) { typeFilter.addEventListener('change', triggerFilter); lifecycle.onCleanup(() => typeFilter.removeEventListener('change', triggerFilter)); }
    if (statusFilter) { statusFilter.addEventListener('change', triggerFilter); lifecycle.onCleanup(() => statusFilter.removeEventListener('change', triggerFilter)); }

    const btnCreate = container.querySelector('#btn-create-store-modal');
    if (btnCreate) {
      const h = () => this.openStoreModal(container);
      btnCreate.addEventListener('click', h);
      lifecycle.onCleanup(() => btnCreate.removeEventListener('click', h));
    }

    const btnCloseForm = container.querySelector('#btn-close-store-modal');
    const btnCancelForm = container.querySelector('#btn-cancel-store-form');
    const handleCloseForm = () => this.closeStoreModal(container);
    if (btnCloseForm) { btnCloseForm.addEventListener('click', handleCloseForm); lifecycle.onCleanup(() => btnCloseForm.removeEventListener('click', handleCloseForm)); }
    if (btnCancelForm) { btnCancelForm.addEventListener('click', handleCloseForm); lifecycle.onCleanup(() => btnCancelForm.removeEventListener('click', handleCloseForm)); }

    const formStore = container.querySelector('#form-store-management');
    if (formStore) {
      const h = (e) => { e.preventDefault(); this.saveStoreForm(container, formStore); };
      formStore.addEventListener('submit', h);
      lifecycle.onCleanup(() => formStore.removeEventListener('submit', h));
    }

    const tbody = container.querySelector('#stores-table-body');
    if (tbody) {
      const h = async (e) => {
        const btn = e.target.closest('button');
        if (!btn) return;

        const tr = btn.closest('tr');
        const id = parseInt(tr.dataset.id);
        const action = btn.dataset.action;

        if (action === 'edit') {
          const store = this.stores.find(s => s.id === id);
          if (store) this.openStoreModal(container, store);
        } else if (action === 'toggle-status') {
          await this.toggleStoreStatus(container, id);
        } else if (action === 'docs') {
          this.openDocsModal(container, id);
        }
      };
      tbody.addEventListener('click', h);
      lifecycle.onCleanup(() => tbody.removeEventListener('click', h));
    }

    const btnCloseDocs = container.querySelector('#btn-close-docs-modal');
    const btnCloseDocsFooter = container.querySelector('#btn-close-docs-footer');
    const handleCloseDocs = () => this.closeDocsModal(container);
    if (btnCloseDocs) { btnCloseDocs.addEventListener('click', handleCloseDocs); lifecycle.onCleanup(() => btnCloseDocs.removeEventListener('click', handleCloseDocs)); }
    if (btnCloseDocsFooter) { btnCloseDocsFooter.addEventListener('click', handleCloseDocs); lifecycle.onCleanup(() => btnCloseDocsFooter.removeEventListener('click', handleCloseDocs)); }

    const formDocUpload = container.querySelector('#form-store-upload-doc');
    if (formDocUpload) {
      const h = (e) => { e.preventDefault(); this.uploadStoreFile(container, formDocUpload); };
      formDocUpload.addEventListener('submit', h);
      lifecycle.onCleanup(() => formDocUpload.removeEventListener('submit', h));
    }

    const docsContainer = container.querySelector('#store-docs-list-container');
    if (docsContainer) {
      const h = async (e) => {
        const btn = e.target.closest('button[data-action="delete-doc"]');
        if (!btn) return;
        const row = btn.closest('.doc-item-row');
        const docId = parseInt(row.dataset.docId);
        await this.removeStoreFile(container, docId);
      };
      docsContainer.addEventListener('click', h);
      lifecycle.onCleanup(() => docsContainer.removeEventListener('click', h));
    }
  }

  destroy() {
    logger.info('RegionalAdminStores', 'Stores Management page destroyed.');
  }
}