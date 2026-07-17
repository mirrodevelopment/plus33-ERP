-- Update layout formats/types for all 10 seeded store records
UPDATE stores SET type = 'FLAGSHIP_CAFE' WHERE code IN ('ST_PARIS_01', 'ST_LYON_01', 'ST_BORD_01', 'ST_TLSE_01');
UPDATE stores SET type = 'KIOSK' WHERE code IN ('ST_PARIS_02', 'ST_NICE_01', 'ST_GREN_01');
UPDATE stores SET type = 'COMPACT_CAFE' WHERE code IN ('ST_MARS_01', 'ST_MTPL_01', 'ST_LARO_01');
