/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : profile.js
 * Path              : frontend/modules/store-employee/pages/profile-supervisor/profile.js
 * Purpose           : Shift Supervisor user profile workspace component; handles self-profile data loading from GET /api/v1/auth/me, onboarding document status, profile updating via PUT /api/v1/auth/me, custom avatar uploading, and DocumentHubComponent integration.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Specialized profile page controller for Shift Supervisors.
 * Capabilities:
 *   - Injects HTML template modules/store-employee/pages/profile-supervisor/profile.html and stylesheet profile.css.
 *   - Loads live profile fields and onboarding document status.
 *   - Toggles interactive editing for personal and banking fields (first name, last name, phone, gender, bank name, account, IFSC, branch).
 *   - Locks system-governed administrative fields (email, employee ID, designation, joined date, salary).
 *   - Supports custom file avatar uploads to /api/upload-avatar or selecting gender default presets.
 *   - Renders DocumentHubComponent configured specifically for shiftSupervisor role type.
 ******************************************************************************/
import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { DocumentHubComponent } from '../../../../shared/profile/DocumentHubComponent.js';

const TEMPLATE_URL = 'modules/store-employee/pages/profile-supervisor/profile.html';

export default class SupervisorProfilePage {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.docs = {};
    this.isEditing = false;
    this.docHub = null;
  }

  async mount(container, lifecycle) {
    logger.info('SupervisorProfile', 'Mounting supervisor profile page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    await this.loadProfileData();
    await this.loadDocumentData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const cssId = 'css-supervisor-profile';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId; link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/profile-supervisor/profile.css';
      document.head.appendChild(link);
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
      logger.error('SupervisorProfile', 'Error loading profile:', e);
    }
  }

  async loadDocumentData() {
    try {
      const docResponse = await apiClient.get('/api/v2/employee-self-service/documents');
      if (docResponse && docResponse.success && Array.isArray(docResponse.data)) {
        const docs = {};
        docResponse.data.forEach(d => {
          docs[d.documentType] = {
            id: d.id, name: d.documentName, url: d.filePath,
            approved: d.approved,
            uploadedAt: d.uploadedAt ? new Date(d.uploadedAt).toLocaleString('en-US', { hour: '2-digit', minute: '2-digit', year: 'numeric', month: '2-digit', day: '2-digit' }) : ''
          };
        });
        this.docs = docs;
      }
    } catch (e) {
      logger.error('SupervisorProfile', 'Error loading documents:', e);
    }
  }

  render(container) {
    this.user = authStore.getUser();
    const p = this.profile || {};
    const fullName = (p.firstName || p.lastName) ? `${p.firstName || ''} ${p.lastName || ''}`.trim() : (p.fullName || p.name || this.user?.name || '');
    const email = p.email || this.user?.email || '';
    const storeName = p.storeName || p.store || '';

    const nameEl = container.querySelector('#sup-user-fullname');
    const emailEl = container.querySelector('#sup-user-email');
    const scopeBadgeEl = container.querySelector('#sup-scope-badge');
    const avatarImg = container.querySelector('#sup-avatar-img');
    const countryBadge = container.querySelector('#sup-country-code-badge');

    if (nameEl) nameEl.textContent = fullName || 'Supervisor Profile';
    if (emailEl) emailEl.textContent = email || 'No Email Registered';
    if (scopeBadgeEl) scopeBadgeEl.textContent = storeName ? `📍 ${storeName}` : '📍 Outlet #104';

    if (countryBadge) {
      const countryStr = String(p.country || '').toLowerCase();
      const phoneStr = String(p.phone || p.phoneNumber || '');
      if (countryStr.includes('france') || phoneStr.startsWith('+33')) countryBadge.textContent = '🇫🇷 FR (+33)';
      else if (countryStr.includes('uae') || countryStr.includes('emirates') || phoneStr.startsWith('+971')) countryBadge.textContent = '🇦🇪 AE (+971)';
      else if (countryStr.includes('singapore') || phoneStr.startsWith('+65')) countryBadge.textContent = '🇸🇬 SG (+65)';
      else if (countryStr.includes('usa') || countryStr.includes('united states') || phoneStr.startsWith('+1')) countryBadge.textContent = '🇺🇸 US (+1)';
      else countryBadge.textContent = '🇮🇳 IN (+91)';
    }

    const docsBadge = container.querySelector('#sup-docs-verified-badge');
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

    const fields = {
      '#input-sup-firstname': p.firstName || '',
      '#input-sup-lastname': p.lastName || '',
      '#input-sup-email': email,
      '#input-sup-phone': p.phone || p.phoneNumber || '',
      '#input-sup-empid': p.employeeId || p.employeeCode || '',
      '#input-sup-designation': p.designation || p.role || '',
      '#input-sup-joined': p.joinedDate || p.hireDate || '',
      '#input-sup-salary': p.baseSalary || p.basicSalary || '',
      '#input-sup-bankname': p.bankName || '',
      '#input-sup-bankacc': p.bankAccount || p.bankAccountNumber || '',
      '#input-sup-ifsc': p.ifscCode || p.ifscNumber || '',
      '#input-sup-branch': p.branchName || p.branchLocation || '',
    };
    Object.entries(fields).forEach(([sel, val]) => {
      const el = container.querySelector(sel);
      if (el) el.value = val;
    });
    const genderSelect = container.querySelector('#input-sup-gender');
    if (genderSelect) genderSelect.value = p.gender || 'Male';

    // Mount Document Hub
    const docHubBox = container.querySelector('#sup-doc-hub-container');
    if (docHubBox && !this.docHub) {
      this.docHub = new DocumentHubComponent({ roleType: 'supervisor', user: this.user, profile: this.profile, initialDocs: this.docs });
      this.docHub.render(docHubBox);
    }
  }

  bindEvents(container, lifecycle) {
    const btnEdit = container.querySelector('#btn-toggle-edit-personal');
    const btnCancel = container.querySelector('#btn-cancel-personal');
    const actionsBox = container.querySelector('#sup-personal-actions');
    const fnameInput = container.querySelector('#input-sup-firstname');
    const lnameInput = container.querySelector('#input-sup-lastname');
    const phoneInput = container.querySelector('#input-sup-phone');
    const genderSelect = container.querySelector('#input-sup-gender');
    const bankNameInput = container.querySelector('#input-sup-bankname');
    const bankAccInput = container.querySelector('#input-sup-bankacc');
    const ifscInput = container.querySelector('#input-sup-ifsc');
    const branchInput = container.querySelector('#input-sup-branch');
    const editableInputs = [fnameInput, lnameInput, phoneInput, genderSelect, bankNameInput, bankAccInput, ifscInput, branchInput];
    const lockedInputs = [
      container.querySelector('#input-sup-email'), container.querySelector('#input-sup-empid'),
      container.querySelector('#input-sup-designation'), container.querySelector('#input-sup-joined'), container.querySelector('#input-sup-salary')
    ];

    const toggleEdit = (editing) => {
      this.isEditing = editing;
      editableInputs.forEach(input => {
        if (!input) return;
        input.disabled = !editing;
        const p = input.closest('.sup-form-input-wrap') || input.parentElement;
        if (p) {
          p.style.background = editing ? 'rgba(249,115,22,0.08)' : 'rgba(24,24,27,0.8)';
          p.style.borderColor = editing ? 'rgba(249,115,22,0.4)' : 'rgba(255,255,255,0.1)';
          p.style.boxShadow = editing ? '0 0 10px rgba(249,115,22,0.15)' : 'none';
        }
      });
      lockedInputs.forEach(input => {
        if (!input) return;
        const p = input.closest('.sup-form-input-wrap') || input.parentElement;
        if (p) {
          p.style.background = editing ? 'rgba(0,0,0,0.55)' : 'rgba(24,24,27,0.8)';
          p.style.borderColor = editing ? 'rgba(255,255,255,0.04)' : 'rgba(255,255,255,0.1)';
          p.style.opacity = editing ? '0.35' : '1';
          p.style.filter = editing ? 'grayscale(1)' : 'none';
          p.style.cursor = editing ? 'not-allowed' : 'default';
          p.title = editing ? '🔒 Read-only field — cannot be edited' : '';
        }
      });
      if (actionsBox) actionsBox.style.display = editing ? 'flex' : 'none';
      if (btnEdit) btnEdit.textContent = editing ? '⏳ Editing...' : '✏️ Edit Details';
    };

    if (genderSelect) {
      const onGenderChange = (e) => {
        const val = e.target.value;
        const defaultAvatar = val === 'Female' ? 'imgs/female-avatar.jpg' : 'imgs/male-avatar.png';
        const avatarImg = container.querySelector('#sup-avatar-img');
        if (avatarImg) avatarImg.src = defaultAvatar;
        const ha = document.getElementById('user-avatar-header'); if (ha) ha.src = defaultAvatar;
        const sa = document.getElementById('user-avatar-sidebar'); if (sa) sa.src = defaultAvatar;
        this.profile.gender = val; this.profile.avatarUrl = defaultAvatar;
        userStore.updateProfile(this.user?.role, { gender: val, avatarUrl: defaultAvatar });
        authStore.updateUser({ avatarUrl: defaultAvatar });
      };
      genderSelect.addEventListener('change', onGenderChange);
      lifecycle.onCleanup(() => genderSelect.removeEventListener('change', onGenderChange));
    }

    if (btnEdit) { const h = () => toggleEdit(!this.isEditing); btnEdit.addEventListener('click', h); lifecycle.onCleanup(() => btnEdit.removeEventListener('click', h)); }
    if (btnCancel) { const h = () => toggleEdit(false); btnCancel.addEventListener('click', h); lifecycle.onCleanup(() => btnCancel.removeEventListener('click', h)); }

    const personalForm = container.querySelector('#form-sup-personal-info');
    if (personalForm) {
      const onSubmit = async (e) => {
        e.preventDefault();
        try {
          const selectedGender = genderSelect?.value || 'Male';
          let avatarUrl = this.profile?.avatarUrl;
          if (!avatarUrl || avatarUrl === 'imgs/male-avatar.png' || avatarUrl === 'imgs/female-avatar.jpg') {
            avatarUrl = selectedGender === 'Female' ? 'imgs/female-avatar.jpg' : 'imgs/male-avatar.png';
          }
          this.profile.avatarUrl = avatarUrl;
          const payload = {
            firstName: fnameInput?.value || '', lastName: lnameInput?.value || '',
            phone: phoneInput?.value || '', gender: selectedGender,
            bankName: bankNameInput?.value || '', bankAccount: bankAccInput?.value || '',
            ifscCode: ifscInput?.value || '', branchName: branchInput?.value || '', avatarUrl
          };
          try {
            const apiRes = await apiClient.put('/api/v1/auth/me', payload);
            this.profile = apiRes?.success && apiRes.data ? { ...this.profile, ...apiRes.data } : { ...this.profile, ...payload };
          } catch (err) {
            logger.warn('SupervisorProfile', 'Backend offline, using local state:', err);
            this.profile = { ...this.profile, ...payload };
          }
          const updatedFullName = `${this.profile.firstName || ''} ${this.profile.lastName || ''}`.trim() || 'Supervisor Profile';
          this.profile.name = updatedFullName;
          userStore.updateProfile(this.user?.role, this.profile);
          authStore.updateUser({ name: updatedFullName, avatarUrl: this.profile.avatarUrl });
          const heroNameEl = container.querySelector('#sup-user-fullname'); if (heroNameEl) heroNameEl.textContent = updatedFullName;
          const headerNameEl = document.getElementById('user-name-header'); if (headerNameEl) headerNameEl.textContent = updatedFullName;
          const sidebarNameEl = document.getElementById('user-name-sidebar'); if (sidebarNameEl) sidebarNameEl.textContent = updatedFullName;
          const welcomeNameEl = document.getElementById('header-user-name'); if (welcomeNameEl) welcomeNameEl.textContent = updatedFullName;
          this.render(container);
          toggleEdit(false);
          notificationStore.success('Supervisor profile and banking details saved successfully.');
        } catch (err) {
          logger.error('SupervisorProfile', 'Failed to update profile:', err);
        }
      };
      personalForm.addEventListener('submit', onSubmit);
      lifecycle.onCleanup(() => personalForm.removeEventListener('submit', onSubmit));
    }

    // Avatar modal
    const avatarFileInput = container.querySelector('#input-sup-avatar-file');
    const avatarImg = container.querySelector('#sup-avatar-img');
    const avatarBox = container.querySelector('#avatar-container-box') || avatarImg?.parentElement;
    const updateAvatarUrl = (url) => {
      if (avatarImg) avatarImg.src = url;
      const ha = document.getElementById('user-avatar-header'); if (ha) ha.src = url;
      this.profile.avatarUrl = url;
      userStore.updateProfile(this.user?.role, { avatarUrl: url });
      authStore.updateUser({ avatarUrl: url });
      notificationStore.success('Profile avatar updated successfully.');
    };
    const openAvatarModal = () => {
      const old = document.getElementById('modal-avatar-choice'); if (old) old.remove();
      const overlay = document.createElement('div'); overlay.id = 'modal-avatar-choice';
      overlay.style.cssText = 'position:fixed;inset:0;z-index:99999;background:rgba(0,0,0,0.75);backdrop-filter:blur(12px);display:flex;align-items:center;justify-content:center;padding:20px;';
      overlay.innerHTML = `<div style="background:#18181b;border:1px solid rgba(255,255,255,0.12);border-radius:16px;padding:24px;max-width:440px;width:100%;box-shadow:0 20px 50px rgba(0,0,0,0.6);color:#fff;display:flex;flex-direction:column;gap:18px;">
        <div style="display:flex;align-items:center;justify-content:space-between;border-bottom:1px solid rgba(255,255,255,0.08);padding-bottom:12px;">
          <div style="display:flex;align-items:center;gap:10px;"><span style="font-size:1.3rem;">🖼️</span><h3 style="margin:0;font-size:1.05rem;font-weight:800;">Change Profile Avatar</h3></div>
          <button id="btn-close-avatar-modal" style="background:transparent;border:none;color:#a1a1aa;font-size:1.2rem;cursor:pointer;">✕</button>
        </div>
        <p style="margin:0;font-size:0.82rem;color:#a1a1aa;">Choose to upload a custom image or select a system default avatar:</p>
        <div style="display:flex;flex-direction:column;gap:12px;">
          <button id="btn-option-custom-avatar" style="padding:14px 16px;border-radius:12px;background:rgba(255,255,255,0.04);border:1px solid rgba(255,255,255,0.1);color:#fff;display:flex;align-items:center;justify-content:space-between;cursor:pointer;">
            <div style="display:flex;align-items:center;gap:12px;"><span style="font-size:1.4rem;">📁</span><div style="text-align:left;"><strong style="font-size:0.88rem;display:block;">Upload Custom Image</strong><span style="font-size:0.72rem;color:#a1a1aa;">Choose from your device</span></div></div>
            <span style="color:#f97316;font-weight:800;">→</span>
          </button>
          <div style="border-radius:12px;background:rgba(255,255,255,0.02);border:1px solid rgba(255,255,255,0.08);padding:14px;display:flex;flex-direction:column;gap:10px;">
            <span style="font-size:0.72rem;font-weight:800;color:#a1a1aa;text-transform:uppercase;">Select System Default:</span>
            <div style="display:grid;grid-template-columns:repeat(2,1fr);gap:10px;">
              <button id="btn-default-male" style="padding:10px;border-radius:10px;background:rgba(24,24,27,0.8);border:1px solid rgba(255,255,255,0.1);color:#fff;display:flex;align-items:center;gap:10px;cursor:pointer;">
                <img src="imgs/male-avatar.png" style="width:32px;height:32px;border-radius:50%;object-fit:cover;"><span style="font-size:0.78rem;font-weight:700;">👨 Male Default</span>
              </button>
              <button id="btn-default-female" style="padding:10px;border-radius:10px;background:rgba(24,24,27,0.8);border:1px solid rgba(255,255,255,0.1);color:#fff;display:flex;align-items:center;gap:10px;cursor:pointer;">
                <img src="imgs/female-avatar.jpg" style="width:32px;height:32px;border-radius:50%;object-fit:cover;"><span style="font-size:0.78rem;font-weight:700;">👩 Female Default</span>
              </button>
            </div>
          </div>
        </div>
        <div style="display:flex;justify-content:flex-end;">
          <button id="btn-cancel-avatar-modal" style="padding:8px 16px;border-radius:8px;background:transparent;border:1px solid rgba(255,255,255,0.1);color:#a1a1aa;font-size:0.8rem;cursor:pointer;">Cancel</button>
        </div>
      </div>`;
      document.body.appendChild(overlay);
      const close = () => overlay.remove();
      overlay.querySelector('#btn-close-avatar-modal').addEventListener('click', close);
      overlay.querySelector('#btn-cancel-avatar-modal').addEventListener('click', close);
      overlay.addEventListener('click', (e) => { if (e.target === overlay) close(); });
      overlay.querySelector('#btn-option-custom-avatar').addEventListener('click', () => { close(); if (avatarFileInput) avatarFileInput.click(); });
      overlay.querySelector('#btn-default-male').addEventListener('click', () => { close(); updateAvatarUrl('imgs/male-avatar.png'); });
      overlay.querySelector('#btn-default-female').addEventListener('click', () => { close(); updateAvatarUrl('imgs/female-avatar.jpg'); });
    };
    if (avatarBox) { const h = () => openAvatarModal(); avatarBox.addEventListener('click', h); lifecycle.onCleanup(() => avatarBox.removeEventListener('click', h)); }
    const pencilBtn = container.querySelector('#btn-edit-avatar-pencil');
    if (pencilBtn) { pencilBtn.addEventListener('click', (e) => { e.stopPropagation(); openAvatarModal(); }); }
    if (avatarFileInput) {
      const onFileChange = async (e) => {
        const file = e.target.files?.[0]; if (!file) return;
        const reader = new FileReader();
        reader.onload = (evt) => updateAvatarUrl(evt.target.result);
        reader.readAsDataURL(file);
        try {
          const res = await fetch('/api/upload-avatar', { method: 'POST', headers: { 'Content-Type': file.type || 'image/png', 'X-Role': this.user?.role || 'supervisor' }, body: file });
          const data = await res.json();
          if (data.success && data.url) updateAvatarUrl(data.url);
        } catch (err) { logger.error('SupervisorProfile', 'Avatar upload error:', err); }
      };
      avatarFileInput.addEventListener('change', onFileChange);
      lifecycle.onCleanup(() => avatarFileInput.removeEventListener('change', onFileChange));
    }
  }
}
