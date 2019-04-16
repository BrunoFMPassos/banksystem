package com.mycompany.vision;

;
import com.mycompany.control.ServiceLogin;
import com.mycompany.control.ServicePerfil;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;


public class BasePage extends WebPage {

    @SpringBean(name = "loginService")
    private ServiceLogin s;

    @SpringBean(name = "perfilService")
    private ServicePerfil p;



     public BasePage() {
         if(BaseSession.get().getUser() != null) {
             add(addContainerPessoas());
             add(addLogoutButton());
         }else{
             RequestCycle.get().setResponsePage(Login.class);
         }
    }

    public MarkupContainer addContainerPessoas(){
         MarkupContainer pessoas = new MarkupContainer("containerpessoas") {
         };
         if(p.verificaPerfil().equals("Gerente")){
             p.hide(pessoas);
         }
         return pessoas;
    }

    public AjaxLink addLogoutButton() {

        AjaxLink botaologout = new AjaxLink("logoutBtn") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("Entrou no onSubmit logout!");
                s.logout();
            }

        };
        return botaologout;

    }
}
