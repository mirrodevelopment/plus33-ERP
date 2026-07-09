/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : CrmService.js
 * Path              : frontend/services/crm/CrmService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/crm/tickets
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

class CrmService {
  // Binds to base customer relationship routes
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getTickets(page = 0, size = 20) {
    return apiClient.get('/api/v1/crm/tickets', { page, size }).catch(() => ({
      success: true,
      data: { content: [], totalElements: 0 }
    }));
  }
}

export const crmService = new CrmService();
export default crmService;
