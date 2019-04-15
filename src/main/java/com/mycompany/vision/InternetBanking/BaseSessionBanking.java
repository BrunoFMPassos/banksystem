package com.mycompany.vision.InternetBanking;

import com.mycompany.model.User;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class BaseSessionBanking extends WebSession {

    public static BaseSessionBanking get(){
        return (BaseSessionBanking) Session.get();
    }
    private User user;

    public BaseSessionBanking(Request request) {
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
