package isp.search.chess;


import isp.search.chess.ai.*;
import isp.search.chess.enums.PieceColor;

import java.util.Collection;
import java.util.Collections;

public class App {
    public static void main(String[] args) {

        // die ersten Acht für die Figuren auf dem Board, w oder b für wer beginnt, White und Black können auf beiden seiden castlen
        // no enpassant, counter halfmove (bis 50 wird zurückgesetzt bei pawn move oder capture einer Figur) , counter fullmove (Wie viele runden gespielt wurden)
        String fenStringStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        ChessGame chessGame = new ChessGame(fenStringStartingPosition);
        ChessAI randomChessAIW = new ChessAIRandom(chessGame, PieceColor.WHITE);
        //ChessAI evaluatorChessAI = new ChessAiAlphaBetaPruning(chessGame, PieceColor.WHITE, Evaluator::evaluatorV1, 1);
        ChessAI randomChessAIB = new ChessAIRandom(chessGame, PieceColor.BLACK);

        ChessAI evaluatorChessAIW = new ChessAiAlphaBetaPruning(chessGame, PieceColor.WHITE, Evaluator::evaluatorV1, 1);

        ChessAI evaluatorChessAIB = new ChessAiAlphaBetaPruning(chessGame, PieceColor.BLACK, Evaluator::evaluatorV3, 2);

//        ChessAI mctsWhite = new ChessAiMCTS(chessGame, PieceColor.WHITE, Evaluator::evaluatorV3);
//        ChessAI mctsBlack = new ChessAiMCTS(chessGame, PieceColor.BLACK, Evaluator::evaluatorV3);

        LocalPlayer localPlayer = new LocalPlayer(chessGame, PieceColor.BLACK);
        LocalPlayer localPlayer1 = new LocalPlayer(chessGame, PieceColor.WHITE);

        ChessAI chessAIMinMaxW = new ChessAIMinMax(chessGame, PieceColor.WHITE, Evaluator::evaluatorV3, 1);

        ChessAI chessAIMinMaxB = new ChessAIMinMax(chessGame, PieceColor.BLACK, Evaluator::evaluatorV3, 1);

        chessGame.setPlayerWhite(chessAIMinMaxW);
        chessGame.setPlayerBlack(chessAIMinMaxB);

        for(int i=0;i<10;i++) {
            chessGame.start(true);
            chessGame.reset();
        }
        System.out.println(chessGame.winnerMap);




    }

}
