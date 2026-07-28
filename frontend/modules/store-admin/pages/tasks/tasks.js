/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin Module
 * File              : tasks.js
 * Path              : frontend/modules/store-admin/pages/tasks/tasks.js
 * Purpose           : Wrapper entry point for Store Admin Task Governance page
 ******************************************************************************/

import StoreTasksGovernance from './store-tasks.js';

export default class StoreAdminTasks {
  constructor() {
    this.delegate = new StoreTasksGovernance();
  }

  async mount(container, lifecycle) {
    await this.delegate.mount(container, lifecycle);
  }
}
