package com.mycompany.tests;

import com.mycompany.DAO.DaoUser;
import com.mycompany.model.User;

public class UserTest {
	
	
	public static void main(String[] args) {
		User user = new User();
		DaoUser<User> dao = new DaoUser<User>();
		
		user.setUsername("Teste2");
		user.setPassword("testes123");
		user.setPerfil("Cliente");
		
		dao.insert(user);
		
		
		
	}

}
