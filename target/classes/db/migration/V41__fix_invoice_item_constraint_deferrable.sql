-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 41
-- File              : V41__fix_invoice_item_constraint_deferrable.sql
-- Operation Type    : Schema Alteration
-- Purpose           : fix invoice item constraint deferrable
--
-- Tables Created    : N/A
-- Tables Altered    : supplier_invoice_items, supplier_invoice_items
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V41: Make the supplier_invoice_items unique constraint DEFERRABLE INITIALLY DEFERRED
--
-- Background:
-- When updating SupplierInvoice items via the JPA orphan-removal pattern, Hibernate may
-- INSERT the new items before it flushes the orphan-removal DELETEs for the old items
-- within the same transaction. This caused a DataIntegrityViolationException on the
-- uk_supplier_invoice_item_po_item UNIQUE (supplier_invoice_id, purchase_order_item_id)
-- constraint.
--
-- Making this constraint DEFERRABLE INITIALLY DEFERRED tells PostgreSQL to evaluate the
-- constraint at transaction COMMIT time rather than at each INSERT statement. This allows
-- the INSERT + DELETE to coexist transiently within the same transaction without error,
-- while still preventing duplicate (invoice, po_item) combinations in committed data.

ALTER TABLE supplier_invoice_items
    DROP CONSTRAINT uk_supplier_invoice_item_po_item;

ALTER TABLE supplier_invoice_items
    ADD CONSTRAINT uk_supplier_invoice_item_po_item
        UNIQUE (supplier_invoice_id, purchase_order_item_id)
        DEFERRABLE INITIALLY DEFERRED;
