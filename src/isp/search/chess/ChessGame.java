package isp.search.chess;

import isp.search.chess.enums.PieceColor;
import isp.search.chess.gui.SingleplayerFrame;
import isp.search.chess.util.FenLoader;

public class ChessGame {
    private final GameState gameState;

    private final SingleplayerFrame singleplayerFrame;


    private Player playerWhite, playerBlack;

    public ChessGame(String fenString) {

        //load game state
        this.gameState = FenLoader.loadGameStateFromFenString(fenString);

        //create frame
        this.singleplayerFrame = new SingleplayerFrame();

        rerender();
    }


    public void setPlayerWhite(Player playerWhite) {
        this.playerWhite = playerWhite;
    }

    public void setPlayerBlack(Player playerBlack) {
        this.playerBlack = playerBlack;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void rerender() {

        this.singleplayerFrame.renderBoard(gameState, playerBlack, playerWhite);
    }

    //start chess game
    public boolean start() {
        if (playerBlack == null || playerWhite == null) return false;

        while (!gameState.isGameFinished()) {

            if (gameState.getTurnColor() == PieceColor.WHITE) {
                //request move
                playerWhite.onMoveRequested();


            } else if (gameState.getTurnColor() == PieceColor.BLACK) {
                //request move
                playerBlack.onMoveRequested();

            }

            rerender();

        }

        if (gameState.getWinnerColor() != null) {
            System.out.println(gameState.getWinnerColor() + " WON!");
        } else {
            System.out.println("REMI!");
        }

        return true;
    }

    public void createUserBoardListener(UserInputListener userInputListener) {
        //this.userBoardListener = new UserBoardListener(userInputListener);

        this.singleplayerFrame.addBoardListener(new UserBoardListener(userInputListener));
    }
}
