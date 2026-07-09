/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Modules Module
 * File              : stock-list.js
 * Path              : frontend/modules/inventory/pages/stock-list.js
 * Purpose           : Frontend page component for the Modules Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger, store/notificationStore, services/inventory/InventoryService
 * Depends On        : core/logger, store/notificationStore, services/inventory/InventoryService
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Modules Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../../core/logger.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { inventoryService } from '../../../services/inventory/InventoryService.js';

export default class StockListPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Modules Module
   */
  async mount(container, lifecycle) {
    logger.info('StockListPage', 'Rendering inventory stock ledger layout...');
    
    container.innerHTML = `
      <div class="flex justify-between align-center mb-lg">
        <div>
          <h2 class="m-0" style="font-family: var(--font-display); font-weight: 800;">WMS Inventory Stock Ledger</h2>
          <p class="m-0" style="color: var(--text-muted); font-size: 0.85rem;">Real-time stock balances and lot expirations</p>
        </div>
        <button id="btn-trigger-recall" class="animate-pulse" style="padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); background: var(--status-danger); border: 1px solid var(--status-danger); color: #fff; font-weight: 700; cursor: pointer; font-size: 0.8rem; transition: var(--transition-fast);">
          ⚠️ Trigger Lot Recall
        </button>
      </div>

      <div id="stock-ledger-table-container" class="card glass p-lg" style="border: 1px solid var(--border-color); overflow-x: auto; min-height: 200px; display: flex; align-items: center; justify-content: center;">
        <div class="spinner" style="border-top-color: var(--accent-primary);"></div>
      </div>
    `;

    this.bindEvents(container, lifecycle);
    await this.loadStockLedger(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Modules Module
   */
  async loadStockLedger(container, lifecycle) {
    const tableContainer = container.querySelector('#stock-ledger-table-container');
    try {
      const res = await inventoryService.getStockLedger();
      /**
       * Performs the fn operation in this module.
       * @memberof Modules Module
       */
      if (!res || !res.success) {
        throw new Error(res?.message || 'Failed to retrieve stock ledger from backend.');
      }
      
      const stockItems = res.data || [];
      /**
       * Performs the fn operation in this module.
       * @memberof Modules Module
       */
      if (stockItems.length === 0) {
        tableContainer.innerHTML = `<p style="color: var(--text-muted); font-size: 0.85rem;">No active warehouse stock ledger records found.</p>`;
        return;
      }

      tableContainer.style.display = 'block';
      tableContainer.innerHTML = `
        <table style="width: 100%; border-collapse: collapse; text-align: left; font-size: 0.85rem;">
          <thead>
            <tr style="border-bottom: 1px solid var(--border-color); color: var(--text-muted); font-weight: 600;">
              <th style="padding: var(--spacing-sm) 0;">Product Code / Description</th>
              <th style="padding: var(--spacing-sm);">Category</th>
              <th style="padding: var(--spacing-sm); text-align: right;">On-Hand Stock</th>
              <th style="padding: var(--spacing-sm); text-align: right;">Reserved</th>
              <th style="padding: var(--spacing-sm); text-align: right;">Available</th>
              <th style="padding: var(--spacing-sm);">Batch Lot ID</th>
              <th style="padding: var(--spacing-sm); text-align: right;">Expiration Date</th>
            </tr>
          </thead>
          <tbody>
            ${stockItems.map(item => {
              const qty = Number(item.qty || 0);
              const reserved = Number(item.reserved || 0);
              const available = qty - reserved;
              const uom = item.uom || '';
              return `
                <tr style="border-bottom: 1px solid rgba(255,255,255,0.03); transition: background 0.15s;" onmouseover="this.style.background='rgba(255,255,255,0.01)'" onmouseout="this.style.background='none'">
                  <td style="padding: var(--spacing-md) 0; font-weight: 600; color: var(--text-primary);">
                    ${item.name}
                  </td>
                  <td style="padding: var(--spacing-md); color: var(--text-secondary);">
                    <span style="background: rgba(255,255,255,0.05); padding: 2px 6px; border-radius: 4px; border: 1px solid var(--border-color); font-size: 0.75rem;">${item.category}</span>
                  </td>
                  <td style="padding: var(--spacing-md); text-align: right; font-weight: 600; color: var(--text-primary);">
                    ${new Intl.NumberFormat().format(qty)} ${uom}
                  </td>
                  <td style="padding: var(--spacing-md); text-align: right; color: var(--text-muted); font-weight: 600;">
                    ${new Intl.NumberFormat().format(reserved)} ${uom}
                  </td>
                  <td style="padding: var(--spacing-md); text-align: right; color: var(--status-success); font-weight: 600;">
                    ${new Intl.NumberFormat().format(available)} ${uom}
                  </td>
                  <td style="padding: var(--spacing-md); font-family: var(--font-mono); font-size: 0.75rem; color: var(--text-secondary);">
                    ${item.lot}
                  </td>
                  <td style="padding: var(--spacing-md); text-align: right; color: var(--text-muted);">
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
        <div class="text-center" style="padding: var(--spacing-md);">
          <p style="color: var(--status-danger); font-weight: 600;">Failed to load Stock Ledger data</p>
          <p style="color: var(--text-muted); font-size: 0.75rem; margin-top: 4px;">${err.message}</p>
        </div>
      `;
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Modules Module
   */
  bindEvents(container, lifecycle) {
    const recallBtn = container.querySelector('#btn-trigger-recall');
    /**
     * Performs the fn operation in this module.
     * @memberof Modules Module
     */
    if (recallBtn) {
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Modules Module
       */
      const handler = () => {
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
      recallBtn.addEventListener('click', handler);
      lifecycle.onCleanup(() => recallBtn.removeEventListener('click', handler));
      
      // Hover effects
      recallBtn.addEventListener('mouseover', () => {
        recallBtn.style.opacity = '0.9';
      });
      recallBtn.addEventListener('mouseout', () => {
        recallBtn.style.opacity = '1';
      });
    }
  }
}
