package com.mycompany.control;


import com.mycompany.DAO.DaoColaborador;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceColaborador {

    @SpringBean(name = "colaboradorDao")
    private DaoColaborador colaboradorDao;
    @SpringBean(name = "genericDao")
    private GenericDao<Colaborador> genericDao;


    public void insert(Colaborador colaborador) {
        colaboradorDao.insert(colaborador);
    }


    public Colaborador searchForName(String nome) {

        return colaboradorDao.searchForName(nome);
    }

    public User searchForUser(Colaborador colaborador) {
        return colaboradorDao.searchForUser(colaborador);
    }

    public List<Colaborador> listColaborador(Colaborador colaborador) {
        return genericDao.list(colaborador);
    }


    public void deleteColaborador(Colaborador colaborador) {
        genericDao.delete(colaborador);
    }

    public List<Colaborador> searchForNameList(Colaborador colaborador, String string, String colum){
        return genericDao.searchForString(colaborador,string,colum);
    }

    public void setColaboradorDao(DaoColaborador colaboradorDao) {
        this.colaboradorDao = colaboradorDao;
    }

    public void setGenericDao(GenericDao<Colaborador> genericDao) {
        this.genericDao = genericDao;
    }
}
