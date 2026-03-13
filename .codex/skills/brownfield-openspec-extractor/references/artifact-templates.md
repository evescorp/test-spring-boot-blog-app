# Artifact Templates

Use these templates as starting points. Keep `spec.md` strictly canonical.

## `spec.md`

Do not add custom sections unless OpenSpec clearly permits them and they are needed. If unsure, use exactly this shape:

```md
# <Domain Name>

## Purpose

<One short paragraph describing the capability owned by this domain inside this repository.>

## Requirements

### Requirement: <requirement name>
The system SHALL <confirmed externally observable behavior>.

#### Scenario: <scenario name>
- GIVEN <confirmed precondition in this repository>
- WHEN <confirmed user action or system trigger>
- THEN <confirmed visible outcome or enforced result>
```

Spec checklist:
- requirement is confirmed
- wording is externally observable
- scenario does not cross an unproven boundary
- no Mermaid
- no implementation narration
- no inferred states or policies

## `event-flow.md`

```md
# <Domain Name> Event Flow

## Flow: <flow name>

- Type: <Local complete flow | Local partial flow | Boundary flow>
- Summary: <one or two sentences>

### Command
<user action or system trigger>

### Event Flow
1. <step grounded in evidence>
2. <step grounded in evidence>
3. <visible outcome or boundary handoff>

### View
<externally visible result>

### Boundary Notes
- <where the flow leaves this repository, if applicable>

### Uncertainty Notes
- <only if needed>
```

Use a Mermaid sequence diagram only when it clarifies a non-trivial flow better than text alone.

## `design.md`

```md
# <Domain Name> Design Notes

## Scope
<what part of the repository this note covers>

## Entry Points
- <route, controller, handler, job, subscriber>

## Main Components
- <component and responsibility>

## Persistence
- <tables, entities, repositories, important reads/writes>

## External Interactions
- <clients, queues, external systems, boundary handoffs>

## Observations
- <technical constraints, inconsistencies, or notable patterns>
```

Keep this technical. Do not frame it as source-of-truth behavior.

## `brownfield-analysis-summary.md`

```md
# Brownfield Analysis Summary

## Repository Role
<what this repository appears to own>

## Discovered Domains
- <domain>: <short capability summary>

## Major Flows
- <flow summary and whether it is local or boundary-crossing>

## Boundaries And Handoffs
- <upstream or downstream systems and what is or is not proven here>

## Ambiguities And Blind Spots
- <important inferred or unclear areas>
```

Repository summary checklist:
- makes repo scope explicit
- distinguishes confirmed from inferred or unclear
- names major boundaries
- does not imply platform-wide ownership without proof
