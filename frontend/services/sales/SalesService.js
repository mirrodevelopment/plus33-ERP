/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : SalesService.js
 * Path              : frontend/services/sales/SalesService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/sales-orders, GET /api/v1/customer-invoices, GET /api/v1/customers, GET /api/v1/customer-returns
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

class SalesService {
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getSalesOrders(page = 0, size = 20) {
    return apiClient.get('/api/v1/sales-orders', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getCustomerInvoices(page = 0, size = 20) {
    return apiClient.get('/api/v1/customer-invoices', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getCustomers(page = 0, size = 20) {
    return apiClient.get('/api/v1/customers', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getCustomerReturns(page = 0, size = 20) {
    return apiClient.get('/api/v1/customer-returns', { page, size });
  }
}

export const salesService = new SalesService();
export default salesService;
