# URL Shortener Service

## Project Overview

This is a comprehensive URL Shortener Service built using Java and Spring Boot. The application allows users to shorten long URLs into compact, shareable links, track click statistics, and provides both a REST API and a web-based user interface. It's designed with scalability, reliability, and ease of deployment in mind, featuring containerization with Docker and Docker Compose for seamless local and production setups.

## Key Features

- **URL Shortening**: Convert long URLs into short, unique codes using base-62 encoding
- **Redirection**: Automatic redirection from short URLs to original URLs with click tracking
- **Statistics Tracking**: View creation date and click count for each shortened URL
- **REST API**: Full RESTful API for programmatic access
- **Web Interface**: User-friendly web UI built with Thymeleaf for manual URL shortening and stats checking
- **Data Persistence**: Robust database integration with PostgreSQL for production and H2 for development
- **Input Validation**: Comprehensive validation using Bean Validation annotations
- **Error Handling**: Global exception handling with custom error responses
- **Containerization**: Docker and Docker Compose setup for easy deployment
- **Health Checks**: Built-in health check endpoint for monitoring

## Technologies Used

- **Backend Framework**: Spring Boot 4.0.1
- **Language**: Java 21
- **Database**: PostgreSQL (production), H2 (development/testing)
- **ORM**: Spring Data JPA with Hibernate
- **Web Framework**: Spring MVC
- **Template Engine**: Thymeleaf
- **Validation**: Hibernate Validator
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Testing**: Spring Boot Test, JUnit
- **Utilities**: Lombok for boilerplate reduction

## Architecture

The application follows a layered architecture pattern:

### 1. Presentation Layer
- **Controllers**: Handle HTTP requests and responses
  - `UrlController`: REST API endpoints
  - `PageController`: Web interface endpoints
  - `HealthCheckController`: Application health monitoring

### 2. Service Layer
- **UrlShortenerService**: Core business logic
  - URL shortening with base-62 encoding
  - Click tracking and statistics retrieval
  - Transaction management

### 3. Data Access Layer
- **UrlMappingRepository**: JPA repository interface
  - CRUD operations for URL mappings
  - Custom query methods

### 4. Data Model
- **UrlMapping Entity**: Represents URL mappings in the database
  - Fields: id, originalUrl, shortCode, creationDate, clickCount

### 5. DTOs (Data Transfer Objects)
- **ShortenUrlRequest**: Input validation for URL shortening
- **ShortenUrlResponse**: Response for successful shortening
- **UrlStatsResponse**: Statistics data structure

### 6. Exception Handling
- **GlobalExceptionHandler**: Centralized error handling
- **UrlNotFoundException**: Custom exception for missing URLs

## API Endpoints

### REST API

1. **POST /api/v1/url/shorten**
   - **Purpose**: Create a short URL
   - **Request Body**: `{"url": "https://example.com/long-url"}`
   - **Response**: `{"shortUrl": "http://localhost:8080/abc123"}`
   - **Status**: 201 Created

2. **GET /{shortCode}**
   - **Purpose**: Redirect to original URL and increment click count
   - **Response**: 302 Found with Location header
   - **Example**: `GET /abc123` redirects to original URL

3. **GET /api/v1/url/stats/{shortCode}**
   - **Purpose**: Retrieve statistics for a short URL
   - **Response**: JSON with original URL, short URL, creation date, and click count
   - **Status**: 200 OK or 404 Not Found

### Web Interface

1. **GET /**: Main page with forms for shortening and checking stats
2. **POST /shorten-web**: Handle URL shortening form submission
3. **POST /check-stats**: Handle statistics check form submission

### Health Check

1. **GET /health**: Application health status

## Database Schema

The application uses a single table `url_mapping`:

```sql
CREATE TABLE url_mapping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_url TEXT NOT NULL,
    short_code VARCHAR(255) UNIQUE,
    creation_date TIMESTAMP,
    click_count BIGINT DEFAULT 0
);
```

- **id**: Auto-generated primary key
- **original_url**: The full original URL (LOB for long URLs)
- **short_code**: Unique base-62 encoded short code
- **creation_date**: When the mapping was created
- **click_count**: Number of times the short URL has been accessed

## Setup and Installation

### Prerequisites
- Java 21
- Maven 3.8+
- Docker and Docker Compose (for containerized deployment)

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd url-shortener-service
   ```

2. **Build the application**
   ```bash
   mvn clean install
   ```

3. **Run with H2 database**
   ```bash
   mvn spring-boot:run
   ```
   - Application runs on http://localhost:8080
   - H2 console available at http://localhost:8080/h2-console

### Docker Deployment

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```
   - PostgreSQL database on port 5433
   - Application on port 8080

2. **Access the application**
   - Web UI: http://localhost:8080
   - API endpoints: http://localhost:8080/api/v1/...

## Usage

### Using the Web Interface

1. Navigate to http://localhost:8080
2. Enter a long URL in the "Enter a long URL to shorten" field
3. Click "Shorten URL"
4. Copy the generated short URL
5. To check statistics, enter the short code in the "Check Link Statistics" section

### Using the REST API

**Shorten a URL:**
```bash
curl -X POST http://localhost:8080/api/v1/url/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://example.com/very/long/url"}'
```

**Get statistics:**
```bash
curl http://localhost:8080/api/v1/url/stats/abc123
```

**Use short URL:**
```bash
curl -L http://localhost:8080/abc123
```

## Configuration

### Application Profiles

- **default**: Uses H2 in-memory database
- **prod**: Uses PostgreSQL (configured via environment variables)

### Environment Variables (for Docker)

- `SPRING_PROFILES_ACTIVE=prod`
- `DB_USERNAME=myuser`
- `DB_PASSWORD=mypassword`

## Testing

The project includes comprehensive tests:

- **Unit Tests**: Service layer testing
- **Integration Tests**: Full application testing with test database
- **API Tests**: REST endpoint testing

Run tests:
```bash
mvn test
```

## Deployment

### Docker Compose Production Setup

The `docker-compose.yml` provides:
- PostgreSQL database with persistent volume
- Spring Boot application container
- Health checks for service dependencies
- Port mappings for external access

### Scaling Considerations

- Database connection pooling (configured via Spring Boot defaults)
- Stateless application design for horizontal scaling
- External database for data persistence

## Security Considerations

- Input validation prevents malicious URLs
- Unique constraints prevent duplicate short codes
- No authentication implemented (could be added for production use)
- HTTPS recommended for production deployment

## Performance Optimizations

- Base-62 encoding for compact short codes
- Database indexing on short_code column
- Transactional operations for data consistency
- Lazy loading and efficient queries

## Future Enhancements

1. **Authentication & Authorization**: User accounts and API keys
2. **Rate Limiting**: Prevent abuse of the shortening service
3. **Analytics Dashboard**: Detailed click analytics and trends
4. **Custom Short Codes**: Allow users to specify custom short codes
5. **Expiration**: Automatic cleanup of old/unused URLs
6. **Caching**: Redis integration for faster lookups
7. **Monitoring**: Integration with tools like Prometheus/Grafana
8. **API Versioning**: Support multiple API versions
9. **Batch Operations**: Shorten multiple URLs at once
10. **QR Code Generation**: Generate QR codes for short URLs

## Interview Talking Points

When explaining this project in an interview, you can cover:

1. **Architecture Decisions**: Why Spring Boot? Why JPA? Why Docker?
2. **Design Patterns**: Repository pattern, Service layer, DTOs
3. **Database Design**: Choice of PostgreSQL, schema design
4. **Encoding Algorithm**: Base-62 encoding explanation
5. **Transaction Management**: Why @Transactional on service methods
6. **Error Handling**: Global exception handler benefits
7. **Validation**: Bean validation vs manual validation
8. **Containerization**: Multi-stage Docker builds, Docker Compose benefits
9. **Testing Strategy**: Unit vs integration tests
10. **Scalability**: How the app can handle increased load
11. **Security**: Input validation, potential vulnerabilities
12. **Performance**: Optimizations made and potential improvements

This project demonstrates a solid understanding of modern Java development practices, microservices architecture, and DevOps principles.</content>
<parameter name="filePath">C:\Users\saura\Java + SpringBoot Projects\URL-Shortener-Service\README.md
