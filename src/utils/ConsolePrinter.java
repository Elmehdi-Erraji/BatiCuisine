package utils;

import domain.entities.Client;
import domain.entities.Quote;
import domain.entities.Project;
import utils.Types.CostBreakdown;

public class ConsolePrinter {
    public static void mainMenu(){
        System.out.println(" +----------------------------------------+");
        System.out.println(" |               Main Menu                |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  1. Create a new project               |");
        System.out.println(" |  2. Add a Client                       |");
//        System.out.println(" |  2. Show existing projects             |");
//        System.out.println(" |  3. Calculate the cost of a project    |");
        System.out.println(" |  4. Exit                               |");
        System.out.println(" +----------------------------------------+");
        System.out.print(" ==> Enter your choice: ");
    }
    public static void clientMenu(){
        System.out.println(" +----------------------------------------+");
        System.out.println(" |               Client Menu              |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  1. Search for an existing customer    |");
        System.out.println(" |  2. Add a new customer                 |");
        System.out.println(" |  4. Exit                               |");
        System.out.println(" +----------------------------------------+");
        System.out.print(" ==> Enter your choice: ");
    }

    public static void printError(String message){
        String redText = "\033[0;31m";
        String resetText = "\033[0m";

        System.out.println("\n" + redText + "--------------------------------------");
        System.out.println( message );
        System.out.println("--------------------------------------" + resetText + "\n" );
    }

    public static void printClient(Client client){
        String redText = "\033[0;31m";
        String resetText = "\033[0m";
        System.out.println("\n" + redText);
        System.out.println(" +----------------------------------------+");
        System.out.println(" |                 Client                 |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Name          | " + client.getName());
        System.out.println(" | Address       | "  + client.getAddress());
        System.out.println(" | PhoneNumber   | "  + client.getPhone());
        System.out.println(" | Professional  | "  + client.isProfessional());
        System.out.println(" +----------------------------------------+");
        System.out.println(resetText + "\n" );

    }

    public static void printCostDetails(CostBreakdown costBreakdown){
        String redText = "\033[0;33m";
        String resetText = "\033[0m";
        System.out.println("\n" + redText);
        System.out.println(" +----------------------------------------+");
        System.out.println(" |           Project's Cost [MAD]          |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Base Cost       | " + costBreakdown.getBaseCost());
        System.out.println(" | Tax Amount      | "  + costBreakdown.getTaxAmount());
        System.out.println(" | Profit Amount   | "  + costBreakdown.getProfit());
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Total Cost      | "  + costBreakdown.getTotalCost());
        System.out.println(" +----------------------------------------+");
        System.out.println(resetText + "\n" );

    }

/*
    public static void  printDevis(Devis devis){
        String redText = "\033[0;33m";
        String resetText = "\033[0m";
        System.out.println("\n" + redText);
        System.out.println(" +----------------------------------------+");
        System.out.println(" |                  Devis                 |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Client's Name     | " + devis.getProjet().getClient().getName());
        System.out.println(" | Project's Name    | " + devis.getProjet().getProjectName());
        System.out.println(" | Project's Cost    | " + devis.getProjet().getTotalCost());
        System.out.println(" | Devis is Accepted | "  + devis.getAccepted());
        System.out.println(" | Valid Until       | "  + devis.getValidityDate().toString());
        System.out.println(" +----------------------------------------+");
        System.out.println(resetText + "\n" );
    }
*/

    public static void printSuccess(String message){
        String greenText = "\033[0;32m";
        String resetText = "\033[0m";
        System.out.println("\n" + greenText + "--------------------------------------");
        System.out.println(" " +  message );
        System.out.println("--------------------------------------" + resetText + "\n" );

    }
}
