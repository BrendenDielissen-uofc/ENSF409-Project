package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class InventoryTest {

    public InventoryTest() {

    }

    public final static String DIR = "order";
    public final static String FILE = "orderform.txt";

    /**
     * Pre- and Post-test processes
     */
    @Before
    // Delete all data from database, reload fresh database
    public void start() {
        dropDb();
        createDb();
    }

    @After
    public void end() {
        dropDb();
        createDb();
    }

    /*
     * Utility methods to perform common routines
     */

    //
    public void dropDb() {
        Inventory testJDBC = new Inventory("jdbc:mysql://localhost/INVENTORY", "scm", "ensf409");
        Connection testDbConnect = null;
        ResultSet results = null;

        try {
            testDbConnect = testJDBC.initializeConnection();
            Statement dropDbQuery = testDbConnect.createStatement();
            results = dropDbQuery.executeQuery("DROP DATABASE INVENTORY IF EXISTS");

            results.close();
            dropDbQuery.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDb() {

    }

    // Add a directory path to a file
    public String addPath(String file) {
        File path = new File(DIR);
        File full = new File(path, file);
        return full.getPath();
    }

    public void writeFile(String[] data) throws Exception {
        BufferedWriter file = null;
        File directory = new File(DIR);

        // Create directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fn = addPath(FILE);
        file = new BufferedWriter(new FileWriter(fn));

        for (String txt : data) {
            file.write(txt, 0, txt.length());
            file.newLine();
        }
        file.close();
    }

    public void removeAllData(String file) {
        // Get current working directory and append the provided
        // file/dir
        String absolute_path = System.getProperty("user.dir");
        File absolute = new File(absolute_path);
        File path = new File(absolute, file);
        removeAllData(path);
    }

    public void removeAllData(File path) {
        // If there are files in the directory, delete them first
        if (path.isDirectory()) {
            // Get all files in the directory
            File[] files = path.listFiles();

            // Recursively delete all files/subdirs
            if (files != null) {
                for (File file : files) {
                    removeAllData(file);
                }
            }
        }
        path.delete();
    }

    @Test(expected = SQLException.class)
    public void testDbConnection() throws SQLException {
        Inventory test = new Inventory("jdbc:mysql://localhost/INVENTORY", "Marasco", "ensf409");
        test.initializeConnection();
    }

    @Test
    public void testDbCloseConnection() {

    }

    // Testing User input
    @Test
    public void testGetRequest_AllValidUserInputs_ReturnsUserInput() {
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Desk";
        testOrder.quantity = 1;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRequest_InvalidFurnitureCategory_ExceptionThrown() {
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Bed";
        testOrder.furnitureType = "Standing";
        testOrder.quantity = 2;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRequest_InvalidFurnitureType_ExceptionThrown() {
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Standing";
        testOrder.quantity = 2;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRequest_InvalidFurnitureQuantity_ExceptionThrown() {
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Desk";
        testOrder.quantity = -1;
    }

    @Test
    public void testChairConstructor() {

    }

    @Test
    public void testDeskConstructor() {

    }

    @Test
    public void testFilingConstructor() {

    }

    @Test
    public void testLampConstructor() {

    }

    @Test
    public void testManufacturerConstructor() {

    }

    @Test
    public void testFileOutput() throws Exception {
        String[] data = new String[] { "mesh", "chair" };
        writeFile(data);
    }
}
