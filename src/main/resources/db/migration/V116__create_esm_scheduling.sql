-- V116: Workforce Scheduling, Technicians, and Skill Certifications
CREATE TABLE IF NOT EXISTS esm_technician_skills (
    id BIGSERIAL PRIMARY KEY,
    technician_id BIGINT NOT NULL,
    skill_code VARCHAR(50) NOT NULL,
    proficiency_level VARCHAR(20) NOT NULL,
    UNIQUE(technician_id, skill_code)
);
