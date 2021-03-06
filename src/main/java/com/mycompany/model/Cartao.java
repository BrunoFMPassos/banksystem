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
    private String dataValidade;
    @Column(nullable=false, length=200)
    private String status;
    @Column(nullable=false, length=200)
    private String limite;
    @Column(nullable=false, length=200)
    private String senha;

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

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public TipoDeCartao getTipoDeCartao() {
        return tipoDeCartao;
    }


    public void setTipoDeCartao(TipoDeCartao tipoDeCartao) {
        this.tipoDeCartao = tipoDeCartao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLimite() {
        return limite;
    }

    public void setLimite(String limite) {
        this.limite = limite;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
