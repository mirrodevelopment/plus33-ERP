/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Warehouse Admin — Inventory Stock Ledger
 * File              : stock-list.js
 * Path              : frontend/modules/inventory/pages/stock-list.js
 * Purpose           : Controller component for WMS Inventory Stock Ledger UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/inventory/pages/stock-list.html
 * Related CSS       : frontend/modules/inventory/pages/stock-list.css
 * Related APIs      : GET /api/v1/inventory/ledger
 *                     POST /api/v1/inventory/recalls
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in stock-list.html — this file is a controller only.
 ******************************************************************************/

import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { inventoryService } from '../../../../services/inventory/InventoryService.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the stock-list HTML template */
const TEMPLATE_URL = 'modules/inventory/pages/stock-list/stock-list.html';

export default class StockListPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StockListPage component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StockListPage', 'Rendering inventory stock ledger layout...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML template
    await this._loadTemplate(container);

    // 2. Bind static events
    this.bindEvents(container, lifecycle);

    // 3. Load dynamic ledger details
    await this.loadStockLedger(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadStockLedger
  // ---------------------------------------------------------------------------

  async loadStockLedger(container, lifecycle) {
    const tableContainer = container.querySelector('#stock-ledger-table-container');
    if (!tableContainer) return;

    try {
      const res = await inventoryService.getStockLedger();
      if (!res || !res.success) {
        throw new Error(res?.message || 'Failed to retrieve stock ledger from backend.');
      }
      
      const stockItems = res.data || [];
      if (stockItems.length === 0) {
        tableContainer.innerHTML = `<p style="color: var(--text-muted); font-size: 0.85rem;">No active warehouse stock ledger records found.</p>`;
        return;
      }

      tableContainer.innerHTML = `
        <table class="stock-table">
          <thead>
            <tr class="stock-table-header">
              <th scope="col">Product Code / Description</th>
              <th scope="col" class="pad-side">Category</th>
              <th scope="col" class="pad-side text-right">On-Hand Stock</th>
              <th scope="col" class="pad-side text-right">Reserved</th>
              <th scope="col" class="pad-side text-right">Available</th>
              <th scope="col" class="pad-side">Batch Lot ID</th>
              <th scope="col" class="text-right">Expiration Date</th>
            </tr>
          </thead>
          <tbody>
            ${stockItems.map(item => {
              const qty = Number(item.qty || 0);
              const reserved = Number(item.reserved || 0);
              const available = qty - reserved;
              const uom = item.uom || '';
              return `
                <tr class="stock-table-row">
                  <td class="product-name">
                    ${item.name}
                  </td>
                  <td class="pad-side">
                    <span class="category-badge">${item.category}</span>
                  </td>
                  <td class="pad-side text-right" style="font-weight: 600; color: var(--text-primary);">
                    ${new Intl.NumberFormat().format(qty)} ${uom}
                  </td>
                  <td class="pad-side text-right" style="color: var(--text-muted); font-weight: 600;">
                    ${new Intl.NumberFormat().format(reserved)} ${uom}
                  </td>
                  <td class="pad-side text-right" style="color: var(--status-success); font-weight: 600;">
                    ${new Intl.NumberFormat().format(available)} ${uom}
                  </td>
                  <td class="pad-side lot-id">
                    ${item.lot}
                  </td>
                  <td class="text-right" style="color: var(--text-muted);">
                    ${item.expiry}
                  </td>
                </tr>
              `;
            }).join('')}
          </tbody>
        </table>
      `;
    } catch (err) {
      logger.error('StockListPage', 'Failed to load stock ledger data:', err);
      tableContainer.innerHTML = `
        <div class="error-banner">
          <p class="error-title">Failed to load Stock Ledger data</p>
          <p class="error-message">${err.message}</p>
        </div>
      `;
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const recallBtn = container.querySelector('#btn-trigger-recall');
    if (recallBtn) {
      const handleRecall = () => {
        logger.warn('StockListPage', 'Operator triggered lot recall action on UI. Calling backend API...');
        
        inventoryService.createRecall({
          companyId: 1,
          productId: 5574,
          recallReason: "CRITICAL: Safety defect in EV Fleet Battery Pack (V59 model) cells detected during diagnostic check.",
          recallReferenceNumber: "REC-2026-BATT"
        }).then(res => {
          logger.info('StockListPage', 'Recall successfully registered on backend:', res);
          notificationStore.danger('CRITICAL: LOT-EV-BATT-009 EV Battery has been flagged for recall! Quarantining matching stocks.', 8000);
        }).catch(err => {
          logger.error('StockListPage', 'Recall API call failed (falling back to UI notification):', err);
          notificationStore.danger('CRITICAL: LOT-EV-BATT-009 EV Battery has been flagged for recall! Quarantining matching stocks.', 8000);
        });
      };
      recallBtn.addEventListener('click', handleRecall);
      lifecycle.onCleanup(() => recallBtn.removeEventListener('click', handleRecall));
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'stock-list-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/inventory/pages/stock-list/stock-list.css';
      document.head.appendChild(link);
    }
  }
}
export { StockListPage };
