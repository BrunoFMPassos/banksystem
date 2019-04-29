package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.model.Colaborador;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;


public class ModalExcluir extends Panel {
    Form<Colaborador> formexcluir;
    private Colaborador colaborador;

    public ModalExcluir(String id) {
        super(id);
        add(CriarContainer());
    }

    private WebMarkupContainer CriarContainer() {
        WebMarkupContainer container = new WebMarkupContainer("containerExcluir");

        formexcluir = new Form<Colaborador>("formexcluir", new CompoundPropertyModel<Colaborador>(colaborador));
        formexcluir.add(mostrarValorASerExcluido(colaborador));
        formexcluir.add(criarBtnSim());
        formexcluir.add(criarBtnNao());
        container.add(formexcluir);

        return container;
    }

    private AjaxLink<Void> criarBtnSim() {

        AjaxLink<Void> sim = new AjaxLink<Void>("sim") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                excluir(target, colaborador);
            }

        };
        return sim;
    }

    private AjaxLink<Void> criarBtnNao() {

        AjaxLink<Void> nao = new AjaxLink<Void>("nao") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                fecharSemExcluir(target, colaborador);
            }

        };
        return nao;
    }

    public void excluir(AjaxRequestTarget target, Colaborador colaborador) {

    }

    public void fecharSemExcluir(AjaxRequestTarget target, Colaborador colaborador) {

    }

    public Label mostrarValorASerExcluido(Colaborador colaborador){
        Label valoraserexcluido = new Label("valor", colaborador.getNome());
        return valoraserexcluido;
    }


}
