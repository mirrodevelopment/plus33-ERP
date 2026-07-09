/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Boot Module
 * File              : router.js
 * Path              : frontend/boot/router.js
 * Purpose           : Client-side hash-based router defining application navigation
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : routing/router, core/logger
 * Depends On        : routing/router, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Client-side hash-based router defining application navigation. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { router } from '../routing/router.js';
import { logger } from '../core/logger.js';

logger.info('Boot', 'Loading router configuration...');

// Trigger initial hash evaluation
router.handleRouting();
