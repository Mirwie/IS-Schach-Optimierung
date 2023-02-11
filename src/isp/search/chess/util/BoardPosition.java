package isp.search.chess.util;

import java.util.Objects;

public class BoardPosition {
    private int boardX, boardY;

    public BoardPosition(int boardX, int boardY) {
        this.boardX = boardX;
        this.boardY = boardY;
    }

    public int getBoardX() {
        return this.boardX;
    }

    public void setBoardX(int boardX) {
        this.boardX = boardX;
    }

    public int getBoardY() {
        return this.boardY;
    }

    public void setBoardY(int boardY) {
        this.boardY = boardY;
    }

    public void setPosition(int boardX, int boardY) {
        this.boardX = boardX;
        this.boardY = boardY;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardPosition boardPosition)) {
            return false;
        }
        return boardX == boardPosition.boardX && boardY == boardPosition.boardY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardX, boardY);
    }

    @Override
    public String toString() {
        return ((char) (getBoardX() + 'a')) + "" + (getBoardY() + 1);
    }


}
