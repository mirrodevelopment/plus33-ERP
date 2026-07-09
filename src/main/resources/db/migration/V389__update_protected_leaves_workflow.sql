-- Update workflow for protected leave types (BEREAVEMENT, MATERNITY, PATERNITY)
-- to be routed to SUPERVISOR instead of SYSTEM, requiring manual approval.
UPDATE leave_approval_workflows
SET approver_role = 'SUPERVISOR', sla_hours = 24
WHERE approver_role = 'SYSTEM';
