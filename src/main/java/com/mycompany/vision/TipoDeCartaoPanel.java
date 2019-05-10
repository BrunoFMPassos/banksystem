package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceTipoDeCartao;
import com.mycompany.model.TipoDeCartao;
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

public class TipoDeCartaoPanel extends Panel {
    @SpringBean(name = "tipoDeCartaoService")
    ServiceTipoDeCartao serviceTipoDeCartao;

    private TipoDeCartao tipoDeCartao;

    Form<TipoDeCartao> form;

    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");

    public TipoDeCartaoPanel(String id, TipoDeCartao tipoDeCartao) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.tipoDeCartao = tipoDeCartao;
        add(Criarcontainer());
    }

    private WebMarkupContainer Criarcontainer() {
        WebMarkupContainer container = new WebMarkupContainer("container");

        form = new Form<TipoDeCartao>("formulariocadastrotipodecartao", new CompoundPropertyModel<TipoDeCartao>(tipoDeCartao));
        form.add(feedbackPanel);
        form.add(criarBtnSalvar());
        form.add(criarTextFieldBaseLimite());
        form.add(criarTextFieldDescricao());
        form.add(criarTextFieldTarifa());
        form.add(criarSelectPessoa());
        container.add(form);
        return  container;

    }

    private AjaxButton criarBtnSalvar() {
        AjaxButton inserir = new AjaxButton("salvartipodecartao") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target, tipoDeCartao);
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

    private NumberTextField criarTextFieldTarifa() {
        NumberTextField tarifa = new NumberTextField("tarifa");
        return tarifa;
    }

    private NumberTextField criarTextFieldBaseLimite() {
        NumberTextField baselimite = new NumberTextField("baselimite");
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

    public void executaAoClicarEmSalvar(AjaxRequestTarget target, TipoDeCartao tipoDeCartao){

    }
}
