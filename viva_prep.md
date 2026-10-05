# Viva Voce Preparation Guide — Movie Ticket Booking System

This document contains key questions and faculty-ready answers covering Java Swing, JDBC, Database Normalization, Transactions, and System Architecture.

---

## 1. Java & Swing / AWT

### Q1: Why did you use Java Swing/AWT instead of JavaFX or Web?
**Answer**: Java Swing provides a lightweight, pure Java GUI toolkit built directly into standard JDKs. It uses lightweight components drawn directly by Java rather than heavy native OS widgets, enabling cross-platform desktop UI without external framework dependencies.

### Q2: What layout managers did you use and why?
**Answer**:
- `CardLayout`: Used in `MainFrame` to switch seamlessly between different screens (Movie Selection, Showtimes, Seat Selection, Customer Form, History) within a single window.
- `BorderLayout`: Used as the main structural container (Header on `NORTH`, Grid/Table on `CENTER`, Action Buttons on `SOUTH`).
- `GridLayout`: Used in `SeatSelectionPanel` to render a 5-column grid of seat `JButton`s.

### Q3: What is the Event Dispatch Thread (EDT) in Swing?
**Answer**: Swing components are not thread-safe. All GUI updates, event handling, and repaint operations must occur on a single dedicated background thread called the **Event Dispatch Thread (EDT)**. We use `SwingUtilities.invokeLater()` in `Main.java` to start the application safely on the EDT.

---

## 2. Database Design & 3NF Normalization

### Q4: Explain the step-by-step normalization process for your database.
**Answer**:
- **UNF**: Unstructured receipt table containing multi-valued seats (`A1, A2, A3`) and redundant customer/movie details.
- **1NF**: Atomic cell values. Broken multi-valued seats into separate atomic rows.
- **2NF**: Removed partial dependencies. Split entity attributes into `MOVIE`, `SCREEN`, `SEAT`, and `CUSTOMER` tables so all non-key attributes depend on whole primary keys.
- **3NF**: Removed transitive dependencies. Separated screening schedules into `SHOW` and ticket linkages into `BOOKING_SEAT`.

### Q5: How is duplicate seat booking prevented at the database level?
**Answer**: In the `BOOKING_SEAT` table, we defined a composite **Primary Key** on `(show_id, seat_id)`. When two concurrent transactions attempt to insert the same `(show_id, seat_id)` tuple, Oracle Database enforces row-level locking and raises an `ORA-00001: unique constraint violated` exception on the second insert. The JDBC transaction catches this exception and executes `connection.rollback()`.

### Q6: Why does `BOOKING_SEAT` have a composite Foreign Key `(booking_id, show_id)`?
**Answer**: Since `booking_id \rightarrow show_id` holds in the parent `BOOKING` table, adding `(booking_id, show_id)` as a Composite Foreign Key in `BOOKING_SEAT` guarantees that `BOOKING_SEAT.show_id` **must strictly equal** `BOOKING.show_id`, maintaining 3NF compliance while enforcing database-level seat uniqueness.

---

## 3. JDBC & Transaction Management

### Q7: What is a JDBC Transaction and why is it mandatory for booking?
**Answer**: A transaction is a logical unit of work that must satisfy the ACID properties (Atomicity, Consistency, Isolation, Durability). Booking a ticket involves inserting a customer, creating a booking header, and reserving individual seats across multiple tables. By setting `connection.setAutoCommit(false)`, all inserts succeed or fail together. If any insert fails, `connection.rollback()` reverts the database to its previous valid state.

### Q8: Why use `PreparedStatement` instead of `Statement`?
**Answer**:
1. **Security**: `PreparedStatement` uses parameterized input (`?`), completely preventing **SQL Injection** attacks.
2. **Performance**: Pre-compiles SQL execution plans inside Oracle DB for repeated query execution.

---

## 4. Architecture & Security

### Q9: Explain your 3-Tier Layered Architecture.
**Answer**:
- **Presentation Layer (Swing GUI)**: Handles UI interaction and events (`MoviePanel`, `SeatSelectionPanel`, etc.).
- **Service Layer**: Implements business rules, validation (`ValidationUtil`), and JDBC transaction management (`BookingService`).
- **Data Access Object (DAO) Layer**: Performs isolated SQL CRUD operations using `PreparedStatement` (`MovieDAO`, `BookingDAO`, etc.).

### Q10: How are database credentials secured?
**Answer**: Credentials are not hardcoded into Java source code. They are dynamically loaded from `src/main/resources/config.properties` or environment variables (`DB_URL`, `DB_USER`, `DB_PASSWORD`), and `config.properties` is excluded from Git via `.gitignore`.
