package com.br.javapay.infra.security;

import com.br.javapay.domain.usuario.Roles;
import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${security.password.encoder.secret}")
    private String secretPassword;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail("admin@javapay.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador Master");
            admin.setEmail("admin@javapay.com");
            admin.setSenha(passwordEncoder.encode(secretPassword));
            admin.setRole(Roles.ADMIN);

            usuarioRepository.save(admin);
            System.out.println("🚀 [JavaPay] Primeiro Administrador criado com sucesso!");
        }
    }
}