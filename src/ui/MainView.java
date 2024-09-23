package ui;

import domain.entities.*;
import domain.enums.ProjectStatus;
import domain.enums.ComponentType;
import service.implimentation.ClientServiceImpl;
import service.implimentation.QuoteServiceImpl;
import service.implimentation.ProjectServiceImpl;
import utils.ConsolePrinter;
import utils.Types.CostBreakdown;
import repository.Interfaces.ClientRepository;
import repository.implimentation.ClientRepositoryImpl;
import repository.Interfaces.ProjectRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        ClientServiceImpl clientServiceImpl = new ClientServiceImpl(clientRepository);
        clientServiceImpl.createClient(new Client(null, name, address, phoneNumber, ispro));
        System.out.println("Client created Successfully");

    }

    static public void createProject(){
        System.out.print(" ==> Entre Client's ID: ");
        Integer clientId = scanner.nextInt();
        scanner.nextLine();

        ClientRepository clientRepository = new ClientRepositoryImpl();
        ClientServiceImpl clientServiceImpl = new ClientServiceImpl(clientRepository);
        Optional<Client> client =  clientServiceImpl.getClientById(clientId);

        if(client.isPresent()){
            ConsolePrinter.printClient(client.get());

            System.out.print(" ==> Entre Project Name: ");
            String projectName = scanner.nextLine();
            System.out.print(" ==> Entre Profit Margin (%): ");
            Double profit = scanner.nextDouble();
            scanner.nextLine();

            ProjectRepository ProjectRepository = new repository.implimentation.ProjectRepositoryImpl();
            ProjectServiceImpl ProjectServiceImpl = new ProjectServiceImpl(ProjectRepository);

            Project Project = new Project(null, projectName, profit, null,null, null);
            Project.setClient(client.get());

            materialsLoop:
            while (true){
                scanner.nextLine();
                System.out.print(" ==> Do you want to add Materials to this Project? [y/n]: ");
                String componentsChoice = scanner.nextLine();
                switch (componentsChoice){
                    case "y":
                        Material material =  addMaterialsView();
                        Project.getComponents().add(material);
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
                        Labour Labour =  addLaborView();
                        Project.getComponents().add(Labour);
                        ConsolePrinter.printSuccess("Labor Has Been Added Successfully");
                        break;
                    case "n":
                        break laborLoop;
                }
            }

            CostBreakdown costBreakdown = calculateCost(Project.getComponents());

            System.out.print(" ==> Do you want to apply a profit margin? [y/n]: ");
            String marginChoice = scanner.nextLine();

            if(marginChoice.equals("y")){
                costBreakdown.setProfit(costBreakdown.getBaseCost() * (Project.getProfit() / 100));
            }

            System.out.print(" ==> Do you want to apply a Discount to This Client? [y/n]: ");
            String discountChoice = scanner.nextLine();

            if(discountChoice.equals("y")){
                System.out.print(" ==> Entre Discount percentage (%): ");
                Double discount = scanner.nextDouble();
                scanner.nextLine();

                Project.setDiscount(discount);
            }else {
                Project.setDiscount(0.0);
            }

            costBreakdown.setDiscount(costBreakdown.getProfit() * (Project.getDiscount() / 100));

            Project.setTotalCost(costBreakdown.getTotalCost());
            Project.setProjectStatus(ProjectStatus.INPROGRESS);


            ConsolePrinter.printCostDetails(costBreakdown);

            System.out.print(" ==> Do you want to save this project? [y/n]: ");
            String saveChoice = scanner.nextLine();

            if(saveChoice.equals("y")){
                ProjectServiceImpl.createprojectWithComponents(Project);
            }

            QuoteServiceImpl QuoteServiceImpl = new QuoteServiceImpl();
            addDevisView(Project);


        }else {
            System.out.println("Client not found");
        }

    }

    static public void acceptDevis(){
        System.out.print(" ==> Entre Client's ID: ");
        Integer clientId = scanner.nextInt();
        scanner.nextLine();

        ClientRepository clientRepository = new ClientRepositoryImpl();
        ClientServiceImpl clientServiceImpl = new ClientServiceImpl(clientRepository);
        Optional<Client> client =  clientServiceImpl.getClientById(clientId);

        if(client.isPresent()){
            ConsolePrinter.printClient(client.get());

            QuoteServiceImpl QuoteServiceImpl = new QuoteServiceImpl();
            List<Quote> quotesList = QuoteServiceImpl.getQuoteWithProject(client.get());

            for(Quote quotes : quotesList){
                ConsolePrinter.printQuote(quotes);
            }

            System.out.print(" ==> Do you want to accept a devis? [y/n]: ");
            String saveChoice = scanner.nextLine();

            if(saveChoice.equals("y")){
                System.out.print(" ==> Enter Devis ID [To Accept]: ");
                Integer quoteId = scanner.nextInt();
                scanner.nextLine();

                Quote currentquotes = quotesList.stream()
                        .filter(quote -> Objects.equals(quote.getId(), quoteId))
                        .findFirst()
                        .orElse(null);

                assert currentquotes != null;
                if(!currentquotes.getAccepted()){
                    try {
                        Quote returnedQuotes = QuoteServiceImpl.acceptQuote(currentquotes);
                        ConsolePrinter.printSuccess("Devis Accepted Successfully: ID " + returnedQuotes.getId());
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

    static private Material addMaterialsView(){
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

        return new Material(materialName, taxRate, ComponentType.MATERIEL, null,  unitCost, quanitity, transport, coefficient);

    }

    static private Labour addLaborView(){
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

        return new Labour(workerType, taxRate, ComponentType.LABOR, null,  hourlyRate, worKHoursCount, coefficient);

    }

    static private void addDevisView(Project Project){
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

        QuoteServiceImpl QuoteServiceImpl = new QuoteServiceImpl();
        Quote quote = new Quote(
                null,
                Project.getTotalCost(),
                LocalDate.parse(issueDate, formatter),
                LocalDate.parse(validity, formatter),
                Boolean.FALSE,
                Project
        );

        ConsolePrinter.printQuote(quote);
        QuoteServiceImpl.createQuote(quote);

    }

    static private CostBreakdown calculateCost(List<Component> components) {
        return components.stream()
                .map(component -> {
                    double baseCost = calculateBaseCost(component);
                    double taxAmount = baseCost * (component.getTaxRate() / 100);
                    return new CostBreakdown(baseCost, taxAmount);
                })
                .reduce(new CostBreakdown(0, 0), (subtotal, element) -> new CostBreakdown(
                        subtotal.getBaseCost() + element.getBaseCost(),
                        subtotal.getTaxAmount() + element.getTaxAmount()
                ));
    }

    static private double calculateBaseCost(Component component) {
        if (component instanceof Material Material) {
            return Material.getUnitCost()
                    * Material.getQuantity()
                    * Material.getQualityCoefficient()
                    + Material.getTransportCost();
        } else if (component instanceof Labour Labour) {
            return Labour.getHourlyRate()
                    * Labour.getWorkHoursCount()
                    * Labour.getProductivityRate();
        }
        return 0.0; // or throw an exception for unknown component types
    }

}
