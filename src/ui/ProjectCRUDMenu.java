package ui;

import config.dbConnection;
import domain.entities.*;
import domain.enums.ComponentType;
import domain.enums.ProjectStatus;
import repository.Interfaces.ProjectRepository;
import repository.Interfaces.UserRepository;
import repository.implimentation.LabourRepositoryImpl;
import repository.implimentation.MaterialsRepositoryImpl;
import repository.implimentation.ProjectRepositoryImpl;
import repository.implimentation.UserRepositoryImpl;
import service.LabourService;
import service.MaterialsService;
import service.ProjectService;
import service.UserService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ProjectCRUDMenu {

    private static final ComponentCRUDMenu componentCRUDMenu = new ComponentCRUDMenu();

    public static void main(String[] args) {

        Connection connection = dbConnection.getConnection();

        // Initialize repositories and pass the connection object to their constructors
        ProjectRepositoryImpl projectRepository = new ProjectRepositoryImpl(connection, new UserService(new UserRepositoryImpl(connection)));
        LabourRepositoryImpl labourRepository = new LabourRepositoryImpl(connection);
        MaterialsRepositoryImpl materialsRepository = new MaterialsRepositoryImpl(connection);
        UserRepositoryImpl userRepository = new UserRepositoryImpl(connection);

        // Initialize services
        LabourService labourService = new LabourService(labourRepository);
        MaterialsService materialsService = new MaterialsService(materialsRepository);
        ProjectService projectService = new ProjectService(projectRepository, labourService, materialsService);
        UserService userService = new UserService(userRepository);

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("Project CRUD Menu:");
            System.out.println("1. Add Project");
            System.out.println("2. View Project");
            System.out.println("3. Update Project");
            System.out.println("4. Delete Project");
            System.out.println("5. List All Projects");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProject(scanner, userService, projectService);
                    break;

                case 2:
                    viewProject(scanner, projectService);
                    break;

                case 3:
                    updateProject(scanner, projectService);
                    break;

                case 4:
                    deleteProject(scanner, projectService);
                    break;

                case 5:
                    listAllProjects(projectService);
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }

        // Close the scanner
        scanner.close();

        // Close the database connection when done
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void addProject(Scanner scanner, UserService userService, ProjectService projectService) {
        // Specify the client (user)
        System.out.print("Enter user ID to associate with this project: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        User user = userService.getUserById(userId);
        if (user == null) {
            System.out.println("User not found! Cannot proceed.");
            return;
        }

        // Display project statuses as numbered options
        System.out.println("Select project status:");
        ProjectStatus[] statuses = ProjectStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i]);
        }
        System.out.print("Enter the number corresponding to the project status: ");
        int statusChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        ProjectStatus projectStatus = statuses[statusChoice - 1];

        // Collect other project details
        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();
        System.out.print("Enter project profit margin: ");
        double profitMargin = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        // Get project components
        List<Component> components = getComponentsForProject(scanner);

        // Calculate total cost based on components
        double totalCost = calculateTotalCost(Collections.singletonList(components));

        // Create and add the project
        Project project = new Project(projectName, profitMargin, totalCost, projectStatus, user, components);
        projectService.addProject(project);

        System.out.println("Project added successfully with total cost calculated.");
    }

    private static void viewProject(Scanner scanner, ProjectService projectService) {
        System.out.print("Enter project ID to view: ");
        int projectId = scanner.nextInt();
        Project retrievedProject = projectService.getProjectById(projectId);
        if (retrievedProject != null) {
            System.out.println(retrievedProject);
        } else {
            System.out.println("Project not found!");
        }
    }

    private static void updateProject(Scanner scanner, ProjectService projectService) {
        System.out.print("Enter project ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Project projectToUpdate = projectService.getProjectById(updateId);
        if (projectToUpdate != null) {
            System.out.print("Enter new project name (or press Enter to keep current): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                projectToUpdate.setName(newName);
            }

            System.out.print("Enter new profit margin (or press Enter to keep current): ");
            String profitMarginStr = scanner.nextLine();
            if (!profitMarginStr.isEmpty()) {
                projectToUpdate.setProfitMargin(Double.parseDouble(profitMarginStr));
            }

            System.out.print("Enter new total cost (or press Enter to keep current): ");
            String totalCostStr = scanner.nextLine();
            if (!totalCostStr.isEmpty()) {
                projectToUpdate.setTotalCost(Double.parseDouble(totalCostStr));
            } else {
                projectToUpdate.setTotalCost(null); // Set total cost to null if input is empty
            }

            System.out.print("Enter new project status (e.g., IN_PROGRESS, COMPLETED, ON_HOLD) (or press Enter to keep current): ");
            String newStatusStr = scanner.nextLine();
            if (!newStatusStr.isEmpty()) {
                try {
                    projectToUpdate.setProjectStatus(ProjectStatus.valueOf(newStatusStr));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status. Keeping the current status.");
                }
            }

            projectService.updateProject(projectToUpdate);
            System.out.println("Project updated successfully!");
        } else {
            System.out.println("Project not found!");
        }
    }

    private static void deleteProject(Scanner scanner, ProjectService projectService) {
        System.out.print("Enter project ID to delete: ");
        int deleteId = scanner.nextInt();
        projectService.deleteProject(deleteId);
        System.out.println("Project deleted successfully!");
    }

    private static void listAllProjects(ProjectService projectService) {
        List<Project> projects = projectService.getAllProjects();
        for (Project p : projects) {
            System.out.println(p);
        }
    }

    private static List<Component> getComponentsForProject(Scanner scanner) {
        List<Component> components = new ArrayList<>();

        while (true) {
            System.out.println("Select component type:");
            System.out.println("1. Labour");
            System.out.println("2. Material");
            System.out.println("3. Done");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 3) {
                break;
            }

            System.out.print("Enter component name: ");
            String name = scanner.nextLine();

            System.out.print("Enter tax rate: ");
            Double taxRate = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Labour
                    System.out.print("Enter ID: ");
                    Integer id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter component type (enum value): ");
                    ComponentType componentType = ComponentType.valueOf(scanner.nextLine());

                    System.out.print("Enter hourly rate: ");
                    Double hourlyRate = scanner.nextDouble();

                    System.out.print("Enter work hours count: ");
                    Double workHoursCount = scanner.nextDouble();

                    System.out.print("Enter productivity rate: ");
                    Double productivityRate = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    components.add(new Labour(name, taxRate, id, componentType, hourlyRate, workHoursCount, productivityRate));
                    break;

                case 2: // Material
                    System.out.print("Enter ID: ");
                    Integer materialId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter component type (enum value): ");
                    ComponentType materialType = ComponentType.valueOf(scanner.nextLine());

                    System.out.print("Enter unit cost: ");
                    Double unitCost = scanner.nextDouble();

                    System.out.print("Enter quantity: ");
                    Double quantity = scanner.nextDouble();

                    System.out.print("Enter transport cost: ");
                    Double transportCost = scanner.nextDouble();

                    System.out.print("Enter quality coefficient: ");
                    Double qualityCoefficient = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    components.add(new Material(name, taxRate, materialId, materialType, unitCost, quantity, transportCost, qualityCoefficient));
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        return components;
    }

    private static double calculateTotalCost(List<Object> components) {
        // Placeholder logic for calculating total cost from components
        double totalCost = 0.0;
        for (Object component : components) {
            if (component instanceof Material) {
                Material material = (Material) component;
                totalCost += material.getUnitCost() * material.getQuantity();
            } else if (component instanceof Labour) {
                Labour labour = (Labour) component;
                totalCost += labour.getHourlyRate() * labour.getWorkHoursCount();
            }
        }
        return totalCost;
    }
}
