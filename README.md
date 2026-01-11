# GitHub Proxy

A lightweight proxy application exposing a single endpoint to retrieve non-fork GitHub repositories for a given user.  
The application uses the official GitHub REST API v3 as the backing source.

---

## Endpoint

### `GET /users/{username}/repositories`

Returns repositories that are **not forks**, including:

- Repository name  
- Owner login  
- Branches with:
  - Branch name
  - Last commit SHA

---

## Example response

```json
[
  {
    "repositoryName": "example-repo",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123def456"
      }
    ]
  }
]
```

---

## Error handling

For a non-existing GitHub user the API returns:

```json
{
  "status": 404,
  "message": "User 'missing' not found"
}
```

## Tests

The project includes integration tests using WireMock to emulate GitHub API responses.
Tests validate:

- Returning only non-fork repositories  
- Mapping branches and commit SHA
- Handling 404 for missing users

### Run tests:

```bash
./gradlew test
```

---

## Running the application:

Requirements:
- Java 25
- Gradle

Launch the application:

```bash
./gradlew bootRun
```

---

## Technologies:

- Java 25
- Spring Boot 4.0.1
- Spring Web MVC
- Spring RestClient
- WireMock for integration tests
