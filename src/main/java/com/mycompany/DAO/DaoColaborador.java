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


    public void inserir(Colaborador colaborador, String Operacao) {
        Boolean colaboradorNull = verificaSeColaboradorNull(colaborador);
        if (colaboradorNull && Operacao == "inserir") {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            User user = pesquisarObjetoUserPorColaborador(colaborador);
            Boolean userNull = verificaSeUserNull(user);
            if (userNull) {
                user = new User();
                colaborador.setUser(user);
            }
            preparaUserParaInserir(colaborador, user);
            session.saveOrUpdate(user);
            session.saveOrUpdate(colaborador);
            session.getTransaction().commit();
            session.close();
        }else if(!colaboradorNull && Operacao == "update") {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            User user = pesquisarObjetoUserPorColaborador(colaborador);
            preparaUserParaInserir(colaborador,user);
            session.saveOrUpdate(user);
            session.saveOrUpdate(colaborador);
            session.getTransaction().commit();
            session.close();
        }else
        {
            System.out.println("Colaborador já existente!");
        }
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

    public void deletar(Colaborador colaborador) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = (pesquisarObjetoUserPorColaborador(colaborador));
        session.delete(colaborador);
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }


    public boolean verificaSeColaboradorNull(Colaborador colaborador) {
        Colaborador colaboradorparaVerificar = pesquisaObjetoColaboradorPorId(colaborador.getId());
        Boolean colaboradorNull;

        if (colaboradorparaVerificar == null) {
            colaboradorNull = true;
        } else {
            colaboradorNull = false;
        }
        return colaboradorNull;
    }

    public boolean verificaSeUserNull(User user) {
        Boolean userNull;
        if (user == null) {
            userNull = true;
        } else {
            userNull = false;
        }
        return userNull;
    }

    public void preparaUserParaInserir(Colaborador colaborador, User user) {
        user.setUsername(colaborador.getUsername());
        user.setPassword(colaborador.getPassword());
        user.setPerfil(colaborador.getPerfil());
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
