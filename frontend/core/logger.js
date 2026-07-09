/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Core Module
 * File              : logger.js
 * Path              : frontend/core/logger.js
 * Purpose           : Core frontend infrastructure: logger
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : 
 * Depends On        : 
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core frontend infrastructure: logger. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

export const logger = {
  debug(context, message, data = null) {
    this._log('DEBUG', context, message, data);
  },
  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  info(context, message, data = null) {
    this._log('INFO', context, message, data);
  },
  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  warn(context, message, data = null) {
    this._log('WARN', context, message, data);
  },
  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  error(context, message, data = null) {
    this._log('ERROR', context, message, data);
  },
  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  _log(level, context, message, data) {
    const timestamp = new Date().toISOString();
    const formatted = `[${timestamp}] [${level}] [${context}] ${message}`;
    /**
     * Performs the fn operation in this module.
     * @memberof Core Module
     */
    if (level === 'ERROR') {
      console.error(formatted, data || '');
    } else if (level === 'WARN') {
      console.warn(formatted, data || '');
    } else if (level === 'DEBUG') {
      console.debug(formatted, data || '');
    } else {
      console.log(formatted, data || '');
    }
  }
};
