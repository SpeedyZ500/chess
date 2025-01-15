package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        int direction = 0;
        boolean start = false;
        //int enPassantRow = 0;
        switch(piece.getTeamColor()){
            case BLACK:
                direction = -1;
                start = (position.getRow() == 7);
                //enPassantRow = 4;
                break;
            case WHITE:
                direction = 1;
                start = (position.getRow() == 2);
                //enPassantRow = 5;
                break;
            default:
                break;
        };
        moves.addAll(pawnForward(board, position, direction, start));
        moves.addAll(pawnCapture(board, position, direction, piece));

        return moves;
    }

    private Collection<ChessMove> pawnPromotion(ChessPosition startPosition, ChessPosition endPosition){
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece.PieceType[] promotions = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
        for(var promotion : promotions) {moves.add(new ChessMove(startPosition, endPosition, promotion));}
        return moves;
    }
    private Collection<ChessMove> pawnForward(ChessBoard board, ChessPosition position, int direction, boolean start){
        List<ChessMove> moves = new ArrayList<>();
        int row = position.getRow() + direction;
        int col = position.getColumn();
        ChessPosition nextPosition = new ChessPosition(row, col);
        if(!board.outOfBounds(nextPosition)){
            ChessPiece checkPosition = board.getPiece(nextPosition);
            if(checkPosition == null){
                if (row == 8 || row == 1){
                    moves.addAll(this.pawnPromotion(position,nextPosition));
                }
                else{
                    moves.add(new ChessMove(position, nextPosition, null));
                    if (start){
                        moves.addAll(this.pawnForward(board, position, direction + direction, false));
                    }
                }
            }

        }
        return moves;
    }
    private Collection<ChessMove> pawnCapture(ChessBoard board, ChessPosition position, int direction, ChessPiece piece){
        List<ChessMove> moves = new ArrayList<>();
        int row = position.getRow() + direction;
        for(int i = -1; i <= 1; i += 2){
            int col = position.getColumn() + i;
            ChessPosition nextPosition = new ChessPosition(row, col);
            if(!board.outOfBounds(nextPosition)){
                ChessPiece checkPosition = board.getPiece(nextPosition);
                if(checkPosition != null && checkPosition.getTeamColor() != piece.getTeamColor()){
                    if (row == 8 || row == 1){
                        moves.addAll(this.pawnPromotion(position,nextPosition));
                    }
                    else {
                        moves.add(new ChessMove(position, nextPosition, null));
                    }
                }
            }
        }
        return moves;
    }
}


