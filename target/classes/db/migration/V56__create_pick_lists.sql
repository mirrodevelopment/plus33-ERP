-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 56
-- File              : V56__create_pick_lists.sql
-- Operation Type    : Schema Creation
-- Purpose           : create pick lists
--
-- Tables Created    : pick_lists, pick_list_items, inventory_allocations
-- Tables Altered    : sales_order_items
-- Seed Data For     : N/A
-- Indexes           : idx_pick_lists_company, idx_pick_lists_order, idx_pick_items_list, idx_allocations_order, idx_allocations_status
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V56__create_pick_lists.sql
-- PLUS33 ERP — Order Fulfillment & Picking Schema
-- ============================================================

CREATE SEQUENCE pick_list_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE pick_lists (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    sales_order_id BIGINT NOT NULL,
    pick_number VARCHAR(50) NOT NULL,
    client_reference_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    
    -- Location scoping
    warehouse_id BIGINT,
    store_id BIGINT,
    
    -- Audit trail fields
    created_by BIGINT NOT NULL,
    released_by BIGINT,
    picked_by BIGINT,
    packed_by BIGINT,
    shipped_by BIGINT,
    cancelled_by BIGINT,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    released_at TIMESTAMP,
    picked_at TIMESTAMP,
    packed_at TIMESTAMP,
    shipped_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_pick_lists_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_pick_lists_order FOREIGN KEY (sales_order_id) REFERENCES sales_orders(id),
    CONSTRAINT fk_pick_lists_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_pick_lists_store FOREIGN KEY (store_id) REFERENCES stores(id),
    CONSTRAINT fk_pick_lists_created FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_pick_lists_released FOREIGN KEY (released_by) REFERENCES users(id),
    CONSTRAINT fk_pick_lists_picked FOREIGN KEY (picked_by) REFERENCES users(id),
    CONSTRAINT fk_pick_lists_packed FOREIGN KEY (packed_by) REFERENCES users(id),
    CONSTRAINT fk_pick_lists_shipped FOREIGN KEY (shipped_by) REFERENCES users(id),
    CONSTRAINT fk_pick_lists_cancelled FOREIGN KEY (cancelled_by) REFERENCES users(id),
    
    CONSTRAINT uk_pick_list_company_number UNIQUE (company_id, pick_number),
    CONSTRAINT uk_pick_list_client_reference UNIQUE (company_id, client_reference_id),
    CONSTRAINT chk_pick_list_location CHECK (
        (warehouse_id IS NOT NULL AND store_id IS NULL)
        OR
        (warehouse_id IS NULL AND store_id IS NOT NULL)
    ),
    CONSTRAINT chk_pick_list_status CHECK (
        status IN ('DRAFT', 'RELEASED', 'PICKING', 'PICKED', 'PACKED', 'SHIPPED', 'CANCELLED')
    )
);

CREATE TABLE pick_list_items (
    id BIGSERIAL PRIMARY KEY,
    pick_list_id BIGINT NOT NULL,
    sales_order_item_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    ordered_quantity DECIMAL(12,2) NOT NULL,
    allocated_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    picked_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    shipped_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_pick_items_list FOREIGN KEY (pick_list_id) REFERENCES pick_lists(id) ON DELETE CASCADE,
    CONSTRAINT fk_pick_items_so_item FOREIGN KEY (sales_order_item_id) REFERENCES sales_order_items(id),
    CONSTRAINT fk_pick_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT uq_pick_item_product UNIQUE (pick_list_id, product_id) DEFERRABLE INITIALLY DEFERRED,
    
    CONSTRAINT chk_pick_item_ordered CHECK (ordered_quantity > 0.00),
    CONSTRAINT chk_pick_item_allocated CHECK (allocated_quantity >= 0.00 AND allocated_quantity <= ordered_quantity),
    CONSTRAINT chk_pick_item_picked CHECK (picked_quantity >= 0.00 AND picked_quantity <= allocated_quantity),
    CONSTRAINT chk_pick_item_shipped CHECK (shipped_quantity >= 0.00 AND shipped_quantity <= picked_quantity)
);

CREATE TABLE inventory_allocations (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    sales_order_id BIGINT NOT NULL,
    sales_order_item_id BIGINT NOT NULL,
    pick_list_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    
    -- Location where stock is allocated
    warehouse_id BIGINT,
    store_id BIGINT,
    
    allocated_quantity DECIMAL(12,2) NOT NULL,
    allocation_status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    allocation_reference VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_allocations_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_allocations_order FOREIGN KEY (sales_order_id) REFERENCES sales_orders(id),
    CONSTRAINT fk_allocations_so_item FOREIGN KEY (sales_order_item_id) REFERENCES sales_order_items(id),
    CONSTRAINT fk_allocations_pick_list FOREIGN KEY (pick_list_id) REFERENCES pick_lists(id),
    CONSTRAINT fk_allocations_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_allocations_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_allocations_store FOREIGN KEY (store_id) REFERENCES stores(id),
    
    CONSTRAINT chk_allocation_location CHECK (
        (warehouse_id IS NOT NULL AND store_id IS NULL)
        OR
        (warehouse_id IS NULL AND store_id IS NOT NULL)
    ),
    CONSTRAINT chk_allocation_status CHECK (
        allocation_status IN ('ACTIVE', 'RELEASED', 'CONSUMED', 'CANCELLED')
    ),
    CONSTRAINT chk_allocation_quantity CHECK (allocated_quantity >= 0.00)
);

-- Alter sales_order_items to add quantity tracking fields
ALTER TABLE sales_order_items 
ADD COLUMN allocated_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
ADD COLUMN fulfilled_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
ADD COLUMN invoiced_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
ADD CONSTRAINT chk_so_item_allocated CHECK (allocated_quantity >= 0.00),
ADD CONSTRAINT chk_so_item_fulfilled CHECK (fulfilled_quantity >= 0.00),
ADD CONSTRAINT chk_so_item_invoiced CHECK (invoiced_quantity >= 0.00);

CREATE INDEX idx_pick_lists_company ON pick_lists(company_id);
CREATE INDEX idx_pick_lists_order ON pick_lists(sales_order_id);
CREATE INDEX idx_pick_items_list ON pick_list_items(pick_list_id);
CREATE INDEX idx_allocations_order ON inventory_allocations(sales_order_id);
CREATE INDEX idx_allocations_status ON inventory_allocations(allocation_status);
