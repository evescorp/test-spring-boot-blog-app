# Phased Workflow

Use this workflow when extracting source-of-truth OpenSpec artifacts from an existing repository.

## Phase 1: Domain Discovery

Goal: build a capability map without writing authoritative specs yet.

Inspect:
- routes and route registration
- controllers, handlers, actions, jobs
- services with externally visible effects
- entities, schemas, migrations, repositories
- validators and form/request models
- serializers, presenters, API contracts
- tests that exercise current behavior
- frontend actions, page states, error states, empty states
- config and feature flags that materially change behavior
- event publishers/subscribers and external clients

Capture:
- candidate domain name
- short capability statement
- primary actors
- major local flows
- persistence touchpoints
- integrations and boundary exits
- confidence notes

Reject candidate domains that are only:
- folders
- technical layers
- UI widgets
- helper abstractions
- internal adapters

Discovery output format:

```md
## Proposed Domains

### <domain-name>
- Capability:
- Primary actors:
- Core evidence:
- Boundary notes:
- Why this is a domain:

## Suggested Extraction Order

1. <domain-name> - because ...
2. <domain-name> - because ...
```

Stop here unless the user asked for more.

## Phase 2: Domain Spec Extraction

Goal: produce `/openspec/specs/<domain>/spec.md`.

Method:
1. Gather only evidence relevant to the named domain.
2. List candidate statements.
3. Grade each statement as confirmed, inferred, or unclear.
4. Keep only confirmed statements.
5. Convert them into concise OpenSpec requirements and scenarios.

Spec writing rules:
- express behavior, not architecture
- prefer several small truthful requirements over one broad speculative one
- write scenarios that mirror real repository-supported flows
- stop at repository boundaries

## Phase 3: Event Flow Extraction

Goal: produce `/openspec/specs/<domain>/event-flow.md` only if the domain has a meaningful temporal flow.

For each flow:
- identify the command or trigger
- identify the real state change or externally meaningful event
- identify the visible outcome
- classify the flow

Flow classification:
- `Local complete flow`: the full observed flow completes inside this repo
- `Local partial flow`: this repo implements only part of a larger flow
- `Boundary flow`: the flow clearly crosses into another system or repository

Do not invent event names. Prefer plain language over pseudo-DDD labels unless the code explicitly uses them.

## Phase 4: Design Note Extraction

Goal: produce `/openspec/specs/<domain>/design.md`.

Document:
- primary entry points
- collaborating components
- persistence reads and writes
- background jobs or scheduled work
- external integrations
- structural constraints, inconsistencies, or technical debt relevant to understanding the domain

Do not restate requirements as if design were authoritative.

## Phase 5: Repository Synthesis

Goal: produce `/openspec/brownfield-analysis-summary.md`.

Summarize:
- what this repository appears to own
- what it does not prove
- which domains were extracted or proposed
- key boundaries and handoffs
- major ambiguity hotspots

## Checkpoint Practice

Brownfield extraction is safer in checkpoints. Prefer these stopping points:

1. discovery complete, no specs yet
2. one domain spec complete
3. one domain flow complete
4. one domain design note complete
5. repository summary complete

At each checkpoint, explicitly say:
- what was proven
- what was intentionally excluded
- what remains unclear
