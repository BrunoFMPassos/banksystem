package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceConta;
import com.mycompany.control.ServiceOperacoes;
import com.mycompany.model.Conta;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OperacoesPanel extends Panel {

    @SpringBean(name = "contaService")
    ServiceConta serviceConta;
    @SpringBean(name = "operacoesService")
    ServiceOperacoes serviceOperacoes;

    private Conta conta = new Conta();
    Form<Conta> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");

    public OperacoesPanel(String id, Conta conta, String op) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.conta = conta;
        add(criarContainer(op));
    }

    public WebMarkupContainer criarContainer(String op){
        WebMarkupContainer container = new WebMarkupContainer("container");
        form = new Form<Conta>("formulariocadastroconta", new CompoundPropertyModel<Conta>(conta));
        form.setOutputMarkupId(true);
        form.add(feedbackPanel);
        form.add(criarBtnAdicionarContato(op));
        form.add(criarBtnFinalizar());
        form.add(criarLabelContaDestino(op));
        form.add(criarLabelDeposito(op));
        form.add(criarLabelSaque(op));
        form.add(criarLabelTransferencia(op));
        form.add(criarTextFieldDigito());
        form.add(criarTextFieldNumero());
        form.add(criarTextFieldNumeroContaDestino(op));
        form.add(criarTextFieldSenha());
        form.add(criarTextFieldValor());

        container.add(form);
        return container;
    }

    private AjaxButton criarBtnFinalizar(){
        AjaxButton finalizar = new AjaxButton("finalizar") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target,conta);
            }

        };
        finalizar.setOutputMarkupId(true);
        return finalizar;
    }

    private AjaxLink criarBtnAdicionarContato(String op){
        AjaxLink contato = new AjaxLink("adicionarContato") {
            @Override
            public void onClick(AjaxRequestTarget target) {

            }
        };
        contato.setOutputMarkupId(true);
        serviceOperacoes.ocultarAjaxLinkNaVisao(contato,op);
        return contato;
    }

    private Label criarLabelSaque(String op){
        Label labelSaque = new Label("labelSaque");
        serviceOperacoes.ocultarLabelNaVisaoParaSaque(labelSaque,op);;
        return labelSaque;
    }

    private Label criarLabelDeposito(String op){
        Label labelDeposito = new Label("labelDeposito");
        serviceOperacoes.ocultarLabelNaVisaoParaDeposito(labelDeposito,op);
        return labelDeposito;
    }

    private Label criarLabelTransferencia(String op){
        Label labelTransferencia = new Label("labelTransferencia");
        serviceOperacoes.ocultarLabelNaVisaoParaTransferencia(labelTransferencia,op);
        return labelTransferencia;
    }

    private Label criarLabelContaDestino(String op){
        Label labelContaDestino = new Label("labelContaDestino");
        serviceOperacoes.ocultarLabelNaVisaoParaTransferencia(labelContaDestino,op);
        return labelContaDestino;
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

    private PasswordTextField criarTextFieldSenha() {
        PasswordTextField senha = new PasswordTextField("senhaVerificar");
        return senha;
    }

    private TextField criarTextFieldNumeroContaDestino(String op) {
        TextField numeroContaDestino = new TextField("numeroContaDestino");
        serviceOperacoes.ocultarTextFieldNaVisao(numeroContaDestino,op);
        return numeroContaDestino;
    }


    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Conta conta) {

    }
}
