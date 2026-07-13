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
 * Related APIs      : None (uses LocalStorage states caching)
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in documents.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the documents HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/documents/documents.html';

export default class StoreEmployeeDocuments {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `emp_dashboard_state_${this.user?.username || 'neha'}`;
    
    this.filterCategory = 'all'; // 'all', 'sop', 'hr', 'compliance'
    this.searchQuery = '';
    
    this.loadState();
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

    // 2. Read state details
    this.loadState();

    // 3. Render layout elements
    this.render(container);

    // 4. Bind event listeners
    this.bindEvents(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  loadState() {
    const cachedState = localStorage.getItem(this.stateKey);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
        if (!this.state.documentsList) {
          this.state.documentsList = [
            { id: 1, name: 'Grinder Calibration & Beverage SOP', category: 'sop', size: '2.4 MB', type: 'PDF', date: '01 Jun 2026' },
            { id: 2, name: 'Barista Compensation Agreement', category: 'hr', size: '1.8 MB', type: 'PDF', date: '12 May 2026' },
            { id: 3, name: 'FSSAI Hygiene License - Green Park Café', category: 'compliance', size: '4.1 MB', type: 'PDF', date: '10 Jan 2026' },
            { id: 4, name: 'Monsoon Drink Recipe Brew Sheet', category: 'sop', size: '1.2 MB', type: 'PDF', date: '01 Jul 2026' }
          ];
        }
      } catch (err) {
        logger.error('StoreEmployeeDocuments', 'Error parsing cached state', err);
        this.initDefaultState();
      }
    } else {
      this.initDefaultState();
    }
  }

  initDefaultState() {
    this.state = {
      name: this.profile.name || 'Neha Sharma',
      id: 'EMP10245',
      level: 'Senior Barista',
      store: 'Green Park Café, City Center',
      clockedIn: true,
      clockInTime: '08:02 AM',
      trainingProgress: 72,
      performanceScore: 4.6,
      tasks: [],
      leave: { available: 12.5, pending: 1, approved: 3 },
      attendanceLogs: [],
      activities: [],
      documentsList: [
        { id: 1, name: 'Grinder Calibration & Beverage SOP', category: 'sop', size: '2.4 MB', type: 'PDF', date: '01 Jun 2026' },
        { id: 2, name: 'Barista Compensation Agreement', category: 'hr', size: '1.8 MB', type: 'PDF', date: '12 May 2026' },
        { id: 3, name: 'FSSAI Hygiene License - Green Park Café', category: 'compliance', size: '4.1 MB', type: 'PDF', date: '10 Jan 2026' },
        { id: 4, name: 'Monsoon Drink Recipe Brew Sheet', category: 'sop', size: '1.2 MB', type: 'PDF', date: '01 Jul 2026' }
      ]
    };
    this.saveState();
  }

  saveState() {
    localStorage.setItem(this.stateKey, JSON.stringify(this.state));
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    // 1. Sync header text
    const storeEl = container.querySelector('#lbl-repository-name');
    if (storeEl) storeEl.textContent = this.state.store || 'Green Park Café';

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

    // 4. Render Repository list rows
    this._renderDocsList(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#doc-search');
    const filterBtns = container.querySelectorAll('[data-cat]');
    const downloadBtns = container.querySelectorAll('.btn-doc-download');
    const dropzone = container.querySelector('#doc-upload-dropzone');
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
        const name = btn.getAttribute('data-name');
        notificationStore.success(`Initiated secure download of: ${name}`);
      };
      btn.addEventListener('click', handleDownload);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDownload));
    });

    // Drag & Drop mock selector click handler
    if (dropzone) {
      const handleMockUpload = () => {
        const mockFilename = 'Barista_Certification_SAFE101.pdf';
        
        dropzone.style.display = 'none';
        if (progressFilename) progressFilename.textContent = mockFilename;
        if (progressContainer) progressContainer.style.display = 'block';
        
        let percentage = 0;
        const interval = setInterval(() => {
          percentage += 20;
          if (progressBar) progressBar.style.width = `${percentage}%`;
          
          if (percentage >= 100) {
            clearInterval(interval);
            setTimeout(() => {
              const newId = this.state.documentsList.length ? Math.max(...this.state.documentsList.map(d => d.id)) + 1 : 1;
              this.state.documentsList.push({
                id: newId,
                name: mockFilename,
                category: 'compliance',
                size: '1.5 MB',
                type: 'PDF',
                date: 'Today, Just now'
              });
              
              notificationStore.success(`Successfully uploaded: ${mockFilename}`);
              this.state.activities.unshift({ text: `Uploaded certificate "${mockFilename}"`, time: 'Just now' });
              
              this.saveState();
              this.render(container);
              this.bindEvents(container, lifecycle);
            }, 300);
          }
        }, 150);
      };
      dropzone.addEventListener('click', handleMockUpload);
      lifecycle.onCleanup(() => dropzone.removeEventListener('click', handleMockUpload));
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderDocsList(container) {
    const listContainer = container.querySelector('#doc-feed-container');
    const emptyTpl = container.querySelector('#doc-empty-list-tpl');
    const rowTpl = container.querySelector('#doc-item-row-tpl');

    if (!listContainer) return;

    listContainer.replaceChildren();

    const filteredDocs = this.state.documentsList.filter(d => {
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

      const row = clone.querySelector('.doc-row');
      const nameEl = clone.querySelector('.doc-name');
      const sublineEl = clone.querySelector('.doc-subline');
      const downloadBtn = clone.querySelector('.btn-doc-download');

      if (nameEl) nameEl.textContent = doc.name;
      if (sublineEl) {
        sublineEl.innerHTML = `Size: ${doc.size} &nbsp;·&nbsp; Type: ${doc.type} &nbsp;·&nbsp; Uploaded: ${doc.date}`;
      }
      if (downloadBtn) {
        downloadBtn.setAttribute('data-name', doc.name);
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
