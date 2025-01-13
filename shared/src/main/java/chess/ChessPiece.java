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
        PieceType[] possiblePromotions = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};
        int qRow;
        int qCol;
        ChessPosition qPosition;
        ChessPiece checkPosition;
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        switch (piece.getPieceType()){
            case KING:

                    break;
            case PAWN:
                int direction = 0;
                boolean start = false;
                int enPassantRow = 0;
                switch(piece.getTeamColor()){
                    case BLACK:
                        direction = -1;
                        start = (row == 7);
                        enPassantRow = 4;
                        break;
                    case WHITE:
                        direction = 1;
                        start = (row == 2);
                        enPassantRow = 5;
                        break;
                    default:
                        break;
                    };
                qRow = row + direction;
                if(qRow >= 1 && qRow <= 8){
                    for(int i = -1; i <= 1; i++){
                        qCol = col + i;
                        if(qCol >= 1 && qCol <= 8){
                            qPosition = new ChessPosition(qRow, qCol);
                            checkPosition = board.getPiece(qPosition);
                            if(checkPosition == null && qCol == col){
                                if(qRow == 1 || qRow == 8){for (PieceType possiblePromotion : possiblePromotions) {moves.add(new ChessMove(myPosition, qPosition, possiblePromotion));}}
                                else{
                                    moves.add(new ChessMove(myPosition, qPosition, null));
                                    if(start){
                                        qPosition = new ChessPosition(qRow+direction, qCol);
                                        checkPosition = board.getPiece(qPosition);
                                        if(checkPosition == null){moves.add(new ChessMove(myPosition, qPosition, null));}
                                    }
                                }
                            }
                            /*
                            some settup for en Passant
                            else if(checkPosition == null && row == enPassantRow && qCol != col){
                                if(board.getPiece(ChessPosition()))
                            }*/
                            else if(checkPosition != null && qCol != col){
                                if(checkPosition.getTeamColor() != piece.getTeamColor()){
                                    if(qRow == 1 || qRow == 8){
                                        for (PieceType possiblePromotion : possiblePromotions) {moves.add(new ChessMove(myPosition, qPosition, possiblePromotion));}
                                    }
                                    else{moves.add(new ChessMove(myPosition, qPosition, null));}
                                }
                            }

                        }
                    }

                }

                break;
            case ROOK, QUEEN:

                if (piece.getPieceType() == PieceType.ROOK){ break;}
            case BISHOP:

                for(int i = -1; i <= 1; i += 2){
                    qRow = row;
                    qCol = col;
                    for(int j = -1; j <= 1; j += 2){
                        qRow = row + i;
                        qCol = col + j;
                        while(qRow >= 1 && qRow <= 8 && qCol >= 1 && qCol <= 8 ) {
                            qPosition = new ChessPosition(qRow, qCol);
                            checkPosition = board.getPiece(qPosition);
                            if(checkPosition == null){moves.add(new ChessMove(myPosition, qPosition, null));}
                            else if(checkPosition.getTeamColor() != piece.getTeamColor()){
                                moves.add(new ChessMove(myPosition, qPosition, null));
                                break;
                            }
                            else{break;}
                            qRow += i;
                            qCol += j;
                        }
                    }
                }
                break;
            case KNIGHT:
                    int[] path =  {1, 2};
                    for(int i = 1; i <= 2; i++){
                        for(int j = -1; j <= 1; j += 2){
                            for(int k = -1; k <= 1; k += 2){
                                qRow = row + (path[i%2] * j);
                                qCol = col + (path[(i+1)%2] * k);
                                if(!(qRow < 1 || qCol < 1 || qRow > 8 || qCol > 8)){
                                    qPosition = new ChessPosition(qRow, qCol);
                                    checkPosition= board.getPiece(qPosition);
                                    if (checkPosition == null) {moves.add(new ChessMove(myPosition, qPosition, null));}
                                    else if (checkPosition.getTeamColor() != piece.getTeamColor()) {moves.add(new ChessMove(myPosition, qPosition, null));}
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
