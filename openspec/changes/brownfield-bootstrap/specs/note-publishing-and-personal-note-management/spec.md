# Note Publishing and Personal Note Management

## Purpose

This domain covers the repository behavior for showing a shared note feed, allowing authenticated users to create notes, giving signed-in users a personal notes page, and limiting note editing and deletion to the note owner.

## Requirements

### Requirement: Public note feed
The system SHALL render a shared note feed on the landing page for anonymous and authenticated visitors.

#### Scenario: Landing page shows published notes
- GIVEN notes exist in the repository
- WHEN a visitor loads `/`
- THEN the response includes the published notes in descending creation order

### Requirement: Authenticated note publishing
The system SHALL allow authenticated users to publish a new note.

#### Scenario: Signed-in user posts a note
- GIVEN an authenticated user is viewing a page with the note form
- WHEN the user submits a note to `/note/add`
- THEN the system stores the note and the new note appears in subsequent page views

### Requirement: Personal notes view
The system SHALL provide an authenticated personal notes page that is scoped to the current user.

#### Scenario: User opens personal notes page
- GIVEN an authenticated user has access to `/note`
- WHEN the user loads `/note`
- THEN the response includes that user's notes and excludes notes created by other users

### Requirement: Owner-only note modification
The system SHALL restrict note editing and deletion to the note owner.

#### Scenario: Owner edits a note
- GIVEN an authenticated user owns an existing note
- WHEN the user opens `/note/edit/{id}` and submits updated title and content to `/note/edit`
- THEN the note is updated and the previous title and content are no longer shown on the user's note views

#### Scenario: Owner deletes a note
- GIVEN an authenticated user owns an existing note
- WHEN the user requests `/note/delete/{id}`
- THEN the note is removed and no longer appears in the shared feed
