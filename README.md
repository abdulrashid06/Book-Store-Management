### Online Bookstore Management System
## Overview
The Online Bookstore Management System is a comprehensive web application designed to manage books, orders, and user accounts. It provides an easy-to-use interface for customers and administrators to interact with the bookstore. The system is built using Spring Boot, MySQL, and includes security features powered by Spring Security. It also utilizes Swagger for API documentation and JUnit/Mockito for testing.

## Features
**User Management**
User Registration and Authentication: Users can sign up and log in to the system. Authentication is secured using JWT.
Role-Based Access Control: The system supports two roles: User and Admin. Admins have additional privileges to manage books and orders.
Profile Management: Users can view and update their profile information.
</br>

**Book Management**
Add/Update/Delete Books: Admins can manage the inventory of books in the store.
View Books: Users can browse through the catalog of available books.
</br>

**Order Management**
Place Orders: Users can place orders for books.
Update Order Status: Admins can update the status of an order (e.g., shipped, delivered).
Cancel Orders: Users can cancel orders if they have not yet been delivered.
</br>

**Security**
JWT Authentication: Secure the APIs with JWT tokens.
Role-Based Authorization: Protect resources based on user roles (User, Admin).

</br>
**Testing**
Unit Tests: Comprehensive unit tests using JUnit and Mockito.
API Documentation: Interactive API documentation using Swagger.
</br>

**Technology Stack**
Backend: Spring Boot, Java
Database: MySQL
Security: Spring Security with JWT
API Documentation: Swagger
Testing: JUnit, Mockito


## Getting Started

**Prerequisites**
Java 11 or higher
Maven 3.6.3 or higher
MySQL 8.0 or higher

</br>

## Installation
**Clone the repository:**

```
git clone https://github.com/yourusername/online-bookstore-management-system.git
cd online-bookstore-management-system
```


**Set up the database:**
Create a MySQL database named bookstore_db.
Update the application.properties file with your MySQL username and password.


**Access the application:**
The application will be available at http://localhost:8080.
Swagger UI will be available at http://localhost:8080/swagger-ui.html.
</br>

## API Endpoints
```
User Management
Register a new user

POST /api/users/register
User Login

POST /api/auth/login
Book Management
Add a new book

POST /api/books
Update a book

PUT /api/books/{bookId}
Get all books

GET /api/books
Delete a book

DELETE /api/books/{bookId}
Order Management
Place a new order

POST /api/orders
Update order status

PUT /api/orders/{orderId}/status
Cancel an order

DELETE /api/orders/{orderId}
Get all orders

GET /api/orders
Get order by ID

GET /api/orders/{orderId}
 ```
</br>

**API Documentation**
Swagger is used for API documentation. You can interact with the API using the Swagger UI, available at http://localhost:8080/swagger-ui.html.
</br>

**Contributing**
Contributions are welcome! Please fork this repository, create a new branch, and submit a pull request.

</br>
**License**
This project is licensed under the MIT License - see the LICENSE file for details.
</br>

**Contact**
For any inquiries or support, please contact:

Name: Abdul Rashid
Email: abdulrashid987655@gmail.com
