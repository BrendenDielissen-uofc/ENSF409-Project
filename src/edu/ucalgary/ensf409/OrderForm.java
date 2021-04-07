package edu.ucalgary.ensf409;

import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;
import java.util.*;

public class OrderForm {
	public String furnitureCategory;
	public String furnitureType;
	public int quantity;
	private final Inventory inventory = new Inventory(
			"jdbc:mysql://localhost:3306/inventory", "root", "Bfluff3!");
//	private final Inventory inventory = new Inventory(
//			"jdbc:mysql://localhost/inventory", "scm", "ensf409!");


	public void getOrder() throws SQLException {
		
        int numberOfParts = 0;
        HashSet<String> completed = new HashSet<String>();
        boolean filled = true;
        String createTable = "";

        try {
            // drop temporary table if exists
            Statement dropTableQuery = this.inventory.initializeConnection().createStatement();
        	dropTableQuery.executeUpdate("DROP TABLE IF EXISTS T");
            
        	//make temporary table, all combinations, sort by price in ascending
            if (this.furnitureCategory.equalsIgnoreCase("lamp")) {
            	createTable = "CREATE TABLE T AS SELECT l1.ID AS c0, l2.ID AS c1, CASE\r\n"
            			+ "WHEN l1.ID = l2.ID THEN l1.Price\r\n"
            			+ "ELSE l1.Price + l2.Price\r\n"
            			+ "END AS TotalPrice\r\n"
            			+ "FROM (SELECT l1.ID, l1.Price\r\n"
            			+ "FROM LAMP as l1\r\n"
            			+ "WHERE l1.Bulb = 'Y' and l1.Type = ?) AS l1\r\n"
            			+ "CROSS JOIN (SELECT l2.ID, l2.Price\r\n"
            			+ "FROM LAMP as l2\r\n"
            			+ "WHERE l2.Base = 'Y' and l2.Type = ?) AS l2\r\n"
            			+ "ORDER BY TotalPrice ASC;";
                numberOfParts = 2;
            } else if (this.furnitureCategory.equalsIgnoreCase("filing")){
            	createTable = "CREATE TABLE T AS SELECT c0, c1, l3.ID AS c2, CASE\r\n"
            			+ "WHEN l3.ID = c0 OR l3.ID = c1 THEN l.Price\r\n"
            			+ "ELSE l.Price + l3.Price\r\n"
            			+ "END AS TotalPrice\r\n"
            			+ "FROM (SELECT l3.ID, l3.Price\r\n"
            			+ "FROM FILING as l3\r\n"
            			+ "WHERE l3.Rails = 'Y' and l3.Type = ?) AS l3\r\n"
            			+ "CROSS JOIN\r\n"
            			+ "(SELECT l1.ID AS c0, l2.ID AS c1, CASE\r\n"
            			+ "WHEN l1.ID = l2.ID THEN l1.Price\r\n"
            			+ "ELSE l1.Price + l2.Price\r\n"
            			+ "END AS Price\r\n"
            			+ "FROM (SELECT l1.ID, l1.Price\r\n"
            			+ "FROM FILING as l1\r\n"
            			+ "WHERE l1.Drawers = 'Y' and l1.Type = ?) AS l1\r\n"
            			+ "CROSS JOIN (SELECT l2.ID, l2.Price\r\n"
            			+ "FROM FILING as l2\r\n"
            			+ "WHERE l2.Cabinet = 'Y' and l2.Type = ?) AS l2) AS l\r\n"
            			+ "ORDER BY TotalPrice ASC;";
                numberOfParts = 3;
            } else if (this.furnitureCategory.equalsIgnoreCase("desk")) {
            	createTable = "CREATE TABLE T AS SELECT c0, c1, l3.ID AS c2, CASE\r\n"
            			+ "WHEN l3.ID = c0 OR l3.ID = c1 THEN l.Price\r\n"
            			+ "ELSE l.Price + l3.Price\r\n"
            			+ "END AS TotalPrice\r\n"
            			+ "FROM (SELECT l3.ID, l3.Price\r\n"
            			+ "FROM DESK as l3\r\n"
            			+ "WHERE l3.Drawer = 'Y' and l3.Type = ?) AS l3\r\n"
            			+ "CROSS JOIN\r\n"
            			+ "(SELECT l1.ID AS c0, l2.ID AS c1, CASE\r\n"
            			+ "WHEN l1.ID = l2.ID THEN l1.Price\r\n"
            			+ "ELSE l1.Price + l2.Price\r\n"
            			+ "END AS Price\r\n"
            			+ "FROM (SELECT l1.ID, l1.Price\r\n"
            			+ "FROM DESK as l1\r\n"
            			+ "WHERE l1.Legs = 'Y' and l1.Type =?) AS l1\r\n"
            			+ "CROSS JOIN (SELECT l2.ID, l2.Price\r\n"
            			+ "FROM DESK as l2\r\n"
            			+ "WHERE l2.Top = 'Y' and l2.Type = ?) AS l2) AS l\r\n"
            			+ "ORDER BY TotalPrice ASC;";
                numberOfParts = 3;
            } else {
            	createTable = "CREATE TABLE T AS SELECT c0, c1, c2, l4.ID AS c3, CASE\r\n"
            			+ "WHEN l4.ID = c0 OR l4.ID = c1 OR l4.ID = c2 THEN f.Price\r\n"
            			+ "ELSE f.Price + l4.Price\r\n"
            			+ "END AS TotalPrice\r\n"
            			+ "FROM (SELECT l4.ID, l4.Price\r\n"
            			+ "FROM CHAIR as l4\r\n"
            			+ "WHERE l4.Cushion = 'Y' and l4.Type = ?) AS l4\r\n"
            			+ "CROSS JOIN\r\n"
            			+ "(SELECT c0, c1, l3.ID AS c2, CASE\r\n"
            			+ "WHEN l3.ID = c0 OR l3.ID = c1 THEN l.Price\r\n"
            			+ "ELSE l.Price + l3.Price\r\n"
            			+ "END AS Price\r\n"
            			+ "FROM (SELECT l3.ID, l3.Price\r\n"
            			+ "FROM CHAIR as l3\r\n"
            			+ "WHERE l3.Seat = 'Y' and l3.Type = ?) AS l3\r\n"
            			+ "CROSS JOIN\r\n"
            			+ "(SELECT l1.ID AS c0, l2.ID AS c1, CASE\r\n"
            			+ "WHEN l1.ID = l2.ID THEN l1.Price\r\n"
            			+ "ELSE l1.Price + l2.Price\r\n"
            			+ "END AS Price\r\n"
            			+ "FROM (SELECT l1.ID, l1.Price\r\n"
            			+ "FROM CHAIR as l1\r\n"
            			+ "WHERE l1.Legs = 'Y' and l1.Type = ?) AS l1\r\n"
            			+ "CROSS JOIN (SELECT l2.ID, l2.Price\r\n"
            			+ "FROM CHAIR as l2\r\n"
            			+ "WHERE l2.Arms = 'Y' and l2.Type = ?) AS l2) AS l) AS f\r\n"
            			+ "ORDER BY TotalPrice ASC;";
                numberOfParts = 4;
            }
            PreparedStatement query = this.inventory.initializeConnection().prepareStatement(createTable);
            for(int i = 0; i < numberOfParts; i++) {
            	query.setString(i + 1, this.furnitureType);
            }

            query.executeUpdate();
            
            String[] currentPart = new String[numberOfParts];

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
            dropTableQuery = this.inventory.initializeConnection().createStatement();
        	dropTableQuery.executeUpdate("DROP TABLE IF EXISTS T");
     
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
	
	public void orderFilled(HashSet<String> completed) throws SQLException {
		String sum = ""; //total price variable
		try {
	    	List<String> components = new ArrayList<String>(completed);
	    	//delete query to delete furniture taken
	    	String delete = "DELETE FROM " + furnitureCategory + " WHERE ID = '" + components.get(0) + "'";
	    	//summing query to get total price of all furniture taken
	    	String totalPrice = "SELECT SUM(Price) FROM " + furnitureCategory + " WHERE ID = '" + components.get(0) + "'";
	    	//iterate over unique ids
	    	for(int i = 1; i < components.size(); i++) {
				delete += " OR ID = '" + components.get(i) + "'";
				totalPrice += " OR ID = '" + components.get(i) + "'";
			}
	    	//get total price
	    	Statement priceQuery = this.inventory.initializeConnection().createStatement();
	    	ResultSet price = priceQuery.executeQuery(totalPrice);
	    	while(price.next()) {
	    		sum = price.getString("SUM(Price)");
	    	}
	    	
	    	//delete furniture taken
	    	Statement deleteQuery = this.inventory.initializeConnection().createStatement();
	    	deleteQuery.executeUpdate(delete);
	    	
	    	System.out.println(totalPrice);
	    	System.out.println(sum);
	    	System.out.println(delete);
		} catch (SQLException e) {
            e.printStackTrace();
        }	
	}

	public void printOrder() {

	}

	public void getRequest() throws SQLException {
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
		getOrder();
	}

	public static void main(String[] args) throws SQLException {
		OrderForm orderForm = new OrderForm();
		orderForm.getRequest();
	}
}
