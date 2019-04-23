package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.control.ServiceColaborador;
import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
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
    private static final long serialVersionUID = -2798690654299606509L;

    @SpringBean(name = "colaboradorService")
    ServiceColaborador sc;
    final Colaborador colaborador = new Colaborador();
    private List<Colaborador> listadecolaboradores = new ArrayList<Colaborador>();
    Form<Colaborador> form;
    TextField<String> inputNome = new TextField<String>("nome");
    TextField<String> inputAgencia = new TextField<String>("agencia");
    private String nomeFiltrar = "";
    private String agenciaFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    //ModalWindow modalWindow = new ModalWindow("modalinserir");
    //ModalWindow modalWindoweditar = new ModalWindow("modaleditar");
    //ModalWindow modalWindowExcluir = new ModalWindow("modalexcluir");


    public CrudColaborador() {
        listadecolaboradores.addAll(sc.listColaborador(colaborador));
        CompoundPropertyModel<Colaborador> compoundPropertyModelColaborador = new CompoundPropertyModel<Colaborador>(colaborador);
        form = new Form<Colaborador>("formcolaborador", compoundPropertyModelColaborador) {
            @Override
            public void onSubmit() {

            }
        };

        add(form);
        form.add(criarTextFieldNomefiltro());
        form.add(criarTextFieldAgenciafiltro());
        form.add(criarBtnListar());
        form.add(criarBtnInserir());
        form.add(criarTabela());
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
                return listadecolaboradores;
            }
        };

        DataView<Colaborador> lv = new DataView<Colaborador>("rows", listDataProvider) {

            private static final long serialVersionUID = -527963121947516657L;

            @Override
            protected void populateItem(Item<Colaborador> item) {

                final Colaborador colaboradorDaLista = (Colaborador) item.getModelObject();
                Label textnome = new Label("textnome", colaboradorDaLista.getNome());
                User user =  sc.searchForUser(colaboradorDaLista);
                Label textusuario = new Label("textusuario", user.getUsername());
                Label textperfil = new Label("textperfil", user.getPerfil());


                AjaxLink<?> editar = new AjaxLink<Object>("editar") {

                    public void onClick(AjaxRequestTarget target) {
                        System.out.println("Clicou no editar");
                    }
                };

                AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        System.out.println("Clicou no excluir");
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


    private AjaxLink<?> criarBtnInserir() {
        AjaxLink<?> inserir = new AjaxLink<Object>("inserir") {

            public void onClick(AjaxRequestTarget target) {
                System.out.println("Clicou no inserir");
            }
        };

        return inserir;
    }


    private AjaxButton criarBtnListar() {

        AjaxButton botao = new AjaxButton("btn", form) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                nomeFiltrar = inputNome.getInput();
                agenciaFiltrar = inputAgencia.getInput();

                String nome = nomeFiltrar;
                String agencia = agenciaFiltrar;

                if (!nome.isEmpty() && agencia.isEmpty()){
                    listadecolaboradores.clear();
                    listadecolaboradores.addAll(sc.searchForNameList(colaborador,"nome",nome));
                    target.add(rowPanel);

                }else if(nome.isEmpty() && !agencia.isEmpty()){
                    listadecolaboradores.clear();
                    listadecolaboradores.addAll(sc.searchForNameList(colaborador,"agencia",agencia));
                    target.add(rowPanel);

                }else if(!nome.isEmpty() && !agencia.isEmpty()){
                    listadecolaboradores.clear();
                    listadecolaboradores.addAll(sc.searchForNameList2Tables(colaborador,"nome","agencia",nome,agencia));
                    target.add(rowPanel);
                } else {
                    listadecolaboradores.clear();
                    listadecolaboradores.addAll(sc.listColaborador(colaborador));
                }
                target.add(rowPanel);
            }
        };
        return botao;
    }
}
