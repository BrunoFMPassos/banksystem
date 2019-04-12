package com.mycompany.control;

import com.mycompany.model.User;
import com.mycompany.vision.BaseSession;
import com.mycompany.vision.Dashboard;
import com.sun.java.swing.plaf.windows.WindowsBorders;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

public class ServiceLogin {

    @SpringBean(name = "userService")
    private ServiceUser serviceuser;

    public void loginValidate(AjaxRequestTarget target,
                              String username,
                              String password,
                              List<User> listusers,
                              User user,
                              FeedbackPanel fb) {

        if (!username.isEmpty()
                && !password.isEmpty()) {
            listusers.clear();
            user = serviceuser.searchforname(username);

            if (user.getPassword().equals(password)) {
                BaseSession.get().setUser(user);
                System.out.println("Usuário Autorizado!");
                System.out.println(user.getPerfil());
                Dashboard dashboard = new Dashboard();
                RequestCycle.get().setResponsePage(dashboard);

            } else {
                fb.error("Incorrect username or password!");
                target.add(fb);
            }
        }
    }

    public ServiceUser getServiceuser() {
        return serviceuser;
    }

    public void setServiceuser(ServiceUser serviceuser) {
        this.serviceuser = serviceuser;
    }
}