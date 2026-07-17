-- V416__add_emergency_contact_phone_to_employees.sql
-- Adds an emergency contact phone number column to the employees table
-- so staff can record a next-of-kin / emergency contact number on their profile.

ALTER TABLE employees
    ADD COLUMN IF NOT EXISTS emergency_contact_phone VARCHAR(30) DEFAULT NULL;
