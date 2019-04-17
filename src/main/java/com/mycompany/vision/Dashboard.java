package com.mycompany.vision;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;


public class Dashboard extends BasePage {
    Form form;


    public Dashboard() {
        // CompoundPropertyModel<Objeto> compoundPropertyModelDash = new CompoundPropertyModel<Objeto>(objeto);
        pageCreate();
    }

    public void pageCreate() {
        form = new Form("formulariodash") {

            @Override
            public void onSubmit() {

            }
        };
        Label titulo = new Label("titulo", "SystemBank");
        add(form);
        form.add(titulo);
    }


}
