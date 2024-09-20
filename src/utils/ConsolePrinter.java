package Utils;

import Entities.Client;
import Entities.Devis;
import Entities.Projet;
import Utils.Types.CostBreakdown;

public class ConsolePrinter {

    public static void mainMenu(){
        System.out.println(" +----------------------------------------+");
        System.out.println(" |           professionnel Menu           |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  1. Create a new project               |");
        System.out.println(" |  2. Add Components to a Project        |");
        System.out.println(" |  3. Get Details of a Project           |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |               Client Menu              |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  4. Add a Client                       |");
        System.out.println(" |  5. Accept/Refuse Devis                |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  6. Exit                               |");
        System.out.println(" +----------------------------------------+");
        System.out.print(" ==> Enter your choice: ");
    }

    public static void clientMenu(){
        System.out.println(" +----------------------------------------+");
        System.out.println(" |               Client Menu              |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" |  1. Search for an existing Client    |");
        System.out.println(" |  2. Add a new Client                 |");
        System.out.println(" |  4. Exit                               |");
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
        System.out.println(" | PhoneNumber   | "  + client.getPhoneNumber());
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

    public static void  printDevis(Devis devis){
        String redText = "\033[0;33m";
        String resetText = "\033[0m";
        System.out.println( redText);
        System.out.println(" +----------------------------------------+");
        System.out.println(" |                  Devis                 |");
        System.out.println(" +----------------------------------------+");
        System.out.println(" | Devis's ID        | " + devis.getId());
        if (devis.getProjet().getClient() != null){
            System.out.println(" | Client's Name     | " + devis.getProjet().getClient().getName());
        }
        if (devis.getProjet().getId() != null){
            System.out.println(" | Project's ID      | " + devis.getProjet().getId());
            System.out.println(" | Project's Name    | " + devis.getProjet().getProjectName());
        }
        System.out.println(" | Project's Cost    | " + devis.getEstimatedPrice());
        System.out.println(" | Devis is Accepted | "  + (devis.getAccepted() ? "Accepted" : "Rejected"));
        System.out.println(" | Valid Until       | "  + devis.getValidityDate().toString());
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
