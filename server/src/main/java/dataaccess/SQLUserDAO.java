package dataaccess;
//really wanted to put this in the userdao subpackage,
// but it wouldn't let me because the methods for DatabaseManager are default

import dataaccess.userdao.UserDAO;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLUserDAO extends SQLDataAccess implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
        return userData;
    }



    @Override
    public UserData getUser(String username) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,username);
                try(var rs = ps.executeQuery()){
                    if(rs.next()){
                        return readUser(rs);
                    }
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException{
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }


    @Override
    public void deleteUser(String username) throws DataAccessException {
        if(getUser(username) == null){
            throw new DataAccessException("Can't delete a user that does not exist");
        }
        var statement = "DELETE FROM user WHERE  username=?";
        executeUpdate(statement, username);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    @Override
    public Collection<UserData> listUsers() throws DataAccessException {
        var result = new ArrayList<UserData>();
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM user";
            try (var ps = conn.prepareStatement(statement)){
                try(var rs = ps.executeQuery()){
                    while(rs.next()){
                        result.add(readUser(rs));
                    }
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }



    private final String[] createStatements = {
            """
            CREATE TABLE if NOT EXISTS user(
                username VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255),
                PRIMARY KEY (username)
            )
            """
    };

}
