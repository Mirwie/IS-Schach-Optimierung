package isp.search.chess.ai;

import isp.search.chess.ChessGame;
import isp.search.chess.GameState;
import isp.search.chess.Piece;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.BoardPosition;
import isp.search.chess.util.MoveCalculator;

import java.util.List;

/**
 * Macht immer den erstmöglichen Zug, einfach um Dinge zu testen, da Spielzüge des Gegners einfach vorausschaubar sind.
 */
public class ChessAIFirstMove extends ChessAI {

    public ChessAIFirstMove(ChessGame chessGame, PieceColor pieceColor) {
        super(chessGame, pieceColor);
    }


    @Override
    public void move(boolean withOutputs) {
        GameState currentGameState = chessGame.getGameState();

        Piece FirstPieceThatCanMove = currentGameState.getPieces().stream()
                .filter(p -> p.getPieceColor() == pieceColor)
                .filter(p -> MoveCalculator.getLegalMoves(currentGameState, p).size() > 0)
                .findFirst()
                .get();


        List<BoardPosition> moves = MoveCalculator.getLegalMoves(currentGameState, FirstPieceThatCanMove);
        BoardPosition firstMove = moves.get(0);

        //move
        currentGameState.movePieceWithLegalCheck(FirstPieceThatCanMove, firstMove);
    }

}
