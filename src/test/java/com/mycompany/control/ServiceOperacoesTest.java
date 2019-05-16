package com.mycompany.control;

import com.mycompany.DAO.DaoConta;
import com.mycompany.model.Conta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")

public class ServiceOperacoesTest {

    @Autowired
    private ServiceOperacoes serviceOperacoes;

    @Autowired
    private DaoConta daoConta;

    Conta conta1 = new Conta();
    Conta conta2 = new Conta();
    Conta conta3 = new Conta();

    @Before
    public void setUp() throws Exception {
            conta1 = daoConta.pesquisaObjetoContaPorId(1L);
            conta2 =  daoConta.pesquisaObjetoContaPorId(2L);
            conta3 = daoConta.pesquisaObjetoContaPorId(3L);
    }

    @Test
    public void deposito() {
        try{
            serviceOperacoes.deposito(conta3,"100");
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void saque() {
        try{

            serviceOperacoes.saque(conta1,"100");
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void transferencia() {
        try{
            serviceOperacoes.transferencia(conta2,conta3,"100");
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void pegarDataHoraAtual() {
    }
}