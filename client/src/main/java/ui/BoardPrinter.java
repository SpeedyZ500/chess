package ui;

import chess.*;

import java.util.*;

import static ui.EscapeSequences.*;

public class BoardPrinter {

    private static final String[] FILES = {EMPTY, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", EMPTY};
    private ChessGame game;

    public BoardPrinter(){
        this.game = new ChessGame();
    }
    public ChessBoard getBoard(){
        return this.game.getBoard();
    }
    public void updateGame(ChessGame game){
        this.game = game;
    }

    public String print(){
        return print(ChessGame.TeamColor.WHITE);
    }

    public String print(ChessGame.TeamColor team){
        ChessBoard board = game.getBoard();
        return print(team, board);
    }
    public static String print(ChessBoard board){
        ChessGame.TeamColor team = ChessGame.TeamColor.WHITE;
        return print(team, board);
    }

    public String highlightValidMoves(ChessGame.TeamColor team, ChessPosition selected){
        Iterator<ChessMove> iter = game.validMoves(selected).iterator();
        List<ChessPosition> highlightPositions = new ArrayList<>();
        while(iter.hasNext()){
            ChessMove move = iter.next();
            highlightPositions.add(move.getEndPosition());
        }
        return print(team, game.getBoard(), selected, highlightPositions);
    }

    private static String print(ChessGame.TeamColor team, ChessBoard board,
                                ChessPosition selected, Collection<ChessPosition> highlightPositions){
        int direction = team == ChessGame.TeamColor.WHITE ? -1 : 1;
        int startingPosition = team == ChessGame.TeamColor.WHITE ? 9 : 0;

        StringBuilder output = new StringBuilder();
        output.append(RESET + SET_TEXT_BOLD);
        for(int rank = startingPosition; rank <= 9 && rank >= 0; rank += direction){
            output.append("\n");
            for(int file = startingPosition; file <= 9 && file >= 0; file += direction){
                if(rank == 9 || rank == 0 || file == 9 || file == 0){
                    output.append(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_YELLOW);
                    output.append(rank == 9 || rank == 0 ? FILES[file] : " " + rank + " ");
                }
                else{
                    ChessMove lastMove = board.getLastMove();
                    ChessPosition lastMoveStart = lastMove != null ? lastMove.getStartPosition() : null;
                    ChessPosition lastMoveEnd = lastMove != null ? lastMove.getStartPosition() : null;
                    ChessPosition positionCheck = new ChessPosition(rank,  9 - file);
                    if(positionCheck.equals(lastMoveStart) || positionCheck.equals(lastMoveEnd)){
                        output.append(SET_BG_COLOR_MAGENTA);
                    }
                    else if(positionCheck.equals(selected)){
                        output.append(SET_BG_COLOR_YELLOW);
                    }
                    else if(highlightPositions.contains(positionCheck)){
                        output.append(SET_BG_COLOR_GREEN);
                    }
                    else if((rank + file) % 2 == 0){
                        output.append(SET_BG_COLOR_LIGHT_GREY);
                    }
                    else{
                        output.append(SET_BG_COLOR_BLACK);
                    }
                    ChessPiece piece = board.getPiece(positionCheck);
                    output.append(displayPiece(piece));
                }
            }
            output.append(RESET_BG_COLOR);
        }
        output.append(RESET);
        return output.toString();
    }

    public static String print(ChessGame.TeamColor team, ChessBoard board){
        return print(team, board, null, Collections.emptyList());
    }

    private static String displayPiece(ChessPiece piece){
        if(piece == null){
            return EMPTY;
        }
        return switch(piece.getPieceType()){
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                    SET_TEXT_COLOR_WHITE + WHITE_KING :
                    SET_TEXT_COLOR_BLUE + BLACK_KING;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                    SET_TEXT_COLOR_WHITE + WHITE_PAWN :
                    SET_TEXT_COLOR_BLUE + BLACK_PAWN;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                    SET_TEXT_COLOR_WHITE + WHITE_ROOK :
                    SET_TEXT_COLOR_BLUE + BLACK_ROOK;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                    SET_TEXT_COLOR_WHITE + WHITE_BISHOP :
                    SET_TEXT_COLOR_BLUE + BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                    SET_TEXT_COLOR_WHITE + WHITE_KNIGHT :
                    SET_TEXT_COLOR_BLUE + BLACK_KNIGHT;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                    SET_TEXT_COLOR_WHITE + WHITE_QUEEN :
                    SET_TEXT_COLOR_BLUE + BLACK_QUEEN;
            default -> EMPTY;
        };
    }
}
