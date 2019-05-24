package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.model.Conta;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

public class DesativarContaPanel extends Panel {

    Form formDesativar;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");
    private Conta conta;

    public DesativarContaPanel(String id, Conta conta) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        add(CriarContainer());
    }

    private WebMarkupContainer CriarContainer() {
        WebMarkupContainer container = new WebMarkupContainer("containerExcluir");

        formDesativar = new Form("formexcluir", new CompoundPropertyModel(conta));
        formDesativar.add(feedbackPanel);
        formDesativar.add(criarBtnSim());
        formDesativar.add(criarBtnNao());
        container.add(formDesativar);

        return container;
    }

    private AjaxLink<Void> criarBtnSim() {

        AjaxLink<Void> sim = new AjaxLink<Void>("sim") {


            @Override
            public void onClick(AjaxRequestTarget target) {
                desativar(target,conta);
            }

        };
        return sim;
    }

    private AjaxLink<Void> criarBtnNao() {

        AjaxLink<Void> nao = new AjaxLink<Void>("nao") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                fecharSemDesativar(target,conta);
            }

        };
        return nao;
    }

    public void desativar(AjaxRequestTarget target, Conta conta) {

    }

    public void fecharSemDesativar(AjaxRequestTarget target, Conta conta) {

    }

}
