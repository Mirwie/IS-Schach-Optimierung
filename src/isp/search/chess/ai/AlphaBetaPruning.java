package isp.search.chess.ai;

import isp.search.chess.GameState;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.FenLoader;
import isp.search.chess.util.Move;
import isp.search.chess.util.MoveCalculator;

import java.util.List;
import java.util.function.Function;

public class AlphaBetaPruning {

    private final Function<GameState, Double> evaluationFunction;

    //private GameState initGameState;

    public AlphaBetaPruning(Function<GameState, Double> evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
        //this.initGameState = initGameState;
    }

    // Aufgerufen durch pruning_max(jetzigerSpielstand, wieTief?,Farbe, -unendlich,unendlich)
    public double pruning_max(GameState currentGameState, PieceColor pieceColor, int depth, double alpha, double beta) {
        if (depth == 0) return evaluationFunction.apply(currentGameState);


        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor());


        for (Move legalMove : allLegalMoves) {

            //clone gameState and move
            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

            clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());
            alpha = Math.max(alpha, pruning_min(clonedGameState, pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE, depth - 1, alpha, beta));

            if (alpha >= beta) {
                return alpha;
            }
        }

        return alpha;
    }

    public double pruning_min(GameState currentGameState, PieceColor pieceColor, int depth, double alpha, double beta) {
        if (depth == 0) return evaluationFunction.apply(currentGameState);

        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor()); // TODO PieceColor.WHITE
        for (Move legalMove : allLegalMoves) {

            //clone gameState and move
            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

            clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

            beta = Math.min(beta, pruning_max(clonedGameState, pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE, depth - 1, alpha, beta));

            if (alpha >= beta) {
                return beta;
            }
        }

        return beta;
    }


    public double minimax(GameState currentGameState, PieceColor maximizingPieceColor, int depth, double alpha, double beta) {
        if (depth == 0 || currentGameState.isGameFinished()) return evaluationFunction.apply(currentGameState);


        if (currentGameState.getTurnColor() == PieceColor.WHITE) {
            double maxEval = Double.NEGATIVE_INFINITY;

            List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor());
            for (Move legalMove : allLegalMoves) {

                //clone gameState and move
                String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
                GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

                clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

                double eval = minimax(clonedGameState, maximizingPieceColor, depth - 1, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (alpha >= beta) {
                    break;
                }
            }

            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;

            List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor());
            for (Move legalMove : allLegalMoves) {

                //clone gameState and move
                String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
                GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

                clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

                double eval = minimax(clonedGameState, maximizingPieceColor, depth - 1, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (alpha >= beta) {
                    break;
                }
            }

            return minEval;
        }
    }


    public double max(GameState currentGameState, PieceColor pieceColor, int depth) {
        if (depth == 0)
            return pieceColor == PieceColor.WHITE ? evaluationFunction.apply(currentGameState) : -evaluationFunction.apply(currentGameState);


        double bestMoveEval = -Double.MAX_VALUE;
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor());
        for (Move legalMove : allLegalMoves) {

            //clone gameState and move
            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

            clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

            double value = min(clonedGameState, pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE, depth - 1);

            if (value > bestMoveEval) {
                bestMoveEval = value;
            }
        }

        return bestMoveEval;
    }


    public double min(GameState currentGameState, PieceColor pieceColor, int depth) {
        if (depth == 0)
            return pieceColor == PieceColor.WHITE ? evaluationFunction.apply(currentGameState) : -evaluationFunction.apply(currentGameState);


        double bestMoveEval = -Double.MAX_VALUE;
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor());
        for (Move legalMove : allLegalMoves) {

            //clone gameState and move
            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

            clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

            double value = max(clonedGameState, pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE, depth - 1);

            if (value > bestMoveEval) {
                bestMoveEval = value;
            }
        }

        return bestMoveEval;
    }


}
