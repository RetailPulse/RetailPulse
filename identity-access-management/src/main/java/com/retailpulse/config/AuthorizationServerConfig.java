package com.retailpulse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.sql.DataSource;


@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());

        http.exceptionHandling((e) ->
                e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(Customizer.withDefaults());

        http.authorizeHttpRequests(
                c -> c.anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        // Customize JdbcTemplate if needed (e.g., query timeout)
        jdbcTemplate.setQueryTimeout(30); // Example: Set query timeout to 30 seconds
        return jdbcTemplate;
    }

    @Bean
    public JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
    }

    @Bean
    public JdbcOAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository);
    }


}
