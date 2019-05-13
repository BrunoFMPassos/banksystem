package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public List<Conta> pesquisarListaDeObjetosContaPorAgencia(Conta conta, Agencia agencia){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + conta.getClass().getCanonicalName() + " as c where c.agencia = :agencia";
        Query query = session.createQuery(hql);
        query.setParameter("agencia", agencia);
        @SuppressWarnings("unchecked")
        List<Conta> results = query.list();
        session.close();
        return results;
    }

    public List<Conta> pesquisarListaDeObjetosContaPorTipo(Conta conta, TipoDeConta tipoDeConta){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + conta.getClass().getCanonicalName() + " as c where c.tipoDeConta = :tipoDeConta";
        Query query = session.createQuery(hql);
        query.setParameter("tipoDeConta", tipoDeConta);
        @SuppressWarnings("unchecked")
        List<Conta> results = query.list();
        session.close();
        return results;
    }

    public List<Conta> pesquisarListaDeContas(Conta conta) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criterio = session.createCriteria(conta.getClass());
        List<Conta> resultado = criterio.list();
        session.getTransaction().commit();
        session.close();
        return resultado;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
