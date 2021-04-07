package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.sql.SQLException;
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
    public void start() {
        removeAllData(DIR);
    }

    @After
    public void end() {
        removeAllData(DIR);
    }

    /*
     * Utility methods to perform common routines
     */

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

    @Test
    public void testValidUserInput() {

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
