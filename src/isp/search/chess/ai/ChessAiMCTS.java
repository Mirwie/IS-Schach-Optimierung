package isp.search.chess.ai;

import isp.search.chess.ChessGame;
import isp.search.chess.GameState;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.util.BoardPosition;
import isp.search.chess.util.FenLoader;
import isp.search.chess.util.Move;
import isp.search.chess.util.MoveCalculator;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.sqrt;

public class ChessAiMCTS<T extends Move>  extends ChessAI{

    // Where we are
    private MCTNode<T> current;

    private static final double C = sqrt(2);


    public ChessAiMCTS(ChessGame chessGame, PieceColor pieceColor) {
        super(chessGame, pieceColor);
        String currentGameFenString = FenLoader.generateFenStringFromGameState(chessGame.getGameState());
        GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

        current = new MCTNode<>(null,null,false,clonedGameState);
    }


    @Override
    public void move(boolean withOutputs) {
        // Im Moment so implementiert, dass immer ein Move gemacht wird, auch wenn eigentlich keiner gefunden wird.
        // Daher wird zwar immer gespielt aber oftmals ist es eher ein zufälliger Zug
        long startTime = System.currentTimeMillis();
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(chessGame.getGameState(), this.pieceColor);
        if(allLegalMoves.isEmpty()) {
            System.out.println("Keine Moves mehr verfügbar");
        }

        MCTNode<T> nodeToExpand;
        do {
            nodeToExpand = selection();
            if (nodeToExpand == null) {
                break;
            }
            // the tree has not been fully explored yet
            MCTNode<T> expandedNode = expansion(nodeToExpand);
            PieceColor winner = simulation();
            backPropagation(expandedNode, winner);
            System.out.println(System.currentTimeMillis() - startTime);
        } while (System.currentTimeMillis() - startTime > 4000);


        Move best = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        // all possible transitions have been set on root node
        // see expansion(N node)
        for (MCTNode<T> child : current.getChildren()) {
            double value = child.ratio(this.pieceColor);
            if (value > bestValue) {
                bestValue = value;
                best = child.getTransition();
            }
        }

        boolean falseMove = true;
        for(Move move: allLegalMoves) {
            BoardPosition oldB = move.getOldBoardPosition();
            BoardPosition newB = move.getNewBoardPosition();
            if(oldB.equals(best.getOldBoardPosition()) && newB.equals(best.getNewBoardPosition())) {
                falseMove = false;
            }
        }

        if(falseMove) {
            best = allLegalMoves.get(ThreadLocalRandom.current().nextInt(allLegalMoves.size()));
        }

        System.out.printf("Best Move for %s: %s with eval of %s%n", this.pieceColor, best, bestValue);
        System.out.println("------------------------------------");


        chessGame.getGameState().movePieceWithLegalCheck(chessGame.getGameState().getPieceAtPosition(best.getOldBoardPosition()), best.getNewBoardPosition());
    }

    private MCTNode<T> selection() {
        MCTNode<T> n = current;
        MCTNode<T> next;
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
                    next = new MCTNode<>(n, transition, current.getGameState().isGameFinished(),current.getGameState());
                }
            }
            n = next;
        } while (!n.isLeaf());
        return n;
    }

    public Move selectTransition(MCTNode<T> node, final PieceColor player) {
        double v = Double.NEGATIVE_INFINITY;
        Move best = null;
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(current.getGameState(), current.getGameState().getTurnColor());
        for (Move transition : allLegalMoves) {
            MCTNode<T> n = node.getChild(transition);
            if (n == null) {
                // unexplored path
                return transition;
            }
            if (!n.isTerminal()) {
                // child already explored and non terminal
                long simulations = n.simulations();
                assert simulations > 0;
                long wins = n.wins(player);
                double value = (simulations == 0 ? 0 : wins / simulations + C * sqrt( node.simulations()) / simulations);
                if (value > v) {
                    v = value;
                    best = transition;
                }
            }
        }
        return best;
    }

    private MCTNode<T> expansion(final MCTNode<T> leaf) {
        if (leaf.isTerminal()) {
            return leaf;
        }
        Move transition = expansionTransition();
        if (transition != null) {
            // expand the path with the chosen transition

            current.getGameState().movePieceWithLegalCheck(current.getGameState().getPieceAtPosition(transition.getOldBoardPosition()), transition.getNewBoardPosition());
            return new MCTNode<>(leaf, transition, current.getGameState().isGameFinished(),current.getGameState());
        } else {
            return leaf;
        }
    }

    public Move expansionTransition() {
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(current.getGameState(), this.pieceColor);

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
            boolean moveSuccess = current.getGameState().movePieceWithLegalCheck(current.getGameState().getPieceAtPosition(transition.getOldBoardPosition()), transition.getNewBoardPosition());
            transitions.add(transition);
        }

        PieceColor winner = current.getGameState().getWinnerColor();

        // undo
        current.setGameState(gameStateSave);
        return winner;
    }

    public Move simulationTransition() {
        List<Move> allLegalMoves = MoveCalculator.getAllLegalMoves(current.getGameState(), current.getGameState().getTurnColor());

        if (allLegalMoves.isEmpty()) {
            return null;
        }
        return allLegalMoves.get((int) Math.floor(Math.random() * allLegalMoves.size()));
    }

    private void backPropagation(MCTNode<T> expandedNode, final PieceColor winner) {
        MCTNode<T> n = expandedNode;
        while (n != null) {
            n.result(winner);
            MCTNode<T> parent = n.getParent();
            if (parent == null) {
                // root reached
                break;
            }
            n = parent;
        }
    }



}
