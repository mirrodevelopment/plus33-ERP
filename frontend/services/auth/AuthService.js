/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : AuthService.js
 * Path              : frontend/services/auth/AuthService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : POST /api/v1/auth/login
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client, core/storage, core/eventBus, core/logger
 * Depends On        : api/client, core/storage, core/eventBus, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend service wrapping backend REST APIs for Services Module. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../api/client.js';
import { storage } from '../../core/storage.js';
import { eventBus } from '../../core/eventBus.js';
import { logger } from '../../core/logger.js';

class AuthService {
  /**
   * Log in user using credentials
   * @param {string} email 
   * @param {string} password 
   * @returns {Promise<Object>} TokenResponse
   */
  async login(email, password) {
    logger.info('AuthService', `Logging in operator: ${email}`);
    const loginRequest = { email, password };
    
    try {
      const response = await apiClient.post('/api/v1/auth/login', loginRequest);
      /**
       * Performs the fn operation in this module.
       * @memberof Services Module
       */
      if (response.success && response.data) {
        const tokenResponse = response.data; // { accessToken, tokenType, expiresIn }
        storage.set('plus33-token', tokenResponse.accessToken);
        
        // Extract permissions and roles from JWT token (mock decode for simplicity in frontend)
        const payload = this.decodeJwt(tokenResponse.accessToken);
        const userProfile = {
          email: payload.sub,
          name: payload.name || email.split('@')[0],
          role: payload.role || 'ultimateAdmin',
          permissions: payload.authorities || ['*']
        };
        
        storage.set('plus33-user', userProfile);
        logger.info('AuthService', `Login successful. User role: ${userProfile.role}`);
        eventBus.emit('auth:state-changed', { user: userProfile, loggedIn: true });
        
        return userProfile;
      } else {
        throw new Error(response.message || 'Authentication clearance failed.');
      }
    } catch (err) {
      logger.error('AuthService', 'Login authentication fault occurred:', err);
      throw err;
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  logout() {
    logger.info('AuthService', 'Logging out active session.');
    storage.remove('plus33-token');
    storage.remove('plus33-user');
    eventBus.emit('auth:state-changed', { user: null, loggedIn: false });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  decodeJwt(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (e) {
      // JWT is malformed or invalid — return null so callers force logout
      return null;
    }
  }
}

export const authService = new AuthService();
export default authService;
