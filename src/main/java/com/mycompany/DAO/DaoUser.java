package com.mycompany.DAO;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.mycompany.model.User;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;


@SuppressWarnings("deprecation")
public class DaoUser extends GenericDao<User> implements Serializable {

	private static final long serialVersionUID = 5608018075698240400L;


    public User searchForUserName(String username) {

		User user = new User();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		String hql = "from " + user.getClass().getCanonicalName()
				+ " as c where c.username = :username";
		System.out.println(hql);

		Query query = session.createQuery(hql);
		query.setParameter("username",username);
		user = (User) query.uniqueResult();
		session.close();
		
		return user;

	}

}
