package ui;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import facade.NotificationHandler;
import facade.ServerFacade;
import facade.WebSocketFacade;
import model.CreateGameResponse;
import model.GameData;
import model.ListGamesResponse;
import model.UserAndAuthResponse;
import webSocketMessages.serverMessages.ServerMessage;

public class Menu implements NotificationHandler {
    ServerFacade facade;
    WebSocketFacade wsfacade;
    String user;
    String authToken;
    ArrayList<GameData> gameList = new ArrayList<>();

    public Menu() throws Exception {
        this.facade = new ServerFacade(8080);
        this.wsfacade = new WebSocketFacade("http://localhost:8080",this);
    }

    public static void main(String[] args) throws Exception {
        Menu menu = new Menu();

        boolean loggedIn = false;
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
                        int gameID = menu.gameList.get((Integer.parseInt(gameNum)-1)).gameID();

                        ServerFacade.joinGame(menu.authToken,color.toUpperCase(),gameID);
                        menu.wsfacade.send()

                        System.out.print("  \uD83D\uDD79 [GAME] Game joined as " + color.toUpperCase() + " player!\n");
                        ChessBoard.run();


                    } else if (input.equalsIgnoreCase("observe")) {
                        System.out.print("  \uD83D\uDD79 [GAME] Please enter the game number: ");
                        String gameNum = scanner.nextLine();
                        int gameID = menu.gameList.get((Integer.parseInt(gameNum))-1).gameID();

                        ServerFacade.joinGame(menu.authToken,null,gameID);
                        System.out.print("  \uD83D\uDD79 [GAME] Game joined as an observer.\n");
                        ChessBoard.run();

                    } else {
                        System.out.print("  \uD83D\uDD79 [GAME] Sorry, I don't know that command. Try typing \"help\" into the console for a list of available commands.\n");
                    }





                } catch (Exception e) {
                    System.out.print("  \uD83D\uDD79 [GAME] Sorry, " + e + "\n");
                }

            }
        }
    }

    @Override
    public void notify(ServerMessage notification) {

    }
}
