package edu.ucalgary.ensf409;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class Inventory {
    /**
     * The constant furnitureTypesMap.
     */
    public static HashMap<String, List<String>> furnitureTypesMap = new HashMap<>(
            Map.ofEntries(entry("DESK", Desk.TYPES), entry("FILING", Filing.TYPES), entry("LAMP", Lamp.TYPES),
                    entry("CHAIR", Chair.TYPES)));
    /**
     * The constant furnitureManufacturersMap.
     */
    public static HashMap<String, List<Manufacturer>> furnitureManufacturersMap = new HashMap<>(
            Map.ofEntries(entry("DESK", Desk.MANUFACTURERS), entry("FILING", Filing.MANUFACTURERS),
                    entry("LAMP", Lamp.MANUFACTURERS), entry("CHAIR", Chair.MANUFACTURERS)));
    /**
     * The Furniture result set ctor map.
     */
    public HashMap<String, Constructor> furnitureResultSetCtorMap;
    /**
     * The Furniture default ctor map.
     */
    public HashMap<String, Constructor> furnitureDefaultCtorMap;
    private Connection dbConnect;
    private ResultSet results;
    private String DBURL = "";
    private String USERNAME = "";
    private String PASSWORD = "";

    /**
     * Main used for inventory and debugging. COmment this out if main is somewhere
     * else
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
     * @param DBURL
     * @param USERNAME
     * @param PASSWORD
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
        try {
            for (Furniture value : furniture) {
                String furnitureCat = value.getClass().getSimpleName().toUpperCase();
                String query = "DELETE FROM " + furnitureCat + " where ID = ?";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setString(1, value.getId());

                int rowCount = myStmt.executeUpdate();
                // System.out.println("Deleting Furniture. Rows affected: " + rowCount);

                myStmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.closeConnection();
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
     * Gets corresponding furniture's hash map that can be used to count the amount
     * of components per order.
     *
     * @param furnitureCategory the furniture category
     * @return the hash map
     */
    public HashMap<String, Integer> getFurnitureCountingMap(String furnitureCategory) {
        HashMap<String, Integer> temp = null;
        try {
            Furniture furniture = (Furniture) this.furnitureDefaultCtorMap.get(furnitureCategory).newInstance();
            temp = furniture.getCountingMap();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Grabs all furniture corresponding to the type and category.
     *
     * @param furnitureType     String type of the specified furniture wanted
     * @param furnitureCategory the furniture category
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
            // System.out.println(myStmt);
            results = myStmt.executeQuery();
            while (results.next()) {
                Furniture furnitureItem = (Furniture) this.furnitureResultSetCtorMap.get(furnitureCategory)
                        .newInstance(results);
                myFurniture.add(furnitureItem);
            }

            myStmt.close();
        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
            this.closeConnection();
        }

        Furniture[] array = new Furniture[myFurniture.size()];
        myFurniture.toArray(array);
        return array;
    }

    /**
     * Utilizes SQL queries to get the cheapest order available for the desired
     * furniture type and category. Returns null if order cannot be completed.
     *
     * @param furnitureType     the furniture type
     * @param furnitureCategory the furniture category
     * @param quantity          the quantity
     * @return the furniture [ ]
     */
    public Furniture[] getCheapestOrder(String furnitureType, String furnitureCategory, int quantity) {
        HashSet<String> completed = new HashSet<String>();
        boolean filled = true;

        try {
            Statement dropTableQuery = this.dbConnect.createStatement();
            dropTableQuery.executeUpdate("DROP TABLE IF EXISTS T");
            dropTableQuery.executeUpdate("DROP TABLE IF EXISTS C");

            Furniture furniture = (Furniture) this.furnitureDefaultCtorMap.get(furnitureCategory).newInstance();
            HashMap<String, String> combinationsQueryMap = furniture.getAllCombinationsQueryMap();

            String createTable = combinationsQueryMap.get("createTable");
            // System.out.println(createTable);
            String numberOfPartsTable = combinationsQueryMap.get("numberOfPartsTable");
            String getOrder = combinationsQueryMap.get("getOrder");
            int numberOfParts = furniture.getComponents().size();

            Statement tableQuery = this.dbConnect.createStatement();
            tableQuery.executeUpdate(numberOfPartsTable);

            PreparedStatement query = this.dbConnect.prepareStatement(createTable);
            for (int i = 0; i < numberOfParts; i++)
                query.setString(i + 1, furnitureType);
            query.executeUpdate();

            String[] currentPart = new String[numberOfParts];

            for (int counter = 0; counter < quantity; counter++) {
                // get one set
                Statement resultsQuery = this.dbConnect.createStatement();
                ResultSet results = resultsQuery.executeQuery(getOrder);
                // no set returned, cannot fill order
                if (!results.isBeforeFirst()) {
                    filled = false;
                    break;
                }
                // one set found
                while (results.next()) {
                    // place in completed and currentPart
                    for (int i = 0; i < numberOfParts; i++) {
                        completed.add(results.getString("c" + i));
                        currentPart[i] = results.getString("c" + i);
                    }
                    // delete statement based on set taken
                    String delete = "DELETE FROM T WHERE c0 = '" + currentPart[0] + "'";
                    for (int i = 1; i < numberOfParts; i++) {
                        delete += " OR c" + i + " = '" + currentPart[i] + "'";
                    }
                    // delete from temporary table row components
                    Statement deleteQuery = this.dbConnect.createStatement();
                    deleteQuery.executeUpdate(delete);
                }
            }
            dropTableQuery.executeUpdate("DROP TABLE IF EXISTS T");
            dropTableQuery.executeUpdate("DROP TABLE IF EXISTS C");

        } catch (SQLException | IllegalAccessException | InstantiationException
                | InvocationTargetException throwables) {
            throwables.printStackTrace();
            this.closeConnection();
        }
        if (filled) {
            ArrayList<Furniture> cheapestCombo = new ArrayList<Furniture>(
                    Arrays.asList(this.getAllFurniture(furnitureType, furnitureCategory)));
            cheapestCombo = (ArrayList<Furniture>) cheapestCombo.stream()
                    .filter(item -> completed.contains(item.getId())).collect(Collectors.toList());
            Furniture[] array = new Furniture[cheapestCombo.size()];
            cheapestCombo.toArray(array);
            return array;
        } else
            return null;
    }
}
