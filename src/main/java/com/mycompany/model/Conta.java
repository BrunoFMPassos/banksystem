package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "conta")
public class Conta implements Serializable {

    @OneToMany(mappedBy = "conta", targetEntity = Movimentacao.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)

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
    private String senha;
    @Column(nullable = false, length = 200)
    private String saldo;
    @Column(nullable = false, length = 200)
    private Long verificador;
    @Column(nullable=false, length=200)
    private String status;
    @Column(nullable=false, length=200)
    private String dataAbertura;
    @Column(nullable=false, length=200)
    private String limiteConta;
    @Transient
    private String senhaCartao;
    @Transient
    private String tipoDeCartao;
    @Transient
    private String titular;
    @Transient
    private String agenciaFiltrar;
    @Transient
    private String tipoDeContaFiltrar;
    @Transient
    private String numeroCartao;
    @Transient
    private String cvvCartao;
    @Transient
    private String limiteCartao;
    @Transient
    private String dataValidadeCartao;
    @Transient
    private String labelNumero = "Numero:";
    @Transient
    private String labelSaldo = "Saldo:";
    @Transient
    private String labelLimite = "Limite:";
    @Transient
    private String labelNumeroCartao = "Numero Cartão:";
    @Transient
    private String labelLimiteCartao = "Limite:";
    @Transient
    private String labelValidade= "Validade:";



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

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getLimiteConta() {
        return limiteConta;
    }

    public void setLimiteConta(String limiteConta) {
        this.limiteConta = limiteConta;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAgenciaFiltrar() {
        return agenciaFiltrar;
    }

    public void setAgenciaFiltrar(String agenciaFiltrar) {
        this.agenciaFiltrar = agenciaFiltrar;
    }

    public String getTipoDeContaFiltrar() {
        return tipoDeContaFiltrar;
    }

    public void setTipoDeContaFiltrar(String tipoDeContaFiltrar) {
        this.tipoDeContaFiltrar = tipoDeContaFiltrar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSenhaCartao() {
        return senhaCartao;
    }

    public void setSenhaCartao(String senhaCartao) {
        this.senhaCartao = senhaCartao;
    }

    public String getTipoDeCartao() {
        return tipoDeCartao;
    }

    public void setTipoDeCartao(String tipoDeCartao) {
        this.tipoDeCartao = tipoDeCartao;
    }

    public Long getVerificador() {
        return verificador;
    }

    public void setVerificador(Long verificador) {
        this.verificador = verificador;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getCvvCartao() {
        return cvvCartao;
    }

    public void setCvvCartao(String cvvCartao) {
        this.cvvCartao = cvvCartao;
    }

    public String getLimiteCartao() {
        return limiteCartao;
    }

    public void setLimiteCartao(String limiteCartao) {
        this.limiteCartao = limiteCartao;
    }

    public String getDataValidadeCartao() {
        return dataValidadeCartao;
    }

    public void setDataValidadeCartao(String dataValidadeCartao) {
        this.dataValidadeCartao = dataValidadeCartao;
    }

    public String getLabelNumero() {
        return labelNumero;
    }

    public void setLabelNumero(String labelNumero) {
        this.labelNumero = labelNumero;
    }
}
