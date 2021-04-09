/**
 * This class acts as a data container for chair furniture types
 * @author Brenden Dielissen
 * @author Maria Martine Baclig
 * @author Nafisa Tabassum
 * @author Ronn Delos Reyes
 * @version 1.0
 */
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
    public static ArrayList<String> TYPES = new ArrayList<>() {
        {
            add("KNEELING");
            add("TASK");
            add("MESH");
            add("EXECUTIVE");
            add("ERGONOMIC");
        }
    };
    public static ArrayList<Manufacturer> MANUFACTURERS = new ArrayList<>() {
        {
            add(new Manufacturer("002", "Office Furnishings", "587-890-4387", "AB"));
            add(new Manufacturer("003", "Chairs R Us", "705-667-9481", "ON"));
            add(new Manufacturer("004", "Furniture Goods", "306-512-5508", "SK"));
            add(new Manufacturer("005", "Fine Office Supplies", "403-980-9876", "AB"));
        }
    };
    /**
     * The Legs.
     */
    private final boolean legs;
    /**
     * The Arms.
     */
    private final boolean arms;
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
    public Chair() {
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
    public Chair(ResultSet chairRs) {
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
    public boolean hasLegs() {
        return this.legs;
    }

    /**
     * Has arms boolean.
     *
     * @return the boolean
     */
    public boolean hasArms() {
        return this.arms;
    }

    /**
     * Has seat boolean.
     *
     * @return the boolean
     */
    public boolean hasSeat() {
        return this.seat;
    }

    /**
     * Has cushion boolean.
     *
     * @return the boolean
     */
    public boolean hasCushion() {
        return this.cushion;
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
        return Chair.queryString;
    }

    @Override
    public HashMap<String, Integer> getCountingMap() {
        return new HashMap<String, Integer>(
                Map.ofEntries(entry("legs", 0), entry("arms", 0), entry("seat", 0), entry("cushion", 0)));
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(entry("legs", this.legs), entry("arms", this.arms), entry("seat", this.seat),
                entry("cushion", this.cushion)));
    }
}