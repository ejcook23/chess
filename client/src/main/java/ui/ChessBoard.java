package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import ui.*;
import chess.*;

import static chess.ChessGame.TeamColor.BLACK;
import static ui.ES.*;

public class ChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";
    static ArrayList<String> boardArray = new ArrayList<>();
    static ArrayList<String> colorArray = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        run();
    }

    public static void run() throws Exception {
        chess.ChessBoard board = new chess.ChessBoard();
        board.resetBoard();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        populateBoardArrays(out, board, boardArray, colorArray);
        printBoard(out, true,boardArray, colorArray);

        out.print("\n");

        printBoard(out, false,boardArray, colorArray);

        out.print("\n");

        //out.print(boardArray.toString());
        //out.print(colorArray.toString());

        //out.print("\n");

        //printSquare(out, "white","black",BLACK_KING);






    }

    public static void populateBoardArrays(PrintStream out, chess.ChessBoard board, ArrayList<String> boardArray, ArrayList<String> colorArray) {

        String pieceString = null;
        for(int row = 1; row > 0 && row < 9;) {
            for(int col = 1; col > 0 && col < 9;) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(row,col));

                if (currPiece != null) {
                    ChessGame.TeamColor color = currPiece.getTeamColor();

                    // SETS THE PIECE STRING TO THE CORRECT TYPE FOR ITS COLOR
                    switch(currPiece.getPieceType() ) {
                        case KING:
                            if(color == BLACK) {
                                pieceString = BLACK_KING;
                            } else {
                                pieceString = WHITE_KING;
                            }
                            break;
                        case QUEEN:
                            if(color == BLACK) {
                                pieceString = BLACK_QUEEN;
                            } else {
                                pieceString = WHITE_QUEEN;
                            }
                            break;
                        case KNIGHT:
                            if(color == BLACK) {
                                pieceString = BLACK_KNIGHT;
                            } else {
                                pieceString = WHITE_KNIGHT;
                            }
                            break;
                        case BISHOP:
                            if(color == BLACK) {
                                pieceString = BLACK_BISHOP;
                            } else {
                                pieceString = WHITE_BISHOP;
                            }
                            break;
                        case ROOK:
                            if(color == BLACK) {
                                pieceString = BLACK_ROOK;
                            } else {
                                pieceString = WHITE_ROOK;
                            }
                            break;
                        case PAWN:
                            if(color == BLACK) {
                                pieceString = BLACK_PAWN;
                            } else {
                                pieceString = WHITE_PAWN;
                            }
                            break;

                    }

                    // ADD COLOR TO ARRAY
                    if(color == BLACK) {
                        colorArray.add("BLACK");
                    } else {
                        colorArray.add("WHITE");
                    }

                    // ADD PIECE TO ARRAY
                    boardArray.add(pieceString);
                } else {
                    colorArray.add("null");
                    boardArray.add(null);
                }


                col++;
            }
            row++;
        }


    }

    public static void printBoard(PrintStream out, Boolean whiteOnBottom, ArrayList<String> boardArray, ArrayList<String> colorArray) {
        int rowCounter = 0;
        int rowCounterRev = 8;
        List<String> messages = Arrays.asList("a", "b", "c", "d", "e","f","g","h");

        // DRAW BOARD
        if (whiteOnBottom) {

            // LETTERS TOP
            printSquare(out,"grey","uhh", EMPTY);
            for(String m : messages) {
                printSquare(out,"grey","uhh", " " + m + " ");
            }
            printSquare(out,"grey","uhh", EMPTY);

            out.print(SET_BG_COLOR_DARK_GREY + "\n");


            for (int counter = boardArray.size() - 1; counter >= 0; counter--) {
                // LEFT DESCENDING NUM
                if(counter%8 == 7) {
                    printSquare(out,"grey","uhh", " " + (rowCounterRev) + " ");
                }

                // CENTER BOARD
                if(counter%2 == 0) {
                    if(rowCounter%2 == 0) {
                        printSquare(out,"BLACK",colorArray.get(counter),boardArray.get(counter));
                    } else {
                        printSquare(out,"WHITE",colorArray.get(counter),boardArray.get(counter));
                    }

                } else {
                    if(rowCounter%2 == 0) {
                        printSquare(out,"WHITE",colorArray.get(counter),boardArray.get(counter));
                    } else {
                        printSquare(out,"BLACK",colorArray.get(counter),boardArray.get(counter));
                    }

                }

                // RIGHT DESCENDING NUM
                if(counter%8 == 0) {
                    printSquare(out,"grey","uhh", " " + (rowCounterRev) + " ");
                    out.print(SET_BG_COLOR_DARK_GREY + "\n");
                    rowCounter++;
                    rowCounterRev--;
                }
            }

            // LETTERS BOTTOM
            printSquare(out,"grey","uhh", EMPTY);
            for(String m : messages) {
                printSquare(out,"grey","uhh", " " + m + " ");
            }
            printSquare(out,"grey","uhh", EMPTY);
            out.print(SET_BG_COLOR_DARK_GREY + "\n");

        } else {

            printSquare(out,"grey","uhh", EMPTY);
            for(int i = messages.size()-1; i >= 0; i--) {
                printSquare(out,"grey","uhh", " " + messages.get(i) + " ");
            }
            printSquare(out,"grey","uhh", EMPTY);
            out.print(SET_BG_COLOR_DARK_GREY + "\n");


            for (int counter = 0; counter <= boardArray.size() - 1; counter++) {
                if(counter%8 == 0) {
                    printSquare(out,"grey","uhh", " " + (rowCounter+1) + " ");
                }
                if(counter%2 == 0) {
                    if(rowCounter%2 == 0) {
                        printSquare(out,"WHITE",colorArray.get(counter),boardArray.get(counter));
                    } else {
                        printSquare(out,"BLACK",colorArray.get(counter),boardArray.get(counter));
                    }

                } else {
                    if(rowCounter%2 == 0) {
                        printSquare(out,"BLACK",colorArray.get(counter),boardArray.get(counter));
                    } else {
                        printSquare(out,"WHITE",colorArray.get(counter),boardArray.get(counter));
                    }

                }
                if(counter%8 == 7) {
                    printSquare(out,"grey","uhh", " " + (rowCounter+1) + " ");
                    out.print(SET_BG_COLOR_DARK_GREY + "\n");
                    rowCounter++;
                    rowCounterRev--;
                }
            }

            printSquare(out,"grey","uhh", EMPTY);
            for(int i = messages.size()-1; i >= 0; i--) {
                printSquare(out,"grey","uhh", " " + messages.get(i) + " ");
            }
            printSquare(out,"grey","uhh", EMPTY);
            out.print(SET_BG_COLOR_DARK_GREY + "\n");

        }

        out.print(SET_TEXT_COLOR_WHITE);


    }



    public static void printSquare(PrintStream out, String squareColor, String teamColor, String piece) {
        // SET BACKGROUND COLOR
        if(Objects.equals(squareColor.toUpperCase(), "BLACK")) {
            out.print(SET_BG_COLOR_BLACK);
        } else if (Objects.equals(squareColor.toUpperCase(), "WHITE")){
            out.print(SET_BG_COLOR_WHITE);
        } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
        }

        // SET TEAM COLOR
        if(Objects.equals(teamColor.toUpperCase(), "BLACK")) {
            out.print(SET_TEXT_COLOR_RED);

        } else if (Objects.equals(teamColor.toUpperCase(), "WHITE")){
            out.print(SET_TEXT_COLOR_BLUE);
        } else {
            out.print(SET_TEXT_COLOR_WHITE);
        }

        // PRINT CONTENT
        if(piece == null) {
            out.print(EMPTY);
        } else {
            out.print(piece);
        }

    }



}
