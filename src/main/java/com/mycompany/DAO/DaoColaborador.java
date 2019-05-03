package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Search;
import com.mycompany.model.Agencia;
import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class DaoColaborador extends GenericDAOImpl<Colaborador, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;


    public void inserir(Colaborador colaborador, User user) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.saveOrUpdate(colaborador);
            session.getTransaction().commit();
            session.close();
    }

    public Colaborador pesquisaObjetoColaboradorPorNome(String nome) {
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

    public Colaborador pesquisaObjetoColaboradorPorId(Long id) {
        Colaborador colaborador = new Colaborador();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + colaborador.getClass().getCanonicalName()
                + " as c where c.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        colaborador = (Colaborador) query.uniqueResult();
        session.close();
        return colaborador;
    }

    public User pesquisarObjetoUserPorColaborador(Colaborador colaborador) {
        User user = colaborador.getUser();
        return user;
    }

    public List<Colaborador> pesquisaListadeObjetoColaboradorPorAgencia(Colaborador colaborador, String colum, Agencia agencia) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Long id = agencia.getId();
        //List<Colaborador> results = agencia.getColaboradores();
        String hql = "select nome from " + colaborador.getClass().getCanonicalName() + " as c where c.agencia = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        @SuppressWarnings("unchecked")
        List<Colaborador> results = query.list();
        session.close();
        return results;
    }


    public List<User> pesquisarListaDeUsuariosPorUsername(String username) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<User> listaDeUsuarios = search(new Search(User.class).addFilterLike("username", "%" + username + "%"));
        session.close();
        return listaDeUsuarios;
    }

    public List<Colaborador> pesquisarListaDeColaboradoresPorNome(String nome) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Colaborador> listaDeColaboradores = search(new Search(Colaborador.class).addFilterLike("nome", "%" + nome + "%"));
        session.close();
        return listaDeColaboradores;
    }

    public List<User> pesquisarListaDeUsuariosExistentes() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criterio = session.createCriteria(User.class);
        List<User> listaDeUsers = criterio.list();
        session.getTransaction().commit();
        session.close();
        return listaDeUsers;
    }

    public List<Colaborador> pesquisarListaDeColaboradoresExistentes() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criterio = session.createCriteria(Colaborador.class);
        List<Colaborador> listaDeColaboradores = criterio.list();
        session.getTransaction().commit();
        session.close();
        return listaDeColaboradores;
    }

    public void deletar(Colaborador colaborador) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = (pesquisarObjetoUserPorColaborador(colaborador));
        session.delete(colaborador);
        session.delete(user);
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
