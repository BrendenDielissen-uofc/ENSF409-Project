package edu.ucalgary.ensf409;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

/**
 * The type Desk.
 */
class Desk extends Furniture {
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

    /**
     * Get query string string.
     *
     * @return the string
     */
    public static String getQueryString() {
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
