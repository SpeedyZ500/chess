package dataaccess;

import exception.DataAccessException;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLDataAccess {

    void configureDatabase(String[] createStatements) throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    int executeUpdate(String statement, Object... params) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)){
                for (var i = 0; i < params.length; i++){
                    var param = params[i];

                    switch (param) {
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        case String p -> ps.setString(i + 1, p);
                        default -> {
                        }
                    }


                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if(rs.next()){
                    return rs.getInt(1);
                }
                return 0;
            }
        }
        catch(SQLException e){
            if(e.getErrorCode() == 1082){ //1082 is the duplicate code
                throw new DataAccessException("Duplicate Item");
            }
            else{
                throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
            }
        }
    }
}
