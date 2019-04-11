package com.mycompany.control;

import com.mycompany.DAO.DaoUser;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.User;

public class ServiceUser {

    private GenericDao<User> dao = new GenericDao<User>();
    private DaoUser daouser = new DaoUser();

    public void insert(User user){
        dao.insert(user);
    }



}
