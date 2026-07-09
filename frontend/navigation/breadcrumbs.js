/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Navigation Module
 * File              : breadcrumbs.js
 * Path              : frontend/navigation/breadcrumbs.js
 * Purpose           : Frontend utility: breadcrumbs for PLUS33 Coffee ERP
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
 * Frontend utility: breadcrumbs for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

/**
 * Computes breadcrumb arrays dynamically depending on the current hash route.
 * @param {string} hash 
 * @returns {Array<{name: string, route?: string}>}
 */
export function getBreadcrumbs(hash) {
  const crumbs = [{ name: 'Home', route: '#dashboard' }];
  
  /**
   * Performs the fn operation in this module.
   * @memberof Navigation Module
   */
  if (hash === '#dashboard' || !hash) {
    crumbs.push({ name: 'Enterprise Overview' });
  } else if (hash === '#inventory') {
    crumbs.push({ name: 'Supply Chain & WMS', route: '#inventory' });
    crumbs.push({ name: 'Stock Ledger' });
  } else if (hash === '#warehouse') {
    crumbs.push({ name: 'Supply Chain & WMS', route: '#inventory' });
    crumbs.push({ name: 'Physical Topology' });
  } else if (hash === '#docs') {
    crumbs.push({ name: 'Platform Docs', route: '#docs' });
    crumbs.push({ name: 'Architecture Standards' });
  } else if (hash === '#regions') {
    crumbs.push({ name: 'Administration', route: '#regions' });
    crumbs.push({ name: 'Regions Management' });
  } else if (hash) {
    // Dynamic formatting fallback for other registered routes
    const cleanName = hash.replace('#', '')
                          .split('-')
                          .map(w => w.charAt(0).toUpperCase() + w.slice(1))
                          .join(' ');
    crumbs.push({ name: cleanName });
  } else {
    crumbs.push({ name: 'Error' });
  }

  return crumbs;
}
