# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Korean financial blog built with Spring Boot 3.x. The application provides a platform for sharing financial knowledge including investments, savings, and fintech topics. It features user authentication, blog post management, categories, comments, and admin functionality.

## Technology Stack

- **Backend**: Spring Boot 3.2.0 with Java 17
- **Security**: Spring Security 6 with BCrypt password encoding
- **Database**: PostgreSQL (production), H2 (local development)
- **ORM**: Spring Data JPA with Hibernate
- **Frontend**: Thymeleaf templates with Tailwind CSS (CDN)
- **Utilities**: Lombok, Slugify for URL generation

## Development Commands

### Local Development
```bash
# Run with local profile (uses H2 database)
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Run with default profile (uses PostgreSQL via DATABASE_URL)
mvn spring-boot:run
```

### Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ClassName

# Run with specific profile
mvn test -Dspring.profiles.active=local
```

### Build and Package
```bash
# Clean and compile
mvn clean compile

# Package application
mvn clean package

# Skip tests during packaging
mvn clean package -DskipTests
```

## Database Configuration

### Local Development
- Uses H2 in-memory database
- Accessible via H2 console at `/h2-console`
- Database URL: `jdbc:h2:mem:testdb`
- Schema is created/dropped on startup via `DataInitializer`

### Production
- Uses PostgreSQL via `DATABASE_URL` environment variable
- Schema updates automatically via `spring.jpa.hibernate.ddl-auto=update`

## Architecture Overview

### Entity Layer (`src/main/java/com/financeblog/entity/`)
- **User**: Implements `UserDetails` for Spring Security integration
- **Post**: Main content entity with slug-based URLs, view tracking, and publish status
- **Category**: Organizes posts by financial topics (투자, 저축, 핀테크)
- **Comment**: Nested comments on posts
- **Tag**: Many-to-many relationship with posts

### Service Layer (`src/main/java/com/financeblog/service/`)
- **UserService**: Implements `UserDetailsService`, handles user registration and authentication
- **PostService**: Post CRUD operations, search, and view count management
- **CategoryService**: Category management with post count tracking
- **CommentService**: Comment operations with spam protection

### Security Configuration
- Role-based access: `ADMIN` for `/admin/**`, `USER` for authenticated features
- Public access: home page, post viewing, user registration/login
- CSRF protection disabled for `/api/**` endpoints
- Custom login page at `/login`

### URL Structure
- Posts use slug-based URLs: `/posts/{slug}`
- Categories: `/category/{slug}`
- Admin panel: `/admin/**`
- API endpoints: `/api/**`

## Key Features

### Content Management
- Rich text post creation with summaries
- Featured post highlighting
- Category-based organization
- Tag system for cross-referencing
- View count tracking

### User System
- Role-based access (USER/ADMIN)
- Registration with email validation
- Spring Security integration
- Default admin account (admin/admin123)

### Data Initialization
- `DataInitializer` runs on `local` profile only
- Creates sample content: admin user, categories, and demo posts
- Populates with Korean financial content

## Development Notes

### Profiles
- `local`: H2 database, debug logging, sample data initialization
- `default`: PostgreSQL database, production settings

### Korean Language Support
- Uses Pretendard font for Korean text rendering
- Content and UI text are in Korean
- Database and code comments use Korean where appropriate

### Deployment
- Configured for Railway.app deployment
- Requires PostgreSQL database service
- Uses Nixpacks build system
- Java 17 runtime specified in `system.properties`