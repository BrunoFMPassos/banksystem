package com.mycompany.control;

import com.mycompany.DAO.DaoPJ;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Conta;
import com.mycompany.model.PessoaJuridica;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServicePJ {
    @SpringBean(name = "pjDao")
    private DaoPJ pjDao;
    @SpringBean(name = "genericDao")
    private GenericDao<PessoaJuridica> genericDao;
    @SpringBean(name = "contaService")
    private ServiceConta serviceConta;



    public Mensagem inserir(PessoaJuridica pessoaJuridica) {

        Mensagem mensagem = new Mensagem();
        Boolean pjNull = verificaSePessoaJuridicaNullParaInserir(pessoaJuridica);
            if (pjNull) {
                if (verificaSeCnpjUnicoParaInserir(pessoaJuridica)){
                    genericDao.inserir(pessoaJuridica);
                }else{
                    mensagem.adcionarMensagemNaLista("Cnpj já existente!");
                }
            }else {
                mensagem.adcionarMensagemNaLista("Pessoa Jurídica já existente!");
            }
        return mensagem;
    }

    public Mensagem update(PessoaJuridica pessoaJuridica) {

        Mensagem mensagem = new Mensagem();
        Boolean pfNull = verificaSePessoaJuridicaNullParaUpdate(pessoaJuridica);
            if (!pfNull) {
                if (verificaSeCnpjUnicoParaUpdate(pessoaJuridica)){
                    genericDao.inserir(pessoaJuridica);
                }else{
                    mensagem.adcionarMensagemNaLista("Cnpj já existente!");
                }
            }else {
                mensagem.adcionarMensagemNaLista("Pessoa Jurídica já existente!");
            }
        return mensagem;
    }

    public void executarAoClicarEmSalvarNaModalSalvar(
            List<PessoaJuridica> listaDePessoasJuridicas, PessoaJuridica pessoaJuridica,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = inserir(pessoaJuridica);
        if(mensagem.getListaVazia()) {
            listaDePessoasJuridicas.clear();
            listaDePessoasJuridicas.addAll(listarPessoasJuridicas(pessoaJuridica));
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
            List<PessoaJuridica> listaDePessoasJuridicas, PessoaJuridica pessoaJuridica,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        PessoaJuridica pjExistente =pesquisarObjetoPessoaJutidicaPorId(pessoaJuridica.getId());
        Mensagem mensagem = update(pessoaJuridica);

        if(mensagem.getListaVazia()) {
            listaDePessoasJuridicas.clear();
            listaDePessoasJuridicas.addAll(listarPessoasJuridicas(pessoaJuridica));
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


    public PessoaJuridica pesquisaObjetoPessoaJuridicaPorRazaoSocial(String razaoSocial) {
        return pjDao.pesquisaObjetoPessoaJuridicaPorRazaoSocial(razaoSocial);
    }

    public PessoaJuridica pesquisarObjetoPessoaJutidicaPorId(Long id) {
        return pjDao.pesquisaObjetoPessoaJuridicaPorId(id);
    }

    public PessoaJuridica pesquisaObjetoPessoaFisicaPorCnpj(String cnpj) {
        return pjDao.pesquisaObjetoPessoaJuridicaPorCnpj(cnpj);
    }

    public Long pesquisarIdDaPessoaJuridicaPorRazaoSocial(String razaoSocial){
        return pjDao.pesquisaIdDaPessoaJuridicaPorRazaoSocial(razaoSocial);
    }

    public List<PessoaJuridica> pesquisarListaDePessoasJuridicasPorString(PessoaJuridica pessoaJuridica, String colum, String string) {
        return genericDao.pesquisaListadeObjetosPorString(pessoaJuridica, colum, string);
    }

    public List<PessoaJuridica> pesquisarListaDePessoasJuridicasPorStringEmDuasColunas(PessoaJuridica pessoaJuridica, String colum1, String colum2, String string1, String string2) {
        return genericDao.pesquisarListaDeObjetosPorStringEmDuasColunas(pessoaJuridica, colum1, colum2, string1, string2);
    }

    public List<PessoaJuridica> listarPessoasJuridicas(PessoaJuridica pessoaJuridica){
        return genericDao.pesquisarListaDeObjeto(pessoaJuridica);
    }


    public void executarAoClicarEmExcluir(PessoaJuridica pessoaJuridica, AjaxRequestTarget target,
                                          ModalWindow modalWindow, FeedbackPanel feedbackPanel){
        Mensagem mensagem = deletarPessoaJuridica(pessoaJuridica,modalWindow,target);
        if(!mensagem.getListaVazia()){
            for(String mensagemDaLista: mensagem.getListaDeMensagens()){
                feedbackPanel.error(mensagemDaLista);
                target.add(feedbackPanel);
            }
        }
    }

    public Mensagem deletarPessoaJuridica(PessoaJuridica pessoaJuridica, ModalWindow modalWindow, AjaxRequestTarget target) {
        Mensagem mensagem = new Mensagem();
        Boolean deletaPessoaJuridica = validaPessoaJuridicaParaDeletar(pessoaJuridica);
        if(deletaPessoaJuridica) {
            genericDao.deletar(pessoaJuridica);
            modalWindow.close(target);
        }else{
            mensagem.adcionarMensagemNaLista("Pessoa jurídica em uso!");
        }
        return mensagem;
    }

    public Boolean validaPessoaJuridicaParaDeletar(PessoaJuridica pessoaJuridica) {
        Boolean deletarPessoaJuridica = true;
        Conta conta = new Conta();
        List<Conta> listaDeContas = serviceConta.pesquisarListaDeContas(conta);
        for (Conta contaDaLista : listaDeContas) {
            if (contaDaLista.getPessoaJuridica() != null) {
                if (contaDaLista.getPessoaJuridica().getId().equals(pessoaJuridica.getId())) {
                    deletarPessoaJuridica = false;
                }
            }
        }
        return deletarPessoaJuridica;
    }

    public void filtrarPessoaJuridicaNaVisao(String razaoSocial, String cnpj, List<PessoaJuridica> listaDePessoasJuridicas,
                                             PessoaJuridica pessoaJuridica, AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!razaoSocial.isEmpty() && cnpj.isEmpty()) {
            listaDePessoasJuridicas.clear();
            listaDePessoasJuridicas.addAll(pesquisarListaDePessoasJuridicasPorString(pessoaJuridica, "razaoSocial", razaoSocial));
            target.add(rowPanel);

        } else if (razaoSocial.isEmpty() && !cnpj.isEmpty()) {
            listaDePessoasJuridicas.clear();
            listaDePessoasJuridicas.addAll(pesquisarListaDePessoasJuridicasPorString(pessoaJuridica, "cnpj", cnpj));
            target.add(rowPanel);

        } else if (!razaoSocial.isEmpty() && !cnpj.isEmpty()) {
            listaDePessoasJuridicas.clear();
            listaDePessoasJuridicas.addAll(pesquisarListaDePessoasJuridicasPorStringEmDuasColunas(pessoaJuridica, "razaoSocial",
                    "cnpj", razaoSocial, cnpj));
            target.add(rowPanel);
        } else {
            listaDePessoasJuridicas.clear();
            listaDePessoasJuridicas.addAll(listarPessoasJuridicas(pessoaJuridica));
        }
        target.add(rowPanel);
    }

    public boolean verificaSePessoaJuridicaNullParaInserir(PessoaJuridica pessoaJuridica) {
        Boolean pessoaJuridicaNull = true;
        PessoaJuridica pessoaJuridicaParaVerificar = pesquisaObjetoPessoaJuridicaPorRazaoSocial(pessoaJuridica.getRazaoSocial());
        if (pessoaJuridicaParaVerificar == null) {
            pessoaJuridicaNull = true;
        } else {
            pessoaJuridicaNull = false;
        }
        return pessoaJuridicaNull;
    }

    public boolean verificaSePessoaJuridicaNullParaUpdate(PessoaJuridica pessoaJuridica) {
        Boolean pessoaJuridicaNull = true;
        PessoaJuridica pessoaJuridicaParaVerificar = pjDao.pesquisaObjetoPessoaJuridicaPorId(pessoaJuridica.getId());
        if (pessoaJuridicaParaVerificar == null) {
            pessoaJuridicaNull = true;
        } else {
            pessoaJuridicaNull = false;
        }
        return pessoaJuridicaNull;
    }

    public boolean verificaSeCnpjUnicoParaInserir(PessoaJuridica pessoaJuridica) {
        Boolean cnpjUnico = true;
        int verificador = 0;
        for (PessoaJuridica pjDaLista : listarPessoasJuridicas(pessoaJuridica)) {
            if (pjDaLista.getCnpj().equals(pessoaJuridica.getCnpj())) {
                verificador++;
            }
        }
        if (verificador > 0) {
            cnpjUnico = false;
        }
        return cnpjUnico;
    }


    public boolean verificaSeCnpjUnicoParaUpdate(PessoaJuridica pessoaJuridica) {
        Boolean cnpjUnico = true;
        int verificador = 0;
        for (PessoaJuridica pjDaLista : listarPessoasJuridicas(pessoaJuridica)) {
            if (pessoaJuridica.getId().toString().equals(pjDaLista.getId().toString())){
            }else{
                if (pjDaLista.getCnpj().equals(pessoaJuridica.getCnpj())) {
                    verificador++;
                }
            }
        }
        if (verificador > 0) {
            cnpjUnico = false;
        }
        return cnpjUnico;
    }




    public void setPjDao(DaoPJ pjDao) {
        this.pjDao = pjDao;
    }

    public void setGenericDao(GenericDao<PessoaJuridica> genericDao) {
        this.genericDao = genericDao;
    }

    public void setServiceConta(ServiceConta serviceConta) {
        this.serviceConta = serviceConta;
    }
}


