package edu.ucalgary.ensf409;

import java.sql.SQLException;
import java.util.*;

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
        this.inventory = new Inventory("jdbc:mysql://localhost/inventory", "Marasco", "ensf409");
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
        Furniture[] furnitureCombo = this.inventory.getCheapestOrder(this.furnitureType, this.furnitureCategory, this.quantity);
        ArrayList<Furniture> cheapestFurnitureCombo = furnitureCombo != null ? new ArrayList<Furniture>(Arrays.asList(furnitureCombo)) : new ArrayList<>();

        if (cheapestFurnitureCombo.size() < 1)
            return -1;

        int sum = 0;
        // counting map stores the individual component count for the items, so this could be mentioned as an expandable feature we have ( display missing items
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
            //this.inventory.deleteFurniture(array);

            System.out.println("Furniture Order Form \n");
            System.out.println("Faculty Name: ");
            System.out.println("Contact: ");
            System.out.println("Date: \n");
            System.out.println("Original Request: " + this.furnitureType + " "
                    + this.furnitureCategory + ", " + this.quantity + "\n");
            System.out.println("Items Ordered");
            for (Furniture furniture : cheapestCombo) {
                System.out.println("ID: " + furniture.getId());
            }
            System.out.println();
            System.out.println("Total Price: " + cost);
        }
        this.inventory.closeConnection();
    }

    private void printManufacturers() {
        var furnitureManufacturers = Inventory.furnitureManufacturersMap.get(this.furnitureCategory);
        String[] manufacturerNames = (String[]) furnitureManufacturers.stream().map(manufacturer -> manufacturer.name).toArray();

        System.out.println("_____________________________________________");
        System.out.println("Furniture Order Form \n");
        System.out.println("Faculty Name: ");
        System.out.println("Contact: ");
        System.out.println("Date: \n");
        System.out.println("Original Request: " + this.furnitureType + " "
                + this.furnitureCategory + ", " + this.quantity + "\n");
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
            throw new IllegalArgumentException("Category and type must be strings, number of items must be an integer number.");
        }

        if (!furnitureCategory.chars().allMatch(Character::isLetter) || !new ArrayList<String>(Inventory.furnitureTypesMap.keySet()).contains(furnitureCategory))
            throw new IllegalArgumentException(String.format("Furniture category \"%s\" is invalid.", furnitureCategory));
        var test = Inventory.furnitureTypesMap.get(furnitureCategory);
        if (!furnitureType.chars().allMatch(Character::isLetter) || !Inventory.furnitureTypesMap.get(furnitureCategory).contains(furnitureType))
            throw new IllegalArgumentException(String.format("Furniture type \"%s\" is invalid.", furnitureType));
        if (quantity < 1)
            throw new IllegalArgumentException("Number of items must be greater than 0.");

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
        orderForm.requestOrder();

        // set dummy data for the corresponding values
//        orderForm.furnitureCategory = "Chair";
//        orderForm.furnitureType = "Mesh";
//        orderForm.quantity = 2;
    }
}
