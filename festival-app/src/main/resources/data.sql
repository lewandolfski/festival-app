-- Festival Application - Sample Data Initialization
-- This script populates the database with sample DJs and Performances

-- Insert sample DJs
INSERT INTO djs (id, name, genre, email) VALUES 
('dj-001', 'DJ Thunderbolt', 'Electronic', 'thunderbolt@festival.com'),
('dj-002', 'DJ Harmony', 'House', 'harmony@festival.com'),
('dj-003', 'DJ Vortex', 'Techno', 'vortex@festival.com'),
('dj-004', 'DJ Serenity', 'Ambient', 'serenity@festival.com'),
('dj-005', 'DJ Phoenix', 'Trance', 'phoenix@festival.com');

-- Insert sample Performances
INSERT INTO performances (id, title, description, start_time, end_time, dj_id) VALUES 
('perf-001', 'Electric Night Opening', 'Opening ceremony with electronic beats', '2024-07-15 18:00:00', '2024-07-15 20:00:00', 'dj-001'),
('perf-002', 'House Party Vibes', 'Deep house music session', '2024-07-15 20:30:00', '2024-07-15 22:30:00', 'dj-002'),
('perf-003', 'Techno Underground', 'Underground techno experience', '2024-07-15 23:00:00', '2024-07-16 01:00:00', 'dj-003'),
('perf-004', 'Ambient Dreams', 'Relaxing ambient soundscape', '2024-07-16 14:00:00', '2024-07-16 16:00:00', 'dj-004'),
('perf-005', 'Trance Journey', 'Epic trance music journey', '2024-07-16 21:00:00', '2024-07-16 23:30:00', 'dj-005');
