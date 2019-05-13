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
    private String baselimite;
    @Column(nullable=false, length=200)
    private String Tarifa;
    @Column(nullable=false, length=200)
    private String pessoa;

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

    public String getBaselimite() {
        return baselimite;
    }

    public void setBaselimite(String baselimite) {
        this.baselimite = baselimite;
    }

    public String getTarifa() {
        return Tarifa;
    }

    public void setTarifa(String tarifa) {
        Tarifa = tarifa;
    }

    public String getPessoa() {
        return pessoa;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }

}
