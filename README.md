# sportradar-sports-calendar

**********************************************************************************************************************************************************************

# 🏆 SportFlow: Multi-Sport Event Management System
![example image](https://github.com/user-attachments/assets/16bbc2e2-dea9-437d-8185-29b899ef58f2)


## 📝 Project Overview
SportFlow is a robust management dashboard and RESTful API built with Spring Boot to orchestrate sports events, teams, and competitions. Whether managing the UEFA Champions League, NBA seasons, or Grand Slam Tennis, SportFlow provides a centralized hub for administrators to schedule matches, manage venues, and track real-time results.

The system follows the MVC (Model-View-Controller) architectural pattern, ensuring a clean separation between the event logic, team management, and venue coordination. It is designed to be highly scalable, using a modular approach to handle the diverse rules and scheduling needs of different athletic disciplines.

## ⚙️ Core Functionality & CRUD Operations
The system provides full CRUD (Create, Read, Update, Delete) operations for all major classes. To maintain relational integrity and a clean database state, the system enforces a specific Logical Flow for data entry.

## 🏅 Multi-Sport Support
All entities are governed by a centralized SportType system, allowing the application to support unique scoring and rules for:

FOOTBALL | BASKETBALL | TENNIS | ICE_HOCKEY

## 📋 Entity Management & Logical Flow
To maintain database consistency, the application enforces a hierarchical creation order. You cannot create a match without its building blocks:

Independent Entities (Create these first):

Sports: Define the core disciplines.

Teams: Register teams with names, slugs, and country codes.

Venues: Maintain a registry of stadiums, cities, and countries.

Stages: Define tournament phases (e.g., "Group Stage", "Final").

Competitions: These are linked to a specific Sport (e.g., Champions League ⮕ Football).

Events (Matches): The central entity linking a Competition, Venue, Stage, and Two Teams.

Results: Sport-specific data (goals, points, or sets) linked 1-to-1 with an Event.

## 📊 Database Modeling & Design Decisions
![ERD (Entity Relationship Diagram)](https://github.com/user-attachments/assets/b20775e5-6089-4177-a6bb-6933cc33e23b)

## 🏗️ 3rd Normal Form (3NF) Compliance
The database schema is fully normalized to 3NF to eliminate redundancy and ensure data integrity:

No Transitive Dependencies: Non-key attributes are strictly dependent on the primary key. For example, City and Country belong to the Venue table, while Team details are isolated from the Event table.

Atomic Data: Results are separated into specific tables based on the sport to avoid "God Tables" with hundreds of null columns.

## 🔗 Naming Conventions (Task Requirement)
As per the specific project requirements, all Foreign Keys in the database are prefixed with an underscore to clearly distinguish them from standard columns:

Examples: _sport_id, _competition_id, _venue_id, _home_team_id, _event_id.

## 🧬 Inheritance Strategy
We implemented a Joined Inheritance strategy for the Result entity. This allows the system to store shared match data (Winner, Message, Event ID) in a base table, while specialized data is stored in sport-specific tables:

Football: Tracks goals and a collection of cards (Yellow, Red).

Basketball: Tracks total points and a list of points per quarter.

Tennis: Tracks sets won and specific set-by-set scores.

Ice Hockey: Tracks goals and period-by-period scores.

## 🔄 Data Integrity & Backend Logic

#### 🌍Global Exception Handling: Implemented a centralized @ControllerAdvice to intercept and manage exceptions across the entire application. This ensures that the API returns consistent, user-friendly error messages and proper HTTP status codes (e.g., 404 Not Found for missing events or 400 Bad Request for validation failures) instead of raw stack traces.

#### 🚀Pagination & Scaling (Events): Implemented Spring Data Pagination specifically for the Event retrieval process. As the core of the system, Events represent the highest volume of data; by fetching them in manageable chunks (pages) rather than loading the entire dataset, the application prevents server/database overload and ensures high performance. While other administrative entities (Sports, Venues) use standard retrieval due to their smaller scale, the Event system is built to scale to thousands of records seamlessly.

#### 🔍Dynamic Filtering: Added backend support to filter Events by Date and Sport. This allows users to quickly find specific matches (e.g., all "Football" matches on a specific Saturday) without loading unnecessary data.

#### 🛡️Relational Validation: The system prevents "orphaned" data. You cannot delete a Sport if it still has active Competitions.

#### ✅Spring Validation: Uses @Valid and custom constraints to ensure that all inputs meet business logic (e.g., score values cannot be negative) before persisting.

#### 🔄DTO Mapping: Implements MapStruct to transfer data between the API layer and the database, keeping the internal business logic decoupled from the UI.

#### ⚡Efficient Retrieval: SQL queries are optimized (using Join Fetching) to retrieve event data without executing queries inside loops, satisfying the "No SQL in loops" performance requirement.

#### 🧪 Layered Testing: Verified reliability using JUnit 5 and Mockito, covering both Service layer (business logic) and Controller layer (MockMvc integration tests).

## 🛠️ Technologies Used
Language: Java 17

Framework: Spring Boot 4.0.x

Persistence: Spring Data JPA + Hibernate

Database: PostgreSQL (Configurable for MySQL)

Build Tool: Maven

Mapping: MapStruct

Validation: Spring Validation API

Testing: JUnit 5

Boilerplate: Lombok (@Data, @SuperBuilder)

## ⚙️ Setup & Installation
Prerequisites
JDK 17 

Maven 3.x

PostgreSQL instance (Running on port 5432)

### Instructions
Clone the Repository:

Bash
git clone https://github.com/AsadBabayev/sportradar_sports_calendar.git
cd sportradar_sports_calendar
Configure Database:
Update src/main/resources/application.properties with your credentials:

Properties
#### spring.datasource.url=jdbc:postgresql://localhost:5432/sportflow_db
#### spring.datasource.username=your_username
#### spring.datasource.password=your_password
Build and Run:

Bash
mvn clean install
mvn spring-boot:run
Access the App:

Management Dashboard: http://localhost:8080

### 📧 Contact
Developer: Asad Babayev

Email: asad_babayev@outlook.com
