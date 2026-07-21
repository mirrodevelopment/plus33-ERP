/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * File              : notificationStore.js
 * Path              : frontend/store/notificationStore.js
 * Purpose           : Frontend state store managing Store Module UI state
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/eventBus, core/logger
 * Depends On        : core/eventBus, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend state store managing Store Module UI state. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { eventBus } from '../core/eventBus.js';
import { logger } from '../core/logger.js';

class NotificationStore {
  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  constructor() {
    this.notifications = [];
  }

  /**
   * Push a new notification toast
   * @param {string} message 
   * @param {'success'|'warning'|'danger'|'info'} type 
   * @param {number} duration milliseconds
   */
  push(message, type = 'info', duration = 4000) {
    const id = `toast-${Date.now()}-${Math.floor(Math.random() * 1000)}`;
    const notification = { id, message, type, duration };
    
    this.notifications.push(notification);
    logger.debug('NotificationStore', `Notification added: "${message}" [${type}]`);
    
    // Broadcast toast event for renderers
    eventBus.emit('notification:toast', notification);
    
    // Automatically delete notification after duration
    setTimeout(() => {
      this.remove(id);
    }, duration);
    
    return id;
  }

  /**
   * Alias for addNotification compatibility
   */
  addNotification(opt) {
    if (typeof opt === 'string') {
      return this.push(opt, 'info');
    }
    const msg = opt?.message || opt?.title || 'Notification';
    const type = opt?.type || 'info';
    return this.push(msg, type);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  success(message, duration) {
    return this.push(message, 'success', duration);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  warning(message, duration) {
    return this.push(message, 'warning', duration);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  danger(message, duration) {
    return this.push(message, 'danger', duration);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  info(message, duration) {
    return this.push(message, 'info', duration);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  remove(id) {
    this.notifications = this.notifications.filter(n => n.id !== id);
    eventBus.emit('notification:removed', id);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  getNotifications() {
    return [...this.notifications];
  }
}

export const notificationStore = new NotificationStore();
