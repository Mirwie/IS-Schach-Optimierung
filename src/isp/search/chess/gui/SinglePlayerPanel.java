package isp.search.chess.gui;

import isp.search.chess.GameState;
import isp.search.chess.Piece;
import isp.search.chess.Player;
import isp.search.chess.util.BoardPosition;
import isp.search.chess.util.ChessPieceImageHelper;
import isp.search.chess.util.MoveCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class SinglePlayerPanel extends JPanel {
    private static final int BOARD_X = 0;
    private static final int BOARD_Y = 0;
    public static final int BOARD_SIZE = 1000;
    public static final int ROW_COUNT = 8;

    private GameState gameState;

    private final BufferedImage chessPieceAtlasImage;

    private List<BoardPosition> selectedTiles;


    public SinglePlayerPanel() {
        super();
        chessPieceAtlasImage = ChessPieceImageHelper.getTextureAtlas();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //setup
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );


        //calculate move indicators
        List<BoardPosition> moveIndicatorPositions = new ArrayList<>();

        selectedTiles.stream().forEach(selectedTile -> {
            moveIndicatorPositions.addAll(MoveCalculator.getLegalMoves(gameState, gameState.getPieceAtPosition(selectedTile)));
        });


        //loop over every square
        for (int x = 0; x < ROW_COUNT; x++) {
            for (int y = 0; y < ROW_COUNT; y++) {
                final int tileSize = BOARD_SIZE / ROW_COUNT;

                BoardPosition currentBoardPosition = new BoardPosition(x, y);
                Piece currentPiece = gameState.getPieceAtPosition(currentBoardPosition);

                //draw squares

                g2.setColor((x + y) % 2 == 0 ? new Color(4, 19, 150) : new Color(64, 103, 183));
                //if tile is selected change color
                if (selectedTiles.stream().anyMatch(selectedTile -> selectedTile.equals(currentBoardPosition))) {
                    g2.setColor(new Color(5, 14, 58));
                }

                g2.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);


                //draw move indicators
                if (moveIndicatorPositions.contains(currentBoardPosition)) {

                    if (currentPiece == null) {
                        final short indicatorRadius = tileSize / 2 / 5;
                        g2.setColor(new Color(150, 165, 170));
                        g2.fillOval(x * tileSize + tileSize / 2 - indicatorRadius, y * tileSize + tileSize / 2 - indicatorRadius,
                                indicatorRadius * 2, indicatorRadius * 2);
                    } else {
                        g2.setColor(new Color(197, 93, 100));
                        g2.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                    }
                }


                //draw pieces
                if (currentPiece != null) {
                    Rectangle pieceBounds = ChessPieceImageHelper.getBoundsByPiece(currentPiece.getPieceType(), currentPiece.getPieceColor());

                    g2.drawImage(chessPieceAtlasImage, x * tileSize, y * tileSize, x * tileSize + tileSize, y * tileSize + tileSize,
                            pieceBounds.x, pieceBounds.y, pieceBounds.x + pieceBounds.width, pieceBounds.y + pieceBounds.height, null);
                }


            }
        }

    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


    public void renderBoard(GameState gameState, Player playerBlack, Player playerWhite) {
        this.gameState = gameState;

        this.selectedTiles = new ArrayList<>();
        if (playerBlack != null && playerBlack.getSelectedTile() != null)
            selectedTiles.add(playerBlack.getSelectedTile());
        if (playerWhite != null && playerWhite.getSelectedTile() != null)
            selectedTiles.add(playerWhite.getSelectedTile());


        repaint();

    }

}
