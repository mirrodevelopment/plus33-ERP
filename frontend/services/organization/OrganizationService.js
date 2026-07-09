/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : OrganizationService.js
 * Path              : frontend/services/organization/OrganizationService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/companies, GET /api/v1/regions, GET /api/v1/warehouses, GET /api/v1/stores
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

class OrganizationService {
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getCompanies(page = 0, size = 20) {
    return apiClient.get('/api/v1/companies', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getRegions(page = 0, size = 20) {
    return apiClient.get('/api/v1/regions', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getWarehouses(page = 0, size = 20) {
    return apiClient.get('/api/v1/warehouses', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getStores(page = 0, size = 20) {
    return apiClient.get('/api/v1/stores', { page, size });
  }
}

export const organizationService = new OrganizationService();
export default organizationService;
