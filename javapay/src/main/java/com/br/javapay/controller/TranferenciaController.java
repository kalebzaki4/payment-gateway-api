package com.br.javapay.controller;

import com.br.javapay.domain.tranferencia.TranferenciaDataDTO;
import com.br.javapay.domain.tranferencia.TranferenciaService;
import com.br.javapay.domain.tranferencia.TransferenciaRequestDTO;
import com.br.javapay.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.br.javapay.domain.tranferencia.Transferencia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transferencias")
public class TranferenciaController {
    private final TranferenciaService tranferenciaService;

    @Autowired
    public TranferenciaController(TranferenciaService tranferenciaService) {
        this.tranferenciaService = tranferenciaService;
    }

    // ver todas as tranferencias da minha conta
    @GetMapping(value = "/extrato")
    public ResponseEntity<List<Transferencia>> getAllTransferencias(@AuthenticationPrincipal Usuario usuario) {
        List<Transferencia> transferencias = tranferenciaService.findAll(usuario);
        return ResponseEntity.ok().body(transferencias);
    }

    // ver transferencias pelo id ou data que ela foi feita ou recebida
    @GetMapping(value = "/filtrarTranferencias")
    public ResponseEntity<List<Transferencia>> getTranferenciasPorDataOuId(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dataTransferencia
    ) {
        List<Transferencia> listaEncontrada = tranferenciaService.findByDataOrId(id, dataTransferencia.atStartOfDay(), usuario);
        return ResponseEntity.ok().body(listaEncontrada);
    }

    // realizar uma transferência
    @PostMapping(value = "/realizarTranferencia")
    public ResponseEntity<Transferencia> createTransferencia(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO, @AuthenticationPrincipal Usuario usuarioAutenticado) {
        Transferencia transferencia = tranferenciaService.realizarTransferencia(transferenciaRequestDTO, usuarioAutenticado);
        return ResponseEntity.ok().body(transferencia);
    }

}
