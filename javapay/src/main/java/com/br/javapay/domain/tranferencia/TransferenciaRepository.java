package com.br.javapay.domain.tranferencia;

import com.br.javapay.domain.conta.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Tranferencia, Long> {

}
