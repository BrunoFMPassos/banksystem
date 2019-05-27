package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.googlecode.wicket.jquery.ui.markup.html.link.Link;
import com.mycompany.control.ServicePJ;
import com.mycompany.control.ServiceRelatorios;
import com.mycompany.model.PessoaJuridica;
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

public class CrudPessoaJuridica extends BasePage{
    @SpringBean(name = "pjService")
    ServicePJ servicePJ;
    @SpringBean(name = "relatoriosService")
    ServiceRelatorios<PessoaJuridica> serviceRelatorios;
    final PessoaJuridica pessoaJuridica = new PessoaJuridica();

    private List<PessoaJuridica> listaDePessoasJuridicas = new ArrayList<PessoaJuridica>();
    Form<PessoaJuridica> form;
    TextField<String> inputRazaoSocial = new TextField<String>("razaoSocial");
    TextField<String> inputCnpj = new TextField<String>("cnpj");
    private String razaoSocialFiltrar = "";
    private String cnpjFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirPj = new ModalWindow("modalinserirpj");
    ModalWindow modalWindowEditarPj = new ModalWindow("modaleditarpj");
    ModalWindow modalWindowExcluirPj = new ModalWindow("modalexcluirpj");

    public CrudPessoaJuridica() {
        listaDePessoasJuridicas.addAll(servicePJ.listarPessoasJuridicas(pessoaJuridica));

        modalWindowInserirPj.setAutoSize(false);
        modalWindowInserirPj.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowEditarPj.setAutoSize(false);
        modalWindowEditarPj.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowExcluirPj.setAutoSize(true);
        modalWindowExcluirPj.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });


        CompoundPropertyModel<PessoaJuridica> compoundPropertyModelPessoaJuridica =
                new CompoundPropertyModel<PessoaJuridica>(pessoaJuridica);

        form = new Form<PessoaJuridica>("formpj", compoundPropertyModelPessoaJuridica) {
            @Override
            public void onSubmit() {

            }
        };

        form.setOutputMarkupId(true);
        add(form);
        form.add(criarTextFieldRazaoSocialFiltro());
        form.add(criarTextFieldCnpjFiltro());
        form.add(criarTabela());
        form.add(criarBtnInserir());
        form.add(criarBtnFiltrar());
        form.add(criarModalInserirPj());
        form.add(criarModalEditarPj());
        form.add(criarModalExluirPj());
        form.add(criarRelatorioJasper());
        form.add(criarRelatorioExcel());

    }

    private TextField<String> criarTextFieldRazaoSocialFiltro() {
        return inputRazaoSocial;
    }

    private TextField<String> criarTextFieldCnpjFiltro() {
        return inputCnpj;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<PessoaJuridica> listDataProvider = new ListDataProvider<PessoaJuridica>() {

            @Override
            protected List<PessoaJuridica> getData() {
                return listaDePessoasJuridicas;
            }
        };

        DataView<PessoaJuridica> dataView = new DataView<PessoaJuridica>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<PessoaJuridica> item) {

                final PessoaJuridica pjDaLista = (PessoaJuridica) item.getModelObject();
                Label textRazaoSocial = new Label("textrazaosocial", pjDaLista.getRazaoSocial());
                Label textCnpj = new Label("textcnpj", pjDaLista.getCnpj());
                Label texttelefone = new Label("texttelefone", pjDaLista.getTelefone());

                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {
                        final PessoaJuridicaPanel modalEditarPessoaJuridica = new
                                PessoaJuridicaPanel(modalWindowEditarPj.getContentId(), pjDaLista) {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, PessoaJuridica pessoaJuridica) {
                                        super.executaAoClicarEmSalvar(target, pessoaJuridica);
                                        servicePJ.executarAoClicarEmSalvarNaModalEditar(listaDePessoasJuridicas, pessoaJuridica,
                                                target,rowPanel,modalWindowEditarPj,feedbackPanel);
                                        target.add(feedbackPanel);
                                    }
                                };

                        modalWindowEditarPj.setContent(modalEditarPessoaJuridica);
                        modalWindowEditarPj.show(target);
                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final PanelExcluir<PessoaJuridica> panelExcluirPessoaJuridica = new PanelExcluir<PessoaJuridica>(modalWindowExcluirPj.getContentId()) {
                            @Override
                            public void excluir(AjaxRequestTarget target, PessoaJuridica pessoaJuridica) {
                                super.excluir(target, pessoaJuridica);
                                servicePJ.deletarPessoaJuridica(pjDaLista);
                                listaDePessoasJuridicas.clear();
                                listaDePessoasJuridicas.addAll(servicePJ.listarPessoasJuridicas(pjDaLista));
                                modalWindowExcluirPj.close(target);
                                target.add(rowPanel);
                            }

                            @Override
                            public void fecharSemExcluir(AjaxRequestTarget target, PessoaJuridica pessoaJuridica) {
                                super.fecharSemExcluir(target, pessoaJuridica);
                                modalWindowExcluirPj.close(target);
                            }

                            @Override
                            public Label mostrarValorASerExcluido(String string) {
                                return super.mostrarValorASerExcluido(pjDaLista.getRazaoSocial());
                            }
                        };

                        modalWindowExcluirPj.setContent(panelExcluirPessoaJuridica);
                        modalWindowExcluirPj.show(target);
                    }
                };

                editar.setOutputMarkupId(true);
                excluir.setOutputMarkupId(true);
                item.add(textRazaoSocial);
                item.add(textCnpj);
                item.add(texttelefone);
                item.add(editar);
                item.add(excluir);
            }


        };

        dataView.setItemsPerPage(5);
        rowPanel.add(dataView);
        rowPanel.add(new PagingNavigator("navigator", dataView));
        return rowPanel;
    }

    private ModalWindow criarModalInserirPj() {
        return modalWindowInserirPj;
    }

    private ModalWindow criarModalEditarPj() {
        return modalWindowEditarPj;
    }

    private ModalWindow criarModalExluirPj() {
        return modalWindowExcluirPj;
    }

    private AjaxLink<?> criarBtnInserir() {
        AjaxLink<?> inserir = new AjaxLink<Object>("inserir") {
            public void onClick(AjaxRequestTarget target) {
                final PessoaJuridicaPanel pessoaJuridicaPanel = new PessoaJuridicaPanel
                        (modalWindowInserirPj.getContentId(), new PessoaJuridica()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, PessoaJuridica pessoaJuridica) {
                        servicePJ.executarAoClicarEmSalvarNaModalSalvar(listaDePessoasJuridicas,pessoaJuridica,target,
                                rowPanel,modalWindowInserirPj,feedbackPanel);
                        target.add(feedbackPanel);
                    }
                };
                modalWindowInserirPj.setContent(pessoaJuridicaPanel);
                modalWindowInserirPj.show(target);
            }
        };
        return inserir;
    }



    private AjaxButton criarBtnFiltrar() {

        AjaxButton filtrar = new AjaxButton("filtrar", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                razaoSocialFiltrar = inputRazaoSocial.getInput();
                cnpjFiltrar = inputCnpj.getInput();
                String razaoSocial = razaoSocialFiltrar;
                String cnpj = cnpjFiltrar;
                servicePJ.filtrarPessoaJuridicaNaVisao(razaoSocial,cnpj,listaDePessoasJuridicas,
                        pessoaJuridica,target, rowPanel);
            }
        };
        return filtrar;
    }

    Link<?> criarRelatorioJasper() {

        Link<?> btnRelatorio = new Link<Object>("relatorio"){


            private static final long serialVersionUID = -5081583125636401676L;


            @Override
            public void onClick() {
                serviceRelatorios.gererRelatorioPDF(listaDePessoasJuridicas,"Pessoa_Juridica","Pessoas_Jurídicas");
            }
        };
        btnRelatorio.setOutputMarkupId(true);
        return btnRelatorio;

    };

    Link<?> criarRelatorioExcel() {

        Link<?> btnExcel = new Link<Object>("excel"){

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                serviceRelatorios.gerarRelatorioExcelPessoaJuridica(listaDePessoasJuridicas);
            }

        };
        btnExcel.setOutputMarkupId(true);
        return btnExcel;

    }
}
