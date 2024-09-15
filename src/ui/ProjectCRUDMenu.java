package ui;

import config.dbConnection;
import domain.entities.Project;
import domain.entities.User;
import domain.enums.ProjectStatus;
import repository.Interfaces.ProjectRepository;
import repository.Interfaces.UserRepository;
import repository.implimentation.ProjectRepositoryImpl;
import repository.implimentation.UserRepositoryImpl;
import service.ProjectService;
import service.UserService;

import java.sql.Connection;
import java.util.Scanner;

public class ProjectCRUDMenu {

    public static void main(String[] args) {
        // Establish database connection
        Connection connection = dbConnection.getConnection();

        // Initialize repositories and services
        UserRepository userRepository = new UserRepositoryImpl(connection);
        UserService userService = new UserService(userRepository);
        ProjectRepository projectRepository = new ProjectRepositoryImpl(connection, userService);
        ProjectService projectService = new ProjectService(projectRepository);

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
                    // Step 1: Specify the client (user)
                    System.out.print("Enter user ID to associate with this project: ");
                    int userId = scanner.nextInt();
                    User user = userService.getUserById(userId);
                    if (user == null) {
                        System.out.println("User not found! Cannot proceed.");
                        break;
                    }

                    // Step 2: Add the project details, leaving total cost as null
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter project name: ");
                    String projectName = scanner.nextLine();
                    System.out.print("Enter project profit margin: ");
                    double profitMargin = scanner.nextDouble();
                    System.out.print("Enter project status (e.g., IN_PROGRESS, COMPLETED): ");
                    String projectStatusStr = scanner.next();
                    ProjectStatus projectStatus = ProjectStatus.valueOf(projectStatusStr);

                    // Set total cost to null at the time of creation
                    Double totalCost = null; // To be calculated later

                    Project project = new Project(0, projectName, profitMargin, totalCost, projectStatus, user);
                    projectService.addProject(project);

                    System.out.println("Project added successfully with total cost set to null.");
                    break;

                case 2:
                    System.out.print("Enter project ID to view: ");
                    int projectId = scanner.nextInt();
                    Project retrievedProject = projectService.getProjectById(projectId);
                    if (retrievedProject != null) {
                        System.out.println(retrievedProject);
                    } else {
                        System.out.println("Project not found!");
                    }
                    break;

                case 3:
                    // Implement update logic if necessary
                    break;

                case 4:
                    // Implement delete logic if necessary
                    break;

                case 5:
                    // Implement listing logic if necessary
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
}
