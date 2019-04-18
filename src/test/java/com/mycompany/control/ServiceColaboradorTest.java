package com.mycompany.control;

import com.mycompany.DAO.DaoColaborador;
import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")


public class ServiceColaboradorTest {

    @Autowired
    private ServiceColaborador serviceColaborador;

    Colaborador colaborador = new Colaborador();
    Colaborador colaborador2 = new Colaborador();
    Colaborador colaborador3 = new Colaborador();

    @Before
    public void setUp() throws Exception {

        colaborador.setNome("Bruno");
        colaborador.setCpf(11111111111L);
        colaborador.setData_nascimento(27081996L);
        colaborador.setRg(5810483L);
        colaborador.setSexo("Masculino");
        colaborador.setUsername("admin");
        colaborador.setPassword("123456");
        colaborador.setPerfil("Diretor");

        colaborador2.setNome("João");
        colaborador2.setCpf(22222222222L);
        colaborador2.setData_nascimento(26061998L);
        colaborador2.setRg(444444L);
        colaborador2.setSexo("Masculino");
        colaborador2.setUsername("joao");
        colaborador2.setPassword("123456");
        colaborador2.setPerfil("Caixa");

        colaborador3.setNome("Maria");
        colaborador3.setCpf(88888888888L);
        colaborador3.setData_nascimento(19071997L);
        colaborador3.setRg(222222L);
        colaborador3.setSexo("Feminino");
        colaborador3.setUsername("maria");
        colaborador3.setPassword("123456");
        colaborador3.setPerfil("Gerente");



    }
    @Test
    public void insert() {

        try {
            serviceColaborador.insert(colaborador2);
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }


    }

    @Test
    public void update() {

        try {
            Colaborador  colaborador = serviceColaborador.searchForName("João");
            colaborador.setCpf(22222222222L);
            colaborador.setPassword("123456");
            colaborador.setUsername("joao");
            colaborador.setPerfil("Caixa");
            serviceColaborador.insert(colaborador);
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }


    }

    @Test
    public void searchForName() {
        try {
            Colaborador  colaborador = serviceColaborador.searchForName("João");
            System.out.println("Colaborador: "+colaborador.getId());

        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void searchForUser() {
        try {
            Colaborador  colaborador = serviceColaborador.searchForName("Maria");
            User user = serviceColaborador.searchForUser(colaborador);
            System.out.println("User: "+user.getUsername());
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void searchForNameList() {
        try {
            List<Colaborador> lista = serviceColaborador.searchForNameList(colaborador,"Bruno","nome");
            System.out.println(lista);
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void listColaborador() {
        try {
            List<Colaborador> lista = serviceColaborador.listColaborador(colaborador);
            System.out.println(lista);
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }

    @Test
    public void deleteColaborador() {
        try {
            serviceColaborador.deleteColaborador(colaborador);
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }
    }
}