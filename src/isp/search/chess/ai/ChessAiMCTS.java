package isp.search.chess.ai;

import isp.search.chess.ChessGame;
import isp.search.chess.GameState;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.FenLoader;
import isp.search.chess.util.Move;
import isp.search.chess.util.MoveCalculator;


import java.util.LinkedList;
import java.util.List;

import static java.lang.StrictMath.sqrt;

public class ChessAiMCTS<T extends Move>  extends ChessAI{

    // Where we are
    private ANode<T> current;

    private static final double C = sqrt(2);


    public ChessAiMCTS(ChessGame chessGame, PieceColor pieceColor) {
        super(chessGame, pieceColor);
        String currentGameFenString = FenLoader.generateFenStringFromGameState(chessGame.getGameState());
        GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

        current = new ANode<>(null,null,false,clonedGameState);
    }


    @Override
    public void move(boolean withOutputs) {
        System.out.println("Player " + this.pieceColor + " ist dran");
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(current.getGameState(), current.getGameState().getTurnColor());
        if(allLegalMoves.isEmpty()) {
            System.out.println("Keine Moves mehr verfügbar");
        }

        ANode<T> nodeToExpand;
        boolean stop = false;
        do {
            nodeToExpand = selection();
            if (nodeToExpand == null) {
                break;
            }
            // the tree has not been fully explored yet
            ANode<T> expandedNode = expansion(nodeToExpand);
            PieceColor winner = simulation();
            backPropagation(expandedNode, winner);
        } while (!stop);


        Move best = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        // all possible transitions have been set on root node
        // see expansion(N node)
        for (ANode<T> child : current.getChilds()) {
            double value = child.ratio(this.pieceColor);
            if (value > bestValue) {
                bestValue = value;
                best = child.getTransition();
                //assert best != null;
            }
        }

        // make best move
            System.out.printf("Best Move for %s: %s with eval of %s", this.pieceColor, best, bestValue);
            System.out.println("------------------------------------");
        boolean fertig = chessGame.getGameState().movePieceWithLegalCheck(chessGame.getGameState().getPieceAtPosition(best.getOldBoardPosition()), best.getNewBoardPosition());
        //System.out.println(fertig);


//            MCTS mcts = new MCTS();
//            mcts.resetSimulationChessboard();
//
//            MCTSNode node = mcts.selection(mcts.rootNode);
//            int diff = mcts.simulation(node);
//
//            GameState gameStateNew = new GameState(node);
//
//
//            mcts.backPropagation(node, diff, node.mcDepth, mcts.isInCheck(gameStateNew));

    }

    private ANode<T> selection() {
        ANode<T> n = current;
        ANode<T> next;
        final PieceColor player = current.getGameState().getTurnColor();
        do {
            Move transition = selectTransition(n, player);
            if (transition == null) {
                n.setTerminal(true);
                if (n == current) {
                    return null;
                } else {
                    // node has parent, rewind
                    current.setGameState(current.getParent().getGameState());
                    next = n.getParent();
                }
            } else {
                next = n.getChild(transition);

                current.getGameState().movePieceWithLegalCheck(current.getGameState().getPieceAtPosition(transition.getOldBoardPosition()), transition.getNewBoardPosition());

                if (next == null) {
                    // this transition has never been explored
                    // create child node and expand it
                    next = new ANode<>(n, transition, current.getGameState().isGameFinished(),current.getGameState());
                }
            }
            n = next;
        } while (!n.isLeaf());
        return n;
    }

    public Move selectTransition(ANode<T> node, final PieceColor player) {
        double v = Double.NEGATIVE_INFINITY;
        Move best = null;
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(current.getGameState(), current.getGameState().getTurnColor());
        for (Move transition : allLegalMoves) {
            ANode<T> n = node.getChild(transition);
            if (n == null) {
                // unexplored path
                return transition;
            }
            if (!n.isTerminal()) {
                // child already explored and non terminal
                long simulations = n.simulations();
                assert simulations > 0;
                long wins = n.wins(player);
                // w/n + C * Math.sqrt(ln(n(p)) / n)
                // TODO : add a random hint to avoid ex-aequo
                double value = (simulations == 0 ? 0 : wins / simulations + C * sqrt(node.simulations()) / simulations);
                if (value > v) {
                    v = value;
                    best = transition;
                }
            }
        }
        return best;
    }

    private ANode<T> expansion(final ANode<T> leaf) {
        if (leaf.isTerminal()) {
            return leaf;
        }
        Move transition = expansionTransition();
        if (transition != null) {
            // expand the path with the chosen transition

            current.getGameState().movePieceWithLegalCheck(current.getGameState().getPieceAtPosition(transition.getOldBoardPosition()), transition.getNewBoardPosition());
            return new ANode<>(leaf, transition, current.getGameState().isGameFinished(),current.getGameState());
        } else {
            return leaf;
        }
    }

    public Move expansionTransition() {
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(current.getGameState(), this.pieceColor);
        System.out.println(allLegalMoves);
        System.out.println("1");

        if (allLegalMoves.isEmpty()) {
            return null;
        }
        return allLegalMoves.get((int) Math.floor(Math.random() * allLegalMoves.size()));
    }

    private PieceColor simulation() {
        GameState gameStateSave = current.getGameState();
        LinkedList<Move> transitions = new LinkedList<>();
        // do
        while (!current.getGameState().isGameFinished()) {
            Move transition = simulationTransition();
            assert transition != null; //TODO vllt auch nochmal raus wegen laufzeit
            boolean moveSuccess = current.getGameState().movePieceWithLegalCheck(current.getGameState().getPieceAtPosition(transition.getOldBoardPosition()), transition.getNewBoardPosition());
            System.out.println(moveSuccess);
            transitions.add(transition);
        }

        PieceColor winner = current.getGameState().getWinnerColor();

        // undo
        current.setGameState(gameStateSave);
        return winner;
    }

    public Move simulationTransition() {
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(current.getGameState(), current.getGameState().getTurnColor());
        System.out.println(allLegalMoves);
        System.out.println("1");

        if (allLegalMoves.isEmpty()) {
            return null;
        }
        return allLegalMoves.get((int) Math.floor(Math.random() * allLegalMoves.size()));
    }

    private void backPropagation(ANode<T> expandedNode, final PieceColor winner) {
        ANode<T> n = expandedNode;
        while (n != null) {
            n.result(winner);
            ANode<T> parent = n.getParent();
            if (parent == null) {
                // root reached
                break;
            }
            n = parent;
        }
    }



}
