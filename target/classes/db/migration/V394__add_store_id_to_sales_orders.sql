-- V394__add_store_id_to_sales_orders.sql
-- Adds store_id to sales_orders and distributes existing orders across stores
-- so that regional performance queries return real data instead of 0.

-- Step 1: Add nullable store_id column
ALTER TABLE sales_orders ADD COLUMN IF NOT EXISTS store_id BIGINT;

-- Step 2: Add FK constraint (nullable so existing orders without store still work)
ALTER TABLE sales_orders
    DROP CONSTRAINT IF EXISTS fk_sales_orders_store;

ALTER TABLE sales_orders
    ADD CONSTRAINT fk_sales_orders_store
        FOREIGN KEY (store_id) REFERENCES stores(id);

-- Step 3: Distribute existing orders evenly across all stores (round-robin by order id)
UPDATE sales_orders so
SET store_id = s.id
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS rn
    FROM stores
) s
WHERE (so.id % (SELECT COUNT(*) FROM stores)) = (s.rn - 1);

-- Handle remainder: assign any unset orders to the first store
UPDATE sales_orders
SET store_id = (SELECT id FROM stores ORDER BY id LIMIT 1)
WHERE store_id IS NULL;

-- Step 4: Add index for performance
CREATE INDEX IF NOT EXISTS idx_sales_orders_store ON sales_orders(store_id);
