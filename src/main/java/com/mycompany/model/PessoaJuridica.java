package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "pessoa_juridica")
public class PessoaJuridica  implements Serializable {

    @OneToMany(mappedBy = "pessoaJuridica", targetEntity = Conta.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    private List<Conta> conta;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable=false, length=200)
    private String razaoSocial;
    @Column(nullable=false, length=200)
    private String nomeFantasia;
    @Column(nullable=false, length=200)
    private Long cnpj;
    @Column(nullable=false, length=200)
    private Long inscricaoEstadual;
    @Column(nullable=false, length=200)
    private Long Telefone;
    @Column(nullable=false, length=200)
    private String cidade;
    @Column(nullable=false, length=200)
    private String UF;
    @Column(nullable=false, length=200)
    private String enderecoDesc;
    @Column(nullable=false, length=200)
    private Integer numero;
    @Column(nullable=true, length=200)
    private String complemento;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public Long getCnpj() {
        return cnpj;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public Long getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(Long inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public Long getTelefone() {
        return Telefone;
    }

    public void setTelefone(Long telefone) {
        Telefone = telefone;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        this.UF = UF;
    }

    public String getEnderecoDesc() {
        return enderecoDesc;
    }

    public void setEnderecoDesc(String enderecoDesc) {
        this.enderecoDesc = enderecoDesc;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public List<Conta> getConta() {
        return conta;
    }

    public void setConta(List<Conta> conta) {
        this.conta = conta;
    }
}
