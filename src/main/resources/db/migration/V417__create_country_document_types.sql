-- =============================================================================
-- V417__create_country_document_types.sql
-- Creates country_document_types table and seeds document requirements for
-- India (IN), European Union (EU) and UAE (AE).
--
-- Each row defines one document that employees from that country must or may
-- upload.  The doc_key column must match the documentType string used by
-- employee_upload_documents and the frontend upload system.
-- =============================================================================

-- ---------------------------------------------------------------------------
-- TABLE
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS country_document_types (
    id                BIGSERIAL    PRIMARY KEY,
    country_code      VARCHAR(10)  NOT NULL,           -- 'IN', 'EU', 'AE'
    category          VARCHAR(50)  NOT NULL,           -- e.g. 'personal', 'tax'
    category_label    VARCHAR(100) NOT NULL,           -- human-readable category name
    category_icon     VARCHAR(50)  NOT NULL DEFAULT 'file-text',
    doc_key           VARCHAR(100) NOT NULL,           -- matches documentType in employee_upload_documents
    doc_title         VARCHAR(200) NOT NULL,
    doc_description   TEXT,
    is_required       BOOLEAN      NOT NULL DEFAULT TRUE,
    category_sort     INTEGER      NOT NULL DEFAULT 0, -- ordering among categories
    doc_sort          INTEGER      NOT NULL DEFAULT 0, -- ordering within category
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_cdt_country_code ON country_document_types(country_code);
CREATE INDEX IF NOT EXISTS idx_cdt_category     ON country_document_types(country_code, category);
CREATE UNIQUE INDEX IF NOT EXISTS idx_cdt_unique ON country_document_types(country_code, doc_key);

-- =============================================================================
-- 🇮🇳  INDIA (IN)
-- =============================================================================
INSERT INTO country_document_types
    (country_code, category, category_label, category_icon, doc_key, doc_title, doc_description, is_required, category_sort, doc_sort)
VALUES

-- Personal Documents --------------------------------------------------------
('IN','personal','Personal Documents','user',
 'personalPhoto','Passport Photo',
 'Passport size official colour photograph on white background',
 TRUE,1,1),

('IN','personal','Personal Documents','user',
 'personalAadhaar','Aadhaar Card',
 'Government issued 12-digit Aadhaar identity card (front & back)',
 TRUE,1,2),

('IN','personal','Personal Documents','user',
 'personalPassport','Passport',
 'Valid Indian passport – front bio-data page (if available)',
 FALSE,1,3),

('IN','personal','Personal Documents','user',
 'personalVoterId','Voter ID / Driving License',
 'Voter ID card or valid state driving license as secondary photo ID',
 FALSE,1,4),

-- Address Documents ---------------------------------------------------------
('IN','address','Address Documents','home',
 'addressAadhaar','Aadhaar Address Proof',
 'Aadhaar card showing current residential address',
 TRUE,2,1),

('IN','address','Address Documents','home',
 'addressUtility','Utility Bill',
 'Electricity / Water / Gas bill (not older than 3 months)',
 TRUE,2,2),

('IN','address','Address Documents','home',
 'addressRental','Rental Agreement',
 'Registered rent / lease agreement showing current address',
 FALSE,2,3),

('IN','address','Address Documents','home',
 'addressBankStatement','Bank Statement',
 'Bank statement (latest month) showing residential address',
 FALSE,2,4),

-- Education Documents -------------------------------------------------------
('IN','education','Education Documents','graduation-cap',
 'education10th','10th Certificate',
 'Secondary school (SSC / SSLC / Matric) marksheet and passing certificate',
 TRUE,3,1),

('IN','education','Education Documents','graduation-cap',
 'education12th','12th Certificate',
 'Higher secondary (HSC / Intermediate / PUC) marksheet and passing certificate',
 TRUE,3,2),

('IN','education','Education Documents','graduation-cap',
 'educationDegree','Degree / Diploma Certificate',
 'University degree or professional diploma (Bachelor / Master / Diploma)',
 FALSE,3,3),

('IN','education','Education Documents','graduation-cap',
 'educationSkill','Skill / Training Certificates',
 'Professional courses, vocational training or skill development certificates',
 FALSE,3,4),

-- Employment Documents ------------------------------------------------------
('IN','employment','Employment Documents','briefcase',
 'employmentResume','Resume / CV',
 'Updated professional curriculum vitae',
 TRUE,4,1),

('IN','employment','Employment Documents','briefcase',
 'employmentOffer','Offer Letter',
 'Signed corporate offer letter from PLUS33',
 TRUE,4,2),

('IN','employment','Employment Documents','briefcase',
 'employmentAgreement','Employment Agreement',
 'Signed employee work terms and conditions agreement',
 TRUE,4,3),

('IN','employment','Employment Documents','briefcase',
 'employmentExperience','Experience Letter',
 'Experience certificate from previous employer',
 FALSE,4,4),

('IN','employment','Employment Documents','briefcase',
 'employmentRelieving','Relieving Letter',
 'Formal relieving letter from last employer confirming exit',
 FALSE,4,5),

-- Banking Documents ---------------------------------------------------------
('IN','banking','Banking Documents','credit-card',
 'bankingAccount','Bank Account Details',
 'Passbook front page or printed bank account statement',
 TRUE,5,1),

('IN','banking','Banking Documents','credit-card',
 'bankingCheque','Cancelled Cheque',
 'Scanned image of a cancelled blank cheque from salary account',
 TRUE,5,2),

('IN','banking','Banking Documents','credit-card',
 'bankingPassbook','Passbook Copy',
 'Scanned copy of salary account passbook first page',
 FALSE,5,3),

-- Tax Documents -------------------------------------------------------------
('IN','tax','Tax Documents','receipt',
 'taxPan','PAN Card',
 'Income Tax-issued Permanent Account Number card (mandatory)',
 TRUE,6,1),

('IN','tax','Tax Documents','receipt',
 'taxForms','Tax Declaration Forms',
 'Investment declaration / Form 12BB / tax withholding forms',
 TRUE,6,2),

-- Social Security -----------------------------------------------------------
('IN','social_security','Social Security Documents','shield',
 'socialUan','UAN – Provident Fund',
 'Universal Account Number issued for Employee Provident Fund (PF)',
 TRUE,7,1),

('IN','social_security','Social Security Documents','shield',
 'socialEsi','ESI Card / Number',
 'Employee State Insurance registration document / ESI card',
 TRUE,7,2),

('IN','social_security','Social Security Documents','shield',
 'socialNomination','Nomination Forms',
 'Nomination declaration forms for PF, ESI and Gratuity',
 TRUE,7,3),

-- Medical Documents ---------------------------------------------------------
('IN','medical','Medical Documents','heart-pulse',
 'medicalFitness','Medical Fitness Certificate',
 'Medical fitness certificate signed by a registered doctor',
 TRUE,8,1),

('IN','medical','Medical Documents','heart-pulse',
 'medicalVaccination','Vaccination Records',
 'Mandatory immunization or vaccination certificates',
 TRUE,8,2),

('IN','medical','Medical Documents','heart-pulse',
 'medicalFoodHandler','Food Handler Certificate',
 'State-issued food handler permission card (required for food handling roles)',
 FALSE,8,3),

-- Legal Documents -----------------------------------------------------------
('IN','legal','Legal Documents','file-text',
 'legalNda','NDA',
 'Signed Non-Disclosure confidentiality Agreement',
 TRUE,9,1),

('IN','legal','Legal Documents','file-text',
 'legalCodeOfConduct','Code of Conduct',
 'Signed company code of conduct and ethics acknowledgement',
 TRUE,9,2),

('IN','legal','Legal Documents','file-text',
 'legalBackground','Background Verification',
 'Third-party background screening clearance report',
 TRUE,9,3),

('IN','legal','Legal Documents','file-text',
 'legalPoliceVerification','Police Verification Certificate',
 'Police character clearance certificate (if required for the role)',
 FALSE,9,4),

-- Immigration Documents (Foreign Employees) ---------------------------------
('IN','immigration','Immigration Documents','globe',
 'immigrationPassport','Passport',
 'Valid national passport – for foreign / expatriate employees',
 FALSE,10,1),

('IN','immigration','Immigration Documents','globe',
 'immigrationVisa','Visa',
 'Valid Indian entry visa (Employment / Business) – foreign employees',
 FALSE,10,2),

('IN','immigration','Immigration Documents','globe',
 'immigrationPermit','Work Permit',
 'Government authorized work permit – for foreign employees',
 FALSE,10,3),

-- Driving Documents ---------------------------------------------------------
('IN','driving','Driving Documents','car',
 'drivingLicense','Driving License',
 'Valid government-issued motor driving license',
 FALSE,11,1),

('IN','driving','Driving Documents','car',
 'drivingCommercial','Commercial Driving License',
 'Commercial vehicle / heavy-transport license (for driver roles only)',
 FALSE,11,2),

-- Emergency Documents -------------------------------------------------------
('IN','emergency','Emergency Documents','phone-call',
 'emergencyContact','Emergency Contact Details',
 'Filled emergency contacts sheet with next-of-kin information',
 TRUE,12,1);


-- =============================================================================
-- 🇪🇺  EUROPEAN UNION (EU) — covers France and all EU member states
-- =============================================================================
INSERT INTO country_document_types
    (country_code, category, category_label, category_icon, doc_key, doc_title, doc_description, is_required, category_sort, doc_sort)
VALUES

-- Personal Documents --------------------------------------------------------
('EU','personal','Personal Documents','user',
 'personalPhoto','Passport Photo',
 'Official passport-size colour photograph on white background',
 TRUE,1,1),

('EU','personal','Personal Documents','user',
 'personalNationalId','National Identity Card',
 'Valid EU national identity card (front and back)',
 TRUE,1,2),

('EU','personal','Personal Documents','user',
 'personalPassport','Passport',
 'Valid national passport (required for non-EU employees)',
 FALSE,1,3),

-- Address Documents ---------------------------------------------------------
('EU','address','Address Documents','home',
 'addressUtility','Utility Bill',
 'Electricity / Gas / Water / Internet bill (not older than 3 months)',
 TRUE,2,1),

('EU','address','Address Documents','home',
 'addressRental','Rental Agreement',
 'Registered tenancy / lease agreement showing current address',
 FALSE,2,2),

('EU','address','Address Documents','home',
 'addressResidenceCertificate','Residence Certificate',
 'Official municipal residence certificate (certificat de résidence / Meldebescheinigung)',
 FALSE,2,3),

-- Education Documents -------------------------------------------------------
('EU','education','Education Documents','graduation-cap',
 'educationSchool','School Certificates',
 'Secondary school leaving certificate / Baccalauréat / A-Levels',
 TRUE,3,1),

('EU','education','Education Documents','graduation-cap',
 'educationDegree','University Degree',
 'Bachelor / Master / Doctorate degree certificate and transcripts',
 FALSE,3,2),

('EU','education','Education Documents','graduation-cap',
 'educationProfessional','Professional Certificates',
 'Professional qualifications, vocational training or industry certifications',
 FALSE,3,3),

-- Employment Documents ------------------------------------------------------
('EU','employment','Employment Documents','briefcase',
 'employmentResume','Resume / CV',
 'Updated professional curriculum vitae',
 TRUE,4,1),

('EU','employment','Employment Documents','briefcase',
 'employmentContract','Employment Contract',
 'Signed French / EU-compliant employment contract (CDI / CDD or equivalent)',
 TRUE,4,2),

('EU','employment','Employment Documents','briefcase',
 'employmentExperience','Previous Employment Certificates',
 'Attestation de travail / certificates from previous employers',
 FALSE,4,3),

-- Banking Documents ---------------------------------------------------------
('EU','banking','Banking Documents','credit-card',
 'bankingIban','IBAN Details',
 'International Bank Account Number (IBAN) for salary payments',
 TRUE,5,1),

('EU','banking','Banking Documents','credit-card',
 'bankingAccount','Bank Account Statement',
 'Bank RIB (Relevé d''Identité Bancaire) or equivalent account confirmation',
 TRUE,5,2),

-- Tax Documents -------------------------------------------------------------
('EU','tax','Tax Documents','receipt',
 'taxTin','Tax Identification Number (TIN)',
 'National Tax Identification Number (NIF / Steuernummer / Numéro Fiscal)',
 TRUE,6,1),

('EU','tax','Tax Documents','receipt',
 'taxForms','Tax Declaration Forms',
 'Tax withholding / income declaration forms as required by national law',
 FALSE,6,2),

-- Social Security -----------------------------------------------------------
('EU','social_security','Social Security Documents','shield',
 'socialSecurityNumber','National Social Security Number',
 'National insurance / social security registration number card',
 TRUE,7,1),

('EU','social_security','Social Security Documents','shield',
 'socialInsurance','Social Insurance Registration',
 'Proof of social insurance enrollment (CPAM / health fund registration)',
 TRUE,7,2),

-- Medical Documents ---------------------------------------------------------
('EU','medical','Medical Documents','heart-pulse',
 'medicalFitness','Medical Fitness Certificate',
 'Visite médicale / occupational medical fitness clearance (if required)',
 FALSE,8,1),

('EU','medical','Medical Documents','heart-pulse',
 'medicalFoodHandler','Food Handler Certificate',
 'Food hygiene / food handler certificate required for food roles',
 TRUE,8,2),

('EU','medical','Medical Documents','heart-pulse',
 'medicalHealthInsurance','Health Insurance Details',
 'Carte Vitale / private health insurance card or mutual (mutuelle) document',
 TRUE,8,3),

-- Legal Documents -----------------------------------------------------------
('EU','legal','Legal Documents','file-text',
 'legalNda','NDA',
 'Signed Non-Disclosure Agreement (accord de confidentialité)',
 TRUE,9,1),

('EU','legal','Legal Documents','file-text',
 'legalAgreement','Employment Agreement',
 'Signed company-specific supplemental agreement and work rules',
 TRUE,9,2),

('EU','legal','Legal Documents','file-text',
 'legalBackground','Background Check',
 'Criminal record extract (casier judiciaire / Führungszeugnis) as required',
 FALSE,9,3),

-- Immigration Documents (Non-EU only) ---------------------------------------
('EU','immigration','Immigration Documents','globe',
 'immigrationPassport','Passport',
 'Valid national passport – for non-EU employees',
 FALSE,10,1),

('EU','immigration','Immigration Documents','globe',
 'immigrationResidencePermit','Residence Permit',
 'Titre de séjour / Aufenthaltstitel / EU residence permit',
 FALSE,10,2),

('EU','immigration','Immigration Documents','globe',
 'immigrationPermit','Work Permit',
 'Autorisation de travail / EU work authorization permit',
 FALSE,10,3),

-- Driving Documents ---------------------------------------------------------
('EU','driving','Driving Documents','car',
 'drivingLicense','EU Driving License',
 'Valid European driving license (Category B or higher)',
 FALSE,11,1),

('EU','driving','Driving Documents','car',
 'drivingCommercial','Commercial Driving License',
 'Professional HGV / commercial vehicle license (for driver roles only)',
 FALSE,11,2),

-- Emergency Documents -------------------------------------------------------
('EU','emergency','Emergency Documents','phone-call',
 'emergencyContact','Emergency Contact Details',
 'Filled emergency contacts sheet with next-of-kin information',
 TRUE,12,1);


-- =============================================================================
-- 🇦🇪  UNITED ARAB EMIRATES (AE)
-- =============================================================================
INSERT INTO country_document_types
    (country_code, category, category_label, category_icon, doc_key, doc_title, doc_description, is_required, category_sort, doc_sort)
VALUES

-- Personal Documents --------------------------------------------------------
('AE','personal','Personal Documents','user',
 'personalPhoto','Passport Photo',
 'Official passport-size colour photograph on white background',
 TRUE,1,1),

('AE','personal','Personal Documents','user',
 'personalEmiratesId','Emirates ID',
 'Valid UAE National Identity / Emirates ID card (front and back)',
 TRUE,1,2),

('AE','personal','Personal Documents','user',
 'personalPassport','Passport',
 'Valid national passport – mandatory for visa and employment registration',
 TRUE,1,3),

-- Address Documents ---------------------------------------------------------
('AE','address','Address Documents','home',
 'addressEmiratesId','Emirates ID (Address Proof)',
 'Emirates ID card showing registered UAE address',
 TRUE,2,1),

('AE','address','Address Documents','home',
 'addressTenancy','Tenancy Contract (Ejari)',
 'Ejari-registered tenancy contract for current UAE residence',
 FALSE,2,2),

('AE','address','Address Documents','home',
 'addressUtility','Utility Bill (DEWA / SEWA)',
 'DEWA / SEWA / ADDC electricity or water bill (not older than 3 months)',
 FALSE,2,3),

-- Education Documents -------------------------------------------------------
('AE','education','Education Documents','graduation-cap',
 'educationSchool','School Certificates',
 'Secondary school leaving certificate (attested if issued outside UAE)',
 TRUE,3,1),

('AE','education','Education Documents','graduation-cap',
 'educationDegree','Degree Certificate',
 'University degree / diploma (MOHESR-attested if issued outside UAE)',
 FALSE,3,2),

('AE','education','Education Documents','graduation-cap',
 'educationProfessional','Professional Certificates',
 'Industry or vocational certifications relevant to the role',
 FALSE,3,3),

-- Employment Documents ------------------------------------------------------
('AE','employment','Employment Documents','briefcase',
 'employmentResume','Resume / CV',
 'Updated professional curriculum vitae',
 TRUE,4,1),

('AE','employment','Employment Documents','briefcase',
 'employmentContract','Employment Contract',
 'MOL-registered UAE employment contract (signed by both parties)',
 TRUE,4,2),

('AE','employment','Employment Documents','briefcase',
 'employmentExperience','Experience Certificate',
 'Experience or reference letter from previous employer',
 FALSE,4,3),

-- Banking Documents ---------------------------------------------------------
('AE','banking','Banking Documents','credit-card',
 'bankingUaeAccount','UAE Bank Account Details',
 'UAE bank account number and branch details for WPS salary transfer',
 TRUE,5,1),

('AE','banking','Banking Documents','credit-card',
 'bankingIban','IBAN',
 'UAE IBAN for salary payments via WPS',
 TRUE,5,2),

-- Tax Documents (limited in UAE) --------------------------------------------
('AE','tax','Tax Documents','receipt',
 'taxDocuments','Tax Identification Documents',
 'TIN / tax registration documents if applicable (non-UAE nationals)',
 FALSE,6,1),

-- Social Security (UAE Nationals) -------------------------------------------
('AE','social_security','Social Security Documents','shield',
 'socialPension','Pension / GPSSA Documents',
 'General Pension & Social Security Authority registration (UAE nationals)',
 FALSE,7,1),

-- Medical Documents ---------------------------------------------------------
('AE','medical','Medical Documents','heart-pulse',
 'medicalFitness','Medical Fitness Certificate',
 'DHA / MOH medical fitness clearance certificate',
 TRUE,8,1),

('AE','medical','Medical Documents','heart-pulse',
 'medicalHealthInsurance','Health Insurance Card',
 'Valid UAE health insurance card / policy (DHA / HAAD / Thiqa)',
 TRUE,8,2),

('AE','medical','Medical Documents','heart-pulse',
 'medicalFoodHandler','Food Handler Certificate',
 'Dubai Municipality or Abu Dhabi food handler training certificate',
 TRUE,8,3),

-- Legal Documents -----------------------------------------------------------
('AE','legal','Legal Documents','file-text',
 'legalNda','NDA',
 'Signed Non-Disclosure Agreement',
 TRUE,9,1),

('AE','legal','Legal Documents','file-text',
 'legalAgreement','Employment Agreement',
 'Supplemental company policy and conduct agreement',
 TRUE,9,2),

('AE','legal','Legal Documents','file-text',
 'legalBackground','Background Verification',
 'Background / police clearance check from country of origin',
 FALSE,9,3),

-- Immigration Documents (Mandatory for expats) ------------------------------
('AE','immigration','Immigration Documents','globe',
 'immigrationPassport','Passport',
 'Valid national passport copy (mandatory for all employees)',
 TRUE,10,1),

('AE','immigration','Immigration Documents','globe',
 'immigrationResidenceVisa','Residence Visa',
 'UAE Residence Visa (Entry permit / stamped visa page)',
 TRUE,10,2),

('AE','immigration','Immigration Documents','globe',
 'immigrationPermit','Work Permit / Labour Card',
 'Ministry of Human Resources (MOHRE) work permit / e-visa card',
 TRUE,10,3),

-- Driving Documents ---------------------------------------------------------
('AE','driving','Driving Documents','car',
 'drivingLicense','UAE Driving License',
 'Valid UAE driving license (or home country license if transferable)',
 FALSE,11,1),

('AE','driving','Driving Documents','car',
 'drivingCommercial','Commercial Driving License',
 'UAE heavy vehicle / commercial transport license (for driver roles only)',
 FALSE,11,2),

-- Emergency Documents -------------------------------------------------------
('AE','emergency','Emergency Documents','phone-call',
 'emergencyContact','Emergency Contact Details',
 'Emergency contact information sheet with next-of-kin details',
 TRUE,12,1);
