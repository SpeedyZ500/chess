package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, AuthData> auths = new HashMap<>();


    @Override
    public String createAuth(AuthData authData) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        auths.put(authToken, new AuthData(authData.username(), authToken));
        return authToken;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    @Override
    public void clearAuth() throws DataAccessException {
        auths.clear();
    }
}
