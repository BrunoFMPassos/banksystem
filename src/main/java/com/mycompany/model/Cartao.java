package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "cartao")
public class Cartao implements Serializable {

    @OneToMany(mappedBy = "cartao", targetEntity = Conta.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    private List<Conta> conta;

    @ManyToOne
    @JoinColumn(name="tipoDeCartao_id")
    private TipoDeCartao tipoDeCartao;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false, length=200)
    private Long numero;
    @Column(nullable=false, length=200)
    private Integer cvv;
    @Column(nullable=false, length=200)
    private Long dataValidade;
    @Column(nullable=false, length=200)
    private String tipo; //credito,debito,credito/debito
    @Column(nullable=false, length=200)
    private Long limite;
    @Column(nullable=false, length=200)
    private Integer senha;

    public List<Conta> getConta() {
        return conta;
    }

    public void setConta(List<Conta> conta) {
        this.conta = conta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public Long getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Long dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }


}
