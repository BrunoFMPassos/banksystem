package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceAgencia;
import com.mycompany.model.Agencia;
import com.mycompany.model.Colaborador;
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

public class ColaboradorPanel extends Panel {

    @SpringBean(name = "agenciaService")
    ServiceAgencia serviceAgencia;

    private Colaborador colaborador;

    Form<Colaborador> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");


    public ColaboradorPanel(String id, Colaborador colaborador) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.colaborador = colaborador;
        add(Criarcontainer());
    }

    private WebMarkupContainer Criarcontainer() {

        WebMarkupContainer container = new WebMarkupContainer("container");

        form = new Form<Colaborador>("formulariocadastrocolaborador", new CompoundPropertyModel<Colaborador>(colaborador));

        form.setOutputMarkupId(true);
        form.add(criarBtnSalvar());
        form.add(feedbackPanel);
        form.add(criarTextFieldNome());
        form.add(criarTextFieldCpf());
        form.add(criarTextFieldRg());
        form.add(criarTextFieldDataDeNascimento());
        form.add(criarRadioGrupoSexo());
        form.add(criarSelectAgencia());
        form.add(criarTextFieldUsusername());
        form.add(criarTextFieldDPassword());
        form.add(criarSelectPerfil());
        form.add(criarTextFieldCidade());
        form.add(criarSelectUF());
        form.add(criarTextFieldEndereco());
        form.add(criarTextFieldBairro());
        form.add(criarTextFieldComplemento());
        form.add(criarTextFieldCep());
        container.add(form);
        return container;

    }

    private AjaxButton criarBtnSalvar() {
        AjaxButton inserir = new AjaxButton("salvarcolaborador") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target, colaborador);
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
        nome.setOutputMarkupId(true);
        nome.setRequired(true);
        return nome;
    }

    private TextField<String> criarTextFieldCpf() {
        TextField<String> cpf = new TextField<String>("cpf");
        cpf.add(new AttributeModifier("onfocus", "$(this).mask('999.999.999-99');"));
        cpf.setOutputMarkupId(true);
        cpf.setRequired(true);
        return cpf;
    }

    private TextField<String> criarTextFieldRg() {
        TextField<String> rg = new TextField<String>("rg");
        rg.setOutputMarkupId(true);
        rg.setRequired(true);
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
        data.setOutputMarkupId(true);
        data.setRequired(true);
        return data;
    }



    /*private TextField<String> criarTextFieldDataDeNascimento() {
        TextField<String> dataDeNascimento = new TextField<String>("dataDeNascimento");
        dataDeNascimento.add(new AttributeModifier("onfocus", "$(this).mask('99/99/9999');"));
        return dataDeNascimento;
    }*/

    private RadioGroup<?> criarRadioGrupoSexo() {
        RadioGroup<?> sexo = new RadioGroup<Object>("sexo");
        sexo.add(new Radio<String>("masculino", new Model<String>("masculino")));
        sexo.add(new Radio<String>("feminino", new Model<String>("feminino")));
        sexo.setOutputMarkupId(true);
        sexo.setRequired(true);
        return sexo;
    }

    private DropDownChoice<Agencia> criarSelectAgencia() {
        Agencia agencia = colaborador.getAgencia();

        if(agencia == null){
            agencia = new Agencia();
        }

        final List<Agencia> listaDeAgenciasPesquisa = serviceAgencia.pesquisarListaDeAgenciasPorAgencia(agencia);
        List<String> listaDeAgencias = new ArrayList<String>();
        for (Agencia agenciaLoop : listaDeAgenciasPesquisa) {
            listaDeAgencias.add(agenciaLoop.getNumero().toString());
        }
        ChoiceRenderer<Agencia> choiceRenderer = new ChoiceRenderer<Agencia>("numero", "id") {
            @Override
            public Object getDisplayValue(Agencia agencia) {
                // TODO Auto-generated method stub
                return agencia.getNumero();
            }
        };
        IModel<List<Agencia>> IModellist = new LoadableDetachableModel<List<Agencia>>() {
            @Override
            protected List<Agencia> load() {
                // TODO Auto-generated method stub
                return listaDeAgenciasPesquisa;
            }
        };

        DropDownChoice<Agencia> selectAgencia = new DropDownChoice<Agencia>(
                "agencia",
                IModellist, choiceRenderer
        );

        selectAgencia.setOutputMarkupId(true);
        selectAgencia.setRequired(true);
        return selectAgencia;

    }

    private TextField<String> criarTextFieldUsusername() {
        TextField<String> username = new TextField<String>("username");
        username.setOutputMarkupId(true);
        username.setRequired(true);
        return username;
    }

    private PasswordTextField criarTextFieldDPassword() {
        PasswordTextField password = new PasswordTextField("password");
        password.setRequired(false);
        password.setOutputMarkupId(true);
        return password;
    }

    private DropDownChoice<String> criarSelectPerfil() {

        final List<String> listaDePerfis = new ArrayList<String>();
        listaDePerfis.add("Diretor");
        listaDePerfis.add("Gerente");
        listaDePerfis.add("Caixa");

        ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("perfil") {
            @Override
            public Object getDisplayValue(String perfil) {
                // TODO Auto-generated method stub
                return perfil;
            }
        };

        IModel<List<String>> IModellist = new LoadableDetachableModel<List<String>>() {

            @Override
            protected List<String> load() {
                // TODO Auto-generated method stub
                return listaDePerfis;
            }
        };

        DropDownChoice<String> selectPerfil = new DropDownChoice<String>("perfil",IModellist, choiceRenderer);
        selectPerfil.setOutputMarkupId(true);
        selectPerfil.setRequired(true);
        return selectPerfil;
    }


    private TextField<String> criarTextFieldCidade() {
        TextField<String> cidade = new TextField<String>("cidade");
        cidade.setOutputMarkupId(true);
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
        TextField<String> endereco = new TextField<String>("endereco");
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


    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Colaborador colaborador) {

    }



}
