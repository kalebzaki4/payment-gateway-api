package com.br.javapay.domain.transferencia;

import com.br.javapay.domain.conta.Conta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Transferencia")
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    private Conta contaDestino;

    @Column(nullable = false)
    private Date dataTransferencia;
}
