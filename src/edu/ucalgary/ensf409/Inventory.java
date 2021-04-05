package edu.ucalgary.ensf409;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

import static java.util.Map.entry;


/**
 * The type Inventory.
 */
public class Inventory {

    /**
     * The Furniture result set ctor map.
     */
    public static HashMap<String, Constructor> furnitureResultSetCtorMap;
    /**
     * The Furniture default ctor map.
     */
    public static HashMap<String, Constructor> furnitureDefaultCtorMap;
	private Connection dbConnect;
	private ResultSet results;
    private String DBURL = "";
    private String USERNAME = "";
    private String PASSWORD = "";

    /**
     * Main used for inventory and debugging. COmment this out if main is somewhere else
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     */
    public static void main(String[] args) throws SQLException {
        Inventory myJDBC = new Inventory("jdbc:mysql://localhost/inventory", "scm", "ensf409");
        myJDBC.initializeConnection();
        Furniture[] testLamps = myJDBC.getAllFurniture("Desk", "Lamp");
        var testLampMap = testLamps[0].getComponents();
        var testLampArrayMap = Arrays.stream(testLamps).map(Furniture::getComponents).toArray();
        System.out.println(Arrays.toString(testLamps));
        Furniture[] testDesks = myJDBC.getAllFurniture("Standing", "Desk");
        var testDeskMap = testDesks[0].getComponents();
        var testDeskArrayMap = Arrays.stream(testDesks).map(Furniture::getComponents).toArray();
        System.out.println(Arrays.toString(testDesks));
    }

    /**
     * Constructor for Inventory Class
     *
     * @param DBURL    the dburl
     * @param USERNAME the username
     * @param PASSWORD the password
     */
    public Inventory(String DBURL, String USERNAME, String PASSWORD) {
    	this.DBURL = DBURL;
    	this.USERNAME = USERNAME;
    	this.PASSWORD = PASSWORD;
        try {
            furnitureResultSetCtorMap = new HashMap<>(Map.ofEntries(
                    entry("desk", Desk.class.getConstructor(ResultSet.class)),
                    entry("filing", Filing.class.getConstructor(ResultSet.class)),
                    entry("lamp", Lamp.class.getConstructor(ResultSet.class)),
                    entry("chair", Chair.class.getConstructor(ResultSet.class))
            ));
            furnitureDefaultCtorMap = new HashMap<>(Map.ofEntries(
                    entry("desk", Desk.class.getConstructor()),
                    entry("filing", Filing.class.getConstructor()),
                    entry("lamp", Lamp.class.getConstructor()),
                    entry("chair", Chair.class.getConstructor())
            ));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            furnitureResultSetCtorMap = null;
            furnitureDefaultCtorMap = null;
        }
    }

    /**
     * Getter method for DBURL
     *
     * @return String DBURL
     */
    public String getDBURL() {
    	return this.DBURL;
    }

    /**
     * Getter method for username
     *
     * @return String USERNAME
     */
    public String getUSERNAME() {
    	return this.USERNAME;
    }

    /**
     * Getter method for password
     *
     * @return String PASSWORD
     */
    public String getPASSWORD() {
    	return this.PASSWORD;
    	
    }

    /**
     * Connects java program to the SQL database
     *
     * @throws SQLException if SQL related error is encountered
     */
    public void initializeConnection() throws SQLException {
    	try {
    		this.dbConnect = DriverManager.getConnection(getDBURL(), getUSERNAME(), getPASSWORD());
    	} catch (SQLException ex) {
    		ex.printStackTrace();
    	}
    }

    /**
     * Deletes listed furniture
     *
     * @param furniture the furniture
     */
    public void deleteFurniture(Furniture[] furniture) {
    	// iterates through each furniture object and deletes in from db
        for (Furniture value : furniture) {
            try {
                String furnitureCat = value.getClass().getSimpleName().toUpperCase();
                String query = "DELETE FROM " + furnitureCat + " where ID = ?";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setString(1, value.getId());

                int rowCount = myStmt.executeUpdate();
                System.out.println("Deleting Furniture. Rows affected: " + rowCount);

                myStmt.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Close database connections after everything is done
     */
    public void closeConnection() {
    	try {
    		this.results.close();
    		this.dbConnect.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Get counting map hash map.
     *
     * @param furnitureCategory the furniture category
     * @return the hash map
     */
    public HashMap<String, Integer> getFurnitureCountingMap(String furnitureCategory){
        HashMap<String, Integer> temp = null;
        try {
            Furniture furniture = (Furniture) Inventory.furnitureDefaultCtorMap.get(furnitureCategory).newInstance();
            temp = furniture.getCountingMap();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Proto-class that grabs furniture and its furniture type and returns a 2D-array of wanted info
     *
     * @param furnitureType String type of the specified furniture wanted
     * @param furniture     String furniture
     * @return Furniture[] Array
     */
    public Furniture[] getAllFurniture(String furnitureType, String furniture) {
        Furniture tempFurniture = null;
        try {
            tempFurniture = (Furniture) Inventory.furnitureDefaultCtorMap.get(furniture).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        ArrayList<Furniture> myFurniture = new ArrayList<Furniture>();
        try {
            assert tempFurniture != null;
            String query = tempFurniture.getQueryString() + " WHERE Type = ?;";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, furnitureType);
            System.out.println(myStmt);
            results = myStmt.executeQuery();
            while (results.next()) {
                Furniture furnitureItem = (Furniture) Inventory.furnitureResultSetCtorMap.get(furniture).newInstance(results);
                myFurniture.add(furnitureItem);
            }

            myStmt.close();
        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }

        Furniture[] array = new Furniture[myFurniture.size()];
        myFurniture.toArray(array);
        return array;
    }
}
