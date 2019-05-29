package com.mycompany.control;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.DAO.DaoPF;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Conta;
import com.mycompany.model.PessoaFisica;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;


public class ServicePF {
    @SpringBean(name = "pfDao")
    private DaoPF pfDao;
    @SpringBean(name = "genericDao")
    private GenericDao<PessoaFisica> genericDao;
    @SpringBean(name = "contaService")
    private ServiceConta serviceConta;

    public Mensagem inserir(PessoaFisica pessoaFisica) {
        Mensagem mensagem = new Mensagem();
        Boolean pfNull = verificaSePessoaFisicaNullParaInserir(pessoaFisica);
            if (pfNull) {
                if (verificaSeCPFUnicoParaInserir(pessoaFisica)) {
                    genericDao.inserir(pessoaFisica);
                } else {
                    mensagem.adcionarMensagemNaLista("Cpf já existente!");
                }
            } else {
                mensagem.adcionarMensagemNaLista("Pessoa Física já existente!");
            }
        return mensagem;
    }

    public Mensagem update(PessoaFisica pessoaFisica) {
        Mensagem mensagem = new Mensagem();
        Boolean pfNull = verificaSePessoaFisicaNullParaUpdate(pessoaFisica);
            if (!pfNull) {
                if (verificaSeCPFUnicoParaUpdate(pessoaFisica)) {
                    genericDao.inserir(pessoaFisica);
                } else {
                    mensagem.adcionarMensagemNaLista("Cpf já existente!");
                }
            } else {
                mensagem.adcionarMensagemNaLista("Pessoa Física já existente!");
            }
        return mensagem;
    }

    public void executarAoClicarEmSalvarNaModalSalvar(
            List<PessoaFisica> listaDePessoasFisicas, PessoaFisica pessoaFisica,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {
        Mensagem mensagem = inserir(pessoaFisica);
        if (mensagem.getListaVazia()) {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(listarPessoasFisicas(pessoaFisica));
            modalWindow.close(target);
            target.add(rowPanel);
        } else {
            int index = 0;
            for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }
    }

    public void executarAoClicarEmSalvarNaModalEditar(
            List<PessoaFisica> listaDePessoasFisicas, PessoaFisica pessoaFisica,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        PessoaFisica pfExistente = pesquisaObjetoPessoaFisicaPorId(pessoaFisica.getId());
        Mensagem mensagem = update(pessoaFisica);

        if (mensagem.getListaVazia()) {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(listarPessoasFisicas(pessoaFisica));
            modalWindow.close(target);
            target.add(rowPanel);
        } else {
            int index = 0;
            for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }

    }


    public PessoaFisica pesquisaObjetoPessoaFisicaPorNome(String nome) {
        return pfDao.pesquisaObjetoPessoaFisicaPorNome(nome);
    }

    public PessoaFisica pesquisaObjetoPessoaFisicaPorId(Long id) {
        return pfDao.pesquisaObjetoPessoaFisicaPorId(id);
    }

    public PessoaFisica pesquisaObjetoPessoaFisicaPorCPF(String cpf) {
        return pesquisaObjetoPessoaFisicaPorCPF(cpf);
    }

    public List<PessoaFisica> pesquisarListaDePessoasFisicasPorString(PessoaFisica pessoaFisica, String colum, String string) {
        return genericDao.pesquisaListadeObjetosPorString(pessoaFisica, colum, string);
    }

    public List<PessoaFisica> pesquisarListaDePessoasFisicasPorStringEmDuasColunas(PessoaFisica pessoaFisica, String colum1, String colum2, String string1, String string2) {
        return genericDao.pesquisarListaDeObjetosPorStringEmDuasColunas(pessoaFisica, colum1, colum2, string1, string2);
    }

    public List<PessoaFisica> listarPessoasFisicas(PessoaFisica pessoaFisica) {
        return genericDao.pesquisarListaDeObjeto(pessoaFisica);
    }

    public Long pesquisarIdPessoaFisicaPorNome(String nome) {
        return pfDao.pesquisaIdDaPessoaFisicaPorNome(nome);
    }

    public void executarAoClicarEmExcluir(PessoaFisica pessoaFisica, AjaxRequestTarget target,
                                          ModalWindow modalWindow, FeedbackPanel feedbackPanel){
        Mensagem mensagem = deletarPessoaFisica(pessoaFisica,modalWindow,target);
        if(!mensagem.getListaVazia()){
            for(String mensagemDaLista: mensagem.getListaDeMensagens()){
                feedbackPanel.error(mensagemDaLista);
                target.add(feedbackPanel);
            }
        }
    }

    public Mensagem deletarPessoaFisica(PessoaFisica pessoaFisica, ModalWindow modalWindow, AjaxRequestTarget target) {
        Mensagem mensagem = new Mensagem();
        Boolean deletaPessoafisica = validaPessoaFisicaParaDeletar(pessoaFisica);
        if(deletaPessoafisica) {
            genericDao.deletar(pessoaFisica);
            modalWindow.close(target);
        }else{
            mensagem.adcionarMensagemNaLista("Pessoa física em uso!");
        }
        return mensagem;
    }

    public Boolean validaPessoaFisicaParaDeletar(PessoaFisica pessoaFisica) {
        Boolean deletarPessoaFisica = true;
        Conta conta = new Conta();
        List<Conta> listaDeContas = serviceConta.pesquisarListaDeContas(conta);
        for (Conta contaDaLista : listaDeContas) {
            if (contaDaLista.getPessoaFisica() != null) {
                if (contaDaLista.getPessoaFisica().getId().equals(pessoaFisica.getId())) {
                    deletarPessoaFisica = false;
                }
            }
        }
        return deletarPessoaFisica;
    }

    public void filtrarPessoaFisicaNaVisao(String nome, String cpf, List<PessoaFisica> listaDePessoasFisicas, PessoaFisica pessoaFisica, AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!nome.isEmpty() && cpf.isEmpty()) {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(pesquisarListaDePessoasFisicasPorString(pessoaFisica, "nome", nome));
            target.add(rowPanel);

        } else if (nome.isEmpty() && !cpf.isEmpty()) {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(pesquisarListaDePessoasFisicasPorString(pessoaFisica, "cpf", cpf));
            target.add(rowPanel);

        } else if (!nome.isEmpty() && !cpf.isEmpty()) {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(pesquisarListaDePessoasFisicasPorStringEmDuasColunas(pessoaFisica, "nome",
                    "cpf", nome, cpf));
            target.add(rowPanel);
        } else {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(listarPessoasFisicas(pessoaFisica));
        }
        target.add(rowPanel);
    }

    public boolean verificaSePessoaFisicaNullParaInserir(PessoaFisica pessoaFisica) {
        Boolean pessoaFisicaNull = true;
        PessoaFisica pessoaFisicaParaVerificar = pfDao.pesquisaObjetoPessoaFisicaPorNome(pessoaFisica.getNome());
        if (pessoaFisicaParaVerificar == null) {
            pessoaFisicaNull = true;
        } else {
            pessoaFisicaNull = false;
        }
        return pessoaFisicaNull;
    }

    public boolean verificaSePessoaFisicaNullParaUpdate(PessoaFisica pessoaFisica) {
        Boolean pessoaFisicaNull = true;
        PessoaFisica pessoaFisicaParaVerificar = pfDao.pesquisaObjetoPessoaFisicaPorId(pessoaFisica.getId());
        if (pessoaFisicaParaVerificar == null) {
            pessoaFisicaNull = true;
        } else {
            pessoaFisicaNull = false;
        }
        return pessoaFisicaNull;
    }

    public boolean verificaSeCPFUnicoParaInserir(PessoaFisica pessoaFisica) {
        Boolean cpfUnico = true;
        int verificador = 0;
        for (PessoaFisica pfDaLista : listarPessoasFisicas(pessoaFisica)) {
            if (pfDaLista.getCpf().equals(pessoaFisica.getCpf())) {
                verificador++;
            }
        }
        if (verificador > 0) {
            cpfUnico = false;
        }
        return cpfUnico;
    }


    public boolean verificaSeCPFUnicoParaUpdate(PessoaFisica pessoaFisica) {
        Boolean cpfUnico = true;
        int verificador = 0;
        for (PessoaFisica pfDaLista : listarPessoasFisicas(pessoaFisica)) {
            if (pessoaFisica.getId().toString().equals(pfDaLista.getId().toString())) {
            } else {
                if (pfDaLista.getCpf().equals(pessoaFisica.getCpf())) {
                    verificador++;
                }
            }
        }
        if (verificador > 0) {
            cpfUnico = false;
        }
        return cpfUnico;
    }



    public void setPfDao(DaoPF pfDao) {
        this.pfDao = pfDao;
    }

    public void setGenericDao(GenericDao<PessoaFisica> genericDao) {
        this.genericDao = genericDao;
    }

    public void setServiceConta(ServiceConta serviceConta) {
        this.serviceConta = serviceConta;
    }
}
