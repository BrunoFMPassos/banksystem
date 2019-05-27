package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.googlecode.wicket.jquery.ui.markup.html.link.Link;
import com.mycompany.control.ServicePF;
import com.mycompany.control.ServiceRelatorios;
import com.mycompany.model.PessoaFisica;
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

public class CrudPessoaFisica extends BasePage {

    @SpringBean(name = "pfService")
    ServicePF servicePF;
    @SpringBean(name = "relatoriosService")
    ServiceRelatorios<PessoaFisica> serviceRelatorios;

    final PessoaFisica pessoaFisica = new PessoaFisica();

    private List<PessoaFisica> listaDePessoasFisicas = new ArrayList<PessoaFisica>();
    Form<PessoaFisica> form;
    TextField<String> inputNome = new TextField<String>("nome");
    TextField<String> inputCpf = new TextField<String>("cpf");
    private String nomeFiltrar = "";
    private String cpfFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirPf = new ModalWindow("modalinserirpf");
    ModalWindow modalWindowEditarPf = new ModalWindow("modaleditarpf");
    ModalWindow modalWindowExcluirPf = new ModalWindow("modalexcluirpf");

    public CrudPessoaFisica() {

        listaDePessoasFisicas.addAll(servicePF.listarPessoasFisicas(pessoaFisica));

        modalWindowInserirPf.setAutoSize(false);
        modalWindowInserirPf.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowEditarPf.setAutoSize(false);
        modalWindowEditarPf.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowExcluirPf.setAutoSize(true);
        modalWindowExcluirPf.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });

        CompoundPropertyModel<PessoaFisica> compoundPropertyModelPessoaFisica =
                new CompoundPropertyModel<PessoaFisica>(pessoaFisica);

        form = new Form<PessoaFisica>("formpf", compoundPropertyModelPessoaFisica) {
            @Override
            public void onSubmit() {

            }
        };
        form.setOutputMarkupId(true);
        add(form);
        form.add(criarTextFieldNomefiltro());
        form.add(criarTextFieldCpfFiltro());
        form.add(criarBtnFiltrar());
        form.add(criarBtnInserir());
        form.add(criarTabela());
        form.add(criarModalInserirPf());
        form.add(criarModalEditarPf());
        form.add(criarModalExluirPf());
        form.add(criarRelatorioExcel());
        form.add(criarRelatorioJasper());
    }

    private TextField<String> criarTextFieldNomefiltro() {
        //TextField<String> inputNome = new TextField<String>("nome");
        return inputNome;
    }

    private TextField<String> criarTextFieldCpfFiltro() {
        //TextField<String> inputAgencia = new TextField<String>("agencia");
        return inputCpf;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<PessoaFisica> listDataProvider = new ListDataProvider<PessoaFisica>() {

            @Override
            protected List<PessoaFisica> getData() {
                return listaDePessoasFisicas;
            }
        };

        DataView<PessoaFisica> dataView = new DataView<PessoaFisica>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<PessoaFisica> item) {

                rowPanel.setOutputMarkupId(true);

                final PessoaFisica pfDaLista = (PessoaFisica) item.getModelObject();
                Label textnome = new Label("textnome", pfDaLista.getNome());
                Label textcpf = new Label("textcpf", pfDaLista.getCpf());
                Label texttelefone = new Label("texttelefone", pfDaLista.getTelefone());

                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {
                        final PessoaFisicaPanel modalEditarPessoaFisica = new
                                PessoaFisicaPanel(modalWindowEditarPf.getContentId(), pfDaLista) {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, PessoaFisica pessoaFisica) {
                                        super.executaAoClicarEmSalvar(target, pessoaFisica);
                                        servicePF.executarAoClicarEmSalvarNaModalEditar(listaDePessoasFisicas, pessoaFisica,
                                                target,rowPanel,modalWindowEditarPf,feedbackPanel);
                                        target.add(feedbackPanel);
                                    }
                                };

                        modalWindowEditarPf.setContent(modalEditarPessoaFisica);
                        modalWindowEditarPf.show(target);
                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final PanelExcluir<PessoaFisica> panelExcluirPessoaFisica = new PanelExcluir<PessoaFisica>(modalWindowExcluirPf.getContentId()) {
                            @Override
                            public void excluir(AjaxRequestTarget target, PessoaFisica pessoaFisica) {
                                super.excluir(target, pessoaFisica);
                                servicePF.deletarPessoaFisica(pfDaLista);
                                listaDePessoasFisicas.clear();
                                listaDePessoasFisicas.addAll(servicePF.listarPessoasFisicas(pfDaLista));
                                modalWindowExcluirPf.close(target);
                                target.add(rowPanel);
                            }

                            @Override
                            public void fecharSemExcluir(AjaxRequestTarget target, PessoaFisica pessoaFisica) {
                                super.fecharSemExcluir(target, pessoaFisica);
                                modalWindowExcluirPf.close(target);
                            }

                            @Override
                            public Label mostrarValorASerExcluido(String string) {
                                return super.mostrarValorASerExcluido(pfDaLista.getNome());
                            }
                        };

                        modalWindowExcluirPf.setContent(panelExcluirPessoaFisica);
                        modalWindowExcluirPf.show(target);
                    }
                };

                editar.setOutputMarkupId(true);
                excluir.setOutputMarkupId(true);

                item.add(textnome);
                item.add(textcpf);
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

    private ModalWindow criarModalInserirPf() {
        return modalWindowInserirPf;
    }

    private ModalWindow criarModalEditarPf() {
        return modalWindowEditarPf;
    }

    private ModalWindow criarModalExluirPf() {
        return modalWindowExcluirPf;
    }

    private AjaxLink<?> criarBtnInserir() {
        AjaxLink<?> inserir = new AjaxLink<Object>("inserir") {
            public void onClick(AjaxRequestTarget target) {

                final PessoaFisicaPanel pessoaFisicaPanel = new PessoaFisicaPanel
                        (modalWindowInserirPf.getContentId(), new PessoaFisica()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, PessoaFisica pessoaFisica) {
                        servicePF.executarAoClicarEmSalvarNaModalSalvar(listaDePessoasFisicas,pessoaFisica,target,
                                rowPanel,modalWindowInserirPf,feedbackPanel);
                        target.add(feedbackPanel);
                    }
                };
                modalWindowInserirPf.setContent(pessoaFisicaPanel);
                modalWindowInserirPf.show(target);
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
                cpfFiltrar = inputCpf.getInput();
                String nome = nomeFiltrar;
                String cpf = cpfFiltrar;
                servicePF.filtrarPessoaFisicaNaVisao(nome, cpf, listaDePessoasFisicas, pessoaFisica, target, rowPanel);
            }
        };
        return filtrar;
    }



    Link<?> criarRelatorioJasper() {

        Link<?> btnRelatorio = new Link<Object>("relatorio"){


            private static final long serialVersionUID = -5081583125636401676L;


            @Override
            public void onClick() {
                serviceRelatorios.gererRelatorioPDF(listaDePessoasFisicas,"Pessoa_Fisica","Pessoas_Físicas");

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
                serviceRelatorios.gerarRelatorioExcelPessoaFisica(listaDePessoasFisicas);
            }

        };
        btnExcel.setOutputMarkupId(true);
        return btnExcel;

    }
}
