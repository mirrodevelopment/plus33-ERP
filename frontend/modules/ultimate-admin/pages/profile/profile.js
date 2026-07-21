/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin Module
 * File              : profile.js
 * Path              : frontend/modules/ultimate-admin/pages/profile/profile.js
 * Purpose           : Ultimate Admin user profile workspace component; handles self-profile data loading from GET /api/v1/auth/me, onboarding document fetching from GET /api/v2/employee-self-service/documents, profile updating via PUT /api/v1/auth/me, custom avatar uploading, and DocumentHubComponent integration.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Specialized profile page controller for Ultimate Admin.
 * Capabilities:
 *   - Injects HTML template modules/ultimate-admin/pages/profile/profile.html and stylesheet profile.css.
 *   - Loads live profile fields and onboarding document status.
 *   - Toggles interactive editing for personal and banking fields (first name, last name, phone, gender, bank name, account, IFSC, branch).
 *   - Locks system-governed administrative fields (email, employee ID, designation, joined date, salary).
 *   - Supports custom file avatar uploads to /api/upload-avatar or selecting gender default presets.
 *   - Renders DocumentHubComponent configured specifically for ultimateAdmin role type.
 ******************************************************************************/
import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { DocumentHubComponent } from '../../../../shared/profile/DocumentHubComponent.js';

const TEMPLATE_URL = 'modules/ultimate-admin/pages/profile/profile.html';

export default class UltimateAdminProfilePage {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.docs = {};
    this.isEditing = false;
    this.docHub = null;
  }

  async mount(container, lifecycle) {
    logger.info('UltimateAdminProfile', 'Mounting ultimate admin profile page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    await this.loadProfileData();
    await this.loadDocumentData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    const cssId = 'css-ultimate-admin-profile';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId; link.rel = 'stylesheet';
      link.href = 'modules/ultimate-admin/pages/profile/profile.css';
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
    } catch (e) { logger.error('UltimateAdminProfile', 'Error loading profile:', e); }
  }

  async loadDocumentData() {
    try {
      const docResponse = await apiClient.get('/api/v2/employee-self-service/documents');
      if (docResponse && docResponse.success && Array.isArray(docResponse.data)) {
        const docs = {};
        docResponse.data.forEach(d => {
          docs[d.documentType] = { id: d.id, name: d.documentName, url: d.filePath, approved: d.approved, uploadedAt: d.uploadedAt ? new Date(d.uploadedAt).toLocaleString() : '' };
        });
        this.docs = docs;
      }
    } catch (e) { logger.error('UltimateAdminProfile', 'Error loading documents:', e); }
  }

  render(container) {
    this.user = authStore.getUser();
    const p = this.profile || {};
    const fullName = (p.firstName || p.lastName) ? `${p.firstName || ''} ${p.lastName || ''}`.trim() : (p.fullName || p.name || this.user?.name || '');
    const email = p.email || this.user?.email || '';

    const nameEl = container.querySelector('#ult-user-fullname'); if (nameEl) nameEl.textContent = fullName || 'Ultimate Admin Profile';
    const emailEl = container.querySelector('#ult-user-email'); if (emailEl) emailEl.textContent = email || 'No Email Registered';
    const avatarImg = container.querySelector('#ult-avatar-img');

    const docsBadge = container.querySelector('#ult-docs-verified-badge');
    if (docsBadge) {
      const docs = this.docs || {};
      const allVerified = docs.panCard?.approved && docs.aadhaarCard?.approved && docs.workPermit?.approved;
      if (allVerified) { docsBadge.style.background = 'rgba(16,185,129,0.1)'; docsBadge.style.borderColor = 'rgba(16,185,129,0.3)'; docsBadge.style.color = '#10b981'; docsBadge.textContent = '✓ Onboarding Documents Verified'; }
      else { docsBadge.style.background = 'rgba(245,158,11,0.12)'; docsBadge.style.borderColor = 'rgba(245,158,11,0.35)'; docsBadge.style.color = '#f59e0b'; docsBadge.textContent = '⏳ Onboarding Documents Pending'; }
    }

    if (avatarImg && (p.avatarUrl || this.user?.avatarUrl)) avatarImg.src = p.avatarUrl || this.user?.avatarUrl;

    const fields = {
      '#input-ult-firstname': p.firstName || '', '#input-ult-lastname': p.lastName || '',
      '#input-ult-email': email, '#input-ult-phone': p.phone || p.phoneNumber || '',
      '#input-ult-empid': p.employeeId || p.employeeCode || '', '#input-ult-designation': p.designation || p.role || '',
      '#input-ult-joined': p.joinedDate || p.hireDate || '', '#input-ult-salary': p.baseSalary || p.basicSalary || '',
      '#input-ult-bankname': p.bankName || '', '#input-ult-bankacc': p.bankAccount || p.bankAccountNumber || '',
      '#input-ult-ifsc': p.ifscCode || p.ifscNumber || '', '#input-ult-branch': p.branchName || p.branchLocation || '',
    };
    Object.entries(fields).forEach(([sel, val]) => { const el = container.querySelector(sel); if (el) el.value = val; });
    const genderSelect = container.querySelector('#input-ult-gender');
    if (genderSelect) genderSelect.value = p.gender || 'Male';

    const docHubBox = container.querySelector('#ult-doc-hub-container');
    if (docHubBox && !this.docHub) {
      this.docHub = new DocumentHubComponent({ roleType: 'ultimateAdmin', user: this.user, profile: this.profile, initialDocs: this.docs });
      this.docHub.render(docHubBox);
    }
  }

  bindEvents(container, lifecycle) {
    const btnEdit = container.querySelector('#btn-toggle-edit-personal');
    const btnCancel = container.querySelector('#btn-cancel-personal');
    const actionsBox = container.querySelector('#ult-personal-actions');
    const fnameInput = container.querySelector('#input-ult-firstname');
    const lnameInput = container.querySelector('#input-ult-lastname');
    const phoneInput = container.querySelector('#input-ult-phone');
    const genderSelect = container.querySelector('#input-ult-gender');
    const bankNameInput = container.querySelector('#input-ult-bankname');
    const bankAccInput = container.querySelector('#input-ult-bankacc');
    const ifscInput = container.querySelector('#input-ult-ifsc');
    const branchInput = container.querySelector('#input-ult-branch');
    const editableInputs = [fnameInput, lnameInput, phoneInput, genderSelect, bankNameInput, bankAccInput, ifscInput, branchInput];
    const lockedInputs = [
      container.querySelector('#input-ult-email'), container.querySelector('#input-ult-empid'),
      container.querySelector('#input-ult-designation'), container.querySelector('#input-ult-joined'), container.querySelector('#input-ult-salary')
    ];

    const toggleEdit = (editing) => {
      this.isEditing = editing;
      editableInputs.forEach(input => {
        if (!input) return;
        input.disabled = !editing;
        const w = input.closest('.ult-form-input-wrap') || input.parentElement;
        if (w) { w.style.background = editing ? 'rgba(217,70,239,0.1)' : 'rgba(24,24,27,0.8)'; w.style.borderColor = editing ? 'rgba(217,70,239,0.5)' : 'rgba(255,255,255,0.1)'; w.style.boxShadow = editing ? '0 0 12px rgba(217,70,239,0.2)' : 'none'; }
      });
      lockedInputs.forEach(input => {
        if (!input) return;
        const w = input.closest('.ult-form-input-wrap') || input.parentElement;
        if (w) { w.style.background = editing ? 'rgba(0,0,0,0.55)' : 'rgba(24,24,27,0.8)'; w.style.borderColor = editing ? 'rgba(255,255,255,0.04)' : 'rgba(255,255,255,0.1)'; w.style.opacity = editing ? '0.35' : '1'; w.style.filter = editing ? 'grayscale(1)' : 'none'; w.style.cursor = editing ? 'not-allowed' : 'default'; w.title = editing ? '🔒 Read-only field' : ''; }
      });
      if (actionsBox) actionsBox.style.display = editing ? 'flex' : 'none';
      if (btnEdit) btnEdit.textContent = editing ? '⏳ Editing...' : '✏️ Edit Details';
    };

    if (genderSelect) {
      const h = (e) => {
        const val = e.target.value, da = val === 'Female' ? 'imgs/female-avatar.jpg' : 'imgs/male-avatar.png';
        const ai = container.querySelector('#ult-avatar-img'); if (ai) ai.src = da;
        const ha = document.getElementById('user-avatar-header'); if (ha) ha.src = da;
        this.profile.gender = val; this.profile.avatarUrl = da;
        userStore.updateProfile(this.user?.role, { gender: val, avatarUrl: da }); authStore.updateUser({ avatarUrl: da });
      };
      genderSelect.addEventListener('change', h); lifecycle.onCleanup(() => genderSelect.removeEventListener('change', h));
    }
    if (btnEdit) { const h = () => toggleEdit(!this.isEditing); btnEdit.addEventListener('click', h); lifecycle.onCleanup(() => btnEdit.removeEventListener('click', h)); }
    if (btnCancel) { const h = () => toggleEdit(false); btnCancel.addEventListener('click', h); lifecycle.onCleanup(() => btnCancel.removeEventListener('click', h)); }

    const personalForm = container.querySelector('#form-ult-personal-info');
    if (personalForm) {
      const onSubmit = async (e) => {
        e.preventDefault();
        try {
          const selectedGender = genderSelect?.value || 'Male';
          let avatarUrl = this.profile?.avatarUrl;
          if (!avatarUrl || avatarUrl === 'imgs/male-avatar.png' || avatarUrl === 'imgs/female-avatar.jpg') avatarUrl = selectedGender === 'Female' ? 'imgs/female-avatar.jpg' : 'imgs/male-avatar.png';
          this.profile.avatarUrl = avatarUrl;
          const payload = { firstName: fnameInput?.value || '', lastName: lnameInput?.value || '', phone: phoneInput?.value || '', gender: selectedGender, bankName: bankNameInput?.value || '', bankAccount: bankAccInput?.value || '', ifscCode: ifscInput?.value || '', branchName: branchInput?.value || '', avatarUrl };
          try { const r = await apiClient.put('/api/v1/auth/me', payload); this.profile = r?.success && r.data ? { ...this.profile, ...r.data } : { ...this.profile, ...payload }; }
          catch (err) { logger.warn('UltimateAdminProfile', 'Backend offline:', err); this.profile = { ...this.profile, ...payload }; }
          const updatedFullName = `${this.profile.firstName || ''} ${this.profile.lastName || ''}`.trim() || 'Ultimate Admin Profile';
          this.profile.name = updatedFullName;
          userStore.updateProfile(this.user?.role, this.profile); authStore.updateUser({ name: updatedFullName, avatarUrl: this.profile.avatarUrl });
          const heroNameEl = container.querySelector('#ult-user-fullname'); if (heroNameEl) heroNameEl.textContent = updatedFullName;
          const headerNameEl = document.getElementById('user-name-header'); if (headerNameEl) headerNameEl.textContent = updatedFullName;
          const sidebarNameEl = document.getElementById('user-name-sidebar'); if (sidebarNameEl) sidebarNameEl.textContent = updatedFullName;
          this.render(container); toggleEdit(false);
          notificationStore.success('Ultimate admin profile and banking details saved successfully.');
        } catch (err) { logger.error('UltimateAdminProfile', 'Failed to update profile:', err); }
      };
      personalForm.addEventListener('submit', onSubmit); lifecycle.onCleanup(() => personalForm.removeEventListener('submit', onSubmit));
    }

    // Avatar modal
    const avatarFileInput = container.querySelector('#input-ult-avatar-file');
    const avatarImg = container.querySelector('#ult-avatar-img');
    const avatarBox = container.querySelector('#avatar-container-box') || avatarImg?.parentElement;
    const updateAvatarUrl = (url) => {
      if (avatarImg) avatarImg.src = url;
      const ha = document.getElementById('user-avatar-header'); if (ha) ha.src = url;
      this.profile.avatarUrl = url; userStore.updateProfile(this.user?.role, { avatarUrl: url }); authStore.updateUser({ avatarUrl: url });
      notificationStore.success('Profile avatar updated successfully.');
    };
    const openAvatarModal = () => {
      const old = document.getElementById('modal-avatar-choice'); if (old) old.remove();
      const overlay = document.createElement('div'); overlay.id = 'modal-avatar-choice';
      overlay.style.cssText = 'position:fixed;inset:0;z-index:99999;background:rgba(0,0,0,0.75);backdrop-filter:blur(12px);display:flex;align-items:center;justify-content:center;padding:20px;';
      overlay.innerHTML = `<div style="background:#18181b;border:1px solid rgba(217,70,239,0.3);border-radius:16px;padding:24px;max-width:440px;width:100%;box-shadow:0 20px 50px rgba(0,0,0,0.6),0 0 40px rgba(217,70,239,0.1);color:#fff;display:flex;flex-direction:column;gap:18px;">
        <div style="display:flex;align-items:center;justify-content:space-between;border-bottom:1px solid rgba(255,255,255,0.08);padding-bottom:12px;"><div style="display:flex;align-items:center;gap:10px;"><span style="font-size:1.3rem;">🖼️</span><h3 style="margin:0;font-size:1.05rem;font-weight:800;">Change Profile Avatar</h3></div><button id="btn-close-avatar-modal" style="background:transparent;border:none;color:#a1a1aa;font-size:1.2rem;cursor:pointer;">✕</button></div>
        <div style="display:flex;flex-direction:column;gap:12px;">
          <button id="btn-option-custom-avatar" style="padding:14px 16px;border-radius:12px;background:rgba(255,255,255,0.04);border:1px solid rgba(255,255,255,0.1);color:#fff;display:flex;align-items:center;justify-content:space-between;cursor:pointer;"><div style="display:flex;align-items:center;gap:12px;"><span style="font-size:1.4rem;">📁</span><div style="text-align:left;"><strong style="font-size:0.88rem;display:block;">Upload Custom Image</strong><span style="font-size:0.72rem;color:#a1a1aa;">From your device</span></div></div><span style="color:#d946ef;font-weight:800;">→</span></button>
          <div style="border-radius:12px;background:rgba(255,255,255,0.02);border:1px solid rgba(255,255,255,0.08);padding:14px;display:flex;flex-direction:column;gap:10px;"><span style="font-size:0.72rem;font-weight:800;color:#a1a1aa;text-transform:uppercase;">System Default:</span><div style="display:grid;grid-template-columns:repeat(2,1fr);gap:10px;"><button id="btn-default-male" style="padding:10px;border-radius:10px;background:rgba(24,24,27,0.8);border:1px solid rgba(255,255,255,0.1);color:#fff;display:flex;align-items:center;gap:10px;cursor:pointer;"><img src="imgs/male-avatar.png" style="width:32px;height:32px;border-radius:50%;object-fit:cover;"><span style="font-size:0.78rem;font-weight:700;">👨 Male</span></button><button id="btn-default-female" style="padding:10px;border-radius:10px;background:rgba(24,24,27,0.8);border:1px solid rgba(255,255,255,0.1);color:#fff;display:flex;align-items:center;gap:10px;cursor:pointer;"><img src="imgs/female-avatar.jpg" style="width:32px;height:32px;border-radius:50%;object-fit:cover;"><span style="font-size:0.78rem;font-weight:700;">👩 Female</span></button></div></div>
        </div>
        <div style="display:flex;justify-content:flex-end;"><button id="btn-cancel-avatar-modal" style="padding:8px 16px;border-radius:8px;background:transparent;border:1px solid rgba(255,255,255,0.1);color:#a1a1aa;font-size:0.8rem;cursor:pointer;">Cancel</button></div>
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
        const reader = new FileReader(); reader.onload = (evt) => updateAvatarUrl(evt.target.result); reader.readAsDataURL(file);
        try { const res = await fetch('/api/upload-avatar', { method: 'POST', headers: { 'Content-Type': file.type || 'image/png', 'X-Role': this.user?.role || 'ultimateAdmin' }, body: file }); const data = await res.json(); if (data.success && data.url) updateAvatarUrl(data.url); }
        catch (err) { logger.error('UltimateAdminProfile', 'Avatar upload error:', err); }
      };
      avatarFileInput.addEventListener('change', onFileChange); lifecycle.onCleanup(() => avatarFileInput.removeEventListener('change', onFileChange));
    }
  }
}
