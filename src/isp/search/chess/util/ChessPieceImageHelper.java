package isp.search.chess.util;

import isp.search.chess.enums.PieceColor;
import isp.search.chess.enums.PieceType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ChessPieceImageHelper {

    final static String CHESS_TEXTURE_ATLAS_PATH = "/piece-atlas.png";

    public static Rectangle getBoundsByPiece(PieceType pieceType, PieceColor pieceColor) {

        final short pieceWidth = 170, pieceHeight = 170;

        short x = 0;
        if (pieceType == PieceType.KING) x = 0;
        if (pieceType == PieceType.QUEEN) x = pieceWidth;
        if (pieceType == PieceType.BISHOP) x = 2 * pieceWidth;
        if (pieceType == PieceType.KNIGHT) x = 3 * pieceWidth;
        if (pieceType == PieceType.ROOK) x = 4 * pieceWidth;
        if (pieceType == PieceType.PAWN) x = 5 * pieceWidth;

        short y = pieceColor == PieceColor.WHITE ? 0 : pieceHeight;

        return new Rectangle(x, y, pieceWidth, pieceHeight);
    }


    public static BufferedImage getTextureAtlas() {

        BufferedImage textureAtlasImage = null;

        try {
            textureAtlasImage = ImageIO.read(Objects.requireNonNull(ChessPieceImageHelper.class.getResource(CHESS_TEXTURE_ATLAS_PATH)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return textureAtlasImage;
    }
}