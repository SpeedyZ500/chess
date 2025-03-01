package dataaccess.userdao;

import dataaccess.DataAccessException;
import model.UserData;

import java.util.Collection;
import java.util.List;

public class SQLUserDAO implements UserDAO{
    //note not writing yet, just using it to implement the logic in the tests

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {

    }

    @Override
    public void clearUsers() throws DataAccessException {

    }

    @Override
    public Collection<UserData> listUsers() throws DataAccessException {
        return List.of();
    }
}
