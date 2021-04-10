/**
 * This class represents all currently available furniture inventory and handles connections to inventory.sql database
 *
 * @author Brenden Dielissen
 * @author Maria Martine Baclig
 * @author Nafisa Tabassum
 * @author Ronn Delos Reyes
 * @version 1.0
 */
package edu.ucalgary.ensf409;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * The type Inventory.
 */
public class Inventory {

    /**
     * Maps furniture category string to the corresponding furniture categories furniture types.
     */
    public static HashMap<String, List<String>> furnitureTypesMap = new HashMap<>(
            Map.ofEntries(entry("DESK", Desk.TYPES), entry("FILING", Filing.TYPES), entry("LAMP", Lamp.LAMP_TYPES),
                    entry("CHAIR", Chair.TYPES)));
    /**
     * Maps furniture category string to the corresponding furniture categories Manufacturers.
     */
    public static HashMap<String, List<Manufacturer>> furnitureManufacturersMap = new HashMap<>(
            Map.ofEntries(entry("DESK", Desk.MANUFACTURERS), entry("FILING", Filing.MANUFACTURERS),
                    entry("LAMP", Lamp.MANUFACTURERS), entry("CHAIR", Chair.MANUFACTURERS)));
    /**
     * Maps furniture category string to the corresponding furniture categories ResultSet constructor.
     */
    public HashMap<String, Constructor> furnitureResultSetCtorMap;
    /**
     * Maps furniture category string to the corresponding furniture categories default constructor.
     */
    public HashMap<String, Constructor> furnitureDefaultCtorMap;
    private Connection dbConnect;
    private ResultSet results;
    private final String DBURL;
    private final String USERNAME;
    private final String PASSWORD;

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
            furnitureResultSetCtorMap = new HashMap<>(
                    Map.ofEntries(entry("DESK", Desk.class.getConstructor(ResultSet.class)),
                            entry("FILING", Filing.class.getConstructor(ResultSet.class)),
                            entry("LAMP", Lamp.class.getConstructor(ResultSet.class)),
                            entry("CHAIR", Chair.class.getConstructor(ResultSet.class))));
            furnitureDefaultCtorMap = new HashMap<>(Map.ofEntries(entry("DESK", Desk.class.getConstructor()),
                    entry("FILING", Filing.class.getConstructor()), entry("LAMP", Lamp.class.getConstructor()),
                    entry("CHAIR", Chair.class.getConstructor())));
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
     * Connects java program to the SQL database.
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
     * Deletes the furniture items corresponding to the input Furniture array.
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
                System.out.println("Deleting " + furnitureCat + ". Rows affected: " + rowCount);

                myStmt.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Closes database connection.
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
     * Get counting map corresponding to furniture category string.
     *
     * @param furnitureCategory the furniture category
     * @return the hash map
     */
    public HashMap<String, Integer> getFurnitureCountingMap(String furnitureCategory) {
        HashMap<String, Integer> temp = null;
        try {
            Furniture furniture = (Furniture) this.furnitureDefaultCtorMap.get(furnitureCategory.toUpperCase())
                    .newInstance();
            temp = furniture.getCountingMap();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Gets all furniture from database matching the input furniture category and type.
     *
     * @param furnitureType     String type of the specified furniture wanted
     * @param furnitureCategory String furniture
     * @return Furniture[] Array
     */
    public Furniture[] getAllFurniture(String furnitureType, String furnitureCategory) {
        Furniture tempFurniture = null;
        try {
            tempFurniture = (Furniture) this.furnitureDefaultCtorMap.get(furnitureCategory).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        ArrayList<Furniture> myFurniture = new ArrayList<Furniture>();
        try {
            assert tempFurniture != null;
            String query = tempFurniture.getQueryString() + " WHERE Type = ?;";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, furnitureType);
            results = myStmt.executeQuery();
            while (results.next()) {
                Furniture furnitureItem = (Furniture) this.furnitureResultSetCtorMap.get(furnitureCategory)
                        .newInstance(results);
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
