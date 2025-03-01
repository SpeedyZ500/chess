package service;

import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.userdao.UserDAO;

public class ClearService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() throws DataAccessException{
        authDAO.clearAuth();
        userDAO.clearUsers();
        gameDAO.clearGames();
    }


}
