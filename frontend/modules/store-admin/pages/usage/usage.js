/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin — Daily Inventory Usage Tracker
 * File              : usage.js
 * Path              : frontend/modules/store-admin/pages/usage/usage.js
 * Purpose           : Controller component for Daily Usage Tracker UI
 * Version           : 1.0.0
 ******************************************************************************/

import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

const TEMPLATE_URL = 'modules/store-admin/pages/usage/usage.html';

export default class StoreUsagePage {
  constructor() {
    this.storeId = null;
    this.companyId = null;
    this.storeName = '';
    this.storeTimezone = 'UTC';
    this.productsCatalog = [];
    this.usageLogs = [];
    this.searchQuery = '';
    this.currentProductImageUrl = '';
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  async mount(container, lifecycle) {
    logger.info('StoreUsagePage', 'Mounting Daily Usage Tracker page...');
    
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
        logger.error('StoreUsagePage', 'Store ID not found in current user context.');
        this.renderErrorMessage(container, 'Failed to resolve active store profile from session.');
        return;
      }

      // 2. Fetch Store Details
      const storeRes = await apiClient.get(`/api/v1/stores/${this.storeId}`);
      if (storeRes?.success && storeRes.data) {
        this.storeName = storeRes.data.name;
        this.storeTimezone = storeRes.data.timezone || 'UTC';
      }

      // Set default date to today's date in store timezone (YYYY-MM-DD)
      const dateInput = container.querySelector('#usage-date-picker');
      if (dateInput) {
        dateInput.value = this.getTodayInTimezone(this.storeTimezone);
      }

      // 3. Parallel fetch products catalog and usage logs
      await Promise.all([
        this.loadProductsCatalog(container),
        this.loadUsageLogs(container)
      ]);

    } catch (err) {
      logger.error('StoreUsagePage', 'Error during initialization:', err);
      this.renderErrorMessage(container, 'An error occurred while initializing daily usage logs.');
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
      logger.error('StoreUsagePage', 'Failed to load products catalog:', err);
    }
  }

  async loadUsageLogs(container) {
    try {
      const res = await apiClient.get(`/api/v1/analytics/inventory/daily-usage-history?storeId=${this.storeId}`);
      if (res?.success && res.data) {
        this.usageLogs = res.data;
        this.renderUsageTable(container);
      } else {
        throw new Error(res?.message || 'Empty response.');
      }
    } catch (err) {
      logger.error('StoreUsagePage', 'Failed to load daily usage logs:', err);
      const tbody = container.querySelector('#usage-table-body');
      if (tbody) {
        tbody.innerHTML = `
          <tr>
            <td colspan="7" class="text-danger" style="text-align: center; padding: 1.5rem;">
              Failed to retrieve daily usage history logs from database.
            </td>
          </tr>
        `;
      }
    }
  }

  // ---------------------------------------------------------------------------
  // INTERACTIVE BINDINGS: setupSearchableSelectDropdown
  // ---------------------------------------------------------------------------

  setupSearchableSelectDropdown(container) {
    const wrapper = container.querySelector('#usage-search-select-wrapper');
    const input = container.querySelector('#usage-product-search');
    const list = container.querySelector('#usage-options-list');
    const hiddenInput = container.querySelector('#usage-product-id-val');
    const viewImg = container.querySelector('#usage-product-img-view');
    
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
            
            if (viewImg) viewImg.src = p.imageUrl || 'imgs/coffee_cup_default.png';

            // Show details inputs and submit fields after choosing product
            const fieldsContainer = container.querySelector('#usage-fields-container');
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
      
      if (viewImg) viewImg.src = 'imgs/coffee_cup_default.png';

      // Hide extra fields since product selection is cleared
      const fieldsContainer = container.querySelector('#usage-fields-container');
      if (fieldsContainer) fieldsContainer.style.display = 'none';
    });

    // Hide dropdown when clicking outside
    document.addEventListener('click', (e) => {
      if (!wrapper.contains(e.target)) {
        list.style.display = 'none';
        wrapper.classList.remove('open');
        
        if (!hiddenInput.value) {
          input.value = '';
          this.currentProductImageUrl = '';
          if (viewImg) viewImg.src = 'imgs/coffee_cup_default.png';

          // Hide fields container
          const fieldsContainer = container.querySelector('#usage-fields-container');
          if (fieldsContainer) fieldsContainer.style.display = 'none';
        }
      }
    });
  }

  // ---------------------------------------------------------------------------
  // VIEW RENDERING HELPERS
  // ---------------------------------------------------------------------------

  renderUsageTable(container) {
    const tbody = container.querySelector('#usage-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    // Filter items based on local search bar
    const filtered = this.usageLogs.filter(item => {
      const query = this.searchQuery.toLowerCase().trim();
      return !query || 
             (item.sku && item.sku.toLowerCase().includes(query)) ||
             (item.name && item.name.toLowerCase().includes(query)) ||
             (item.notes && item.notes.toLowerCase().includes(query));
    });

    if (filtered.length === 0) {
      tbody.innerHTML = `
        <tr>
          <td colspan="7" style="text-align: center; color: var(--text-muted, rgba(255,255,255,0.45)); padding: 2rem;">
            No usage logs match the specified search query.
          </td>
        </tr>
      `;
      return;
    }

    // Render entries
    filtered.forEach(item => {
      const tr = document.createElement('tr');
      
      const qty = parseFloat(item.qty) || 0;
      const notesVal = item.notes ? item.notes : '<span style="opacity:0.35;">—</span>';
      
      // Formatting Date nicely (YYYY-MM-DD)
      let displayDate = item.usageDate;
      if (displayDate && displayDate.includes('T')) {
        displayDate = displayDate.split('T')[0];
      }

      tr.innerHTML = `
        <td>
          <img src="${item.imageUrl || 'imgs/coffee_cup_default.png'}" class="usage-thumb" onerror="this.src='imgs/coffee_cup_default.png'">
        </td>
        <td>${displayDate || 'Today'}</td>
        <td style="font-family: monospace; font-weight: bold; color: var(--accent-primary, #c9a46a);">${item.sku || 'N/A'}</td>
        <td>${item.name || 'Unknown Ingredient'}</td>
        <td>${item.category || 'General'}</td>
        <td><strong>${qty.toLocaleString(undefined, { maximumFractionDigits: 4 })}</strong></td>
        <td><div style="max-width: 250px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="${item.notes || ''}">${notesVal}</div></td>
      `;
      tbody.appendChild(tr);
    });
  }

  // ---------------------------------------------------------------------------
  // INTERACTIVE BINDINGS: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container) {
    // 1. Text Search Input
    const searchInput = container.querySelector('#usage-search-input');
    if (searchInput) {
      searchInput.addEventListener('input', (e) => {
        this.searchQuery = e.target.value;
        this.renderUsageTable(container);
      });
    }

    // 2. Log Usage Form Submit
    const form = container.querySelector('#usage-log-form');
    if (form) {
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const hiddenInput = container.querySelector('#usage-product-id-val');
        const qtyInput = container.querySelector('#usage-qty');
        const dateInput = container.querySelector('#usage-date-picker');
        const notesInput = container.querySelector('#usage-notes');
        
        if (!hiddenInput || !qtyInput || !dateInput || !notesInput) return;

        const productId = hiddenInput.value;
        const quantity = qtyInput.value.trim();
        const usageDate = dateInput.value;
        const notes = notesInput.value.trim();

        if (!productId || !quantity || !usageDate) {
          alert('Please make sure product selection, quantity and consumption date are specified.');
          return;
        }

        try {
          const submitBtn = container.querySelector('#btn-submit-usage');
          if (submitBtn) submitBtn.disabled = true;

          const queryUrl = `/api/v1/analytics/inventory/log-daily-usage?storeId=${this.storeId}&productId=${productId}&quantity=${quantity}&usageDate=${usageDate}&notes=${encodeURIComponent(notes)}`;
          
          const res = await apiClient.post(queryUrl);
          if (res?.success) {
            form.reset();
            this.currentProductImageUrl = '';
            
            const viewImg = container.querySelector('#usage-product-img-view');
            if (viewImg) viewImg.src = 'imgs/coffee_cup_default.png';

            const fieldsContainer = container.querySelector('#usage-fields-container');
            if (fieldsContainer) fieldsContainer.style.display = 'none';

            // Reset date input back to today in store timezone
            dateInput.value = this.getTodayInTimezone(this.storeTimezone);
            
            // Reload historical data and refresh materialized views
            await Promise.all([
              this.loadUsageLogs(container),
              apiClient.post('/api/v1/analytics/inventory/refresh').catch(() => {})
            ]);
          } else {
            alert(res?.message || 'Failed to record daily usage logs.');
          }
        } catch (err) {
          logger.error('StoreUsagePage', 'Error recording daily usage:', err);
          alert(err.message || 'An error occurred while recording daily usage logs.');
        } finally {
          const submitBtn = container.querySelector('#btn-submit-usage');
          if (submitBtn) submitBtn.disabled = false;
        }
      });
    }
  }

  getTodayInTimezone(timezone) {
    try {
      const options = { timeZone: timezone, year: 'numeric', month: '2-digit', day: '2-digit' };
      const formatter = new Intl.DateTimeFormat('en-US', options);
      const parts = formatter.formatToParts(new Date());
      const year = parts.find(p => p.type === 'year').value;
      const month = parts.find(p => p.type === 'month').value;
      const day = parts.find(p => p.type === 'day').value;
      return `${year}-${month}-${day}`;
    } catch (e) {
      const today = new Date();
      const yyyy = today.getFullYear();
      const mm = String(today.getMonth() + 1).padStart(2, '0');
      const dd = String(today.getDate()).padStart(2, '0');
      return `${yyyy}-${mm}-${dd}`;
    }
  }

  renderErrorMessage(container, msg) {
    container.innerHTML = `
      <div class="alert alert-danger" style="margin: 2rem; text-align: center;">
        <h4>System Error</h4>
        <p>${msg}</p>
      </div>
    `;
  }

  // ---------------------------------------------------------------------------
  // UTILITY HELPER: _loadCss
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'store-usage-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/usage/usage.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreUsagePage };
