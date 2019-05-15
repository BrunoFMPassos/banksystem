package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.control.ServiceTipoDeConta;
import com.mycompany.model.TipoDeConta;
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

public class CrudTipoDeConta extends BasePage{

    @SpringBean(name = "tipoDeContaService")
    ServiceTipoDeConta serviceTipoDeConta;
    final TipoDeConta tipoDeConta = new TipoDeConta();

    private List<TipoDeConta> listaDeTiposDeConta = new ArrayList<TipoDeConta>();
    Form<TipoDeConta> form;
    TextField<String> inputDescricao = new TextField<String>("descricao");
    private String descricaoFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirTipoDeConta = new ModalWindow("modalinserirtipodeconta");
    ModalWindow modalWindowEditarTipoDeConta = new ModalWindow("modaleditartipodeconta");
    ModalWindow modalWindowExcluirTipoDeConta = new ModalWindow("modalexcluirtipodeconta");

    public CrudTipoDeConta() {
        listaDeTiposDeConta.addAll(serviceTipoDeConta.listarTiposDeConta(tipoDeConta));

        modalWindowInserirTipoDeConta.setAutoSize(false);
        modalWindowInserirTipoDeConta.setInitialHeight(300);
        modalWindowInserirTipoDeConta.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowEditarTipoDeConta.setAutoSize(false);
        modalWindowEditarTipoDeConta.setInitialHeight(300);
        modalWindowEditarTipoDeConta.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowExcluirTipoDeConta.setAutoSize(true);
        modalWindowExcluirTipoDeConta.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });

        CompoundPropertyModel<TipoDeConta> compoundPropertyModelTipoDeConta =
                new CompoundPropertyModel<TipoDeConta>(tipoDeConta);

        form = new Form<TipoDeConta>("formtipodeconta", compoundPropertyModelTipoDeConta) {
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
        return modalWindowInserirTipoDeConta;
    }

    private ModalWindow criarModalEditarTipoDeConta() {
        return modalWindowEditarTipoDeConta;
    }

    private ModalWindow criarModalExluirTipoDeConta() {
        return modalWindowExcluirTipoDeConta;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<TipoDeConta> listDataProvider = new ListDataProvider<TipoDeConta>() {

            @Override
            protected List<TipoDeConta> getData() {
                return listaDeTiposDeConta;
            }
        };

        DataView<TipoDeConta> dataView = new DataView<TipoDeConta>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<TipoDeConta> item) {

                final TipoDeConta tipoDeContaDaLista = (TipoDeConta) item.getModelObject();
                Label textdescricao = new Label("textdescricao", tipoDeContaDaLista.getDescricao());
                Label texttarifa = new Label("texttarifa", tipoDeContaDaLista.getTarifa());

                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {
                        final TipoDeContaPanel modalEditarTipoDeConta = new
                                TipoDeContaPanel(modalWindowEditarTipoDeConta.getContentId(), tipoDeContaDaLista) {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, TipoDeConta tipoDeConta) {
                                        super.executaAoClicarEmSalvar(target, tipoDeConta);
                                        serviceTipoDeConta.executarAoClicarEmSalvarNaModalEditar(listaDeTiposDeConta,tipoDeConta,
                                                target,rowPanel,modalWindowEditarTipoDeConta,feedbackPanel);
                                        target.add(feedbackPanel);
                                    }
                                };

                        modalWindowEditarTipoDeConta.setContent(modalEditarTipoDeConta);
                        modalWindowEditarTipoDeConta.show(target);
                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final PanelExcluir<TipoDeConta> panelExcluirTipoDeConta = new PanelExcluir<TipoDeConta>(modalWindowExcluirTipoDeConta.getContentId()) {
                            @Override
                            public void excluir(AjaxRequestTarget target, TipoDeConta tipoDeConta) {
                                super.excluir(target, tipoDeConta);
                                serviceTipoDeConta.deletarTipoDeConta(tipoDeContaDaLista);
                                listaDeTiposDeConta.clear();
                                listaDeTiposDeConta.addAll(serviceTipoDeConta.listarTiposDeConta(tipoDeContaDaLista));
                                modalWindowExcluirTipoDeConta.close(target);
                                target.add(rowPanel);
                            }

                            @Override
                            public void fecharSemExcluir(AjaxRequestTarget target, TipoDeConta tipoDeConta) {
                                super.fecharSemExcluir(target, tipoDeConta);
                                modalWindowExcluirTipoDeConta.close(target);
                            }

                            @Override
                            public Label mostrarValorASerExcluido(String string) {
                                return super.mostrarValorASerExcluido(tipoDeContaDaLista.getDescricao());
                            }
                        };

                        modalWindowExcluirTipoDeConta.setContent(panelExcluirTipoDeConta);
                        modalWindowExcluirTipoDeConta.show(target);
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

                final TipoDeContaPanel tipoDeContaPanel = new TipoDeContaPanel
                        (modalWindowInserirTipoDeConta.getContentId(), new TipoDeConta()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, TipoDeConta tipoDeConta) {
                        serviceTipoDeConta.executarAoClicarEmSalvarNaModalSalvar(listaDeTiposDeConta,tipoDeConta,target,
                                rowPanel,modalWindowInserirTipoDeConta,feedbackPanel);
                        target.add(feedbackPanel);
                    }
                };
                modalWindowInserirTipoDeConta.setContent(tipoDeContaPanel);
                modalWindowInserirTipoDeConta.show(target);
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
                serviceTipoDeConta.filtrarTipoDeContaNaVisao(descricao,listaDeTiposDeConta,tipoDeConta,target,rowPanel);
            }
        };
        return filtrar;
    }
}
