package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
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
    TextField<String> inputAgencia = new TextField<String>("agencia");
    private String nomeFiltrar = "";
    private String agenciaFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirColaborador = new ModalWindow("modalinserircolaborador");
    ModalWindow modalWindowEditarColaborador = new ModalWindow("modaleditarcolaborador");
    ModalWindow modalWindowExcluirColaborador = new ModalWindow("modalexcluircolaborador");

    public CrudColaborador() {
        listaDeColaboradores.addAll(serviceColaborador.pesquisarListaDeColaboradoresPorColabordaor(colaborador));
        modalWindowInserirColaborador.setAutoSize(false);
        modalWindowEditarColaborador.setAutoSize(false);
        modalWindowExcluirColaborador.setAutoSize(true);

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

        DataView<Colaborador> lv = new DataView<Colaborador>("rows", listDataProvider) {


            @Override
            protected void populateItem(Item<Colaborador> item) {

                final Colaborador colaboradorDaLista = (Colaborador) item.getModelObject();
                Label textnome = new Label("textnome", colaboradorDaLista.getNome());
                User user = serviceColaborador.pesquisarObjetoUserPorColaborador(colaboradorDaLista);
                Label textusuario = new Label("textusuario", user.getUsername());
                Label textperfil = new Label("textperfil", user.getPerfil());


                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {
                        System.out.println("Clicou no editar");
                        System.out.println(colaboradorDaLista.getNome());
                        ModalColaborador modalEditarColaborador = new
                                 ModalColaborador(modalWindowEditarColaborador.getContentId(), colaboradorDaLista){
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Colaborador colaborador) {
                                        super.executaAoClicarEmSalvar(target,colaborador);
                                        serviceColaborador.executarAoClicarEmSalvarNaModal(listaDeColaboradores,colaborador,target,
                                                rowPanel,modalWindowEditarColaborador,"update");
                                    }
                                };

                        modalWindowEditarColaborador.setContent(modalEditarColaborador);
                        modalWindowEditarColaborador.show(target);

                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final ModalExcluir modalExcluirColaborador = new ModalExcluir(modalWindowExcluirColaborador.getContentId()){
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
                            public Label mostrarValorASerExcluido(Colaborador colaborador) {
                                return super.mostrarValorASerExcluido(colaboradorDaLista);
                            }
                        };

                        modalWindowExcluirColaborador.setContent(modalExcluirColaborador);
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

        lv.setItemsPerPage(10);
        rowPanel.add(lv);
        rowPanel.add(new PagingNavigator("navigator", lv));
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
                final ModalColaborador modalColaborador = new ModalColaborador
                        (modalWindowInserirColaborador.getContentId(), new Colaborador()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Colaborador colaborador) {
                        serviceColaborador.executarAoClicarEmSalvarNaModal(
                                listaDeColaboradores, colaborador, target, rowPanel, modalWindowInserirColaborador,"inserir");
                    }
                };
                modalWindowInserirColaborador.setContent(modalColaborador);
                modalWindowInserirColaborador.show(target);
            }
        };
        return inserir;
    }


    private AjaxButton criarBtnFiltrar() {

        AjaxButton filtrar = new AjaxButton("filtrar", form) {

            private static final long serialVersionUID = 1L;

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
}
