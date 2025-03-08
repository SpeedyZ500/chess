package dataaccess;
//really wanted to put this in the userdao subpackage,
// but it wouldn't let me because the methods for DatabaseManager are default

import dataaccess.userdao.UserDAO;
import model.UserData;

import java.util.Collection;
import java.util.List;

public class SQLUserDAO implements UserDAO {
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
