-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 385
-- File              : V385__create_announcements_table.sql
-- Operation Type    : Create Tables / Seed Data
-- Purpose           : Establish announcements schema and seed initial records
-- ============================================================================

CREATE TABLE announcements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    priority VARCHAR(50) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE announcement_reads (
    id BIGSERIAL PRIMARY KEY,
    announcement_id BIGINT NOT NULL REFERENCES announcements(id) ON DELETE CASCADE,
    username VARCHAR(255) NOT NULL,
    read_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_announcement_reads_user UNIQUE(announcement_id, username)
);

CREATE TABLE announcement_reactions (
    id BIGSERIAL PRIMARY KEY,
    announcement_id BIGINT NOT NULL REFERENCES announcements(id) ON DELETE CASCADE,
    username VARCHAR(255) NOT NULL,
    reaction_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_announcement_reactions_user UNIQUE(announcement_id, username, reaction_type)
);

-- Seed initial announcements
INSERT INTO announcements (title, content, priority, publisher, created_at)
VALUES 
('Upcoming Store Schedule Updates', 'Please check shift rosters for the upcoming week. Stores will operate on Sunday holiday hours (09:00 AM - 05:00 PM) due to city marathon routes.', 'Critical Alert', 'François Martin (Store Manager)', NOW()),
('Barista Safety Standards v2', 'Remember to record machine safety calibration checklists on every morning shift before opening grinders. Grinder #2 requires specific attention.', 'Standard Notice', 'Romain Pichon (Shift Supervisor)', NOW() - INTERVAL '1 day'),
('Monsoon Special Coffee Launch', 'We are launching the Cinnamon Hazelnut Brew starting next Monday. Baristas should check the training hub recipes catalog for brew ratios.', 'Company Bulletin', 'Pooja Sen (L&D Head)', NOW() - INTERVAL '3 days');
