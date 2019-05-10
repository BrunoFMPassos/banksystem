package com.mycompany.control;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")

public class ServiceContaTest {

    @Autowired
    private ServiceConta serviceConta;
    private FeedbackPanel feedbackPanel;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void inserir() {
    }

    @Test
    public void update() {
    }

    @Test
    public void deletar() {
    }

    @Test
    public void desativar() {
    }

    @Test
    public void gerarNumeroDaConta() {
        try{
            System.out.println("Numero da conta: "+serviceConta.gerarNumeroDaConta());
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar Teste!");
        }
    }

    @Test
    public void gerarDigitoConta() {
        try{
            System.out.println("Dígito da conta: "+serviceConta.gerarDigitoConta());
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar Teste!");
        }
    }

    @Test
    public void gerarLimiteDaConta() {
        /*try{
            System.out.println("Limite da conta: R$"+serviceConta.gerarLimiteDaConta(948.25,50.0));
        }catch (Exception e){
            System.out.println("Erro ao realizar Teste!");
        }*/
    }

    @Test
    public void cirarCartao() {
    }

    @Test
    public void gerarNumeroDoCartao() {
        try{
            System.out.println("Número do Cartão: "+serviceConta.gerarNumeroDoCartao());
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar Teste!");
        }
    }

    @Test
    public void gerarCVVDoCartao() {
        try{
            System.out.println("CVV: "+serviceConta.gerarCVVDoCartao());
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar Teste!");
        }
    }

    @Test
    public void gerarDataValidadeDoCartao() {
        try{
            System.out.println("Data de vencimento: "+serviceConta.gerarDataValidadeDoCartao());
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar Teste!");
        }
    }

    @Test
    public void gerarLimiteDoCartao() {
    }

    @Test
    public void verificaSeInformacoesObrigatoriasPreenchidas() {
    }

    @Test
    public void pesquisaPessoaFisicaPorConta() {
    }

    @Test
    public void pesquisaPessoaJuridicaPorConta() {
    }

    @Test
    public void pesquisaAgenciaDaConta() {
    }

    @Test
    public void verificaSeNumeroGeradoUnico() {
    }
}