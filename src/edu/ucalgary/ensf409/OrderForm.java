package edu.ucalgary.ensf409;

import java.sql.SQLException;
import java.util.Scanner;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Properties;

public class OrderForm {
	public String[] furnitureID = new String[10];
	public String furnitureCategory;
	public String furnitureType;
	public int quantity;
	private final Inventory inventory = new Inventory(
			"jdbc:mysql://localhost:3306/inventory", "root", "Bfluff3!");;


	public void getOrder() throws SQLException {
		String confFile = String.format("%s_query.conf", this.furnitureCategory.toLowerCase());
		
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(confFile));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String query = prop.getProperty(String.format("%s_SQL", this.furnitureCategory.toUpperCase()));

        int orders = 2;
        int numberOfParts = 2;
        String furnitureType = "Study";
        String[] currentPart = new String[numberOfParts];
        HashSet<String> completed = new HashSet<String>();
        boolean filled = true;

        try {
            // drop temporary table if exists
            Statement dropTableQuery = this.inventory.initializeConnection().createStatement();
        	dropTableQuery.executeUpdate("DROP TABLE IF EXISTS T");
            
        	//make temporary table, all combinations, sort by price in ascending
            PreparedStatement lampsQuery = this.inventory.initializeConnection().prepareStatement(query);
            lampsQuery.setString(1, furnitureType);
            lampsQuery.setString(2, furnitureType);
            lampsQuery.executeUpdate();
            
            //get set based on number of orders
            for(int counter = 0; counter < this.quantity; counter ++) {
            	//get one set
            	Statement resultsQuery = this.inventory.initializeConnection().createStatement();
            	ResultSet results = resultsQuery.executeQuery("SELECT * FROM T LIMIT 1");
            	//no set returned, cannot fill order
            	if(!results.isBeforeFirst()) {
            		filled = false;
            		break;
            	}
            	//one set found
            	while(results.next()) {
            		//place in completed and currentPart 
            		for(int i = 0; i < numberOfParts; i++) {
            			completed.add(results.getString("c"+i));
            			currentPart[i] = results.getString("c"+i);
                	}
            		//delete statement based on set taken
            		String delete = "DELETE FROM T WHERE c0 = '" + currentPart[0] + "'";
            		for(int i = 1; i < numberOfParts; i++) {
            			delete += " OR c" + i + " = '" + currentPart[i] + "'";
            		}
            		//delete from temporary table row components
            		Statement deleteQuery = this.inventory.initializeConnection().createStatement();
                	deleteQuery.executeUpdate(delete);
            	}
            	
            }
     
        } catch (SQLException e) {
            e.printStackTrace();
        }	
        if(filled) {
        	orderFilled(completed);
        }
        else {
        	orderNotFilled();
        }
	}
	
	public void orderNotFilled() {
		
	}
	
	public void orderFilled(HashSet<String> completed) {
    	List<String> components = new ArrayList<String>(completed);
    	String delete = "DELETE FROM " + furnitureCategory + " WHERE ID = " + components.get(0) + "'";
    	for(int i = 1; i < components.size(); i++) {
			delete += " OR ID = '" + components.get(i) + "'";
		}
    	System.out.println(delete);
	}

	public void printOrder() {

	}

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

	public static void main(String[] args) throws SQLException {
		OrderForm orderForm = new OrderForm();
		orderForm.getOrder();
	}
}
