package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "movimentacao")
public class Movimentacao implements Serializable {

    @ManyToOne
    @JoinColumn(name="conta_id")
    private Conta conta;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false, length=200)
    private String numero;
    @Column(nullable=false, length=200)
    private String descricao;
    @Column(nullable=false, length=200)
    private String valor;
    @Column(nullable=false, length=200)
    private String data;
    @Column(nullable=true, length=200)
    private String contaDestino;
    @Transient
    private String tipoFiltrar;
    @Transient
    private String contaFiltrar;
    @Transient
    private String contaString;


    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipoFiltrar() {
        return tipoFiltrar;
    }

    public void setTipoFiltrar(String tipoFiltrar) {
        this.tipoFiltrar = tipoFiltrar;
    }

    public String getContaFiltrar() {
        return contaFiltrar;
    }

    public void setContaFiltrar(String contaFiltrar) {
        this.contaFiltrar = contaFiltrar;
    }

    public String getContaString() {
        return contaString;
    }

    public void setContaString(String contaString) {
        this.contaString = contaString;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }
}
