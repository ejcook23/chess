package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition startPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        int currRow = myPosition.getRow();
        int currCol = myPosition.getColumn();
        int eRow;
        int eCol;
        ChessPiece piece = board.getPiece(myPosition);
        
        // BISHOP MOVES
        if (piece.getPieceType() == PieceType.BISHOP) {

            int colDir = 1;
            int rowDir = 1;
            // UP AND TO THE RIGHT
            diagonalMoves(currRow + rowDir, currCol, colDir, board, startPos, possibleMoves);

            // DOWN AND TO THE RIGHT
            diagonalMoves(currRow - 1, currCol, 1, board, startPos, possibleMoves);

            // DOWN AND TO THE LEFT
            for (int r = currRow - 1, c = currCol - 1; r >= 0 && r < 8 && c >= 0 && c < 8;) {
                // create an instance for potential position going down and to the left
                ChessPosition potPos = new ChessPosition(r,c);

                // POTENTIAL POSITION IS EMPTY
                if(board.getPiece(potPos) == null) {
                    // creates a new, validated potential Move since there is nothing there
                    ChessMove potMove = new ChessMove(startPos,potPos,null);
                    // adds the potential move to the possible moves array list
                    possibleMoves.add(potMove);
                    r++;
                    c++;
                    continue;
                }

                // POTENTIAL POSITION CONTAINS AN ENEMY PIECE
                if(board.getPiece(potPos).pieceColor != this.pieceColor) {
                    // creates a new, validated potential Move since there is an enemy there
                    ChessMove potMove = new ChessMove(startPos,potPos,null);
                    // adds the potential move to the possible moves array list
                    possibleMoves.add(potMove);
                    break;
                }

                // POTENTIAL POSITION CONTAINS FRIENDLY PIECE, blocking the way
                else {
                    //stop looping, because it's a friendly piece and can't go further because its blocked
                    break;
                }
            }

            // UP AND TO THE LEFT
            for (int r = currRow + 1, c = currCol - 1; r >= 0 && r < 8 && c >= 0 && c < 8;) {
                // create an instance for potential position going up and to the left
                ChessPosition potPos = new ChessPosition(r,c);

                // POTENTIAL POSITION IS EMPTY
                if(board.getPiece(potPos) == null) {
                    // creates a new, validated potential Move since there is nothing there
                    ChessMove potMove = new ChessMove(startPos,potPos,null);
                    // adds the potential move to the possible moves array list
                    possibleMoves.add(potMove);
                    r++;
                    c++;
                    continue;
                }

                // POTENTIAL POSITION CONTAINS AN ENEMY PIECE
                if(board.getPiece(potPos).pieceColor != this.pieceColor) {
                    // creates a new, validated potential Move since there is an enemy there
                    ChessMove potMove = new ChessMove(startPos,potPos,null);
                    // adds the potential move to the possible moves array list
                    possibleMoves.add(potMove);
                    break;
                }

                // POTENTIAL POSITION CONTAINS FRIENDLY PIECE, blocking the way
                else {
                    //stop looping, because it's a friendly piece and can't go further because its blocked
                    break;
                }
            }


        }

        // I want to make a second instance of ChessPosition endPos that is the end move, then put them into
        // a chessmove object and then add it to the chessmove collection, then return it.
        return possibleMoves;
    }

    private void diagonalMoves(int currRow, int currCol, int colDir, ChessBoard board, ChessPosition startPos, Collection<ChessMove> possibleMoves) {
        for (int r = currRow, c = currCol + colDir; isInBounds(r, c); ) {
            // create an instance for potential position going up and to the right
            ChessPosition potPos = new ChessPosition(r, c);

            // POTENTIAL POSITION IS EMPTY
            if (board.getPiece(potPos) == null) {
                // creates a new, validated potential Move since there is nothing there
                ChessMove potMove = new ChessMove(startPos, potPos, null);
                // adds the potential move to the possible moves array list
                possibleMoves.add(potMove);
                r++;
                c++;
                continue;
            }

            // POTENTIAL POSITION CONTAINS AN ENEMY PIECE
            if (board.getPiece(potPos).pieceColor != this.pieceColor) {
                // creates a new, validated potential Move since there is an enemy there
                ChessMove potMove = new ChessMove(startPos, potPos, null);
                // adds the potential move to the possible moves array list
                possibleMoves.add(potMove);
                break;
            }

            // POTENTIAL POSITION CONTAINS FRIENDLY PIECE, blocking the way
            else {
                //stop looping, because it's a friendly piece and can't go further because its blocked
                break;
            }
        }
    }

    private static boolean isInBounds(int r, int c) {
        return r >= 1 && r <= 8 && c >= 1 && c <= 8;
    }
}
