package com.mycompany.control;

import com.mycompany.DAO.DaoConta;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.*;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
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
            preparaContaParaInserir(conta,mensagem);
            if(mensagem.getListaVazia()) {
                preparaCartaoParaInserir(conta, cartao);
                serviceCartao.inserir(cartao);
                conta.setCartao(cartao);
                genericDao.inserir(conta);
            }
        }
        return mensagem;
    }


    public  Mensagem update(Conta conta){
        Mensagem mensagem = new Mensagem();
        Boolean informacoesObrigatoriasPreenchidas = verificaSeInformacoesObrigatoriasPreenchidas(conta,mensagem);
        if(informacoesObrigatoriasPreenchidas){
                    Cartao cartao = conta.getCartao();
                    preparaCartaoParaUpdate(conta, cartao);
                    preparaContaParaInserir(conta,mensagem);
                    conta.setLimiteConta(gerarLimiteDaConta(conta));
                    serviceCartao.inserir(cartao);
                    conta.setCartao(cartao);
                    genericDao.inserir(conta);
        }
        return mensagem;
    }

    public Mensagem deletar(Conta conta){
        Mensagem mensagem = new Mensagem();
        if(conta.getStatus().equals("Inativa")){
            daoConta.deletar(conta);
        }else{
            mensagem.adcionarMensagemNaLista("Contas Ativas não podem ser excluídas!");
        }
        return mensagem;
    }

    public void executarAoClicarEmSimNaModalExcluir(
            List<Conta> listaDeContas, Conta conta,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {
        Mensagem mensagem = deletar(conta);
        if(mensagem.getListaVazia()) {
            listaDeContas.clear();
            listaDeContas.addAll(pesquisarListaDeContas(conta));
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

    public void desativarConta(Conta conta, AjaxRequestTarget target, FeedbackPanel feedbackPanel){
        if(conta.getStatus().equals("Ativa")){
            conta.setStatus("Inativa");
            feedbackPanel.error("Conta desativada com sucesso!");
            target.add(feedbackPanel);
        }
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

    public void preparaContaParaInserir(Conta conta, Mensagem mensagem){
        if(conta.getTipoDeConta().getPessoa().equals("Física")){
            PessoaFisica pf = servicePF.pesquisaObjetoPessoaFisicaPorNome(conta.getTitular());
            if(pf != null) {
                conta.setPessoaFisica(pf);
            }else{
                mensagem.adcionarMensagemNaLista("Esse tipo de conta é permitido apenas para pessoas Físicas!");
            }
        }else if(conta.getTipoDeConta().getPessoa().equals("Jurídica")){
            PessoaJuridica pj = servicePJ.pesquisaObjetoPessoaJuridicaPorRazaoSocial(conta.getTitular());
            if(pj != null){
            conta.setPessoaJuridica(pj);
            }else {
                mensagem.adcionarMensagemNaLista("Esse tipo de conta é permitido apenas para pessoas Jurídicas!");
            }
        }
        if(mensagem.getListaVazia()) {
            if(conta.getStatus()==null) {
                conta.setStatus("Ativa");
            }
            if(conta.getSaldo() == null) {
                conta.setSaldo("0");
            }
            if(conta.getNumero() == null) {
                conta.setNumero(gerarNumeroDaConta());
            }
            if(conta.getDigito() == null) {
                conta.setDigito(gerarDigitoConta());
            }
            if(conta.getLimiteConta() == null) {
                conta.setLimiteConta(gerarLimiteDaConta(conta));
            }
            if(conta.getVerificador() == null) {
                conta.setVerificador(gerarVerificadorConta(conta));
            }
        }
    }

    public void preparaContaParaMostrar(Conta conta){
        String titular;
        if(conta.getPessoaJuridica() == null) {
            conta.setTitular(conta.getPessoaFisica().getNome());
        }else if(conta.getPessoaFisica() == null){
            conta.setTitular(conta.getPessoaJuridica().getRazaoSocial());
        }
        conta.setSenhaCartao(conta.getCartao().getSenha());
        conta.setTipoDeCartao(conta.getCartao().getTipoDeCartao().getDescricao());
        conta.setNumeroCartao(conta.getCartao().getNumero().toString());
        conta.setCvvCartao(conta.getCartao().getCvv().toString());
        conta.setLimiteCartao(conta.getCartao().getLimite());
        conta.setDataValidadeCartao(conta.getCartao().getDataValidade());
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
        cartao.setDataValidade(cartao.getDataValidade());
        cartao.setTipoDeCartao(serviceTipoDeCartao.pesquisarObjetoTipoDeCartaoPorDescricao(conta.getTipoDeCartao()));
        cartao.setLimite(gerarLimiteDoCartao(conta,cartao));
        cartao.setSenha(conta.getSenhaCartao());
        cartao.setStatus(conta.getStatus());
    }

    public void preparaContaParaOperacoes(Conta conta){
        conta.setNumero(conta.getNumero());
        conta.setDigito(conta.getDigito());
        conta.setCartao(conta.getCartao());
        conta.setLimiteConta(conta.getLimiteConta());
        conta.setLimiteCartao(conta.getCartao().getLimite());
        if(conta.getPessoaFisica() == null){
            conta.setTitular(conta.getPessoaJuridica().getRazaoSocial());
        }else if(conta.getPessoaJuridica() == null){
            conta.setTitular(conta.getPessoaFisica().getNome());
        }
        conta.setNumeroCartao(conta.getCartao().getNumero().toString());
        conta.setCvvCartao(conta.getCartao().getCvv().toString());
        conta.setTipoDeCartao(conta.getCartao().getTipoDeCartao().getDescricao());
        conta.setSenhaCartao(conta.getCartao().getSenha());
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

    public Conta pesquisaObjetoContaPorNumero(Long numero){
        return daoConta.pesquisaObjetoContaPorNumero(numero);
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

    public List<Conta> pesquisarListaDeContasParaOperacoes(Conta conta){

        List<Conta> listaDeContas = daoConta.pesquisarListaDeContas(conta);
        List<Conta> listaDeContasAtivas = new ArrayList<Conta>();
        for(Conta contaDaLista: listaDeContas){
            if(contaDaLista.getStatus().equals("Ativa")){
                listaDeContasAtivas.add(contaDaLista);
            }
        }

        return listaDeContasAtivas;
    }

    public boolean verificaSeInformacoesObrigatoriasPreenchidas(Conta conta, Mensagem mensagem){
        Boolean informacoesObrigatoriasPreenchidas = true;

        if (conta.getTipoDeConta() == null){
            informacoesObrigatoriasPreenchidas = false;
            mensagem.adcionarMensagemNaLista("O campo Tipo de Conta é obrigatório!");
        }
        if (conta.getTitular() == null){
            informacoesObrigatoriasPreenchidas = false;
            mensagem.adcionarMensagemNaLista("O campo Titular de Conta é obrigatório!");
        }
        if (conta.getDataAbertura() == null){
            informacoesObrigatoriasPreenchidas = false;
            mensagem.adcionarMensagemNaLista("O campo Data de Abertura é obrigatório!");
        }
        if (conta.getSenha() == null){
            informacoesObrigatoriasPreenchidas = false;
            mensagem.adcionarMensagemNaLista("O campo Senha é obrigatório!");
        }
        if (conta.getAgencia() == null){
            informacoesObrigatoriasPreenchidas = false;
            mensagem.adcionarMensagemNaLista("O campo Agencia é obrigatório!");
        }
        if (conta.getTipoDeCartao() == null){
            informacoesObrigatoriasPreenchidas = false;
            mensagem.adcionarMensagemNaLista("O campo Tipo de Cartão é obrigatório!");
        }
        if (conta.getSenhaCartao() == null){
            informacoesObrigatoriasPreenchidas = false;
            mensagem.adcionarMensagemNaLista("O campo Senha do cartão é obrigatório!");
        }
        return informacoesObrigatoriasPreenchidas;
    }

    public List<Conta> pesquisaListadeContasPorTitular(Conta conta, List<PessoaFisica> listaPF, List<PessoaJuridica> listaPJ) {
        List<Conta> listaDeTodasAsContas = daoConta.pesquisarListaDeContas(conta);
        List<Conta> contasFiltradas = new ArrayList<Conta>();
        for(Conta contaDaLista:listaDeTodasAsContas){
            for(PessoaFisica PFDaLista:listaPF){
                if(contaDaLista.getPessoaFisica()!= null) {
                    String idPFdaListaString = PFDaLista.getId().toString();
                    String idPFdaContaString = contaDaLista.getPessoaFisica().getId().toString();
                    if (idPFdaContaString.equals(idPFdaListaString)) {
                        contasFiltradas.add(contaDaLista);
                    }
                }
            }
            for(PessoaJuridica PJDaLista:listaPJ){
                if(contaDaLista.getPessoaJuridica()!= null) {
                    String idPJdaListaString = PJDaLista.getId().toString();
                    String idPJdaContaString = contaDaLista.getPessoaJuridica().getId().toString();
                    if (idPJdaContaString.equals(idPJdaListaString)) {
                        contasFiltradas.add(contaDaLista);
                    }
                }
            }
        }
        return contasFiltradas;
    }

    public void ocultaLabelNaVisao(Label label, Boolean editar){
        if(!editar){
            label.setVisible(false);
        }else{
            label.setVisible(true);
        }
    }

    public void ocultaTextFieldNaVisao(TextField textField, Boolean editar){
        if(!editar){
            textField.setVisible(false);
        }else{
            textField.setVisible(true);
        }
    }

    public void ocultaAjaxLinkNaVisao(AjaxLink ajaxLink, Boolean editar){
        if(!editar){
            ajaxLink.setVisible(false);
        }else{
            ajaxLink.setVisible(true);
        }
    }


    public void filtrarContaNaVisao(String titular, String agencia, String tipo, List<Conta> listaDeContas, Conta conta, AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!titular.isEmpty() && agencia.isEmpty() && tipo.isEmpty()) {
            PessoaFisica pf = new PessoaFisica();
            PessoaJuridica pj = new PessoaJuridica();
            List<PessoaFisica> listaPF =  servicePF.pesquisarListaDePessoasFisicasPorString(pf,"nome",titular);
            List<PessoaJuridica> listaPJ = servicePJ.pesquisarListaDePessoasJuridicasPorString(pj,"razaoSocial",titular);
            listaDeContas.clear();
            listaDeContas.addAll(pesquisaListadeContasPorTitular(conta,listaPF,listaPJ));
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
            listaAuxiliarContasPorTitular.addAll(pesquisaListadeContasPorTitular(conta,listaPF,listaPJ));

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
            listaAuxiliarContasPorTitular.addAll(pesquisaListadeContasPorTitular(conta,listaPF,listaPJ));

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
        }else if(!titular.isEmpty() && !agencia.isEmpty() && !tipo.isEmpty()) {
            listaDeContas.clear();
            List<Conta> listaAuxiliarContasPorTitular = new ArrayList<Conta>();
            List<Conta> listaAuxiliarContasporAgencia = new ArrayList<Conta>();
            List<Conta> listaAuxiliarContasporTipo = new ArrayList<Conta>();
            PessoaFisica pf = new PessoaFisica();
            PessoaJuridica pj = new PessoaJuridica();
            List<PessoaFisica> listaPF =  servicePF.pesquisarListaDePessoasFisicasPorString(pf,"nome",titular);
            List<PessoaJuridica> listaPJ = servicePJ.pesquisarListaDePessoasJuridicasPorString(pj,"razaoSocial",titular);
            listaAuxiliarContasPorTitular.addAll(pesquisaListadeContasPorTitular(conta,listaPF,listaPJ));

            Agencia agenciaObj = serviceAgencia.pesquisaObjetoAgenciaPorNumero(agencia);
            listaAuxiliarContasporAgencia.addAll(pesquisarListaDeContasPorAgencia(conta,agenciaObj));
            TipoDeConta tipoObj = serviceTipoDeConta.pesquisarObjetoTipoDeContaPorDescricao(tipo);
            listaAuxiliarContasporTipo.addAll(pesquisarListaDeContasPorTipo(conta,tipoObj));;
            for(Conta contaloop: listaAuxiliarContasPorTitular){
                for(Conta contaloop2: listaAuxiliarContasporAgencia){
                        for(Conta contaloop3: listaAuxiliarContasporTipo){
                            if(contaloop.getNumero().equals(contaloop2.getNumero())&&contaloop.getNumero().equals(contaloop3.getNumero())){
                            listaDeContas.add(contaloop);
                        }
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


    public void filtrarContaNaVisaoOperacoes(String numero, List<Conta> listaDeContas, Conta conta, AjaxRequestTarget target, MarkupContainer rowPanel) {
        Long numeroBusca = 0L;
        if(!numero.isEmpty()) {
            numeroBusca = Long.parseLong(numero);
        }
        if (!numero.isEmpty()) {
            listaDeContas.clear();
            listaDeContas.addAll(genericDao.pesquisaListadeObjetosPorLong(conta,"numero",numeroBusca));
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

    public void pesquisarListaDePFePJ(List<String> listaDePessoasPesquisa){
        PessoaFisica pf = new PessoaFisica();
        PessoaJuridica pj = new PessoaJuridica();

            List<PessoaFisica> listaDePF = new ArrayList<PessoaFisica>();
            List<PessoaJuridica> listaDePJ = new ArrayList<PessoaJuridica>();
            listaDePF.addAll(servicePF.listarPessoasFisicas(pf));
            listaDePJ.addAll(servicePJ.listarPessoasJuridicas(pj));
            for(PessoaFisica pfDaLista: listaDePF){
                listaDePessoasPesquisa.add(pfDaLista.getNome());
            }
            for(PessoaJuridica pjDaLista: listaDePJ){
                listaDePessoasPesquisa.add(pjDaLista.getRazaoSocial());
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
