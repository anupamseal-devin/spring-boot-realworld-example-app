# Testing the Spring Boot RealWorld API

## Prerequisites
- Java 11 (`export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64`)
- The app uses SQLite (file-based `dev.db`), no external database setup needed

## Running the App Locally
```bash
cd ~/repos/spring-boot-realworld-example-app
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
./gradlew bootRun
```
App starts on http://localhost:8080. Verify with `curl http://localhost:8080/tags`.

To start fresh, delete `dev.db` before running — Flyway will recreate it from migrations.

## Running Tests
```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
./gradlew clean test
```
Tests use an in-memory SQLite database (`application-test.properties` overrides the datasource URL to `jdbc:sqlite::memory:`).

## Running Lint / Format
```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
./gradlew spotlessJavaApply compileJava
```
Spotless may auto-format files. If the commit hook rejects due to formatting, re-stage the formatted files and retry.

## API Testing Patterns

### Authentication
The app uses JWT tokens. Register a user first, then use the returned token:
```bash
# Register
curl -s -X POST http://localhost:8080/users \
  -H 'Content-Type: application/json' \
  -d '{"user":{"email":"test@test.com","username":"testuser","password":"password123"}}'

# Extract token from response and use as:
# -H "Authorization: Token <jwt_token>"
```

### Public Endpoints (no auth needed)
- `GET /tags` — list all tags
- `GET /articles` — list articles
- `GET /articles/{slug}` — get single article
- `GET /articles/{slug}/comments` — get comments
- `GET /profiles/{username}` — get profile

### Authenticated Endpoints
- `POST /articles` — create article (requires `{"article":{...}}` wrapper)
- `PUT /articles/{slug}` — update article
- `DELETE /articles/{slug}` — delete article
- `POST /articles/{slug}/comments` — create comment
- `PUT /user` — update current user profile
- `GET /user` — get current user

### Important: Jackson UNWRAP_ROOT_VALUE
All request bodies must use root-value wrapping: `{"user":{...}}`, `{"article":{...}}`, `{"comment":{...}}`.

## Verifying Flyway Migrations
Check app startup logs for migration status. To verify database schema directly:
```python
python3 -c "import sqlite3; conn = sqlite3.connect('dev.db'); cursor = conn.execute(\"SELECT name FROM sqlite_master WHERE type='index' ORDER BY name;\"); [print(row[0]) for row in cursor]; conn.close()"
```
Note: `sqlite3` CLI may not be installed on the machine; use Python's built-in `sqlite3` module instead.

## GraphQL
The app also exposes a GraphQL API at `/graphql` with GraphiQL at `/graphiql`.

## Common Issues
- If the commit hook blocks due to secrets in `application.properties`, the JWT secret is a pre-existing value in the repo. You may need to split commits to separate the properties file.
- Spotless may auto-format test files when running `spotlessJavaApply`. Make sure to stage those formatting changes too.
- The `dev.db` file is created in the project root directory. Delete it for a fresh start.
