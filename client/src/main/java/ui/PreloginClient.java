package ui;

import server.ServerFacade;

public class PreloginClient implements Client {
    private final String serverUrl;
    private final ServerFacade server;
    public PreloginClient(String serverUrl){
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
    }
    public PreloginClient(String serverUrl, ServerFacade server){
        this.serverUrl = serverUrl;
        this.server = server;
    }
    @Override
    public String help() {
        return "";
    }

    @Override
    public Client transition(String token) {
        String[] parsed = token.split(",");
        return new PostloginClient(serverUrl, server, parsed[0], parsed[1]);
    }

    @Override
    public Client transition() {
        return this;
    }
}
