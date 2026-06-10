package com.example.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ← Correção do 403 (libera OPTIONS do Axios)
                        .requestMatchers(
                                "/auth/**",
                                "/auth/forgot-password",
                                "/auth/reset-password",
                                "/webhooks/**",
                                "/graphiql",
                                "/graphiql/**",
                                "/graphql",
                                "/error"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
                        .requestMatchers("/categorias/**").hasRole("ADMIN")

                        .requestMatchers("/pagamentos/**").authenticated()
                        .requestMatchers(
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/perfil").authenticated()
                        .requestMatchers(HttpMethod.POST, "/perfil").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/perfil").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/perfil").authenticated()

                        .requestMatchers(HttpMethod.GET, "/projetos/*/atualizacoes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/projetos/*/atualizacoes/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/projetos/*/atualizacoes/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/projetos/*/atualizacoes/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/projetos/*/conteudos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/projetos/*/conteudos/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/projetos/*/conteudos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/projetos/*/conteudos/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/enquetes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/enquetes/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/enquetes/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/enquetes/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/notificacoes/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/notificacoes/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/notificacoes/**").authenticated()

                        .requestMatchers(HttpMethod.GET,    "/projetos/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/projetos/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,    "/projetos/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/projetos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/projetos/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}