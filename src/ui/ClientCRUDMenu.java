package ui;

import config.dbConnection;
import domain.entities.Client;
import repository.Interfaces.CrudRepository;
import repository.implimentation.ClientRepositoryImpl;
import service.ClientService;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientCRUDMenu {

    public static void main(String[] args) {
        // Initialize the database connection
        Connection connection = dbConnection.getConnection();

        // Create repository and service objects
        CrudRepository<Client, Integer> clientRepository = new ClientRepositoryImpl(connection);
        ClientService clientService = new ClientService(clientRepository);

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            displayMenu();
            choice = getChoice(scanner);

            switch (choice) {
                case 1:
                    addClient(scanner, clientService);
                    break;
                case 2:
                    viewClient(scanner, clientService);
                    break;
                case 3:
                    updateClient(scanner, clientService);
                    break;
                case 4:
                    deleteClient(scanner, clientService);
                    break;
                case 5:
                    listAllClients(clientService);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }

        scanner.close();

        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("Client CRUD Menu:");
        System.out.println("1. Add Client");
        System.out.println("2. View Client");
        System.out.println("3. Update Client");
        System.out.println("4. Delete Client");
        System.out.println("5. List All Clients");
        System.out.println("0. Exit");
    }

    private static int getChoice(Scanner scanner) {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void addClient(Scanner scanner, ClientService clientService) {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter client name: ");
        String name = scanner.nextLine();
        System.out.print("Enter client address: ");
        String address = scanner.nextLine();
        System.out.print("Enter client phone: ");
        String phone = scanner.nextLine();
        System.out.print("Is the client professional (true/false)? ");
        boolean isProfessional = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        Client client = new Client(0, name, address, phone, isProfessional);
        Optional<Client> createdClient = clientService.addClient(client);
        if (createdClient.isPresent()) {
            System.out.println("Client added successfully! ID: " + createdClient.get().getId());
        } else {
            System.out.println("Failed to add client.");
        }
    }

    private static void viewClient(Scanner scanner, ClientService clientService) {
        System.out.print("Enter client ID to view: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Optional<Client> retrievedClient = clientService.getClientById(id);
        if (retrievedClient.isPresent()) {
            System.out.println(retrievedClient.get());
        } else {
            System.out.println("Client not found!");
        }
    }

    private static void updateClient(Scanner scanner, ClientService clientService) {
        System.out.print("Enter client ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Optional<Client> optionalClient = clientService.getClientById(updateId);
        if (optionalClient.isPresent()) {
            Client updateClient = optionalClient.get();
            System.out.print("Enter new name: ");
            updateClient.setName(scanner.nextLine());
            System.out.print("Enter new address: ");
            updateClient.setAddress(scanner.nextLine());
            System.out.print("Enter new phone: ");
            updateClient.setPhone(scanner.nextLine());
            System.out.print("Is the client professional (true/false)? ");
            updateClient.setProfessional(scanner.nextBoolean());
            scanner.nextLine(); // Consume newline

            Optional<Client> updatedClient = clientService.updateClient(updateClient);
            if (updatedClient.isPresent()) {
                System.out.println("Client updated successfully!");
            } else {
                System.out.println("Failed to update client.");
            }
        } else {
            System.out.println("Client not found!");
        }
    }

    private static void deleteClient(Scanner scanner, ClientService clientService) {
        System.out.print("Enter client ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        clientService.deleteClient(deleteId);
        System.out.println("Client deleted successfully!");
    }

    private static void listAllClients(ClientService clientService) {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            for (Client c : clients) {
                System.out.println(c);
            }
        }
    }
}
