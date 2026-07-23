package com.br.javapay.domain.tranferencia;

import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.ContaRepository;
import com.br.javapay.domain.conta.Status;
import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.domain.usuario.UsuarioRepository;
import com.br.javapay.infra.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TranferenciaService {
    private final TransferenciaRepository transferenciaRepository;
    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public TranferenciaService(TransferenciaRepository transferenciaRepository, ContaRepository contaRepository, UsuarioRepository usuarioRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<TransferenciaResponseDTO> findAll(Usuario usuario) {
        Usuario usuarioCompleto = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado no banco"));

        Conta contaDoUsuario = usuarioCompleto.getConta();
        List<Transferencia> transferenciasDoUsuario = transferenciaRepository.findByConta(contaDoUsuario);

        if (transferenciasDoUsuario.isEmpty()) {
            throw new SemExtratoException("Nenhuma transferencia encontrada");
        }

        return transferenciasDoUsuario.stream()
                .map(t -> new TransferenciaResponseDTO(
                        t.getId(), t.getValor(), t.getDataTransferencia(),
                        t.getStatus(), t.getContaInicial().getValor(), t.getContaFinal().getId()))
                .toList();
    }

    public List<TransferenciaResponseDTO> findByDataOrId(Long id, LocalDateTime dataRequest, Usuario usuario) {
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("Usuario não encontrado");
        }

        if (id != null && dataRequest != null) {
            throw new IllegalArgumentException("o ID ou a data não podem ser preenchidos juntos, é apenas um ou outro");
        }
        if (id == null && dataRequest == null) {
            throw new IllegalArgumentException("os dados não podem ser nulos");
        }

        Usuario usuarioCompleto = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado no banco"));

        Conta contaDoUsuario = usuarioCompleto.getConta();
        List<Transferencia> extrato = new ArrayList<>();

        if (id == null && dataRequest != null) {
            LocalDateTime dataInicio = dataRequest;
            LocalDateTime dataFim = dataRequest.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            extrato = transferenciaRepository.findByContaAndDataBetween(contaDoUsuario, dataInicio, dataFim);
        }

        if (id != null && dataRequest == null) {
            extrato = transferenciaRepository.findById(id)
                    .filter(t -> t.getContaInicial().equals(contaDoUsuario) || t.getContaFinal().equals(contaDoUsuario))
                    .map(List::of).orElse(new ArrayList<>());
        }

        if (extrato.isEmpty()) {
            throw new SemExtratoException("Nenhuma transferencia encontrada nesse dia");
        }

        return extrato.stream()
                .map(t -> new TransferenciaResponseDTO(
                        t.getId(), t.getValor(), t.getDataTransferencia(),
                        t.getStatus(), t.getContaInicial().getValor(), t.getContaFinal().getId()))
                .toList();
    }

    @Transactional
    public TransferenciaResponseDTO realizarTransferencia(TransferenciaRequestDTO request, Usuario usuarioAutenticado) {
        Usuario usuarioCompleto = usuarioRepository.findById(usuarioAutenticado.getId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado no banco"));

        Conta contaOrigem = usuarioCompleto.getConta();

        if (contaOrigem == null) {
            throw new ContaNaoEncontradaException("Usuário não possui uma conta vinculada.");
        }

        Conta contaDestino;

        if (request.contaFinal() == null && request.cpf() == null) {
            throw new IllegalArgumentException("Você precisa informar o ID da conta ou o CPF.");
        } else if (request.contaFinal() != null && request.cpf() != null) {
            throw new IllegalArgumentException("Envie apenas o ID da conta ou apenas o CPF, não os dois juntos.");
        } else if (request.contaFinal() != null) {
            contaDestino = contaRepository.findById(request.contaFinal())
                    .orElseThrow(() -> new ContaNaoEncontradaException("Conta a receber não encontrada com o ID passado."));
        } else {
            contaDestino = contaRepository.findByUsuarioCpf(request.cpf())
                    .orElseThrow(() -> new CpfInvalidoException("CPF inválido ou não encontrado."));
        }

        validarTransferencia(contaOrigem, contaDestino, request.saldo());
        contaOrigem.debitar(request.saldo());
        contaDestino.creditar(request.saldo());

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transferencia transferencia = new Transferencia();
        transferencia.setContaInicial(contaOrigem);
        transferencia.setContaFinal(contaDestino);
        transferencia.setValor(request.saldo());
        transferencia.setStatus(StatusTransferencia.CONCLUIDA);
        transferencia.setDataTransferencia(LocalDateTime.now(ZoneId.systemDefault()));

        Transferencia transferenciaSalva = transferenciaRepository.save(transferencia);

        return new TransferenciaResponseDTO(
                transferenciaSalva.getId(),
                transferenciaSalva.getValor(),
                transferenciaSalva.getDataTransferencia(),
                transferenciaSalva.getStatus(),
                contaOrigem.getValor(),
                contaDestino.getId()
        );
    }

    private void validarTransferencia(Conta origem, Conta destino, BigDecimal valor) {
        if (origem.getId().equals(destino.getId())) {
            throw new TransferenciaIlegalException("Você não pode realizar uma transferência para a sua própria conta.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SaldoInsulficienteOuInválidoException("O valor da transferência deve ser maior que zero.");
        }
        if (origem.getStatus() == Status.INATIVO) {
            throw new ContaOrigemInativaException("Conta de origem está inativa");
        }
        if (destino.getStatus() == Status.INATIVO) {
            throw new ContaDestinoInativaException("Conta de destino está inativa");
        }
        if (origem.getValor().compareTo(valor) < 0) {
            throw new SaldoInsulficienteOuInválidoException("Saldo insuficiente na conta de origem");
        }
    }
}