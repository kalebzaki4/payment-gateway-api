package com.br.javapay.domain.conta;

import com.br.javapay.domain.transferencia.Transferencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conta")
public class Conta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "contaOrigem")
    private transient List<Transferencia> transferenciasEnviadas;

    @OneToMany(mappedBy = "contaDestino")
    private transient List<Transferencia> transferenciasRecebidas;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private Status statusConta;

}
