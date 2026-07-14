package com.br.javapay.domain.tranferencia;

import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.ContaRepository;
import com.br.javapay.domain.conta.Status;
import com.br.javapay.domain.usuario.Usuario;
import com.br.javapay.infra.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TranferenciaService {
    private final TransferenciaRepository transferenciaRepository;
    private final ContaRepository contaRepository;

    @Autowired
    public TranferenciaService(TransferenciaRepository transferenciaRepository, ContaRepository contaRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
    }

    public List<Transferencia> findAll() {
        return transferenciaRepository.findAll();
    }

    public List<Transferencia> findByDataOrId(Long id, LocalDateTime dataRequest) {
        List<Transferencia> extrato = new ArrayList<>();

        if (id != null && dataRequest != null) {
            throw new IllegalArgumentException("o ID ou a data não podem ser preenchidos juntos, é apenas um ou outro");
        }
        if (id == null && dataRequest == null) {
            throw new IllegalArgumentException("os dados não podem ser nulos");
        }
        if (id == null && dataRequest != null) {
            LocalDateTime dataInicio = dataRequest;
            LocalDateTime dataFim = dataRequest.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            extrato = transferenciaRepository.findByDataTransferenciaBetween(dataInicio, dataFim);
        }
        if (id != null && dataRequest == null) {
            extrato = transferenciaRepository.findById(id).map(List::of).orElse(new ArrayList<>());
        }
        return extrato;
    }

    @Transactional
    public Transferencia realizarTransferencia(TransferenciaRequestDTO request, Usuario usuarioAutenticado) {
        Conta contaOrigem = usuarioAutenticado.getConta();
        if (contaOrigem == null) {
            throw new ContaNaoEncontradaException("Usuário autenticado não possui uma conta vinculada.");
        }

        Conta contaDestino;

        if (request.contaFinal() == null && request.cpf() == null) {
            throw new IllegalArgumentException("Você precisa informar o ID da conta ou o CPF.");
        } else if (request.contaFinal() != null && request.cpf() != null) {
            throw new IllegalArgumentException("Envie apenas o ID da conta ou apenas o CPF, não os dois juntos.");
        } else if (request.contaFinal() != null) {
            contaDestino = contaRepository.findById(request.contaFinal()).orElseThrow(() -> new ContaNaoEncontradaException("Conta a receber não encontrada com o ID passado."));
        } else {
            contaDestino = contaRepository.findByUsuarioCpf(request.cpf()).orElseThrow(() -> new CpfInvalidoException("CPF inválido ou não encontrado."));
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

        return transferenciaRepository.save(transferencia);
    }

    private void validarTransferencia(Conta origem, Conta destino, BigDecimal valor) {
        if (origem.getStatus() == Status.INATIVO) {
            throw new ContaOrigemInativaException("Conta de origem está inativa");
        }
        if (destino.getStatus() == Status.INATIVO) {
            throw new ContaDestinoInativaException("Conta de destino está inativa");
        }
        if (origem.getValor().compareTo(valor) < 0) {
            throw new SaldoInsulficienteException("Saldo insuficiente na conta de origem");
        }
    }
}