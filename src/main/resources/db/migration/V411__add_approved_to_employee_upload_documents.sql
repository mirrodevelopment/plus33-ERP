-- Flyway Migration V411: Add approved status column to employee_upload_documents table.
-- WHAT IT DOES: Alters the table schema in PostgreSQL database to include a boolean flag 'approved'.
-- STORAGE: Placed in postgres public schema, mapping to public.employee_upload_documents.
-- FLOW: Run automatically by Flyway runtime on Spring Boot application boot.
ALTER TABLE employee_upload_documents ADD COLUMN approved BOOLEAN DEFAULT FALSE;
