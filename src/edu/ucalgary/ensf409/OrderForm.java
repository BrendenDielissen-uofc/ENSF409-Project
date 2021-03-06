/**
 * This class processes a user's order request and outputs the cheapest price
 *
 * @author Brenden Dielissen
 * @author Maria Martine Baclig
 * @author Nafisa Tabassum
 * @author Ronn Delos Reyes
 * @version 1.0
 */
package edu.ucalgary.ensf409;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
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
    private Inventory inventory;
    /**
     * The Cheapest combo.
     */
    public ArrayList<Furniture> cheapestCombo;

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
     * Calculates the minimum cost furniture combination, if it is possible, otherwise returns -1.
     *
     * @return the int
     */
    public int calculateOrder() {
        ArrayList<ArrayList<Furniture>> allFurnitureCombos = getAllFurnitureCombos();
        // this flag will be set false once we have a valid combination of furniture for
        // the desired quantity
        boolean flag = true;
        ArrayList<Furniture> possibleCheapCombo = new ArrayList<>();
        int lowestPrice = -1;
        // determine the lowest priced combo of furniture for the desired quantity
        for (ArrayList<Furniture> furnitureCombo : allFurnitureCombos) {
            int sum = 0;
            HashMap<String, Integer> countingMap = inventory.getFurnitureCountingMap(furnitureCategory);
            for (Furniture furniture : furnitureCombo) {
                sum += furniture.getPrice();
                HashMap<String, Boolean> componentMap = furniture.getComponents();
                for (String component : countingMap.keySet()) {
                    if (componentMap.get(component).equals(true))
                        // the counting map shows us the quantity per component for the furniture combination
                        countingMap.put(component, countingMap.get(component) + 1);
                }
            }
            // desirable combinations must contain the proper amount of components to satisfy the quantity requested,
            // so we can filter out anything that does not satisfy this requirement
            if (!countingMap.entrySet().stream().anyMatch(entry -> entry.getValue() < quantity)) {
                // this is our initial value to compare other combos to
                if (lowestPrice == -1) {
                    lowestPrice = sum;
                    possibleCheapCombo = furnitureCombo;
                } else if (sum < lowestPrice) {
                    possibleCheapCombo = furnitureCombo;
                    lowestPrice = sum;
                }
                flag = false;
            }
        }
        // this means we cannot fulfill the order
        if (flag)
            return -1;

        this.cheapestCombo = possibleCheapCombo;
        return lowestPrice;
    }

    private ArrayList<ArrayList<Furniture>> getAllFurnitureCombos() {
        // This method maps the relevant furniture items to the N-combination index lists,
        // essentially making the operation of creating the N-combinations slightly cheaper
        // in memory usage.

        // get all relevant furniture items from database
        ArrayList<Furniture> allFurnitureList = new ArrayList<Furniture>(
                Arrays.asList(inventory.getAllFurniture(furnitureType, furnitureCategory)));
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
        // DO NOT USE FOR A DATABASE WITH 30+ FURNITURE ITEMS PER CATEGORY -> NOT ENOUGH
        // HEAP MEMORY
        // this method generates a list of arrays, corresponding to all possible
        // combinations of an n-lengthed array that is comprised of elements 0 to (n-1)
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
     * Tries to fulfill the order based on requested user input. Prints order if it is possible,
     * otherwise prints the relevant furniture manufacturers.
     */
    public void fulfillOrder() {
        int cost = this.calculateOrder();
        if (cost == -1) {
            this.printManufacturers();
        } else {

            Furniture[] furnitureCombo = getFurnitureList();

            this.inventory.deleteFurniture(furnitureCombo);

            // Parsing order
            String order = "";
            order = order + "Furniture Order Form \n\n";
            order = order + "Faculty Name: \n";
            order = order + "Contact: \n";
            order = order + "Date: \n\n";

            order = order + "Original Request: " + furnitureType + " " + furnitureCategory + ", "
                    + Integer.toString(quantity) + "\n\nItems Ordered:\n";
            for (int i = 0; i < furnitureCombo.length; i++) {
                order = order + "ID: " + furnitureCombo[i].getId() + " $" + furnitureCombo[i].getPrice() + "\n";
            }
            order = order + "\nTotal Price: $" + Integer.toString(cost);

            printOrder(order);
        }
    }

    /**
     * Prints order consisting of the cheapest used furniture combination.
     *
     * @param order the order
     */
    public void printOrder(String order) {

        // Creating the file
        try {
            File orderFile = new File("orderform.txt");
            if (orderFile.createNewFile()) {
                System.out.println("\nFile created: " + orderFile.getName());

            } else {
                System.out.println("\nFile already exists.");
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
     * Getter for the cheapest furniture list.
     *
     * @return the furniture [ ]
     */
    public Furniture[] getFurnitureList() {
        Furniture[] array = new Furniture[this.cheapestCombo.size()];
        this.cheapestCombo.toArray(array);
        return array;
    }

    /**
     * Print manufacturers corresponding to the requested furniture type and category.
     */
    public void printManufacturers() {
        List<Manufacturer> furnitureManufacturers = Inventory.furnitureManufacturersMap.get(this.furnitureCategory);
        List<String> manufacturerNames = furnitureManufacturers.stream().map(manufacturer -> manufacturer.name)
                .collect(Collectors.toList());

        String order = "_____________________________________________\n";
        order = order + "Furniture Order Form \n\n";
        order = order + "Faculty Name: \n";
        order = order + "Contact: \n";
        order = order + "Date: \n\n";
        order = order + "Original Request: " + this.furnitureType + " " + this.furnitureCategory + ", " + this.quantity
                + "\n\n";
        order = order + "Order cannot be fulfilled based on current inventory. Suggested manufacturers:\n";
        order = order + String.join(", ", manufacturerNames);

        printOrder(order);
    }

    /**
     * Gets user input for a requested order. Handles error handling of inputs.
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
        System.out.println("\n---------------------------------------\n");
        scanner.close();

        this.fulfillOrder();

    }

    /**
     * The entry point of application. Please see the README.md for detailed instructions on running the application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Please see README.md for detailed instructions on running the application.
        OrderForm orderForm = new OrderForm();
        orderForm.requestOrder();
        orderForm.inventory.closeConnection();

    }
}