package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, AuthData> auths = new HashMap<>();


    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        authData = new AuthData(UUID.randomUUID().toString(), authData.username());
        auths.put(authData.authToken(), authData);
        return authData;
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
    public void clearAuths() throws DataAccessException {
        auths.clear();
    }

    @Override
    public Collection<AuthData> listAuths() throws DataAccessException {
        return auths.values();
    }
}
