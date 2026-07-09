/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Core Module
 * File              : lifecycle.js
 * Path              : frontend/core/lifecycle.js
 * Purpose           : Core frontend infrastructure: lifecycle
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
 * Core frontend infrastructure: lifecycle. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from './logger.js';

export class Lifecycle {
  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  constructor() {
    this.cleanups = [];
  }

  /**
   * Register a cleanup callback function (e.g. event listener removal)
   * @param {Function} callback 
   */
  onCleanup(callback) {
    if (typeof callback === 'function') {
      this.cleanups.push(callback);
    }
  }

  /**
   * Execute cleanup callbacks and reset collection
   */
  destroy() {
    logger.debug('Lifecycle', `Running ${this.cleanups.length} cleanups.`);
    this.cleanups.forEach(fn => {
      try {
        fn();
      } catch (err) {
        logger.error('Lifecycle', 'Error running cleanup callback:', err);
      }
    });
    this.cleanups = [];
  }
}
