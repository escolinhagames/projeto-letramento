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
    
    TokenService tokenService;
    ProfessorRepository professorRepository;
    SecurityFilter(TokenService tokenService,ProfessorRepository professorRepository){
        this.tokenService=tokenService;
        this.professorRepository=professorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Skip JWT validation for public endpoints
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method) ||
            "HEAD".equalsIgnoreCase(method) ||
            requestURI.startsWith("/auth/") ||
            requestURI.equals("/auth") ||
            requestURI.equals("/login") ||
            requestURI.equals("/register") ||
            requestURI.startsWith("/professores/login") ||
            requestURI.startsWith("/api/bingo/") ||
            requestURI.startsWith("/api/jogos/") ||
            requestURI.startsWith("/bingo/") ||
            requestURI.startsWith("/embaralhar") ||
            requestURI.endsWith(".html") ||
            requestURI.equals("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        if(login != null){
            ProfessorModel professorModel = professorRepository.findOptionalByEmail(login).orElseThrow(() -> new RuntimeException("Professor Not Found"));
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            var authentication = new UsernamePasswordAuthenticationToken(professorModel, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
