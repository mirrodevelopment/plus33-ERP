/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : announcements.js
 * Path              : frontend/modules/store-employee/announcements/announcements.js
 * Purpose           : Barista announcements feed with database integration
 * Version           : 2.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

export default class StoreEmployeeAnnouncements {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.announcementsList = [];
    this.filterStatus = 'all'; // 'all', 'unread', 'critical'
  }

  /**
   * Fetches announcements from PostgreSQL database
   */
  async fetchAnnouncements() {
    try {
      const response = await apiClient.get('/api/v1/announcements');
      if (response && response.success && response.data) {
        this.announcementsList = response.data;
      } else {
        this.announcementsList = [];
      }
    } catch (err) {
      logger.error('StoreEmployeeAnnouncements', 'Error fetching announcements from DB:', err);
      notificationStore.danger('Failed to retrieve announcements from database.');
      this.announcementsList = [];
    }
  }

  /**
   * Mounts the component
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeAnnouncements', 'Mounting Barista Announcements Page...');
    await this.fetchAnnouncements();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  /**
   * Renders announcements board HTML
   */
  render(container) {
    const unreadCount = this.announcementsList.filter(a => !a.read).length;
    const totalCount = this.announcementsList.length;

    // Apply filters
    const filteredAnnouncements = this.announcementsList.filter(a => {
      if (this.filterStatus === 'unread') return !a.read;
      if (this.filterStatus === 'critical') return a.priority === 'Critical Alert';
      return true;
    });

    container.innerHTML = `
      <style>
        .ann-card {
          background: rgba(0,0,0,0.15);
          border: 1px solid var(--border-color);
          border-radius: var(--radius-md);
          padding: var(--spacing-md);
          display: flex;
          flex-direction: column;
          gap: 8px;
          text-align: left;
          cursor: pointer;
          transition: all 0.25s ease-out;
        }
        .ann-card:hover {
          transform: translateY(-1px);
          border-color: rgba(255,255,255,0.08);
          background: rgba(255,255,255,0.02);
        }
        .ann-card.unread {
          border-left: 4px solid var(--accent-primary);
        }
        .ann-card.critical {
          border-left: 4px solid var(--status-danger);
          background: rgba(239,68,68,0.02);
        }
        .filter-pill {
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
        .filter-pill.active {
          border-color: var(--accent-primary);
          background: rgba(201,164,106,0.1);
          color: var(--accent-primary);
        }
      </style>

      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header ribbon -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0;">
              Announcements Board
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              Store Bulletin &nbsp;·&nbsp; Unread Alerts: <span style="color: var(--accent-primary); font-weight: 700;">${unreadCount}</span>
            </p>
          </div>
          <button class="btn btn-secondary" id="btn-mark-all-read" style="padding: 6px 12px; font-weight: 700; font-size: 0.7rem; display: flex; align-items: center; gap: 4px;">
            <i data-lucide="check" style="width: 12px; height: 12px;"></i> Mark all as Read
          </button>
        </div>

        <!-- Filters Ribbon -->
        <div style="display: flex; gap: 6px; align-items: center;">
          <button class="filter-pill ${this.filterStatus === 'all' ? 'active' : ''}" data-filter="all">All (${totalCount})</button>
          <button class="filter-pill ${this.filterStatus === 'unread' ? 'active' : ''}" data-filter="unread">Unread (${unreadCount})</button>
          <button class="filter-pill ${this.filterStatus === 'critical' ? 'active' : ''}" data-filter="critical">Critical</button>
        </div>

        <!-- Main Content layout container -->
        <div style="width: 100%; max-width: 800px; margin: 0 auto; display: flex; flex-direction: column; gap: var(--spacing-md);">
          
          <!-- Announcements feed list -->
          <div style="display: flex; flex-direction: column; gap: var(--spacing-md); width: 100%;">
            ${filteredAnnouncements.length === 0 ? `
              <div class="card glass" style="padding: var(--spacing-lg); color:var(--text-muted); border: 1.5px dashed rgba(255,255,255,0.05); border-radius:var(--radius-md); text-align:center;">
                <i data-lucide="megaphone" style="width:36px; height:36px; margin-bottom:10px; opacity:0.3; display:inline-block;"></i>
                <span style="font-weight:600; font-size:0.8rem; display:block;">No announcements found</span>
                <span style="font-size:0.68rem; margin-top:2px; display:block;">All caught up! Check back later for circulars.</span>
              </div>
            ` : filteredAnnouncements.map(ann => {
              const isCrit = ann.priority === 'Critical Alert';
              const pColor = isCrit ? 'var(--status-danger)' : 
                             (ann.priority === 'Standard Notice' ? 'var(--status-info)' : 'var(--text-muted)');
              
              const reactions = ann.reactions || { thumbsUp: 0, heart: 0, lightbulb: 0, coffee: 0 };
              
              return `
                <div class="ann-card ${!ann.read ? 'unread' : ''} ${isCrit ? 'critical' : ''}" data-id="${ann.id}">
                  <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:6px;">
                    <span style="font-size: 0.6rem; font-weight: 700; color: ${pColor}; text-transform: uppercase;">
                      ${ann.priority}
                    </span>
                    <span style="font-size: 0.65rem; color: var(--text-muted);">${ann.date}</span>
                  </div>
                  
                  <h4 style="margin: 0; font-size: 0.95rem; font-weight: 800; color: var(--text-primary); display:flex; align-items:center; gap:6px;">
                    ${ann.title}
                    ${!ann.read ? `<span style="width: 6px; height: 6px; border-radius: 50%; background: var(--accent-primary); display: inline-block;"></span>` : ''}
                  </h4>
                  <p style="margin: 2px 0 0 0; font-size: 0.76rem; color: var(--text-muted); line-height: 1.4; display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical; overflow:hidden;">
                    ${ann.content}
                  </p>
                  
                  <div style="display: flex; align-items: center; justify-content: space-between; margin-top: 6px; flex-wrap: wrap; gap: 8px;">
                    <span style="font-size: 0.65rem; color: var(--text-secondary); font-weight: 600;">Publisher: ${ann.publisher}</span>
                    
                    <div style="display: flex; align-items: center; gap: 8px;">
                      <!-- Read/Readed button -->
                      ${!ann.read ? `
                        <button class="btn btn-mark-read-card" data-id="${ann.id}" style="background: rgba(201,164,106,0.15); border: 1px solid rgba(201,164,106,0.3); color: var(--accent-primary); font-size: 0.65rem; font-weight: 700; padding: 4px 8px; border-radius: 4px; cursor: pointer; transition: var(--transition-fast);">Readed</button>
                      ` : `
                        <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700;">✓ Readed</span>
                      `}
                    </div>
                  </div>
                  
                  <!-- Reactions row -->
                  <div style="display: flex; align-items: center; gap: 8px; border-top: 1px solid rgba(255,255,255,0.03); margin-top: 6px; padding-top: 6px;">
                    <button class="btn-react" data-id="${ann.id}" data-type="thumbsUp" style="background: none; border: none; font-size: 0.72rem; cursor: pointer; color: var(--text-muted); display: inline-flex; align-items: center; gap: 4px; padding: 2px 6px; border-radius: 4px; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.04)'; this.style.color='var(--text-primary)';" onmouseout="this.style.background='none'; this.style.color='var(--text-muted)';">
                      👍 <span style="font-weight: 600; font-size: 0.65rem;">${reactions.thumbsUp || 0}</span>
                    </button>
                    <button class="btn-react" data-id="${ann.id}" data-type="heart" style="background: none; border: none; font-size: 0.72rem; cursor: pointer; color: var(--text-muted); display: inline-flex; align-items: center; gap: 4px; padding: 2px 6px; border-radius: 4px; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.04)'; this.style.color='var(--text-primary)';" onmouseout="this.style.background='none'; this.style.color='var(--text-muted)';">
                      ❤️ <span style="font-weight: 600; font-size: 0.65rem;">${reactions.heart || 0}</span>
                    </button>
                    <button class="btn-react" data-id="${ann.id}" data-type="lightbulb" style="background: none; border: none; font-size: 0.72rem; cursor: pointer; color: var(--text-muted); display: inline-flex; align-items: center; gap: 4px; padding: 2px 6px; border-radius: 4px; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.04)'; this.style.color='var(--text-primary)';" onmouseout="this.style.background='none'; this.style.color='var(--text-muted)';">
                      💡 <span style="font-weight: 600; font-size: 0.65rem;">${reactions.lightbulb || 0}</span>
                    </button>
                    <button class="btn-react" data-id="${ann.id}" data-type="coffee" style="background: none; border: none; font-size: 0.72rem; cursor: pointer; color: var(--text-muted); display: inline-flex; align-items: center; gap: 4px; padding: 2px 6px; border-radius: 4px; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.04)'; this.style.color='var(--text-primary)';" onmouseout="this.style.background='none'; this.style.color='var(--text-muted)';">
                      ☕ <span style="font-weight: 600; font-size: 0.65rem;">${reactions.coffee || 0}</span>
                    </button>
                  </div>
                </div>
              `;
            }).join('')}
          </div>
        </div>

      </div>

      <!-- Announcements Modal Mount Point Overlay -->
      <div id="ann-modal-overlay" style="position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.65); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 10000; padding: var(--spacing-md);">
        <div id="ann-modal-content" class="card glass animate-slide-up" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); width: 100%; max-width: 500px; padding: var(--spacing-lg); max-height: 90vh; overflow-y: auto; text-align: left;">
          <!-- Modal content injected here -->
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  /**
   * Binds user events
   */
  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#ann-modal-overlay');
    const modalContent = container.querySelector('#ann-modal-content');

    const showModal = (htmlContent) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = htmlContent;
        overlay.style.display = 'flex';
        
        const closeBtn = modalContent.querySelector('.btn-close-modal');
        if (closeBtn) {
          closeBtn.addEventListener('click', () => hideModal());
        }
        if (window.lucide) window.lucide.createIcons();
      }
    };

    const hideModal = () => {
      if (overlay) {
        overlay.style.display = 'none';
      }
    };

    if (overlay) {
      overlay.addEventListener('click', (e) => {
        if (e.target === overlay) hideModal();
      });
    }

    // 1. Click Announcement to View Details & Mark Read
    const annCards = container.querySelectorAll('.ann-card');
    annCards.forEach(card => {
      card.addEventListener('click', async () => {
        const id = parseInt(card.getAttribute('data-id'));
        const ann = this.announcementsList.find(a => a.id === id);
        if (!ann) return;

        // Mark as Read in DB
        try {
          await apiClient.post(`/api/v1/announcements/${ann.id}/read`);
          ann.read = true;
        } catch (e) {
          logger.error('StoreEmployeeAnnouncements', 'Error marking read on modal view:', e);
        }

        const pColor = ann.priority === 'Critical Alert' ? 'var(--status-danger)' : 
                       (ann.priority === 'Standard Notice' ? 'var(--status-info)' : 'var(--text-muted)');

        const detailHtml = `
          <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1.5px solid var(--accent-primary); padding-bottom:var(--spacing-xs); margin-bottom:var(--spacing-md);">
            <h3 style="margin:0; font-weight:800; color:var(--accent-primary); font-family:var(--font-display);">Bulletin circular</h3>
            <button class="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer;"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div style="font-size:0.75rem; color:var(--text-primary); display:flex; flex-direction:column; gap:12px;">
            <div style="display:flex; justify-content:space-between; align-items:center;">
              <span style="font-size:0.6rem; font-weight:700; color:${pColor}; text-transform:uppercase;">${ann.priority}</span>
              <span style="font-size:0.65rem; color:var(--text-muted);">${ann.date}</span>
            </div>
            
            <h4 style="margin:0; font-size:1.15rem; font-weight:800; color:var(--text-primary);">${ann.title}</h4>
            <p style="margin:0; background:rgba(0,0,0,0.15); padding:var(--spacing-md); border-radius:var(--radius-md); line-height:1.45; color:var(--text-muted);">${ann.content}</p>
            
            <div style="display:flex; justify-content:space-between; font-size:0.68rem; color:var(--text-muted); border-top:1px solid rgba(255,255,255,0.05); padding-top:10px;">
              <span>Publisher: <strong>${ann.publisher}</strong></span>
              <span>Status: <strong style="color:var(--status-success);">Read</strong></span>
            </div>
            
            <button class="btn btn-close-modal" style="background:var(--accent-primary); color:#000; font-weight:800; width:100%; margin-top:4px;">
              Acknowledge Receipt
            </button>
          </div>
        `;
        showModal(detailHtml);

        // Bind inner closing btn
        const innerClose = modalContent.querySelector('.btn-close-modal');
        if (innerClose) {
          innerClose.addEventListener('click', () => {
            hideModal();
            this.render(container);
            this.bindEvents(container, lifecycle);
          });
        }
      });
    });

    // Readed button click (stops card details modal from opening)
    const readCardBtns = container.querySelectorAll('.btn-mark-read-card');
    readCardBtns.forEach(btn => {
      btn.addEventListener('click', async (e) => {
        e.stopPropagation();
        const id = parseInt(btn.getAttribute('data-id'));
        const ann = this.announcementsList.find(a => a.id === id);
        if (ann) {
          try {
            await apiClient.post(`/api/v1/announcements/${ann.id}/read`);
            ann.read = true;
            notificationStore.success('Announcement marked as readed.');
            this.render(container);
            this.bindEvents(container, lifecycle);
          } catch (err) {
            logger.error('StoreEmployeeAnnouncements', 'Error marking read:', err);
            notificationStore.danger('Failed to mark read in database.');
          }
        }
      });
    });

    // Reaction button click (stops card details modal from opening)
    const reactBtns = container.querySelectorAll('.btn-react');
    reactBtns.forEach(btn => {
      btn.addEventListener('click', async (e) => {
        e.stopPropagation();
        const id = parseInt(btn.getAttribute('data-id'));
        const type = btn.getAttribute('data-type');
        const ann = this.announcementsList.find(a => a.id === id);
        if (ann) {
          try {
            await apiClient.post(`/api/v1/announcements/${id}/react?type=${type}`);
            
            // Increment local visual count or toggle
            if (!ann.reactions) {
              ann.reactions = { thumbsUp: 0, heart: 0, lightbulb: 0, coffee: 0 };
            }
            // Toggle representation locally to avoid full fetch delay
            ann.reactions[type] = (ann.reactions[type] || 0) + 1;
            
            // Refresh feed from database to fetch precise state
            await this.fetchAnnouncements();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } catch (err) {
            logger.error('StoreEmployeeAnnouncements', 'Reaction failed:', err);
          }
        }
      });
    });

    // 2. Mark All as Read
    const markAllBtn = container.querySelector('#btn-mark-all-read');
    if (markAllBtn) {
      markAllBtn.addEventListener('click', async () => {
        if (markAllBtn) {
          markAllBtn.disabled = true;
          markAllBtn.textContent = 'Processing...';
        }
        try {
          for (let ann of this.announcementsList) {
            if (!ann.read) {
              await apiClient.post(`/api/v1/announcements/${ann.id}/read`);
            }
          }
          notificationStore.success('All bulletins marked as read.');
          await this.fetchAnnouncements();
          this.render(container);
          this.bindEvents(container, lifecycle);
        } catch (err) {
          logger.error('StoreEmployeeAnnouncements', 'Error marking all as read:', err);
        } finally {
          if (markAllBtn) {
            markAllBtn.disabled = false;
            markAllBtn.textContent = 'Mark all as Read';
          }
        }
      });
    }

    // 4. Filters click
    const filterBtns = container.querySelectorAll('[data-filter]');
    filterBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        this.filterStatus = btn.getAttribute('data-filter');
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    });
  }
}
