import ui.MainView;
import utils.ConsolePrinter;

import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            // Display the main BatiCuisine menu
            ConsolePrinter.mainMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: // Client Management Menu
                    ConsolePrinter.clientMenu();
                    int clientChoice = scanner.nextInt();
                    switch (clientChoice) {
                        case 1: // Add a Client
                            MainView.createClient();
                            break;
                        case 2: // Update a Client
                            MainView.updateClient();
                            break;
                        case 3: // Delete a Client
                            MainView.deleteClient();
                            break;
                        case 4: // Accept/Refuse Quote
                            MainView.acceptDevis();
                            break;
                        default:
                            ConsolePrinter.printError("Invalid choice in Client Menu. Please try again.");
                    }
                    break;

                case 2: // Project Management Menu
                    ConsolePrinter.projectMenu();
                    int projectChoice = scanner.nextInt();
                    switch (projectChoice) {
                        case 5: // Create a new project
                            MainView.createProject();
                            break;
                        case 6:
                            //MainView.updateProject();
                            break;
                        case 7: // Delete a Project
                            //MainView.deleteProject();
                            break;
                        case 8: // Add Components to a Project
                            //MainView.addComponentsToProject();
                            break;
                        case 9:
                            //MainView.getProjectDetails();
                            break;
                        default:
                            ConsolePrinter.printError("Invalid choice in Project Menu. Please try again.");
                    }
                    break;

                case 3: // Exit
                    System.exit(0);
                    break;

                default:
                    ConsolePrinter.printError("Invalid choice in Main Menu. Please try again.");
            }
        }
    }

}