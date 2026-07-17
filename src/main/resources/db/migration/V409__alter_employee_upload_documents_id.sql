-- Migration: Alter employee_upload_documents column types to BIGINT
ALTER TABLE employee_upload_documents ALTER COLUMN id TYPE bigint;
ALTER TABLE employee_upload_documents ALTER COLUMN employee_id TYPE bigint;
