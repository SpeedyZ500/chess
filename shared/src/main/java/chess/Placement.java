package chess;

import java.util.Objects;

public class Placement {
    private final ChessPosition position;
    private final ChessPiece piece;


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

    @Override
    public String toString() {
        return "Placement{" +
                "position=" + position +
                ", piece=" + piece +
                '}';
    }
}
