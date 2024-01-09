# Spring Project Manager Deployment - WIP

### Project description

The application allows users to create local accounts or sign in via OAuth 2.0.
Authenticated users can join companies, create, and manage projects. Projects include tasks with
start dates, deadlines, expenses, and varying levels of importance and difficulty. Employees request
to be assigned to projects and receive rewards for completed tasks, incorporating a gamification
aspect with a scoring system.
Tech Stack: Java Spring MVC, Spring Security, PostgreSQL, React JS

### Deployment

The Dockerfiles for the different modules use multiple stage builds. The built Docker images are
collected by a Docker Compose configuration. The database is also built here from official
PostgreSQL docker image.
The project uses Nginx as reverse-proxy and static file server.

### Setup & build

- Copy all `*_env.txt` files, rename to `*.env`, modify values
- Run `docker-compose.yml`

### Coming soon

- Frontend refactoring
- Service level unit tests, integration tests
- Git workflow, CI / CD
- E-mail security verifications and notifications to users
