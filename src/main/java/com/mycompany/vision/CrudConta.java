package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.googlecode.wicket.jquery.ui.markup.html.link.Link;
import com.mycompany.control.ServiceConta;
import com.mycompany.model.Conta;
import com.mycompany.model.PessoaFisica;
import com.mycompany.model.PessoaJuridica;
import com.mycompany.model.User;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class CrudConta extends BasePage{
    @SpringBean(name = "contaService")
    ServiceConta serviceConta;
    final Conta conta = new Conta();
    private List<Conta> listaDeContas = new ArrayList<Conta>();
    Form<Conta> form;
    TextField<String> inputTitular = new TextField<String>("titular");
    TextField<String> inputAgencia = new TextField<String>("agenciaFiltrar");
    TextField<String> inputTipo = new TextField<String>("tipoDeContaFiltrar");
    private String titularFiltrar = "";
    private String agenciaFiltrar = "";
    private String tipoFiltrar = "";
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    public CrudConta() {
        listaDeContas.addAll(serviceConta.pesquisarListaDeContas(conta));
        feedbackPanel.setOutputMarkupId(true);

        CompoundPropertyModel<Conta> compoundPropertyModelConta = new CompoundPropertyModel<Conta>(conta);

        form = new Form<Conta>("formconta", compoundPropertyModelConta) {
            @Override
            public void onSubmit() {

            }
        };

        add(form);
        form.add(feedbackPanel);
        form.add(criarTextFieldTitularfiltro());
        form.add(criarTextFieldAgenciafiltro());
        form.add(criarTextFieldTipofiltro());
        form.add(criarBtnFiltrar());
        form.add(criarBtnInserir());
        form.add(criarTabela());
        form.add(criarRelatorioJasper());
        form.add(criarRelatorioExcel());
    }


    private TextField<String> criarTextFieldTitularfiltro() {
        //TextField<String> inputNome = new TextField<String>("nome");
        return inputTitular;
    }

    private TextField<String> criarTextFieldAgenciafiltro() {
        //TextField<String> inputAgencia = new TextField<String>("agencia");
        return inputAgencia;
    }

    private TextField<String> criarTextFieldTipofiltro() {
        //TextField<String> inputAgencia = new TextField<String>("agencia");
        return inputTipo;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<Conta> listDataProvider = new ListDataProvider<Conta>() {

            @Override
            protected List<Conta> getData() {
                return listaDeContas;
            }
        };

        DataView<Conta> dataView = new DataView<Conta>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<Conta> item) {

                final Conta contaDaLista = (Conta) item.getModelObject();
                Label textnumero = new Label("textnumero", contaDaLista.getNumero().toString());
                PessoaJuridica pj = contaDaLista.getPessoaJuridica();
                PessoaFisica pf = contaDaLista.getPessoaFisica();
                String titular = serviceConta.pesquisarNomeDoTitular(pj, pf);
                Label texttitular = new Label("texttitular", titular);
                Label texttipo = new Label("texttipo", contaDaLista.getTipoDeConta().getDescricao());

                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {
                        System.out.println("Clicou no Editar!");

                    }

                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        System.out.println("Clicou no Excluir!");
                    }

                };
                item.add(textnumero);
                item.add(texttitular);
                item.add(texttipo);
                item.add(editar);
                item.add(excluir);
            }
        };

        dataView.setItemsPerPage(5);
        rowPanel.add(dataView);
        rowPanel.add(new PagingNavigator("navigator", dataView));
        return rowPanel;
    }


    private AjaxLink<?> criarBtnInserir() {
        AjaxLink<?> inserir = new AjaxLink<Object>("inserir") {
            public void onClick(AjaxRequestTarget target) {
                System.out.println("Clicou no Inserir!");
            }
        };
        return inserir;
    }


    private AjaxButton criarBtnFiltrar() {
        AjaxButton filtrar = new AjaxButton("filtrarconta", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                titularFiltrar = inputTitular.getInput();
                agenciaFiltrar = inputAgencia.getInput();
                tipoFiltrar = inputTipo.getInput();
                String titular = titularFiltrar;
                String agencia = agenciaFiltrar;
                String tipo = tipoFiltrar;
                serviceConta.filtrarContaNaVisao(titular,agencia,tipo,listaDeContas,conta,target,rowPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }
        };
        return filtrar;
    }


    Link<?> criarRelatorioJasper() {

        Link<?> btnRelatorio = new Link<Object>("relatorio") {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub
                System.out.println("Clicou no relatório!");
            }
        };
        return btnRelatorio;
    }

    Link<?> criarRelatorioExcel() {

        Link<?> btnExcel = new Link<Object>("excel") {

            @Override
            public void onClick() {
                System.out.println("Clicou no excel!");
            }
        };
        return btnExcel;
    }
}
