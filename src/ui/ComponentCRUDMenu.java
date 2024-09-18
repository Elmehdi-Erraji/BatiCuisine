package ui;

import config.dbConnection;
import repository.implimentation.MaterialsRepositoryImpl;
import repository.implimentation.LabourRepositoryImpl;
import domain.entities.Material;
import domain.entities.Labour;
import domain.enums.ComponentType;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ComponentCRUDMenu {
    static Connection connection = dbConnection.getConnection();
    private static final Scanner scanner = new Scanner(System.in);
    private static final MaterialsRepositoryImpl MaterialRepo = new MaterialsRepositoryImpl(connection);
    private static final LabourRepositoryImpl LabourRepo = new LabourRepositoryImpl(connection);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Main Menu:");
            System.out.println("1. Add Component");
            System.out.println("2. Update Component");
            System.out.println("3. Delete Component");
            System.out.println("4. List Components");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = getChoice();

            switch (choice) {
                case 1:
                    addComponent();
                    break;
                case 2:
                    updateComponent();
                    break;
                case 3:
                    deleteComponent();
                    break;
                case 4:
                    listComponents();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addComponent() {
        System.out.println("Choose component type to add:");
        System.out.println("1. Material");
        System.out.println("2. Labour");
        System.out.print("Choose an option: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                addMaterial();
                break;
            case 2:
                addLabour();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void updateComponent() {
        System.out.println("Choose component type to update:");
        System.out.println("1. Material");
        System.out.println("2. Labour");
        System.out.print("Choose an option: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                updateMaterial();
                break;
            case 2:
                updateLabour();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void deleteComponent() {
        System.out.println("Choose component type to delete:");
        System.out.println("1. Material");
        System.out.println("2. Labour");
        System.out.print("Choose an option: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                deleteMaterial();
                break;
            case 2:
                deleteLabour();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void listComponents() {
        System.out.println("Choose component type to list:");
        System.out.println("1. Material");
        System.out.println("2. Labour");
        System.out.print("Choose an option: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                listMaterial();
                break;
            case 2:
                listLabour();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void addMaterial() {
        System.out.print("Enter material name: ");
        String name = scanner.next();
        System.out.print("Enter tax rate: ");
        Double taxRate = getDoubleInput();
        System.out.print("Enter unit cost: ");
        Double unitCost = getDoubleInput();
        System.out.print("Enter quantity: ");
        Double quantity = getDoubleInput();
        System.out.print("Enter transport cost: ");
        Double transportCost = getDoubleInput();
        System.out.print("Enter quality coefficient: ");
        Double qualityCoefficient = getDoubleInput();

        Material material = new Material(name, taxRate, null, ComponentType.MATERIAL, unitCost, quantity, transportCost, qualityCoefficient);
        MaterialRepo.save(material);
        System.out.println("Material added successfully.");
    }

    private static void updateMaterial() {
        System.out.print("Enter material ID to update: ");
        int id = scanner.nextInt();
        Optional<Material> optionalMaterial = MaterialRepo.findById(id);

        if (optionalMaterial.isPresent()) {
            Material material = optionalMaterial.get();
            System.out.println("Current material details:");
            System.out.println(material);

            System.out.print("Enter new name: ");
            String name = scanner.next();
            System.out.print("Enter new tax rate: ");
            Double taxRate = getDoubleInput();
            System.out.print("Enter new unit cost: ");
            Double unitCost = getDoubleInput();
            System.out.print("Enter new quantity: ");
            Double quantity = getDoubleInput();
            System.out.print("Enter new transport cost: ");
            Double transportCost = getDoubleInput();
            System.out.print("Enter new quality coefficient: ");
            Double qualityCoefficient = getDoubleInput();

            material.setName(name);
            material.setTaxRate(taxRate);
            material.setUnitCost(unitCost);
            material.setQuantity(quantity);
            material.setTransportCost(transportCost);
            material.setQualityCoefficient(qualityCoefficient);

            MaterialRepo.save(material);
            System.out.println("Material updated successfully.");
        } else {
            System.out.println("Material not found.");
        }
    }

    private static void deleteMaterial() {
        System.out.print("Enter material ID to delete: ");
        int id = scanner.nextInt();
        MaterialRepo.deleteById(id);
        System.out.println("Material deleted successfully.");
    }

    private static void listMaterial() {
        List<Material> materials = MaterialRepo.findAll();
        if (materials.isEmpty()) {
            System.out.println("No Material found.");
        } else {
            for (Material material : materials) {
                System.out.println(material);
            }
        }
    }

    private static void addLabour() {
        System.out.print("Enter Labour name: ");
        String name = scanner.next();
        System.out.print("Enter tax rate: ");
        Double taxRate = getDoubleInput();
        System.out.print("Enter hourly rate: ");
        Double hourlyRate = getDoubleInput();
        System.out.print("Enter work hours count: ");
        Double workHoursCount = getDoubleInput();
        System.out.print("Enter productivity rate: ");
        Double productivityRate = getDoubleInput();

        Labour labour = new Labour(name, taxRate, null, ComponentType.LABOR, hourlyRate, workHoursCount, productivityRate);
        LabourRepo.save(labour);
        System.out.println("Labour added successfully.");
    }

    private static void updateLabour() {
        System.out.print("Enter Labour ID to update: ");
        int id = scanner.nextInt();
        Optional<Labour> optionalLabour = LabourRepo.findById(id);

        if (optionalLabour.isPresent()) {
            Labour labour = optionalLabour.get();
            System.out.println("Current Labour details:");
            System.out.println(labour);

            System.out.print("Enter new name: ");
            String name = scanner.next();
            System.out.print("Enter new tax rate: ");
            Double taxRate = getDoubleInput();
            System.out.print("Enter new hourly rate: ");
            Double hourlyRate = getDoubleInput();
            System.out.print("Enter new work hours count: ");
            Double workHoursCount = getDoubleInput();
            System.out.print("Enter new productivity rate: ");
            Double productivityRate = getDoubleInput();

            labour.setName(name);
            labour.setTaxRate(taxRate);
            labour.setHourlyRate(hourlyRate);
            labour.setWorkHoursCount(workHoursCount);
            labour.setProductivityRate(productivityRate);

            LabourRepo.save(labour);
            System.out.println("Labour updated successfully.");
        } else {
            System.out.println("Labour not found.");
        }
    }

    private static void deleteLabour() {
        System.out.print("Enter Labour ID to delete: ");
        int id = scanner.nextInt();
        LabourRepo.deleteById(id);
        System.out.println("Labour deleted successfully.");
    }

    private static void listLabour() {
        List<Labour> labourList = LabourRepo.findAll();
        if (labourList.isEmpty()) {
            System.out.println("No Labour found.");
        } else {
            for (Labour labour : labourList) {
                System.out.println(labour);
            }
        }
    }

    private static int getChoice() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next(); // clear invalid input
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static Double getDoubleInput() {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                scanner.next(); // clear invalid input
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
}
