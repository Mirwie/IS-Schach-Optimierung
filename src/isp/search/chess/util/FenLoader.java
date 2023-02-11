package isp.search.chess.util;

import isp.search.chess.GameState;
import isp.search.chess.Piece;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.enums.PieceType;

import java.util.ArrayList;
import java.util.List;

public class FenLoader {
    private static final int ROW_COUNT = 8;

    public static GameState loadGameStateFromFenString(String fenString) {

        //eg rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

        String positionString = fenString.split(" ")[0];
        String turnColorChar = fenString.split(" ")[1];
        String castleRightString = fenString.split(" ")[2];
        String anPassantString = fenString.split(" ")[3];
        String turn1 = fenString.split(" ")[3];
        String totalMoveCountString = fenString.split(" ")[4];

        List<Piece> pieces = new ArrayList<>();

        //load position
        int positionPointer = 0;
        for (int i = 0; i < positionString.length(); i++) {

            //get current char from position string
            char positionChar = positionString.charAt(i);

            //if not new row
            if (positionChar != '/') {

                if (positionChar >= '0' && positionChar <= '9') {
                    //move pointer if position string has number

                    positionPointer += positionChar - '0';

                } else {
                    //add piece to position

                    PieceType pieceType = PieceType.getByFenChar(positionChar);
                    PieceColor pieceColor = PieceColor.getByFenChar(positionChar);

                    int boardX = positionPointer % ROW_COUNT;
                    int boardY = (ROW_COUNT - 1) - (int) Math.floor((double) positionPointer / ROW_COUNT);

                    Piece piece = new Piece(new BoardPosition(boardX, boardY), pieceType, pieceColor);

                    //add piece
                    pieces.add(piece);

                    //increment pointer
                    positionPointer++;
                }
            }
        }


        //determine turn color
        PieceColor turnColor = turnColorChar.charAt(0) == 'w' ? PieceColor.WHITE : PieceColor.BLACK;

        //determine castle rights
        boolean castleRightsWhiteK = castleRightString.contains("K");
        boolean castleRightsWhiteQ = castleRightString.contains("Q");
        boolean castleRightsBlackK = castleRightString.contains("k");
        boolean castleRightsBlackQ = castleRightString.contains("q");

        //determine move count
        int totalMoveCount = Integer.parseInt(totalMoveCountString);

        //create and return new game state
        GameState gameState = new GameState(pieces, turnColor, castleRightsWhiteK, castleRightsWhiteQ,
                castleRightsBlackK, castleRightsBlackQ, totalMoveCount);

        return gameState;
    }


    public static String generateFenStringFromGameState(GameState gameState) {
        String fenString = "";

        short counter = 0;
        for (int y = ROW_COUNT - 1; y >= 0; y--) {
            for (int x = 0; x < ROW_COUNT; x++) {

                Piece piece = gameState.getPieceAtPosition(new BoardPosition(x, y));

                if (piece != null) {
                    if (counter > 0) {
                        fenString += Short.toString(counter);
                        counter = 0;
                    }

                    char fenChar = getFenByPiece(piece.getPieceType(), piece.getPieceColor());
                    fenString += fenChar;
                } else {
                    counter++;
                }

            }
            //new col
            if (counter > 0) {
                fenString += Short.toString(counter);
                counter = 0;
            }
            if (y != 0) fenString += '/';
        }


        //turn color
        fenString += gameState.getTurnColor() == PieceColor.WHITE ? " w" : " b";


        //castle rights
        fenString += " ";
        if (gameState.isCastleRightsWhiteK()) fenString += "K";
        if (gameState.isCastleRightsWhiteQ()) fenString += "Q";
        if (gameState.isCastleRightsBlackK()) fenString += "k";
        if (gameState.isCastleRightsBlackQ()) fenString += "q";
        fenString += " ";

        fenString += "- 0 "; //TODO


        fenString += Integer.toString(gameState.getTotalMoveCount()); //TODO

        return fenString;
    }


    private static char getFenByPiece(PieceType pieceType, PieceColor pieceColor) {

        switch (pieceType) {
            case ROOK:
                return pieceColor == PieceColor.WHITE ? 'R' : 'r';
            case BISHOP:
                return pieceColor == PieceColor.WHITE ? 'B' : 'b';
            case KNIGHT:
                return pieceColor == PieceColor.WHITE ? 'N' : 'n';
            case PAWN:
                return pieceColor == PieceColor.WHITE ? 'P' : 'p';
            case KING:
                return pieceColor == PieceColor.WHITE ? 'K' : 'k';
            case QUEEN:
                return pieceColor == PieceColor.WHITE ? 'Q' : 'q';
            default:
                return 0;
        }

    }
}
