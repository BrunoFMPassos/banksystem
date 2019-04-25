package com.mycompany.control;

import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Agencia;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceAgencia {
    @SpringBean(name = "genericDao")
    private GenericDao<Agencia> genericDao;


    public List<Agencia> pesquisarListaDeAgenciasPorAgencia(Agencia agencia){
        return genericDao.pesquisarListaDeObjeto(agencia);
    }

    public void setGenericDao(GenericDao<Agencia> genericDao) {
        this.genericDao = genericDao;
    }
}
