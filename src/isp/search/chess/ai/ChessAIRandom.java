package isp.search.chess.ai;

import isp.search.chess.ChessGame;
import isp.search.chess.GameState;
import isp.search.chess.Piece;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.BoardPosition;
import isp.search.chess.util.MoveCalculator;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChessAIRandom extends ChessAI {

    public ChessAIRandom(ChessGame chessGame, PieceColor pieceColor) {
        super(chessGame, pieceColor);
    }

    @Override
    public void move(boolean withOutputs) {
        GameState currentGameState = chessGame.getGameState();

        //get random piece that is movable
        List<Piece> randomPiecesThatCanMove = currentGameState.getPieces().stream()
                .filter(p -> p.getPieceColor() == pieceColor)
                .filter(p -> MoveCalculator.getLegalMoves(currentGameState, p).size() > 0)
                .toList();

        Piece randomPiece = randomPiecesThatCanMove.get(ThreadLocalRandom.current().nextInt(randomPiecesThatCanMove.size()));


        //get random move from that random piece
        List<BoardPosition> randomMoves = MoveCalculator.getLegalMoves(currentGameState, randomPiece);
        BoardPosition randomMove = randomMoves.get(ThreadLocalRandom.current().nextInt(randomMoves.size()));

        //move
        currentGameState.movePieceWithLegalCheck(randomPiece, randomMove);
    }


}
