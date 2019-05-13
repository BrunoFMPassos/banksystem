package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.googlecode.wicket.jquery.ui.markup.html.link.Link;
import com.mycompany.control.ServiceAgencia;
import com.mycompany.control.ServiceConta;
import com.mycompany.control.ServiceTipoDeConta;
import com.mycompany.model.*;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class CrudConta extends BasePage{
    @SpringBean(name = "contaService")
    ServiceConta serviceConta;
    @SpringBean(name = "agenciaService")
    ServiceAgencia serviceAgencia;
    @SpringBean(name = "tipoDeContaService")
    ServiceTipoDeConta serviceTipoDeConta;
    final Conta conta = new Conta();
    private List<Conta> listaDeContas = new ArrayList<Conta>();
    Form<Conta> form;
    TextField<String> inputTitular = new TextField<String>("titular");

    Agencia agencia = new Agencia();
    final List<Agencia> listaDeAgenciasPesquisa = serviceAgencia.pesquisarListaDeAgenciasPorAgencia(agencia);
    ChoiceRenderer<Agencia> choiceRenderer = new ChoiceRenderer<Agencia>("numero", "id") {
        @Override
        public Object getDisplayValue(Agencia agencia) {
            // TODO Auto-generated method stub
            return agencia.getNumero();
        }
    };
    IModel<List<Agencia>> IModellist = new LoadableDetachableModel<List<Agencia>>() {
        @Override
        protected List<Agencia> load() {
            // TODO Auto-generated method stub
            return listaDeAgenciasPesquisa;
        }
    };
    DropDownChoice<Agencia> selectAgencia = new DropDownChoice<Agencia>(
            "agenciaFiltrar",
            IModellist, choiceRenderer
    );


    TipoDeConta tipoDeConta = new TipoDeConta();
    final List<TipoDeConta> listaDeTiposPesquisa = serviceTipoDeConta.listarTiposDeConta(tipoDeConta);
    ChoiceRenderer<TipoDeConta> choiceRendererT = new ChoiceRenderer<TipoDeConta>("descricao",
            "descricao") {
        @Override
        public Object getDisplayValue(TipoDeConta  tipoDeConta) {
            // TODO Auto-generated method stub
            return tipoDeConta.getDescricao();
        }
    };
    IModel<List<TipoDeConta>> IModellistT = new LoadableDetachableModel<List<TipoDeConta>>() {
        @Override
        protected List<TipoDeConta> load() {
            // TODO Auto-generated method stub
            return listaDeTiposPesquisa;
        }
    };
    DropDownChoice<TipoDeConta> selectTipo = new DropDownChoice<TipoDeConta>(
            "tipoDeContaFiltrar",
            IModellistT, choiceRendererT
    );

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
        form.add(criarSelectAgencia());
        form.add(criarSelectTipo());
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

    private DropDownChoice<Agencia> criarSelectAgencia() {

        List<String> listaDeAgencias = new ArrayList<String>();
        for (Agencia agenciaLoop : listaDeAgenciasPesquisa) {
            listaDeAgencias.add(agenciaLoop.getNumero().toString());
        }
        selectAgencia.setOutputMarkupId(true);
        return selectAgencia;

    }

    private DropDownChoice<TipoDeConta> criarSelectTipo() {

        List<String> listaDeTipos = new ArrayList<String>();
        for (TipoDeConta tipoLoop : listaDeTiposPesquisa) {
            listaDeTipos.add(tipoLoop.getDescricao());
        }
        selectTipo.setOutputMarkupId(true);
        return selectTipo;

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
                agenciaFiltrar = selectAgencia.getInput();
                tipoFiltrar = selectTipo.getInput();
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
