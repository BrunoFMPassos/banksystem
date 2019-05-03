package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Pessoa  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable=false, length=200)
    private String nome;
    @Column(nullable=false, length=200)
    private String dataDeNascimento;
    @Column(nullable=false, length=200)
    private String cpf;
    @Column(nullable=true, length=200)
    private String rg;
    @Column(nullable=false, length=200)
    private String sexo;
    @Column(nullable=false, length=200)
    private String cidade;
    @Column(nullable=false, length=200)
    private String cep;
    @Column(nullable=false, length=200)
    private String UF;
    @Column(nullable=false, length=200)
    private String endereco;
    @Column(nullable=false, length=200)
    private String bairro;
    @Column(nullable=true, length=200)
    private String complemento;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
