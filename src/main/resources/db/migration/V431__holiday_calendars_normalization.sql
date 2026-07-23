-- =============================================================================
-- V431: Normalize Holiday Calendars and Holidays
--
-- Replaces singular holiday_calendar table with a normalized structure:
-- holiday_calendars (parent per policy group + year) and holidays (child table).
-- Includes automated data migration.
-- =============================================================================

CREATE TABLE IF NOT EXISTS holiday_calendars (
    id                  BIGSERIAL PRIMARY KEY,
    policy_group_id     BIGINT NOT NULL REFERENCES leave_policy_groups(id) ON DELETE CASCADE,
    year                INT NOT NULL,
    name                VARCHAR(150) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (policy_group_id, year)
);

CREATE TABLE IF NOT EXISTS holidays (
    id                  BIGSERIAL PRIMARY KEY,
    calendar_id         BIGINT NOT NULL REFERENCES holiday_calendars(id) ON DELETE CASCADE,
    holiday_name        VARCHAR(150) NOT NULL,
    holiday_date        DATE NOT NULL,
    is_working_day      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (calendar_id, holiday_date)
);

-- Copy existing data to normalized tables
-- Maps country code: IN/IND -> INDIA; AE/UAE -> UAE; others (FR, EU) -> EU.
INSERT INTO holiday_calendars (policy_group_id, year, name, active)
SELECT DISTINCT
    g.id,
    c.holiday_year,
    g.name || ' Calendar ' || c.holiday_year,
    TRUE
FROM holiday_calendar c
JOIN leave_policy_groups g ON
    (c.country_code IN ('IN', 'IND') AND g.code = 'INDIA') OR
    (c.country_code IN ('AE', 'UAE') AND g.code = 'UAE') OR
    (c.country_code NOT IN ('IN', 'IND', 'AE', 'UAE') AND g.code = 'EU')
ON CONFLICT (policy_group_id, year) DO NOTHING;

INSERT INTO holidays (calendar_id, holiday_name, holiday_date, is_working_day)
SELECT
    hc.id,
    c.holiday_name,
    c.holiday_date,
    COALESCE(c.is_working_day, FALSE)
FROM holiday_calendar c
JOIN leave_policy_groups g ON
    (c.country_code IN ('IN', 'IND') AND g.code = 'INDIA') OR
    (c.country_code IN ('AE', 'UAE') AND g.code = 'UAE') OR
    (c.country_code NOT IN ('IN', 'IND', 'AE', 'UAE') AND g.code = 'EU')
JOIN holiday_calendars hc ON hc.policy_group_id = g.id AND hc.year = c.holiday_year
ON CONFLICT (calendar_id, holiday_date) DO NOTHING;
