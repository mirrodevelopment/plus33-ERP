/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : PlatformService.js
 * Path              : frontend/services/platform/PlatformService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/platform/flag, GET /api/platform/metrics, POST /api/platform/slo/measurement
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

class PlatformService {
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getFeatureFlag(key, userId) {
    return apiClient.get('/api/platform/flag', { key, userId });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getPlatformMetrics() {
    return apiClient.get('/api/platform/metrics');
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  recordSloMeasurement(name, current, budget, operator) {
    return apiClient.post('/api/platform/slo/measurement', { name, current, budget, operator });
  }
}

export const platformService = new PlatformService();
export default platformService;
