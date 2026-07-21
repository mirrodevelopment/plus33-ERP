/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin — Inventory Overview
 * File              : inventory.js
 * Path              : frontend/modules/store-admin/pages/inventory/inventory.js
 * Purpose           : Controller component for Store Admin Inventory UI
 * Version           : 1.0.0
 ******************************************************************************/

import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

const TEMPLATE_URL = 'modules/store-admin/pages/inventory/inventory.html';

export default class StoreInventoryPage {
  constructor() {
    this.storeId = null;
    this.companyId = null;
    this.storeName = '';
    this.stockItems = [];
    this.monthlyTrend = [];
    this.searchQuery = '';
    this.selectedCategory = 'all';
    this.selectedStatus = 'all';
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  async mount(container, lifecycle) {
    logger.info('StoreInventoryPage', 'Mounting Store Inventory Overview page...');
    
    this._loadCss();

    // 1. Inject template
    await htmlLoader.inject(TEMPLATE_URL, container);

    // 2. Fetch context & load data
    await this.init(container);

    // 3. Bind UI events
    this.bindEvents(container);
  }

  // ---------------------------------------------------------------------------
  // DATA ACTIONS: init
  // ---------------------------------------------------------------------------

  async init(container) {
    try {
      // 1. Get Me context to determine active Store ID
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes?.success && meRes.data) {
        this.storeId = meRes.data.storeId;
        this.companyId = meRes.data.companyId;
      }

      if (!this.storeId) {
        logger.error('StoreInventoryPage', 'Store ID not found in current user context.');
        this.renderErrorMessage(container, 'Failed to resolve active store profile from session.');
        return;
      }

      // 2. Fetch Store Details
      const storeRes = await apiClient.get(`/api/v1/stores/${this.storeId}`);
      if (storeRes?.success && storeRes.data) {
        this.storeName = storeRes.data.name;
        const metaEl = container.querySelector('#store-metadata-info');
        if (metaEl) {
          metaEl.textContent = `${this.storeName} (ID: ${this.storeId}) — Region: ${storeRes.data.regionName || 'Default'}`;
        }
      }

      // 3. Load Stock Ledger first, then KPIs (for fallback calculations), Monthly Trends and Catalog
      await this.loadLedger(container);
      await Promise.all([
        this.loadKpis(container),
        this.loadMonthlyTrend(container),
        this.loadProductsCatalog(container)
      ]);

    } catch (err) {
      logger.error('StoreInventoryPage', 'Error during initialization:', err);
      this.renderErrorMessage(container, 'An error occurred while initializing store inventory data.');
    }
  }

  async loadLedger(container) {
    try {
      const res = await apiClient.get(`/api/v1/analytics/inventory/store-ledger?storeId=${this.storeId}`);
      if (res?.success && res.data) {
        this.stockItems = res.data;
        
        // Populate category options filter list
        this.populateCategoriesFilter(container);
        
        // Render data rows
        this.renderLedgerTable(container);
      } else {
        throw new Error(res?.message || 'Empty response.');
      }
    } catch (err) {
      logger.error('StoreInventoryPage', 'Failed to load stock ledger:', err);
      const tbody = container.querySelector('#ledger-table-body');
      if (tbody) {
        tbody.innerHTML = `
          <tr>
            <td colspan="7" class="text-danger" style="text-align: center; padding: 1.5rem;">
              Failed to retrieve stock ledger records from backend database.
            </td>
          </tr>
        `;
      }
    }
  }

  async loadMonthlyTrend(container) {
    const barsWrapper = container.querySelector('#trend-bars-wrapper');
    if (!barsWrapper) return;

    try {
      const res = await apiClient.get(`/api/v1/analytics/inventory/store-trend?storeId=${this.storeId}`);
      if (res?.success && res.data && res.data.length > 0) {
        this.monthlyTrend = res.data;
        
        // Hide no-data message overlay
        const noDataOverlay = container.querySelector('#trend-no-data-msg');
        if (noDataOverlay) noDataOverlay.style.display = 'none';

        // Render trend columns
        this.renderTrendGraph(barsWrapper);
      } else {
        // Keep the "no data available" overlay visible
        logger.info('StoreInventoryPage', 'No historical trend data returned from backend.');
      }
    } catch (err) {
      logger.error('StoreInventoryPage', 'Failed to load monthly inventory trends:', err);
    }
  }

  async loadProductsCatalog(container) {
    try {
      const res = await apiClient.get('/api/v1/analytics/inventory/products-catalog');
      if (res?.success && res.data) {
        this.productsCatalog = res.data;
        this.setupSearchableSelectDropdown(container);
      }
    } catch (err) {
      logger.error('StoreInventoryPage', 'Failed to load products catalog:', err);
    }
  }

  setupSearchableSelectDropdown(container) {
    const wrapper = container.querySelector('#product-search-select-wrapper');
    const input = container.querySelector('#add-product-search');
    const list = container.querySelector('#add-product-options-list');
    const hiddenInput = container.querySelector('#add-product-id-val');
    const fileInput = container.querySelector('#add-product-image-file');
    const viewImg = container.querySelector('#add-product-img-view');
    
    if (!wrapper || !input || !list || !hiddenInput) return;

    // Filter and render list items
    const filterAndRender = (query = '') => {
      list.innerHTML = '';
      const normalizedQuery = query.toLowerCase().trim();
      
      const filtered = (this.productsCatalog || []).filter(p => {
        return !normalizedQuery || 
               (p.name || '').toLowerCase().includes(normalizedQuery) ||
               (p.sku || '').toLowerCase().includes(normalizedQuery) ||
               (p.category || '').toLowerCase().includes(normalizedQuery);
      });

      if (filtered.length === 0) {
        const noRes = document.createElement('div');
        noRes.className = 'searchable-no-results';
        noRes.textContent = 'No matching products found';
        list.appendChild(noRes);
      } else {
        filtered.forEach(p => {
          const item = document.createElement('div');
          item.className = 'searchable-option-item';
          item.textContent = `[${p.sku}] ${p.name} (${p.category})`;
          item.setAttribute('data-id', p.id);
          
          item.addEventListener('click', (e) => {
            e.stopPropagation();
            input.value = `[${p.sku}] ${p.name}`;
            hiddenInput.value = p.id;
            list.style.display = 'none';
            wrapper.classList.remove('open');
            
            // Store product's existing global image URL, and update preview image
            this.currentProductImageUrl = p.imageUrl || '';
            this.selectedImageFile = null;
            
            if (fileInput) fileInput.value = '';
            if (viewImg) viewImg.src = p.imageUrl || 'imgs/coffee_cup_default.png';

            // Show details inputs and submit fields after choosing product
            const fieldsContainer = container.querySelector('#add-product-fields-container');
            if (fieldsContainer) fieldsContainer.style.display = 'block';
          });
          list.appendChild(item);
        });
      }
    };

    // Toggle list visibility on input focus or click
    input.addEventListener('focus', () => {
      filterAndRender(input.value);
      list.style.display = 'block';
      wrapper.classList.add('open');
    });

    input.addEventListener('click', (e) => {
      e.stopPropagation();
      filterAndRender(input.value);
      list.style.display = 'block';
      wrapper.classList.add('open');
    });

    // Dynamic filtering on input keyup
    input.addEventListener('input', (e) => {
      hiddenInput.value = '';
      filterAndRender(e.target.value);
      
      // Reset image properties and preview if they clear selection
      this.currentProductImageUrl = '';
      this.selectedImageFile = null;
      
      if (fileInput) fileInput.value = '';
      if (viewImg) viewImg.src = 'imgs/coffee_cup_default.png';

      // Hide extra fields since product selection is cleared
      const fieldsContainer = container.querySelector('#add-product-fields-container');
      if (fieldsContainer) fieldsContainer.style.display = 'none';
    });

    // Handle selecting device image files live preview
    if (fileInput && viewImg) {
      fileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
          this.selectedImageFile = file;
          viewImg.src = URL.createObjectURL(file);
        }
      });
    }

    // Hide dropdown when clicking outside
    document.addEventListener('click', (e) => {
      if (!wrapper.contains(e.target)) {
        list.style.display = 'none';
        wrapper.classList.remove('open');
        
        if (!hiddenInput.value) {
          input.value = '';
          this.currentProductImageUrl = '';
          this.selectedImageFile = null;
          if (fileInput) fileInput.value = '';
          if (viewImg) viewImg.src = 'imgs/coffee_cup_default.png';

          // Hide fields container
          const fieldsContainer = container.querySelector('#add-product-fields-container');
          if (fieldsContainer) fieldsContainer.style.display = 'none';
        }
      }
    });
  }

  // ---------------------------------------------------------------------------
  // VIEW RENDERING HELPERS
  // ---------------------------------------------------------------------------

  async loadKpis(container) {
    try {
      const res = await apiClient.get(`/api/v1/analytics/inventory/kpis?companyId=${this.companyId}&storeId=${this.storeId}`);
      let kpis = res?.success ? res.data : null;

      // Fallback: If DB materialized views are not refreshed (returning 0) but we have items in stockItems list
      if ((!kpis || !kpis.totalUniqueProducts) && this.stockItems && this.stockItems.length > 0) {
        const uniqueProds = this.stockItems.length;
        const lowStock = this.stockItems.filter(item => {
          const qty = parseFloat(item.qty) || 0;
          const reorder = parseFloat(item.reorderLevel) || 0;
          return qty > 0 && qty <= reorder;
        }).length;
        const outStock = this.stockItems.filter(item => (parseFloat(item.qty) || 0) <= 0).length;

        kpis = {
          totalUniqueProducts: uniqueProds,
          totalStockValue: 0.00,
          lowStockProducts: lowStock,
          outOfStockProducts: outStock
        };
      }

      if (kpis) {
        // 1. Unique products
        const trackedEl = container.querySelector('#val-tracked-products');
        if (trackedEl) {
          trackedEl.textContent = (kpis.totalUniqueProducts || 0).toLocaleString();
        }

        // 2. Total stock value
        const valueEl = container.querySelector('#val-stock-value');
        if (valueEl) {
          const val = parseFloat(kpis.totalStockValue) || 0;
          valueEl.textContent = `₹ ${val.toLocaleString(undefined, { maximumFractionDigits: 0 })}`;
        }

        // 3. Low stock count
        const lowEl = container.querySelector('#val-low-stock');
        if (lowEl) {
          lowEl.textContent = (kpis.lowStockProducts || 0).toLocaleString();
        }

        // 4. Out of stock count
        const outEl = container.querySelector('#val-out-stock');
        if (outEl) {
          outEl.textContent = (kpis.outOfStockProducts || 0).toLocaleString();
        }
      }
    } catch (err) {
      logger.error('StoreInventoryPage', 'Failed to load KPI metrics:', err);
    }
  }

  populateCategoriesFilter(container) {
    const select = container.querySelector('#ledger-category-filter');
    if (!select) return;

    // Clear existing dynamic option elements
    while (select.options.length > 1) {
      select.remove(1);
    }

    const categories = [...new Set(this.stockItems.map(item => item.category))].sort();
    categories.forEach(cat => {
      if (cat) {
        const opt = document.createElement('option');
        opt.value = cat;
        opt.textContent = cat;
        select.appendChild(opt);
      }
    });
  }

  renderTrendGraph(container) {
    // Group monthly trend records by Month/Year and sum their totalIn quantities
    const grouped = {};
    this.monthlyTrend.forEach(row => {
      const label = `${row.monthName.substring(0, 3)} ${row.year.toString().substring(2)}`;
      grouped[label] = (grouped[label] || 0) + (parseFloat(row.totalIn) || 0);
    });

    const entries = Object.entries(grouped);
    if (entries.length === 0) return;

    // Clear container (except overlay which is hidden)
    const elements = container.querySelectorAll('.trend-bar-wrapper');
    elements.forEach(el => el.remove());

    // Find max value to scale the bars height
    const maxVal = Math.max(...entries.map(([_, val]) => val), 100);

    // Update Y-Axis labels dynamically
    const axisLabels = container.parentElement.querySelector('#trend-axis-labels');
    if (axisLabels) {
      axisLabels.innerHTML = `
        <span>${Math.round(maxVal).toLocaleString()}</span>
        <span>${Math.round(maxVal * 0.66).toLocaleString()}</span>
        <span>${Math.round(maxVal * 0.33).toLocaleString()}</span>
        <span>0</span>
      `;
    }

    entries.forEach(([label, value]) => {
      const colHeightPct = Math.min((value / maxVal) * 90 + 5, 95); // bounds height pct between 5% and 95%
      
      const barWrapper = document.createElement('div');
      barWrapper.className = 'trend-bar-wrapper';
      
      barWrapper.innerHTML = `
        <div class="trend-bar-fill" style="height: ${colHeightPct}%;">
          <div class="trend-bar-tooltip">Received: ${value.toLocaleString(undefined, { maximumFractionDigits: 1 })}</div>
        </div>
        <span class="trend-bar-label">${label}</span>
      `;
      container.appendChild(barWrapper);
    });
  }

  renderLedgerTable(container) {
    const tbody = container.querySelector('#ledger-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    // Filter items
    const filtered = this.stockItems.filter(item => {
      // 1. Text Search matching
      const query = this.searchQuery.toLowerCase().trim();
      const matchesText = !query || 
                          (item.sku && item.sku.toLowerCase().includes(query)) ||
                          (item.name && item.name.toLowerCase().includes(query));
      
      // 2. Category matching
      const matchesCategory = this.selectedCategory === 'all' || item.category === this.selectedCategory;
      
      // 3. Status matching
      const qty = parseFloat(item.qty) || 0;
      const reorder = parseFloat(item.reorderLevel) || 0;
      let statusType = 'healthy';
      if (qty <= 0) {
        statusType = 'out';
      } else if (qty <= reorder) {
        statusType = 'low';
      }
      const matchesStatus = this.selectedStatus === 'all' || statusType === this.selectedStatus;

      return matchesText && matchesCategory && matchesStatus;
    });

    if (filtered.length === 0) {
      tbody.innerHTML = `
        <tr>
          <td colspan="8" style="text-align: center; color: var(--text-muted, rgba(255,255,255,0.45)); padding: 2rem;">
            No stock records match the specified search or filter criteria.
          </td>
        </tr>
      `;
      return;
    }

    // Render items
    filtered.forEach(item => {
      const tr = document.createElement('tr');
      
      const qty = parseFloat(item.qty) || 0;
      const reserved = parseFloat(item.reserved) || 0;
      const reorder = parseFloat(item.reorderLevel) || 0;
      const uom = item.uom || 'units';

      let statusBadge = '';
      if (qty <= 0) {
        statusBadge = `<span class="badge badge-out">Out of Stock</span>`;
      } else if (qty <= reorder) {
        statusBadge = `<span class="badge badge-low">Low Stock</span>`;
      } else {
        statusBadge = `<span class="badge badge-healthy">Healthy</span>`;
      }

      tr.innerHTML = `
        <td>
          <img src="${item.imageUrl || 'imgs/coffee_cup_default.png'}" class="ledger-thumb" onerror="this.src='imgs/coffee_cup_default.png'">
        </td>
        <td class="sku-cell">${item.sku || 'N/A'}</td>
        <td>${item.name || 'Unknown Product'}</td>
        <td>${item.category || 'General'}</td>
        <td><strong>${qty.toLocaleString(undefined, { maximumFractionDigits: 1 })}</strong> <span style="font-size: 0.75rem; opacity: 0.65;">${uom}</span></td>
        <td>${reserved.toLocaleString(undefined, { maximumFractionDigits: 1 })} <span style="font-size: 0.75rem; opacity: 0.65;">${uom}</span></td>
        <td>${reorder.toLocaleString(undefined, { maximumFractionDigits: 1 })} <span style="font-size: 0.75rem; opacity: 0.65;">${uom}</span></td>
        <td>${statusBadge}</td>
      `;
      tbody.appendChild(tr);
    });
  }

  renderErrorMessage(container, message) {
    const workspace = container.querySelector('.store-inventory-workspace');
    if (workspace) {
      workspace.innerHTML = `
        <div class="store-inventory-header" style="justify-content: center; padding: 2rem; border-color: rgba(239, 68, 68, 0.3);">
          <div style="text-align: center;">
            <span style="font-size: 2.5rem;">🚨</span>
            <h3 style="margin: 0.5rem 0; color: #ef4444;">Access Restriction / Error</h3>
            <p style="margin: 0; font-size: 0.85rem; color: rgba(255,255,255,0.6);">${message}</p>
          </div>
        </div>
      `;
    }
  }

  // ---------------------------------------------------------------------------
  // INTERACTIONS & EVENTS: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container) {
    // 1. Text Search Box
    const searchInput = container.querySelector('#ledger-search-input');
    if (searchInput) {
      searchInput.addEventListener('input', (e) => {
        this.searchQuery = e.target.value;
        this.renderLedgerTable(container);
      });
    }

    // 2. Category Filter Dropdown
    const catSelect = container.querySelector('#ledger-category-filter');
    if (catSelect) {
      catSelect.addEventListener('change', (e) => {
        this.selectedCategory = e.target.value;
        this.renderLedgerTable(container);
      });
    }

    // 3. Status Button Filters
    const statusBtns = container.querySelectorAll('.status-filter-btn');
    statusBtns.forEach(btn => {
      btn.addEventListener('click', (e) => {
        statusBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        this.selectedStatus = btn.getAttribute('data-status');
        this.renderLedgerTable(container);
      });
    });

    // 4. Add Product Form (Quick Add Stock Card)
    const form = container.querySelector('#add-product-form');
    if (form) {
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const hiddenInput = container.querySelector('#add-product-id-val');
        const qtyInput = container.querySelector('#add-product-qty');
        const fileInput = container.querySelector('#add-product-image-file');
        
        if (!hiddenInput || !qtyInput || !fileInput) return;

        const productId = hiddenInput.value;
        const quantity = qtyInput.value.trim();

        if (!productId) {
          alert('Please select a product from the catalog list.');
          return;
        }

        try {
          const submitBtn = container.querySelector('#btn-submit-add-product');
          if (submitBtn) submitBtn.disabled = true;

          let imageUrl = this.currentProductImageUrl || '';

          // Upload the image file from device if selected
          if (this.selectedImageFile) {
            const uploadRes = await fetch('/api/upload-product-image', {
              method: 'POST',
              headers: {
                'Content-Type': this.selectedImageFile.type,
                'X-File-Name': this.selectedImageFile.name
              },
              body: this.selectedImageFile
            });
            const uploadData = await uploadRes.json();
            if (uploadData.success && uploadData.url) {
              imageUrl = uploadData.url;
            } else {
              throw new Error(uploadData.message || 'Failed to upload image from device.');
            }
          }

          // Build query URL dynamically.
          let queryUrl = `/api/v1/analytics/inventory/add-product-stock?storeId=${this.storeId}&productId=${productId}&imageUrl=${encodeURIComponent(imageUrl)}`;
          if (quantity !== '') {
            queryUrl += `&quantity=${quantity}`;
          }

          const res = await apiClient.post(queryUrl);
          if (res?.success) {
            form.reset();
            this.selectedImageFile = null;
            this.currentProductImageUrl = '';
            
            const viewImg = container.querySelector('#add-product-img-view');
            if (viewImg) viewImg.src = 'imgs/coffee_cup_default.png';

            const fieldsContainer = container.querySelector('#add-product-fields-container');
            if (fieldsContainer) fieldsContainer.style.display = 'none';
            
            // Re-load data catalog so the product catalog has the newly updated image URL!
            await this.loadProductsCatalog(container).catch(() => {});

            await Promise.all([
              this.loadLedger(container),
              this.loadKpis(container),
              apiClient.post('/api/v1/analytics/inventory/refresh').catch(() => {})
            ]);
            this.loadMonthlyTrend(container).catch(() => {});
          } else {
            alert(res?.message || 'Failed to update product stock/image.');
          }
        } catch (err) {
          logger.error('StoreInventoryPage', 'Error updating product stock/image:', err);
          alert(err.message || 'An error occurred while updating product stock/image.');
        } finally {
          const submitBtn = container.querySelector('#btn-submit-add-product');
          if (submitBtn) submitBtn.disabled = false;
        }
      });
    }
  }

  // ---------------------------------------------------------------------------
  // UTILITY HELPER: _loadCss
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'store-inventory-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/inventory/inventory.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreInventoryPage };
