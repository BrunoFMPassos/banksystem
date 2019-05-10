package com.mycompany.control;

import com.mycompany.DAO.DaoTipoDeCartao;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.TipoDeCartao;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceTipoDeCartao {
    @SpringBean(name = "genericDao")
    private GenericDao<TipoDeCartao> genericDao;
    @SpringBean(name = "tipoDeCartaoDao")
    private DaoTipoDeCartao daoTipoDeCartao;


    public Mensagem inserir(TipoDeCartao tipoDeCartao) {

        Mensagem mensagem = new Mensagem();
        Boolean tipoDeContaNull = verificaSeTipoDeCartaoNullParaInserir(tipoDeCartao);
        Boolean informacoesObrigatoriasPreenchidasValidas = VerificaSeInformacoesObrigatoriasPreenchidasValidas(tipoDeCartao,mensagem);
        if(informacoesObrigatoriasPreenchidasValidas){
            if (tipoDeContaNull) {
                genericDao.inserir(tipoDeCartao);
            }
            else {
                mensagem.adcionarMensagemNaLista("Tipo de Conta já existente!");
            }
        }
        return mensagem;
    }

    public Mensagem update(TipoDeCartao tipoDeCartao) {
        Mensagem mensagem = new Mensagem();
        Boolean tipoDeContaNull = verificaSeTipodeCartaoNullParaUpdate(tipoDeCartao);
        Boolean informacoesObrigatoriasPreenchidasValidas = VerificaSeInformacoesObrigatoriasPreenchidasValidas(tipoDeCartao,mensagem);
        if(informacoesObrigatoriasPreenchidasValidas){
            if (!tipoDeContaNull) {
                genericDao.inserir(tipoDeCartao);
            } else {
                mensagem.adcionarMensagemNaLista("Tipo de conta já existente!");
            }
        }
        return mensagem;
    }

    public void executarAoClicarEmSalvarNaModalSalvar(
            List<TipoDeCartao> listaDeTiposDeCartao, TipoDeCartao tipoDeCartao,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = inserir(tipoDeCartao);
        if(mensagem.getListaVazia()) {
            listaDeTiposDeCartao.clear();
            listaDeTiposDeCartao.addAll(genericDao.pesquisarListaDeObjeto(tipoDeCartao));
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
            List<TipoDeCartao> listaDeTiposDeCartao, TipoDeCartao tipoDeCartao,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        TipoDeCartao tipoDeCartaoExistente = pesquisarObjetoTipoDeCartaoPorId(tipoDeCartao.getId());
        Mensagem mensagem = update(tipoDeCartao);

        if(mensagem.getListaVazia()) {
            listaDeTiposDeCartao.clear();
            listaDeTiposDeCartao.addAll(genericDao.pesquisarListaDeObjeto(tipoDeCartao));
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


    public TipoDeCartao pesquisarObjetoTipoDeCartaoPorDescricao(String descricao){
        return daoTipoDeCartao.pesquisaObjetoTipoDeCartaoPorDescricao(descricao);
    }

    public TipoDeCartao pesquisarObjetoTipoDeCartaoPorId(Long id){
        return daoTipoDeCartao.pesquisaObjetoTipoDeCartaoPorId(id);
    }

    public List<TipoDeCartao> listarTiposDeCartao(TipoDeCartao tipoDeCartao){
        return genericDao.pesquisarListaDeObjeto(tipoDeCartao);
    }

    public void deletarTipoDeCartao(TipoDeCartao tipoDeCartao){
        genericDao.deletar(tipoDeCartao);
    }

    public void filtrarTipoDeCartaoNaVisao(String descricao, List<TipoDeCartao> listaDeTiposDeCartao, TipoDeCartao tipoDeCartao,
                                          AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!descricao.isEmpty()) {
            listaDeTiposDeCartao.clear();
            listaDeTiposDeCartao.addAll(genericDao.pesquisaListadeObjetosPorString(tipoDeCartao,"descricao", descricao));
            target.add(rowPanel);

        } else {
            listaDeTiposDeCartao.clear();
            listaDeTiposDeCartao.addAll(genericDao.pesquisarListaDeObjeto(tipoDeCartao));
        }
        target.add(rowPanel);
    }

    public boolean verificaSeTipoDeCartaoNullParaInserir(TipoDeCartao tipoDeCartao) {
        Boolean tipoDeCartaoNull = true;
        TipoDeCartao tipoDeCartaoParaVerificar = daoTipoDeCartao.pesquisaObjetoTipoDeCartaoPorDescricao(tipoDeCartao.getDescricao());
        if (tipoDeCartaoParaVerificar == null) {
            tipoDeCartaoNull = true;
        } else {
            tipoDeCartaoNull = false;
        }
        return tipoDeCartaoNull;
    }

    public boolean verificaSeTipodeCartaoNullParaUpdate(TipoDeCartao tipoDeCartao) {
        Boolean tipoDeCartaoNull = true;
        TipoDeCartao tipoDeCartaoParaVerificar = daoTipoDeCartao.pesquisaObjetoTipoDeCartaoPorId(tipoDeCartao.getId());
        if (tipoDeCartaoParaVerificar == null) {
            tipoDeCartaoNull = true;
        } else {
            tipoDeCartaoNull = false;
        }
        return tipoDeCartaoNull;
    }


    public boolean VerificaSeInformacoesObrigatoriasPreenchidasValidas(TipoDeCartao tipoDeCartao, Mensagem mensagem){

        Boolean informacoesObrigatoriasPreenchidasValidas = true;

        if (tipoDeCartao.getDescricao() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo descricao é obrigatório!");
        }
        if (tipoDeCartao.getTarifa()==null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo tarifa é obrigatório");
        }
        if (tipoDeCartao.getBaselimite() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo base limite é obrigatório");
        }
        if (tipoDeCartao.getPessoa() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo tipo de pessoa é obrigatório");
        }
        return informacoesObrigatoriasPreenchidasValidas;
    }

    public void setGenericDao(GenericDao<TipoDeCartao> genericDao) {
        this.genericDao = genericDao;
    }

    public void setDaoTipoDeCartao(DaoTipoDeCartao daoTipoDeCartao) {
        this.daoTipoDeCartao = daoTipoDeCartao;
    }
}
