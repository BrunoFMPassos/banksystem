package com.mycompany.control;

import com.mycompany.DAO.DaoAgencia;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Agencia;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceAgencia {
    @SpringBean(name = "genericDao")
    private GenericDao<Agencia> genericDao;

    @SpringBean(name = "agenciaDao")
    private DaoAgencia agenciaDao;


    public Mensagem inserir(Agencia agencia) {
        Mensagem mensagem = new Mensagem();
        Boolean agenciaNull = verificaSeAgenciaNullParaInserir(agencia);
        if(agenciaNull) {
            boolean informacoesObrigatoriasPreenchidas = verificaSeInformacoesObrigatoriasPreenchidas(agencia,mensagem);
            if(informacoesObrigatoriasPreenchidas) {
                genericDao.inserir(agencia);
            }
        }else{
            mensagem.adcionarMensagemNaLista("Agência já Existente!");
        }
        return mensagem;
    }

    public Mensagem update(Agencia agencia) {
        Mensagem mensagem = new Mensagem();
        Boolean agenciaAptaAEditar = verificaSeAgenciaAptaParaUpdate(agencia);
        if(agenciaAptaAEditar) {
            boolean informacoesObrigatoriasPreenchidas = verificaSeInformacoesObrigatoriasPreenchidas(agencia,mensagem);
            if(informacoesObrigatoriasPreenchidas) {
                genericDao.inserir(agencia);
            }
        }else{
            mensagem.adcionarMensagemNaLista("Informações já pertencentes a outra agência!");
        }
        return mensagem;
    }


    public List<Agencia> pesquisarListaDeAgenciasPorAgencia(Agencia agencia){
        return genericDao.pesquisarListaDeObjeto(agencia);
    }

    public List<Agencia> pesquisarListaDeAgenciasPorNumero(Agencia agencia){
        return genericDao.pesquisaListadeObjetosPorString(agencia,"numero",agencia.getNumero());
    }

    public Agencia pesquisaObjetoAgenciaPorNumero(String numero){
        Agencia agencia = agenciaDao.pesquisaObjetoAgenciaPorNumero(numero);
        return agencia;
    }



    public void executarAoClicarEmSalvarNaModalSalvar(
            List<Agencia> listaDeAgencias, Agencia agencia,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = inserir(agencia);
        if(mensagem.getListaVazia()) {
            listaDeAgencias.clear();
            listaDeAgencias.addAll(pesquisarListaDeAgenciasPorAgencia(agencia));
            modalWindow.close(target);
            target.add(rowPanel);
        }else{
            int  index = 0;
            for(String mensagemDaLista: mensagem.getListaDeMensagens()){
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }

    }

    public void executarAoClicarEmSalvarNaModalEditar(
            List<Agencia> listaDeAgencias, Agencia agencia,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {


        Mensagem mensagem =update(agencia);
        if(mensagem.getListaVazia()) {
            listaDeAgencias.clear();
            listaDeAgencias.addAll(pesquisarListaDeAgenciasPorAgencia(agencia));
            modalWindow.close(target);
            target.add(rowPanel);
        }else{
            int  index = 0;
            for(String mensagemDaLista: mensagem.getListaDeMensagens()){
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }

    }



    public boolean verificaSeAgenciaNullParaInserir(Agencia agencia) {
        Boolean agenciaNull = true;
        Agencia agenciaParaVerificar = agenciaDao.pesquisaObjetoAgenciaPorNumero(agencia.getNumero());
        if (agenciaParaVerificar == null) {
            agenciaNull = true;
        } else {
            agenciaNull = false;
        }
        return agenciaNull;
    }

    public boolean verificaSeAgenciaAptaParaUpdate(Agencia agencia) {
        Boolean agenciaAptaAEditar = true;
        Agencia agenciaparaVerificarId = agenciaDao.pesquisaObjetoAgenciaPorId(agencia.getId());
        Agencia agenciaParaVerificarNumero = agenciaDao.pesquisaObjetoAgenciaPorNumero(agencia.getNumero());
        if (agenciaparaVerificarId != null && agenciaParaVerificarNumero == null) {
            agenciaAptaAEditar = true;
        } else {
            agenciaAptaAEditar = false;
        }
        return agenciaAptaAEditar;
    }

    public boolean verificaSeInformacoesObrigatoriasPreenchidas(Agencia agencia, Mensagem mensagem) {

        Boolean informacoesObrigatoriasPreenchidas = true;

        if (agencia.getUF() == null) {
            informacoesObrigatoriasPreenchidas = false;
            mensagem.adcionarMensagemNaLista("O campo UF é obrigatório!");
        }

        return informacoesObrigatoriasPreenchidas;
    }


    public void filtrarAgenciaNaVisao(String numero, List<Agencia> listaDeAgencias,Agencia agencia,
                                      AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!numero.isEmpty()) {
            listaDeAgencias.clear();
            listaDeAgencias.addAll(pesquisarListaDeAgenciasPorNumero(agencia));
            target.add(rowPanel);

        } else {
            listaDeAgencias.clear();
            listaDeAgencias.addAll(pesquisarListaDeAgenciasPorAgencia(agencia));
        }
        target.add(rowPanel);
    }

    public Mensagem deletarAgencia(Agencia agencia) {
        Mensagem mensagem = new Mensagem();
        Boolean agenciaEmUso = agenciaDao.verificarSeAgenciaEstaemUso(agencia);
        if(!agenciaEmUso) {
            genericDao.deletar(agencia);
        }else{
            mensagem.adcionarMensagemNaLista("A Agência está em uso!");
        }
        return mensagem;
    }


    public void executarAoClicarEmSimNaModalExcluir(
            List<Agencia> listaDeAgencias, Agencia agencia,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = deletarAgencia(agencia);
        if(mensagem.getListaVazia()) {
            listaDeAgencias.clear();
            listaDeAgencias.addAll(pesquisarListaDeAgenciasPorAgencia(agencia));
            modalWindow.close(target);
            target.add(rowPanel);
        }else{
            int  index = 0;
            for(String mensagemDaLista: mensagem.getListaDeMensagens()){
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }

    }


    public void setGenericDao(GenericDao<Agencia> genericDao) {
        this.genericDao = genericDao;
    }

    public void setAgenciaDao(DaoAgencia agenciaDao) {
        this.agenciaDao = agenciaDao;
    }
}
