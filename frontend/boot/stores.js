/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Boot Module
 * File              : stores.js
 * Path              : frontend/boot/stores.js
 * Purpose           : Frontend state store managing Boot Module UI state
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : store/authStore, store/themeStore, store/permissionStore, store/userStore, store/notificationStore
 * Depends On        : store/authStore, store/themeStore, store/permissionStore, store/userStore, store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend state store managing Boot Module UI state. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { authStore } from '../store/authStore.js';
import { themeStore } from '../store/themeStore.js';
import { permissionStore } from '../store/permissionStore.js';
import { userStore } from '../store/userStore.js';
import { notificationStore } from '../store/notificationStore.js';
import { logger } from '../core/logger.js';

logger.info('Boot', 'Initializing global state stores...');

export const stores = {
  authStore,
  themeStore,
  permissionStore,
  userStore,
  notificationStore
};
