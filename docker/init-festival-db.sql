-- Festival Application Database Initialization Script
-- Creates tables and sample data for the Festival Application

-- Create DJ table
CREATE TABLE IF NOT EXISTS dj (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    experience_years INTEGER NOT NULL CHECK (experience_years >= 0),
    contact_email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Performance table
CREATE TABLE IF NOT EXISTS performance (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    venue VARCHAR(100) NOT NULL,
    dj_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dj_id) REFERENCES dj(id) ON DELETE CASCADE,
    CHECK (end_time > start_time)
);

-- Insert sample DJs
INSERT INTO dj (name, genre, experience_years, contact_email) VALUES
('DJ Thunderbolt', 'Electronic', 8, 'thunderbolt@festival.com'),
('DJ Harmony', 'House', 12, 'harmony@festival.com'),
('DJ Vortex', 'Techno', 6, 'vortex@festival.com'),
('DJ Serenity', 'Ambient', 10, 'serenity@festival.com'),
('DJ Phoenix', 'Trance', 15, 'phoenix@festival.com')
ON CONFLICT (contact_email) DO NOTHING;

-- Insert sample Performances
INSERT INTO performance (title, description, start_time, end_time, venue, dj_id) VALUES
('Electric Night Opening', 'Opening ceremony with electronic beats', '2024-07-15 18:00:00', '2024-07-15 20:00:00', 'Main Stage', 1),
('House Party Vibes', 'Deep house music session', '2024-07-15 20:30:00', '2024-07-15 22:30:00', 'Club Tent', 2),
('Techno Underground', 'Underground techno experience', '2024-07-15 23:00:00', '2024-07-16 01:00:00', 'Underground Stage', 3),
('Ambient Dreams', 'Relaxing ambient soundscape', '2024-07-16 14:00:00', '2024-07-16 16:00:00', 'Chill Zone', 4),
('Trance Journey', 'Epic trance music journey', '2024-07-16 21:00:00', '2024-07-16 23:30:00', 'Main Stage', 5);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_dj_genre ON dj(genre);
CREATE INDEX IF NOT EXISTS idx_performance_start_time ON performance(start_time);
CREATE INDEX IF NOT EXISTS idx_performance_dj_id ON performance(dj_id);
CREATE INDEX IF NOT EXISTS idx_performance_venue ON performance(venue);

-- Update timestamp trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at columns
DROP TRIGGER IF EXISTS update_dj_updated_at ON dj;
CREATE TRIGGER update_dj_updated_at 
    BEFORE UPDATE ON dj 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_performance_updated_at ON performance;
CREATE TRIGGER update_performance_updated_at 
    BEFORE UPDATE ON performance 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO festival_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO festival_user;
