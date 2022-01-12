package de.az82.auth.oauth2.demo.authserver;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_POST;

@Configuration(proxyBeanMethods = false)
public class AuthServerConfig {

    private final String baseUrl;

    @Autowired
    public AuthServerConfig(@Value("${server.port}") int port) {
        baseUrl = "http://oauth2-demo-auth-server:" + port;
    }

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // Apply default OAuth2 security and generate a form login page
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.formLogin(withDefaults()).build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        var client = RegisteredClient.withId(UUID.randomUUID().toString())
                // Single hardcoded client. IRL this would be user repository (DB, LDAP, etc.)
                .clientId("client")
                // Undocumented: Must be prefixed with {ID}, where ID is the ID of a PasswordEncoder implementation
                .clientSecret("{noop}melanie1234")
                // Supported authentication methods
                .clientAuthenticationMethod(CLIENT_SECRET_POST)
                .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                // Supported grant types
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .authorizationGrantType(AUTHORIZATION_CODE)
                .authorizationGrantType(REFRESH_TOKEN)
                // Redirect URIs at the client
                // WARNING: Spring does not support localhost URLs here as those are not recommended by the OAuth2
                // standard
                .redirectUri("http://oauth2-demo-client:8080/authorized")
                .scope("resources.read")
                .scope("resources.add")
                .scope("resources.delete")
                .build();

        return new InMemoryRegisteredClientRepository(client);
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().issuer(baseUrl).build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // Generate a random RSA key pair to use as signing key
        // IRL this would be an injected secret
        var jwkSet = new JWKSet(generateRandomJWK());
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private static JWK generateRandomJWK() {
        KeyPairGenerator keyPairGenerator;

        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        keyPairGenerator.initialize(2048);
        var keyPair = keyPairGenerator.generateKeyPair();

        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

}
