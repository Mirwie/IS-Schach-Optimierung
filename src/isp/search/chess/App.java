package isp.search.chess;


import isp.search.chess.ai.ChessAI;
import isp.search.chess.ai.ChessAIEvaluator;
import isp.search.chess.ai.ChessAIRandom;
import isp.search.chess.ai.Evaluator;
import isp.search.chess.enums.PieceColor;

public class App {
    public static void main(String[] args) {

        // die ersten Acht für die Figuren auf dem Board, w oder b für wer beginnt, White und Black können auf beiden seiden castlen
        // no enpassant, counter halfmove (bis 50 wird zurückgesetzt bei pawn move oder capture einer Figur) , counter fullmove (Wie viele runden gespielt wurden)
        String fenStringStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        ChessGame chessGame = new ChessGame(fenStringStartingPosition);

        ChessAI evaluatorChessAI = new ChessAIEvaluator(chessGame, PieceColor.WHITE, Evaluator::evaluatorV1, 1);

        ChessAI evaluatorChessAI2 = new ChessAIEvaluator(chessGame, PieceColor.BLACK, Evaluator::evaluatorV3, 1);

        ChessAI randomChessAI = new ChessAIRandom(chessGame, PieceColor.BLACK);

        LocalPlayer localPlayer = new LocalPlayer(chessGame, PieceColor.BLACK);

        //set players
        chessGame.setPlayerWhite(evaluatorChessAI);
        chessGame.setPlayerBlack(localPlayer);

        chessGame.start();
    }

}
