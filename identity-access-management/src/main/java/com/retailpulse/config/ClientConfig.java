package com.retailpulse.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Configuration
public class ClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(ClientConfig.class);

    @Value("${client.id}")
    private String clientId;

    @Value("${client.name}")
    private String clientName;

    @Value("${client.redirect-uri}")
    private String redirectUri;

    @Value("${client.post-logout-redirect-uri}")
    private String postLogoutRedirectUri;

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
        return new JdbcRegisteredClientRepository(jdbcOperations);
    }

    @Bean
    public CommandLineRunner initializeClients(RegisteredClientRepository repository) {
        Instant now = Instant.now();

        return args -> {
            try {
                if (repository.findByClientId(clientId) == null) {
                    RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                            .clientId(clientId)
                            .clientName(clientName)
                            .clientSecret("$2b$12$P5vLo3DSfkAOMrVRAqLM4eYkL4YGtDdI5u1JLaGzQI1nPjjaZShsO")
                            .clientIdIssuedAt(now)
                            .clientSecretExpiresAt(getOneYearFromNow(now))
                            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                            .redirectUri(redirectUri)
                            .postLogoutRedirectUri(postLogoutRedirectUri)
                            .scope("openid")
                            .clientSettings(ClientSettings.builder()
                                    .requireProofKey(true)
                                    .requireAuthorizationConsent(true)
                                    .build()
                            )
                            .tokenSettings(TokenSettings.builder()
                                    .authorizationCodeTimeToLive(Duration.ofMinutes(5))
                                    .accessTokenTimeToLive(Duration.ofHours(1))
                                    .refreshTokenTimeToLive(Duration.ofDays(14))
                                    .build()
                            )
                            .build();

                    repository.save(registeredClient);
                    logger.info("Registered client initialized successfully.");
                } else {
                    logger.info("Client with ID '{}' is already registered.", clientId);
                }
            } catch (Exception e) {
                logger.error("Error initializing registered client", e);
            }
        };
    }

    private Instant getOneYearFromNow(Instant now) {
        ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.systemDefault());
        ZonedDateTime oneYearLater = zonedDateTimeNow.plus(1, ChronoUnit.YEARS);
        return oneYearLater.toInstant();
    }
}