# Project Overview

Built upon `README.md` documentation and validated against the current codebase on 2026-03-13.

## Summary

`note-app` is a server-rendered Spring Boot web application for publishing short notes with optional attachments. Anonymous users can read the public feed and sign up. Authenticated users can create, edit, and delete their own notes, manage profile/password, and download note attachments. A light/dark theme toggle is implemented client-side and persisted in `localStorage`.

## What Exists Today

- Monolithic Spring Boot 4.0.1 application with MVC + Thymeleaf.
- Feature modules under `src/main/java/gt/app/modules`:
  - `note`: note CRUD and mapping
  - `user`: signup, profile, password, authorities, auth helpers
  - `file`: attachment storage and download support
  - `email`: signup notification email
- Web adapters under `src/main/java/gt/app/web`:
  - `mvc`: HTML pages and form flows
  - `rest`: small authenticated API surface
- Persistence layer built with Spring Data JPA + Hibernate.
- Local/dev infrastructure with H2, MailHog, Docker Compose, WRO4J asset bundling, and native-image hints.

## Main User Flows

### Anonymous

- Visit `/` to browse the public note feed.
- Visit `/signup` to create an account.
- Download attachments through `/download/file/{uuid}`.

### Authenticated User

- Sign in through Spring Security form login at `/auth/login`.
- Visit `/note` to see only their own notes.
- Post notes from the landing page or user page.
- Edit/delete owned notes through `/note/edit/{id}` and `/note/delete/{id}`.
- Manage account details at `/profile` and `/password`.

### Admin

- Visit `/admin` when holding `ROLE_ADMIN`.

## Folder Structure

```text
src/main/java/gt/app
├── Application.java             Spring Boot entry point + native runtime hints
├── DataCreator.java             Dev/test/docker seed data
├── config/                      Properties, auditing, security, constants
├── domain/                      JPA entities and base entities
├── exception/                   Domain/runtime exceptions
├── modules/                     Feature and infrastructure services
│   ├── email/
│   ├── file/
│   ├── note/
│   └── user/
└── web/
    ├── mvc/                     Thymeleaf controllers
    └── rest/                    JSON endpoints

src/main/resources
├── application*.yml            Shared + profile config
├── static/                     CSS, JS, images
├── templates/                  Thymeleaf views and fragments
└── wro*.{xml,properties}       Asset pipeline config

src/test
├── java/                       JUnit, MockMvc, ArchUnit, Selenium/Selenide
├── groovy/                     Spock examples/specs
└── resources/                  Test config and fixtures
```

## Newly Confirmed Details Not Well Covered By README

- The project now runs on Spring Boot `4.0.1`, not Spring Boot 3.
- `spring.threads.virtual.enabled=true` is active in the main configuration.
- The theme toggle is implemented in shared HTML/JS and covered by automated tests.
- There is a minimal REST API under `/api`, but the app is still primarily server-rendered.
- `DataCreator` seeds users and notes automatically for `dev`, `test`, and `docker` profiles.
- Attachments are stored on the local filesystem under `app-properties.file-storage.upload-folder`, defaulting to `java.io.tmpdir`.

## Gaps Between README And Code

- README still contains historical notes about Spring Boot 3 native testing.
- README documents quality tooling broadly, but not the current friction points in the build:
  - Hibernate enhancement logs type-resolution errors during build.
  - SpotBugs and E2E make test runs heavy.
- README does not explain the actual module boundaries, controller layout, or permission model.

## Suggested README Enhancements

- Add an architecture section with the module map above.
- Add a short “profiles and seeded users” section tied to `DataCreator`.
- Add current CI/build caveats, especially around the heavy test pipeline.
- Add a security section describing public vs authenticated routes.
