package dataaccess.userdao;

import dataaccess.DataAccessException;
import model.UserData;


public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    void clearUsers() throws DataAccessException;
}
