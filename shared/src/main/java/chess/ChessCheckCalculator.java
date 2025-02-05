package chess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChessCheckCalculator {
    private final ChessMovesValidator movesValidator = new ChessMovesValidator();

    ChessCheckCalculator(){}
    /**
     * Determines if the given team is in check
     *
     * @param board the chess board
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamColor) {
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Iterator<Placement> iter = board.iterator();
        ChessPosition kingPosition = null;
        List<ChessMove> oppositeMoves = new ArrayList<>();
        while(iter.hasNext()){
            Placement current = iter.next();
            if(current.getPiece().getTeamColor() != teamColor){
                oppositeMoves.addAll(movesValidator.validMoves(board, current.getPosition()));
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

    /**
     * Determines if the given team is in checkmate
     *
     * @param board the chess board
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor teamColor) {

        return isInCheck(board, teamColor) && isInStalemate(board, teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param board the chess board
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor teamColor) {
        Iterator<Placement> iter = board.iterator();
        List<ChessMove> valid = new ArrayList<>();
        while(iter.hasNext()){
            Placement place = iter.next();
            if(place.getPiece().getTeamColor() == teamColor){
                valid.addAll(movesValidator.validMoves(board, place.getPosition()));
            }
        }
        return valid.isEmpty();
    }


}
