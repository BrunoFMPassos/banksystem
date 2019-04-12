package com.mycompany.vision;

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

public class Login extends WebPage {

    @SpringBean(name = "userService")
    private ServiceUser service;

    @SpringBean(name = "loginService")
    private ServiceLogin servicelogin;

    User user = new User();
    StatelessForm<User> form;
    TextField<String> username;
    PasswordTextField password;
    FeedbackPanel feedbackPanel;
    private List<User> listusers = new ArrayList<User>();
    String username_string;
    String password_string;

    public Login() {

        CompoundPropertyModel<User> compoundPropertyModelEmpresa = new CompoundPropertyModel<User>(user);

        //statelessform faz com que a sessão não expire após um determinado tempo na página de loggin
        form = new StatelessForm<User>("formulariologin", compoundPropertyModelEmpresa);

        add(form);
        form.add(addFeedbackPanel());
        form.add(addUsername());
        form.add(addPassword());
        form.add(addLoginButton());

    }

    private TextField<String> addUsername() {

        username = new RequiredTextField<String>("username");
        return username;
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
                username_string = username.getInput();
                password_string = password.getInput();

                servicelogin.loginValidate(target,username_string,password_string,listusers,user,feedbackPanel);

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
