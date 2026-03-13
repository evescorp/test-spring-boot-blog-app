---
name: brownfield-openspec-extractor
description: Reverse-engineer an existing brownfield repository into evidence-backed OpenSpec artifacts that document implemented behavior as it exists today. Use when analyzing a codebase with missing, stale, or misleading docs and you need phased output such as domain discovery, a strictly OpenSpec-compatible spec.md for one domain, event-flow.md, design.md, or a repository-level brownfield-analysis-summary.md without mixing confirmed truth with inference.
---

# Brownfield OpenSpec Extractor

Extract current truth from code. Do not document intent, roadmap, naming optimism, or UI promises unless the repository proves them.

Keep `spec.md` authoritative and minimal. Put ambiguity, inferred behavior, and technical observations into supporting artifacts instead of promoting them into requirements.

## Operating Modes

Choose the smallest mode that satisfies the user request.

1. `discovery only`
   - Analyze the repository.
   - Identify candidate domains, actors, major local flows, boundaries, and extraction order.
   - Do not generate specs yet.
2. `domain spec`
   - Generate only `/openspec/specs/<domain>/spec.md`.
   - Include only confirmed behavior.
3. `event flow`
   - Generate only `/openspec/specs/<domain>/event-flow.md`.
   - Classify each flow as `Local complete flow`, `Local partial flow`, or `Boundary flow`.
4. `design notes`
   - Generate only `/openspec/specs/<domain>/design.md`.
   - Keep it technical and non-normative.
5. `repository summary`
   - Generate only `/openspec/brownfield-analysis-summary.md`.

If the user asks for “full extraction,” still work domain-by-domain. Do not skip discovery unless the repository structure and domain map are already well established in the current conversation.

## Non-Negotiable Rules

- Treat code, active routes, persistence writes, validations, tests, runtime wiring, and explicit state transitions as stronger evidence than README text or UI copy.
- Promote only `confirmed` behavior into `spec.md`.
- Treat `inferred` and `unclear` behavior as non-authoritative notes.
- Document repository boundaries explicitly when flows leave the repo.
- Prefer omission over fabrication.
- Prefer domain capability names over package or folder names.
- Do not turn helpers, adapters, hooks, or internal implementation steps into requirements unless they produce externally observable behavior.
- Do not add custom sections to `spec.md` unless they are clearly optional and safe. If unsure, do not add them.
- Do not use Mermaid in `spec.md`.

Use the evidence rubric in [references/evidence-rubric.md](./references/evidence-rubric.md) before writing requirements.

## Workflow

### 1. Discover the real domain map

Inspect the repository broadly before writing artifacts. Check routes, controllers, handlers, services, persistence, migrations, validators, serializers, tests, jobs, subscribers, feature flags, config, frontend actions, and visible error or empty states.

Group findings by capability, not by technical layer. A good domain has:
- a real actor or actor group
- a coherent externally visible capability
- evidence-backed local behavior
- clear boundaries when behavior continues elsewhere

For the detailed sequence and checklist, use [references/phased-workflow.md](./references/phased-workflow.md).

### 2. Grade every candidate statement

Before writing any requirement or scenario, classify it:

- `confirmed`: directly supported by implementation or trustworthy runtime-facing tests
- `inferred`: suggested by multiple signals but not fully proven
- `unclear`: unsupported, conflicting, stale, or ambiguous

Only confirmed statements belong in `spec.md`.

### 3. Generate only the requested artifact

Use the templates in [references/artifact-templates.md](./references/artifact-templates.md).

When generating:
- `spec.md`: keep it canonical OpenSpec format only
- `event-flow.md`: explain temporal behavior, boundaries, and uncertainty without redefining truth
- `design.md`: explain structure, components, DB usage, jobs, and integrations without turning them into normative behavior
- `brownfield-analysis-summary.md`: synthesize the repository role, discovered domains, major boundaries, blind spots, and unresolved ambiguity

### 4. Verify before finalizing

Run this mental gate on each requirement and scenario:

- Is it externally observable?
- Is it proven by this repository?
- Does it avoid crossing an unproven boundary?
- Does it avoid implied validation or lifecycle states?
- Would a maintainer accept it as “true today” even if the product story sounds messier?

If any answer is no, move the point out of `spec.md`.

## Artifact Rules

### `spec.md`

Keep this exact shape unless OpenSpec explicitly allows more:

```md
# <Domain Name>

## Purpose

...

## Requirements

### Requirement: <name>
The system SHALL ...

#### Scenario: <name>
- GIVEN ...
- WHEN ...
- THEN ...
```

Rules:
- Write concise, behavior-first requirements.
- Use one domain at a time.
- Keep requirements grounded in externally visible behavior.
- Do not include implementation chatter, Mermaid, invented states, or speculative policies.

### `event-flow.md`

Use this only when temporal behavior adds value. Include:
- flow summary
- flow type
- textual command -> event -> view narrative
- boundary notes
- uncertainty notes when needed

Use Mermaid sequence diagrams only when they materially clarify a non-trivial flow.

### `design.md`

Capture discovered implementation structure:
- main components and interaction points
- persistence and schema touchpoints
- external services and clients
- jobs, queues, schedulers, and subscribers
- technical constraints or inconsistencies

Keep it explanatory, not normative.

### `brownfield-analysis-summary.md`

Summarize:
- repository role
- discovered domains
- actors
- major flows
- boundaries and handoffs
- what is confirmed, inferred, and unclear at repo level

## Prompt Handling

Interpret requests using these defaults:

- “Analyze this repo” -> `discovery only`
- “Generate OpenSpec for this repo” -> discovery first, then propose extraction order unless the user explicitly names a domain
- “Generate spec for <domain>” -> `domain spec`
- “Generate flow for <domain>” -> `event flow`
- “Generate design notes for <domain>” -> `design notes`
- “Summarize this repo for OpenSpec” -> `repository summary`

If the requested domain name is not yet established, discover the domain map first instead of guessing.

## Output Discipline

- State what was confirmed versus what was left out.
- Surface boundary breaks explicitly.
- Preserve messy reality when the codebase is messy.
- Use concise language and domain terms.
- Do not hide ambiguity behind polished prose.

## Resources

Read only what is needed:

- [references/phased-workflow.md](./references/phased-workflow.md)
  Use for discovery order, extraction sequence, and checkpoint behavior.
- [references/evidence-rubric.md](./references/evidence-rubric.md)
  Use when deciding whether a statement qualifies for `spec.md`.
- [references/artifact-templates.md](./references/artifact-templates.md)
  Use for canonical artifact shapes and writing checklists.
