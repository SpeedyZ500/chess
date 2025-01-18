package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final Set<Placement> board;
    private final List<Set<Placement>> history;


    public ChessBoard() {
        this.board = new HashSet<>();
        this.history = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(board, that.board) && Objects.equals(history, that.history);
    }


    @Override
    public int hashCode() {
        return Objects.hash(board, history);
    }

    /**
     * Returns an iterator to remove pieces when captured, or moved
     * @return board.iterator() an iterator used to track piece positions
     */
    public Iterator<Placement> iterator(){
        return board.iterator();
    }

    /**
     * Returns an Iterator of the history of the board to be able to determine if it is a piece's first move
     *
     * @return history.iterator() to iterate through history
     */
    public Iterator<Set<Placement>> history(){
        return history.iterator();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if(outOfBounds(position)){
            throw new InvalidPositionException("Chess Piece out of bounds");
        }
        history.add(board);
        this.board.add(new Placement(position, piece));
    }

    public boolean outOfBounds(ChessPosition position){
        return position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(outOfBounds(position)){
            throw new InvalidPositionException("Chess Piece out of bounds");
        }
        ChessPiece piece = null;
        for (Placement place : board) {
            if(place.getPosition().equals(position) ){
                piece = place.getPiece();
                break;
            }
        }
        return piece;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board.clear();
        history.clear();
        resetBack(ChessGame.TeamColor.BLACK, 8);
        resetPawns(ChessGame.TeamColor.BLACK, 7);
        resetPawns(ChessGame.TeamColor.WHITE, 2);
        resetBack(ChessGame.TeamColor.WHITE, 1);
    }

    private void resetPawns(ChessGame.TeamColor color, int row){
        for(int i = 1; i <= 8; i++){
            addPiece(new ChessPosition(row, i), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }
    private void resetBack(ChessGame.TeamColor color, int row){
        for(int i = 1; i <= 8; i++){
            ChessPiece.PieceType type = switch (i) {
                case 1, 8 -> ChessPiece.PieceType.ROOK;
                case 2, 7 -> ChessPiece.PieceType.KNIGHT;
                case 3, 6 -> ChessPiece.PieceType.BISHOP;
                case 4 -> ChessPiece.PieceType.QUEEN;
                case 5 -> ChessPiece.PieceType.KING;
                default -> null;
            };
            addPiece(new ChessPosition(row, i), new ChessPiece(color, type));
        }
    }
}
