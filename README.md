# Web Quiz Engine

A RESTful web service for creating, managing, and solving quizzes, built as part of the **Hyperskill/JetBrains Academy** Java Backend track.

## Features
* **User Management:** Registration and Authentication using Spring Security and BCrypt.
* **Quiz Engine:** Create quizzes with multiple options and multiple correct answers.
* **Persistence:** All quizzes and user data are stored in an H2 Database.
* **Pagination:** Efficiently browse quizzes and completion history using Spring Data JPA Paging.
* **Security:** Role-based access (only authors can delete their own quizzes).

## Technologies Used
* **Java 17**
* **Spring Boot 3.x** (Web, Security, Data JPA, Actuator)
* **H2 Database**
* **Jakarta Validation**

## API Endpoints
* `POST /api/register` - Register a new user.
* `GET /api/quizzes` - Get all quizzes (paginated).
* `POST /api/quizzes/{id}/solve` - Solve a quiz.
* `DELETE /api/quizzes/{id}` - Delete a quiz (Author only).
* `GET /api/quizzes/completed` - Get user's completion history (paginated).
