/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : FinanceService.js
 * Path              : frontend/services/finance/FinanceService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/supplier-invoices, GET /api/v1/journal-entries, GET /api/v1/payments
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

class FinanceService {
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getSupplierInvoices(page = 0, size = 20) {
    return apiClient.get('/api/v1/supplier-invoices', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getJournalEntries(page = 0, size = 20) {
    return apiClient.get('/api/v1/journal-entries', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getPayments(page = 0, size = 20) {
    return apiClient.get('/api/v1/payments', { page, size });
  }
}

export const financeService = new FinanceService();
export default financeService;
