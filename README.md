# Spring Boot OAuth2 Demo


## Prerequisites

* JDK 17

## Build

```bash
./gradlew clean build
```

## Run

```bash
./gradlew bootRun
```

## Actions

### Client Credentials Grant

```bash
http -f POST localhost:8081/oauth2/token grant_type=client_credentials client_id=client client_secret=melanie1234 audience=test.read
```

## See also

* https://www.baeldung.com/spring-security-oauth-auth-server