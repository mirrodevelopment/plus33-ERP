/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Boot Module
 * File              : events.js
 * Path              : frontend/boot/events.js
 * Purpose           : Core frontend infrastructure: events
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger, store/notificationStore
 * Depends On        : core/logger, store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core frontend infrastructure: events. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../core/logger.js';
import { notificationStore } from '../store/notificationStore.js';

logger.info('Boot', 'Registering system-level safety event hooks...');

// Handle script errors
window.addEventListener('error', (event) => {
  logger.error('SystemError', `Script error detected: ${event.message}`, {
    filename: event.filename,
    lineno: event.lineno,
    colno: event.colno,
    error: event.error
  });
  
  notificationStore.danger('A platform execution fault occurred. See console logs.');
});

// Handle unhandled rejections
window.addEventListener('unhandledrejection', (event) => {
  logger.error('PromiseRejection', `Unhandled Promise rejection: ${event.reason?.message || event.reason}`, {
    reason: event.reason
  });
  
  notificationStore.danger('Async operation failed. Telemetry sent to diagnostic logs.');
});
