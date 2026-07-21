/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : documents.js
 * Path              : frontend/modules/store-employee/documents/documents.js
 * Purpose           : Controller component for Barista SOPs & HR documents UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/documents/documents.html
 * Related CSS       : frontend/modules/store-employee/documents/documents.css
 * Related APIs      : GET/POST/DELETE /api/v2/employee-self-service/documents
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

/** Path to the documents HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/documents/documents.html';

export default class StoreEmployeeDocuments {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = {};
    this.requirements = [];
    this.uploadedDocs = [];
    
    this.filterCategory = 'all'; // 'all', 'sop', 'hr', 'compliance'
    this.searchQuery = '';
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeeDocuments component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeDocuments', 'Mounting Barista Documents Page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout template
    await this._loadTemplate(container);

    // 2. Fetch data from backend APIs
    await this.loadData();

    // 3. Render layout elements
    this.render(container);

    // 4. Bind event listeners
    this.bindEvents(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // DATA SERVICES / API BINDINGS
  // ---------------------------------------------------------------------------

  async loadData() {
    this.loading = true;
    try {
      // 1. Fetch current profile details to resolve store name & country
      const meRes = await apiClient.get('/auth/me');
      if (meRes?.success && meRes.data) {
        this.profile = meRes.data;
      }

      // 2. Determine country code for document requirements
      const country = this.profile.country || 'France';
      
      // 3. Fetch country document requirements from backend
      const reqRes = await apiClient.get('/api/v2/employee-self-service/document-requirements', { country });
      if (reqRes?.success && reqRes.data) {
        this.requirements = reqRes.data;
      } else {
        this.requirements = [];
      }

      // 4. Fetch actual uploaded employee documents from backend
      const docsRes = await apiClient.get('/api/v2/employee-self-service/documents');
      if (docsRes?.success && docsRes.data) {
        this.uploadedDocs = docsRes.data;
      } else {
        this.uploadedDocs = [];
      }
    } catch (err) {
      logger.error('StoreEmployeeDocuments', 'Failed to load documents data', err);
      notificationStore.danger('Error loading documents.');
    } finally {
      this.loading = false;
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    // 1. Sync header text
    const storeEl = container.querySelector('#lbl-repository-name');
    if (storeEl) {
      storeEl.textContent = this.profile.storeName || this.profile.store?.name || 'Green Park Café';
    }

    // 2. Sync search input value
    const searchInput = container.querySelector('#doc-search');
    if (searchInput) searchInput.value = this.searchQuery;

    // 3. Sync category buttons active states
    const btnAll = container.querySelector('#btn-filter-all');
    const btnSop = container.querySelector('#btn-filter-sop');
    const btnHr = container.querySelector('#btn-filter-hr');
    const btnCompliance = container.querySelector('#btn-filter-compliance');

    if (btnAll) btnAll.className = `category-filter-btn ${this.filterCategory === 'all' ? 'active' : ''}`;
    if (btnSop) btnSop.className = `category-filter-btn ${this.filterCategory === 'sop' ? 'active' : ''}`;
    if (btnHr) btnHr.className = `category-filter-btn ${this.filterCategory === 'hr' ? 'active' : ''}`;
    if (btnCompliance) btnCompliance.className = `category-filter-btn ${this.filterCategory === 'compliance' ? 'active' : ''}`;

    // 4. Populate dynamic upload document type selector dropdown
    const typeSelector = container.querySelector('#doc-type-selector');
    if (typeSelector) {
      // Preserve current selected value if any
      const currentSelected = typeSelector.value;
      typeSelector.innerHTML = '<option value="" disabled selected>Choose a category...</option>';
      if (this.requirements) {
        this.requirements.forEach(cat => {
          const groupOpt = document.createElement('optgroup');
          groupOpt.label = cat.categoryLabel;
          if (cat.docs) {
            cat.docs.forEach(d => {
              const opt = document.createElement('option');
              opt.value = d.docKey;
              opt.textContent = `${d.docTitle}${d.required ? ' *' : ''}`;
              groupOpt.appendChild(opt);
            });
          }
          typeSelector.appendChild(groupOpt);
        });
      }
      if (currentSelected) {
        typeSelector.value = currentSelected;
      }
    }

    // 5. Render Repository list rows
    this._renderDocsList(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#doc-search');
    const filterBtns = container.querySelectorAll('[data-cat]');
    const downloadBtns = container.querySelectorAll('.btn-doc-download');
    const deleteBtns = container.querySelectorAll('.btn-doc-delete');
    const dropzone = container.querySelector('#doc-upload-dropzone');
    const fileInput = container.querySelector('#doc-file-input');
    const typeSelector = container.querySelector('#doc-type-selector');
    const progressContainer = container.querySelector('#doc-upload-progress');
    const progressBar = container.querySelector('#doc-upload-bar');
    const progressFilename = container.querySelector('#doc-upload-filename');

    // Search filter input listener
    if (searchInput) {
      const handleSearch = () => {
        this.searchQuery = searchInput.value;
        this.render(container);
        this.bindEvents(container, lifecycle);
        
        const refSearch = container.querySelector('#doc-search');
        if (refSearch) {
          refSearch.focus();
          const val = refSearch.value;
          refSearch.value = '';
          refSearch.value = val;
        }
      };
      searchInput.addEventListener('input', handleSearch);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', handleSearch));
    }

    // Category filter button listener
    filterBtns.forEach(btn => {
      const handleFilter = () => {
        this.filterCategory = btn.getAttribute('data-cat');
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      btn.addEventListener('click', handleFilter);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleFilter));
    });

    // Download button click handler
    downloadBtns.forEach(btn => {
      const handleDownload = () => {
        const path = btn.getAttribute('data-path');
        const name = btn.getAttribute('data-name');
        if (path) {
          window.open(path.startsWith('/') ? path : '/' + path, '_blank');
        } else {
          notificationStore.success(`Initiated secure download of: ${name}`);
        }
      };
      btn.addEventListener('click', handleDownload);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDownload));
    });

    // Delete button click handler
    deleteBtns.forEach(btn => {
      const handleDelete = async () => {
        const type = btn.getAttribute('data-type');
        const name = btn.getAttribute('data-name');
        if (confirm(`Are you sure you want to delete your uploaded document: ${name}?`)) {
          try {
            const delRes = await apiClient.delete(`/api/v2/employee-self-service/documents/${type}`);
            if (delRes?.success) {
              notificationStore.success(`Successfully deleted: ${name}`);
              await this.loadData();
              this.render(container);
              this.bindEvents(container, lifecycle);
            } else {
              throw new Error(delRes?.message || 'Failed to delete document from backend.');
            }
          } catch (err) {
            logger.error('StoreEmployeeDocuments', 'Failed to delete document', err);
            notificationStore.danger(`Deletion failed: ${err.message}`);
          }
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });

    // File selection trigger
    const triggerUpload = async (file) => {
      const docType = typeSelector ? typeSelector.value : '';
      if (!docType) {
        notificationStore.warning('Please select a Document Type before uploading.');
        return;
      }

      if (progressFilename) progressFilename.textContent = file.name;
      if (progressContainer) progressContainer.style.display = 'block';
      if (progressBar) progressBar.style.width = '10%';
      if (dropzone) dropzone.style.display = 'none';

      // Mock progress simulation for smooth UX
      let pct = 10;
      const interval = setInterval(() => {
        pct += 15;
        if (pct > 80) clearInterval(interval);
        if (progressBar) progressBar.style.width = `${pct}%`;
      }, 100);

      try {
        // 1. Post file stream to upload-document on WebServer local storage
        const response = await fetch('/api/upload-document', {
          method: 'POST',
          headers: {
            'Content-Type': file.type,
            'X-Worker-Id': this.profile.employeeCode || String(this.profile.id || 'ADMIN'),
            'X-Document-Type': docType,
            'X-File-Name': file.name
          },
          body: file
        });

        clearInterval(interval);
        if (progressBar) progressBar.style.width = '90%';

        const data = await response.json();
        if (data.success && data.url) {
          // 2. Commit document reference metadata to PostgreSQL
          const saveRes = await apiClient.post('/api/v2/employee-self-service/documents', {
            documentType: docType,
            documentName: file.name,
            filePath: data.url
          });

          if (progressBar) progressBar.style.width = '100%';

          if (saveRes?.success) {
            notificationStore.success(`Successfully uploaded and saved: ${file.name}`);
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            throw new Error(saveRes?.message || 'Failed to persist document metadata.');
          }
        } else {
          throw new Error(data.message || 'File upload failed on server.');
        }
      } catch (err) {
        logger.error('StoreEmployeeDocuments', 'Onboarding document upload failed:', err);
        notificationStore.danger(`Upload failed: ${err.message}`);
      } finally {
        if (progressContainer) progressContainer.style.display = 'none';
        if (dropzone) dropzone.style.display = 'block';
      }
    };

    if (dropzone && fileInput) {
      const handleDropzoneClick = (e) => {
        if (e.target !== fileInput) {
          fileInput.click();
        }
      };
      const handleFileChange = (e) => {
        if (e.target.files && e.target.files[0]) {
          triggerUpload(e.target.files[0]);
        }
      };
      
      dropzone.addEventListener('click', handleDropzoneClick);
      fileInput.addEventListener('change', handleFileChange);
      
      lifecycle.onCleanup(() => {
        dropzone.removeEventListener('click', handleDropzoneClick);
        fileInput.removeEventListener('change', handleFileChange);
      });
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _mapUploadedDoc(doc) {
    // Map database category / type prefix to UI filter categories: 'hr', 'compliance'
    let uiCategory = 'compliance';
    const typeLower = (doc.documentType || '').toLowerCase();
    if (typeLower.startsWith('personal') || typeLower.startsWith('address') || typeLower.includes('contract') || typeLower.includes('hr')) {
      uiCategory = 'hr';
    }
    
    const dateStr = doc.uploadedAt 
      ? new Date(doc.uploadedAt).toLocaleDateString([], { day: '2-digit', month: 'short', year: 'numeric' })
      : 'Today';

    return {
      id: doc.id,
      name: doc.documentName,
      category: uiCategory,
      size: '1.2 MB', 
      type: doc.documentName.split('.').pop().toUpperCase() || 'PDF',
      date: dateStr,
      filePath: doc.filePath,
      isStoreManual: false,
      approved: doc.approved,
      documentType: doc.documentType
    };
  }

  _renderDocsList(container) {
    const listContainer = container.querySelector('#doc-feed-container');
    const emptyTpl = container.querySelector('#doc-empty-list-tpl');
    const rowTpl = container.querySelector('#doc-item-row-tpl');

    if (!listContainer) return;

    listContainer.replaceChildren();

    // Standard static company-provided manuals/SOPs (read-only)
    const storeManuals = [
      { 
        id: 'manual-1',
        name: 'Grinder Calibration & Beverage SOP', 
        category: 'sop', 
        size: '2.4 MB', 
        type: 'PDF', 
        date: '01 Jun 2026', 
        filePath: 'modules/store-employee/pages/documents/sample_sop.pdf',
        isStoreManual: true
      },
      { 
        id: 'manual-2',
        name: 'Monsoon Drink Recipe Brew Sheet', 
        category: 'sop', 
        size: '1.2 MB', 
        type: 'PDF', 
        date: '01 Jul 2026', 
        filePath: 'modules/store-employee/pages/documents/sample_monsoon_brew.pdf',
        isStoreManual: true
      },
      { 
        id: 'manual-3',
        name: 'Store Safety & Emergency Evacuation Plan', 
        category: 'compliance', 
        size: '3.0 MB', 
        type: 'PDF', 
        date: '15 Mar 2026', 
        filePath: 'modules/store-employee/pages/documents/sample_evacuation.pdf',
        isStoreManual: true
      },
      { 
        id: 'manual-4',
        name: 'Employee Handbook & Code of Conduct', 
        category: 'hr', 
        size: '5.5 MB', 
        type: 'PDF', 
        date: '01 Jan 2026', 
        filePath: 'modules/store-employee/pages/documents/sample_handbook.pdf',
        isStoreManual: true
      }
    ];

    const mappedUploaded = (this.uploadedDocs || []).map(d => this._mapUploadedDoc(d));
    const allDocs = [...storeManuals, ...mappedUploaded];

    const filteredDocs = allDocs.filter(d => {
      const matchCat = this.filterCategory === 'all' || d.category === this.filterCategory;
      const matchSearch = d.name.toLowerCase().includes(this.searchQuery.toLowerCase());
      return matchCat && matchSearch;
    });

    if (filteredDocs.length === 0) {
      if (emptyTpl) {
        listContainer.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    filteredDocs.forEach(doc => {
      if (!rowTpl) return;
      const clone = rowTpl.content.cloneNode(true);

      const nameEl = clone.querySelector('.doc-name');
      const sublineEl = clone.querySelector('.doc-subline');
      const downloadBtn = clone.querySelector('.btn-doc-download');
      const deleteBtn = clone.querySelector('.btn-doc-delete');
      const statusBadge = clone.querySelector('.doc-status-badge');

      if (nameEl) nameEl.textContent = doc.name;
      if (sublineEl) {
        sublineEl.innerHTML = `Size: ${doc.size} &nbsp;·&nbsp; Type: ${doc.type} &nbsp;·&nbsp; Uploaded: ${doc.date}`;
      }

      // Download button setup
      if (downloadBtn) {
        downloadBtn.setAttribute('data-name', doc.name);
        downloadBtn.setAttribute('data-path', doc.filePath);
      }

      // Deletion setup (only for employee uploaded documents that are not yet approved)
      if (deleteBtn) {
        if (!doc.isStoreManual && !doc.approved) {
          deleteBtn.style.display = 'block';
          deleteBtn.setAttribute('data-type', doc.documentType);
          deleteBtn.setAttribute('data-name', doc.name);
        } else {
          deleteBtn.style.display = 'none';
        }
      }

      // Status badge setup
      if (statusBadge) {
        if (doc.isStoreManual) {
          statusBadge.style.display = 'none';
        } else {
          statusBadge.style.display = 'inline-block';
          if (doc.approved) {
            statusBadge.className = 'doc-status-badge badge-approved';
            statusBadge.textContent = 'Approved';
          } else {
            statusBadge.className = 'doc-status-badge badge-pending';
            statusBadge.textContent = 'Pending';
          }
        }
      }

      listContainer.appendChild(clone);
    });
  }

  _loadCss() {
    const cssId = 'store-employee-docs-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/documents/documents.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeeDocuments };
