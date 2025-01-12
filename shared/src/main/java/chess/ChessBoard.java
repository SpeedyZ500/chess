package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;


    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessBoard(int rows, int cols){
        this.board = new ChessPiece[rows][cols];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        if(row > 7 || col > 7 ||row < 0 || col < 0){
            throw new InvalidPositionException("Chess Piece out of bounds");
        }
        this.board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        if(row > 7 || col > 7 ||row < 0 || col < 0){
            throw new InvalidPositionException("Chess Piece out of bounds");
        }
        return board[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for(int i = 1; i <= 8; i++){
            ChessGame.TeamColor color = switch(i){
                case 1,2 -> ChessGame.TeamColor.WHITE;
                case 7,8 -> ChessGame.TeamColor.BLACK;
                default -> null;
            };
            for(int j = 1; j <= 8; j++) {
                if(color != null){
                    ChessPiece.PieceType type = switch (i) {
                        case 2, 7 -> ChessPiece.PieceType.PAWN;
                        default -> switch (j) {
                            case 1, 8 -> ChessPiece.PieceType.ROOK;
                            case 2, 7 -> ChessPiece.PieceType.KNIGHT;
                            case 3, 6 -> ChessPiece.PieceType.BISHOP;
                            case 4 -> ChessPiece.PieceType.QUEEN;
                            case 5 -> ChessPiece.PieceType.KING;
                            default -> null;
                        };
                    };
                    addPiece(new ChessPosition(i, j), new ChessPiece(color, type));
                }else{
                    addPiece(new ChessPosition(i, j), null);
                }
            }
        }
    }
}
