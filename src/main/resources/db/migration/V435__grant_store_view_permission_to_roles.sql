-- Grant STORE_VIEW permission to all store-related roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('REGIONAL_ADMIN', 'STORE_ADMIN', 'SHIFT_SUPERVISOR', 'SENIOR_EMPLOYEE', 'JUNIOR_EMPLOYEE', 'TRAINEE')
  AND p.code = 'STORE_VIEW'
ON CONFLICT (role_id, permission_id) DO NOTHING;
