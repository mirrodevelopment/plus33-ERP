/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Boot Module
 * File              : app.js
 * Path              : frontend/boot/app.js
 * Purpose           : Core frontend infrastructure: app
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger, config/config
 * Depends On        : core/logger, config/config
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core frontend infrastructure: app. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../core/logger.js';
import { config } from '../config/config.js';

// Order is crucial: load stores -> recover auth -> apply theme -> bind event checks -> launch router
/**
 * Initializes the initialize application configuration and prepares default state.
 * @memberof Boot Module
 */
export async function initializeApplication() {
  logger.info('System', `--- BOOTSTRAP COMMENCING (${config.branding.appName} v${config.version}) ---`);
  
  try {
    // 1. Initialize Stores
    await import('./stores.js');
    
    // 2. Initialize Auth Check
    await import('./auth.js');
    
    // 3. Initialize Theme
    await import('./theme.js');
    
    // 4. Register Event Listeners
    await import('./events.js');
    
    // 5. Start Routing
    await import('./router.js');
    
    // Hide global loading screen
    const loader = document.getElementById('global-loader');
    /**
     * Performs the fn operation in this module.
     * @memberof Boot Module
     */
    if (loader) {
      loader.classList.add('hidden');
    }
    
    logger.info('System', '--- BOOTSTRAP COMPLETED SUCCESSFULLY ---');
  } catch (error) {
    // Hide global loading screen on error to allow 500 error display
    const loader = document.getElementById('global-loader');
    /**
     * Performs the fn operation in this module.
     * @memberof Boot Module
     */
    if (loader) {
      loader.classList.add('hidden');
    }
    logger.error('System', 'Fatal bootstrap sequence breakdown.', error);
    throw error;
  }
}
