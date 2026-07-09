/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * File              : permissionStore.js
 * Path              : frontend/store/permissionStore.js
 * Purpose           : Frontend state store managing Store Module UI state
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : authStore, core/logger
 * Depends On        : authStore, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend state store managing Store Module UI state. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { authStore } from './authStore.js';
import { logger } from '../core/logger.js';

class PermissionStore {
  /**
   * Check if the active session possesses the requested permission tag.
   * @param {string} permission 
   * @returns {boolean}
   */
  hasPermission(permission) {
    const user = authStore.getUser();
    if (!user) {
      logger.debug('PermissionStore', `Permission check for "${permission}" denied (No session user)`);
      return false;
    }

    // Admin override wildcard
    if (user.permissions.includes('*') || user.role === 'ultimateAdmin') {
      return true;
    }

    const hasIt = user.permissions.includes(permission);
    logger.debug('PermissionStore', `Permission check for "${permission}" resolved: ${hasIt}`);
    return hasIt;
  }
}

export const permissionStore = new PermissionStore();
