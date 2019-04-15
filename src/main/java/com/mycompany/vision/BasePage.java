package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;

public class BasePage extends WebPage {

    public BasePage() {
        add(addLogoutButton());
    }

    private AjaxLink addLogoutButton() {

        AjaxLink botaologout = new AjaxLink("logoutBtn") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("Entrou no onSubmit logout!");
                getSession().invalidate();
                setResponsePage(Login.class);
            }

        };
        return botaologout;

    }
}
