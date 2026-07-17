-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 401
-- File              : V401__seed_full_product_catalog.sql
-- Operation Type    : Schema Seeding / Data Replacement
-- Purpose           : Populate complete coffee franchise product master (150+ products)
-- ============================================================================

-- Clean out old products and categories
TRUNCATE TABLE products, product_categories CASCADE;

-- Insert Parent Categories
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_BEVERAGES',   'Beverages',   NULL, TRUE),
('CAT_FOOD',        'Food',        NULL, TRUE),
('CAT_INGREDIENTS', 'Ingredients', NULL, TRUE),
('CAT_RETAIL',      'Retail',      NULL, TRUE),
('CAT_MERCHANDISE', 'Merchandise', NULL, TRUE),
('CAT_PACKAGING',   'Packaging',   NULL, TRUE),
('CAT_OPERATIONS',  'Operations',  NULL, TRUE),
('CAT_ASSETS',      'Assets',      NULL, TRUE);

-- Insert Subcategories
-- Beverages (5)
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_HOT_COFFEE',   'Hot Coffee',    (SELECT id FROM product_categories WHERE code = 'CAT_BEVERAGES'), TRUE),
('CAT_COLD_COFFEE',  'Cold Coffee',   (SELECT id FROM product_categories WHERE code = 'CAT_BEVERAGES'), TRUE),
('CAT_TEA_BEV',      'Tea Beverages', (SELECT id FROM product_categories WHERE code = 'CAT_BEVERAGES'), TRUE),
('CAT_COLD_BEV',     'Cold Beverages', (SELECT id FROM product_categories WHERE code = 'CAT_BEVERAGES'), TRUE),
('CAT_BOTTLED_DRINKS', 'Bottled Drinks', (SELECT id FROM product_categories WHERE code = 'CAT_BEVERAGES'), TRUE);

-- Food (4)
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_BAKERY',      'Bakery',      (SELECT id FROM product_categories WHERE code = 'CAT_FOOD'), TRUE),
('CAT_SANDWICHES',  'Sandwiches',  (SELECT id FROM product_categories WHERE code = 'CAT_FOOD'), TRUE),
('CAT_SALADS',      'Salads',      (SELECT id FROM product_categories WHERE code = 'CAT_FOOD'), TRUE),
('CAT_DESSERTS',    'Desserts',    (SELECT id FROM product_categories WHERE code = 'CAT_FOOD'), TRUE);

-- Ingredients (10)
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_BEANS',        'Coffee Beans',  (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_GROUND_COFFEE', 'Ground Coffee', (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_TEA',          'Tea',           (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_DAIRY',        'Milk & Dairy',  (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_SYRUPS',       'Syrups',        (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_SAUCES',       'Sauces',        (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_SWEETENERS',   'Sweeteners',    (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_POWDERS',      'Powders',       (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_FRUITS',       'Fruits',        (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE),
('CAT_TOPPINGS',     'Toppings',      (SELECT id FROM product_categories WHERE code = 'CAT_INGREDIENTS'), TRUE);

-- Retail (2)
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_RETAIL_COFFEE', 'Retail Coffee', (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL'), TRUE),
('CAT_GIFT_BOXES_RET', 'Gift Boxes',   (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL'), TRUE);

-- Merchandise (2)
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_MERCHANDISE_SUB', 'Merchandise', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE'), TRUE),
('CAT_ACCESSORIES',     'Accessories', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE'), TRUE);

-- Packaging (1)
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_PACKAGING_SUB', 'Packaging Items', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING'), TRUE);

-- Operations (4)
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_CLEANING',     'Cleaning Supplies', (SELECT id FROM product_categories WHERE code = 'CAT_OPERATIONS'), TRUE),
('CAT_KITCHEN',      'Kitchen Supplies',  (SELECT id FROM product_categories WHERE code = 'CAT_OPERATIONS'), TRUE),
('CAT_OFFICE',       'Office Supplies',   (SELECT id FROM product_categories WHERE code = 'CAT_OPERATIONS'), TRUE),
('CAT_UNIFORMS',     'Uniforms',          (SELECT id FROM product_categories WHERE code = 'CAT_OPERATIONS'), TRUE);

-- Assets (3)
INSERT INTO product_categories (code, name, parent_id, active) VALUES
('CAT_ASSETS_SUB',   'Equipment & Assets', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS'), TRUE),
('CAT_SPARE_PARTS',  'Spare Parts',        (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS'), TRUE),
('CAT_PROMOTIONS',   'Promotional Items',   (SELECT id FROM product_categories WHERE code = 'CAT_OPERATIONS'), TRUE);


-- Seed Products
DO $$
DECLARE
  v_pcs_uom_id BIGINT;
  v_kg_uom_id BIGINT;
  v_l_uom_id BIGINT;
  v_box_uom_id BIGINT;
BEGIN
  -- Lookup dynamic UOMs
  SELECT id INTO v_pcs_uom_id FROM units_of_measure WHERE code IN ('PCS', 'PIECE', 'Piece') LIMIT 1;
  SELECT id INTO v_kg_uom_id FROM units_of_measure WHERE code IN ('KG', 'Kilogram') LIMIT 1;
  SELECT id INTO v_l_uom_id FROM units_of_measure WHERE code IN ('L', 'LITER', 'Litre', 'Liter') LIMIT 1;
  SELECT id INTO v_box_uom_id FROM units_of_measure WHERE code = 'BOX' LIMIT 1;

  -- 1. Coffee Beans (RAW_MATERIAL, KG)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-BEA-ARA', 'Arabica Beans', (SELECT id FROM product_categories WHERE code = 'CAT_BEANS'), v_kg_uom_id, 'RAW_MATERIAL', 20.00, TRUE),
  ('ING-BEA-ROB', 'Robusta Beans', (SELECT id FROM product_categories WHERE code = 'CAT_BEANS'), v_kg_uom_id, 'RAW_MATERIAL', 20.00, TRUE),
  ('ING-BEA-ESP', 'Espresso Blend', (SELECT id FROM product_categories WHERE code = 'CAT_BEANS'), v_kg_uom_id, 'RAW_MATERIAL', 30.00, TRUE),
  ('ING-BEA-HOU', 'House Blend', (SELECT id FROM product_categories WHERE code = 'CAT_BEANS'), v_kg_uom_id, 'RAW_MATERIAL', 25.00, TRUE),
  ('ING-BEA-DEC', 'Decaf Coffee Beans', (SELECT id FROM product_categories WHERE code = 'CAT_BEANS'), v_kg_uom_id, 'RAW_MATERIAL', 10.00, TRUE);

  -- 2. Ground Coffee (FINISHED_GOOD, KG)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-GRN-ESP', 'Espresso Ground Coffee', (SELECT id FROM product_categories WHERE code = 'CAT_GROUND_COFFEE'), v_kg_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('ING-GRN-FRE', 'French Press Coffee', (SELECT id FROM product_categories WHERE code = 'CAT_GROUND_COFFEE'), v_kg_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('ING-GRN-FIL', 'Filter Coffee', (SELECT id FROM product_categories WHERE code = 'CAT_GROUND_COFFEE'), v_kg_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('ING-GRN-CLD', 'Cold Brew Ground Coffee', (SELECT id FROM product_categories WHERE code = 'CAT_GROUND_COFFEE'), v_kg_uom_id, 'FINISHED_GOOD', 10.00, TRUE);

  -- 3. Tea (RAW_MATERIAL, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-TEA-GRN', 'Green Tea', (SELECT id FROM product_categories WHERE code = 'CAT_TEA'), v_pcs_uom_id, 'RAW_MATERIAL', 100.00, TRUE),
  ('ING-TEA-BLK', 'Black Tea', (SELECT id FROM product_categories WHERE code = 'CAT_TEA'), v_pcs_uom_id, 'RAW_MATERIAL', 100.00, TRUE),
  ('ING-TEA-EAR', 'Earl Grey Tea', (SELECT id FROM product_categories WHERE code = 'CAT_TEA'), v_pcs_uom_id, 'RAW_MATERIAL', 50.00, TRUE),
  ('ING-TEA-CHA', 'Chamomile Tea', (SELECT id FROM product_categories WHERE code = 'CAT_TEA'), v_pcs_uom_id, 'RAW_MATERIAL', 50.00, TRUE),
  ('ING-TEA-ENG', 'English Breakfast Tea', (SELECT id FROM product_categories WHERE code = 'CAT_TEA'), v_pcs_uom_id, 'RAW_MATERIAL', 80.00, TRUE),
  ('ING-TEA-JAS', 'Jasmine Green Tea', (SELECT id FROM product_categories WHERE code = 'CAT_TEA'), v_pcs_uom_id, 'RAW_MATERIAL', 50.00, TRUE),
  ('ING-TEA-PEP', 'Peppermint Tea', (SELECT id FROM product_categories WHERE code = 'CAT_TEA'), v_pcs_uom_id, 'RAW_MATERIAL', 50.00, TRUE);

  -- 4. Milk & Dairy (RAW_MATERIAL, L)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-DAI-WHL', 'Whole Milk', (SELECT id FROM product_categories WHERE code = 'CAT_DAIRY'), v_l_uom_id, 'RAW_MATERIAL', 50.00, TRUE),
  ('ING-DAI-SKM', 'Skim Milk', (SELECT id FROM product_categories WHERE code = 'CAT_DAIRY'), v_l_uom_id, 'RAW_MATERIAL', 30.00, TRUE),
  ('ING-DAI-OAT', 'Oat Milk', (SELECT id FROM product_categories WHERE code = 'CAT_DAIRY'), v_l_uom_id, 'RAW_MATERIAL', 40.00, TRUE),
  ('ING-DAI-ALM', 'Almond Milk', (SELECT id FROM product_categories WHERE code = 'CAT_DAIRY'), v_l_uom_id, 'RAW_MATERIAL', 30.00, TRUE),
  ('ING-DAI-SOY', 'Soy Milk', (SELECT id FROM product_categories WHERE code = 'CAT_DAIRY'), v_l_uom_id, 'RAW_MATERIAL', 20.00, TRUE),
  ('ING-DAI-COC', 'Coconut Milk', (SELECT id FROM product_categories WHERE code = 'CAT_DAIRY'), v_l_uom_id, 'RAW_MATERIAL', 20.00, TRUE),
  ('ING-DAI-CRM', 'Fresh Cream', (SELECT id FROM product_categories WHERE code = 'CAT_DAIRY'), v_l_uom_id, 'RAW_MATERIAL', 15.00, TRUE),
  ('ING-DAI-WHP', 'Whipping Cream', (SELECT id FROM product_categories WHERE code = 'CAT_DAIRY'), v_l_uom_id, 'RAW_MATERIAL', 15.00, TRUE);

  -- 5. Syrups (RAW_MATERIAL, L)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-SYR-VAN', 'Vanilla Syrup', (SELECT id FROM product_categories WHERE code = 'CAT_SYRUPS'), v_l_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-SYR-CAR', 'Caramel Syrup', (SELECT id FROM product_categories WHERE code = 'CAT_SYRUPS'), v_l_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-SYR-HAZ', 'Hazelnut Syrup', (SELECT id FROM product_categories WHERE code = 'CAT_SYRUPS'), v_l_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-SYR-IRI', 'Irish Syrup', (SELECT id FROM product_categories WHERE code = 'CAT_SYRUPS'), v_l_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-SYR-PUM', 'Pumpkin Spice Syrup', (SELECT id FROM product_categories WHERE code = 'CAT_SYRUPS'), v_l_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-SYR-TOF', 'Toffee Nut Syrup', (SELECT id FROM product_categories WHERE code = 'CAT_SYRUPS'), v_l_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-SYR-SFV', 'Sugar-Free Vanilla Syrup', (SELECT id FROM product_categories WHERE code = 'CAT_SYRUPS'), v_l_uom_id, 'RAW_MATERIAL', 5.00, TRUE);

  -- 6. Sauces (RAW_MATERIAL, L)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-SAU-CHO', 'Chocolate Sauce', (SELECT id FROM product_categories WHERE code = 'CAT_SAUCES'), v_l_uom_id, 'RAW_MATERIAL', 8.00, TRUE),
  ('ING-SAU-WCH', 'White Chocolate Sauce', (SELECT id FROM product_categories WHERE code = 'CAT_SAUCES'), v_l_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-SAU-CAR', 'Caramel Sauce', (SELECT id FROM product_categories WHERE code = 'CAT_SAUCES'), v_l_uom_id, 'RAW_MATERIAL', 8.00, TRUE),
  ('ING-SAU-MOC', 'Mocha Sauce', (SELECT id FROM product_categories WHERE code = 'CAT_SAUCES'), v_l_uom_id, 'RAW_MATERIAL', 8.00, TRUE),
  ('ING-SAU-DKC', 'Dark Chocolate Sauce', (SELECT id FROM product_categories WHERE code = 'CAT_SAUCES'), v_l_uom_id, 'RAW_MATERIAL', 5.00, TRUE);

  -- 7. Sweeteners (RAW_MATERIAL, KG/L/PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-SWT-WHT', 'White Sugar', (SELECT id FROM product_categories WHERE code = 'CAT_SWEETENERS'), v_kg_uom_id, 'RAW_MATERIAL', 15.00, TRUE),
  ('ING-SWT-BRN', 'Brown Sugar', (SELECT id FROM product_categories WHERE code = 'CAT_SWEETENERS'), v_kg_uom_id, 'RAW_MATERIAL', 15.00, TRUE),
  ('ING-SWT-RAW', 'Raw Sugar', (SELECT id FROM product_categories WHERE code = 'CAT_SWEETENERS'), v_kg_uom_id, 'RAW_MATERIAL', 15.00, TRUE),
  ('ING-SWT-HON', 'Honey', (SELECT id FROM product_categories WHERE code = 'CAT_SWEETENERS'), v_pcs_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-SWT-STE', 'Stevia', (SELECT id FROM product_categories WHERE code = 'CAT_SWEETENERS'), v_pcs_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-SWT-SYR', 'Sugar Syrup', (SELECT id FROM product_categories WHERE code = 'CAT_SWEETENERS'), v_l_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-SWT-LIQ', 'Liquid Sweetener', (SELECT id FROM product_categories WHERE code = 'CAT_SWEETENERS'), v_l_uom_id, 'RAW_MATERIAL', 5.00, TRUE);

  -- 8. Powders (RAW_MATERIAL, KG)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-PWD-MAT', 'Matcha Powder', (SELECT id FROM product_categories WHERE code = 'CAT_POWDERS'), v_kg_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-PWD-COC', 'Cocoa Powder', (SELECT id FROM product_categories WHERE code = 'CAT_POWDERS'), v_kg_uom_id, 'RAW_MATERIAL', 8.00, TRUE),
  ('ING-PWD-VAN', 'Vanilla Powder', (SELECT id FROM product_categories WHERE code = 'CAT_POWDERS'), v_kg_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-PWD-CHA', 'Chai Powder', (SELECT id FROM product_categories WHERE code = 'CAT_POWDERS'), v_kg_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-PWD-ESP', 'Espresso Powder', (SELECT id FROM product_categories WHERE code = 'CAT_POWDERS'), v_kg_uom_id, 'RAW_MATERIAL', 5.00, TRUE);

  -- 9. Fruits (RAW_MATERIAL, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-FRU-LEM', 'Lemon', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 50.00, TRUE),
  ('ING-FRU-LIM', 'Lime', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 30.00, TRUE),
  ('ING-FRU-ORN', 'Orange', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 50.00, TRUE),
  ('ING-FRU-STR', 'Strawberry', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 50.00, TRUE),
  ('ING-FRU-BLU', 'Blueberry', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 30.00, TRUE),
  ('ING-FRU-RAS', 'Raspberry', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 30.00, TRUE),
  ('ING-FRU-MAN', 'Mango', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 30.00, TRUE),
  ('ING-FRU-PEA', 'Peach', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 30.00, TRUE),
  ('ING-FRU-BAN', 'Banana', (SELECT id FROM product_categories WHERE code = 'CAT_FRUITS'), v_pcs_uom_id, 'RAW_MATERIAL', 50.00, TRUE);

  -- 10. Toppings (RAW_MATERIAL, KG/L/PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ING-TOP-WHP', 'Whipped Cream', (SELECT id FROM product_categories WHERE code = 'CAT_TOPPINGS'), v_l_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-TOP-CHP', 'Chocolate Chips', (SELECT id FROM product_categories WHERE code = 'CAT_TOPPINGS'), v_kg_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-TOP-SHV', 'Chocolate Shavings', (SELECT id FROM product_categories WHERE code = 'CAT_TOPPINGS'), v_kg_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-TOP-CAR', 'Caramel Drizzle', (SELECT id FROM product_categories WHERE code = 'CAT_TOPPINGS'), v_pcs_uom_id, 'RAW_MATERIAL', 10.00, TRUE),
  ('ING-TOP-CRM', 'Cookie Crumbs', (SELECT id FROM product_categories WHERE code = 'CAT_TOPPINGS'), v_kg_uom_id, 'RAW_MATERIAL', 5.00, TRUE),
  ('ING-TOP-CIN', 'Cinnamon Powder', (SELECT id FROM product_categories WHERE code = 'CAT_TOPPINGS'), v_kg_uom_id, 'RAW_MATERIAL', 3.00, TRUE),
  ('ING-TOP-DST', 'Cocoa Dust', (SELECT id FROM product_categories WHERE code = 'CAT_TOPPINGS'), v_kg_uom_id, 'RAW_MATERIAL', 3.00, TRUE),
  ('ING-TOP-SPK', 'Sprinkles', (SELECT id FROM product_categories WHERE code = 'CAT_TOPPINGS'), v_kg_uom_id, 'RAW_MATERIAL', 5.00, TRUE);

  -- 11. Hot Coffee (MENU_ITEM, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('BEV-HOT-ESP', 'Espresso', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-DES', 'Double Espresso', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-AME', 'Americano', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-CAP', 'Cappuccino', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-LAT', 'Latte', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-FLW', 'Flat White', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-MOC', 'Mocha', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-MAC', 'Macchiato', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-COR', 'Cortado', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-PIC', 'Piccolo Latte', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-HOT-LGB', 'Long Black', (SELECT id FROM product_categories WHERE code = 'CAT_HOT_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE);

  -- 12. Cold Coffee (MENU_ITEM, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('BEV-CLD-ESP', 'Iced Espresso', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-CLD-AME', 'Iced Americano', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-CLD-LAT', 'Iced Latte', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-CLD-MOC', 'Iced Mocha', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-CLD-CRW', 'Cold Brew', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-CLD-NIT', 'Nitro Cold Brew', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-CLD-FRP', 'Coffee Frappe', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-CLD-CRF', 'Caramel Frappe', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-CLD-MCF', 'Mocha Frappe', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_COFFEE'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE);

  -- 13. Tea Beverages (MENU_ITEM, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('BEV-TEA-GTL', 'Green Tea Latte', (SELECT id FROM product_categories WHERE code = 'CAT_TEA_BEV'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-TEA-CTL', 'Chai Tea Latte', (SELECT id FROM product_categories WHERE code = 'CAT_TEA_BEV'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-TEA-MTL', 'Matcha Latte', (SELECT id FROM product_categories WHERE code = 'CAT_TEA_BEV'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE),
  ('BEV-TEA-HCH', 'Hot Chocolate', (SELECT id FROM product_categories WHERE code = 'CAT_TEA_BEV'), v_pcs_uom_id, 'MENU_ITEM', 0.00, TRUE);

  -- 14. Cold Beverages (FINISHED_GOOD, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('BEV-CLD-LEM', 'Lemonade', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_BEV'), v_pcs_uom_id, 'FINISHED_GOOD', 0.00, TRUE),
  ('BEV-CLD-PIT', 'Peach Iced Tea', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_BEV'), v_pcs_uom_id, 'FINISHED_GOOD', 0.00, TRUE),
  ('BEV-CLD-MSM', 'Mango Smoothie', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_BEV'), v_pcs_uom_id, 'FINISHED_GOOD', 0.00, TRUE),
  ('BEV-CLD-SSM', 'Strawberry Smoothie', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_BEV'), v_pcs_uom_id, 'FINISHED_GOOD', 0.00, TRUE),
  ('BEV-CLD-BSM', 'Mixed Berry Smoothie', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_BEV'), v_pcs_uom_id, 'FINISHED_GOOD', 0.00, TRUE),
  ('BEV-CLD-OJU', 'Orange Juice', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_BEV'), v_pcs_uom_id, 'FINISHED_GOOD', 0.00, TRUE),
  ('BEV-CLD-AJU', 'Apple Juice', (SELECT id FROM product_categories WHERE code = 'CAT_COLD_BEV'), v_pcs_uom_id, 'FINISHED_GOOD', 0.00, TRUE);

  -- 15. Bottled Drinks (FINISHED_GOOD, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('BEV-BOT-STW', 'Still Water', (SELECT id FROM product_categories WHERE code = 'CAT_BOTTLED_DRINKS'), v_pcs_uom_id, 'FINISHED_GOOD', 50.00, TRUE),
  ('BEV-BOT-SPK', 'Sparkling Water', (SELECT id FROM product_categories WHERE code = 'CAT_BOTTLED_DRINKS'), v_pcs_uom_id, 'FINISHED_GOOD', 30.00, TRUE),
  ('BEV-BOT-COL', 'Cola', (SELECT id FROM product_categories WHERE code = 'CAT_BOTTLED_DRINKS'), v_pcs_uom_id, 'FINISHED_GOOD', 40.00, TRUE),
  ('BEV-BOT-DIC', 'Diet Cola', (SELECT id FROM product_categories WHERE code = 'CAT_BOTTLED_DRINKS'), v_pcs_uom_id, 'FINISHED_GOOD', 30.00, TRUE),
  ('BEV-BOT-LSD', 'Lemon Soda', (SELECT id FROM product_categories WHERE code = 'CAT_BOTTLED_DRINKS'), v_pcs_uom_id, 'FINISHED_GOOD', 24.00, TRUE);

  -- 16. Bakery (FINISHED_GOOD, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('FOD-BAK-CRO', 'Butter Croissant', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 20.00, TRUE),
  ('FOD-BAK-CHC', 'Chocolate Croissant', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-BLM', 'Blueberry Muffin', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-CHM', 'Chocolate Muffin', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-VNM', 'Vanilla Muffin', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-BAK-BRN', 'Brownie', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-CBR', 'Chocolate Brownie', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-DNT', 'Donut', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 20.00, TRUE),
  ('FOD-BAK-CDN', 'Chocolate Donut', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-GDN', 'Glazed Donut', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-CIN', 'Cinnamon Roll', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-DAN', 'Danish Pastry', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-BAK-COK', 'Cookie', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 30.00, TRUE),
  ('FOD-BAK-CCC', 'Chocolate Chip Cookie', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 20.00, TRUE),
  ('FOD-BAK-OAT', 'Oatmeal Cookie', (SELECT id FROM product_categories WHERE code = 'CAT_BAKERY'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE);

  -- 17. Sandwiches (FINISHED_GOOD, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('FOD-SND-VEG', 'Veg Sandwich', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-SND-GCH', 'Grilled Cheese Sandwich', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-SND-CHK', 'Chicken Sandwich', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-SND-TUR', 'Turkey Sandwich', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-SND-CWR', 'Chicken Wrap', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-SND-VWR', 'Veg Wrap', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-SND-HAM', 'Ham & Cheese Sandwich', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-SND-CLB', 'Club Sandwich', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-SND-PAN', 'Panini', (SELECT id FROM product_categories WHERE code = 'CAT_SANDWICHES'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE);

  -- 18. Salads (FINISHED_GOOD, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('FOD-SAL-CAE', 'Caesar Salad', (SELECT id FROM product_categories WHERE code = 'CAT_SALADS'), v_pcs_uom_id, 'FINISHED_GOOD', 8.00, TRUE),
  ('FOD-SAL-GRK', 'Greek Salad', (SELECT id FROM product_categories WHERE code = 'CAT_SALADS'), v_pcs_uom_id, 'FINISHED_GOOD', 8.00, TRUE),
  ('FOD-SAL-GAR', 'Garden Salad', (SELECT id FROM product_categories WHERE code = 'CAT_SALADS'), v_pcs_uom_id, 'FINISHED_GOOD', 8.00, TRUE),
  ('FOD-SAL-FRU', 'Fruit Salad', (SELECT id FROM product_categories WHERE code = 'CAT_SALADS'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE);

  -- 19. Desserts (FINISHED_GOOD, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('FOD-DES-CHK', 'Cheesecake', (SELECT id FROM product_categories WHERE code = 'CAT_DESSERTS'), v_pcs_uom_id, 'FINISHED_GOOD', 12.00, TRUE),
  ('FOD-DES-CLC', 'Chocolate Cake', (SELECT id FROM product_categories WHERE code = 'CAT_DESSERTS'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-DES-RVC', 'Red Velvet Cake', (SELECT id FROM product_categories WHERE code = 'CAT_DESSERTS'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-DES-TIR', 'Tiramisu', (SELECT id FROM product_categories WHERE code = 'CAT_DESSERTS'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-DES-MAC', 'Macarons', (SELECT id FROM product_categories WHERE code = 'CAT_DESSERTS'), v_pcs_uom_id, 'FINISHED_GOOD', 15.00, TRUE),
  ('FOD-DES-APL', 'Apple Pie', (SELECT id FROM product_categories WHERE code = 'CAT_DESSERTS'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-DES-LMT', 'Lemon Tart', (SELECT id FROM product_categories WHERE code = 'CAT_DESSERTS'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE),
  ('FOD-DES-CHT', 'Chocolate Tart', (SELECT id FROM product_categories WHERE code = 'CAT_DESSERTS'), v_pcs_uom_id, 'FINISHED_GOOD', 10.00, TRUE);

  -- 20. Retail Coffee (RETAIL_PRODUCT, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('RET-BEA-250', 'Whole Bean Coffee 250g', (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL_COFFEE'), v_pcs_uom_id, 'RETAIL_PRODUCT', 20.00, TRUE),
  ('RET-BEA-500', 'Whole Bean Coffee 500g', (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL_COFFEE'), v_pcs_uom_id, 'RETAIL_PRODUCT', 15.00, TRUE),
  ('RET-BEA-1KG', 'Whole Bean Coffee 1kg', (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL_COFFEE'), v_pcs_uom_id, 'RETAIL_PRODUCT', 10.00, TRUE),
  ('RET-GRN-250', 'Ground Coffee 250g', (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL_COFFEE'), v_pcs_uom_id, 'RETAIL_PRODUCT', 15.00, TRUE),
  ('RET-GRN-500', 'Ground Coffee 500g', (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL_COFFEE'), v_pcs_uom_id, 'RETAIL_PRODUCT', 10.00, TRUE),
  ('RET-GRN-1KG', 'Ground Coffee 1kg', (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL_COFFEE'), v_pcs_uom_id, 'RETAIL_PRODUCT', 10.00, TRUE),
  ('RET-INS-JAR', 'Instant Coffee Jar', (SELECT id FROM product_categories WHERE code = 'CAT_RETAIL_COFFEE'), v_pcs_uom_id, 'RETAIL_PRODUCT', 15.00, TRUE);

  -- 21. Merchandise (MERCHANDISE, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('MER-MUG-COF', 'Coffee Mug', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE_SUB'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('MER-MUG-TRV', 'Travel Mug', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE_SUB'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('MER-MUG-CRM', 'Ceramic Mug', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE_SUB'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('MER-MUG-GLS', 'Glass Mug', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE_SUB'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('MER-TUM-SST', 'Stainless Steel Tumbler', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE_SUB'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('MER-BOT-INS', 'Insulated Bottle', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE_SUB'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('MER-BOT-WAT', 'Water Bottle', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE_SUB'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('MER-CUP-REU', 'Reusable Cup', (SELECT id FROM product_categories WHERE code = 'CAT_MERCHANDISE_SUB'), v_pcs_uom_id, 'MERCHANDISE', 15.00, TRUE);

  -- 22. Accessories (MERCHANDISE, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('ACC-PRS-FRE', 'French Press', (SELECT id FROM product_categories WHERE code = 'CAT_ACCESSORIES'), v_pcs_uom_id, 'MERCHANDISE', 5.00, TRUE),
  ('ACC-GRN-COF', 'Coffee Grinder', (SELECT id FROM product_categories WHERE code = 'CAT_ACCESSORIES'), v_pcs_uom_id, 'MERCHANDISE', 5.00, TRUE),
  ('ACC-DRP-POV', 'Pour Over Dripper', (SELECT id FROM product_categories WHERE code = 'CAT_ACCESSORIES'), v_pcs_uom_id, 'MERCHANDISE', 5.00, TRUE),
  ('ACC-FLT-COF', 'Coffee Filter', (SELECT id FROM product_categories WHERE code = 'CAT_ACCESSORIES'), v_pcs_uom_id, 'MERCHANDISE', 20.00, TRUE),
  ('ACC-SPN-MES', 'Measuring Spoon', (SELECT id FROM product_categories WHERE code = 'CAT_ACCESSORIES'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('ACC-SCP-COF', 'Coffee Scoop', (SELECT id FROM product_categories WHERE code = 'CAT_ACCESSORIES'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('ACC-PIT-MLK', 'Milk Pitcher', (SELECT id FROM product_categories WHERE code = 'CAT_ACCESSORIES'), v_pcs_uom_id, 'MERCHANDISE', 10.00, TRUE),
  ('ACC-TMP-COF', 'Tamper', (SELECT id FROM product_categories WHERE code = 'CAT_ACCESSORIES'), v_pcs_uom_id, 'MERCHANDISE', 5.00, TRUE);

  -- 23. Packaging (PACKAGING, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('PKG-CUP-SML', 'Small Paper Cup', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 500.00, TRUE),
  ('PKG-CUP-MED', 'Medium Paper Cup', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 500.00, TRUE),
  ('PKG-CUP-LRG', 'Large Paper Cup', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 500.00, TRUE),
  ('PKG-CUP-PLS', 'Plastic Cup', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 500.00, TRUE),
  ('PKG-LID-HOT', 'Hot Cup Lid', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 1000.00, TRUE),
  ('PKG-LID-CLD', 'Cold Cup Lid', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 1000.00, TRUE),
  ('PKG-SLV-CUP', 'Cup Sleeve', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 500.00, TRUE),
  ('PKG-STR-PAP', 'Paper Straw', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 1000.00, TRUE),
  ('PKG-STR-PLS', 'Plastic Straw', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 1000.00, TRUE),
  ('PKG-STR-WOD', 'Wooden Stirrer', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 1000.00, TRUE),
  ('PKG-NPK-ALL', 'Napkin', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 2000.00, TRUE),
  ('PKG-BAG-CRY', 'Carry Bag', (SELECT id FROM product_categories WHERE code = 'CAT_PACKAGING_SUB'), v_pcs_uom_id, 'PACKAGING', 500.00, TRUE);

  -- 24. Cleaning Supplies (CONSUMABLE, PCS/L)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('OPS-CLN-DSH', 'Dish Wash Liquid', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_l_uom_id, 'CONSUMABLE', 5.00, TRUE),
  ('OPS-CLN-SUR', 'Surface Cleaner', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_l_uom_id, 'CONSUMABLE', 5.00, TRUE),
  ('OPS-CLN-GLS', 'Glass Cleaner', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_l_uom_id, 'CONSUMABLE', 5.00, TRUE),
  ('OPS-CLN-MAC', 'Coffee Machine Cleaner', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_pcs_uom_id, 'CONSUMABLE', 10.00, TRUE),
  ('OPS-CLN-SAN', 'Sanitizer', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_l_uom_id, 'CONSUMABLE', 5.00, TRUE),
  ('OPS-CLN-HSN', 'Hand Sanitizer', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_l_uom_id, 'CONSUMABLE', 5.00, TRUE),
  ('OPS-CLN-GLV', 'Disposable Gloves', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_pcs_uom_id, 'CONSUMABLE', 100.00, TRUE),
  ('OPS-CLN-CLT', 'Cleaning Cloth', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_pcs_uom_id, 'CONSUMABLE', 20.00, TRUE),
  ('OPS-CLN-MOP', 'Mop', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_pcs_uom_id, 'CONSUMABLE', 2.00, TRUE),
  ('OPS-CLN-GBG', 'Garbage Bag', (SELECT id FROM product_categories WHERE code = 'CAT_CLEANING'), v_pcs_uom_id, 'CONSUMABLE', 50.00, TRUE);

  -- 25. Kitchen Supplies (CONSUMABLE, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('OPS-KIT-FOL', 'Aluminium Foil', (SELECT id FROM product_categories WHERE code = 'CAT_KITCHEN'), v_pcs_uom_id, 'CONSUMABLE', 10.00, TRUE),
  ('OPS-KIT-WRP', 'Cling Wrap', (SELECT id FROM product_categories WHERE code = 'CAT_KITCHEN'), v_pcs_uom_id, 'CONSUMABLE', 10.00, TRUE),
  ('OPS-KIT-PAP', 'Baking Paper', (SELECT id FROM product_categories WHERE code = 'CAT_KITCHEN'), v_pcs_uom_id, 'CONSUMABLE', 10.00, TRUE),
  ('OPS-KIT-GAS', 'Gas Canister', (SELECT id FROM product_categories WHERE code = 'CAT_KITCHEN'), v_pcs_uom_id, 'CONSUMABLE', 5.00, TRUE),
  ('OPS-KIT-TIS', 'Kitchen Tissue', (SELECT id FROM product_categories WHERE code = 'CAT_KITCHEN'), v_pcs_uom_id, 'CONSUMABLE', 20.00, TRUE),
  ('OPS-KIT-CNT', 'Food Container', (SELECT id FROM product_categories WHERE code = 'CAT_KITCHEN'), v_pcs_uom_id, 'CONSUMABLE', 30.00, TRUE);

  -- 26. Office Supplies (CONSUMABLE, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('OPS-OFC-ROL', 'Receipt Roll', (SELECT id FROM product_categories WHERE code = 'CAT_OFFICE'), v_pcs_uom_id, 'CONSUMABLE', 50.00, TRUE),
  ('OPS-OFC-PPR', 'Printer Paper', (SELECT id FROM product_categories WHERE code = 'CAT_OFFICE'), v_box_uom_id, 'CONSUMABLE', 2.00, TRUE),
  ('OPS-OFC-A4P', 'A4 Paper', (SELECT id FROM product_categories WHERE code = 'CAT_OFFICE'), v_box_uom_id, 'CONSUMABLE', 2.00, TRUE),
  ('OPS-OFC-PEN', 'Pen', (SELECT id FROM product_categories WHERE code = 'CAT_OFFICE'), v_pcs_uom_id, 'CONSUMABLE', 20.00, TRUE),
  ('OPS-OFC-MRK', 'Marker', (SELECT id FROM product_categories WHERE code = 'CAT_OFFICE'), v_pcs_uom_id, 'CONSUMABLE', 10.00, TRUE),
  ('OPS-OFC-STP', 'Stapler', (SELECT id FROM product_categories WHERE code = 'CAT_OFFICE'), v_pcs_uom_id, 'CONSUMABLE', 2.00, TRUE),
  ('OPS-OFC-NTB', 'Notebook', (SELECT id FROM product_categories WHERE code = 'CAT_OFFICE'), v_pcs_uom_id, 'CONSUMABLE', 5.00, TRUE);

  -- 27. Uniforms (NON_INVENTORY, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('OPS-UNI-APR', 'Barista Apron', (SELECT id FROM product_categories WHERE code = 'CAT_UNIFORMS'), v_pcs_uom_id, 'NON_INVENTORY', 10.00, TRUE),
  ('OPS-UNI-TSH', 'Staff T-Shirt', (SELECT id FROM product_categories WHERE code = 'CAT_UNIFORMS'), v_pcs_uom_id, 'NON_INVENTORY', 20.00, TRUE),
  ('OPS-UNI-JKT', 'Chef Jacket', (SELECT id FROM product_categories WHERE code = 'CAT_UNIFORMS'), v_pcs_uom_id, 'NON_INVENTORY', 5.00, TRUE),
  ('OPS-UNI-CAP', 'Cap', (SELECT id FROM product_categories WHERE code = 'CAT_UNIFORMS'), v_pcs_uom_id, 'NON_INVENTORY', 10.00, TRUE),
  ('OPS-UNI-NET', 'Hair Net', (SELECT id FROM product_categories WHERE code = 'CAT_UNIFORMS'), v_pcs_uom_id, 'NON_INVENTORY', 100.00, TRUE),
  ('OPS-UNI-BDG', 'Name Badge', (SELECT id FROM product_categories WHERE code = 'CAT_UNIFORMS'), v_pcs_uom_id, 'NON_INVENTORY', 10.00, TRUE);

  -- 28. Equipment & Assets (ASSET, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('AST-EQP-ESM', 'Espresso Machine', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-GRN', 'Coffee Grinder Machine', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-BWR', 'Coffee Brewer', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-REF', 'Refrigerator', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-FRZ', 'Freezer', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-ICE', 'Ice Machine', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-OVN', 'Oven', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-MWO', 'Microwave Oven', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-DSH', 'Dishwasher', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-POS', 'POS Terminal', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-SCN', 'Barcode Scanner', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-PRN', 'Receipt Printer', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-DWR', 'Cash Drawer', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE),
  ('AST-EQP-TAB', 'Tablet', (SELECT id FROM product_categories WHERE code = 'CAT_ASSETS_SUB'), v_pcs_uom_id, 'ASSET', 0.00, TRUE);

  -- 29. Spare Parts (SPARE_PART, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('AST-PAR-BUR', 'Grinder Burr', (SELECT id FROM product_categories WHERE code = 'CAT_SPARE_PARTS'), v_pcs_uom_id, 'SPARE_PART', 2.00, TRUE),
  ('AST-PAR-FLT', 'Water Filter', (SELECT id FROM product_categories WHERE code = 'CAT_SPARE_PARTS'), v_pcs_uom_id, 'SPARE_PART', 5.00, TRUE),
  ('AST-PAR-WND', 'Steam Wand', (SELECT id FROM product_categories WHERE code = 'CAT_SPARE_PARTS'), v_pcs_uom_id, 'SPARE_PART', 2.00, TRUE),
  ('AST-PAR-FTH', 'Milk Frother', (SELECT id FROM product_categories WHERE code = 'CAT_SPARE_PARTS'), v_pcs_uom_id, 'SPARE_PART', 2.00, TRUE),
  ('AST-PAR-PMP', 'Pump', (SELECT id FROM product_categories WHERE code = 'CAT_SPARE_PARTS'), v_pcs_uom_id, 'SPARE_PART', 1.00, TRUE),
  ('AST-PAR-HTE', 'Heating Element', (SELECT id FROM product_categories WHERE code = 'CAT_SPARE_PARTS'), v_pcs_uom_id, 'SPARE_PART', 1.00, TRUE),
  ('AST-PAR-GSK', 'Gasket', (SELECT id FROM product_categories WHERE code = 'CAT_SPARE_PARTS'), v_pcs_uom_id, 'SPARE_PART', 10.00, TRUE),
  ('AST-PAR-VLV', 'Coffee Machine Valve', (SELECT id FROM product_categories WHERE code = 'CAT_SPARE_PARTS'), v_pcs_uom_id, 'SPARE_PART', 2.00, TRUE);

  -- 30. Promotional Items (MARKETING, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('OPS-MKT-CRD', 'Gift Card', (SELECT id FROM product_categories WHERE code = 'CAT_PROMOTIONS'), v_pcs_uom_id, 'MARKETING', 100.00, TRUE),
  ('OPS-MKT-VCH', 'Gift Voucher', (SELECT id FROM product_categories WHERE code = 'CAT_PROMOTIONS'), v_pcs_uom_id, 'MARKETING', 100.00, TRUE),
  ('OPS-MKT-STK', 'Sticker Pack', (SELECT id FROM product_categories WHERE code = 'CAT_PROMOTIONS'), v_pcs_uom_id, 'MARKETING', 50.00, TRUE),
  ('OPS-MKT-CPN', 'Promotional Coupon', (SELECT id FROM product_categories WHERE code = 'CAT_PROMOTIONS'), v_pcs_uom_id, 'MARKETING', 100.00, TRUE);

  -- 31. Gift Boxes (RETAIL_PRODUCT, PCS)
  INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active) VALUES
  ('RET-BOX-GF1', 'Coffee Gift Box', (SELECT id FROM product_categories WHERE code = 'CAT_GIFT_BOXES_RET'), v_pcs_uom_id, 'RETAIL_PRODUCT', 5.00, TRUE),
  ('RET-BOX-GF2', 'Premium Gift Box', (SELECT id FROM product_categories WHERE code = 'CAT_GIFT_BOXES_RET'), v_pcs_uom_id, 'RETAIL_PRODUCT', 5.00, TRUE),
  ('RET-BOX-SET', 'Merchandise Gift Set', (SELECT id FROM product_categories WHERE code = 'CAT_GIFT_BOXES_RET'), v_pcs_uom_id, 'RETAIL_PRODUCT', 5.00, TRUE),
  ('RET-BOX-HMP', 'Seasonal Gift Hamper', (SELECT id FROM product_categories WHERE code = 'CAT_GIFT_BOXES_RET'), v_pcs_uom_id, 'RETAIL_PRODUCT', 5.00, TRUE);

END $$;
