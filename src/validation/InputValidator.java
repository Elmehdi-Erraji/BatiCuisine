package validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputValidator {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String validateNonEmptyString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Error: Input cannot be empty. Please try again.");
            }
        }
    }


    public static int validateIntInput(String prompt) {
        while (true) {
            String input = validateNonEmptyString(prompt);
            try {
                return Integer.parseInt(input); // Attempt to parse input to an integer
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
            }
        }
    }
    public static String validatePhoneNumber(String prompt) {
        String phoneNumber;
        Pattern phonePattern = Pattern.compile("\\d{10}");
        while (true) {
            System.out.print(prompt);
            phoneNumber = scanner.nextLine().trim();
            if (phonePattern.matcher(phoneNumber).matches()) {
                return phoneNumber;
            } else {
                System.out.println("Error: Invalid phone number. Enter a 10-digit phone number.");
            }
        }
    }

    public static boolean validateYesNo(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt + " (y/n): ");
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("Error: Please enter 'y' for yes or 'n' for no.");
            }
        }
    }

    public static double validateDouble(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid numeric value.");
            }
        }
    }

    public static int validateInteger(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
            }
        }
    }

    public static LocalDate validateDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Please enter a valid date in the format YYYY-MM-DD.");
            }
        }
    }

    public static void validateIssueDateBeforeValidity(LocalDate issueDate, LocalDate validityDate) {
        if (!issueDate.isBefore(validityDate)) {
            throw new IllegalArgumentException("Error: Issue date must be before the validity date.");
        }
    }
}
