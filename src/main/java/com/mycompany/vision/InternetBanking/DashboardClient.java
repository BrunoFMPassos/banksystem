package com.mycompany.vision.InternetBanking;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.vision.BasePage;
import com.mycompany.vision.Login;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;


public class DashboardClient extends BasePage {
    Form form;


    public DashboardClient() {
        // CompoundPropertyModel<Objeto> compoundPropertyModelDash = new CompoundPropertyModel<Objeto>(objeto);
        pageCreate();
    }

    public void pageCreate(){
        form = new Form("formulariodash") {

            @Override
            public void onSubmit() {

            }
        };
        Label titulo = new Label("titulo", "Empresa");
        add(form);
        form.add(titulo);
        form.add(addLogoutButton());
    }

    private AjaxButton addLogoutButton() {

        AjaxButton botaologout = new AjaxButton("logoutBtn", form) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                System.out.println("Entrou no onSubmit logout!");
                getSession().invalidate();
                setResponsePage(Login.class);
            }
        };

        return botaologout;
    }

}
