package chess;

import java.util.Objects;

public class Placement {
    private ChessPosition position;
    private ChessPiece piece;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Placement placement = (Placement) o;
        return Objects.equals(position, placement.position) && Objects.equals(piece, placement.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, piece);
    }

    Placement(ChessPosition position, ChessPiece piece){
        this.position = position;
        this.piece = piece;
    }
    /**
      * gets the position of the placement
      *
      * @return the position of this placement
     */
    public ChessPosition getPosition(){
        return position;
    }

    /**
     * gets the ChessPiece of this position
     *
     * @return the piece at this placement
     */
    public ChessPiece getPiece(){
        return piece;
    }

    /**
     * promotes the piece at this placement
     * @param piece
     * (how we update the placement of a Pawn to a Queen/Rook/Bishop/Knight)
     */
    public void promotePiece(ChessPiece piece) {
        this.piece = piece;
    }

    /**
     * moves the placement to a new position
     *
     * @param position
     * (how we update the position of a piece, through the use of a placement class)
     */
    public void movePiece(ChessPosition position){
        this.position = position;
    }


}
