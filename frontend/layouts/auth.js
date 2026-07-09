/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Layouts Module
 * File              : auth.js
 * Path              : frontend/layouts/auth.js
 * Purpose           : Auth layout controller — loads HTML template
 * Version           : 2.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in layouts/html/auth-layout.html
 ******************************************************************************/

import { htmlLoader } from '../core/htmlLoader.js';

const LAYOUT_TEMPLATE_URL = 'layouts/html/auth-layout.html';

export const authLayout = {
  /**
   * Render the auth layout shell.
   * @param {HTMLElement} container - The #app root element
   */
  async render(container) {
    await htmlLoader.inject(LAYOUT_TEMPLATE_URL, container);
  }
};

export default authLayout;
