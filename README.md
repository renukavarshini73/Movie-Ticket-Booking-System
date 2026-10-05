# Movie Ticket Booking System

A standalone desktop application developed in **Java Swing/AWT**, **JDBC**, and **Oracle Database running in Docker**, built with **Maven** for dependency management.

Designed for college mini-project demonstration, featuring 3-tier layered DAO architecture, 3NF database normalization, and strict **database-level duplicate seat booking prevention**.

---

## Features

- **Movie Management**: View available movies with language, genre, duration, and certificate rating.
- **Show Scheduling**: Retrieve showtimes, screen assignments, and ticket pricing.
- **Interactive Seat Selection Grid**: Dynamic `JButton` visual seat map with real-time state color coding:
  - 🟢 **Green**: Available
  - 🟡 **Yellow**: Selected
  - 🔴 **Red (Disabled)**: Already Booked
- **Real-Time Pricing**: Automatic calculation of ticket count and total cost.
- **Customer Contact Validation**: Input validation for name, phone, and email.
- **Atomic JDBC Transactions**: Complete `setAutoCommit(false)`, `commit()`, and `rollback()` handling across Customer, Booking, and Booking-Seat records.
- **Strict Concurrency Protection**: Database-level constraint (`PRIMARY KEY (show_id, seat_id)`) prevents duplicate seat allocations.
- **Booking Receipts**: Formatted summary popup upon confirmation.
- **Booking History & Reports**: Search bookings by ID/Phone and view management analytics (Total Bookings, Revenue, Tickets Sold).

---

## Technology Stack

- **Language**: Java (JDK 17)
- **GUI Framework**: Java Swing / AWT
- **Database**: Oracle Database 21c XE (Running inside Docker)
- **Database Connectivity**: JDBC (`com.oracle.database.jdbc:ojdbc8`)
- **Build & Dependency Tool**: Apache Maven

---

## Architecture Overview

```text
       +------------------------------------+
       |       Java Swing / AWT GUI         |
       |  (MainFrame, Panels, Dialogs)      |
       +-----------------+------------------+
                         |
                         v
       +-----------------+------------------+
       |          Service Layer             |
       | (MovieService, BookingService, etc)|
       +-----------------+------------------+
                         |
                         v
       +-----------------+------------------+
       |            DAO Layer               |
       |   (MovieDAO, BookingDAO, etc)      |
       +-----------------+------------------+
                         |
                         v
       +-----------------+------------------+
       |         JDBC Connection            |
       |   (PreparedStatement, Rollback)    |
       +-----------------+------------------+
                         |
                         v
       +-----------------+------------------+
       |    Oracle Database 21c (Docker)    |
       |  (3NF Normalized, PK/FK/Unique)    |
       +------------------------------------+
```

---

## Database Setup (Oracle in Docker)

1. Start your Oracle container:
   ```bash
   docker start oracle-db
   ```

2. Execute database DDL and sample data scripts:
   ```bash
   docker cp database/schema.sql oracle-db:/tmp/schema.sql
   docker cp database/sample_data.sql oracle-db:/tmp/sample_data.sql

   docker exec -i oracle-db sqlplus -s movieuser/MoviePass123@localhost:1521/XE @/tmp/schema.sql
   docker exec -i oracle-db sqlplus -s movieuser/MoviePass123@localhost:1521/XE @/tmp/sample_data.sql
   ```

---

## Configuration

Database credentials are loaded from `src/main/resources/config.properties` (or environment variables `DB_URL`, `DB_USER`, `DB_PASSWORD`):

```properties
db.url=jdbc:oracle:thin:@localhost:1521/XE
db.user=movieuser
db.password=MoviePass123
```

---

## How to Build & Run

### 1. Compile Java Source Code
```bash
/opt/homebrew/opt/openjdk/bin/javac -cp "target/classes:lib/ojdbc8.jar" -d target/classes \
src/main/java/com/movieticket/model/*.java \
src/main/java/com/movieticket/util/*.java \
src/main/java/com/movieticket/dao/*.java \
src/main/java/com/movieticket/service/*.java \
src/main/java/com/movieticket/gui/*.java \
src/main/java/com/movieticket/Main.java
```

### 2. Launch Desktop Application
```bash
/opt/homebrew/opt/openjdk/bin/java -cp "target/classes:lib/ojdbc8.jar:src/main/resources" com.movieticket.Main
```

---

## GitHub Setup Commands

```bash
git init
git add .
git commit -m "Initial commit: Movie Ticket Booking System (Swing + JDBC + Oracle)"
git branch -M main
git remote add origin <your-github-repo-url>
git push -u origin main
```
