package com.mycompany;

import com.mycompany.vision.BaseSession;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.UrlPathPageParametersEncoder;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import com.mycompany.vision.Login;

/*
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 *
 * @see com.mycompany.Start#main(String[])
 */
public class WicketApplication extends WebApplication {
    /*
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {

        return Login.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new BaseSession(request);
    }

    /*
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        //mount(new MountedMapper("/internetbanking", LoginInternetBanking.class, new UrlPathPageParametersEncoder()));

        // add your configuration here
    }
}
