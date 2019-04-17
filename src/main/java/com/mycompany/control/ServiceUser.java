package com.mycompany.control;

import com.mycompany.DAO.DaoUser;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.User;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceUser {

    @SpringBean(name = "userDao")
    private DaoUser userDao;
    @SpringBean(name = "genericDao")
    private GenericDao<User> genericDao;

    public void insert(User user) {
        genericDao.insert(user);
    }

    public User searchForName(String username) {

        return userDao.searchForUserName(username);
    }

    public List<User> listUser(User user) {
        return genericDao.list(user);
    }

    public void inserUser(User user) {
        genericDao.insert(user);
    }

    public void deleteUser(User user) {
        genericDao.delete(user);
    }


    public void setUserDao(DaoUser dao) {
        this.userDao = dao;
    }

    public void setGenericDao(GenericDao<User> genericDao) {
        this.genericDao = genericDao;
    }
}
