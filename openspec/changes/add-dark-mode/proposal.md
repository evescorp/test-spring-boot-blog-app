## Why
Users currently cannot switch to a dark visual theme, which reduces comfort in low-light environments and limits accessibility preferences. Adding dark mode now improves usability with minimal functional risk because the app already uses shared layout fragments and centralized CSS/JS.

## What Changes
- Add a theme toggle in shared header/navigation so users can switch between light and dark mode.
- Persist theme preference client-side and apply it on page load to avoid reverting between pages.
- Introduce dark-mode CSS token overrides for shared UI surfaces (body, navbar, cards, tables, forms, footer).
- Add tests for theme toggle presence and persistence behavior across navigation/refresh.

## Capabilities

### New Capabilities
- `ui-theme`: User-selectable and persistent UI theme behavior (light/dark) across rendered pages.

### Modified Capabilities
- None.

## Impact
- Affected code: Thymeleaf shared fragments (`_fragments/header.html` and possibly head/footer includes), static CSS (`app.css`, `app2.css`), static JS (`custom.js` or equivalent), and related tests.
- APIs: No REST contract changes.
- Dependencies: No new runtime dependency expected.
- Security/Data/Migration: No authentication model change, no database schema change, no migration needed.
