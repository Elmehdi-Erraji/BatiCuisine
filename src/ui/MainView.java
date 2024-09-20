
package Presentation;

import Entities.*;
import Enums.EtatProject;
import Enums.TypeComposant;
import Services.ClientService;
import Services.DevisService;
import Services.ProjetService;
import Utils.ConsolePrinter;
import Utils.Types.CostBreakdown;
import repositories.Client.ClientRepository;
import repositories.Client.ClientRepositoryImpl;
import repositories.Projet.ProjetRepository;
import repositories.Projet.ProjetRepositoryImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MainView {
    private static final Scanner scanner = new Scanner(System.in);

    static public void createClient(){
        System.out.print(" ==> Entre FullName: ");
        String name = scanner.nextLine();
        System.out.print(" ==> Entre Address: ");
        String address = scanner.nextLine();
        System.out.print(" ==> Entre Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print(" ==> is Professional ? (n/y): ");
        String isProfessional = scanner.nextLine();


        Boolean ispro = isProfessional.equals("y") ? Boolean.TRUE : Boolean.FALSE;
        ClientRepository clientRepository = new ClientRepositoryImpl();
        ClientService clientService = new ClientService(clientRepository);
        clientService.createClient(new Client(null, name, address, phoneNumber, ispro));
        System.out.println("Client created Successfully");

    }

    static public void createProject(){
        System.out.print(" ==> Entre Client's ID: ");
        Integer clientId = scanner.nextInt();
        scanner.nextLine();

        ClientRepository clientRepository = new ClientRepositoryImpl();
        ClientService clientService = new ClientService(clientRepository);
        Optional<Client> client =  clientService.getClientById(clientId);

        if(client.isPresent()){
            ConsolePrinter.printClient(client.get());

            System.out.print(" ==> Entre Project Name: ");
            String projectName = scanner.nextLine();
            System.out.print(" ==> Entre Profit Margin (%): ");
            Double profit = scanner.nextDouble();
            scanner.nextLine();

            ProjetRepository projetRepository = new ProjetRepositoryImpl();
            ProjetService projetService = new ProjetService(projetRepository);

            Projet projet = new Projet(null, projectName, profit, null,null, null);
            projet.setClient(client.get());

            materialsLoop:
            while (true){
                scanner.nextLine();
                System.out.print(" ==> Do you want to add Materials to this Project? [y/n]: ");
                String componentsChoice = scanner.nextLine();
                switch (componentsChoice){
                    case "y":
                        Materiaux materiaux =  addMaterialsView();
                        projet.getComposants().add(materiaux);
                        ConsolePrinter.printSuccess("Material Has Been Added Successfully");
                        break;
                    case "n":
                        break materialsLoop;
                }
            }

            laborLoop:
            while (true){
                scanner.nextLine();
                System.out.print(" ==> Do you want to add Labor to this Project? [y/n]: ");
                String laborChoice = scanner.nextLine();
                switch (laborChoice){
                    case "y":
                        MainDoeuvre mainDoeuvre =  addLaborView();
                        projet.getComposants().add(mainDoeuvre);
                        ConsolePrinter.printSuccess("Labor Has Been Added Successfully");
                        break;
                    case "n":
                        break laborLoop;
                }
            }

            CostBreakdown  costBreakdown = calculateCost(projet.getComposants());

            System.out.print(" ==> Do you want to apply a profit margin? [y/n]: ");
            String marginChoice = scanner.nextLine();

            if(marginChoice.equals("y")){
                costBreakdown.setProfit(costBreakdown.getBaseCost() * (projet.getProfit() / 100));
            }

            System.out.print(" ==> Do you want to apply a Discount to This Client? [y/n]: ");
            String discountChoice = scanner.nextLine();

            if(discountChoice.equals("y")){
                System.out.print(" ==> Entre Discount percentage (%): ");
                Double discount = scanner.nextDouble();
                scanner.nextLine();

                projet.setDiscount(discount);
            }else {
                projet.setDiscount(0.0);
            }

            costBreakdown.setDiscount(costBreakdown.getProfit() * (projet.getDiscount() / 100));

            projet.setTotalCost(costBreakdown.getTotalCost());
            projet.setProjectStatus(EtatProject.INPROGRESS);


            ConsolePrinter.printCostDetails(costBreakdown);

            System.out.print(" ==> Do you want to save this project? [y/n]: ");
            String saveChoice = scanner.nextLine();

            if(saveChoice.equals("y")){
                projetService.createProjetWithComponents(projet);
            }

            DevisService devisService = new DevisService();
            addDevisView(projet);


        }else {
            System.out.println("Client not found");
        }

    }

    static public void acceptDevis(){
        System.out.print(" ==> Entre Client's ID: ");
        Integer clientId = scanner.nextInt();
        scanner.nextLine();

        ClientRepository clientRepository = new ClientRepositoryImpl();
        ClientService clientService = new ClientService(clientRepository);
        Optional<Client> client =  clientService.getClientById(clientId);

        if(client.isPresent()){
            ConsolePrinter.printClient(client.get());

            DevisService devisService = new DevisService();
            List<Devis> devisList = devisService.getDevisWithProject(client.get());

            for(Devis devis : devisList){
                ConsolePrinter.printDevis(devis);
            }

            System.out.print(" ==> Do you want to accept a devis? [y/n]: ");
            String saveChoice = scanner.nextLine();

            if(saveChoice.equals("y")){
                System.out.print(" ==> Enter Devis ID [To Accept]: ");
                Integer devisId = scanner.nextInt();
                scanner.nextLine();

                Devis currentdevis = devisList.stream()
                        .filter(devis -> Objects.equals(devis.getId(), devisId))
                        .findFirst()
                        .orElse(null);

                assert currentdevis != null;
                if(!currentdevis.getAccepted()){
                    try {
                        Devis returnedDevis = devisService.acceptDevis(currentdevis);
                        ConsolePrinter.printSuccess("Devis Accepted Successfully: ID " + returnedDevis.getId());
                    } catch (Exception e){
                        ConsolePrinter.printError(e.getMessage());
                    }

                }else {
                    ConsolePrinter.printError("Devis Already Accepted");
                }
            }

        }else {
            System.out.println("Client not found");
        }

    }

    static private Materiaux addMaterialsView(){
        System.out.print(" ==> Entre the name of the Material: ");
        String materialName = scanner.nextLine();

        System.out.print(" ==> Entre the Unit Cost of the material [MAD]: ");
        Double unitCost = scanner.nextDouble();

        System.out.print(" ==> Entre The Quantity of the Material: ");
        Double quanitity = scanner.nextDouble();

        System.out.print(" ==> Add the Tax Rate [%]: ");
        Double taxRate = scanner.nextDouble();

        System.out.print(" ==> Add Transport Cost [MAD]: ");
        Double transport = scanner.nextDouble();

        System.out.print(" ==> Add Material quality coefficient (1.0 = standard > 1.0 = high quality: ");
        Double coefficient = scanner.nextDouble();

        return new Materiaux(materialName, taxRate, TypeComposant.MATERIEL, null,  unitCost, quanitity, transport, coefficient);

    }

    static private MainDoeuvre addLaborView(){
        System.out.print(" ==> Entre the Type of the worker : ");
        String workerType = scanner.nextLine();

        System.out.print(" ==> Add the Tax Rate [%]: ");
        Double taxRate = scanner.nextDouble();

        System.out.print(" ==> Entre Hourly Rate of the Labor [MAD]: ");
        Double hourlyRate = scanner.nextDouble();

        System.out.print(" ==> Entre the number od working hours [Hours]: ");
        Double worKHoursCount = scanner.nextDouble();

        System.out.print(" ==> Add Labor Productivity factor (1.0 = standard > 1.0 = high quality: ");
        Double coefficient = scanner.nextDouble();

        return new MainDoeuvre(workerType, taxRate, TypeComposant.MAINDOUVRE, null,  hourlyRate, worKHoursCount, coefficient);

    }

    static private void addDevisView(Projet projet){
        System.out.print(" ==> Do you want to Create Devis? [y/n]: ");
        String devisChoice = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String  issueDate = "";
        String validity = "";


        if(devisChoice.equals("y")){
            System.out.print(" ==> Entre the issue date  [YYYY-MM-DD]: ");
            issueDate = scanner.nextLine();
            System.out.print(" ==> Valid Until [YYYY-MM-DD]: ");
            validity = scanner.nextLine();
        }

        DevisService devisService = new DevisService();
        Devis devis = new Devis(
                null,
                projet.getTotalCost(),
                LocalDate.parse(issueDate, formatter),
                LocalDate.parse(validity, formatter),
                Boolean.FALSE,
                projet
        );

        ConsolePrinter.printDevis(devis);
        devisService.createDevis(devis);

    }

    static private CostBreakdown calculateCost(List<Composants> composants) {
        return composants.stream()
                .map(composant -> {
                    double baseCost = calculateBaseCost(composant);
                    double taxAmount = baseCost * (composant.getTaxRate() / 100);
                    return new CostBreakdown(baseCost, taxAmount);
                })
                .reduce(new CostBreakdown(0, 0), (subtotal, element) -> new CostBreakdown(
                        subtotal.getBaseCost() + element.getBaseCost(),
                        subtotal.getTaxAmount() + element.getTaxAmount()
                ));
    }

    static private double calculateBaseCost(Composants composant) {
        if (composant instanceof Materiaux materiaux) {
            return materiaux.getUnitCost()
                    * materiaux.getQuantity()
                    * materiaux.getQualityCoefficient()
                    + materiaux.getTransportCost();
        } else if (composant instanceof MainDoeuvre mainDoeuvre) {
            return mainDoeuvre.getHourlyRate()
                    * mainDoeuvre.getWorkHoursCount()
                    * mainDoeuvre.getProductivityRate();
        }
        return 0.0; // or throw an exception for unknown component types
    }

}
