package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceTipoDeConta;
import com.mycompany.model.TipoDeConta;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;


public class TipoDeContaPanel extends Panel {

    @SpringBean(name = "tipoDeContaService")
    ServiceTipoDeConta serviceTipoDeConta;

    private TipoDeConta tipoDeConta;

    Form<TipoDeConta> form;

    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");

    public TipoDeContaPanel(String id, TipoDeConta tipoDeConta) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.tipoDeConta = tipoDeConta;
        add(Criarcontainer());
    }

    private WebMarkupContainer Criarcontainer() {
        WebMarkupContainer container = new WebMarkupContainer("container");

        form = new Form<TipoDeConta>("formulariocadastrotipodeconta", new CompoundPropertyModel<TipoDeConta>(tipoDeConta));
        form.add(feedbackPanel);
        form.add(criarBtnSalvar());
        form.add(criarTextFieldBaseLimite());
        form.add(criarTextFieldDescricao());
        form.add(criarTextFieldTarifa());
        form.add(criarSelectPessoa());
        form.add(criarTextFieldTaxaDeTransferencia());
        container.add(form);
        return  container;

    }

    private AjaxButton criarBtnSalvar() {
        AjaxButton inserir = new AjaxButton("salvartipodeconta") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target, tipoDeConta);
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

    private RequiredTextField<String> criarTextFieldDescricao() {
        RequiredTextField<String> descricao = new RequiredTextField<String>("descricao");
        return descricao;
    }

    private TextField criarTextFieldTarifa() {
        TextField tarifa = new TextField("tarifa");
        tarifa.add(new AttributeModifier("onfocus", "$(this).mask('999.999');"));
        return tarifa;
    }

    private TextField criarTextFieldBaseLimite() {
        TextField baselimite = new TextField("baselimite");
        baselimite.add(new AttributeModifier("onfocus", "$(this).mask('999');"));
        return baselimite;
    }

    private TextField criarTextFieldTaxaDeTransferencia() {
        TextField baselimite = new TextField("taxaDeTransferencia");
        baselimite.add(new AttributeModifier("onfocus", "$(this).mask('99999');"));
        return baselimite;
    }


    private DropDownChoice<String> criarSelectPessoa() {

        final List<String> listaDePessoas = new ArrayList<String>();
        listaDePessoas.add("Física");
        listaDePessoas.add("Jurídica");


        ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("pessoa") {
            @Override
            public Object getDisplayValue(String pessoa) {
                // TODO Auto-generated method stub
                return pessoa;
            }
        };

        IModel<List<String>> IModellist = new LoadableDetachableModel<List<String>>() {

            @Override
            protected List<String> load() {
                // TODO Auto-generated method stub
                return listaDePessoas;
            }
        };

        DropDownChoice<String> selectPessoas = new DropDownChoice<String>("pessoa",IModellist, choiceRenderer);
        selectPessoas.setOutputMarkupId(true);
        return selectPessoas;
    }

    public void executaAoClicarEmSalvar(AjaxRequestTarget target, TipoDeConta tipoDeConta){

    }
}
