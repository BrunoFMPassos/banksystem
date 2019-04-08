package com.mycompany.tests;

import com.mycompany.control.GenericDao;
import com.mycompany.model.User;

public class UserTest {
	
	
	public static void main(String[] args) {
		User user = new User();
		GenericDao<User> dao = new GenericDao<User>();
		
		user.setUsername("Teste");
		user.setPassword("testes123");
		user.setPerfil("Gerente");
		
		dao.insert(user);
		
		
		
	}

}
