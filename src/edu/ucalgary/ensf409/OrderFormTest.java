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
public class OrderFormTest {
    public final static String FILE = "orderform.txt";
    private Inventory testJDBC = new Inventory("jdbc:mysql://localhost/INVENTORY", "scm", "ensf409");
    private Connection testDbConnect = null;
    private ResultSet results = null;

    /**
     * Pre- and Post-test processes
     */
    // @Before
    // public void start() {
    // removeAllData(FILE);
    // }

    // @After
    // public void end() {
    // removeAllData(FILE);
    // }

    /*
     * Utility methods to perform common routines
     */

    // Add a directory path to a file
    public String addPath(String file) {
        File full = new File(file);
        return full.getPath();
    }

    // public void writeFile(String[] data) throws Exception {
    // BufferedWriter file = null;
    // File directory = new File(DIR);

    // // Create directory if it doesn't exist
    // if (!directory.exists()) {
    // directory.mkdir();
    // }

    // String fn = addPath(FILE);
    // file = new BufferedWriter(new FileWriter(fn));

    // for (String txt : data) {
    // file.write(txt, 0, txt.length());
    // file.newLine();
    // }
    // file.close();
    // }

    // Read in generated file and store output in string
    public String readFile(String file) throws IOException {
        StringBuilder fileContents = new StringBuilder();
        // Ensure file exists
        File testFile = new File(file);
        if (!testFile.exists()) {
            System.err.println("File " + testFile + " could not be opened as it does not exist.");

        }
        Scanner in = null;
        try {
            in = new Scanner(new File(file));
            while (in.hasNextLine()) {
                fileContents.append(in.nextLine());
            }
        } catch (IOException e) {
            System.err.println("I/O error opening/reading file " + file + ".");
            in.close();
            e.printStackTrace();
        }
        in.close();
        return fileContents.toString();
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
        String furniture = "Lamp";
        String furnitureType = "Desk";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        int orderPrice = testOrder.calculateOrder();
        System.out.println(orderPrice);
        assertEquals("20", String.valueOf(orderPrice));
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
    public void testGetOrder_1LampDesk_OutputOrderForm() throws Exception {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Desk";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        assertTrue("fulfillOrder() does not correctly produce form for 1 Desk Lamp",
                readFile(FILE).contains("L564Total Price: $20"));
    }

    @Test
    public void testGetOrder_2LampDesk_OutputOrderForm() throws Exception {
        // Placing two desk lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Desk";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));

        assertTrue("fulfillOrder() does not correctly produce form for 2 Desk Lamps",
                readFile(FILE).contains("ID: L013ID: L342ID: L564Total Price: $40"));

    }

    @Test
    // Check for manufacturer list output
    public void testGetOrder_ManyLampDesk_OutputOrderForm() throws Exception {
        // Placing multiple desk lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Desk";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 4;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not produce form with listed manufacturers", readFile(FILE)
                .contains("Suggested manufacturers:Office Furnishings, Furniture Goods, Fine Office Supplies"));
    }

    // Testing study lamp orders
    @Test
    public void testGetOrder_1LampStudy_OutputOrderForm() throws Exception {
        // Placing one study lamp order
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Study";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 1 Study Lamp",
                readFile(FILE).contains("L928Total Price: $10"));

    }

    @Test
    public void testGetOrder_2LampStudy_OutputOrderForm() throws Exception {
        // Placing two study lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Study";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 2 Study Lamps",
                readFile(FILE).contains("Total Price: $20"));

    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyLampStudy_OutputOrderForm() throws Exception {
        // Placing many study lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Study";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 4;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not produce form with listed manufacturers", readFile(FILE)
                .contains("Suggested manufacturers:Office Furnishings, Furniture Goods, Fine Office Supplies"));
    }

    // Testing swing arm lamp orders
    @Test
    public void testGetOrder_1LampSwingArm_OutputOrderForm() throws Exception {
        // Placing one swing arm lamp order
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Swing Arm";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 1 Swing Arm Lamp",
                readFile(FILE).contains("Total Price: $30"));
    }

    @Test
    public void testGetOrder_2LampSwingArm_OutputOrderForm() throws Exception {
        // Placing two swing arm lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Swing Arm";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 2 Swing Arm Lamps",
                readFile(FILE).contains("Total Price: $60"));
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyLampSwingArm_OutputOrderForm() throws Exception {
        // Placing many swing arm lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Swing Arm";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 4;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not produce form with listed manufacturers", readFile(FILE)
                .contains("Suggested manufacturers:Office Furnishings, Furniture Goods, Fine Office Supplies"));
    }

    // Testing small filing cabinet orders
    @Test
    public void testGetOrder_1FilingSmall_OutputOrderForm() throws Exception {
        // Placing one small filing cabinet order
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Filing";
        String furnitureType = "Small";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 1 Small Filing Cabinet",
                readFile(FILE).contains("Total Price: $100"));
    }

    @Test
    public void testGetOrder_2FilingSmall_OutputOrderForm() throws Exception {
        // Place two small filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "SMALL";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 2 Small Filing Cabinets",
                readFile(FILE).contains("Total Price: $225"));
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyFilingSmall_OutputOrderForm() throws Exception {
        // Place many small filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "SMALL";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not produce form with listed manufacturers", readFile(FILE)
                .contains("Suggested manufacturers:Office Furnishings, Furniture Goods, Fine Office Supplies"));
    }

    // Testing medium filing cabinet orders
    @Test
    public void testGetOrder_1FilingMedium_OutputOrderForm() throws Exception {
        // Placing one medium filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "MEDIUM";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 1 Medium Filing Cabinet",
                readFile(FILE).contains("Total Price: $200"));
    }

    @Test
    public void testGetOrder_2FilingMedium_OutputOrderForm() throws Exception {
        // Placing two medium filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "MEDIUM";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 2 Medium Filing Cabinets",
                readFile(FILE).contains("Total Price: $400"));
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyFilingMedium_OutputOrderForm() throws Exception {
        // Placing many medium filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "MEDIUM";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not produce form with listed manufacturers", readFile(FILE)
                .contains("Suggested manufacturers:Office Furnishings, Furniture Goods, Fine Office Supplies"));
    }

    // Testing large filing cabinet orders
    @Test
    public void testGetOrder_1FilingLarge_OutputOrderForm() throws Exception {
        // Placing one large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "LARGE";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 1 Large Filing Cabinet",
                readFile(FILE).contains("Total Price: $300"));
    }

    @Test
    public void testGetOrder_2FilingLarge_OutputOrderForm() throws Exception {
        // Placing two large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "LARGE";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 2 Large Filing Cabinets",
                readFile(FILE).contains("Total Price: $600"));
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyFilingLarge_OutputOrderForm() throws Exception {
        // Placing many large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "LARGE";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not produce form with listed manufacturers", readFile(FILE)
                .contains("Suggested manufacturers:Office Furnishings, Furniture Goods, Fine Office Supplies"));
    }

    // Testing adjustable desk orders
    @Test
    public void testGetOrder_1DeskAdjustable_OutputOrderForm() throws Exception {
        // Placing one adjustable desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "ADJUSTABLE";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 1 Adjustable Desk",
                readFile(FILE).contains("Total Price: $400"));
    }

    @Test
    public void testGetOrder_2DeskAdjustable_OutputOrderForm() throws Exception {
        // Placing two adjustable desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "ADJUSTABLE";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 2 Adjustable Desks",
                readFile(FILE).contains("Total Price: $800"));
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyDeskAdjustable_OutputOrderForm() throws Exception {
        // Placing many adjustable desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "ADJUSTABLE";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not produce form with listed manufacturers", readFile(FILE).contains(
                "Suggested manufacturers:Academic Desks, Office Furnishings, Furniture Goods, Fine Office Supplies"));
    }

    // Testing standing desk orders
    @Test
    public void testGetOrder_1DeskStanding_OutputOrderForm() throws Exception {
        // Placing one standing desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "STANDING";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 1 Standing Desk",
                readFile(FILE).contains("Total Price: $300"));
    }

    @Test
    public void testGetOrder_2DeskStanding_OutputOrderForm() throws Exception {
        // Placing two standing desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "STANDING";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 2 Standing Desks",
                readFile(FILE).contains("Total Price: $600"));
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyDeskStanding_OutputOrderForm() throws Exception {
        // Placing many standing desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "STANDING";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not produce form with listed manufacturers", readFile(FILE).contains(
                "Suggested manufacturers:Academic Desks, Office Furnishings, Furniture Goods, Fine Office Supplies"));
    }

    // Testing traditional desk orders
    @Test
    public void testGetOrder_1DeskTraditional_OutputOrderForm() throws Exception {
        // Placing one traditional desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "TRADITIONAL";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 1 Traditional Desk",
                readFile(FILE).contains("Total Price: $100"));
    }

    @Test
    public void testGetOrder_2DeskTraditional_OutputOrderForm() throws Exception {
        // Placing two traditional desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "TRADITIONAL";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();

        System.out.println(readFile(FILE));
        assertTrue("fulfillOrder() does not correctly produce form for 2 Traditional Desks",
                readFile(FILE).contains("Total Price: $200"));
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyDeskTraditional_OutputOrderForm() {
        // Placing many traditional desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "TRADITIONAL";
        testOrder.quantity = 4;

    }

    // Testing ergonomic chair orders
    @Test
    public void testGetOrder_1ChairErgonomic_OutputOrderForm() {
        // Placing one ergonomic chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "ERGONOMIC";
        testOrder.quantity = 1;
        int cost = testOrder.calculateOrder();
        assertEquals(250, cost);
    }

    @Test
    public void testGetOrder_2ChairErgonomic_OutputOrderForm() {
        // Placing two ergonomic chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "ERGONOMIC";
        testOrder.quantity = 2;
        int cost = testOrder.calculateOrder();
        assertEquals(-1, cost);
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairErgonomic_OutputOrderForm() {
        // Placing many ergonomic chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "ERGONOMIC";
        testOrder.quantity = 4;
    }

    // Testing executive chair orders
    @Test
    public void testGetOrder_1ChairExecutive_OutputOrderForm() {
        // Placing one executive chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "EXECUTIVE";
        testOrder.quantity = 1;
        int cost = testOrder.calculateOrder();
        assertEquals(400, cost);
    }

    @Test
    public void testGetOrder_2ChairExecutive_OutputOrderForm() {
        // Placing two executive chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "EXECUTIVE";
        testOrder.quantity = 2;
        int cost = testOrder.calculateOrder();
        assertEquals(-1, cost);
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairExecutive_OutputOrderForm() {
        // Placing many executive chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "EXECUTIVE";
        testOrder.quantity = 4;
    }

    // Testing kneeling chair orders
    @Test
    public void testGetOrder_1ChairKneeling_OutputOrderForm() {
        // Placing one kneeling chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "KNEELING";
        testOrder.quantity = 1;
        int cost = testOrder.calculateOrder();
        assertEquals(-1, cost);
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_2ChairKneeling_OutputOrderForm() {
        // Placing two kneeling chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "KNEELING";
        testOrder.quantity = 2;
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairKneeling_OutputOrderForm() {
        // Placing many kneeling chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "KNEELING";
        testOrder.quantity = 4;
    }

    // Testing mesh chair orders
    @Test
    public void testGetOrder_1ChairMesh_OutputOrderForm() {
        // Placing one mesh chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "MESH";
        testOrder.quantity = 1;
        int cost = testOrder.calculateOrder();
        assertEquals(200, cost);

    }

    @Test
    public void testGetOrder_2ChairMesh_OutputOrderForm() {
        // Placing two mesh chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "MESH";
        testOrder.quantity = 2;
        int cost = testOrder.calculateOrder();
        assertEquals(-1, cost);
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairMesh_OutputOrderForm() {
        // Placing many mesh chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "MESH";
        testOrder.quantity = 4;
    }

    // Testing task chair orders
    @Test
    public void testGetOrder_1ChairTask_OutputOrderForm() {
        // Placing one task chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "TASK";
        testOrder.quantity = 1;
        int cost = testOrder.calculateOrder();
        assertEquals(150, cost);
    }

    @Test
    public void testGetOrder_2ChairTask_OutputOrderForm() {
        // Placing two task chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "TASK";
        testOrder.quantity = 2;
        int cost = testOrder.calculateOrder();
        assertEquals(-1, cost);
    }

    // Check for manufacturer list output
    @Test
    public void testGetOrder_ManyChairTask_OutputOrderForm() {
        // Placing many task chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "TASK";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
    }
}
