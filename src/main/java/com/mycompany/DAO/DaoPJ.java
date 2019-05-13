package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.PessoaJuridica;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;

public class DaoPJ extends GenericDAOImpl<PessoaJuridica, Long> implements Serializable {
    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;


    public PessoaJuridica pesquisaObjetoPessoaJuridicaPorRazaoSocial(String razaoSocial) {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + pessoaJuridica.getClass().getCanonicalName()
                + " as p where p.razaoSocial = :razaoSocial";
        Query query = session.createQuery(hql);
        query.setParameter("razaoSocial", razaoSocial);
        pessoaJuridica = (PessoaJuridica) query.uniqueResult();
        session.close();
        return pessoaJuridica;
    }

    public PessoaJuridica pesquisaObjetoPessoaJuridicaPorId(Long id) {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + pessoaJuridica.getClass().getCanonicalName()
                + " as p where p.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        pessoaJuridica = (PessoaJuridica) query.uniqueResult();
        session.close();
        return pessoaJuridica;
    }

    public PessoaJuridica pesquisaObjetoPessoaJuridicaPorCnpj(String cnpj) {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + pessoaJuridica.getClass().getCanonicalName()
                + " as p where p.cnpj = :cnpj";
        Query query = session.createQuery(hql);
        query.setParameter("cnpj", cnpj);
        pessoaJuridica = (PessoaJuridica) query.uniqueResult();
        session.close();
        return pessoaJuridica;
    }

    public Long pesquisaIdDaPessoaJuridicaPorRazaoSocial(String razaoSocial){
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + pessoaJuridica.getClass().getCanonicalName()
                + " as p where p.razaoSocial = :razaoSocial";
        Query query = session.createQuery(hql);
        query.setParameter("razaoSocial", razaoSocial);
        pessoaJuridica = (PessoaJuridica) query.uniqueResult();
        Long idPessoaJuridica= pessoaJuridica.getId();
        session.close();
        return idPessoaJuridica;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
