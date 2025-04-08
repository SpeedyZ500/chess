package ui;

import exception.ResponseException;

public interface Client {
    String help();
    Client transition(String token) throws ResponseException;
    Client transition();
    String terminalState();
    String eval(String input);




}
