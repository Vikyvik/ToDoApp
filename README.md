# todo-backend

A minimal Todo backend built with Java 17, Spring Boot 3.3.x, H2 in-memory DB, Spring Data JPA, and Bean Validation.  
Implements full CRUD for tasks, safe error handling, CORS for local dev, and includes automated and usage tests.

## Features

- **CRUD API** under `/api/tasks`:
  - `GET /api/tasks` — List all tasks
  - `GET /api/tasks/{id}` — Get single task
  - `POST /api/tasks` — Create a task (title required, description optional)
  - `PATCH /api/tasks/{id}` — Update any of title/description/done
  - `DELETE /api/tasks/{id}` — Delete a task
- **Entity fields**: id (Long), title (required), description (optional), done (default `false`), createdAt (auto, UTC)
- **Validation**: Returns 400 if required fields are missing/invalid, 404 for not found, 500 for safe generic errors
- **H2 in-memory database** for development/testing — zero setup, launches with API
- **CORS**: Allows requests from `http://localhost:3000` (for local frontend dev)
- **Open Source**: Only uses OSS Java, Spring Boot, and H2 libraries

## Getting Started

### Prerequisites

- Java 17+ (or 21) JDK:
    - [Adoptium / Eclipse Temurin](https://adoptium.net)
    - [OpenJDK](https://jdk.java.net)
- Maven 3.9+

### Quick Setup

1. **Install Java JDK 17 or 21**  
   Download and install a JDK (e.g. Temurin, OpenJDK).
2. **Set JAVA_HOME and PATH**
   - macOS/Linux:
     ```sh
     export JAVA_HOME=<jdk_path>
     export PATH="$JAVA_HOME/bin:$PATH"
     ```
   - Windows:
     Set JAVA_HOME under System Environment Variables  
     Add `%JAVA_HOME%\bin` to your PATH.
3. **Verify installation:**
   ```sh
   java -version
   javac -version
   ```
4. **Install Maven** if not already installed:
   [Maven installation guide](https://maven.apache.org/install.html)

### Run locally

```sh
cd todo-backend
mvn spring-boot:run
```

API is at: http://localhost:8080/api/tasks

### Run tests

```sh
mvn test
```

## Usage Examples (curl)

Create a new task:
```sh
curl -X POST http://localhost:8080/api/tasks -H 'Content-Type: application/json' -d '{"title":"Buy milk"}'
```

List all tasks:
```sh
curl http://localhost:8080/api/tasks
```

Get task by id:
```sh
curl http://localhost:8080/api/tasks/1
```

Update title/done (PATCH):
```sh
curl -X PATCH http://localhost:8080/api/tasks/1 -H 'Content-Type: application/json' -d '{"title":"Buy bread","done":true}'
```

Delete task:
```sh
curl -X DELETE http://localhost:8080/api/tasks/1
```

## H2 Console

Launch dev DB web console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console).  
JDBC URL: `jdbc:h2:mem:todo-db` (user: `sa`, blank password)

## Notes & Security

- Follows Spring best practices; **never logs PII**. All errors have safe messages.
- Validation and exception handling ensure stable and predictable REST API.
- For prod use, switch to persistent DB and review all config for security hardening.

---
© OSS, MIT/Apache licensed components only.
