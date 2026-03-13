# Note Publishing and Personal Note Management Event Flow

## Flow: Authenticated note publishing

- Type: Local complete flow
- Summary: An authenticated user submits the note form, the repository persists the note, and the new note becomes visible on later page renders within this application.

### Command
Authenticated user submits the note form to `/note/add`.

### Event Flow
1. A signed-in user loads a page that renders the note form, such as `/` or `/note`, and enters note title and content.
2. The browser submits the form to `/note/add`, and the controller delegates to `NoteService.createNote`.
3. The service maps the submitted data to a `Note`, persists it through `NoteRepository`, and the controller redirects the user to `/`.
4. A subsequent landing-page or personal-notes page render reads notes from the repository and includes the newly stored note in the response.

### View
The user is redirected after submission, and the created note is visible on later page views in this repository.

### Boundary Notes
- This flow completes inside this repository for note persistence and page rendering.
- If files are included in the same form submission, attachment storage also touches the local filesystem; that boundary is intentionally not expanded here because attachment behavior was left out of the reviewed authoritative spec.

### Uncertainty Notes
- The note form markup marks title and content as required, but the controller still contains TODO comments for validation handling, so no stronger validation behavior is asserted here.
- The spec does not claim whether the newly created note must appear at the top of the feed immediately after redirect; the repository does prove that later page views include it.
