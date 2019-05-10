package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServicePF;
import com.mycompany.model.PessoaFisica;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaPanel extends Panel {

    @SpringBean(name = "pfService")
    ServicePF servicePF;

    private PessoaFisica pessoaFisica;

    Form<PessoaFisica> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");

    public PessoaFisicaPanel(String id, PessoaFisica pessoaFisica) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.pessoaFisica = pessoaFisica;
        add(Criarcontainer());
    }

    private WebMarkupContainer Criarcontainer() {

        WebMarkupContainer container = new WebMarkupContainer("container");

        form = new Form<PessoaFisica>("formulariocadastropf", new CompoundPropertyModel<PessoaFisica>(pessoaFisica));

        form.setOutputMarkupId(true);
        form.add(criarBtnSalvar());
        form.add(feedbackPanel);
        form.add(criarTextFieldNome());
        form.add(criarTextFieldCpf());
        form.add(criarTextFieldRg());
        form.add(criarTextFieldDataDeNascimento());
        form.add(criarRadioGrupoSexo());
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
        AjaxButton inserir = new AjaxButton("salvarpf") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target, pessoaFisica);
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

    private TextField<String> criarTextFieldNome() {
        TextField<String> nome = new TextField<String>("nome");
        return nome;
    }

    private TextField<String> criarTextFieldCpf() {
        TextField<String> cpf = new TextField<String>("cpf");
        cpf.add(new AttributeModifier("onfocus", "$(this).mask('999.999.999-99');"));
        return cpf;
    }

    private TextField<String> criarTextFieldRg() {
        TextField<String> rg = new TextField<String>("rg");
        return rg;
    }

    private DateTextField criarTextFieldDataDeNascimento(){
        DatePicker datePicker = new DatePicker(){
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean alignWithIcon() {
                return true;
            }
            @Override
            protected boolean enableMonthYearSelection() {
                return true;
            }
        };

        DateTextField data = new DateTextField("dataDeNascimento");
        data.add(datePicker);
        data.add(new AttributeModifier("onfocus", "$(this).mask('99/99/99');"));
        return data;
    }

    private TextField<String> criarTextFieldTelefone() {
        TextField<String> telefone = new TextField<String>("telefone");
        telefone.add(new AttributeModifier("onfocus", "$(this).mask('(99)9 9999-9999');"));
        return telefone;
    }

    private TextField<String> criarTextFieldRendaMensal() {
        TextField<String> rendaMensal = new TextField<String>("rendaMensal");
        return rendaMensal;
    }

    private RadioGroup<?> criarRadioGrupoSexo() {
        RadioGroup<?> sexo = new RadioGroup<Object>("sexo");
        sexo.add(new Radio<String>("masculino", new Model<String>("masculino")));
        sexo.add(new Radio<String>("feminino", new Model<String>("feminino")));
        return sexo;
    }

    private TextField<String> criarTextFieldCidade() {
        TextField<String> cidade = new TextField<String>("cidade");
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
        return selectUF;
    }

    private TextField<String> criarTextFieldEndereco() {
        TextField<String> endereco = new TextField<String>("endereco");
        return endereco;
    }

    private TextField<String> criarTextFieldBairro() {
        TextField<String> bairro = new TextField<String>("bairro");
        return bairro;
    }

    private TextField<String> criarTextFieldComplemento() {
        TextField<String> complemento = new TextField<String>("complemento");
        return complemento;
    }

    private TextField<String> criarTextFieldCep() {
        TextField<String> cep = new TextField<String>("cep");
        //cep.add(new AttributeModifier("onfocus", "$(this).mask('99999-999');"));
        return cep;
    }


    public void executaAoClicarEmSalvar(AjaxRequestTarget target, PessoaFisica pessoaFisica) {

    }
}
