package ui;

import java.util.*;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import facade.NotificationHandler;
import facade.ServerFacade;
import facade.WebSocketFacade;
import model.CreateGameResponse;
import model.GameData;
import model.ListGamesResponse;
import model.UserAndAuthResponse;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

public class Menu implements NotificationHandler {
    ServerFacade facade;
    WebSocketFacade wsfacade;
    String user;
    String authToken;
    ArrayList<GameData> gameList = new ArrayList<>();
    ui.ChessBoard chessboard = new ChessBoard();
    chess.ChessBoard currBoard;
    Boolean isWhite = true;
    Boolean isObserver = false;
    Integer currGameID = 0;
    List<Integer> brickedGames = new ArrayList<>();

    public void setIsWhite(Boolean white) {
        isWhite = white;
    }

    public Menu() throws Exception {
        this.facade = new ServerFacade(8080);
        this.wsfacade = new WebSocketFacade("http://localhost:8080",this);
    }

    public void setCurrGameID(Integer currGameID) {
        this.currGameID = currGameID;
    }

    public Integer getCurrGameID() {
        return currGameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.deepEquals(brickedGames, menu.brickedGames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brickedGames);
    }

    public static void main(String[] args) throws Exception {
        Menu menu = new Menu();
        Map<String, Integer> colMap = new HashMap<>() {{
            put("a", 8);
            put("b", 7);
            put("c", 6);
            put("d", 5);
            put("e", 4);
            put("f", 3);
            put("g", 2);
            put("h", 1);
        }};


        boolean loggedIn = false;
        boolean inGame = false;
        menu.setIsWhite(true);
        String prefix = ES.SET_TEXT_COLOR_GREEN;
        Scanner scanner = new Scanner(System.in);
        System.out.print(ES.SET_TEXT_COLOR_WHITE);
        System.out.print("\n=================================\n");
        System.out.println("\uD83D\uDD79 " + ES.SET_TEXT_BOLD + "Welcome to Big Chess Game 2" + ES.RESET_TEXT_BOLD_FAINT +" \uD83D\uDD79");
        System.out.print("=================================\n");
        System.out.print("\u26A0 Type \"help\" into the console to get started! \u26A0 \n\n");

        // PRE-LOGIN UI

        while(!loggedIn) {
            try {
                System.out.print("\uD83D\uDD12 [LOGGED OUT] >> ");

                String line = scanner.nextLine();

                if (line.equalsIgnoreCase("quit")) {
                    System.out.print("  \uD83D\uDD79 [GAME] Thanks for playing. Goodbye!\n\n");
                    break;
                } else if (line.equalsIgnoreCase("help")) {
                    System.out.print(prefix + "  register" + ES.SET_TEXT_COLOR_WHITE + " - to create an account\n");
                    System.out.print(prefix + "  login" + ES.SET_TEXT_COLOR_WHITE + " - to play chess\n");
                    System.out.print(prefix + "  quit" + ES.SET_TEXT_COLOR_WHITE + " - to leave the game\n");
                    System.out.print(prefix + "  help" + ES.SET_TEXT_COLOR_WHITE + " - with possible commands\n");
                } else if (line.equalsIgnoreCase("login")) {
                    System.out.print("  \uD83D\uDD79 [GAME] Please enter your username: ");
                    String username = scanner.nextLine();
                    System.out.print("  \uD83D\uDD79 [GAME] Please enter your password: ");
                    String password = scanner.nextLine();
                    UserAndAuthResponse response = ServerFacade.login(username, password);
                    menu.user = response.username();
                    menu.authToken = response.authToken();
                    loggedIn = true;
                    System.out.print("  \uD83D\uDD79 [GAME] Welcome, " + menu.user + "! Type \"help\" into the console to see available commands.\n");

                } else if (line.equalsIgnoreCase("register")) {

                    System.out.print("  \uD83D\uDD79 [GAME] Please enter a username: ");
                    String username = scanner.nextLine();
                    while (Objects.equals(username, "")) {
                        System.out.print("  \uD83D\uDD79 [GAME] Sorry, username cannot be blank. Please enter a username: ");
                        username = scanner.nextLine();
                    }
                    System.out.print("  \uD83D\uDD79 [GAME] Please enter an email: ");
                    String email = scanner.nextLine();
                    System.out.print("  \uD83D\uDD79 [GAME] Please choose a password: ");
                    String password = scanner.nextLine();
                    System.out.print("  \uD83D\uDD79 [GAME] Please confirm your password: ");
                    String passwordConfirm = scanner.nextLine();
                    if (!Objects.equals(password, passwordConfirm)) {
                        System.out.print("  \uD83D\uDD79 [GAME] Error! Passwords do not match. Registration failed.\n");
                    } else {
                        UserAndAuthResponse response = ServerFacade.register(username, password, email);
                        System.out.print("  \uD83D\uDD79 [GAME] Registration successful. Logging you in...\n");
                        loggedIn = true;
                        menu.user = response.username();
                        menu.authToken = response.authToken();
                        System.out.print("  \uD83D\uDD79 [GAME] Welcome, " + menu.user + "! Type \"help\" into the console to see available commands.\n");
                    }
                } else {
                    System.out.print("  \uD83D\uDD79 [GAME] Sorry, I don't know that command. Try typing \"help\" into the console for a list of available commands.\n");
                }
            } catch (Exception e) {
                System.out.print("  \uD83D\uDD79 [GAME] Sorry, " + e + "\n");
            }





            /// LOGGED IN LOOP
            while(loggedIn) {

                try {
                    System.out.print("\uD83D\uDFE9 ["+ menu.user + "] >> ");
                    String input = scanner.nextLine();

                    if (input.equalsIgnoreCase("logout")) {
                        System.out.print("  \uD83D\uDD79 [GAME] Thanks for playing. Goodbye!\n");
                        ServerFacade.logout(menu.authToken);
                        loggedIn = false;
                        break;

                    } else if (input.equalsIgnoreCase("help")) {
                        System.out.print(prefix + "  create" + ES.SET_TEXT_COLOR_WHITE + " - to create a game\n");
                        System.out.print(prefix + "  list" + ES.SET_TEXT_COLOR_WHITE + " - to list all games\n");
                        System.out.print(prefix + "  join" + ES.SET_TEXT_COLOR_WHITE + " - to join a game\n");
                        System.out.print(prefix + "  observe" + ES.SET_TEXT_COLOR_WHITE + " - to watch a game\n");
                        System.out.print(prefix + "  logout" + ES.SET_TEXT_COLOR_WHITE + " - to log out\n");
                        System.out.print(prefix + "  help" + ES.SET_TEXT_COLOR_WHITE + " - to see possible command options\n");

                    } else if (input.equalsIgnoreCase("create")) {
                        System.out.print("  \uD83D\uDD79 [GAME] Enter your game name here: ");
                        String gameName = scanner.nextLine();
                        CreateGameResponse response =  ServerFacade.createGame(menu.authToken,gameName);
                        System.out.print("  \uD83D\uDD79 [GAME] Great! Game has been created with name " + prefix +  gameName + ES.SET_TEXT_COLOR_WHITE + " and ID " + prefix +  response.gameID() + ES.SET_TEXT_COLOR_WHITE + "!\n");

                    } else if (input.equalsIgnoreCase("list")) {
                        ListGamesResponse response =  ServerFacade.listGames(menu.authToken);
                        menu.gameList.clear();
                        if(!response.games().isEmpty()) {
                            int i = 0;
                            System.out.print("  \uD83D\uDD79 [GAME] Here are all the current games. Remember the number if you'd like to join!\n");
                            System.out.printf(prefix + "       %-5s %-15s %-15s %-15s\n", "#", "Name", "Black Player","White Player" + ES.SET_TEXT_COLOR_WHITE);
                            for(GameData g : response.games()) {
                                i++;
                                menu.gameList.add(g);
                                System.out.printf("       %-5s %-15s %-15s %-15s\n", i, g.gameName(), g.blackUsername(),g.whiteUsername());
                            }
                        } else {
                            System.out.print("  \uD83D\uDD79 [GAME] Sorry, looks like there are no games. Type \"create\" to make one!\n");
                        }

                    } else if (input.equalsIgnoreCase("join")) {
                        System.out.print("  \uD83D\uDD79 [GAME] Please enter the game number: ");
                        String gameNum = scanner.nextLine();
                        System.out.print("  \uD83D\uDD79 [GAME] Please choose (by typing) WHITE or BLACK: ");
                        String color = scanner.nextLine();
                        ChessGame.TeamColor teamColor;
                        int gameID = menu.gameList.get((Integer.parseInt(gameNum)-1)).gameID();

                        menu.currGameID = gameID;

                        if(color.equalsIgnoreCase("BLACK")) {
                            teamColor = ChessGame.TeamColor.BLACK;
                            menu.isObserver = false;
                            menu.setIsWhite(false);
                        } else if (color.equalsIgnoreCase("WHITE")) {
                            teamColor = ChessGame.TeamColor.WHITE;
                            menu.isObserver = false;
                            menu.setIsWhite(true);
                        } else {
                            teamColor = null;
                            menu.isObserver = true;
                            menu.setIsWhite(true);
                        }

                        if(menu.isObserver) {
                            JoinObserver joinObserver = new JoinObserver(menu.authToken, gameID);
                            menu.wsfacade.send(new Gson().toJson(joinObserver));
                            inGame = true;

                            System.out.print("  \uD83D\uDD79 [GAME] Game joined as an observer.\n");
                        } else {
                            ServerFacade.joinGame(menu.authToken,color.toUpperCase(),gameID);
                            JoinPlayer joinPlayer = new JoinPlayer(menu.authToken, gameID, teamColor);
                            menu.wsfacade.send(new Gson().toJson(joinPlayer));
                            inGame = true;

                            System.out.print("  \uD83D\uDD79 [GAME] Game joined as " + color.toUpperCase() + " player!\n");
                        }


                    } else if (input.equalsIgnoreCase("observe")) {
                        System.out.print("  \uD83D\uDD79 [GAME] Please enter the game number: ");
                        String gameNum = scanner.nextLine();
                        int gameID = menu.gameList.get((Integer.parseInt(gameNum))-1).gameID();

                        menu.setCurrGameID(gameID);

                        ServerFacade.joinGame(menu.authToken,null,gameID);
                        JoinObserver joinObserver = new JoinObserver(menu.authToken,gameID);
                        menu.wsfacade.send(new Gson().toJson(joinObserver));
                        inGame = true;

                        System.out.print("  \uD83D\uDD79 [GAME] Game joined as an observer.\n");


                    } else {
                        System.out.print("  \uD83D\uDD79 [GAME] Sorry, I don't know that command. Try typing \"help\" into the console for a list of available commands.\n");
                    }





                } catch (Exception e) {
                    System.out.print("  \uD83D\uDD79 [GAME] Sorry, " + e + "\n");
                }

                while(inGame) {

                    try {
                        System.out.print("\uD83D\uDFE9 [ "+ menu.user + " - IN GAME ] >> ");
                        String input = scanner.nextLine();


                        if (input.equalsIgnoreCase("leave")) {
                            System.out.print("  \uD83D\uDD79 [GAME] Leaving current game.\n");

                            Leave leavePlayer = new Leave(menu.authToken, menu.currGameID);
                            menu.wsfacade.send(new Gson().toJson(leavePlayer));
                            inGame = false;
                            break;

                        } else if (input.equalsIgnoreCase("help")) {
                            System.out.print(prefix + "  redraw" + ES.SET_TEXT_COLOR_WHITE + " - to redraw the chessboard\n");
                            System.out.print(prefix + "  leave" + ES.SET_TEXT_COLOR_WHITE + " - to leave the current game\n");
                            System.out.print(prefix + "  move" + ES.SET_TEXT_COLOR_WHITE + " - to make a move\n");
                            System.out.print(prefix + "  resign" + ES.SET_TEXT_COLOR_WHITE + " - to resign the current game\n");
                            System.out.print(prefix + "  highlight" + ES.SET_TEXT_COLOR_WHITE + " - to highlight legal moves\n");
                            System.out.print(prefix + "  help" + ES.SET_TEXT_COLOR_WHITE + " - to see possible command options\n");

                        } else if (input.equalsIgnoreCase("move")) {
                            if (menu.brickedGames.) {
                                System.out.print("  \uD83D\uDD79 [GAME] Sorry, this game is over.");

                            } else {
                                System.out.print("  \uD83D\uDD79 [GAME] Enter STARTING position ROW: ");
                                int startRow = Integer.parseInt(scanner.nextLine());
                                System.out.print("  \uD83D\uDD79 [GAME] Enter STARTING position COLUMN: ");
                                String startCol = scanner.nextLine();
                                System.out.print("  \uD83D\uDD79 [GAME] Enter ENDING position ROW: ");
                                int endRow = Integer.parseInt(scanner.nextLine());
                                System.out.print("  \uD83D\uDD79 [GAME] Enter ENDING position COLUMN: ");
                                String endCol = scanner.nextLine();

                                int startColInt = colMap.get(startCol);
                                int endColInt = colMap.get(endCol);
                                ChessPiece.PieceType startPiece = menu.currBoard.getPiece(new ChessPosition(startRow, startColInt)).getPieceType();
                                ChessPiece.PieceType promoPiece = null;

                                if(((menu.isWhite && endRow == 8) || (!menu.isWhite && endRow == 1)) && startPiece == ChessPiece.PieceType.PAWN) {
                                    System.out.print("  \uD83D\uDD79 [GAME] CONGRATS! Select your promotion piece: (QUEEN, ROOK, KNIGHT, BISHOP)");
                                    String promoPieceString = scanner.nextLine();
                                    promoPieceString = promoPieceString.toUpperCase();

                                    promoPiece = switch (promoPieceString) {
                                        case "QUEEN" -> ChessPiece.PieceType.QUEEN;
                                        case "ROOK" -> ChessPiece.PieceType.ROOK;
                                        case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
                                        case "BISHOP" -> ChessPiece.PieceType.BISHOP;
                                        default -> ChessPiece.PieceType.PAWN;
                                    };


                                }


                                if(!(startRow > 0 && startRow <= 8) || !(endRow > 0 && endRow <= 8) || !colMap.containsKey(startCol) || !colMap.containsKey(endCol) || promoPiece == ChessPiece.PieceType.PAWN) {
                                    System.out.print("  \uD83D\uDD79 [GAME] Sorry, those aren't valid inputs.\n");
                                } else {

                                    ChessPosition startPos = new ChessPosition(startRow,startColInt);
                                    ChessPosition endPos = new ChessPosition(endRow, endColInt);

                                    ChessMove move = new ChessMove(startPos, endPos, promoPiece);
                                    MakeMove makeMove = new MakeMove(menu.authToken, menu.currGameID, move);
                                    menu.wsfacade.send(new Gson().toJson(makeMove));
                                }
                            }


                        } else if (input.equalsIgnoreCase("redraw")) {
                            System.out.print("  \uD83D\uDD79 [GAME] Here's the current game board: \n");
                            menu.runBoard();

                        } else if (input.equalsIgnoreCase("resign")) {
                            System.out.print("  \uD83D\uDD79 [GAME] Are you sure you want to resign? (YES / NO)");
                            String confirmResign = scanner.nextLine().toUpperCase();
                            if(confirmResign.equals("YES")) {
                                menu.brickedGames.add(menu.currGameID);
                                Resign resign = new Resign(menu.authToken, menu.currGameID);
                                menu.wsfacade.send(new Gson().toJson(resign));
                            } else {
                                System.out.print("  \uD83D\uDD79 [GAME] Invalid response. Not resigning.");
                            }


                        }


                    } catch (Exception e) {
                        System.out.print("  \uD83D\uDD79 [GAME] Sorry, " + e + "\n");
                    }

                }

            }
        }
    }

    @Override
    public void onMessage(String json) throws Exception {
        ServerMessage notification = new Gson().fromJson(json, ServerMessage.class);

        switch (notification.getServerMessageType()) {
            case ERROR -> printError(json);
            case NOTIFICATION -> printNotification(json);
            case LOAD_GAME -> printLoadGame(json);
        }
    }

    private void printLoadGame(String json) throws Exception {
        //deserialize json into the game board and print the board
        LoadGame loadGame = new Gson().fromJson(json, LoadGame.class);
        GameData gameData = loadGame.getGame();
        currBoard = null;
        currBoard = gameData.game().getBoard();
        runBoard();
        if(gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) || gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            System.out.print("\n  \uD83D\uDD79 [GAME] CHECKMATE! Game is over.\n");
            brickedGames.add(currGameID);
        }
        System.out.print("\n \uD83D\uDFE9 ["+ this.user + " - IN GAME ] >> ");



    }

    private void runBoard() throws Exception {
        chessboard.run(currBoard, isWhite);

    }

    private void printNotification(String json) {
        Notification notification = new Gson().fromJson(json, Notification.class);
        String message = notification.getMessage();
        System.out.print("\n  \uD83D\uDD79 [GAME] NOTIFICATION: " + message + "\n");
        System.out.print("\uD83D\uDFE9 ["+ this.user + " - IN GAME ] >> ");

    }

    private void printError(String json) {
        Error error = new Gson().fromJson(json, Error.class);
        String message = error.getMessage();
        System.out.print("\n  \uD83D\uDD79 [GAME] ERROR: " + message + "\n");
        System.out.print("\uD83D\uDFE9 ["+ this.user + " - IN GAME ] >> ");
    }
}
