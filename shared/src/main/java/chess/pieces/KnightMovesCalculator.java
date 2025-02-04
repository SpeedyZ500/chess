package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator implements ChessMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        int[] path =  {-2, -1, 1, 2};
        for(int vert : path){
            int row = vert + position.getRow();
            for(int horiz : path){
                int col = horiz + position.getColumn();
                ChessPosition pos = new ChessPosition(row, col);
                if((Math.abs(vert)!= Math.abs(horiz)) && !board.outOfBounds(pos)){
                    ChessPiece checkPosition= board.getPiece(pos);
                    if (checkPosition == null || checkPosition.getTeamColor() != piece.getTeamColor()) {
                        moves.add(new ChessMove(position, pos, null));
                    }
                }
            }
        }
        return moves;
    }
}
