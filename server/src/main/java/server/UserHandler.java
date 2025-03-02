package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {
    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) throws ResponseException{
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        if(userData.username() == null || userData.password() == null || userData.email() == null){
            res.status(400);
            throw new ResponseException(400, "Error: bad request");
        }
        else{
            AuthData auth = userService.register(userData);
            res.status(200);
            res.body(new Gson().toJson(auth));
        }
        return res.body();
    }
    public Object login(Request req, Response res) throws ResponseException {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        if (userData.username() == null || userData.password() == null) {
            throw new ResponseException(401, "Error: unauthorized");
        } else {
            AuthData auth = userService.login(userData);
            res.status(200);
            res.body(new Gson().toJson(auth));
        }
        return res.body();
    }

}
