package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.*;
import com.mycompany.model.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class ContaPanel extends Panel {

    @SpringBean(name = "agenciaService")
    ServiceAgencia serviceAgencia;
    @SpringBean(name = "contaService")
    ServiceConta serviceConta;
    @SpringBean(name = "tipoDeContaService")
    ServiceTipoDeConta serviceTipoDeConta;
    @SpringBean(name = "tipoDeCartaoService")
    ServiceTipoDeCartao serviceTipoDeCartao;
    @SpringBean(name = "pfService")
    ServicePF servicePF;
    @SpringBean(name = "pjService")
    ServicePJ servicePJ;

    private Conta conta = new Conta();
    Form<Conta> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");


    public ContaPanel(String id, Conta conta, Boolean editar) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.conta = conta;
        add(criarContainer(editar));
    }

    public WebMarkupContainer criarContainer(Boolean editar){
        WebMarkupContainer container = new WebMarkupContainer("container");
        form = new Form<Conta>("formulariocadastroconta", new CompoundPropertyModel<Conta>(conta));
        form.setOutputMarkupId(true);
        form.add(feedbackPanel);
        form.add(criarBtnSalvar());
        form.add(criarSelectAgencia());
        form.add(criarSelectTipoDeCartao());
        form.add(criarSelectTipoDeConta());
        form.add(criarSelectTitular());
        form.add(criarTextFieldDataDeAbertura());
        form.add(criarTextFieldSenha());
        form.add(criarTextFieldSenhaCartao());
        form.add(criarLabelNumero(editar));
        form.add(criarLabelSaldo(editar));
        form.add(criarLabelLimite(editar));
        form.add(criarLabelNumeroCartao(editar));
        form.add(criarLabelLimiteCartao(editar));
        form.add(criarLabelValidade(editar));
        form.add(criarTextFieldNumero(editar));
        form.add(criarTextFieldDigito(editar));
        form.add(criarTextFieldSaldo(editar));
        form.add(criarTextFieldLimite(editar));
        form.add(criarTextFieldNumeroCartao(editar));
        form.add(criarTextFieldLimiteCartao(editar));
        form.add(criarTextFieldDataValidadeCartao(editar));
        form.add(criarBtnDesativar(editar));
        container.add(form);
        return container;
    }

    private AjaxButton criarBtnSalvar(){
      AjaxButton inserir = new AjaxButton("salvarconta") {
          @Override
          protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target,conta);
          }
          @Override
          protected void onError(AjaxRequestTarget target, Form<?> form) {
              super.onError(target, form);
              target.add(feedbackPanel);
          }
      };
      inserir.setOutputMarkupId(true);
      return inserir;
    }

    private AjaxLink criarBtnDesativar(Boolean editar){
        AjaxLink desativar = new AjaxLink("desativarconta") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                serviceConta.desativarConta(conta,target,feedbackPanel);
            }
        };
        desativar.setOutputMarkupId(true);
        serviceConta.ocultaAjaxLinkNaVisao(desativar,editar);
        return desativar;
    }

    private Label criarLabelNumero(Boolean editar){
        Label labelNumero = new Label("labelNumero");
        serviceConta.ocultaLabelNaVisao(labelNumero,editar);
        return labelNumero;
    }

    private Label criarLabelSaldo(Boolean editar){
        Label labelSaldo = new Label("labelSaldo");
        serviceConta.ocultaLabelNaVisao(labelSaldo,editar);
        return labelSaldo;
    }

    private Label criarLabelLimite(Boolean editar){
        Label labelLimite = new Label("labelLimite");
        serviceConta.ocultaLabelNaVisao(labelLimite,editar);
        return labelLimite;
    }

    private Label criarLabelNumeroCartao(Boolean editar){
        Label labelNumeroCartao = new Label("labelNumeroCartao");
        serviceConta.ocultaLabelNaVisao(labelNumeroCartao,editar);
        return labelNumeroCartao;
    }

    private Label criarLabelLimiteCartao(Boolean editar){
        Label labelLimiteCartao = new Label("labelLimiteCartao");
        serviceConta.ocultaLabelNaVisao(labelLimiteCartao,editar);
        return labelLimiteCartao;
    }

    private Label criarLabelValidade(Boolean editar){
        Label labelValidade = new Label("labelValidade");
        serviceConta.ocultaLabelNaVisao(labelValidade,editar);
        return labelValidade;
    }

    private TextField criarTextFieldNumero(Boolean editar) {
        TextField numero = new TextField("numero");
        numero.setEnabled(false);
        numero.setOutputMarkupId(true);
        serviceConta.ocultaTextFieldNaVisao(numero,editar);
        return numero;
    }

    private TextField criarTextFieldDigito(Boolean editar) {
        TextField digito = new TextField("digito");
        digito.setEnabled(false);
        serviceConta.ocultaTextFieldNaVisao(digito,editar);
        return digito;
    }

    private TextField criarTextFieldSaldo(Boolean editar) {
        TextField saldo = new TextField("saldo");
        saldo.setEnabled(false);
        serviceConta.ocultaTextFieldNaVisao(saldo,editar);
        return saldo;
    }

    private TextField criarTextFieldLimite(Boolean editar) {
        TextField limite = new TextField("limiteConta");
        limite.setEnabled(false);
        serviceConta.ocultaTextFieldNaVisao(limite,editar);
        return limite;
    }

    private TextField criarTextFieldNumeroCartao(Boolean editar) {
        TextField numeroCartao = new TextField("numeroCartao");
        numeroCartao.setEnabled(false);
        serviceConta.ocultaTextFieldNaVisao(numeroCartao,editar);
        return numeroCartao;
    }


    private TextField criarTextFieldLimiteCartao(Boolean editar) {
        TextField limiteCartao = new TextField("limiteCartao");
        limiteCartao.setEnabled(false);
        serviceConta.ocultaTextFieldNaVisao(limiteCartao,editar);
        return limiteCartao;
    }

    private TextField criarTextFieldDataValidadeCartao(Boolean editar) {
        TextField dataValidadeCartao = new TextField("dataValidadeCartao");
        dataValidadeCartao.setEnabled(false);
        serviceConta.ocultaTextFieldNaVisao(dataValidadeCartao,editar);
        return dataValidadeCartao;
    }

    private DropDownChoice<TipoDeConta> criarSelectTipoDeConta() {

        TipoDeConta tipoDeConta = new TipoDeConta();
        final List<TipoDeConta> listaDeTiposDeContaPesquisa = serviceTipoDeConta.listarTiposDeConta(tipoDeConta);
        final List<String> listaDeTiposDeConta = new ArrayList<String>();
        for (TipoDeConta tipoDeContaLoop : listaDeTiposDeContaPesquisa) {
            listaDeTiposDeConta.add(tipoDeContaLoop.getDescricao());
        }

        ChoiceRenderer<TipoDeConta> choiceRenderer = new ChoiceRenderer<TipoDeConta>("descricao", "descricao") {
            @Override
            public Object getDisplayValue(TipoDeConta tipoDeConta) {
                // TODO Auto-generated method stub
                return tipoDeConta.getDescricao();
            }
        };
        IModel<List<TipoDeConta>> IModellist = new LoadableDetachableModel<List<TipoDeConta>>() {
            @Override
            protected List<TipoDeConta> load() {
                // TODO Auto-generated method stub
                return listaDeTiposDeContaPesquisa;
            }
        };

        DropDownChoice<TipoDeConta> selectTipoDeConta = new DropDownChoice<TipoDeConta>(
                "tipoDeConta",
                IModellist, choiceRenderer
        );
        selectTipoDeConta.setOutputMarkupId(true);
        return selectTipoDeConta;
    }


    private DropDownChoice<Agencia> criarSelectAgencia() {

        Agencia agencia = new Agencia();

        final List<Agencia> listaDeAgenciasPesquisa = serviceAgencia.pesquisarListaDeAgenciasPorAgencia(agencia);
        List<String> listaDeAgencias = new ArrayList<String>();
        for (Agencia agenciaLoop : listaDeAgenciasPesquisa) {
            listaDeAgencias.add(agenciaLoop.getNumero().toString());
        }
        ChoiceRenderer<Agencia> choiceRenderer = new ChoiceRenderer<Agencia>("numero", "id") {
            @Override
            public Object getDisplayValue(Agencia agencia) {
                // TODO Auto-generated method stub
                return agencia.getNumero();
            }
        };
        IModel<List<Agencia>> IModellist = new LoadableDetachableModel<List<Agencia>>() {
            @Override
            protected List<Agencia> load() {
                // TODO Auto-generated method stub
                return listaDeAgenciasPesquisa;
            }
        };

        DropDownChoice<Agencia> selectAgencia = new DropDownChoice<Agencia>(
                "agencia",
                IModellist, choiceRenderer
        );

        selectAgencia.setOutputMarkupId(true);
        return selectAgencia;

    }

    private DropDownChoice<String> criarSelectTitular() {


        final List<String> listaDePessoasPesquisa = new ArrayList<String>();
        serviceConta.pesquisarListaDePFePJ(listaDePessoasPesquisa);

        ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("titular") {
            @Override
            public Object getDisplayValue(String titular) {
                // TODO Auto-generated method stub
                return titular;
            }
        };
        IModel<List<String>> IModellist = new LoadableDetachableModel<List<String>>() {
            @Override
            protected List<String> load() {
                // TODO Auto-generated method stub
                return listaDePessoasPesquisa;
            }
        };

        DropDownChoice<String> selectTitular = new DropDownChoice<String>(
                "titular",
                IModellist, choiceRenderer
        );


        selectTitular.setOutputMarkupId(true);
        return selectTitular;

    }

    private DateTextField criarTextFieldDataDeAbertura(){
        DatePicker datePicker = new DatePicker(){
            private static final long serialVersionUID = 1L;
            @Override
            protected boolean alignWithIcon() {
                return true;
            }
            @Override
            protected boolean enableMonthYearSelection() {
                return true;
            }
        };

        DateTextField data = new DateTextField("dataAbertura");
        data.add(datePicker);
        data.add(new AttributeModifier("onfocus", "$(this).mask('99/99/99');"));
        return data;
    }

    private TextField criarTextFieldSenha() {
        TextField senha = new TextField("senha");
        senha.add(new AttributeModifier("onfocus", "$(this).mask('999999');"));
        return senha;
    }


    private DropDownChoice<String> criarSelectTipoDeCartao() {

        TipoDeCartao tipoDeCartao = new TipoDeCartao();

        final List<TipoDeCartao> listaDeTiposDeCartaoPesquisa = serviceTipoDeCartao.listarTiposDeCartao(tipoDeCartao);
        final List<String> listaDeTiposDeCartao = new ArrayList<String>();
        for (TipoDeCartao tipoDeCartaoLoop : listaDeTiposDeCartaoPesquisa) {
            listaDeTiposDeCartao.add(tipoDeCartaoLoop.getDescricao());
        }
        ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("descricao") {
            @Override
            public Object getDisplayValue(String descricao) {
                // TODO Auto-generated method stub
                return descricao;
            }
        };
        IModel<List<String>> IModellist = new LoadableDetachableModel<List<String>>() {
            @Override
            protected List<String> load() {
                // TODO Auto-generated method stub
                return listaDeTiposDeCartao;
            }
        };

        DropDownChoice<String> selectTipoDeCartao = new DropDownChoice<String>(
                "tipoDeCartao",
                IModellist, choiceRenderer
        );


        selectTipoDeCartao.setOutputMarkupId(true);
        return selectTipoDeCartao;

    }

    private TextField criarTextFieldSenhaCartao(){
        TextField senhaCartao = new TextField("senhaCartao");
        senhaCartao.add(new AttributeModifier("onfocus", "$(this).mask('9999');"));
        return senhaCartao;
    }


    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Conta conta) {

    }

}
