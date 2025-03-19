package service;

import exception.DataAccessException;
import dataaccess.authdao.AuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.userdao.UserDAO;
import exception.ResponseException;

public class ClearService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() throws ResponseException {
        try{
            authDAO.clearAuths();
            userDAO.clearUsers();
            gameDAO.clearGames();
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error:" + e.getMessage());
        }
    }


}
