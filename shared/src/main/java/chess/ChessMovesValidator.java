package chess;

import java.util.*;

public class ChessMovesValidator {
    private ChessCheckCalculator checkCalculator = new ChessCheckCalculator();
    ChessMovesValidator(){}
    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param board the chess board
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition){
        ChessPiece piece = board.getPiece(startPosition);
        List<ChessMove> valid = null;
        if(piece != null){
            ChessGame.TeamColor color = piece.getTeamColor();
            valid = new ArrayList<>();
            valid.addAll(piece.pieceMoves(board, startPosition));
            Set<ChessPosition> invalidDir = new HashSet<>();
            Iterator<ChessMove> iter = valid.iterator();
            int row = startPosition.getRow();
            int col = startPosition.getColumn();
            while(iter.hasNext()){
                ChessBoard boardCopy = board;
                ChessMove move = iter.next();
                boardCopy.movePiece(move);
                ChessPosition end = move.getEndPosition();
                int endRow = end.getRow();
                int endCol = end.getColumn();
                int rankDiff = endRow - row;
                int fileDiff = endCol - col;
                ChessPosition dir = new ChessPosition(rankDiff/Math.abs(rankDiff), fileDiff/Math.abs(fileDiff));
                if(checkCalculator.isInCheck(boardCopy, color) || invalidDir.contains(dir)){
                    invalidDir.add(dir);
                    iter.remove();
                }
            }
        }
        return valid;
    }

}
