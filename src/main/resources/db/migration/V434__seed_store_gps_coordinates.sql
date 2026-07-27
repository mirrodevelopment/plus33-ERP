-- Seeding default GPS coordinates for stores that do not have them configured
UPDATE stores 
SET latitude = 11.020470, 
    longitude = 76.970698 
WHERE latitude IS NULL OR longitude IS NULL;
