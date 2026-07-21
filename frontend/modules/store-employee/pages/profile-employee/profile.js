/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : profile.js
 * Path              : frontend/modules/store-employee/pages/profile-employee/profile.js
 * Purpose           : Barista/Employee user profile workspace component; handles self-profile data loading from GET /api/v1/auth/me, onboarding document status, profile updating via PUT /api/v1/auth/me, custom avatar uploading, and DocumentHubComponent integration.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Specialized profile page controller for Baristas and Store Employees.
 * Capabilities:
 *   - Injects HTML template modules/store-employee/pages/profile-employee/profile.html and stylesheet profile.css.
 *   - Loads live profile fields and onboarding document status.
 *   - Toggles interactive editing for personal and banking fields (first name, last name, phone, gender, bank name, account, IFSC, branch).
 *   - Locks system-governed administrative fields (email, employee ID, designation, joined date, salary).
 *   - Supports custom file avatar uploads to /api/upload-avatar or selecting gender default presets.
 *   - Renders DocumentHubComponent configured specifically for storeEmployee role type.
 ******************************************************************************/
import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { DocumentHubComponent } from '../../../../shared/profile/DocumentHubComponent.js';

const TEMPLATE_URL = 'modules/store-employee/pages/profile-employee/profile.html';

export default class ProfilePage {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.docs = {};
    this.isEditing = false;
    this.docHub = null;
  }

  async mount(container, lifecycle) {
    logger.info('EmployeeProfile', 'Mounting employee profile page with multi-region document engine...');

    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    await this.loadProfileData();
    await this.loadDocumentData();

    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const cssId = 'css-employee-profile';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/profile-employee/profile.css';
      document.head.appendChild(link);
    }
  }

  async loadDocumentData() {
    try {
      const docResponse = await apiClient.get('/api/v2/employee-self-service/documents');
      if (docResponse && docResponse.success && Array.isArray(docResponse.data)) {
        const docs = {};
        docResponse.data.forEach(d => {
          docs[d.documentType] = {
            id: d.id,
            name: d.documentName,
            url: d.filePath,
            approved: d.approved,
            uploadedAt: d.uploadedAt ? new Date(d.uploadedAt).toLocaleString('en-US', {hour: '2-digit', minute: '2-digit', year: 'numeric', month: '2-digit', day: '2-digit'}) : ''
          };
        });
        this.docs = docs;
      }
    } catch (e) {
      logger.error('EmployeeProfile', 'Error loading database documents:', e);
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
      logger.error('EmployeeProfile', 'Error loading database profile:', e);
    }
  }

  render(container) {
    this.user = authStore.getUser();
    const p = this.profile || {};

    const fullName = (p.firstName || p.lastName)
      ? `${p.firstName || ''} ${p.lastName || ''}`.trim()
      : (p.fullName || p.name || this.user?.name || '');

    const email = p.email || this.user?.email || '';
    const storeName = p.storeName || p.store || '';

    const nameEl = container.querySelector('#emp-user-fullname');
    const emailEl = container.querySelector('#emp-user-email');
    const scopeBadgeEl = container.querySelector('#emp-scope-badge');
    const avatarImg = container.querySelector('#emp-avatar-img');

    const countryBadge = container.querySelector('#emp-country-code-badge');

    if (nameEl) nameEl.textContent = fullName || 'User Profile';
    if (emailEl) emailEl.textContent = email || 'No Email Registered';
    if (scopeBadgeEl) scopeBadgeEl.textContent = storeName || 'Main Store';
    if (countryBadge) {
      const countryStr = String(p.country || '').toLowerCase();
      const phoneStr = String(p.phone || p.phoneNumber || '');
      if (countryStr.includes('france') || phoneStr.startsWith('+33')) countryBadge.textContent = '🇫🇷 FR (+33)';
      else if (countryStr.includes('uae') || countryStr.includes('emirates') || phoneStr.startsWith('+971')) countryBadge.textContent = '🇦🇪 AE (+971)';
      else if (countryStr.includes('singapore') || phoneStr.startsWith('+65')) countryBadge.textContent = '🇸🇬 SG (+65)';
      else if (countryStr.includes('usa') || countryStr.includes('united states') || phoneStr.startsWith('+1')) countryBadge.textContent = '🇺🇸 US (+1)';
      else countryBadge.textContent = '🇮🇳 IN (+91)';
    }

    const docsBadge = container.querySelector('#emp-docs-verified-badge');
    if (docsBadge) {
      const docs = this.docs || {};
      const panApproved = docs.panCard && docs.panCard.approved === true;
      const aadhaarApproved = docs.aadhaarCard && docs.aadhaarCard.approved === true;
      const permitApproved = docs.workPermit && docs.workPermit.approved === true;
      const allVerified = panApproved && aadhaarApproved && permitApproved;

      if (allVerified) {
        docsBadge.style.background = 'rgba(16,185,129,0.1)';
        docsBadge.style.borderColor = 'rgba(16,185,129,0.3)';
        docsBadge.style.color = '#10b981';
        docsBadge.textContent = '✓ Onboarding Documents Verified';
      } else {
        docsBadge.style.background = 'rgba(245,158,11,0.12)';
        docsBadge.style.borderColor = 'rgba(245,158,11,0.35)';
        docsBadge.style.color = '#f59e0b';
        docsBadge.textContent = '⏳ Onboarding Documents Pending';
      }
    }

    if (avatarImg && (p.avatarUrl || this.user?.avatarUrl)) {
      avatarImg.src = p.avatarUrl || this.user?.avatarUrl;
    }

    const fnameInput = container.querySelector('#input-emp-firstname');
    const lnameInput = container.querySelector('#input-emp-lastname');
    const emailInput = container.querySelector('#input-emp-email');
    const phoneInput = container.querySelector('#input-emp-phone');
    const empIdInput = container.querySelector('#input-emp-empid');
    const desigInput = container.querySelector('#input-emp-designation');
    const joinedInput = container.querySelector('#input-emp-joined');
    const salaryInput = container.querySelector('#input-emp-salary');
    const bankNameInput = container.querySelector('#input-emp-bankname');
    const bankAccInput = container.querySelector('#input-emp-bankacc');
    const ifscInput = container.querySelector('#input-emp-ifsc');
    const branchInput = container.querySelector('#input-emp-branch');

    const genderSelect = container.querySelector('#input-emp-gender');

    if (fnameInput) fnameInput.value = p.firstName || '';
    if (lnameInput) lnameInput.value = p.lastName || '';
    if (emailInput) emailInput.value = email || '';
    if (phoneInput) phoneInput.value = p.phone || p.phoneNumber || '';
    if (empIdInput) empIdInput.value = p.employeeId || p.employeeCode || '';
    if (desigInput) desigInput.value = p.designation || p.role || '';
    if (joinedInput) joinedInput.value = p.joinedDate || p.hireDate || '';
    if (salaryInput) salaryInput.value = p.baseSalary || p.basicSalary || '';
    if (genderSelect) genderSelect.value = p.gender || 'Female';
    if (bankNameInput) bankNameInput.value = p.bankName || '';
    if (bankAccInput) bankAccInput.value = p.bankAccount || p.bankAccountNumber || '';
    if (ifscInput) ifscInput.value = p.ifscCode || p.ifscNumber || '';
    if (branchInput) branchInput.value = p.branchName || p.branchLocation || '';

    // Mount Multi-Region Document Hub Component
    const docHubBox = container.querySelector('#emp-doc-hub-container') || container.querySelector('.emp-docs-list');
    if (docHubBox) {
      this.docHub = new DocumentHubComponent({
        roleType: 'storeEmployee',
        user: this.user,
        profile: this.profile,
        initialDocs: this.docs
      });
      this.docHub.render(docHubBox);
    }
  }

  bindEvents(container, lifecycle) {
    const btnEdit = container.querySelector('#btn-toggle-edit-personal');
    const btnCancel = container.querySelector('#btn-cancel-personal');
    const actionsBox = container.querySelector('#emp-personal-actions');

    const fnameInput = container.querySelector('#input-emp-firstname');
    const lnameInput = container.querySelector('#input-emp-lastname');
    const phoneInput = container.querySelector('#input-emp-phone');
    const genderSelect = container.querySelector('#input-emp-gender');
    const bankNameInput = container.querySelector('#input-emp-bankname');
    const bankAccInput = container.querySelector('#input-emp-bankacc');
    const ifscInput = container.querySelector('#input-emp-ifsc');
    const branchInput = container.querySelector('#input-emp-branch');

    const editableInputs = [fnameInput, lnameInput, phoneInput, genderSelect, bankNameInput, bankAccInput, ifscInput, branchInput];
    const lockedInputs = [
      container.querySelector('#input-emp-email'),
      container.querySelector('#input-emp-empid'),
      container.querySelector('#input-emp-designation'),
      container.querySelector('#input-emp-joined'),
      container.querySelector('#input-emp-salary')
    ];

    const toggleEdit = (editing) => {
      this.isEditing = editing;

      // Editable fields styling on edit mode
      editableInputs.forEach(input => {
        if (!input) return;
        input.disabled = !editing;
        const parent = input.parentElement;
        if (parent) {
          if (editing) {
            parent.style.background = 'rgba(16, 185, 129, 0.08)';
            parent.style.borderColor = 'rgba(16, 185, 129, 0.4)';
            parent.style.boxShadow = '0 0 10px rgba(16, 185, 129, 0.15)';
            parent.style.opacity = '1';
            parent.style.filter = 'none';
          } else {
            parent.style.background = 'rgba(24, 24, 27, 0.8)';
            parent.style.borderColor = 'rgba(255, 255, 255, 0.1)';
            parent.style.boxShadow = 'none';
            parent.style.opacity = '1';
            parent.style.filter = 'none';
          }
        }
      });

      // Locked / Read-only fields dark-gray overlay on edit mode
      lockedInputs.forEach(input => {
        if (!input) return;
        const parent = input.parentElement;
        if (parent) {
          if (editing) {
            parent.style.background = 'rgba(0, 0, 0, 0.55)';
            parent.style.borderColor = 'rgba(255, 255, 255, 0.04)';
            parent.style.opacity = '0.35';
            parent.style.filter = 'grayscale(1)';
            parent.style.cursor = 'not-allowed';
            parent.title = '🔒 Read-only field — cannot be edited';
          } else {
            parent.style.background = 'rgba(24, 24, 27, 0.8)';
            parent.style.borderColor = 'rgba(255, 255, 255, 0.1)';
            parent.style.opacity = '1';
            parent.style.filter = 'none';
            parent.style.cursor = 'default';
            parent.title = '';
          }
        }
      });

      if (actionsBox) actionsBox.style.display = editing ? 'flex' : 'none';
      if (btnEdit) {
        const textSpan = container.querySelector('#btn-edit-personal-text');
        if (textSpan) textSpan.textContent = editing ? 'Editing...' : 'Edit Details';
      }
    };

    if (genderSelect) {
      const onGenderChange = (e) => {
        const selectedVal = e.target.value;
        const defaultAvatar = selectedVal === 'Female' ? 'imgs/female-avatar.jpg' : 'imgs/male-avatar.png';
        
        const avatarImg = container.querySelector('#emp-avatar-img');
        if (avatarImg) avatarImg.src = defaultAvatar;
        const headerAvatar = document.getElementById('user-avatar-header');
        if (headerAvatar) headerAvatar.src = defaultAvatar;
        const sidebarAvatar = document.getElementById('user-avatar-sidebar');
        if (sidebarAvatar) sidebarAvatar.src = defaultAvatar;

        this.profile.gender = selectedVal;
        this.profile.avatarUrl = defaultAvatar;
        userStore.updateProfile(this.user?.role, { gender: selectedVal, avatarUrl: defaultAvatar });
        authStore.updateUser({ avatarUrl: defaultAvatar });
      };
      genderSelect.addEventListener('change', onGenderChange);
      lifecycle.onCleanup(() => genderSelect.removeEventListener('change', onGenderChange));
    }

    if (btnEdit) {
      const onEditClick = () => toggleEdit(!this.isEditing);
      btnEdit.addEventListener('click', onEditClick);
      lifecycle.onCleanup(() => btnEdit.removeEventListener('click', onEditClick));
    }

    if (btnCancel) {
      const onCancelClick = () => toggleEdit(false);
      btnCancel.addEventListener('click', onCancelClick);
      lifecycle.onCleanup(() => btnCancel.removeEventListener('click', onCancelClick));
    }

    const personalForm = container.querySelector('#form-emp-personal-info');
    if (personalForm) {
      const onPersonalSubmit = async (e) => {
        e.preventDefault();
        try {
          const selectedGender = genderSelect?.value || 'Male';
          let avatarUrl = this.profile?.avatarUrl;
          if (!avatarUrl || avatarUrl === 'imgs/male-avatar.png' || avatarUrl === 'imgs/female-avatar.jpg') {
            avatarUrl = selectedGender === 'Female' ? 'imgs/female-avatar.jpg' : 'imgs/male-avatar.png';
          }
          this.profile.avatarUrl = avatarUrl;

          const payload = {
            firstName: fnameInput?.value || '',
            lastName: lnameInput?.value || '',
            phone: phoneInput?.value || '',
            gender: selectedGender,
            bankName: bankNameInput?.value || '',
            bankAccount: bankAccInput?.value || '',
            ifscCode: ifscInput?.value || '',
            branchName: branchInput?.value || '',
            avatarUrl: avatarUrl
          };

          // 1. Call Backend REST API to persist updates into database
          try {
            const apiRes = await apiClient.put('/api/v1/auth/me', payload);
            if (apiRes && apiRes.success && apiRes.data) {
              this.profile = { ...this.profile, ...apiRes.data };
            } else {
              this.profile = { ...this.profile, ...payload };
            }
          } catch (backendErr) {
            logger.warn('EmployeeProfile', 'Backend API offline or mock mode, updating state store:', backendErr);
            this.profile = { ...this.profile, ...payload };
          }

          // 2. Sync state store & auth store
          const updatedFullName = `${this.profile.firstName || ''} ${this.profile.lastName || ''}`.trim() || 'User Profile';
          this.profile.name = updatedFullName;

          userStore.updateProfile(this.user?.role, this.profile);
          authStore.updateUser({
            name: updatedFullName,
            avatarUrl: this.profile.avatarUrl
          });

          // Explicitly update all hero, header, and sidebar name elements in DOM
          const heroNameEl = container.querySelector('#emp-user-fullname');
          if (heroNameEl) heroNameEl.textContent = updatedFullName;
          const headerNameEl = document.getElementById('user-name-header');
          if (headerNameEl) headerNameEl.textContent = updatedFullName;
          const sidebarNameEl = document.getElementById('user-name-sidebar');
          if (sidebarNameEl) sidebarNameEl.textContent = updatedFullName;
          const welcomeNameEl = document.getElementById('header-user-name');
          if (welcomeNameEl) welcomeNameEl.textContent = updatedFullName;

          this.render(container);
          toggleEdit(false);

          notificationStore.success('Barista employee profile and banking details saved to database successfully.');
        } catch (err) {
          logger.error('EmployeeProfile', 'Failed to update employee profile in database:', err);
        }
      };

      personalForm.addEventListener('submit', onPersonalSubmit);
      lifecycle.onCleanup(() => personalForm.removeEventListener('submit', onPersonalSubmit));
    }

    // Avatar Change Pencil Icon & Choice Modal Listener
    const avatarFileInput = container.querySelector('#input-emp-avatar-file');
    const avatarImg = container.querySelector('#emp-avatar-img');
    const pencilBtn = container.querySelector('#btn-edit-avatar-pencil');
    const avatarBox = container.querySelector('#avatar-container-box') || avatarImg?.parentElement;

    const updateAvatarUrl = (newAvatarUrl) => {
      if (avatarImg) avatarImg.src = newAvatarUrl;
      const headerAvatar = document.getElementById('user-avatar-header');
      if (headerAvatar) headerAvatar.src = newAvatarUrl;

      this.profile.avatarUrl = newAvatarUrl;
      userStore.updateProfile(this.user?.role, { avatarUrl: newAvatarUrl });
      authStore.updateUser({ avatarUrl: newAvatarUrl });

      notificationStore.success('Your profile avatar has been updated successfully.');
    };

    const openAvatarChoiceModal = () => {
      const oldModal = document.getElementById('modal-avatar-choice');
      if (oldModal) oldModal.remove();

      const modalOverlay = document.createElement('div');
      modalOverlay.id = 'modal-avatar-choice';
      modalOverlay.style.cssText = `
        position: fixed; inset: 0; z-index: 99999;
        background: rgba(0, 0, 0, 0.75); backdrop-filter: blur(12px);
        display: flex; align-items: center; justify-content: center; padding: 20px;
      `;

      modalOverlay.innerHTML = `
        <div style="background: #18181b; border: 1px solid rgba(255, 255, 255, 0.12); border-radius: 16px; padding: 24px; max-width: 440px; width: 100%; box-shadow: 0 20px 50px rgba(0,0,0,0.6); color: #ffffff; display: flex; flex-direction: column; gap: 18px;">
          
          <div style="display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid rgba(255,255,255,0.08); padding-bottom: 12px;">
            <div style="display: flex; align-items: center; gap: 10px;">
              <span style="font-size: 1.3rem;">🖼️</span>
              <h3 style="margin: 0; font-size: 1.05rem; font-weight: 800;">Change Profile Avatar</h3>
            </div>
            <button id="btn-close-avatar-modal" style="background: transparent; border: none; color: #a1a1aa; font-size: 1.2rem; cursor: pointer; padding: 4px;">✕</button>
          </div>

          <p style="margin: 0; font-size: 0.82rem; color: #a1a1aa;">Choose whether to upload a custom image from your device or select a system default avatar:</p>

          <div style="display: flex; flex-direction: column; gap: 12px;">
            
            <button id="btn-option-custom-avatar" style="padding: 14px 16px; border-radius: 12px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.1); color: #ffffff; display: flex; align-items: center; justify-content: space-between; cursor: pointer; transition: all 0.2s ease;">
              <div style="display: flex; align-items: center; gap: 12px;">
                <span style="font-size: 1.4rem;">📁</span>
                <div style="text-align: left;">
                  <strong style="font-size: 0.88rem; display: block; color: #ffffff;">Upload Custom Image</strong>
                  <span style="font-size: 0.72rem; color: #a1a1aa;">Choose a photo from your computer or phone</span>
                </div>
              </div>
              <span style="color: #10b981; font-weight: 800; font-size: 0.9rem;">→</span>
            </button>

            <div style="border-radius: 12px; background: rgba(255,255,255,0.02); border: 1px solid rgba(255,255,255,0.08); padding: 14px; display: flex; flex-direction: column; gap: 10px;">
              <span style="font-size: 0.72rem; font-weight: 800; color: #a1a1aa; text-transform: uppercase;">Or Select System Default Avatar:</span>
              
              <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px;">
                <button id="btn-default-male" style="padding: 10px; border-radius: 10px; background: rgba(24,24,27,0.8); border: 1px solid rgba(255,255,255,0.1); color: #ffffff; display: flex; align-items: center; gap: 10px; cursor: pointer;">
                  <img src="imgs/male-avatar.png" style="width: 32px; height: 32px; border-radius: 50%; object-fit: cover;">
                  <span style="font-size: 0.78rem; font-weight: 700;">👨 Male Default</span>
                </button>

                <button id="btn-default-female" style="padding: 10px; border-radius: 10px; background: rgba(24,24,27,0.8); border: 1px solid rgba(255,255,255,0.1); color: #ffffff; display: flex; align-items: center; gap: 10px; cursor: pointer;">
                  <img src="imgs/female-avatar.jpg" style="width: 32px; height: 32px; border-radius: 50%; object-fit: cover;">
                  <span style="font-size: 0.78rem; font-weight: 700;">👩 Female Default</span>
                </button>
              </div>
            </div>

          </div>

          <div style="display: flex; justify-content: flex-end; margin-top: 4px;">
            <button id="btn-cancel-avatar-modal" style="padding: 8px 16px; border-radius: 8px; background: transparent; border: 1px solid rgba(255,255,255,0.1); color: #a1a1aa; font-size: 0.8rem; cursor: pointer;">Cancel</button>
          </div>

        </div>
      `;

      document.body.appendChild(modalOverlay);

      const closeModal = () => modalOverlay.remove();
      modalOverlay.querySelector('#btn-close-avatar-modal').addEventListener('click', closeModal);
      modalOverlay.querySelector('#btn-cancel-avatar-modal').addEventListener('click', closeModal);
      modalOverlay.addEventListener('click', (e) => {
        if (e.target === modalOverlay) closeModal();
      });

      modalOverlay.querySelector('#btn-option-custom-avatar').addEventListener('click', () => {
        closeModal();
        if (avatarFileInput) avatarFileInput.click();
      });

      modalOverlay.querySelector('#btn-default-male').addEventListener('click', () => {
        closeModal();
        updateAvatarUrl('imgs/male-avatar.png');
      });

      modalOverlay.querySelector('#btn-default-female').addEventListener('click', () => {
        closeModal();
        updateAvatarUrl('imgs/female-avatar.jpg');
      });
    };

    if (avatarBox) {
      avatarBox.addEventListener('click', openAvatarChoiceModal);
      lifecycle.onCleanup(() => avatarBox.removeEventListener('click', openAvatarChoiceModal));
    }

    if (pencilBtn) {
      pencilBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        openAvatarChoiceModal();
      });
    }

    if (avatarFileInput) {
      const onAvatarChange = async (e) => {
        const file = e.target.files?.[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = (evt) => {
          updateAvatarUrl(evt.target.result);
        };
        reader.readAsDataURL(file);

        try {
          const response = await fetch('/api/upload-avatar', {
            method: 'POST',
            headers: {
              'Content-Type': file.type || 'image/png',
              'X-Username': this.profile.firstName ? `${this.profile.firstName} ${this.profile.lastName}` : (this.user?.name || 'Barista'),
              'X-Role': this.user?.role || 'storeEmployee',
              'X-Worker-Id': this.profile.employeeId || 'EMP-30194'
            },
            body: file
          });

          const data = await response.json();
          if (data.success && data.url) {
            updateAvatarUrl(data.url);
          }
        } catch (err) {
          logger.error('EmployeeProfile', 'Error uploading custom avatar:', err);
        }
      };

      avatarFileInput.addEventListener('change', onAvatarChange);
      lifecycle.onCleanup(() => avatarFileInput.removeEventListener('change', onAvatarChange));
    }
  }
}
