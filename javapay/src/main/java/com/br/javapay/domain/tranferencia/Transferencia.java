package com.br.javapay.domain.tranferencia;

import com.br.javapay.domain.conta.Conta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transferencias")
public class Transferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    private LocalDateTime dataTransferencia;

    @ManyToOne
    @JoinColumn(name = "conta_inicial_id")
    private Conta contaInicial;

    @ManyToOne
    @JoinColumn(name = "conta_final_id")
    private Conta contaFinal;

    @Enumerated(EnumType.STRING)
    private StatusTransferencia status;
}