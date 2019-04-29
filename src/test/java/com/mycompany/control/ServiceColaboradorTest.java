package com.mycompany.control;

import com.mycompany.model.Agencia;
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

        Agencia agencia = new Agencia();
        agencia.setId(1L);
        agencia.setCidade("Anápolis");
        agencia.setUF("GO");
        agencia.setNumero(1);

        Agencia agencia2 = new Agencia();
        agencia2.setId(2L);
        agencia2.setCidade("Anápolis");
        agencia2.setUF("GO");
        agencia2.setNumero(2);

        colaborador.setNome("Bruno");
        colaborador.setCpf("111.111.111-11");
        colaborador.setDataDeNascimento("27/08/1996");
        colaborador.setRg("5810483");
        colaborador.setSexo("Masculino");
        colaborador.setUsername("admin");
        colaborador.setPassword("123456");
        colaborador.setPerfil("Diretor");
        colaborador.setCidade("Anápolis");
        colaborador.setUF("GO");
        colaborador.setEndereco("Rua teste");
        colaborador.setNumero(102);
        colaborador.setComplemento("testedecomplemento");
        colaborador4.setAgencia(agencia2);

        colaborador2.setNome("João");
        colaborador2.setCpf("111.111.111-11");
        colaborador2.setDataDeNascimento("27/08/1996");
        colaborador2.setRg("5810483");
        colaborador2.setSexo("Masculino");
        colaborador2.setUsername("joao");
        colaborador2.setPassword("123456");
        colaborador2.setPerfil("Caixa");
        colaborador2.setCidade("Anápolis");
        colaborador2.setUF("GO");
        colaborador2.setEndereco("Rua teste");
        colaborador2.setNumero(102);
        colaborador2.setComplemento("testedecomplemento");
        colaborador4.setAgencia(agencia);

        colaborador3.setNome("Maria");
        colaborador3.setCpf("111.111.111-11");
        colaborador3.setDataDeNascimento("27/08/1996");
        colaborador3.setRg("5810483");
        colaborador3.setSexo("Feminino");
        colaborador3.setUsername("maria");
        colaborador3.setPassword("123456");
        colaborador3.setPerfil("Gerente");
        colaborador3.setCidade("Anápolis");
        colaborador3.setUF("GO");
        colaborador3.setEndereco("Rua teste");
        colaborador3.setNumero(102);
        colaborador3.setComplemento("testedecomplemento");
        colaborador4.setAgencia(agencia);

        colaborador4.setNome("Teste");
        colaborador4.setCpf("111.111.111-11");
        colaborador4.setDataDeNascimento("27/08/1996");
        colaborador4.setRg("5810483");
        colaborador4.setSexo("Masculino");
        colaborador4.setUsername("teste");
        colaborador4.setPassword("testes123");
        colaborador4.setPerfil("Diretor");
        colaborador4.setCidade("Anápolis");
        colaborador4.setUF("GO");
        colaborador4.setEndereco("Rua teste");
        colaborador4.setNumero(102);
        colaborador4.setComplemento("testedecomplemento");
        colaborador4.setAgencia(agencia2);



    }
    @Test
    public void insert() {

        try {
            serviceColaborador.inserir(colaborador3,"inserir");
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }

    }

    @Test
    public void update() {

        try {
            Colaborador  colaborador = serviceColaborador.pesquisarObjetoColaboradorPorNome("Teste");
            colaborador.setCpf("222.222.222-22");
            colaborador.setPassword("testes123");
            colaborador.setUsername("teste");
            colaborador.setPerfil("Diretor");
            serviceColaborador.inserir(colaborador,"update");
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