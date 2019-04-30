package com.mycompany.control;

import com.mycompany.DAO.DaoColaborador;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Colaborador;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class Validador {

    @SpringBean(name = "colaboradorDao")
    private DaoColaborador colaboradorDao;
    @SpringBean(name = "genericDao")
    private GenericDao<Colaborador> genericDao;

    public boolean validaTamanhoCpf(String cpf){

        boolean tamanhoCpfValidado;

        if(cpf.length() != 14){
            tamanhoCpfValidado = false;
        }else{
            tamanhoCpfValidado = true;
        }
        return tamanhoCpfValidado;
    }

    public boolean verificaSeCPFUnicoParaInserir(Colaborador colaborador) {
        Boolean cpfUnico = true;
        int verificador = 0;
            for (Colaborador colaboradorDaLista : colaboradorDao.pesquisarListaDeColaboradoresExistentes()) {
                if (colaboradorDaLista.getCpf().equals(colaborador.getCpf())) {
                    verificador++;
                }
            }
        if (verificador > 0) {
            cpfUnico = false;
        }
        return cpfUnico;
    }

    public boolean verificaSeCPFUnicoParaUpdate(Colaborador colaborador) {
        Boolean cpfUnico = true;
        int verificador = 0;
            for (Colaborador colaboradorDaLista : colaboradorDao.pesquisarListaDeColaboradoresExistentes()) {
                if (colaboradorDaLista.getCpf().equals(colaborador.getCpf())
                        &&colaboradorDaLista.getId()!=colaborador.getId()) {
                    verificador++;
                }
            }
        if (verificador > 0) {
            cpfUnico = false;
        }
        return cpfUnico;
    }

    public boolean verificaSeRGUnicoParaInserir(Colaborador colaborador) {
        Boolean rgUnico = true;
        int verificador = 0;
            for (Colaborador colaboradorDaLista : colaboradorDao.pesquisarListaDeColaboradoresExistentes()) {
                if (colaboradorDaLista.getRg().equals(colaborador.getRg())) {
                    verificador++;
                }
            }
        if (verificador > 0) {
            rgUnico = false;
        }
        return rgUnico;
    }

    public boolean verificaSeRGUnicoParaUpdate(Colaborador colaborador) {
        Boolean rgUnico = true;
        int verificador = 0;
            for (Colaborador colaboradorDaLista : colaboradorDao.pesquisarListaDeColaboradoresExistentes()) {
                if (colaboradorDaLista.getRg().equals(colaborador.getRg())
                        && colaboradorDaLista.getId()!=colaborador.getId()) {
                    verificador++;
                }
            }
        if (verificador > 0) {
            rgUnico = false;
        }
        return rgUnico;
    }

    public void setColaboradorDao(DaoColaborador colaboradorDao) {
        this.colaboradorDao = colaboradorDao;
    }

    public void setGenericDao(GenericDao<Colaborador> genericDao) {
        this.genericDao = genericDao;
    }
}
