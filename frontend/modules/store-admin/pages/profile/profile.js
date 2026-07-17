import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

const TEMPLATE_URL = 'modules/store-admin/pages/profile/profile.html';

export default class ProfilePage {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.docs = {
      franchise: null,
      legal: null,
      foodSafety: null,
      property: null,
      employees: null,
      finance: null,
      suppliers: null,
      equipment: null,
      compliance: null,
      insurance: null,
      operations: null
    };
    this.selectedFile = null;
    this.isEditing = false;
  }

  async mount(container, lifecycle) {
    logger.info('StoreAdminProfile', 'Mounting store admin profile page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML template layout
    await htmlLoader.inject(TEMPLATE_URL, container);

    // 2. Load profile data and documents from server
    await this.loadProfileData();
    await this.loadDocumentData();

    // 3. Populate data and bind events
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  async loadDocumentData() {
    try {
      const docResponse = await apiClient.get('/api/v2/employee-self-service/documents');
      if (docResponse && docResponse.success && Array.isArray(docResponse.data)) {
        const docs = {
          franchise: null,
          legal: null,
          foodSafety: null,
          property: null,
          employees: null,
          finance: null,
          suppliers: null,
          equipment: null,
          compliance: null,
          insurance: null,
          operations: null
        };
        docResponse.data.forEach(d => {
          if (docs.hasOwnProperty(d.documentType)) {
            docs[d.documentType] = {
              id: d.id,
              name: d.documentName,
              url: d.filePath,
              approved: d.approved,
              uploadedAt: d.uploadedAt ? new Date(d.uploadedAt).toLocaleString('en-US', {hour: '2-digit', minute: '2-digit', year: 'numeric', month: '2-digit', day: '2-digit'}) : ''
            };
          }
        });
        this.docs = docs;
      }
    } catch (e) {
      logger.error('StoreAdminProfile', 'Error loading database documents:', e);
    }
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
      logger.error('StoreAdminProfile', 'Error loading database profile:', e);
    }
  }

  render(container) {
    if (!this.profile) return;

    // Profile Card Header fields
    const avatarImg = container.querySelector('#profile-card-image');
    if (avatarImg) {
      const avatarBuster = this.profile.avatarUrl ? `${this.profile.avatarUrl}?v=${Date.now()}` : 'imgs/male-avatar.png';
      avatarImg.src = avatarBuster;
      avatarImg.alt = this.profile.name || 'Store Admin';
    }

    const displayName = container.querySelector('#profile-display-name');
    if (displayName) displayName.textContent = this.profile.name || 'Not set';

    const displayStore = container.querySelector('#profile-display-store');
    if (displayStore) displayStore.textContent = this.profile.store || 'Corporate Head Office';

    const displayRole = container.querySelector('#profile-display-role');
    if (displayRole) displayRole.textContent = (this.user?.role || 'Store Admin').replace(/([A-Z])/g, ' $1').toUpperCase();

    const displayCode = container.querySelector('#profile-display-code');
    if (displayCode) displayCode.textContent = this.profile.employeeCode ? `ID: ${this.profile.employeeCode}` : 'ID: N/A';

    // Info Details Grid
    const nameVal = container.querySelector('#info-name');
    if (nameVal) nameVal.textContent = this.profile.name || 'Not set';

    const codeVal = container.querySelector('#info-code');
    if (codeVal) codeVal.textContent = this.profile.employeeCode || 'N/A';

    const emailVal = container.querySelector('#info-email');
    if (emailVal) emailVal.textContent = this.profile.email || 'Not set';

    const phoneVal = container.querySelector('#info-phone');
    if (phoneVal) phoneVal.textContent = this.profile.phone || 'Not set';

    const designationVal = container.querySelector('#info-designation');
    if (designationVal) designationVal.textContent = this.profile.designation || 'Store Manager';

    const departmentVal = container.querySelector('#info-department');
    if (departmentVal) departmentVal.textContent = this.profile.department || 'Store Operations';

    const storeVal = container.querySelector('#info-store');
    if (storeVal) storeVal.textContent = this.profile.store || 'Corporate Head Office';

    // STORE TYPE
    const storeTypeVal = container.querySelector('#info-store-type');
    if (storeTypeVal) storeTypeVal.textContent = this.profile.storeType ? this.profile.storeType.replace('_', ' ') : 'FLAGSHIP CAFE';

    const regionVal = container.querySelector('#info-region');
    if (regionVal) regionVal.textContent = this.profile.storeRegion || 'N/A';

    const countryVal = container.querySelector('#info-country');
    if (countryVal) countryVal.textContent = this.profile.country || 'N/A';

    const addressVal = container.querySelector('#info-address');
    if (addressVal) addressVal.textContent = this.profile.address || 'Not set';

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

    // Render operational document rows status & actions
    [
      'franchise', 'legal', 'foodSafety', 'property',
      'employees', 'finance', 'suppliers', 'equipment',
      'compliance', 'insurance', 'operations'
    ].forEach(type => {
      const doc = this.docs[type];
      const statusContainer = container.querySelector(`#status-${type}`);
      const actionsContainer = container.querySelector(`#actions-${type}`);

      if (statusContainer && actionsContainer) {
        statusContainer.innerHTML = '';
        actionsContainer.innerHTML = '';

        if (doc) {
          statusContainer.innerHTML = `
            <span class="status-badge ${doc.approved ? 'verified' : 'pending'}" style="font-size: 0.65rem; font-weight: 700; padding: 2px 6px; border-radius: 4px; display: inline-block; ${doc.approved ? 'background: rgba(46, 213, 115, 0.12); color: #2ed573;' : 'background: rgba(255, 71, 87, 0.12); color: #ff4757;'}">
              ${doc.approved ? 'APPROVED' : 'PENDING REVIEW'}
            </span>
            <span class="doc-uploaded-at" style="font-size: 0.62rem; color: var(--text-muted); display: block; margin-top: 4px;">Uploaded: <strong>${doc.uploadedAt || 'N/A'}</strong></span>
          `;
          actionsContainer.innerHTML = `
            <a href="${doc.url}" target="_blank" class="btn btn-secondary" style="padding: 6px 12px; font-size: 0.68rem; width: auto; margin: 0; display: inline-flex;">View File</a>
            <button type="button" class="btn-delete btn" data-type="${type}" style="padding: 6px 12px; font-size: 0.68rem; background: rgba(255, 71, 87, 0.1); color: #ff4757; border: 1px solid rgba(255, 71, 87, 0.2); cursor: pointer; border-radius: var(--radius-xs); transition: all 0.2s;">Delete</button>
          `;
        } else {
          statusContainer.innerHTML = `
            <span class="status-badge pending" style="font-size: 0.65rem; font-weight: 700; padding: 2px 6px; border-radius: 4px; display: inline-block; background: rgba(255, 165, 0, 0.1); color: orange;">NOT UPLOADED</span>
          `;
          actionsContainer.innerHTML = `
            <button type="button" class="btn-upload btn btn-primary" data-type="${type}" style="padding: 6px 12px; font-size: 0.68rem; border-radius: var(--radius-xs);">Upload File</button>
          `;
        }
      }
    });
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
            designation: this.profile.designation || 'Store Manager'
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
          logger.error('StoreAdminProfile', 'Failed to update user profile:', err);
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
          logger.error('StoreAdminProfile', 'Failed to update password:', err);
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

    // Document uploads event handling
    const handleDocUpload = (btn) => {
      const type = btn.getAttribute('data-type');
      const docInput = container.querySelector(`#input-doc-${type}`);
      if (docInput) {
        const triggerDocFile = () => docInput.click();
        btn.addEventListener('click', triggerDocFile);
        
        const handleDocFileChange = async () => {
          const file = docInput.files[0];
          if (!file) return;

          const progContainer = container.querySelector(`#progress-container-${type}`);
          const progBar = container.querySelector(`#progress-bar-${type}`);
          const progText = container.querySelector(`#progress-text-${type}`);
          const progPct = container.querySelector(`#progress-pct-${type}`);

          if (progContainer) progContainer.style.display = 'block';
          if (progText) progText.textContent = 'Uploading...';

          let pct = 0;
          const interval = setInterval(() => {
            pct += 10;
            if (pct > 90) clearInterval(interval);
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
            if (progText) progText.textContent = 'Completing...';

            const data = await response.json();
            if (data.success && data.url) {
              const saveResponse = await apiClient.post('/api/v2/employee-self-service/documents', {
                documentType: type,
                documentName: file.name,
                filePath: data.url
              });
              if (saveResponse && saveResponse.success) {
                this.docs[type] = {
                  name: file.name,
                  url: data.url,
                  uploadedAt: new Date().toLocaleString()
                };
                notificationStore.success(`${file.name} uploaded successfully!`);
              } else {
                throw new Error(saveResponse?.message || 'Database link failed.');
              }
            } else {
              throw new Error(data.message || 'Server upload failed.');
            }
            await this.loadDocumentData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } catch (err) {
            clearInterval(interval);
            if (progContainer) progContainer.style.display = 'none';
            logger.error('StoreAdminProfile', 'Document upload failed:', err);
            notificationStore.danger(`Upload failed: ${err.message}`);
          }
        };
        docInput.addEventListener('change', handleDocFileChange);
      }
    };

    container.querySelectorAll('.btn-upload').forEach(handleDocUpload);

    // Document deletion event handling
    const handleDocDelete = (btn) => {
      const type = btn.getAttribute('data-type');
      const triggerDocDelete = async () => {
        try {
          const deleteResponse = await apiClient.delete(`/api/v2/employee-self-service/documents/${type}`);
          if (deleteResponse && deleteResponse.success) {
            this.docs[type] = null;
            notificationStore.success('Document deleted successfully.');
          } else {
            throw new Error(deleteResponse?.message || 'Deletion failed.');
          }
          await this.loadDocumentData();
          this.render(container);
          this.bindEvents(container, lifecycle);
        } catch (err) {
          logger.error('StoreAdminProfile', 'Failed to delete document:', err);
          notificationStore.danger(`Delete failed: ${err.message}`);
        }
      };
      btn.addEventListener('click', triggerDocDelete);
    };

    container.querySelectorAll('.btn-delete').forEach(handleDocDelete);
  }

  _loadCss() {
    const cssId = 'store-admin-profile-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/profile/profile.css';
      document.head.appendChild(link);
    }
  }
}
