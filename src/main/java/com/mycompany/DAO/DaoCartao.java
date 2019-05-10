package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.Cartao;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class DaoCartao extends GenericDAOImpl<Cartao, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;


    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
