# Domain Map

## Repository Scope

<!-- Describe what this repository appears to own based on evidence. -->

## Proposed Domains

### `<domain-name>`
- Capability: <!-- Real system capability, not a technical layer -->
- Primary actors: <!-- Who initiates or depends on this behavior -->
- Core evidence: <!-- Routes, handlers, tests, persistence, UI states, config, etc. -->
- Major local flows: <!-- Main local behaviors visible in this repo -->
- Boundary notes: <!-- Upstream/downstream systems, integrations, or handoffs -->
- Confidence notes: <!-- Confirmed / inferred / unclear boundary of understanding -->

## Cross-Domain Observations

- <!-- Shared actors, overlapping boundaries, naming conflicts, or possible domain splits/merges -->

## Suggested Extraction Order

1. `<domain-name>` - <!-- Why this domain should be extracted first -->
2. `<domain-name>` - <!-- Why this domain should follow -->

## Review Checklist

- Each domain represents a real capability.
- Each domain uses domain language rather than implementation language.
- Each domain has a clear actor or actor group.
- Repo boundaries and external dependencies are explicitly marked.
- Weakly supported domains are flagged instead of overstated.
- No specs, event flows, or design notes are generated here.
