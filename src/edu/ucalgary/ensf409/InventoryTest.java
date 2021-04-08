package edu.ucalgary.ensf409;

import org.junit.*;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InventoryTest {
    public final static String DIR = "order";
    public final static String FILE = "orderform.txt";
    private Inventory testJDBC = new Inventory("jdbc:mysql://localhost/INVENTORY", "scm", "ensf409");
    private Connection testDbConnect = null;
    private ResultSet results = null;

    /**
     * Pre- and Post-test processes
     */
    @Before
    public void start() {
        // dropDb();
        // createDb();
    }

    @After
    public void end() {
        // dropDb();
        // createDb();
    }

    /*
     * Utility methods to perform common routines
     */

    public void dropDb() {
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

    // Testing User input
    @Test
    public void testGetRequest_AllValidUserInputs_ReturnsUserInput() {
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
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
    public void testGetRequest_InvalidFurnitureQuantity_ExceptionThrown() {
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Desk";
        testOrder.quantity = -1;

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRequest_InvalidFurnitureType_ExceptionThrown() {
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Standing";
        testOrder.quantity = 2;
    }

    // Testing desk lamp orders
    @Test
    public void testGetOrder_1LampDesk_OutputOrderForm() {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Desk";
        testOrder.quantity = 1;

        // Check if ID is removed from lamp table
        boolean removed = false;
        try {
            testOrder.getOrder();
            testDbConnect = testJDBC.initializeConnection();
            Statement resultsQuery = testDbConnect.createStatement();
            results = resultsQuery.executeQuery("SELECT * FROM LAMP WHERE ID='L564'");

            if (!results.isBeforeFirst()) {
                removed = true;
            }

            results.close();
            resultsQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue("L564 was removed from inventory", removed);
    }

    @Test
    public void testGetOrder_2LampDesk_OutputOrderForm() {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Desk";
        testOrder.quantity = 2;

        // Check if ID is removed from lamp table
        boolean removed = false;
        try {
            testOrder.getOrder();
            testDbConnect = testJDBC.initializeConnection();
            Statement resultsQuery = testDbConnect.createStatement();
            results = resultsQuery.executeQuery("SELECT * FROM LAMP WHERE ID='L013' OR ID='L342'");

            if (!results.isBeforeFirst()) {
                removed = true;
            }

            results.close();
            resultsQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue("L013 and L342 were removed from inventory", removed);
    }

    @Test
    // Check for manufacturer list output
    public void testGetOrder_ManyDeskLamps_OutputOrderForm() {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Desk";
        testOrder.quantity = 4;
    }

    // Testing study lamp orders
    @Test
    public void testGetOrder_1LampStudy_OutputOrderForm() {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Study";
        testOrder.quantity = 1;

        // Check if ID is removed from lamp table
        boolean removed = false;
        try {
            testOrder.getOrder();
            testDbConnect = testJDBC.initializeConnection();
            Statement resultsQuery = testDbConnect.createStatement();
            results = resultsQuery.executeQuery("SELECT * FROM LAMP WHERE ID='L928'");

            if (!results.isBeforeFirst()) {
                removed = true;
            }

            results.close();
            resultsQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue("L928 was removed from inventory", removed);
    }

    @Test
    public void testGetOrder_2LampStudy_OutputOrderForm() {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Study";
        testOrder.quantity = 2;

        // Check if ID is removed from lamp table
        boolean removed = false;
        try {
            testOrder.getOrder();
            testDbConnect = testJDBC.initializeConnection();
            Statement resultsQuery = testDbConnect.createStatement();
            results = resultsQuery.executeQuery("SELECT * FROM LAMP WHERE ID='L982' OR ID='L980'");

            if (!results.isBeforeFirst()) {
                removed = true;
            }

            results.close();
            resultsQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue("L928 and L980 was removed from inventory", removed);
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyStudyLamps_OutputOrderForm() {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Study";
        testOrder.quantity = 4;
    }

}
