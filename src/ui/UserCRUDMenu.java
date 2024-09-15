package ui;

import config.dbConnection;
import domain.entities.User;
import repository.Interfaces.UserRepository;
import repository.implimentation.UserRepositoryImpl;
import service.UserService;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class UserCRUDMenu {

    public static void main(String[] args) {
        // Get database connection
        Connection connection = dbConnection.getConnection();

        // Pass connection to UserRepositoryImpl
        UserRepository userRepository = new UserRepositoryImpl(connection);
        UserService userService = new UserService(userRepository);

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("User CRUD Menu:");
            System.out.println("1. Add User");
            System.out.println("2. View User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. List All Users");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter user name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter user address: ");
                    String address = scanner.nextLine();
                    System.out.print("Enter user phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Is the user professional (true/false)? ");
                    boolean isProfessional = scanner.nextBoolean();
                    User user = new User(0, name, address, phone, isProfessional);
                    userService.addUser(user);
                    System.out.println("User added successfully!");
                    break;
                case 2:
                    System.out.print("Enter user ID to view: ");
                    int id = scanner.nextInt();
                    User retrievedUser = userService.getUserById(id);
                    if (retrievedUser != null) {
                        System.out.println(retrievedUser);
                    } else {
                        System.out.println("User not found!");
                    }
                    break;
                case 3:
                    System.out.print("Enter user ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    User updateUser = userService.getUserById(updateId);
                    if (updateUser != null) {
                        System.out.print("Enter new name: ");
                        updateUser.setName(scanner.nextLine());
                        System.out.print("Enter new address: ");
                        updateUser.setAddress(scanner.nextLine());
                        System.out.print("Enter new phone: ");
                        updateUser.setPhone(scanner.nextLine());
                        System.out.print("Is the user professional (true/false)? ");
                        updateUser.setProfessional(scanner.nextBoolean());
                        userService.updateUser(updateUser);
                        System.out.println("User updated successfully!");
                    } else {
                        System.out.println("User not found!");
                    }
                    break;
                case 4:
                    System.out.print("Enter user ID to delete: ");
                    int deleteId = scanner.nextInt();
                    userService.deleteUser(deleteId);
                    System.out.println("User deleted successfully!");
                    break;
                case 5:
                    List<User> users = userService.getAllUsers();
                    for (User u : users) {
                        System.out.println(u);
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
