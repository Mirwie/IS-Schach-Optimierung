package isp.search.chess.ai;

import isp.search.chess.GameState;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.Move;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ANode<T extends Move> {
    private final Map<Move, ANode<T>> childs;
    private final Map<PieceColor, Integer> wins;
    private long simulations = 0;
    private boolean terminal;
    private final Move transition;
    private ANode<T> parent;

    private GameState gameState;

    private GameState simulationBoard;

    // Creates Child
    ANode(ANode<T> parent, Move transition, boolean terminal,GameState gameState) {
        System.out.println("Neuer Node erzeugt");
        this.terminal = terminal;
        this.parent = parent;
        this.transition = parent == null ? null : transition;
        this.childs = new HashMap<>();
        this.wins = new HashMap<>();
        if (parent != null) {
            parent.childs.put(transition, this);
        }
        this.gameState = gameState;
    }

    // Creates Parent
    ANode(ANode<T> child) {
        this.terminal = false;
        this.parent = null;
        this.transition = null;
        this.childs = new HashMap<>();
        this.wins = new HashMap<>();
        this.simulations = child.simulations();
        // copy stats
        childs.put(child.getTransition(), child);
        for (Map.Entry<PieceColor, Integer> e : child.wins.entrySet()) {
            wins.put(e.getKey(), e.getValue());
        }
    }

    public boolean isTerminal() {
        return this.terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public double value(PieceColor player) {
        return wins(player);
    }

    public ANode<T> getChild(Move transition) {
        return childs.get(transition);
    }

    public ANode<T> getParent() {
        return parent;
    }

    public Move getTransition() {
        return transition;
    }

    public void makeRoot() {
        this.parent = null;
    }

    public boolean isLeaf() {
        return childs.isEmpty();
    }

    public long simulations() {
        return simulations;
    }

    public double ratio(PieceColor pieceColor) {
        Integer w = wins.get(pieceColor);
        if (w == null) {
            return 0;
        } else {
            return ((double) w) / simulations;
        }
    }

    public long wins(PieceColor pieceColor) {
        Integer w = wins.get(pieceColor);
        if (w == null) {
            return 0;
        } else {
            return w;
        }
    }

    public void result(PieceColor winner) {
        simulations++;
        Integer w = wins.get(winner);
        if (w == null) {
            wins.put(winner, 1);
        } else {
            wins.put(winner, w + 1);
        }
    }

    public Collection<ANode<T>> getChilds() {
        return childs.values();
    }

    public ANode<T> getNode(T transition) {
        return childs.get(transition);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getSimulationBoard() {
        return simulationBoard;
    }

    public void setSimulationBoard(GameState simulationBoard) {
        this.simulationBoard = simulationBoard;
    }
}
