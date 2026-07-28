-- ============================================================
-- PLUS33 Coffee ERP Migration: V441__seed_store_tasks_data.sql
-- Module  : Store Operations — Clear Seed Data & Reset
-- Purpose : Reset store_tasks table so only live tasks are active
-- ============================================================

TRUNCATE TABLE store_tasks CASCADE;
