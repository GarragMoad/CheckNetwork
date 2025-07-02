package com.example.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource source = new LdapContextSource();
        source.setUrl("ldap://localhost:8389/");
        source.setBase("dc=example,dc=com");
        return source;
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthProvider() {
        BindAuthenticator authenticator = new BindAuthenticator(contextSource());
        authenticator.setUserDnPatterns(new String[]{"uid={0},ou=people"});

        DefaultLdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(contextSource(), "ou=groups");
        populator.setGroupSearchFilter("member={0}");
        populator.setIgnorePartialResultException(true);

        return new LdapAuthenticationProvider(authenticator, populator);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // désactive CSRF
                .cors(Customizer.withDefaults()) // active la gestion CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // prévol CORS
                        .requestMatchers("/api/auth/**").permitAll() // auth publique
                        .requestMatchers("/ws/**", "/topic/**", "/app/**").permitAll() // WebSocket et STOMP
                        //.anyRequest().authenticated() // le reste nécessite auth

                        .requestMatchers("/api/snmp/status/**").permitAll()
                        .requestMatchers("/api/dashboards/**").permitAll()
                        .requestMatchers("/api/guacamole/connect/by-ip/**").permitAll()
                )
                .authenticationProvider(ldapAuthProvider());

        return http.build();
    }

}
