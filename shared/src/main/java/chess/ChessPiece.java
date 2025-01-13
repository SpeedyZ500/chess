package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        List<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        switch (piece.getPieceType()){
            case KING:

                    break;
            case PAWN:
                int direction = switch(piece.getTeamColor()){
                        case BLACK -> -1;
                        case WHITE -> 1;
                        default -> 0;
                    };
                break;
            case ROOK:

                    break;
            case QUEEN:

                    break;
            case BISHOP:

                    break;
            case KNIGHT:
                    int[] path =  {1, 2};
                    for(int i = 1; i <= 2; i++){
                        for(int j = -1; j <= 1; j += 2){
                            for(int k = -1; k <= 1; k += 2){
                                int qRow = row + (path[i%2] * j);
                                int qCol = col + (path[(i+1)%2] * k);
                                if(!(qRow < 1 || qCol < 1 || qRow > 8 || qCol > 8)){
                                    ChessPosition qPosition = new ChessPosition(qRow, qCol);
                                    ChessPiece checkPosition= board.getPiece(qPosition);
                                    if (checkPosition == null) {
                                        moves.add(new ChessMove(myPosition, qPosition, null));
                                    } else if (checkPosition.getTeamColor() != piece.getTeamColor()) {
                                        moves.add(new ChessMove(myPosition, qPosition, null));
                                    }
                                }
                            }
                        }
                    }

                    break;
            default:

                    break;
        }
        return moves;
        //throw new RuntimeException("Not implemented");
    }
}
