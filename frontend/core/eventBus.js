/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Core Module
 * File              : eventBus.js
 * Path              : frontend/core/eventBus.js
 * Purpose           : Core frontend infrastructure: eventBus
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
 * Core frontend infrastructure: eventBus. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

class EventBus {
  constructor() {
    this.listeners = {};
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  on(event, callback) {
    /**
     * Performs the fn operation in this module.
     * @memberof Core Module
     */
    if (!this.listeners[event]) {
      this.listeners[event] = [];
    }
    this.listeners[event].push(callback);
    return () => this.off(event, callback);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  off(event, callback) {
    if (!this.listeners[event]) return;
    this.listeners[event] = this.listeners[event].filter(cb => cb !== callback);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Core Module
   */
  emit(event, data = null) {
    if (!this.listeners[event]) return;
    this.listeners[event].forEach(callback => {
      try {
        callback(data);
      } catch (err) {
        console.error(`Error in event listener for "${event}":`, err);
      }
    });
  }
}

export const eventBus = new EventBus();
