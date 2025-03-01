package dataaccess.userdao;

import dataaccess.DataAccessException;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        users.remove(username);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        users.clear();
    }
}
