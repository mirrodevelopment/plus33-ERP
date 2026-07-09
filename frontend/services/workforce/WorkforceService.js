/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Services Module
 * File              : WorkforceService.js
 * Path              : frontend/services/workforce/WorkforceService.js
 * Purpose           : Frontend service wrapping backend REST APIs for Services Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/employees, GET /api/v2/employee-self-service/profile, POST /api/v2/payroll/process
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client
 * Depends On        : api/client
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend service wrapping backend REST APIs for Services Module. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../api/client.js';

class WorkforceService {
  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getEmployees(page = 0, size = 20) {
    return apiClient.get('/api/v1/employees', { page, size });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  getSelfServiceProfile() {
    return apiClient.get('/api/v2/employee-self-service/profile');
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Services Module
   */
  processPayroll(departmentId, payPeriod) {
    return apiClient.post('/api/v2/payroll/process', { departmentId, payPeriod });
  }
}

export const workforceService = new WorkforceService();
export default workforceService;
