# Agent Constitution Summary

Generated on 2026-03-13 from README and code analysis.

## Outcome

- Created a new root `AGENTS.md` because no project-local AGENTS file was present in the workspace.
- Did not create subdirectory AGENTS files because this repository is a single Spring Boot application rather than a split frontend/backend repo.

## Source Inputs

- `README.md`
- `pom.xml`
- `application*.yml`
- MVC, REST, service, domain, and test packages
- `.github/workflows/maven.yml`
- `Dockerfile`
- `docker-compose.yml`

## Principles Captured In AGENTS.md

- Treat the app as a server-rendered Spring MVC monolith first.
- Keep controllers thin and logic in services/modules.
- Respect the existing package structure and security model.
- Preserve ArchUnit-enforced conventions where possible.
- Prefer tests that match the touched layer.
- Document security-sensitive behavior when changing auth, downloads, or persistence.

## Known Constraints Added

- Default profile is `dev`.
- Seed data is auto-created in `dev`, `test`, and `docker`.
- Attachments use local filesystem storage.
- Build quality stack is extensive and may slow feedback loops.
- CI/workflow definitions need modernization.
