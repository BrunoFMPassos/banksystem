package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Search;
import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class DaoColaborador extends GenericDAOImpl<Colaborador, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;


    //retorna o objeto
    public Colaborador searchForName(String nome) {
        Colaborador colaborador = new Colaborador();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String hql = "from " + colaborador.getClass().getCanonicalName()
                + " as c where c.nome = :nome";

        Query query = session.createQuery(hql);
        query.setParameter("nome", nome);
        colaborador = (Colaborador) query.uniqueResult();
        session.close();

        return colaborador;

    }

    public List<User> buscarporNomeSearch(String nome) {
        setSessionFactory(sessionFactory);
        sessionFactory.getCurrentSession().beginTransaction();

        return search(new Search(User.class).addFilterLike("nome", "%" + nome + "%"));
    }

    public void insert(Colaborador colaborador) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = new User();
        user.setUsername(colaborador.getUsername());
        user.setPassword(colaborador.getPassword());
        user.setPerfil(colaborador.getPerfil());
        user.setColaborador(colaborador);
        session.saveOrUpdate(colaborador);
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();
    }


    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
