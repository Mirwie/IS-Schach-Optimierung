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
public class ChessAIEvaluator extends ChessAI {
    private final Function<GameState, Double> evalFunction;
    private final int depth;

    public ChessAIEvaluator(ChessGame chessGame, PieceColor pieceColor, Function<GameState, Double> evalFunction, int depth) {
        super(chessGame, pieceColor);
        this.evalFunction = evalFunction;
        this.depth = depth;
    }


    @Override
    public void move() {

        GameState currentGameState = chessGame.getGameState();

        //new Alpha Beta Pruning with eval method
        AlphaBetaPruning alphaBetaPruning = new AlphaBetaPruning(this.evalFunction);

        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(currentGameState, this.pieceColor);


        if (this.pieceColor == PieceColor.WHITE) {
            Move bestMove = null;
            double bestMoveEval = Double.NEGATIVE_INFINITY;

            //for every move: select best
            for (Move legalMove : allLegalMoves) {
                //clone gameState and move
                String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
                GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);
                clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

                //System.out.println(bestMoveEval);
                //double evalOfMove = alphaBetaPruning.pruning_min(clonedGameState, this.pieceColor, this.depth, bestMoveEval, Double.MAX_VALUE);

                double evalOfMove = alphaBetaPruning.minimax(clonedGameState, PieceColor.WHITE, this.depth, bestMoveEval, Double.POSITIVE_INFINITY);

                //System.out.println(String.format("Move for %s: %s with eval of %s", this.pieceColor, legalMove, evalOfMove));

                if (evalOfMove >= bestMoveEval) {
                    bestMove = legalMove;
                    bestMoveEval = evalOfMove;

                }
            }

            System.out.printf("Best Move for %s: %s with eval of %s%n", this.pieceColor, bestMove, bestMoveEval);
            System.out.println("------------------------------------");

            //move best move
            currentGameState.movePieceWithLegalCheck(currentGameState.getPieceAtPosition(bestMove.getOldBoardPosition()), bestMove.getNewBoardPosition());
        }


        if (this.pieceColor == PieceColor.BLACK) {
            Move bestMove = null;
            double bestMoveEval = Double.POSITIVE_INFINITY;

            //for every move: select best
            for (Move legalMove : allLegalMoves) {
                //clone gameState and move
                String currentGameFenString = FenLoader.generateFenStringFromGameState(currentGameState);
                GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);
                clonedGameState.movePieceWithLegalCheck(clonedGameState.getPieceAtPosition(legalMove.getOldBoardPosition()), legalMove.getNewBoardPosition());

                //System.out.println(bestMoveEval);
                //double evalOfMove = alphaBetaPruning.pruning_min(clonedGameState, this.pieceColor, this.depth, bestMoveEval, Double.MAX_VALUE);

                double evalOfMove = alphaBetaPruning.minimax(clonedGameState, PieceColor.BLACK, this.depth, Double.NEGATIVE_INFINITY, bestMoveEval);

                System.out.printf("Move for %s: %s with eval of %s%n", this.pieceColor, legalMove, evalOfMove);

                if (evalOfMove <= bestMoveEval) {
                    bestMove = legalMove;
                    bestMoveEval = evalOfMove;

                }
            }

            System.out.printf("Best Move for %s: %s with eval of %s%n", this.pieceColor, bestMove, bestMoveEval);
            System.out.println("------------------------------------");

            //move best move
            currentGameState.movePieceWithLegalCheck(currentGameState.getPieceAtPosition(bestMove.getOldBoardPosition()), bestMove.getNewBoardPosition());
        }
    }
}
