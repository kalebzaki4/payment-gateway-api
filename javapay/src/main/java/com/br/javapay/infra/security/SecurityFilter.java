package com.br.javapay.infra.security;

import com.br.javapay.infra.exception.TokenInvalidoException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final AutenticationService autenticationService;
    private final HandlerExceptionResolver exceptionResolver;

    @Autowired
    public SecurityFilter(TokenService tokenService, AutenticationService autenticationService, HandlerExceptionResolver exceptionResolver) {
        this.tokenService = tokenService;
        this.autenticationService = autenticationService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperarToken(request);
        if (token != null) {
            try {
                String subject = tokenService.getSubject(token);
                var usuario = autenticationService.loadUserByUsername(subject);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (TokenInvalidoException e) {
                exceptionResolver.resolveException(request, response, null, new TokenInvalidoException("Token expirado ou inválido. Por favor, faça login novamente."));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authheader = request.getHeader("Authorization");
        if (authheader == null || !authheader.startsWith("Bearer ")) {
            return null;
        }
        return authheader.substring(7);
    }
}