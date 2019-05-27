package com.mycompany.vision;


import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceAgencia;
import com.mycompany.model.Agencia;
import com.mycompany.model.Colaborador;
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

public class AgenciaPanel extends Panel {

    @SpringBean(name = "agenciaService")
    ServiceAgencia serviceAgencia;

    private Agencia agencia;

    Form<Agencia> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");

    public AgenciaPanel(String id, Agencia agencia) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.agencia = agencia;
        add(Criarcontainer());
    }

    private WebMarkupContainer Criarcontainer() {
        WebMarkupContainer container = new WebMarkupContainer("container");

        form = new Form<Agencia>("formulariocadastroagencia", new CompoundPropertyModel<Agencia>(agencia));
        form.add(feedbackPanel);
        form.add(criarBtnSalvar());
        form.add(criarSelectUF());
        form.add(criarTextFieldCidade());
        form.add(criarTextFieldNumero());
        container.add(form);
        return  container;

    }

    private AjaxButton criarBtnSalvar() {
        AjaxButton inserir = new AjaxButton("salvaragencia") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target, agencia);
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

    private RequiredTextField<String> criarTextFieldNumero() {
        RequiredTextField<String> numero = new RequiredTextField<String>("numero");
        numero.setOutputMarkupId(true);
        numero.setRequired(true);
        return numero;
    }

    private DropDownChoice<String> criarSelectUF() {

        final List<String> listaDeEstados = new ArrayList<String>();
        listaDeEstados.add("AL");
        listaDeEstados.add("AP");
        listaDeEstados.add("AM");
        listaDeEstados.add("BA");
        listaDeEstados.add("CE");
        listaDeEstados.add("DF");
        listaDeEstados.add("ES");
        listaDeEstados.add("GO");
        listaDeEstados.add("MA");
        listaDeEstados.add("MT");
        listaDeEstados.add("MS");
        listaDeEstados.add("MG");
        listaDeEstados.add("PA");
        listaDeEstados.add("PB");
        listaDeEstados.add("PR");
        listaDeEstados.add("PE");
        listaDeEstados.add("PI");
        listaDeEstados.add("RJ");
        listaDeEstados.add("RN");
        listaDeEstados.add("RS");
        listaDeEstados.add("RO");
        listaDeEstados.add("RR");
        listaDeEstados.add("SC");
        listaDeEstados.add("SP");
        listaDeEstados.add("SE");
        listaDeEstados.add("TO");

        ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("UF") {
            @Override
            public Object getDisplayValue(String uf) {
                // TODO Auto-generated method stub
                return uf;
            }
        };

        IModel<List<String>> IModellist = new LoadableDetachableModel<List<String>>() {

            @Override
            protected List<String> load() {
                // TODO Auto-generated method stub
                return listaDeEstados;
            }
        };

        DropDownChoice<String> selectUF = new DropDownChoice<String>("UF",IModellist, choiceRenderer);
        selectUF.setOutputMarkupId(true);
        selectUF.setRequired(true);
        return selectUF;
    }

    private RequiredTextField<String> criarTextFieldCidade() {
        RequiredTextField<String> cidade = new RequiredTextField<String>("cidade");
        cidade.setOutputMarkupId(true);
        cidade.setRequired(true);
        return cidade;
    }


    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Agencia agencia) {

    }

}
