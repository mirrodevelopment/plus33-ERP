/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : ProcurementService.js
 * Path              : frontend/services/procurement/ProcurementService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/purchase-requests, GET /api/v1/purchase-orders, GET /api/v1/goods-receipts, GET /api/v1/suppliers
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

class ProcurementService {
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getPurchaseRequests(page = 0, size = 20) {
    return apiClient.get('/api/v1/purchase-requests', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getPurchaseOrders(page = 0, size = 20) {
    return apiClient.get('/api/v1/purchase-orders', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getGoodsReceipts(page = 0, size = 20) {
    return apiClient.get('/api/v1/goods-receipts', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getSuppliers(page = 0, size = 20) {
    return apiClient.get('/api/v1/suppliers', { page, size });
  }
}

export const procurementService = new ProcurementService();
export default procurementService;
