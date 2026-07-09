/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Layouts Module
 * File              : fullscreen.js
 * Path              : frontend/layouts/fullscreen.js
 * Purpose           : Frontend utility: fullscreen for PLUS33 Coffee ERP
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
 * Frontend utility: fullscreen for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

/**
 * Fullscreen layout framework wrapper.
 */
export const fullscreenLayout = {
  render(container) {
    container.innerHTML = `
      <div class="fullscreen-layout animate-fade-in" style="width: 100vw; height: 100vh; overflow: hidden; background: var(--bg-app);">
        <div id="main-content" class="h-full w-full"></div>
      </div>
    `;
  }
};

export default fullscreenLayout;
