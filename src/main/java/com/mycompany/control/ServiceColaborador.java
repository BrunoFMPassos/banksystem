package com.mycompany.control;


import com.mycompany.DAO.DaoColaborador;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.lang.annotation.Target;
import java.util.List;

public class ServiceColaborador {

    @SpringBean(name = "colaboradorDao")
    private DaoColaborador colaboradorDao;
    @SpringBean(name = "genericDao")
    private GenericDao<Colaborador> genericDao;

    public void inserir(Colaborador colaborador) {
        colaboradorDao.inserir(colaborador);
    }

    public Colaborador pesquisarObjetoColaboradorPorNome(String nome) {
        return colaboradorDao.pesquisaObjetoColaboradorPorNome(nome);
    }

    public User pesquisarObjetoUserPorColaborador(Colaborador colaborador) {
        return colaboradorDao.pesquisarObjetoUserPorColaborador(colaborador);
    }

    public List<Colaborador> pesquisarListaDeColaboradoresPorColabordaor(Colaborador colaborador) {
        return genericDao.pesquisarListaDeObjeto(colaborador);
    }

    public List<Colaborador> pesquisarListaDeColaboradoresPorNome(Colaborador colaborador, String colum, String string) {
        return genericDao.pesquisaListadeObjetosPorString(colaborador, colum, string);
    }

    public List<Colaborador> pesquisarListaDeColaboradoresPorNomeEmDuasTabelas(Colaborador colaborador, String colum1, String colum2, String string1, String string2) {
        return genericDao.pesquisarListaDeObjetosPorStringEmDuasTabelas(colaborador, colum1, colum2, string1, string2);
    }

    public void deletarColaborador(Colaborador colaborador) {
        colaboradorDao.deletar(colaborador);
    }

    public void filtrarColaboradorNaVisao(String nome, String agencia, List<Colaborador> listaDeColaboradores, Colaborador colaborador, AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!nome.isEmpty() && agencia.isEmpty()) {
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorNome(colaborador, "nome", nome));
            target.add(rowPanel);

        } else if (nome.isEmpty() && !agencia.isEmpty()) {
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorNome(colaborador, "agencia", agencia));
            target.add(rowPanel);

        } else if (!nome.isEmpty() && !agencia.isEmpty()) {
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorNomeEmDuasTabelas(colaborador, "nome", "agencia", nome, agencia));
            target.add(rowPanel);
        } else {
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorColabordaor(colaborador));
        }
        target.add(rowPanel);
    }

    public void executarAoClicarEmSalvarNaModal(
            List<Colaborador> listaDeColaboradores, Colaborador colaborador,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow) {
            System.out.println("Clicou no Salvar!");
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorColabordaor(colaborador));
            modalWindow.close(target);
            target.add(rowPanel);
    }

    public void setColaboradorDao(DaoColaborador colaboradorDao) {
        this.colaboradorDao = colaboradorDao;
    }
    public void setGenericDao(GenericDao<Colaborador> genericDao) {
        this.genericDao = genericDao;
    }
}
