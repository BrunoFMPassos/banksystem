package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.model.Colaborador;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;


public class PanelExcluir<T extends Object> extends Panel {
    Form<T> formexcluir;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");
    private T t;

    public PanelExcluir(String id) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        add(CriarContainer());
    }

    private WebMarkupContainer CriarContainer() {
        WebMarkupContainer container = new WebMarkupContainer("containerExcluir");

        formexcluir = new Form<T>("formexcluir", new CompoundPropertyModel<T>(t));
        formexcluir.add(feedbackPanel);
        formexcluir.add(mostrarValorASerExcluido(""));
        formexcluir.add(criarBtnSim());
        formexcluir.add(criarBtnNao());
        container.add(formexcluir);

        return container;
    }

    private AjaxLink<Void> criarBtnSim() {

        AjaxLink<Void> sim = new AjaxLink<Void>("sim") {


            @Override
            public void onClick(AjaxRequestTarget target) {
                excluir(target,t);
            }

        };
        return sim;
    }

    private AjaxLink<Void> criarBtnNao() {

        AjaxLink<Void> nao = new AjaxLink<Void>("nao") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                fecharSemExcluir(target, t);
            }

        };
        return nao;
    }

    public void excluir(AjaxRequestTarget target, T t) {

    }

    public void fecharSemExcluir(AjaxRequestTarget target, T t) {

    }

    public Label mostrarValorASerExcluido(String string){
        Label valoraserexcluido = new Label("valor",string);
        return valoraserexcluido;
    }


}
