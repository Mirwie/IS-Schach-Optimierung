package isp.search.chess;


import isp.search.chess.ai.ChessAI;
import isp.search.chess.ai.ChessAiAlphaBetaPruning;
import isp.search.chess.ai.ChessAIRandom;
import isp.search.chess.ai.Evaluator;
import isp.search.chess.enums.PieceColor;

import java.util.Collection;
import java.util.Collections;

public class App {
    public static void main(String[] args) {

        // die ersten Acht für die Figuren auf dem Board, w oder b für wer beginnt, White und Black können auf beiden seiden castlen
        // no enpassant, counter halfmove (bis 50 wird zurückgesetzt bei pawn move oder capture einer Figur) , counter fullmove (Wie viele runden gespielt wurden)
        String fenStringStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        ChessGame chessGame = new ChessGame(fenStringStartingPosition);
        ChessAI randomChessAI1 = new ChessAIRandom(chessGame, PieceColor.WHITE);
        //ChessAI evaluatorChessAI = new ChessAiAlphaBetaPruning(chessGame, PieceColor.WHITE, Evaluator::evaluatorV1, 1);
        ChessAI randomChessAI = new ChessAIRandom(chessGame, PieceColor.BLACK);

        ChessAI evaluatorChessAI = new ChessAiAlphaBetaPruning(chessGame, PieceColor.WHITE, Evaluator::evaluatorV1, 0);

        ChessAI evaluatorChessAI2 = new ChessAiAlphaBetaPruning(chessGame, PieceColor.BLACK, Evaluator::evaluatorV3, 0);


        LocalPlayer localPlayer = new LocalPlayer(chessGame, PieceColor.BLACK);
        LocalPlayer localPlayer1 = new LocalPlayer(chessGame, PieceColor.WHITE);

        chessGame.setPlayerWhite(evaluatorChessAI);
        chessGame.setPlayerBlack(evaluatorChessAI2);

        for(int i=0;i<6;i++) {
            chessGame.start(false);
            chessGame.reset();
        }
        System.out.println(chessGame.winnerMap);

    }

}
