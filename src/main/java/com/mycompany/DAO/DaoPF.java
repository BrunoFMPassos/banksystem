package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.Agencia;
import com.mycompany.model.Pessoa;
import com.mycompany.model.PessoaFisica;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class DaoPF extends GenericDAOImpl<PessoaFisica, Long> implements Serializable {
    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;


    public PessoaFisica pesquisaObjetoPessoaFisicaPorNome(String nome) {
        PessoaFisica pessoaFisica = new PessoaFisica();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + pessoaFisica.getClass().getCanonicalName()
                + " as p where p.nome = :nome";
        Query query = session.createQuery(hql);
        query.setParameter("nome", nome);
        pessoaFisica = (PessoaFisica) query.uniqueResult();
        session.close();
        return pessoaFisica;
    }

    public PessoaFisica pesquisaObjetoPessoaFisicaPorId(Long id) {
        PessoaFisica pessoaFisica = new PessoaFisica();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + pessoaFisica.getClass().getCanonicalName()
                + " as p where p.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        pessoaFisica = (PessoaFisica) query.uniqueResult();
        session.close();
        return pessoaFisica;
    }

    public PessoaFisica pesquisaObjetoPessoaFisicaPorCPF(String cpf) {
        PessoaFisica pessoaFisica = new PessoaFisica();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + pessoaFisica.getClass().getCanonicalName()
                + " as p where p.cpf = :cpf";
        Query query = session.createQuery(hql);
        query.setParameter("cpf", cpf);
        pessoaFisica = (PessoaFisica) query.uniqueResult();
        session.close();
        return pessoaFisica;
    }

    public Long pesquisaIdDaPessoaFisicaPorNome(String nome){
        PessoaFisica pessoaFisica = new PessoaFisica();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + pessoaFisica.getClass().getCanonicalName()
                + " as p where p.nome = :nome";
        Query query = session.createQuery(hql);
        query.setParameter("nome", nome);
        pessoaFisica = (PessoaFisica) query.uniqueResult();
        Long idPessoaFisica = pessoaFisica.getId();
        session.close();
        return idPessoaFisica;
    }


    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
