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
    private Long dataDeNascimento;
    @Column(nullable=false, length=200)
    private Long cpf;
    @Column(nullable=false, length=200)
    private Long rg;
    @Column(nullable=false, length=200)
    private String sexo;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Long dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Long getRg() {
        return rg;
    }

    public void setRg(Long rg) {
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
}
