package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceConta;
import com.mycompany.control.ServiceContato;
import com.mycompany.control.ServiceOperacoes;
import com.mycompany.model.Conta;
import com.mycompany.model.Contato;
import com.mycompany.model.Movimentacao;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class OperacoesPanel extends Panel {

    @SpringBean(name = "contaService")
    ServiceConta serviceConta;
    @SpringBean(name = "operacoesService")
    ServiceOperacoes serviceOperacoes;
    @SpringBean(name = "contatoService")
    ServiceContato serviceContato;

    private Conta conta = new Conta();
    Form<Conta> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");
    FeedbackPanel feedbackPanelSuccess = new FeedbackPanel("feedbackpanelsuccess");
    DropDownChoice<Contato> selectContatos;
    TextField apelido;
    TextField numeroContaDestino;
    TextField numeroBanco;
    WebMarkupContainer container;

    AjaxButton finalizar;
    Link comprovante;
    AjaxLink fechar;

    public OperacoesPanel(String id, Conta conta, String op) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanelSuccess.setOutputMarkupId(true);
        this.conta = conta;
        add(criarContainer(op));
    }

    public WebMarkupContainer criarContainer(String op) {
        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        form = new Form<Conta>("formulariocadastroconta", new CompoundPropertyModel<Conta>(conta));
        form.setOutputMarkupId(true);
        form.add(feedbackPanel);
        form.add(feedbackPanelSuccess);
        form.add(criarBtnAdicionarContato(op));
        form.add(criarBtnFinalizar(op));
        form.add(criarLabelContaDestino(op));
        form.add(criarLabelDeposito(op));
        form.add(criarLabelSaque(op));
        form.add(criarLabelTransferencia(op));
        form.add(criarLabelContato(op));
        form.add(criarTextFieldDigito());
        form.add(criarTextFieldNumero());
        form.add(criarTextFieldNumeroContaDestino(op));
        form.add(criarTextFieldNumeroBanco(op));
        form.add(criarTextFieldApelido(op));
        form.add(criarSelectContato(op));
        form.add(criarTextFieldSenha(op));
        form.add(criarTextFieldValor());
        form.add(criarBtnComprovante());
        form.add(criarBtnFechar());


        container.add(form);
        return container;
    }

    private AjaxButton criarBtnFinalizar(final String op) {
            finalizar = new AjaxButton("finalizar") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target, conta);
                serviceOperacoes.preparaVisãoParaEmitirComprovante(finalizar,comprovante,fechar,op,feedbackPanel);
                target.add(container);
            }

        };
        finalizar.setOutputMarkupId(true);
        return finalizar;
    }

    private AjaxLink criarBtnAdicionarContato(String op) {
        AjaxLink contato = new AjaxLink("adicionarContato") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String apelidoString = apelido.getValue();
                String numeroContaDestinoString = numeroContaDestino.getValue();
                String numeroBancoString = numeroBanco.getValue();
                serviceOperacoes.inserirContato(conta, apelidoString, numeroContaDestinoString, numeroBancoString, target, selectContatos, feedbackPanel);
            }
        };
        contato.setOutputMarkupId(true);
        serviceOperacoes.ocultarAjaxLinkNaVisaoParaTransferencia(contato, op);
        return contato;
    }
    
    private Link criarBtnComprovante(){
         comprovante = new Link("comprovante") {
            @Override
            public void onClick() {

                serviceOperacoes.emiteComprovante(conta);
            }
        };
         comprovante.setOutputMarkupId(true);
        comprovante.setVisible(false);
        return comprovante;
    }

    private AjaxLink criarBtnFechar(){
           fechar = new AjaxLink("fechar") {
               @Override
               public void onClick(AjaxRequestTarget target) {
                   executaAoClicarEmFechar(target);
               }
           };
        fechar.setVisible(false);
        fechar.setOutputMarkupId(true);
        return fechar;
    }

    private Label criarLabelSaque(String op) {
        Label labelSaque = new Label("labelSaque");
        serviceOperacoes.ocultarLabelNaVisaoParaSaque(labelSaque, op);
        ;
        return labelSaque;
    }

    private Label criarLabelDeposito(String op) {
        Label labelDeposito = new Label("labelDeposito");
        serviceOperacoes.ocultarLabelNaVisaoParaDeposito(labelDeposito, op);
        return labelDeposito;
    }

    private Label criarLabelTransferencia(String op) {
        Label labelTransferencia = new Label("labelTransferencia");
        serviceOperacoes.ocultarLabelNaVisaoParaTransferencia(labelTransferencia, op);
        return labelTransferencia;
    }

    private Label criarLabelContaDestino(String op) {
        Label labelContaDestino = new Label("labelContaDestino");
        serviceOperacoes.ocultarLabelNaVisaoParaTransferencia(labelContaDestino, op);
        return labelContaDestino;
    }

    private Label criarLabelContato(String op) {
        Label labelContato = new Label("labelContato");
        serviceOperacoes.ocultarLabelNaVisaoParaTransferencia(labelContato, op);
        return labelContato;
    }

    private TextField criarTextFieldNumero() {
        TextField numero = new TextField("numero");
        numero.setEnabled(false);
        numero.setOutputMarkupId(true);
        return numero;
    }

    private TextField criarTextFieldDigito() {
        TextField digito = new TextField("digito");
        digito.setEnabled(false);
        return digito;
    }

    private TextField criarTextFieldValor() {
        TextField valor = new TextField("valor");
        return valor;
    }

    private PasswordTextField criarTextFieldSenha(String op) {
        PasswordTextField senha = new PasswordTextField("senhaVerificar");
        senha.add(new AttributeModifier("onfocus", "$(this).mask('999999');"));
        serviceOperacoes.ocultarCampoSenhaParDeposito(senha,op);
        return senha;
    }

    private TextField criarTextFieldNumeroContaDestino(String op) {
        numeroContaDestino = new TextField("numeroContaDestino");
        AjaxFormComponentUpdatingBehavior  updating = new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        };
        numeroContaDestino.add(updating);
        serviceOperacoes.ocultarTextFieldNaVisao(numeroContaDestino, op);
        numeroContaDestino.add(new AttributeModifier("onfocus", "$(this).mask('99999');"));
        return numeroContaDestino;
    }

    private TextField criarTextFieldNumeroBanco(String op) {
        numeroBanco = new TextField("numeroBanco");
        AjaxFormComponentUpdatingBehavior  updating = new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        };
        numeroBanco.add(updating);
        serviceOperacoes.ocultarTextFieldNaVisao(numeroBanco, op);
        numeroBanco.add(new AttributeModifier("onfocus", "$(this).mask('999');"));
        return numeroBanco;
    }

    private TextField criarTextFieldApelido(String op) {
        apelido = new TextField("apelidoContato");
        AjaxFormComponentUpdatingBehavior  updating = new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        };
        apelido.add(updating);
        serviceOperacoes.ocultarTextFieldNaVisao(apelido, op);
        return apelido;
    }

    private DropDownChoice<Contato> criarSelectContato(String op) {

        Contato contato = new Contato();
        final List<Contato> listaDeContatos = serviceContato.pesquisaListaDeContatosPorConta(contato, conta);

        ChoiceRenderer<Contato> choiceRenderer = new ChoiceRenderer<Contato>("apelido") {
            @Override
            public Object getDisplayValue(Contato contato) {
                // TODO Auto-generated method stub
                return contato.getApelido();
            }
        };

        IModel<List<Contato>> IModellist = new LoadableDetachableModel<List<Contato>>() {
            @Override
            protected List<Contato> load() {
                // TODO Auto-generated method stub
                return listaDeContatos;
            }
        };

        selectContatos = new DropDownChoice<Contato>(
                "contatoObjeto",
                IModellist, choiceRenderer
        ) {
            @Override
            protected String getNullValidDisplayValue() {
                return "Selecione...";
            }
        };
        selectContatos.setOutputMarkupId(true);
        selectContatos.setNullValid(true);
        serviceOperacoes.ocultarDropDownChoicedNaVisao(selectContatos, op);
        return selectContatos;
    }


    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Conta conta) {

    }

    public void executaAoClicarEmFechar(AjaxRequestTarget target) {

    }


}
