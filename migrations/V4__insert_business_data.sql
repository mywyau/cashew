INSERT INTO business (business_id, business_name, contact_email, contact_phone, created_at) VALUES
    ('BUS123456', 'Tech Innovations Inc.', 'info@techinnovations.com', '123-456-7890', '2024-10-10 10:30:00'),
    ('BUS123457', 'Health & Wellness Co.', 'contact@healthwellness.com', '098-765-4321', '2024-10-11 09:45:00'),
    ('BUS123458', 'Urban Spaces LLC', 'support@urbanspaces.com', '111-222-3333', '2024-10-12 11:15:00'),
    ('BUS123459', 'Creative Solutions', 'hello@creativesolutions.com', '444-555-6666', '2024-10-13 14:20:00'),
    ('BUS123460', 'Skyline Ventures', 'sales@skylineventures.com', '777-888-9999', '2024-10-14 16:00:00');

-- Insert data into the facilities table
INSERT INTO facilities (name) VALUES
    ('Wi-Fi'),
    ('Parking'),
    ('Projector'),
    ('Air Conditioning'),
    ('Whiteboard'),
    ('Printing/Scanning Services'),
    ('Lockable Storage'),
    ('Soundproof Phone Booth');


-- Insert data into the amenities table
INSERT INTO amenities (name) VALUES
    ('Coffee Machine'),
    ('Kitchenette'),
    ('Lounge Area'),
    ('Snacks'),
    ('Filtered Water'),
    ('Standing Desks'),
    ('Gym/Fitness Area'),
    ('Game Area'),
    ('Childcare Facility'),
    ('Yoga/Meditation Room');

-- Insert initial workspaces
INSERT INTO workspaces (business_id, workspace_id, name, description, address, city, country, postcode, price_per_day, latitude, longitude)
VALUES
('BUS123456', 'WORK123456', 'Downtown Office', 'A modern office in the city center', '123 Main St', 'New York', 'USA', '10001', 50.00, 40.7128, -74.0060),
('BUS123457', 'WORK123457', 'Uptown Workspace', 'Spacious shared workspace with amenities', '456 Elm St', 'New York', 'USA', '10002', 30.00, 40.7306, -73.9352),
('BUS123458', 'WORK123458', 'London Workspace', 'Spacious shared workspace with amenities', 'Canary Wharf', 'London', 'United Kingdom', 'NW1 4NP', 30.00, 40.7306, -73.9352),
('BUS123459', 'WORK123459', 'New York Workspace', 'Spacious shared workspace with amenities', '456 Elm St', 'New York', 'USA', '10002', 30.00, 40.7306, -73.9352),
('BUS123460', 'WORK123460', 'Cardiff Workspace', 'Spacious shared workspace with amenities', '456 Cardiff Bay', 'Cardiff', 'United Kingdom', 'CF3 3NJ', 30.00, 40.7306, -73.9352);


-- Insert data into the workspace_facilities table
-- Assuming workspace IDs are already created, linking facilities to these workspaces.
INSERT INTO workspace_facilities (workspace_id, facility_id) VALUES
    (1, 1),  -- Workspace 1 has Wi-Fi
    (1, 4),  -- Workspace 1 has Air Conditioning
    (1, 6),  -- Workspace 1 has Printing/Scanning Services

    (2, 1),  -- Workspace 2 has Wi-Fi
    (2, 2),  -- Workspace 2 has Parking
    (2, 5),  -- Workspace 2 has Whiteboard

    (3, 1),  -- Workspace 3 has Wi-Fi
    (3, 3),  -- Workspace 3 has Projector
    (3, 8),  -- Workspace 3 has Soundproof Phone Booth

    (4, 2),  -- Workspace 4 has Parking
    (4, 6),  -- Workspace 4 has Printing/Scanning Services

    (5, 1),  -- Workspace 5 has Wi-Fi
    (5, 7);  -- Workspace 5 has Lockable Storage


