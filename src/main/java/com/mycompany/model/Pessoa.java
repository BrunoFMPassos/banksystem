package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Pessoa  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable=false, length=200)
    private String nome;
    @Column(nullable=false, length=200)
    private String data_de_nascimento;
    @Column(nullable=false, length=200)
    private String cpf;
    @Column(nullable=false, length=200)
    private String rg;
    @Column(nullable=false, length=200)
    private String sexo;
    @Column(nullable=false, length=200)
    private String cidade;
    @Column(nullable=false, length=200)
    private String UF;
    @Column(nullable=false, length=200)
    private String endereco;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataDeNascimento() {
        return data_de_nascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.data_de_nascimento = dataDeNascimento;
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
}
