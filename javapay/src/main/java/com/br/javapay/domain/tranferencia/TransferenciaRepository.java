package com.br.javapay.domain.tranferencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    @Query("SELECT t FROM Transferencia t WHERE " +
            "(t.contaInicial.id = :contaId OR t.contaFinal.id = :contaId) " +
            "AND t.dataTransferencia BETWEEN :dataInicio AND :dataFim")
    List<Transferencia> findByContaEData(
            @Param("contaId") Long contaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}