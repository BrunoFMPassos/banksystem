package com.mycompany.control;

import com.mycompany.DAO.DaoAgencia;
import com.mycompany.DAO.DaoColaborador;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Agencia;
import org.apache.wicket.spring.injection.annot.SpringBean;


import java.util.List;

public class ServiceAgencia {
    @SpringBean(name = "genericDao")
    private GenericDao<Agencia> genericDao;

    @SpringBean(name = "agenciaDao")
    private DaoAgencia agenciaDao;


    public List<Agencia> pesquisarListaDeAgenciasPorAgencia(Agencia agencia){
        return genericDao.pesquisarListaDeObjeto(agencia);
    }

    public Agencia pesquisaObjetoAgenciaPorNumero(String numero){
        Agencia agencia = agenciaDao.pesquisaObjetoAgenciaPorNumero(numero);
        return agencia;
    }



    public void setGenericDao(GenericDao<Agencia> genericDao) {
        this.genericDao = genericDao;
    }

    public void setAgenciaDao(DaoAgencia agenciaDao) {
        this.agenciaDao = agenciaDao;
    }
}
