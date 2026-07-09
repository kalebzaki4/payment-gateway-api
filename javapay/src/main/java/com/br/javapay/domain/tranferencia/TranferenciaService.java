package com.br.javapay.domain.tranferencia;

import com.br.javapay.domain.conta.Conta;
import com.br.javapay.domain.conta.ContaRepository;
import com.br.javapay.domain.conta.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // ver transferencias pela data que ela foi feita ou recebida
    public List<Transferencia> findByData(Long contaId, TranferenciaDataDTO dataRequest) {
        LocalDate diaDoFiltro = dataRequest.dataTransferencia().toLocalDate();

        LocalDateTime dataInicio = diaDoFiltro.atStartOfDay(); // 00:00:00
        LocalDateTime dataFim = diaDoFiltro.atTime(23, 59, 59); // 23:59:59

        return transferenciaRepository.findByContaEData(
                contaId,
                dataInicio,
                dataFim
        );
    }

    @Transactional
    public Transferencia realizarTransferencia(TransferenciaRequestDTO request) {
        Conta contaOrigem = contaRepository.findById(request.contaInicial()).orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));
        Conta contaDestino = contaRepository.findById(request.contaFinal()).orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

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
        transferencia.setDataTransferencia(LocalDateTime.now());

        return transferenciaRepository.save(transferencia);
    }

    private void validarTransferencia(Conta origem, Conta destino, BigDecimal valor) {
        if (origem.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Conta de origem está inativa");
        }
        if (destino.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Conta de destino está inativa");
        }
        if (origem.getValor().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente na conta de origem");
        }
    }
}