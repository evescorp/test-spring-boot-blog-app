## 1. Theme Infrastructure

- [ ] 1.1 Add global theme toggle control to shared header fragment with accessible labeling/state.
- [ ] 1.2 Add early initialization script in shared head fragment to apply saved/system theme before render.
- [ ] 1.3 Implement client-side toggle logic in static JS to update `data-theme` and persist `app-theme`.

## 2. Styling Updates

- [ ] 2.1 Define light/dark CSS variable tokens for background, surface, text, border, and link colors.
- [ ] 2.2 Apply token-based dark-mode overrides to shared UI surfaces (body, navbar, cards, forms, tables, footer).
- [ ] 2.3 Ensure WRO asset pipeline still bundles modified CSS/JS correctly in dev profile.

## 3. Validation

- [ ] 3.1 Add/update MVC or template tests to verify toggle rendering on shared-layout pages.
- [ ] 3.2 Add/update e2e coverage for theme persistence across navigation and refresh.
- [ ] 3.3 Run targeted test commands and manual browser checks for readability and contrast.

## 4. Documentation & Config

- [ ] 4.1 Document dark mode behavior and persistence in project docs/README.
- [ ] 4.2 Confirm no additional profile or docker configuration changes are required (or document if needed).
