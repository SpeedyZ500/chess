package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class KingMovesCalculator implements ChessMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                int row = position.getRow() + i;
                int col = position.getColumn() + j;
                ChessPosition nextPosition = new ChessPosition(row, col);
                if(!board.outOfBounds(nextPosition)){
                    ChessPiece checkPosition = board.getPiece(nextPosition);
                    if(checkPosition == null || checkPosition.getTeamColor() != piece.getTeamColor()){
                        moves.add(new ChessMove(position, nextPosition, null));
                    }
                }
            }
        }
        moves.addAll(castleMove(board, position));
        return moves;
    }

    private Collection<ChessMove> castleMove(ChessBoard board, ChessPosition position){
        List<ChessMove> moves = new ArrayList<>();
        int[] rookStartCols = {1, 8};
        ChessPiece king = board.getPiece(position);
        ChessPosition kingStart = new ChessPosition(king.getTeamColor() == ChessGame.TeamColor.WHITE ? 1: 8, 5);
        for(int rookCol : rookStartCols){
            ChessPosition rookPosition = new ChessPosition(position.getRow(), rookCol);
            ChessPiece rook = board.getPiece(rookPosition);
            if(position.equals(kingStart) &&
                    rook != null && rook.getPieceType() == ChessPiece.PieceType.ROOK &&
                    rook.getTeamColor() == king.getTeamColor()){
                int row = kingStart.getRow();
                int col = kingStart.getColumn();
                int dir = rookCol > col?  1 : -1;
                ChessPosition endPosition = new ChessPosition(row, col + (2*dir));
                boolean pathOpen = true;
                for(int i = col + dir; i != rookCol; i += dir){
                    if(board.getPiece(new ChessPosition(row, i)) != null){
                        pathOpen = false;
                        break;
                    }
                }
                if(!pathOpen){
                    continue;
                }
                boolean rookMoved = false;
                boolean kingMoved = false;
                Iterator<ChessBoard> hist = board.history();
                while(hist.hasNext()){
                    ChessBoard check = hist.next();
                    ChessPiece checkRook = check.getPiece(rookPosition);
                    ChessPiece checkKing = check.getPiece(kingStart);
                    if(!king.equals(checkKing)){
                        kingMoved = true;
                    }
                    if(!rook.equals(checkRook)){
                        rookMoved = true;

                    }
                }
                if(kingMoved){
                    break;
                }
                if(rookMoved){
                    continue;
                }
                moves.add(new ChessMove(position, endPosition, null));
            }
        }
        return moves;
    }
}
