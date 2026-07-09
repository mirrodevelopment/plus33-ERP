/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Core Module
 * File              : storage.js
 * Path              : frontend/core/storage.js
 * Purpose           : Core frontend infrastructure: storage
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : logger
 * Depends On        : logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core frontend infrastructure: storage. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from './logger.js';

export const storage = {
  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  get(key, defaultValue = null) {
    try {
      const item = localStorage.getItem(key);
      if (item === null) return defaultValue;
      return JSON.parse(item);
    } catch (err) {
      logger.error('Storage', `Error reading key "${key}" from localStorage:`, err);
      return defaultValue;
    }
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  set(key, value) {
    try {
      localStorage.setItem(key, JSON.stringify(value));
      return true;
    } catch (err) {
      logger.error('Storage', `Error writing key "${key}" to localStorage:`, err);
      return false;
    }
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  remove(key) {
    try {
      localStorage.removeItem(key);
      return true;
    } catch (err) {
      logger.error('Storage', `Error deleting key "${key}" from localStorage:`, err);
      return false;
    }
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  clear() {
    try {
      localStorage.clear();
      return true;
    } catch (err) {
      logger.error('Storage', 'Error clearing localStorage:', err);
      return false;
    }
  }
};
