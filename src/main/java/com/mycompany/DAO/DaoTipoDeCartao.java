package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.TipoDeCartao;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;

public class DaoTipoDeCartao  extends GenericDAOImpl<TipoDeCartao, Long> implements Serializable {
    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    public TipoDeCartao pesquisaObjetoTipoDeCartaoPorDescricao(String descricao) {
        TipoDeCartao tipoDeCartao = new TipoDeCartao();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + tipoDeCartao.getClass().getCanonicalName()
                + " as p where p.descricao = :descricao";
        Query query = session.createQuery(hql);
        query.setParameter("descricao", descricao);
        tipoDeCartao = (TipoDeCartao) query.uniqueResult();
        session.close();
        return tipoDeCartao;
    }

    public TipoDeCartao pesquisaObjetoTipoDeCartaoPorId(Long id) {
        TipoDeCartao tipoDeCartao = new TipoDeCartao();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + tipoDeCartao.getClass().getCanonicalName()
                + " as p where p.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        tipoDeCartao = (TipoDeCartao) query.uniqueResult();
        session.close();
        return tipoDeCartao;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
