Library Management System

This project is a comprehensive library management system built using Java and Spring Boot. It includes various features such as book rental and purchase, user authentication, real-time notifications via WebSockets, payment integration with Stripe, and more.
Features

    User Authentication: Registration, login, and JWT-based authorization.
    Book Management: Viewing, renting, and purchasing books.
    Real-time Notifications: WebSocket-based notifications for book transactions.
    Payment Integration: Stripe integration for handling book purchases.
    Web Interface: Interactive user interface built with Thymeleaf.

Technologies Used

    Java
    Spring Boot
    Spring Security
    Spring WebSocket
    Spring MVC
    Thymeleaf
    Stripe API
    JWT (JSON Web Tokens)
    Maven

Getting Started
Prerequisites

    Java 11 or higher
    Maven
    Stripe API Key

Installation

    Clone the repository:

    sh
 
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system

Set up the database:
Configure your database settings in src/main/resources/application.properties.

Configure Stripe:
Add your Stripe API key to src/main/resources/application.properties:

properties

stripe.api.key=your_stripe_api_key

 Build the project:

    mvn clean install

Run the application:

    mvn spring-boot:run

Usage

    Access the web application at http://localhost:8080.
    Register a new user or login with an existing account.
    Browse, rent, or purchase books.
    Receive real-time notifications for your transactions.

Project Structure

    Configuration:
        WebSecurityConfiguration: Configures security settings, CORS, and password encoding.
        WebSocketConfig: Sets up WebSocket handlers for real-time notifications.

    Controllers:
        AuthController: Handles login and registration pages.
        BookController: Manages book listing, renting, and purchasing.
        NotificationController: Manages the notification page.
        PaymentController: Handles payment processing with Stripe.
        UserController: Manages user-related operations and JWT authentication.

    Entities:
        Book: Represents a book entity.
        User: Represents a user entity.
        UserBookPurchase: Represents a purchase record of a user.

    Services:
        BookService: Contains business logic related to books.
        UserService: Contains business logic related to users.

    Security:
        JwtTokenProvider: Generates JWT tokens.
        GenerateSecretKey: Utility to generate a secret key for JWT.

WebSocket Notifications

The NotificationWebSocketHandler manages WebSocket connections and sends notifications to connected clients. Notifications are sent when a book is purchased.
Payment Integration

Payments are processed using Stripe. The PaymentController handles the payment process, from displaying the payment page to processing the payment and updating the user's purchased books.
Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.
