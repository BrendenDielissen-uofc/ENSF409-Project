package edu.ucalgary.ensf409;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

/**
 * The type Desk.
 */
class Desk extends Furniture {
    public static ArrayList<String> TYPES = new ArrayList<>() {{
        add("Standing");
        add("Adjustable");
        add("Traditional");
    }};
    public static ArrayList<Manufacturer> MANUFACTURERS = new ArrayList<>() {{
        add(new Manufacturer("001", "Academic Desks", "236-145-2542", "BC"));
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
                    + "FROM DESK as l3\r\n"
                    + "WHERE l3.Drawer = 'Y' and l3.Type = ?) AS l3\r\n"
                    + "CROSS JOIN\r\n"
                    + "(SELECT l1.ID AS c0, l2.ID AS c1, CASE\r\n"
                    + "WHEN l1.ID = l2.ID THEN l1.Price\r\n"
                    + "ELSE l1.Price + l2.Price\r\n" + "END AS Price\r\n"
                    + "FROM (SELECT l1.ID, l1.Price\r\n"
                    + "FROM DESK as l1\r\n"
                    + "WHERE l1.Legs = 'Y' and l1.Type =?) AS l1\r\n"
                    + "CROSS JOIN (SELECT l2.ID, l2.Price\r\n"
                    + "FROM DESK as l2\r\n"
                    + "WHERE l2.Top = 'Y' and l2.Type = ?) AS l2) AS l\r\n"
                    + "ORDER BY TotalPrice ASC;"),
            entry("numberOfPartsTable", "CREATE TABLE C AS\r\n"
                    + "SELECT ID, COUNT(ID) AS NumParts FROM \r\n"
                    + "(SELECT * FROM DESK WHERE Legs = 'Y'\r\n"
                    + "UNION ALL\r\n"
                    + "SELECT * FROM DESK WHERE Top = 'Y'\r\n"
                    + "UNION ALL\r\n"
                    + "SELECT * FROM DESK WHERE Drawer = 'Y') AS t1\r\n"
                    + "GROUP BY ID;"),
            entry("getOrder", "SELECT t.c0, t.c1, t.c2, t.TotalPrice, c0.NumParts + c1.NumParts + c2.NumParts AS NumParts\r\n"
                    + "FROM (SELECT * FROM T WHERE TotalPrice = (SELECT MIN(TotalPrice) FROM T)) AS t\r\n"
                    + "LEFT JOIN C AS c0 ON c0.ID = t.c0\r\n"
                    + "LEFT JOIN C AS c1 ON c1.ID = t.c1\r\n"
                    + "LEFT JOIN C AS c2 ON c2.ID = t.c2\r\n"
                    + "ORDER BY NumParts DESC LIMIT 1;")
    ));
    /**
     * The Legs.
     */
    private final boolean legs;

    private final boolean top;
    /**
     * The Drawers.
     */
    private final boolean drawer;
    /**
     * The constant queryString.
     */
    private static final String queryString = "SELECT * FROM DESK";

    /**
     * Default constructor for Desk.
     */
    public Desk() {
        super();
        this.legs = false;
        this.top = false;
        this.drawer = false;
    }

    /**
     * Instantiates a new Desk from a SQL ResultSet.
     *
     * @param deskRs the desk rs
     */
    public Desk(ResultSet deskRs) {
        super(deskRs);
        boolean legs = false;
        boolean top = false;
        boolean drawer = false;
        try {
            legs = deskRs.getString("Legs").equals("Y");
            drawer = deskRs.getString("Drawer").equals("Y");
            top = deskRs.getString("Top").equals("Y");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.legs = legs;
        this.drawer = drawer;
        this.top = top;
    }

    /**
     * Get legs boolean.
     *
     * @return the boolean
     */
    public boolean hasLegs() {
        return this.legs;
    }

    /**
     * Get drawers boolean.
     *
     * @return the boolean
     */
    public boolean hasDrawers() {
        return this.drawer;
    }

    /**
     * Get cabinet boolean.
     *
     * @return the boolean
     */
    public boolean hasTop() {
        return this.top;
    }

    @Override
    public HashMap<String, String> getAllCombinationsQueryMap() {
        return this.allCombinationsQueryMap;
    }

    /**
     * Get query string string.
     *
     * @return the string
     */
    @Override
    public String getQueryString() {
        return Desk.queryString;
    }

    @Override
    public HashMap<String, Integer> getCountingMap() {
        return new HashMap<String, Integer>(Map.ofEntries(
                entry("legs", 0),
                entry("drawer", 0),
                entry("top", 0)
        ));
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(
                entry("legs", this.legs),
                entry("drawer", this.drawer),
                entry("top", this.top)
        ));
    }
}
