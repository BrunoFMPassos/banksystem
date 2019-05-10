package com.mycompany.control;

import com.mycompany.DAO.DaoConta;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;

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

    public Mensagem inserir(){
        return null;
    }

    public  Mensagem update(){
        return null;
    }

    public Mensagem deletar(){
        return null;
    }

    public Mensagem desativar(){
        return null;
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

    public Double gerarLimiteDaConta(Conta conta){
        Double rendaMensalDoTitular = conta.getPessoaFisica().getRendaMensal();
        Double baseLimiteDoTipoDeConta = conta.getTipoDeConta().getBaselimite();
        Double limiteDaConta = (rendaMensalDoTitular*baseLimiteDoTipoDeConta)/100;
        double limiteDaContaArredondado = Math.round(limiteDaConta);
        return  limiteDaContaArredondado;
    }

    public Cartao preparaCartaoParaInserir(Conta conta, Cartao cartao){
        cartao.setNumero(gerarNumeroDoCartao());
        cartao.setCvv(gerarCVVDoCartao());
        cartao.setDataValidade(gerarDataValidadeDoCartao());
        cartao.setLimite(gerarLimiteDoCartao(conta));
        cartao.setSenha(conta.getSenha());
        cartao.setTipoDeCartao(serviceTipoDeCartao.pesquisarObjetoTipoDeCartaoPorDescricao(conta.getTipoDeCartao()));
        cartao.setStatus(conta.getStatus());
        return null;
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

    public Double gerarLimiteDoCartao(Conta conta){
        Double rendaMensalDoTitular = conta.getPessoaFisica().getRendaMensal();
        Double baseLimiteDoTipoDeCartao = conta.getCartao().getTipoDeCartao().getBaselimite();
        Double limiteDoCartao = (rendaMensalDoTitular*baseLimiteDoTipoDeCartao)/100;
        double limiteDoCartaoArredondado = Math.round(limiteDoCartao);
        return  limiteDoCartaoArredondado;
    }


    public boolean VerificaSeInformacoesObrigatoriasPreenchidas(Conta conta, Mensagem mensagem){
        return true;
    }

    public PessoaFisica pesquisaPessoaFisicaPorConta(Conta conta){
        return null;
    }

    public PessoaJuridica pesquisaPessoaJuridicaPorConta(Conta conta){
        return null;
    }

    public Agencia pesquisaAgenciaDaConta(Conta conta){
        return null;
    }

    public boolean verificaSeNumeroGeradoUnico(Long numero){
        return true;
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
