import java.util.Scanner;
import ui.ES;

public class PreGameUI {

    public static void main(String[] args) {
        System.out.print(ES.SET_TEXT_COLOR_WHITE);
        System.out.print("\n=================================\n");
        System.out.println("\uD83D\uDD79 " + ES.SET_TEXT_BOLD + "Welcome to Big Chess Game 2" + ES.RESET_TEXT_BOLD_FAINT +" \uD83D\uDD79");
        System.out.print("=================================\n");
        System.out.print("\u26A0 Type \"help\" into the console to get started! \u26A0 \n\n");


        ;


        while(true) {
            System.out.print("\uD83D\uDD12 [LOGGED OUT] >> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();

            if(line.equalsIgnoreCase("quit")) {
                System.out.print("\n  \uD83D\uDD79 [GAME] Thanks for playing. Goodbye!\n\n");
                break;
            } else if (line.equalsIgnoreCase("help")) {
                String prefix = ES.SET_TEXT_COLOR_GREEN;
                System.out.print(prefix + "\n  register <USERNAME> <PASSWORD> <EMAIL>" + ES.SET_TEXT_COLOR_WHITE + " - to create an account\n");
                System.out.print(prefix + "  login <USERNAME> <PASSWORD>" + ES.SET_TEXT_COLOR_WHITE + " - to play chess\n");
                System.out.print(prefix + "  quit" + ES.SET_TEXT_COLOR_WHITE + " - to leave the game\n");
                System.out.print(prefix + "  help" + ES.SET_TEXT_COLOR_WHITE + "- with possible commands\n\n");
            } else if (line.equalsIgnoreCase("login")) {

            } else if (line.equalsIgnoreCase("register")) {

            } else {
                System.out.print("  \uD83D\uDD79 [GAME] Sorry, I don't know that command. Try typing \"help\" into the console for a list of available commands.\n");
            }

        }

    }
}
