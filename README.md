# Bajaj Finserv Hiring Round 1 — SQL Query Auto-Submission

A **Spring Boot application** built for the Bajaj Finserv Round 1 hiring challenge. The app automatically fires off SQL query submissions to Bajaj's external API the moment it starts up — no manual triggering required.

---

## What This Project Is About

Bajaj Finserv's hiring process begins with a pre-placement programming challenge. Candidates receive an API endpoint and are required to write a program that:

1. **Hits Bajaj's API** with a structured request payload
2. **Submits SQL-related answers** (queries or query results) as part of that payload
3. **Does this automatically on application startup** — not when a user calls an endpoint, but as soon as the Spring Boot app boots up

This repository is the implementation of that Round 1 challenge. The entire submission logic lives inside a startup hook, so running the app IS the submission.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Core language |
| Spring Boot | 3.5.0 | Application framework |
| Spring Web | (included) | HTTP client (RestTemplate) for calling Bajaj's API |
| Maven | Wrapper included | Build tool |

> No database dependency is declared — the SQL queries are constructed in code and submitted directly to the external API, not executed against a local DB.

---

## How It Works

The flow is entirely automatic once the app starts:

```
Application Starts
       │
       ▼
Startup Hook Runs (ApplicationRunner / CommandLineRunner)
       │
       ▼
Build the Request Payload
  - Your name, email, registration number
  - SQL query / query answer
       │
       ▼
POST to Bajaj's API Endpoint
       │
       ▼
Response Logged / Success Confirmed
```

### Why a Startup Hook?

Spring Boot provides a `CommandLineRunner` or `ApplicationRunner` interface that lets you execute code immediately after the application context is fully loaded. This project uses one of these to ensure the API call is made the instant the app runs — perfect for a one-shot challenge submission.

---

## Project Structure

```
Krish-Kumar-Bajaj-round-1/
│
├── src/
│   └── main/
│       └── java/
│           └── com/bajaj/bajajqualifier/
│               ├── BajajQualifierApplication.java   ← Main entry point (@SpringBootApplication)
│               └── (Startup runner / service class)  ← Where the API call logic lives
│
├── .mvn/wrapper/
│   └── maven-wrapper.properties                     ← Pins the Maven version used
│
├── mvnw                                             ← Maven wrapper script (Linux/Mac)
├── mvnw.cmd                                         ← Maven wrapper script (Windows)
└── pom.xml                                          ← Dependencies and build config
```

### Key Files Explained

**`BajajQualifierApplication.java`**
The standard Spring Boot main class annotated with `@SpringBootApplication`. This is where the JVM enters the program.

**Startup Runner (service/runner class)**
This is the heart of the project. It implements `ApplicationRunner` or `CommandLineRunner` (or uses `@EventListener(ApplicationReadyEvent.class)`) and contains:
- Construction of the JSON request body (your details + SQL query/answer)
- A `RestTemplate` HTTP POST call to Bajaj's API endpoint
- Response handling and logging

**`pom.xml`**
Declares two dependencies:
- `spring-boot-starter-web` — provides RestTemplate and embedded Tomcat
- `spring-boot-starter-test` — for unit tests (test scope only)

---

## Prerequisites

Before running this project, make sure you have the following installed:

- **Java 17** or higher — [Download here](https://adoptium.net/)
- **Git** — to clone the repository
- No Maven installation needed — the project ships with a Maven wrapper (`mvnw`)

To verify Java is set up:
```bash
java -version
# Should print something like: openjdk version "17.x.x"
```

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/krisjscott/Krish-Kumar-Bajaj-round-1.git
cd Krish-Kumar-Bajaj-round-1
```

### 2. Build the Project

Use the included Maven wrapper — no separate Maven installation needed:

```bash
# Linux / macOS
./mvnw clean install

# Windows
mvnw.cmd clean install
```

This downloads all dependencies and compiles the code. A `target/` folder will be created with the compiled `.jar`.

### 3. Run the Application

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

**Or run the compiled JAR directly:**

```bash
java -jar target/bajaj-qualifier-0.0.1-SNAPSHOT.jar
```

As soon as the application starts, the startup hook will fire and automatically submit the SQL query to Bajaj's API. Watch the console output for the API response.

---

## What Happens on Startup

When you run the app, Spring Boot will:

1. Load the application context
2. Trigger the startup runner
3. Send a POST request to Bajaj's API with the challenge payload
4. Print the API response to the console

You should see a success/failure message in the logs almost immediately after the Spring Boot banner appears. Look for log output around lines like:

```
Started BajajQualifierApplication in X.XXX seconds
>> API Response: { ... }
```

---

## Challenge Context

This project was built for **Bajaj Finserv's off-campus / on-campus hiring drive**. Round 1 is an API challenge where candidates must demonstrate they can:

- Work with REST APIs programmatically
- Construct and send structured JSON payloads
- Handle HTTP responses
- Integrate all of this inside a working Java/Spring Boot application

Successfully completing this round (getting a success response from the API) qualifies candidates for the subsequent HackerEarth coding test (Round 2).

---

## Dependencies (from pom.xml)

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>3.5.0</version>
</parent>

<dependencies>
  <!-- Web + RestTemplate for making HTTP calls to Bajaj's API -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>

  <!-- Testing support -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
</dependencies>
```

---

## Notes

- The app is designed as a **one-shot submission tool** — it runs, submits, and you're done.
- Since the submission includes personal identifiers (name, email, registration number), the credentials in the code are specific to the author and won't produce a valid submission if run by someone else.
- The `target/` directory is committed in this repo (which is generally not best practice, but common in quick challenge submissions).

---

## Author

**Krish Kumar** — Built as part of the Bajaj Finserv hiring challenge (Round 1).

GitHub: [@krisjscott](https://github.com/krisjscott)
