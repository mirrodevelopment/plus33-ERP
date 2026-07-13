/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : tasks.js
 * Path              : frontend/modules/store-employee/tasks/tasks.js
 * Purpose           : Controller component for Barista daily checklist shift tasks UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/tasks/tasks.html
 * Related CSS       : frontend/modules/store-employee/tasks/tasks.css
 * Related APIs      : None (uses LocalStorage states caching)
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in tasks.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the tasks HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/tasks/tasks.html';

export default class StoreEmployeeTasks {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `emp_dashboard_state_${this.user?.username || 'neha'}`;
    
    this.filterStatus = 'all'; // 'all', 'pending', 'completed'
    this.filterPriority = 'all'; // 'all', 'high', 'medium', 'low'
    
    this.loadState();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeeTasks component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeTasks', 'Mounting Barista Tasks Page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout template
    await this._loadTemplate(container);

    // 2. Read state details
    this.loadState();

    // 3. Render layout elements
    this.render(container);

    // 4. Bind event listeners
    this.bindEvents(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  loadState() {
    const cachedState = localStorage.getItem(this.stateKey);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
      } catch (err) {
        logger.error('StoreEmployeeTasks', 'Error parsing cached state', err);
        this.initDefaultState();
      }
    } else {
      this.initDefaultState();
    }
  }

  initDefaultState() {
    this.state = {
      name: this.profile.name || 'Neha Sharma',
      id: 'EMP10245',
      level: 'Senior Barista',
      store: 'Green Park Café, City Center',
      clockedIn: true,
      clockInTime: '08:02 AM',
      trainingProgress: 72,
      performanceScore: 4.6,
      tasks: [
        { id: 1, name: 'Prepare espresso bar and grinders', priority: 'High Priority', status: 'In Progress', section: 'Espresso Bar' },
        { id: 2, name: 'Restock milk cartons and syrups', priority: 'Medium Priority', status: 'Not Started', section: 'Inventory' },
        { id: 3, name: 'Sanitize customer tables and counters', priority: 'Medium Priority', status: 'Not Started', section: 'Cleaning' },
        { id: 4, name: 'Verify cold brew keg pressures', priority: 'Low Priority', status: 'In Progress', section: 'Espresso Bar' },
        { id: 5, name: 'Log temperature check logs', priority: 'High Priority', status: 'Not Started', section: 'Safety Check' }
      ],
      leave: { available: 12.5, pending: 1, approved: 3 },
      attendanceLogs: [
        { date: 'Today', shift: 'Morning Shift', checkIn: '08:02 AM', checkOut: '--:--', hours: '6.2h', status: 'Active' }
      ],
      activities: []
    };
    this.saveState();
  }

  saveState() {
    localStorage.setItem(this.stateKey, JSON.stringify(this.state));
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    // 1. Sync header text
    const designationEl = container.querySelector('#lbl-employee-designation');
    if (designationEl) designationEl.textContent = this.profile.designation || 'Senior Barista';

    // 2. Calculate shift stats
    const total = this.state.tasks.length;
    const completed = this.state.tasks.filter(t => t.status === 'Completed').length;
    const pending = total - completed;
    const rate = total > 0 ? Math.round((completed / total) * 100) : 0;

    // 3. Sync stats widgets text & bars
    const totalEl = container.querySelector('#kpi-total-tasks');
    const pendingEl = container.querySelector('#kpi-pending-tasks');
    const completedEl = container.querySelector('#kpi-completed-tasks');
    const rateEl = container.querySelector('#kpi-completion-rate');
    const rateBar = container.querySelector('#kpi-progress-bar');

    if (totalEl) totalEl.textContent = `${total} Tasks`;
    if (pendingEl) pendingEl.textContent = `${pending} Pending`;
    if (completedEl) completedEl.textContent = `${completed} Completed`;
    if (rateEl) rateEl.textContent = `${rate}%`;
    if (rateBar) {
      rateBar.style.width = `${rate}%`;
    }

    // 4. Sync status filters pills active state
    const btnAll = container.querySelector('#btn-filter-all');
    const btnPending = container.querySelector('#btn-filter-pending');
    const btnCompleted = container.querySelector('#btn-filter-completed');

    if (btnAll) btnAll.className = `filter-btn ${this.filterStatus === 'all' ? 'active' : ''}`;
    if (btnPending) btnPending.className = `filter-btn ${this.filterStatus === 'pending' ? 'active' : ''}`;
    if (btnCompleted) btnCompleted.className = `filter-btn ${this.filterStatus === 'completed' ? 'active' : ''}`;

    // 5. Sync priority filter dropdown value
    const prioritySelect = container.querySelector('#select-filter-priority');
    if (prioritySelect) {
      prioritySelect.value = this.filterPriority;
    }

    // 6. Render Tasks checklist logs rows
    this._renderTasksList(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const submitBtn = container.querySelector('#btn-submit-task');
    const resetBtn = container.querySelector('#btn-reset-tasks');
    const statusBtns = container.querySelectorAll('[data-status]');
    const prioritySelect = container.querySelector('#select-filter-priority');

    // 1. Checkbox toggle event listeners inside checklist table
    const checkboxes = container.querySelectorAll('.task-manager-checkbox');
    checkboxes.forEach(chk => {
      const handleToggle = () => {
        const id = parseInt(chk.getAttribute('data-id'));
        const task = this.state.tasks.find(t => t.id === id);
        if (task) {
          task.status = chk.checked ? 'Completed' : 'In Progress';
          
          if (chk.checked) {
            notificationStore.info(`Completed task: ${task.name}`);
            this.state.activities.unshift({ text: `Completed checklist item "${task.name}"`, time: 'Just now' });
          }
          
          this.saveState();
          this.render(container);
          this.bindEvents(container, lifecycle);
        }
      };
      chk.addEventListener('change', handleToggle);
      lifecycle.onCleanup(() => chk.removeEventListener('change', handleToggle));
    });

    // 2. Custom checkmark click events (bridge toggle since native checkbox is hidden)
    const customSpans = container.querySelectorAll('.custom-checkbox-span');
    customSpans.forEach(span => {
      const chk = span.previousElementSibling;
      if (chk) {
        const handleSpanClick = () => {
          chk.checked = !chk.checked;
          chk.dispatchEvent(new Event('change'));
        };
        span.addEventListener('click', handleSpanClick);
        lifecycle.onCleanup(() => span.removeEventListener('click', handleSpanClick));
      }
    });

    // 3. Delete task button click handler
    const deleteBtns = container.querySelectorAll('.btn-manager-delete-task');
    deleteBtns.forEach(btn => {
      const handleDelete = () => {
        const id = parseInt(btn.getAttribute('data-id'));
        const index = this.state.tasks.findIndex(t => t.id === id);
        if (index > -1) {
          const taskName = this.state.tasks[index].name;
          this.state.tasks.splice(index, 1);
          notificationStore.info(`Removed task: ${taskName}`);
          
          this.saveState();
          this.render(container);
          this.bindEvents(container, lifecycle);
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });

    // 4. Add Custom Task submit handler
    if (submitBtn) {
      const handleSubmitTask = () => {
        const nameInput = container.querySelector('#input-task-name');
        const sectionSelect = container.querySelector('#select-task-section');
        const prioritySelectEl = container.querySelector('#select-task-priority');
        
        const name = nameInput.value.trim();
        const section = sectionSelect.value;
        const priority = prioritySelectEl.value;

        if (!name) {
          notificationStore.danger('Please specify a task name.');
          return;
        }

        const newId = this.state.tasks.length ? Math.max(...this.state.tasks.map(t => t.id)) + 1 : 1;
        this.state.tasks.push({
          id: newId,
          name,
          section,
          priority,
          status: 'Not Started'
        });

        notificationStore.success(`Created task: ${name}`);
        this.state.activities.unshift({ text: `Added custom task "${name}"`, time: 'Just now' });
        
        nameInput.value = '';
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      submitBtn.addEventListener('click', handleSubmitTask);
      lifecycle.onCleanup(() => submitBtn.removeEventListener('click', handleSubmitTask));
    }

    // 5. Status filter click handler
    statusBtns.forEach(btn => {
      const handleStatusFilter = () => {
        this.filterStatus = btn.getAttribute('data-status');
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      btn.addEventListener('click', handleStatusFilter);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleStatusFilter));
    });

    // 6. Priority Filter Dropdown change listener
    if (prioritySelect) {
      const handlePriorityFilter = () => {
        this.filterPriority = prioritySelect.value;
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      prioritySelect.addEventListener('change', handlePriorityFilter);
      lifecycle.onCleanup(() => prioritySelect.removeEventListener('change', handlePriorityFilter));
    }

    // 7. Reset to Defaults button listener
    if (resetBtn) {
      const handleReset = () => {
        this.initDefaultState();
        notificationStore.success('Tasks list has been reset to shift defaults.');
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      resetBtn.addEventListener('click', handleReset);
      lifecycle.onCleanup(() => resetBtn.removeEventListener('click', handleReset));
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderTasksList(container) {
    const listContainer = container.querySelector('#tasks-manager-list');
    const emptyTpl = container.querySelector('#tasks-empty-list-tpl');
    const rowTpl = container.querySelector('#task-item-row-tpl');

    if (!listContainer) return;

    listContainer.replaceChildren();

    const filteredTasks = this.state.tasks.filter(t => {
      const matchStatus = this.filterStatus === 'all' || 
                          (this.filterStatus === 'completed' && t.status === 'Completed') ||
                          (this.filterStatus === 'pending' && t.status !== 'Completed');
                          
      const matchPriority = this.filterPriority === 'all' || 
                            t.priority.toLowerCase().includes(this.filterPriority);
                            
      return matchStatus && matchPriority;
    });

    if (filteredTasks.length === 0) {
      if (emptyTpl) {
        listContainer.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    filteredTasks.forEach(t => {
      if (!rowTpl) return;
      const clone = rowTpl.content.cloneNode(true);

      const chk = clone.querySelector('.task-manager-checkbox');
      const customSpan = clone.querySelector('.custom-checkbox-span');
      const nameEl = clone.querySelector('.task-name-text');
      const sectionTag = clone.querySelector('.task-section-tag');
      const priorityBadge = clone.querySelector('.priority-badge-label');
      const deleteBtn = clone.querySelector('.btn-manager-delete-task');

      const isComp = t.status === 'Completed';

      if (chk) {
        chk.setAttribute('data-id', String(t.id));
        chk.checked = isComp;
      }

      if (customSpan) {
        customSpan.setAttribute('data-id', String(t.id));
        if (isComp) {
          customSpan.className = 'custom-checkbox-span checked';
          customSpan.setAttribute('aria-checked', 'true');
          customSpan.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#000" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="20 6 9 17 4 12"></polyline>
            </svg>`;
        } else {
          customSpan.className = 'custom-checkbox-span';
          customSpan.setAttribute('aria-checked', 'false');
        }
      }

      if (nameEl) {
        nameEl.textContent = t.name;
        if (isComp) {
          nameEl.className = 'task-name-text line-through';
        }
      }

      if (sectionTag) {
        sectionTag.textContent = `Section: ${t.section || 'General'}`;
      }

      if (priorityBadge) {
        priorityBadge.textContent = t.priority.split(' ')[0];
        if (t.priority === 'High Priority') {
          priorityBadge.className = 'priority-badge-label priority-badge-label--high font-bold';
        } else if (t.priority === 'Medium Priority') {
          priorityBadge.className = 'priority-badge-label priority-badge-label--medium font-bold';
        } else {
          priorityBadge.className = 'priority-badge-label priority-badge-label--low font-bold';
        }
      }

      if (deleteBtn) {
        deleteBtn.setAttribute('data-id', String(t.id));
      }

      listContainer.appendChild(clone);
    });
  }

  _loadCss() {
    const cssId = 'store-employee-tasks-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/tasks/tasks.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeeTasks };
