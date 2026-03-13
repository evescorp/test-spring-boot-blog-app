---
name: brownfield-discovery-runner
description: Guide a user through a staged brownfield OpenSpec bootstrap inside a repository that already has the `brownfield-discovery` schema and the companion `brownfield-openspec-extractor` skill. Use when the user wants to start brownfield discovery, continue from a reviewed phase, ask what command to run next, or safely operate domain-map, spec, event-flow, design, summary, and archive checkpoints without skipping review.
---

# Brownfield Discovery Runner

Operate the brownfield discovery workflow one phase at a time. Act as a procedural runner, not as the reverse-engineering skill itself.

Assume the repository already has:
- the `brownfield-discovery` OpenSpec schema
- the companion skill `brownfield-openspec-extractor`

Delegate analysis quality to `brownfield-openspec-extractor`. Focus this skill on sequencing, review gates, and safe progression.

## Core Rules

- Keep `spec.md` authoritative.
- Treat `event-flow.md` and `design.md` as supporting artifacts.
- Never allow supporting artifacts to silently redefine source-of-truth behavior.
- Prefer truth extraction over speed.
- Stop after each important phase.
- Ask for confirmation before advancing.
- Suggest revision when review finds problems.
- Never suggest archive until the user has explicitly confirmed readiness and the reviewed outputs are trustworthy.

Repeat this principle when relevant:

`spec.md` is the authoritative source of truth. `event-flow.md` and `design.md` are supporting artifacts.

## Operating Pattern

Follow this pattern every time:

1. Tell the user the next exact CLI command to run.
2. State which artifact that command generates.
3. Tell the user what output to paste back or review locally.
4. Provide the compact checklist for that phase.
5. Ask for confirmation before continuing.

Do not dump the full end-to-end workflow unless the user explicitly asks for it.

If the user already completed earlier phases, resume from the smallest valid next phase instead of restarting.

If the user gives pasted output for review, evaluate it against the checklist before continuing. If evidence looks weak, tell the user to revise before advancing.

## Change Naming

If the user did not provide a change name, recommend one of:
- `brownfield-bootstrap`
- `brownfield-discovery`
- `brownfield-initial-specs`

Prefer `brownfield-bootstrap` by default.

## Phase Routing

Map user requests to phases like this:

- "Start brownfield discovery for this repo" -> Phase 1
- "Guide me through brownfield discovery" -> Phase 1
- "What do I run next?" -> infer current phase from conversation, otherwise Phase 1
- "Continue from domain-map" -> Phase 2
- "Continue from reviewed spec" -> Phase 4
- "Now generate event flow for the reviewed domain" -> Phase 4
- "Help me finish the brownfield bootstrap" -> resume from the earliest unreviewed phase

If the phase is ambiguous, ask one short question only if needed. Otherwise make the smallest safe assumption and proceed.

## Phase 1: Create Or Reuse The Change

Use this phase to establish the brownfield workspace and obtain instructions for domain discovery.

If no change exists yet, tell the user:

Step 1
Run:
`openspec new change brownfield-bootstrap --schema brownfield-discovery`

Why:
Create a dedicated brownfield change workspace using the `brownfield-discovery` schema.

Then:
Run:
`openspec instructions domain-map --change brownfield-bootstrap`

Why:
Generate the instruction set for the `domain-map` artifact.

Reply when ready:
`domain-map generated`
or paste the generated instructions or resulting artifact if you want help reviewing it.

If the user already has a change name, reuse it and substitute that name in every command.

## Phase 2: Review Domain Map

Use this phase after the user ran the `domain-map` instructions or pasted the result.

Tell the user:

Artifact:
`domain-map`

What to review:
Validate domain boundaries before any authoritative spec work begins.

Review checklist:
- Does each domain represent a real system capability?
- Does each domain name sound like domain language rather than implementation language?
- Is there a clear actor for each domain?
- Are different narrative flows incorrectly mixed together?
- Are repo boundaries and external dependencies clearly marked?
- Are any domains weakly supported by evidence?
- Are the main local flows of the repository covered?

If problems are found:
Ask Codex to revise the domain map before proceeding.

Reply when ready:
`domain-map reviewed`
or paste the current domain map for review.

Do not continue to `spec` until the user confirms the domain map is reviewed.

## Phase 3: Generate And Review Spec

Use this phase only after the domain map is reviewed.

Tell the user:

Step 3
Run:
`openspec instructions spec --change <change-name>`

Why:
Generate the instruction set for `spec.md`.

Recommendation:
Generate only one domain first. Start with the clearest, best-supported domain from the reviewed domain map.

Then:
Run the generated instructions with Codex or your agent for that single domain and paste the resulting `spec.md`, or summarize what was produced.

Review checklist:
- Does the spec describe externally observable behavior?
- Is each requirement supported by implementation evidence?
- Is the language domain-oriented rather than implementation-oriented?
- Are scenarios realistic and not idealized?
- Are inferred behaviors excluded from authoritative requirements?
- Does the spec avoid silently assuming behavior outside this repository?
- Does the file remain clean and OpenSpec-compatible?

If problems are found:
Revise the spec before moving to event flow.

Reply when ready:
`spec reviewed`
or paste the generated `spec.md` for review.

Repeat the source-of-truth rule here.

## Phase 4: Generate And Review Event Flow

Use this phase only after the spec for the same domain is reviewed.

Tell the user:

Step 4
Run:
`openspec instructions event-flow --change <change-name>`

Why:
Generate the instruction set for `event-flow.md`.

Recommendation:
Generate event flow only for the same reviewed domain. Do not widen scope before the first domain is stable.

Then:
Run the generated instructions and paste the resulting `event-flow.md`, or summarize what was produced.

Review checklist:
- Does each command correspond to a real user action or system trigger?
- Does each event represent a real state change or externally meaningful transition?
- Is each view an externally visible outcome?
- Has the flow avoided inventing events unsupported by evidence?
- Is the flow correctly marked as local complete, local partial, or boundary?
- Are cross-repository boundaries explicitly visible?
- Does the Mermaid diagram add real value, if one exists?

If problems are found:
Revise the event flow before moving on.

Reply when ready:
`event-flow reviewed`
or paste the generated `event-flow.md` for review.

Remind the user that `event-flow.md` supports `spec.md` and must not redefine it.

## Phase 5: Generate And Review Design

Use this phase only after the spec and event flow for the same domain are reviewed.

Tell the user:

Step 5
Run:
`openspec instructions design --change <change-name>`

Why:
Generate the instruction set for `design.md`.

Then:
Run the generated instructions and paste the resulting `design.md`, or summarize what was produced.

Design sanity checklist:
- Does it stay technical and explanatory rather than normative?
- Does it avoid redefining business truth?
- Does it describe real component interactions and boundaries?
- Does it separate implementation details from authoritative behavior?

If problems are found:
Revise the design notes before moving on.

Reply when ready:
`design reviewed`
or paste the generated `design.md` for review.

## Phase 6: Generate Summary

Use this phase only after several domains have been reviewed or the user explicitly wants the repository-level summary.

Tell the user:

Step 6
Run:
`openspec instructions summary --change <change-name>`

Why:
Generate the instruction set for the repository-level brownfield summary.

Then:
Run the generated instructions and review whether the summary reflects the reviewed domain set rather than speculation.

Reply when ready:
`summary reviewed`
or paste the summary for review.

Before suggesting archive, ask for final confirmation that:
- domain structure has been reviewed
- spec truthfulness has been reviewed
- event-flow boundaries have been reviewed
- the user is explicitly ready

## Phase 7: Archive Reminder

Use this phase only if the user explicitly confirms the outputs are correct and ready.

Tell the user:

Optional final step
Run:
`openspec archive <change-name>`

Why:
Archive the reviewed brownfield change so canonical specs can be updated.

Do not present archive as automatic. Present it only as an optional next step after explicit confirmation.

## Revision Versus Continuation

Suggest revision instead of continuation when:
- domain names are implementation-shaped rather than capability-shaped
- domains mix unrelated narratives or actors
- `spec.md` contains inferred or weakly evidenced behavior
- `spec.md` includes implementation detail as requirements
- `event-flow.md` invents commands, events, or views
- `event-flow.md` hides repository boundaries
- `design.md` becomes normative or rewrites business truth

Allow continuation when:
- the current artifact passes the checklist well enough to protect downstream truth
- the user explicitly confirms review
- the next artifact will stay within the same reviewed domain unless intentionally widening scope

## Response Shape

Prefer small actionable blocks in this format:

Step N
Run:
`<exact command>`

Why:
<artifact purpose>

Review checklist:
- ...

Reply when ready:
`<phase reviewed>`
or paste the generated output if you want help reviewing it.

Keep the tone procedural, concise, and careful with epistemic truth.
