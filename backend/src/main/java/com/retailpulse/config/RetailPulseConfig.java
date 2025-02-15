package com.retailpulse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Author WilliamSiling
 * @create 5/2/2025 3:38 pm
 */
@Configuration
public class RetailPulseConfig {

    @Value("${auth.jwt.key.set.uri}")
    private String keySetUri;

    @Value("${auth.enabled}")
    private boolean authEnabled;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

      if (authEnabled) {          
        http.oauth2ResourceServer(
                c -> c.jwt(
                        j -> j.jwkSetUri(keySetUri).jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
        );

        http.authorizeHttpRequests(
                c -> c.requestMatchers("/hello").authenticated() //.hasRole("SUPER").anyRequest().authenticated()
        );

      } else {
        http.authorizeHttpRequests(
          c -> c.anyRequest().permitAll()
        );        

        http.csrf(csrf -> csrf.disable());
      }

      return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        return jwtGrantedAuthoritiesConverter;
    }

}
