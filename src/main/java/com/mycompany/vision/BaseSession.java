package com.mycompany.vision;

import com.mycompany.model.User;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class BaseSession extends WebSession {

    public static BaseSession get(){
        return (BaseSession) Session.get();
    }
    private User user;

    public BaseSession(Request request) {
        super(request);
    }

    public boolean isAuthenticated(){
        return(user !=null);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
