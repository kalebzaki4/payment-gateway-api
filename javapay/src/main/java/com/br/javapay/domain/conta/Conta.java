package com.br.javapay.domain.conta;

import com.br.javapay.domain.tranferencia.Tranferencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "conta")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Conta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "contaInicial")
    private transient List<Tranferencia> transferencias;

    @OneToMany(mappedBy = "contaFinal")
    private transient List<Tranferencia> transferenciasRecebidas;

    public void debitar(BigDecimal valor) {
        this.valor = this.valor.subtract(valor);
    }

    public void creditar(BigDecimal valor) {
        this.valor = this.valor.add(valor);
    }

}
