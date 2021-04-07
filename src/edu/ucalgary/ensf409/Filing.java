package edu.ucalgary.ensf409;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

/**
 * The type Filing.
 */
public class Filing extends Furniture {
    public static ArrayList<String> TYPES = new ArrayList<>(){{
        add("Small");
        add("Medium");
        add("Large");
    }};
    public static ArrayList<Manufacturer> MANUFACTURERS = new ArrayList<>(){{
        add(new Manufacturer("002", "Office Furnishings", "587-890-4387", "AB"));
        add(new Manufacturer("004", "Furniture Goods", "306-512-5508", "SK"));
        add(new Manufacturer("005", "Fine Office Supplies", "403-980-9876", "AB"));
    }};
    private final HashMap<String, String> allCombinationsQueryMap = new HashMap<>(Map.ofEntries(
            entry("createTable", "CREATE TABLE T AS SELECT c0, c1, l3.ID AS c2, CASE\r\n"
                    + "WHEN l3.ID = c0 OR l3.ID = c1 THEN l.Price\r\n"
                    + "ELSE l.Price + l3.Price\r\n"
                    + "END AS TotalPrice\r\n"
                    + "FROM (SELECT l3.ID, l3.Price\r\n"
                    + "FROM FILING as l3\r\n"
                    + "WHERE l3.Rails = 'Y' and l3.Type = ?) AS l3\r\n"
                    + "CROSS JOIN\r\n"
                    + "(SELECT l1.ID AS c0, l2.ID AS c1, CASE\r\n"
                    + "WHEN l1.ID = l2.ID THEN l1.Price\r\n"
                    + "ELSE l1.Price + l2.Price\r\n" + "END AS Price\r\n"
                    + "FROM (SELECT l1.ID, l1.Price\r\n"
                    + "FROM FILING as l1\r\n"
                    + "WHERE l1.Drawers = 'Y' and l1.Type = ?) AS l1\r\n"
                    + "CROSS JOIN (SELECT l2.ID, l2.Price\r\n"
                    + "FROM FILING as l2\r\n"
                    + "WHERE l2.Cabinet = 'Y' and l2.Type = ?) AS l2) AS l\r\n"
                    + "ORDER BY TotalPrice ASC;"),
            entry("numberOfPartsTable", "CREATE TABLE C AS\r\n"
                    + "SELECT ID, COUNT(ID) AS NumParts FROM \r\n"
                    + "(SELECT * FROM FILING WHERE Rails = 'Y'\r\n"
                    + "UNION ALL\r\n"
                    + "SELECT * FROM FILING WHERE Drawers = 'Y'\r\n"
                    + "UNION ALL\r\n"
                    + "SELECT * FROM FILING WHERE Cabinet = 'Y') AS t1\r\n"
                    + "GROUP BY ID;"),
            entry("getOrder", "SELECT t.c0, t.c1, t.c2, t.TotalPrice, c0.NumParts + c1.NumParts + c2.NumParts AS NumParts\r\n"
                    + "FROM (SELECT * \r\n"
                    + "FROM T \r\n"
                    + "WHERE TotalPrice = (SELECT MIN(TotalPrice) FROM T)) AS t\r\n"
                    + "LEFT JOIN C AS c0\r\n"
                    + "ON c0.ID = t.c0\r\n"
                    + "LEFT JOIN C AS c1\r\n"
                    + "ON c1.ID = t.c1\r\n"
                    + "LEFT JOIN C AS c2\r\n"
                    + "ON c2.ID = t.c2\r\n"
                    + "ORDER BY NumParts DESC LIMIT 1;")
    ));
    /**
     * The Rails.
     */
    private final boolean rails;
    /**
     * The Drawers.
     */
    private final boolean drawers;
    /**
     * The Cabinet.
     */
    private final boolean cabinet;
    /**
     * The constant queryString.
     */
    private static final String queryString = "SELECT * FROM FILING";

    /**
     * Default constructor for Filing.
     */
    public Filing() {
        super();
        this.rails = false;
        this.drawers = false;
        this.cabinet = false;
    }

    /**
     * Instantiates a new Filing from a SQL ResultSet.
     *
     * @param filingRs the filing rs
     */
    public Filing(ResultSet filingRs) {
        super(filingRs);
        boolean rails = false;
        boolean drawers = false;
        boolean cabinet = false;
        try {
            rails = filingRs.getString("Rails").equals("Y");
            drawers = filingRs.getString("Drawers").equals("Y");
            cabinet = filingRs.getString("Cabinet").equals("Y");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.rails = rails;
        this.drawers = drawers;
        this.cabinet = cabinet;
    }

    /**
     * Get rails boolean.
     *
     * @return the boolean
     */
    public boolean hasRails() {
        return this.rails;
    }

    /**
     * Get drawers boolean.
     *
     * @return the boolean
     */
    public boolean hasDrawers() {
        return this.drawers;
    }

    /**
     * Get cabinet boolean.
     *
     * @return the boolean
     */
    public boolean hasCabinet() {
        return this.cabinet;
    }

    @Override
    public HashMap<String, String> getAllCombinationsQueryMap(){ return this.allCombinationsQueryMap;}

    /**
     * Get query string string.
     *
     * @return the string
     */
    @Override
    public  String getQueryString() {
        return Filing.queryString;
    }

    @Override
    public HashMap<String, Integer> getCountingMap() {
        return new HashMap<String, Integer>(Map.ofEntries(
                entry("rails", 0),
                entry("drawers", 0),
                entry("cabinet", 0)
        ));
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(
                entry("rails", this.rails),
                entry("drawers", this.drawers),
                entry("cabinet", this.cabinet)
        ));
    }
}
