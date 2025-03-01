package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.Collection;
import java.util.List;

public class SQLAuthDAO implements AuthDAO{
    //note not writing yet, just using it to implement the logic in the tests
    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void clearAuths() throws DataAccessException {

    }

    @Override
    public Collection<AuthData> listAuths() throws DataAccessException {
        return List.of();
    }
}
