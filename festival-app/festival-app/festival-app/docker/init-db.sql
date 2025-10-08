-- Festival Application - PostgreSQL Database Initialization
-- This script creates the database and user for the Festival Application

-- Create database (if not exists)
SELECT 'CREATE DATABASE festival'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'festival')\gexec

-- Create user and grant privileges
DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_roles
      WHERE  rolname = 'festival_user') THEN

      CREATE ROLE festival_user LOGIN PASSWORD 'festival_pass';
   END IF;
END
$do$;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE festival TO festival_user;

-- Connect to festival database
\c festival;

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO festival_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO festival_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO festival_user;

-- Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO festival_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO festival_user;
