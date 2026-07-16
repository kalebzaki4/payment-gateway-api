package com.br.javapay.domain.tranferencia;

import com.br.javapay.domain.conta.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByDataTransferenciaBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Transferencia> findByid(Long id);

    @Query("SELECT t FROM Transferencia t WHERE (t.contaInicial = :conta OR t.contaFinal = :conta) AND t.dataTransferencia BETWEEN :inicio AND :fim")
    List<Transferencia> findByContaAndDataBetween(@Param("conta") Conta conta, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT t FROM Transferencia t WHERE t.contaInicial = :conta OR t.contaFinal = :conta")
    List<Transferencia> findByConta(@Param("conta") Conta conta);
}