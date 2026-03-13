# Brownfield PRD

## Document Information

**Product Name:** Note App (Existing System)  
**Analysis Date:** 2026-03-13  
**Analyzer:** Codex (codebase-driven analysis)  
**Codebase Version:** `ef9bb22`  
**Overall Confidence:** 87% (High)  
**Status:** Draft

---

## 1. Executive Summary

### Current State Overview

Note App is a server-rendered Spring Boot microblogging application where authenticated users can publish short notes with optional file attachments, manage their own profile and password, and browse a shared public feed. Anonymous visitors can view published notes and create an account, while authenticated users can access a personal notes area and account settings.

The system is structured as a monolithic web application using Spring MVC, Thymeleaf, Spring Security, Spring Data JPA, and an H2/MySQL-compatible persistence layer. The codebase includes seeded demo data, integration tests, E2E coverage for the main flows, and a recently added client-side theme toggle persisted in browser storage.

### Key Findings

**Features Identified:**
- **Core Features:** 6
- **Secondary Features:** 4
- **Legacy/Dev-Only Features:** 2

**Overall Confidence:** 87%
- High Confidence: 8 features
- Medium Confidence: 3 features
- Low Confidence: 1 feature

**Technology Stack:**
- Backend: Java 21, Spring Boot, Spring MVC, Spring Security, Spring Data JPA
- Frontend: Thymeleaf templates, Bootstrap 4, jQuery, vanilla JavaScript
- Database: H2 for local/test, configurable SQL database for docker/prod profiles
- Infrastructure: Docker Compose support, MailHog for local mail, local filesystem storage for attachments

### Top Modernization Priorities

1. **Add robust validation and error handling for note creation/editing**
   - Current code contains explicit `TODO` markers and limited server-side validation for note workflows.
   - Impact: reduce malformed submissions and improve reliability.

2. **Harden file attachment lifecycle and access controls**
   - Downloads are public by design and note deletion does not clearly clean up stored files.
   - Impact: reduce storage leaks and tighten attachment exposure.

3. **Replace dev-oriented defaults with production-ready account and email workflows**
   - Signup sends email, but activation/reset flows are not implemented even though domain fields exist.
   - Impact: safer onboarding and clearer account lifecycle.

4. **Clarify and evolve the API surface**
   - The REST layer is minimal and partly dev-only.
   - Impact: enable future frontend/API separation or integrations.

5. **Introduce schema migration support**
   - README explicitly lists Liquibase/Flyway as future work.
   - Impact: safer environment promotion and operational consistency.

---

## 2. Product Overview (As-Is)

### What It Does

The current application supports these user-visible capabilities:

- Anonymous users can browse a public feed of recent notes.
- Anonymous users can sign up for a new account.
- Authenticated users can sign in through Spring Security form login.
- Authenticated users can post notes with a title, content, and optional attachment.
- Authenticated users can view a private page showing only their own notes.
- Authenticated users can edit or delete only notes they own.
- Authenticated users can update their profile details and password.
- All users can toggle between light and dark theme; the preference is stored in `localStorage`.
- Files attached to notes can be downloaded through a public download URL.

### Current Users (Inferred)

**Primary Persona: Registered Note Author**
- Type: individual internal/demo user or small-group user
- Goal: publish and manage short notes quickly
- Behavior: posts from the web UI, reviews own notes, updates account settings

**Secondary Persona: Public Reader**
- Type: anonymous visitor
- Goal: read recent notes without logging in
- Behavior: lands on home page, browses feed, optionally signs up

**Administrative Persona: Seeded Admin User**
- Type: privileged operator/developer
- Goal: access admin-only page and seeded environment setup
- Behavior: mainly relevant in development/test environments

### Technology Stack

**Backend**
- Java 21
- Spring Boot
- Spring MVC + Thymeleaf
- Spring Security with form login and method-level authorization
- Spring Data JPA / Hibernate

**Frontend**
- Thymeleaf server-rendered templates
- Bootstrap 4.5 via WebJars
- jQuery
- Vanilla JavaScript for theme persistence

**Data and Storage**
- JPA entities: `AppUser`, `Authority`, `Note`, `ReceivedFile`
- Attachment files stored under configured local upload folder, defaulting to `java.io.tmpdir`
- H2 enabled for local development; alternate DB profiles supported

**Integrations**
- JavaMailSender-based email delivery
- MailHog documented for local development
- Swagger/OpenAPI endpoints exposed in security allowlist

**Infrastructure**
- Dockerfile and Docker Compose present
- WRO4J asset processing configured
- Virtual threads enabled in Spring configuration

---

## 3. Feature Inventory

### Core Features

#### Feature 1: Public Note Feed

**Category:** Core  
**Confidence:** 96% (High)

**Description:** The landing page shows the most recent notes from all users, including title, content, author, timestamp, and attachment links when present.

**User Value:** Lets visitors immediately consume published content without friction.

**Technical Evidence:**
- MVC route `/` in `IndexController`
- Feed rendering in `landing.html` and `note/_notes.html`
- E2E assertions in `WebAppIT.testPublicPage`

**Validation Needed:** None.

#### Feature 2: Account Registration

**Category:** Core  
**Confidence:** 92% (High)

**Description:** Anonymous users can register with first name, last name, email, username, and password. New users are assigned the standard user role and receive a welcome email.

**User Value:** Enables self-service onboarding.

**Technical Evidence:**
- MVC routes `/signup` GET/POST
- DTO validation in `UserSignUpDTO`
- Custom uniqueness/password checks in `UserSignupValidator`
- Role assignment and email send in `UserService.create`

**Validation Needed:**
- Confirm expected behavior when email delivery fails; current implementation logs failures and continues.

#### Feature 3: Authenticated Note Posting with Attachments

**Category:** Core  
**Confidence:** 90% (High)

**Description:** Logged-in users can create notes and optionally upload one attachment per submitted file input.

**User Value:** Core publishing workflow of the product.

**Technical Evidence:**
- POST `/note/add`
- Attachment handling in `NoteService.createNote`
- File storage in `FileService`
- UI forms in `landing.html` and `note.html`
- E2E note creation coverage in `WebAppIT.testLoggedInUserPage`

**Validation Needed:**
- Confirm intended limits for file size, file count, and accepted MIME types; no explicit restrictions were found.

#### Feature 4: Personal Notes Management

**Category:** Core  
**Confidence:** 95% (High)

**Description:** Logged-in users can open a private notes page showing only their own notes.

**User Value:** Separates personal authored content from the global public feed.

**Technical Evidence:**
- GET `/note` in `IndexController`
- User-scoped query in `NoteService.readAllByUser`
- UI behavior verified by `WebAppIT.testUserPage`

**Validation Needed:** None.

#### Feature 5: Note Editing and Deletion with Ownership Enforcement

**Category:** Core  
**Confidence:** 93% (High)

**Description:** Users can edit or delete only notes they own. Access is enforced both in the UI and at the controller level using a permission evaluator.

**User Value:** Supports safe self-service content management.

**Technical Evidence:**
- GET `/note/edit/{id}`, POST `/note/edit`, GET `/note/delete/{id}`
- `@PreAuthorize("@permEvaluator.hasAccess(...))"`
- Conditional links in `note.html`
- E2E coverage for update/delete in `WebAppIT.testUserPage`

**Validation Needed:**
- Confirm expected UX for authorization failures beyond generic access denial.

#### Feature 6: Account Self-Service

**Category:** Core  
**Confidence:** 91% (High)

**Description:** Authenticated users can update profile data and change password from the account menu.

**User Value:** Basic account maintenance without administrator intervention.

**Technical Evidence:**
- GET/POST `/profile`
- GET/POST `/password`
- Update logic in `UserService`
- Custom weak-password check in `PasswordUpdateValidator`

**Validation Needed:**
- Confirm whether password policy should be stronger than “must not contain username”.

### Secondary Features

#### Feature 7: Theme Toggle and Preference Persistence

**Category:** Secondary  
**Confidence:** 97% (High)

**Description:** Users can toggle light/dark mode from the navbar. The preference persists across navigation and refresh using browser `localStorage`.

**User Value:** Improves comfort and perceived polish.

**Technical Evidence:**
- Theme initializer in `header.html`
- Toggle logic in `static/js/app.js`
- MVC and E2E coverage in `ThemeToggleMvcIT` and `WebAppIT.themePreferencePersistsAcrossNavigationAndRefresh`

**Validation Needed:** None.

#### Feature 8: Attachment Download

**Category:** Secondary  
**Confidence:** 83% (Medium)

**Description:** Attached files are downloadable via `/download/file/{id}`.

**User Value:** Allows lightweight document sharing alongside notes.

**Technical Evidence:**
- `DownloadController`
- `ReceivedFileService` lookup and `FileService.loadAsResource`
- Attachment links in note templates

**Validation Needed:**
- Confirm whether public download access is intentional for all attachments.
- Confirm retention/deletion policy for stored files when notes are updated or removed.

#### Feature 9: Seeded Demo Environment

**Category:** Secondary  
**Confidence:** 94% (High)

**Description:** Development/test/docker profiles automatically create demo users, roles, and sample notes.

**User Value:** Speeds local onboarding, QA, and demos.

**Technical Evidence:**
- `DataCreator`
- README default credentials

**Validation Needed:** None for dev/test usage.

#### Feature 10: Authenticated Account API

**Category:** Secondary  
**Confidence:** 76% (Medium)

**Description:** An authenticated API endpoint exposes current account details.

**User Value:** Supports AJAX or future frontend/API integrations.

**Technical Evidence:**
- GET `/api/account` in `UserResource`
- `/api/**` requires authentication in security config

**Validation Needed:**
- Confirm whether this endpoint is actively used; no current UI dependency was found.

### Legacy / Dev-Only Features

#### Feature 11: Debug/Test Email Endpoints

**Category:** Legacy/Dev-Only  
**Confidence:** 82% (Medium)

**Description:** Development and test profiles expose `/debug/hello` and `/debug/sendEmail`.

**User Value:** Supports local verification and diagnostics, not end-user workflows.

**Technical Evidence:**
- `HelloResource`
- `@Profile(dev, test)` restriction

**Validation Needed:**
- Confirm whether these endpoints should remain, be moved behind stronger controls, or be removed.

#### Feature 12: Admin Landing Page

**Category:** Legacy/Unclear  
**Confidence:** 48% (Low)

**Description:** An admin-only page exists at `/admin`, but the business purpose appears minimal or placeholder.

**User Value:** Unclear from current implementation.

**Technical Evidence:**
- GET `/admin` in `IndexController`
- Security restriction to `ROLE_ADMIN`
- Template exists as `admin.html`

**Validation Needed:**
- Determine intended admin use cases.
- Confirm whether this page is a placeholder, demo artifact, or planned feature.

---

## 4. User Flows (Reconstructed)

### Flow 1: Anonymous Visitor to Registered User

1. User lands on `/` and reads the public note feed.
2. User clicks `Signup`.
3. User submits registration form.
4. System validates username uniqueness and rejects weak password patterns.
5. System creates the account, assigns `ROLE_USER`, attempts welcome email delivery, and redirects to home.

**Confidence:** 91% (High)

### Flow 2: Returning User Publishes a Note

1. User signs in using form login handled by Spring Security.
2. User sees authenticated navigation and note posting form.
3. User submits title, content, and optional attachment.
4. System stores attachment file, persists note metadata, and redirects back with success feedback.
5. New note appears in the feed and in the user-specific notes page.

**Confidence:** 93% (High)

### Flow 3: User Manages Existing Note

1. User opens `/note`.
2. User sees only notes created by that account.
3. User selects `Edit` or `Delete`.
4. Permission evaluator verifies ownership.
5. System updates or removes the note and redirects with success feedback.

**Confidence:** 94% (High)

### Flow 4: User Maintains Account

1. User opens profile or password screen from navbar dropdown.
2. User submits updated details.
3. System validates input and saves changes.
4. User is redirected to home with success message.

**Confidence:** 90% (High)

---

## 5. Business Rules and Constraints (Inferred)

- Anonymous access is allowed for `/`, signup, static assets, Swagger endpoints, H2 console, and debug endpoints.
- All other routes require authentication unless explicitly allowlisted.
- `/admin/**` requires `ROLE_ADMIN`.
- Note edit/delete requires ownership via custom permission evaluation.
- Usernames must be unique.
- Signup and password update reject passwords that contain the username or vice versa.
- New signup defaults to `ROLE_USER`.
- Notes are displayed newest first.
- Public feed shows notes from all users; private notes page is author-scoped.
- Attachment storage uses generated UUID-based server filenames, not original names.

**Confidence:** 89% (High)

---

## 6. Known Gaps, Risks, and Technical Debt

### Product/UX Gaps

- Note creation and edit flows lack robust validation and form error recovery (`TODO` comments remain).
- Login UX relies on Spring Security defaults; no custom login page or richer onboarding flow is present.
- Admin experience is thin and may not correspond to a real operational workflow.
- There is no visible pagination or search/filtering despite pageable repository usage.

### Security/Compliance Risks

- CSRF is disabled globally.
- Attachment downloads are publicly accessible if the file UUID is known.
- No explicit file size, type, or antivirus controls were found.
- Password policy is minimal.

### Data/Operational Risks

- Upload storage defaults to temp directory, which is fragile for persistent environments.
- No evidence that deleting a note removes the stored attachment bytes from disk.
- No schema migration tooling is configured yet.

### Architecture/Maintainability Gaps

- API layer is minimal and inconsistent with a broader platform strategy.
- Several features appear demo-oriented rather than productized.
- Email send failures are logged but do not affect transactional outcomes.

---

## 7. Validation Needed

These areas should be confirmed with stakeholders or maintainers:

1. Intended target audience: demo/sample app vs internal tool vs real product.
2. Expected production storage strategy for attachments.
3. Whether public attachment downloads are intentional.
4. Required password policy and account recovery flows.
5. Intended role and roadmap for the `/admin` area.
6. Whether `/api/account` is part of an active integration contract.
7. Expected note validation rules, especially title/content requirements and attachment constraints.
8. Desired behavior when email delivery fails during signup.

---

## 8. Modernization Roadmap

### Priority 1: Stabilize Core Content Workflows

- Add bean validation for note create/edit DTOs.
- Return users to forms with inline validation errors.
- Add tests for invalid note submissions and attachment edge cases.

### Priority 2: Secure Files and Sessions

- Re-enable CSRF selectively or adopt an intentional alternative.
- Decide attachment access model: public, authenticated, or owner-only.
- Add file constraints, retention rules, and orphan cleanup.

### Priority 3: Productize Account Lifecycle

- Add explicit email verification and password reset flows using existing domain fields (`activationKey`, `resetKey`).
- Strengthen password policy.
- Decide whether welcome email failures should block signup or queue retry.

### Priority 4: Improve Discoverability and Scale

- Add pagination controls, search, and richer note metadata.
- Consider note formatting support; README already identifies Markdown editor as future work.
- Expand API surface if frontend decoupling is a goal.

### Priority 5: Operational Hardening

- Introduce Liquibase or Flyway.
- Externalize persistent file storage.
- Clarify production profile defaults and deployment expectations.

---

## 9. Recommended Next PRDs / Follow-Up Specs

- **Attachment Security and Lifecycle PRD**
- **Account Recovery and Verification PRD**
- **Note Validation, Search, and Pagination PRD**
- **Admin Console Clarification PRD**
- **Production Readiness and Deployment Hardening PRD**

---

## 10. Evidence Base

Primary evidence used for this PRD:

- `README.md`
- MVC controllers under `src/main/java/gt/app/web/mvc/`
- REST controllers under `src/main/java/gt/app/web/rest/`
- Domain models under `src/main/java/gt/app/domain/`
- Services under `src/main/java/gt/app/modules/`
- Thymeleaf templates under `src/main/resources/templates/`
- App configuration in `src/main/resources/application.yml`
- Integration and E2E tests in `src/test/java/gt/app/`

