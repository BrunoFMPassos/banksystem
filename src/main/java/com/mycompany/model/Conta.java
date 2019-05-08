package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "conta")
public class Conta implements Serializable {

    @ManyToOne
    @JoinColumn(name="pessoafisica_id")
    private PessoaFisica pessoaFisica;

    @ManyToOne
    @JoinColumn(name="pessoajuridica_id")
    private PessoaJuridica pessoaJuridica;

    @ManyToOne
    @JoinColumn(name="agencia_id")
    private Agencia agencia;

    @ManyToOne
    @JoinColumn(name="cartao_id")
    private Cartao cartao;

    @ManyToOne
    @JoinColumn(name="tipoDeConta_id")
    private TipoDeConta tipoDeConta;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false, length=200)
    private Long numero;
    @Column(nullable=false, length=200)
    private Integer digito;
    @Column(nullable=false, length=200)
    private Integer senha;
    @Column(nullable = false, length = 200)
    private Long saldo;
    @Column(nullable = false, length = 200)
    private Long tarifa;
    @Column(nullable=false, length=200)
    private String status;
    @Column(nullable=false, length=200)
    private String dataAbertura;
    @Column(nullable=false, length=200)
    private Long limiteConta;



    public PessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(PessoaFisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public PessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public TipoDeConta getTipoDeConta() {
        return tipoDeConta;
    }

    public void setTipoDeConta(TipoDeConta tipoDeConta) {
        this.tipoDeConta = tipoDeConta;
    }

    public Long getLimiteConta() {
        return limiteConta;
    }

    public void setLimiteConta(Long limiteConta) {
        this.limiteConta = limiteConta;
    }

    public Integer getSenha() {
        return senha;
    }

    public void setSenha(Integer senha) {
        this.senha = senha;
    }

    public Long getSaldo() {
        return saldo;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTarifa() {
        return tarifa;
    }

    public void setTarifa(Long tarifa) {
        this.tarifa = tarifa;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Integer getDigito() {
        return digito;
    }

    public void setDigito(Integer digito) {
        this.digito = digito;
    }


}
