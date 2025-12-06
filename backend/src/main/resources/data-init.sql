-- Script para insertar datos de prueba en MigajaApp

-- Usuario admin (password: Yz125-:*)
INSERT INTO users (id, username, email, password, full_name, role, active, created_at) VALUES
(1, 'admin', 'admin@migaja.com', '$2b$12$yZyF/EJHR5QBTca4uSVpDuB/5WG6/LFF6H2kRpGLyB87qHbN8Ov8G', 'Administrador', 'ADMIN', true, CURRENT_TIMESTAMP);

-- Torneos
INSERT INTO tournaments (id, title, description, start_date, end_date, voting_start_date, voting_end_date, 
    entry_fee, prize_amount, status, created_at) VALUES
(1, 'Torneo del Desamor 2025', 
    'Primer gran torneo de historias de desamor. La mejor historia gana $100.000! 
    Inscripción: $10.000. Comparte tu historia y deja que la comunidad vote.', 
    '2025-12-01', '2025-12-31', '2025-12-15', '2025-12-28', 
    10000.00, 100000.00, 'REGISTRATION_OPEN', CURRENT_TIMESTAMP),

(2, 'Historias de Verano', 
    'Comparte tu historia de amor de verano. Premio: $50.000.000', 
    '2026-01-01', '2026-01-31', '2026-01-15', '2026-01-28', 
    5000.00, 50000000.00, 'UPCOMING', CURRENT_TIMESTAMP);

-- Reiniciar secuencias para evitar conflictos con IDs generados
ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE stories ALTER COLUMN id RESTART WITH 100;
ALTER TABLE ratings ALTER COLUMN id RESTART WITH 100;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tournaments ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tournament_entries ALTER COLUMN id RESTART WITH 100;
ALTER TABLE notifications ALTER COLUMN id RESTART WITH 100;
