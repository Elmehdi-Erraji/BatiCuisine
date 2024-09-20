package ui;

import config.dbConnection;
import domain.entities.*;
import domain.enums.ComponentType;
import domain.enums.ProjectStatus;
import repository.Interfaces.CrudRepository;
import repository.Interfaces.ProjectRepository;
import repository.implimentation.ClientRepositoryImpl;
import repository.implimentation.ProjectRepositoryImpl;
import service.ClientService;
import service.ProjectService;
import utils.ConsolePrinter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {

    public static void main(String[] args) {
        try {
            Connection connection = dbConnection.getInstance().getConnection();

            CrudRepository<Client, Integer> clientRepository = new ClientRepositoryImpl(connection);
            ClientService clientService = new ClientService(clientRepository);

            ProjectRepository projectRepository = new ProjectRepositoryImpl(connection);
            ProjectService projectService = new ProjectService(projectRepository);

            Scanner scanner = new Scanner(System.in);
            int choice = -1;

            while (choice != 0) {
                displayMenu();
                choice = getChoice(scanner);

                switch (choice) {
                    case 1:
                        addProject(scanner, clientService, projectService);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }

            scanner.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private static void displayMenu() {
        System.out.println("Project CRUD Menu:");
        System.out.println("1. Add Project");
        System.out.println("0. Exit");
    }

    private static int getChoice(Scanner scanner) {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void addProject(Scanner scanner, ClientService clientService, ProjectService projectService) throws SQLException {
        System.out.print(" ==> Enter Client's ID: ");
        Integer clientId = scanner.nextInt();
        scanner.nextLine();

        Optional<Client> client = clientService.getClientById(clientId);

        if (client.isPresent()) {
            System.out.print(" ==> Enter Project Name: ");
            String projectName = scanner.nextLine();
            System.out.print(" ==> Enter Profit Margin (%): ");
            Double profit = scanner.nextDouble();
            scanner.nextLine();

            Project project = new Project(null, projectName, profit, null, null);
            project.setClient(client.get());

            // Adding materials
            boolean addingMaterials = true;
            while (addingMaterials) {
                Material material = addMaterialView(scanner);
                project.getComponents().add(material);
                System.out.print(" ==> Do you want to add another Material? [y/n]: ");
                String continueChoice = scanner.nextLine();
                addingMaterials = continueChoice.equalsIgnoreCase("y");
            }

            // Adding labor
            boolean addingLabor = true;
            while (addingLabor) {
                Labour labour = addLabourView(scanner);
                project.getComponents().add(labour);
                System.out.print(" ==> Do you want to add another Labor? [y/n]: ");
                String continueChoice = scanner.nextLine();
                addingLabor = continueChoice.equalsIgnoreCase("y");
            }

            // Calculate the total cost
            double totalCost = calculateTotalCost(project.getComponents());
            project.setTotalCost(totalCost);
            project.setProjectStatus(ProjectStatus.INPROGRESS);

            ConsolePrinter.printCostDetails(totalCost);

            System.out.print(" ==> Do you want to save this project? [y/n]: ");
            String saveChoice = scanner.nextLine();

            if (saveChoice.equalsIgnoreCase("y")) {
                projectService.createProjectWithComponents(project);
                ConsolePrinter.printSuccess("Project has been saved successfully.");
            }

        } else {
            System.out.println("Client not found");
        }
    }

    private static Material addMaterialView(Scanner scanner) {
        scanner.nextLine(); // Consume newline
        System.out.print(" ==> Enter the name of the Material: ");
        String materialName = scanner.nextLine();
        System.out.print(" ==> Enter the Unit Cost of the material [MAD]: ");
        Double unitCost = scanner.nextDouble();
        System.out.print(" ==> Enter the Quantity of the Material: ");
        Double quantity = scanner.nextDouble();
        System.out.print(" ==> Add the Tax Rate [%]: ");
        Double taxRate = scanner.nextDouble();
        System.out.print(" ==> Add Transport Cost [MAD]: ");
        Double transport = scanner.nextDouble();
        System.out.print(" ==> Add Material quality coefficient (1.0 = standard, > 1.0 = high quality): ");
        Double coefficient = scanner.nextDouble();

        return new Material(materialName, taxRate, null, ComponentType.MATERIAL, unitCost, quantity, transport, coefficient);
    }

    private static Labour addLabourView(Scanner scanner) {
        scanner.nextLine(); // Consume newline
        System.out.print(" ==> Enter the Type of the worker: ");
        String workerType = scanner.nextLine();
        System.out.print(" ==> Add the Tax Rate [%]: ");
        Double taxRate = scanner.nextDouble();
        System.out.print(" ==> Enter Hourly Rate of the Labour [MAD]: ");
        Double hourlyRate = scanner.nextDouble();
        System.out.print(" ==> Enter the number of working hours [Hours]: ");
        Double workHoursCount = scanner.nextDouble();
        System.out.print(" ==> Add Labour Productivity factor (1.0 = standard, > 1.0 = high quality): ");
        Double coefficient = scanner.nextDouble();

        return new Labour(workerType, taxRate, null, ComponentType.LABOR, hourlyRate, workHoursCount, coefficient);
    }

    private static double calculateTotalCost(List<Component> components) {
        double totalCost = 0;
        for (Component component : components) {
            totalCost += component.calculateCost();
        }
        return totalCost;
    }
    private static double calculateTotalCost(List<Component> components) {
        double totalCost = 0;
        for (Component component : components) {
            if (component instanceof Material) {
                Material material = (Material) component;
                totalCost += material.getUnitCost() * material.getQuantity() + material.getTransport() +
                        (material.getUnitCost() * material.getQuantity() * (material.getTaxRate() / 100));
            } else if (component instanceof Labour) {
                Labour labour = (Labour) component;
                totalCost += labour.getHourlyRate() * labour.getWorkHoursCount() +
                        (labour.getHourlyRate() * labour.getWorkHoursCount() * (labour.getTaxRate() / 100));
            }
        }
        return totalCost;
    }
}
