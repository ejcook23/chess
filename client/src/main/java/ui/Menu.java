package ui;

import java.util.Objects;
import java.util.Scanner;
import facade.ServerFacade;
import model.UserAndAuthResponse;

public class Menu {
    static ServerFacade facade;
    static String user;


    public static void main(String[] args) throws Exception {

        facade = new ServerFacade(8080);
        boolean loggedIn = false;

        System.out.print(ES.SET_TEXT_COLOR_WHITE);
        System.out.print("\n=================================\n");
        System.out.println("\uD83D\uDD79 " + ES.SET_TEXT_BOLD + "Welcome to Big Chess Game 2" + ES.RESET_TEXT_BOLD_FAINT +" \uD83D\uDD79");
        System.out.print("=================================\n");
        System.out.print("\u26A0 Type \"help\" into the console to get started! \u26A0 \n\n");

        // PRE-LOGIN UI

        while(!loggedIn) {
            System.out.print("\uD83D\uDD12 [LOGGED OUT] >> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();

            if(line.equalsIgnoreCase("quit")) {
                System.out.print("  \uD83D\uDD79 [GAME] Thanks for playing. Goodbye!\n\n");
                break;
            } else if (line.equalsIgnoreCase("help")) {
                String prefix = ES.SET_TEXT_COLOR_GREEN;
                System.out.print(prefix + "  register" + ES.SET_TEXT_COLOR_WHITE + " - to create an account\n");
                System.out.print(prefix + "  login" + ES.SET_TEXT_COLOR_WHITE + " - to play chess\n");
                System.out.print(prefix + "  quit" + ES.SET_TEXT_COLOR_WHITE + " - to leave the game\n");
                System.out.print(prefix + "  help" + ES.SET_TEXT_COLOR_WHITE + " - with possible commands\n");
            } else if (line.equalsIgnoreCase("login")) {
                System.out.print("  \uD83D\uDD79 [GAME] Please enter your username: ");
                String username = scanner.nextLine();
                System.out.print("  \uD83D\uDD79 [GAME] Please enter your password: ");
                String password = scanner.nextLine();
                UserAndAuthResponse response =  ServerFacade.login(username,password);
                if(response != null) {
                    user = response.username();
                    loggedIn = true;
                } else {
                    System.out.print("  \uD83D\uDD79 [GAME] Incorrect credentials. \n");
                }



            } else if (line.equalsIgnoreCase("register")) {

                    System.out.print("  \uD83D\uDD79 [GAME] Please enter a username: ");
                    String username = scanner.nextLine();
                    while(Objects.equals(username, "")) {
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
                        UserAndAuthResponse response = ServerFacade.register(username,password,email);
                        System.out.print("  \uD83D\uDD79 [GAME] Registration successful. Logging you in...\n" );
                        loggedIn = true;
                        user = response.username();

                    }
            } else {
                System.out.print("  \uD83D\uDD79 [GAME] Sorry, I don't know that command. Try typing \"help\" into the console for a list of available commands.\n");
            }

            // POST LOGIN UI
            while(loggedIn) {
                System.out.print("  \uD83D\uDD79 [GAME] Welcome, " + user + "! Type \"help\" into the console to see available commands.\n");

                System.out.print("\uD83D\uDFE9 ["+ user + "] >> ");
                String input = scanner.nextLine();
                break;
            }


        }

    }
}
