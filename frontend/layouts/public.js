/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Layouts Module
 * File              : public.js
 * Path              : frontend/layouts/public.js
 * Purpose           : Frontend utility: public for PLUS33 Coffee ERP
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
 * Frontend utility: public for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

/**
 * Basic public wrapper layout.
 */
export const publicLayout = {
  render(container) {
    container.innerHTML = `
      <div class="public-layout animate-fade-in">
        <div id="main-content"></div>
      </div>
    `;
  }
};

export default publicLayout;
