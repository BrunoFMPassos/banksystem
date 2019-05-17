package com.mycompany.DAO;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class GenericDao<T extends Object> implements Serializable {

    GenericDao<T> dao;


    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    public void setDao(GenericDao<T> dao) {
        this.dao = dao;
    }

    public void inserir(T obj) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(obj);
        session.getTransaction().commit();
        session.close();
    }


    public List<T> pesquisarListaDeObjeto(T obj) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criterio = session.createCriteria(obj.getClass());
        List<T> resultado = criterio.list();
        session.getTransaction().commit();
        session.close();
        return resultado;
    }

    public List<T> pesquisaListadeObjetosPorString(T obj, String colum, String string) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + obj.getClass().getCanonicalName() + " as c where c." + colum + " like :string";
        Query query = session.createQuery(hql);
        query.setParameter("string", "%" + string + "%");
        @SuppressWarnings("unchecked")
        List<T> results = query.list();
        session.close();
        return results;
    }

    public List<T> pesquisaListadeObjetosPorLong(T obj, String colum, Long long1) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + obj.getClass().getCanonicalName() + " as c where c." + colum + " =:long1";
        Query query = session.createQuery(hql);
        query.setParameter("long1", long1);
        @SuppressWarnings("unchecked")
        List<T> results = query.list();
        session.close();
        return results;
    }

    public List<T> pesquisarListaDeObjetosPorStringEmDuasColunas(T obj, String colum1, String colum2,
                                                                 String string1, String string2) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + obj.getClass().getCanonicalName() + " as c where c." + colum1 +
                " like :string1 AND c." + colum2 + " like :string2";
        Query query = session.createQuery(hql);
        query.setParameter("string1", "%" + string1 + "%");
        query.setParameter("string2", "%" + string2 + "%");
        @SuppressWarnings("unchecked")
        List<T> results = query.list();
        session.close();
        return results;
    }

    public List<T> pesquisarListaDeObjetosPorStringELongEmDuasColunas(T obj, String colum1, String colum2,
                                                                      String string, Long numero) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + obj.getClass().getCanonicalName() + " as c where c." + colum1 +
                " like :string AND c." + colum2 + " like :numero";
        Query query = session.createQuery(hql);
        query.setParameter("string", "%" + string + "%");
        query.setParameter("numero", "%" + numero + "%");
        @SuppressWarnings("unchecked")
        List<T> results = query.list();
        session.close();
        return results;
    }

    public void deletar(T obj) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(obj);
        session.getTransaction().commit();
        session.close();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


}
