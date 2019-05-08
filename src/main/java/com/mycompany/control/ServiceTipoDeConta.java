package com.mycompany.control;

import com.mycompany.DAO.DaoTipoDeConta;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.TipoDeConta;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceTipoDeConta {
    @SpringBean(name = "genericDao")
    private GenericDao<TipoDeConta> genericDao;
    @SpringBean(name = "tipoDeContaDao")
    private DaoTipoDeConta daoTipoDeConta;


    public Mensagem inserir(TipoDeConta tipoDeConta) {

        Mensagem mensagem = new Mensagem();
        Boolean tipoDeContaNull = verificaSeTipoDeContaNullParaInserir(tipoDeConta);
        Boolean informacoesObrigatoriasPreenchidasValidas = VerificaSeInformacoesObrigatoriasPreenchidasValidas(tipoDeConta,mensagem);
        if(informacoesObrigatoriasPreenchidasValidas){
            if (tipoDeContaNull) {
                    genericDao.inserir(tipoDeConta);
                }
            else {
                mensagem.adcionarMensagemNaLista("Tipo de Conta já existente!");
            }
        }
        return mensagem;
    }

    public Mensagem update(TipoDeConta tipoDeConta) {
        Mensagem mensagem = new Mensagem();
        Boolean tipoDeContaNull = verificaSeTipodeContaNullParaUpdate(tipoDeConta);
        Boolean informacoesObrigatoriasPreenchidasValidas = VerificaSeInformacoesObrigatoriasPreenchidasValidas(tipoDeConta,mensagem);
        if(informacoesObrigatoriasPreenchidasValidas){
            if (!tipoDeContaNull) {

                    genericDao.inserir(tipoDeConta);
                } else {
                mensagem.adcionarMensagemNaLista("Tipo de conta já existente!");
            }
        }
        return mensagem;
    }

    public void executarAoClicarEmSalvarNaModalSalvar(
            List<TipoDeConta> listaDeTiposDeConta, TipoDeConta tipoDeConta,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = inserir(tipoDeConta);
        if(mensagem.getListaVazia()) {
            listaDeTiposDeConta.clear();
            listaDeTiposDeConta.addAll(genericDao.pesquisarListaDeObjeto(tipoDeConta));
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
            List<TipoDeConta> listaDeTiposDeConta, TipoDeConta tipoDeConta,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        TipoDeConta tipoDeContaExistente = pesquisarObjetoTipoDeContaPorId(tipoDeConta.getId());
        Mensagem mensagem = update(tipoDeConta);

        if(mensagem.getListaVazia()) {
            listaDeTiposDeConta.clear();
            listaDeTiposDeConta.addAll(genericDao.pesquisarListaDeObjeto(tipoDeConta));
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


    public TipoDeConta pesquisarObjetoTipoDeContaPorDescricao(String descricao){
        return daoTipoDeConta.pesquisaObjetoTipoDeContaPorDescricao(descricao);
    }

    public TipoDeConta pesquisarObjetoTipoDeContaPorId(Long id){
        return daoTipoDeConta.pesquisaObjetoTipoDeContaPorId(id);
    }

    public void filtrarTipoDeContaNaVisao(String descricao, List<TipoDeConta> listaDePessoasFisicas, TipoDeConta tipoDeConta,
                                           AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!descricao.isEmpty()) {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(genericDao.pesquisaListadeObjetosPorString(tipoDeConta,"descricao", descricao));
            target.add(rowPanel);

        } else {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(genericDao.pesquisarListaDeObjeto(tipoDeConta));
        }
        target.add(rowPanel);
    }

    public boolean verificaSeTipoDeContaNullParaInserir(TipoDeConta tipoDeConta) {
        Boolean tipoDeContaNull = true;
        TipoDeConta tipoDeContaParaVerificar = daoTipoDeConta.pesquisaObjetoTipoDeContaPorDescricao(tipoDeConta.getDescricao());
        if (tipoDeContaParaVerificar == null) {
            tipoDeContaNull = true;
        } else {
            tipoDeContaNull = false;
        }
        return tipoDeContaNull;
    }

    public boolean verificaSeTipodeContaNullParaUpdate(TipoDeConta tipoDeConta) {
        Boolean tipoDeContaNull = true;
        TipoDeConta tipoDeContaParaVerificar = daoTipoDeConta.pesquisaObjetoTipoDeContaPorId(tipoDeConta.getId());
        if (tipoDeContaParaVerificar == null) {
            tipoDeContaNull = true;
        } else {
            tipoDeContaNull = false;
        }
        return tipoDeContaNull;
    }


    public boolean VerificaSeInformacoesObrigatoriasPreenchidasValidas(TipoDeConta tipoDeConta, Mensagem mensagem){

        Boolean informacoesObrigatoriasPreenchidasValidas = true;

        if (tipoDeConta.getDescricao() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo descricao é obrigatório!");
        }
        if (tipoDeConta.getTarifa()==null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo tarifa é obrigatório");
        }
        if (tipoDeConta.getBaseLimite() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo base limite é");
        }

        return informacoesObrigatoriasPreenchidasValidas;
    }

    public void setDaoTipoDeConta(DaoTipoDeConta daoTipoDeConta) {
        this.daoTipoDeConta = daoTipoDeConta;
    }


    public void setGenericDao(GenericDao<TipoDeConta> genericDao) {
        this.genericDao = genericDao;
    }
}
