package com.br.javapay.controller;

import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRequestDTO;
import com.br.javapay.domain.usuario.UsuarioService;
import com.br.javapay.domain.usuario.UsuarioUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping(value = "/me")
    public ResponseEntity<Void> updateUsuario(@AuthenticationPrincipal Usuario usuario, @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        usuarioService.updateUsuario(usuario.getId(), usuarioUpdateDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = usuarioService.updateUsuario(id, usuarioRequestDTO);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}