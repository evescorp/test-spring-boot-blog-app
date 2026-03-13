# Architecture Analysis

Validated from code on 2026-03-13.

## Stack

- Language: Java 21
- Build: Maven Wrapper (`mvnw`)
- Frameworks:
  - Spring Boot 4.0.1
  - Spring MVC
  - Spring Security
  - Spring Data JPA / Hibernate ORM 7
  - Thymeleaf
- Mapping / codegen:
  - Lombok
  - MapStruct
- Frontend assets:
  - Bootstrap 4.5 via WebJars
  - jQuery
  - vanilla JavaScript
  - WRO4J bundling/minification
- Testing:
  - JUnit 5
  - MockMvc
  - ArchUnit
  - Selenide
  - Spock / Groovy
- Quality plugins:
  - Error Prone
  - Checkstyle
  - Modernizer
  - SpotBugs + FindSecBugs
  - JaCoCo
  - OWASP dependency-check

## Architecture Pattern

Layered monolith with a feature-oriented service layer:

- Web layer handles HTTP concerns and templates.
- Modules encapsulate business logic and repositories.
- Domain layer holds JPA entities and auditing state.
- Security and persistence cross-cut through `config/`.

This is not a strict hexagonal implementation, but it has some useful boundaries:
- controllers stay thin
- repositories are package-private in places
- method-level authorization is used for ownership checks
- ArchUnit rules attempt to preserve conventions

## Request And Data Flow

1. Request enters MVC controller in `gt.app.web.mvc`.
2. Spring Security checks route access using `SecurityConfig`.
3. For note ownership operations, `@PreAuthorize` delegates to `AppPermissionEvaluatorService`.
4. Controller calls a service in `gt.app.modules.*`.
5. Service uses repositories and mappers, persists entities, and returns DTOs/entities.
6. Thymeleaf renders HTML or REST controller returns JSON.

## Security Model

### Public Routes

- `/`
- `/signup/**`
- Swagger/OpenAPI endpoints
- `/webjars/**`, `/static/**`, `/error/**`, `/h2-console/**`

### Protected Routes

- `/admin/**` requires `ROLE_ADMIN`
- `/user/**` requires `ROLE_USER`
- `/api/**` requires authentication
- all other routes default to authenticated unless allowlisted

### Ownership Enforcement

`NoteController` uses `@PreAuthorize("@permEvaluator.hasAccess(...))"` for edit/delete operations. The permission evaluator resolves the current `AppUserDetails` and delegates to `UserAuthorityService`.

## Persistence Model

Core entities:

- `AppUser`
- `Authority`
- `Note`
- `ReceivedFile`
- `LiteUser` as a lean user representation for references/auditing

Key patterns:

- JPA auditing enabled via `@EnableJpaAuditing`
- current auditor resolved from Spring Security context
- `NoteRepository` uses `@EntityGraph` to fetch author and attachments eagerly for read flows
- note attachments are modeled as `@OneToMany` from `Note` to `ReceivedFile`

## Configuration And Profiles

- `application.yml`:
  - virtual threads enabled
  - mail defaults to localhost/MailHog
  - file uploads default to temp dir
- `application-default.yml`:
  - activates `dev` profile by default
- `application-dev.yml`:
  - file-based template/static reload
  - H2 console enabled
  - Docker Compose profile `mailHog`

## Build And Delivery

- `Dockerfile` builds from a packaged jar.
- `docker-compose.yml` provides MailHog and optional MySQL.
- GitHub Actions workflow exists in `.github/workflows/maven.yml`.

## Drift / Issues Detected

- CI workflow is old:
  - uses `actions/checkout@v2`
  - uses `actions/setup-java@v1`
  - runs `mvn`, not `./mvnw`
- README references older Spring Boot 3 native-test behavior, while the build is now Spring Boot 4.0.1.
- Build output shows Hibernate enhancement type-resolution errors during compile/test setup, which should be investigated even if later phases continue.
- `NoteRepository` fetches collections with pagination, causing Hibernate to log `firstResult/maxResults specified with collection fetch; applying in memory`.

## Recommended Architecture Documentation Updates

- Document the actual layer/module split.
- Document security rules and note ownership checks.
- Document file storage and public attachment download behavior.
- Document that the app is server-rendered first, with only a small REST surface.
