package com.retailpulse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @Author WilliamSiling
 * @create 16/1/2025 10:18 pm
 */
@Configuration
public class ClientConfig {

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
        return new JdbcRegisteredClientRepository(jdbcOperations);
    }

//    @Bean
//    public CommandLineRunner initializeClients(RegisteredClientRepository repository) {
//        Instant now = Instant.now();
//
//        return args -> {
//            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                    .clientId("client")
//                    .clientName("retail-pulse")
//                    .clientIdIssuedAt(now)
//                    .clientSecret("$2b$12$P5vLo3DSfkAOMrVRAqLM4eYkL4YGtDdI5u1JLaGzQI1nPjjaZShsO")
//                    .clientSecretExpiresAt(getOneYearFromNow(now))
//                    .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                    .redirectUri("http://localhost:4200")
//                    .postLogoutRedirectUri("http://localhost:4200")
//                    .scope("openid")
//                    .clientSettings(ClientSettings.builder()
//                            .requireProofKey(true)
//                            .requireAuthorizationConsent(true)
//                            .build()
//                    )
//                    .tokenSettings(TokenSettings.builder()
//                            .authorizationCodeTimeToLive(Duration.ofMinutes(5))
//                            .accessTokenTimeToLive(Duration.ofHours(1))
//                            .refreshTokenTimeToLive(Duration.ofDays(14))
//                            .build()
//                    )
//                    .build();
//
//            repository.save(registeredClient);
//        };
//    }

    private Instant getOneYearFromNow(Instant now) {
        // Convert Instant to ZonedDateTime in the system default time zone
        ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.systemDefault());

        // Add 1 year to the current ZonedDateTime
        ZonedDateTime oneYearLater = zonedDateTimeNow.plus(1, ChronoUnit.YEARS);

        // Convert back to Instant if needed
        return oneYearLater.toInstant();
    }

}
