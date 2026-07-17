import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

const TEMPLATE_URL = 'modules/store-employee/pages/profile-employee/profile.html';

export default class ProfilePage {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.docs = {
      personalPhoto: null,
      personalId: null,
      personalAddress: null,
      educationSchool: null,
      educationCollege: null,
      educationCertificates: null,
      employmentResume: null,
      employmentOffer: null,
      employmentExperience: null,
      bankingAccount: null,
      bankingCheque: null,
      taxPan: null,
      taxForms: null,
      socialSecurityPf: null,
      socialSecurityEsi: null,
      socialSecurity: null,
      medicalFitness: null,
      medicalVaccination: null,
      medicalFoodHandler: null,
      legalNda: null,
      legalAgreement: null,
      legalBackground: null,
      immigrationPassport: null,
      immigrationVisa: null,
      immigrationPermit: null,
      drivingLicense: null,
      drivingCommercial: null,
      emergencyContact: null
    };
    this.selectedFile = null;
    this.isEditing = false;
    this.payslipLoaded = false;
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeProfile', 'Mounting store employee profile page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML template layout
    await htmlLoader.inject(TEMPLATE_URL, container);

    // 2. Load profile and document data from server
    await this.loadProfileData();
    await this.loadDocumentData();
    await this.loadDocumentRequirements();

    // 3. Populate data and bind events
    this.render(container);
    this.bindEvents(container, lifecycle);

    // 4. Init tab navigation
    this._initTabs(container);
  }

  async loadProfileData() {
    this.user = authStore.getUser();
    try {
      const response = await apiClient.get('/api/v1/auth/me');
      if (response && response.success && response.data) {
        this.profile = response.data;
        userStore.updateProfile(this.user?.role, response.data);
      }
    } catch (e) {
      logger.error('StoreEmployeeProfile', 'Error loading database profile:', e);
    }
  }

  async loadDocumentData() {
    try {
      const docResponse = await apiClient.get('/api/v2/employee-self-service/documents');
      if (docResponse && docResponse.success && Array.isArray(docResponse.data)) {
        // Store as a plain map keyed by documentType — works for any key the API returns
        this.docs = {};
        docResponse.data.forEach(d => {
          this.docs[d.documentType] = {
            id: d.id,
            name: d.documentName,
            url: d.filePath,
            approved: d.approved,
            uploadedAt: d.uploadedAt
              ? new Date(d.uploadedAt).toLocaleString('en-US', {
                  hour: '2-digit', minute: '2-digit',
                  year: 'numeric', month: '2-digit', day: '2-digit'
                })
              : ''
          };
        });
      }
    } catch (e) {
      logger.error('StoreEmployeeProfile', 'Error loading database documents:', e);
    }
  }

  /**
   * Fetches the document requirements for the employee's country from the backend.
   * Stores them in this.docRequirements as an array of category objects.
   */
  async loadDocumentRequirements() {
    try {
      const country = encodeURIComponent(this.profile?.country || 'India');
      const res = await apiClient.get(`/api/v2/employee-self-service/document-requirements?country=${country}`);
      if (res && res.success && Array.isArray(res.data)) {
        this.docRequirements = res.data;
      } else {
        this.docRequirements = [];
      }
    } catch (e) {
      logger.error('StoreEmployeeProfile', 'Error loading document requirements:', e);
      this.docRequirements = [];
    }
  }

  render(container) {
    if (!this.profile) return;

    // Profile Card Header fields
    const avatarImg = container.querySelector('#profile-card-image');
    if (avatarImg) {
      const avatarBuster = this.profile.avatarUrl ? `${this.profile.avatarUrl}?v=${Date.now()}` : 'imgs/male-avatar.png';
      avatarImg.src = avatarBuster;
      avatarImg.alt = this.profile.name || 'Store Employee';
    }

    const displayName = container.querySelector('#profile-display-name');
    if (displayName) displayName.textContent = this.profile.name || 'Not set';

    const displayStore = container.querySelector('#profile-display-store');
    if (displayStore) displayStore.textContent = this.profile.store || 'Corporate Head Office';

    const displayRole = container.querySelector('#profile-display-role');
    if (displayRole) displayRole.textContent = (this.user?.role || 'Store Employee').replace(/([A-Z])/g, ' $1').toUpperCase();

    const displayCode = container.querySelector('#profile-display-code');
    if (displayCode) displayCode.textContent = this.profile.employeeCode ? `ID: ${this.profile.employeeCode}` : 'ID: N/A';

    // Info Details Grid
    const nameVal = container.querySelector('#info-name');
    if (nameVal) nameVal.textContent = this.profile.name || 'Not set';

    const codeVal = container.querySelector('#info-code');
    if (codeVal) codeVal.textContent = this.profile.employeeCode || 'N/A';

    const designationVal = container.querySelector('#info-designation');
    if (designationVal) designationVal.textContent = this.profile.designation || 'Barista';

    const emailVal = container.querySelector('#info-email');
    if (emailVal) emailVal.textContent = this.profile.email || 'Not set';

    const phoneVal = container.querySelector('#info-phone');
    if (phoneVal) phoneVal.textContent = this.profile.phone || 'Not set';

    const addressVal = container.querySelector('#info-address');
    if (addressVal) addressVal.textContent = this.profile.address || 'Not set';

    const departmentVal = container.querySelector('#info-department');
    if (departmentVal) departmentVal.textContent = this.profile.department || 'N/A';

    const workingVal = container.querySelector('#info-working-type');
    if (workingVal) workingVal.textContent = this.profile.employmentType || 'Permanent';

    const salaryVal = container.querySelector('#info-salary');
    if (salaryVal) {
      salaryVal.textContent = `€${parseFloat(this.profile.hourlyRate || 15.00).toFixed(2)}/hr`;
    }

    const joinedVal = container.querySelector('#info-joined-date');
    if (joinedVal) joinedVal.textContent = this.profile.joinedDate || 'N/A';

    const storeVal = container.querySelector('#info-store');
    if (storeVal) storeVal.textContent = this.profile.store || 'Corporate Head Office';

    // STORE TYPE
    const storeTypeVal = container.querySelector('#info-store-type');
    if (storeTypeVal) storeTypeVal.textContent = this.profile.storeType ? this.profile.storeType.replace('_', ' ') : 'FLAGSHIP CAFE';

    const regionVal = container.querySelector('#info-region');
    if (regionVal) regionVal.textContent = this.profile.storeRegion || 'N/A';

    const countryVal = container.querySelector('#info-country');
    if (countryVal) countryVal.textContent = this.profile.country || 'N/A';

    // Edit form fields prepopulation
    const editCard = container.querySelector('#edit-profile-card');
    if (editCard) {
      editCard.style.display = this.isEditing ? 'block' : 'none';
    }

    const editName = container.querySelector('#input-profile-name');
    if (editName) editName.value = this.profile.name || '';

    const editEmail = container.querySelector('#input-profile-email');
    if (editEmail) editEmail.value = this.profile.email || '';

    const editPhone = container.querySelector('#input-profile-phone');
    if (editPhone) editPhone.value = this.profile.phone || '';

    const editGender = container.querySelector('#select-profile-gender');
    if (editGender) editGender.value = this.profile.gender || 'Male';

    const editAvatarType = container.querySelector('#select-profile-avatar-type');
    if (editAvatarType) {
      const isPreset = ['imgs/male-avatar.png', 'imgs/female-avatar.png'].includes(this.profile.avatarUrl);
      editAvatarType.value = isPreset ? this.profile.avatarUrl : 'custom';
    }

    // Render the dynamic documents tab based on country requirements
    this.renderDocumentsTab(container);
  }

  bindEvents(container, lifecycle) {
    const toggleEditBtn = container.querySelector('#btn-toggle-edit-mode');
    if (toggleEditBtn) {
      const handleToggle = () => {
        this.isEditing = !this.isEditing;
        this.render(container);
        if (this.isEditing) {
          const editCard = container.querySelector('#edit-profile-card');
          if (editCard) editCard.scrollIntoView({ behavior: 'smooth' });
        }
      };
      toggleEditBtn.addEventListener('click', handleToggle);
      lifecycle.onCleanup(() => toggleEditBtn.removeEventListener('click', handleToggle));
    }

    const editAvatarType = container.querySelector('#select-profile-avatar-type');
    const uploadContainer = container.querySelector('#avatar-file-upload-container');
    const fileInput = container.querySelector('#input-profile-file');
    const dropZone = container.querySelector('#avatar-drop-zone');
    const fileNameLabel = container.querySelector('#avatar-file-name');

    if (editAvatarType && uploadContainer) {
      const handleAvatarTypeChange = () => {
        uploadContainer.style.display = editAvatarType.value === 'custom' ? 'block' : 'none';
      };
      editAvatarType.addEventListener('change', handleAvatarTypeChange);
      lifecycle.onCleanup(() => editAvatarType.removeEventListener('change', handleAvatarTypeChange));
      
      // Initial trigger
      handleAvatarTypeChange();
    }

    if (dropZone && fileInput) {
      const triggerFile = () => fileInput.click();
      dropZone.addEventListener('click', triggerFile);
      lifecycle.onCleanup(() => dropZone.removeEventListener('click', triggerFile));

      const handleFileChange = () => {
        const file = fileInput.files[0];
        if (file) {
          this.selectedFile = file;
          if (fileNameLabel) fileNameLabel.textContent = file.name;

          // Local file preview logic
          const reader = new FileReader();
          reader.onload = (e) => {
            const previewImg = container.querySelector('#profile-card-image');
            if (previewImg) previewImg.src = e.target.result;
          };
          reader.readAsDataURL(file);
        }
      };
      fileInput.addEventListener('change', handleFileChange);
      lifecycle.onCleanup(() => fileInput.removeEventListener('change', handleFileChange));
    }

    // Save profile form submission
    const profileForm = container.querySelector('#form-edit-profile');
    if (profileForm) {
      const handleProfileSubmit = async (e) => {
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
        const avatarType = editAvatarType.value;
        let avatarUrl = '';

        try {
          if (avatarType !== 'custom') {
            avatarUrl = avatarType;
          } else {
            if (this.selectedFile) {
              const response = await fetch('/api/upload-avatar', {
                method: 'POST',
                headers: {
                  'Content-Type': this.selectedFile.type,
                  'X-Username': name,
                  'X-Role': this.user.role,
                  'X-Worker-Id': this.profile.employeeCode || String(this.profile.id || 'ADMIN')
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
              avatarUrl = this.profile.avatarUrl || 'imgs/male-avatar.png';
            }
          }

          const updateResponse = await apiClient.put('/api/v1/auth/me', {
            name,
            email,
            avatarUrl,
            phone,
            gender,
            designation: this.profile.designation || 'Barista'
          });

          if (updateResponse && updateResponse.success && updateResponse.data) {
            this.profile = updateResponse.data;
            userStore.updateProfile(this.user.role, updateResponse.data);

            authStore.updateUser({
              name: updateResponse.data.name,
              username: updateResponse.data.email,
              avatarUrl: updateResponse.data.avatarUrl
            });
            notificationStore.success('Profile changes saved successfully to database!');
          } else {
            throw new Error(updateResponse?.message || 'Database update failed.');
          }

          this.selectedFile = null;
          this.isEditing = false;
          this.render(container);
        } catch (err) {
          logger.error('StoreEmployeeProfile', 'Failed to update user profile:', err);
          notificationStore.danger(`Failed to save profile: ${err.message}`);
        } finally {
          if (saveButton) {
            saveButton.disabled = false;
            saveButton.textContent = 'Save Profile Changes';
          }
        }
      };
      profileForm.addEventListener('submit', handleProfileSubmit);
      lifecycle.onCleanup(() => profileForm.removeEventListener('submit', handleProfileSubmit));
    }

    // Password change form submission
    const passwordForm = container.querySelector('#form-change-password');
    if (passwordForm) {
      const handlePasswordSubmit = async (e) => {
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
          logger.error('StoreEmployeeProfile', 'Failed to update password:', err);
          notificationStore.danger(`Password change failed: ${err.message}`);
        } finally {
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Update Security Password';
          }
        }
      };
      passwordForm.addEventListener('submit', handlePasswordSubmit);
      lifecycle.onCleanup(() => passwordForm.removeEventListener('submit', handlePasswordSubmit));
    }

    // ── Document tab: event delegation (handles dynamically-rendered cards) ──
    const docsContainer = container.querySelector('#docs-dynamic-container');
    if (docsContainer) {
      const handleDocsDelegated = async (e) => {
        // Upload button click → trigger hidden file input
        if (e.target.matches('.btn-upload[data-type]')) {
          const type = e.target.getAttribute('data-type');
          const docInput = docsContainer.querySelector(`#input-doc-${type}`);
          if (docInput) docInput.click();
          return;
        }

        // Hidden file input change → upload file
        if (e.target.matches('input[type="file"][data-type]')) {
          const type = e.target.getAttribute('data-type');
          const file = e.target.files[0];
          if (!file) return;

          const progContainer = docsContainer.querySelector(`#progress-container-${type}`);
          const progBar      = docsContainer.querySelector(`#progress-bar-${type}`);
          const progText     = docsContainer.querySelector(`#progress-text-${type}`);
          const progPct      = docsContainer.querySelector(`#progress-pct-${type}`);

          if (progContainer) progContainer.style.display = 'block';
          if (progText) progText.textContent = 'Uploading...';

          let pct = 0;
          const interval = setInterval(() => {
            pct = Math.min(pct + 10, 90);
            if (progBar) progBar.style.width = `${pct}%`;
            if (progPct) progPct.textContent = `${pct}%`;
          }, 100);

          try {
            const response = await fetch('/api/upload-document', {
              method: 'POST',
              headers: {
                'Content-Type': file.type,
                'X-Worker-Id': this.profile.employeeCode || String(this.profile.id || 'ADMIN'),
                'X-Document-Type': type,
                'X-File-Name': file.name
              },
              body: file
            });

            clearInterval(interval);
            if (progBar) progBar.style.width = '100%';
            if (progPct) progPct.textContent = '100%';
            if (progText) progText.textContent = 'Completing…';

            const data = await response.json();
            if (data.success && data.url) {
              const saveRes = await apiClient.post('/api/v2/employee-self-service/documents', {
                documentType: type,
                documentName: file.name,
                filePath: data.url
              });
              if (saveRes && saveRes.success) {
                this.docs[type] = {
                  name: file.name, url: data.url,
                  uploadedAt: new Date().toLocaleString()
                };
                notificationStore.success(`${file.name} uploaded successfully!`);
              } else {
                throw new Error(saveRes?.message || 'Database link failed.');
              }
            } else {
              throw new Error(data.message || 'Server upload failed.');
            }
            await this.loadDocumentData();
            this.renderDocumentsTab(container);
          } catch (err) {
            clearInterval(interval);
            if (progContainer) progContainer.style.display = 'none';
            logger.error('StoreEmployeeProfile', 'Document upload failed:', err);
            notificationStore.danger(`Upload failed: ${err.message}`);
          }
          return;
        }

        // Delete button click
        if (e.target.matches('.btn-delete[data-type]')) {
          const type = e.target.getAttribute('data-type');
          try {
            const delRes = await apiClient.delete(`/api/v2/employee-self-service/documents/${type}`);
            if (delRes && delRes.success) {
              delete this.docs[type];
              notificationStore.success('Document deleted successfully.');
            } else {
              throw new Error(delRes?.message || 'Deletion failed.');
            }
            await this.loadDocumentData();
            this.renderDocumentsTab(container);
          } catch (err) {
            logger.error('StoreEmployeeProfile', 'Failed to delete document:', err);
            notificationStore.danger(`Delete failed: ${err.message}`);
          }
        }
      };

      docsContainer.addEventListener('click', handleDocsDelegated);
      docsContainer.addEventListener('change', handleDocsDelegated);
      lifecycle.onCleanup(() => {
        docsContainer.removeEventListener('click', handleDocsDelegated);
        docsContainer.removeEventListener('change', handleDocsDelegated);
      });
    }
  }

  /**
   * Dynamically renders the Documents tab from this.docRequirements and this.docs.
   * Called on mount and after every upload/delete.
   */
  renderDocumentsTab(container) {
    const wrapper = container.querySelector('#docs-dynamic-container');
    if (!wrapper) return;

    const requirements = this.docRequirements || [];
    const uploadedDocs  = this.docs || {};
    const country       = this.profile?.country || 'India';

    if (requirements.length === 0) {
      wrapper.innerHTML = `
        <div class="docs-loading-state">
          <p>No document requirements found for <strong>${country}</strong>.</p>
        </div>`;
      return;
    }

    wrapper.innerHTML = `
      <div class="docs-country-banner">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none"
          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/>
          <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
        </svg>
        Showing document requirements for <strong>${country}</strong>
      </div>
      <div class="docs-tabs-grid">
        ${requirements.map(cat => this._buildDocCategoryCard(cat, uploadedDocs)).join('')}
      </div>`;

    if (typeof lucide !== 'undefined') lucide.createIcons();
  }

  _buildDocCategoryCard(cat, uploadedDocs) {
    const uploadedCount = cat.docs.filter(d => uploadedDocs[d.docKey]).length;
    const totalCount    = cat.docs.length;
    const allDone       = uploadedCount === totalCount;

    return `
      <div class="card glass doc-card">
        <div class="card-header-row">
          <div>
            <h3 class="card-title">
              ${this._buildDocIcon(cat.categoryIcon)}
              ${cat.categoryLabel}
            </h3>
            <span class="card-subtitle">${uploadedCount} / ${totalCount} documents uploaded</span>
          </div>
          <span class="badge-required ${allDone ? 'badge-complete' : ''}">${allDone ? 'COMPLETE' : 'IN PROGRESS'}</span>
        </div>
        <div class="docs-list">
          ${cat.docs.map(doc => this._buildDocRow(doc, uploadedDocs[doc.docKey])).join('')}
        </div>
      </div>`;
  }

  _buildDocRow(doc, uploaded) {
    const type = doc.docKey;
    const isUploaded = !!uploaded;

    const statusHtml = isUploaded
      ? `<span class="status-badge ${uploaded.approved ? 'verified' : 'pending'}">
           ${uploaded.approved ? 'VERIFIED' : 'PENDING REVIEW'}
         </span>
         <span class="doc-uploaded-at">Uploaded: <strong>${uploaded.uploadedAt || 'N/A'}</strong></span>`
      : `<span class="status-badge pending" style="background:rgba(255,165,0,0.1);color:orange;">
           ${doc.required ? 'REQUIRED' : 'OPTIONAL'} — NOT UPLOADED
         </span>`;

    const actionsHtml = isUploaded
      ? `<a href="${uploaded.url}" target="_blank" class="btn btn-secondary"
            style="padding:6px 12px;font-size:0.68rem;width:auto;margin:0;">View File</a>
         <button type="button" class="btn-delete" data-type="${type}">Delete</button>`
      : `<button type="button" class="btn-upload" data-type="${type}">Upload Document</button>
         <input type="file" id="input-doc-${type}" data-type="${type}"
                style="display:none;" accept=".pdf,.jpg,.jpeg,.png,.doc,.docx">`;

    return `
      <div class="doc-row" id="doc-row-${type}">
        <div class="doc-icon-box">${this._buildDocIcon('file-text')}</div>
        <div class="doc-info">
          <span class="doc-title">${doc.docTitle}</span>
          <span class="doc-description">${doc.docDescription || ''}</span>
        </div>
        <div class="doc-status" id="status-${type}">${statusHtml}</div>
        <div class="doc-actions" id="actions-${type}">${actionsHtml}</div>
        <div class="progress-container" id="progress-container-${type}" style="display:none;">
          <div class="progress-bar-track">
            <div class="progress-bar" id="progress-bar-${type}" style="width:0%"></div>
          </div>
          <div class="progress-meta">
            <span id="progress-text-${type}">Uploading…</span>
            <span id="progress-pct-${type}">0%</span>
          </div>
        </div>
      </div>`;
  }

  _buildDocIcon(iconName) {
    const icons = {
      'user':          '<circle cx="12" cy="8" r="5"/><path d="M3 21v-2a7 7 0 0 1 14 0v2"/>',
      'home':          '<path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/>',
      'graduation-cap':'<path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/>',
      'briefcase':     '<rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 7V5a2 2 0 0 0-4 0v2"/><line x1="12" y1="12" x2="12" y2="12"/>',
      'credit-card':   '<rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/>',
      'receipt':       '<path d="M4 2v20l2-1 2 1 2-1 2 1 2-1 2 1 2-1 2 1V2l-2 1-2-1-2 1-2-1-2 1-2-1-2 1z"/><line x1="8" y1="9" x2="16" y2="9"/><line x1="8" y1="13" x2="16" y2="13"/>',
      'shield':        '<path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>',
      'heart-pulse':   '<path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/><polyline points="22 12 18 12 15 17 9 7 6 12 2 12"/>',
      'file-text':     '<path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/>',
      'globe':         '<circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>',
      'car':           '<rect x="1" y="11" width="22" height="9" rx="1"/><path d="M5 11l1.5-7h11L19 11"/><circle cx="7" cy="20" r="1"/><circle cx="17" cy="20" r="1"/>',
      'phone-call':    '<path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12a19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 3.6 1.26h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L7.91 8.91a16 16 0 0 0 6.18 6.18l.95-.95a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/>'
    };
    const path = icons[iconName] || icons['file-text'];
    return `<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
      fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
      stroke-linejoin="round" class="accent-icon">${path}</svg>`;
  }

  /**
   * Initialise tab switching for the profile page.
   * Tabs: profile | documents | payslips
   */
  _initTabs(container) {
    const tabBtns = container.querySelectorAll('.profile-tab-btn');
    const tabPanes = container.querySelectorAll('.profile-tab-pane');

    tabBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const target = btn.getAttribute('data-tab');

        // Toggle active states
        tabBtns.forEach(b => b.classList.remove('active'));
        tabPanes.forEach(p => p.classList.remove('active'));

        btn.classList.add('active');
        const pane = container.querySelector(`#tab-${target}`);
        if (pane) pane.classList.add('active');

        // Lazy-load payslip summary on first open
        if (target === 'payslips' && !this.payslipLoaded) {
          this.payslipLoaded = true;
          this._loadPayslipSummary(container);
        }

        // Re-initialise Lucide icons inside newly visible pane
        if (typeof lucide !== 'undefined') {
          lucide.createIcons();
        }
      });
    });
  }

  /**
   * Load a quick payslip summary into the Payslips tab.
   * Shows KPI totals and the 6 most recent payslip rows.
   */
  async _loadPayslipSummary(container) {
    try {
      const res = await apiClient.get('/api/v2/employee-self-service/payslips?limit=6');
      if (!res || !res.success || !Array.isArray(res.data) || res.data.length === 0) {
        const tbody = container.querySelector('#payslip-mini-tbody');
        if (tbody) tbody.innerHTML = `<tr><td colspan="6" class="payslip-loading-row"><span class="payslip-loading-text">No payslip records found.</span></td></tr>`;
        return;
      }

      const payslips = res.data;
      const latest = payslips[0];

      // KPI values
      const setKpi = (id, val) => {
        const el = container.querySelector(`#${id}`);
        if (el) el.textContent = val;
      };

      setKpi('ps-kpi-net-salary', latest.netSalary != null ? `€${parseFloat(latest.netSalary).toFixed(2)}` : '—');
      setKpi('ps-kpi-gross', latest.grossSalary != null ? `€${parseFloat(latest.grossSalary).toFixed(2)}` : '—');
      setKpi('ps-kpi-deductions', latest.deductions != null ? `€${parseFloat(latest.deductions).toFixed(2)}` : '—');
      setKpi('ps-kpi-last-date', latest.payDate ? new Date(latest.payDate).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' }) : '—');

      // Build table rows
      const tbody = container.querySelector('#payslip-mini-tbody');
      if (!tbody) return;

      tbody.innerHTML = payslips.map(ps => {
        const statusClass = ps.status === 'PAID' ? 'verified' : 'pending';
        const period = ps.payPeriod || (ps.payDate ? new Date(ps.payDate).toLocaleDateString('en-GB', { month: 'long', year: 'numeric' }) : '—');
        const downloadBtn = ps.filePath
          ? `<a href="${ps.filePath}" target="_blank" class="btn-upload" style="font-size:0.68rem;padding:5px 10px;">Download</a>`
          : `<span style="color:var(--text-muted);font-size:0.68rem;">—</span>`;
        return `<tr>
          <td>${period}</td>
          <td>€${parseFloat(ps.grossSalary || 0).toFixed(2)}</td>
          <td>€${parseFloat(ps.deductions || 0).toFixed(2)}</td>
          <td style="font-weight:700;color:var(--text-primary);">€${parseFloat(ps.netSalary || 0).toFixed(2)}</td>
          <td><span class="status-badge ${statusClass}">${ps.status || 'PENDING'}</span></td>
          <td>${downloadBtn}</td>
        </tr>`;
      }).join('');

    } catch (e) {
      logger.error('StoreEmployeeProfile', 'Failed to load payslip summary:', e);
      const tbody = container.querySelector('#payslip-mini-tbody');
      if (tbody) tbody.innerHTML = `<tr><td colspan="6" class="payslip-loading-row"><span class="payslip-loading-text">Could not load payslip data.</span></td></tr>`;
    }
  }

  _loadCss() {
    const cssId = 'store-employee-profile-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/profile-employee/profile.css';
      document.head.appendChild(link);
    }
  }
}
