package isp.search.chess.util;

import isp.search.chess.GameState;
import isp.search.chess.Piece;
import isp.search.chess.enums.PieceColor;
import isp.search.chess.enums.PieceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class MoveCalculator {
    private static final int ROW_COUNT = 8;

    public static List<BoardPosition> getLegalMoves(GameState gameState, Piece piece) {
        return getLegalMoves(gameState, piece, true);
    }

    public static List<BoardPosition> getLegalMoves(GameState gameState, Piece piece, boolean checkForCheck) {

        List<BoardPosition> legalMoves = new ArrayList<>();


        if (piece != null) {
            //PAWN
            if (piece.getPieceType() == PieceType.PAWN) {
                if (piece.getPieceColor() == PieceColor.WHITE) {
                    //WHITE PAWNS
                    //if not at end of board
                    if (piece.getBoardY() < ROW_COUNT - 1) {

                        //move 2 forward
                        if (piece.getBoardY() == 1) {
                            if (isSquareEmpty(gameState, piece.getBoardX(), piece.getBoardY() + 1) && isSquareEmpty(gameState, piece.getBoardX(), piece.getBoardY() + 2)) {
                                legalMoves.add(new BoardPosition(piece.getBoardX(), piece.getBoardY() + 2));
                            }
                        }

                        //move 1
                        //move forward
                        if (isSquareEmpty(gameState, piece.getBoardX(), piece.getBoardY() + 1)) {
                            legalMoves.add(new BoardPosition(piece.getBoardX(), piece.getBoardY() + 1));
                        }

                        //capture left
                        if (piece.getBoardX() - 1 >= 0) {
                            if (!isSquareEmpty(gameState, piece.getBoardX() - 1, piece.getBoardY() + 1) && !isSquareFriendly(gameState, piece.getBoardX() - 1, piece.getBoardY() + 1, PieceColor.WHITE)) {
                                legalMoves.add(new BoardPosition(piece.getBoardX() - 1, piece.getBoardY() + 1));
                            }

                        }
                        //capture right
                        if (piece.getBoardX() + 1 <= ROW_COUNT - 1) {
                            if (!isSquareEmpty(gameState, piece.getBoardX() + 1, piece.getBoardY() + 1) && !isSquareFriendly(gameState, piece.getBoardX() + 1, piece.getBoardY() + 1, PieceColor.WHITE)) {
                                legalMoves.add(new BoardPosition(piece.getBoardX() + 1, piece.getBoardY() + 1));
                            }
                        }

                    }

                } else {
                    //BLACK PAWNS
                    //if not at end of board
                    if (piece.getBoardY() > 0) {

                        //move 2 forward
                        if (piece.getBoardY() == ROW_COUNT - 1 - 1) {
                            if (isSquareEmpty(gameState, piece.getBoardX(), piece.getBoardY() - 1) && isSquareEmpty(gameState, piece.getBoardX(), piece.getBoardY() - 2)) {
                                legalMoves.add(new BoardPosition(piece.getBoardX(), piece.getBoardY() - 2));
                            }
                        }

                        //move 1
                        //move forward
                        if (isSquareEmpty(gameState, piece.getBoardX(), piece.getBoardY() - 1)) {
                            legalMoves.add(new BoardPosition(piece.getBoardX(), piece.getBoardY() - 1));
                        }

                        //capture left
                        if (piece.getBoardX() - 1 >= 0) {
                            if (!isSquareEmpty(gameState, piece.getBoardX() - 1, piece.getBoardY() - 1) && !isSquareFriendly(gameState, piece.getBoardX() - 1, piece.getBoardY() - 1, PieceColor.BLACK)) {
                                legalMoves.add(new BoardPosition(piece.getBoardX() - 1, piece.getBoardY() - 1));
                            }

                        }
                        //capture right
                        if (piece.getBoardX() + 1 <= ROW_COUNT - 1) {
                            if (!isSquareEmpty(gameState, piece.getBoardX() + 1, piece.getBoardY() - 1) && !isSquareFriendly(gameState, piece.getBoardX() + 1, piece.getBoardY() - 1, PieceColor.BLACK)) {
                                legalMoves.add(new BoardPosition(piece.getBoardX() + 1, piece.getBoardY() - 1));
                            }
                        }

                    }
                }
            }

            //KNIGHT
            if (piece.getPieceType() == PieceType.KNIGHT) {
                BoardPosition[] theoritacalKnightMoves = { //TODO nicht jedes mal berechnen
                        new BoardPosition(piece.getBoardX() - 1, piece.getBoardY() + 2),
                        new BoardPosition(piece.getBoardX() + 1, piece.getBoardY() + 2),
                        new BoardPosition(piece.getBoardX() + 2, piece.getBoardY() + 1),
                        new BoardPosition(piece.getBoardX() + 2, piece.getBoardY() - 1),
                        new BoardPosition(piece.getBoardX() + 1, piece.getBoardY() - 2),
                        new BoardPosition(piece.getBoardX() - 1, piece.getBoardY() - 2),
                        new BoardPosition(piece.getBoardX() - 2, piece.getBoardY() - 1),
                        new BoardPosition(piece.getBoardX() - 2, piece.getBoardY() + 1),
                };

                List<BoardPosition> possibleKnightMoves = Arrays.stream(theoritacalKnightMoves)
                        .filter(tkm -> isSquareOnBoard(tkm.getBoardX(), tkm.getBoardY()))
                        .filter(tkm -> isSquareEmpty(gameState, tkm.getBoardX(), tkm.getBoardY()) || !isSquareFriendly(gameState, tkm.getBoardX(), tkm.getBoardY(), piece.getPieceColor()))
                        .collect(Collectors.toList());

                //add all moves to legal moves
                legalMoves.addAll(possibleKnightMoves);


            }

            //ROOK
            if (piece.getPieceType() == PieceType.ROOK) {

                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 1, 0));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, -1, 0));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 0, 1));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 0, -1));

            }

            //BISHOP
            if (piece.getPieceType() == PieceType.BISHOP) {

                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 1, 1));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, -1, 1));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 1, -1));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, -1, -1));

            }

            //KING
            if (piece.getPieceType() == PieceType.KING) {

                BoardPosition[] theoreticalKingMoves = {
                        new BoardPosition(piece.getBoardX(), piece.getBoardY() + 1),
                        new BoardPosition(piece.getBoardX() - 1, piece.getBoardY() + 1),
                        new BoardPosition(piece.getBoardX() - 1, piece.getBoardY()),
                        new BoardPosition(piece.getBoardX() - 1, piece.getBoardY() - 1),
                        new BoardPosition(piece.getBoardX(), piece.getBoardY() - 1),
                        new BoardPosition(piece.getBoardX() + 1, piece.getBoardY() - 1),
                        new BoardPosition(piece.getBoardX() + 1, piece.getBoardY()),
                        new BoardPosition(piece.getBoardX() + 1, piece.getBoardY() + 1),
                };


                List<BoardPosition> possibleKingMoves = Arrays.stream(theoreticalKingMoves)
                        .filter(tkm -> isSquareOnBoard(tkm.getBoardX(), tkm.getBoardY()))
                        .filter(tkm -> isSquareEmpty(gameState, tkm.getBoardX(), tkm.getBoardY()) || !isSquareFriendly(gameState, tkm.getBoardX(), tkm.getBoardY(), piece.getPieceColor()))
                        .collect(Collectors.toList());

                legalMoves.addAll(possibleKingMoves);

                //castle
                if (piece.getPieceColor() == PieceColor.BLACK) {
                    if (gameState.isCastleRightsBlackK()) {
                        if (isSquareEmpty(gameState, 5, 7) && isSquareEmpty(gameState, 6, 7)) {
                            if (!canOpponentPiecesSeeSquare(gameState, new BoardPosition(4, 7), piece.getPieceColor())
                                    && !canOpponentPiecesSeeSquare(gameState, new BoardPosition(5, 7), piece.getPieceColor())) {
                                legalMoves.add(new BoardPosition(piece.getBoardX() + 2, piece.getBoardY()));
                            }
                        }
                    }
                    if (gameState.isCastleRightsBlackQ()) {
                        if (isSquareEmpty(gameState, 3, 7) && isSquareEmpty(gameState, 2, 7) && isSquareEmpty(gameState, 1, 7)) {
                            if (!canOpponentPiecesSeeSquare(gameState, new BoardPosition(4, 7), piece.getPieceColor())
                                    && !canOpponentPiecesSeeSquare(gameState, new BoardPosition(3, 7), piece.getPieceColor())
                                    && !canOpponentPiecesSeeSquare(gameState, new BoardPosition(2, 7), piece.getPieceColor())) {
                                legalMoves.add(new BoardPosition(piece.getBoardX() - 2, piece.getBoardY()));
                            }
                        }
                    }
                } else if (piece.getPieceColor() == PieceColor.WHITE) {
                    if (gameState.isCastleRightsWhiteK()) {
                        if (isSquareEmpty(gameState, 5, 0) && isSquareEmpty(gameState, 6, 0)) {
                            legalMoves.add(new BoardPosition(piece.getBoardX() + 2, piece.getBoardY()));
                        }
                    }
                    if (gameState.isCastleRightsWhiteQ()) {
                        if (isSquareEmpty(gameState, 3, 0) && isSquareEmpty(gameState, 2, 0) && isSquareEmpty(gameState, 1, 0)) {
                            legalMoves.add(new BoardPosition(piece.getBoardX() - 2, piece.getBoardY()));
                        }
                    }
                }
            }

            //QUEEN
            if (piece.getPieceType() == PieceType.QUEEN) {

                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 1, 0));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, -1, 0));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 0, 1));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 0, -1));

                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 1, 1));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, -1, 1));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, 1, -1));
                legalMoves.addAll(scanLineForLegalMoves(gameState, piece, -1, -1));

            }


            //remove check putting moves
            if (checkForCheck) {
                ListIterator<BoardPosition> legalMovesIterator = legalMoves.listIterator();
                while (legalMovesIterator.hasNext()) {
                    BoardPosition legalMove = legalMovesIterator.next();

                    //simulate move
                    //clone game state
                    String currentGameFenString = FenLoader.generateFenStringFromGameState(gameState);
                    GameState clonedGameState = FenLoader.loadGameStateFromFenString(currentGameFenString);

                    //get piece
                    Piece clonedStatePiece = clonedGameState.getPieceAtPosition(piece.getBoardPosition());

                    //hard move piece
                    clonedGameState.hardMovePiece(clonedStatePiece, legalMove);

                    //check for check
                    BoardPosition clonedKingPosition = clonedGameState.getPieces().stream()
                            .filter(p -> p.getPieceType() == PieceType.KING && p.getPieceColor() == clonedStatePiece.getPieceColor())
                            .findFirst()
                            .get()
                            .getBoardPosition();

                    AtomicBoolean foundCheck = new AtomicBoolean(false);
                    clonedGameState.getPieces().stream()
                            .filter(p -> p.getPieceColor() != clonedStatePiece.getPieceColor()) //filter opponents
                            .forEach(opponentsPiece -> {
                                if (foundCheck.get()) return;

                                List<BoardPosition> legalMovesFromOpponent = getLegalMoves(clonedGameState, opponentsPiece, false);

                                foundCheck.set(
                                        legalMovesFromOpponent.stream()
                                                .anyMatch(opponentLegalMove -> opponentLegalMove.equals(clonedKingPosition)));

                            });

                    //remove move if results in check
                    if (foundCheck.get()) legalMovesIterator.remove();
                }
            }

        }

        return legalMoves;
    }


    public static List<Move> getAllLegalMoves(GameState currentGameState, PieceColor pieceColor) {

        List<Move> allLegalMoves = new ArrayList<>();

        //get random piece that is movable
        List<Piece> allPieces = currentGameState.getPieces().stream()
                .filter(p -> p.getPieceColor() == pieceColor)
                .toList();

        for (Piece piece : allPieces) {
            getLegalMoves(currentGameState, piece).stream().forEach(moveToPos -> {
                allLegalMoves.add(new Move(piece.getBoardPosition(), moveToPos));
            });
        }

        return allLegalMoves;
    }


    //--position heper--

    //check if square is not occupied by other piece
    private static boolean isSquareEmpty(GameState gameState, int boardX, int boardY) {
        return !gameState.getPieces().stream()
                .anyMatch(piece -> (piece.getBoardPosition().equals(new BoardPosition(boardX, boardY))));
    }

    //check if square has a piece with the same color as given
    private static boolean isSquareFriendly(GameState gameState, int boardX, int boardY, PieceColor ownPieceColor) {
        Piece piece = gameState.getPieceAtPosition(new BoardPosition(boardX, boardY));

        if (piece == null) return false;

        return piece.getPieceColor() == ownPieceColor;
    }

    //check board bounds
    private static boolean isSquareOnBoard(int boardX, int boardY) {
        return boardX >= 0 && boardX <= ROW_COUNT - 1 && boardY >= 0 && boardY <= ROW_COUNT - 1;
    }

    /*const isKingInCheck = (gameState, isWhite) => {
        const kingToTestInCheck =  gameState.pieces.find(piece => piece.isWhite == isWhite && piece.type == PieceType.King);
        const attackingPieces = gameState.pieces.filter(piece => piece.isWhite == !isWhite);

        return attackingPieces.some(piece => {
            const pieceMoves = getLegalMoves(gameState, piece, false);

            return pieceMoves.some(move => move.newBoardX == kingToTestInCheck.boardX && move.newBoardY == kingToTestInCheck.boardY);
        });
    }*/

    //scans line for legal moves from piece, line direction is determined by x- and yIncrement
    private static List<BoardPosition> scanLineForLegalMoves(GameState gameState, Piece piece, int xIncrement, int yIncrement) {

        List<BoardPosition> legalMoves = new ArrayList<>();

        int x = piece.getBoardX() + xIncrement;
        int y = piece.getBoardY() + yIncrement;

        while (isSquareOnBoard(x, y)) {

            if (isSquareEmpty(gameState, x, y)) {
                legalMoves.add(new BoardPosition(x, y));
            } else {
                if (!isSquareFriendly(gameState, x, y, piece.getPieceColor())) {
                    legalMoves.add(new BoardPosition(x, y));
                }
                break;
            }

            x += xIncrement;
            y += yIncrement;
        }

        return legalMoves;
    }


    private static boolean canOpponentPiecesSeeSquare(GameState gameState, BoardPosition boardPosition, PieceColor ownColor) {


        AtomicBoolean foundCheck = new AtomicBoolean(false);
        gameState.getPieces().stream()
                .filter(p -> p.getPieceColor() != ownColor) //filter opponents
                .forEach(opponentsPiece -> {
                    if (foundCheck.get()) return;

                    List<BoardPosition> legalMovesFromOpponent = getLegalMoves(gameState, opponentsPiece, false);

                    foundCheck.set(
                            legalMovesFromOpponent.stream()
                                    .anyMatch(opponentLegalMove -> opponentLegalMove.equals(boardPosition))
                    );

                });

        return foundCheck.get();
    }

}
