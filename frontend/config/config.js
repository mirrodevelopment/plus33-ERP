/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Config Module
 * File              : config.js
 * Path              : frontend/config/config.js
 * Purpose           : Frontend utility: config for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : 
 * Depends On        : 
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: config for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

/**
 * Main application config registry.
 */
export const config = {
  environment: 'development',
  version: '1.0.0',
  build: '0002',
  api: {
    baseUrl: '/api/v1',
    timeout: 15000,
    retryCount: 3
  },
  branding: {
    appName: 'PLUS33 Franchise ERP',
    logoText: 'PLUS33',
    themeDefault: 'coffee-dark',
    supportEmail: 'support@plus33.coffee'
  },
  featureFlags: {
    aiAssistant: true,
    digitalTwins: true,
    fleetSimulation: true,
    esgReporting: true,
    dynamicWmsRouting: true
  },
  security: {
    sessionTimeoutMs: 1800000, // 30 minutes
    maxLoginAttempts: 5
  }
};
