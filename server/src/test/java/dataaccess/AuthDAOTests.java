package dataaccess;

import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class AuthDAOTests {

    private AuthDAO getAuthDAO(Class<? extends AuthDAO> authDatabaseClass) throws DataAccessException{
        AuthDAO db;
        if(authDatabaseClass.equals(SQLAuthDAO.class)){
            db = new SQLAuthDAO();
        }
        else{
            db = new MemoryAuthDAO();
        }
        db.clearAuths();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void createAuth(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO authDAO = getAuthDAO(dbClass);
        var authData = new AuthData("", "bill_nye_science");
        assertDoesNotThrow(() -> authDAO.createAuth(authData));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void noProvided(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO authDAO = getAuthDAO(dbClass);
        var authData = new AuthData("", null);
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(authData));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void getAuth(Class<? extends AuthDAO> dbClass) throws DataAccessException{
        AuthDAO authDAO = getAuthDAO(dbClass);
        var expected = authDAO.createAuth(new AuthData("", "bill_nye_science"));
        var actual = authDAO.getAuth(expected.authToken());
        assertAuthDataEqual(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void missingAuth(Class<? extends AuthDAO> dbClass) throws DataAccessException{
        AuthDAO authDAO = getAuthDAO(dbClass);
        var actual = authDAO.getAuth("why");
        assertEquals(null, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void listAuths(Class<? extends AuthDAO> dbClass) throws DataAccessException{
        AuthDAO authDAO = getAuthDAO(dbClass);
        List<AuthData> expected = new ArrayList<>();
        expected.add(authDAO.createAuth(new AuthData("", "bill_nye_science")));
        expected.add(authDAO.createAuth(new AuthData("", "sonic_the_hedgehog")));
        expected.add(authDAO.createAuth(new AuthData("", "ash_catch_em")));
        Map<String, AuthData> expectedMap = new HashMap<>();
        expected.forEach((auth) -> expectedMap.put(auth.authToken(), auth));

        var actual = authDAO.listAuths();
        Map<String, AuthData> actualMap = new HashMap<>();
        actual.forEach((auth) -> actualMap.put(auth.authToken(), auth));

        assertAuthCollectionEqual(expectedMap.values(), actualMap.values());
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void emptyAuths(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO authDAO = getAuthDAO(dbClass);
        var actual = authDAO.listAuths();
        assertEquals(0, actual.size());
    }


    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void deleteAuth(Class<? extends AuthDAO> dbClass) throws DataAccessException{
        AuthDAO authDAO = getAuthDAO(dbClass);
        List<AuthData> expected = new ArrayList<>();
        var deleteAuth = authDAO.createAuth(new AuthData("", "bill_nye_science"));
        expected.add(authDAO.createAuth(new AuthData("","sonic_the_hedgehog")));
        expected.add(authDAO.createAuth(new AuthData("","ash_catch_em")));
        Map<String, AuthData> expectedMap = new HashMap<>();
        expected.forEach((auth) -> expectedMap.put(auth.authToken(), auth));
        authDAO.deleteAuth(deleteAuth.authToken());

        var actual = authDAO.listAuths();
        Map<String, AuthData> actualMap = new HashMap<>();
        actual.forEach((auth) -> actualMap.put(auth.authToken(), auth));

        assertAuthCollectionEqual(expectedMap.values(), actualMap.values());

    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void noAuthToDelete(Class<? extends AuthDAO> dbClass) throws DataAccessException{
        AuthDAO authDAO = getAuthDAO(dbClass);
        assertThrows(DataAccessException.class, () -> authDAO.deleteAuth("IDK"));

    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    void clearAuths(Class<? extends AuthDAO> dbClass) throws DataAccessException{
        AuthDAO authDAO = getAuthDAO(dbClass);
        authDAO.createAuth(new AuthData("", "bill_nye_science"));
        authDAO.createAuth(new AuthData("", "sonic_the_hedgehog"));
        authDAO.createAuth(new AuthData("", "ash_catch_em"));
        authDAO.clearAuths();
        var actual = authDAO.listAuths();
        assertEquals(0, actual.size());
    }



    public static void assertAuthDataEqual(AuthData expected, AuthData actual){
        assertEquals(expected.username(), actual.username());
        assertEquals(expected.authToken(), actual.authToken());
    }

    public static void assertAuthCollectionEqual(Collection<AuthData> excepted, Collection<AuthData> actual){
        AuthData[] actualList = actual.toArray(new AuthData[]{});
        AuthData[] expectedList = excepted.toArray(new AuthData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++){
            assertAuthDataEqual(expectedList[i], actualList[i]);
        }

    }
}
