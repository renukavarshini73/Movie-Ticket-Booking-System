-- Movie Ticket Booking System - Sample Data Populate Script

-- 1. Insert Movies
INSERT INTO movie (title, genre, duration_minutes, language, rating) 
VALUES ('Inception', 'Sci-Fi / Action', 148, 'English', 'PG-13');

INSERT INTO movie (title, genre, duration_minutes, language, rating) 
VALUES ('Interstellar', 'Sci-Fi / Adventure', 169, 'English', 'PG-13');

INSERT INTO movie (title, genre, duration_minutes, language, rating) 
VALUES ('The Dark Knight', 'Action / Crime', 152, 'English', 'PG-13');

INSERT INTO movie (title, genre, duration_minutes, language, rating) 
VALUES ('Avatar: The Way of Water', 'Sci-Fi / Action', 192, 'English', 'PG-13');

-- 2. Insert Screens
INSERT INTO screen (screen_name, total_seats) VALUES ('Audi 1 (IMAX)', 20);
INSERT INTO screen (screen_name, total_seats) VALUES ('Audi 2 (Dolby Atmos)', 20);

-- 3. Insert Seats for Screen 1 (Audi 1) - Rows A, B, C, D (5 seats each)
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'A1', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'A2', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'A3', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'A4', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'A5', 'STANDARD');

INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'B1', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'B2', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'B3', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'B4', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'B5', 'STANDARD');

INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'C1', 'PREMIUM');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'C2', 'PREMIUM');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'C3', 'PREMIUM');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'C4', 'PREMIUM');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'C5', 'PREMIUM');

INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'D1', 'VIP');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'D2', 'VIP');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'D3', 'VIP');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'D4', 'VIP');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (1, 'D5', 'VIP');

-- Insert Seats for Screen 2 (Audi 2)
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'A1', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'A2', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'A3', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'A4', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'A5', 'STANDARD');

INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'B1', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'B2', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'B3', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'B4', 'STANDARD');
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES (2, 'B5', 'STANDARD');

-- 4. Insert Shows
-- Shows for Inception (movie_id = 1)
INSERT INTO show (movie_id, screen_id, show_date, show_time, ticket_price) 
VALUES (1, 1, TRUNC(SYSDATE), '10:00 AM', 12.50);

INSERT INTO show (movie_id, screen_id, show_date, show_time, ticket_price) 
VALUES (1, 1, TRUNC(SYSDATE), '02:30 PM', 15.00);

-- Shows for Interstellar (movie_id = 2)
INSERT INTO show (movie_id, screen_id, show_date, show_time, ticket_price) 
VALUES (2, 2, TRUNC(SYSDATE), '06:00 PM', 14.00);

-- Shows for The Dark Knight (movie_id = 3)
INSERT INTO show (movie_id, screen_id, show_date, show_time, ticket_price) 
VALUES (3, 1, TRUNC(SYSDATE + 1), '07:00 PM', 15.00);

COMMIT;
