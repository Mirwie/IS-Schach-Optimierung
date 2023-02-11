package isp.search.chess.gui;


import isp.search.chess.GameState;
import isp.search.chess.Player;
import isp.search.chess.UserBoardListener;

import javax.swing.*;

public class SingleplayerFrame extends JFrame {
    private final SinglePlayerPanel singlePlayerPanel;

    public SingleplayerFrame() {

        setTitle("Chess");
        setSize(1001, 1038); // Sieht auch wenn die Werte unschön sind so am besten aus
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        singlePlayerPanel = new SinglePlayerPanel();

        getContentPane().add(singlePlayerPanel);

        //show frame
        setVisible(true);
    }


    public void renderBoard(GameState gameState, Player playerBlack, Player playerWhite) {
        singlePlayerPanel.renderBoard(gameState, playerBlack, playerWhite);
    }

    public void addBoardListener(UserBoardListener boardListener) {
        //this.boardListener = boardListener;
        singlePlayerPanel.addMouseListener(boardListener);
    }

}
