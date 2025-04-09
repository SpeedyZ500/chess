package ui;

import client.websocket.NotificationHandler;
import exception.ResponseException;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private Client client;
    public Repl(String serverUrl) {
        client = new PreloginClient(serverUrl, this);
    }

    public void run(){
        System.out.println(WHITE_KING + "Welcome to 240 Chess by Spencer Zaugg. type help to get started" + BLACK_KING);
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equalsIgnoreCase("quit")){
            printPrompt();
            String line = scanner.nextLine();
            result = client.eval(line);
            String[] splitResult = result.split(";;");
            System.out.print(splitResult[splitResult.length -1]);
            if(splitResult[0].trim().equalsIgnoreCase("transition")){
                if(splitResult.length >= 3){
                    try{
                        client = client.transition(splitResult[1].trim());
                    } catch (ResponseException e) {
                        ErrorMessage message = new ErrorMessage("Error: Failed to update client, " + e.getMessage());
                        warn(message);
                    }

                }
                else{
                    client = client.transition();
                }
            }

        }
    }
    private void printPrompt() {
        System.out.print("\n" + RESET + client.terminalState() + ">>> " + SET_TEXT_COLOR_GREEN);
    }


    @Override
    public void notify(NotificationMessage notification) {
        System.out.println("\n" + notification.getMessage());
        printPrompt();
    }

    @Override
    public void warn(ErrorMessage error){
        System.out.println("\n" + SET_TEXT_COLOR_RED + error.getErrorMessage());
        printPrompt();
    }

    @Override
    public void loadGame(LoadGameMessage game){
        System.out.print(client.loadGame(game.getGame()));
        printPrompt();
    }

}

