/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : RegionalAdmin — Employees
 * File              : employees.js
 * Purpose           : Controller component for Employee Overview page
 * Version           : 1.0.0
 *
 * Related HTML      : frontend/modules/regional-admin/pages/employees/employees.html
 * Related CSS       : frontend/modules/regional-admin/pages/employees/employees.css
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/regional-admin/pages/employees/employees.html';

export default class RegionalAdminEmployees {

  constructor() {
    this.user = authStore.getUser();
  }

  async mount(container, lifecycle) {
    logger.info('RegionalAdminEmployees', 'Mounting Employee Overview page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    this._bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'regional-employees-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/regional-admin/pages/employees/employees.css';
      document.head.appendChild(link);
    }
  }

  _bindEvents(container, lifecycle) {
    // TODO: Bind page-specific events
  }

  destroy() {
    logger.info('RegionalAdminEmployees', 'Employee Overview page destroyed.');
  }
}