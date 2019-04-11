package com.mycompany.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.io.Serializable;
import java.util.List;

public class GenericDao <T extends Object> implements Serializable {

    public void insert(T obj) {

        SessionFactory factory = new AnnotationConfiguration().configure()
                .buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(obj);
        System.out.println(obj);
        session.getTransaction().commit();
        session.close();
    }

    public List<T> list(T obj) {

        SessionFactory factory = new AnnotationConfiguration().configure()
                .buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();

        Criteria criterio = session.createCriteria(obj.getClass());
        @SuppressWarnings("unchecked")
        List<T> resultado = criterio.list();
        session.getTransaction().commit();
        session.close();
        System.out.println("Resultado da listagem" + resultado);
        return resultado;

    }

    public void delete(T obj) {
        SessionFactory factory = new AnnotationConfiguration().configure()
                .buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();
        session.delete(obj);
        session.getTransaction().commit();
        session.close();
    }
}
