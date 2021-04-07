package edu.ucalgary.ensf409;

import java.sql.*;

public class Inventory {
	
	private Connection dbConnect;

    private String DBURL = "";
    private String USERNAME = "";
    private String PASSWORD = "";
   
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
    
    /**
     * Connects java program to the SQL database
     * @throws SQLException  if SQL related error is encountered
     */
    public Connection initializeConnection() throws SQLException {
        try {
            this.dbConnect = DriverManager.getConnection(getDBURL(), getUSERNAME(), getPASSWORD());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return this.dbConnect;
    }
    
    /**
     * Closes the connection from the java program to the SQL database
     */
    public void closeConnection() {
    	try {
    		this.dbConnect.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
}
