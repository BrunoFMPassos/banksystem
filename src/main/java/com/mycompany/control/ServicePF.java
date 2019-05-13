package com.mycompany.control;

import com.mycompany.DAO.DaoPF;
import com.mycompany.DAO.GenericDao;
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

    public Mensagem inserir(PessoaFisica pessoaFisica) {

        Mensagem mensagem = new Mensagem();
        Boolean pfNull = verificaSePessoaFisicaNullParaInserir(pessoaFisica);
        Boolean informacoesObrigatoriasPreenchidasValidas = VerificaSeInformacoesObrigatoriasPreenchidasValidas(pessoaFisica,mensagem);
        if(informacoesObrigatoriasPreenchidasValidas){
            if (pfNull) {
                    if (verificaSeCPFUnicoParaInserir(pessoaFisica)){
                        genericDao.inserir(pessoaFisica);
                    }else{
                        mensagem.adcionarMensagemNaLista("Cpf já existente!");
                    }
            }else {
                mensagem.adcionarMensagemNaLista("Pessoa Física já existente!");
            }
        }
        return mensagem;
    }

    public Mensagem update(PessoaFisica pessoaFisica) {

        Mensagem mensagem = new Mensagem();
        Boolean pfNull = verificaSePessoaFisicaNullParaUpdate(pessoaFisica);
        Boolean informacoesObrigatoriasPreenchidasValidas = VerificaSeInformacoesObrigatoriasPreenchidasValidas(pessoaFisica,mensagem);
        if(informacoesObrigatoriasPreenchidasValidas){
            if (!pfNull) {
                if (verificaSeCPFUnicoParaUpdate(pessoaFisica)){
                    genericDao.inserir(pessoaFisica);
                }else{
                    mensagem.adcionarMensagemNaLista("Cpf já existente!");
                }
            }else {
                mensagem.adcionarMensagemNaLista("Pessoa Física já existente!");
            }
        }
        return mensagem;
    }

    public void executarAoClicarEmSalvarNaModalSalvar(
            List<PessoaFisica> listaDePessoasFisicas, PessoaFisica pessoaFisica,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = inserir(pessoaFisica);
        if(mensagem.getListaVazia()) {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(listarPessoasFisicas(pessoaFisica));
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
            List<PessoaFisica> listaDePessoasFisicas, PessoaFisica pessoaFisica,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        PessoaFisica pfExistente = pesquisaObjetoPessoaFisicaPorId(pessoaFisica.getId());
        Mensagem mensagem = update(pessoaFisica);

        if(mensagem.getListaVazia()) {
            listaDePessoasFisicas.clear();
            listaDePessoasFisicas.addAll(listarPessoasFisicas(pessoaFisica));
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

    public List<PessoaFisica> listarPessoasFisicas(PessoaFisica pessoaFisica){
        return genericDao.pesquisarListaDeObjeto(pessoaFisica);
    }

    public Long pesquisarIdPessoaFisicaPorNome(String nome){
        return pfDao.pesquisaIdDaPessoaFisicaPorNome(nome);
    }

    public void deletarPessoaFisica(PessoaFisica pessoaFisica) {
        genericDao.deletar(pessoaFisica);
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
            if (pessoaFisica.getId().toString().equals(pfDaLista.getId().toString())){
            }else{
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

    public boolean VerificaSeInformacoesObrigatoriasPreenchidasValidas(PessoaFisica pessoaFisica, Mensagem mensagem){

        Boolean informacoesObrigatoriasPreenchidasValidas = true;

        if (pessoaFisica.getNome() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo nome é obrigatório!");
        }
        if (pessoaFisica.getCpf().length()!= 14){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo cpf deve conter 11 dígitos!");
        }
        if (pessoaFisica.getCpf() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo cpf é obrigatório!");
        }
        if (pessoaFisica.getDataDeNascimento() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo data de nascimento é obrigatório!");
        }
        if (pessoaFisica.getTelefone() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo telefone é obrigatório!");
        }
        if (pessoaFisica.getRendaMensal() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo renda mensal é obrigatório!");
        }
        if (pessoaFisica.getSexo() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo sexo é obrigatório!");
        }
        if (pessoaFisica.getCep() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo cep é obrigatório!");
        }
        if (pessoaFisica.getCidade() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo cidade é obrigatório!");
        }
        if (pessoaFisica.getUF() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo UF é obrigatório!");
        }
        if (pessoaFisica.getEndereco() == null){
            informacoesObrigatoriasPreenchidasValidas = false;
            mensagem.adcionarMensagemNaLista("O campo endereço é obrigatório!");
        }

        return informacoesObrigatoriasPreenchidasValidas;
    }


    public void setPfDao(DaoPF pfDao) {
        this.pfDao = pfDao;
    }

    public void setGenericDao(GenericDao<PessoaFisica> genericDao) {
        this.genericDao = genericDao;
    }
}
