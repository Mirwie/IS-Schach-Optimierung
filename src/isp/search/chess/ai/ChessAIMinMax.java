package isp.search.chess.ai;

import isp.search.chess.ChessGame;
import isp.search.chess.GameState;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.FenLoader;
import isp.search.chess.util.Move;
import isp.search.chess.util.MoveCalculator;

import java.util.List;
import java.util.function.Function;

public class ChessAIMinMax extends ChessAI{

    private final Function<GameState, Double> evalFunction;
    private final int depth;

    int count = 0;

    public ChessAIMinMax(ChessGame chessGame, PieceColor pieceColor, Function<GameState, Double> evalFunction, int depth) {
        super(chessGame, pieceColor);
        this.evalFunction = evalFunction;
        this.depth = depth;
    }

    @Override
    public void move(boolean withOutput) {
        GameState currentGameState = chessGame.getGameState();
        final long startTime = System.currentTimeMillis();

        Move bestMove = null;
        double highestValue = -Double.MAX_VALUE;
        double lowestValue = Double.MAX_VALUE;
        double currentValue = 0;
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, this.pieceColor);

        for (Move legalMove : allLegalMoves) {
            iterateCount();
            //clone gameState and move
            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);
            clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

            // White is Maximizing Player and after white comes black so first is min and andersrum
            currentValue = currentGameState.getTurnColor() == PieceColor.WHITE ? min(clonedGameState, depth - 1) : max(clonedGameState, depth - 1);

            if (currentGameState.getTurnColor() == PieceColor.WHITE && currentValue >= highestValue) {
                highestValue = currentValue;
                bestMove = legalMove;
            } else if (currentGameState.getTurnColor() == PieceColor.BLACK && currentValue <= lowestValue) {
                lowestValue = currentValue;
                bestMove = legalMove;
            }
        }

        if(bestMove == null) {
            System.out.println("Current Turn: " + currentGameState.getTurnColor());
            System.out.println("current: " + currentValue);
            System.out.println(allLegalMoves);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
             bestMove = allLegalMoves.get(0);
        }

        long executionTime = System.currentTimeMillis() - startTime;

        if (withOutput) {
        System.out.printf("Best Move for %s: %s with eval of %s in %s milliseconds%n with %s ausprobiert", this.pieceColor, bestMove, currentValue, executionTime, count);
        System.out.println("------------------------------------");
        }

        //move
        currentGameState.movePieceWithLegalCheck(currentGameState.getPieceAtPosition(bestMove.getOldBoardPosition()), bestMove.getNewBoardPosition());
        resetCount();
    }

    public double max(GameState currentGameState, int depth) {
        if (depth <= 0 || currentGameState.isGameFinished()) return evalFunction.apply(currentGameState);

        double highest = -Double.MAX_VALUE;
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor());
        String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);

        for (Move legalMove : allLegalMoves) {
            iterateCount();
            //clone gameState and move
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);
            clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

            double currentValue = min(clonedGameState, depth - 1);

            if (currentValue >= highest) {
                highest = currentValue;
            }
        }

        return highest;
    }


    public double min(GameState currentGameState, int depth) {
        if (depth <= 0 || currentGameState.isGameFinished()) return evalFunction.apply(currentGameState);

        double lowest = Double.MAX_VALUE;
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor());
        String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);

        for (Move legalMove : allLegalMoves) {
            iterateCount();
            //clone gameState and move
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);
            clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

            double currentValue = max(clonedGameState, depth - 1);

            if (currentValue <= lowest) {
                lowest = currentValue;
            }

        }
        return lowest;
    }

    public void iterateCount() {
        this.count++;
    }

    public void resetCount() {
        this.count = 0;
    }
}
