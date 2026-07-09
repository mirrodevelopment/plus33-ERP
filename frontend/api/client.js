/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Api Module
 * File              : client.js
 * Path              : frontend/api/client.js
 * Purpose           : HTTP API client wrapper for backend communication
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger, core/storage, core/eventBus
 * Depends On        : core/logger, core/storage, core/eventBus
 *
 * Description
 * ---------------------------------------------------------------------------
 * HTTP API client wrapper for backend communication. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../core/logger.js';
import { storage } from '../core/storage.js';
import { eventBus } from '../core/eventBus.js';

class ApiClient {
  /**
   * Performs the fn operation in this module.
   * @memberof Api Module
   */
  constructor() {
    this.baseUrl = '/api/v1';
    this.timeout = 15000;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Api Module
   */
  async request(endpoint, options = {}) {
    const url = endpoint.startsWith('/api/') ? endpoint : `${this.baseUrl}${endpoint}`;
    const token = storage.get('plus33-token');
    
    const headers = {
      ...options.headers
    };
    if (!(options.body instanceof FormData) && !headers['Content-Type']) {
      headers['Content-Type'] = 'application/json';
    }
    
    /**
     * Performs the fn operation in this module.
     * @memberof Api Module
     */
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const controller = new AbortController();
    const id = setTimeout(() => controller.abort(), this.timeout);
    
    const fetchOptions = {
      ...options,
      headers,
      signal: controller.signal
    };

    logger.debug('ApiClient', `HTTP Request: ${options.method || 'GET'} -> ${url}`);

    try {
      const response = await fetch(url, fetchOptions);
      clearTimeout(id);
      
      logger.debug('ApiClient', `HTTP Response Status: ${response.status} from ${url}`);
      
      // Handle authentication expiration (401) and security clearance denial (403)
      /**
       * Performs the fn operation in this module.
       * @memberof Api Module
       */
      if (response.status === 401) {
        logger.warn('ApiClient', 'Session credentials expired (401). Intercepting for redirect.');
        eventBus.emit('auth:session-expired');
        throw new Error('Session expired. Please re-authenticate.');
      }

      /**
       * Performs the fn operation in this module.
       * @memberof Api Module
       */
      if (response.status === 403) {
        logger.warn('ApiClient', 'Clearance access denied (403).');
        throw new Error('Access denied. You do not possess clearance for this operation.');
      }

      let json;
      try {
        json = await response.json();
      } catch (parseErr) {
        logger.error('ApiClient', 'Response parse failed: HTML/text returned instead of JSON', parseErr);
        throw new Error('Invalid response from authentication server. Please verify the Spring Boot backend is running on port 8080.');
      }
      
      // Auto-correct encoding anomalies from JVM/DB seed data
      /**
       * Performs the sanitizeData operation in this module.
       * @memberof Api Module
       */
      const sanitizeData = (obj) => {
        if (obj === null || obj === undefined) return obj;
        /**
         * Performs the fn operation in this module.
         * @memberof Api Module
         */
        if (typeof obj === 'string') {
          return obj
            .replace(/\?le-de-France/gi, 'Île-de-France')
            .replace(/CafǸ/gi, 'Café')
            .replace(/Caf\u01e7/gi, 'Café');
        }
        if (Array.isArray(obj)) {
          return obj.map(sanitizeData);
        }
        /**
         * Performs the fn operation in this module.
         * @memberof Api Module
         */
        if (typeof obj === 'object') {
          const res = {};
          for (const [k, v] of Object.entries(obj)) {
            res[k] = sanitizeData(v);
          }
          return res;
        }
        return obj;
      };
      
      json = sanitizeData(json);
      
      // Handle standard Spring Boot validation argument anomalies and business exceptions
      /**
       * Performs the fn operation in this module.
       * @memberof Api Module
       */
      if (!response.ok) {
        const errorMsg = json.message || response.statusText;
        logger.error('ApiClient', `REST operation failed: ${errorMsg}`, json);
        
        const error = new Error(errorMsg);
        error.status = response.status;
        error.fieldErrors = json.fieldErrors || null; // standard validation message maps
        error.error = json.error || 'Server Error';
        throw error;
      }
      
      return json; // Returns full ApiResponse envelope: { success, message, data, metadata, timestamp }
    } catch (err) {
      clearTimeout(id);
      /**
       * Performs the fn operation in this module.
       * @memberof Api Module
       */
      if (err.name === 'AbortError') {
        logger.error('ApiClient', `Request timed out after ${this.timeout}ms on ${url}`);
        throw new Error('Connection timed out. Check network status.');
      }
      if (err.message && (err.message.includes('Failed to fetch') || err.message.includes('network') || err.name === 'TypeError')) {
        throw new Error('Failed to connect to the backend server. Please verify that the Spring Boot backend is running on port 8080.');
      }
      throw err;
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Api Module
   */
  get(endpoint, queryParams = null) {
    let url = endpoint;
    /**
     * Performs the fn operation in this module.
     * @memberof Api Module
     */
    if (queryParams) {
      const parts = Object.entries(queryParams)
        .filter(([_, v]) => v !== null && v !== undefined)
        .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`);
      /**
       * Performs the fn operation in this module.
       * @memberof Api Module
       */
      if (parts.length > 0) {
        url += `?${parts.join('&')}`;
      }
    }
    return this.request(url, { method: 'GET' });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Api Module
   */
  post(endpoint, body) {
    return this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(body)
    });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Api Module
   */
  put(endpoint, body) {
    return this.request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(body)
    });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Api Module
   */
  delete(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  }
}

export const apiClient = new ApiClient();
export default apiClient;
