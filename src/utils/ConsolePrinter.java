package utils;

import domain.entities.Client;
import domain.entities.Quote;
import utils.Types.CostBreakdown;

public class ConsolePrinter {

    public static void mainMenu() {
        System.out.println(" +----------------------------------------+");
        System.out.println(" |           🏗️ BatiCuisine Menu        |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  👤  1. Client Management Menu         |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  📁  2. Project Management Menu        |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  ❌  3. Exit                           |");
        System.out.println(" +----------------------------------------+");
        System.out.print(" ==> Enter your choice: ");
    }

    public static void clientMenu() {
        System.out.println(" |               Client Menu              |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  👤  1. Add a Client                   |");
        System.out.println(" |  ✏️  2. Update a Client                |");
        System.out.println(" |  ❌  3. Delete a Client                |");
        System.out.println(" |  ✔️  4. Accept/Refuse Quote            |");
        System.out.println(" +----------------------------------------+");
        System.out.print(" ==> Enter your choice: ");
    }

    public static void projectMenu() {
        System.out.println(" |               Project Menu             |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  📁  5. Create a new project           |");
        System.out.println(" |  🔄  6. Update a Project               |");
        System.out.println(" |  🗑️  7. Delete a Project               |");
        System.out.println(" |  ➕  8. Add Components to a Project     |");
        System.out.println(" |  ℹ️  9. Get Details of a Project        |");
        System.out.println(" +----------------------------------------+");
        System.out.print(" ==> Enter your choice: ");
    }


    public static void printError(String message){
        String redText = "\033[0;31m";
        String resetText = "\033[0m";

        System.out.println(redText + "--------------------------------------");
        System.out.println( message );
        System.out.println("--------------------------------------" + resetText);
    }

    public static void printClient(Client client){
        String redText = "\033[0;31m";
        String resetText = "\033[0m";
        System.out.println(redText);
        System.out.println(" +----------------------------------------+");
        System.out.println(" |                 Client                 |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Name          | " + client.getName());
        System.out.println(" | Address       | "  + client.getAddress());
        System.out.println(" | PhoneNumber   | "  + client.getPhone());
        System.out.println(" | Professional  | "  + client.getProfessional());
        System.out.println(" +----------------------------------------+");
        System.out.println(resetText);

    }

    public static void printCostDetails(CostBreakdown costBreakdown){
        String redText = "\033[0;33m";
        String resetText = "\033[0m";

        System.out.println(redText);
        System.out.println(" +----------------------------------------+");
        System.out.println(" |           Project's Cost [MAD]          |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Base Cost       | " + costBreakdown.getBaseCost());
        System.out.println(" | Tax Amount      | "  + costBreakdown.getTaxAmount());
        System.out.println(" | Profit Amount   | "  + costBreakdown.getProfit());
        System.out.println(" | Discount Amount | "  + costBreakdown.getDiscount());
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Total Cost      | "  + costBreakdown.getTotalCost());
        System.out.println(" +----------------------------------------+");
        System.out.println(resetText);

    }

    public static void  printQuote(Quote Quote){
        String redText = "\033[0;33m";
        String resetText = "\033[0m";
        System.out.println( redText);
        System.out.println(" +----------------------------------------+");
        System.out.println(" |                  Quote                 |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Quote's ID        | " + Quote.getId());
        if (Quote.getProject().getClient() != null){
            System.out.println(" | Client's Name     | " + Quote.getProject().getClient().getName());
        }
        if (Quote.getProject().getId() != null){
            System.out.println(" | Project's ID      | " + Quote.getProject().getId());
            System.out.println(" | Project's Name    | " + Quote.getProject().getProjectName());
        }
        System.out.println(" | Project's Cost    | " + Quote.getEstimatedPrice());
        System.out.println(" | Quote is Accepted | "  + (Quote.getAccepted() ? "Accepted" : "Rejected"));
        System.out.println(" | Valid Until       | "  + Quote.getValidityDate().toString());
        System.out.println(" +----------------------------------------+");
        System.out.println(resetText);
    }

    public static void printSuccess(String message){
        String greenText = "\033[0;32m";
        String resetText = "\033[0m";
        System.out.println(greenText + "--------------------------------------");
        System.out.println(" " +  message );
        System.out.println("--------------------------------------" + resetText  );
    }
}
