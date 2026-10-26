# Calculator REST API — CI/CD Pipeline

An end-to-end DevOps mini-project that automates the building, testing, containerization, and deployment of a Calculator REST API using **Java, Spring Boot, Maven, Jenkins, Docker, Docker Compose, Nginx, and GitHub**.

**GitHub:** [Calculator_devops](https://github.com/praneethem/Calculator_devops)  
**Docker Hub:** [calculator-api](https://hub.docker.com/r/praneethem/calculator-api)

## Architecture

```text
Developer → GitHub → Jenkins Pipeline
                         |
                   Build & Test
                         |
                    Docker Build
                         |
                     Docker Hub
                         |
                  Docker Compose
                    /         \
                  Nginx    Calculator API
                 :8088         :8080
                         |
                    Health Check
                         |
                  Email Notification
```

## Technology Stack

- **Backend:** Java 21, Spring Boot
- **Build and Testing:** Maven, JUnit 5, MockMvc
- **Version Control:** Git, GitHub
- **CI/CD:** Jenkins
- **Containerization:** Docker
- **Deployment:** Docker Compose, Nginx
- **Health Checks:** Spring Boot Actuator

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/add?a=10&b=20` | Addition |
| GET | `/api/v1/subtract?a=10&b=5` | Subtraction |
| GET | `/api/v1/multiply?a=10&b=5` | Multiplication |
| GET | `/api/v1/divide?a=10&b=5` | Division |
| GET | `/actuator/health` | Application health |

### Example: Addition

```bash
curl "http://localhost:8088/api/v1/add?a=10&b=20"
```

Response:

```json
{
  "result": 30.0,
  "operation": "add",
  "a": 10.0,
  "b": 20.0
}
```

Division by zero and invalid inputs return HTTP `400 Bad Request` with a structured JSON error response.

## Getting Started

### Prerequisites

- Java 21
- Git
- Docker Engine
- Docker Compose v2

### Clone the Repository

```bash
git clone https://github.com/praneethem/Calculator_devops.git
cd Calculator_devops
```

### Build and Test

Build the application and run the tests:

```bash
./mvnw -B clean package
```

Run the tests separately:

```bash
./mvnw -B test
```

The test suite covers arithmetic operations, invalid inputs, missing parameters, and division by zero. Unit tests and Spring MVC integration tests are included.

### Run Locally

Start the application:

```bash
./mvnw spring-boot:run
```

The application uses port `8080` by default. If that port is occupied, start it on port `8081`:

```bash
SERVER_PORT=8081 ./mvnw spring-boot:run
```

Example request for the default port:

```bash
curl "http://localhost:8080/api/v1/add?a=10&b=20"
```

### Run with Docker

Build the application image:

```bash
docker build -t calculator-api:local .
```

Run the standalone container:

```bash
docker run --rm -p 8082:8080 calculator-api:local
```

Test the container:

```bash
curl "http://localhost:8082/api/v1/add?a=10&b=20"
```

## Docker Compose Deployment

The deployment includes the Calculator API and Nginx. The application is accessible through Nginx on port `8088`.

Start the stack:

```bash
docker compose up -d
```

Check the running services:

```bash
docker compose ps
```

Check application health:

```bash
curl -i "http://localhost:8088/actuator/health"
```

Test the API through Nginx:

```bash
curl "http://localhost:8088/api/v1/add?a=10&b=20"
```

Stop the stack:

```bash
docker compose down
```

The Compose configuration includes a bridge network, an application health check, restart policies, and environment configuration.

## CI/CD Pipeline

The declarative Jenkins pipeline is defined in `Jenkinsfile` and configured for the `main` branch.

| Stage | Description |
|---|---|
| Checkout | Retrieves source code from GitHub |
| Build | Compiles and packages the application using Maven |
| Test | Runs automated tests and publishes JUnit results |
| Docker Build | Builds the application image |
| Docker Push | Pushes the image to Docker Hub |
| Deploy Compose | Deploys the application using Docker Compose |
| Health Check | Verifies application health |
| Notifications | Sends success or failure email notifications |

**Trigger:** The Jenkins job is configured to poll the GitHub repository approximately every two minutes.

Docker Hub credentials are stored in Jenkins Credentials. Email notifications use the configured SMTP settings.

## Docker Hub

The application image is published to:

**[praneethem/calculator-api](https://hub.docker.com/r/praneethem/calculator-api)**

The Jenkins pipeline tags Docker images with the corresponding build number.

## Project Structure

```text
Calculator_devops/
├── src/
│   ├── main/
│   └── test/
├── nginx/
│   └── default.conf
├── .dockerignore
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Security

- Store credentials in Jenkins Credentials rather than source code.
- Do not commit passwords, access tokens, or other secrets.
- The application container runs as a non-root user.

---

**Author:** Praneeth Marudi

