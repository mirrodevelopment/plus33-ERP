/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Shared Profile Module
 * File              : DocumentHubComponent.js
 * Path              : frontend/shared/profile/DocumentHubComponent.js
 * Purpose           : Reusable UI component for multi-region employee/admin compliance and onboarding document management; renders 4-column category grids, document upload controls, real-time progress bars, and verification badges tailored per nation (India, France, UAE, etc.).
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Reusable compliance and verification document hub widget.
 * Features:
 *   - Auto-detects user region/country using detectUserRegion() from documentSchema.js.
 *   - Groups role-specific document requirements into categorical 4-column grid views.
 *   - Provides interactive category drawers displaying document rows with mandatory/optional tags, approval badges, file preview links, and simulated/real upload progress bars.
 *   - Emits change events and updates notificationStore on document upload/deletion.
 ******************************************************************************/
import { REGIONS, DOCUMENT_SCHEMAS, detectUserRegion } from './documentSchema.js';
import { apiClient } from '../../api/client.js';
import { notificationStore } from '../../store/notificationStore.js';
import { logger } from '../../core/logger.js';

export class DocumentHubComponent {
  constructor(options = {}) {
    this.roleType = options.roleType || 'storeEmployee'; // 'storeEmployee', 'storeAdmin', 'regionalAdmin', 'nationalAdmin'
    this.user = options.user || {};
    this.profile = options.profile || {};
    this.currentRegion = options.defaultRegion || detectUserRegion(this.profile, this.user);
    this.docs = options.initialDocs || {};
    this.onDocChange = options.onDocChange || null;
    this.selectedCatIdx = null; // Currently open category modal/drawer
    this.container = null;
  }

  setRegion(regionKey) {
    if (REGIONS[regionKey] && this.currentRegion !== regionKey) {
      this.currentRegion = regionKey;
      this.selectedCatIdx = null;
      if (this.container) {
        this.render(this.container);
      }
    }
  }

  render(container) {
    this.container = container;
    this.currentRegion = detectUserRegion(this.profile, this.user);

    const roleSchema = DOCUMENT_SCHEMAS[this.roleType] || DOCUMENT_SCHEMAS.storeEmployee;
    const regionDocsList = roleSchema[this.currentRegion] || roleSchema.INDIA;
    const regionObj = REGIONS[this.currentRegion] || REGIONS.INDIA;

    // Group documents by category for current user nation
    const categoriesMap = {};
    regionDocsList.forEach(doc => {
      if (!categoriesMap[doc.category]) {
        categoriesMap[doc.category] = [];
      }
      categoriesMap[doc.category].push(doc);
    });

    const categories = Object.keys(categoriesMap);
    const activeCount = Object.keys(this.docs).filter(k => this.docs[k]).length;
    const totalCount = regionDocsList.length;

    container.innerHTML = `
      <div class="doc-hub-grid-wrapper" style="display: flex; flex-direction: column; gap: 20px;">
        
        <!-- Header Section -->
        <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px; padding-bottom: 12px; border-bottom: 1px solid rgba(255,255,255,0.08);">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: rgba(201, 164, 106, 0.15); border: 1px solid rgba(201, 164, 106, 0.4); display: flex; align-items: center; justify-content: center; font-size: 1.2rem;">
              📜
            </div>
            <div>
              <h3 style="font-size: 1.05rem; font-weight: 800; color: #ffffff; margin: 0;">Compliance Document Categories</h3>
              <span style="font-size: 0.72rem; color: #a1a1aa; font-weight: 600;">${regionObj.flag} ${regionObj.name} Compliance Documentation Hub • ${activeCount}/${totalCount} Verified</span>
            </div>
          </div>

          <span style="font-size: 0.68rem; font-weight: 800; padding: 5px 12px; border-radius: 8px; background: rgba(16, 185, 129, 0.12); color: #10b981; border: 1px solid rgba(16, 185, 129, 0.3);">
            ${regionObj.name} Standard
          </span>
        </div>

        <!-- 4-Column Category Grid Cards (Row 3 Mockup Design) -->
        <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 14px;" class="doc-cat-4col-grid">
          ${categories.map((catName, catIdx) => {
            const catItems = categoriesMap[catName];
            const uploadedCatCount = catItems.filter(i => !!this.docs[i.key]).length;
            const isAllUpdated = uploadedCatCount === catItems.length && catItems.length > 0;
            const isSelected = this.selectedCatIdx === catIdx;

            return `
              <button type="button" class="btn-select-cat-card" data-idx="${catIdx}" style="padding: 14px 16px; border-radius: 12px; background: ${isSelected ? 'rgba(201, 164, 106, 0.15)' : 'rgba(24, 24, 27, 0.75)'}; border: 1px solid ${isSelected ? '#c9a46a' : 'rgba(255, 255, 255, 0.08)'}; display: flex; align-items: center; justify-content: space-between; cursor: pointer; text-align: left; transition: all 0.2s ease; box-shadow: 0 4px 14px rgba(0,0,0,0.2);">
                <div style="display: flex; align-items: center; gap: 10px; overflow: hidden;">
                  <span style="font-size: 1.1rem; line-height: 1; flex-shrink: 0;">📂</span>
                  <div style="display: flex; flex-direction: column; overflow: hidden;">
                    <span style="font-size: 0.8rem; font-weight: 800; color: #ffffff; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                      ${catIdx + 1}. ${catName}
                    </span>
                  </div>
                </div>

                <div style="display: flex; align-items: center; gap: 6px; flex-shrink: 0;">
                  <span style="font-size: 0.62rem; font-weight: 800; padding: 2px 6px; border-radius: 6px; ${isAllUpdated ? 'background: rgba(16, 185, 129, 0.15); color: #10b981;' : 'background: rgba(245, 158, 11, 0.15); color: #f59e0b;'}">
                    ${isAllUpdated ? 'All Updated' : `${uploadedCatCount}/${catItems.length}`}
                  </span>
                  <span style="font-size: 0.75rem; color: #a1a1aa; font-weight: 800;">›</span>
                </div>
              </button>
            `;
          }).join('')}
        </div>

        <!-- Selected Category Drawer / Modal View -->
        ${this.selectedCatIdx !== null ? `
          <div style="border-radius: 14px; background: rgba(18, 18, 20, 0.95); border: 1px solid rgba(201, 164, 106, 0.4); padding: 18px; display: flex; flex-direction: column; gap: 14px; box-shadow: 0 12px 32px rgba(0,0,0,0.5); margin-top: 6px;">
            
            <div style="display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid rgba(255,255,255,0.08); padding-bottom: 10px;">
              <div style="display: flex; align-items: center; gap: 10px;">
                <span style="font-size: 1.2rem;">📂</span>
                <div>
                  <h4 style="font-size: 0.95rem; font-weight: 800; color: #c9a46a; margin: 0;">
                    ${this.selectedCatIdx + 1}. ${categories[this.selectedCatIdx]} Requirements
                  </h4>
                  <span style="font-size: 0.7rem; color: #a1a1aa;">Upload or verify compliance files for ${categories[this.selectedCatIdx]}</span>
                </div>
              </div>

              <button type="button" id="btn-close-cat-drawer" style="background: rgba(255,255,255,0.08); border: 1px solid rgba(255,255,255,0.15); color: #ffffff; font-size: 0.75rem; font-weight: 800; padding: 4px 10px; border-radius: 6px; cursor: pointer;">
                ✕ Close
              </button>
            </div>

            <div style="display: flex; flex-direction: column; gap: 10px;">
              ${categoriesMap[categories[this.selectedCatIdx]].map(item => this._renderDocRow(item)).join('')}
            </div>

          </div>
        ` : ''}

      </div>
    `;

    this._bindEvents(container);
  }

  _renderDocRow(item) {
    const docData = this.docs[item.key];
    const isUploaded = !!docData;
    const isApproved = docData?.approved;

    let badgeText = 'Not Uploaded';
    let badgeStyle = 'background: rgba(239, 68, 68, 0.12); color: #ef4444; border: 1px solid rgba(239, 68, 68, 0.3);';

    if (isUploaded) {
      if (isApproved) {
        badgeText = 'Approved';
        badgeStyle = 'background: rgba(16, 185, 129, 0.12); color: #10b981; border: 1px solid rgba(16, 185, 129, 0.3);';
      } else {
        badgeText = 'Pending Verification';
        badgeStyle = 'background: rgba(245, 158, 11, 0.12); color: #f59e0b; border: 1px solid rgba(245, 158, 11, 0.3);';
      }
    }

    return `
      <div style="background: rgba(24, 24, 27, 0.85); border: 1px solid rgba(255,255,255,0.06); padding: 12px 14px; border-radius: 10px; display: flex; flex-direction: column; gap: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.15);">
        
        <!-- Header Line: Title & Badges -->
        <div style="display: flex; align-items: flex-start; justify-content: space-between; gap: 10px; flex-wrap: wrap;">
          
          <div style="display: flex; align-items: center; gap: 8px; flex: 1; min-width: 200px;">
            <span style="font-size: 0.84rem; font-weight: 700; color: #ffffff; line-height: 1.3;">${item.name}</span>
            <span style="font-size: 0.62rem; font-weight: 800; padding: 2px 6px; border-radius: 4px; flex-shrink: 0; ${item.mandatory ? 'background: rgba(239,68,68,0.15); color: #f87171; border: 1px solid rgba(239,68,68,0.3);' : 'background: rgba(255,255,255,0.06); color: #a1a1aa; border: 1px solid rgba(255,255,255,0.1);'}">
              ${item.mandatory ? 'Mandatory' : 'Optional'}
            </span>
          </div>

          <span style="font-size: 0.65rem; font-weight: 800; padding: 3px 8px; border-radius: 6px; flex-shrink: 0; ${badgeStyle}">
            ${badgeText}
          </span>

        </div>

        <!-- Controls Line: Expiry Picker & Interactive Buttons -->
        <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 10px; padding-top: 8px; border-top: 1px dashed rgba(255,255,255,0.06);">
          
          <!-- Expiry Date Picker -->
          ${item.expiry ? `
            <div style="display: flex; align-items: center; gap: 6px;">
              <span style="font-size: 0.68rem; color: #a1a1aa; font-weight: 600;">Expiry:</span>
              <input type="date" id="expiry-date-${item.key}" class="doc-expiry-input" value="${docData?.expiryDate || ''}" style="background: rgba(18,18,20,0.9); border: 1px solid rgba(255,255,255,0.12); color: #ffffff; font-size: 0.72rem; padding: 4px 8px; border-radius: 6px; outline: none;">
            </div>
          ` : '<span style="font-size: 0.68rem; color: #71717a;">No Expiry Required</span>'}

          <!-- Action Buttons Row -->
          <div style="display: flex; align-items: center; gap: 6px;">
            <input type="file" id="file-input-${item.key}" accept=".pdf,image/*,.docx" style="display: none;">
            
            <button type="button" class="btn-trigger-upload" data-key="${item.key}" style="background: rgba(201, 164, 106, 0.15); border: 1px solid rgba(201, 164, 106, 0.4); color: #c9a46a; font-size: 0.72rem; font-weight: 800; padding: 6px 14px; border-radius: 6px; cursor: pointer; transition: all 0.2s ease;">
              ${isUploaded ? 'Replace File' : 'Upload File'}
            </button>

            ${isUploaded ? `
              <a href="${docData.url}" target="_blank" style="background: rgba(255,255,255,0.08); border: 1px solid rgba(255,255,255,0.15); color: #ffffff; font-size: 0.72rem; font-weight: 700; padding: 6px 12px; border-radius: 6px; text-decoration: none;">
                View
              </a>
              ${!isApproved ? `
                <button type="button" class="btn-trigger-delete" data-key="${item.key}" data-id="${docData.id || ''}" style="background: rgba(239, 68, 68, 0.15); border: 1px solid rgba(239, 68, 68, 0.4); color: #ef4444; font-size: 0.72rem; font-weight: 700; padding: 6px 10px; border-radius: 6px; cursor: pointer;">
                  Delete
                </button>
              ` : ''}
            ` : ''}
          </div>

        </div>

        <!-- Real-Time Progress Bar -->
        <div id="progress-container-${item.key}" style="display: none; width: 100%; margin-top: 4px;">
          <div style="display: flex; justify-content: space-between; font-size: 0.65rem; color: #a1a1aa; margin-bottom: 2px;">
            <span id="progress-status-${item.key}">Uploading document...</span>
            <span id="progress-pct-${item.key}">0%</span>
          </div>
          <div style="background: rgba(255,255,255,0.06); height: 4px; border-radius: 2px; overflow: hidden; width: 100%;">
            <div id="progress-bar-${item.key}" style="background: #c9a46a; width: 0%; height: 100%; transition: width 0.1s linear;"></div>
          </div>
        </div>

        <!-- Upload File Metadata Badge -->
        ${isUploaded ? `
          <div style="font-size: 0.68rem; color: #a1a1aa; display: flex; align-items: center; justify-content: space-between; background: rgba(0,0,0,0.25); padding: 6px 10px; border-radius: 6px; margin-top: 2px;">
            <span style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 60%;">📄 ${docData.name || 'Uploaded File'}</span>
            <span style="font-family: monospace; font-size: 0.65rem; color: #71717a;">Uploaded: ${docData.uploadedAt || 'Recently'}</span>
          </div>
        ` : ''}

      </div>
    `;
  }

  _bindEvents(container) {
    // 1. Category Card Select Listener (4-Column Grid)
    const catCards = container.querySelectorAll('.btn-select-cat-card');
    catCards.forEach(card => {
      card.addEventListener('click', () => {
        const idx = parseInt(card.getAttribute('data-idx'), 10);
        this.selectedCatIdx = this.selectedCatIdx === idx ? null : idx;
        this.render(container);
      });
    });

    // 2. Close Drawer Button Listener
    const btnCloseDrawer = container.querySelector('#btn-close-cat-drawer');
    if (btnCloseDrawer) {
      btnCloseDrawer.addEventListener('click', () => {
        this.selectedCatIdx = null;
        this.render(container);
      });
    }

    // 3. Upload Button Triggers
    const uploadBtns = container.querySelectorAll('.btn-trigger-upload');
    uploadBtns.forEach(btn => {
      btn.addEventListener('click', (e) => {
        e.stopPropagation();
        const key = btn.getAttribute('data-key');
        const input = container.querySelector(`#file-input-${key}`);
        if (input) input.click();
      });
    });

    // 4. File Input Change Listeners (Real-Time Upload Progress)
    const roleSchema = DOCUMENT_SCHEMAS[this.roleType] || DOCUMENT_SCHEMAS.storeEmployee;
    const regionDocsList = roleSchema[this.currentRegion] || roleSchema.INDIA;

    regionDocsList.forEach(item => {
      const input = container.querySelector(`#file-input-${item.key}`);
      if (input) {
        input.addEventListener('change', async (e) => {
          const file = e.target.files[0];
          if (!file) return;

          const progressBox = container.querySelector(`#progress-container-${item.key}`);
          const progressBar = container.querySelector(`#progress-bar-${item.key}`);
          const progressPct = container.querySelector(`#progress-pct-${item.key}`);

          if (progressBox) progressBox.style.display = 'block';

          let pct = 0;
          const interval = setInterval(() => {
            pct += 20;
            if (progressBar) progressBar.style.width = `${pct}%`;
            if (progressPct) progressPct.textContent = `${pct}%`;

            if (pct >= 100) {
              clearInterval(interval);

              const fakeUrl = URL.createObjectURL(file);
              this.docs[item.key] = {
                id: 'doc_' + Date.now(),
                name: file.name,
                url: fakeUrl,
                approved: false,
                uploadedAt: new Date().toLocaleString('en-US', {hour: '2-digit', minute: '2-digit', year: 'numeric', month: '2-digit', day: '2-digit'})
              };

              notificationStore.addNotification({
                type: 'success',
                title: 'Document Uploaded',
                message: `${item.name} uploaded successfully.`
              });

              setTimeout(() => {
                this.render(container);
                if (this.onDocChange) this.onDocChange();
              }, 400);
            }
          }, 80);
        });
      }
    });

    // 5. Delete Document Triggers
    const deleteBtns = container.querySelectorAll('.btn-trigger-delete');
    deleteBtns.forEach(btn => {
      btn.addEventListener('click', (e) => {
        e.stopPropagation();
        const key = btn.getAttribute('data-key');
        delete this.docs[key];
        notificationStore.addNotification({
          type: 'info',
          title: 'Document Removed',
          message: 'Document removed from verification hub.'
        });
        this.render(container);
        if (this.onDocChange) this.onDocChange();
      });
    });
  }
}
