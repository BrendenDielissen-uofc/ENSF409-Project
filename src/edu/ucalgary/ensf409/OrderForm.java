package edu.ucalgary.ensf409;

import java.sql.SQLException;
import java.util.Scanner;

public class OrderForm {
	public String[] furnitureID = new String[10];
	public String furnitureCategory;
	public String furnitureType;
	public int quantity;

	public void calculateOrder(int col) {
	}

	public void getOrder() throws SQLException {
		Inventory myJDBC = new Inventory(
				"jdbc:mysql://localhost:3306/inventory", "root", "Bfluff3!");
		myJDBC.initializeConnection();

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
