package service;

import exception.DataAccessException;
import dataaccess.authdao.AuthDAO;
import dataaccess.userdao.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public Collection<UserData> listUsers() throws ResponseException {
        try{
            return userDAO.listUsers();
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    public Collection<AuthData> listAuths() throws ResponseException{
        try{
            return authDAO.listAuths();
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    public AuthData register(UserData userData) throws ResponseException {
        try {
            UserData exists = null;
            exists = userDAO.getUser(userData.username());
            if (exists != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            String hashedPass = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            exists = userDAO.createUser(new UserData(userData.username(), hashedPass, userData.email()));
            return authDAO.createAuth(new AuthData("", exists.username()));
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public AuthData login(UserData user) throws ResponseException{
        try{
            UserData existing = userDAO.getUser(user.username());
            if(existing == null || !BCrypt.checkpw(user.password(), existing.password())){
                throw new ResponseException(401, "Error: unauthorized");
            }
            return authDAO.createAuth(new AuthData("", user.username()));
        }
        catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public boolean verifyToken(String authToken) throws ResponseException{
        try{
            return (authDAO.getAuth(authToken) != null);
        }
        catch(DataAccessException e){
            throw new ResponseException(500,"Error: " + e.getMessage());
        }
    }

    public void logout(String authToken) throws ResponseException{
        try{
            authDAO.deleteAuth(authToken);
        }
        catch(DataAccessException e){
            throw new ResponseException(500,"Error: " + e.getMessage());
        }
    }

    public String getUsername(String authToken) throws ResponseException{
        try{
            return authDAO.getAuth(authToken).username();
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}
