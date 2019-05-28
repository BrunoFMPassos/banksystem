package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.googlecode.wicket.jquery.ui.markup.html.link.Link;
import com.mycompany.control.ServiceOperacoes;
import com.mycompany.control.ServiceRelatorios;
import com.mycompany.model.Agencia;
import com.mycompany.model.Movimentacao;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MovimentacoesView extends BasePage{
    @SpringBean(name = "operacoesService")
    ServiceOperacoes serviceOperacoes;
    @SpringBean(name = "relatoriosService")
    ServiceRelatorios<Movimentacao> serviceRelatorios;

    final Movimentacao movimentacao = new Movimentacao();
    Form<Movimentacao> form;
    private List<Movimentacao> listaDeMovimentacoes = new ArrayList<Movimentacao>();
    TextField<String> inputConta = new TextField<String>("contaFiltrar");
    DropDownChoice<String> selectTipo;
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");
    private String contaFiltrar = "";
    private String tipoFiltrar = "";

    public MovimentacoesView() {
        listaDeMovimentacoes.addAll(serviceOperacoes.pesquisaTodasAsMovimentacoes(movimentacao));
        CompoundPropertyModel<Movimentacao> compoundPropertyModelAgencia = new CompoundPropertyModel<Movimentacao>(movimentacao);
        form = new Form<Movimentacao>("formmovimentacao", compoundPropertyModelAgencia) {
        };
        add(form);
        form.add(criarTextFieldContafiltro());
        form.add(criarSelectTipo());
        form.add(criarBtnFiltrar());
        form.add(criarTabela());
        form.add(criarRelatorioJasper() );
    }

    private TextField<String> criarTextFieldContafiltro() {
        inputConta.setOutputMarkupId(true);
        return inputConta;
    }

    private DropDownChoice<String> criarSelectTipo() {

        final List<String> listaDeTipos = new ArrayList<String>();
        listaDeTipos.add("Saque");
        listaDeTipos.add("Depósito");
        listaDeTipos.add("Débito de transferência");
        listaDeTipos.add("Crédito de transferência");
        listaDeTipos.add("Tarifa de conta");
        listaDeTipos.add("Taxa de transferência");


        ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("tipoFiltrar") {
            @Override
            public Object getDisplayValue(String tipoFiltrar) {
                // TODO Auto-generated method stub
                return tipoFiltrar;
            }
        };
        IModel<List<String>> IModellist = new LoadableDetachableModel<List<String>>() {
            @Override
            protected List<String> load() {
                // TODO Auto-generated method stub
                return listaDeTipos;
            }
        };

           selectTipo = new DropDownChoice<String>("tipoFiltrar",IModellist, choiceRenderer){
            @Override
            protected String getNullValidDisplayValue() {
                return "Selecione...";
            }
        };
        selectTipo.setOutputMarkupId(true);
        selectTipo.setNullValid(true);
        return selectTipo;
    }


    private AjaxButton criarBtnFiltrar() {

        AjaxButton filtrar = new AjaxButton("filtrar", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                contaFiltrar = inputConta.getInput();
                tipoFiltrar =  selectTipo.getInput();
                serviceOperacoes.filtrarMovimentacaoNaVisao(contaFiltrar,tipoFiltrar,listaDeMovimentacoes,movimentacao,
                        target,rowPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
            }
        };
        return filtrar;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<Movimentacao> listDataProvider = new ListDataProvider<Movimentacao>() {

            @Override
            protected List<Movimentacao> getData() {
                Collections.sort(listaDeMovimentacoes, new Comparator<Movimentacao>() {
                    @Override
                    public int compare(Movimentacao o1, Movimentacao o2) {
                        return o1.getData().compareTo(o2.getData());
                    }
                });
                return listaDeMovimentacoes;
            }
        };

        DataView<Movimentacao> dataView = new DataView<Movimentacao>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<Movimentacao> item) {

                final Movimentacao movimentacaoDaLista = (Movimentacao) item.getModelObject();
                Label textdescricao = new Label("textdescricao", movimentacaoDaLista.getDescricao());
                Label textconta = new Label("textconta", movimentacaoDaLista.getConta().getNumero());
                Label textdata = new Label("textdata", movimentacaoDaLista.getData());
                Label textvalor = new Label("textvalor", movimentacaoDaLista.getValor());


                item.add(textdescricao);
                item.add(textconta);
                item.add(textvalor);
                item.add(textdata);
            }


        };

        dataView.setItemsPerPage(5);
        rowPanel.add(dataView);
        rowPanel.add(new PagingNavigator("navigator", dataView));
        return rowPanel;
    }

    Link<?> criarRelatorioJasper() {

        Link<?> btnRelatorio = new Link<Object>("relatorio"){

            private static final long serialVersionUID = -5081583125636401676L;

            @Override
            public void onClick() {
                serviceOperacoes.preparaMovimentacaoParaRelatorio(listaDeMovimentacoes);
                serviceRelatorios.gererRelatorioPDF(listaDeMovimentacoes,"Movimentacao","Movimentações");
            }
        };
        btnRelatorio.setOutputMarkupId(true);
        return btnRelatorio;

    };

}
