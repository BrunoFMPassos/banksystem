package com.mycompany;

import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.UrlPathPageParametersEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.vision.HomePage;
import com.mycompany.vision.Login;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.mycompany.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return Login.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	
	 */
	@Override
	public void init()
	{
		super.init();
		mount(new MountedMapper("/home", HomePage.class, new UrlPathPageParametersEncoder()));

		// add your configuration here
	}
}
