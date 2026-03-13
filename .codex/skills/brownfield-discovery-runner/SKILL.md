---
name: brownfield-discovery-runner
description: Safely orchestrate brownfield discovery using the OpenSpec `brownfield-discovery` schema. Executes the OpenSpec CLI, materializes artifacts, and enforces review checkpoints to protect source-of-truth integrity.
---

# Brownfield Discovery Runner

Operate the brownfield discovery workflow one phase at a time.

This skill acts as a **procedural runner** for the OpenSpec brownfield workflow.

Responsibilities:

- execute OpenSpec CLI commands
- materialize artifacts to disk
- enforce review checkpoints
- protect epistemic truth
- prevent unsafe automation

Assume the repository already contains:

- OpenSpec schema `brownfield-discovery`
- companion skill `brownfield-openspec-extractor`

Delegate reverse-engineering analysis quality to that companion skill.

This skill focuses on **workflow orchestration and safety**.

---

# Core Principles

Always protect epistemic truth.

`spec.md` is the authoritative source of truth.

Supporting artifacts:

- `event-flow.md`
- `design.md`

must **never redefine system behavior**.

Prefer **truth extraction over speed**.

Stop at review checkpoints before advancing.

Never archive automatically.

---

# Execution Rule

This skill has shell access inside the repository workspace.

When a phase requires an OpenSpec CLI command:

- execute the command directly
- capture command output
- interpret the generated instructions
- materialize the artifact
- write it to disk
- verify the file exists
- present the review checkpoint

Do not require the user to manually run commands unless shell execution fails.

---

# Artifact Materialization Rule

`openspec instructions` only generates **instructions**, not artifacts.

After running:

```

openspec instructions <artifact> --change <change-name>

```

you must:

1. capture the generated instructions
2. interpret them
3. generate the artifact content
4. write the artifact file to disk
5. verify the file exists
6. summarize the artifact
7. only then present the review checkpoint

Never report an artifact as generated if only instructions were produced.

Always distinguish:

- instructions generated
- artifact materialized

---

# File Verification Rule

After writing any artifact:

1. confirm the file exists
2. read the file from disk
3. summarize the actual contents

Never rely only on command output.

---

# Operating Pattern

For each phase:

1. Execute the OpenSpec CLI command.
2. Capture the instructions.
3. Generate the artifact content.
4. Write it to disk.
5. Verify the file exists.
6. Summarize the result.
7. Present the review checklist.
8. Pause for user confirmation.

Never skip review checkpoints.

---

# Change Naming

If the user does not specify a change name, default to:

```

brownfield-bootstrap

```

Other acceptable names:

- `brownfield-discovery`
- `brownfield-initial-specs`

---

# Phase Detection

Map user requests to phases:

| User intent | Phase |
|---|---|
Start brownfield discovery | Phase 1 |
Guide me through brownfield discovery | Phase 1 |
Continue from domain-map | Phase 2 |
Continue from reviewed spec | Phase 4 |
Generate event flow | Phase 4 |
Finish bootstrap | resume next phase |

If uncertain, ask one short clarification question.

---

# Phase 1 — Initialize Brownfield Change

Check if change exists.

If not, execute:

```

openspec new change brownfield-bootstrap --schema brownfield-discovery

```

Confirm change workspace creation.

Then execute:

```

openspec instructions domain-map --change brownfield-bootstrap

```

Capture the generated instructions.

---

## Artifact Materialization

Using those instructions:

Generate the artifact:

```

openspec/changes/brownfield-bootstrap/domain-map.md

```

Populate it using `brownfield-openspec-extractor`.

Analyze the repository and extract:

- system domains
- actors
- local flows
- repo boundaries

Write the artifact to disk.

Verify the file exists.

Summarize the domain map.

---

# Phase 2 — Review Domain Map

Artifact:

```

domain-map.md

```

Validate domain boundaries before generating specs.

## Domain Map Review Checklist

- Does each domain represent a real system capability?
- Does each domain name sound like domain language rather than implementation language?
- Is there a clear actor for each domain?
- Are different narrative flows incorrectly mixed together?
- Are repo boundaries and external dependencies clearly marked?
- Are any domains weakly supported by evidence?
- Are the main local flows of the repository covered?

If problems appear:

revise the domain map before continuing.

Pause until confirmation:

```

domain-map reviewed

```

---

# Phase 3 — Generate Spec

Execute:

```

openspec instructions spec --change <change-name>

```

Capture the instructions.

---

## Artifact Materialization

Generate:

```

openspec/changes/<change-name>/spec.md

```

Important rule:

Generate **only one domain first**.

Prefer the clearest domain from the domain map.

Write the file.

Verify it exists.

Summarize the spec.

---

## Spec Review Checklist

- Does the spec describe externally observable behavior?
- Is each requirement supported by implementation evidence?
- Is the language domain-oriented rather than implementation-oriented?
- Are scenarios realistic and not idealized?
- Are inferred behaviors excluded from authoritative requirements?
- Does the spec avoid silently assuming behavior outside this repository?
- Is the file clean and OpenSpec-compatible?

Pause until confirmation:

```

spec reviewed

```

Reminder:

`spec.md` is the authoritative source of truth.

---

# Phase 4 — Generate Event Flow

Execute:

```

openspec instructions event-flow --change <change-name>

```

Capture the instructions.

---

## Artifact Materialization

Generate:

```

openspec/changes/<change-name>/event-flow.md

```

Scope rule:

Generate event flow **only for the reviewed domain**.

Write the file.

Verify it exists.

Summarize the event flow.

---

## Event Flow Review Checklist

- Does each command correspond to a real user action?
- Does each event represent a real state transition?
- Is each view externally visible behavior?
- Are invented events avoided?
- Are flows classified correctly (local complete / partial / boundary)?
- Are cross-repository boundaries visible?
- Does Mermaid add real explanatory value?

Pause until confirmation:

```

event-flow reviewed

```

Reminder:

`event-flow.md` supports `spec.md` and must not redefine behavior.

---

# Phase 5 — Generate Design

Execute:

```

openspec instructions design --change <change-name>

```

Capture the instructions.

---

## Artifact Materialization

Generate:

```

openspec/changes/<change-name>/design.md

```

Write the file.

Verify it exists.

Summarize the design.

---

## Design Review Checklist

- Does it remain technical rather than normative?
- Does it avoid redefining business behavior?
- Are component boundaries described clearly?
- Are implementation details separated from system behavior?

Pause until confirmation:

```

design reviewed

```

---

# Phase 6 — Generate Repository Summary

Execute:

```

openspec instructions summary --change <change-name>

```

Capture instructions.

---

## Artifact Materialization

Generate:

```

openspec/changes/<change-name>/summary.md

```

Write the file.

Verify it exists.

Summarize the repository.

Pause until confirmation:

```

summary reviewed

```

---

# Phase 7 — Archive (Optional)

Only suggest this step after explicit confirmation.

Execute:

```

openspec archive <change-name>

```

Explain:

This promotes the reviewed specs into canonical OpenSpec specs.

Never archive automatically.

---

# Artifact Safety Rule

Before generating artifacts:

Check whether the artifact already exists.

If it exists:

- do not overwrite automatically
- ask whether to regenerate
- allow safe workflow resumption

---

# Revision Rules

Suggest revision instead of continuing when:

- domain names reflect technical components rather than capabilities
- domains mix unrelated narratives
- spec contains inferred behavior
- spec includes implementation details as requirements
- event flow invents commands or events
- event flow hides system boundaries
- design becomes normative

Allow continuation when:

- artifacts satisfy the checklist
- user confirms review
- next artifact stays within same domain.

---

# Response Format

Prefer small procedural blocks.

Example:

```

Executing Phase 3 — Generate Spec

Running:
openspec instructions spec --change brownfield-bootstrap

Artifact generated:
openspec/changes/brownfield-bootstrap/spec.md

Summary: <short explanation>

Review checklist:

* ...
* ...

Reply when ready:
spec reviewed

```

Tone:

procedural  
concise  
truth-preserving
```

