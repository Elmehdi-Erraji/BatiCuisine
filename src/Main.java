import ui.MainView;
import utils.ConsolePrinter;

import java.util.Scanner;

import static validation.InputValidator.validateIntInput;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            ConsolePrinter.mainMenu();

            int choice = validateIntInput(" ==> Enter your choice: ");

            switch (choice) {
                case 1:
                    ConsolePrinter.clientMenu();
                    int clientChoice = validateIntInput(" ==> Enter your choice for Client Menu: ");
                    switch (clientChoice) {
                        case 1:
                            MainView.createClient();
                            break;
                        case 2:
                            MainView.updateClient();
                            break;
                        case 3:
                           MainView.deleteClient();
                            break;
                        case 4:
                            MainView.acceptDevis();
                            break;
                        case 0:
                            System.out.println("Returning to Main Menu...");
                            break;
                        default:
                            ConsolePrinter.printError("Invalid choice in Client Menu. Please try again.");
                    }
                    break;

                case 2:
                    ConsolePrinter.projectMenu();
                    int projectChoice = validateIntInput(" ==> Enter your choice for Project Menu: ");
                    switch (projectChoice) {
                        case 1:
                            MainView.createProject();
                            break;
                        case 2:
                            MainView.deleteProject();
                            break;

                        case 3:
                            MainView.getProjectDetails();
                            break;
                        case 0:
                            System.out.println("Returning to Main Menu...");
                            break;
                        default:
                            ConsolePrinter.printError("Invalid choice in Project Menu. Please try again.");
                    }
                    break;

                case 3:
                    System.exit(0);
                    break;

                default:
                    ConsolePrinter.printError("Invalid choice in Main Menu. Please try again.");
            }
        }
    }


}
