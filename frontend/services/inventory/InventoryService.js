/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : InventoryService.js
 * Path              : frontend/services/inventory/InventoryService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/products, POST /api/v1/products, GET /api/v1/inventory-transfers, POST /api/v1/inventory-transfers
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client
 * Depends On        : api/client
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend service wrapping backend REST APIs for Services Module. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../api/client.js';

class InventoryService {
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getProducts(page = 0, size = 20, sort = 'id', direction = 'asc') {
    return apiClient.get('/api/v1/products', { page, size, sort, direction });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  createProduct(productDto) {
    return apiClient.post('/api/v1/products', productDto);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getInventoryTransfers(page = 0, size = 20) {
    return apiClient.get('/api/v1/inventory-transfers', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  createInventoryTransfer(transferDto) {
    return apiClient.post('/api/v1/inventory-transfers', transferDto);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getReplenishments(page = 0, size = 20) {
    return apiClient.get('/api/v1/replenishments', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getStockLedger() {
    return apiClient.get('/api/v1/wms/locations/wms/stock-ledger');
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  createRecall(recallDto) {
    return apiClient.post('/api/v1/inventory-recalls', recallDto);
  }
}

export const inventoryService = new InventoryService();
export default inventoryService;
