package com.mycompany.model;

import javax.persistence.*;

@Entity
@Table(name = "contato")
public class Contato {

    @ManyToOne
    @JoinColumn(name="pessoafisica_id")
    private PessoaFisica pessoaFisica;

    @ManyToOne
    @JoinColumn(name="pessoajuridica_id")
    private PessoaJuridica pessoaJuridica;

    @ManyToOne
    @JoinColumn(name="conta_id")
    private Conta conta;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, length=200)
    private String apelido;

}
