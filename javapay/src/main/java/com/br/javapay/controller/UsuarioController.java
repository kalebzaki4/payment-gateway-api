package com.br.javapay.controller;

import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRequestDTO;
import com.br.javapay.domain.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable long id) {
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        Usuario usuario = usuarioService.createUsuario(usuarioRequestDTO);
        var uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable long id, @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = usuarioService.updateUsuario(id, usuarioRequestDTO);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}