package isp.search.chess;

import isp.search.chess.util.BoardPosition;

public abstract class Player {

    public abstract void onMoveRequested(boolean withOutputs);

    abstract public BoardPosition getSelectedTile();
}
