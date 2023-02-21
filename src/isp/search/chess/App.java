package isp.search.chess;


import isp.search.chess.ai.*;
import isp.search.chess.enums.PieceColor;

public class App {
    public static void main(String[] args) {

        // die ersten Acht für die Figuren auf dem Board, w oder b für wer beginnt, White und Black können auf beiden seiden castlen
        // no enpassant, counter halfmove (bis 50 wird zurückgesetzt bei pawn move oder capture einer Figur) , counter fullmove (Wie viele runden gespielt wurden)
        String fenStringStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        ChessGame chessGame = new ChessGame(fenStringStartingPosition);

        ChessAI randomChessAIW = new ChessAIRandom(chessGame, PieceColor.WHITE);
        ChessAI randomChessAIB = new ChessAIRandom(chessGame, PieceColor.BLACK);

        ChessAI chessAiAlphaBetaW = new ChessAiAlphaBetaPruning(chessGame, PieceColor.WHITE, Evaluator::evaluatorV1, 2);
        ChessAI chessAiAlphaBetaB = new ChessAiAlphaBetaPruning(chessGame, PieceColor.BLACK, Evaluator::evaluatorV1, 2);

//        ChessAI mctsWhite = new ChessAiMCTS(chessGame, PieceColor.WHITE, Evaluator::evaluatorV3);
//        ChessAI mctsBlack = new ChessAiMCTS(chessGame, PieceColor.BLACK, Evaluator::evaluatorV3);

        LocalPlayer localPlayer = new LocalPlayer(chessGame, PieceColor.BLACK);
        LocalPlayer localPlayerW = new LocalPlayer(chessGame, PieceColor.WHITE);

        ChessAI chessAIMinMaxW = new ChessAIMinMax(chessGame, PieceColor.WHITE, Evaluator::evaluatorV1, 2);
        ChessAI chessAIMinMaxB = new ChessAIMinMax(chessGame, PieceColor.BLACK, Evaluator::evaluatorV1, 2);

        ChessAI ChessAIAlphaBetaMeinsW = new ChessAIAlphaBeta2(chessGame, PieceColor.WHITE, Evaluator::evaluatorV1, 2);
        ChessAI ChessAIAlphaBetaMeinsB = new ChessAIAlphaBeta2(chessGame, PieceColor.BLACK, Evaluator::evaluatorV1, 2);


        chessGame.setPlayerWhite(localPlayerW);
        chessGame.setPlayerBlack(ChessAIAlphaBetaMeinsB);

        for(int i=0;i<10;i++) {
            chessGame.start(true);
            chessGame.reset();
        }
        System.out.println(chessGame.winnerMap);



    }
}
