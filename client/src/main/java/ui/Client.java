package ui;

import chess.ChessGame;
import exception.ResponseException;

public interface Client {
    String help();
    Client transition(String token) throws ResponseException;
    Client transition();
    String terminalState();
    String eval(String input);
    String loadGame(ChessGame game);



}
