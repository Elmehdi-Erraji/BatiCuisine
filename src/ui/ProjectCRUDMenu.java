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
import java.util.List;
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
                    Double totalCost = 0.0; // Total cost will be calculated later

                    // Create and add the project
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
                                projectToUpdate.setStatus(ProjectStatus.valueOf(newStatusStr));
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid status. Keeping the current status.");
                            }
                        }

                        projectService.updateProject(projectToUpdate);
                        System.out.println("Project updated successfully!");
                    } else {
                        System.out.println("Project not found!");
                    }
                    break;

                case 4:
                    System.out.print("Enter project ID to delete: ");
                    int deleteId = scanner.nextInt();
                    projectService.deleteProject(deleteId);
                    System.out.println("Project deleted successfully!");
                    break;

                case 5:
                    List<Project> projects = projectService.getAllProjects();
                    for (Project p : projects) {
                        System.out.println(p);
                    }
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
