package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements ChessMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        moves.addAll(new BishopMovesCalculator().pieceMoves(board, position));
        moves.addAll(new RookMovesCalculator().pieceMoves(board, position));
        return moves;
    }

}
