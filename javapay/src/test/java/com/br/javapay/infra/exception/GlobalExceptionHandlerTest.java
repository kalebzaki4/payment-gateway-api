package com.br.javapay.infra.exception;

import com.br.javapay.infra.security.AutenticationService;
import com.br.javapay.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
@Import({GlobalExceptionHandler.class, GlobalExceptionHandlerTest.TestController.class})
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutenticationService autenticationService;

    @MockitoBean
    private TokenService tokenService;

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/bad-credentials")
        void throwBadCredentials() {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        @GetMapping("/conta-nao-encontrada")
        void throwContaNaoEncontrada() {
            throw new ContaNaoEncontradaException("Conta não encontrada");
        }
    }

    @Test
    @DisplayName("Deve retornar status 401 ao capturar BadCredentialsException")
    void testBadCredentials() throws Exception {
        mockMvc.perform(get("/test/bad-credentials")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar status 404 ao capturar exceção de conta não encontrada")
    void testContaNaoEncontrada() throws Exception {
        mockMvc.perform(get("/test/conta-nao-encontrada")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}