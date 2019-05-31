package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.control.ServiceAgencia;
import com.mycompany.control.ServiceImportAgencia;
import com.mycompany.model.Agencia;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.FileNotFoundException;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CrudAgencia extends BasePage {
    @SpringBean(name = "agenciaService")
    ServiceAgencia serviceAgencia;

    final Agencia agencia = new Agencia();
    Form<Agencia> form;
    private List<Agencia> listaDeAgencias = new ArrayList<Agencia>();
    TextField<String> inputNumero = new TextField<String>("numero");
    private String numeroFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");
    ModalWindow modalWindowInserirAgencia = new ModalWindow("modalinseriragencia");
    ModalWindow modalWindowEditarAgencia = new ModalWindow("modaleditaragencia");
    ModalWindow modalWindowExcluirAgencia = new ModalWindow("modalexcluiragencia");



    public CrudAgencia() {
        listaDeAgencias.addAll(serviceAgencia.pesquisarListaDeAgenciasPorAgencia(agencia));
        modalWindowInserirAgencia.setAutoSize(false);
        modalWindowInserirAgencia.setInitialWidth(400);
        modalWindowInserirAgencia.setInitialHeight(250);
        modalWindowInserirAgencia.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });

        modalWindowEditarAgencia.setAutoSize(false);
        modalWindowEditarAgencia.setInitialWidth(400);
        modalWindowEditarAgencia.setInitialHeight(250);
        modalWindowEditarAgencia.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });

        modalWindowExcluirAgencia.setAutoSize(false);
        modalWindowExcluirAgencia.setInitialWidth(300);
        modalWindowExcluirAgencia.setInitialHeight(220);
        modalWindowExcluirAgencia.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });


        CompoundPropertyModel<Agencia> compoundPropertyModelAgencia = new CompoundPropertyModel<Agencia>(agencia);
        form = new Form<Agencia>("formagencia", compoundPropertyModelAgencia) {
        };

        add(form);
        form.add(criarTextFieldNumerofiltro());
        form.add(criarBtnFiltrar());
        form.add(criarBtnInserir());
        form.add(criarTabela());
        form.add(criarModalInserirAgencia());
        form.add(criarModalEditarAgencia());
        form.add(criarModalExcluirAgencia());
    }

    private TextField<String> criarTextFieldNumerofiltro() {
        return inputNumero;
    }

    private ModalWindow criarModalInserirAgencia() {
        return modalWindowInserirAgencia;
    }
    private ModalWindow criarModalEditarAgencia() {
        return modalWindowEditarAgencia;
    }
    private ModalWindow criarModalExcluirAgencia() {
        return modalWindowExcluirAgencia;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<Agencia> listDataProvider = new ListDataProvider<Agencia>() {

            @Override
            protected List<Agencia> getData() {
                Collections.sort(listaDeAgencias, new Comparator<Agencia>() {
                    @Override
                    public int compare(Agencia o1, Agencia o2) {
                        return o1.getNumero().compareTo(o2.getNumero());
                    }
                });
                return listaDeAgencias;
            }
        };

        DataView<Agencia> dataView = new DataView<Agencia>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<Agencia> item) {

                final Agencia agenciaDaLista = (Agencia) item.getModelObject();
                Label textnumero = new Label("textnumero", agenciaDaLista.getNumero());
                Label textcidade = new Label("textcidade", agenciaDaLista.getCidade());
                Label textuf = new Label("textuf", agenciaDaLista.getUF());

                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {

                        final AgenciaPanel modalEditarAgencia = new
                                AgenciaPanel(modalWindowEditarAgencia.getContentId(), agenciaDaLista){
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Agencia agencia) {
                                        super.executaAoClicarEmSalvar(target,agencia);
                                        serviceAgencia.executarAoClicarEmSalvarNaModalEditar(listaDeAgencias, agenciaDaLista, target,
                                                rowPanel,modalWindowEditarAgencia,feedbackPanel);
                                        target.add(feedbackPanel);
                                    }
                                };

                        modalWindowEditarAgencia.setContent(modalEditarAgencia);
                        modalWindowEditarAgencia.show(target);
                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        final PanelExcluir<Agencia> panelExcluirColaborador = new PanelExcluir<Agencia>(modalWindowExcluirAgencia.getContentId()){
                            @Override
                            public void excluir(AjaxRequestTarget target, Agencia agencia) {
                                super.excluir(target, agencia);
                                serviceAgencia.executarAoClicarEmSimNaModalExcluir(listaDeAgencias, agenciaDaLista, target,rowPanel,modalWindowExcluirAgencia
                                ,feedbackPanel);
                                target.add(feedbackPanel);
                            }

                            @Override
                            public void fecharSemExcluir(AjaxRequestTarget target, Agencia agencia) {
                                super.fecharSemExcluir(target, agencia);
                                modalWindowExcluirAgencia.close(target);
                            }

                            @Override
                            public Label mostrarValorASerExcluido(String string) {
                                return super.mostrarValorASerExcluido("Agência: "+agenciaDaLista.getNumero());
                            }
                        };

                        modalWindowExcluirAgencia.setContent(panelExcluirColaborador);
                        modalWindowExcluirAgencia.show(target);

                    }
                };

                item.add(textnumero);
                item.add(textcidade);
                item.add(textuf);
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
                final AgenciaPanel agenciaPanel = new AgenciaPanel
                        (modalWindowInserirAgencia.getContentId(), new Agencia()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Agencia agencia) {
                        serviceAgencia.executarAoClicarEmSalvarNaModalSalvar(listaDeAgencias, agencia, target, rowPanel,
                                modalWindowInserirAgencia, feedbackPanel);
                        target.add(feedbackPanel);
                    }

                };
                modalWindowInserirAgencia.setContent(agenciaPanel);
                modalWindowInserirAgencia.show(target);
            }
        };
        return inserir;
    }


    private AjaxButton criarBtnFiltrar() {

        AjaxButton filtrar = new AjaxButton("filtrar", form) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                numeroFiltrar = inputNumero.getInput();
                String numero = numeroFiltrar;
                serviceAgencia.filtrarAgenciaNaVisao(numero,listaDeAgencias,agencia,target,rowPanel);
            }
        };
        return filtrar;
    }
}
