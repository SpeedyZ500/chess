package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        moves.addAll(diagonalMovement(board, position));
        moves.addAll(straightMovement(board, position));
        return moves;
    }
    private Collection<ChessMove> diagonalMovement(ChessBoard board, ChessPosition position){
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        for(int i = -1; i <= 1; i += 2){
            for(int j = -1; j <= 1; j += 2){
                int row = position.getRow() + i;
                int col = position.getColumn() + j;
                ChessPosition nextPosition = new ChessPosition(row, col);
                while(!board.outOfBounds(nextPosition)) {
                    nextPosition = new ChessPosition(row, col);
                    ChessPiece checkPosition = board.getPiece(nextPosition);
                    if(checkPosition == null){moves.add(new ChessMove(position, nextPosition, null));}
                    else if(checkPosition.getTeamColor() != piece.getTeamColor()){
                        moves.add(new ChessMove(position, nextPosition, null));
                        break;
                    }
                    else{break;}
                    row += i;
                    col += j;
                    nextPosition = new ChessPosition(row, col);
                }
            }
        }
        return moves;
    }
    private Collection<ChessMove> straightMovement(ChessBoard board, ChessPosition position){
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        for(int i = -1; i <= 1; i += 2){
            int qRow = position.getRow() + i;
            int qCol = position.getColumn() + i;
            ChessPosition nextPosition = new ChessPosition(qRow, col);
            while(!board.outOfBounds(nextPosition)){
                ChessPiece checkPosition = board.getPiece(nextPosition);
                if(checkPosition == null){moves.add(
                        new ChessMove(position, nextPosition, null));
                }
                else if(checkPosition.getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(position, nextPosition, null));
                    break;
                }
                else{
                    break;
                }
                qRow += i;
                nextPosition = new ChessPosition(qRow, col);
            }
            nextPosition = new ChessPosition(row, qCol);
            while(!board.outOfBounds(nextPosition)){

                ChessPiece checkPosition = board.getPiece(nextPosition);
                if(checkPosition == null){
                    moves.add(new ChessMove(position, nextPosition, null));
                }
                else if(checkPosition.getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(position, nextPosition, null));
                    break;
                }
                else{
                    break;
                }
                qCol += i;
                nextPosition = new ChessPosition(row, qCol);
            }
        }
        return moves;
    }
}
