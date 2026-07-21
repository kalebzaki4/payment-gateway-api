package com.br.javapay.infra.exception;

import com.br.javapay.infra.exception.ContaNaoEncontradaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // Controller fictício para disparar as exceções
    @RestController
    static class TestController {
        @GetMapping("/test/conta-nao-encontrada")
        public void throwContaNaoEncontrada() {
            throw new ContaNaoEncontradaException("Conta não encontrada com o ID: 1");
        }

        @GetMapping("/test/bad-credentials")
        public void throwBadCredentials() {
            throw new BadCredentialsException("Senha errada");
        }

        @GetMapping("/test/generico")
        public void throwException() {
            throw new RuntimeException("Erro inesperado");
        }
    }

    @Test
    @DisplayName("Deve retornar 404 e mensagem correta para ContaNaoEncontradaException")
    void testContaNaoEncontrada() throws Exception {
        mockMvc.perform(get("/test/conta-nao-encontrada")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Conta não encontrada com o ID: 1"));
    }

    @Test
    @DisplayName("Deve retornar 401 para BadCredentialsException")
    void testBadCredentials() throws Exception {
        mockMvc.perform(get("/test/bad-credentials")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("E-mail ou senha incorretos."));
    }

    @Test
    @DisplayName("Deve retornar 500 para exceções genéricas")
    void testGenericException() throws Exception {
        mockMvc.perform(get("/test/generico")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists()); // Verifica se a mensagem foi gerada
    }
}