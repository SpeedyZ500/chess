package ui;

public interface Client {
    String help();
    Client transition(String token);
    Client transition();
    String terminalState();
    String eval(String input);




}
