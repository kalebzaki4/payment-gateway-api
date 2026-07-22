package com.br.javapay.infra.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.br.javapay.infra.exception.ErrorMessageDTO;
import com.br.javapay.infra.exception.TokenInvalidoException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final AutenticationService autenticationService;
    private final ObjectMapper objectMapper;

    @Autowired
    public SecurityFilter(TokenService tokenService,
                          AutenticationService autenticationService,
                          ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.autenticationService = autenticationService;
        this.objectMapper = objectMapper;
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
            } catch (TokenExpiredException | TokenInvalidoException e) {
                enviarErro(response, "Token expirado ou inválido. Por favor, faça login novamente.", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void enviarErro(HttpServletResponse response, String mensagem, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(
                mensagem,
                LocalDateTime.now(ZoneId.of("UTC")).toString(),
                null
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorMessageDTO));
    }

    private String recuperarToken(HttpServletRequest request) {
        String authheader = request.getHeader("Authorization");
        if (authheader == null || !authheader.startsWith("Bearer ")) {
            return null;
        }
        return authheader.substring(7);
    }
}