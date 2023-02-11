package isp.search.chess;


import isp.search.chess.enums.PieceColor;
import isp.search.chess.enums.PieceType;
import isp.search.chess.util.BoardPosition;

public class Piece {
    private BoardPosition boardPosition;
    private PieceType pieceType;
    private final PieceColor pieceColor;


    public Piece(BoardPosition boardPosition, PieceType pieceType, PieceColor pieceColor) {
        this.boardPosition = boardPosition;
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
    }


    public BoardPosition getBoardPosition() {
        return boardPosition;
    }

    public void setBoardPosition(BoardPosition boardPosition) {
        this.boardPosition = boardPosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public PieceColor getPieceColor() {
        return this.pieceColor;
    }


    public int getBoardX() {
        return boardPosition.getBoardX();
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public int getBoardY() {
        return boardPosition.getBoardY();
    }

    @Override
    public String toString() {
        return "Piece{" +
                "boardPosition=" + boardPosition +
                ", pieceType=" + pieceType +
                ", pieceColor=" + pieceColor +
                '}';
    }
}
