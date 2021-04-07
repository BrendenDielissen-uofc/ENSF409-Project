package edu.ucalgary.ensf409;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;


/**
 * The type Lamp.
 */
public class Lamp extends Furniture {
    public static ArrayList<String> TYPES = new ArrayList<>(){{
        add("Desk");
        add("Study");
        add("Swing Arm");
    }};
    public static ArrayList<Manufacturer> MANUFACTURERS = new ArrayList<>(){{
        add(new Manufacturer("002", "Office Furnishings", "587-890-4387", "AB"));
        add(new Manufacturer("004", "Furniture Goods", "306-512-5508", "SK"));
        add(new Manufacturer("005", "Fine Office Supplies", "403-980-9876", "AB"));
    }};
    private final HashMap<String, String> allCombinationsQueryMap = new HashMap<>(Map.ofEntries(
            entry("createTable", "CREATE TABLE T AS SELECT l1.ID AS c0, l2.ID AS c1, CASE\r\n"
                    + "WHEN l1.ID = l2.ID THEN l1.Price\r\n"
                    + "ELSE l1.Price + l2.Price\r\n"
                    + "END AS TotalPrice\r\n"
                    + "FROM (SELECT l1.ID, l1.Price\r\n"
                    + "FROM LAMP as l1\r\n"
                    + "WHERE l1.Bulb = 'Y' and l1.Type = ?) AS l1\r\n"
                    + "CROSS JOIN (SELECT l2.ID, l2.Price\r\n"
                    + "FROM LAMP as l2\r\n"
                    + "WHERE l2.Base = 'Y' and l2.Type = ?) AS l2\r\n"
                    + "ORDER BY TotalPrice ASC;"),
            entry("numberOfPartsTable", "CREATE TABLE C AS\r\n"
                    + "SELECT ID, COUNT(ID) AS NumParts FROM \r\n"
                    + "(SELECT * FROM LAMP WHERE Base = 'Y'\r\n"
                    + "UNION ALL\r\n"
                    + "SELECT * FROM LAMP WHERE Bulb = 'Y') AS t1\r\n"
                    + "GROUP BY ID;"),
            entry("getOrder", "SELECT t.c0, t.c1, t.TotalPrice, c0.NumParts + c1.NumParts AS NumParts\r\n"
                    + "FROM (SELECT * FROM T WHERE TotalPrice = (SELECT MIN(TotalPrice) FROM T)) AS t\r\n"
                    + "LEFT JOIN C AS c0 ON c0.ID = t.c0\r\n"
                    + "LEFT JOIN C AS c1 ON c1.ID = t.c1\r\n"
                    + "ORDER BY NumParts DESC LIMIT 1;")
    ));
    /**
     * The Base.
     */
    private final boolean base;
    /**
     * The Bulb.
     */
    private final boolean bulb;
    /**
     * The constant queryString.
     */
    private static final String queryString = "SELECT * FROM LAMP";

    /**
     * Default constructor for Lamp.
     */
    public Lamp(){
        super();
        this.base = false;
        this.bulb = false;
    }

    /**
     * Instantiates a new Lamp from a SQL ResultSet.
     *
     * @param lampRs the lamp rs
     */
    public Lamp(ResultSet lampRs) {
        super(lampRs);
        boolean base = false;
        boolean bulb = false;
        try {
            base = lampRs.getString("Base").equals("Y");
            bulb = lampRs.getString("Bulb").equals("Y");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.base = base;
        this.bulb = bulb;
    }

    /**
     * Has base boolean.
     *
     * @return the boolean
     */
    public boolean hasBase(){return this.base;}

    /**
     * Has bulb boolean.
     *
     * @return the boolean
     */
    public boolean hasBulb(){return this.bulb;}

    @Override
    public HashMap<String, String> getAllCombinationsQueryMap(){ return this.allCombinationsQueryMap;}

    /**
     * Gets query string.
     *
     * @return the query string
     */
    @Override
    public String getQueryString() { return Lamp.queryString;}

    @Override
    public HashMap<String, Integer> getCountingMap() {
        return new HashMap<String, Integer>(Map.ofEntries(
                entry("base", 0),
                entry("bulb", 0)
        ));
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(
                entry("base", this.base),
                entry("bulb", this.bulb)
        ));
    }
}
