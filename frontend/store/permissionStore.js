/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * File              : permissionStore.js
 * Path              : frontend/store/permissionStore.js
 * Purpose           : RBAC permission evaluator component; checks current user session permissions and roles from authStore to authorize navigation routes and UI controls.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend RBAC access evaluation component.
 * Features:
 *   - Evaluates permission strings via hasPermission(permission) against active user permissions list.
 *   - Grants automatic wildcard clearance for ultimateAdmin role or '*' authority.
 *   - Used by client-side router (router.js) and navigation menus (menus.js) to enforce route protection.
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
