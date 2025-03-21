package dataaccess;
//really wanted to put this in the authdao subpackage,
// but it wouldn't let me because the methods for DatabaseManager are default
import dataaccess.authdao.AuthDAO;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


public class SQLAuthDAO extends SQLDataAccess implements AuthDAO {
    public SQLAuthDAO() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        authData = new AuthData(UUID.randomUUID().toString(), authData.username());
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authData.authToken(), authData.username());
        return authData;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if(getAuth(authToken) == null){
            throw new DataAccessException("Can't Delete what doesn't exist");
        }
        var statement = "DELETE FROM auth WHERE  authToken=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM auth WHERE authToken=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,authToken);
                try(var rs = ps.executeQuery()){
                    if(rs.next()){
                        return readAuth(rs);
                    }
                }
            }

        }
        catch(SQLException e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException{
        var username = rs.getString("username");
        var authToken = rs.getString("authToken");
        return new AuthData(authToken, username);
    }

    @Override
    public void clearAuths() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    @Override
    public Collection<AuthData> listAuths() throws DataAccessException {
        var result = new ArrayList<AuthData>();
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM auth";
            try (var ps = conn.prepareStatement(statement)){
                try(var rs = ps.executeQuery()){
                    while(rs.next()){
                        result.add(readAuth(rs));
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
            CREATE TABLE if NOT EXISTS auth(
                authToken VARCHAR(255) NOT NULL UNIQUE,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            )
            """
    };

}
