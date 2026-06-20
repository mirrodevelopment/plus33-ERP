-- ============================================================
-- V18__create_store_operations.sql
-- PLUS33 ERP — Store Operations module schema
-- ============================================================

CREATE TABLE recipes (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    version INTEGER NOT NULL DEFAULT 1,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_recipes_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT fk_recipes_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT uk_recipe_product_version
        UNIQUE (product_id, version)
);

CREATE TABLE recipe_items (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    ingredient_product_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,

    CONSTRAINT fk_recipe_items_recipe
        FOREIGN KEY (recipe_id)
        REFERENCES recipes(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_recipe_items_product
        FOREIGN KEY (ingredient_product_id)
        REFERENCES products(id),

    CONSTRAINT uk_recipe_ingredient
        UNIQUE (recipe_id, ingredient_product_id)
);

CREATE TABLE sales_transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_number VARCHAR(50) NOT NULL UNIQUE,
    company_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,
    cashier_user_id BIGINT NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    payment_status VARCHAR(30) NOT NULL DEFAULT 'PAID',
    status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    transaction_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sales_transactions_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT fk_sales_transactions_store
        FOREIGN KEY (store_id)
        REFERENCES stores(id),

    CONSTRAINT fk_sales_transactions_user
        FOREIGN KEY (cashier_user_id)
        REFERENCES users(id)
);

CREATE TABLE sales_transaction_items (
    id BIGSERIAL PRIMARY KEY,
    sales_transaction_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    line_total DECIMAL(12,2) NOT NULL,
    stock_movement_id BIGINT,

    CONSTRAINT fk_sales_items_transaction
        FOREIGN KEY (sales_transaction_id)
        REFERENCES sales_transactions(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_sales_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT fk_sales_items_stock_movement
        FOREIGN KEY (stock_movement_id)
        REFERENCES stock_movements(id)
);

CREATE TABLE waste_records (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    waste_type VARCHAR(30) NOT NULL,
    reason VARCHAR(255),
    notes TEXT,
    recorded_by BIGINT NOT NULL,
    approved_by BIGINT,
    stock_movement_id BIGINT,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_waste_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT fk_waste_store
        FOREIGN KEY (store_id)
        REFERENCES stores(id),

    CONSTRAINT fk_waste_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT fk_waste_user
        FOREIGN KEY (recorded_by)
        REFERENCES users(id),

    CONSTRAINT fk_waste_approver
        FOREIGN KEY (approved_by)
        REFERENCES users(id),

    CONSTRAINT fk_waste_stock_movement
        FOREIGN KEY (stock_movement_id)
        REFERENCES stock_movements(id)
);

-- Performance Indexes
CREATE INDEX idx_recipes_company ON recipes(company_id);
CREATE INDEX idx_recipes_product ON recipes(product_id);
CREATE INDEX idx_recipes_active ON recipes(active);

CREATE INDEX idx_recipe_items_recipe ON recipe_items(recipe_id);
CREATE INDEX idx_recipe_items_ingredient ON recipe_items(ingredient_product_id);

CREATE INDEX idx_sales_transactions_company ON sales_transactions(company_id);
CREATE INDEX idx_sales_transactions_store ON sales_transactions(store_id);
CREATE INDEX idx_sales_transactions_cashier ON sales_transactions(cashier_user_id);
CREATE INDEX idx_sales_transactions_time ON sales_transactions(transaction_time);

CREATE INDEX idx_sales_transaction_items_tx ON sales_transaction_items(sales_transaction_id);
CREATE INDEX idx_sales_transaction_items_prod ON sales_transaction_items(product_id);
CREATE INDEX idx_sales_transaction_items_sm ON sales_transaction_items(stock_movement_id);

CREATE INDEX idx_waste_records_company ON waste_records(company_id);
CREATE INDEX idx_waste_records_store ON waste_records(store_id);
CREATE INDEX idx_waste_records_product ON waste_records(product_id);
CREATE INDEX idx_waste_records_recorder ON waste_records(recorded_by);
CREATE INDEX idx_waste_records_approved_by ON waste_records(approved_by);
CREATE INDEX idx_waste_records_sm ON waste_records(stock_movement_id);
CREATE INDEX idx_waste_records_time ON waste_records(recorded_at);
