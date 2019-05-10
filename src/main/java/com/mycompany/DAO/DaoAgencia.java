package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.Agencia;
import com.mycompany.model.Colaborador;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class DaoAgencia extends GenericDAOImpl<Agencia, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    public Agencia pesquisaObjetoAgenciaPorNumero(String numero) {
        Agencia agencia = new Agencia();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + agencia.getClass().getCanonicalName()
                + " as a where a.numero = :numero";
        Query query = session.createQuery(hql);
        query.setParameter("numero", numero);
        agencia = (Agencia) query.uniqueResult();
        session.close();
        return agencia;
    }

    public Agencia pesquisaObjetoAgenciaPorId(Long id) {
        Agencia agencia = new Agencia();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + agencia.getClass().getCanonicalName()
                + " as a where a.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        agencia = (Agencia) query.uniqueResult();
        session.close();
        return agencia;
    }

    public Boolean verificarSeAgenciaEstaEmUsoEmColaboradores(Agencia agencia){
        Boolean agenciaEmUso = false;
        Colaborador colaborador = new Colaborador();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + colaborador.getClass().getCanonicalName()
                + " as c where c.agencia = :agencia";
        Query query = session.createQuery(hql);
        query.setParameter("agencia", agencia);
        List<Agencia> agencias = query.list();
        session.close();
        if(!agencias.isEmpty()){
            agenciaEmUso = true;
        }
        return agenciaEmUso;
    }


    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
