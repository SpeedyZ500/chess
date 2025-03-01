package dataaccess.userdao;

import dataaccess.DataAccessException;
import model.UserData;

import java.util.Collection;


public interface UserDAO {
    UserData createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    void clearUsers() throws DataAccessException;
    //for testing purposes
    Collection<UserData> listUsers() throws DataAccessException;
}
