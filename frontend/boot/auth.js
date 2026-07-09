/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Boot Module
 * File              : auth.js
 * Path              : frontend/boot/auth.js
 * Purpose           : Core frontend infrastructure: auth
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : store/authStore, core/logger
 * Depends On        : store/authStore, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core frontend infrastructure: auth. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { authStore } from '../store/authStore.js';
import { logger } from '../core/logger.js';

logger.info('Boot', 'Evaluating credentials authentication status...');

if (authStore.isLoggedIn()) {
  const user = authStore.getUser();
  logger.info('Boot', `Session recovered successfully. Welcome back, ${user.name} (${user.role}).`);
} else {
  logger.info('Boot', 'No active session token recovered. Routing to credentials login.');
}
