package com.mycompany.control;

import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")


public class ServiceColaboradorTest {

    @Autowired
    private ServiceColaborador serviceColaborador;

    Colaborador colaborador = new Colaborador();
    Colaborador colaborador2 = new Colaborador();
    Colaborador colaborador3 = new Colaborador();
    Colaborador colaborador4 = new Colaborador();

    @Before
    public void setUp() throws Exception {

        colaborador.setNome("Bruno");
        colaborador.setCpf(11111111111L);
        colaborador.setDataDeNascimento(27081996L);
        colaborador.setRg(5810483L);
        colaborador.setSexo("Masculino");
        colaborador.setUsername("admin");
        colaborador.setPassword("123456");
        colaborador.setPerfil("Diretor");
        colaborador.setCidade("Anápolis");
        colaborador.setUF("GO");
        colaborador.setEnderecoDesc("Rua teste");
        colaborador.setNumero(102);
        colaborador.setComplemento("testedecomplemento");
        colaborador.setAgencia("1234");

        colaborador2.setNome("João");
        colaborador2.setCpf(22222222222L);
        colaborador2.setDataDeNascimento(26061998L);
        colaborador2.setRg(444444L);
        colaborador2.setSexo("Masculino");
        colaborador2.setUsername("joao");
        colaborador2.setPassword("123456");
        colaborador2.setPerfil("Caixa");
        colaborador2.setCidade("Anápolis");
        colaborador2.setUF("GO");
        colaborador2.setEnderecoDesc("Rua teste");
        colaborador2.setNumero(102);
        colaborador2.setComplemento("testedecomplemento");
        colaborador2.setAgencia("1234");

        colaborador3.setNome("Maria");
        colaborador3.setCpf(88888888888L);
        colaborador3.setDataDeNascimento(19071997L);
        colaborador3.setRg(222222L);
        colaborador3.setSexo("Feminino");
        colaborador3.setUsername("maria");
        colaborador3.setPassword("123456");
        colaborador3.setPerfil("Gerente");
        colaborador3.setCidade("Anápolis");
        colaborador3.setUF("GO");
        colaborador3.setEnderecoDesc("Rua teste");
        colaborador3.setNumero(102);
        colaborador3.setComplemento("testedecomplemento");
        colaborador3.setAgencia("1234");

        colaborador4.setNome("Teste");
        colaborador4.setCpf(00000000000L);
        colaborador4.setDataDeNascimento(10101999L);
        colaborador4.setRg(111111L);
        colaborador4.setSexo("Masculino");
        colaborador4.setUsername("teste");
        colaborador4.setPassword("testes123");
        colaborador4.setPerfil("Diretor");
        colaborador4.setCidade("Anápolis");
        colaborador4.setUF("GO");
        colaborador4.setEnderecoDesc("Rua teste");
        colaborador4.setNumero(102);
        colaborador4.setComplemento("testedecomplemento");
        colaborador4.setAgencia("1234");



    }
    @Test
    public void insert() {

        try {
            serviceColaborador.inserir(colaborador4);
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }


    }

    @Test
    public void update() {

        try {
            Colaborador  colaborador = serviceColaborador.pesquisarObjetoColaboradorPorNome("João");
            colaborador.setCpf(22222222222L);
            colaborador.setPassword("123456");
            colaborador.setUsername("joao");
            colaborador.setPerfil("Caixa");
            serviceColaborador.inserir(colaborador);
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }


    }

    @Test
    public void searchForName() {
        try {
            Colaborador  colaborador = serviceColaborador.pesquisarObjetoColaboradorPorNome("João");
            System.out.println("Colaborador: "+colaborador.getId());

        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void searchForUser() {
        try {
            Colaborador  colaborador = serviceColaborador.pesquisarObjetoColaboradorPorNome("Maria");
            User user = serviceColaborador.pesquisarObjetoUserPorColaborador(colaborador);
            System.out.println("User: "+user.getUsername());
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void searchForNameList() {
        try {
            List<Colaborador> lista = serviceColaborador.pesquisarListaDeColaboradoresPorNome(colaborador,"nome","o");
            System.out.println(lista);
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void listColaborador() {
        try {
            List<Colaborador> lista = serviceColaborador.pesquisarListaDeColaboradoresPorColabordaor(colaborador);
            System.out.println(lista);
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void deleteColaborador() {
        try {
            Colaborador colaboradordelete = serviceColaborador.pesquisarObjetoColaboradorPorNome("João");
            serviceColaborador.deletarColaborador(colaboradordelete);
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }
}