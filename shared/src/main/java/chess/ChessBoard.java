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
    private final List<ChessBoard> history;




    public ChessBoard() {
        this.board = new HashSet<>();
        this.history = new ArrayList<>();
    }
    public ChessBoard(Set<Placement> board){
        this.board = board;
        this.history = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(board, that.board) /*&& Objects.equals(history, that.history)*/;
    }


    @Override
    public int hashCode() {
        return Objects.hash(board/*, history*/);
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
    public Iterator<ChessBoard> history(){
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
        this.board.add(new Placement(position, piece));
    }

    public void addPiece(Placement place){
        addPiece(place.getPosition(), place.getPiece());
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
        resetBack(ChessGame.TeamColor.WHITE, 1);
        resetPawns(ChessGame.TeamColor.WHITE, 2);
        resetPawns(ChessGame.TeamColor.BLACK, 7);
        resetBack(ChessGame.TeamColor.BLACK, 8);
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

    /**
     * Moves Pieces on the board
     * checks to see if there was a piece at the original position
     * and then removes it
     * if the chess piece is a pawn, and it moved diagonally (changed file)
     * but there was no piece to capture en Passant must have occurred
     * so remove the piece below it
     * if the king did not take a piece, and the absolute delta file was 2
     * that is castling, move the rook that is from the same direction as the king moved
     * to the other side of the king
     * If the promotion isn't null, promote the piece
     * @param startPosition
     * @param endPosition
     * @param promotion
     */
    public void movePiece(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotion){
        history.add(new ChessBoard(board));
        ChessPiece thisPiece = getPiece(startPosition);
        ChessPiece endPiece = getPiece(endPosition);

        int endCol = endPosition.getColumn();
        int startCol = startPosition.getColumn();
        int file = endCol - startCol;
        int startRow = startPosition.getRow();
        if(endPiece != null){
            board.remove(new Placement(endPosition, endPiece));
        } else if (thisPiece.getPieceType() == ChessPiece.PieceType.PAWN && startCol != endCol) {
            ChessPosition checkPosition = new ChessPosition(startRow, endCol);
            ChessPiece checkPiece = getPiece(checkPosition);
            if(checkPiece != null){
                board.remove(new Placement(checkPosition, checkPiece));
            }
        }else if(thisPiece.getPieceType() == ChessPiece.PieceType.KING && Math.abs(file) == 2){
            int dir = file/Math.abs(file);
            int rookCol = endCol - dir;
            ChessPosition rookPosition = new ChessPosition(startRow, dir > 0 ? 8 : 1);
            ChessPiece rook = getPiece(rookPosition);
            if(rook != null) {
                board.remove(new Placement(rookPosition, rook));
                board.add(new Placement(new ChessPosition(startRow, rookCol), rook));
            }
        }
        if(promotion != null){
            board.add(new Placement(endPosition, new ChessPiece(thisPiece.getTeamColor(), promotion)));
        }else{
            board.add(new Placement(endPosition, thisPiece));
        }
        board.remove(new Placement(startPosition, thisPiece));
    }
    public void movePiece(ChessMove move){
        movePiece(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
    }

    }
