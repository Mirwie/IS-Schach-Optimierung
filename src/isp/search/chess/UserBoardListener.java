package isp.search.chess;

import isp.search.chess.gui.SinglePlayerPanel;
import isp.search.chess.util.BoardPosition;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;

public class UserBoardListener implements MouseInputListener {

    private final UserInputListener callback;

    public UserBoardListener(UserInputListener callback) {
        this.callback = callback;
    }


    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {

        //calculate board position
        int boardX = (int) Math.floor(1.0f * e.getX() / SinglePlayerPanel.BOARD_SIZE * SinglePlayerPanel.ROW_COUNT);
        int boardY = (int) Math.floor(1.0f * e.getY() / SinglePlayerPanel.BOARD_SIZE * SinglePlayerPanel.ROW_COUNT);

        BoardPosition pressedBoardPosition = new BoardPosition(boardX, boardY);


        callback.onTilePressed(pressedBoardPosition);
    }

    @Override
    public void mouseReleased(MouseEvent e) {


    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

}
