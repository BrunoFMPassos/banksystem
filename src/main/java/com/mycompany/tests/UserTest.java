package com.mycompany.tests;

import com.mycompany.control.ServiceUser;
import com.mycompany.model.User;

public class UserTest {
	
	
	public static void main(String[] args) {
		User user = new User();
		ServiceUser service = new ServiceUser();

		
		user.setUsername("Teste2");
		user.setPassword("testes123");
		user.setPerfil("Cliente");
		
		service.insert(user);
		
		
		
	}

}
