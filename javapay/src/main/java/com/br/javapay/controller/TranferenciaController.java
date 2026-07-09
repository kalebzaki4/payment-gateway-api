package com.br.javapay.controller;

import com.br.javapay.domain.tranferencia.TranferenciaDataDTO;
import com.br.javapay.domain.tranferencia.TranferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.br.javapay.domain.tranferencia.Transferencia;

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
    public ResponseEntity<List<Transferencia>> getAllTransferencias() {
        List<Transferencia> transferencias = tranferenciaService.findAll();
        return ResponseEntity.ok().body(transferencias);
    }

    // ver transferencias pela data que ela foi feita ou recebida
    @GetMapping(value = "/dataTransferencia/{id}")
    public ResponseEntity<List<Transferencia>> getTranferenciasPorData(@PathVariable Long id, @RequestBody TranferenciaDataDTO transferenciaDataDTO) {
        List<Transferencia> listaEncontrada = tranferenciaService.findByData(id, transferenciaDataDTO);
        return ResponseEntity.ok().body(listaEncontrada);
    }

}
