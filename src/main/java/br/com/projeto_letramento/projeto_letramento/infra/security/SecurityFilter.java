package br.com.projeto_letramento.projeto_letramento.infra.security;

import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;
import br.com.projeto_letramento.projeto_letramento.repository.ProfessorRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final ProfessorRepository professorRepository;

    public SecurityFilter(TokenService tokenService, ProfessorRepository professorRepository) {
        this.tokenService = tokenService;
        this.professorRepository = professorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        var token = recoverToken(request);
        System.out.println(">>> TOKEN RECEBIDO: " + token);

        if (token != null) {
            var login = tokenService.validateToken(token);
            System.out.println(">>> LOGIN VALIDADO: " + login);

            if (login != null) {
                ProfessorModel professorModel = professorRepository
                        .findOptionalByEmail(login)
                        .orElseThrow(() -> new RuntimeException("Professor Not Found"));

                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

                var authentication = new UsernamePasswordAuthenticationToken(
                        professorModel,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        return authHeader.substring(7); 
    }
}