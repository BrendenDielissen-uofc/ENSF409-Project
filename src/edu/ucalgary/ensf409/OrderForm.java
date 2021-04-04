package edu.ucalgary.ensf409;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
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
        this.inventory = new Inventory("jdbc:mysql://localhost/inventory", "Marasco", "ensf409");
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
        ArrayList<Furniture> allFurnitureList = new ArrayList<Furniture>(Arrays.asList(inventory.getAllFurniture(furnitureType, furnitureCategory)));
        if(allFurnitureList.size() < 1)
            return -1;
        // use this map to help ensure we have correct quantity of each component comprising the furniture item
        HashMap<String, Integer> countingMap = inventory.getCountingMap(furnitureCategory);
        // store furniture items that we want to use
        ArrayList<Furniture> usedUpFurniture = new ArrayList<>();
        // continue try to get values unless we have counted the correct quantity of each component comprising the furniture item
        while(countingMap.keySet().stream().anyMatch(key -> countingMap.get(key) < quantity)){
            // countingMap's keys correspond to all of the components necessary to build the particular furniture item.
            // we need to loop through them all, adding to corresponding countingMap entry whenever we can add a new
            // furniture item that has the matching component.
            for (String component : countingMap.keySet()) {
                // get all furniture items that have the component in them (i.e. value of true)
                ArrayList<Furniture> availableFurniture = allFurnitureList.stream().filter(furniture -> furniture.getComponents().get(component).equals(true)).collect(Collectors.toCollection(ArrayList<Furniture>::new));
                if(availableFurniture.size() < 1)
                    continue;
                // loop through all the furniture items that have the desired component to find the cheapest
                Furniture cheapestFurniture = availableFurniture.get(0);
                for(Furniture furniture : availableFurniture){
                    if(furniture.getPrice() < cheapestFurniture.getPrice())
                        cheapestFurniture = furniture;
                }
                // append the cheapest to our usedFurniture list
                usedUpFurniture.add(cheapestFurniture);
                // increase the count of that component in the counting map, corresponding to the correct component of that furniture
                countingMap.put(component, countingMap.get(component)+1);
            }
            allFurnitureList.removeAll(usedUpFurniture);
            if(allFurnitureList.size() < quantity)
                break;
        }
        if(countingMap.keySet().stream().anyMatch(key -> countingMap.get(key) < quantity))
        {
            // this means we cannot fulfill the order
            return -1;
        }
        // usedFurniture -> contains the furniture for a possible solution(s), but it/they still need(s) to be found!!

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
        //orderForm.getRequest();
        // set dummy data for the corresponding values
        orderForm.furnitureCategory = "chair";
        orderForm.furnitureType = "mesh";
        orderForm.quantity = 2;
        var cost = orderForm.calculateOrder();
    }
}
