package isp.search.chess.ai;

import isp.search.chess.ChessGame;
import isp.search.chess.Player;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.BoardPosition;

public abstract class ChessAI extends Player {
    public ChessGame chessGame;
    public PieceColor pieceColor;

    public ChessAI(ChessGame chessGame, PieceColor pieceColor) {
        this.chessGame = chessGame;
        this.pieceColor = pieceColor;
    }

    abstract public void move();

    @Override
    public void onMoveRequested() {
        move();
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public BoardPosition getSelectedTile() {
        return null;
    }
}
