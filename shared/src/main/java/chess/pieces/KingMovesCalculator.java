package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
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
        return moves;
    }

}
