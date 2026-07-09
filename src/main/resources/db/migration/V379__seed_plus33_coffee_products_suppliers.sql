-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 379
-- File              : V379__seed_plus33_coffee_products_suppliers.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed plus33 coffee products suppliers
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : product_categories, products, suppliers, units_of_measure
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V379__seed_plus33_coffee_products_suppliers.sql
-- PLUS33 ERP — Product Categories, UoM, Products, Suppliers
-- ============================================================

-- ── Units of Measure ────────────────────────────────────────
INSERT INTO units_of_measure (code, name) VALUES
('PCS', 'Piece'),
('KG',  'Kilogram'),
('L',   'Litre'),
('BOX', 'Box'),
('SET', 'Set')
ON CONFLICT DO NOTHING;

-- ── Product Categories (15) ─────────────────────────────────
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_HOT_COFFEE',  'Hot Coffee',        NULL, TRUE),
('CAT_COLD_COFFEE', 'Cold Coffee',       NULL, TRUE),
('CAT_ESPRESSO',    'Espresso',          NULL, TRUE),
('CAT_LATTE',       'Latte',             NULL, TRUE),
('CAT_CAPPUCCINO',  'Cappuccino',        NULL, TRUE),
('CAT_MOCHA',       'Mocha',             NULL, TRUE),
('CAT_TEA',         'Tea',               NULL, TRUE),
('CAT_COLD_DRINKS', 'Cold Drinks',       NULL, TRUE),
('CAT_DESSERTS',    'Desserts',          NULL, TRUE),
('CAT_BAKERY',      'Bakery',            NULL, TRUE),
('CAT_SNACKS',      'Snacks',            NULL, TRUE),
('CAT_BEANS',       'Coffee Beans',      NULL, TRUE),
('CAT_MERCH',       'Merchandise',       NULL, TRUE),
('CAT_PACKAGING',   'Packaging',         NULL, TRUE),
('CAT_SUPPLIES',    'Inventory Supplies', NULL, TRUE);

-- ── Suppliers (30) ──────────────────────────────────────────
DO $$
DECLARE v_company_id BIGINT;
BEGIN
    SELECT id INTO v_company_id FROM companies WHERE code = 'PLUS33_COFFEE';

    INSERT INTO suppliers (code, name, contact_person, email, phone, address, tax_number, active, company_id, bank_name, bank_account_number, swift_code) VALUES
    -- Coffee Beans (4)
    ('SUP-001', 'Torréfaction Parisienne',      'Marc Berthelot',   'marc@torrefaction-parisienne.fr',   '+33-1-42-85-1001', '15 Rue du Café, 75011 Paris',           'FR12345678901', TRUE, v_company_id, 'BNP Paribas',     'FR7630004000031234567890143', 'BNPAFRPP'),
    ('SUP-002', 'Cafés du Sud',                 'Lucie Fontaine',   'lucie@cafes-du-sud.fr',             '+33-4-91-23-1002', '8 Boulevard Longchamp, 13001 Marseille', 'FR12345678902', TRUE, v_company_id, 'Crédit Agricole',  'FR7630006000011234567890178', 'AGRIFRPP'),
    ('SUP-003', 'Maison Verlet Importateur',    'Pierre Verlet',    'pierre@maisonverlet.fr',            '+33-1-42-60-1003', '256 Rue Saint-Honoré, 75001 Paris',     'FR12345678903', TRUE, v_company_id, 'Société Générale', 'FR7630003000011234567890182', 'SOGEFRPP'),
    ('SUP-004', 'Comptoir Éthiopien',           'Amara Bekele',     'amara@comptoir-ethiopien.fr',       '+33-4-72-40-1004', '42 Rue de la Bourse, 69002 Lyon',       'FR12345678904', TRUE, v_company_id, 'LCL',              'FR7630002000011234567890123', 'CRLYFRPP'),
    -- Milk (3)
    ('SUP-005', 'Laiterie de Normandie',        'Jacques Lemoine',  'jacques@laiterie-normandie.fr',     '+33-2-31-45-1005', '12 Route du Lait, 14000 Caen',          'FR12345678905', TRUE, v_company_id, 'Crédit Mutuel',    'FR7610278000011234567890134', 'CMCIFRPP'),
    ('SUP-006', 'Bio Lait Frais',               'Anne Dupont',      'anne@biolaitfrais.fr',              '+33-3-88-22-1006', '5 Rue des Producteurs, 67000 Strasbourg','FR12345678906', TRUE, v_company_id, 'BNP Paribas',     'FR7630004000031234567890256', 'BNPAFRPP'),
    ('SUP-007', 'Alpina Lait Végétal',          'Léa Bernard',      'lea@alpina-vegetal.fr',             '+33-4-76-87-1007', '20 Chemin des Alpages, 38000 Grenoble', 'FR12345678907', TRUE, v_company_id, 'Société Générale', 'FR7630003000011234567890289', 'SOGEFRPP'),
    -- Bakery (4)
    ('SUP-008', 'Boulangerie Industrielle Poilâne', 'Henri Poilâne', 'henri@poilane-ind.fr',            '+33-1-45-48-1008', '8 Rue du Cherche-Midi, 75006 Paris',    'FR12345678908', TRUE, v_company_id, 'BNP Paribas',     'FR7630004000031234567890367', 'BNPAFRPP'),
    ('SUP-009', 'Pâtisserie Lenôtre Wholesale', 'Sophie Lenôtre',   'sophie@lenotre-wholesale.fr',       '+33-1-44-09-1009', '44 Rue d''Auteuil, 75016 Paris',       'FR12345678909', TRUE, v_company_id, 'HSBC France',     'FR7630056000011234567890123', 'CCFRFRPP'),
    ('SUP-010', 'Les Croissants de Provence',   'Paul Riche',       'paul@croissants-provence.fr',       '+33-4-42-26-1010', '15 Cours Mirabeau, 13100 Aix-en-Prov', 'FR12345678910', TRUE, v_company_id, 'Crédit Agricole',  'FR7630006000011234567890289', 'AGRIFRPP'),
    ('SUP-011', 'Fournil Lyon Métropole',       'Marion Blanc',     'marion@fournil-lyon.fr',            '+33-4-78-62-1011', '30 Rue Mercière, 69002 Lyon',           'FR12345678911', TRUE, v_company_id, 'LCL',              'FR7630002000011234567890234', 'CRLYFRPP'),
    -- Packaging (3)
    ('SUP-012', 'EcoPack Solutions',            'Thomas Grenier',   'thomas@ecopack.fr',                 '+33-1-43-55-1012', '12 Zone Industrielle, 93100 Montreuil', 'FR12345678912', TRUE, v_company_id, 'BNP Paribas',     'FR7630004000031234567890478', 'BNPAFRPP'),
    ('SUP-013', 'Carton du Midi',               'Béatrice Roux',    'beatrice@carton-midi.fr',           '+33-5-61-99-1013', '8 ZI Toulouse Nord, 31150 Fenouillet',  'FR12345678913', TRUE, v_company_id, 'Société Générale', 'FR7630003000011234567890390', 'SOGEFRPP'),
    ('SUP-014', 'GreenCup France',              'Nicolas Verdier',  'nicolas@greencup.fr',               '+33-4-72-33-1014', '25 Rue de l''Innovation, 69003 Lyon',  'FR12345678914', TRUE, v_company_id, 'Crédit Mutuel',    'FR7610278000011234567890245', 'CMCIFRPP'),
    -- Cleaning (3)
    ('SUP-015', 'ProClean France',              'Isabelle Martin',  'isabelle@proclean.fr',              '+33-1-48-78-1015', '55 Rue de la Propreté, 92300 Levallois','FR12345678915', TRUE, v_company_id, 'BNP Paribas',     'FR7630004000031234567890589', 'BNPAFRPP'),
    ('SUP-016', 'Hygiène Professionnelle Sud',  'Rémi Casanova',    'remi@hygiene-pro-sud.fr',           '+33-4-91-50-1016', '10 ZI Les Paluds, 13400 Aubagne',      'FR12345678916', TRUE, v_company_id, 'Crédit Agricole',  'FR7630006000011234567890390', 'AGRIFRPP'),
    ('SUP-017', 'NaturNet Produits Bio',        'Claire Perrault',  'claire@naturnet.fr',                '+33-5-56-92-1017', '18 Quai de Bacalan, 33300 Bordeaux',   'FR12345678917', TRUE, v_company_id, 'Société Générale', 'FR7630003000011234567890401', 'SOGEFRPP'),
    -- Equipment (4)
    ('SUP-018', 'Machine à Café Pro',           'Alain Dumont',     'alain@machinecafe-pro.fr',          '+33-1-43-12-1018', '22 Bd de la Villette, 75010 Paris',    'FR12345678918', TRUE, v_company_id, 'BNP Paribas',     'FR7630004000031234567890690', 'BNPAFRPP'),
    ('SUP-019', 'Équipements Barista',          'Géraldine Vial',   'geraldine@equipbarista.fr',         '+33-4-78-37-1019', '14 Rue de Condé, 69002 Lyon',          'FR12345678919', TRUE, v_company_id, 'LCL',              'FR7630002000011234567890345', 'CRLYFRPP'),
    ('SUP-020', 'La Marzocco France',           'Roberto Mieli',    'roberto@lamarzocco.fr',             '+33-1-42-65-1020', '90 Rue de Rivoli, 75001 Paris',        'FR12345678920', TRUE, v_company_id, 'HSBC France',     'FR7630056000011234567890234', 'CCFRFRPP'),
    ('SUP-021', 'TechCafé Distribution',        'Sandra Morel',     'sandra@techcafe.fr',                '+33-5-61-44-1021', '5 Place Wilson, 31000 Toulouse',        'FR12345678921', TRUE, v_company_id, 'Crédit Mutuel',    'FR7610278000011234567890356', 'CMCIFRPP'),
    -- Furniture (3)
    ('SUP-022', 'Mobilier Café Design',         'Yann Le Coz',      'yann@mobcafe-design.fr',            '+33-1-44-07-1022', '28 Rue du Faubourg Saint-Antoine, 75012 Paris', 'FR12345678922', TRUE, v_company_id, 'BNP Paribas', 'FR7630004000031234567890701', 'BNPAFRPP'),
    ('SUP-023', 'Agencement Pro Méditerranée',  'Mélanie Sauvage',  'melanie@agen-pro-med.fr',           '+33-4-93-80-1023', '12 Promenade du Paillon, 06300 Nice',  'FR12345678923', TRUE, v_company_id, 'Crédit Agricole',  'FR7630006000011234567890401', 'AGRIFRPP'),
    ('SUP-024', 'Atelier Bois et Métal',        'Julien Carrier',   'julien@boisetmetal.fr',             '+33-5-56-81-1024', '45 Cours de la Marne, 33800 Bordeaux', 'FR12345678924', TRUE, v_company_id, 'Société Générale', 'FR7630003000011234567890512', 'SOGEFRPP'),
    -- Uniforms (3)
    ('SUP-025', 'Textile Pro Paris',            'Fabienne Leroux',  'fabienne@textile-pro.fr',           '+33-1-40-13-1025', '33 Rue du Sentier, 75002 Paris',       'FR12345678925', TRUE, v_company_id, 'BNP Paribas',     'FR7630004000031234567890812', 'BNPAFRPP'),
    ('SUP-026', 'Vêtements de Travail Sud',     'Pascal Mounier',   'pascal@vdt-sud.fr',                 '+33-4-91-08-1026', '20 Avenue du Prado, 13006 Marseille',  'FR12345678926', TRUE, v_company_id, 'LCL',              'FR7630002000011234567890456', 'CRLYFRPP'),
    ('SUP-027', 'Uniformes Prestige',           'Stéphanie Grand',  'stephanie@uniformes-prestige.fr',   '+33-4-72-56-1027', '8 Place Bellecour, 69002 Lyon',        'FR12345678927', TRUE, v_company_id, 'Crédit Mutuel',    'FR7610278000011234567890467', 'CMCIFRPP'),
    -- Utilities (3)
    ('SUP-028', 'EDF Pro Entreprises',          'Christophe Davy',  'christophe@edf-pro.fr',             '+33-1-40-42-1028', '22-30 Avenue de Wagram, 75008 Paris',  'FR12345678928', TRUE, v_company_id, 'BNP Paribas',     'FR7630004000031234567890923', 'BNPAFRPP'),
    ('SUP-029', 'Veolia Eau Services',          'Nathalie Blanc',   'nathalie@veolia-eau.fr',            '+33-1-71-75-1029', '36-38 Avenue Kléber, 75016 Paris',     'FR12345678929', TRUE, v_company_id, 'Société Générale', 'FR7630003000011234567890623', 'SOGEFRPP'),
    ('SUP-030', 'Engie Solutions Climat',       'Didier Fournier',  'didier@engie-solutions.fr',         '+33-1-44-22-1030', '1 Place Samuel de Champlain, 92400 Courbevoie', 'FR12345678930', TRUE, v_company_id, 'HSBC France', 'FR7630056000011234567890345', 'CCFRFRPP');
END $$;

-- ── Products (250) ──────────────────────────────────────────
DO $$
DECLARE
    v_cat_id BIGINT;
    v_uom_pcs BIGINT;
    v_uom_kg  BIGINT;
    v_uom_l   BIGINT;
    v_uom_box BIGINT;
    v_uom_set BIGINT;
BEGIN
    SELECT id INTO v_uom_pcs FROM units_of_measure WHERE code = 'PCS';
    SELECT id INTO v_uom_kg  FROM units_of_measure WHERE code = 'KG';
    SELECT id INTO v_uom_l   FROM units_of_measure WHERE code = 'L';
    SELECT id INTO v_uom_box FROM units_of_measure WHERE code = 'BOX';
    SELECT id INTO v_uom_set FROM units_of_measure WHERE code = 'SET';

    -- ── Hot Coffee (20) ─────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_HOT_COFFEE';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('HOT-FIL-001', 'Café Filtre Classique',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 50, TRUE),
    ('HOT-FIL-002', 'Café Filtre Décaféiné',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('HOT-AME-001', 'Americano',                    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('HOT-AME-002', 'Americano Long',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('HOT-TUR-001', 'Café Turc',                    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('HOT-IRL-001', 'Irish Coffee',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('HOT-VIE-001', 'Café Viennois',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('HOT-NOS-001', 'Noisette',                     v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('HOT-CRE-001', 'Café Crème',                   v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('HOT-COR-001', 'Cortado',                      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('HOT-AFF-001', 'Affogato',                     v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('HOT-MAC-001', 'Macchiato Chaud',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('HOT-FLT-001', 'Flat White',                   v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('HOT-RIS-001', 'Ristretto',                    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('HOT-ARA-001', 'Café Arabica Pur',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('HOT-ROB-001', 'Café Robusta Intense',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('HOT-MEL-001', 'Mélange Maison',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 45, TRUE),
    ('HOT-CHO-001', 'Chocolat Chaud',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 50, TRUE),
    ('HOT-CHO-002', 'Chocolat Chaud Noir 70%',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('HOT-CID-001', 'Cidre Chaud aux Épices',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE);

    -- ── Cold Coffee (15) ────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_COLD_COFFEE';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('CLD-ICE-001', 'Iced Coffee Classique',        v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('CLD-ICE-002', 'Iced Coffee Vanille',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('CLD-ICE-003', 'Iced Coffee Caramel',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('CLD-FRP-001', 'Frappuccino Moka',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CLD-FRP-002', 'Frappuccino Vanille',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CLD-FRP-003', 'Frappuccino Caramel',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CLD-CDB-001', 'Cold Brew Classique',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('CLD-CDB-002', 'Cold Brew Nitro',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('CLD-SHA-001', 'Coffee Shake Chocolat',        v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('CLD-SHA-002', 'Coffee Shake Noisette',        v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('CLD-TON-001', 'Espresso Tonic',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('CLD-ICM-001', 'Iced Macchiato',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CLD-ICM-002', 'Iced Macchiato Caramel',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CLD-SHK-001', 'Shakerato',                    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('CLD-ICL-001', 'Iced Latte Matcha',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE);

    -- ── Espresso (15) ───────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_ESPRESSO';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('ESP-SIN-001', 'Espresso Simple',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 60, TRUE),
    ('ESP-DOU-001', 'Espresso Double',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 50, TRUE),
    ('ESP-TRP-001', 'Espresso Triple',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('ESP-LNG-001', 'Espresso Lungo',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('ESP-RIS-001', 'Espresso Ristretto',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('ESP-DEC-001', 'Espresso Décaféiné',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('ESP-NOI-001', 'Espresso Noisette',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('ESP-VAN-001', 'Espresso Vanille',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('ESP-CAR-001', 'Espresso Caramel',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('ESP-CON-001', 'Espresso Con Panna',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('ESP-ROM-001', 'Espresso Romano',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('ESP-BIO-001', 'Espresso Bio Équitable',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('ESP-ETH-001', 'Espresso Éthiopie Yirgacheffe',v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('ESP-COL-001', 'Espresso Colombie Suprême',    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('ESP-BRE-001', 'Espresso Brésil Santos',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE);

    -- ── Latte (15) ──────────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_LATTE';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('LAT-CLS-001', 'Latte Classique',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 50, TRUE),
    ('LAT-VAN-001', 'Latte Vanille',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('LAT-CAR-001', 'Latte Caramel',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('LAT-NOI-001', 'Latte Noisette',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('LAT-MAT-001', 'Latte Matcha',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('LAT-CHA-001', 'Chai Latte',                   v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('LAT-TUR-001', 'Turmeric Latte',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('LAT-LAV-001', 'Latte Lavande',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('LAT-CIN-001', 'Latte Cannelle',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('LAT-AMN-001', 'Latte Amande',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('LAT-AVO-001', 'Latte Avoine',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('LAT-SOJ-001', 'Latte Soja',                   v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('LAT-COC-001', 'Latte Coco',                   v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('LAT-PIS-001', 'Latte Pistache',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('LAT-PMP-001', 'Pumpkin Spice Latte',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE);

    -- ── Cappuccino (15) ─────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_CAPPUCCINO';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('CAP-CLS-001', 'Cappuccino Classique',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 50, TRUE),
    ('CAP-DOU-001', 'Cappuccino Double',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('CAP-VAN-001', 'Cappuccino Vanille',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CAP-CAR-001', 'Cappuccino Caramel',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CAP-CHO-001', 'Cappuccino Chocolat',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('CAP-NOI-001', 'Cappuccino Noisette',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('CAP-CAN-001', 'Cappuccino Cannelle',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('CAP-ICE-001', 'Iced Cappuccino',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CAP-DEC-001', 'Cappuccino Décaféiné',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('CAP-AVO-001', 'Cappuccino Avoine',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('CAP-AMN-001', 'Cappuccino Amande',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('CAP-SOJ-001', 'Cappuccino Soja',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('CAP-XL-001',  'Cappuccino XL (500ml)',        v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('CAP-FRD-001', 'Cappuccino Freddo',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('CAP-BIO-001', 'Cappuccino Bio',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE);

    -- ── Mocha (15) ──────────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_MOCHA';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('MOC-CLS-001', 'Mocha Classique',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('MOC-BLA-001', 'Mocha Blanc',                  v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('MOC-NOI-001', 'Mocha Noir 70%',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('MOC-CAR-001', 'Mocha Caramel',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('MOC-NOX-001', 'Mocha Noisette',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('MOC-MNT-001', 'Mocha Menthe',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('MOC-FRA-001', 'Mocha Framboise',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('MOC-ICE-001', 'Iced Mocha',                   v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('MOC-ICW-001', 'Iced Mocha Blanc',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('MOC-XL-001',  'Mocha XL (500ml)',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('MOC-DEC-001', 'Mocha Décaféiné',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('MOC-AVO-001', 'Mocha Avoine',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('MOC-COC-001', 'Mocha Coco',                   v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('MOC-PRA-001', 'Mocha Praliné',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('MOC-TIR-001', 'Mocha Tiramisu',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE);

    -- ── Tea (20) ────────────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_TEA';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('TEA-EAR-001', 'Earl Grey',                    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('TEA-ENG-001', 'English Breakfast',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('TEA-GRN-001', 'Thé Vert Sencha',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('TEA-GRN-002', 'Thé Vert Jasmin',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('TEA-MAT-001', 'Matcha Cérémonie',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('TEA-CHA-001', 'Thé Chai Épicé',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('TEA-ROO-001', 'Rooibos Vanille',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('TEA-CAM-001', 'Camomille',                    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('TEA-MNT-001', 'Thé à la Menthe',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('TEA-FRT-001', 'Infusion Fruits Rouges',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('TEA-VER-001', 'Verveine',                     v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('TEA-TIL-001', 'Tilleul',                      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('TEA-OOL-001', 'Oolong Formosa',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('TEA-DAR-001', 'Darjeeling First Flush',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('TEA-ICG-001', 'Iced Green Tea',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('TEA-ICE-001', 'Iced Tea Pêche',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('TEA-ICL-001', 'Iced Tea Citron',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('TEA-BUB-001', 'Bubble Tea Taro',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('TEA-BUB-002', 'Bubble Tea Mangue',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('TEA-DET-001', 'Infusion Détox',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE);

    -- ── Cold Drinks (15) ────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_COLD_DRINKS';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('DRK-JUS-001', 'Jus d''Orange Pressé',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('DRK-JUS-002', 'Jus de Pomme Bio',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('DRK-SMO-001', 'Smoothie Mangue-Passion',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DRK-SMO-002', 'Smoothie Fraise-Banane',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DRK-SMO-003', 'Smoothie Vert Détox',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('DRK-LIM-001', 'Limonade Maison',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('DRK-LIM-002', 'Limonade Gingembre',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DRK-EAU-001', 'Eau Minérale Plate 50cl',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 100, TRUE),
    ('DRK-EAU-002', 'Eau Minérale Gazeuse 50cl',    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 80, TRUE),
    ('DRK-SIR-001', 'Sirop Grenadine',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('DRK-SIR-002', 'Sirop Menthe',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('DRK-CHO-001', 'Chocolat Froid',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DRK-SOD-001', 'Soda Artisanal Cola',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('DRK-SOD-002', 'Soda Artisanal Citron',        v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DRK-KOM-001', 'Kombucha Gingembre',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE);

    -- ── Desserts (20) ───────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_DESSERTS';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('DES-TIR-001', 'Tiramisu au Café',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('DES-FND-001', 'Fondant au Chocolat',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DES-CRB-001', 'Crème Brûlée',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('DES-TAR-001', 'Tarte au Citron',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('DES-TAR-002', 'Tarte Tatin',                  v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('DES-ECL-001', 'Éclair au Café',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DES-ECL-002', 'Éclair au Chocolat',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DES-MAC-001', 'Macaron Café',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('DES-MAC-002', 'Macaron Chocolat',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('DES-MAC-003', 'Macaron Framboise',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DES-MAC-004', 'Macaron Pistache',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DES-MAC-005', 'Coffret Macarons (6 pièces)',  v_cat_id, v_uom_box, 'FINISHED_GOOD', 15, TRUE),
    ('DES-MIL-001', 'Mille-Feuille',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('DES-PAN-001', 'Panna Cotta Vanille',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('DES-MOU-001', 'Mousse au Chocolat',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('DES-CAN-001', 'Cannelé de Bordeaux',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('DES-CHX-001', 'Chou à la Crème',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('DES-OPR-001', 'Opéra',                        v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('DES-PST-001', 'Paris-Brest',                  v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('DES-PRF-001', 'Profiteroles',                 v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE);

    -- ── Bakery (20) ─────────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_BAKERY';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('BAK-CRO-001', 'Croissant au Beurre',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 60, TRUE),
    ('BAK-CRO-002', 'Croissant aux Amandes',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('BAK-PCH-001', 'Pain au Chocolat',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 60, TRUE),
    ('BAK-PRR-001', 'Pain aux Raisins',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('BAK-CHS-001', 'Chausson aux Pommes',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('BAK-BRI-001', 'Brioche Nature',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('BAK-BRI-002', 'Brioche au Sucre',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('BAK-FOU-001', 'Fougasse Olives',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('BAK-QCH-001', 'Quiche Lorraine (part)',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('BAK-QCH-002', 'Quiche Chèvre-Épinards',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('BAK-CRQ-001', 'Croque-Monsieur',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 35, TRUE),
    ('BAK-CRQ-002', 'Croque-Madame',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('BAK-SAN-001', 'Sandwich Jambon-Beurre',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('BAK-SAN-002', 'Sandwich Poulet-Crudités',     v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('BAK-SAN-003', 'Sandwich Végétarien',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('BAK-WRP-001', 'Wrap Poulet-César',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('BAK-SAL-001', 'Salade César',                  v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('BAK-SAL-002', 'Salade Chèvre Chaud',          v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('BAK-PNI-001', 'Panini Mozzarella-Tomates',    v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('BAK-PNI-002', 'Panini Thon-Crudités',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE);

    -- ── Snacks (15) ─────────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_SNACKS';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('SNK-COK-001', 'Cookie Pépites Chocolat',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('SNK-COK-002', 'Cookie Noix de Macadamia',     v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('SNK-MUF-001', 'Muffin Myrtilles',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('SNK-MUF-002', 'Muffin Chocolat',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('SNK-BRW-001', 'Brownie Chocolat-Noix',        v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('SNK-SCN-001', 'Scone Nature',                  v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('SNK-SCN-002', 'Scone Fruits Rouges',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('SNK-GRN-001', 'Barre Granola Maison',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('SNK-GRN-002', 'Barre Énergétique Cacao',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('SNK-FRT-001', 'Salade de Fruits',              v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('SNK-YOG-001', 'Yaourt Granola',                v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('SNK-NUT-001', 'Mix Noix-Fruits Secs',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('SNK-CHP-001', 'Chips Artisanales',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('SNK-MAD-001', 'Madeleine au Citron',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('SNK-FIN-001', 'Financier aux Amandes',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE);

    -- ── Coffee Beans (20) ───────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_BEANS';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('BNS-ARA-001', 'Arabica Éthiopie 250g',        v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 100, TRUE),
    ('BNS-ARA-002', 'Arabica Colombie 250g',        v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 100, TRUE),
    ('BNS-ARA-003', 'Arabica Brésil 250g',          v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 100, TRUE),
    ('BNS-ARA-004', 'Arabica Guatemala 250g',       v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 80, TRUE),
    ('BNS-ARA-005', 'Arabica Costa Rica 250g',      v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 60, TRUE),
    ('BNS-ROB-001', 'Robusta Vietnam 250g',         v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 80, TRUE),
    ('BNS-ROB-002', 'Robusta Inde 250g',            v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 60, TRUE),
    ('BNS-MEL-001', 'Mélange Maison 250g',          v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 150, TRUE),
    ('BNS-MEL-002', 'Mélange Parisien 250g',        v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 120, TRUE),
    ('BNS-MEL-003', 'Mélange Méditerranéen 250g',   v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 100, TRUE),
    ('BNS-DEC-001', 'Décaféiné Suisse 250g',        v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 80, TRUE),
    ('BNS-BIO-001', 'Bio Équitable Pérou 250g',     v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 80, TRUE),
    ('BNS-BIO-002', 'Bio Équitable Rwanda 250g',    v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 60, TRUE),
    ('BNS-ARA-006', 'Arabica Kenya AA 1kg',         v_cat_id, v_uom_kg,  'RAW_MATERIAL', 50, TRUE),
    ('BNS-ARA-007', 'Arabica Éthiopie 1kg',         v_cat_id, v_uom_kg,  'RAW_MATERIAL', 60, TRUE),
    ('BNS-MEL-004', 'Mélange Maison 1kg',           v_cat_id, v_uom_kg,  'RAW_MATERIAL', 80, TRUE),
    ('BNS-MEL-005', 'Mélange Expresso 1kg',         v_cat_id, v_uom_kg,  'RAW_MATERIAL', 80, TRUE),
    ('BNS-CAP-001', 'Capsules Compatibles x20',     v_cat_id, v_uom_box, 'FINISHED_GOOD', 100, TRUE),
    ('BNS-CAP-002', 'Capsules Décaféiné x20',       v_cat_id, v_uom_box, 'FINISHED_GOOD', 60, TRUE),
    ('BNS-MOL-001', 'Café Moulu Filtre 500g',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 80, TRUE);

    -- ── Merchandise (20) ────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_MERCH';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('MER-MUG-001', 'Mug PLUS33 Coffee 350ml',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 50, TRUE),
    ('MER-MUG-002', 'Mug Édition Paris',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('MER-MUG-003', 'Mug Édition Lyon',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('MER-TUM-001', 'Tumbler Isotherme 500ml',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 40, TRUE),
    ('MER-TUM-002', 'Tumbler Bambou 400ml',         v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('MER-TSH-001', 'T-Shirt PLUS33 Coffee S',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('MER-TSH-002', 'T-Shirt PLUS33 Coffee M',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('MER-TSH-003', 'T-Shirt PLUS33 Coffee L',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 25, TRUE),
    ('MER-TOT-001', 'Tote Bag PLUS33 Coffee',       v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('MER-CAP-001', 'Casquette PLUS33 Coffee',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('MER-TAB-001', 'Tablier Barista',               v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('MER-PRE-001', 'Presse Française 1L',           v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE),
    ('MER-AER-001', 'Aeropress',                     v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('MER-V60-001', 'Dripper V60',                   v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('MER-MOK-001', 'Cafetière Moka 6 Tasses',      v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 15, TRUE),
    ('MER-BOX-001', 'Coffret Découverte (4x100g)',   v_cat_id, v_uom_set, 'FINISHED_GOOD', 25, TRUE),
    ('MER-BOX-002', 'Coffret Premium (6x100g)',      v_cat_id, v_uom_set, 'FINISHED_GOOD', 15, TRUE),
    ('MER-CFT-001', 'Carte Cadeau 25€',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 50, TRUE),
    ('MER-CFT-002', 'Carte Cadeau 50€',             v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 30, TRUE),
    ('MER-CFT-003', 'Carte Cadeau 100€',            v_cat_id, v_uom_pcs, 'FINISHED_GOOD', 20, TRUE);

    -- ── Packaging (20) ──────────────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_PACKAGING';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('PKG-CUP-101', 'Gobelet Papier 250ml',         v_cat_id, v_uom_box, 'RAW_MATERIAL', 200, TRUE),
    ('PKG-CUP-002', 'Gobelet Papier 350ml',         v_cat_id, v_uom_box, 'RAW_MATERIAL', 200, TRUE),
    ('PKG-CUP-003', 'Gobelet Papier 500ml',         v_cat_id, v_uom_box, 'RAW_MATERIAL', 150, TRUE),
    ('PKG-LID-001', 'Couvercle Compostable 250ml',  v_cat_id, v_uom_box, 'RAW_MATERIAL', 200, TRUE),
    ('PKG-LID-002', 'Couvercle Compostable 350ml',  v_cat_id, v_uom_box, 'RAW_MATERIAL', 200, TRUE),
    ('PKG-LID-003', 'Couvercle Compostable 500ml',  v_cat_id, v_uom_box, 'RAW_MATERIAL', 150, TRUE),
    ('PKG-SLV-001', 'Manchon Isolant',               v_cat_id, v_uom_box, 'RAW_MATERIAL', 150, TRUE),
    ('PKG-STW-001', 'Paille Papier',                 v_cat_id, v_uom_box, 'RAW_MATERIAL', 100, TRUE),
    ('PKG-STR-001', 'Touillette Bois',               v_cat_id, v_uom_box, 'RAW_MATERIAL', 100, TRUE),
    ('PKG-NAP-001', 'Serviette Papier Recyclé',      v_cat_id, v_uom_box, 'RAW_MATERIAL', 100, TRUE),
    ('PKG-BAG-001', 'Sac Papier Petit',              v_cat_id, v_uom_box, 'RAW_MATERIAL', 80, TRUE),
    ('PKG-BAG-002', 'Sac Papier Grand',              v_cat_id, v_uom_box, 'RAW_MATERIAL', 60, TRUE),
    ('PKG-BOX-001', 'Boîte Pâtisserie 1 pièce',    v_cat_id, v_uom_box, 'RAW_MATERIAL', 80, TRUE),
    ('PKG-BOX-002', 'Boîte Pâtisserie 6 pièces',   v_cat_id, v_uom_box, 'RAW_MATERIAL', 40, TRUE),
    ('PKG-WRP-001', 'Papier Emballage Sandwich',     v_cat_id, v_uom_box, 'RAW_MATERIAL', 100, TRUE),
    ('PKG-CNT-001', 'Barquette Salade',              v_cat_id, v_uom_box, 'RAW_MATERIAL', 60, TRUE),
    ('PKG-FRK-001', 'Fourchette Bois',               v_cat_id, v_uom_box, 'RAW_MATERIAL', 50, TRUE),
    ('PKG-KNF-001', 'Couteau Bois',                  v_cat_id, v_uom_box, 'RAW_MATERIAL', 50, TRUE),
    ('PKG-PLT-001', 'Assiette Carton',               v_cat_id, v_uom_box, 'RAW_MATERIAL', 40, TRUE),
    ('PKG-TRY-001', 'Plateau Transport 4 Gobelets',  v_cat_id, v_uom_box, 'RAW_MATERIAL', 60, TRUE);

    -- ── Inventory Supplies (10) ─────────────────────────────
    SELECT id INTO v_cat_id FROM product_categories WHERE code = 'CAT_SUPPLIES';
    INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
    ('SUP-CLN-001', 'Nettoyant Machine Café',        v_cat_id, v_uom_l,   'RAW_MATERIAL', 20, TRUE),
    ('SUP-CLN-002', 'Détartrant Machine',            v_cat_id, v_uom_l,   'RAW_MATERIAL', 15, TRUE),
    ('SUP-CLN-003', 'Liquide Vaisselle Pro',         v_cat_id, v_uom_l,   'RAW_MATERIAL', 30, TRUE),
    ('SUP-CLN-004', 'Nettoyant Multi-Surfaces',      v_cat_id, v_uom_l,   'RAW_MATERIAL', 20, TRUE),
    ('SUP-CLN-005', 'Désinfectant Alimentaire',      v_cat_id, v_uom_l,   'RAW_MATERIAL', 15, TRUE),
    ('SUP-FIL-001', 'Filtre à Eau Machine',          v_cat_id, v_uom_pcs, 'RAW_MATERIAL', 10, TRUE),
    ('SUP-FIL-002', 'Filtre Papier Dripper x100',   v_cat_id, v_uom_box, 'RAW_MATERIAL', 20, TRUE),
    ('SUP-GLV-001', 'Gants Nitrile (boîte 100)',     v_cat_id, v_uom_box, 'RAW_MATERIAL', 10, TRUE),
    ('SUP-SPG-001', 'Éponge Professionnelle x10',    v_cat_id, v_uom_box, 'RAW_MATERIAL', 10, TRUE),
    ('SUP-TRC-001', 'Torchon Microfibres x5',        v_cat_id, v_uom_box, 'RAW_MATERIAL', 10, TRUE);

END $$;
