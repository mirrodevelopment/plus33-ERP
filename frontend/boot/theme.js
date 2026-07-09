/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Boot Module
 * File              : theme.js
 * Path              : frontend/boot/theme.js
 * Purpose           : Core frontend infrastructure: theme
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : store/themeStore, core/logger
 * Depends On        : store/themeStore, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core frontend infrastructure: theme. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { themeStore } from '../store/themeStore.js';
import { logger } from '../core/logger.js';

logger.info('Boot', 'Loading active layout skin theme...');

const activeTheme = themeStore.getTheme();
document.documentElement.setAttribute('data-theme', activeTheme);
logger.info('Boot', `Active skin applied: ${activeTheme}`);
