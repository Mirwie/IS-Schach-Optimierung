package isp.search.chess;

import isp.search.chess.enums.PieceColor;
import isp.search.chess.gui.SingleplayerFrame;
import isp.search.chess.util.FenLoader;

import java.util.HashMap;

public class ChessGame {
    private GameState gameState;

    private final SingleplayerFrame singleplayerFrame;

    HashMap<String,Integer> winnerMap;


    private Player playerWhite, playerBlack;

    public ChessGame(String fenString) {

        //load game state
        this.gameState = FenLoader.loadGameStateFromFenString(fenString);

        //create frame
        this.singleplayerFrame = new SingleplayerFrame();

        this.winnerMap = new HashMap<>(){{
            put("WHITE Won", 0);
            put("Remi", 0);
            put("BLACK Won", 0);
        }};

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
    public boolean start(boolean withOutputs) {
        if (playerBlack == null || playerWhite == null) return false;

        while (!gameState.isGameFinished()) {

            if (gameState.getTurnColor() == PieceColor.WHITE) {
                //request move
                playerWhite.onMoveRequested(withOutputs);


            } else if (gameState.getTurnColor() == PieceColor.BLACK) {
                //request move
                playerBlack.onMoveRequested(withOutputs);

            }

            rerender();

        }

        if (gameState.getWinnerColor() != null) {
            if(withOutputs) {
                System.out.println(gameState.getWinnerColor() + " WON!");
            }
            Integer integer = winnerMap.get(gameState.getWinnerKey());
            winnerMap.put(gameState.getWinnerKey(),integer + 1);

        } else {
            if(withOutputs) {
                System.out.println("REMI!");
            }
            Integer integer2 = winnerMap.get("Remi");
            winnerMap.put("Remi", integer2 + 1);
        }

        return true;
    }

    public void reset() {
        String fenStringStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        this.gameState = FenLoader.loadGameStateFromFenString(fenStringStartingPosition);
    }

    public void createUserBoardListener(UserInputListener userInputListener) {
        //this.userBoardListener = new UserBoardListener(userInputListener);

        this.singleplayerFrame.addBoardListener(new UserBoardListener(userInputListener));
    }
}
