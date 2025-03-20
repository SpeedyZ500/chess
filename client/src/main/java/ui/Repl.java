package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private Client client;
    public Repl(String serverUrl) {
        client = new PreloginClient(serverUrl);
    }

    public void run(){
        System.out.println(WHITE_KING + "Welcome to 240 Chess by Spencer Zaugg. type help to get started" + BLACK_KING);
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equalsIgnoreCase("quit")){
            printPrompt();
            String line = scanner.nextLine();
            result = client.
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + client.terminalState() + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}

