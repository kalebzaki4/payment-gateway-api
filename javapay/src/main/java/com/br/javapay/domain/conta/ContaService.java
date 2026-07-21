package com.br.javapay.domain.conta;

import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.infra.exception.ContaNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {
    private final ContaRepository contaRepository;

    @Autowired
    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    // ver conta expecifica
    public Conta getContaById(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada com o ID: " + id));
    }

    // ver todas as contas
    public List<Conta> getAllContas() {
        return contaRepository.findAll();
    }

    // ver saldo da conta
    public Conta verSaldoDaConta(Usuario usuario){
        return contaRepository.findById(usuario.getConta().getId()).orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada"));
    }
}
