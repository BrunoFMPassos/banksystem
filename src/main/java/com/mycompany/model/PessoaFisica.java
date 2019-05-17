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

    @OneToMany(mappedBy = "pessoaFisica", targetEntity = Contato.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contato> contato;

    @Column(nullable=false, length=200)
    private String rendaMensal;

    @Column(nullable=false, length=200)
    private String telefone;

    public List<Conta> getConta() {
        return conta;
    }

    public void setConta(List<Conta> conta) {
        this.conta = conta;
    }

    public String getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(String rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
