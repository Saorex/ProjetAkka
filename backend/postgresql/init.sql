CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    wallet_key TEXT NOT NULL
    );

-- Exemple
INSERT INTO users (username, email, password_hash, wallet_key)
VALUES
    ('user1', 'user1@example.com', 'hashed_password_1', 'walletkey1'),
    ('user2', 'user2@example.com', 'hashed_password_2', 'walletkey2')
    ON CONFLICT (username) DO NOTHING;