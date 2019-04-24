package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.model.Colaborador;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

public class ModalColaborador extends Panel {

    private Colaborador colaborador;

    Form<Colaborador> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");

    public ModalColaborador(String id, Colaborador colaborador) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.colaborador = colaborador;
        add(Criarcontainer());
    }

    private WebMarkupContainer Criarcontainer() {

        WebMarkupContainer container = new WebMarkupContainer("container");

        form = new Form<Colaborador>("formulariocadastrocolaborador", new CompoundPropertyModel<Colaborador>(colaborador));
        form.setOutputMarkupId(true);
        form.add(criarBtnSalvar());
        form.setMultiPart(true);
        form.add(feedbackPanel);
        form.add(criarTextFieldNome());
        form.add(criarTextFieldCpf());
        form.add(criarTextFieldRg());
        form.add(criarTextFieldDataDeNascimento());
        container.add(form);
        return container;
    }

    private AjaxButton criarBtnSalvar() {
        AjaxButton inserir = new AjaxButton("salvarcolaborador") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                System.out.println("Clicou no save");
                executaAoClicarEmSalvar(target, colaborador);
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

    private TextField<String> criarTextFieldNome(){
        TextField<String> nome = new TextField<String>("nome");
        return nome;
    }

    private TextField<String> criarTextFieldCpf(){
        TextField<String> cpf = new TextField<String>("cpf");
        return cpf;
    }

    private TextField<String> criarTextFieldRg(){
        TextField<String> rg = new TextField<String>("rg");
        return rg;
    }

    private TextField<String> criarTextFieldDataDeNascimento(){
        TextField<String> dataDeNascimento = new TextField<String>("dataDeNascimento");
        return dataDeNascimento;
    }

    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Colaborador colaborador) {

    }

}
