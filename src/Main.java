import Presentation.MainView;
import Utils.ConsolePrinter;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            ConsolePrinter.mainMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    MainView.createProject();
                    break;
                case 2:
                    //
                    break;
                case 3:
                    //
                    break;
                case 4:
                    MainView.createClient();
                    break;
                case 5:
                    MainView.acceptDevis();
                    break;
                case 6:
                    System.exit(0);
                    break;
                default:
                    ConsolePrinter.printError("Invalid choice. Please try again.");
            }
        }


    }
}