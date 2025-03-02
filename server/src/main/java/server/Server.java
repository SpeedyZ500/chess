package server;

import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.UserDAO;
import service.ClearService;
import service.GameService;
import service.ResponseException;
import service.UserService;
import spark.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static spark.Spark.halt;

public class Server {
    private final UserDAO userDAO= new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO);
    private final UserHandler userHandler = new UserHandler(userService);







    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.before("/session", this::filter);
        Spark.before("/game", this::filter);
        Spark.delete("/session", userHandler::logout);
        Spark.exception(ResponseException.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint



        Spark.awaitInitialization();
        return Spark.port();
    }
    private void filter(Request req, Response res) throws ResponseException{
        String path = req.pathInfo();
        String method = req.requestMethod().toLowerCase();
        if(!path.equals("/session") || !method.equals("post") ){
            String header = req.headers("authorization");
            if(!userService.verifyToken(header) || header == null){
                throw new ResponseException(401, "Error: unauthorized");
            }
        }

    }

    private void exceptionHandler(ResponseException ex, Request req, Response res){
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }

    private Object clear(Request req, Response res) throws ResponseException {
        clearService.clear();
        res.status(200);
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
