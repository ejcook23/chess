package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {

    }

    private ChessBoard chessBoard;
    private TeamColor teamColor;
    private TeamColor teamTurn;
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
        Collection<ChessMove> possibleMoves = new ArrayList<>();
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean inCheck = false;
        ChessPiece findPiece = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition kingPosition;
        kingPosition = findPiece(findPiece);

        // goes through all the board spots
        aBreak:
        for(int row = 1; row > 0 && row < 9;) {
            for(int col = 1; col > 0 && col < 9;) {
                // if it is an enemy color and not null
                ChessPosition testPos = new ChessPosition(row,col) ;
                if((chessBoard.getPiece(testPos).getTeamColor() != teamColor) && (chessBoard.getPiece(testPos) != null)) {
                    // see if any of its end positions are on the king spot
                    for(ChessMove move : chessBoard.getPiece(testPos).pieceMoves(chessBoard,testPos)) {
                        inCheck = (move.getEndPosition() == testPos);
                        if(inCheck) {
                            break aBreak;
                        }
                    }
                }
                col++;
            }
            row++;
        }
        return inCheck;
    }

    private ChessPosition findPiece(ChessPiece findPiece) {
        ChessPosition piecePos = null;
        bigBreak:
        for(int row = 1; row > 0 && row < 9;) {
            for(int col = 1; col > 0 && col < 9;) {
                if(chessBoard.getPiece(new ChessPosition(row,col)) == findPiece) {
                    piecePos = new ChessPosition(row,col);
                    break bigBreak;
                }
                col++;
            }
            row++;
        }
        return piecePos;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */


    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
