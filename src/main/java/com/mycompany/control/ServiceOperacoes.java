package com.mycompany.control;

import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Conta;
import com.mycompany.model.Movimentacao;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceOperacoes {

    @SpringBean(name = "genericDao")
    private GenericDao<Movimentacao> genericDao;
    @SpringBean(name = "contaService")
    private ServiceConta serviceConta;

    public Mensagem deposito(Conta conta, String valor) {
        Mensagem mensagem = new Mensagem();
        String statusConta = conta.getStatus();
        if (statusConta.equals("Ativa")) {
            String numeroOp = "1";
            String descricaoOp = "Depósito";
            Movimentacao movimentacao = new Movimentacao();
            Double valorDeposito = Double.parseDouble(valor);
            Double saldoAtual = Double.parseDouble(conta.getSaldo());
            List<Movimentacao> listaDeMovimentacoes = new ArrayList<Movimentacao>();
            listaDeMovimentacoes.addAll(genericDao.pesquisarListaDeObjeto(movimentacao));
            Boolean primeiraMovimentacao = true;
            for(Movimentacao movimentacaoDaLista: listaDeMovimentacoes){
                if(movimentacaoDaLista.getConta().getId().equals(conta.getId())){
                    primeiraMovimentacao = false;
                }
            }
            if(primeiraMovimentacao == true){
                Movimentacao primeiraMovimentacaoRealizada = new Movimentacao();
                Double tarifaDaConta = Double.parseDouble(conta.getTipoDeConta().getTarifa());
                valorDeposito = valorDeposito - tarifaDaConta;
                preparaMovimentaçãoParaInserir(primeiraMovimentacaoRealizada,conta,"Tatifa de Conta","0",tarifaDaConta.toString());
                genericDao.inserir(primeiraMovimentacaoRealizada);
            }
            saldoAtual = saldoAtual + valorDeposito;
            conta.setSaldo(saldoAtual.toString());
            serviceConta.preparaContaParaOperacoes(conta);
            serviceConta.update(conta);
            preparaMovimentaçãoParaInserir(movimentacao, conta, descricaoOp, numeroOp, valor);
            genericDao.inserir(movimentacao);
        } else {
            mensagem.adcionarMensagemNaLista("Somente contas ativas podem realizar operações!");
        }
        return mensagem;
    }

    public Mensagem saque(Conta conta, String valor) {
        Mensagem mensagem = new Mensagem();
        String statusConta = conta.getStatus();
        if (statusConta.equals("Ativa")) {
            Movimentacao movimentacao = new Movimentacao();
            String numeroOp = "2";
            String descricaoOp = "Saque";
            Double valorSaque = Double.parseDouble(valor);
            Double saldoAtual = Double.parseDouble(conta.getSaldo());
            Double limiteConta = Double.parseDouble(conta.getLimiteConta());
            Double limiteDeSaque = saldoAtual + limiteConta;
            if (limiteDeSaque > valorSaque) {
                saldoAtual = saldoAtual - valorSaque;
                conta.setSaldo(saldoAtual.toString());
                serviceConta.preparaContaParaOperacoes(conta);
                serviceConta.update(conta);
                preparaMovimentaçãoParaInserir(movimentacao, conta, descricaoOp, numeroOp, valor);
                genericDao.inserir(movimentacao);
            } else {
                mensagem.adcionarMensagemNaLista("Saldo insuficiente!");
            }
        } else {
            mensagem.adcionarMensagemNaLista("Somente contas ativas podem realizar operações!");
        }
        return mensagem;
    }

    public Mensagem transferencia(Conta contaDestino, Conta contaOrigem, String valor) {
        Mensagem mensagem = new Mensagem();
        String statusContaOrigem = contaOrigem.getStatus();
        String statusContaDestino = contaDestino.getStatus();
        if (statusContaOrigem.equals("Ativa") && statusContaDestino.equals("Ativa")) {
            Movimentacao movimentacao = new Movimentacao();
            Movimentacao movimentacaoDestino = new Movimentacao();
            String numeroOp = "3";
            String descricaoOp = "Transferência";
            String numeroOpParaDestino = "4";
            String descricaoOpParaDestino = "Transferência Recebida";
            Double valorDaTransferencia = Double.parseDouble(valor);
            Double saldoContaOrigem = Double.parseDouble(contaOrigem.getSaldo());
            Double limiteContaOrigem = Double.parseDouble(contaOrigem.getLimiteConta());
            Double saldoContaDestino = Double.parseDouble(contaDestino.getSaldo());
            Double limiteParaTransferir = saldoContaOrigem + limiteContaOrigem;
            List<Movimentacao> listaDeMovimentacoes = new ArrayList<Movimentacao>();
            listaDeMovimentacoes.addAll(genericDao.pesquisarListaDeObjeto(movimentacao));
            Boolean primeiraMovimentacao = true;
            for(Movimentacao movimentacaoDaLista: listaDeMovimentacoes){
                if(movimentacaoDaLista.getConta().getId().equals(contaDestino.getId())){
                    primeiraMovimentacao = false;
                }
            }
            if(primeiraMovimentacao == true){
                Movimentacao primeiraMovimentacaoRealizada = new Movimentacao();
                Double tarifaDaConta = Double.parseDouble(contaDestino.getTipoDeConta().getTarifa());
                valorDaTransferencia = valorDaTransferencia - tarifaDaConta;
                preparaMovimentaçãoParaInserir(primeiraMovimentacaoRealizada,contaDestino,"Tatifa de Conta","0",tarifaDaConta.toString());
                genericDao.inserir(primeiraMovimentacaoRealizada);
            }
            if (limiteParaTransferir > valorDaTransferencia) {
                saldoContaOrigem = saldoContaOrigem - valorDaTransferencia;
                saldoContaDestino = saldoContaDestino + valorDaTransferencia;
                contaOrigem.setSaldo(saldoContaOrigem.toString());
                contaDestino.setSaldo(saldoContaDestino.toString());
                serviceConta.preparaContaParaOperacoes(contaOrigem);
                serviceConta.update(contaOrigem);
                preparaMovimentaçãoParaInserir(movimentacao, contaOrigem, descricaoOp, numeroOp, valor);
                genericDao.inserir(movimentacao);
                serviceConta.preparaContaParaOperacoes(contaDestino);
                serviceConta.update(contaDestino);
                preparaMovimentaçãoParaInserir(movimentacaoDestino, contaDestino, descricaoOpParaDestino, numeroOpParaDestino, valor);
                genericDao.inserir(movimentacaoDestino);
            } else {
                mensagem.adcionarMensagemNaLista("Saldo insuficiente!");
            }
        } else {
            mensagem.adcionarMensagemNaLista("Somente contas ativas podem realizar operações!");
        }
        return mensagem;
    }

    public String pegarDataHoraAtual() {
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d = new Date(System.currentTimeMillis());
        String dataHoraAtual = formatador.format(d);
        return dataHoraAtual;
    }

    public void preparaMovimentaçãoParaInserir(Movimentacao movimentacao, Conta conta, String descricaoOp, String numeroOp, String valor) {
        movimentacao.setConta(conta);
        movimentacao.setDescricao(descricaoOp);
        movimentacao.setNumero(numeroOp);
        movimentacao.setData(pegarDataHoraAtual());
        movimentacao.setValor(valor);
    }


    public void setServiceConta(ServiceConta serviceConta) {
        this.serviceConta = serviceConta;
    }

    public void setGenericDao(GenericDao<Movimentacao> genericDao) {
        this.genericDao = genericDao;
    }
}
