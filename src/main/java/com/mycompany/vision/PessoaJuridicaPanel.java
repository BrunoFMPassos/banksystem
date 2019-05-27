package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServicePJ;
import com.mycompany.model.PessoaFisica;
import com.mycompany.model.PessoaJuridica;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaPanel extends Panel {

    @SpringBean(name = "pjService")
    ServicePJ servicePJ;

    private PessoaJuridica pessoaJuridica;

    Form<PessoaJuridica> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");


    public PessoaJuridicaPanel(String id, PessoaJuridica pessoaJuridica) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.pessoaJuridica = pessoaJuridica;
        add(Criarcontainer());
    }

    private WebMarkupContainer Criarcontainer() {

        WebMarkupContainer container = new WebMarkupContainer("container");

        form = new Form<PessoaJuridica>("formulariocadastropj", new CompoundPropertyModel<PessoaJuridica>(pessoaJuridica));

        form.setOutputMarkupId(true);
        form.add(criarBtnSalvar());
        form.add(feedbackPanel);
        form.add(criarTextFieldRazaoSocial());
        form.add(criarTextFieldCnpj());
        form.add(criarTextFieldInscricaoEstadual());
        form.add(criarTextFieldNomeFantasia());
        form.add(criarTextFieldCidade());
        form.add(criarSelectUF());
        form.add(criarTextFieldEndereco());
        form.add(criarTextFieldBairro());
        form.add(criarTextFieldComplemento());
        form.add(criarTextFieldCep());
        form.add(criarTextFieldRendaMensal());
        form.add(criarTextFieldTelefone());
        container.add(form);
        return container;

    }

    private AjaxButton criarBtnSalvar() {
        AjaxButton inserir = new AjaxButton("salvarpj") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target, pessoaJuridica);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }

        };
        inserir.setOutputMarkupId(true);
        return inserir;
    }

    private TextField<String> criarTextFieldRazaoSocial() {
        TextField<String> razaoSocial = new TextField<String>("razaoSocial");
        razaoSocial.setRequired(true);
        return razaoSocial;
    }

    private TextField<String> criarTextFieldNomeFantasia() {
        TextField<String> nomeFantasia = new TextField<String>("nomeFantasia");
        return nomeFantasia;
    }

    private TextField<String> criarTextFieldCnpj() {
        TextField<String> cnpj = new TextField<String>("cnpj");
        cnpj.add(new AttributeModifier("onfocus", "$(this).mask('99.999.999/9999-99');"));
        cnpj.setRequired(true);
        return cnpj;
    }

    private TextField<String> criarTextFieldInscricaoEstadual() {
        TextField<String> inscricaoEstadual = new TextField<String>("inscricaoEstadual");
        inscricaoEstadual.setRequired(true);
        return inscricaoEstadual;
    }

    private TextField<String> criarTextFieldTelefone() {
        TextField<String> telefone = new TextField<String>("telefone");
        telefone.add(new AttributeModifier("onfocus", "$(this).mask('(99)9 9999-9999');"));
        telefone.setRequired(true);
        return telefone;
    }

    private TextField<String> criarTextFieldRendaMensal() {
        TextField<String> rendaMensal = new TextField<String>("rendaMensal");
        rendaMensal.setRequired(true);
        return rendaMensal;
    }

    private TextField<String> criarTextFieldCidade() {
        TextField<String> cidade = new TextField<String>("cidade");
        cidade.setRequired(true);
        return cidade;
    }

    private DropDownChoice<String> criarSelectUF() {

        final List<String> listaDeEstados = new ArrayList<String>();
        listaDeEstados.add("AL");
        listaDeEstados.add("AP");
        listaDeEstados.add("AM");
        listaDeEstados.add("BA");
        listaDeEstados.add("CE");
        listaDeEstados.add("DF");
        listaDeEstados.add("ES");
        listaDeEstados.add("GO");
        listaDeEstados.add("MA");
        listaDeEstados.add("MT");
        listaDeEstados.add("MS");
        listaDeEstados.add("MG");
        listaDeEstados.add("PA");
        listaDeEstados.add("PB");
        listaDeEstados.add("PR");
        listaDeEstados.add("PE");
        listaDeEstados.add("PI");
        listaDeEstados.add("RJ");
        listaDeEstados.add("RN");
        listaDeEstados.add("RS");
        listaDeEstados.add("RO");
        listaDeEstados.add("RR");
        listaDeEstados.add("SC");
        listaDeEstados.add("SP");
        listaDeEstados.add("SE");
        listaDeEstados.add("TO");

        ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("UF") {
            @Override
            public Object getDisplayValue(String uf) {
                // TODO Auto-generated method stub
                return uf;
            }
        };

        IModel<List<String>> IModellist = new LoadableDetachableModel<List<String>>() {

            @Override
            protected List<String> load() {
                // TODO Auto-generated method stub
                return listaDeEstados;
            }
        };

        DropDownChoice<String> selectUF = new DropDownChoice<String>("UF",IModellist, choiceRenderer);
        selectUF.setOutputMarkupId(true);
        selectUF.setRequired(true);
        return selectUF;
    }

    private TextField<String> criarTextFieldEndereco() {
        TextField<String> endereco = new TextField<String>("enderecoDesc");
        endereco.setRequired(true);
        return endereco;
    }

    private TextField<String> criarTextFieldBairro() {
        TextField<String> bairro = new TextField<String>("bairro");
        bairro.setRequired(true);
        return bairro;
    }

    private TextField<String> criarTextFieldComplemento() {
        TextField<String> complemento = new TextField<String>("complemento");
        return complemento;
    }

    private TextField<String> criarTextFieldCep() {
        TextField<String> cep = new TextField<String>("cep");
        //cep.add(new AttributeModifier("onfocus", "$(this).mask('99999-999');"));
        cep.setRequired(true);
        return cep;
    }


    public void executaAoClicarEmSalvar(AjaxRequestTarget target, PessoaJuridica pessoaJuridica) {

    }

}
