package com.mycompany.control;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Conta;
import com.mycompany.model.Contato;
import com.mycompany.model.Movimentacao;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
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
    @SpringBean(name = "relatoriosService")
    private ServiceRelatorios<Movimentacao> serviceRelatorios;

    public Mensagem deposito(Conta conta, String valor) {
        Mensagem mensagem = new Mensagem();
        if (!valor.isEmpty()) {
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
            mensagem.adcionarMensagemNaLista("Campo valor obrigatório");
        }
        return mensagem;
    }

    public Mensagem saque(Conta conta, String valor, String senha) {
        Mensagem mensagem = new Mensagem();
        Boolean senhaCorreta = validarSenha(conta, senha);
        if (!valor.isEmpty()) {
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
        } else {
            mensagem.adcionarMensagemNaLista("Campo valor obrigatório");
        }
        return mensagem;
    }

    public Mensagem transferencia(Conta contaDestino, Conta contaOrigem, String valor, String senha) {
        Mensagem mensagem = new Mensagem();
        Movimentacao movimentacaoDebito = new Movimentacao();
        Movimentacao movimentacaoCredito = new Movimentacao();
        Movimentacao movimentacaoTaxa = new Movimentacao();
        if (contaOrigem.getContatoObjeto() != null) {
            contaOrigem.setNumeroBanco(contaOrigem.getContatoObjeto().getNumeroBanco());
            contaDestino = serviceConta.pesquisaObjetoContaPorNumero(Long.parseLong(contaOrigem.getContatoObjeto().getContaDestino()));
        }
        if (contaOrigem.getNumeroBanco() != null) {
            if (!valor.isEmpty() && Double.parseDouble(valor) > 0) {
                if (contaOrigem.getNumeroBanco().equals("001")) {
                    Boolean contaVerificada = verificaSeContaCorreta(contaOrigem);
                    Boolean contaValida = validaConta(contaDestino);
                    if (contaValida) {
                        if (!contaDestino.getNumero().equals(contaOrigem.getNumero())) {
                            Boolean senhaValida = validarSenha(contaOrigem, senha);
                            if (senhaValida) {
                                Double valorDaTransferencia = Double.parseDouble(valor);
                                if ((Double.parseDouble(contaOrigem.getSaldo()) + Double.parseDouble(contaOrigem.getLimiteConta())) >= valorDaTransferencia) {
                                    Double saldoContaDestino = Double.parseDouble(contaDestino.getSaldo()) + valorDaTransferencia;
                                    contaDestino.setSaldo(saldoContaDestino.toString());
                                    serviceConta.preparaContaParaOperacoes(contaDestino);
                                    serviceConta.update(contaDestino);
                                    preparaMovimentaçãoParaInserir(movimentacaoCredito, contaDestino, "Crédito de transferência", "4", valor);
                                    genericDao.inserir(movimentacaoCredito);
                                    Double saldoContaOrigem = Double.parseDouble(contaOrigem.getSaldo()) - valorDaTransferencia;
                                    contaOrigem.setSaldo(saldoContaOrigem.toString());
                                    serviceConta.preparaContaParaOperacoes(contaOrigem);
                                    serviceConta.update(contaOrigem);
                                    preparaMovimentaçãoParaInserir(movimentacaoDebito, contaOrigem, "Débito de transferência", "3", valor);
                                    genericDao.inserir(movimentacaoDebito);
                                } else {
                                    mensagem.adcionarMensagemNaLista("Saldo insuficiente!");
                                }
                            } else {
                                mensagem.adcionarMensagemNaLista("Senha inválida!");
                            }
                        } else {
                            mensagem.adcionarMensagemNaLista("Você não pode transferir para sua própria conta.");
                        }
                    } else {
                        mensagem.adcionarMensagemNaLista("Conta inválida!");
                    }
                } else {
                    Boolean contaVerificada = verificaSeContaCorreta(contaOrigem);
                    if (contaVerificada) {
                        Boolean senhaValida = validarSenha(contaOrigem, senha);
                        if (senhaValida) {
                            Double valorDaTransferencia = Double.parseDouble(valor);
                            if ((Double.parseDouble(contaOrigem.getSaldo()) + Double.parseDouble(contaOrigem.getLimiteConta()))
                                    >= (valorDaTransferencia + Double.parseDouble(contaOrigem.getTipoDeConta().getTaxaDeTransferencia()))) {
                                Double taxa = Double.parseDouble(contaOrigem.getTipoDeConta().getTaxaDeTransferencia());
                                Double valorADescontar = valorDaTransferencia + taxa;
                                Double saldoContaOrigem = Double.parseDouble(contaOrigem.getSaldo());
                                Double novoSaldo = saldoContaOrigem - valorADescontar;
                                contaOrigem.setSaldo(novoSaldo.toString());
                                serviceConta.preparaContaParaOperacoes(contaOrigem);
                                serviceConta.update(contaOrigem);
                                preparaMovimentaçãoParaInserir(movimentacaoTaxa, contaOrigem, "Taxa de transferência", "5",
                                        contaOrigem.getTipoDeConta().getTaxaDeTransferencia());
                                genericDao.inserir(movimentacaoTaxa);
                                preparaMovimentaçãoParaInserir(movimentacaoDebito, contaOrigem, "Débito de transferência", "3", valor);
                                genericDao.inserir(movimentacaoDebito);
                            } else {
                                mensagem.adcionarMensagemNaLista("Saldo insuficiente!");
                            }
                        } else {
                            mensagem.adcionarMensagemNaLista("Senha inválida!");
                        }
                    } else {
                        mensagem.adcionarMensagemNaLista("Conta Inválida");
                    }
                }
            } else {
                mensagem.adcionarMensagemNaLista("O valor da operação deve ser superior a zero!");
            }
        } else {
            mensagem.adcionarMensagemNaLista("O número do banco deve ser informado!");
        }
        return mensagem;
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

    public Boolean validaConta(Conta conta) {
        boolean contaValida = false;
        if (conta.getNumero() != null) {
            List<Conta> listaDeContas = serviceConta.pesquisarListaDeContas(conta);
            for (Conta contaDaLista : listaDeContas) {
                if (contaDaLista.getNumero().equals(conta.getNumero())) {
                    contaValida = true;
                }
            }
        }
        return contaValida;
    }

    public Boolean validarSenha(Conta conta, String senha) {
        Boolean senhaValida = false;
        Boolean contaCorreta = verificaSeContaCorreta(conta);
        if (senha.equals(conta.getSenha()) && contaCorreta) {
            senhaValida = true;
        }
        return senhaValida;
    }

    public void emiteComprovante(Conta conta) {
        Movimentacao movimentacao = new Movimentacao();
        List<Movimentacao> listaDeMovimentacoes = new ArrayList<Movimentacao>();
        listaDeMovimentacoes = buscaMovimentacoesPorConta(movimentacao, conta);
        List<Movimentacao> ultimaMovimentacao = new ArrayList<Movimentacao>();
        int tamanhoDaLista = listaDeMovimentacoes.size();
        movimentacao = listaDeMovimentacoes.get(tamanhoDaLista - 1);
        ultimaMovimentacao.add(movimentacao);

        serviceRelatorios.gererRelatorioExtratoPDF(ultimaMovimentacao, conta);

    }

    public void preparaVisãoParaEmitirComprovante(AjaxButton finalizar, Link comprovante, AjaxLink fechar, String op,
                                                  FeedbackPanel feedbackPanel) {
        List<FeedbackMessage> listaDeMensagens = feedbackPanel.getFeedbackMessages().toList();

        if (listaDeMensagens.isEmpty()) {
            if (op.equals("Deposito") || op.equals("Transferencia")) {
                ocultarAjaxButtonNaVisao(finalizar);
                mostrarLinkNaVisao(comprovante);
                mostrarAjaxLinkNaVisao(fechar);
            }
        }
    }

    public void executarAoClicarEmFecharNaVisão(ModalWindow modalWindow, AjaxRequestTarget target) {
        modalWindow.close(target);
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
            Mensagem mensagem = deposito(conta, valor);
            if (mensagem.getListaVazia()) {

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
            Long numeroContaDestinoLong = null;
            if (numeroContaDestino == null && contato!=null) {
                numeroContaDestinoLong = Long.parseLong(contato.getContaDestino());
            } else if(numeroContaDestino!=null){
                numeroContaDestinoLong = Long.parseLong(numeroContaDestino);
            }
            Conta contaDestino = new Conta();
            if (numeroContaDestinoLong != null) {
                contaDestino = serviceConta.pesquisaObjetoContaPorNumero(numeroContaDestinoLong);
            }
            Mensagem mensagem = transferencia(contaDestino, conta, valor, senha);
            if (mensagem.getListaVazia()) {

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
        if (op.equals("Transferencia") || op.equals("Deposito")) {
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

    public void ocultarAjaxLinkNaVisaoParaTransferencia(AjaxLink ajaxLink, String op) {
        if (op.equals("Saque") || op.equals("Deposito")) {
            ajaxLink.setVisible(false);
        }
    }

    public void ocultarAjaxButtonNaVisao(AjaxButton ajaxButton) {
        ajaxButton.setVisible(false);
    }

    public void mostrarLinkNaVisao(Link link) {
        link.setVisible(true);
    }

    public void mostrarAjaxLinkNaVisao(AjaxLink link) {
        link.setVisible(true);
    }

    public void ocultarBtnPorContaNaVisão(AjaxLink ajaxLink, Conta conta) {
        if (conta.getTipoDeConta().getDescricao().equals("Conta Salário")) {
            ajaxLink.setVisible(false);
        }
    }

    public void ocultarCampoSenhaParDeposito(TextField textField, String op){
        if(op.equals("Deposito")) {
            textField.setVisible(false);
        }
    }

    public void inserirContato(Conta conta, String apelido, String contaDestino, String numeroBanco,
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
            if (!contaDestino.isEmpty()) {
                contato.setConta(conta);
                contato.setContaDestino(contaDestino);
                contato.setApelido(apelido);
                contato.setNumeroBanco(numeroBanco);
                Conta contaDoContato = serviceConta.pesquisaObjetoContaPorNumero(Long.parseLong(contaDestino));
                if(contato.getNumeroBanco().equals("001")) {
                    if (contaDoContato.getPessoaFisica() == null) {
                        contato.setPessoaJuridica(contaDoContato.getPessoaJuridica());
                    }
                    if (contaDoContato.getPessoaJuridica() == null) {
                        contato.setPessoaFisica(contaDoContato.getPessoaFisica());
                    }
                }
                if (!contato.getContaDestino().isEmpty() && !contato.getApelido().isEmpty() && !contato.getNumeroBanco().isEmpty()) {
                    serviceContato.inserir(contato);
                    mensagem.adcionarMensagemNaLista("Contato inserido com sucesso");
                } else {
                    mensagem.adcionarMensagemNaLista("Preencha todos os dados do contato!");
                }
                target.add(dropDownChoice);
            } else {
                mensagem.adcionarMensagemNaLista("Conta inexistente!");
            }
        }
        for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
            feedbackPanel.error(mensagemDaLista);
        }
        target.add(feedbackPanel);

    }

    public List<Movimentacao> buscaMovimentacoesPorConta(Movimentacao movimentacao, Conta conta) {
        List<Movimentacao> listaDeMovimentacoes = genericDao.pesquisarListaDeObjeto(movimentacao);
        List<Movimentacao> listaDeMovimentacoesDaConta = new ArrayList<Movimentacao>();
        for (Movimentacao movimentacaoDaLista : listaDeMovimentacoes) {
            if (movimentacaoDaLista.getConta().getNumero().equals(conta.getNumero())) {
                listaDeMovimentacoesDaConta.add(movimentacaoDaLista);
            }
        }
        return listaDeMovimentacoesDaConta;
    }


    public void setServiceRelatorios(ServiceRelatorios serviceRelatorios) {
        this.serviceRelatorios = serviceRelatorios;
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
