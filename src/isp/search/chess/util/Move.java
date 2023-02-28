package isp.search.chess.util;

import java.util.Objects;

public class Move {
    private BoardPosition oldBoardPosition;
    private BoardPosition newBoardPosition;
    private int moveNumber;

    public Move(BoardPosition oldBoardPosition, BoardPosition newBoardPosition) {
        this.oldBoardPosition = oldBoardPosition;
        this.newBoardPosition = newBoardPosition;
    }

    public Move() {

    }

    public BoardPosition getOldBoardPosition() {
        return oldBoardPosition;
    }

    public void setOldBoardPosition(BoardPosition oldBoardPosition) {
        this.oldBoardPosition = oldBoardPosition;
    }

    public BoardPosition getNewBoardPosition() {
        return newBoardPosition;
    }

    public void setNewBoardPosition(BoardPosition newBoardPosition) {
        this.newBoardPosition = newBoardPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(oldBoardPosition, move.oldBoardPosition) && Objects.equals(newBoardPosition, move.newBoardPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldBoardPosition, newBoardPosition);
    }

    @Override
    public String toString() {
        return "Move{" +
                "oldBoardPosition=" + oldBoardPosition +
                ", newBoardPosition=" + newBoardPosition +
                '}';
    }
}
