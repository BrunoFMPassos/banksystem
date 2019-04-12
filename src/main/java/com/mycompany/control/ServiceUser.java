package com.mycompany.control;

import com.mycompany.DAO.DaoUser;
import com.mycompany.model.User;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ServiceUser {

    @SpringBean(name="userDao")
    private DaoUser userDao;

    public void insert(User user){
        userDao.insert(user);
    }

    public User searchforname(String username){
        return userDao.searchForUserName(username);
    }

    public DaoUser getUserDao() {
        return userDao;
    }

    public void setUserDao(DaoUser dao) {
        this.userDao = dao;
    }



}
