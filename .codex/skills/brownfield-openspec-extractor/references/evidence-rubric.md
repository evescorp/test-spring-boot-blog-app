# Evidence Rubric

Use this rubric before promoting any statement into `spec.md`.

## Classifications

### Confirmed

Use `confirmed` when the behavior is directly supported by one or more of:
- route and handler logic
- controller action and returned response
- persistence writes or state transitions
- validation logic enforced by the backend
- schema constraints with visible behavioral effect
- background job wiring plus visible effect
- high-signal tests that match active code paths
- frontend behavior paired with backend support in the same repo

`Confirmed` statements may be promoted into `spec.md`.

### Inferred

Use `inferred` when behavior is strongly suggested but not fully proven, for example:
- UI copy suggests a validation rule that backend code does not enforce
- multiple components imply a flow but the terminal step is outside the repo
- an old test or comment suggests behavior not clearly present in runtime code
- naming suggests lifecycle states with no explicit transition logic

`Inferred` statements must stay out of `spec.md`.

### Unclear

Use `unclear` when evidence is missing, conflicting, dead, or stale, for example:
- code paths appear unused
- frontend and backend disagree
- tests contradict implementation
- a feature flag exists but active behavior is uncertain
- an integration client exists but no reachable usage is proven

`Unclear` statements must stay out of `spec.md`.

## Inclusion Test For `spec.md`

Every requirement and scenario must pass all of these:

1. The behavior is externally observable.
2. The repository contains direct evidence for it.
3. The wording avoids speculative lifecycle states or policies.
4. The behavior does not silently cross an unproven boundary.
5. The statement describes what the system does, not how helpers are implemented.

If any check fails, do not include the statement in `spec.md`.

## Evidence Weighting

Prefer stronger evidence when sources disagree:

1. active runtime path with explicit behavior
2. persistence or validation logic
3. high-signal current tests
4. schema constraints with visible effect
5. configuration that clearly enables or disables behavior
6. UI copy and labels
7. README, comments, issue references

Lower-ranked sources may explain context but should not overrule higher-ranked implementation evidence.

## Boundary Handling

When behavior leaves the repository:
- keep local behavior if it is confirmed
- stop the requirement at the handoff
- note the downstream step in `event-flow.md`, `design.md`, or `brownfield-analysis-summary.md`
- do not claim end-to-end success unless the repo proves it

## Common Failure Modes

Do not do these:
- infer a business rule from a field name alone
- infer backend validation from frontend error text alone
- infer lifecycle states from status badges alone
- infer an end-to-end platform journey from one internal API client
- convert service orchestration details into user-facing requirements
- trust outdated tests over current reachable code without reconciliation
