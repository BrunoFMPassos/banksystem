package com.mycompany.control;

import com.mycompany.DAO.DaoTipoDeConta;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Conta;
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
    @SpringBean(name = "contaService")
    private ServiceConta serviceConta;
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

    public List<TipoDeConta> listarTiposDeConta(TipoDeConta tipoDeConta){
        return genericDao.pesquisarListaDeObjeto(tipoDeConta);
    }

    public void executarAoClicarEmExcluir(TipoDeConta tipoDeConta, AjaxRequestTarget target,
                                          ModalWindow modalWindow, FeedbackPanel feedbackPanel){
        Mensagem mensagem = deletarTipoDeConta(tipoDeConta,modalWindow,target);
        if(!mensagem.getListaVazia()){
            for(String mensagemDaLista: mensagem.getListaDeMensagens()){
                feedbackPanel.error(mensagemDaLista);
                target.add(feedbackPanel);
            }
        }
    }

    public Mensagem deletarTipoDeConta(TipoDeConta tipoDeConta, ModalWindow modalWindow, AjaxRequestTarget target){
        Mensagem mensagem = new Mensagem();
        Boolean deletaTipoConta = validaTipoDeContaParaDeletar(tipoDeConta);
        if(deletaTipoConta) {
            genericDao.deletar(tipoDeConta);
            modalWindow.close(target);
        }else{
            mensagem.adcionarMensagemNaLista("Tipo de conta em uso!");
        }
        return mensagem;
    }

    public Boolean validaTipoDeContaParaDeletar(TipoDeConta tipoDeConta){
        Boolean deletaTipoDeConta = true;
        Conta conta = new Conta();
        List<Conta> listaDeContas = serviceConta.pesquisarListaDeContas(conta);
        for(Conta contaDaLista:listaDeContas){
            if(contaDaLista.getTipoDeConta().getId().equals(tipoDeConta.getId())){
                deletaTipoDeConta = false;
            }
        }
        return deletaTipoDeConta;
    }

    public void filtrarTipoDeContaNaVisao(String descricao, List<TipoDeConta> listaDeTiposDeConta, TipoDeConta tipoDeConta,
                                           AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!descricao.isEmpty()) {
            listaDeTiposDeConta.clear();
            listaDeTiposDeConta.addAll(genericDao.pesquisaListadeObjetosPorString(tipoDeConta,"descricao", descricao));
            target.add(rowPanel);

        } else {
            listaDeTiposDeConta.clear();
            listaDeTiposDeConta.addAll(genericDao.pesquisarListaDeObjeto(tipoDeConta));
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
        if (tipoDeConta.getBaselimite() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo base limite é obrigatório");
        }
        if (tipoDeConta.getPessoa() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo tipo de pessoa é obrigatório");
        }
        if(tipoDeConta.getTaxaDeTransferencia()==null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo taxa de transferência é obrigatório");
        }
        return informacoesObrigatoriasPreenchidasValidas;
    }

    public void setDaoTipoDeConta(DaoTipoDeConta daoTipoDeConta) {
        this.daoTipoDeConta = daoTipoDeConta;
    }


    public void setGenericDao(GenericDao<TipoDeConta> genericDao) {
        this.genericDao = genericDao;
    }

    public void setServiceConta(ServiceConta serviceConta) {
        this.serviceConta = serviceConta;
    }
}
