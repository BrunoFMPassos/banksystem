package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TipoDeConta")
public class TipoDeConta implements Serializable {

    @OneToMany(mappedBy = "tipoDeConta", targetEntity = Conta.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Conta> contas;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable=false, length=200)
    private String descricao;
    @Column(nullable=false, length=200)
    private Double baselimite;
    @Column(nullable=false, length=200)
    private Double tarifa;
    @Column(nullable=false, length=200)
    private String pessoa;


    public List<Conta> getContas() {
        return contas;
    }

    public String getPessoa() {
        return pessoa;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }

    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getBaselimite() {
        return baselimite;
    }

    public void setBaselimite(Double baselimite) {
        this.baselimite = baselimite;
    }

    public Double getTarifa() {
        return tarifa;
    }

    public void setTarifa(Double tarifa) {
        this.tarifa = tarifa;
    }
}
