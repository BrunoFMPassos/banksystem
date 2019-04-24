package com.mycompany.DAO;

import java.io.Serializable;
import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;

import com.googlecode.genericdao.search.Search;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;

import com.mycompany.model.User;
import org.hibernate.SessionFactory;


@SuppressWarnings("deprecation")
public class DaoUser extends GenericDAOImpl<User, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    private static final long serialVersionUID = 5608018075698240400L;

    public User pesquisarObjetoUsuarioPorNome(String username) {
        User user = new User();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + user.getClass().getCanonicalName()
                + " as c where c.username = :username";
        System.out.println(hql);
        Query query = session.createQuery(hql);
        query.setParameter("username", username);
        user = (User) query.uniqueResult();
        session.close();
        return user;
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
