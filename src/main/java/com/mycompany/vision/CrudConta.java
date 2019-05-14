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
    Conta conta = new Conta();
    private List<Conta> listaDeContas = new ArrayList<Conta>();
    Form<Conta> form;
    TextField<String> inputTitular = new TextField<String>("titular");

    DropDownChoice<Agencia> selectAgenciaFiltrar;
    DropDownChoice<TipoDeConta> selectTipoFiltrar;

    private String titularFiltrar = "";
    private String agenciaFiltrar = "";
    private String tipoFiltrar = "";
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirConta = new ModalWindow("modalinserirconta");

    public CrudConta() {
        listaDeContas.addAll(serviceConta.pesquisarListaDeContas(conta));
        modalWindowInserirConta.setAutoSize(false);
        modalWindowInserirConta.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
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
        form.add(criarSelectAgenciaFiltrar());
        form.add(criarSelectTipoFiltrar());
        form.add(criarBtnFiltrar());
        form.add(criarBtnInserir());
        form.add(criarTabela());
        form.add(criarRelatorioJasper());
        form.add(criarRelatorioExcel());
        form.add(criarModalInserirConta());
    }

    private ModalWindow criarModalInserirConta() {
        return modalWindowInserirConta;
    }

    private TextField<String> criarTextFieldTitularfiltro() {
        //TextField<String> inputNome = new TextField<String>("nome");
        return inputTitular;
    }

    private DropDownChoice<Agencia> criarSelectAgenciaFiltrar() {
        Agencia agenciaObjFiltrar = new Agencia();
        final List<Agencia> listaDeAgenciasPesquisaFiltro = serviceAgencia.pesquisarListaDeAgenciasPorAgencia(agenciaObjFiltrar);
        List<String> listaDeAgenciasFiltrar = new ArrayList<String>();
        for (Agencia agenciaLoopFiltrar : listaDeAgenciasPesquisaFiltro) {
            listaDeAgenciasFiltrar.add(agenciaLoopFiltrar.getNumero().toString());
        }
        ChoiceRenderer<Agencia> choiceRendererA = new ChoiceRenderer<Agencia>("numero", "id") {
            @Override
            public Object getDisplayValue(Agencia agencia) {
                // TODO Auto-generated method stub
                return agencia.getNumero();
            }
        };
        IModel<List<Agencia>> IModellistA = new LoadableDetachableModel<List<Agencia>>() {
            @Override
            protected List<Agencia> load() {
                // TODO Auto-generated method stub
                return listaDeAgenciasPesquisaFiltro;
            }
        };
        selectAgenciaFiltrar = new DropDownChoice<Agencia>(
                "agenciaFiltrar",
                IModellistA, choiceRendererA
        ){
            @Override
            protected String getNullValidDisplayValue() {
                return "Selecione...";
            }
        };

        selectAgenciaFiltrar.setOutputMarkupId(true);
        selectAgenciaFiltrar.setNullValid(true);
        return selectAgenciaFiltrar;

    }

    private DropDownChoice<TipoDeConta> criarSelectTipoFiltrar() {
        TipoDeConta tipoDeContaObjFiltrar = new TipoDeConta();
        final List<TipoDeConta> listaDeTiposPesquisaFiltrar = serviceTipoDeConta.listarTiposDeConta(tipoDeContaObjFiltrar);
        List<String> listaDeTiposFiltrar = new ArrayList<String>();
        for (TipoDeConta tipoLoopFiltrar : listaDeTiposPesquisaFiltrar) {
            listaDeTiposFiltrar.add(tipoLoopFiltrar.getDescricao());
        }

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
                return listaDeTiposPesquisaFiltrar;
            }
        };


        selectTipoFiltrar = new DropDownChoice<TipoDeConta>(
                "tipoDeContaFiltrar",
                IModellistT, choiceRendererT
        ){
            @Override
            protected String getNullValidDisplayValue() {
                return "Selecione...";
            }
        };

        selectTipoFiltrar.setOutputMarkupId(true);
        selectTipoFiltrar.setNullValid(true);
        return selectTipoFiltrar;

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
                final ContaInserirPanel contaInserirPanel = new ContaInserirPanel
                        (modalWindowInserirConta.getContentId(), new Conta()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Conta conta) {
                        serviceConta.executarAoClicarEmSalvarNaModalSalvar(listaDeContas,conta,target,rowPanel,
                                modalWindowInserirConta,feedbackPanel);
                        target.add(feedbackPanel);
                    }

                };
                modalWindowInserirConta.setContent(contaInserirPanel);
                modalWindowInserirConta.show(target);
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
                agenciaFiltrar = selectAgenciaFiltrar.getInput();
                tipoFiltrar = selectTipoFiltrar.getInput();
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
