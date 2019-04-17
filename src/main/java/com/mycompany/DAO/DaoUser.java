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

    //retorna o objeto
    public User searchForUserName(String username) {


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

    public List<User> buscarporNomeSearch(String nome) {
        setSessionFactory(sessionFactory);
        sessionFactory.getCurrentSession().beginTransaction();

        return search(new Search(User.class).addFilterLike("nome", "%" + nome + "%"));
    }

    @SuppressWarnings("static-access")
    public List<User> buscarporSearchTeste(String username, String perfil) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Filter fusername = new Filter();
        fusername.like("username", "%" + username + "%");
        Filter fperfil = new Filter();
        fperfil.equal("perfil", perfil);

        Filter filterOr = Filter.or();

        Search searchUser = new Search(User.class);

        filterOr.add(Filter.equal("perfil", perfil));
        filterOr.add(Filter.like("username", "%" + username + "%"));

        searchUser.addFilter(filterOr);

        return search(searchUser);
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
