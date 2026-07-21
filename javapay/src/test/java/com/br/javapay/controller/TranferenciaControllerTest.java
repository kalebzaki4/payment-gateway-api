package com.br.javapay.controller;

import com.br.javapay.domain.tranferencia.StatusTransferencia;
import com.br.javapay.domain.tranferencia.TranferenciaService;
import com.br.javapay.domain.tranferencia.TransferenciaRequestDTO;
import com.br.javapay.domain.tranferencia.TransferenciaResponseDTO;
import com.br.javapay.domain.usuario.Roles;
import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.infra.security.AutenticationService;
import com.br.javapay.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TranferenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TranferenciaService transferenciaService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AutenticationService autenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private TransferenciaRequestDTO transferenciaRequestDTO;
    private TransferenciaResponseDTO transferenciaResponseDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuario Teste");
        usuario.setCpf("12345678909");
        usuario.setEmail("teste@teste.com");
        usuario.setRole(Roles.USER);

        transferenciaRequestDTO = new TransferenciaRequestDTO(
                1L,
                2L,
                new BigDecimal("100.00"),
                "12345678909"
        );

        transferenciaResponseDTO = new TransferenciaResponseDTO(
                1L,
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                StatusTransferencia.CONCLUIDA,
                new BigDecimal("400.00"),
                2L
        );
    }

    @Test
    void testGetAllTransferenciasSuccess() throws Exception {
        when(transferenciaService.findAll(any(Usuario.class)))
                .thenReturn(List.of(transferenciaResponseDTO));

        mockMvc.perform(get("/transferencias")
                        .with(user(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(transferenciaService, times(1)).findAll(any(Usuario.class));
    }

    @Test
    void testGetTransferenciasByDateOrIdSuccess() throws Exception {
        when(transferenciaService.findByDataOrId(eq(1L), any(), any(Usuario.class)))
                .thenReturn(List.of(transferenciaResponseDTO));

        mockMvc.perform(get("/transferencias/filtrarTranferencias")
                        .param("id", "1")
                        .with(user(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(transferenciaService, times(1)).findByDataOrId(eq(1L), any(), any(Usuario.class));
    }

    @Test
    void testRealizarTransferenciaSuccess() throws Exception {
        when(transferenciaService.realizarTransferencia(any(TransferenciaRequestDTO.class), any(Usuario.class)))
                .thenReturn(transferenciaResponseDTO);

        mockMvc.perform(post("/transferencias/realizarTranferencia")
                        .with(user(usuario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferenciaRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(transferenciaService, times(1))
                .realizarTransferencia(any(TransferenciaRequestDTO.class), any(Usuario.class));
    }
}