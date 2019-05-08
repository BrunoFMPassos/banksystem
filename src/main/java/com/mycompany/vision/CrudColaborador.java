package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.googlecode.wicket.jquery.ui.markup.html.link.Link;
import com.mycompany.control.ServiceColaborador;
import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
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

import java.util.ArrayList;
import java.util.List;


public class CrudColaborador extends BasePage {

    @SpringBean(name = "colaboradorService")
    ServiceColaborador serviceColaborador;
    final Colaborador colaborador = new Colaborador();
    private List<Colaborador> listaDeColaboradores = new ArrayList<Colaborador>();
    Form<Colaborador> form;
    TextField<String> inputNome = new TextField<String>("nome");
    TextField<String> inputAgencia = new TextField<String>("agenciapesquisa");
    private String nomeFiltrar = "";
    private String agenciaFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirColaborador = new ModalWindow("modalinserircolaborador");
    ModalWindow modalWindowEditarColaborador = new ModalWindow("modaleditarcolaborador");
    ModalWindow modalWindowExcluirColaborador = new ModalWindow("modalexcluircolaborador");

    public CrudColaborador() {
        listaDeColaboradores.addAll(serviceColaborador.pesquisarListaDeColaboradoresPorColabordaor(colaborador));
        modalWindowInserirColaborador.setAutoSize(false);
        modalWindowInserirColaborador.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowEditarColaborador.setAutoSize(false);
        modalWindowEditarColaborador.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowExcluirColaborador.setAutoSize(true);
        modalWindowExcluirColaborador.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });

        CompoundPropertyModel<Colaborador> compoundPropertyModelColaborador = new CompoundPropertyModel<Colaborador>(colaborador);

        form = new Form<Colaborador>("formcolaborador", compoundPropertyModelColaborador) {
            @Override
            public void onSubmit() {

            }
        };


        add(form);
        form.add(criarTextFieldNomefiltro());
        form.add(criarTextFieldAgenciafiltro());
        form.add(criarBtnFiltrar());
        form.add(criarBtnInserir());
        form.add(criarTabela());
        form.add(cirarModalInserirColaborador());
        form.add(cirarModalEditarColaborador());
        form.add(cirarModalExluirColaborador());
        form.add(criarRelatorioJasper());
        form.add(criarRelatorioExcel());
    }


    private TextField<String> criarTextFieldNomefiltro() {
        //TextField<String> inputNome = new TextField<String>("nome");
        return inputNome;
    }

    private TextField<String> criarTextFieldAgenciafiltro() {
        //TextField<String> inputAgencia = new TextField<String>("agencia");
        return inputAgencia;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<Colaborador> listDataProvider = new ListDataProvider<Colaborador>() {

            @Override
            protected List<Colaborador> getData() {
                return listaDeColaboradores;
            }
        };

        DataView<Colaborador> dataView = new DataView<Colaborador>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<Colaborador> item) {

                final Colaborador colaboradorDaLista = (Colaborador) item.getModelObject();
                Label textnome = new Label("textnome", colaboradorDaLista.getNome());
                User user = serviceColaborador.pesquisarObjetoUserPorColaborador(colaboradorDaLista);
                Label textusuario = new Label("textusuario", user.getUsername());
                Label textperfil = new Label("textperfil", user.getPerfil());

                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {
                        final ColaboradorPanel modalEditarColaborador = new
                                ColaboradorPanel(modalWindowEditarColaborador.getContentId(), colaboradorDaLista) {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Colaborador colaborador) {
                                        super.executaAoClicarEmSalvar(target, colaborador);
                                        serviceColaborador.executarAoClicarEmSalvarNaModalEditar(listaDeColaboradores, colaborador, target,
                                                rowPanel, modalWindowEditarColaborador, feedbackPanel);
                                        target.add(feedbackPanel);
                                    }
                                };

                        modalWindowEditarColaborador.setContent(modalEditarColaborador);
                        modalWindowEditarColaborador.show(target);

                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final PanelExcluir<Colaborador> panelExcluirColaborador = new PanelExcluir<Colaborador>(modalWindowExcluirColaborador.getContentId()) {
                            @Override
                            public void excluir(AjaxRequestTarget target, Colaborador colaborador) {
                                super.excluir(target, colaborador);
                                serviceColaborador.deletarColaborador(colaboradorDaLista);
                                listaDeColaboradores.clear();
                                listaDeColaboradores.addAll(serviceColaborador.pesquisarListaDeColaboradoresPorColabordaor(colaboradorDaLista));
                                modalWindowExcluirColaborador.close(target);
                                target.add(rowPanel);
                            }

                            @Override
                            public void fecharSemExcluir(AjaxRequestTarget target, Colaborador colaborador) {
                                super.fecharSemExcluir(target, colaborador);
                                modalWindowExcluirColaborador.close(target);
                            }

                            @Override
                            public Label mostrarValorASerExcluido(String string) {
                                return super.mostrarValorASerExcluido(colaboradorDaLista.getNome());
                            }
                        };

                        modalWindowExcluirColaborador.setContent(panelExcluirColaborador);
                        modalWindowExcluirColaborador.show(target);

                    }
                };

                item.add(textnome);
                item.add(textusuario);
                item.add(textperfil);
                item.add(editar);
                item.add(excluir);
            }


        };

        dataView.setItemsPerPage(5);
        rowPanel.add(dataView);
        rowPanel.add(new PagingNavigator("navigator", dataView));
        return rowPanel;
    }

    private ModalWindow cirarModalInserirColaborador() {
        return modalWindowInserirColaborador;
    }

    private ModalWindow cirarModalEditarColaborador() {
        return modalWindowEditarColaborador;
    }

    private ModalWindow cirarModalExluirColaborador() {
        return modalWindowExcluirColaborador;
    }

    private AjaxLink<?> criarBtnInserir() {
        AjaxLink<?> inserir = new AjaxLink<Object>("inserir") {
            public void onClick(AjaxRequestTarget target) {
                final ColaboradorPanel colaboradorPanel = new ColaboradorPanel
                        (modalWindowInserirColaborador.getContentId(), new Colaborador()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Colaborador colaborador) {
                        serviceColaborador.executarAoClicarEmSalvarNaModalSalvar(
                                listaDeColaboradores, colaborador, target, rowPanel, modalWindowInserirColaborador, feedbackPanel);
                        target.add(feedbackPanel);
                    }

                };
                modalWindowInserirColaborador.setContent(colaboradorPanel);
                modalWindowInserirColaborador.show(target);
            }
        };
        return inserir;
    }


    private AjaxButton criarBtnFiltrar() {

        AjaxButton filtrar = new AjaxButton("filtrar", form) {


            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                nomeFiltrar = inputNome.getInput();
                agenciaFiltrar = inputAgencia.getInput();
                String nome = nomeFiltrar;
                String agencia = agenciaFiltrar;
                serviceColaborador.filtrarColaboradorNaVisao(nome, agencia, listaDeColaboradores, colaborador, target, rowPanel);
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
