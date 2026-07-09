/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module - Shift Supervisor Sub-Role
 * File              : announcements.js
 * Path              : frontend/modules/store-employee/supervisor-announcements/announcements.js
 * Purpose           : Broadcast announcements to baristas with database integration
 * Version           : 2.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

export default class SupervisorAnnouncements {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.announcements = [];
  }

  /**
   * Retrieves all announcements from the database
   */
  async fetchAnnouncements() {
    try {
      const response = await apiClient.get('/api/v1/announcements');
      if (response && response.success && response.data) {
        this.announcements = response.data;
      } else {
        this.announcements = [];
      }
    } catch (e) {
      logger.error('SupervisorAnnouncements', 'Error reading announcements from database:', e);
      this.announcements = [];
    }
  }

  /**
   * Mounts the view
   */
  async mount(container, lifecycle) {
    logger.info('SupervisorAnnouncements', 'Mounting supervisor announcements view...');
    await this.fetchAnnouncements();
    this.render(container);
    this.bindEvents(container);
  }

  /**
   * Renders the supervisor announcement panel
   */
  render(container) {
    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="max-width: 1000px; margin: 0 auto; padding: var(--spacing-xl) var(--spacing-lg);">
        
        <!-- Header -->
        <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-md); margin-bottom: var(--spacing-xl); display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: var(--spacing-sm);">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.6rem; color: var(--text-primary); margin: 0; display: flex; align-items: center; gap: var(--spacing-xs);">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><path d="m22 2-7 20-4-9-9-4Z"/><path d="M22 2 11 13"/></svg>
              Broadcast Announcements
            </h2>
            <span style="font-size: 0.72rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Publish shift bulletins and broadcast alerts to your active café roster</span>
          </div>
          <span style="font-size: 0.65rem; font-weight: 700; color: var(--accent-primary); background: rgba(201,164,106,0.1); padding: 6px 12px; border-radius: var(--radius-md); border: 1px solid rgba(201,164,106,0.2); display: flex; align-items: center; gap: 6px;">
            <span style="width: 6px; height: 6px; border-radius: 50%; background: var(--status-success); animation: pulse 2s infinite;"></span>
            DB BACKED
          </span>
        </div>

        <div style="display: grid; grid-template-columns: 1.1fr 0.9fr; gap: var(--spacing-xl); align-items: start;">
          
          <!-- Column 1: Active Published Broadcasts -->
          <div style="display: flex; flex-direction: column; gap: var(--spacing-lg);">
            <div class="card glass" style="padding: var(--spacing-lg); border: 1px solid var(--border-color); background: var(--bg-card); border-radius: var(--radius-lg);">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; color: var(--text-primary); margin: 0 0 var(--spacing-md) 0; display: flex; align-items: center; gap: 8px;">
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><rect width="20" height="20" x="2" y="2" rx="5" ry="5"/><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"/><line x1="17.5" x2="17.51" y1="6.5" y2="6.5"/></svg>
                Active Broadcast Feed (${this.announcements.length})
              </h3>
              
              <div style="display: flex; flex-direction: column; gap: var(--spacing-md);">
                ${this.announcements.length === 0 ? `
                  <div style="text-align: center; padding: 40px var(--spacing-md); border: 1px dashed rgba(255,255,255,0.05); border-radius: var(--radius-md); background: rgba(0,0,0,0.15);">
                    <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" style="color: var(--text-muted); margin-bottom: 8px;"><rect width="20" height="20" x="2" y="2" rx="5" ry="5"/><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"/><line x1="17.5" x2="17.51" y1="6.5" y2="6.5"/></svg>
                    <p style="font-size: 0.8rem; color: var(--text-secondary); margin: 0; font-weight: 600;">No active circulars broadcasted</p>
                    <p style="font-size: 0.68rem; color: var(--text-muted); margin: 4px 0 0 0;">Create and publish a bulletin on the right to notify your barista roster.</p>
                  </div>
                ` : this.announcements.map(ann => {
                  let badgeColor = 'rgba(255, 255, 255, 0.05)';
                  let textColor = 'var(--text-secondary)';
                  if (ann.priority === 'Critical Alert') {
                    badgeColor = 'rgba(235, 94, 85, 0.12)';
                    textColor = '#ff6b6b';
                  } else if (ann.priority === 'Company Bulletin') {
                    badgeColor = 'rgba(201, 164, 106, 0.12)';
                    textColor = 'var(--accent-primary)';
                  }
                  
                  return `
                    <div style="background: rgba(255,255,255,0.01); border: 1px solid var(--border-color); padding: var(--spacing-md); border-radius: var(--radius-md); display: flex; flex-direction: column; gap: var(--spacing-sm); position: relative; border-left: 3px solid ${ann.priority === 'Critical Alert' ? '#ff6b6b' : 'var(--accent-primary)'};">
                      <div style="display: flex; justify-content: space-between; align-items: start; gap: var(--spacing-md);">
                        <div>
                          <span style="font-size: 0.62rem; font-weight: 700; text-transform: uppercase; padding: 3px 8px; border-radius: 4px; background: ${badgeColor}; color: ${textColor}; display: inline-block; margin-bottom: 6px;">
                            ${ann.priority}
                          </span>
                          <h4 style="font-weight: 700; font-size: 0.9rem; color: var(--text-primary); margin: 0;">${ann.title}</h4>
                        </div>
                        <button type="button" class="btn-delete-announcement" data-id="${ann.id}" style="background:none; border:none; color:var(--text-muted); cursor:pointer; padding:4px; border-radius:4px; transition:var(--transition-fast);" onmouseover="this.style.color='#ff6b6b';" onmouseout="this.style.color='var(--text-muted)';">
                          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>
                        </button>
                      </div>
                      
                      <p style="font-size: 0.78rem; color: var(--text-secondary); line-height: 1.45; margin: 4px 0 0 0;">${ann.content}</p>
                      
                      <div style="border-top: 1px solid rgba(255,255,255,0.03); margin-top: 4px; padding-top: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center; font-size: 0.65rem; color: var(--text-muted); flex-wrap: wrap; gap: 4px;">
                        <span>Published by: <strong>${ann.publisher}</strong></span>
                        <span>Date: <strong>${ann.date}</strong></span>
                      </div>
                    </div>
                  `;
                }).join('')}
              </div>
            </div>
          </div>

          <!-- Column 2: Broadcast Creator Form -->
          <div class="card glass" style="padding: var(--spacing-lg); border: 1px solid var(--border-color); background: var(--bg-card); border-radius: var(--radius-lg); text-align: left;">
            <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; color: var(--text-primary); margin: 0; display: flex; align-items: center; gap: 8px;">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><path d="M5 12h14"/><path d="M12 5v14"/></svg>
              Compose Broadcast Circular
            </h3>
            <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block; margin-bottom: var(--spacing-md);">Publish alerts to the team dashboard in real-time</span>
            
            <form id="form-broadcast-announcement" style="display: flex; flex-direction: column; gap: var(--spacing-md);">
              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Bulletin Title</label>
                <input type="text" id="input-ann-title" placeholder="e.g. Grinder Maintenance Reminder" required style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Alert Priority / Category</label>
                <select id="select-ann-priority" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; cursor: pointer; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
                  <option value="Standard Notice">Standard Notice</option>
                  <option value="Critical Alert">Critical Alert</option>
                  <option value="Company Bulletin">Company Bulletin</option>
                </select>
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Broadcast Content</label>
                <textarea id="input-ann-content" placeholder="Enter message for baristas..." rows="5" required style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; resize: none; transition: var(--transition-fast); line-height: 1.45;" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';"></textarea>
              </div>

              <button type="submit" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.82rem; padding: 10px 20px; border-radius: var(--radius-md); border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 6px; transition: var(--transition-fast); margin-top: var(--spacing-xs);" onmouseover="this.style.opacity='0.9';" onmouseout="this.style.opacity='1';">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="22" x2="11" y1="2" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
                Publish & Broadcast
              </button>
            </form>
          </div>
          
        </div>
      </div>
    `;
  }

  /**
   * Binds user events
   */
  bindEvents(container) {
    const form = container.querySelector('#form-broadcast-announcement');
    if (form) {
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const titleInput = container.querySelector('#input-ann-title');
        const prioritySelect = container.querySelector('#select-ann-priority');
        const contentInput = container.querySelector('#input-ann-content');
        
        const title = titleInput.value.trim();
        const priority = prioritySelect.value;
        const content = contentInput.value.trim();
        
        if (!title || !content) {
          notificationStore.danger('Title and message content cannot be empty.');
          return;
        }

        try {
          const payload = { title, content, priority };
          const response = await apiClient.post('/api/v1/announcements', payload);

          if (response && response.success) {
            notificationStore.success('Announcement broadcasted and saved to database successfully!');
            form.reset();
            await this.fetchAnnouncements();
            this.render(container);
            this.bindEvents(container);
          } else {
            throw new Error(response.message || 'Database insert failed.');
          }
        } catch (err) {
          logger.error('SupervisorAnnouncements', 'Failed to publish announcement:', err);
          notificationStore.danger(`Broadcast failed: ${err.message}`);
        }
      });
    }

    // Delete announcement button
    const deleteButtons = container.querySelectorAll('.btn-delete-announcement');
    deleteButtons.forEach(btn => {
      btn.addEventListener('click', async () => {
        const id = btn.getAttribute('data-id');
        try {
          const response = await apiClient.delete(`/api/v1/announcements/${id}`);
          if (response && response.success) {
            notificationStore.success('Announcement removed from database.');
            await this.fetchAnnouncements();
            this.render(container);
            this.bindEvents(container);
          } else {
            throw new Error(response.message || 'Database delete failed.');
          }
        } catch (err) {
          logger.error('SupervisorAnnouncements', 'Failed to delete announcement:', err);
          notificationStore.danger('Failed to delete announcement from database.');
        }
      });
    });
  }
}
