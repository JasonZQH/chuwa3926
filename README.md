# Spring Boot Redbook

A Spring Boot REST API demo for posts, comments, authentication, validation, Swagger, and local HTTPS development.

## Stack

- Java 11+
- Spring Boot 2.7.3
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL / MySQL via Spring profiles
- Swagger (Springfox 3)
- AOP logging

## Features

- CRUD APIs for posts
- CRUD APIs for comments
- User signup and signin with JWT
- Request validation with `@Valid`
- Global exception handling
- Swagger API documentation
- Local HTTPS with a self-signed certificate
- HTTP to HTTPS redirect for local testing

## Project Structure

```text
src/main/java/com/chuwa/redbook
├── aop
├── config
├── controller
├── dao
├── entity
├── exception
├── payload
├── security
├── service
└── util
```

## Database Profiles

This project supports both MySQL and PostgreSQL by using Spring profiles.

- Default profile: `postgres`
- PostgreSQL config: `src/main/resources/application-postgres.properties`
- MySQL config: `src/main/resources/application-mysql.properties`
- Shared config: `src/main/resources/application.properties`

To switch to MySQL:

```properties
spring.profiles.active=mysql
```

Or run with:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Local PostgreSQL Setup

The current default profile is PostgreSQL.

Example PostgreSQL connection:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/redbook
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Create the database

Create a local database named `redbook` in pgAdmin or PostgreSQL CLI.

### Start the application once to create tables

```bash
./mvnw spring-boot:run
```

With `spring.jpa.hibernate.ddl-auto=update`, Hibernate will create the schema automatically.

### Initialize roles

Before testing signup/signin, insert required roles:

```sql
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');
INSERT INTO roles(name) VALUES ('ROLE_USER');
```

Without these rows, signup will fail because the application looks up `ROLE_ADMIN` or `ROLE_USER` during registration.

## Local HTTPS Setup

This project is already configured to run with HTTPS locally using a self-signed certificate.

### Current ports

- HTTPS application port: `8443`
- HTTP redirect port: `8080`

### Current SSL settings

Configured in `src/main/resources/application.properties`:

```properties
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:redbook-local.p12
server.ssl.key-store-type=PKCS12
server.ssl.key-store-password=changeit
server.ssl.key-alias=redbook-local
app.http.port=8080
```

### Self-signed certificate file

The local certificate is stored at:

```text
src/main/resources/redbook-local.p12
```

### Regenerate the certificate

If you want to regenerate the local self-signed certificate, run:

```bash
keytool -genkeypair \
  -alias redbook-local \
  -keyalg RSA \
  -keysize 2048 \
  -storetype PKCS12 \
  -keystore src/main/resources/redbook-local.p12 \
  -validity 3650 \
  -storepass changeit \
  -keypass changeit \
  -dname "CN=localhost, OU=Dev, O=Chuwa, L=San Francisco, ST=CA, C=US" \
  -ext "SAN=dns:localhost,ip:127.0.0.1"
```

The `SAN` value is important for local HTTPS validation with `localhost`.

## HTTP to HTTPS Redirect

HTTP redirect is implemented in:

```text
src/main/java/com/chuwa/redbook/config/HttpsRedirectConfig.java
```

Behavior:

- Requests to `http://localhost:8080/...`
- are redirected to `https://localhost:8443/...`

If `8080` is already in use on your machine, the app will fail to start. In that case:

1. Stop the process using `8080`, or
2. Change the redirect port in `application.properties`

Example:

```properties
app.http.port=8081
```

## Run the Application

Start the application:

```bash
./mvnw spring-boot:run
```

Successful startup should show something similar to:

```text
Tomcat started on port(s): 8443 (https) 8080 (http)
Started RedbookApplication
```

## Verify HTTPS and Redirect

### Verify HTTPS directly

Use `curl -k` to skip certificate trust validation for the self-signed certificate:

```bash
curl -k -I https://localhost:8443/api/v1/posts
```

Expected result:

- You get an HTTP response from the app
- A `401` response is acceptable for protected endpoints because it means HTTPS is working and Spring Security handled the request

### Verify HTTP redirect

```bash
curl -k -I http://localhost:8080/api/v1/posts
```

Expected result:

- `302 Found`
- `Location: https://localhost:8443/api/v1/posts`

### Verify redirect without following it

```bash
curl -k -I -L --max-redirs 0 http://localhost:8080/api/v1/posts
```

Expected result:

- Response includes the HTTPS `Location` header

## Test APIs with curl

### Signup

```bash
curl -k -X POST https://localhost:8443/api/v1/auth/jwt/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name":"test user",
    "account":"chuwa_test",
    "email":"test@example.com",
    "password":"123456"
  }'
```

### Signin

```bash
curl -k -X POST https://localhost:8443/api/v1/auth/jwt/signin \
  -H "Content-Type: application/json" \
  -d '{
    "accountOrEmail":"test@example.com",
    "password":"123456"
  }'
```

### Get posts

```bash
curl -k https://localhost:8443/api/v1/posts
```

### Create a post with JWT

```bash
curl -k -X POST https://localhost:8443/api/v1/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "title":"My first post",
    "description":"This is my first post description",
    "content":"This is the content of my first post."
  }'
```

## Swagger

After startup, try one of these URLs in your browser:

- `https://localhost:8443/swagger-ui/`
- `https://localhost:8443/swagger-ui/index.html`

Because the certificate is self-signed, your browser may show a security warning first. That is expected in local development.

## Validation

Validation is enabled for:

- `PostDto`
- `CommentDto`
- `@Valid @RequestBody` in post and comment controllers

Examples:

- Post title must not be empty
- Post description must be at least 10 characters
- Comment body must be at least 5 characters
- Comment email must be a valid email format

## Notes

- The project defaults to PostgreSQL right now.
- `curl -k` is the easiest way to test self-signed HTTPS from terminal.
- If signup succeeds but signin fails, check whether the user was created and whether roles exist in the database.
- If the app fails on startup, check:
  - database connectivity
  - port conflicts
  - SSL keystore path and password
