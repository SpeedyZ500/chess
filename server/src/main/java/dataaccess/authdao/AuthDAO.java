package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    String createAuth(AuthData authData) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void clearAuth() throws DataAccessException;
}
