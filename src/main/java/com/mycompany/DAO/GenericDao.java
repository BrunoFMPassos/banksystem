package com.mycompany.DAO;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.io.Serializable;
import java.util.List;

public class GenericDao <T extends Object> implements Serializable {

    GenericDao<T> dao;

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    public void setDao(GenericDao<T> dao) {
        this.dao = dao;
    }

    public void insert(T obj) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(obj);
        System.out.println(obj);
        session.getTransaction().commit();
        session.close();
    }

    public List<T> list(T obj) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Criteria criterio = session.createCriteria(obj.getClass());
        @SuppressWarnings("unchecked")
        List<T> resultado = criterio.list();
        session.getTransaction().commit();
        session.close();
        System.out.println("Resultado da listagem" + resultado);
        return resultado;

    }

    public List<T> searchForString(T obj,String string, String colum){

        SessionFactory factory = new AnnotationConfiguration().configure().buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();

        String hql = "from "+obj.getClass().getCanonicalName()+ " as c where c."+"colum"+" like :string";
        System.out.println(hql);

        Query query = session.createQuery(hql);
        query.setParameter("nome","%"+string+"%");
        @SuppressWarnings("unchecked")
        List<T> results = query.list();
        session.close();
        System.out.println(results);
        return results;

    }

    public void delete(T obj) {
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
