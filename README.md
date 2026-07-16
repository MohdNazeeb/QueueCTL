# QueueCTL

A CLI-based Background Job Queue System built in Java that supports asynchronous job processing, retries, multiple workers, persistence, and a Dead Letter Queue (DLQ).

---

## Features

- Background job processing
- Multi-worker execution
- SQLite persistent storage
- Retry mechanism with exponential backoff
- Dead Letter Queue (DLQ)
- Job status tracking
- CLI using Picocli
- JSON-based job enqueueing

---

## Tech Stack

- Java 21
- Maven
- SQLite
- JDBC
- Picocli
- Jackson

---

## Project Structure

```
QueueCTL
│
├── cli/
├── config/
├── model/
├── repository/
├── worker/
├── util/
│
├── queue.db
├── pom.xml
└── README.md
```

---

## Build

```bash
mvn clean package
```

---

## Run

```bash
java -jar target/queuectl-1.0-SNAPSHOT.jar --help
```

---

## Available Commands

### Enqueue Job

```bash
queuectl enqueue "{\"id\":\"job1\",\"command\":\"echo Hello\"}"
```

---

### List Jobs

```bash
queuectl list
```

Filter by state:

```bash
queuectl list --state DEAD
```

---

### Queue Status

```bash
queuectl status
```

---

### Start Workers

```bash
queuectl worker start --count 2
```

---

### Dead Letter Queue

List dead jobs:

```bash
queuectl dlq list
```

Retry a dead job:

```bash
queuectl dlq retry <jobId>
```

---

## Job Lifecycle

```
PENDING
   │
   ▼
PROCESSING
   │
   ├──────────────► COMPLETED
   │
   ▼
FAILED
   │
Retry
   │
Max Retries Reached
   │
   ▼
DEAD
   │
dlq retry
   │
   ▼
PENDING
```

---

## Example Workflow

### Enqueue a Job

```bash
java -jar target/queuectl-1.0-SNAPSHOT.jar enqueue "{\"id\":\"job1\",\"command\":\"echo Hello\"}"
```

### Start Workers

```bash
java -jar target/queuectl-1.0-SNAPSHOT.jar worker start --count 2
```

### View Queue

```bash
java -jar target/queuectl-1.0-SNAPSHOT.jar list
```

### View Queue Status

```bash
java -jar target/queuectl-1.0-SNAPSHOT.jar status
```

### View Dead Letter Queue

```bash
java -jar target/queuectl-1.0-SNAPSHOT.jar dlq list
```

### Retry Dead Job

```bash
java -jar target/queuectl-1.0-SNAPSHOT.jar dlq retry fail1
```
## Key Concepts

- Atomic job claiming to prevent duplicate execution
- Concurrent worker support
- SQLite persistence
- Retry with exponential backoff
- Dead Letter Queue handling

---

## Author

**Mohd Nazeeb Mansoori**