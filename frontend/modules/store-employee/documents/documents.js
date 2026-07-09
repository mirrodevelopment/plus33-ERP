/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : documents.js
 * Path              : frontend/modules/store-employee/documents/documents.js
 * Purpose           : Barista SOPs, HR payslip downloads, and certification files repository
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';

export default class StoreEmployeeDocuments {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `emp_dashboard_state_${this.user?.username || 'neha'}`;
    
    this.filterCategory = 'all'; // 'all', 'sop', 'hr', 'compliance'
    this.searchQuery = '';
    
    this.loadState();
  }

  loadState() {
    const cachedState = localStorage.getItem(this.stateKey);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
        // Ensure specific documents lists exist
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

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeDocuments', 'Mounting Barista Documents Page...');
    this.loadState();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  render(container) {
    // Apply filters & search
    const filteredDocs = this.state.documentsList.filter(d => {
      const matchCat = this.filterCategory === 'all' || d.category === this.filterCategory;
      const matchSearch = d.name.toLowerCase().includes(this.searchQuery.toLowerCase());
      return matchCat && matchSearch;
    });

    container.innerHTML = `
      <style>
        .doc-row {
          background: rgba(0,0,0,0.15);
          border: 1px solid var(--border-color);
          border-radius: var(--radius-md);
          padding: var(--spacing-sm) var(--spacing-md);
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: var(--spacing-md);
          transition: all 0.2s ease-out;
        }
        .doc-row:hover {
          border-color: rgba(255,255,255,0.08);
          background: rgba(255,255,255,0.02);
        }
        .category-filter-btn {
          padding: 6px 14px;
          border-radius: var(--radius-md);
          font-weight: 700;
          font-size: 0.72rem;
          cursor: pointer;
          background: transparent;
          border: 1px solid rgba(255,255,255,0.08);
          color: var(--text-muted);
          transition: var(--transition-fast);
        }
        .category-filter-btn.active {
          border-color: var(--accent-primary);
          background: rgba(201,164,106,0.1);
          color: var(--accent-primary);
        }
      </style>

      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header ribbon -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0;">
              Documents Center
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              SOPs, Pay Slips &amp; Compliance &nbsp;·&nbsp; Repository: <span style="color: var(--accent-primary); font-weight: 700;">Green Park Café</span>
            </p>
          </div>
          <div style="background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.72rem; font-weight: 600; color: var(--status-success); display: flex; align-items: center; gap: 6px;">
            <i data-lucide="shield-check" style="width: 14px; height: 14px;"></i> Encrypted Storage
          </div>
        </div>

        <!-- Search Bar and Category filters -->
        <div class="flex flex-col gap-sm text-left">
          <div style="width: 100%; max-width: 500px; position: relative;">
            <input type="text" id="doc-search" value="${this.searchQuery}" placeholder="Search file manuals..." style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm) var(--spacing-sm) var(--spacing-sm) 34px; outline: none; font-size: 0.8rem;">
            <i data-lucide="search" style="position: absolute; left: 10px; top: 50%; transform: translateY(-50%); width: 14px; height: 14px; color: var(--text-muted);"></i>
          </div>

          <div style="display: flex; gap: 6px; flex-wrap: wrap; margin-top: 4px;">
            <button class="category-filter-btn ${this.filterCategory === 'all' ? 'active' : ''}" data-cat="all">All Files</button>
            <button class="category-filter-btn ${this.filterCategory === 'sop' ? 'active' : ''}" data-cat="sop">SOPs &amp; Guides</button>
            <button class="category-filter-btn ${this.filterCategory === 'hr' ? 'active' : ''}" data-cat="hr">HR &amp; Agreements</button>
            <button class="category-filter-btn ${this.filterCategory === 'compliance' ? 'active' : ''}" data-cat="compliance">Compliance</button>
          </div>
        </div>

        <!-- Main workspace grid layout -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Column Left: Documents list -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; flex: 1.6;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Repository Files</h3>
              <i data-lucide="folder-open" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); min-height: 250px;">
              ${filteredDocs.length === 0 ? `
                <div style="flex:1; display:flex; flex-direction:column; align-items:center; justify-content:center; padding:var(--spacing-lg); color:var(--text-muted); border: 1.5px dashed rgba(255,255,255,0.05); border-radius:var(--radius-md); text-align:center;">
                  <i data-lucide="file-warning" style="width:36px; height:36px; margin-bottom:10px; opacity:0.3;"></i>
                  <span style="font-weight:600; font-size:0.8rem;">No matching documents found</span>
                  <span style="font-size:0.68rem; margin-top:2px;">Try adjusting your search criteria or filters.</span>
                </div>
              ` : filteredDocs.map(doc => `
                <div class="doc-row">
                  <div style="display: flex; align-items: center; gap: var(--spacing-md); flex: 1;">
                    <div style="background: rgba(201,164,106,0.1); color: var(--accent-primary); padding: 8px; border-radius: var(--radius-md); display: flex; align-items: center;">
                      <i data-lucide="file-text" style="width: 18px; height: 18px;"></i>
                    </div>
                    <div>
                      <strong style="color:var(--text-primary); font-size:0.82rem; display:block;">${doc.name}</strong>
                      <span style="font-size:0.62rem; color:var(--text-muted);">Size: ${doc.size} &nbsp;·&nbsp; Type: ${doc.type} &nbsp;·&nbsp; Uploaded: ${doc.date}</span>
                    </div>
                  </div>
                  <button class="btn btn-secondary btn-doc-download" data-name="${doc.name}" style="padding: 4px 10px; font-size: 0.65rem; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; cursor: pointer;">
                    <i data-lucide="download" style="width: 12px; height: 12px; color:var(--accent-primary);"></i> Download
                  </button>
                </div>
              `).join('')}
            </div>
          </div>

          <!-- Column Right: Mock Upload Portal -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; height: fit-content; flex: 1;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Upload Document</h3>
              <i data-lucide="upload-cloud" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); font-size: 0.76rem;">
              <p style="margin: 0; color: var(--text-muted); font-size: 0.72rem; line-height: 1.4;">
                Upload medical certificates, food safety credential cards, or training logs.
              </p>

              <!-- Drag & Drop Zone -->
              <div id="doc-upload-dropzone" style="border: 1.5px dashed var(--border-color); border-radius: var(--radius-md); padding: var(--spacing-lg); text-align: center; cursor: pointer; transition: var(--transition-fast);" onmouseover="this.style.borderColor='var(--accent-primary)';" onmouseout="this.style.borderColor='var(--border-color)';">
                <i data-lucide="upload" style="width: 24px; height: 24px; color: var(--accent-primary); margin-bottom: 6px; display: inline-block;"></i>
                <strong style="color: var(--text-primary); display: block; font-size: 0.76rem;">Choose a file or drag it here</strong>
                <span style="font-size: 0.62rem; color: var(--text-muted); display: block; margin-top: 2px;">Supported formats: PDF, PNG, JPG (Max 5MB)</span>
                
                <!-- Mock file selector -->
                <input type="file" id="doc-file-input" style="display: none;">
              </div>

              <!-- Uploading progress animation -->
              <div id="doc-upload-progress" style="display: none; background: rgba(0,0,0,0.15); padding: 10px; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                <div style="display: flex; justify-content: space-between; font-size: 0.68rem; margin-bottom: 4px;">
                  <span id="doc-upload-filename" style="font-weight: 700; color: var(--text-primary);">File.pdf</span>
                  <span style="color: var(--accent-primary); font-weight: 700;">Uploading...</span>
                </div>
                <div style="width: 100%; height: 4px; background: rgba(255,255,255,0.05); border-radius: 2px; overflow: hidden;">
                  <div id="doc-upload-bar" style="width: 0%; height: 100%; background: var(--accent-primary); border-radius: 2px; transition: width 0.1s;"></div>
                </div>
              </div>
            </div>
          </div>

        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    // 1. Search filter input
    const searchInput = container.querySelector('#doc-search');
    if (searchInput) {
      searchInput.addEventListener('input', () => {
        this.searchQuery = searchInput.value;
        this.render(container);
        this.bindEvents(container, lifecycle);
        
        // Re-focus and put cursor at end of input
        const refSearch = container.querySelector('#doc-search');
        if (refSearch) {
          refSearch.focus();
          const val = refSearch.value;
          refSearch.value = '';
          refSearch.value = val;
        }
      });
    }

    // 2. Category Filter buttons
    const filterBtns = container.querySelectorAll('[data-cat]');
    filterBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        this.filterCategory = btn.getAttribute('data-cat');
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    });

    // 3. Download trigger
    const downloadBtns = container.querySelectorAll('.btn-doc-download');
    downloadBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const name = btn.getAttribute('data-name');
        notificationStore.success(`Initiated secure download of: ${name}`);
      });
    });

    // 4. Mock File Upload dropzone click
    const dropzone = container.querySelector('#doc-upload-dropzone');
    const progressContainer = container.querySelector('#doc-upload-progress');
    const progressBar = container.querySelector('#doc-upload-bar');
    const progressFilename = container.querySelector('#doc-upload-filename');

    if (dropzone) {
      dropzone.addEventListener('click', () => {
        // Trigger mock prompt selector or just simulate immediate selection
        const mockFilename = 'Barista_Certification_SAFE101.pdf';
        
        dropzone.style.display = 'none';
        progressFilename.textContent = mockFilename;
        progressContainer.style.display = 'block';
        
        let percentage = 0;
        const interval = setInterval(() => {
          percentage += 20;
          progressBar.style.width = `${percentage}%`;
          
          if (percentage >= 100) {
            clearInterval(interval);
            setTimeout(() => {
              // Append to list
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
      });
    }
  }
}
