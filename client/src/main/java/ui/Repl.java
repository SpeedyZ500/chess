package ui;

public class Repl {
    private Client client;
    public Repl(String serverUrl) {
        client = new PreloginClient(serverUrl);
    }
}
