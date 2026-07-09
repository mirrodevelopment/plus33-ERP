/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : DashboardService.js
 * Path              : frontend/services/dashboard/DashboardService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/dashboard/overview
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client, core/eventBus, core/logger, core/storage
 * Depends On        : api/client, core/eventBus, core/logger, core/storage
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend service wrapping backend REST APIs for Services Module. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../api/client.js';
import { eventBus } from '../../core/eventBus.js';
import { logger } from '../../core/logger.js';
import { storage } from '../../core/storage.js';

class DashboardService {
  /**
   * Fetches the unified dashboard overview payload from the backend.
   * Retries up to maxRetries times with exponential backoff.
   * All data must come from the database — no fallback mock data.
   */
  async getDashboardOverview(filters = {}, maxRetries = 3) {
    eventBus.emit('dashboard:loading');
    logger.info('DashboardService', 'Fetching backend dashboard overview...', filters);

    // Verify we have a real JWT before attempting
    const token = storage.get('plus33-token');
    if (!token || !token.startsWith('eyJ')) {
      logger.warn('DashboardService', 'No valid JWT found — cannot load dashboard.');
      eventBus.emit('dashboard:error', { message: 'Not authenticated. Please log in.' });
      return null;
    }

    let lastError;
    /**
     * Performs the attempt operation in this module.
     * @memberof Services Module
     */
    for (let attempt = 1; attempt <= maxRetries; attempt++) {
      try {
        const response = await apiClient.get('/api/v1/dashboard/overview', filters);
        /**
         * Performs the fn operation in this module.
         * @memberof Services Module
         */
        if (response && response.success && response.data) {
          logger.info('DashboardService', `Backend data loaded (attempt ${attempt}).`);
          eventBus.emit('dashboard:loaded', response.data);
          return response.data;
        }
        throw new Error(response?.message || 'Empty overview response from server.');
      } catch (err) {
        lastError = err;
        logger.warn('DashboardService', `Attempt ${attempt}/${maxRetries} failed: ${err.message}`);
        /**
         * Performs the fn operation in this module.
         * @memberof Services Module
         */
        if (attempt < maxRetries) {
          const delay = 500 * Math.pow(2, attempt - 1);
          await new Promise(resolve => setTimeout(resolve, delay));
        }
      }
    }

    logger.error('DashboardService', 'Backend unreachable after retries — dashboard unavailable.', lastError);
    eventBus.emit('dashboard:error', { message: 'Could not connect to server. Please try again later.' });
    return null;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  async refresh(filters = {}) {
    return this.getDashboardOverview(filters);
  }

}

export const dashboardService = new DashboardService();
export default dashboardService;
