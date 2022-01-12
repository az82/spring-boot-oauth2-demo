package de.az82.auth.oauth2.demo.resourceserver;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.GET;

@EnableWebSecurity
public class ResourceServerSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.mvcMatcher("/resources/**")
                .authorizeRequests()
                .mvcMatchers(GET, "/resources")
                .access("hasAuthority('SCOPE_resources.read')")

                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/resources")
                .access("hasAuthority('SCOPE_resources.add')")

                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.DELETE, "/resources/**")
                .access("hasAuthority('SCOPE_resources.delete')")

                .and()
                .oauth2ResourceServer()
                .jwt();

        return http.build();
    }

}
