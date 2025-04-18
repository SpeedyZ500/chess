package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board = new ChessBoard();
    private boolean gameOver;


    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.board.resetBoard();
        this.gameOver = false;
    }
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        List<ChessMove> valid = new ArrayList<>();
        if(piece != null){
           valid.addAll(new ChessMovesValidator(board).validMoves(startPosition));

        }
        return valid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPiece piece = board.getPiece(start);
        List<ChessMove> valid = new ArrayList<>(validMoves(start));
        if(!valid.contains(move) || piece == null){
            throw new InvalidMoveException(String.format("Error: From %s to %s is not a valid move",
                    move.getStartPosition().prettyOutput(), move.getEndPosition().prettyOutput()));
        }
        else if(piece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException(String.format("Error: it is %s team's turn", getTeamTurn()));
        }
        board.movePiece(move);
        if(turn == TeamColor.WHITE){
            turn = TeamColor.BLACK;
        }
        else if (turn == TeamColor.BLACK){
            turn = TeamColor.WHITE;

        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return new ChessCheckCalculator(board).isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = new ChessCheckCalculator(board).isInCheckmate(teamColor);
        if(checkmate){
            gameOver = true;
        }
        return checkmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = new ChessCheckCalculator(board).isInStalemate(teamColor);
        if(stalemate){
            gameOver = true;
        }
        return stalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        this.board = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "turn=" + turn +
                ", board=" + board +
                ", gameOver=" + gameOver +
                '}';
    }
}
