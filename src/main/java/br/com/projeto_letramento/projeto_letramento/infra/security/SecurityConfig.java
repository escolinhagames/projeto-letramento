package br.com.projeto_letramento.projeto_letramento.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- alterado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.HEAD, "/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/professores/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jogos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/jogos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/bingo/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/bingo/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/bingo/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/embaralhar/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/embaralhar/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/jogo-imagem/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/jogo-imagem/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/jogo-imagem/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/jogo-imagem/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/jogo-imagem/**").permitAll()
                        .requestMatchers("/*.html").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "https://projeto-letramento.vercel.app", // <-- coloque a URL exata do seu Vercel
            "http://localhost:4200"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}