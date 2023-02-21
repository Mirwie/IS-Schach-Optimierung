package isp.search.chess.ai;

import isp.search.chess.ChessGame;
import isp.search.chess.GameState;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.FenLoader;
import isp.search.chess.util.Move;
import isp.search.chess.util.MoveCalculator;

import java.util.List;
import java.util.function.Function;

/*
 * Diese Klasse erweitert die Klasse ChessAI und implementiert die Bewertung der Spielsituation.
 */
public class ChessAiAlphaBetaPruning extends ChessAI {
    private final Function<GameState, Double> evalFunction;
    private final int depth;

    int count = 0;

    public ChessAiAlphaBetaPruning(ChessGame chessGame, PieceColor pieceColor, Function<GameState, Double> evalFunction, int depth) {
        super(chessGame, pieceColor);
        this.evalFunction = evalFunction;
        this.depth = depth;
    }


    @Override
    public void move(boolean withOutput) {

        GameState currentGameState = chessGame.getGameState();

        //new Alpha Beta Pruning with eval method

        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, this.pieceColor);

        long startTime = System.currentTimeMillis();

        if (this.pieceColor == PieceColor.WHITE) {
            Move bestMove = null;
            double bestMoveEval = Double.NEGATIVE_INFINITY;

            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

            //for every move: select best
            for (Move legalMove : allLegalMoves) {
                //clone gameState and move
                clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());
                iterateCount();

                double evalOfMove = minimax(clonedGameState, this.depth - 1, bestMoveEval, Double.POSITIVE_INFINITY);


                if (evalOfMove >= bestMoveEval) {
                    bestMove = legalMove;
                    bestMoveEval = evalOfMove;

                }
            }

            long executeTime = System.currentTimeMillis() - startTime;
            if (withOutput) {
                System.out.printf("Best Move for %s: %s with eval of %s mit %s ausprobiert in %sms %n", this.pieceColor, bestMove, bestMoveEval,count,executeTime);
                System.out.println("------------------------------------");
            }

            //move best move
            currentGameState.movePieceWithLegalCheck(currentGameState.getPieceAtPosition(bestMove.getOldBoardPosition()), bestMove.getNewBoardPosition());
            resetCount();
        }


        if (this.pieceColor == PieceColor.BLACK) {
            Move bestMove = null;
            double bestMoveEval = Double.POSITIVE_INFINITY;

            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);


            //for every move: select best
            for (Move legalMove : allLegalMoves) {
                //clone gameState and move
                clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());
                iterateCount();

                double evalOfMove = minimax(clonedGameState, this.depth - 1, Double.NEGATIVE_INFINITY, bestMoveEval);


                if (evalOfMove <= bestMoveEval) {
                    bestMove = legalMove;
                    bestMoveEval = evalOfMove;

                }
            }

            long executeTime = System.currentTimeMillis() - startTime;
            if (withOutput) {
                System.out.printf("Best Move for %s: %s with eval of %s mit %s ausprobiert in %sms %n", this.pieceColor, bestMove, bestMoveEval,count,executeTime);
                System.out.println("------------------------------------");
            }

            //move best move
            currentGameState.movePieceWithLegalCheck(currentGameState.getPieceAtPosition(bestMove.getOldBoardPosition()), bestMove.getNewBoardPosition());
            resetCount();
        }
    }

    public double minimax(GameState currentGameState, int depth, double alpha, double beta) {
        if (depth <= 0 || currentGameState.isGameFinished()) return evalFunction.apply(currentGameState);


        if (currentGameState.getTurnColor() == PieceColor.WHITE) {
            double maxEval = Double.NEGATIVE_INFINITY;

            List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, currentGameState.getTurnColor());
            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);
            for (Move legalMove : allLegalMoves) {

                //clone gameState and move
               clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());
                iterateCount();

                double eval = minimax(clonedGameState, depth - 1, alpha, beta);
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
            String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
            GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

            for (Move legalMove : allLegalMoves) {

                //clone gameState and move

                clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());
                iterateCount();

                double eval = minimax(clonedGameState, depth - 1, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (alpha >= beta) {
                    break;
                }
            }

            return minEval;
        }
    }

    public void iterateCount() {
        this.count++;
    }

    public void resetCount() {
        this.count = 0;
    }
}
