# RateMyProtein

RateMyProtein is a community-driven web application for discovering and reviewing protein supplements. Users can compare products, rate individual product characteristics, submit new products for approval, and manage their own reviews. Administrators moderate product submissions, catalogue data, and reviews.

This project was developed as a bachelor project using Java, Spring Boot, Thymeleaf, and PostgreSQL.

## Live Application

[Open the deployed application](https://YOUR-RAILWAY-DOMAIN.up.railway.app)

> Replace the URL above with the public domain generated for the application in Railway.

## Main Features

### Public features

- Browse active protein products
- Search by product name, brand, or flavor
- Filter by brand, flavor, and protein type
- View product details, nutrition information, price, and availability
- View community rating averages
- View top-rated products using Bayesian ranking
- View custom error pages for invalid or unauthorized requests

### Registered-user features

- Register, log in, and log out
- Submit one review per product
- Rate overall quality, taste, texture, mixability, sweetness, and aftertaste
- Mark whether the product would be purchased again
- Add an optional written review
- Edit or delete owned reviews
- Submit new products for administrator approval
- View reviews and product submissions from the profile page

### Administrator features

- View pending product submissions
- Approve or reject submitted products
- Edit product information
- Activate or deactivate catalogue products
- View and remove inappropriate reviews
- Access administrator-only pages protected by role-based authorization

## Technology Stack

### Backend

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Security
- Jakarta Bean Validation
- Maven

### Frontend

- Thymeleaf
- HTML5
- CSS3
- Bootstrap 5
- Google Fonts

### Database and deployment

- PostgreSQL
- Railway
- GitHub-based automatic deployment

## Application Architecture

The application follows a layered Spring Boot architecture:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
PostgreSQL
```

```text
src/main/java/com/ratemyprotein
├── config
├── controller
├── dto
├── entity
├── repository
├── security
└── service

src/main/resources
├── static
│   └── css
├── templates
│   ├── admin
│   ├── auth
│   ├── errors
│   ├── fragments
│   ├── products
│   ├── profile
│   └── reviews
└── application.properties
```

## Main Data Model

The main domain entities are:

- `AppUser`
- `Brand`
- `Flavor`
- `Product`
- `Review`
- `Role`
- `ProteinType`

Important relationships include:

- One brand can have many products.
- One flavor can belong to many products.
- One user can write many reviews.
- One product can have many reviews.
- One user can submit many products.
- A user can submit only one review for the same product.

Product moderation is represented using fields such as:

- `active`
- `submittedBy`
- `submittedAt`
- `approvedAt`

## Security

The application includes:

- BCrypt password hashing
- Form-based authentication
- Role-based authorization with `ROLE_USER` and `ROLE_ADMIN`
- CSRF protection
- Review ownership checks
- Administrator route protection
- Server-side validation
- Environment-variable-based database credentials

Passwords and production database credentials must never be committed to GitHub.

## Local Installation

### Prerequisites

Install:

- Java 21
- PostgreSQL
- Git

The Maven Wrapper is included, so a separate Maven installation is not required.

### 1. Clone the repository

```bash
git clone https://github.com/elenetsotsonava/rate-my-protein.git
cd rate-my-protein
```

### 2. Create the PostgreSQL database

Create a local PostgreSQL database named:

```text
ratemyprotein
```

Example SQL:

```sql
CREATE DATABASE ratemyprotein;
```

### 3. Configure environment variables

In Windows PowerShell:

```powershell
$env:DB_URL='jdbc:postgresql://localhost:5432/ratemyprotein'
$env:DB_USERNAME='postgres'
$env:DB_PASSWORD='YOUR_LOCAL_POSTGRES_PASSWORD'
```

Do not add the real password to `application.properties` or GitHub.

Optional environment variables:

```powershell
$env:DDL_AUTO='update'
$env:SHOW_SQL='false'
$env:THYMELEAF_CACHE='false'
```

### 4. Run the application

```powershell
.\mvnw.cmd spring-boot:run
```

Open:

```text
http://localhost:8080
```

### 5. Build the application

```powershell
.\mvnw.cmd clean package
```

The Spring context test requires a working PostgreSQL connection and the database environment variables shown above.

## Environment Variables

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://localhost:5432/ratemyprotein` |
| `DB_USERNAME` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | Stored securely |
| `PORT` | HTTP port assigned by the hosting platform | `8080` |
| `DDL_AUTO` | Hibernate schema behavior | `update` |
| `SHOW_SQL` | Enables or disables SQL logging | `false` |
| `THYMELEAF_CACHE` | Enables or disables Thymeleaf caching | `true` in production |

## Administrator Setup

Public registration always creates a normal user account.

To create an administrator:

1. Register an account normally.
2. Promote the account from `ROLE_USER` to `ROLE_ADMIN` in the PostgreSQL database.
3. Log out.
4. Log in again so Spring Security loads the updated role.

There is intentionally no public “register as administrator” feature.

## Product Review Rules

- Ratings are required.
- Each rating must be between 1 and 5.
- Written review text is optional.
- One user can review a product only once.
- Users can edit and delete only their own reviews.
- Administrators can remove inappropriate reviews but should not rewrite another user’s opinion.

## Product Approval Workflow

```text
User submits product
        ↓
Product remains pending and hidden
        ↓
Administrator reviews the information
        ↓
Administrator approves or rejects it
        ↓
Approved product becomes publicly visible
```

## Currency

All prices are displayed in United States dollars (`USD`).

The current version uses a single application-wide currency. Automatic currency conversion is outside the current project scope.

## Deployment

The production application is hosted on Railway using two services:

```text
Railway project
├── Spring Boot application service
└── PostgreSQL database service
```

The application service is connected to the GitHub `main` branch. New commits can trigger automatic deployments.

The application service uses Railway references to the PostgreSQL service:

```text
DB_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
DB_USERNAME=${{Postgres.PGUSER}}
DB_PASSWORD=${{Postgres.PGPASSWORD}}
DDL_AUTO=update
SHOW_SQL=false
THYMELEAF_CACHE=true
```

## Testing

Run all configured automated tests with:

```powershell
.\mvnw.cmd test
```

Build and test together with:

```powershell
.\mvnw.cmd clean package
```

The final application was also manually tested for:

- Registration and authentication
- Role-based access
- Search and filtering
- Review creation, validation, editing, and deletion
- Optional review text
- Duplicate-review prevention
- Product submission
- Product approval and rejection
- Product activation and deactivation
- Administrator review moderation
- Custom 403, 404, and 500 pages
- Railway deployment and PostgreSQL connectivity

## Screenshots

Create a folder such as:

```text
docs/screenshots
```

Then add screenshots and reference them here:

```markdown
![Homepage](docs/screenshots/homepage.png)
![Product catalogue](docs/screenshots/products.png)
![Product details](docs/screenshots/product-details.png)
![Review validation](docs/screenshots/review-validation.png)
![User profile](docs/screenshots/profile.png)
![Pending products](docs/screenshots/admin-pending-products.png)
![Admin products](docs/screenshots/admin-products.png)
![Review moderation](docs/screenshots/admin-reviews.png)
```

## Known Limitations

- Only USD is supported.
- Product images are provided through public image URLs.
- Administrator promotion is performed through the database.
- Email verification is not implemented.
- Password recovery is not implemented.
- The current automated test suite is limited.
- The application is not an online shop and does not process purchases or payments.

## Possible Future Improvements

- Multi-currency support
- Internationalization and multiple languages
- Direct image uploads
- Email verification
- Password-reset workflow
- Review reporting and soft deletion
- Pagination
- Product comparison
- More automated unit and integration tests
- CI quality checks
- Advanced recommendation features

## Author

**Elene Tsotsonava**

Bachelor project: **RateMyProtein**

## License

This repository was created for academic purposes. Add a formal open-source license only when the project owner chooses one.
