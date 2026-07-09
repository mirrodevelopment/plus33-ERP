/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : App Module
 * File              : main.js
 * Path              : frontend/app/main.js
 * Purpose           : Core frontend infrastructure: main
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : boot/app
 * Depends On        : boot/app
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core frontend infrastructure: main. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { initializeApplication } from '../boot/app.js';
import '../ai/chat/assistant.js'; // AI Copilot Float Widget Launcher

// Boot application
initializeApplication().catch(err => {
  console.error('Fatal initialization error:', err);
});
