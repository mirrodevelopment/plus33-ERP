-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 429
-- File              : V429__create_broadcast_announcements_table.sql
-- Purpose           : Dedicated broadcast_announcements table with 15-day auto-disappear,
--                     60-day soft delete retention, role colors, and multi-media support
-- ============================================================================

CREATE TABLE IF NOT EXISTS broadcast_announcements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    priority VARCHAR(50) NOT NULL DEFAULT 'Standard Notice',
    publisher VARCHAR(255) NOT NULL,
    publisher_role VARCHAR(50) NOT NULL DEFAULT 'STORE_ADMIN',
    publisher_color VARCHAR(30) NOT NULL DEFAULT '#c9a46a',
    image_url VARCHAR(512),
    media_type VARCHAR(30) NOT NULL DEFAULT 'IMAGE',
    target_region_id BIGINT REFERENCES regions(id) ON DELETE CASCADE,
    target_store_id BIGINT REFERENCES stores(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP NOT NULL DEFAULT (NOW() + INTERVAL '15 days'),
    deleted_at TIMESTAMP NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_broadcast_ann_active ON broadcast_announcements(is_deleted, expires_at, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_broadcast_ann_region ON broadcast_announcements(target_region_id);
CREATE INDEX IF NOT EXISTS idx_broadcast_ann_store ON broadcast_announcements(target_store_id);

-- Migrate existing records from legacy announcements table if populated
INSERT INTO broadcast_announcements (title, content, priority, publisher, publisher_role, publisher_color, image_url, media_type, target_region_id, target_store_id, created_at, expires_at)
SELECT 
    title, 
    content, 
    priority, 
    publisher, 
    'REGIONAL_ADMIN', 
    '#06b6d4', 
    image_url, 
    CASE 
        WHEN LOWER(image_url) LIKE '%.mp4' OR LOWER(image_url) LIKE '%.webm' OR LOWER(image_url) LIKE '%.mov' THEN 'VIDEO'
        WHEN LOWER(image_url) LIKE '%.mp3' OR LOWER(image_url) LIKE '%.wav' OR LOWER(image_url) LIKE '%.aac' OR LOWER(image_url) LIKE '%.m4a' THEN 'AUDIO'
        WHEN LOWER(image_url) LIKE '%.pdf' THEN 'PDF'
        WHEN LOWER(image_url) LIKE '%.doc%' OR LOWER(image_url) LIKE '%.xls%' OR LOWER(image_url) LIKE '%.zip' THEN 'DOCUMENT'
        ELSE 'IMAGE'
    END,
    target_region_id, 
    target_store_id, 
    created_at, 
    created_at + INTERVAL '15 days'
FROM announcements
ON CONFLICT DO NOTHING;
