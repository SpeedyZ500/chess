package chess;

import chess.pieces.KingMovesCalculator;

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
        switch (piece.getPieceType()){
            case KING -> moves.addAll(new KingMovesCalculator().pieceMoves(board, myPosition));
            case PAWN -> moves.addAll(this.pawnMoves(board, myPosition, piece));
            case ROOK -> moves.addAll(this.rookMoves(board, myPosition, piece));
            case BISHOP -> moves.addAll(this.bishopMoves(board,myPosition, piece));
            case KNIGHT -> moves.addAll(this.knightMoves(board, myPosition, piece));
            case QUEEN -> {
                moves.addAll(this.bishopMoves(board,myPosition, piece));
                moves.addAll(this.rookMoves(board, myPosition, piece));
            }

        }
        return moves;
        //throw new RuntimeException("Not implemented");
    }



    private Collection<ChessMove> knightMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> moves = new ArrayList<>();
        int[] path =  {-2, -1, 1, 2};
        for(int vert : path){
            int row = vert + myPosition.getRow();
            for(int horiz : path){
                int col = horiz + myPosition.getColumn();
                ChessPosition pos = new ChessPosition(row, col);
                if((Math.abs(vert)!= Math.abs(horiz)) && !board.outOfBounds(pos)){
                    ChessPiece checkPosition= board.getPiece(pos);
                    if (checkPosition == null || checkPosition.getTeamColor() != piece.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, pos, null));
                    }
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> moves = new ArrayList<>();
        int direction = 0;
        boolean start = false;
        //int enPassantRow = 0;
        switch(piece.getTeamColor()){
            case BLACK:
                direction = -1;
                start = (myPosition.getRow() == 7);
                //enPassantRow = 4;
                break;
            case WHITE:
                direction = 1;
                start = (myPosition.getRow() == 2);
                //enPassantRow = 5;
                break;
            default:
                break;
        };
        moves.addAll(pawnForward(board, myPosition, direction, start));
        moves.addAll(pawnCapture(board, myPosition, direction, piece));

        return moves;
    }

    private Collection<ChessMove> pawnPromotion(ChessPosition startPosition, ChessPosition endPosition){
        List<ChessMove> moves = new ArrayList<>();
        PieceType[] promotions = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};
        for(var promotion : promotions) {moves.add(new ChessMove(startPosition, endPosition, promotion));}
        return moves;
    }
    private Collection<ChessMove> pawnForward(ChessBoard board, ChessPosition myPosition, int direction, boolean start){
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow() + direction;
        int col = myPosition.getColumn();
        ChessPosition position = new ChessPosition(row, col);
        if(!board.outOfBounds(position)){
            ChessPiece checkPosition = board.getPiece(position);
            if(checkPosition == null){
                if (row == 8 || row == 1){
                    moves.addAll(this.pawnPromotion(myPosition,position));
                }
                else{
                    moves.add(new ChessMove(myPosition, position, null));
                    if (start){
                        moves.addAll(this.pawnForward(board, myPosition, direction + direction, false));
                    }
                }
            }

        }
        return moves;
    }
    private Collection<ChessMove> pawnCapture(ChessBoard board, ChessPosition myPosition, int direction, ChessPiece piece){
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow() + direction;
        for(int i = -1; i <= 1; i += 2){
            int col = myPosition.getColumn() + i;
            ChessPosition position = new ChessPosition(row, col);
            if(!board.outOfBounds(position)){
                ChessPiece checkPosition = board.getPiece(position);
                if(checkPosition != null && checkPosition.getTeamColor() != piece.getTeamColor()){
                    if (row == 8 || row == 1){
                        moves.addAll(this.pawnPromotion(myPosition,position));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, position, null));
                    }
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> rookMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for(int i = -1; i <= 1; i += 2){
            int qRow = myPosition.getRow() + i;
            int qCol = myPosition.getColumn() + i;
            ChessPosition position = new ChessPosition(qRow, col);
            while(!board.outOfBounds(position)){
                ChessPiece checkPosition = board.getPiece(position);
                if(checkPosition == null){moves.add(
                        new ChessMove(myPosition, position, null));
                }
                else if(checkPosition.getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, position, null));
                    break;
                }
                else{
                    break;
                }
                qRow += i;
                position = new ChessPosition(qRow, col);
            }
            position = new ChessPosition(row, qCol);
            while(!board.outOfBounds(position)){

                ChessPiece checkPosition = board.getPiece(position);
                if(checkPosition == null){
                    moves.add(new ChessMove(myPosition, position, null));
                }
                else if(checkPosition.getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(myPosition, position, null));
                    break;
                }
                else{
                    break;
                }
                qCol += i;
                position = new ChessPosition(row, qCol);
            }
        }
        return moves;
    }
    private Collection<ChessMove> bishopMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> moves = new ArrayList<>();
        for(int i = -1; i <= 1; i += 2){
            for(int j = -1; j <= 1; j += 2){
                int row = myPosition.getRow() + i;
                int col = myPosition.getColumn() + j;
                ChessPosition position = new ChessPosition(row, col);
                while(!board.outOfBounds(position)) {
                    position = new ChessPosition(row, col);
                    ChessPiece checkPosition = board.getPiece(position);
                    if(checkPosition == null){moves.add(new ChessMove(myPosition, position, null));}
                    else if(checkPosition.getTeamColor() != piece.getTeamColor()){
                        moves.add(new ChessMove(myPosition, position, null));
                        break;
                    }
                    else{break;}
                    row += i;
                    col += j;
                    position = new ChessPosition(row, col);
                }
            }
        }
        return moves;
    }
}
