package com.br.javapay.controller;

import com.br.javapay.domain.usuario.Roles;
import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRequestDTO;
import com.br.javapay.domain.usuario.UsuarioService;
import com.br.javapay.domain.usuario.UsuarioUpdateDTO;
import com.br.javapay.infra.security.AutenticationService;
import com.br.javapay.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AutenticationService autenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;
    private UsuarioUpdateDTO usuarioUpdateDTO;

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
        usuarioUpdateDTO = new UsuarioUpdateDTO("Teste Atualizado", "novo@teste.com", "novasenha123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsuariosAsAdmin() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllUsuariosAsUserForbidden() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isForbidden());

        verify(usuarioService, never()).findAll();
    }

    @Test
    void testGetAllUsuariosWithoutTokenUnauthorized() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isForbidden());

        verify(usuarioService, never()).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUsuarioByIdAsAdmin() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(usuarioService, times(1)).findById(1L);
    }

    @Test
    void testUpdateMeSuccess() throws Exception {
        when(usuarioService.updateUsuario(eq(1L), any(UsuarioUpdateDTO.class))).thenReturn(usuario);

        mockMvc.perform(put("/usuarios/updateMe")
                        .with(user(usuario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioUpdateDTO)))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).updateUsuario(eq(1L), any(UsuarioUpdateDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUsuarioAsAdmin() throws Exception {
        when(usuarioService.updateUsuario(eq(1L), any(UsuarioRequestDTO.class))).thenReturn(usuario);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(usuarioService, times(1)).updateUsuario(eq(1L), any(UsuarioRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUsuarioAsAdmin() throws Exception {
        doNothing().when(usuarioService).deleteUsuario(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deleteUsuario(1L);
    }
}