package com.mycompany.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "agencia")
public class Agencia implements Serializable{

    @OneToMany(mappedBy = "agencia", targetEntity = Colaborador.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Colaborador> colaboradores;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false, length=200)
    private String cidade;
    @Column(nullable=false, length=200)
    private String UF;
    @Column(nullable=false, length=200)
    private String numero;
    @Transient
    private File  selectArquivo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<Colaborador> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(List<Colaborador> colaboradores) {
        this.colaboradores = colaboradores;
    }

    public File getSelectArquivo() {
        return selectArquivo;
    }

    public void setSelectArquivo(File selectArquivo) {
        this.selectArquivo = selectArquivo;
    }
}
