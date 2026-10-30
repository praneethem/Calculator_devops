# Calculator REST API — CI/CD Pipeline

An end-to-end CI/CD mini-project that builds, tests, containerizes, and deploys a Calculator REST API using **Java 21, Spring Boot, Maven, Jenkins, Docker, Docker Compose, Nginx, and GitHub**.

**GitHub Repository:** https://github.com/praneethem/Calculator_devops  
**Docker Hub:** https://hub.docker.com/r/praneethem/calculator-api

## 1. Application Overview

The Calculator REST API supports four arithmetic operations:

| Operation | Endpoint |
|---|---|
| Addition | `GET /api/v1/add?a=10&b=20` |
| Subtraction | `GET /api/v1/subtract?a=10&b=5` |
| Multiplication | `GET /api/v1/multiply?a=10&b=5` |
| Division | `GET /api/v1/divide?a=10&b=5` |
| Health Check | `GET /actuator/health` |

The API accepts numeric query parameters and returns JSON responses.

Invalid or missing parameters and division by zero return HTTP `400 Bad Request` with a structured JSON error response.

### Example API Request

```bash
curl "http://localhost:8080/api/v1/add?a=10&b=20"
```

**Example response:**

```json
{
  "result": 30.0,
  "operation": "add",
  "a": 10.0,
  "b": 20.0
}
```

## 2. Build and Test

### Prerequisites

- Java 21
- Git
- Docker Engine
- Docker Compose v2
- Maven Wrapper (included in the repository)

### Clone the Repository

```bash
git clone https://github.com/praneethem/Calculator_devops.git
cd Calculator_devops
```

### Build the Application

```bash
./mvnw -B clean package
```

### Run Tests

```bash
./mvnw -B test
```

The test suite includes JUnit 5 unit tests for arithmetic operations and error cases, along with Spring MVC integration tests using MockMvc.

Maven generates JUnit XML test reports under:

```text
target/surefire-reports/
```

These reports are published by Jenkins.

## 3. Run the Application Locally

Start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The application runs on port `8080` by default.

Test the API:

```bash
curl "http://localhost:8080/api/v1/add?a=10&b=20"
```

Check application health:

```bash
curl -i http://localhost:8080/actuator/health
```

If port `8080` is occupied, start the application on another port:

```bash
SERVER_PORT=8081 ./mvnw spring-boot:run
```

## 4. Run with Docker

Build the Docker image:

```bash
docker build -t calculator-api:local .
```

Run the container:

```bash
docker run --rm -p 8082:8080 calculator-api:local
```

Call the API:

```bash
curl "http://localhost:8082/api/v1/add?a=10&b=20"
```

The Dockerfile uses a multi-stage build with Maven/JDK for building and a slim JRE runtime image. The application runs as a non-root user.

## 5. Deploy with Docker Compose

The Docker Compose deployment contains two services:

- **Calculator API:** Spring Boot application running internally on port `8080`.
- **Nginx:** Reverse proxy that exposes the API on host port `8088`.

Start the complete stack:

```bash
docker compose up -d
```

Check running containers:

```bash
docker compose ps
```

Call the API through Nginx:

```bash
curl "http://localhost:8088/api/v1/add?a=10&b=20"
```

Verify application health:

```bash
curl -i http://localhost:8088/actuator/health
```

Stop the deployment:

```bash
docker compose down
```

The Compose configuration includes a bridge network, health checks, restart policies, and environment configuration.

## 6. CI/CD Pipeline

The project uses a declarative Jenkins pipeline defined in the `Jenkinsfile`.

### Pipeline Workflow

| Stage | Description |
|---|---|
| Checkout | Checks out the latest source code from GitHub. |
| Build | Builds and packages the Spring Boot application using Maven. |
| Test | Runs automated tests and publishes JUnit reports. |
| Docker Build | Builds the Docker image using the Jenkins build number as its tag. |
| Docker Push | Publishes the image to Docker Hub using Jenkins-managed credentials. |
| Deploy Compose | Deploys the application using Docker Compose. |
| Health Check | Verifies the deployed application's health. |

### Automatic Triggers

The Jenkins job supports:

- **GitHub Webhook:** Automatically triggers the pipeline when a push event occurs in the repository.
- **Poll SCM:** Periodically checks GitHub for changes as a fallback trigger.

The webhook integration was verified using an actual GitHub push, which successfully triggered a Jenkins build.

### Email Notifications

Jenkins sends email notifications for successful and failed builds using configured SMTP and Extended Email Notification settings.

Docker Hub credentials are stored securely in the Jenkins Credentials store and are not hardcoded in the pipeline.

## 7. Docker Hub

The application Docker image is published to:

**https://hub.docker.com/r/praneethem/calculator-api**

The Jenkins pipeline publishes images tagged with the corresponding build number.

To pull a published image:

```bash
docker pull praneethem/calculator-api:<build-number>
```

Replace `<build-number>` with an available image tag.

## 8. Project Structure

```text
Calculator_devops/
├── .mvn/
├── src/
│   ├── main/
│   └── test/
├── nginx/
│   └── default.conf
├── screenshots/
├── .dockerignore
├── .gitattributes
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── mvnw
├── mvnw.cmd
├── pom.xml
├── HELP.md
├── README.md
└── IMT2023555_Praneeth_Marudi.pdf
```

## Author

**Praneeth Marudi**  
