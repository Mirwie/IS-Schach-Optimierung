package isp.search.chess;

import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.BoardPosition;

public class LocalPlayer extends Player implements UserInputListener {
    public ChessGame chessGame;
    public PieceColor pieceColor;

    private BoardPosition selectedTile = null;

    private boolean hasMoved = false;
    private boolean isToMove = false;

    public LocalPlayer(ChessGame chessGame, PieceColor pieceColor) {
        this.chessGame = chessGame;
        this.pieceColor = pieceColor;

        //set user input listener
        chessGame.createUserBoardListener(this);
    }


    @Override
    public void onTilePressed(BoardPosition pressedTile) {
        GameState currentGameState = chessGame.getGameState();

        if (selectedTile != null && selectedTile.equals(pressedTile)) {
            selectedTile = null;
        } else { //if new tile pressed
            Piece pieceAtSelectedTile = currentGameState.getPieceAtPosition(selectedTile);
            if (pieceAtSelectedTile != null) {
                //move player
                //if (!currentGameState.isGameFinished()) {
                if (isToMove) {
                    //move
                    boolean moveSuccess = currentGameState.movePieceWithLegalCheck(pieceAtSelectedTile, pressedTile);

                    if (moveSuccess) {
                        //move ai
                        //if (!gameState.isGameFinished()) {
                        //    chessAI.move();
                        //}
                        hasMoved = true;
                        System.out.println("move success");

                        //deselect move indicator
                        selectedTile = null;

                        //} else {
                        //    selectedTile = pressedTile;
                        //}
                    } else {
                        selectedTile = pressedTile;
                    }
                }
            } else {
                selectedTile = pressedTile;
            }
        }

        //rerender
        chessGame.rerender();
    }

    @Override
    public void onMoveRequested(boolean withOutputs) {
        isToMove = true;

        while (!hasMoved) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        isToMove = false;
        hasMoved = false;
    }

    @Override
    public BoardPosition getSelectedTile() {
        return this.selectedTile;
    }
}
