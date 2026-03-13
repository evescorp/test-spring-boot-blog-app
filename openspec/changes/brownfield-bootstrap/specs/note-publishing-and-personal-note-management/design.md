# Note Publishing and Personal Note Management Design Notes

## Scope

This note covers the implementation structure behind the public landing-page feed, authenticated note creation, the user-scoped `/note` page, and owner-gated note edit and delete behavior.

## Entry Points

- `GET /` in [`IndexController`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/IndexController.java) loads the shared feed and adds a note form model object.
- `GET /note` in [`IndexController`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/IndexController.java) loads the authenticated user's notes and reuses the note form.
- `POST /note/add` in [`NoteController`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/NoteController.java) creates notes.
- `GET /note/edit/{id}` and `POST /note/edit` in [`NoteController`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/NoteController.java) load and update note content.
- `GET /note/delete/{id}` in [`NoteController`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/NoteController.java) deletes a note.

## Main Components

- [`NoteController`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/NoteController.java) handles note mutation routes and redirects after writes.
- [`IndexController`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/web/mvc/IndexController.java) serves both the public feed and the authenticated personal note page.
- [`NoteService`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/note/NoteService.java) is the main application service for create, read, update, delete, and user ownership lookup.
- [`NoteMapper`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/note/dto/NoteMapper.java) maps between form DTOs, entities, and read DTOs used by Thymeleaf templates.
- [`AppPermissionEvaluatorService`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/user/AppPermissionEvaluatorService.java) and [`UserAuthorityService`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/user/UserAuthorityService.java) enforce owner-only edit/delete access through `@PreAuthorize`.
- Thymeleaf templates [`landing.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/landing.html), [`note.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/note.html), [`note/edit-note.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/note/edit-note.html), and [`note/_notes.html`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/resources/templates/note/_notes.html) render shared note UI, note forms, and note cards.

## Persistence

- [`Note`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/domain/Note.java) is the main entity. It inherits created/modified audit fields and owner references from [`BaseAuditingEntity`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/domain/BaseAuditingEntity.java).
- [`NoteRepository`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/note/NoteRepository.java) provides:
  - `findAll(Pageable)` for the shared feed
  - `findByCreatedByUserIdOrderByCreatedDateDesc(Pageable, Long)` for the personal note page
  - `findById(Long)` for edit reads
  - `findCreatedByUserIdById(Long)` for owner checks
- Note read models include attachment metadata and owner identity through [`NoteReadDto`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/note/dto/NoteReadDto.java).
- Auditing is enabled through [`JpaConfig`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/config/JpaConfig.java), so created-by and created-date fields are populated by Spring Data JPA infrastructure.

## External Interactions

- Note creation can delegate attachment storage to [`FileService`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/modules/file/FileService.java), which writes binaries under the configured upload folder from [`AppProperties`](/home/eve/WebProjects/test-spring-boot-blog-app/src/main/java/gt/app/config/AppProperties.java). That crosses from JPA persistence into local filesystem storage.
- Authentication and authorization depend on Spring Security configuration and the current authenticated principal, but all relevant enforcement logic for note ownership remains inside this repository.

## Observations

- `NoteController` contains TODO comments for create and edit validation error handling, so form validation for note writes is not fully implemented at the controller flow level.
- `NoteRepository` combines pageable queries with collection fetching of `attachedFiles`; repository documentation in `.onboard` notes this causes Hibernate in-memory pagination warnings.
- Owner checks are implemented twice in practice: the UI hides edit/delete links for non-owners, and server-side `@PreAuthorize` enforces access before mutation routes execute.
- Note edit updates title and content only. Attachment updates and cleanup are outside this domain note and are not implemented in the edit path.
