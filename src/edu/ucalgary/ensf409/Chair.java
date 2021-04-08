package edu.ucalgary.ensf409;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;

/**
 * The type Chair.
 */
public class Chair extends Furniture {
    public static ArrayList<String> TYPES = new ArrayList<>(){{
        add("KNEELING");
        add("TASK");
        add("MESH");
        add("EXECUTIVE");
        add("ERGONOMIC");
    }};
    public static ArrayList<Manufacturer> MANUFACTURERS = new ArrayList<>(){{
        add(new Manufacturer("002", "Office Furnishings", "587-890-4387", "AB"));
        add(new Manufacturer("003", "Chairs R Us", "705-667-9481", "ON"));
        add(new Manufacturer("004", "Furniture Goods", "306-512-5508", "SK"));
        add(new Manufacturer("005", "Fine Office Supplies", "403-980-9876", "AB"));
    }};
    private final HashMap<String, String> allCombinationsQueryMap = new HashMap<>(Map.ofEntries(
            entry("createTable", "CREATE TABLE T AS SELECT c0, c1, c2, l4.ID AS c3, CASE\r\n"
                    + "WHEN l4.ID = c0 OR l4.ID = c1 OR l4.ID = c2 THEN f.Price\r\n"
                    + "ELSE f.Price + l4.Price\r\n"
                    + "END AS TotalPrice\r\n"
                    + "FROM (SELECT l4.ID, l4.Price\r\n"
                    + "FROM CHAIR as l4\r\n"
                    + "WHERE l4.Cushion = 'Y' and l4.Type = ?) AS l4\r\n"
                    + "CROSS JOIN\r\n"
                    + "(SELECT c0, c1, l3.ID AS c2, CASE\r\n"
                    + "WHEN l3.ID = c0 OR l3.ID = c1 THEN l.Price\r\n"
                    + "ELSE l.Price + l3.Price\r\n" + "END AS Price\r\n"
                    + "FROM (SELECT l3.ID, l3.Price\r\n"
                    + "FROM CHAIR as l3\r\n"
                    + "WHERE l3.Seat = 'Y' and l3.Type = ?) AS l3\r\n"
                    + "CROSS JOIN\r\n"
                    + "(SELECT l1.ID AS c0, l2.ID AS c1, CASE\r\n"
                    + "WHEN l1.ID = l2.ID THEN l1.Price\r\n"
                    + "ELSE l1.Price + l2.Price\r\n" + "END AS Price\r\n"
                    + "FROM (SELECT l1.ID, l1.Price\r\n"
                    + "FROM CHAIR as l1\r\n"
                    + "WHERE l1.Legs = 'Y' and l1.Type = ?) AS l1\r\n"
                    + "CROSS JOIN (SELECT l2.ID, l2.Price\r\n"
                    + "FROM CHAIR as l2\r\n"
                    + "WHERE l2.Arms = 'Y' and l2.Type = ?) AS l2) AS l) AS f\r\n"
                    + "ORDER BY TotalPrice ASC;"),
            entry("numberOfPartsTable", "CREATE TABLE C AS\r\n"
                    + "SELECT ID, COUNT(ID) AS NumParts FROM \r\n"
                    + "(SELECT * FROM CHAIR WHERE Legs = 'Y'\r\n"
                    + "UNION ALL\r\n"
                    + "SELECT * FROM CHAIR WHERE Arms = 'Y'\r\n"
                    + "UNION ALL\r\n"
                    + "SELECT * FROM CHAIR WHERE Seat = 'Y'\r\n"
                    + "UNION ALL\r\n"
                    + "SELECT * FROM CHAIR WHERE Cushion = 'Y') AS t1\r\n"
                    + "GROUP BY ID ORDER BY NumParts DESC;"),
            entry("getOrder", "SELECT t.c0, t.c1, t.c2, t.c3, t.TotalPrice, c0.NumParts + c1.NumParts + c2.NumParts + c3.NumParts AS NumParts\r\n"
                    + "FROM (SELECT * FROM T WHERE TotalPrice = (SELECT MIN(TotalPrice) FROM T)) AS t\r\n"
                    + "LEFT JOIN C AS c0 ON c0.ID = t.c0\r\n"
                    + "LEFT JOIN C AS c1 ON c1.ID = t.c1\r\n"
                    + "LEFT JOIN C AS c2 ON c2.ID = t.c2\r\n"
                    + "LEFT JOIN C AS c3 ON c3.ID = t.c3\r\n"
                    + "ORDER BY NumParts DESC LIMIT 1;")
    ));
    /**
     * The Legs.
     */
    private final boolean legs;
    /**
     * The Arms.
     */
    private final  boolean arms;
    /**
     * The Seat.
     */
    private final boolean seat;
    /**
     * The Cushion.
     */
    private final boolean cushion;
    /**
     * The constant queryString.
     */
    private static final String queryString = "SELECT * FROM CHAIR";

    /**
     * Default constructor for Chair.
     */
    public Chair(){
        super();
        this.legs = false;
        this.arms = false;
        this.seat = false;
        this.cushion = false;
    }

    /**
     * Instantiates a new Chair from a SQL ResultSet.
     *
     * @param chairRs the chair rs
     */
    public Chair(ResultSet chairRs){
        super(chairRs);
        boolean legs = false;
        boolean arms = false;
        boolean seat = false;
        boolean cushion = false;
        try {
            legs = chairRs.getString("Legs").equals("Y");
            arms = chairRs.getString("Arms").equals("Y");
            seat = chairRs.getString("Seat").equals("Y");
            cushion = chairRs.getString("Cushion").equals("Y");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.legs = legs;
        this.arms = arms;
        this.seat = seat;
        this.cushion = cushion;
    }

    /**
     * Has legs boolean.
     *
     * @return the boolean
     */
    public boolean hasLegs(){return this.legs;}

    /**
     * Has arms boolean.
     *
     * @return the boolean
     */
    public boolean hasArms(){return this.arms;}

    /**
     * Has seat boolean.
     *
     * @return the boolean
     */
    public boolean hasSeat(){ return this.seat;}

    /**
     * Has cushion boolean.
     *
     * @return the boolean
     */
    public boolean hasCushion(){return this.cushion;}

    @Override
    public HashMap<String, String> getAllCombinationsQueryMap(){ return this.allCombinationsQueryMap;}

    /**
     * Get query string string.
     *
     * @return the string
     */
    @Override
    public String getQueryString(){ return Chair.queryString;}

    @Override
    public HashMap<String, Integer> getCountingMap() {
        return new HashMap<String, Integer>(Map.ofEntries(
                entry("legs", 0),
                entry("arms", 0),
                entry("seat", 0),
                entry("cushion", 0)
        ));
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(
            entry("legs", this.legs),
            entry("arms", this.arms),
            entry("seat", this.seat),
            entry("cushion", this.cushion)
        ));
    }
}