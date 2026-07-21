/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Shared Profile Module
 * File              : documentSchema.js
 * Path              : frontend/shared/profile/documentSchema.js
 * Purpose           : Master configuration schema and utility functions for multi-region compliance documents across India, EU, and UAE nations; defines 12 to 14 document categories tailored for Store Employees, Store Admins, Regional Admins, and National Admins.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Master document classification schema used by DocumentHubComponent.js.
 * Features:
 *   - Exports REGIONS constant defining regional metadata (India, EU, UAE flags and currencies).
 *   - Exports detectUserRegion() helper to dynamically detect user country from profile/user attributes.
 *   - Exports DOCUMENT_SCHEMAS master matrix mapping role types (storeEmployee, storeAdmin, regionalAdmin, nationalAdmin) and regional keys to structured document category lists.
 ******************************************************************************/

export const REGIONS = {
  INDIA: { id: 'INDIA', name: 'India', flag: '🇮🇳', currency: '₹' },
  EU: { id: 'EU', name: 'European Union (EU)', flag: '🇪🇺', currency: '€' },
  UAE: { id: 'UAE', name: 'United Arab Emirates (UAE)', flag: '🇦🇪', currency: 'AED' }
};

export function detectUserRegion(profile = {}, user = {}) {
  const text = String(profile.country || profile.storeRegion || profile.store || user.country || user.storeRegion || '').toLowerCase();
  if (text.includes('uae') || text.includes('dubai') || text.includes('emirate') || text.includes('ae')) {
    return 'UAE';
  }
  if (text.includes('france') || text.includes('eu') || text.includes('europe') || text.includes('paris') || text.includes('fr')) {
    return 'EU';
  }
  return 'INDIA';
}

export const DOCUMENT_SCHEMAS = {
  
  // ============================================================
  // 1. STORE EMPLOYEE / BARISTA DOCUMENT MATRIX (12 Categories)
  // ============================================================
  storeEmployee: {
    INDIA: [
      { category: 'Personal', key: 'in_emp_photo', name: 'Passport Photo', mandatory: true, expiry: false },
      { category: 'Personal', key: 'in_aadhaar', name: 'Aadhaar Card (UIDAI)', mandatory: true, expiry: false },
      { category: 'Personal', key: 'in_passport', name: 'Passport', mandatory: false, expiry: true },
      { category: 'Personal', key: 'in_voter_dl', name: 'Voter ID / Driving License', mandatory: false, expiry: true },
      
      { category: 'Address', key: 'in_utility_bill', name: 'Utility Bill / Residence Proof', mandatory: true, expiry: false },
      { category: 'Address', key: 'in_rental_agree', name: 'Rental Agreement', mandatory: false, expiry: true },
      
      { category: 'Education', key: 'in_edu_10th', name: '10th Secondary Certificate', mandatory: true, expiry: false },
      { category: 'Education', key: 'in_edu_12th', name: '12th Higher Secondary Certificate', mandatory: true, expiry: false },
      { category: 'Education', key: 'in_edu_degree', name: 'Degree / Diploma Certificate', mandatory: true, expiry: false },
      
      { category: 'Employment', key: 'in_resume', name: 'Resume / Curriculum Vitae', mandatory: true, expiry: false },
      { category: 'Employment', key: 'in_offer_letter', name: 'Offer Letter & Employment Agreement', mandatory: true, expiry: false },
      { category: 'Employment', key: 'in_exp_relieving', name: 'Experience & Relieving Letter', mandatory: false, expiry: false },
      
      { category: 'Banking', key: 'in_bank_cheque', name: 'Bank Passbook / Cancelled Cheque', mandatory: true, expiry: false },
      
      { category: 'Tax', key: 'in_pan', name: 'PAN Card', mandatory: true, expiry: false },
      { category: 'Tax', key: 'in_tax_decl', name: 'Form 12BB / Tax Declaration Form', mandatory: false, expiry: false },
      
      { category: 'Social Security', key: 'in_uan', name: 'UAN (Provident Fund Account)', mandatory: true, expiry: false },
      { category: 'Social Security', key: 'in_esi', name: 'ESI Insurance Card / Registration', mandatory: false, expiry: false },
      
      { category: 'Medical', key: 'in_med_fit', name: 'Medical Fitness Certificate', mandatory: false, expiry: true },
      { category: 'Medical', key: 'in_food_cert', name: 'FSSAI Food Handler Certificate', mandatory: true, expiry: true },
      
      { category: 'Legal', key: 'in_nda', name: 'Non-Disclosure Agreement (NDA)', mandatory: true, expiry: false },
      { category: 'Legal', key: 'in_police_verif', name: 'Police Verification Record', mandatory: false, expiry: false },
      
      { category: 'Immigration', key: 'in_visa_permit', name: 'Work Permit & Visa (Foreign Expats)', mandatory: false, expiry: true },
      
      { category: 'Driving', key: 'in_dl', name: 'Driving License / Commercial License', mandatory: false, expiry: true },
      
      { category: 'Emergency', key: 'in_emergency_contact', name: 'Emergency Contact Form', mandatory: true, expiry: false }
    ],

    EU: [
      { category: 'Personal', key: 'eu_emp_photo', name: 'Passport Photo', mandatory: true, expiry: false },
      { category: 'Personal', key: 'eu_cni', name: 'National Identity Card', mandatory: true, expiry: true },
      { category: 'Personal', key: 'eu_passport', name: 'Passport Copy', mandatory: true, expiry: true },
      
      { category: 'Address', key: 'eu_proof_address', name: 'Proof of Address (Utility Bill / Cert)', mandatory: true, expiry: true },
      
      { category: 'Education', key: 'eu_school_cert', name: 'School Leaving Certificate', mandatory: true, expiry: false },
      { category: 'Education', key: 'eu_degree', name: 'University Degree / Diploma', mandatory: true, expiry: false },
      
      { category: 'Employment', key: 'eu_cv', name: 'Resume / Curriculum Vitae', mandatory: true, expiry: false },
      { category: 'Employment', key: 'eu_contract', name: 'Employment Contract', mandatory: true, expiry: false },
      { category: 'Employment', key: 'eu_work_certs', name: 'Previous Employment Certificates', mandatory: false, expiry: false },
      
      { category: 'Banking', key: 'eu_rib', name: 'RIB / IBAN Bank Details', mandatory: true, expiry: false },
      
      { category: 'Tax', key: 'eu_tin', name: 'Tax Identification Number (TIN)', mandatory: true, expiry: false },
      
      { category: 'Social Security', key: 'eu_secu', name: 'Carte Vitale / Social Security Registration', mandatory: true, expiry: false },
      
      { category: 'Medical', key: 'eu_med_fit', name: 'Medical Fitness Certificate', mandatory: true, expiry: true },
      { category: 'Medical', key: 'eu_food_hygiene', name: 'Food Hygiene Training Certificate (HACCP)', mandatory: true, expiry: true },
      
      { category: 'Legal', key: 'eu_nda', name: 'NDA & Employment Agreement', mandatory: true, expiry: false },
      
      { category: 'Immigration', key: 'eu_residence_permit', name: 'Residence Permit & Work Authorization', mandatory: false, expiry: true },
      
      { category: 'Driving', key: 'eu_dl', name: 'EU Driving License', mandatory: false, expiry: true },
      
      { category: 'Emergency', key: 'eu_emergency_contact', name: 'Emergency Contact Form', mandatory: true, expiry: false }
    ],

    UAE: [
      { category: 'Personal', key: 'uae_emp_photo', name: 'Passport Photo (White Background)', mandatory: true, expiry: false },
      { category: 'Personal', key: 'uae_emirates_id', name: 'Emirates ID Card (Front & Back)', mandatory: true, expiry: true },
      { category: 'Personal', key: 'uae_passport', name: 'Passport Copy (6 Mos Validity)', mandatory: true, expiry: true },
      
      { category: 'Address', key: 'uae_tenancy', name: 'Tenancy Contract / Ejari', mandatory: true, expiry: true },
      
      { category: 'Education', key: 'uae_attested_degree', name: 'Attested Degree / Diploma Certificate', mandatory: true, expiry: false },
      
      { category: 'Employment', key: 'uae_cv', name: 'Resume / Curriculum Vitae', mandatory: true, expiry: false },
      { category: 'Employment', key: 'uae_contract', name: 'MOL Employment Contract', mandatory: true, expiry: false },
      
      { category: 'Banking', key: 'uae_iban', name: 'UAE Bank Account IBAN Certificate', mandatory: true, expiry: false },
      
      { category: 'Tax', key: 'uae_tax_doc', name: 'Tax Identification Certificate (If Applicable)', mandatory: false, expiry: false },
      
      { category: 'Social Security', key: 'uae_pension', name: 'GPSSA Pension Registration (UAE Nationals)', mandatory: false, expiry: false },
      
      { category: 'Medical', key: 'uae_med_fit', name: 'DHA / DOH Medical Fitness Certificate', mandatory: true, expiry: true },
      { category: 'Medical', key: 'uae_health_card', name: 'Essential Health Insurance Card', mandatory: true, expiry: true },
      { category: 'Medical', key: 'uae_food_handler', name: 'Occupational Health / Food Handler Card', mandatory: true, expiry: true },
      
      { category: 'Legal', key: 'uae_nda', name: 'NDA & Code of Conduct Agreement', mandatory: true, expiry: false },
      
      { category: 'Immigration', key: 'uae_residence_visa', name: 'UAE Stamped Residence Visa', mandatory: true, expiry: true },
      { category: 'Immigration', key: 'uae_work_permit', name: 'Work Permit / Labour Card', mandatory: true, expiry: true },
      
      { category: 'Driving', key: 'uae_dl', name: 'UAE Driving License', mandatory: false, expiry: true },
      
      { category: 'Emergency', key: 'uae_emergency_contact', name: 'Emergency Contact Form', mandatory: true, expiry: false }
    ]
  },

  // ============================================================
  // 2. STORE OUTLET ADMIN DOCUMENT MATRIX (13 Categories)
  // ============================================================
  storeAdmin: {
    INDIA: [
      { category: 'Business Registration', key: 'in_st_bus_reg', name: 'Business Registration Certificate', mandatory: true, expiry: false },
      { category: 'Business Registration', key: 'in_st_gst', name: 'GST Registration Certificate', mandatory: true, expiry: false },
      { category: 'Business Registration', key: 'in_st_pan', name: 'Store Entity PAN Card', mandatory: true, expiry: false },
      
      { category: 'Franchise', key: 'in_st_franchise_agree', name: 'Franchise Agreement', mandatory: true, expiry: true },
      { category: 'Franchise', key: 'in_st_brand_license', name: 'Brand License Authorization', mandatory: true, expiry: true },
      
      { category: 'Property', key: 'in_st_lease', name: 'Store Property Lease / Ownership Deed', mandatory: true, expiry: true },
      
      { category: 'Store Licenses', key: 'in_st_shop_est', name: 'Shop & Establishment License', mandatory: true, expiry: true },
      { category: 'Store Licenses', key: 'in_st_trade_lic', name: 'Municipal Trade License', mandatory: true, expiry: true },
      
      { category: 'Food Safety', key: 'in_st_fssai', name: 'FSSAI Food Business License', mandatory: true, expiry: true },
      
      { category: 'Safety & Compliance', key: 'in_st_fire_noc', name: 'Fire Department NOC', mandatory: true, expiry: true },
      { category: 'Safety & Compliance', key: 'in_st_elec_safety', name: 'Electrical Safety Certificate', mandatory: true, expiry: true },
      { category: 'Safety & Compliance', key: 'in_st_pest_control', name: 'Pest Control Service Certificate', mandatory: true, expiry: true },
      
      { category: 'Insurance', key: 'in_st_shop_insurance', name: 'Comprehensive Shop & Fire Insurance', mandatory: true, expiry: true },
      { category: 'Insurance', key: 'in_st_public_liab', name: 'Public Liability Insurance Policy', mandatory: true, expiry: true },
      
      { category: 'Utilities', key: 'in_st_power_water', name: 'Electricity & Water Connection Agreement', mandatory: true, expiry: false },
      
      { category: 'Banking', key: 'in_st_bank_cheque', name: 'Store Bank Account Cancelled Cheque', mandatory: true, expiry: false },
      
      { category: 'Suppliers', key: 'in_st_vendor_agree', name: 'Coffee Bean & Ingredient Supply Contract', mandatory: true, expiry: true },
      
      { category: 'Equipment', key: 'in_st_espresso_amc', name: 'Espresso Machine AMC & Maintenance Cert', mandatory: true, expiry: true },
      
      { category: 'Legal', key: 'in_st_compliance_cert', name: 'Local Municipal Compliance Certificate', mandatory: true, expiry: false },
      
      { category: 'Operations', key: 'in_st_sop_checklists', name: 'Daily Opening/Closing Checklists & Audits', mandatory: true, expiry: false }
    ],

    EU: [
      { category: 'Business Registration', key: 'eu_st_comp_reg', name: 'Company Registration Certificate', mandatory: true, expiry: false },
      { category: 'Business Registration', key: 'eu_st_vat', name: 'VAT Registration Certificate', mandatory: true, expiry: false },
      
      { category: 'Franchise', key: 'eu_st_franchise_agree', name: 'Franchise Operating Contract', mandatory: true, expiry: true },
      { category: 'Franchise', key: 'eu_st_brand_license', name: 'Brand License Authorization', mandatory: true, expiry: true },
      
      { category: 'Property', key: 'eu_st_lease', name: 'Commercial Lease Agreement (Bail Commercial)', mandatory: true, expiry: true },
      
      { category: 'Store Licenses', key: 'eu_st_trade_permit', name: 'Local Business Trade Permit', mandatory: true, expiry: true },
      
      { category: 'Food Safety', key: 'eu_st_haccp', name: 'Food Establishment HACCP Documentation', mandatory: true, expiry: true },
      { category: 'Food Safety', key: 'eu_st_hygiene', name: 'Food Hygiene Certification', mandatory: true, expiry: true },
      
      { category: 'Safety & Compliance', key: 'eu_st_fire_safety', name: 'Fire Safety & Electrical Clearance', mandatory: true, expiry: true },
      
      { category: 'Insurance', key: 'eu_st_insurance', name: 'Business & Public Liability Insurance', mandatory: true, expiry: true },
      
      { category: 'Utilities', key: 'eu_st_utility_contracts', name: 'Utility Service Contracts (Power/Water)', mandatory: true, expiry: false },
      
      { category: 'Banking', key: 'eu_st_iban', name: 'Business Bank Account IBAN Certificate', mandatory: true, expiry: false },
      
      { category: 'Suppliers', key: 'eu_st_procurement', name: 'Master Supplier Procurement Agreements', mandatory: true, expiry: true },
      
      { category: 'Equipment', key: 'eu_st_equipment_maint', name: 'Equipment Warranty & Maintenance Certs', mandatory: true, expiry: true },
      
      { category: 'Legal', key: 'eu_st_env_compliance', name: 'Environmental & Local Authority Permits', mandatory: true, expiry: false },
      
      { category: 'Operations', key: 'eu_st_temp_logs', name: 'Temperature Logs & Daily Audit Checklists', mandatory: true, expiry: false }
    ],

    UAE: [
      { category: 'Business Registration', key: 'uae_st_trade_lic', name: 'DED Trade License Copy', mandatory: true, expiry: true },
      { category: 'Business Registration', key: 'uae_st_vat', name: 'FTA VAT Registration Certificate', mandatory: true, expiry: false },
      
      { category: 'Franchise', key: 'uae_st_franchise_agree', name: 'Franchise Agreement & Approval Letter', mandatory: true, expiry: true },
      
      { category: 'Property', key: 'uae_st_ejari', name: 'Tenancy Contract & Ejari Registration', mandatory: true, expiry: true },
      
      { category: 'Store Licenses', key: 'uae_st_muni_lic', name: 'Municipality Commercial License', mandatory: true, expiry: true },
      
      { category: 'Food Safety', key: 'uae_st_food_lic', name: 'Food Establishment License & Approval', mandatory: true, expiry: true },
      
      { category: 'Safety & Compliance', key: 'uae_st_civil_defence', name: 'Civil Defence Fire Safety Certificate', mandatory: true, expiry: true },
      { category: 'Safety & Compliance', key: 'uae_st_pest', name: 'Pest Control Clearance Certificate', mandatory: true, expiry: true },
      
      { category: 'Insurance', key: 'uae_st_insurance', name: 'Comprehensive Property & Liability Insurance', mandatory: true, expiry: true },
      
      { category: 'Utilities', key: 'uae_st_dewa', name: 'DEWA Connection & Utility Documents', mandatory: true, expiry: false },
      
      { category: 'Banking', key: 'uae_st_iban', name: 'Corporate Bank IBAN Certificate', mandatory: true, expiry: false },
      
      { category: 'Suppliers', key: 'uae_st_vendor_agree', name: 'Registered Supply Contracts', mandatory: true, expiry: true },
      
      { category: 'Equipment', key: 'uae_st_espresso_amc', name: 'Coffee Machine AMC & Warranty Service Records', mandatory: true, expiry: true },
      
      { category: 'Legal', key: 'uae_st_gov_permits', name: 'Government & Municipality Permits', mandatory: true, expiry: false },
      
      { category: 'Operations', key: 'uae_st_audit_records', name: 'Opening/Closing Checklists & Audit Logs', mandatory: true, expiry: false }
    ]
  },

  // ============================================================
  // 3. REGIONAL HEAD OFFICE DOCUMENT MATRIX (14 Categories)
  // ============================================================
  regionalAdmin: {
    INDIA: [
      { category: 'Business Registration', key: 'in_reg_comp_reg', name: 'Company Incorporation & PAN', mandatory: true, expiry: false },
      { category: 'Regional Authorization', key: 'in_reg_appoint_letter', name: 'Regional Manager Appointment Letter', mandatory: true, expiry: false },
      { category: 'Franchise', key: 'in_reg_master_franchise', name: 'Master Franchise Agreement', mandatory: true, expiry: true },
      { category: 'Office Property', key: 'in_reg_lease', name: 'Regional Office Lease Agreement', mandatory: true, expiry: true },
      { category: 'Office Licenses', key: 'in_reg_shop_est', name: 'Regional Office Trade License', mandatory: true, expiry: true },
      { category: 'Tax', key: 'in_reg_gst_tan', name: 'GST & TAN Certificate', mandatory: true, expiry: false },
      { category: 'Banking', key: 'in_reg_bank_mandate', name: 'Regional Bank Account Mandates', mandatory: true, expiry: false },
      { category: 'HR & Payroll', key: 'in_reg_pf_esi', name: 'Regional PF & ESI Registration', mandatory: true, expiry: false },
      { category: 'Legal', key: 'in_reg_compliance', name: 'Compliance Certs & Vendor NDAs', mandatory: true, expiry: false },
      { category: 'Insurance', key: 'in_reg_office_insurance', name: 'Office Asset & Liability Insurance', mandatory: true, expiry: true },
      { category: 'Vendors & Procurement', key: 'in_reg_procurement', name: 'Regional Procurement Contracts', mandatory: true, expiry: true },
      { category: 'Fleet', key: 'in_reg_fleet_insurance', name: 'Regional Logistics & Vehicle Records', mandatory: false, expiry: true },
      { category: 'Finance', key: 'in_reg_audit_reports', name: 'Regional Budget & Audit Reports', mandatory: true, expiry: false },
      { category: 'Operations', key: 'in_reg_sops', name: 'Regional Store SOPs & Inspection Reports', mandatory: true, expiry: false }
    ],

    EU: [
      { category: 'Business Registration', key: 'eu_reg_comp_reg', name: 'EU Company Registration & VAT', mandatory: true, expiry: false },
      { category: 'Regional Authorization', key: 'eu_reg_director_appoint', name: 'Regional Director Appointment Letter', mandatory: true, expiry: false },
      { category: 'Franchise', key: 'eu_reg_master_franchise', name: 'Master Regional Franchise Contract', mandatory: true, expiry: true },
      { category: 'Office Property', key: 'eu_reg_lease', name: 'Regional HQ Office Lease', mandatory: true, expiry: true },
      { category: 'Office Licenses', key: 'eu_reg_business_permit', name: 'Local Business Registration Permit', mandatory: true, expiry: true },
      { category: 'Tax', key: 'eu_reg_tin', name: 'Tax Identification Number (TIN)', mandatory: true, expiry: false },
      { category: 'Banking', key: 'eu_reg_iban', name: 'Regional Corporate IBAN Account', mandatory: true, expiry: false },
      { category: 'HR & Payroll', key: 'eu_reg_social_sec', name: 'Social Security Employer Registration', mandatory: true, expiry: false },
      { category: 'Legal', key: 'eu_reg_gdpr_compliance', name: 'GDPR & Legal Compliance Documents', mandatory: true, expiry: false },
      { category: 'Insurance', key: 'eu_reg_insurance', name: 'Property & Public Liability Insurance', mandatory: true, expiry: true },
      { category: 'Vendors & Procurement', key: 'eu_reg_procurement', name: 'Regional Supplier Master Agreements', mandatory: true, expiry: true },
      { category: 'Fleet', key: 'eu_reg_fleet', name: 'Vehicle Fleet Insurance & Reg', mandatory: false, expiry: true },
      { category: 'Finance', key: 'eu_reg_financial_stmt', name: 'Financial Statements & Audit Reports', mandatory: true, expiry: false },
      { category: 'Operations', key: 'eu_reg_kpi_reports', name: 'Regional SOPs & Compliance Reports', mandatory: true, expiry: false }
    ],

    UAE: [
      { category: 'Business Registration', key: 'uae_reg_trade_lic', name: 'DED Commercial Trade License', mandatory: true, expiry: true },
      { category: 'Regional Authorization', key: 'uae_reg_appoint_letter', name: 'Regional Manager Appointment Decree', mandatory: true, expiry: false },
      { category: 'Franchise', key: 'uae_reg_master_franchise', name: 'Regional Master Franchise Agreement', mandatory: true, expiry: true },
      { category: 'Office Property', key: 'uae_reg_ejari', name: 'Office Tenancy Contract (Ejari)', mandatory: true, expiry: true },
      { category: 'Office Licenses', key: 'uae_reg_muni_lic', name: 'Municipality Commercial License', mandatory: true, expiry: true },
      { category: 'Tax', key: 'uae_reg_vat_cert', name: 'FTA VAT Registration Certificate', mandatory: true, expiry: false },
      { category: 'Banking', key: 'uae_reg_iban', name: 'Corporate Bank Account IBAN Certificate', mandatory: true, expiry: false },
      { category: 'HR & Payroll', key: 'uae_reg_labour_reg', name: 'Ministry of Labour Registration', mandatory: true, expiry: false },
      { category: 'Legal', key: 'uae_reg_legal_compliance', name: 'Corporate NDAs & Legal Certs', mandatory: true, expiry: false },
      { category: 'Insurance', key: 'uae_reg_insurance', name: 'Office & Asset Insurance Policy', mandatory: true, expiry: true },
      { category: 'Vendors & Procurement', key: 'uae_reg_vendor_contracts', name: 'Regional Service & Vendor Contracts', mandatory: true, expiry: true },
      { category: 'Fleet', key: 'uae_reg_fleet', name: 'Fleet Vehicle Registration & Insurance', mandatory: false, expiry: true },
      { category: 'Finance', key: 'uae_reg_audit_reports', name: 'Financial Reports & VAT Filings', mandatory: true, expiry: false },
      { category: 'Operations', key: 'uae_reg_audit_kpis', name: 'Regional Audit & KPI Performance Logs', mandatory: true, expiry: false }
    ]
  },

  // ============================================================
  // 4. NATIONAL HEAD OFFICE DOCUMENT MATRIX (13 Categories)
  // ============================================================
  nationalAdmin: {
    INDIA: [
      { category: 'Business Registration', key: 'in_nat_coi', name: 'Certificate of Incorporation & CIN', mandatory: true, expiry: false },
      { category: 'Corporate Governance', key: 'in_nat_moa_aoa', name: 'MoA, AoA & Board Resolutions', mandatory: true, expiry: false },
      { category: 'Franchise', key: 'in_nat_master_franchise', name: 'Master Franchise & Brand License Agreement', mandatory: true, expiry: true },
      { category: 'Corporate Licenses', key: 'in_nat_fssai_central', name: 'FSSAI Central Food License', mandatory: true, expiry: true },
      { category: 'Tax', key: 'in_nat_gst_pan', name: 'National GST Certificate & PAN/TAN', mandatory: true, expiry: false },
      { category: 'Banking', key: 'in_nat_corporate_bank', name: 'Corporate Banking Authorizations & Mandates', mandatory: true, expiry: false },
      { category: 'HR & Payroll', key: 'in_nat_hr_policies', name: 'National HR Policies & PF/ESI Registration', mandatory: true, expiry: false },
      { category: 'Legal', key: 'in_nat_trademarks', name: 'Trademark & Copyright Certificates', mandatory: true, expiry: false },
      { category: 'Insurance', key: 'in_nat_corp_insurance', name: 'National Asset & Public Liability Insurance', mandatory: true, expiry: true },
      { category: 'Finance', key: 'in_nat_audited_financials', name: 'Audited Financial Statements & Tax Returns', mandatory: true, expiry: false },
      { category: 'Vendors & Procurement', key: 'in_nat_vendor_agreements', name: 'National Supply Chain Contracts', mandatory: true, expiry: true },
      { category: 'Assets', key: 'in_nat_asset_register', name: 'Enterprise IT & Fixed Asset Register', mandatory: true, expiry: false },
      { category: 'Operations', key: 'in_nat_corporate_sops', name: 'Corporate Master SOPs & National KPI Audit Logs', mandatory: true, expiry: false }
    ],

    EU: [
      { category: 'Business Registration', key: 'eu_nat_comp_reg', name: 'EU Company Registration Certificate', mandatory: true, expiry: false },
      { category: 'Corporate Governance', key: 'eu_nat_aoa_shareholders', name: 'Articles of Association & Board Resolutions', mandatory: true, expiry: false },
      { category: 'Franchise', key: 'eu_nat_master_franchise', name: 'EU Master Franchise Agreement', mandatory: true, expiry: true },
      { category: 'Corporate Licenses', key: 'eu_nat_haccp_lic', name: 'EU Food Safety & HACCP Master Registration', mandatory: true, expiry: true },
      { category: 'Tax', key: 'eu_nat_vat_tin', name: 'EU VAT & National Tax ID (TIN)', mandatory: true, expiry: false },
      { category: 'Banking', key: 'eu_nat_bank_iban', name: 'National Corporate Bank IBAN Mandates', mandatory: true, expiry: false },
      { category: 'HR & Payroll', key: 'eu_nat_hr_policies', name: 'EU HR & Social Insurance Regulations', mandatory: true, expiry: false },
      { category: 'Legal', key: 'eu_nat_gdpr_trademarks', name: 'GDPR Compliance & Trademark Registrations', mandatory: true, expiry: false },
      { category: 'Insurance', key: 'eu_nat_insurance', name: 'Enterprise Property & Liability Insurance', mandatory: true, expiry: true },
      { category: 'Finance', key: 'eu_nat_financials', name: 'Audited Financial Statements & Tax Returns', mandatory: true, expiry: false },
      { category: 'Vendors & Procurement', key: 'eu_nat_procurement', name: 'EU National Supplier Contracts', mandatory: true, expiry: true },
      { category: 'Assets', key: 'eu_nat_asset_inventory', name: 'Enterprise IT Asset Register', mandatory: true, expiry: false },
      { category: 'Operations', key: 'eu_nat_sops', name: 'Corporate Master SOPs & Internal Audit Reports', mandatory: true, expiry: false }
    ],

    UAE: [
      { category: 'Business Registration', key: 'uae_nat_trade_lic', name: 'DED National Commercial Trade License', mandatory: true, expiry: true },
      { category: 'Corporate Governance', key: 'uae_nat_moa', name: 'Memorandum of Association & Shareholder Resolutions', mandatory: true, expiry: false },
      { category: 'Franchise', key: 'uae_nat_master_franchise', name: 'UAE Master Franchise Agreement', mandatory: true, expiry: true },
      { category: 'Corporate Licenses', key: 'uae_nat_muni_lic', name: 'Municipality Food Establishment License', mandatory: true, expiry: true },
      { category: 'Tax', key: 'uae_nat_vat', name: 'FTA National VAT Certificate', mandatory: true, expiry: false },
      { category: 'Banking', key: 'uae_nat_iban', name: 'Corporate Bank Account IBAN Mandate', mandatory: true, expiry: false },
      { category: 'HR & Payroll', key: 'uae_nat_labour_reg', name: 'MOHRE Labour Registration & HR Policies', mandatory: true, expiry: false },
      { category: 'Legal', key: 'uae_nat_trademarks', name: 'Trademark & Brand Intellectual Property Certs', mandatory: true, expiry: false },
      { category: 'Insurance', key: 'uae_nat_insurance', name: 'Corporate Asset & Public Liability Insurance', mandatory: true, expiry: true },
      { category: 'Finance', key: 'uae_nat_financials', name: 'Audited Financial Reports & VAT Returns', mandatory: true, expiry: false },
      { category: 'Vendors & Procurement', key: 'uae_nat_procurement', name: 'National Procurement & Supply Contracts', mandatory: true, expiry: true },
      { category: 'Assets', key: 'uae_nat_assets', name: 'National Fixed & IT Asset Register', mandatory: true, expiry: false },
      { category: 'Operations', key: 'uae_nat_audit_kpis', name: 'Corporate Master SOPs & National Audit Logs', mandatory: true, expiry: false }
    ]
  }

};
