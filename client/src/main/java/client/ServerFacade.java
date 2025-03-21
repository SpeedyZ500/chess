package client;

import com.google.gson.Gson;
import exception.ResponseException;
import gson.GsonConfig;
import model.AuthData;
import model.GameData;

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
    private static final Gson GSON = GsonConfig.createGson();

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clear() throws ResponseException{
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        var req = Map.of("username", username, "password", password, "email", email);
        return this.makeRequest("POST", path, req, AuthData.class, null);
    }

    public AuthData login(String username, String password) throws ResponseException {
        var path = "/session";
        var req = Map.of("username", username, "password", password);

        return this.makeRequest("POST", path, req, AuthData.class, null);
    }

    public void logout(String authToken) throws ResponseException{
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public GameData[] listGames(String authToken) throws ResponseException {
        var path = "/game";
        record ListGameResponse(GameData[] games){
        }
        var response = this.makeRequest("GET", path, null, ListGameResponse.class, authToken);
        return response.games();
    }

    public int createGame(String gameName, String authToken) throws ResponseException{
        var path = "/game";
        record GameID (int gameID){}
        var requestBody = Map.of("gameName", gameName);
        var response = this.makeRequest("POST", path, requestBody, GameID.class, authToken);
        return response.gameID();
    }


    public void joinGame(String playerColor, int gameId, String authToken) throws ResponseException {
        var request = Map.of("playerColor", playerColor, "gameID", gameId);
        var path = "/game";
        this.makeRequest("PUT", path, request, null,  authToken);
    }


    private <T> T makeRequest(
            String method,
            String path,
            Object request,
            Class<T> responseClass,
            String authToken
    ) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null ){
                http.setRequestProperty("Authorization", authToken);
            }
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
                    response = GSON.fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
