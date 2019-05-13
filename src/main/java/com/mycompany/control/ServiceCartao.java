package com.mycompany.control;

import com.mycompany.DAO.DaoCartao;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Cartao;
import com.mycompany.model.TipoDeCartao;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceCartao {
    @SpringBean(name = "genericDao")
    private GenericDao<Cartao> genericDao;

    @SpringBean(name = "cartaoDao")
    private DaoCartao daoCartao;


    public void inserir(Cartao cartao){
        genericDao.inserir(cartao);
    }


    public List<Cartao> pesquisaListaDeCartoesExistentes(Cartao cartao){
        return genericDao.pesquisarListaDeObjeto(cartao);
    }

    public void deletarCartao(Cartao cartao){
       genericDao.deletar(cartao);
    }


    public void setDaoCartao(DaoCartao daoCartao) {
        this.daoCartao = daoCartao;
    }

    public void setGenericDao(GenericDao<Cartao> genericDao) {
        this.genericDao = genericDao;
    }
}
