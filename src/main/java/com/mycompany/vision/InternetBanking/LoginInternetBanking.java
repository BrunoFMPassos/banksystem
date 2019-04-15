package com.mycompany.vision.InternetBanking;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceLogin;
import com.mycompany.control.ServiceUser;
import com.mycompany.model.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class LoginInternetBanking extends WebPage {


    User user = new User();
    StatelessForm<User> form;
    TextField<String> cpf;
    PasswordTextField password;
    FeedbackPanel feedbackPanel;
    private List<User> listusers = new ArrayList<User>();
    String username_string;
    String password_string;

    public LoginInternetBanking() {

        CompoundPropertyModel<User> compoundPropertyModelEmpresa = new CompoundPropertyModel<User>(user);

        //statelessform faz com que a sessão não expire após um determinado tempo na página de loggin
        form = new StatelessForm<User>("formulariologininternet", compoundPropertyModelEmpresa);

        add(form);
        form.add(addFeedbackPanel());
        form.add(addCpf());
        form.add(addPassword());
        form.add(addLoginButton());

    }

    private TextField<String> addCpf() {

        cpf = new RequiredTextField<String>("cpf");
        return cpf;
    }

    private PasswordTextField addPassword() {

        password = new PasswordTextField("password");
        return password;
    }

    private AjaxButton addLoginButton() {

        AjaxButton botaologin = new AjaxButton("btnlogin", form) {

            private static final long serialVersionUID = 9139263898606661858L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                super.onSubmit(target, form);
                System.out.println("Entrou no onSubmit!");
                username_string = cpf.getInput();
                password_string = password.getInput();

            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }

        };

        return botaologin;

    }

    private FeedbackPanel addFeedbackPanel() {

        feedbackPanel = new FeedbackPanel("message");
        feedbackPanel.setOutputMarkupId(true);
        return feedbackPanel;

    }

}
