-- Dynamic seeder: uses information_schema to build INSERTs for remaining empty tables
-- Bypasses FK and check constraints via session_replication_role=replica

SET session_replication_role = replica;

DO $$
DECLARE
  tbl TEXT;
  cols TEXT;
  vals TEXT;
  sql TEXT;
  col_rec RECORD;
  col_name_arr TEXT[];
  val_arr TEXT[];
  val_str TEXT;
  final_str TEXT;
  max_len INT;
  has_warehouse BOOLEAN;
  has_store BOOLEAN;
  has_source_wh BOOLEAN;
  has_source_store BOOLEAN;
  has_dest_wh BOOLEAN;
  has_dest_store BOOLEAN;
  has_supplier_inv BOOLEAN;
  has_customer_inv BOOLEAN;
  has_supplier_id BOOLEAN;
  has_customer_id BOOLEAN;
BEGIN
  FOR tbl IN
    SELECT t.table_name
    FROM information_schema.tables t
    JOIN pg_stat_user_tables s ON s.relname = t.table_name
    WHERE t.table_schema = 'public' AND s.n_live_tup = 0
    ORDER BY t.table_name
  LOOP
    col_name_arr := ARRAY[]::TEXT[];
    val_arr := ARRAY[]::TEXT[];
    
    -- Check columns present in the table to satisfy XOR location/target check constraints
    SELECT 
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='warehouse_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='store_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='source_warehouse_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='source_store_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='dest_warehouse_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='dest_store_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='supplier_invoice_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='customer_invoice_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='supplier_id'),
      EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema='public' AND table_name=tbl AND column_name='customer_id')
    INTO 
      has_warehouse, has_store, 
      has_source_wh, has_source_store,
      has_dest_wh, has_dest_store,
      has_supplier_inv, has_customer_inv,
      has_supplier_id, has_customer_id;

    FOR col_rec IN
      SELECT column_name, data_type, udt_name, is_nullable, column_default,
             character_maximum_length, numeric_precision, numeric_scale,
             is_identity, is_generated
      FROM information_schema.columns
      WHERE table_schema = 'public' AND table_name = tbl
      ORDER BY ordinal_position
    LOOP
      -- Skip columns that have defaults, are identities, or are generated always
      IF col_rec.column_default IS NOT NULL 
         OR col_rec.is_identity = 'YES' 
         OR col_rec.is_generated = 'ALWAYS' THEN
        CONTINUE;
      END IF;
      
      col_name_arr := array_append(col_name_arr, '"' || col_rec.column_name || '"');
      
      -- Location check constraints overrides
      IF col_rec.column_name = 'warehouse_id' AND has_store THEN
        val_str := '1';
      ELSIF col_rec.column_name = 'store_id' AND has_warehouse THEN
        val_str := 'NULL';
      
      ELSIF col_rec.column_name = 'source_warehouse_id' AND has_source_store THEN
        val_str := '1';
      ELSIF col_rec.column_name = 'source_store_id' AND has_source_wh THEN
        val_str := 'NULL';

      ELSIF col_rec.column_name = 'dest_warehouse_id' AND has_dest_store THEN
        val_str := '1';
      ELSIF col_rec.column_name = 'dest_store_id' AND has_dest_wh THEN
        val_str := 'NULL';

      -- Payment allocation check constraint overrides
      ELSIF col_rec.column_name = 'supplier_invoice_id' AND has_customer_inv THEN
        val_str := '1';
      ELSIF col_rec.column_name = 'customer_invoice_id' AND has_supplier_inv THEN
        val_str := 'NULL';

      -- Payments table supplier_id vs customer_id override
      ELSIF col_rec.column_name = 'supplier_id' AND has_customer_id AND tbl = 'payments' THEN
        val_str := '1';
      ELSIF col_rec.column_name = 'customer_id' AND has_supplier_id AND tbl = 'payments' THEN
        val_str := 'NULL';

      -- Numeric check constraints for replenishment_rules
      ELSIF col_rec.column_name = 'min_quantity' AND tbl = 'replenishment_rules' THEN
        val_str := '1.00';
      ELSIF col_rec.column_name = 'reorder_point' AND tbl = 'replenishment_rules' THEN
        val_str := '2.00';
      ELSIF col_rec.column_name = 'max_quantity' AND tbl = 'replenishment_rules' THEN
        val_str := '100.00';
      ELSIF col_rec.column_name = 'reorder_quantity' AND tbl = 'replenishment_rules' THEN
        val_str := '10.00';

      -- Generic generation
      ELSIF col_rec.data_type = 'boolean' THEN
        val_str := 'false';
      
      ELSIF col_rec.data_type LIKE 'timestamp%' THEN
        val_str := 'NOW()';
      
      ELSIF col_rec.data_type = 'date' THEN
        val_str := 'CURRENT_DATE';
      
      ELSIF col_rec.data_type LIKE 'time%' THEN
        val_str := '''09:00:00''';
      
      ELSIF col_rec.data_type IN ('numeric','decimal','real','double precision') THEN
        val_str := '1.00';
      
      ELSIF col_rec.data_type IN ('integer','bigint','smallint') THEN
        IF col_rec.column_name = 'company_id' THEN 
          val_str := '1';
        ELSIF col_rec.column_name IN ('created_by','updated_by','approved_by','modified_by',
              'submitted_by','received_by','requested_by','assigned_to','reviewed_by',
              'closed_by','cancelled_by','inspected_by','posted_by','locked_by',
              'released_by','picked_by','packed_by','shipped_by','started_by',
              'assigned_by','generated_by','employee_id','user_id','shift_id','product_id') THEN 
          val_str := '207';
        ELSIF col_rec.column_name LIKE '%_id' AND col_rec.is_nullable = 'YES' THEN 
          val_str := 'NULL';
        ELSE 
          val_str := '1';
        END IF;
      
      ELSIF col_rec.udt_name = 'uuid' THEN
        val_str := 'gen_random_uuid()';
      
      ELSIF col_rec.data_type IN ('json','jsonb') THEN
        val_str := '''{}''::json';
      
      ELSIF left(col_rec.udt_name, 1) = '_' OR col_rec.data_type = 'ARRAY' THEN
        val_str := '''{}''';
      
      ELSIF col_rec.data_type = 'interval' THEN
        val_str := '''1 day''';
      
      ELSIF col_rec.udt_name IN ('inet','cidr') THEN
        val_str := '''192.168.1.1''';
      
      ELSIF col_rec.data_type IN ('character varying','text','character','name','citext') THEN
        max_len := col_rec.character_maximum_length;
        
        -- Custom check constraints values
        IF col_rec.column_name = 'count_type' AND tbl = 'stock_counts' THEN
          final_str := 'FULL';
        ELSIF col_rec.column_name = 'status' AND tbl = 'stock_counts' THEN
          final_str := 'DRAFT';
        ELSIF col_rec.column_name = 'entity_type' AND tbl = 'tax_registrations' THEN
          final_str := 'COMPANY';
        ELSIF col_rec.column_name = 'event_type' AND tbl = 'inventory_trace_events' THEN
          final_str := 'RECEIPT';
        ELSIF col_rec.column_name = 'reference_type' AND tbl = 'inventory_trace_events' THEN
          final_str := 'GOODS_RECEIPT';
        ELSIF col_rec.column_name = 'payment_method' AND tbl = 'payments' THEN
          final_str := 'BANK_TRANSFER';
        ELSIF col_rec.column_name = 'payment_type' AND tbl = 'payments' THEN
          final_str := 'PAYABLE';
        ELSIF col_rec.column_name = 'status' AND tbl = 'payments' THEN
          final_str := 'COMPLETED';
        ELSIF col_rec.column_name = 'status' AND tbl = 'inventory_transfers' THEN
          final_str := 'DRAFT';
        ELSIF col_rec.column_name = 'status' AND tbl = 'inventory_adjustments' THEN
          final_str := 'DRAFT';
        ELSIF col_rec.column_name = 'adjustment_type' AND tbl = 'inventory_adjustments' THEN
          final_str := 'DAMAGE';
        ELSIF col_rec.column_name = 'result' AND tbl = 'fixed_asset_audit_items' THEN
          final_str := 'FOUND_OK';
        ELSIF col_rec.column_name = 'lease_type' AND tbl = 'fixed_asset_leases' THEN
          final_str := 'OPERATING';
        ELSIF col_rec.column_name = 'relation_type' AND tbl = 'fixed_asset_relations' THEN
          final_str := 'PARENT_CHILD';
          
        ELSIF col_rec.column_name LIKE '%status%' OR col_rec.column_name = 'status' THEN
          final_str := 'ACTIVE';
        ELSIF col_rec.column_name LIKE '%type%' THEN
          final_str := CASE WHEN max_len IS NOT NULL AND max_len <= 6 THEN 'TYPE1' ELSE 'STANDARD' END;
        ELSIF col_rec.column_name LIKE '%code%' THEN
          final_str := CASE WHEN max_len IS NOT NULL AND max_len <= 3 THEN 'EUR' ELSE 'CODE-001' END;
        ELSIF col_rec.column_name LIKE '%currency%' THEN
          final_str := 'EUR';
        ELSIF col_rec.column_name LIKE '%method%' THEN
          final_str := CASE WHEN max_len IS NOT NULL AND max_len <= 6 THEN 'SL' ELSE 'STRAIGHT_LINE' END;
        ELSIF col_rec.column_name LIKE '%email%' THEN
          final_str := 'seed@plus33.com';
        ELSIF col_rec.column_name LIKE '%number%' OR col_rec.column_name LIKE '%reference%' OR col_rec.column_name LIKE '%ref%' THEN
          final_str := 'REF-SEED-001';
        ELSIF col_rec.is_nullable = 'YES' THEN
          val_str := 'NULL';
          val_arr := array_append(val_arr, val_str);
          CONTINUE;
        ELSE
          final_str := 'Seed_' || col_rec.column_name;
        END IF;
        
        IF max_len IS NOT NULL AND length(final_str) > max_len THEN
          final_str := left(final_str, max_len);
        END IF;
        
        val_str := '''' || replace(final_str, '''', '''''') || '''';
      
      ELSE
        IF col_rec.is_nullable = 'YES' THEN
          val_str := 'NULL';
        ELSE
          val_str := '''1''';
        END IF;
      END IF;
      
      val_arr := array_append(val_arr, val_str);
    END LOOP;
    
    IF array_length(col_name_arr, 1) IS NULL THEN
      RAISE NOTICE 'SKIP (no insertable columns): %', tbl;
      CONTINUE;
    END IF;
    
    cols := array_to_string(col_name_arr, ', ');
    vals := array_to_string(val_arr, ', ');
    
    -- Insert 5 rows (generate unique numbers to avoid unique constraint violations)
    FOR j IN 1..5 LOOP
      -- Replace REF-SEED-001 with a unique number per row
      DECLARE
        row_vals TEXT;
      BEGIN
        row_vals := replace(vals, '''REF-SEED-001''', '''' || 'REF-SEED-' || tbl || '-' || j || '''');
        sql := 'INSERT INTO "' || tbl || '" (' || cols || ') VALUES (' || row_vals || ')';
        EXECUTE sql;
      EXCEPTION WHEN OTHERS THEN
        RAISE NOTICE 'ERR % row %: % (SQL: %)', tbl, j, SQLERRM, sql;
      END;
    END LOOP;
    
    RAISE NOTICE 'Seeded: %', tbl;
  END LOOP;
END $$;

SET session_replication_role = DEFAULT;
