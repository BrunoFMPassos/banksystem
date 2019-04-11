package com.mycompany.vision;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.DAO.DaoUser;
import com.mycompany.model.User;

public class Login extends WebPage {

	private static final long serialVersionUID = 3634362543497996554L;
	User user = new User();
	DaoUser<User> gd = new DaoUser<User>();
	Form<User> form;
	TextField<String> username;
	TextField<String> password;
	FeedbackPanel feedbackPanel;
	private List<User> listusers = new ArrayList<User>();
	String username_string;
	String password_string;

	public Login() {

		CompoundPropertyModel<User> compoundPropertyModelEmpresa = new CompoundPropertyModel<User>(
				user);

		form = new Form<User>("formulariologin", compoundPropertyModelEmpresa);

		add(form);
		form.add(addFeedbackPanel());
		form.add(addUsername());
		form.add(addPassword());
		form.add(addLoginButton());

	}

	public TextField<String> addUsername() {

		username = new RequiredTextField<String>("username");
		return username;
	}

	public TextField<String> addPassword() {

		password = new RequiredTextField<String>("password");
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

				if (username_string.isEmpty() == false
						&& password_string.isEmpty() == false) {
					listusers.clear();
					user = gd.searchForUserName(username_string);

					if (user.getPassword().equals(password_string)) {
						System.out.println("Usuário Autorizado!");
						System.out.println(user.getPerfil());
					} else {
						feedbackPanel.error("Incorrect username or password!");
						target.add(feedbackPanel);
					}
				}
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
