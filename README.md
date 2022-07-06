# Spring Boot OAuth2 Demo

## Prerequisites

* JDK 17

## Build

For `auth-server`, `client` and `resource-server`: 

```bash
./gradlew clean build
```

## Run

For `auth-server`, `client` and `resource-server`:

```bash
./gradlew bootRun
```

## Preparation

Create host entries:

```bash
sudo cat >> /etc/hosts <<EOT
127.0.0.1 oauth2-demo-auth-server
127.0.0.1 oauth2-demo-resource-server
127.0.0.1 oauth2-demo-client
EOT
```

## Actions

### Client Credentials Grant

```bash
http -v -f POST oauth2-demo-auth-server:8081/oauth2/token grant_type=client_credentials client_id=client client_secret=melanie1234 audience=test.read
```

### Direct Access To The Resource Server

Will fail, no token.

```bash
http -v -f GET oauth2-demo-resource-server:8082/resources/
```

### Access With The Client Application

1. Open the overview page in a browser: `http://oauth2-demo-client:8080/`
   * The overview page will be displayed, the client has the `resources.read` scope
2. Add a resource
   * Resource will be added, the client has the `resources.write` scope
3. Delete a resource
   * Will fail because the client does not request tokens with `resources.delete` scope
   * See `client/src/main/resources/application.yaml`
4. Add the scope to `client/src/main/resources/application.yaml` and restart the client
5. Delete a resource
   * The resource will be deleted

## See also

* https://www.baeldung.com/spring-security-oauth-auth-server