package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.TipoDeConta;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
;

public class DaoTipoDeConta  extends GenericDAOImpl<TipoDeConta, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    public TipoDeConta pesquisaObjetoTipoDeContaPorDescricao(String descricao) {
       TipoDeConta tipoDeConta = new TipoDeConta();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + tipoDeConta.getClass().getCanonicalName()
                + " as p where p.descricao = :descricao";
        Query query = session.createQuery(hql);
        query.setParameter("descricao", descricao);
        tipoDeConta = (TipoDeConta) query.uniqueResult();
        session.close();
        return tipoDeConta;
    }

    public TipoDeConta pesquisaObjetoTipoDeContaPorId(Long id) {
        TipoDeConta tipoDeConta = new TipoDeConta();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + tipoDeConta.getClass().getCanonicalName()
                + " as p where p.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        tipoDeConta = (TipoDeConta) query.uniqueResult();
        session.close();
        return tipoDeConta;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
