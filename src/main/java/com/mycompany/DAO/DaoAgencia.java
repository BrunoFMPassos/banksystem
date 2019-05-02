package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.Agencia;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;

public class DaoAgencia extends GenericDAOImpl<Agencia, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    public Agencia pesquisaObjetoAgenciaPorNumero(String numero) {
        int numeroInt = Integer.valueOf(numero);
        Agencia agencia = new Agencia();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + agencia.getClass().getCanonicalName()
                + " as a where a.numero = :numeroInt";
        Query query = session.createQuery(hql);
        query.setParameter("numeroInt", numeroInt);
        agencia = (Agencia) query.uniqueResult();
        session.close();
        return agencia;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
