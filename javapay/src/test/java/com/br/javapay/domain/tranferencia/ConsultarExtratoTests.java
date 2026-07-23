package com.br.javapay.domain.tranferencia;

import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.Status;
import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRepository;
import com.br.javapay.infra.exception.SemExtratoException;
import com.br.javapay.infra.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("SpellCheckingInspection")
public class ConsultarExtratoTests {

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TranferenciaService tranferenciaService;

    private Usuario usuarioOrigem;
    private Conta contaOrigem;
    private Conta contaDestino;

    @BeforeEach
    void setUp() {
        contaOrigem = new Conta();
        contaOrigem.setId(1L);
        contaOrigem.setValor(new BigDecimal("500.00"));
        contaOrigem.setStatus(Status.ATIVO);

        usuarioOrigem = new Usuario();
        usuarioOrigem.setId(10L);
        usuarioOrigem.setConta(contaOrigem);

        contaDestino = new Conta();
        contaDestino.setId(2L);
        contaDestino.setValor(new BigDecimal("100.00"));
        contaDestino.setStatus(Status.ATIVO);
    }

    @Test
    @DisplayName("Should return extract successfully in findAll")
    void deveRetornarExtratoComSucesso() {
        Transferencia t = new Transferencia();
        t.setId(1L);
        t.setValor(new BigDecimal("100.00"));
        t.setDataTransferencia(LocalDateTime.now());
        t.setStatus(StatusTransferencia.CONCLUIDA);
        t.setContaInicial(contaOrigem);
        t.setContaFinal(contaDestino);

        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioOrigem));
        when(transferenciaRepository.findByConta(contaOrigem)).thenReturn(List.of(t));

        List<TransferenciaResponseDTO> resultado = tranferenciaService.findAll(usuarioOrigem);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.getFirst().id());
    }

    @Test
    @DisplayName("Should find transfer by ID successfully")
    void shouldFindTransferByIdSuccessfully() {
        Long id = 1L;
        Transferencia t = new Transferencia();
        t.setId(id);
        t.setContaInicial(contaOrigem);
        t.setContaFinal(contaDestino);
        t.setValor(new BigDecimal("50.00"));

        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioOrigem));
        when(transferenciaRepository.findById(id)).thenReturn(Optional.of(t));

        List<TransferenciaResponseDTO> results = tranferenciaService.findByDataOrId(id, null, usuarioOrigem);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(id, results.getFirst().id());
    }

    @Test
    @DisplayName("Should find transfer by Date successfully")
    void shouldFindTransferByDateSuccessfully() {
        LocalDateTime data = LocalDateTime.now();
        Transferencia t = new Transferencia();
        t.setId(1L);
        t.setContaInicial(contaOrigem);
        t.setContaFinal(contaDestino);
        t.setValor(new BigDecimal("50.00"));

        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioOrigem));
        when(transferenciaRepository.findByContaAndDataBetween(eq(contaOrigem), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t));

        List<TransferenciaResponseDTO> results = tranferenciaService.findByDataOrId(null, data, usuarioOrigem);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(1L, results.getFirst().id());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when ID and Date are passed together")
    void shouldThrowExceptionWhenIdAndDateArePassedTogether() {
        LocalDateTime agora = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () ->
                tranferenciaService.findByDataOrId(1L, agora, usuarioOrigem)
        );
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when ID and Date are null")
    void shouldThrowExceptionWhenIdAndDateAreNull() {
        assertThrows(IllegalArgumentException.class, () ->
                tranferenciaService.findByDataOrId(null, null, usuarioOrigem)
        );
    }

    @Test
    @DisplayName("Should throw UsuarioNaoEncontradoException when user is null")
    void shouldThrowExceptionWhenUserIsNull() {
        assertThrows(UsuarioNaoEncontradoException.class, () ->
                tranferenciaService.findByDataOrId(1L, null, null)
        );
    }

    @Test
    @DisplayName("Should throw SemExtratoException when findAll is empty")
    void deveLancarExcecaoQuandoExtratoVazio() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioOrigem));
        when(transferenciaRepository.findByConta(contaOrigem)).thenReturn(Collections.emptyList());

        assertThrows(SemExtratoException.class, () ->
                tranferenciaService.findAll(usuarioOrigem)
        );
    }
}