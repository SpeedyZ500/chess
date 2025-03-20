package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clear() throws ResponseException{
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public AuthData register(UserData userData) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, userData, AuthData.class);
    }

    public AuthData login(UserData userData) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, userData, AuthData.class);
    }

    public void logout() throws ResponseException{
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public GameData[] listGames() throws ResponseException {
        var path = "/game";
        record listGameResponse(GameData[] game){
        }
        var response = this.makeRequest("GET", path, null, listGameResponse.class);
        return response.game();
    }

    public int createGame(String gameName) throws ResponseException{
        var path = "/game";
        record gameID (int gameID){};
        var requestBody = Map.of("name", gameName);
        var response = this.makeRequest("POST", path, requestBody, gameID.class);
        return response.gameID();
    }


    public void joinGame(String playerColor, int gameId) throws ResponseException {
        var request = Map.of("playerColor", playerColor, "gameID", gameId);
        var path = "/game";
        this.makeRequest("PUT", path, request, null);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
