package edu.ucalgary.ensf409;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class Inventory {
	
	private Connection dbConnect;
	private ResultSet results;

    private String DBURL = "";
    private String USERNAME = "";
    private String PASSWORD = "";
    
    public static void main(String[] args) throws SQLException {
    	
    	Inventory myJDBC = new Inventory("jdbc:mysql://localhost/inventory","Marasco","ensf409");
    	myJDBC.initializeConnection();
    	Lamp[] testLamp = myJDBC.getAllLamp("Desk", "lamp");
    	for (int i = 0; i < testLamp.length; i++) {
    		System.out.println(testLamp[i].getId() + " " + Boolean.toString(testLamp[i].isBase()) 
    				+ " " + Boolean.toString(testLamp[i].isBulb()));
    	}
    	

    	
    	
    	

    	
    }
   
    /**
     * Constructor for Inventory Class
     * @param DBURL
     * @param USERNAME
     * @param PASSWORD
     */
    public Inventory(String DBURL, String USERNAME, String PASSWORD) {
    	this.DBURL = DBURL;
    	this.USERNAME = USERNAME;
    	this.PASSWORD = PASSWORD;
    }
    
    /**
     * Getter method for DBURL
     * @return String DBURL
     */
    public String getDBURL() {
    	return this.DBURL;
    }
    
    /**
     * Getter method for username
     * @return String USERNAME
     */
    public String getUSERNAME() {
    	return this.USERNAME;
    }
    
    /**
     * Getter method for password
     * @return String PASSWORD
     */
    public String getPASSWORD() {
    	return this.PASSWORD;
    	
    }
    
    public void initializeConnection() throws SQLException {
    	try {
    		this.dbConnect = DriverManager.getConnection(getDBURL(), getUSERNAME(), getPASSWORD());
    	} catch (SQLException ex) {
    		ex.printStackTrace();
    	}
    }
    
    public Lamp[] getAllLamp(String furnitureType, String furniture) {
    	ArrayList<Lamp> myLamps = new ArrayList<Lamp>();
    	StringBuffer ret = new StringBuffer();
    	try {
    		String query = "SELECT * FROM " + furniture + " WHERE Type = ?;";
    		PreparedStatement myStmt = dbConnect.prepareStatement(query);
    		
    		myStmt.setString(1, furnitureType);
    		System.out.println(myStmt);
        	while (results.next()) {
        		myLamps.add(new Lamp(results));

        	}
        
        	myStmt.close();


    	} catch (SQLException ex) {
    		ex.printStackTrace();
    	}
    	
    	Lamp[] array = new Lamp[myLamps.size()];
    	myLamps.toArray(array);
		return array;   	
     	
    }
    
    
   


}
