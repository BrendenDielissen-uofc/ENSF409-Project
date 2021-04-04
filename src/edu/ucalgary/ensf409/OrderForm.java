package edu.ucalgary.ensf409;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Order form.
 */
public class OrderForm {
    /**
     * The Furniture id.
     */
    public String[] furnitureID = new String[10];
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
     * Instantiates a new Order form.
     */
    public OrderForm() {
        this.inventory = new Inventory("jdbc:mysql://localhost/INVENTORY", "scm", "ensf409");
        try {
            this.inventory.initializeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Calculate order int.
     *
     * @return the int
     */
    public int calculateOrder() {
        // get all furniture of the correct type, as an ArrayList
        ArrayList<Furniture> allFurnitureList = new ArrayList<Furniture>(
                Arrays.asList(inventory.getAllFurniture(furnitureType, furnitureCategory)));
        if (allFurnitureList.size() < 1)
            return -1;
        // use this map to help ensure we have correct quantity of each component that
        // the furniture item requires.
        HashMap<String, Integer> countingMap = inventory.getCountingMap(furnitureCategory);
        // create a linkedHashSet to store the furniture items we want to use, ensuring
        // no duplicates can be added
        LinkedHashSet<Furniture> furnitureLinkedHashSet = new LinkedHashSet<>();
        // flag will be set to true if we cannot fulfill the order with the furniture
        // currently in the database.
        boolean flag = false;
        // countingMap's keys correspond to all of the components necessary to build the
        // particular furniture item.
        // we need to loop through them all, and check if the quantities of components
        // available will be able fulfill the requested
        // order quantity.
        for (String component : countingMap.keySet()) {
            // get all furniture items that have the component in them (i.e. value of true)
            ArrayList<Furniture> availableFurniture = allFurnitureList.stream()
                    .filter(furniture -> furniture.getComponents().get(component).equals(true))
                    .collect(Collectors.toCollection(ArrayList<Furniture>::new));
            // if there are not enough furniture items with the component quantities we
            // need, set flag true, but keep going because we wanna know what we are
            // missing!
            if (availableFurniture.size() < quantity) {
                flag = true;
                // add how many of these components we do have available currently, might be
                // handy!
                countingMap.put(component, availableFurniture.size());
                continue;
            }
            // sort them by price
            availableFurniture.sort(Comparator.comparing(Furniture::getPrice));
            // grab necessary amount of furniture items, from bottom of list (i.e. lowest
            // priced items)
            availableFurniture = new ArrayList<>(availableFurniture.subList(0, quantity));
            // append the cheapest to our usedFurniture list
            furnitureLinkedHashSet.addAll(availableFurniture);
            countingMap.put(component, quantity);
        }
        if (flag) {
            // this means we cannot fulfill the order
            // countingMap should show which components we are missing!!
            return -1;
        }
        // usedFurniture -> contains the furniture for a possible solution(s), but
        // it/they still need(s) to be found!!
        return 1;
    }

    /**
     * Gets order.
     */
    public void getOrder() {

    }

    /**
     * Print order.
     */
    public void printOrder() {

    }

    /**
     * Gets request.
     */
    public void getRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Used Furniture Request Form");

        System.out.println("Enter furniture category: ");
        this.furnitureCategory = scanner.nextLine();

        System.out.println("Enter furniture type: ");
        this.furnitureType = scanner.nextLine();

        System.out.println("Enter number of items needed: ");
        this.quantity = scanner.nextInt();

        System.out.println("Request received for:");
        System.out.println("Furniture Category: " + this.furnitureCategory);
        System.out.println("Furniture Type: " + this.furnitureType);
        System.out.println("Quantity: " + this.quantity);
        scanner.close();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        OrderForm orderForm = new OrderForm();
        // orderForm.getRequest();
        // set dummy data for the corresponding values
        orderForm.furnitureCategory = "desk";
        orderForm.furnitureType = "standing";
        orderForm.quantity = 1;
        var cost = orderForm.calculateOrder();
    }
}
