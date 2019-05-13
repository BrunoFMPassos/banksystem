package com.mycompany.control;

import com.mycompany.model.Conta;
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
    @Autowired
    private ServiceAgencia serviceAgencia;
    @Autowired
    private ServicePF servicePF;
    @Autowired
    private ServicePJ servicePJ;
    @Autowired
    private ServiceTipoDeCartao serviceTipoDeCartao;
    @Autowired
    private ServiceTipoDeConta serviceTipoDeConta;
    private FeedbackPanel feedbackPanel;

    Conta conta = new Conta();
    Conta conta2 = new Conta();
    Conta conta3 = new Conta();


    @Before
    public void setUp() throws Exception {

        conta.setStatus("Ativa");
        conta.setAgencia(serviceAgencia.pesquisaObjetoAgenciaPorNumero("1"));
        conta.setDataAbertura("13/05/2019");
        conta.setPessoaFisica(servicePF.pesquisaObjetoPessoaFisicaPorId(786432L));
        conta.setSaldo("1000");
        conta.setSenha(1234);
        conta.setSenhaCartao(1234);
        conta.setTipoDeCartao(serviceTipoDeCartao.pesquisarObjetoTipoDeCartaoPorId(65536L).getDescricao());
        conta.setTipoDeConta(serviceTipoDeConta.pesquisarObjetoTipoDeContaPorId(131072L));

        conta2.setStatus("Ativa");
        conta2.setAgencia(serviceAgencia.pesquisaObjetoAgenciaPorNumero("1"));
        conta2.setDataAbertura("13/05/2019");
        conta2.setPessoaFisica(servicePF.pesquisaObjetoPessoaFisicaPorId(786433L));
        conta2.setSaldo("3000");
        conta2.setSenha(1234);
        conta2.setSenhaCartao(1234);
        conta2.setTipoDeCartao(serviceTipoDeCartao.pesquisarObjetoTipoDeCartaoPorId(65536L).getDescricao());
        conta2.setTipoDeConta(serviceTipoDeConta.pesquisarObjetoTipoDeContaPorId(98304L));

        conta3.setStatus("Ativa");
        conta3.setAgencia(serviceAgencia.pesquisaObjetoAgenciaPorNumero("2"));
        conta3.setDataAbertura("13/05/2019");
        conta3.setPessoaJuridica(servicePJ.pesquisarObjetoPessoaJutidicaPorId(2L));
        conta3.setSaldo("2000");
        conta3.setSenha(1234);
        conta3.setSenhaCartao(1234);
        conta3.setTipoDeCartao(serviceTipoDeCartao.pesquisarObjetoTipoDeCartaoPorId(65537L).getDescricao());
        conta3.setTipoDeConta(serviceTipoDeConta.pesquisarObjetoTipoDeContaPorId(131073L));
    }

    @Test
    public void inserir() {
        try{
            serviceConta.inserir(conta3);
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar Teste!");
        }

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