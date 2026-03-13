# Domain Map

## Repository Scope

This repository appears to own a server-rendered note application with local authentication, role-gated pages, note CRUD for authenticated users, public note browsing, optional file attachment storage and download, and basic account self-service flows. Evidence comes from MVC controllers and Thymeleaf pages for landing, note, signup, profile, and password screens; JPA entities and repositories for users, notes, authorities, and received files; security configuration for anonymous and authenticated routes; and integration/e2e tests that exercise browsing, login, note posting, editing, deleting, and theme persistence.

The repository also exposes a small authenticated `/api/account` endpoint and dev/test-only debug email endpoints. It does not prove ownership of an external identity provider, external object storage, or downstream email delivery guarantees beyond calling local Spring Mail infrastructure.

## Proposed Domains

### `note publishing and personal note management`
- Capability: Publish short notes to the shared feed and let authenticated users manage their own notes from a personal notes page.
- Primary actors: Anonymous visitors, authenticated note authors, admin users who can also browse as authenticated users.
- Core evidence: [`src/main/java/gt/app/web/mvc/IndexController.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/IndexController.java), [`src/main/java/gt/app/web/mvc/NoteController.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/NoteController.java), [`src/main/java/gt/app/modules/note/NoteService.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/note/NoteService.java), [`src/main/java/gt/app/modules/note/NoteRepository.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/note/NoteRepository.java), [`src/main/resources/templates/landing.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/landing.html), [`src/main/resources/templates/note.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/note.html), [`src/test/java/gt/app/e2e/WebAppIT.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/test/java/gt/app/e2e/WebAppIT.java).
- Major local flows: Public landing page shows recent notes; authenticated users can post notes from landing and personal pages; authenticated users can view only their own notes on `/note`; owners can edit and delete notes; dev/test seed data creates starter notes and users.
- Boundary notes: Ownership checks depend on Spring Security plus a permission evaluator service inside this repo; attachments delegate to the file storage domain; pagination is limited to local repository queries and current templates only show the first page.
- Confidence notes: Confirmed. This is the clearest, best-evidenced domain and has end-to-end test coverage.

### `user registration and self-service account maintenance`
- Capability: Let anonymous users sign up locally, and let authenticated users view/update profile details, change password, and inspect their authenticated account via `/api/account`.
- Primary actors: Anonymous visitors registering accounts, authenticated end users managing their own account, authenticated API clients calling `/api/account`.
- Core evidence: [`src/main/java/gt/app/web/mvc/UserController.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/UserController.java), [`src/main/java/gt/app/modules/user/UserService.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/user/UserService.java), [`src/main/java/gt/app/modules/user/UserSignupValidator.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/user/UserSignupValidator.java), [`src/main/java/gt/app/modules/user/PasswordUpdateValidator.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/user/PasswordUpdateValidator.java), [`src/main/java/gt/app/web/rest/UserResource.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/rest/UserResource.java), [`src/main/resources/templates/user/signup.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/user/signup.html), [`src/test/java/gt/app/web/rest/AppUserResourceIT.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/test/java/gt/app/web/rest/AppUserResourceIT.java), [`src/test/java/gt/app/e2e/WebAppIT.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/test/java/gt/app/e2e/WebAppIT.java).
- Major local flows: Render signup form; reject duplicate usernames and weak passwords that contain the username; create a local user with `ROLE_USER`; render profile and password forms for authenticated users; update stored profile fields and encoded password; return the current authenticated principal at `/api/account`.
- Boundary notes: Account creation sends a welcome email through Spring Mail; authentication itself is handled by Spring Security form login within this repo rather than an external IdP; no email verification or password reset workflow is proven here.
- Confidence notes: Confirmed for signup/profile/password/account lookup. Unclear for broader account lifecycle beyond local self-service because no deactivate UI, reset flow, or admin user management UI is exposed.

### `attachment storage and public file retrieval`
- Capability: Accept uploaded note attachments, persist attachment metadata, store binary files on local filesystem-backed storage, and serve downloads by attachment id.
- Primary actors: Authenticated note authors uploading files, any caller who can access a download URL.
- Core evidence: [`src/main/java/gt/app/modules/file/FileService.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/file/FileService.java), [`src/main/java/gt/app/modules/file/ReceivedFileService.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/file/ReceivedFileRepository.java), [`src/main/java/gt/app/web/mvc/DownloadController.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/DownloadController.java), [`src/main/java/gt/app/domain/ReceivedFile.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/domain/ReceivedFile.java), [`src/main/resources/templates/note.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/note.html), [`src/test/java/gt/app/modules/file/FileDownloadUtilTest.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/test/java/gt/app/modules/file/FileDownloadUtilTest.java).
- Major local flows: During note creation, non-empty multipart files are stored under the configured upload folder and linked to the note as `NOTE_ATTACHMENT`; note pages render attachment links; `/download/file/{id}` loads attachment metadata and streams the stored file with download headers.
- Boundary notes: Storage is filesystem-based under `app-properties.file-storage.upload-folder`; attachment binaries are outside the database; download endpoint explicitly omits an authorization check, so access control depends on URL knowledge rather than ownership enforcement.
- Confidence notes: Confirmed for create-and-download behavior. Unclear for attachment deletion, replacement, and lifecycle cleanup because note edit does not manipulate files and no cleanup workflow is visible.

### `environment support and diagnostics`
- Capability: Seed development/test data, expose debug endpoints in non-prod profiles, and support UI theme preference persistence in the browser.
- Primary actors: Developers, testers, operators running non-prod profiles, end users toggling theme preference.
- Core evidence: [`src/main/java/gt/app/DataCreator.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/DataCreator.java), [`src/main/java/gt/app/web/rest/HelloResource.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/rest/HelloResource.java), [`src/main/resources/templates/_fragments/header.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/_fragments/header.html), [`src/main/resources/static/js/app.js`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/static/js/app.js), [`src/test/java/gt/app/web/mvc/ThemeToggleMvcIT.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/test/java/gt/app/web/mvc/ThemeToggleMvcIT.java), [`src/test/java/gt/app/e2e/WebAppIT.java`](/home/eve/WebProjects/test-spring-boot-blog-app/src/test/java/gt/app/e2e/WebAppIT.java).
- Major local flows: Seed roles, users, notes, and upload directories on context refresh in dev/test/docker; expose `/debug/hello` and `/debug/sendEmail` only in dev/test profiles; initialize and persist a light/dark theme choice in browser local storage across navigation and refresh.
- Boundary notes: This mixes developer support behavior with a small user-facing UI preference; email debug relies on the same local Spring Mail integration used elsewhere; theme persistence is client-side only and not tied to user accounts.
- Confidence notes: Weakly supported as a cohesive domain. The pieces are real, but they span diagnostics, bootstrap, and cross-cutting UI preference rather than one business capability.

## Cross-Domain Observations

- Spring Security and method-level permission checks are cross-cutting and shape the note and account domains, but they read as enforcement infrastructure rather than a standalone business domain.
- Public browsing and authenticated note management share the same note persistence model and templates; they likely belong in one note domain with public and owner-specific views rather than separate specs.
- Theme preference is implemented entirely in the browser and touches multiple pages, so it may be better treated as a supporting cross-domain concern unless later extraction explicitly targets UI theme behavior.
- Email sending appears as a boundary from account registration and debug tooling to SMTP infrastructure; this repo proves the send attempt, not downstream delivery or mailbox consumption.
- Attachment download is public by current implementation, which is a significant boundary/security characteristic to preserve during later spec extraction.

## Suggested Extraction Order

1. `note publishing and personal note management` - Clearest capability boundary, strongest controller/service/template/test evidence, and lowest ambiguity for the first authoritative spec.
2. `user registration and self-service account maintenance` - Strong route and validator evidence, but with slightly more ambiguity around broader lifecycle and email side effects.
3. `attachment storage and public file retrieval` - Important because it crosses filesystem boundaries and has an explicit public-access decision, but part of its lifecycle remains incomplete.
4. `environment support and diagnostics` - Extract last, if at all, because it is weakly cohesive and may be better split into supporting notes instead of a canonical domain spec.

## Review Checklist

- Each domain represents a real capability.
- Each domain uses domain language rather than implementation language.
- Each domain has a clear actor or actor group.
- Repo boundaries and external dependencies are explicitly marked.
- Weakly supported domains are flagged instead of overstated.
- No specs, event flows, or design notes are generated here.
