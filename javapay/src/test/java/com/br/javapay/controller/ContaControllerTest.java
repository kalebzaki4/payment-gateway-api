package com.br.javapay.controller;

import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.ContaService;
import com.br.javapay.infra.security.AutenticationService;
import com.br.javapay.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContaService contaService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AutenticationService autenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Conta contaExemplo;

    @BeforeEach
    void setUp() {
        contaExemplo = new Conta();
        contaExemplo.setId(1L);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a conta por ID (Admin)")
    @WithMockUser(roles = "ADMIN")
    void testGetContaByIdAsAdmin() throws Exception {
        // CORREÇÃO: Usando o método real getContaById do seu Service
        when(contaService.getContaById(1L)).thenReturn(contaExemplo);

        mockMvc.perform(get("/contas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(contaService, times(1)).getContaById(1L);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a lista de contas (Admin)")
    @WithMockUser(roles = "ADMIN")
    void testGetAllContasAsAdmin() throws Exception {
        // CORREÇÃO: Usando o método real getAllContas do seu Service
        when(contaService.getAllContas()).thenReturn(List.of(contaExemplo));

        mockMvc.perform(get("/contas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(contaService, times(1)).getAllContas();
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden para usuário sem perfil ADMIN")
    @WithMockUser(roles = "USER")
    void testGetAllContasAsUserForbidden() throws Exception {
        mockMvc.perform(get("/contas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // Verifica que o service NUNCA foi chamado nesse cenário
        verify(contaService, never()).getAllContas();
    }
}