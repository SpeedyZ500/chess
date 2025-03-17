package chess.pieces;

import chess.*;

import java.util.*;

public class PawnMovesCalculator implements ChessMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        int direction = 0;
        boolean start = false;
        int enPassantRow = 0;
        switch(piece.getTeamColor()){
            case BLACK:
                direction = -1;
                start = (position.getRow() == 7);
                enPassantRow = 4;
                break;
            case WHITE:
                direction = 1;
                start = (position.getRow() == 2);
                enPassantRow = 5;
                break;
            default:
                break;
        };
        moves.addAll(pawnForward(board, position, direction, start));
        moves.addAll(pawnCapture(board, position, direction, piece));
        moves.addAll(enPassant(board, position, direction, enPassantRow, piece));
        return moves;
    }

    private Collection<ChessMove> pawnPromotion(ChessPosition startPosition, ChessPosition endPosition){
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece.PieceType[] promotions = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
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

    /**
     * Checks to see if you can en Passant, and if so add that to possible moves list
     *
     * @param board
     * @param position
     * @param direction
     * @param enPassantRow
     * @param piece
     * @return a list of possible en Passant moves
     */
    private Collection<ChessMove> enPassant(ChessBoard board, ChessPosition position, int direction, int enPassantRow, ChessPiece piece){
        List<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessMove prevMove = board.getLastMove();

        if(row == enPassantRow && prevMove != null){
            for(int i = -1; i <= 1; i += 2){
                int checkCol = col + i;
                ChessPosition checkPosition = new ChessPosition(row, checkCol);
                ChessPiece checkPiece = !board.outOfBounds(checkPosition) ? board.getPiece(checkPosition) : null;

                if( checkPiece != null && (checkPiece.getPieceType() == piece.getPieceType() &&
                        checkPiece.getTeamColor() != piece.getTeamColor())) {
                    int checkRow = row + (direction * 2);

                    ChessPosition prevPosition = new ChessPosition(checkRow, checkCol);
                    ChessPosition endPosition = new ChessPosition(row + direction, checkCol);
                    ChessMove checkMove = new ChessMove(prevPosition, checkPosition, null);
                    ChessPiece pawnPosition = board.getPiece(checkPosition);
                    if(checkMove.equals(prevMove) &&
                            pawnPosition != null && pawnPosition.getPieceType() == ChessPiece.PieceType.PAWN
                            && pawnPosition.getTeamColor() != piece.getTeamColor() &&
                            (board.getPiece(endPosition) == null)
                    ) {
                        moves.add(new ChessMove(position, endPosition, null));
                    }
                }

            }
        }
        return moves;
    }
}


