## Context
The application is a Spring Boot + Thymeleaf server-rendered UI using shared fragments and bundled static assets. Current styling is light-oriented and based on Bootstrap 4 plus project CSS via WRO aggregation. The change must keep existing page behavior intact, avoid server-side persistence complexity, and remain compatible with authenticated/anonymous pages.

## Goals / Non-Goals

**Goals:**
- Provide a global, user-accessible dark mode toggle.
- Persist theme selection per browser and apply it consistently across page loads.
- Keep implementation additive with minimal template/controller impact.
- Validate behavior with automated tests for visibility and persistence.

**Non-Goals:**
- Replacing Bootstrap or redesigning all screens.
- Storing theme in user profile/database.
- Adding more themes beyond light/dark in this change.

## Decisions
- Decision: Use a root HTML attribute (`data-theme="light|dark"`) to drive theme styling.
  Rationale: Works cleanly with server-rendered pages and avoids per-component class churn.
  Alternative considered: per-element dark classes; rejected due to higher maintenance and risk.

- Decision: Persist preference in `localStorage` key `app-theme` and read it early in shared head fragment.
  Rationale: No backend changes, survives reloads, and supports anonymous users.
  Alternative considered: cookie/server-side preference; rejected for added complexity.

- Decision: Add CSS custom properties for theme tokens and override only shared/common surfaces.
  Rationale: Centralized palette management and lower regression risk than broad selector rewrites.
  Alternative considered: duplicate dark stylesheet; rejected due to duplication and drift risk.

- Decision: Expose the toggle in shared header fragment with accessible label/state.
  Rationale: Ensures feature availability on primary pages and keyboard accessibility.

## Risks / Trade-offs
- [Incomplete dark coverage on some components] -> Mitigation: prioritize shared primitives (body, nav, cards, tables, forms, footer) and add targeted regression checks.
- [Theme flash during first paint] -> Mitigation: run small initialization script in shared head before main CSS/JS execution.
- [Contrast/accessibility regressions] -> Mitigation: use conservative token palette and manual verification on key pages plus e2e validation.
- [Bootstrap class conflicts] -> Mitigation: prefer scoped overrides via CSS variables and avoid heavy `!important` usage.
