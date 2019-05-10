package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.Conta;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;

public class DaoConta extends GenericDAOImpl<Conta, Long> implements Serializable {
    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;


    public Conta pesquisaObjetoContaPorNumero(Long numero) {
        Conta conta = new Conta();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + conta.getClass().getCanonicalName()
                + " as a where a.numero = :numero";
        Query query = session.createQuery(hql);
        query.setParameter("numero", numero);
        conta = (Conta) query.uniqueResult();
        session.close();
        return conta;
    }

    public Conta pesquisaObjetoContaPorId(Long id) {
        Conta conta = new Conta();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + conta.getClass().getCanonicalName()
                + " as a where a.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        conta = (Conta) query.uniqueResult();
        session.close();
        return conta;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
