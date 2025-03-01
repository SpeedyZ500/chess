package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    AuthData createAuth(AuthData authData) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void clearAuths() throws DataAccessException;
    //note fore testing purposes
    Collection<AuthData> listAuths() throws DataAccessException;
}
