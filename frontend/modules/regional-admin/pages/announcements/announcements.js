/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Regional Admin Module
 * File              : announcements.js
 * Path              : frontend/modules/regional-admin/pages/announcements/announcements.js
 * Purpose           : Regional Admin announcements broadcasting to all regional stores (Database backend)
 * Version           : 2.0.0
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';

export default class RegionalAnnouncements {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.announcements = [];
  }

  renderAttachment(url) {
    if (!url) return '';
    const lower = url.toLowerCase();
    if (lower.endsWith('.mp4') || lower.endsWith('.webm') || lower.endsWith('.ogg') || lower.endsWith('.mov')) {
      return `
        <div style="margin: 8px 0; border-radius: var(--radius-sm); overflow: hidden; border: 1px solid var(--border-color); max-height: 250px; background: #000;">
          <video src="${url}" controls style="width: 100%; height: auto; max-height: 250px;"></video>
        </div>
      `;
    } else if (lower.endsWith('.pdf')) {
      return `
        <div style="margin: 8px 0; padding: var(--spacing-sm); border-radius: var(--radius-sm); border: 1px solid var(--border-color); background: rgba(255,255,255,0.02); display: flex; align-items: center; justify-content: space-between; gap: var(--spacing-sm);">
          <div style="display: flex; align-items: center; gap: 8px;">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ff6b6b" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M15 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7Z"/><path d="M14 2v4a2 2 0 0 0 2 2h4"/></svg>
            <span style="font-size: 0.78rem; color: var(--text-primary); font-weight: 600;">PDF Document Attachment</span>
          </div>
          <a href="${url}" target="_blank" class="btn" style="padding: 4px 10px; font-size: 0.7rem; border-radius: var(--radius-xs); border: 1px solid var(--accent-primary); background: transparent; color: var(--accent-primary); font-weight: 700; text-decoration: none;">Open PDF</a>
        </div>
      `;
    } else {
      return `
        <div style="margin: 8px 0; max-height: 180px; overflow: hidden; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
          <img src="${url}" alt="Attachment" style="width: 100%; height: auto; object-fit: cover; max-height: 180px;">
        </div>
      `;
    }
  }

  /**
   * Retrieves all active published announcements from database
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
      logger.error('RegionalAnnouncements', 'Error reading published announcements:', e);
      this.announcements = [];
    }
  }

  /**
   * Mounts the view
   */
  async mount(container, lifecycle) {
    logger.info('RegionalAnnouncements', 'Mounting regional admin announcements view...');
    await this.fetchAnnouncements();
    this.render(container);
    this.bindEvents(container);
  }

  /**
   * Renders the regional admin announcement panel
   */
  render(container) {
    const isNational = this.user?.role === 'nationalAdmin';
    const scopeLabel = isNational ? 'National' : 'Regional';
    const scopeLower = isNational ? 'nation' : 'region';

    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="max-width: 1000px; margin: 0 auto; padding: var(--spacing-xl) var(--spacing-lg);">
        
        <!-- Header -->
        <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-md); margin-bottom: var(--spacing-xl); display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: var(--spacing-sm);">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.6rem; color: var(--text-primary); margin: 0; display: flex; align-items: center; gap: var(--spacing-xs);">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><path d="m22 2-7 20-4-9-9-4Z"/><path d="M22 2 11 13"/></svg>
              Broadcast ${scopeLabel} Bulletins
            </h2>
            <span style="font-size: 0.72rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Publish storewide updates and compliance alerts to all stores & employees under your ${scopeLower}</span>
          </div>
          <span style="font-size: 0.65rem; font-weight: 700; color: var(--accent-primary); background: rgba(201,164,106,0.1); padding: 6px 12px; border-radius: var(--radius-md); border: 1px solid rgba(201,164,106,0.2); display: flex; align-items: center; gap: 6px;">
            <span style="width: 6px; height: 6px; border-radius: 50%; background: var(--status-success); animation: pulse 2s infinite;"></span>
            ${scopeLabel.toUpperCase()} BROADCAST
          </span>
        </div>

        <div style="display: grid; grid-template-columns: 1.1fr 0.9fr; gap: var(--spacing-xl); align-items: start;">
          
          <!-- Column 1: Active Published Broadcasts -->
          <div style="display: flex; flex-direction: column; gap: var(--spacing-lg);">
            <div class="card glass" style="padding: var(--spacing-lg); border: 1px solid var(--border-color); background: var(--bg-card); border-radius: var(--radius-lg);">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; color: var(--text-primary); margin: 0 0 var(--spacing-md) 0; display: flex; align-items: center; gap: 8px;">
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><rect width="20" height="20" x="2" y="2" rx="5" ry="5"/><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"/><line x1="17.5" x2="17.51" y1="6.5" y2="6.5"/></svg>
                Published Bulletins Feed (${this.announcements.length})
              </h3>
              
              <div style="display: flex; flex-direction: column; gap: var(--spacing-md);">
                ${this.announcements.length === 0 ? `
                  <div style="text-align: center; padding: 40px var(--spacing-md); border: 1px dashed rgba(255,255,255,0.05); border-radius: var(--radius-md); background: rgba(0,0,0,0.15);">
                    <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" style="color: var(--text-muted); margin-bottom: 8px;"><rect width="20" height="20" x="2" y="2" rx="5" ry="5"/><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"/><line x1="17.5" x2="17.51" y1="6.5" y2="6.5"/></svg>
                    <p style="font-size: 0.8rem; color: var(--text-secondary); margin: 0; font-weight: 600;">No active bulletins</p>
                    <p style="font-size: 0.68rem; color: var(--text-muted); margin: 4px 0 0 0;">Create and publish a bulletin on the right to notify your roster.</p>
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
                        <button type="button" class="btn-delete-announcement" data-id="${ann.id}" style="background:#ff6b6b; border:none; color:#000; cursor:pointer; padding:5px 10px; border-radius:6px; transition:all 0.2s; font-size:0.68rem; font-weight:700; display:flex; align-items:center; gap:4px;" onmouseover="this.style.background='#ff5252'; this.style.color='#fff';" onmouseout="this.style.background='#ff6b6b'; this.style.color='#000';">
                          <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>
                          Delete
                        </button>
                      </div>
                      
                      <p style="font-size: 0.78rem; color: var(--text-secondary); line-height: 1.45; margin: 4px 0 0 0;">${ann.content}</p>
                      
                      ${this.renderAttachment(ann.imageUrl)}

                      <div style="border-top: 1px solid rgba(255,255,255,0.03); margin-top: 4px; padding-top: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center; font-size: 0.65rem; color: var(--text-muted); flex-wrap: wrap; gap: 4px;">
                        <span>Publisher: <strong>${ann.publisher}${ann.publisherRole ? ` (${ann.publisherRole})` : ''}</strong></span>
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
              Compose ${scopeLabel} Bulletin
            </h3>
            <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block; margin-bottom: var(--spacing-md);">Broadcast a notification to all store managers & staff under your ${scopeLower}</span>
            
            <form id="form-broadcast-announcement" style="display: flex; flex-direction: column; gap: var(--spacing-md);">
              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Bulletin Title</label>
                <input type="text" id="input-ann-title" placeholder="e.g. ${scopeLabel} Standards Update" required style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Priority / Classification</label>
                <select id="select-ann-priority" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; cursor: pointer; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
                  <option value="Standard Notice">Standard Notice</option>
                  <option value="Critical Alert">Critical Alert</option>
                  <option value="Company Bulletin">Company Bulletin</option>
                </select>
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Bulletin Content</label>
                <textarea id="input-ann-content" placeholder="Enter message for all ${scopeLower} store managers and store staff..." rows="5" required style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; resize: none; transition: var(--transition-fast); line-height: 1.45;" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';"></textarea>
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Attachment File (Optional - Image, Video, PDF)</label>
                <input type="file" id="input-ann-file" accept="image/*,video/*,application/pdf" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <button type="submit" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.82rem; padding: 10px 20px; border-radius: var(--radius-md); border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 6px; transition: var(--transition-fast); margin-top: var(--spacing-xs);" onmouseover="this.style.opacity='0.9';" onmouseout="this.style.opacity='1';">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="22" x2="11" y1="2" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
                Broadcast ${scopeLabel}wide
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
        const fileInput = container.querySelector('#input-ann-file');

        const title = titleInput.value.trim();
        const priority = prioritySelect.value;
        const content = contentInput.value.trim();
        
        if (!title || !content) {
          notificationStore.danger('Title and message content cannot be empty.');
          return;
        }

        let imageUrl = '';
        const file = fileInput && fileInput.files ? fileInput.files[0] : null;

        if (file) {
          try {
            notificationStore.info('Uploading attachment file to server...');
            const response = await fetch('/api/upload-announcement-attachment', {
              method: 'POST',
              headers: {
                'Content-Type': file.type,
                'X-File-Name': file.name
              },
              body: file
            });
            const data = await response.json();
            if (data && data.success && data.url) {
              imageUrl = data.url;
            } else {
              throw new Error(data.message || 'File server upload failed.');
            }
          } catch (uploadErr) {
            logger.error('RegionalAnnouncements', 'Error uploading media attachment:', uploadErr);
            notificationStore.danger('Failed to upload file attachment: ' + uploadErr.message);
            return;
          }
        }

        try {
          const payload = { title, content, priority, imageUrl };
          const response = await apiClient.post('/api/v1/announcements', payload);
          if (response && response.success) {
            notificationStore.success('Regional bulletin broadcasted successfully!');
            
            // Clear inputs
            titleInput.value = '';
            contentInput.value = '';
            if (fileInput) fileInput.value = '';
            prioritySelect.selectedIndex = 0;

            await this.fetchAnnouncements();
            this.render(container);
            this.bindEvents(container);
          } else {
            notificationStore.danger(response.message || 'Failed to broadcast announcement.');
          }
        } catch (err) {
          logger.error('RegionalAnnouncements', 'Error composing regional bulletin:', err);
          notificationStore.danger('Database error publishing bulletin circular.');
        }
      });
    }

    const deleteBtns = container.querySelectorAll('.btn-delete-announcement');
    deleteBtns.forEach(btn => {
      btn.addEventListener('click', async () => {
        const id = btn.getAttribute('data-id');
        if (confirm('Are you sure you want to delete this regional bulletin?')) {
          try {
            const res = await apiClient.delete(`/api/v1/announcements/${id}`);
            if (res && res.success) {
              notificationStore.success('Regional bulletin deleted successfully.');
              await this.fetchAnnouncements();
              this.render(container);
              this.bindEvents(container);
            } else {
              notificationStore.danger(res.message || 'Failed to delete bulletin.');
            }
          } catch (e) {
            logger.error('RegionalAnnouncements', 'Error deleting regional bulletin:', e);
            notificationStore.danger('Failed to execute database delete command.');
          }
        }
      });
    });
  }
}
