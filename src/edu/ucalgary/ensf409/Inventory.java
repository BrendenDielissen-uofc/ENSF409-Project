package edu.ucalgary.ensf409;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The type Inventory.
 */
public class Inventory {
    private Connection dbConnect;
    private ResultSet results;
    private String DBURL = "";
    private String USERNAME = "";
    private String PASSWORD = "";

    /**
     * Main used for inventory and debugging. Comment this out if main is somewhere
     * else
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     */
    public static void main(String[] args) throws SQLException {
        Inventory myJDBC = new Inventory("jdbc:mysql://localhost/INVENTORY", "scm", "ensf409");
        myJDBC.initializeConnection();
        Furniture[] testLamps = myJDBC.getAllFurniture("Desk", "Lamp");
        // var testLampMap = testLamps[0].getComponents();
        // var testLampArrayMap =
        // Arrays.stream(testLamps).map(Furniture::getComponents).toArray();
        // System.out.println(Arrays.toString(testLamps));
        // Furniture[] testDesks = myJDBC.getAllFurniture("Standing", "Desk");
        // var testDeskMap = testDesks[0].getComponents();
        // var testDeskArrayMap =
        // Arrays.stream(testDesks).map(Furniture::getComponents).toArray();
        // System.out.println(Arrays.toString(testDesks));
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
    public Connection initializeConnection() throws SQLException {
        try {
            this.dbConnect = DriverManager.getConnection(getDBURL(), getUSERNAME(), getPASSWORD());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return this.dbConnect;
    }

    /**
     * Get counting map hash map.
     *
     * @param furniture the furniture
     * @return the hash map
     */
    public HashMap<String, Integer> getCountingMap(String furniture) {
        HashMap<String, Integer> countingMap = null;
        if (furniture.toLowerCase().equals("lamp")) {
            countingMap = new Lamp().getCountingMap();
        } else if (furniture.toLowerCase().equals("desk")) {
            countingMap = new Desk().getCountingMap();
        } else if (furniture.toLowerCase().equals("chair")) {
            countingMap = new Chair().getCountingMap();
        } else if (furniture.toLowerCase().equals("filing")) {
            countingMap = new Filing().getCountingMap();
        }
        return countingMap;
    }

    /**
     * Proto-class that grabs furniture and its furniture type and returns a
     * 2D-array of wanted info
     *
     * @param furnitureType String type of the specified furniture wanted
     * @param furniture     String furniture
     * @return Furniture[] Array
     */
    public Furniture[] getAllFurniture(String furnitureType, String furniture) {
        Furniture[] tempFurniture = null;
        if (furniture.toLowerCase().equals("lamp")) {
            tempFurniture = this._getAllLamps(furnitureType);
        } else if (furniture.toLowerCase().equals("desk")) {
            tempFurniture = this._getAllDesks(furnitureType);
        } else if (furniture.toLowerCase().equals("chair")) {
            tempFurniture = this._getAllChairs(furnitureType);
        } else if (furniture.toLowerCase().equals("filing")) {
            tempFurniture = this._getAllFilings(furnitureType);
        }
        return tempFurniture;
    }

    private Lamp[] _getAllLamps(String furnitureType) {
        ArrayList<Lamp> myLamps = new ArrayList<Lamp>();
        try {
            String query = Lamp.getQueryString() + " WHERE Type = ?;";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, furnitureType);
            System.out.println(myStmt);
            results = myStmt.executeQuery();
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

    private Desk[] _getAllDesks(String furnitureType) {
        ArrayList<Desk> myLamps = new ArrayList<Desk>();
        try {
            String query = Desk.getQueryString() + " WHERE Type = ?;";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, furnitureType);
            System.out.println(myStmt);
            results = myStmt.executeQuery();
            while (results.next()) {
                myLamps.add(new Desk(results));
            }

            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Desk[] array = new Desk[myLamps.size()];
        myLamps.toArray(array);
        return array;
    }

    private Chair[] _getAllChairs(String furnitureType) {
        ArrayList<Chair> myLamps = new ArrayList<Chair>();
        try {
            String query = Chair.getQueryString() + " WHERE Type = ?;";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, furnitureType);
            System.out.println(myStmt);
            results = myStmt.executeQuery();
            while (results.next()) {
                myLamps.add(new Chair(results));
            }

            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Chair[] array = new Chair[myLamps.size()];
        myLamps.toArray(array);
        return array;
    }

    private Filing[] _getAllFilings(String furnitureType) {
        ArrayList<Filing> myLamps = new ArrayList<Filing>();
        try {
            String query = Filing.getQueryString() + " WHERE Type = ?;";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, furnitureType);
            System.out.println(myStmt);
            results = myStmt.executeQuery();
            while (results.next()) {
                myLamps.add(new Filing(results));
            }

            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Filing[] array = new Filing[myLamps.size()];
        myLamps.toArray(array);
        return array;
    }
}
