package com.mycompany.control;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;



import com.googlecode.genericdao.search.Search;
import com.mycompany.model.User;

@SuppressWarnings("deprecation")
public class DaoUser<T extends Object> implements Serializable {

	private static final long serialVersionUID = 5608018075698240400L;

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

	public User searchForUserName(String username) {
		
		User user = new User();

		SessionFactory factory = new AnnotationConfiguration().configure()
				.buildSessionFactory();
		Session session = factory.openSession();
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
