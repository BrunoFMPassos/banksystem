package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.control.ServiceTipoDeCartao;
import com.mycompany.model.TipoDeCartao;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CrudTipoDeCartao extends BasePage {

    @SpringBean(name = "tipoDeCartaoService")
    ServiceTipoDeCartao serviceTipoDeCartao;
    final TipoDeCartao tipoDeCartao = new TipoDeCartao();

    private List<TipoDeCartao> listaDeTiposDeCartao = new ArrayList<TipoDeCartao>();
    Form<TipoDeCartao> form;
    TextField<String> inputDescricao = new TextField<String>("descricao");
    private String descricaoFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirTipoDeCartao = new ModalWindow("modalinserirtipodecartao");
    ModalWindow modalWindowEditarTipoDeCartao = new ModalWindow("modaleditartipodecartao");
    ModalWindow modalWindowExcluirTipoDeCartao = new ModalWindow("modalexcluirtipodecartao");

    public CrudTipoDeCartao() {
        listaDeTiposDeCartao.addAll(serviceTipoDeCartao.listarTiposDeCartao(tipoDeCartao));

        modalWindowInserirTipoDeCartao.setAutoSize(false);
        modalWindowExcluirTipoDeCartao.setInitialHeight(300);
        modalWindowInserirTipoDeCartao.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowEditarTipoDeCartao.setAutoSize(false);
        modalWindowEditarTipoDeCartao.setInitialHeight(300);
        modalWindowEditarTipoDeCartao.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowExcluirTipoDeCartao.setAutoSize(true);
        modalWindowExcluirTipoDeCartao.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });

        CompoundPropertyModel<TipoDeCartao> compoundPropertyModelTipoDeCartao =
                new CompoundPropertyModel<TipoDeCartao>(tipoDeCartao);

        form = new Form<TipoDeCartao>("formtipodecartao", compoundPropertyModelTipoDeCartao) {
            @Override
            public void onSubmit() {

            }
        };
        add(form);
        form.add(criarBtnFiltrar());
        form.add(criarBtnInserir());
        form.add(criarTabela());
        form.add(criarTextFieldDescricaofiltro());
        form.add(criarModalInserirTipoDeConta());
        form.add(criarModalEditarTipoDeConta());
        form.add(criarModalExluirTipoDeConta());
    }

    private TextField<String> criarTextFieldDescricaofiltro() {
        //TextField<String> inputNome = new TextField<String>("nome");
        return inputDescricao;
    }

    private ModalWindow criarModalInserirTipoDeConta() {
        return modalWindowInserirTipoDeCartao;
    }

    private ModalWindow criarModalEditarTipoDeConta() {
        return modalWindowEditarTipoDeCartao;
    }

    private ModalWindow criarModalExluirTipoDeConta() {
        return modalWindowExcluirTipoDeCartao;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<TipoDeCartao> listDataProvider = new ListDataProvider<TipoDeCartao>() {

            @Override
            protected List<TipoDeCartao> getData() {
                Collections.sort(listaDeTiposDeCartao, new Comparator<TipoDeCartao>() {
                    @Override
                    public int compare(TipoDeCartao o1, TipoDeCartao o2) {
                        return o1.getDescricao().compareTo(o2.getDescricao());
                    }
                });
                return listaDeTiposDeCartao;
            }
        };

        DataView<TipoDeCartao> dataView = new DataView<TipoDeCartao>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<TipoDeCartao> item) {

                final TipoDeCartao tipoDeCartaoDaLista = (TipoDeCartao) item.getModelObject();
                Label textdescricao = new Label("textdescricao", tipoDeCartaoDaLista.getDescricao());
                Label texttarifa = new Label("texttarifa", tipoDeCartaoDaLista.getTarifa());

                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {
                        final TipoDeCartaoPanel modalEditarTipoDeCartao = new
                                TipoDeCartaoPanel(modalWindowEditarTipoDeCartao.getContentId(), tipoDeCartaoDaLista) {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, TipoDeCartao tipoDeCartao) {
                                        super.executaAoClicarEmSalvar(target, tipoDeCartao);
                                        serviceTipoDeCartao.executarAoClicarEmSalvarNaModalEditar(listaDeTiposDeCartao, tipoDeCartao,
                                                target, rowPanel, modalWindowEditarTipoDeCartao, feedbackPanel);
                                        target.add(feedbackPanel);
                                    }
                                };

                        modalWindowEditarTipoDeCartao.setContent(modalEditarTipoDeCartao);
                        modalWindowEditarTipoDeCartao.show(target);
                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final PanelExcluir<TipoDeCartao> panelExcluirTipoDeCartao = new PanelExcluir<TipoDeCartao>(modalWindowExcluirTipoDeCartao.getContentId()) {
                            @Override
                            public void excluir(AjaxRequestTarget target, TipoDeCartao tipoDeCartao) {
                                super.excluir(target, tipoDeCartao);
                                serviceTipoDeCartao.executarAoClicarEmExcluir(tipoDeCartaoDaLista,target,modalWindowExcluirTipoDeCartao,feedbackPanel);
                                listaDeTiposDeCartao.clear();
                                listaDeTiposDeCartao.addAll(serviceTipoDeCartao.listarTiposDeCartao(tipoDeCartaoDaLista));
                                target.add(rowPanel);
                            }

                            @Override
                            public void fecharSemExcluir(AjaxRequestTarget target, TipoDeCartao tipoDeCartao) {
                                super.fecharSemExcluir(target, tipoDeCartao);
                                modalWindowExcluirTipoDeCartao.close(target);
                            }

                            @Override
                            public Label mostrarValorASerExcluido(String string) {
                                return super.mostrarValorASerExcluido(tipoDeCartaoDaLista.getDescricao());
                            }
                        };

                        modalWindowExcluirTipoDeCartao.setContent(panelExcluirTipoDeCartao);
                        modalWindowExcluirTipoDeCartao.show(target);
                    }
                };

                item.add(textdescricao);
                item.add(texttarifa);
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

                final TipoDeCartaoPanel tipoDeCartaoPanel = new TipoDeCartaoPanel
                        (modalWindowInserirTipoDeCartao.getContentId(), new TipoDeCartao()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, TipoDeCartao tipoDeCartao) {
                        serviceTipoDeCartao.executarAoClicarEmSalvarNaModalSalvar(listaDeTiposDeCartao, tipoDeCartao, target,
                                rowPanel, modalWindowInserirTipoDeCartao, feedbackPanel);
                        target.add(feedbackPanel);
                    }
                };
                modalWindowInserirTipoDeCartao.setContent(tipoDeCartaoPanel);
                modalWindowInserirTipoDeCartao.show(target);
            }
        };
        return inserir;
    }

    private AjaxButton criarBtnFiltrar() {

        AjaxButton filtrar = new AjaxButton("filtrar", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                descricaoFiltrar = inputDescricao.getInput();
                String descricao = descricaoFiltrar;
                serviceTipoDeCartao.filtrarTipoDeCartaoNaVisao(descricao, listaDeTiposDeCartao, tipoDeCartao, target, rowPanel);
            }
        };
        return filtrar;
    }


}
