-- Movie Ticket Booking System - Queries Reference Script

-- 1. Retrieve all available movies
SELECT movie_id, title, genre, duration_minutes, language, rating 
FROM movie 
ORDER BY title;

-- 2. Retrieve shows for a selected movie
SELECT s.show_id, m.title AS movie_title, sc.screen_name, s.show_date, s.show_time, s.ticket_price
FROM show s
JOIN movie m ON s.movie_id = m.movie_id
JOIN screen sc ON s.screen_id = sc.screen_id
WHERE s.movie_id = ?
ORDER BY s.show_date, s.show_time;

-- 3. Retrieve all seats for a screen
SELECT seat_id, screen_id, seat_number, seat_type
FROM seat
WHERE screen_id = ?
ORDER BY seat_number;

-- 4. Retrieve already booked seat IDs for a specific show (used by Java Swing to disable booked buttons)
SELECT seat_id 
FROM booking_seat 
WHERE show_id = ?;

-- 5. Search customer by phone number (returns customer_id if exists)
SELECT customer_id, name, phone, email 
FROM customer 
WHERE phone = ?;

-- 6. Atomic Booking Insertion Transaction
-- Step 6a: Insert or retrieve Customer
INSERT INTO customer (name, phone, email) VALUES (?, ?, ?);

-- Step 6b: Insert Booking
INSERT INTO booking (customer_id, show_id, total_amount, booking_status) 
VALUES (?, ?, ?, 'CONFIRMED');

-- Step 6c: Insert Reserved Seats into BOOKING_SEAT
-- If (show_id, seat_id) is already booked, Oracle rejects with ORA-00001
INSERT INTO booking_seat (show_id, seat_id, booking_id) 
VALUES (?, ?, ?);

-- 7. Search Booking Details by Booking ID or Phone
SELECT b.booking_id, c.name AS customer_name, c.phone, m.title AS movie_title, 
       sc.screen_name, s.show_date, s.show_time, b.total_amount, b.booking_date, b.booking_status
FROM booking b
JOIN customer c ON b.customer_id = c.customer_id
JOIN show s ON b.show_id = s.show_id
JOIN movie m ON s.movie_id = m.movie_id
JOIN screen sc ON s.screen_id = sc.screen_id
WHERE b.booking_id = ? OR c.phone = ?
ORDER BY b.booking_date DESC;

-- 8. Management Summary Report (Total Bookings, Total Revenue, Tickets Sold)
SELECT 
    COUNT(DISTINCT b.booking_id) AS total_bookings,
    NVL(SUM(b.total_amount), 0) AS total_revenue,
    COUNT(bs.seat_id) AS tickets_sold
FROM booking b
JOIN booking_seat bs ON b.booking_id = bs.booking_id AND b.show_id = bs.show_id
WHERE b.booking_status = 'CONFIRMED';
