/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * File              : authStore.js
 * Path              : frontend/store/authStore.js
 * Purpose           : Frontend state store managing Store Module UI state
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : POST /api/v1/auth/login
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/eventBus, core/storage, core/logger
 * Depends On        : core/eventBus, core/storage, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend state store managing Store Module UI state. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { eventBus } from '../core/eventBus.js';
import { storage } from '../core/storage.js';
import { logger } from '../core/logger.js';

class AuthStore {
  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  constructor() {
    this.user = storage.get('plus33-user');
    this.token = storage.get('plus33-token');
    
    // Purge any non-JWT tokens (mock, dev-local) — force fresh backend login
    if (this.token && !this.token.startsWith('eyJ')) {
      storage.remove('plus33-user');
      storage.remove('plus33-token');
      this.user = null;
      this.token = null;
      logger.info('AuthStore', 'Purged non-JWT session token — will re-authenticate.');
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  async login(username, password, selectedRole = 'ultimateAdmin') {
    logger.info('AuthStore', `Attempting backend login for username: ${username}`);
    
    // Normalize username: append @plus33.com domain suffix if no @ provided
    let realEmail = username.trim();
    let realPassword = password;
    if (!realEmail.includes('@')) {
      realEmail = realEmail + '@plus33.com';
    }

    try {
      const { apiClient } = await import('../api/client.js');
      const response = await apiClient.post('/api/v1/auth/login', { email: realEmail, password: realPassword });

      /**
       * Performs the fn operation in this module.
       * @memberof Store Module
       */
      if (response && response.success && response.data) {
        const tokenResponse = response.data;
        this.token = tokenResponse.accessToken;

        // Decode JWT payload to extract user metadata
        let name = realEmail.split('@')[0];
        let authorities = ['*'];
        try {
          const base64Url = this.token.split('.')[1];
          const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
          const pad = base64.length % 4 ? '='.repeat(4 - base64.length % 4) : '';
          const jsonPayload = decodeURIComponent(
            atob(base64 + pad).split('').map(c =>
              '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
            ).join('')
          );
          const payload = JSON.parse(jsonPayload);
          name        = payload.name || payload.sub || name;
          authorities = payload.authorities || payload.roles || ['*'];
        } catch (e) {
          logger.warn('AuthStore', 'JWT decode failed, using defaults.');
        }

        this.user = {
          username: realEmail,
          name,
          role: selectedRole,
          companyId: 1,
          permissions: authorities
        };

        storage.set('plus33-user', this.user);
        storage.set('plus33-token', this.token);

        logger.info('AuthStore', `Authenticated via backend: ${this.user.name} (${selectedRole})`);
        eventBus.emit('auth:state-changed', { user: this.user, loggedIn: true });
        return true;
      }

      throw new Error(response?.message || 'Empty auth response.');

    } catch (err) {
      logger.error('AuthStore', `Backend login failed: ${err.message}`);

      // Show clear error — no silent fallback to dev sessions
      eventBus.emit('auth:login-failed', { message: err.message });
      return false;
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  logout() {
    logger.info('AuthStore', `User logged out: ${this.user ? this.user.name : 'Unknown'}`);
    this.user  = null;
    this.token = null;
    storage.remove('plus33-user');
    storage.remove('plus33-token');
    eventBus.emit('auth:state-changed', { user: null, loggedIn: false });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  isLoggedIn() {
    return !!this.token && this.token.startsWith('eyJ'); // real JWT only
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  getUser() {
    return this.user;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  getRole() {
    return this.user ? this.user.role : null;
  }
}

export const authStore = new AuthStore();
