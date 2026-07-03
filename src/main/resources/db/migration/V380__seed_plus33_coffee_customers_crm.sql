-- ============================================================
-- V380__seed_plus33_coffee_customers_crm.sql
-- PLUS33 ERP — Customers, CRM Leads, Opportunities, Cases, Timeline
-- ============================================================

DO $$
DECLARE
    v_company_id BIGINT;
    v_now TIMESTAMP := NOW();
    rec RECORD;
    v_cust_id BIGINT;
    v_lead_id BIGINT;
    v_opp_id BIGINT;
    v_owner_id BIGINT;
    v_emp_id BIGINT;
BEGIN
    SELECT id INTO v_company_id FROM companies WHERE code = 'PLUS33_COFFEE';

    -- ── 1. Create 100 Customers (B2B and B2C) ─────────────────
    -- Let's define some B2B customers
    CREATE TEMP TABLE tmp_customers (
        seq INT,
        code VARCHAR(50),
        name VARCHAR(150),
        cust_type VARCHAR(20),
        contact VARCHAR(150),
        email VARCHAR(150),
        phone VARCHAR(30),
        addr VARCHAR(255),
        credit NUMERIC(12,2),
        terms INT
    ) ON COMMIT DROP;

    INSERT INTO tmp_customers VALUES
    -- B2B Cafes & Restaurants (30)
    (1,  'CUST-B2B-001', 'Café de Flore',              'B2B', 'Jean-Paul Sartre',  'contact@cafedeflore.fr',       '+33-1-45-48-5526', '172 Boulevard Saint-Germain, 75006 Paris', 5000.00, 30),
    (2,  'CUST-B2B-002', 'Les Deux Magots',            'B2B', 'Simone de Beauvoir', 'info@lesdeuxmagots.fr',        '+33-1-45-48-5525', '6 Place Saint-Germain-des-Prés, 75006 Paris', 6000.00, 30),
    (3,  'CUST-B2B-003', 'Le Procope',                 'B2B', 'François Voltaire',  'reservation@procope.com',      '+33-1-40-46-7900', '13 Rue de l''Ancienne Comédie, 75006 Paris', 10000.00, 45),
    (4,  'CUST-B2B-004', 'Café de la Paix',            'B2B', 'Charles Garnier',    'contact@cafedelapaix.fr',      '+33-1-40-07-3636', '5 Place de l''Opéra, 75009 Paris', 12000.00, 45),
    (5,  'CUST-B2B-005', 'Le Train Bleu',              'B2B', 'Albert Clavel',      'trainbleu@gare-lyon.fr',       '+33-1-43-43-0906', 'Place Louis-Armand, Gare de Lyon, 75012 Paris', 15000.00, 60),
    (6,  'CUST-B2B-006', 'Brasserie Lipp',             'B2B', 'Léonard Lipp',       'contact@brasserielipp.fr',     '+33-1-45-48-5391', '151 Boulevard Saint-Germain, 75006 Paris', 8000.00, 30),
    (7,  'CUST-B2B-007', 'Café Marly',                 'B2B', 'Louvre Management',  'marly@louvre-cafe.fr',         '+33-1-49-26-0660', '93 Rue de Rivoli, 75001 Paris', 9000.00, 30),
    (8,  'CUST-B2B-008', 'Café des Deux Moulins',      'B2B', 'Amélie Poulain',     'deuxmoulins@montmartre.fr',    '+33-1-42-54-9050', '15 Rue Lepic, 75018 Paris', 4000.00, 30),
    (9,  'CUST-B2B-009', 'La Coupole',                 'B2B', 'René Lafon',         'lacoupole@group-rest.fr',      '+33-1-43-20-1420', '102 Boulevard du Montparnasse, 75014 Paris', 11000.00, 45),
    (10, 'CUST-B2B-010', 'Bofinger',                   'B2B', 'Frédéric Bofinger',  'bofinger@bastille-resto.fr',   '+33-1-42-72-8782', '5-7 Rue de la Bastille, 75004 Paris', 7000.00, 30),
    (11, 'CUST-B2B-011', 'Le Dôme Montparnasse',       'B2B', 'Paul Chambon',       'dome@montparnasse-cafe.fr',    '+33-1-43-35-2581', '108 Boulevard du Montparnasse, 75014 Paris', 9500.00, 30),
    (12, 'CUST-B2B-012', 'Café de la Mairie',          'B2B', 'Marc Audibert',      'mairie@saint-sulpice-cafe.fr', '+33-1-43-26-6785', '8 Place Saint-Sulpice, 75006 Paris', 3000.00, 15),
    (13, 'CUST-B2B-013', 'Angelina Paris',             'B2B', 'Antoine Rumpelmayer','angelina@rue-rivoli.fr',       '+33-1-42-60-8200', '226 Rue de Rivoli, 75001 Paris', 14000.00, 45),
    (14, 'CUST-B2B-014', 'Le Select',                  'B2B', 'Jacques Jalouzot',   'select@montparnasse.fr',       '+33-1-42-22-6527', '99 Boulevard du Montparnasse, 75006 Paris', 5000.00, 30),
    (15, 'CUST-B2B-015', 'La Closerie des Lilas',      'B2B', 'Paul Cézanne',       'closerie@lilas-resto.fr',      '+33-1-40-51-3450', '171 Boulevard du Montparnasse, 75006 Paris', 8500.00, 45),
    (16, 'CUST-B2B-016', 'Grand Café Capucines',       'B2B', 'Emma Bovary',        'capucines@grand-cafe.fr',      '+33-1-47-42-1900', '4 Boulevard des Capucines, 75009 Paris', 10500.00, 30),
    (17, 'CUST-B2B-017', 'Le Café Blanc',              'B2B', 'Charles Baudelaire', 'blanc@palais-royal-cafe.fr',   '+33-1-42-60-0722', '10 Rue de Croix des Petits Champs, 75001 Paris', 4500.00, 30),
    (18, 'CUST-B2B-018', 'Brasserie Georges Lyon',     'B2B', 'Georges Brasserie',  'georges@perrache-lyon.fr',     '+33-4-72-77-1000', '30 Cours de Verdun Rambaud, 69002 Lyon', 13000.00, 45),
    (19, 'CUST-B2B-019', 'Café des Négociants',        'B2B', 'Albert Camus',       'negociants@presquile-lyon.fr',  '+33-4-78-42-5005', '1 Place Francisque Régaud, 69002 Lyon', 7500.00, 30),
    (20, 'CUST-B2B-020', 'Grand Café des Négociants',  'B2B', 'Edouard Négociant',  'grandcafe@lyon-resto.fr',      '+33-4-78-42-5000', '1 Place Francisque Régaud, 69002 Lyon', 12000.00, 45),
    (21, 'CUST-B2B-021', 'Café Turin',                 'B2B', 'Cesare Pavese',      'turin@nice-resto.fr',          '+33-4-93-62-2772', '5 Place Garibaldi, 06300 Nice', 9000.00, 30),
    (22, 'CUST-B2B-022', 'La Caravelle Marseille',     'B2B', 'Jean-Claude Izzo',   'caravelle@vieuxport-mars.fr',  '+33-4-91-90-3664', '34 Quai du Port, 13002 Marseille', 6500.00, 30),
    (23, 'CUST-B2B-023', 'Café Bibent Toulouse',       'B2B', 'Jean Jaurès',        'bibent@capitole-tlse.fr',      '+33-5-61-23-8903', '5 Place du Capitole, 31000 Toulouse', 8500.00, 30),
    (24, 'CUST-B2B-024', 'Café Régent Bordeaux',       'B2B', 'François Mauriac',   'regent@bordeaux-resto.fr',     '+33-5-56-44-1212', '53 Place Gambetta, 33000 Bordeaux', 9500.00, 30),
    (25, 'CUST-B2B-025', 'Le Coq Hardi',               'B2B', 'Paul Bocuse',        'coqhardi@lyon-suburbs.fr',     '+33-4-78-33-4455', '8 Avenue de la République, 69160 Tassin', 5500.00, 30),
    (26, 'CUST-B2B-026', 'Chez Hugon',                 'B2B', 'Arlette Hugon',      'contact@chezhugon.fr',         '+33-4-78-28-1094', '12 Rue Pizay, 69001 Lyon', 4000.00, 15),
    (27, 'CUST-B2B-027', 'La Mère Brazier',            'B2B', 'Eugénie Brazier',    'brazier@lyon-gastronomie.fr',  '+33-4-78-23-1720', '12 Rue Royale, 69001 Lyon', 18000.00, 60),
    (28, 'CUST-B2B-028', 'Café des Tribunaux',         'B2B', 'Gustave Flaubert',   'tribunaux@dieppe-cafe.fr',     '+33-2-35-84-1234', 'Place du Puits Salé, 76200 Dieppe', 3500.00, 30),
    (29, 'CUST-B2B-029', 'Le Grand Colbert',           'B2B', 'Jean-Baptiste Colbert','colbert@colbert-resto.fr',    '+33-1-42-96-8175', '2 Rue de la Banque, 75002 Paris', 11500.00, 45),
    (30, 'CUST-B2B-030', 'Café de l''Industrie',        'B2B', 'Léon Blum',          'industrie@bastille-cafe.fr',   '+33-1-47-00-1353', '16 Rue Saint-Sabin, 75011 Paris', 6000.00, 30);

    -- Add 20 B2B Corporate Offices / Pantries
    INSERT INTO tmp_customers VALUES
    (31, 'CUST-B2B-031', 'Société Générale HQ',        'B2B', 'Marc Lhermitte',     'pantry@socgen.fr',             '+33-1-42-14-2000', '17 Cours Valmy, 92800 Puteaux', 25000.00, 60),
    (32, 'CUST-B2B-032', 'TotalEnergies La Défense',  'B2B', 'Patrick Pouyanné',   'coffee.pantry@totalenergies.fr','+33-1-47-44-4546', '2 Place Jean Millier, 92400 Courbevoie', 30000.00, 60),
    (33, 'CUST-B2B-033', 'L''Oréal Clichy',            'B2B', 'Jean-Paul Agon',     'pantries@loreal.com',          '+33-1-47-56-7000', '41 Rue de Martre, 92110 Clichy', 20000.00, 45),
    (34, 'CUST-B2B-034', 'AXA Group GIE',              'B2B', 'Thomas Buberl',      'facility@axa.com',             '+33-1-40-75-5700', '25 Avenue Matignon, 75008 Paris', 22000.00, 45),
    (35, 'CUST-B2B-035', 'Sanofi Gentilly',            'B2B', 'Paul Hudson',        'pantry.gentilly@sanofi.com',   '+33-1-57-63-0000', '82 Avenue Raspail, 94250 Gentilly', 28000.00, 60),
    (36, 'CUST-B2B-036', 'Carrefour Massy',            'B2B', 'Alexandre Bompard',  'coffee@carrefour.com',         '+33-1-64-50-5000', '93 Avenue de Paris, 91300 Massy', 15000.00, 30),
    (37, 'CUST-B2B-037', 'Danone Boulevard Haussmann', 'B2B', 'Antoine de Saint-Aff','services@danone.com',          '+33-1-44-35-2020', '17 Boulevard Haussmann, 75009 Paris', 18000.00, 45),
    (38, 'CUST-B2B-038', 'Orange Arcueil',             'B2B', 'Christel Heydemann', 'orange.pantry@orange.com',     '+33-1-55-22-2222', '78 Rue de la République, 94110 Arcueil', 24000.00, 60),
    (39, 'CUST-B2B-039', 'Renault Boulogne',           'B2B', 'Luca de Meo',        'pantry.renault@renault.com',   '+33-1-76-84-1111', '13-15 Quai Alphonse Le Gallo, 92100 Boulogne', 17000.00, 45),
    (40, 'CUST-B2B-040', 'Michelin Clermont',          'B2B', 'Florent Menegaux',   'pantry@michelin.com',          '+33-4-73-32-2000', '23 Place des Carmes Dechaux, 63000 Clermont', 21000.00, 45),
    (41, 'CUST-B2B-041', 'Capgemini Paris',            'B2B', 'Aiman Ezzat',        'capgemini.pantry@capgemini.com','+33-1-47-54-5000', '11 Rue de Tilsitt, 75017 Paris', 26000.00, 60),
    (42, 'CUST-B2B-042', 'BNP Paribas HQ',             'B2B', 'Jean-Laurent Bonnafé','pantries@bnpparibas.com',      '+33-1-40-14-4546', '16 Boulevard des Italiens, 75009 Paris', 35000.00, 60),
    (43, 'CUST-B2B-043', 'Saint-Gobain Courbevoie',    'B2B', 'Benoit Bazin',       'pantry.sg@saint-gobain.com',   '+33-1-88-54-0000', '12 Place de l''Iris, 92400 Courbevoie', 19000.00, 45),
    (44, 'CUST-B2B-044', 'Schneider Electric Rueil',   'B2B', 'Peter Herweck',      'pantry.se@se.com',             '+33-1-41-29-7000', '35 Rue Joseph Monier, 92500 Rueil-Malmaison', 23000.00, 45),
    (45, 'CUST-B2B-045', 'Veolia Aubervilliers',       'B2B', 'Estelle Brachlianoff','pantries@veolia.com',          '+33-1-85-57-7000', '30 Rue Madeleine Vionnet, 93300 Aubervilliers', 18000.00, 45),
    (46, 'CUST-B2B-046', 'Engie Courbevoie',           'B2B', 'Catherine MacGregor','pantry.engie@engie.com',       '+33-1-44-22-0000', '1 Place Samuel de Champlain, 92400 Courbevoie', 25000.00, 60),
    (47, 'CUST-B2B-047', 'Saint-Laurent HQ',           'B2B', 'Francesca Bellettini','pantry@ysl.com',               '+33-1-84-80-0000', '37 Rue de Bellechasse, 75007 Paris', 15000.00, 30),
    (48, 'CUST-B2B-048', 'Hermès International',       'B2B', 'Axel Dumas',         'pantry@hermes.com',            '+33-1-40-17-4717', '24 Rue du Faubourg Saint-Honoré, 75008 Paris', 20000.00, 30),
    (49, 'CUST-B2B-049', 'Chanel Neuilly',             'B2B', 'Alain Wertheimer',   'pantries@chanel.com',          '+33-1-46-43-4000', '135 Avenue Charles de Gaulle, 92200 Neuilly', 22000.00, 45),
    (50, 'CUST-B2B-050', 'Christian Dior Couture',     'B2B', 'Pietro Beccari',     'pantries@dior.com',            '+33-1-40-73-7373', '30 Avenue Montaigne, 75008 Paris', 25000.00, 45);

    -- Add 50 B2C VIP Customers
    FOR i IN 51..100 LOOP
        INSERT INTO tmp_customers VALUES (
            i,
            'CUST-B2C-' || LPAD(i::TEXT, 3, '0'),
            CASE (i % 5)
                WHEN 0 THEN 'Jean'
                WHEN 1 THEN 'Pierre'
                WHEN 2 THEN 'Michel'
                WHEN 3 THEN 'André'
                ELSE 'René'
            END || ' ' || CASE (i % 6)
                WHEN 0 THEN 'Martin'
                WHEN 1 THEN 'Bernard'
                WHEN 2 THEN 'Thomas'
                WHEN 3 THEN 'Petit'
                WHEN 4 THEN 'Richard'
                ELSE 'Durand'
            END || ' ' || i::TEXT,
            'B2C',
            NULL,
            'customer.' || i::TEXT || '@gmail.com',
            '+33-6-' || LPAD((50000000 + i * 37)::TEXT, 8, '0'),
            (10 + (i % 90))::TEXT || ' Rue de la République, ' || CASE (i % 5)
                WHEN 0 THEN '75001 Paris'
                WHEN 1 THEN '13001 Marseille'
                WHEN 2 THEN '69001 Lyon'
                WHEN 3 THEN '31000 Toulouse'
                ELSE '33000 Bordeaux'
            END,
            1000.00,
            0
        );
    END LOOP;

    -- Insert into customers table
    FOR rec IN SELECT * FROM tmp_customers ORDER BY seq LOOP
        INSERT INTO customers (
            company_id, code, name, customer_type, status, contact_person, email, phone,
            billing_address, shipping_address, tax_number, tax_profile, credit_limit,
            outstanding_balance, pricing_tier, discount_rate, payment_terms_days, currency_code,
            created_at, updated_at, version
        ) VALUES (
            v_company_id,
            rec.code,
            rec.name,
            rec.cust_type,
            'ACTIVE',
            rec.contact,
            rec.email,
            rec.phone,
            rec.addr,
            rec.addr,
            CASE WHEN rec.cust_type = 'B2B' THEN 'FR' || (987654321 + rec.seq)::TEXT ELSE NULL END,
            CASE WHEN rec.cust_type = 'B2B' THEN 'STANDARD' ELSE 'STANDARD' END,
            rec.credit,
            0.00,
            CASE WHEN rec.cust_type = 'B2B' THEN 'WHOLESALE' ELSE 'RETAIL' END,
            CASE WHEN rec.cust_type = 'B2B' THEN 5.00 ELSE 0.00 END,
            rec.terms,
            'EUR',
            v_now - INTERVAL '90 days',
            v_now,
            1
        );
    END LOOP;

    -- ── 2. Create 200 CRM Leads ───────────────────────────────
    -- owner_id will be one of the CRM Executives (users with emails ending with @plus33coffee.fr and role STORE_ADMIN or similar, let's select from employees with CRM designation/department)
    SELECT u.id INTO v_owner_id 
    FROM employees e
    JOIN users u ON e.user_id = u.id
    WHERE e.company_id = v_company_id AND e.department = 'CRM'
    LIMIT 1;

    IF v_owner_id IS NULL THEN
        SELECT id INTO v_owner_id FROM users LIMIT 1;
    END IF;

    FOR i IN 1..200 LOOP
        INSERT INTO crm_leads (
            company_id, first_name, last_name, organization_name, email, phone,
            status, score, source, campaign_attribution, created_at, updated_at
        ) VALUES (
            v_company_id,
            'LeadFirst' || i,
            'LeadLast' || i,
            CASE WHEN i % 2 = 0 THEN 'Cafe Enterprise ' || i ELSE 'Boutique Coffee Shop ' || i END,
            'lead.' || i || '@outlook.com',
            '+33-7-555-' || LPAD(i::TEXT, 4, '0'),
            CASE (i % 5)
                WHEN 0 THEN 'NEW'
                WHEN 1 THEN 'CONTACTED'
                WHEN 2 THEN 'QUALIFIED'
                WHEN 3 THEN 'UNQUALIFIED'
                ELSE 'LOST'
            END,
            (40 + (i % 60)),
            CASE (i % 4)
                WHEN 0 THEN 'WEBSITE'
                WHEN 1 THEN 'REFERRAL'
                WHEN 2 THEN 'TRADE_SHOW'
                ELSE 'COLD_CALL'
            END,
            CASE (i % 3)
                WHEN 0 THEN 'Google Search Ads'
                WHEN 1 THEN 'Paris Coffee Expo 2026'
                ELSE 'Organic Search'
            END,
            v_now - (i * INTERVAL '12 hours'),
            v_now - (i * INTERVAL '6 hours')
        );
    END LOOP;

    -- ── 3. Create 150 CRM Opportunities ───────────────────────
    FOR i IN 1..150 LOOP
        -- Select a customer
        SELECT id INTO v_cust_id FROM customers WHERE company_id = v_company_id OFFSET (i % 100) LIMIT 1;
        -- Select a lead
        SELECT id INTO v_lead_id FROM crm_leads WHERE company_id = v_company_id OFFSET (i % 200) LIMIT 1;
        -- Select an owner exec
        SELECT u.id INTO v_owner_id 
        FROM employees e
        JOIN users u ON e.user_id = u.id
        WHERE e.company_id = v_company_id AND e.department = 'CRM'
        OFFSET (i % 10) LIMIT 1;

        IF v_owner_id IS NULL THEN
            SELECT id INTO v_owner_id FROM users LIMIT 1;
        END IF;

        INSERT INTO crm_opportunities (
            company_id, customer_id, lead_id, title, stage, amount, probability, close_date, owner_id, created_at, updated_at
        ) VALUES (
            v_company_id,
            v_cust_id,
            v_lead_id,
            'Premium Coffee Supply Contract ' || i,
            CASE (i % 6)
                WHEN 0 THEN 'PROSPECTING'
                WHEN 1 THEN 'QUALIFICATION'
                WHEN 2 THEN 'PROPOSAL'
                WHEN 3 THEN 'NEGOTIATION'
                WHEN 4 THEN 'WON'
                ELSE 'LOST'
            END,
            1500.00 + (i * 250.00),
            CASE (i % 6)
                WHEN 0 THEN 10.00
                WHEN 1 THEN 30.00
                WHEN 2 THEN 60.00
                WHEN 3 THEN 80.00
                WHEN 4 THEN 100.00
                ELSE 0.00
            END,
            (v_now::DATE - 30) + (i % 60),
            v_owner_id,
            v_now - (i * INTERVAL '18 hours'),
            v_now - (i * INTERVAL '12 hours')
        );
    END LOOP;

    -- ── 4. Create 100 CRM Cases ───────────────────────────────
    FOR i IN 1..100 LOOP
        SELECT id INTO v_cust_id FROM customers WHERE company_id = v_company_id OFFSET (i % 100) LIMIT 1;

        INSERT INTO crm_cases (
            company_id, customer_id, case_number, priority, status, category, description, sla_breached, resolved_at, created_at
        ) VALUES (
            v_company_id,
            v_cust_id,
            'CASE-' || LPAD(i::TEXT, 5, '0'),
            CASE (i % 3)
                WHEN 0 THEN 'HIGH'
                WHEN 1 THEN 'MEDIUM'
                ELSE 'LOW'
            END,
            CASE (i % 4)
                WHEN 0 THEN 'NEW'
                WHEN 1 THEN 'ASSIGNED'
                WHEN 2 THEN 'RESOLVED'
                ELSE 'CLOSED'
            END,
            CASE (i % 4)
                WHEN 0 THEN 'DELIVERY_DELAY'
                WHEN 1 THEN 'QUALITY_ISSUE'
                WHEN 2 THEN 'BILLING_ERROR'
                ELSE 'EQUIPMENT_SUPPORT'
            END,
            'Customer reported issue category ' || (i % 4) || ' for order ref ' || i,
            CASE WHEN i % 10 = 0 THEN TRUE ELSE FALSE END,
            CASE WHEN i % 4 IN (2, 3) THEN v_now - (i * INTERVAL '4 hours') ELSE NULL END,
            v_now - (i * INTERVAL '1 day')
        );
    END LOOP;

    -- ── 5. Create 200 CRM Timeline Events ─────────────────────
    FOR i IN 1..200 LOOP
        SELECT id INTO v_cust_id FROM customers WHERE company_id = v_company_id OFFSET (i % 100) LIMIT 1;

        INSERT INTO crm_timeline_events (
            company_id, customer_id, event_type, description, occurred_at
        ) VALUES (
            v_company_id,
            v_cust_id,
            CASE (i % 4)
                WHEN 0 THEN 'CALL'
                WHEN 1 THEN 'EMAIL'
                WHEN 2 THEN 'MEETING'
                ELSE 'CONTRACT_SIGN'
            END,
            'Timeline event notes for interaction sequence ' || i,
            v_now - (i * INTERVAL '8 hours')
        );
    END LOOP;

END $$;
