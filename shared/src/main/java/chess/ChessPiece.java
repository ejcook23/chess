package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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

        ChessPiece piece = board.getPiece(myPosition);
        
        // BISHOP MOVES
        if (piece.getPieceType() == PieceType.BISHOP) {

            calcBishopMoves(board, myPosition, possibleMoves);

        }

        if (piece.getPieceType() == PieceType.KING) {

            calcKingMoves(board, myPosition, possibleMoves);

        }

        if (piece.getPieceType() == PieceType.KNIGHT) {

            calcKnightMoves(board, myPosition, possibleMoves);

        }

        if (piece.getPieceType() == PieceType.ROOK) {

            calcRookMoves(board, myPosition, possibleMoves);

        }

        if (piece.getPieceType() == PieceType.QUEEN) {

            calcRookMoves(board, myPosition, possibleMoves);
            calcBishopMoves(board, myPosition, possibleMoves);

        }

        if (piece.getPieceType() == PieceType.PAWN) {

            calcPawnMoves(board, myPosition, possibleMoves);


        }

        // I want to make a second instance of ChessPosition endPos that is the end move, then put them into
        // a chessmove object and then add it to the chessmove collection, then return it.
        return possibleMoves;
    }

    private void calcPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        int rowDir;
        int colDir;

        // IF PAWN IS WHITE
        if(this.pieceColor == ChessGame.TeamColor.WHITE) {

            // UP LEFT
            rowDir = 1;
            colDir = -1;
            pawnMove(board, myPosition, rowDir, colDir, possibleMoves);

            // UP
            rowDir = 1;
            colDir = 0;
            pawnMove(board, myPosition, rowDir, colDir, possibleMoves);

            // UP TWO
            if (myPosition.getRow() == 2 && board.getPiece(new ChessPosition(3, myPosition.getColumn())) == null) {
                rowDir = 2;
                colDir = 0;
                pawnMove(board, myPosition, rowDir, colDir, possibleMoves);
            }

            // UP RIGHT
            rowDir = 1;
            colDir = 1;
            pawnMove(board, myPosition, rowDir, colDir, possibleMoves);

        }

        // IF PAWN IS BLACK
        if(this.pieceColor == ChessGame.TeamColor.BLACK) {

            // DOWN LEFT
            rowDir = -1;
            colDir = -1;
            pawnMove(board, myPosition, rowDir, colDir, possibleMoves);

            // DOWN
            rowDir = -1;
            colDir = 0;
            pawnMove(board, myPosition, rowDir, colDir, possibleMoves);

            // DOWN TWO
            if (myPosition.getRow() == 7 && board.getPiece(new ChessPosition(6, myPosition.getColumn())) == null) {
                rowDir = -2;
                colDir = 0;
                pawnMove(board, myPosition, rowDir, colDir, possibleMoves);
            }

            // DOWN RIGHT
            rowDir = -1;
            colDir = 1;
            pawnMove(board, myPosition, rowDir, colDir, possibleMoves);

        }
    }

    private void calcRookMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        int rowDir;
        int colDir;

        // UP
        rowDir = 1;
        colDir = 0;
        diagonalMoves(rowDir, colDir, board, myPosition, possibleMoves);

        // RIGHT
        rowDir = 0;
        colDir = 1;
        diagonalMoves(rowDir, colDir, board, myPosition, possibleMoves);

        // DOWN
        rowDir = -1;
        colDir = 0;
        diagonalMoves(rowDir, colDir, board, myPosition, possibleMoves);

        // LEFT
        rowDir = 0;
        colDir = -1;
        diagonalMoves(rowDir, colDir, board, myPosition, possibleMoves);
    }

    private void pawnMove(ChessBoard board, ChessPosition myPosition, int rowDir, int colDir, Collection<ChessMove> possibleMoves) {
        int r = myPosition.getRow() + rowDir;
        int c = myPosition.getColumn() + colDir;

        if(isInBounds(r,c)) {

            ChessPosition potPos = new ChessPosition(r,c);

            if (promotionCheck(board, colDir, potPos)) {
                //checks to see if it is in the promotion lane for its color, then adds the potential promotions
                if(inPromotionLane(r)) {
                    PieceType[] promoPieces = {PieceType.ROOK, PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT};
                    for (PieceType promoPiece : promoPieces) {
                        ChessMove potMove = new ChessMove(myPosition, potPos, promoPiece);
                        // adds the potential move to the possible moves array list
                        possibleMoves.add(potMove);
                    }
                }
                else {
                    // creates a new, validated potential Move ONLY IF EMPTY AND IN SAME COLUMN (white)
                    ChessMove potMove = new ChessMove(myPosition, potPos, null);
                    // adds the potential move to the possible moves array list
                    possibleMoves.add(potMove);
                }
            }

            else {
                if((board.getPiece(potPos) != null && board.getPiece(potPos).pieceColor != this.pieceColor) && colDir != 0) {
                    // checks to see if in promotion lane, then adds potential promotions
                    if(inPromotionLane(r)) {
                        PieceType[] promoPieces = {PieceType.ROOK, PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT};
                        for (PieceType promoPiece : promoPieces) {
                            ChessMove potMove = new ChessMove(myPosition, potPos, promoPiece);
                            // adds the potential move to the possible moves array list
                            possibleMoves.add(potMove);
                        }
                    }
                    else {
                        // creates a new, validated potential Move since there is an enemy there (white), and not in the same column
                        ChessMove potMove = new ChessMove(myPosition, potPos, null);
                        // adds the potential move to the possible moves array list
                        possibleMoves.add(potMove);
                    }

                }

            }
        }
    }

    private static boolean promotionCheck(ChessBoard board, int colDir, ChessPosition potPos) {
        return board.getPiece(potPos) == null && colDir == 0;
    }

    private boolean inPromotionLane(int r) {
        return r == 8 && this.pieceColor == ChessGame.TeamColor.WHITE || r == 1 && this.pieceColor == ChessGame.TeamColor.BLACK;
    }

    private void calcKnightMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        int rowDir;
        int colDir;

        // UP L LEAN LEFT
        rowDir = 2;
        colDir = -1;
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        // UP L LEAN RIGHT
        rowDir = 2;
        colDir = 1;
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        // RIGHT L UP
        rowDir = 1;
        colDir = 2;
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        // RIGHT L DOWN
        rowDir = -1;
        colDir = 2;
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        // DOWN L RIGHT
        rowDir = -2;
        colDir = 1;
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        // DOWN L LEFT
        rowDir = -2;
        colDir = -1;
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        // LEFT L DOWN
        rowDir = -1;
        colDir = -2;
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        // LEFT L UP
        rowDir = 1;
        colDir = -2;
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);
    }

    private void calcKingMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        int colDir = 0;
        int rowDir = 1;
        // UP
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        colDir = 1;
        rowDir = 1;
        // UP RIGHT
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        colDir = 1;
        rowDir = 0;
        // RIGHT
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        colDir = 1;
        rowDir = -1;
        // DOWN RIGHT
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        colDir = 0;
        rowDir = -1;
        // DOWN
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        colDir = -1;
        rowDir = -1;
        // DOWN LEFT
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        colDir = -1;
        rowDir = 0;
        // LEFT
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);

        colDir = -1;
        rowDir = 1;
        // UP LEFT
        singleMove(board, myPosition, rowDir, colDir, possibleMoves);
    }



    private void singleMove(ChessBoard board, ChessPosition myPosition, int rowDir, int colDir, Collection<ChessMove> possibleMoves) {
        int r = myPosition.getRow() + rowDir;
        int c = myPosition.getColumn() + colDir;

        if(isInBounds(r,c)) {

            ChessPosition potPos = new ChessPosition(r,c);

            if (board.getPiece(potPos) == null) {
                // creates a new, validated potential Move since there is nothing there
                ChessMove potMove = new ChessMove(myPosition, potPos, null);
                // adds the potential move to the possible moves array list
                possibleMoves.add(potMove);

            }
            else {
                if((board.getPiece(potPos).pieceColor != this.pieceColor)) {
                    // creates a new, validated potential Move since there is an enemy there
                    ChessMove potMove = new ChessMove(myPosition, potPos, null);
                    // adds the potential move to the possible moves array list
                    possibleMoves.add(potMove);
                }

            }
        }
    }

    private void calcBishopMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        int colDir = 1;
        int rowDir = 1;
        // UP AND TO THE RIGHT
        diagonalMoves(rowDir, colDir, board, myPosition, possibleMoves);

        colDir = 1;
        rowDir = -1;
        // DOWN AND TO THE RIGHT
        diagonalMoves(rowDir, colDir, board, myPosition, possibleMoves);

        colDir = -1;
        rowDir = -1;
        // DOWN AND TO THE LEFT
        diagonalMoves(rowDir, colDir, board, myPosition, possibleMoves);

        colDir = -1;
        rowDir = 1;
        // UP AND TO THE LEFT
        diagonalMoves(rowDir, colDir, board, myPosition, possibleMoves);
    }

    private void diagonalMoves(int rowDir, int colDir, ChessBoard board, ChessPosition startPos, Collection<ChessMove> possibleMoves) {
        int r = startPos.getRow() + rowDir;
        int c = startPos.getColumn() + colDir;


        while(isInBounds(r, c)) {
            // create an instance for potential position going up and to the right
            ChessPosition potPos = new ChessPosition(r, c);

            // POTENTIAL POSITION IS EMPTY
            if (board.getPiece(potPos) == null) {
                // creates a new, validated potential Move since there is nothing there
                ChessMove potMove = new ChessMove(startPos, potPos, null);
                // adds the potential move to the possible moves array list
                possibleMoves.add(potMove);
                r = r + rowDir;
                c = c + colDir;
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

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }


}
