# AI Agent Constitution - note-app

> Generated from README and code analysis on 2026-03-13.

## Project Context

### Purpose

`note-app` is a server-rendered Spring Boot application for publishing short notes with optional attachments. Anonymous users can browse the public feed and sign up. Authenticated users can manage their own notes, profile, and password.

### Current Status

- Phase: Active development / sample app with production-style patterns
- Version: 4.0.1
- Team Size: Unknown
- Active Development: Yes, recent changes include theme toggle support and brownfield documentation

### Project Health

- Test Coverage: Unknown percentage, but broad test types exist
- CI/CD: Basic
- Documentation: Adequate after `.onboard/` generation; README still has stale sections
- Technical Debt: Medium

## Architecture Quick Reference

### Stack Overview

- Languages: Java, Groovy, HTML, CSS, JavaScript
- Frameworks: Spring Boot, Spring MVC, Spring Security, Spring Data JPA, Thymeleaf
- Database: H2 for dev/test, MySQL-compatible setup for docker/prod-style environments
- Deployment: Maven-built jar, Dockerfile, Docker Compose, GitHub Actions

### Architecture Pattern

Layered monolith with feature modules under `gt.app.modules`.

### Key Directories

```text
src/main/java/gt/app
├── config/        Security, auditing, properties, constants
├── domain/        JPA entities and base classes
├── exception/     Application exceptions
├── modules/       Business logic by feature/infrastructure area
└── web/           MVC and REST adapters

src/main/resources
├── templates/     Thymeleaf pages/fragments
├── static/        CSS, JS, images
└── application*.yml

src/test
├── java/          JUnit, MockMvc, ArchUnit, Selenide tests
└── groovy/        Spock specs
```

## Coding Principles

### General Guidelines

1. Code Style
   - Follow existing Spring + Lombok + MapStruct style.
   - Prefer constructor injection and final fields.
   - Keep naming aligned with current package/module conventions.

2. Code Organization
   - Keep web concerns in `gt.app.web`.
   - Keep business logic in `gt.app.modules`.
   - Keep JPA entities in `gt.app.domain`.
   - Avoid cross-controller coupling.

3. Error Handling
   - Throw specific application exceptions, not generic ones.
   - Log operational failures at service/infrastructure boundaries.
   - For form flows, prefer validation and user-facing error rendering over silent redirects.

4. Testing Requirements
   - Add or update tests in the closest layer you touch:
     - controller changes: MockMvc/integration tests
     - service/query changes: unit/integration tests
     - user-flow changes: e2e tests when behavior spans pages
   - Preserve ArchUnit rule compliance.

5. Documentation Standards
   - Update `README.md` or `.onboard/` docs when behavior, setup, or architecture changes materially.
   - Document any security-sensitive behavior changes explicitly.

### Patterns To Follow

- Keep controllers thin and delegate to services.
- Use DTOs/mappers for web-facing note and user flows.
- Use explicit authorization checks for ownership-sensitive operations.
- Reuse the existing feature packages instead of creating scattered utility packages.

### Anti-Patterns To Avoid

- Adding business logic directly to controllers.
- Bypassing `@PreAuthorize` or the permission evaluator for note ownership flows.
- Introducing new dependencies or build plugins without a clear need.
- Expanding the REST API casually in ways that conflict with the server-rendered architecture.

## Dependencies & External Services

### Adding New Dependencies

- Prefer existing Spring Boot starters and current repo conventions.
- Keep the Maven build reproducible through `./mvnw`.
- Consider build-time cost: this repo already runs many quality plugins.

### External Services

- Mail: SMTP via Spring Mail, local MailHog in dev
- Storage: local filesystem under `app-properties.file-storage.upload-folder`
- OpenAPI/Swagger UI is enabled

## Git & Development Workflow

### Branch Strategy

Not documented in-repo. Assume a simple branch + PR workflow unless the user says otherwise.

### Commit Conventions

Not formally documented in-repo. Keep messages short and descriptive.

### PR Guidelines

- Prefer small, reviewable changes.
- Call out test/build impact when changing Maven plugins, security, persistence, or file handling.
- Mention README/doc updates when behavior changes.

## Common Tasks

### Running The Project

```bash
./mvnw compile spring-boot:run
```

### Running Tests

```bash
./mvnw clean verify
./mvnw compiler:testCompile resources:testResources surefire:test
./mvnw compiler:testCompile resources:testResources failsafe:integration-test
```

### Building For Production

```bash
./mvnw package
./mvnw spring-boot:build-image
```

## Known Issues & Gotchas

- Default profile is `dev`; seeded users are created automatically in `dev`, `test`, and `docker`.
- README contains some stale historical notes from earlier Spring Boot versions.
- Note pagination currently triggers Hibernate in-memory pagination warnings because of collection fetching.
- Note create/edit validation is incomplete and marked with TODOs.
- Attachment download is public by current design and should be treated as an explicit product/security decision.
- The Maven build is comprehensive but slow; quality plugins and e2e coverage increase feedback time.

## Additional Resources

- `README.md` for setup and commands
- `.onboard/overview.md`
- `.onboard/architecture.md`
- `.onboard/design_suggestions.md`
- `.onboard/guardrail_suggestions.md`

## For AI Agents: Validation Checklist

When making changes, ensure:

- Code stays within the current package boundaries and layering
- Security changes preserve route and ownership rules
- Tests are updated at the appropriate layer
- Documentation is updated when behavior or setup changes
- New build complexity is justified
