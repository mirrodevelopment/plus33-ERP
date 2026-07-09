/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/profile/page.js
 * Purpose           : Frontend page component for User Profile settings page
 * Version           : 0.0.2-SNAPSHOT
 *
 * Related API       : POST /api/upload-avatar
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : store/authStore, store/userStore, store/notificationStore, core/logger
 * Depends On        : store/authStore, store/userStore, store/notificationStore, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * User profile dashboard workspace. Part of the PLUS33 Coffee ERP vanilla JS SPA.
 * Allows viewing and editing user credentials, displaying avatars, selecting from
 * presets, or uploading local system images reactively.
 ******************************************************************************/

import { authStore } from '../../store/authStore.js';
import { userStore } from '../../store/userStore.js';
import { notificationStore } from '../../store/notificationStore.js';
import { logger } from '../../core/logger.js';
import { apiClient } from '../../api/client.js';

export default class ProfilePage {
  /**
   * Instantiates the ProfilePage class.
   * @memberof Pages Module
   */
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role);
    this.selectedFile = null;
    this.isEditing = false;
  }

  /**
   * Mounts the page layout inside the main application viewport.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('ProfilePage', 'Mounting user profile settings page...');
    this.user = authStore.getUser();
    try {
      const response = await apiClient.get('/api/v1/auth/me');
      if (response && response.success && response.data) {
        this.profile = response.data;
        userStore.updateProfile(this.user?.role, response.data);
      }
    } catch (e) {
      logger.error('ProfilePage', 'Error loading database profile:', e);
    }
    this.render(container);
    this.bindEvents(container);
  }

  /**
   * Renders the glassmorphism profile interface markup.
   * @memberof Pages Module
   */
  render(container) {
    this.user = authStore.getUser();
    if (!this.profile || !this.profile.storeRegion) {
      this.profile = userStore.getProfile(this.user?.role);
    }
    
    // Check if current avatar is custom
    const isPreset = this.profile.avatarUrl.includes('unsplash.com') || 
                     this.profile.avatarUrl === 'imgs/male-avatar.png' || 
                     this.profile.avatarUrl === 'imgs/female-avatar.jpg' || 
                     this.profile.avatarUrl === '';

    const isEmployee = true; // Enabled for all profiles
    
    let verificationDocsHtml = '';
    if (isEmployee) {
      // Load verification documents state from localStorage
      const docsKey = `plus33_user_verification_docs_${this.user?.username || 'user'}`;
      let docs = { panCard: null, aadhaarCard: null, workPermit: null };
      try {
        const cachedDocs = localStorage.getItem(docsKey);
        if (cachedDocs) {
          docs = JSON.parse(cachedDocs);
        }
      } catch (e) {
        logger.error('ProfilePage', 'Error reading verification documents state', e);
      }
      
      const renderDocRow = (type, title, desc, iconSvg) => {
        const docData = docs[type];
        
        let badgeText = 'Not Uploaded';
        let badgeStyle = 'background: rgba(235, 94, 85, 0.1); border: 1px solid rgba(235, 94, 85, 0.2); color: #ff6b6b;';
        if (docData) {
          badgeText = 'Pending Verification';
          badgeStyle = 'background: rgba(201, 164, 106, 0.15); border: 1px solid rgba(201, 164, 106, 0.3); color: var(--accent-primary);';
        }
        
        return `
          <div class="doc-row" style="background: rgba(255,255,255,0.02); border: 1px solid var(--border-color); padding: var(--spacing-md); border-radius: var(--radius-md); display: flex; flex-direction: column; gap: var(--spacing-sm);">
            <div style="display: flex; align-items: center; justify-content: space-between; gap: var(--spacing-md); flex-wrap: wrap;">
              <div style="display: flex; align-items: center; gap: var(--spacing-md);">
                <div style="background: rgba(255,255,255,0.04); width: 40px; height: 40px; border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; color: var(--text-secondary); border: 1px solid rgba(255,255,255,0.05); flex-shrink: 0;">
                  ${iconSvg}
                </div>
                <div>
                  <h4 style="font-weight: 700; font-size: 0.85rem; color: var(--text-primary); margin: 0;">${title}</h4>
                  <p style="font-size: 0.7rem; color: var(--text-muted); margin: 2px 0 0 0;">${desc}</p>
                </div>
              </div>
              
              <div style="display: flex; align-items: center; gap: var(--spacing-md); flex-wrap: wrap;">
                <!-- Status Badge -->
                <span style="font-size: 0.65rem; font-weight: 700; text-transform: uppercase; padding: 4px 10px; border-radius: var(--radius-md); ${badgeStyle}">
                  ${badgeText}
                </span>
                
                <!-- Action Buttons -->
                <div style="display: flex; align-items: center; gap: 8px;">
                  <!-- Upload button -->
                  <input type="file" id="input-doc-${type}" accept=".pdf,image/*" style="display: none;">
                  <button type="button" class="btn btn-upload-doc" data-type="${type}" style="background: rgba(255,255,255,0.05); border: 1px solid var(--border-color); color: var(--text-primary); font-weight: 700; font-size: 0.72rem; padding: 6px 12px; border-radius: var(--radius-md); cursor: pointer; display: ${docData ? 'none' : 'block'}; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.1)';" onmouseout="this.style.background='rgba(255,255,255,0.05)';">
                    Upload File
                  </button>
                  
                  <!-- Action items for uploaded document -->
                  ${docData ? `
                    <a href="${docData.url}" target="_blank" class="btn" style="background: rgba(201,164,106,0.1); border: 1px solid rgba(201,164,106,0.3); color: var(--accent-primary); font-size: 0.72rem; padding: 6px 12px; border-radius: var(--radius-md); text-decoration: none; display: inline-flex; align-items: center; gap: 4px; font-weight: 600; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(201,164,106,0.2)';" onmouseout="this.style.background='rgba(201,164,106,0.1)';">
                      <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                      View Doc
                    </a>
                    <button type="button" class="btn btn-delete-doc" data-type="${type}" style="background: rgba(235, 94, 85, 0.1); border: 1px solid rgba(235, 94, 85, 0.2); color: #ff6b6b; font-size: 0.72rem; padding: 6px 10px; border-radius: var(--radius-md); cursor: pointer; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(235, 94, 85, 0.2)';" onmouseout="this.style.background='rgba(235, 94, 85, 0.1)';">
                      <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
                    </button>
                  ` : ''}
                </div>
              </div>
            </div>

            <!-- Upload progress bar container -->
            <div id="progress-container-${type}" style="display: none; width: 100%; margin-top: 4px;">
              <div style="display: flex; justify-content: space-between; font-size: 0.65rem; color: var(--text-muted); margin-bottom: 2px;">
                <span id="progress-text-${type}">Uploading...</span>
                <span id="progress-pct-${type}">0%</span>
              </div>
              <div style="background: rgba(255,255,255,0.05); height: 4px; border-radius: 2px; width: 100%; overflow: hidden;">
                <div id="progress-bar-${type}" style="background: var(--accent-primary); width: 0%; height: 100%; transition: width 0.1s linear;"></div>
              </div>
            </div>

            <!-- Uploaded File info metadata -->
            ${docData ? `
              <div style="background: rgba(0,0,0,0.15); border-radius: var(--radius-md); padding: 8px 12px; display: flex; align-items: center; justify-content: space-between; font-size: 0.7rem; color: var(--text-secondary); margin-top: 4px; border-left: 2px solid var(--accent-primary);">
                <div style="display: flex; align-items: center; gap: 6px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 70%;">
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--text-muted); flex-shrink: 0;"><path d="M21.44 11.05l-9.19 9.19a6 6 0 0 1-8.49-8.49l9.19-9.19a4 4 0 0 1 5.66 5.66l-9.2 9.19a2 2 0 0 1-2.83-2.83l8.49-8.48"/></svg>
                  <span style="font-weight: 500; overflow: hidden; text-overflow: ellipsis;">${docData.name}</span>
                </div>
                <span style="font-size: 0.65rem; color: var(--text-muted); font-family: monospace;">Uploaded: ${docData.uploadedAt}</span>
              </div>
            ` : ''}
          </div>
        `;
      };
      
      verificationDocsHtml = `
        <!-- Work Verification Documents Card -->
        <div class="card glass" style="padding: var(--spacing-xl); border: 1px solid var(--border-color); background: var(--bg-card); border-radius: var(--radius-lg); text-align: left;">
          <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm); margin-bottom: var(--spacing-md); display: flex; align-items: center; justify-content: space-between;">
            <div>
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; color: var(--text-primary); margin: 0; display: flex; align-items: center; gap: 8px;">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/><polyline points="14 2 14 8 20 8"/></svg>
                Work Verification Documents
              </h3>
              <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase;">Onboarding, Identity and Work Authorization Records</span>
            </div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--accent-primary); background: rgba(201,164,106,0.1); padding: 4px 10px; border-radius: 12px; display: flex; align-items: center; gap: 4px;">
              <span style="width: 6px; height: 6px; border-radius: 50%; background: var(--accent-primary); display: inline-block;"></span>
              Required
            </span>
          </div>

          <div style="display: flex; flex-direction: column; gap: var(--spacing-md);">
            ${renderDocRow('panCard', 'PAN Card (Permanent Account Number)', 'Upload your 10-digit alphanumeric IT Department-issued card.', `
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><rect x="3" y="4" width="18" height="16" rx="2" ry="2"/><line x1="7" y1="8" x2="17" y2="8"/><line x1="7" y1="12" x2="17" y2="12"/><line x1="7" y1="16" x2="13" y2="16"/></svg>
            `)}
            
            ${renderDocRow('aadhaarCard', 'Aadhaar Card (UIDAI)', 'Upload your 12-digit unique identity card issued by UIDAI.', `
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
            `)}
            
            ${renderDocRow('workPermit', 'Work Permit / Contract Agreement', 'Upload your signed employment contract or work visa document.', `
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
            `)}
          </div>
        </div>
      `;
    }
    
    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="max-width: 800px; margin: 0 auto; padding: var(--spacing-xl) var(--spacing-lg);">
        
        <!-- Header -->
        <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-md); margin-bottom: var(--spacing-xl); text-align: left;">
          <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.6rem; color: var(--text-primary); margin: 0;">My Profile</h2>
          <span style="font-size: 0.72rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Manage your enterprise user credentials and configuration</span>
        </div>

        <div style="display: grid; grid-template-columns: 1fr; gap: var(--spacing-xl);">
          
          <!-- Profile Card -->
          <div class="card glass flex flex-col align-center text-center" style="padding: var(--spacing-xl); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; align-items: center; justify-content: center; gap: var(--spacing-md); border-radius: var(--radius-lg);">
            <div style="position: relative;">
              <img id="profile-card-image" src="${this.profile.avatarUrl || 'imgs/male-avatar.png'}" alt="${this.profile.name}" style="width: 110px; height: 110px; border-radius: 50%; object-fit: cover; border: 2.5px solid var(--accent-primary); box-shadow: var(--shadow-lg);">
              <div style="position: absolute; bottom: 2px; right: 2px; width: 14px; height: 14px; border-radius: 50%; background: var(--status-success); border: 2px solid var(--bg-card);"></div>
            </div>
            
            <div style="margin-top: var(--spacing-xs);">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.25rem; color: var(--text-primary); margin: 0 0 4px 0;">${this.profile.name}</h3>
              ${this.profile.store ? `
                <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; margin-bottom: 8px; display: flex; align-items: center; justify-content: center; gap: 4px;">
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: var(--accent-primary);"><path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/><circle cx="12" cy="10" r="3"/></svg>
                  ${this.profile.store}
                </div>
              ` : ''}
              <span style="font-size: 0.72rem; font-weight: 700; color: var(--accent-primary); text-transform: uppercase; letter-spacing: 0.5px; background: rgba(201,164,106,0.1); padding: 4px 10px; border-radius: 12px;">${this.user?.role || 'Guest'}</span>
            </div>

            <!-- Profile Info Grid -->
            <div style="width: 100%; border-top: 1px solid rgba(255,255,255,0.05); margin-top: var(--spacing-sm); padding-top: var(--spacing-md); display: grid; grid-template-columns: 1fr 1fr; gap: var(--spacing-md); text-align: left;">
              <div>
                <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block;">Department</span>
                <span style="font-size: 0.85rem; color: var(--text-primary); font-weight: 600;">${this.profile.department}</span>
              </div>
              <div>
                <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block;">Joined Date</span>
                <span style="font-size: 0.85rem; color: var(--text-primary); font-weight: 600;">${this.profile.joinedDate}</span>
              </div>
              <div>
                <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block;">Mobile Number</span>
                <span style="font-size: 0.85rem; color: var(--text-primary); font-weight: 600;">${this.profile.phone || 'Not set'}</span>
              </div>
              <div>
                <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block;">Gender</span>
                <span style="font-size: 0.85rem; color: var(--text-primary); font-weight: 600;">${this.profile.gender || 'Not set'}</span>
              </div>
              ${this.profile.store ? `
              <div>
                <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block;">Assigned Store</span>
                <span style="font-size: 0.85rem; color: var(--text-primary); font-weight: 600;">${this.profile.store}</span>
              </div>
              ` : ''}
              ${this.profile.storeRegion ? `
              <div>
                <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block;">Store Region</span>
                <span style="font-size: 0.85rem; color: var(--text-primary); font-weight: 600;">${this.profile.storeRegion}</span>
              </div>
              ` : ''}
              ${this.profile.country ? `
              <div>
                <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase; display: block;">Country</span>
                <span style="font-size: 0.85rem; color: var(--text-primary); font-weight: 600;">${this.profile.country}</span>
              </div>
              ` : ''}
            </div>

            <button type="button" id="btn-toggle-edit-mode" class="btn" style="background: ${this.isEditing ? 'rgba(235, 94, 85, 0.15)' : 'rgba(255, 255, 255, 0.05)'}; border: 1px solid ${this.isEditing ? 'rgba(235, 94, 85, 0.3)' : 'var(--border-color)'}; color: ${this.isEditing ? '#ff6b6b' : 'var(--text-primary)'}; font-weight: 700; font-size: 0.8rem; padding: 8px 16px; border-radius: var(--radius-md); cursor: pointer; transition: var(--transition-fast); margin-top: var(--spacing-sm); width: 100%; display: flex; align-items: center; justify-content: center; gap: 8px;" onmouseover="this.style.opacity='0.85';" onmouseout="this.style.opacity='1';">
              ${this.isEditing ? '❌ Close Edit Profile' : '✏️ Edit Profile Details'}
            </button>
          </div>

          <!-- Edit Profile Form -->
          <div id="edit-profile-card" class="card glass" style="display: ${this.isEditing ? 'block' : 'none'}; padding: var(--spacing-xl); border: 1px solid var(--border-color); background: var(--bg-card); border-radius: var(--radius-lg); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm); margin-bottom: var(--spacing-md);">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; color: var(--text-primary); margin: 0;">Edit Profile Details</h3>
              <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase;">Update your credentials shown across the platform</span>
            </div>

            <form id="form-edit-profile" class="flex flex-col gap-md" style="display: flex; flex-direction: column; gap: var(--spacing-md);">
              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Full Name</label>
                <input type="text" id="input-profile-name" value="${this.profile.name}" required style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Email Address</label>
                <input type="email" id="input-profile-email" value="${this.profile.email}" required style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Mobile Number</label>
                <input type="text" id="input-profile-phone" value="${this.profile.phone || ''}" placeholder="Enter mobile number" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Gender</label>
                <select id="select-profile-gender" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; cursor: pointer; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
                  <option value="Male" ${this.profile.gender === 'Male' ? 'selected' : ''}>Male</option>
                  <option value="Female" ${this.profile.gender === 'Female' ? 'selected' : ''}>Female</option>
                  <option value="Other" ${this.profile.gender === 'Other' ? 'selected' : ''}>Other</option>
                </select>
              </div>

              <!-- Avatar Dropdown Selector -->
              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Select Profile Avatar</label>
                <select id="select-profile-avatar-type" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; cursor: pointer; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
                  <option value="imgs/male-avatar.png" ${this.profile.avatarUrl === 'imgs/male-avatar.png' ? 'selected' : ''}>Preset: Default Male Avatar</option>
                  <option value="imgs/female-avatar.jpg" ${this.profile.avatarUrl === 'imgs/female-avatar.jpg' ? 'selected' : ''}>Preset: Default Female Avatar</option>
                  <option value="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80" ${this.profile.avatarUrl.includes('photo-1534528741775') ? 'selected' : ''}>Preset: Executive Avatar</option>
                  <option value="custom" ${(!isPreset) ? 'selected' : ''}>Upload Custom Avatar Image...</option>
                </select>
              </div>

              <!-- Custom Avatar File Upload Field (Visible only if 'custom' is selected) -->
              <div id="custom-avatar-upload-group" style="display: ${(!isPreset) ? 'flex' : 'none'}; flex-direction: column; gap: 8px; border: 1px dashed var(--border-color); padding: var(--spacing-md); border-radius: var(--radius-md); background: rgba(0,0,0,0.15);">
                <div>
                  <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; display: block; margin-bottom: 6px;">Choose Image File from System</label>
                  <div style="display: flex; align-items: center; gap: var(--spacing-sm);">
                    <input type="file" id="input-profile-file" accept="image/*" style="display: none;">
                    <button type="button" id="btn-trigger-file" class="btn" style="background: rgba(255,255,255,0.05); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.75rem; padding: 6px 12px; border-radius: var(--radius-md); cursor: pointer; transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.1)';" onmouseout="this.style.background='rgba(255,255,255,0.05)';">
                      Select Image File
                    </button>
                    <span id="label-file-name" style="font-size: 0.72rem; color: var(--text-muted); max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                      ${this.profile.avatarUrl.startsWith('imgs/avatars/') ? this.profile.avatarUrl.split('/').pop() : 'No file chosen'}
                    </span>
                  </div>
                </div>
                
                <!-- Display manual text input fallback if needed -->
                <div style="margin-top: 4px; border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm);">
                  <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; display: block; margin-bottom: 4px;">Or Enter Custom Image URL</label>
                  <input type="url" id="input-profile-avatar-custom-url" value="${(!isPreset) ? this.profile.avatarUrl : ''}" placeholder="http://example.com/avatar.png" style="padding: 8px 10px; width: 100%; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.78rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
                </div>
              </div>

              <button type="submit" id="btn-save-profile" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 20px; border-radius: var(--radius-md); border: none; cursor: pointer; align-self: flex-start; transition: var(--transition-fast); margin-top: var(--spacing-sm);" onmouseover="this.style.opacity='0.9';" onmouseout="this.style.opacity='1';">
                Save Profile Changes
              </button>
            </form>
          </div>

          ${verificationDocsHtml}

          <!-- Change Password Card -->
          <div class="card glass" style="padding: var(--spacing-xl); border: 1px solid var(--border-color); background: var(--bg-card); border-radius: var(--radius-lg); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm); margin-bottom: var(--spacing-md);">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; color: var(--text-primary); margin: 0;">Change Account Password</h3>
              <span style="font-size: 0.65rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase;">Update your security credentials for authentication</span>
            </div>

            <form id="form-change-password" class="flex flex-col gap-md" style="display: flex; flex-direction: column; gap: var(--spacing-md);">
              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Current Password</label>
                <input type="password" id="input-password-current" required placeholder="Enter current password" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">New Password</label>
                <input type="password" id="input-password-new" required placeholder="Minimum 6 characters" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <div class="form-group flex flex-col gap-xs" style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Confirm New Password</label>
                <input type="password" id="input-password-confirm" required placeholder="Confirm new password" style="padding: 10px 12px; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.82rem; outline: none; transition: var(--transition-fast);" onfocus="this.style.borderColor='var(--accent-primary)';" onblur="this.style.borderColor='var(--border-color)';">
              </div>

              <button type="submit" id="btn-submit-password" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 20px; border-radius: var(--radius-md); border: none; cursor: pointer; align-self: flex-start; transition: var(--transition-fast); margin-top: var(--spacing-sm);" onmouseover="this.style.opacity='0.9';" onmouseout="this.style.opacity='1';">
                Update Security Password
              </button>
            </form>
          </div>

        </div>
      </div>
    `;
  }

  /**
   * Attaches submit handlers to the profile edit form.
   * @memberof Pages Module
   */
  bindEvents(container) {
    const dropdown = container.querySelector('#select-profile-avatar-type');
    const uploadGroup = container.querySelector('#custom-avatar-upload-group');
    const fileTrigger = container.querySelector('#btn-trigger-file');
    const fileInput = container.querySelector('#input-profile-file');
    const fileNameLabel = container.querySelector('#label-file-name');
    const form = container.querySelector('#form-edit-profile');

    // 1. Toggle custom file upload container visibility
    if (dropdown && uploadGroup) {
      dropdown.addEventListener('change', () => {
        if (dropdown.value === 'custom') {
          uploadGroup.style.display = 'flex';
        } else {
          uploadGroup.style.display = 'none';
        }
      });
    }

    // 2. Trigger hidden file input click
    if (fileTrigger && fileInput) {
      fileTrigger.addEventListener('click', () => {
        fileInput.click();
      });
    }

    // 3. Update filename label and show preview on file selection
    if (fileInput && fileNameLabel) {
      fileInput.addEventListener('change', () => {
        const file = fileInput.files[0];
        if (file) {
          this.selectedFile = file;
          fileNameLabel.textContent = file.name;
          
          // Show quick local base64 preview of selected image in card
          const reader = new FileReader();
          reader.onload = (e) => {
            const previewImg = container.querySelector('#profile-card-image');
            if (previewImg) {
              previewImg.src = e.target.result;
            }
          };
          reader.readAsDataURL(file);
        }
      });
    }

    // 4. Save profile edits
    if (form) {
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const saveButton = container.querySelector('#btn-save-profile');
        if (saveButton) {
          saveButton.disabled = true;
          saveButton.textContent = 'Saving Changes...';
        }

        const name = container.querySelector('#input-profile-name').value.trim();
        const email = container.querySelector('#input-profile-email').value.trim();
        const phone = container.querySelector('#input-profile-phone').value.trim();
        const gender = container.querySelector('#select-profile-gender').value;
        const avatarType = dropdown.value;
        let avatarUrl = '';

        if (!name || !email) {
          notificationStore.danger('Name and Email fields are required.');
          if (saveButton) {
            saveButton.disabled = false;
            saveButton.textContent = 'Save Profile Changes';
          }
          return;
        }

        try {
          if (avatarType !== 'custom') {
            avatarUrl = avatarType;
          } else {
            // Upload selected file to WebServer
            if (this.selectedFile) {
              const response = await fetch('/api/upload-avatar', {
                method: 'POST',
                headers: {
                  'Content-Type': this.selectedFile.type,
                  'X-Username': name,
                  'X-Role': this.user.role
                },
                body: this.selectedFile
              });
              
              const data = await response.json();
              if (data.success && data.url) {
                avatarUrl = data.url;
              } else {
                throw new Error(data.message || 'File upload failed on server.');
              }
            } else {
              // Fallback to manual text input value
              avatarUrl = container.querySelector('#input-profile-avatar-custom-url').value.trim();
              if (!avatarUrl) {
                avatarUrl = this.profile.avatarUrl; // Keep original
              }
            }
          }

          // Update user details in database
          const updateResponse = await apiClient.put('/api/v1/auth/me', { name, email, avatarUrl, phone, gender });
          if (updateResponse && updateResponse.success && updateResponse.data) {
            this.profile = updateResponse.data;
            userStore.updateProfile(this.user.role, updateResponse.data);
            notificationStore.success('Profile changes saved successfully to database!');
          } else {
            throw new Error(updateResponse?.message || 'Database update failed.');
          }
          
          this.selectedFile = null;
          this.isEditing = false;
          this.render(container);
          this.bindEvents(container);
        } catch (err) {
          logger.error('ProfilePage', 'Failed to update user profile:', err);
          notificationStore.danger(`Failed to save profile: ${err.message}`);
          if (saveButton) {
            saveButton.disabled = false;
            saveButton.textContent = 'Save Profile Changes';
          }
        }
      });
    }

    // 4b. Toggle Edit Mode Handler
    const toggleEditBtn = container.querySelector('#btn-toggle-edit-mode');
    if (toggleEditBtn) {
      toggleEditBtn.addEventListener('click', () => {
        this.isEditing = !this.isEditing;
        this.render(container);
        this.bindEvents(container);
        if (this.isEditing) {
          const editCard = container.querySelector('#edit-profile-card');
          if (editCard) editCard.scrollIntoView({ behavior: 'smooth' });
        }
      });
    }

    // 5. Change Password Form Submit Handler
    const passwordForm = container.querySelector('#form-change-password');
    if (passwordForm) {
      passwordForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const currentPassword = container.querySelector('#input-password-current').value;
        const newPassword = container.querySelector('#input-password-new').value;
        const confirmPassword = container.querySelector('#input-password-confirm').value;
        const submitBtn = container.querySelector('#btn-submit-password');

        if (newPassword.length < 6) {
          notificationStore.danger('New password must be at least 6 characters long.');
          return;
        }

        if (newPassword !== confirmPassword) {
          notificationStore.danger('New password and confirmation password do not match.');
          return;
        }

        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.textContent = 'Updating Password...';
        }

        try {
          const response = await apiClient.put('/api/v1/auth/change-password', {
            currentPassword,
            newPassword
          });

          if (response && response.success) {
            notificationStore.success('Account password updated successfully!');
            passwordForm.reset();
          } else {
            throw new Error(response.message || 'Incorrect current password or invalid request');
          }
        } catch (err) {
          logger.error('ProfilePage', 'Failed to update password:', err);
          notificationStore.danger(`Password change failed: ${err.message}`);
        } finally {
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Update Security Password';
          }
        }
      });
    }

    // 6. Onboarding Documents Upload and Delete handlers
    const docUploadButtons = container.querySelectorAll('.btn-upload-doc');
    docUploadButtons.forEach(btn => {
      const type = btn.getAttribute('data-type');
      const fileInput = container.querySelector(`#input-doc-${type}`);
      
      btn.addEventListener('click', () => {
        if (fileInput) fileInput.click();
      });
      
      if (fileInput) {
        fileInput.addEventListener('change', async () => {
          const file = fileInput.files[0];
          if (!file) return;
          
          // Show progress bar
          const progContainer = container.querySelector(`#progress-container-${type}`);
          const progBar = container.querySelector(`#progress-bar-${type}`);
          const progText = container.querySelector(`#progress-text-${type}`);
          const progPct = container.querySelector(`#progress-pct-${type}`);
          
          if (progContainer) progContainer.style.display = 'block';
          if (progText) progText.textContent = 'Uploading...';
          
          // Progress animation simulation before real server call
          let pct = 0;
          const interval = setInterval(() => {
            pct += 10;
            if (pct > 90) clearInterval(interval);
            if (progBar) progBar.style.width = `${pct}%`;
            if (progPct) progPct.textContent = `${pct}%`;
          }, 100);
          
          try {
            // Perform actual API upload using existing upload endpoint
            const response = await fetch('/api/upload-avatar', {
              method: 'POST',
              headers: {
                'Content-Type': file.type,
                'X-Username': this.profile.name,
                'X-Role': this.user.role
              },
              body: file
            });
            
            clearInterval(interval);
            
            if (progBar) progBar.style.width = '100%';
            if (progPct) progPct.textContent = '100%';
            if (progText) progText.textContent = 'Completing upload...';
            
            const data = await response.json();
            if (data.success && data.url) {
              // Update local storage verification status
              const docsKey = `plus33_user_verification_docs_${this.user?.username || 'user'}`;
              const cachedDocs = localStorage.getItem(docsKey);
              let currentDocs = { panCard: null, aadhaarCard: null, workPermit: null };
              if (cachedDocs) {
                try { currentDocs = JSON.parse(cachedDocs); } catch(e) {}
              }
              
              currentDocs[type] = {
                name: file.name,
                url: data.url,
                uploadedAt: new Date().toLocaleDateString() + ' ' + new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
              };
              
              localStorage.setItem(docsKey, JSON.stringify(currentDocs));
              notificationStore.success(`${file.name} uploaded successfully!`);
              
              // Re-render and re-bind
              this.render(container);
              this.bindEvents(container);
            } else {
              throw new Error(data.message || 'Server upload failed.');
            }
          } catch (err) {
            clearInterval(interval);
            if (progContainer) progContainer.style.display = 'none';
            logger.error('ProfilePage', 'Verification document upload failed:', err);
            notificationStore.danger(`Upload failed: ${err.message}`);
          }
        });
      }
    });
    
    // Delete document handler
    const deleteDocButtons = container.querySelectorAll('.btn-delete-doc');
    deleteDocButtons.forEach(btn => {
      const type = btn.getAttribute('data-type');
      btn.addEventListener('click', () => {
        const docsKey = `plus33_user_verification_docs_${this.user?.username || 'user'}`;
        const cachedDocs = localStorage.getItem(docsKey);
        let currentDocs = { panCard: null, aadhaarCard: null, workPermit: null };
        if (cachedDocs) {
          try { currentDocs = JSON.parse(cachedDocs); } catch(e) {}
        }
        
        currentDocs[type] = null;
        localStorage.setItem(docsKey, JSON.stringify(currentDocs));
        notificationStore.success('Document deleted successfully.');
        
        this.render(container);
        this.bindEvents(container);
      });
    });
  }
}



