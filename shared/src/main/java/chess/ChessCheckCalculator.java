package chess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChessCheckCalculator {
    private ChessBoard board;
    ChessCheckCalculator(ChessBoard board){
        this.board = board;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(ChessGame.TeamColor teamColor, int level) {
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Iterator<Placement> iter = board.iterator();
        ChessPosition kingPosition = null;
        List<ChessMove> oppositeMoves = new ArrayList<>();
        while(iter.hasNext()){
            Placement current = iter.next();
            if(current.getPiece().getTeamColor() != teamColor){
                oppositeMoves.addAll(new ChessMovesValidator(board).validMoves(current.getPosition(), level));
            }
            else if(current.getPiece().equals(king)){
                kingPosition = current.getPosition();
            }
        }
        Iterator<ChessMove> moveIter = oppositeMoves.iterator();
        boolean inCheck = false;
        while(moveIter.hasNext()){
            ChessMove move = moveIter.next();
            if(move.getEndPosition().equals(kingPosition)){
                inCheck = true;
                break;
            }
        }
        return inCheck;
    }

    public boolean isInCheck(ChessGame.TeamColor teamColor) {
        return isInCheck(teamColor, 0);
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(ChessGame.TeamColor teamColor) {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(ChessGame.TeamColor teamColor, int level) {
        Iterator<Placement> iter = board.iterator();
        List<ChessMove> valid = new ArrayList<>();
        while(iter.hasNext()){
            Placement place = iter.next();
            if(place.getPiece().getTeamColor() == teamColor){
                valid.addAll(new ChessMovesValidator(board).validMoves(place.getPosition(), level));
            }
        }
        return valid.isEmpty();
    }
    public boolean isInStalemate(ChessGame.TeamColor teamColor) {
        return isInStalemate(teamColor, 0);
    }



}
