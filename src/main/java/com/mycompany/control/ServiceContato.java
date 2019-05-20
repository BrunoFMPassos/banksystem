package com.mycompany.control;

import com.mycompany.DAO.DaoContato;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Conta;
import com.mycompany.model.Contato;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceContato {
    @SpringBean(name = "genericDao")
    private GenericDao<Contato> genericDao;

    @SpringBean(name = "contatoDao")
    private DaoContato daoContato;

    public void inserir(Contato contato){
        genericDao.inserir(contato);
    }

    public List<Contato> pesquisaListaDeContatosPorConta(Contato contato, Conta conta){
        return daoContato.pesquisarListaDeObjetosContatoPorConta(contato,conta);
    }


    public void setGenericDao(GenericDao<Contato> genericDao) {
        this.genericDao = genericDao;
    }

    public void setDaoContato(DaoContato daoContato) {
        this.daoContato = daoContato;
    }
}
