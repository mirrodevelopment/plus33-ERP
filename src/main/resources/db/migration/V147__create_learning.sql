-- V147: LMS Learning and Certifications
CREATE TABLE IF NOT EXISTS hcm_courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    mandatory BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS hcm_learning_enrollments (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ENROLLED',
    completion_date DATE,
    expiry_date DATE
);
