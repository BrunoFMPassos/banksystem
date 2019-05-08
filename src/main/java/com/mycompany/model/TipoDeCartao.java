package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TipoDeCartao")
public class TipoDeCartao implements Serializable {

    @OneToMany(mappedBy = "tipoDeCartao", targetEntity = Cartao.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    private List<Cartao> cartoes;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable=false, length=200)
    private String descricao;
    @Column(nullable=false, length=200)
    private Double BaseLimite;
    @Column(nullable=false, length=200)
    private Double Tarifa;

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
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

    public Double getBaseLimite() {
        return BaseLimite;
    }

    public void setBaseLimite(Double baseLimite) {
        BaseLimite = baseLimite;
    }

    public Double getTarifa() {
        return Tarifa;
    }

    public void setTarifa(Double tarifa) {
        Tarifa = tarifa;
    }
}
