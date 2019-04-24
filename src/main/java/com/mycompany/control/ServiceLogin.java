package com.mycompany.control;


import com.mycompany.model.User;
import com.mycompany.vision.BaseSession;
import com.mycompany.vision.Dashboard;
import com.mycompany.vision.Login;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static org.apache.wicket.ThreadContext.getSession;

public class ServiceLogin {

    @SpringBean(name = "userService")
    private ServiceUser serviceUser;

    public void loginValidate(AjaxRequestTarget target,
                              String username,
                              String password,
                              List<User> listusers,
                              User user,
                              FeedbackPanel fb) {

        if (!username.isEmpty()
                && !password.isEmpty()) {
            listusers.clear();
            user = serviceUser.searchForName(username);

            if (user != null) {

                if (user.getPassword().equals(password)) {
                    BaseSession.get().setUser(user);
                    Dashboard dashboard = new Dashboard();
                    RequestCycle.get().setResponsePage(dashboard);
                } else {
                    fb.error("Usuário ou senha incorretos!");
                    target.add(fb);
                }
            } else {
                fb.error("Usuário ou senha incorretos!");
                target.add(fb);
            }
        } else {
            fb.error("Usuário ou senha incorretos!");
            target.add(fb);
        }
    }

    public void logout() {
        getSession().invalidate();
        RequestCycle.get().setResponsePage(Login.class);
    }

    public void setServiceUser(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }
}


