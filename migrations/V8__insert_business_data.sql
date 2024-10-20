-- Insert data into the businesses table
INSERT INTO businesses (user_id, name, contact_email, contact_phone, created_at) VALUES
    (1, 'Tech Innovations Inc.', 'contact@techinnovations.com', '123-456-7890', '2024-10-01 10:00:00'),
    (2, 'Health & Wellness Solutions', 'info@wellness.com', '098-765-4321', '2024-10-02 09:30:00'),
    (3, 'Creative Workspaces', 'hello@creativeworkspaces.com', '111-222-3333', '2024-10-05 14:45:00'),
    (4, 'Skyline Ventures', 'support@skylineventures.com', '444-555-6666', '2024-10-08 11:15:00'),
    (5, 'Urban Spaces Co.', 'contact@urbanspaces.com', '777-888-9999', '2024-10-10 16:20:00');

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
