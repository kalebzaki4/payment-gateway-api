package com.br.javapay.controller;

import com.br.javapay.domain.usuario.Roles;
import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRequestDTO;
import com.br.javapay.domain.usuario.UsuarioService;
import com.br.javapay.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private com.br.javapay.infra.security.AutenticationService autenticationService;

    @MockitoBean
    private com.br.javapay.infra.security.SecurityFilter securityFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");
        usuario.setCpf("12345678909");
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("senha123");
        usuario.setRole(Roles.USER);

        usuarioRequestDTO = new UsuarioRequestDTO("123.456.789-09", "teste@teste.com", "senha123", "Teste");
    }

    @Test
    void testLoginSuccess() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(any(Usuario.class))).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(any(Usuario.class));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        when(usuarioService.createUsuario(any(UsuarioRequestDTO.class))).thenReturn(usuario);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        verify(usuarioService, times(1)).createUsuario(any(UsuarioRequestDTO.class));
    }
}