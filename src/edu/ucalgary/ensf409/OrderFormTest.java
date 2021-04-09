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
    private Connection testDbConnect = null;
    private ResultSet results = null;

    /**
     * Pre and Post-test processes
     */
    @Before
    public void start() {
        removeAllData(FILE);
        restoreDatabase();
    }

    @After
    public void end() {
        removeAllData(FILE);
        restoreDatabase();
    }

    /*
     * Utility methods to perform common routines
     */

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
                fileContents.append(in.nextLine() + "\n");
            }
        } catch (IOException e) {
            System.err.println("I/O error opening/reading file " + file + ".");
            in.close();
            e.printStackTrace();
        }
        in.close();
        return fileContents.toString();
    }

    // Removes generated order forms
    public void removeAllData(String file) {
        // Get current working directory and append the provided
        // file/dir
        String absolute_path = System.getProperty("user.dir");
        File absolute = new File(absolute_path);
        File path = new File(absolute, file);
        removeAllData(path);
    }

    // Removes generated order forms
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

    // Checking if component IDs and total price are in file output string
    public static boolean containsWords(String input, String[] words) {
        return Arrays.stream(words).allMatch(input::contains);
    }

    // Rolls back database modifications
    public void restoreDatabase() {
        try {
            testDbConnect = DriverManager.getConnection("jdbc:mysql://localhost/INVENTORY", "scm", "ensf409");
            Statement query = testDbConnect.createStatement();
            // chair
            query.executeUpdate("DROP TABLE IF EXISTS CHAIR");
            query.executeUpdate("CREATE TABLE CHAIR (\r\n" + "	ID				char(5)	not null,\r\n"
                    + "	Type			varchar(25),\r\n" + "	Legs			char(1),\r\n"
                    + "	Arms			char(1),\r\n" + "	Seat			char(1),\r\n"
                    + "	Cushion			char(1),\r\n" + "    Price			integer,\r\n"
                    + "	ManuID			char(3),\r\n" + "	primary key (ID),\r\n"
                    + "	foreign key (ManuID) references MANUFACTURER(ManuID) ON UPDATE CASCADE\r\n" + ")");
            query.executeUpdate("INSERT INTO CHAIR (ID, Type, Legs, Arms, Seat, Cushion, Price, ManuID)\r\n"
                    + "VALUES\r\n" + "('C1320',	'Kneeling',	'Y',	'N',	'N',	'N',	50,	'002'),\r\n"
                    + "('C3405',	'Task',	'Y',	'Y',	'N',	'N',	100,	'003'),\r\n"
                    + "('C9890',	'Mesh',	'N',	'Y',	'N',	'Y',	50,	'003'),\r\n"
                    + "('C7268',	'Executive',	'N',	'N',	'Y',	'N',	75,	'004'),\r\n"
                    + "('C0942',	'Mesh',	'Y',	'N',	'Y',	'Y',	175,	'005'),\r\n"
                    + "('C4839',	'Ergonomic',	'N',	'N',	'N',	'Y',	50,	'002'),\r\n"
                    + "('C2483',	'Executive',	'Y',	'Y',	'N',	'N',	175,	'002'),\r\n"
                    + "('C5789',	'Ergonomic',	'Y',	'N',	'N',	'Y',	125,	'003'),\r\n"
                    + "('C3819',	'Kneeling',	'N',	'N',	'Y',	'N',	75,	'005'),\r\n"
                    + "('C5784',	'Executive',	'Y',	'N',	'N',	'Y',	150,	'004'),\r\n"
                    + "('C6748',	'Mesh',	'Y',	'N',	'N',	'N',	75,	'003'),\r\n"
                    + "('C0914',	'Task',	'N',	'N',	'Y',	'Y',	50,	'002'),\r\n"
                    + "('C1148',	'Task',	'Y',	'N',	'Y',	'Y',	125,	'003'),\r\n"
                    + "('C5409',	'Ergonomic',	'Y',	'Y',	'Y',	'N',	200,	'003'),\r\n"
                    + "('C8138',	'Mesh',	'N',	'N',	'Y',	'N',	75,	'005')");
            // desk
            query.executeUpdate("DROP TABLE IF EXISTS DESK");
            query.executeUpdate("CREATE TABLE DESK (\r\n" + "	ID				char(5)	not null,\r\n"
                    + "	Type			varchar(25),\r\n" + "	Legs			char(1),\r\n"
                    + "	Top			char(1),\r\n" + "	Drawer			char(1),\r\n"
                    + "    Price			integer,\r\n" + "	ManuID			char(3),\r\n"
                    + "	primary key (ID),\r\n"
                    + "	foreign key (ManuID) references MANUFACTURER(ManuID) ON UPDATE CASCADE\r\n" + ")");
            query.executeUpdate("INSERT INTO DESK (ID, Type, Legs, Top, Drawer, Price, ManuID)\r\n" + "VALUES\r\n"
                    + "('D3820',	'Standing',	'Y',	'N',	'N',	150,	'001'),\r\n"
                    + "('D4475',	'Adjustable',	'N',	'Y',	'Y',	200,	'002'),\r\n"
                    + "('D0890',	'Traditional',	'N',	'N',	'Y',	25,	'002'),\r\n"
                    + "('D2341',	'Standing',	'N',	'Y',	'N',	100,	'001'),\r\n"
                    + "('D9387',	'Standing',	'Y',	'Y',	'N',	250,	'004'),\r\n"
                    + "('D7373',	'Adjustable',	'Y',	'Y',	'N',	350,	'005'),\r\n"
                    + "('D2746',	'Adjustable',	'Y',	'N',	'Y',	250,	'004'),\r\n"
                    + "('D9352',	'Traditional',	'Y',	'N',	'Y',	75,	'002'),\r\n"
                    + "('D4231',	'Traditional',	'N',	'Y',	'Y',	50,	'005'),\r\n"
                    + "('D8675',	'Traditional',	'Y',	'Y',	'N',	75,	'001'),\r\n"
                    + "('D1927',	'Standing',	'Y',	'N',	'Y',	200,	'005'),\r\n"
                    + "('D1030',	'Adjustable',	'N',	'Y',	'N',	150,	'002'),\r\n"
                    + "('D4438',	'Standing',	'N',	'Y',	'Y',	150,	'004'),\r\n"
                    + "('D5437',	'Adjustable',	'Y',	'N',	'N',	200,	'001'),\r\n"
                    + "('D3682',	'Adjustable',	'N',	'N',	'Y',	50,	'005');");
            // lamp
            query.executeUpdate("DROP TABLE IF EXISTS LAMP");
            query.executeUpdate("CREATE TABLE LAMP (\r\n" + "	ID				char(4)	not null,\r\n"
                    + "	Type			varchar(25),\r\n" + "	Base			char(1),\r\n"
                    + "	Bulb			char(1),\r\n" + "    Price			integer,\r\n"
                    + "	ManuID			char(3),\r\n" + "	primary key (ID),\r\n"
                    + "	foreign key (ManuID) references MANUFACTURER(ManuID) ON UPDATE CASCADE\r\n" + ");");
            query.executeUpdate("INSERT INTO LAMP (ID, Type, Base, Bulb, Price, ManuID)\r\n" + "VALUES\r\n"
                    + "('L132',	'Desk',	'Y',	'N',	18,	'005'),\r\n"
                    + "('L980',	'Study',	'N',	'Y',	2,	'004'),\r\n"
                    + "('L487',	'Swing Arm',	'Y',	'N',	27,	'002'),\r\n"
                    + "('L564',	'Desk',	'Y',	'Y',	20,	'004'),\r\n"
                    + "('L342',	'Desk',	'N',	'Y',	2,	'002'),\r\n"
                    + "('L982',	'Study',	'Y',	'N',	8,	'002'),\r\n"
                    + "('L879',	'Swing Arm',	'N',	'Y',	3,	'005'),\r\n"
                    + "('L208',	'Desk',	'N',	'Y',	2,	'005'),\r\n"
                    + "('L223',	'Study',	'N',	'Y',	2,	'005'),\r\n"
                    + "('L928',	'Study',	'Y',	'Y',	10,	'002'),\r\n"
                    + "('L013',	'Desk',	'Y',	'N',	18,	'004'),\r\n"
                    + "('L053',	'Swing Arm',	'Y',	'N',	27,	'002'),\r\n"
                    + "('L112',	'Desk',	'Y',	'N',	18,	'005'),\r\n"
                    + "('L649',	'Desk',	'Y',	'N',	18,	'004'),\r\n"
                    + "('L096',	'Swing Arm',	'N',	'Y',	3,	'002')");

            // filing
            query.executeUpdate("DROP TABLE IF EXISTS FILING");
            query.executeUpdate("CREATE TABLE FILING (\r\n" + "	ID				char(4)	not null,\r\n"
                    + "	Type			varchar(25),\r\n" + "	Rails			char(1),\r\n"
                    + "	Drawers			char(1),\r\n" + "	Cabinet			char(1),\r\n"
                    + "    Price			integer,\r\n" + "	ManuID			char(3),\r\n"
                    + "	primary key (ID),\r\n"
                    + "	foreign key (ManuID) references MANUFACTURER(ManuID) ON UPDATE CASCADE\r\n" + ")");
            query.executeUpdate("INSERT INTO FILING (ID, Type, Rails, Drawers, Cabinet, Price, ManuID)\r\n"
                    + "VALUES\r\n" + "('F001',	'Small',	'Y',	'Y',	'N',	50,	'005'),\r\n"
                    + "('F002',	'Medium',	'N',	'N',	'Y',	100,	'004'),\r\n"
                    + "('F003',	'Large',	'N',	'N',	'Y',	150,	'002'),\r\n"
                    + "('F004',	'Small',	'N',	'Y',	'Y',	75,	'004'),\r\n"
                    + "('F005',	'Small',	'Y',	'N',	'Y',	75,	'005'),\r\n"
                    + "('F006',	'Small',	'Y',	'Y',	'N',	50,	'005'),\r\n"
                    + "('F007',	'Medium',	'N',	'Y',	'Y',	150,	'002'),\r\n"
                    + "('F008',	'Medium',	'Y',	'N',	'N',	50,	'005'),\r\n"
                    + "('F009',	'Medium',	'Y',	'Y',	'N',	100,	'004'),\r\n"
                    + "('F010',	'Large',	'Y',	'N',	'Y',	225,	'002'),\r\n"
                    + "('F011',	'Large',	'N',	'Y',	'Y',	225,	'005'),\r\n"
                    + "('F012',	'Large',	'N',	'Y',	'N',	75,	'005'),\r\n"
                    + "('F013',	'Small',	'N',	'N',	'Y',	50,	'002'),\r\n"
                    + "('F014',	'Medium',	'Y',	'Y',	'Y',	200,	'002'),\r\n"
                    + "('F015',	'Large',	'Y',	'N',	'N',	75,	'004')");

            query.close();
            testDbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Testing User input
    @Test
    public void testRequestOrder_AllValidUserInputs_ReturnsUserInput() {
        OrderForm testOrder = new OrderForm();
        String userInput = "Lamp" + "\nDesk" + "\n1";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        testOrder.requestOrder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestOrder_InvalidFurnitureCategory_ExceptionThrown() {
        OrderForm testOrder = new OrderForm();
        String userInput = "Bed" + "\nStanding" + "\n2";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        testOrder.requestOrder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestOrder_InvalidFurnitureQuantity_ExceptionThrown() {
        OrderForm testOrder = new OrderForm();
        String userInput = "Chair" + "\nMesh" + "\n0";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        testOrder.requestOrder();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestOrder_InvalidFurnitureType_ExceptionThrown() {
        OrderForm testOrder = new OrderForm();
        String userInput = "Lamp" + "\nStanding" + "\n1";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        testOrder.requestOrder();
    }

    // Testing desk lamp orders
    @Test
    public void testFulfillOrder_1LampDesk_OutputOrderForm() throws Exception {
        // Placing one desk lamp order
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Desk";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "L013", "L208", "Total Price: $20" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Desk Lamp",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2LampDesk_OutputOrderForm() throws Exception {
        // Placing two desk lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Desk";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "L013", "L112", "L208", "Total Price: $40" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Desk Lamps",
                containsWords(readFile(FILE), expectedOutput));

    }

    @Test
    // Check for manufacturer list output
    public void testFulfillOrder_ManyLampDesk_OutputOrderForm() throws Exception {
        // Placing multiple desk lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Desk";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing study lamp orders
    @Test
    public void testFulfillOrder_1LampStudy_OutputOrderForm() throws Exception {
        // Placing one study lamp order
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Study";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "L223", "L982", "Total Price: $10" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Study Lamp",
                containsWords(readFile(FILE), expectedOutput));

    }

    @Test
    public void testFulfillOrder_2LampStudy_OutputOrderForm() throws Exception {
        // Placing two study lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Study";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "L223", "L928", "L982", "Total Price: $20" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Study Lamps",
                containsWords(readFile(FILE), expectedOutput));

    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyLampStudy_OutputOrderForm() throws Exception {
        // Placing many study lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Study";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing swing arm lamp orders
    @Test
    public void testFulfillOrder_1LampSwingArm_OutputOrderForm() throws Exception {
        // Placing one swing arm lamp order
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Swing Arm";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "L053", "L096", "Total Price: $30" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Swing Arm Lamp",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2LampSwingArm_OutputOrderForm() throws Exception {
        // Placing two swing arm lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Swing Arm";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "L053", "L096", "L487", "L879", "Total Price: $60" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Swing Arm Lamps",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyLampSwingArm_OutputOrderForm() throws Exception {
        // Placing many swing arm lamp orders
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Lamp";
        String furnitureType = "Swing Arm";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing small filing cabinet orders
    @Test
    public void testFulfillOrder_1FilingSmall_OutputOrderForm() throws Exception {
        // Placing one small filing cabinet order
        OrderForm testOrder = new OrderForm();
        // Simulates potential user input
        String furniture = "Filing";
        String furnitureType = "Small";
        testOrder.furnitureCategory = furniture.toUpperCase();
        testOrder.furnitureType = furnitureType.toUpperCase();
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "F001", "F013", "Total Price: $100" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Small Filing Cabinet",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2FilingSmall_OutputOrderForm() throws Exception {
        // Place two small filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "SMALL";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "F001", "F004", "F005", "Total Price: $200" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Small Filing Cabinets",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyFilingSmall_OutputOrderForm() throws Exception {
        // Place many small filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "SMALL";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing medium filing cabinet orders
    @Test
    public void testFulfillOrder_1FilingMedium_OutputOrderForm() throws Exception {
        // Placing one medium filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "MEDIUM";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "F002", "F009", "Total Price: $200" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Medium Filing Cabinet",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2FilingMedium_OutputOrderForm() throws Exception {
        // Placing two medium filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "MEDIUM";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "F002", "F007", "F008", "F009", "Total Price: $400" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Medium Filing Cabinets",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyFilingMedium_OutputOrderForm() throws Exception {
        // Placing many medium filing cabinet orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "MEDIUM";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));

    }

    @Test
    public void testFulfillOrder_1FilingLarge_OutputOrderForm() throws Exception {
        // Placing one large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "LARGE";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "F003", "F012", "F015", "Total Price: $300" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Large Filing Cabinet",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2FilingLarge_OutputOrderForm() throws Exception {
        // Placing two large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "LARGE";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "F010", "F011", "F012", "F015", "Total Price: $600" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Large Filing Cabinets",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyFilingLarge_OutputOrderForm() throws Exception {
        // Placing many large filing cabinet order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "FILING";
        testOrder.furnitureType = "LARGE";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing adjustable desk orders
    @Test
    public void testFulfillOrder_1DeskAdjustable_OutputOrderForm() throws Exception {
        // Placing one adjustable desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "ADJUSTABLE";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "D1030", "D3682", "D5437", "Total Price: $400" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Adjustable Desk",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2DeskAdjustable_OutputOrderForm() throws Exception {
        // Placing two adjustable desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "ADJUSTABLE";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "D1030", "D2746", "D3682", "D7373", "Total Price: $800" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Adjustable Desks",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyDeskAdjustable_OutputOrderForm() throws Exception {
        // Placing many adjustable desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "ADJUSTABLE";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Academic Desks", "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing standing desk orders
    @Test
    public void testFulfillOrder_1DeskStanding_OutputOrderForm() throws Exception {
        // Placing one standing desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "STANDING";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "D1927", "D2341", "Total Price: $300" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Standing Desk",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2DeskStanding_OutputOrderForm() throws Exception {
        // Placing two standing desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "STANDING";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "D1927", "D2341", "D3820", "D4438", "Total Price: $600" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Standing Desks",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyDeskStanding_OutputOrderForm() throws Exception {
        // Placing many standing desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "STANDING";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Academic Desks", "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing traditional desk orders
    @Test
    public void testFulfillOrder_1DeskTraditional_OutputOrderForm() throws Exception {
        // Placing one traditional desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "TRADITIONAL";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "D0890", "D8675", "Total Price: $100" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Traditional Desk",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2DeskTraditional_OutputOrderForm() throws Exception {
        // Placing two traditional desk order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "TRADITIONAL";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "D4231", "D8675", "D9352", "Total Price: $200" };

        assertTrue("fulfillOrder() does not correctly produce form for 2 Traditional Desks",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyDeskTraditional_OutputOrderForm() throws Exception {
        // Placing many traditional desk orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "DESK";
        testOrder.furnitureType = "TRADITIONAL";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Academic Desks", "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing ergonomic chair orders
    @Test
    public void testFulfillOrder_1ChairErgonomic_OutputOrderForm() throws Exception {
        // Placing one ergonomic chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "ERGONOMIC";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "C4839", "C5409", "Total Price: $250" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Ergonomic Chair",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2ChairErgonomic_OutputOrderForm() throws Exception {
        // Placing two ergonomic chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "ERGONOMIC";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyChairErgonomic_OutputOrderForm() throws Exception {
        // Placing many ergonomic chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "ERGONOMIC";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing executive chair orders
    @Test
    public void testFulfillOrder_1ChairExecutive_OutputOrderForm() throws Exception {
        // Placing one executive chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "EXECUTIVE";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "C2483", "C5784", "C7268", "Total Price: $400" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Executive Chair",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2ChairExecutive_OutputOrderForm() throws Exception {
        // Placing two executive chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "EXECUTIVE";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyChairExecutive_OutputOrderForm() throws Exception {
        // Placing many executive chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "EXECUTIVE";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing kneeling chair orders
    @Test
    public void testFulfillOrder_1ChairKneeling_OutputOrderForm() throws Exception {
        // Placing one kneeling chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "KNEELING";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_2ChairKneeling_OutputOrderForm() throws Exception {
        // Placing two kneeling chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "KNEELING";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyChairKneeling_OutputOrderForm() throws Exception {
        // Placing many kneeling chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "KNEELING";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing mesh chair orders
    @Test
    public void testFulfillOrder_1ChairMesh_OutputOrderForm() throws Exception {
        // Placing one mesh chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "MESH";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "C6748", "C8138", "C9890", "Total Price: $200" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Mesh Chair",
                containsWords(readFile(FILE), expectedOutput));

    }

    @Test
    public void testFulfillOrder_2ChairMesh_OutputOrderForm() throws Exception {
        // Placing two mesh chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "MESH";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyChairMesh_OutputOrderForm() throws Exception {
        // Placing many mesh chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "MESH";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing task chair orders
    @Test
    public void testFulfillOrder_1ChairTask_OutputOrderForm() throws Exception {
        // Placing one task chair order
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "TASK";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "C0914", "C3405", "Total Price: $150" };

        assertTrue("fulfillOrder() does not correctly produce form for 1 Task Chair",
                containsWords(readFile(FILE), expectedOutput));
    }

    @Test
    public void testFulfillOrder_2ChairTask_OutputOrderForm() throws Exception {
        // Placing two task chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "TASK";
        testOrder.quantity = 2;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Check for manufacturer list output
    @Test
    public void testFulfillOrder_ManyChairTask_OutputOrderForm() throws Exception {
        // Placing many task chair orders
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "CHAIR";
        testOrder.furnitureType = "TASK";
        testOrder.quantity = 4;
        testOrder.fulfillOrder();
        String[] expectedOutput = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };

        assertTrue("fulfillOrder() does not produce form with listed manufacturers",
                containsWords(readFile(FILE), expectedOutput));
    }

    // Testing whether entries are removed after successful orders
    @Test
    public void testDeleteFurniture_1LampOrder_NoReturnedRows() {
        OrderForm testOrder = new OrderForm();
        testOrder.furnitureCategory = "LAMP";
        testOrder.furnitureType = "DESK";
        testOrder.quantity = 1;
        testOrder.fulfillOrder();

        try {
            testDbConnect = DriverManager.getConnection("jdbc:mysql://localhost/INVENTORY", "scm", "ensf409");
            Statement query = testDbConnect.createStatement();
            results = query.executeQuery("SELECT * FROM LAMP WHERE ID='L013' OR ID='L208'");

            assertTrue("deleteFurniture() failed to remove entries from database after successful order",
                    !results.isBeforeFirst());

            results.close();
            query.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
