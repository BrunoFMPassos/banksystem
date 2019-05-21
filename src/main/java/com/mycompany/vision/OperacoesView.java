package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.control.ServiceAgencia;
import com.mycompany.control.ServiceConta;
import com.mycompany.control.ServiceOperacoes;
import com.mycompany.control.ServiceTipoDeConta;
import com.mycompany.model.*;
import org.apache.wicket.AttributeModifier;
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

public class OperacoesView extends BasePage{
    @SpringBean(name = "contaService")
    ServiceConta serviceConta;
    @SpringBean(name = "operacoesService")
    ServiceOperacoes serviceOperacoes;
    @SpringBean(name = "agenciaService")
    ServiceAgencia serviceAgencia;
    @SpringBean(name = "tipoDeContaService")
    ServiceTipoDeConta serviceTipoDeConta;
    Conta conta = new Conta();
    private List<Conta> listaDeContas = new ArrayList<Conta>();
    Form<Conta> form;
    TextField<String> inputNumero = new TextField<String>("numero");


    private String numeroFiltrar = "";
    private String agenciaFiltrar = "";
    private String tipoFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalOperacao = new ModalWindow("modaloperacao");

    public OperacoesView() {
        listaDeContas.addAll(serviceConta.pesquisarListaDeContasParaOperacoes(conta));
        modalOperacao.setAutoSize(false);
        modalOperacao.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalOperacao.setResizable(false);

        CompoundPropertyModel<Conta> compoundPropertyModelConta = new CompoundPropertyModel<Conta>(conta);
        form = new Form<Conta>("formconta", compoundPropertyModelConta) {
            @Override
            public void onSubmit() {
            }
        };

        add(form);
        form.add(criarModalOperacao());
        form.add(criarTextFieldTitularfiltro());
        form.add(criarBtnFiltrar());
        form.add(criarTabela());
    }

    private ModalWindow criarModalOperacao() {
        return modalOperacao;
    }

    private TextField<String> criarTextFieldTitularfiltro() {
        inputNumero.add(new AttributeModifier("onfocus", "$(this).mask('99999');"));
        return inputNumero;
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


                AjaxLink<?> saque = new AjaxLink<Object>("saque") {
                    public void onClick(AjaxRequestTarget target) {
                        final OperacoesPanel operacoesPanel = new
                                OperacoesPanel(modalOperacao.getContentId(),contaDaLista,"Saque") {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Conta conta) {
                                        super.executaAoClicarEmSalvar(target, conta);
                                        serviceOperacoes.executarAoCLicarEmFinalizarNaModal(listaDeContas,conta,
                                                target,modalOperacao,feedbackPanel,"Saque", conta.getSenhaVerificar(),
                                                conta.getValor(),conta.getNumeroContaDestino(),conta.getContatoObjeto());
                                        target.add(feedbackPanel);
                                    }
                                };
                        modalOperacao.setContent(operacoesPanel);
                        modalOperacao.show(target);
                    }
                };

                final AjaxLink<?> deposito = new AjaxLink<Object>("deposito") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final OperacoesPanel operacoesPanel = new
                                OperacoesPanel(modalOperacao.getContentId(),contaDaLista,"Deposito") {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Conta conta) {
                                        super.executaAoClicarEmSalvar(target, conta);
                                        serviceOperacoes.executarAoCLicarEmFinalizarNaModal(listaDeContas,conta,
                                                target,modalOperacao,feedbackPanel,"Deposito", conta.getSenhaVerificar(),
                                                conta.getValor(),conta.getNumeroContaDestino(),conta.getContatoObjeto());
                                        target.add(feedbackPanel);
                                    }
                                };
                        modalOperacao.setContent(operacoesPanel);
                        modalOperacao.show(target);
                    }

                };

                final AjaxLink<?> transferencia = new AjaxLink<Object>("transferencia") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final OperacoesPanel operacoesPanel = new
                                OperacoesPanel(modalOperacao.getContentId(),contaDaLista,"Transferencia") {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Conta conta) {
                                        super.executaAoClicarEmSalvar(target, conta);
                                        serviceOperacoes.executarAoCLicarEmFinalizarNaModal(listaDeContas,conta,
                                                target,modalOperacao,feedbackPanel,"Transferencia", conta.getSenhaVerificar(),
                                                conta.getValor(),conta.getNumeroContaDestino(),conta.getContatoObjeto());
                                        target.add(feedbackPanel);
                                    }
                                };
                        modalOperacao.setContent(operacoesPanel);
                        modalOperacao.show(target);
                    }

                };

                serviceOperacoes.ocultarBtnParaContaSalarioNaVisao(deposito,contaDaLista);
                serviceOperacoes.ocultarBtnParaContaSalarioNaVisao(transferencia,contaDaLista);

                item.add(textnumero);
                item.add(texttitular);
                item.add(saque);
                item.add(deposito);
                item.add(transferencia);
            }
        };

        dataView.setItemsPerPage(5);
        rowPanel.add(dataView);
        rowPanel.add(new PagingNavigator("navigator", dataView));
        return rowPanel;
    }


    private AjaxButton criarBtnFiltrar() {
        AjaxButton filtrar = new AjaxButton("filtrarconta", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                numeroFiltrar = inputNumero.getInput();
                String numero = numeroFiltrar;
                serviceConta.filtrarContaNaVisaoOperacoes(numero,listaDeContas,conta,target,rowPanel);
            }
        };
        return filtrar;
    }
}
