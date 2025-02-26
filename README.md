# CoinMarket - Cryptocurrency Trading Platform

Forget clunky interfaces and guesswork – the future of crypto trading is here. CoinMarket isn't just keeping pace with the cryptocurrency revolution, we're driving it. This Spring Boot powered platform redefines what you should expect from a trading experience.  Imagine:  AI-powered insights from Google's Gemini 1.5 Pro guiding your decisions, real-time trades executing in a blink, and a portfolio that practically manages itself.  With CoinMarket, you're not just trading crypto, you're mastering it.  Intrigued? Keep reading to unlock the power within

![login](https://i.imgur.com/5KkHMUZ.gif)

![MAIN-SCREEN](https://i.imgur.com/jg3wpGU.png)

## 🌟 Key Features

![FEATURE](https://i.imgur.com/2PT8Puf.png)

### 🤖 AI Chatbot Integration
- Powered by Google's Gemini 1.5 Pro API
- Natural language processing for crypto-related queries
- Real-time market insights and analysis
- Two chat modes:
  - Detailed coin analysis with structured responses
  - Simple chat for general queries

### 💰 Payment Integration
- Multiple payment gateways supported:
  - PayPal
  - Stripe
  - RazorPay
- Secure transaction processing
- Payment status tracking
- Wallet management

### 📊 Trading Features
- Real-time cryptocurrency data via CoinGecko API
- Buy/Sell orders
- Portfolio tracking
- Watchlist management
- Asset management

### 🔐 Security
- JWT-based authentication
- Two-factor authentication (Email/Mobile)
- Password reset functionality
- Role-based access control

## 📁 Project Structure

### Configuration 
- `AppConfig`: Security and CORS configuration
- `JwtProvider`: JWT token generation and validation
- `PaypalConfiguration`: PayPal API integration setup

### Controllers
- `AuthController`: Authentication and user management
- `ChatbotController`: AI chatbot endpoints
- `CoinController`: Cryptocurrency data endpoints
- `OrderController`: Trading order management
- `PaymentController`: Payment processing
- `WalletController`: Digital wallet operations

### Enums
- `OrderStatus`: Trading order statuses
- `OrderType`: Buy/Sell order types
- `PaymentMethod`: Supported payment methods
- `USER_ROLE`: User authorization roles
- `VerificationType`: 2FA verification types

### Models 
- `Asset`: User's cryptocurrency holdings
- `Coin`: Cryptocurrency details
- `Order`: Trading order information
- `User`: User account details
- `Wallet`: Digital wallet management
- `Watchlist`: User's tracked cryptocurrencies

### PayPal Integration 
- `PaypalService`: PayPal payment processing
- `CreatePaymentRequest`: Payment request handling
- `IPaypalService`: PayPal service interface

### Repositories 
- JPA repositories for all entities
- Custom query methods
- Data persistence layer

### Request/Response 
- DTOs for API requests
- Structured API responses
- Payment processing requests
- Authentication responses

### Services 
- Business logic implementation
- External API integrations
- Data processing and validation
- Transaction management

### Utilities 
- `OtpUtil`: OTP generation
- Helper functions
- Common utilities

## 🔧 Technical Stack

- **Backend**: Spring Boot
- **Security**: Spring Security, JWT
- **Database**: JPA/Hibernate
- **APIs**: 
  - CoinGecko (Cryptocurrency data)
  - Google Gemini 1.5 Pro (AI chatbot)
  - Payment gateways (PayPal, Stripe, RazorPay)
- **Authentication**: JWT + 2FA

## 🚀 Getting Started

1. Clone the repository
2. Configure application.properties with:
   - Database credentials
   - API keys (CoinGecko, Gemini, Payment gateways)
   - JWT secret
3. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

## 📝 API Documentation

The API endpoints are grouped into the following categories:

- `/api/auth/*`: Authentication endpoints
- `/api/coins/*`: Cryptocurrency data
- `/api/orders/*`: Trading operations
- `/api/wallet/*`: Wallet management
- `/api/payment/*`: Payment processing
- `/ai/chat/*`: AI chatbot endpoints

## 🔒 Security Considerations

- JWT token validation
- 2FA implementation
- Payment gateway security
- Data encryption
- Role-based access control

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
