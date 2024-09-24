package ui;

import domain.entities.*;
import domain.enums.ProjectStatus;
import domain.enums.ComponentType;
import repository.implimentation.ProjectRepositoryImpl;
import service.implimentation.ClientServiceImpl;
import service.implimentation.ProjectServiceImpl;
import service.implimentation.QuoteServiceImpl;
import utils.ConsolePrinter;
import utils.Types.CostBreakdown;
import repository.Interfaces.ClientRepository;
import repository.implimentation.ClientRepositoryImpl;
import repository.Interfaces.ProjectRepository;
import validation.InputValidator;

import java.time.LocalDate;
import java.util.*;

public class MainView {
    private static final Scanner scanner = new Scanner(System.in);
    private static ClientRepository clientRepository = new ClientRepositoryImpl();
    private static ClientServiceImpl clientService = new ClientServiceImpl(clientRepository);
    private static ProjectRepository projectRepository = new ProjectRepositoryImpl();
    private static ProjectServiceImpl projectService = new ProjectServiceImpl(projectRepository);

    static public void createClient() {
        String name = InputValidator.validateNonEmptyString(" ==> Enter Full Name: ");
        String lowercaseName = name.toLowerCase();

        Optional<Client> existingClient = clientService.getClientByName(lowercaseName);

        if (existingClient.isPresent()) {
            System.out.println("Error: A client with this name already exists. Please try again.");
        } else {
            String address = InputValidator.validateNonEmptyString(" ==> Enter Address: ");
            String phoneNumber = InputValidator.validatePhoneNumber(" ==> Enter Phone Number: ");
            boolean isProfessional = InputValidator.validateYesNo(" ==> Is Professional?");

            Client newClient = new Client(null, name, address, phoneNumber, isProfessional);
            clientService.createClient(newClient);
            System.out.println("Client created Successfully");
        }
    }


    static public void createProject() {
        String clientName = InputValidator.validateNonEmptyString(" ==> Enter Client's Name: ");
        String lowercaseName = clientName.toLowerCase();
        Optional<Client> client = clientService.getClientByName(lowercaseName);

        if (client.isPresent()) {
            ConsolePrinter.printClient(client.get());
            String projectName = InputValidator.validateNonEmptyString(" ==> Enter Project Name: ");
            Double profit = InputValidator.validateDouble(" ==> Enter Profit Margin (%): ");

            ProjectRepository projectRepository = new repository.implimentation.ProjectRepositoryImpl();
            ProjectServiceImpl projectService = new ProjectServiceImpl(projectRepository);
            Project project = new Project(null, projectName, profit, null, null, null);
            project.setClient(client.get());

            materialsLoop:
            while (true) {
                boolean addMaterial = InputValidator.validateYesNo(" ==> Do you want to add Materials to this Project?");
                if (addMaterial) {
                    Material material = addMaterialsView();
                    project.getComponents().add(material);
                    ConsolePrinter.printSuccess("Material Has Been Added Successfully");
                } else {
                    break materialsLoop;
                }
            }

            laborLoop:
            while (true) {
                boolean addLabor = InputValidator.validateYesNo(" ==> Do you want to add Labor to this Project?");
                if (addLabor) {
                    Labour labour = addLaborView();
                    project.getComponents().add(labour);
                    ConsolePrinter.printSuccess("Labor Has Been Added Successfully");
                } else {
                    break laborLoop;
                }
            }

            CostBreakdown costBreakdown = calculateCost(project.getComponents());

            boolean applyMargin = InputValidator.validateYesNo(" ==> Do you want to apply a profit margin?");
            if (applyMargin) {
                costBreakdown.setProfit(costBreakdown.getBaseCost() * (project.getProfit() / 100));
            }

            boolean applyDiscount = InputValidator.validateYesNo(" ==> Do you want to apply a Discount to This Client?");
            if (applyDiscount) {
                Double discount = InputValidator.validateDouble(" ==> Enter Discount percentage (%): ");
                project.setDiscount(discount);
            } else {
                project.setDiscount(0.0);
            }

            costBreakdown.setDiscount(costBreakdown.getProfit() * (project.getDiscount() / 100));
            project.setTotalCost(costBreakdown.getTotalCost());
            project.setProjectStatus(ProjectStatus.INPROGRESS);

            ConsolePrinter.printCostDetails(costBreakdown);

            boolean saveProject = InputValidator.validateYesNo(" ==> Do you want to save this project?");
            if (saveProject) {
                projectService.createprojectWithComponents(project);
            }

            QuoteServiceImpl quoteService = new QuoteServiceImpl();
            addquoteView(project);
        } else {
            System.out.println("Client not found");
        }
    }

    static public void acceptDevis() {
        Integer clientId = InputValidator.validateInteger(" ==> Enter Client's ID: ");
        Optional<Client> client = clientService.getClientById(clientId);

        if (client.isPresent()) {
            ConsolePrinter.printClient(client.get());
            QuoteServiceImpl quoteService = new QuoteServiceImpl();
            List<Quote> quotesList = quoteService.getQuoteWithProject(client.get());

            for (Quote quotes : quotesList) {
                ConsolePrinter.printQuote(quotes);
            }

            if (InputValidator.validateYesNo(" ==> Do you want to accept a devis?")) {
                Integer quoteId = InputValidator.validateInteger(" ==> Enter Devis ID [To Accept]: ");
                Quote currentQuote = quotesList.stream()
                        .filter(quote -> Objects.equals(quote.getId(), quoteId))
                        .findFirst()
                        .orElse(null);

                if (currentQuote != null && !currentQuote.getAccepted()) {
                    try {
                        Quote returnedQuote = quoteService.acceptQuote(currentQuote);
                        ConsolePrinter.printSuccess("Devis Accepted Successfully: ID " + returnedQuote.getId());
                    } catch (Exception e) {
                        ConsolePrinter.printError(e.getMessage());
                    }
                } else {
                    ConsolePrinter.printError(currentQuote == null ? "Devis not found." : "Devis Already Accepted");
                }
            }
        } else {
            System.out.println("Client not found");
        }
    }

    static private Material addMaterialsView() {
        String materialName = InputValidator.validateNonEmptyString(" ==> Enter the name of the Material: ");
        Double unitCost = InputValidator.validateDouble(" ==> Enter the Unit Cost of the material [MAD]: ");
        Double quantity = InputValidator.validateDouble(" ==> Enter The Quantity of the Material: ");
        Double taxRate = InputValidator.validateDouble(" ==> Add the Tax Rate [%]: ");
        Double transport = InputValidator.validateDouble(" ==> Add Transport Cost [MAD]: ");
        Double coefficient = InputValidator.validateDouble(" ==> Add Material quality coefficient (1.0 = standard > 1.0 = high quality): ");

        return new Material(materialName, taxRate, ComponentType.MATERIEL, null, unitCost, quantity, transport, coefficient);
    }

    static private Labour addLaborView() {
        String workerType = InputValidator.validateNonEmptyString(" ==> Enter the Name of the worker: ");
        Double taxRate = InputValidator.validateDouble(" ==> Add the Tax Rate [%]: ");
        Double hourlyRate = InputValidator.validateDouble(" ==> Enter Hourly Rate of the Labor [MAD]: ");
        Double workHoursCount = InputValidator.validateDouble(" ==> Enter the number of working hours [Hours]: ");
        Double coefficient = InputValidator.validateDouble(" ==> Add Labor Productivity factor (1.0 = standard > 1.0 = high quality): ");

        return new Labour(workerType, taxRate, ComponentType.LABOR, null, hourlyRate, workHoursCount, coefficient);
    }

    static private void addquoteView(Project project) {
        if (InputValidator.validateYesNo(" ==> Do you want to Create Devis?")) {
            LocalDate issueDate = InputValidator.validateDate(" ==> Enter the issue date [YYYY-MM-DD]: ");
            LocalDate validityDate = InputValidator.validateDate(" ==> Valid Until [YYYY-MM-DD]: ");

            // Validate that issue date is before validity date
            try {
                InputValidator.validateIssueDateBeforeValidity(issueDate, validityDate);
            } catch (IllegalArgumentException e) {
                ConsolePrinter.printError(e.getMessage());
                return;
            }

            QuoteServiceImpl quoteService = new QuoteServiceImpl();
            Quote quote = new Quote(null, project.getTotalCost(), issueDate, validityDate, Boolean.FALSE, project);
            ConsolePrinter.printQuote(quote);
            quoteService.createQuote(quote);
        }
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
        if (component instanceof Material material) {
            return material.getUnitCost() * material.getQuantity() * material.getQualityCoefficient() + material.getTransportCost();
        } else if (component instanceof Labour labour) {
            return labour.getHourlyRate() * labour.getWorkHoursCount() * labour.getProductivityRate();
        }
        return 0.0;
    }

    public static void updateClient() {
        System.out.print("Enter client ID to update: ");
        int clientId = InputValidator.validateInteger("Client ID: ");
        Optional<Client> clientOptional = clientService.getClientById(clientId);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            String name = InputValidator.validateNonEmptyString("Enter new client's name (leave empty to keep current): ");
            if (!name.isEmpty()) client.setName(name);
            String address = InputValidator.validateNonEmptyString("Enter new client's address (leave empty to keep current): ");
            if (!address.isEmpty()) client.setAddress(address);
            String phoneNumber = InputValidator.validatePhoneNumber("Enter new client's phone number (leave empty to keep current): ");
            if (!phoneNumber.isEmpty()) client.setPhone(phoneNumber);
            boolean isProfessional = InputValidator.validateYesNo("Is the client a professional? (Y/N, leave empty to keep current): ");
            if (isProfessional) client.setProfessional(true);

            clientService.updateClient(client);
            System.out.println("Client updated successfully.");
        } else {
            System.out.println("Client not found.");
        }
    }

    public static void deleteClient() {
        System.out.print("Enter client ID to delete: ");
        int clientId = InputValidator.validateInteger("Client ID: ");
        Optional<Client> clientOptional = clientService.getClientById(clientId);

        if (clientOptional.isPresent()) {
            clientService.deleteClient(clientId);
            System.out.println("Client deleted successfully.");
        } else {
            System.out.println("Client not found.");
        }
    }

    public static void deleteProject(){
        System.out.println("Enter project ID to delete :");
        int projectId = InputValidator.validateInteger("Project ID: ");
        Optional<Project> projectOptional = projectService.getprojectById(projectId);

        if (projectOptional.isPresent()) {
            projectService.deleteproject(projectId);
            System.out.println("Project deleted successfully.");
        }else{
            System.out.println("Project not found.");
        }
    }

    public static void getProjectDetails() {
        int projectId = InputValidator.validateInteger(" ==> Enter Project's ID: ");
        Optional<Project> projectOptional = projectService.getprojectById(projectId);

        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            ConsolePrinter.printProject(project);
        } else {
            System.out.println("Project not found.");
        }
    }

}
