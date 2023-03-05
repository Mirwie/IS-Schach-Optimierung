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

        ChessAI chessAiAlphaBetaW = new ChessAiAlphaBetaPruning(chessGame, PieceColor.WHITE, Heuristics::evaluatorV2, 2);
        ChessAI chessAiAlphaBetaB = new ChessAiAlphaBetaPruning(chessGame, PieceColor.BLACK, Heuristics::evaluatorV1, 2);

        ChessAI mctsWhite = new ChessAiMCTS<>(chessGame, PieceColor.WHITE);
        ChessAI mctsBlack = new ChessAiMCTS<>(chessGame, PieceColor.BLACK);

        LocalPlayer localPlayerB = new LocalPlayer(chessGame, PieceColor.BLACK);
        LocalPlayer localPlayerW = new LocalPlayer(chessGame, PieceColor.WHITE);

        ChessAI chessAIMinMaxW = new ChessAIMinMax(chessGame, PieceColor.WHITE, Heuristics::movePossibilitiesEvaluator, 3);
        ChessAI chessAIMinMaxB = new ChessAIMinMax(chessGame, PieceColor.BLACK, Heuristics::evaluatorV1, 3);

        ChessAI ChessAIAlphaBeta2W = new ChessAIAlphaBeta2(chessGame, PieceColor.WHITE, Heuristics::evaluatorV1, 2);
        ChessAI ChessAIAlphaBeta2B = new ChessAIAlphaBeta2(chessGame, PieceColor.BLACK, Heuristics::evaluatorV1, 2);


        chessGame.setPlayerWhite(mctsWhite);
        chessGame.setPlayerBlack(chessAiAlphaBetaB);
        System.out.println(Double.NEGATIVE_INFINITY);
        for(int i=0;i<10;i++) {
            chessGame.start(true);
            chessGame.reset();
        }
        System.out.println(chessGame.winnerMap);



    }
}
