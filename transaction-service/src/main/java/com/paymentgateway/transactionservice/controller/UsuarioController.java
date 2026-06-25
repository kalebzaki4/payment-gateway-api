package com.paymentgateway.transactionservice.controller;

import com.paymentgateway.transactionservice.domain.usuario.CadastroUsuarioDTO;
import com.paymentgateway.transactionservice.domain.usuario.Usuario;
import com.paymentgateway.transactionservice.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) {
        var uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.ok().location(uri).body(usuarioService.buscarUsuarioPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody CadastroUsuarioDTO cadastroUsuarioDTO, UriComponentsBuilder uriComponentsBuilder) {
        Usuario usuarioSalvo = usuarioService.cadastrar(cadastroUsuarioDTO);
        var uri = UriComponentsBuilder.fromPath("/usuarios/{id}").buildAndExpand(usuarioSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(usuarioSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid CadastroUsuarioDTO cadastroUsuarioDTO) {
        Usuario usuarioExistente = usuarioService.atualizarUsuario(id, cadastroUsuarioDTO);
        return ResponseEntity.ok().body(usuarioExistente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
