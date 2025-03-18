package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.UserDAO;
import gson.GsonConfig;
import service.ClearService;
import service.GameService;
import service.ResponseException;
import service.UserService;
import spark.*;

import static spark.Spark.halt;

public class Server {

    private final ClearService clearService;
    private final UserService userService;
    private final UserHandler userHandler;
    private final GameHandler gameHandler;

    public Server(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        try{
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
        }
        catch(DataAccessException e){
            userDAO = new MemoryUserDAO();
            authDAO = new MemoryAuthDAO();
            gameDAO = new MemoryGameDAO();
        }

        clearService = new ClearService(authDAO, userDAO, gameDAO);
        userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO);
        userHandler = new UserHandler(userService);
        gameHandler = new GameHandler(userService, gameService);
    }




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
        Spark.get("/game", gameHandler::listGames);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);
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
        res.status(ex.statusCode());
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
