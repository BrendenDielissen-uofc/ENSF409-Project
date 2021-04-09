/**
 * This class acts as a data container for desk furniture types
 *
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
 * The type Desk.
 */
class Desk extends Furniture {
    public static final ArrayList<String> TYPES = new ArrayList<>() {
        {
            add("STANDING");
            add("ADJUSTABLE");
            add("TRADITIONAL");
        }
    };
    public static final ArrayList<Manufacturer> MANUFACTURERS = new ArrayList<>() {
        {
            add(new Manufacturer("001", "Academic Desks", "236-145-2542", "BC"));
            add(new Manufacturer("002", "Office Furnishings", "587-890-4387", "AB"));
            add(new Manufacturer("004", "Furniture Goods", "306-512-5508", "SK"));
            add(new Manufacturer("005", "Fine Office Supplies", "403-980-9876", "AB"));
        }
    };
    /**
     * The Legs.
     */
    private boolean legs;

    private boolean top;
    /**
     * The Drawers.
     */
    private boolean drawer;
    /**
     * The constant queryString.
     */
    private static String queryString = "SELECT * FROM DESK";

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
        return new HashMap<String, Integer>(Map.ofEntries(entry("legs", 0), entry("drawer", 0), entry("top", 0)));
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(
                Map.ofEntries(entry("legs", this.legs), entry("drawer", this.drawer), entry("top", this.top)));
    }
}
