package service;

import dataaccess.authdao.AuthDAO;
import dataaccess.userdao.UserDAO;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
}
