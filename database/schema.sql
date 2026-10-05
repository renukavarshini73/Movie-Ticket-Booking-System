-- Movie Ticket Booking System - Database Schema
-- Compatible with Oracle Database 12c / 18c / 19c / 21c / 23c

SET SQLBLANKLINES ON;

-- Drop tables if they exist (clean setup)
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE booking_seat CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE booking CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE customer CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE show CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE seat CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE screen CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE movie CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

-- 1. MOVIE Table
CREATE TABLE movie (
    movie_id         NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title            VARCHAR2(100) NOT NULL,
    genre            VARCHAR2(50)  NOT NULL,
    duration_minutes NUMBER(3)     NOT NULL CHECK (duration_minutes > 0),
    language         VARCHAR2(30)  NOT NULL,
    rating           VARCHAR2(10)  NOT NULL
);

-- 2. SCREEN Table
CREATE TABLE screen (
    screen_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    screen_name      VARCHAR2(50)  NOT NULL UNIQUE,
    total_seats      NUMBER(4)     NOT NULL CHECK (total_seats > 0)
);

-- 3. SEAT Table
CREATE TABLE seat (
    seat_id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    screen_id        NUMBER        NOT NULL,
    seat_number      VARCHAR2(10)  NOT NULL,
    seat_type        VARCHAR2(20)  DEFAULT 'STANDARD' NOT NULL CHECK (seat_type IN ('STANDARD', 'PREMIUM', 'VIP')),
    CONSTRAINT fk_seat_screen FOREIGN KEY (screen_id) REFERENCES screen(screen_id) ON DELETE CASCADE,
    CONSTRAINT uq_screen_seat_num UNIQUE (screen_id, seat_number)
);

-- 4. SHOW Table
CREATE TABLE show (
    show_id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    movie_id         NUMBER        NOT NULL,
    screen_id        NUMBER        NOT NULL,
    show_date        DATE          NOT NULL,
    show_time        VARCHAR2(10)  NOT NULL,
    ticket_price     NUMBER(8, 2)  NOT NULL CHECK (ticket_price >= 0),
    CONSTRAINT fk_show_movie FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON DELETE CASCADE,
    CONSTRAINT fk_show_screen FOREIGN KEY (screen_id) REFERENCES screen(screen_id) ON DELETE CASCADE,
    CONSTRAINT uq_screen_showtime UNIQUE (screen_id, show_date, show_time)
);

-- 5. CUSTOMER Table
CREATE TABLE customer (
    customer_id      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name             VARCHAR2(100) NOT NULL,
    phone            VARCHAR2(15)  NOT NULL UNIQUE,
    email            VARCHAR2(100) NOT NULL UNIQUE
);

-- 6. BOOKING Table
-- Note: Oracle requires referenced parent columns of a composite foreign key to be a unique or candidate key.
-- This constraint does not introduce any new business dependency because booking_id is already the primary key.
CREATE TABLE booking (
    booking_id       NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id      NUMBER        NOT NULL,
    show_id          NUMBER        NOT NULL,
    booking_date     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    total_amount     NUMBER(10, 2) NOT NULL CHECK (total_amount >= 0),
    booking_status   VARCHAR2(20)  DEFAULT 'CONFIRMED' NOT NULL CHECK (booking_status IN ('CONFIRMED', 'CANCELLED')),
    CONSTRAINT fk_booking_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT fk_booking_show FOREIGN KEY (show_id) REFERENCES show(show_id),
    CONSTRAINT uq_booking_show UNIQUE (booking_id, show_id)
);

-- 7. BOOKING_SEAT Table
CREATE TABLE booking_seat (
    show_id          NUMBER        NOT NULL,
    seat_id          NUMBER        NOT NULL,
    booking_id       NUMBER        NOT NULL,
    CONSTRAINT pk_booking_seat PRIMARY KEY (show_id, seat_id),
    CONSTRAINT fk_bs_show FOREIGN KEY (show_id) REFERENCES show(show_id),
    CONSTRAINT fk_bs_seat FOREIGN KEY (seat_id) REFERENCES seat(seat_id),
    CONSTRAINT fk_bs_booking_show FOREIGN KEY (booking_id, show_id) REFERENCES booking(booking_id, show_id) ON DELETE CASCADE
);

-- Performance Indexes for Quick Lookups
CREATE INDEX idx_show_movie ON show(movie_id);
CREATE INDEX idx_booking_customer ON booking(customer_id);
CREATE INDEX idx_booking_show ON booking(show_id);
