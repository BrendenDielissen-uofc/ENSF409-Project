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
        // Placing two desk lamp orders
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
    public void testGetOrder_ManyLampDesk_OutputOrderForm() {
        // Placing multiple desk lamp orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Desk";
        testOrder.quantity = 4;
    }

    // Testing study lamp orders
    @Test
    public void testGetOrder_1LampStudy_OutputOrderForm() {
        // Placing one study lamp order
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
        // Placing two study lamp orders
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
    public void testGetOrder_ManyLampStudy_OutputOrderForm() {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Study";
        testOrder.quantity = 4;
    }

    // Testing swing arm lamp orders
    @Test
    public void testGetOrder_1LampSwingArm_OutputOrderForm() {
        // Placing one swing arm lamp order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Swing Arm";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2LampSwingArm_OutputOrderForm() {
        // Placing two swing arm lamp orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Swing Arm";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyLampSwingArm_OutputOrderForm() {
        // Placing many swing arm lamp orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Lamp";
        testOrder.furnitureType = "Swing Arm";
        testOrder.quantity = 4;
    }

    // Testing small filing cabinet orders
    @Test
    public void testGetOrder_1FilingSmall_OutputOrderForm() {
        // Placing one small filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Small";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2FilingSmall_OutputOrderForm() {
        // Place two small filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Small";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyFilingSmall_OutputOrderForm() {
        // Place many small filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Small";
        testOrder.quantity = 4;
    }

    // Testing medium filing cabinet orders
    @Test
    public void testGetOrder_1FilingMedium_OutputOrderForm() {
        // Placing one medium filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Medium";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2FilingMedium_OutputOrderForm() {
        // Placing two medium filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Medium";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyFilingMedium_OutputOrderForm() {
        // Placing many medium filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Medium";
        testOrder.quantity = 4;
    }

    // Testing large filing cabinet orders
    @Test
    public void testGetOrder_1FilingLarge_OutputOrderForm() {
        // Placing one large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Large";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2FilingLarge_OutputOrderForm() {
        // Placing two large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Large";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyFilingLarge_OutputOrderForm() {
        // Placing many large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Filing";
        testOrder.furnitureType = "Large";
        testOrder.quantity = 4;
    }

    // Testing adjustable desk orders
    @Test
    public void testGetOrder_1DeskAdjustable_OutputOrderForm() {
        // Placing one adjustable desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Adjustable";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2DeskAdjustable_OutputOrderForm() {
        // Placing two adjustable desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Adjustable";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyDeskAdjustable_OutputOrderForm() {
        // Placing many adjustable desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Adjustable";
        testOrder.quantity = 4;
    }

    // Testing standing desk orders
    @Test
    public void testGetOrder_1DeskStanding_OutputOrderForm() {
        // Placing one standing desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Standing";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2DeskStanding_OutputOrderForm() {
        // Placing two standing desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Standing";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyDeskStanding_OutputOrderForm() {
        // Placing many standing desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Standing";
        testOrder.quantity = 4;
    }

    // Testing traditional desk orders
    @Test
    public void testGetOrder_1DeskTraditional_OutputOrderForm() {
        // Placing one traditional desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Traditional";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2DeskTraditional_OutputOrderForm() {
        // Placing two traditional desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Traditional";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyDeskTraditional_OutputOrderForm() {
        // Placing many traditional desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Desk";
        testOrder.furnitureType = "Traditional";
        testOrder.quantity = 4;

    }

    // Testing ergonomic chair orders
    @Test
    public void testGetOrder_1ChairErgonomic_OutputOrderForm() {
        // Placing one ergonomic chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Ergonomic";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2ChairErgonomic_OutputOrderForm() {
        // Placing two ergonomic chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Ergonomic";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairErgonomic_OutputOrderForm() {
        // Placing many ergonomic chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Ergonomic";
        testOrder.quantity = 4;
    }

    // Testing executive chair orders
    @Test
    public void testGetOrder_1ChairExecutive_OutputOrderForm() {
        // Placing one executive chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Executive";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2ChairExecutive_OutputOrderForm() {
        // Placing two executive chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Executive";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairExecutive_OutputOrderForm() {
        // Placing many executive chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Executive";
        testOrder.quantity = 4;
    }

    // Testing kneeling chair orders
    @Test
    public void testGetOrder_1ChairKneeling_OutputOrderForm() {
        // Placing one kneeling chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Kneeling";
        testOrder.quantity = 1;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_2ChairKneeling_OutputOrderForm() {
        // Placing two kneeling chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Kneeling";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairKneeling_OutputOrderForm() {
        // Placing many kneeling chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Kneeling";
        testOrder.quantity = 4;
    }

    // Testing mesh chair orders
    @Test
    public void testGetOrder_1ChairMesh_OutputOrderForm() {
        // Placing one mesh chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Mesh";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2ChairMesh_OutputOrderForm() {
        // Placing two mesh chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Mesh";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairMesh_OutputOrderForm() {
        // Placing many mesh chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Mesh";
        testOrder.quantity = 4;
    }

    // Testing task chair orders
    @Test
    public void testGetOrder_1ChairTask_OutputOrderForm() {
        // Placing one task chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Task";
        testOrder.quantity = 1;
    }

    @Test
    public void testGetOrder_2ChairTask_OutputOrderForm() {
        // Placing two task chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Task";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairTask_OutputOrderForm() {
        // Placing many task chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "Chair";
        testOrder.furnitureType = "Task";
        testOrder.quantity = 4;
    }
}
