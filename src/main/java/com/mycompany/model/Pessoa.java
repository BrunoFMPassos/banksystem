package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable=false, length=200)
    private String nome;
    @Column(nullable=false, length=200)
    private Long data_nascimento;
    @Column(nullable=false, length=200)
    private Long cpf;
    @Column(nullable=false, length=200)
    private Long rg;
    @Column(nullable=false, length=200)
    private String sexo;


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

    public Long getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Long data_nascimento) {
        this.data_nascimento = data_nascimento;
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
}
