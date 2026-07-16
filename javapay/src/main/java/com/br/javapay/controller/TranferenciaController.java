package com.br.javapay.controller;

import com.br.javapay.domain.tranferencia.*;
import com.br.javapay.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/extrato")
    public ResponseEntity<List<TransferenciaResponseDTO>> getAllTransferencias(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok().body(tranferenciaService.findAll(usuario));
    }

    @GetMapping(value = "/filtrarTranferencias")
    public ResponseEntity<List<TransferenciaResponseDTO>> getTranferenciasPorDataOuId(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dataTransferencia
    ) {
        LocalDateTime dataBusca = (dataTransferencia != null) ? dataTransferencia.atStartOfDay() : null;
        return ResponseEntity.ok().body(tranferenciaService.findByDataOrId(id, dataBusca, usuario));
    }

    @PostMapping(value = "/realizarTranferencia")
    public ResponseEntity<TransferenciaResponseDTO> createTransferencia(
            @RequestBody TransferenciaRequestDTO transferenciaRequestDTO,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {
        return ResponseEntity.ok().body(tranferenciaService.realizarTransferencia(transferenciaRequestDTO, usuarioAutenticado));
    }
}