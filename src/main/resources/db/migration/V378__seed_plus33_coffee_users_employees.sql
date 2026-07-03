-- ============================================================
-- V378__seed_plus33_coffee_users_employees.sql
-- PLUS33 ERP — Users, Employees, Shifts, Leave Types
-- ============================================================

-- ── Shifts (3) ──────────────────────────────────────────────
INSERT INTO shifts (code, name, company_id, start_time, end_time, break_minutes, overnight, active)
SELECT 'SHIFT_MORN', 'Morning Shift',   c.id, '06:00'::TIME, '14:00'::TIME, 30, FALSE, TRUE FROM companies c WHERE c.code = 'PLUS33_COFFEE'
UNION ALL
SELECT 'SHIFT_AFT',  'Afternoon Shift', c.id, '14:00'::TIME, '22:00'::TIME, 30, FALSE, TRUE FROM companies c WHERE c.code = 'PLUS33_COFFEE'
UNION ALL
SELECT 'SHIFT_NGHT', 'Night Shift',     c.id, '22:00'::TIME, '06:00'::TIME, 30, TRUE,  TRUE FROM companies c WHERE c.code = 'PLUS33_COFFEE';

-- ── Leave Types (5) ─────────────────────────────────────────
INSERT INTO leave_types (code, name, paid, annual_limit, carry_forward, active)
VALUES
('LT_ANNUAL',    'Annual Leave',     TRUE,  25, TRUE,  TRUE),
('LT_SICK',      'Sick Leave',       TRUE,  12, FALSE, TRUE),
('LT_MATERNITY', 'Maternity Leave',  TRUE,  112, FALSE, TRUE),
('LT_PATERNITY', 'Paternity Leave',  TRUE,  25, FALSE, TRUE),
('LT_UNPAID',    'Unpaid Leave',     FALSE, 30, FALSE, TRUE)
ON CONFLICT DO NOTHING;

-- ── Users (200) — all use shared dev password ───────────────
-- Password: admin123 (bcrypt)
-- Role mapping: designation → role code

DO $$
DECLARE
    v_company_id BIGINT;
    v_password TEXT := '$2a$10$6pRRvx2jqvZqcfWjVPGjgO9DzPOh4NOUUozM/37iVk4O.2BZK8TQa';
    v_now TIMESTAMP := NOW();
    rec RECORD;
BEGIN
    SELECT id INTO v_company_id FROM companies WHERE code = 'PLUS33_COFFEE';

    -- Create temp table with all employee data
    CREATE TEMP TABLE tmp_employees (
        seq INT,
        first_name VARCHAR(100),
        last_name VARCHAR(100),
        designation VARCHAR(100),
        department VARCHAR(100),
        role_code VARCHAR(50),
        region_code VARCHAR(50),
        store_code VARCHAR(50),
        employment_type VARCHAR(50)
    ) ON COMMIT DROP;

    -- 1 Ultimate Admin
    INSERT INTO tmp_employees VALUES
    (1, 'Jean-Pierre', 'Moreau', 'Chief Executive Officer', 'Administration', 'ULTIMATE_ADMIN', 'IDF', 'ST_PARIS_01', 'PERMANENT');

    -- 5 Regional Admins
    INSERT INTO tmp_employees VALUES
    (2,  'Marie',    'Dubois',    'Regional Director',     'Administration', 'REGIONAL_ADMIN', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (3,  'Pierre',   'Laurent',   'Regional Director',     'Administration', 'REGIONAL_ADMIN', 'PACA', 'ST_MARS_01',  'PERMANENT'),
    (4,  'Sophie',   'Bernard',   'Regional Director',     'Administration', 'REGIONAL_ADMIN', 'ARA',  'ST_LYON_01',  'PERMANENT'),
    (5,  'Antoine',  'Petit',     'Regional Director',     'Administration', 'REGIONAL_ADMIN', 'OCC',  'ST_TLSE_01',  'PERMANENT'),
    (6,  'Isabelle', 'Roux',      'Regional Director',     'Administration', 'REGIONAL_ADMIN', 'NAQ',  'ST_BORD_01',  'PERMANENT');

    -- 10 Store Admins
    INSERT INTO tmp_employees VALUES
    (7,  'François',  'Martin',    'Store Manager', 'Sales', 'STORE_ADMIN', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (8,  'Claire',    'Thomas',    'Store Manager', 'Sales', 'STORE_ADMIN', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (9,  'Julien',    'Robert',    'Store Manager', 'Sales', 'STORE_ADMIN', 'PACA', 'ST_MARS_01',  'PERMANENT'),
    (10, 'Camille',   'Richard',   'Store Manager', 'Sales', 'STORE_ADMIN', 'PACA', 'ST_NICE_01',  'PERMANENT'),
    (11, 'Nicolas',   'Durand',    'Store Manager', 'Sales', 'STORE_ADMIN', 'ARA',  'ST_LYON_01',  'PERMANENT'),
    (12, 'Amélie',    'Leroy',     'Store Manager', 'Sales', 'STORE_ADMIN', 'ARA',  'ST_GREN_01',  'PERMANENT'),
    (13, 'Mathieu',   'Simon',     'Store Manager', 'Sales', 'STORE_ADMIN', 'OCC',  'ST_TLSE_01',  'PERMANENT'),
    (14, 'Émilie',    'Michel',    'Store Manager', 'Sales', 'STORE_ADMIN', 'OCC',  'ST_MTPL_01',  'PERMANENT'),
    (15, 'Sébastien', 'Lefèvre',   'Store Manager', 'Sales', 'STORE_ADMIN', 'NAQ',  'ST_BORD_01',  'PERMANENT'),
    (16, 'Marine',    'Garcia',    'Store Manager', 'Sales', 'STORE_ADMIN', 'NAQ',  'ST_LARO_01',  'PERMANENT');

    -- 10 Warehouse Managers (5 per warehouse)
    INSERT INTO tmp_employees VALUES
    (17, 'Alexandre',  'Fournier',  'Warehouse Manager',    'Warehouse', 'STORE_ADMIN', 'IDF', 'ST_PARIS_01', 'PERMANENT'),
    (18, 'Nathalie',   'Girard',    'Warehouse Manager',    'Warehouse', 'STORE_ADMIN', 'IDF', 'ST_PARIS_01', 'PERMANENT'),
    (19, 'Philippe',   'Bonnet',    'Warehouse Supervisor', 'Warehouse', 'STORE_ADMIN', 'IDF', 'ST_PARIS_01', 'PERMANENT'),
    (20, 'Sandrine',   'Dupont',    'Warehouse Supervisor', 'Warehouse', 'STORE_ADMIN', 'IDF', 'ST_PARIS_01', 'PERMANENT'),
    (21, 'Christophe', 'Lambert',   'Warehouse Associate',  'Warehouse', 'STORE_ADMIN', 'IDF', 'ST_PARIS_01', 'PERMANENT'),
    (22, 'David',      'Fontaine',  'Warehouse Manager',    'Warehouse', 'STORE_ADMIN', 'ARA', 'ST_LYON_01',  'PERMANENT'),
    (23, 'Valérie',    'Rousseau',  'Warehouse Manager',    'Warehouse', 'STORE_ADMIN', 'ARA', 'ST_LYON_01',  'PERMANENT'),
    (24, 'Stéphane',   'Vincent',   'Warehouse Supervisor', 'Warehouse', 'STORE_ADMIN', 'ARA', 'ST_LYON_01',  'PERMANENT'),
    (25, 'Catherine',  'Muller',    'Warehouse Supervisor', 'Warehouse', 'STORE_ADMIN', 'ARA', 'ST_LYON_01',  'PERMANENT'),
    (26, 'Guillaume',  'Lefevre',   'Warehouse Associate',  'Warehouse', 'STORE_ADMIN', 'ARA', 'ST_LYON_01',  'PERMANENT');

    -- 10 Finance Managers
    INSERT INTO tmp_employees VALUES
    (27, 'Pauline',   'Mercier',   'Finance Director',    'Finance', 'REGIONAL_ADMIN', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (28, 'Olivier',   'Blanc',     'Finance Manager',     'Finance', 'STORE_ADMIN',    'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (29, 'Aurélie',   'Guérin',    'Financial Controller','Finance', 'STORE_ADMIN',    'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (30, 'Thomas',    'Boyer',     'Accountant',          'Finance', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (31, 'Laure',     'André',     'Accountant',          'Finance', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_02', 'PERMANENT'),
    (32, 'Marc',      'Lemoine',   'Accounts Payable',    'Finance', 'SENIOR_EMPLOYEE','PACA', 'ST_MARS_01',  'PERMANENT'),
    (33, 'Christine', 'Perrin',    'Accounts Receivable', 'Finance', 'SENIOR_EMPLOYEE','ARA',  'ST_LYON_01',  'PERMANENT'),
    (34, 'Patrick',   'Morel',     'Treasury Analyst',    'Finance', 'SENIOR_EMPLOYEE','OCC',  'ST_TLSE_01',  'PERMANENT'),
    (35, 'Sylvie',    'Henry',     'Budget Analyst',      'Finance', 'SENIOR_EMPLOYEE','NAQ',  'ST_BORD_01',  'PERMANENT'),
    (36, 'Frédéric',  'Roussel',   'Tax Specialist',      'Finance', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT');

    -- 10 HR Managers
    INSERT INTO tmp_employees VALUES
    (37, 'Véronique', 'Masson',    'HR Director',         'Human Resources', 'REGIONAL_ADMIN', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (38, 'Laurent',   'Marchand',  'HR Manager',          'Human Resources', 'STORE_ADMIN',    'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (39, 'Delphine',  'Duval',     'Recruitment Manager', 'Human Resources', 'STORE_ADMIN',    'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (40, 'Bruno',     'Lemaire',   'Payroll Manager',     'Human Resources', 'STORE_ADMIN',    'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (41, 'Hélène',    'Picard',    'HR Business Partner', 'Human Resources', 'SENIOR_EMPLOYEE','PACA', 'ST_MARS_01',  'PERMANENT'),
    (42, 'Jérôme',    'Giraud',    'HR Business Partner', 'Human Resources', 'SENIOR_EMPLOYEE','ARA',  'ST_LYON_01',  'PERMANENT'),
    (43, 'Monique',   'Roger',     'Training Coordinator','Human Resources', 'SENIOR_EMPLOYEE','OCC',  'ST_TLSE_01',  'PERMANENT'),
    (44, 'Thierry',   'Colin',     'Benefits Specialist', 'Human Resources', 'SENIOR_EMPLOYEE','NAQ',  'ST_BORD_01',  'PERMANENT'),
    (45, 'Béatrice',  'Faure',     'HR Assistant',        'Human Resources', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (46, 'Pascal',    'Gauthier',  'HR Assistant',        'Human Resources', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_02', 'PERMANENT');

    -- 10 Procurement Officers
    INSERT INTO tmp_employees VALUES
    (47, 'Cécile',    'Chevalier', 'Procurement Director',  'Procurement', 'REGIONAL_ADMIN', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (48, 'Arnaud',    'Renault',   'Procurement Manager',   'Procurement', 'STORE_ADMIN',    'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (49, 'Virginie',  'Caron',     'Senior Buyer',          'Procurement', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (50, 'Éric',      'Legrand',   'Buyer',                 'Procurement', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (51, 'Corinne',   'Rivière',   'Buyer',                 'Procurement', 'SENIOR_EMPLOYEE','PACA', 'ST_MARS_01',  'PERMANENT'),
    (52, 'Franck',    'Barbier',   'Supply Chain Analyst',  'Procurement', 'SENIOR_EMPLOYEE','ARA',  'ST_LYON_01',  'PERMANENT'),
    (53, 'Mireille',  'Arnaud',    'Vendor Manager',        'Procurement', 'SENIOR_EMPLOYEE','OCC',  'ST_TLSE_01',  'PERMANENT'),
    (54, 'Gilles',    'Martinez',  'Purchasing Assistant',  'Procurement', 'JUNIOR_EMPLOYEE','NAQ',  'ST_BORD_01',  'PERMANENT'),
    (55, 'Dominique', 'Perez',     'Purchasing Assistant',  'Procurement', 'JUNIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (56, 'Agnès',     'Clement',   'Purchasing Assistant',  'Procurement', 'JUNIOR_EMPLOYEE','IDF',  'ST_PARIS_02', 'PERMANENT');

    -- 10 CRM Executives
    INSERT INTO tmp_employees VALUES
    (57, 'Raphaël',   'Nicolas',   'CRM Director',        'CRM', 'REGIONAL_ADMIN', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (58, 'Laurence',  'Dumas',     'CRM Manager',         'CRM', 'STORE_ADMIN',    'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (59, 'Vincent',   'Renard',    'CRM Analyst',         'CRM', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (60, 'Martine',   'Hubert',    'CRM Analyst',         'CRM', 'SENIOR_EMPLOYEE','PACA', 'ST_MARS_01',  'PERMANENT'),
    (61, 'Yves',      'Morin',     'Campaign Manager',    'CRM', 'SENIOR_EMPLOYEE','ARA',  'ST_LYON_01',  'PERMANENT'),
    (62, 'Florence',  'Lemaitre',  'Loyalty Specialist',  'CRM', 'SENIOR_EMPLOYEE','OCC',  'ST_TLSE_01',  'PERMANENT'),
    (63, 'Alain',     'Philippe',  'Data Analyst',        'CRM', 'JUNIOR_EMPLOYEE','NAQ',  'ST_BORD_01',  'PERMANENT'),
    (64, 'Brigitte',  'Roche',     'CRM Coordinator',     'CRM', 'JUNIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (65, 'René',      'David',     'CRM Coordinator',     'CRM', 'JUNIOR_EMPLOYEE','IDF',  'ST_PARIS_02', 'PERMANENT'),
    (66, 'Chantal',   'Bertrand',  'CRM Assistant',       'CRM', 'JUNIOR_EMPLOYEE','PACA', 'ST_NICE_01',  'PERMANENT');

    -- 10 Customer Support Executives
    INSERT INTO tmp_employees VALUES
    (67, 'Joël',      'Robin',     'Support Director',    'Customer Support', 'REGIONAL_ADMIN', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (68, 'Nadine',    'Leclerc',   'Support Manager',     'Customer Support', 'STORE_ADMIN',    'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (69, 'Hervé',     'Fabre',     'Senior Support Agent','Customer Support', 'SENIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (70, 'Danielle',  'Carpentier','Senior Support Agent','Customer Support', 'SENIOR_EMPLOYEE','PACA', 'ST_MARS_01',  'PERMANENT'),
    (71, 'Claude',    'Sanchez',   'Support Agent',       'Customer Support', 'JUNIOR_EMPLOYEE','ARA',  'ST_LYON_01',  'PERMANENT'),
    (72, 'Jacqueline','Dupuis',    'Support Agent',       'Customer Support', 'JUNIOR_EMPLOYEE','OCC',  'ST_TLSE_01',  'PERMANENT'),
    (73, 'Didier',    'Benoit',    'Support Agent',       'Customer Support', 'JUNIOR_EMPLOYEE','NAQ',  'ST_BORD_01',  'PERMANENT'),
    (74, 'Michèle',   'Maréchal',  'Support Agent',       'Customer Support', 'JUNIOR_EMPLOYEE','IDF',  'ST_PARIS_02', 'PERMANENT'),
    (75, 'Gérard',    'Brunet',    'Quality Analyst',     'Customer Support', 'JUNIOR_EMPLOYEE','IDF',  'ST_PARIS_01', 'PERMANENT'),
    (76, 'Annick',    'Mallet',    'Quality Analyst',     'Customer Support', 'JUNIOR_EMPLOYEE','PACA', 'ST_NICE_01',  'PERMANENT');

    -- 20 Shift Supervisors (2 per store)
    INSERT INTO tmp_employees VALUES
    (77,  'Romain',   'Pichon',    'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (78,  'Audrey',   'Rolland',   'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (79,  'Maxime',   'Clément',   'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (80,  'Elodie',   'Rey',       'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (81,  'Adrien',   'Brun',      'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'PACA', 'ST_MARS_01',  'PERMANENT'),
    (82,  'Manon',    'Leconte',   'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'PACA', 'ST_MARS_01',  'PERMANENT'),
    (83,  'Damien',   'Picard',    'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'PACA', 'ST_NICE_01',  'PERMANENT'),
    (84,  'Margaux',  'Guillot',   'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'PACA', 'ST_NICE_01',  'PERMANENT'),
    (85,  'Quentin',  'Georges',   'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'ARA',  'ST_LYON_01',  'PERMANENT'),
    (86,  'Léa',      'Meunier',   'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'ARA',  'ST_LYON_01',  'PERMANENT'),
    (87,  'Thibault', 'Perrot',    'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'ARA',  'ST_GREN_01',  'PERMANENT'),
    (88,  'Anaïs',    'Charpentier','Shift Supervisor','Sales', 'SHIFT_SUPERVISOR', 'ARA',  'ST_GREN_01',  'PERMANENT'),
    (89,  'Loïc',     'De Oliveira','Shift Supervisor','Sales', 'SHIFT_SUPERVISOR', 'OCC',  'ST_TLSE_01',  'PERMANENT'),
    (90,  'Clara',    'Huet',      'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'OCC',  'ST_TLSE_01',  'PERMANENT'),
    (91,  'Kévin',    'Baron',     'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'OCC',  'ST_MTPL_01',  'PERMANENT'),
    (92,  'Inès',     'Nguyen',    'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'OCC',  'ST_MTPL_01',  'PERMANENT'),
    (93,  'Florian',  'Pons',      'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'NAQ',  'ST_BORD_01',  'PERMANENT'),
    (94,  'Pauline',  'Charles',   'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'NAQ',  'ST_BORD_01',  'PERMANENT'),
    (95,  'Bastien',  'Aubert',    'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'NAQ',  'ST_LARO_01',  'PERMANENT'),
    (96,  'Céline',   'Descamps',  'Shift Supervisor', 'Sales', 'SHIFT_SUPERVISOR', 'NAQ',  'ST_LARO_01',  'PERMANENT');

    -- ~104 Store Employees (approx 10-11 per store)
    INSERT INTO tmp_employees VALUES
    -- ST_PARIS_01 (11 employees)
    (97,  'Hugo',     'Vasseur',   'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (98,  'Jade',     'Lacroix',   'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (99,  'Léo',      'Gaillard',  'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (100, 'Chloé',    'Bouvier',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (101, 'Nathan',   'Poirier',   'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (102, 'Emma',     'Renaud',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (103, 'Arthur',   'Legros',    'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (104, 'Louise',   'Collet',    'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (105, 'Louis',    'Chauvin',   'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_01', 'PERMANENT'),
    (106, 'Alice',    'Marchal',   'Trainee Barista','Sales', 'TRAINEE',         'IDF',  'ST_PARIS_01', 'CONTRACT'),
    (107, 'Ethan',    'Guyot',     'Trainee Barista','Sales', 'TRAINEE',         'IDF',  'ST_PARIS_01', 'CONTRACT'),
    -- ST_PARIS_02 (11 employees)
    (108, 'Gabriel',  'Deschamps', 'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (109, 'Lina',     'Dubois',    'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (110, 'Raphaël',  'Moulin',    'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (111, 'Mila',     'Blanchard', 'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (112, 'Adam',     'Maury',     'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (113, 'Rose',     'Vidal',     'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (114, 'Jules',    'Berger',    'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (115, 'Anna',     'Tanguy',    'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (116, 'Tom',      'Lebreton',  'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'IDF',  'ST_PARIS_02', 'PERMANENT'),
    (117, 'Zoé',      'Cordier',   'Trainee Barista','Sales', 'TRAINEE',         'IDF',  'ST_PARIS_02', 'CONTRACT'),
    (118, 'Théo',     'Remy',      'Trainee Barista','Sales', 'TRAINEE',         'IDF',  'ST_PARIS_02', 'CONTRACT'),
    -- ST_MARS_01 (10 employees)
    (119, 'Lucas',    'Pasquier',  'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (120, 'Ambre',    'Prevost',   'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (121, 'Enzo',     'Bouchet',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (122, 'Lola',     'Carre',     'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (123, 'Paul',     'Joubert',   'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (124, 'Eva',      'Ferreira',  'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (125, 'Sacha',    'Leblanc',   'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (126, 'Nina',     'Michaud',   'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (127, 'Oscar',    'Launay',    'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_MARS_01', 'PERMANENT'),
    (128, 'Iris',     'Schneider', 'Trainee Barista','Sales', 'TRAINEE',         'PACA', 'ST_MARS_01', 'CONTRACT'),
    -- ST_NICE_01 (10 employees)
    (129, 'Victor',   'Olivier',   'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (130, 'Juliette', 'Cousin',    'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (131, 'Axel',     'Germain',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (132, 'Lucie',    'Breton',    'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (133, 'Aaron',    'Besson',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (134, 'Agathe',   'Le Gall',   'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (135, 'Noé',      'Roussel',   'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (136, 'Romane',   'Hardy',     'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (137, 'Mathis',   'Delorme',   'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'PACA', 'ST_NICE_01', 'PERMANENT'),
    (138, 'Capucine', 'Martel',    'Trainee Barista','Sales', 'TRAINEE',         'PACA', 'ST_NICE_01', 'CONTRACT'),
    -- ST_LYON_01 (11 employees)
    (139, 'Clément',  'Bourgeois', 'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (140, 'Maëlys',   'Gros',      'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (141, 'Nolan',    'Royer',     'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (142, 'Lena',     'Jacquet',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (143, 'Baptiste', 'Collin',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (144, 'Sarah',    'Monnier',   'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (145, 'Mattéo',   'Guillon',   'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (146, 'Margot',   'Godard',    'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (147, 'Timéo',    'Boucher',   'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_LYON_01', 'PERMANENT'),
    (148, 'Lilou',    'Pelletier', 'Trainee Barista','Sales', 'TRAINEE',         'ARA', 'ST_LYON_01', 'CONTRACT'),
    (149, 'Gabin',    'Buisson',   'Trainee Barista','Sales', 'TRAINEE',         'ARA', 'ST_LYON_01', 'CONTRACT'),
    -- ST_GREN_01 (10 employees)
    (150, 'Alexis',   'Maillard',  'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (151, 'Océane',   'Poulain',   'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (152, 'Dylan',    'Texier',    'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (153, 'Lou',      'Voisin',    'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (154, 'Lilian',   'Courtois',  'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (155, 'Élisa',    'Didier',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (156, 'Evan',     'Gaudin',    'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (157, 'Camille',  'Becker',    'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (158, 'Marius',   'Chabert',   'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'ARA', 'ST_GREN_01', 'PERMANENT'),
    (159, 'Alix',     'Daniel',    'Trainee Barista','Sales', 'TRAINEE',         'ARA', 'ST_GREN_01', 'CONTRACT'),
    -- ST_TLSE_01 (11 employees)
    (160, 'Corentin', 'Pottier',   'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (161, 'Noa',      'Carlier',   'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (162, 'Malo',     'Thierry',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (163, 'Apolline', 'Blondel',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (164, 'Maël',     'Hamon',     'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (165, 'Constance','Pruvost',   'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (166, 'Valentin', 'Allard',    'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (167, 'Charlotte','Devaux',    'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (168, 'Dorian',   'Raymond',   'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_TLSE_01', 'PERMANENT'),
    (169, 'Noémie',   'Vasseur',   'Trainee Barista','Sales', 'TRAINEE',         'OCC', 'ST_TLSE_01', 'CONTRACT'),
    (170, 'Rémi',     'Mary',      'Trainee Barista','Sales', 'TRAINEE',         'OCC', 'ST_TLSE_01', 'CONTRACT'),
    -- ST_MTPL_01 (10 employees)
    (171, 'Simon',    'Adam',      'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (172, 'Victoire', 'Paris',     'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (173, 'Robin',    'Lamy',      'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (174, 'Jeanne',   'Perrier',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (175, 'Augustin', 'Dumont',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (176, 'Diane',    'Bailly',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (177, 'Ugo',      'Lacombe',   'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (178, 'Elsa',     'Lebrun',    'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (179, 'Eliott',   'Jacquemin', 'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'OCC', 'ST_MTPL_01', 'PERMANENT'),
    (180, 'Salomé',   'Auger',     'Trainee Barista','Sales', 'TRAINEE',         'OCC', 'ST_MTPL_01', 'CONTRACT'),
    -- ST_BORD_01 (10 employees)
    (181, 'Côme',     'Guichard',  'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (182, 'Héloïse',  'Delaunay',  'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (183, 'Léon',     'Paquette',  'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (184, 'Gabrielle','Flament',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (185, 'Marin',    'Dufour',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (186, 'Adèle',    'Gérard',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (187, 'Ewen',     'Coulon',    'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (188, 'Maëlle',   'Leclercq',  'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (189, 'Lenny',    'Bourdon',   'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_BORD_01', 'PERMANENT'),
    (190, 'Aya',      'Briand',    'Trainee Barista','Sales', 'TRAINEE',         'NAQ', 'ST_BORD_01', 'CONTRACT'),
    -- ST_LARO_01 (10 employees)
    (191, 'Antonin',  'Chevallier','Barista',        'Sales', 'SENIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (192, 'Lise',     'Dupré',     'Barista',        'Sales', 'SENIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (193, 'Timothée', 'Normand',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (194, 'Madeleine','Hervier',   'Barista',        'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (195, 'Emile',    'Laurent',   'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (196, 'Suzanne',  'Pascal',    'Cashier',        'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (197, 'Achille',  'Blot',      'Baker',          'Sales', 'SENIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (198, 'Elise',    'Verdier',   'Baker',          'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (199, 'Ilian',    'Boutin',    'Cleaning Staff', 'Sales', 'JUNIOR_EMPLOYEE', 'NAQ', 'ST_LARO_01', 'PERMANENT'),
    (200, 'Tessa',    'Colomb',    'Trainee Barista','Sales', 'TRAINEE',         'NAQ', 'ST_LARO_01', 'CONTRACT');

    -- ── Insert users from tmp_employees ─────────────────────
    FOR rec IN SELECT * FROM tmp_employees ORDER BY seq LOOP
        INSERT INTO users (email, password, first_name, last_name, active)
        VALUES (
            LOWER(rec.first_name || '.' || rec.last_name || '@plus33coffee.fr'),
            v_password,
            rec.first_name,
            rec.last_name,
            TRUE
        );
    END LOOP;

    -- ── Insert employees linked to users ────────────────────
    FOR rec IN SELECT * FROM tmp_employees ORDER BY seq LOOP
        INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
        SELECT
            'EMP-' || LPAD(rec.seq::TEXT, 4, '0'),
            u.id,
            v_company_id,
            rec.first_name,
            rec.last_name,
            u.email,
            '+33-' || (1 + (rec.seq % 5))::TEXT || '-' || LPAD((40000000 + rec.seq * 137)::TEXT, 8, '0'),
            rec.designation,
            rec.department,
            rec.employment_type,
            DATE '2024-01-15' + (rec.seq * 1) * INTERVAL '1 day',
            'ACTIVE',
            TRUE
        FROM users u
        WHERE u.email = LOWER(rec.first_name || '.' || rec.last_name || '@plus33coffee.fr');
    END LOOP;

    -- ── Assign roles to users ───────────────────────────────
    FOR rec IN SELECT * FROM tmp_employees ORDER BY seq LOOP
        INSERT INTO user_roles (user_id, role_id)
        SELECT u.id, r.id
        FROM users u
        CROSS JOIN roles r
        WHERE u.email = LOWER(rec.first_name || '.' || rec.last_name || '@plus33coffee.fr')
          AND r.code = rec.role_code;
    END LOOP;

    -- ── Assign users to regions ─────────────────────────────
    FOR rec IN SELECT * FROM tmp_employees ORDER BY seq LOOP
        INSERT INTO user_regions (user_id, region_id, assigned_at)
        SELECT u.id, rg.id, v_now
        FROM users u
        CROSS JOIN regions rg
        WHERE u.email = LOWER(rec.first_name || '.' || rec.last_name || '@plus33coffee.fr')
          AND rg.code = rec.region_code;
    END LOOP;

    -- ── Assign users to stores ──────────────────────────────
    FOR rec IN SELECT * FROM tmp_employees ORDER BY seq LOOP
        INSERT INTO user_stores (user_id, store_id, assigned_at)
        SELECT u.id, s.id, v_now
        FROM users u
        CROSS JOIN stores s
        WHERE u.email = LOWER(rec.first_name || '.' || rec.last_name || '@plus33coffee.fr')
          AND s.code = rec.store_code;
    END LOOP;

    -- ── Assign warehouse managers to warehouses ─────────────
    -- employees 17-21 → WH_PARIS, employees 22-26 → WH_LYON
    FOR rec IN SELECT * FROM tmp_employees WHERE seq BETWEEN 17 AND 26 ORDER BY seq LOOP
        INSERT INTO user_warehouses (user_id, warehouse_id, assigned_at)
        SELECT u.id, wh.id, v_now
        FROM users u
        CROSS JOIN warehouses wh
        WHERE u.email = LOWER(rec.first_name || '.' || rec.last_name || '@plus33coffee.fr')
          AND wh.code = CASE WHEN rec.seq <= 21 THEN 'WH_PARIS' ELSE 'WH_LYON' END;
    END LOOP;

    -- ── Assign shifts to employees ──────────────────────────
    FOR rec IN SELECT * FROM tmp_employees ORDER BY seq LOOP
        INSERT INTO employee_shifts (employee_id, shift_id, effective_from, effective_to)
        SELECT e.id, s.id, DATE '2024-01-15', NULL
        FROM employees e
        CROSS JOIN shifts s
        WHERE e.employee_code = 'EMP-' || LPAD(rec.seq::TEXT, 4, '0')
          AND e.company_id = v_company_id
          AND s.code = CASE
              WHEN rec.seq % 3 = 0 THEN 'SHIFT_NGHT'
              WHEN rec.seq % 3 = 1 THEN 'SHIFT_MORN'
              ELSE 'SHIFT_AFT'
          END
          AND s.company_id = v_company_id;
    END LOOP;

END $$;
