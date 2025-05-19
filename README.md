**Spring Security**

A step‑by‑step, production‑ready starter for **Spring Boot 3 + Spring Security 6** that demonstrates how to build a modern, stateless authentication and authorisation layer with JWT, e‑mail verification and secure cookie handling.  

---

**Key Features**

| Area |  |
|------|------------------|
| **User lifecycle** | Sign‑up, e‑mail confirmation, login, resend confirmation, change password |
| **Authentication** | Username + password, stateless JWT, HttpOnly/Secure cookies, login‑attempt throttling |
| **Authorisation** | Role & authority model (`ROLE_ADMIN`, `ROLE_USER`, …), method‑level security, `@PreAuthorize` examples |
| **Domain auditing** | Auditable base entity with `createdAt`, `createdBy`, `lastModifiedAt`, `lastModifiedBy` |
| **Exception handling** | Consistent JSON error envelopes with i18n‑ready error codes |
| **Caching** | In‑memory user cache with Spring Cache abstraction |
| **Persistence** | PostgreSQL, JPA + Hibernate, Flyway migrations |
| **Testing** | JUnit 5 with Testcontainers for PostgreSQL |
| **Packaging & Ops** | Multi‑stage Dockerfile, `.env` support, ready for Docker Compose/Kubernetes |

---

**Technologies Used**

* **Java 21**, **Spring Boot 3.2.x**, **Spring Security 6**  
* **PostgreSQL**, **Flyway**, **Hibernate/JPA**  
* **Maven**, **Lombok**, **MapStruct**  
* **JWT (jjwt‑api)**, **Jakarta Validation**  
* **Docker**, **Docker Compose**  
* 99 % Java and a dash of PL/pgSQL for database functions :contentReference[oaicite:0]{index=0}

---

**Getting Started**

**Prerequisites**

| Tool | Version (or later) |
|------|--------------------|
| JDK 21 | `java ‑version` |
| Maven 3.9 | `mvn ‑v` |
| Docker + Docker Compose | for local containers |
| (Optional) Postman or curl | for API calls |

**Clone & Run with Docker Compose**

```bash
git clone https://github.com/abdulalimswe/SpringSecurity.git
cd SpringSecurity

# copy environment variables template and adjust as needed
cp docker/.env.example .env

# build the image & start PostgreSQL + the API
docker compose up --build

