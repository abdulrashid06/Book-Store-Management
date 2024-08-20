# Online Bookstore Management System

## Project Overview

The **Online Bookstore Management System** is a web-based application designed to manage the complete lifecycle of a bookstore. It provides functionalities for users to browse books, place orders, and manage their shopping cart, while allowing administrators to manage books, orders, and users. The system integrates JWT-based authentication for secure user management and role-based access control for different functionalities.

## Features

- **User Features:**
  - User registration and authentication
  - Browse and search for books
  - View book details
  - Add books to the shopping cart
  - Place orders
  - View order history and details

- **Admin Features:**
  - Manage book inventory (add, update, delete books)
  - Manage user accounts (view, update, delete users)
  - View and manage all orders
  - Update order status (shipped, delivered, canceled)

- **Common Features:**
  - JWT-based authentication and authorization
  - Role-based access control
  - Responsive web design with HTML, CSS, and JavaScript
  - API documentation with Swagger

## Technology Stack

- **Backend:**
  - Spring Boot
  - Spring Security
  - JPA (Hibernate)
  - MySQL
  - JWT (JSON Web Token)

- **Frontend:**
  - HTML
  - CSS
  - JavaScript

- **Testing:**
  - JUnit
  - Mockito

- **API Documentation:**
  - Swagger

## Setup and Installation

### Prerequisites

- Java 11 or later
- Maven
- MySQL
- Node.js (for frontend development)

### Backend Setup

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/your-repo/online-bookstore-management-system.git
    cd online-bookstore-management-system
    ```

2. **Configure MySQL Database:**
   - Create a new database named `bookstore`.
   - Update `src/main/resources/application.properties` with your MySQL credentials.

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
    spring.datasource.username=root
    spring.datasource.password=password
    ```

3. **Build and Run the Backend:**

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

### Frontend Setup

1. **Navigate to Frontend Directory:**

    ```bash
    cd frontend
    ```

2. **Install Dependencies:**

    ```bash
    npm install
    ```

3. **Run the Frontend:**

    ```bash
    npm start
    ```

### API Endpoints

- **User Authentication:**
  - `POST /api/auth/login`: Authenticate user and obtain JWT token.
  - `POST /api/auth/register`: Register a new user.

- **Book Management:**
  - `GET /api/books`: Retrieve a list of books.
  - `GET /api/books/{id}`: Retrieve book details by ID.
  - `POST /api/books`: Add a new book (Admin only).
  - `PUT /api/books/{id}`: Update book details (Admin only).
  - `DELETE /api/books/{id}`: Delete a book (Admin only).

- **Order Management:**
  - `POST /api/orders`: Place a new order.
  - `GET /api/orders/{id}`: Retrieve order details by ID.
  - `GET /api/orders`: Retrieve all orders (Admin) or user-specific orders.
  - `PUT /api/orders/{id}/status`: Update order status (Admin only).
  - `DELETE /api/orders/{id}`: Delete an order (Admin only).

## Testing

- **Backend Tests:**
  - Run unit tests using JUnit and Mockito:

    ```bash
    mvn test
    ```

## Project Structure

- `src/main/java/com/bookstore/` - Contains the main application code.
- `src/main/resources/` - Configuration files and application properties.
- `src/main/resources/static/` - Static resources like HTML, CSS, and JS files.
- `src/test/java/com/bookstore/` - Unit and integration tests.

## Contribution

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Spring Boot for rapid development.
- MySQL for database management.
- Swagger for API documentation.
- JUnit and Mockito for testing.

For any questions or issues, please open an issue on the GitHub repository or contact the project maintainers.
