package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.Conta;
import com.mycompany.model.Contato;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class DaoContato extends GenericDAOImpl<Contato, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;


    public List<Contato> pesquisarListaDeObjetosContatoPorConta(Contato contato, Conta conta){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + contato.getClass().getCanonicalName() + " as c where c.conta = :conta";
        Query query = session.createQuery(hql);
        query.setParameter("conta", conta);
        @SuppressWarnings("unchecked")
        List<Contato> results = query.list();
        session.close();
        return results;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
