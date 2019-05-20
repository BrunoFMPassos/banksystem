package com.mycompany.control;

import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Conta;
import com.mycompany.model.Contato;
import com.mycompany.model.Movimentacao;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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
    @SpringBean(name = "contatoService")
    private ServiceContato serviceContato;

    public Mensagem deposito(Conta conta, String valor, String senha) {
        Mensagem mensagem = new Mensagem();
        Boolean senhaCorreta = validarSenha(conta, senha);
        if (senhaCorreta) {
            String statusConta = conta.getStatus();
            if (statusConta.equals("Ativa")) {
                String numeroOp = "1";
                String descricaoOp = "Depósito";
                Movimentacao movimentacao = new Movimentacao();
                Double valorDeposito = Double.parseDouble(valor);
                Double saldoAtual = Double.parseDouble(conta.getSaldo());
                valorDeposito = cobraTarifaSePrimeiroDepósito(movimentacao, conta, valorDeposito);
                saldoAtual = saldoAtual + valorDeposito;
                conta.setSaldo(saldoAtual.toString());
                serviceConta.preparaContaParaOperacoes(conta);
                serviceConta.update(conta);
                preparaMovimentaçãoParaInserir(movimentacao, conta, descricaoOp, numeroOp, valor);
                genericDao.inserir(movimentacao);
            } else {
                mensagem.adcionarMensagemNaLista("Somente contas ativas podem realizar operações!");
            }
        } else {
            mensagem.adcionarMensagemNaLista("Senha Incorreta!");
        }
        return mensagem;
    }

    public Mensagem saque(Conta conta, String valor, String senha) {
        Mensagem mensagem = new Mensagem();
        Boolean senhaCorreta = validarSenha(conta, senha);
        if (senhaCorreta) {
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
        } else {
            mensagem.adcionarMensagemNaLista("Senha Incorreta!");
        }
        return mensagem;
    }

    public Mensagem transferencia(Conta contaDestino, Conta contaOrigem, String valor, String senha) {
        Mensagem mensagem = new Mensagem();
        Boolean senhaCorreta = validarSenha(contaOrigem, senha);
        if (senhaCorreta) {
            String statusContaOrigem = contaOrigem.getStatus();
            if (contaDestino != null && contaOrigem.getNumeroBanco().equals("001")) {
                String statusContaDestino = contaDestino.getStatus();
                if (statusContaOrigem.equals("Ativa") && statusContaDestino.equals("Ativa")) {
                    realizaTransferencia(valor,contaOrigem,contaDestino,mensagem);
                } else {
                    mensagem.adcionarMensagemNaLista("Somente contas ativas podem realizar operações!");
                }
            }else{
                if (statusContaOrigem.equals("Ativa")) {
                    realizaTransferencia(valor,contaOrigem,contaDestino,mensagem);
                } else {
                    mensagem.adcionarMensagemNaLista("Somente contas ativas podem realizar operações!");
                }
            }
        } else {
            mensagem.adcionarMensagemNaLista("Senha Incorreta!");
        }
        return mensagem;
    }

    public void realizaTransferencia(String valor, Conta contaOrigem, Conta contaDestino, Mensagem mensagem) {
        Movimentacao movimentacao = new Movimentacao();
        Movimentacao movimentacaoDestino = new Movimentacao();
        String numeroOp = "3";
        String descricaoOp = "Transferência";
        String numeroOpParaDestino = "4";
        String descricaoOpParaDestino = "Transferência Recebida";
        Double valorDaTransferencia;
        if(contaOrigem.getNumeroBanco().equals("001")) {
             valorDaTransferencia = Double.parseDouble(valor);
        }else{
            valorDaTransferencia = Double.parseDouble(valor)-9;
            preparaMovimentaçãoParaInserir(movimentacao, contaOrigem,"Tarifa de Transferência","0","9");
            genericDao.inserir(movimentacao);
        }
        Double saldoContaOrigem = Double.parseDouble(contaOrigem.getSaldo());
        Double limiteContaOrigem = Double.parseDouble(contaOrigem.getLimiteConta());
        Double saldoContaDestino;
        if(contaDestino != null) {
             saldoContaDestino = Double.parseDouble(contaDestino.getSaldo());
        }else{
             saldoContaDestino = 0.0;
        }
        Double limiteParaTransferir = saldoContaOrigem + limiteContaOrigem;
        if (limiteParaTransferir > valorDaTransferencia) {
            saldoContaOrigem = saldoContaOrigem - valorDaTransferencia;
            saldoContaDestino = saldoContaDestino + valorDaTransferencia;
            contaOrigem.setSaldo(saldoContaOrigem.toString());
            serviceConta.preparaContaParaOperacoes(contaOrigem);
            serviceConta.update(contaOrigem);
            preparaMovimentaçãoParaInserir(movimentacao, contaOrigem, descricaoOp, numeroOp, valor);
            genericDao.inserir(movimentacao);
            if(contaDestino != null) {
                contaDestino.setSaldo(saldoContaDestino.toString());
                serviceConta.preparaContaParaOperacoes(contaDestino);
                serviceConta.update(contaDestino);
                preparaMovimentaçãoParaInserir(movimentacaoDestino, contaDestino, descricaoOpParaDestino, numeroOpParaDestino, valor);
                genericDao.inserir(movimentacaoDestino);
            }
        }else {
            mensagem.adcionarMensagemNaLista("Saldo insuficiente!");
        }
    }


    public Double cobraTarifaSePrimeiroDepósito(Movimentacao movimentacao, Conta conta, Double valor) {
        List<Movimentacao> listaDeMovimentacoes = new ArrayList<Movimentacao>();
        listaDeMovimentacoes.addAll(genericDao.pesquisarListaDeObjeto(movimentacao));
        Boolean primeiraMovimentacao = true;
        for (Movimentacao movimentacaoDaLista : listaDeMovimentacoes) {
            if (movimentacaoDaLista.getConta().getId().equals(conta.getId()) && movimentacaoDaLista.getNumero().equals("1")) {
                primeiraMovimentacao = false;
            }
        }
        if (primeiraMovimentacao == true) {
            Movimentacao primeiraMovimentacaoRealizada = new Movimentacao();
            Double tarifaDaConta = Double.parseDouble(conta.getTipoDeConta().getTarifa());
            valor = valor - tarifaDaConta;
            preparaMovimentaçãoParaInserir(primeiraMovimentacaoRealizada, conta, "Tarifa de Conta", "0", tarifaDaConta.toString());
            genericDao.inserir(primeiraMovimentacaoRealizada);
        }

        return valor;
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

    public Boolean verificaSeContaCorreta(Conta conta) {
        boolean contaCorreta = false;
        Long verificacao = conta.getNumero() * conta.getDigito();
        if (verificacao.equals(conta.getVerificador())) {
            contaCorreta = true;
        }
        return contaCorreta;
    }

    public Boolean validarSenha(Conta conta, String senha) {
        Boolean senhaValida = false;
        Boolean contaCorreta = verificaSeContaCorreta(conta);
        if (senha.equals(conta.getSenha()) && contaCorreta) {
            senhaValida = true;
        }
        return senhaValida;
    }

    public void executarAoCLicarEmFinalizarNaModal(List<Conta> listaDeContas, Conta conta,
                                                   AjaxRequestTarget target,
                                                   ModalWindow modalWindow, FeedbackPanel feedbackPanel, String op,
                                                   String senha, String valor, String numeroContaDestino, Contato contato) {
        if (op.equals("Saque")) {
            Mensagem mensagem = saque(conta, valor, senha);
            if (mensagem.getListaVazia()) {
                modalWindow.close(target);
            } else {
                int index = 0;
                for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                    feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                    index++;
                }
                target.add(feedbackPanel);
            }
        }
        if (op.equals("Deposito")) {
            Mensagem mensagem = deposito(conta, valor, senha);
            if (mensagem.getListaVazia()) {
                modalWindow.close(target);
            } else {
                int index = 0;
                for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                    feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                    index++;
                }
                target.add(feedbackPanel);
            }
        }
        if (op.equals("Transferencia")) {
            Long numeroContaDestinoLong;
            if (numeroContaDestino == null) {
                numeroContaDestinoLong = Long.parseLong(contato.getContaDestino());
            } else {
                numeroContaDestinoLong = Long.parseLong(numeroContaDestino);
            }
            Conta contaDestino = serviceConta.pesquisaObjetoContaPorNumero(numeroContaDestinoLong);

            Mensagem mensagem = transferencia(contaDestino, conta, valor, senha);
            if (mensagem.getListaVazia()) {
                modalWindow.close(target);
            } else {
                int index = 0;
                for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                    feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                    index++;
                }
                target.add(feedbackPanel);
            }
        }

    }

    public void ocultarLabelNaVisaoParaSaque(Label label, String op) {
        if (op.equals("Transferencia")) {
            label.setVisible(false);
        }
    }

    public void ocultarLabelNaVisaoParaDeposito(Label label, String op) {
        if (op.equals("Transferencia") || op.equals("Saque")) {
            label.setVisible(false);
        }
    }

    public void ocultarLabelNaVisaoParaTransferencia(Label label, String op) {
        if (op.equals("Saque") || op.equals("Deposito")) {
            label.setVisible(false);
        }
    }

    public void ocultarTextFieldNaVisao(TextField textField, String op) {
        if (op.equals("Saque") || op.equals("Deposito")) {
            textField.setVisible(false);
        }
    }

    public void ocultarDropDownChoicedNaVisao(DropDownChoice dropDownChoice, String op) {
        if (op.equals("Saque") || op.equals("Deposito")) {
            dropDownChoice.setVisible(false);
        }
    }

    public void ocultarAjaxLinkNaVisao(AjaxLink ajaxLink, String op) {
        if (op.equals("Saque") || op.equals("Deposito")) {
            ajaxLink.setVisible(false);
        }
    }

    public void inserirContato(Conta conta, String apelido, String contaDestino,
                               AjaxRequestTarget target, DropDownChoice dropDownChoice, FeedbackPanel feedbackPanel) {
        Mensagem mensagem = new Mensagem();
        Contato contato = new Contato();
        List<Contato> listaDeContatosExistente = serviceContato.pesquisaListaDeContatosPorConta(contato, conta);
        Boolean contatoExistente = false;
        for (Contato contatoDaLista : listaDeContatosExistente) {
            if (contatoDaLista.getApelido().equals(apelido)) {
                contatoExistente = true;
            }
            if (contatoDaLista.getContaDestino().equals(contaDestino)) {
                contatoExistente = true;
            }
        }
        if (contatoExistente) {
            mensagem.adcionarMensagemNaLista("Contato com mesmo apelido ou número de conta já existente");
        } else {
            contato.setConta(conta);
            contato.setContaDestino(contaDestino);
            contato.setApelido(apelido);
            Conta contaDoContato = serviceConta.pesquisaObjetoContaPorNumero(Long.parseLong(contaDestino));
            if (contaDoContato.getPessoaFisica() == null) {
                contato.setPessoaJuridica(contaDoContato.getPessoaJuridica());
            }
            if (contaDoContato.getPessoaJuridica() == null) {
                contato.setPessoaFisica(contaDoContato.getPessoaFisica());
            }
            serviceContato.inserir(contato);
            mensagem.adcionarMensagemNaLista("Contato inserido com sucesso");
            target.add(dropDownChoice);
        }

        for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
            feedbackPanel.error(mensagemDaLista);
        }
        target.add(feedbackPanel);

    }


    public void setServiceConta(ServiceConta serviceConta) {
        this.serviceConta = serviceConta;
    }

    public void setGenericDao(GenericDao<Movimentacao> genericDao) {
        this.genericDao = genericDao;
    }

    public void setServiceContato(ServiceContato serviceContato) {
        this.serviceContato = serviceContato;
    }
}
