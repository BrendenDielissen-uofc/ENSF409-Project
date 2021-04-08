package edu.ucalgary.ensf409;

import java.sql.SQLException;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

/**
 * The type Order form.
 */
public class OrderForm {
    /**
     * The Furniture category.
     */
    public String furnitureCategory;
    /**
     * The Furniture type.
     */
    public String furnitureType;
    /**
     * The Quantity.
     */
    public int quantity;
    private final Inventory inventory;
    /**
     * The Cheapest combo.
     */
    public ArrayList<Furniture> cheapestCombo;

    /**
     * Instantiates a new Order form.
     */
    public OrderForm() {
        this.inventory = new Inventory("jdbc:mysql://localhost/INVENTORY", "Marasco", "ensf409");
        try {
            this.inventory.initializeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Calculates the total cost of the order
     *
     * @return the int
     */
    private int calculateOrder() {
        Furniture[] furnitureCombo = this.inventory.getCheapestOrder(this.furnitureType, this.furnitureCategory,
                this.quantity);
        ArrayList<Furniture> cheapestFurnitureCombo = furnitureCombo != null
                ? new ArrayList<Furniture>(Arrays.asList(furnitureCombo))
                : new ArrayList<>();

        if (cheapestFurnitureCombo.size() < 1)
            return -1;

        int sum = 0;
        // counting map stores the individual component count for the items, so this
        // could be mentioned as an expandable feature we have ( display missing items
        // that were need to fulfill an order or something)
        HashMap<String, Integer> countingMap = inventory.getFurnitureCountingMap(this.furnitureCategory);
        for (Furniture furniture : cheapestFurnitureCombo) {
            sum += furniture.getPrice();
            HashMap<String, Boolean> componentMap = furniture.getComponents();
            for (String component : countingMap.keySet()) {
                if (componentMap.get(component).equals(true))
                    countingMap.put(component, countingMap.get(component) + 1);
            }
        }
        this.cheapestCombo = cheapestFurnitureCombo;
        return sum;
    }

    /**
     * Print order.
     */
    private void fulfillOrder() {
        int cost = this.calculateOrder();
        if (cost == -1) {
            this.printManufacturers();
        } else {
            Furniture[] array = new Furniture[cheapestCombo.size()];
            cheapestCombo.toArray(array);

            // UNCOMMENT TO TEST DELETION
            // this.inventory.deleteFurniture(array);

            // Parsing order
            String order = "";
            order = order + "Furniture Order Form \n\n";
            order = order + "Faculty Name: \n";
            order = order + "Contact: \n";
            order = order + "Date: \n\n";
            order = order + "Original Request: " + this.furnitureType + " " + this.furnitureCategory + ", "
                    + this.quantity + "\n\n";
            for (Furniture furniture : cheapestCombo) {
                order = order + "ID: " + furniture.getId() + "\n";
            }
            order = order + "\n";
            order = order + "Total Price: $" + cost;
            printOrder(order);
        }
        this.inventory.closeConnection();
    }

    /**
     * Output order form file for successful orders
     * 
     * @param order Customer order
     */
    private void printOrder(String order) {
        // Creating the file
        try {
            File orderFile = new File("orderform.txt");
            if (orderFile.createNewFile()) {
                System.out.println("File created: " + orderFile.getName());

            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Writing to the file
        try {
            FileWriter orderWrite = new FileWriter("orderform.txt");
            orderWrite.write(order);
            orderWrite.close();
            System.out.println("Succesfully wrote to the file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints out list of manufacturers if order cannot be fulfilled
     */
    private void printManufacturers() {
        List<Manufacturer> furnitureManufacturers = Inventory.furnitureManufacturersMap.get(this.furnitureCategory);
        List<String> manufacturerNames = furnitureManufacturers.stream().map(manufacturer -> manufacturer.name).collect(Collectors.toList());

        System.out.println("_____________________________________________");
        System.out.println("Furniture Order Form \n");
        System.out.println("Faculty Name: ");
        System.out.println("Contact: ");
        System.out.println("Date: \n");
        System.out.println(
                "Original Request: " + this.furnitureType + " " + this.furnitureCategory + ", " + this.quantity + "\n");
        System.out.println("Order cannot be fulfilled based on current inventory. Suggested manufacturers:");

        System.out.println(String.join(", ", manufacturerNames));
    }

    /**
     * Gets request.
     */
    public void requestOrder() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\tUofC Used Furniture Request Form");
        System.out.println("---------------------------------------\n");

        try {
            System.out.println("Enter furniture category: ");
            furnitureCategory = scanner.nextLine().toUpperCase().trim();
            System.out.println("Enter furniture type: ");
            furnitureType = scanner.nextLine().toUpperCase().trim();
            System.out.println("Enter number of items needed: ");
            quantity = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.close();
            throw new IllegalArgumentException(
                    "Category and type must be strings, number of items must be an integer number.");
        }

        if (!furnitureCategory.chars().allMatch(Character::isLetter)
                || !new ArrayList<String>(Inventory.furnitureTypesMap.keySet()).contains(furnitureCategory)) {
            scanner.close();
            throw new IllegalArgumentException(
                    String.format("Furniture category \"%s\" is invalid.", furnitureCategory));
        }

        var test = Inventory.furnitureTypesMap.get(furnitureCategory);

        if (!furnitureType.chars().allMatch(Character::isLetter)
                || !Inventory.furnitureTypesMap.get(furnitureCategory).contains(furnitureType)) {
            scanner.close();
            throw new IllegalArgumentException(String.format("Furniture type \"%s\" is invalid.", furnitureType));
        }

        if (quantity < 1) {
            scanner.close();
            throw new IllegalArgumentException("Number of items must be greater than 0.");
        }

        System.out.println("\n\tRequest received for:");
        System.out.println("---------------------------------------\n");
        System.out.println("Furniture Category: " + furnitureCategory);
        System.out.println("Furniture Type: " + furnitureType);
        System.out.println("Quantity: " + quantity);
        scanner.close();

        this.fulfillOrder();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        OrderForm orderForm = new OrderForm();
//        orderForm.requestOrder();

        // set dummy data for the corresponding values
         orderForm.furnitureCategory = "LAMP";
         orderForm.furnitureType = "DESK";
         orderForm.quantity = 4;
         orderForm.fulfillOrder();
    }
}
