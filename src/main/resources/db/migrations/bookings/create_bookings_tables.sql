CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,           -- Unique ID for each booking
    user_id INT NOT NULL REFERENCES users(id),  -- Foreign key referencing the user who made the booking
    workspace_id INT NOT NULL REFERENCES workspaces(id),  -- Foreign key referencing the workspace being booked
    booking_date DATE NOT NULL,      -- Date of the booking
    start_time TIME NOT NULL,        -- Start time of the booking
    end_time TIME NOT NULL,          -- End time of the booking
    status VARCHAR(50) DEFAULT 'pending',  -- Status of the booking ('pending', 'confirmed', 'cancelled')
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp of booking creation
);

