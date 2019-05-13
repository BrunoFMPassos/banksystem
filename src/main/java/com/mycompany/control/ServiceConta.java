package com.mycompany.control;

import com.mycompany.DAO.DaoConta;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.*;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Query;
import org.hibernate.Session;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServiceConta {
    @SpringBean(name = "contaDao")
    private DaoConta daoConta;
    @SpringBean(name = "genericDao")
    private GenericDao<Conta> genericDao;
    @SpringBean(name = "agenciaService")
    private ServiceAgencia serviceAgencia;
    @SpringBean(name = "cartaoService")
    private ServiceCartao serviceCartao;
    @SpringBean(name = "pfService")
    private ServicePF servicePF;
    @SpringBean(name = "pjService")
    private ServicePJ servicePJ;
    @SpringBean(name = "tipoDeContaService")
    private ServiceTipoDeConta serviceTipoDeConta;
    @SpringBean(name = "tipoDeCartaoService")
    private ServiceTipoDeCartao serviceTipoDeCartao;

    public Mensagem inserir(Conta conta){
        Mensagem mensagem = new Mensagem();
        Boolean informacoesObrigatoriasPreenchidas = verificaSeInformacoesObrigatoriasPreenchidas(conta,mensagem);
        if(informacoesObrigatoriasPreenchidas){
            Cartao cartao = new Cartao();
            preparaCartaoParaInserir(conta,cartao);
            serviceCartao.inserir(cartao);
            conta.setCartao(cartao);
            conta.setNumero(gerarNumeroDaConta());
            conta.setDigito(gerarDigitoConta());
            conta.setLimiteConta(gerarLimiteDaConta(conta));
            conta.setVerificador(gerarVerificadorConta(conta));
            genericDao.inserir(conta);
        }
        return mensagem;
    }

    public  Mensagem update(Conta conta){
        Mensagem mensagem = new Mensagem();
        Boolean informacoesObrigatoriasPreenchidas = verificaSeInformacoesObrigatoriasPreenchidas(conta,mensagem);
        if(informacoesObrigatoriasPreenchidas){
            if(conta.getStatus().equals("Ativa")) {
                Cartao cartao = conta.getCartao();
                preparaCartaoParaUpdate(conta,cartao);
                conta.setLimiteConta(gerarLimiteDaConta(conta));
                genericDao.inserir(conta);
            }
        }
        return mensagem;
    }

    public Mensagem deletar(Conta conta){
        Mensagem mensagem = new Mensagem();
        if(conta.getStatus().equals("Inativa")){
            Cartao cartao = conta.getCartao();
            serviceCartao.deletarCartao(cartao);
            genericDao.deletar(conta);
        }
        return null;
    }

    public Mensagem ativarOuDesativarConta(Conta conta){
        Mensagem mensagem = new Mensagem();
        if(conta.getStatus().equals("Inativa")){
            conta.setStatus("Ativa");
        }
        return mensagem;
    }

    public Mensagem desativar(Conta conta){
        Mensagem mensagem = new Mensagem();
        if(conta.getStatus().equals("Ativa")){
            conta.setStatus("Inativa");
        }
        return mensagem;
    }

    public Long gerarNumeroDaConta(){
        Conta conta = new Conta();
        List<Conta> listaDeContasExistentes = new ArrayList<Conta>();
        listaDeContasExistentes.addAll(genericDao.pesquisarListaDeObjeto(conta));
        Random gerador = new Random();
        List<Integer> listaDeNumerosIndividuais = new ArrayList<Integer>();
        String listaDeNumerosIndivuaisConcatenada = "";
        for(int i = 0; i < 5; i++){
            int numero = gerador.nextInt(9)+1;
           listaDeNumerosIndividuais.add(numero);
        }
        for(int i = 0; i < 5; i++){
          listaDeNumerosIndivuaisConcatenada = listaDeNumerosIndivuaisConcatenada+listaDeNumerosIndividuais.get(i).toString();
        }
        Long numeroDaConta = Long.parseLong(listaDeNumerosIndivuaisConcatenada);

        if(!listaDeContasExistentes.isEmpty()){
            for(Conta contaDaLista: listaDeContasExistentes){
                if(numeroDaConta == contaDaLista.getNumero()){
                    gerarNumeroDaConta();
                }
            }
        }
        return numeroDaConta;
    }

    public int gerarDigitoConta(){
        Random gerador = new Random();
        int  digito = gerador.nextInt(9)+1;
        return digito;
    }

    public Long gerarVerificadorConta(Conta conta){
        Long verificador = (conta.getNumero()*conta.getDigito());
        return verificador;
    }

    public String gerarLimiteDaConta(Conta conta){
        Double rendaMensalDoTitular = null;
        if(conta.getPessoaJuridica() == null) {
             rendaMensalDoTitular = Double.parseDouble(conta.getPessoaFisica().getRendaMensal());
        }else if(conta.getPessoaFisica() == null) {
            rendaMensalDoTitular = Double.parseDouble(conta.getPessoaJuridica().getRendaMensal());
        }
        Double baseLimiteDoTipoDeConta = Double.parseDouble(conta.getTipoDeConta().getBaselimite());
        Double limiteDaConta = (rendaMensalDoTitular*baseLimiteDoTipoDeConta)/100;
        return  limiteDaConta.toString();
    }

    public void preparaCartaoParaInserir(Conta conta, Cartao cartao){
        cartao.setNumero(gerarNumeroDoCartao());
        cartao.setCvv(gerarCVVDoCartao());
        cartao.setDataValidade(gerarDataValidadeDoCartao());
        cartao.setTipoDeCartao(serviceTipoDeCartao.pesquisarObjetoTipoDeCartaoPorDescricao(conta.getTipoDeCartao()));
        cartao.setLimite(gerarLimiteDoCartao(conta,cartao));
        cartao.setSenha(conta.getSenhaCartao());
        cartao.setStatus(conta.getStatus());
    }

    public void preparaCartaoParaUpdate(Conta conta, Cartao cartao){
        cartao.setNumero(conta.getCartao().getNumero());
        cartao.setCvv(conta.getCartao().getCvv());
        cartao.setDataValidade(gerarDataValidadeDoCartao());
        cartao.setTipoDeCartao(serviceTipoDeCartao.pesquisarObjetoTipoDeCartaoPorDescricao(conta.getTipoDeCartao()));
        cartao.setLimite(gerarLimiteDoCartao(conta,cartao));
        cartao.setSenha(conta.getSenhaCartao());
        cartao.setStatus(conta.getStatus());
    }

    public Long gerarNumeroDoCartao(){
        Cartao cartao = new Cartao();
        List<Cartao> listaDeCartoesExistentes = new ArrayList<Cartao>();
        listaDeCartoesExistentes.addAll(serviceCartao.pesquisaListaDeCartoesExistentes(cartao));
        Random gerador = new Random();
        List<Integer> listaDeNumerosIndividuais = new ArrayList<Integer>();
        String listaDeNumerosIndivuaisConcatenada = "";
        for(int i = 0; i < 16; i++){
            int numero = gerador.nextInt(9)+1;
            listaDeNumerosIndividuais.add(numero);
        }
        for(int i = 0; i < 16; i++){
            listaDeNumerosIndivuaisConcatenada = listaDeNumerosIndivuaisConcatenada+listaDeNumerosIndividuais.get(i).toString();
        }
        Long numeroDoCartao = Long.parseLong(listaDeNumerosIndivuaisConcatenada);

        if(!listaDeCartoesExistentes.isEmpty()){
            for(Cartao cartaoDaLista: listaDeCartoesExistentes){
                if(numeroDoCartao == cartaoDaLista.getNumero()){
                    gerarNumeroDoCartao();
                }
            }
        }
        return numeroDoCartao;
    }

    public int gerarCVVDoCartao(){
        Random gerador = new Random();
        List<Integer> listaDeNumerosIndividuais = new ArrayList<Integer>();
        String listaDeNumerosIndivuaisConcatenada = "";
        for(int i = 0; i < 3; i++){
            int numero = gerador.nextInt(9)+1;
            listaDeNumerosIndividuais.add(numero);
        }
        for(int i = 0; i < 3; i++){
            listaDeNumerosIndivuaisConcatenada = listaDeNumerosIndivuaisConcatenada+listaDeNumerosIndividuais.get(i).toString();
        }
        int cvvDoCartao = Integer.parseInt(listaDeNumerosIndivuaisConcatenada);

        return cvvDoCartao;
    }

    public String gerarDataValidadeDoCartao(){
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date(System.currentTimeMillis());
        String dataAtual = formatador.format(d);
        String[] dataAtualSeparada = dataAtual.split("/");
        Integer anoDeVencimento = Integer.parseInt(dataAtualSeparada[2])+4;
        String dataDeValidadeDoCartao = dataAtualSeparada[0]+"/"+dataAtualSeparada[1]+"/"+anoDeVencimento.toString();
        return dataDeValidadeDoCartao;
    }

    public String gerarLimiteDoCartao(Conta conta, Cartao cartao){
        Double rendaMensalDoTitular = null;
        if(conta.getPessoaJuridica() == null) {
            rendaMensalDoTitular = Double.parseDouble(conta.getPessoaFisica().getRendaMensal());
        }else if(conta.getPessoaFisica() == null){
            rendaMensalDoTitular = Double.parseDouble(conta.getPessoaJuridica().getRendaMensal());
        }
        Double baseLimiteDoTipoDeCartao = Double.parseDouble(cartao.getTipoDeCartao().getBaselimite());
        Double limiteDoCartao = (rendaMensalDoTitular*baseLimiteDoTipoDeCartao)/100;
        return  limiteDoCartao.toString();
    }

    public String pesquisarNomeDoTitular(PessoaJuridica pj, PessoaFisica pf){
        String titular = "";
        if(pf == null){
            titular = pj.getRazaoSocial();
        }else if(pj == null){
            titular = pf.getNome();
        }
        return titular;
    }

    public List<Conta> pesquisarListaDeContasPorAgencia(Conta conta, Agencia agencia){
        return daoConta.pesquisarListaDeObjetosContaPorAgencia(conta,agencia);
    }

    public List<Conta> pesquisarListaDeContasPorTipo(Conta conta, TipoDeConta tipoDeConta){
        return daoConta.pesquisarListaDeObjetosContaPorTipo(conta, tipoDeConta);
    }

    public List<Conta> pesquisarListaDeContas(Conta conta){
        return daoConta.pesquisarListaDeContas(conta);
    }

    public boolean verificaSeInformacoesObrigatoriasPreenchidas(Conta conta, Mensagem mensagem){
        return true;
    }

    public List<Conta> pesquisaListadeContasPorTitular(Conta conta, List<Long> listaIdsPF, List<Long> listaIdsPJ) {
        List<Conta> listaDeTodasAsContas = daoConta.pesquisarListaDeContas(conta);
        List<Conta> contasFiltradas = new ArrayList<>();
        for(Conta contaDaLista:listaDeTodasAsContas){
            for(Long idPFDaLista:listaIdsPF){
                if(idPFDaLista.equals(contaDaLista.getPessoaFisica().getId())){
                    contasFiltradas.add(contaDaLista);
                }
            }
            for(Long idPJDaLista:listaIdsPJ){
                if(idPJDaLista.equals(contaDaLista.getPessoaJuridica().getId())){
                    contasFiltradas.add(contaDaLista);
                }
            }
        }
        return contasFiltradas;
    }

    public void filtrarContaNaVisao(String titular, String agencia, String tipo, List<Conta> listaDeContas, Conta conta, AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!titular.isEmpty() && agencia.isEmpty() && tipo.isEmpty()) {
            listaDeContas.clear();
            PessoaFisica pf = new PessoaFisica();
            PessoaJuridica pj = new PessoaJuridica();
            List<PessoaFisica> listaPF =  servicePF.pesquisarListaDePessoasFisicasPorString(pf,"nome",titular);
            List<PessoaJuridica> listaPJ = servicePJ.pesquisarListaDePessoasJuridicasPorString(pj,"razaoSocial",titular);
            List<Long> listaDeIdsPF = new ArrayList<Long>();
            List<Long> listaDeIdsPJ = new ArrayList<Long>();
            listaDeContas.addAll(pesquisaListadeContasPorTitular(conta,listaDeIdsPF,listaDeIdsPJ));
            target.add(rowPanel);

        } else if (titular.isEmpty() && !agencia.isEmpty() && tipo.isEmpty()) {
            listaDeContas.clear();
            Agencia agenciaObj = serviceAgencia.pesquisaObjetoAgenciaPorNumero(agencia);
            listaDeContas.addAll(pesquisarListaDeContasPorAgencia(conta, agenciaObj));
            target.add(rowPanel);

        }else if (titular.isEmpty() && agencia.isEmpty() && !tipo.isEmpty()) {
            listaDeContas.clear();
            TipoDeConta tipoObj = serviceTipoDeConta.pesquisarObjetoTipoDeContaPorDescricao(tipo);
            listaDeContas.addAll( pesquisarListaDeContasPorTipo(conta, tipoObj));
            target.add(rowPanel);

        } else if (!titular.isEmpty() && !agencia.isEmpty() && tipo.isEmpty()) {
            listaDeContas.clear();
            List<Conta> listaAuxiliarContasPorTitular = new ArrayList<Conta>();
            List<Conta> listaAuxiliarContasporAgencia = new ArrayList<Conta>();
            PessoaFisica pf = new PessoaFisica();
            PessoaJuridica pj = new PessoaJuridica();
            List<PessoaFisica> listaPF =  servicePF.pesquisarListaDePessoasFisicasPorString(pf,"nome",titular);
            List<PessoaJuridica> listaPJ = servicePJ.pesquisarListaDePessoasJuridicasPorString(pj,"razaoSocial",titular);
            List<Long> listaDeIdsPF = new ArrayList<Long>();
            List<Long> listaDeIdsPJ = new ArrayList<Long>();
            listaAuxiliarContasPorTitular.addAll(pesquisaListadeContasPorTitular(conta,listaDeIdsPF,listaDeIdsPJ));

            Agencia agenciaObj = serviceAgencia.pesquisaObjetoAgenciaPorNumero(agencia);
            listaAuxiliarContasporAgencia.addAll(pesquisarListaDeContasPorAgencia(conta,agenciaObj));

            for(Conta contaloop: listaAuxiliarContasPorTitular){
                for(Conta contaloop2: listaAuxiliarContasporAgencia){
                    if(contaloop.getNumero().equals(contaloop2.getNumero())){
                        listaDeContas.add(contaloop);
                    }
                }
            }
            target.add(rowPanel);
        }else if (!titular.isEmpty() && agencia.isEmpty() && !tipo.isEmpty()) {
            listaDeContas.clear();
            List<Conta> listaAuxiliarContasPorTitular = new ArrayList<Conta>();
            List<Conta> listaAuxiliarContasporTipo = new ArrayList<Conta>();
            PessoaFisica pf = new PessoaFisica();
            PessoaJuridica pj = new PessoaJuridica();
            List<PessoaFisica> listaPF =  servicePF.pesquisarListaDePessoasFisicasPorString(pf,"nome",titular);
            List<PessoaJuridica> listaPJ = servicePJ.pesquisarListaDePessoasJuridicasPorString(pj,"razaoSocial",titular);
            List<Long> listaDeIdsPF = new ArrayList<Long>();
            List<Long> listaDeIdsPJ = new ArrayList<Long>();
            listaAuxiliarContasPorTitular.addAll(pesquisaListadeContasPorTitular(conta,listaDeIdsPF,listaDeIdsPJ));

            TipoDeConta tipoObj = serviceTipoDeConta.pesquisarObjetoTipoDeContaPorDescricao(tipo);
            listaAuxiliarContasporTipo.addAll(pesquisarListaDeContasPorTipo(conta,tipoObj));

            for(Conta contaloop: listaAuxiliarContasPorTitular){
                for(Conta contaloop2: listaAuxiliarContasporTipo){
                    if(contaloop.getNumero().equals(contaloop2.getNumero())){
                        listaDeContas.add(contaloop);
                    }
                }
            }
            target.add(rowPanel);
        } else if (titular.isEmpty() && !agencia.isEmpty() && !tipo.isEmpty()) {
            listaDeContas.clear();
            List<Conta> listaAuxiliarContasPorAgencia = new ArrayList<Conta>();
            List<Conta> listaAuxiliarContasporTipo = new ArrayList<Conta>();

            Agencia agenciaObj = serviceAgencia.pesquisaObjetoAgenciaPorNumero(agencia);
            listaAuxiliarContasPorAgencia.addAll(pesquisarListaDeContasPorAgencia(conta,agenciaObj));

            TipoDeConta tipoObj = serviceTipoDeConta.pesquisarObjetoTipoDeContaPorDescricao(tipo);
            listaAuxiliarContasporTipo.addAll(pesquisarListaDeContasPorTipo(conta,tipoObj));

            for(Conta contaloop: listaAuxiliarContasPorAgencia){
                for(Conta contaloop2: listaAuxiliarContasporTipo){
                    if(contaloop.getNumero().equals(contaloop2.getNumero())){
                        listaDeContas.add(contaloop);
                    }
                }
            }
            target.add(rowPanel);
        }else {
            listaDeContas.clear();
            listaDeContas.addAll(daoConta.pesquisarListaDeContas(conta));
        }
        target.add(rowPanel);
    }

    public void executarAoClicarEmSalvarNaModalSalvar(
            List<Conta> listaDeContas, Conta conta,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = inserir(conta);
        if(mensagem.getListaVazia()) {
            listaDeContas.clear();
            listaDeContas.addAll(daoConta.pesquisarListaDeContas(conta));
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
            List<Conta> listaDeContas, Conta conta,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = update(conta);
        if(mensagem.getListaVazia()) {
            listaDeContas.clear();
            listaDeContas.addAll(daoConta.pesquisarListaDeContas(conta));
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




    public void setServiceCartao(ServiceCartao serviceCartao) {
        this.serviceCartao = serviceCartao;
    }

    public void setDaoConta(DaoConta daoConta) {
        this.daoConta = daoConta;
    }

    public void setGenericDao(GenericDao<Conta> genericDao) {
        this.genericDao = genericDao;
    }

    public void setServiceAgencia(ServiceAgencia serviceAgencia) {
        this.serviceAgencia = serviceAgencia;
    }

    public void setServicePF(ServicePF servicePF) {
        this.servicePF = servicePF;
    }

    public void setServicePJ(ServicePJ servicePJ) {
        this.servicePJ = servicePJ;
    }

    public void setServiceTipoDeConta(ServiceTipoDeConta serviceTipoDeConta) {
        this.serviceTipoDeConta = serviceTipoDeConta;
    }

    public void setServiceTipoDeCartao(ServiceTipoDeCartao serviceTipoDeCartao) {
        this.serviceTipoDeCartao = serviceTipoDeCartao;
    }
}
