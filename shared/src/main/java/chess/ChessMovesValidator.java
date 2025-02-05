package chess;

import java.util.*;

public class ChessMovesValidator {
    private ChessBoard board;
    ChessMovesValidator(ChessBoard board){
        this.board = board;
    }
    private static final int MAX_RECURSION = 3;
    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition, int level){
        ChessPiece piece = board.getPiece(startPosition);
        List<ChessMove> valid = new ArrayList<>();
        if(piece != null && level < MAX_RECURSION){
            ChessGame.TeamColor color = piece.getTeamColor();

            valid.addAll(piece.pieceMoves(board, startPosition));
            Set<ChessPosition> invalidDir = new HashSet<>();
            Iterator<ChessMove> iter = valid.iterator();
            int row = startPosition.getRow();
            int col = startPosition.getColumn();
            boolean check = new ChessCheckCalculator(board).isInCheck(color, level);
            while(iter.hasNext()){
                Iterator<Placement> boardIter = board.iterator();
                ChessBoard copyBoard = new ChessBoard();
                ChessMove move = iter.next();
                ChessPosition end = move.getEndPosition();
                while(boardIter.hasNext()){
                    Placement place = boardIter.next();
                    if(place.getPosition() != startPosition && place.getPosition() != end){
                        copyBoard.addPiece(place);
                    }
                }

                copyBoard.addPiece(end, move.getPromotionPiece() == null ? new ChessPiece(color, move.getPromotionPiece()) : piece);

                int endRow = end.getRow();
                int endCol = end.getColumn();
                int rankDiff = endRow - row;
                int fileDiff = endCol - col;
                ChessPosition dir = new ChessPosition(rankDiff != 0 ? rankDiff/Math.abs(rankDiff) : rankDiff, fileDiff != 0 ? fileDiff/Math.abs(fileDiff) : fileDiff);
                if(new ChessCheckCalculator(copyBoard).isInCheck(color, level + 1) || (!check && invalidDir.contains(dir))){
                    if(!check){
                        invalidDir.add(dir);
                    }
                    iter.remove();
                }
            }
        }
        return valid;
    }
    public Collection<ChessMove> validMoves( ChessPosition startPosition){
        return validMoves(startPosition, 0);
    }


    }
