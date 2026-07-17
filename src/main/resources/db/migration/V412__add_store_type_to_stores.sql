-- Add store type column to stores table
ALTER TABLE stores ADD COLUMN type VARCHAR(30) NOT NULL DEFAULT 'COMPACT_CAFE';
