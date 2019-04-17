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
        if (BaseSession.get().getUser() != null) {
            add(addContainerPessoas());
            add(addContainerConta());
            add(addContainerEmpresarial());
            add(addLogoutButton());
        } else {
            RequestCycle.get().setResponsePage(Login.class);
        }
    }

    public MarkupContainer addContainerPessoas() {
        MarkupContainer pessoas = new MarkupContainer("containerpessoas") {
        };
        if (p.verificaPerfil().equals("Gerente") || p.verificaPerfil().equals("Caixa")) {
            p.hide(pessoas);
        }
        return pessoas;
    }

    public MarkupContainer addContainerConta() {
        MarkupContainer conta = new MarkupContainer("containerconta") {
        };
        conta.add(addcontali());
        conta.add(addoperacoesli());
        conta.add(addanaliseli());
        return conta;
    }

    public MarkupContainer addcontali() {
        MarkupContainer contali = new MarkupContainer("conta-conta") {
        };
        if (p.verificaPerfil().equals("Caixa")) {
            p.hide(contali);
        }
        return contali;
    }

    public MarkupContainer addoperacoesli() {
        MarkupContainer operacoesli = new MarkupContainer("conta-operacoes") {
        };
        return operacoesli;
    }

    public MarkupContainer addanaliseli() {
        MarkupContainer analiseli = new MarkupContainer("conta-analise") {
        };
        if (p.verificaPerfil().equals("Caixa")) {
            p.hide(analiseli);
        }
        return analiseli;
    }

    public MarkupContainer addContainerEmpresarial() {
        MarkupContainer empresarial = new MarkupContainer("containerempresarial") {
        };
        if (p.verificaPerfil().equals("Gerente") || p.verificaPerfil().equals("Caixa")) {
            p.hide(empresarial);
        }
        return empresarial;
    }


    public AjaxLink addLogoutButton() {

        AjaxLink botaologout = new AjaxLink("logoutBtn") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                s.logout();
            }

        };
        return botaologout;

    }
}
