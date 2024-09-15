package ui;

import domain.entities.Quote;
import repository.implimentation.QuoteRepositoryImpl;
import service.QuoteService;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import config.dbConnection;
public class QuotesCRUDMenu {

    public static void main(String[] args) {
        Connection connection = dbConnection.getConnection();


        QuoteRepositoryImpl quoteRepository = new QuoteRepositoryImpl(connection, null); // Replace null with an appropriate UserService instance if needed
        QuoteService quoteService = new QuoteService(quoteRepository);

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("\n--- Quote Management Menu ---");
            System.out.println("1. Create a new Quote");
            System.out.println("2. View a Quote by ID");
            System.out.println("3. View all Quotes");
            System.out.println("4. Update a Quote");
            System.out.println("5. Delete a Quote");
            System.out.println("6. Find Quotes by Project ID");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    createQuote(quoteService, scanner);
                    break;
                case 2:
                    viewQuoteById(quoteService, scanner);
                    break;
                case 3:
                    viewAllQuotes(quoteService);
                    break;
                case 4:
                    updateQuote(quoteService, scanner);
                    break;
                case 5:
                    deleteQuote(quoteService, scanner);
                    break;
                case 6:
                    findQuotesByProjectId(quoteService, scanner);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
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

    private static void createQuote(QuoteService quoteService, Scanner scanner) {
        System.out.println("\n--- Create a New Quote ---");
        System.out.print("Enter estimated price: ");
        Double estimatedPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter issue date (YYYY-MM-DD): ");
        LocalDate issueDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter validity date (YYYY-MM-DD): ");
        LocalDate validityDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Is the quote accepted? (true/false): ");
        Boolean accepted = scanner.nextBoolean();
        System.out.print("Enter project ID: ");
        Integer projectId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Quote newQuote = new Quote(null, estimatedPrice, issueDate, validityDate, accepted, projectId);
        quoteService.createQuote(newQuote);
        System.out.println("Quote created successfully!");
    }

    private static void viewQuoteById(QuoteService quoteService, Scanner scanner) {
        System.out.println("\n--- View Quote by ID ---");
        System.out.print("Enter Quote ID: ");
        Integer id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Optional<Quote> quote = quoteService.getQuoteById(id);
        if (quote.isPresent()) {
            displayQuote(quote.get());
        } else {
            System.out.println("Quote not found.");
        }
    }

    private static void viewAllQuotes(QuoteService quoteService) {
        System.out.println("\n--- View All Quotes ---");
        List<Quote> quotes = quoteService.getAllQuote();
        if (quotes.isEmpty()) {
            System.out.println("No quotes found.");
        } else {
            for (Quote quote : quotes) {
                displayQuote(quote);
            }
        }
    }

    private static void updateQuote(QuoteService quoteService, Scanner scanner) {
        System.out.println("\n--- Update a Quote ---");
        System.out.print("Enter Quote ID to update: ");
        Integer id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Optional<Quote> quoteOpt = quoteService.getQuoteById(id);
        if (quoteOpt.isPresent()) {
            Quote quote = quoteOpt.get();
            System.out.print("Enter new estimated price (or press Enter to keep current): ");
            String estimatedPriceStr = scanner.nextLine();
            if (!estimatedPriceStr.isEmpty()) {
                quote.setEstimatedPrice(Double.parseDouble(estimatedPriceStr));
            }

            System.out.print("Enter new issue date (YYYY-MM-DD) (or press Enter to keep current): ");
            String issueDateStr = scanner.nextLine();
            if (!issueDateStr.isEmpty()) {
                quote.setIssueDate(LocalDate.parse(issueDateStr));
            }

            System.out.print("Enter new validity date (YYYY-MM-DD) (or press Enter to keep current): ");
            String validityDateStr = scanner.nextLine();
            if (!validityDateStr.isEmpty()) {
                quote.setValidityDate(LocalDate.parse(validityDateStr));
            }

            System.out.print("Is the quote accepted? (true/false) (or press Enter to keep current): ");
            String acceptedStr = scanner.nextLine();
            if (!acceptedStr.isEmpty()) {
                quote.setAccepted(Boolean.parseBoolean(acceptedStr));
            }

            System.out.print("Enter new project ID (or press Enter to keep current): ");
            String projectIdStr = scanner.nextLine();
            if (!projectIdStr.isEmpty()) {
                quote.setProjectId(Integer.parseInt(projectIdStr));
            }

            quoteService.updateQuote(quote);
            System.out.println("Quote updated successfully!");
        } else {
            System.out.println("Quote not found.");
        }
    }

    private static void deleteQuote(QuoteService quoteService, Scanner scanner) {
        System.out.println("\n--- Delete a Quote ---");
        System.out.print("Enter Quote ID to delete: ");
        Integer id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        quoteService.deleteQuote(id);
        System.out.println("Quote deleted successfully.");
    }

    private static void findQuotesByProjectId(QuoteService quoteService, Scanner scanner) {
        System.out.println("\n--- Find Quotes by Project ID ---");
        System.out.print("Enter Project ID: ");
        Integer projectId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<Quote> quotes = quoteService.getQuoteByProjectId(projectId);
        if (quotes.isEmpty()) {
            System.out.println("No quotes found for the given project ID.");
        } else {
            for (Quote quote : quotes) {
                displayQuote(quote);
            }
        }
    }

    private static void displayQuote(Quote quote) {
        System.out.println("Quote ID: " + quote.getId());
        System.out.println("Estimated Price: " + quote.getEstimatedPrice());
        System.out.println("Issue Date: " + quote.getIssueDate());
        System.out.println("Validity Date: " + quote.getValidityDate());
        System.out.println("Accepted: " + quote.getAccepted());
        System.out.println("Project ID: " + quote.getProjectId());
        System.out.println("---------------------------");
    }
}
