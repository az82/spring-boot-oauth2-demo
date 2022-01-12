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
http -f POST localhost:8081/oauth2/token grant_type=client_credentials client_id=client client_secret=melanie1234 audience=test.read
```

### Authorization Code Grant

```bash
http -v -f GET localhost:8081/oauth2/authorize response_type==code client_id==client redirect_uri==http://localhost:8080/authorized state==123 scope==resources.read
```


## See also

* https://www.baeldung.com/spring-security-oauth-auth-server