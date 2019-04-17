package com.mycompany.control;

import com.mycompany.DAO.DaoColaborador;
import com.mycompany.model.Colaborador;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")


public class ServiceColaboradorTest {

    @Autowired
    private ServiceColaborador serviceColaborador;

    Colaborador colaborador = new Colaborador();

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

    }

    @Test
    public void insert() {

        try {
            serviceColaborador.insert(colaborador);
            System.out.println("Teste realizado com sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao realizar teste: "+e);
        }


    }

    @Test
    public void searchForName() {
    }

    @Test
    public void listColaborador() {
    }

    @Test
    public void deleteColaborador() {
    }
}