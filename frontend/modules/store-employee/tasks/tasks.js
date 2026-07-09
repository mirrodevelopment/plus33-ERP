/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : tasks.js
 * Path              : frontend/modules/store-employee/tasks/tasks.js
 * Purpose           : Barista task checklists, prioritization, and synchronization
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';

export default class StoreEmployeeTasks {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `emp_dashboard_state_${this.user?.username || 'neha'}`;
    
    this.filterStatus = 'all'; // 'all', 'pending', 'completed'
    this.filterPriority = 'all'; // 'all', 'high', 'medium', 'low'
    
    this.loadState();
  }

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
      leave: {
        available: 12.5,
        pending: 1,
        approved: 3
      },
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

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeTasks', 'Mounting Barista Tasks Page...');
    this.loadState();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  render(container) {
    // Recalculate metrics
    const total = this.state.tasks.length;
    const completed = this.state.tasks.filter(t => t.status === 'Completed').length;
    const pending = total - completed;
    const rate = total > 0 ? Math.round((completed / total) * 100) : 0;
    
    // Apply filters
    const filteredTasks = this.state.tasks.filter(t => {
      const matchStatus = this.filterStatus === 'all' || 
                          (this.filterStatus === 'completed' && t.status === 'Completed') ||
                          (this.filterStatus === 'pending' && t.status !== 'Completed');
                          
      const matchPriority = this.filterPriority === 'all' || 
                            t.priority.toLowerCase().includes(this.filterPriority);
                            
      return matchStatus && matchPriority;
    });

    container.innerHTML = `
      <style>
        .filter-btn {
          padding: 6px 14px;
          border-radius: var(--radius-md);
          font-weight: 700;
          font-size: 0.72rem;
          cursor: pointer;
          background: transparent;
          border: 1px solid rgba(255,255,255,0.08);
          color: var(--text-muted);
          transition: var(--transition-fast);
        }
        .filter-btn.active {
          border-color: var(--accent-primary);
          background: rgba(201,164,106,0.1);
          color: var(--accent-primary);
        }
        .task-detail-row {
          background: rgba(0,0,0,0.15);
          border: 1px solid var(--border-color);
          border-radius: var(--radius-md);
          padding: var(--spacing-md);
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: var(--spacing-md);
          transition: all 0.2s ease-out;
        }
        .task-detail-row:hover {
          border-color: rgba(255,255,255,0.08);
          background: rgba(255,255,255,0.02);
        }
      </style>

      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header banner -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0;">
              Daily Shift Tasks
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              Role: <span style="color: var(--accent-primary); font-weight: 700;">${this.profile.designation || 'Senior Barista'}</span> &nbsp;·&nbsp; Location: <span style="color: var(--accent-primary); font-weight: 700;">Green Park Café</span>
            </p>
          </div>
          <button class="btn btn-secondary" id="btn-reset-tasks" style="padding: 6px 12px; font-weight: 700; font-size: 0.7rem; display: flex; align-items: center; gap: 4px;">
            <i data-lucide="rotate-ccw" style="width: 12px; height: 12px;"></i> Reset to Defaults
          </button>
        </div>

        <!-- Dashboard Stats Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: var(--spacing-md); width: 100%;">
          
          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
              <i data-lucide="check-square" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${total} Tasks</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Assigned Roster</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(239,68,68,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-danger); display:flex; align-items:center;">
              <i data-lucide="alert-circle" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${pending} Pending</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Attention Required</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(74,222,128,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
              <i data-lucide="check-circle-2" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${completed} Completed</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Shift Goals Done</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
              <i data-lucide="trending-up" style="width: 22px; height: 22px;"></i>
            </div>
            <div style="flex:1;">
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${rate}%</div>
              <div style="width: 100%; height: 5px; background: rgba(255,255,255,0.08); border-radius: 3px; margin-top: 6px; overflow:hidden;">
                <div style="width: ${rate}%; height: 100%; background: var(--accent-primary); border-radius: 3px; transition: width 0.3s;"></div>
              </div>
            </div>
          </div>

        </div>

        <!-- Main workspace columns -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Left Column: Tasks Manager List -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; flex: 1.8;">
            
            <!-- Filters Ribbon -->
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: var(--spacing-sm); border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <div style="display: flex; gap: 6px;">
                <button class="filter-btn ${this.filterStatus === 'all' ? 'active' : ''}" data-status="all">All</button>
                <button class="filter-btn ${this.filterStatus === 'pending' ? 'active' : ''}" data-status="pending">Pending</button>
                <button class="filter-btn ${this.filterStatus === 'completed' ? 'active' : ''}" data-status="completed">Completed</button>
              </div>

              <div style="display: flex; align-items: center; gap: 8px;">
                <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Priority</span>
                <select id="select-filter-priority" style="background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); font-size: 0.72rem; padding: 4px 10px; outline: none; cursor: pointer;">
                  <option value="all" ${this.filterPriority === 'all' ? 'selected' : ''}>All Priorities</option>
                  <option value="high" ${this.filterPriority === 'high' ? 'selected' : ''}>High Only</option>
                  <option value="medium" ${this.filterPriority === 'medium' ? 'selected' : ''}>Medium Only</option>
                  <option value="low" ${this.filterPriority === 'low' ? 'selected' : ''}>Low Only</option>
                </select>
              </div>
            </div>

            <!-- List Body -->
            <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); min-height: 300px;" id="tasks-manager-list">
              ${filteredTasks.length === 0 ? `
                <div style="flex:1; display:flex; flex-direction:column; align-items:center; justify-content:center; padding:var(--spacing-lg); color:var(--text-muted); border: 1.5px dashed rgba(255,255,255,0.05); border-radius:var(--radius-md); text-align:center;">
                  <i data-lucide="clipboard-list" style="width:36px; height:36px; margin-bottom:10px; opacity:0.3;"></i>
                  <span style="font-weight:600; font-size:0.8rem;">No matching tasks found</span>
                  <span style="font-size:0.68rem; margin-top:2px;">Adjust filters or add a new custom task to start.</span>
                </div>
              ` : filteredTasks.map(t => {
                const isComp = t.status === 'Completed';
                const pColor = t.priority === 'High Priority' ? 'var(--status-danger)' : 
                               (t.priority === 'Medium Priority' ? 'var(--status-warning)' : 'var(--text-muted)');
                return `
                  <div class="task-detail-row">
                    <div style="display: flex; align-items: center; gap: var(--spacing-md); flex: 1;">
                      <label style="display: flex; align-items: center; gap: 8px; cursor: pointer; margin: 0; width: 100%;">
                        <input type="checkbox" class="task-manager-checkbox" data-id="${t.id}" ${isComp ? 'checked' : ''} style="display: none;">
                        <span style="width: 18px; height: 18px; border-radius: 4px; border: 1.5px solid ${isComp ? 'var(--accent-primary)' : 'var(--text-muted)'}; display: inline-flex; align-items: center; justify-content: center; background: ${isComp ? 'var(--accent-primary)' : 'transparent'}; transition: all 0.2s;">
                          ${isComp ? `
                            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#000" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round">
                              <polyline points="20 6 9 17 4 12"></polyline>
                            </svg>
                          ` : ''}
                        </span>
                        <div style="text-align: left;">
                          <div style="font-weight: 700; font-size: 0.82rem; color: ${isComp ? 'var(--text-muted)' : 'var(--text-primary)'}; text-decoration: ${isComp ? 'line-through' : 'none'};">${t.name}</div>
                          <span style="font-size: 0.62rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase;">Section: ${t.section || 'General'}</span>
                        </div>
                      </label>
                    </div>

                    <div style="display: flex; align-items: center; gap: var(--spacing-md);">
                      <span style="font-size: 0.6rem; font-weight: 700; padding: 2px 8px; border-radius: 4px; background: rgba(255,255,255,0.03); color: ${pColor}; text-transform: uppercase; border: 1px solid rgba(255,255,255,0.01);">
                        ${t.priority.split(' ')[0]}
                      </span>
                      <button class="btn-manager-delete-task" data-id="${t.id}" style="background: none; border: none; color: var(--text-muted); cursor: pointer; display: flex; align-items: center;" onmouseover="this.style.color='var(--status-danger)'" onmouseout="this.style.color='var(--text-muted)'">
                        <i data-lucide="trash-2" style="width: 14px; height: 14px;"></i>
                      </button>
                    </div>
                  </div>
                `;
              }).join('')}
            </div>

          </div>

          <!-- Right Column: Add Custom Task Form -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; height: fit-content; flex: 1;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Add New Shift Task</h3>
              <i data-lucide="plus-circle" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); font-size: 0.76rem;">
              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Task Goal / Name</label>
                <input type="text" id="input-task-name" placeholder="e.g. Sanitize milk frothers" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
              </div>

              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Task Station / Section</label>
                <select id="select-task-section" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none; cursor: pointer;">
                  <option value="Espresso Bar">Espresso Bar</option>
                  <option value="Inventory">Inventory</option>
                  <option value="Cleaning">Cleaning</option>
                  <option value="Safety Check">Safety Check</option>
                  <option value="General">General</option>
                </select>
              </div>

              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Priority Level</label>
                <select id="select-task-priority" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none; cursor: pointer;">
                  <option value="High Priority">High Priority</option>
                  <option value="Medium Priority" selected>Medium Priority</option>
                  <option value="Low Priority">Low Priority</option>
                </select>
              </div>

              <button class="btn" id="btn-submit-task" style="background: var(--accent-primary); color: #000; font-weight: 800; border: none; border-radius: var(--radius-md); padding: var(--spacing-sm); cursor: pointer; transition: var(--transition-fast); margin-top: 4px;">
                Create Roster Task
              </button>
            </div>
          </div>

        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    // 1. Checkbox toggle
    const checkboxes = container.querySelectorAll('.task-manager-checkbox');
    checkboxes.forEach(chk => {
      chk.addEventListener('change', () => {
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
      });
    });

    // 2. Delete task
    const deleteBtns = container.querySelectorAll('.btn-manager-delete-task');
    deleteBtns.forEach(btn => {
      btn.addEventListener('click', () => {
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
      });
    });

    // 3. Add Custom Task
    const submitBtn = container.querySelector('#btn-submit-task');
    if (submitBtn) {
      submitBtn.addEventListener('click', () => {
        const nameInput = container.querySelector('#input-task-name');
        const sectionSelect = container.querySelector('#select-task-section');
        const prioritySelect = container.querySelector('#select-task-priority');
        
        const name = nameInput.value.trim();
        const section = sectionSelect.value;
        const priority = prioritySelect.value;

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
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    }

    // 4. Status Filter Buttons
    const statusBtns = container.querySelectorAll('[data-status]');
    statusBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        this.filterStatus = btn.getAttribute('data-status');
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    });

    // 5. Priority Filter Dropdown
    const prioritySelect = container.querySelector('#select-filter-priority');
    if (prioritySelect) {
      prioritySelect.addEventListener('change', () => {
        this.filterPriority = prioritySelect.value;
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    }

    // 6. Reset to Defaults
    const resetBtn = container.querySelector('#btn-reset-tasks');
    if (resetBtn) {
      resetBtn.addEventListener('click', () => {
        this.initDefaultState();
        notificationStore.success('Tasks list has been reset to shift defaults.');
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    }
  }
}
