package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "pessoa_fisica")
public class PessoaFisica extends Pessoa implements Serializable {

    @OneToMany(mappedBy = "pessoaFisica", targetEntity = Conta.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    private List<Conta> conta;

    @Column(nullable=false, length=200)
    private Long rendaMensal;

    @Column(nullable=false, length=200)
    private String telefone;

    public List<Conta> getConta() {
        return conta;
    }

    public void setConta(List<Conta> conta) {
        this.conta = conta;
    }

    public Long getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(Long rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
