## ADDED Requirements

### Requirement: User-selectable theme control
The system SHALL provide a globally available control that allows users to switch between light and dark theme modes.

#### Scenario: Theme toggle is available on shared-layout pages
- **WHEN** a user loads a page that renders the shared header/navigation fragment
- **THEN** the user can access a theme toggle control

### Requirement: Theme preference persistence
The system SHALL persist the user-selected theme in browser storage and reuse it on later page loads.

#### Scenario: Preference remains after navigation
- **WHEN** a user selects dark mode and navigates to another application page
- **THEN** the new page renders with dark mode active

#### Scenario: Preference remains after browser refresh
- **WHEN** a user selects dark mode and refreshes the current page
- **THEN** dark mode remains active after refresh

### Requirement: Initial theme resolution order
The system SHALL resolve initial theme in this order: saved user preference, then system preference, then light fallback.

#### Scenario: Saved preference takes priority
- **WHEN** a saved `app-theme` value exists in browser storage at page initialization
- **THEN** the saved value is applied as the active theme

#### Scenario: System preference fallback
- **WHEN** no saved `app-theme` value exists and the browser reports dark color-scheme preference
- **THEN** dark theme is applied as the initial theme

### Requirement: Readable dark-mode presentation
The system SHALL maintain readable contrast for shared UI surfaces in dark mode.

#### Scenario: Shared components remain legible
- **WHEN** dark mode is active on primary application pages
- **THEN** text and controls in body, navigation, forms, tables, cards, and footer remain readable
