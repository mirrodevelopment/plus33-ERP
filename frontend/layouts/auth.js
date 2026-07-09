/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Layouts Module
 * File              : auth.js
 * Path              : frontend/layouts/auth.js
 * Purpose           : Frontend utility: auth for PLUS33 Coffee ERP
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
 * Frontend utility: auth for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

/**
 * Layout configuration for public/authentication pages.
 */
export const authLayout = {
  render(container) {
    container.innerHTML = `
      <div class="auth-layout">
        <div class="auth-bg-decor"></div>
        <div class="auth-container">
          <div id="main-content" class="auth-box animate-pop-in"></div>
        </div>
      </div>
    `;
  }
};

export default authLayout;
