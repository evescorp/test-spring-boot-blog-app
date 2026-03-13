# Design Suggestions

Assessment date: 2026-03-13.

## What Is Working Well

- Controllers are mostly thin and delegate to services.
- The codebase has clear module names and predictable package layout.
- Security ownership checks are explicit at controller boundaries.
- ArchUnit rules encode several architectural expectations.
- The theme toggle was added in a focused way and covered by tests.

## Main Design Risks

### 1. Note form validation is incomplete

`NoteController` contains explicit TODOs for validation on create/edit and currently redirects without a proper form-error cycle.

Recommendation:
- add Bean Validation to note DTOs
- validate in controller methods
- return the relevant view with field errors instead of redirecting blindly

### 2. Attachment lifecycle is underspecified

Files are stored on disk through `FileService`, but note deletion does not obviously remove the physical files. Downloads are intentionally public.

Recommendation:
- define whether attachments are public forever or scoped to note visibility
- add cleanup on note/file deletion
- add size/type/count limits
- test storage cleanup explicitly

### 3. Public file download may be too permissive

`DownloadController` states “no security check needed”. That may be valid for a demo app, but it is an important product/security decision and should be documented.

Recommendation:
- either keep public downloads and state that clearly
- or add authorization rules tied to note visibility/ownership

### 4. Pagination strategy is inefficient for fetched collections

`NoteRepository` combines `Pageable` with collection fetching, and Hibernate warns that pagination is applied in memory.

Recommendation:
- fetch note IDs page-first, then load associations in a second query
- or use projection-based list views

### 5. Domain/account model exposes unfinished product ideas

`AppUser` includes `activationKey` and `resetKey`, but there is no completed activation/reset flow in the visible app.

Recommendation:
- either implement account lifecycle features fully
- or remove dormant fields until needed

## Secondary Improvements

- Standardize repository visibility/naming; current code mixes package-private repository interfaces and service-centric access.
- Clarify why the app has both `AppUser` and `LiteUser`; the pattern is workable but should be documented for contributors.
- Tighten equals/hashCode implementations flagged by Error Prone on entity/security classes.
- Reduce noisy historical content in README so current behavior is easier to trust.

## README / Documentation Changes Worth Making

- add a “module map” section
- add a “security and permissions” section
- add a “known build issues” section
- add a “file attachment behavior” section
