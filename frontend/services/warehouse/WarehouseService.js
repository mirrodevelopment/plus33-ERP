/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : WarehouseService.js
 * Path              : frontend/services/warehouse/WarehouseService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/warehouse-locations, GET /api/v1/cycle-counts
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

class WarehouseService {
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getWarehouseLocations(page = 0, size = 20) {
    return apiClient.get('/api/v1/warehouse-locations', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getCycleCounts(page = 0, size = 20) {
    return apiClient.get('/api/v1/cycle-counts', { page, size });
  }
}

export const warehouseService = new WarehouseService();
export default warehouseService;
