package dataaccess;

import dataaccess.userdao.UserDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class UserDAOTests {

    private UserDAO getUserDAO(Class<? extends UserDAO> userDatabaseClass) throws DataAccessException{
        UserDAO db;
        if(userDatabaseClass.equals(SQLUserDAO.class)){
            db = new SQLUserDAO();
        }
        else{
            db = new MemoryUserDAO();
        }
        db.clearUsers();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class})
    void createUser(Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO userDAO = getUserDAO(dbClass);
        var userData = new UserData("bill_nye_science", "12345", "billnye@scienceguy.com");
        assertDoesNotThrow(() -> userDAO.createUser(userData));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class})
    void getUser(Class<? extends UserDAO> dbClass) throws DataAccessException{
        UserDAO userDAO = getUserDAO(dbClass);
        var expected = userDAO.createUser(new UserData("bill_nye_science", "12345", "billnye@scienceguy.com"));
        var actual = userDAO.getUser(expected.username());
        assertUserDataEqual(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class})
    void listUsers(Class<? extends UserDAO> dbClass) throws DataAccessException{
        UserDAO userDAO = getUserDAO(dbClass);
        Map<String, UserData> expected = new HashMap<>();
        expected.put("bill_nye_science", userDAO.createUser(new UserData("bill_nye_science", "12345", "billnye@scienceguy.com")));
        expected.put("sonic_the_hedgehog", userDAO.createUser(new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org")));
        expected.put("ash_catch_em", userDAO.createUser(new UserData("ash_catch_em", "peek@U4L!fe", "ash@pokemon.com")));

        var actual = userDAO.listUsers();
        assertUserCollectionEqual(expected.values(), actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class})
    void deleteUser(Class<? extends UserDAO> dbClass) throws DataAccessException{
        UserDAO userDAO = getUserDAO(dbClass);
        Map<String, UserData> expected = new HashMap<>();
        var deleteUser = userDAO.createUser(new UserData("bill_nye_science", "12345", "billnye@scienceguy.com"));
        expected.put("sonic_the_hedgehog", userDAO.createUser(new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org")));
        expected.put("ash_catch_em", userDAO.createUser(new UserData("ash_catch_em", "peek@U4L!fe", "ash@pokemon.com")));

        userDAO.deleteUser(deleteUser.username());

        var actual = userDAO.listUsers();
        assertUserCollectionEqual(expected.values(), actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class})
    void clearUsers(Class<? extends UserDAO> dbClass) throws DataAccessException{
        UserDAO userDAO = getUserDAO(dbClass);
        userDAO.createUser(new UserData("bill_nye_science", "12345", "billnye@scienceguy.com"));
        userDAO.createUser(new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org"));
        userDAO.createUser(new UserData("ash_catch_em", "peek@U4L!fe", "ash@pokemon.com"));
        userDAO.clearUsers();
        var actual = userDAO.listUsers();
        assertEquals(0, actual.size());
    }



    public static void assertUserDataEqual(UserData expected, UserData actual){
        assertEquals(expected.username(), actual.username());
        assertEquals(expected.password(), actual.password());
        assertEquals(expected.email(), actual.email());

    }

    public static void assertUserCollectionEqual(Collection<UserData> excepted, Collection<UserData> actual){
        UserData[] actualList = actual.toArray(new UserData[]{});
        UserData[] expectedList = excepted.toArray(new UserData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++){
            assertUserDataEqual(expectedList[i], actualList[i]);
        }

    }
}
