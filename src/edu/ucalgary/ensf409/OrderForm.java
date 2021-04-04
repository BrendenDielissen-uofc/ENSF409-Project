package edu.ucalgary.ensf409;

import java.sql.SQLException;
import java.util.*;

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
        ArrayList<ArrayList<Furniture>> allFurnitureCombos = getAllFurnitureCombos();
        // this flag will be set false once we have a valid combination of furniture for the desired quantity
        boolean flag = true;
        ArrayList<Furniture> cheapestCombo = new ArrayList<>();
        int lowestPrice = -1;
        // determine the lowest priced combo of furniture for the desired quantity
        for (ArrayList<Furniture> furnitureCombo : allFurnitureCombos) {
            int sum = 0;
            HashMap<String, Integer> countingMap = inventory.getCountingMap(furnitureCategory);
            for (Furniture furniture : furnitureCombo) {
                sum += furniture.getPrice();
                HashMap<String, Boolean> componentMap = furniture.getComponents();
                for (String component : countingMap.keySet()) {
                    if (componentMap.get(component).equals(true))
                        countingMap.put(component, countingMap.get(component) + 1);
                }
            }
            if (!countingMap.entrySet().stream().anyMatch(entry -> entry.getValue() < quantity)) {
                // this is our initial value to compare other combos to
                if (lowestPrice == -1) {
                    lowestPrice = sum;
                    cheapestCombo = furnitureCombo;
                } else if (sum < lowestPrice) {
                    cheapestCombo = furnitureCombo;
                }
                flag = false;
            }
        }

        if (flag) {
            // this means we cannot fulfill the order
            // countingMap should show which components we are missing!!
            return -1;
        }
        return 1;
    }

    /**
     * Get all possible furniture combos of the furniture type requested.
     *
     * @return the array list
     */
    public ArrayList<ArrayList<Furniture>> getAllFurnitureCombos() {
        // get all relevant furniture items from database
        ArrayList<Furniture> allFurnitureList = new ArrayList<Furniture>(Arrays.asList(inventory.getAllFurniture(furnitureType, furnitureCategory)));
        if (allFurnitureList.size() < 1)
            return null;
        // generate the indexes for all possible combinations
        List<int[]> indexLists = generateIndexLists(allFurnitureList.size());
        // map furniture items to the index lists
        ArrayList<ArrayList<Furniture>> allFurnitureCombos = new ArrayList<>();
        for (int[] indexArr : indexLists) {
            ArrayList<Furniture> furnitureList = new ArrayList<>();
            for (int index : indexArr)
                furnitureList.add(allFurnitureList.get(index));
            allFurnitureCombos.add(furnitureList);
        }
        return allFurnitureCombos;
    }

    private List<int[]> generateIndexLists(int n) {
        // DO NOT USE FOR A DATABASE WITH 30+ FURNITURE ITEMS PER CATEGORY -> NOT ENOUGH HEAP MEMORY
        // this method generates a list of arrays, corresponding to all possible combinations
        // of an n-lengthed array that is comprised of elements 0 to (n-1)

        // example: generateIndexLists(2) -> List( [0], [1], [0, 1], [1, 0] )
        int r = n;
        List<int[]> allCombinations = new ArrayList<>();
        while (r > 0) {
            List<int[]> rCombinations = new ArrayList<>();
            int[] combination = new int[r];

            for (int i = 0; i < r; i++)
                combination[i] = i;
            while (combination[r - 1] < n) {
                rCombinations.add(combination.clone());

                int t = r - 1;
                while (t != 0 && combination[t] == n - r + t)
                    t--;
                combination[t]++;
                for (int i = t + 1; i < r; i++)
                    combination[i] = combination[i - 1] + 1;
            }
            allCombinations.addAll(rCombinations);
            r--;
        }
        return allCombinations;
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
//        orderForm.getRequest();
        // set dummy data for the corresponding values
        orderForm.furnitureCategory = "filing";
        orderForm.furnitureType = "medium";
        orderForm.quantity = 1;
        var cost = orderForm.calculateOrder();
    }
}
