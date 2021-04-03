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
    private static final String queryString = "SELECT * FROM DESK";

    /**
     * Default constructor for Desk.
     */
    public Desk() {
        super();
        this.legs = false;
        this.drawers = false;
        this.cabinet = false;
    }

    /**
     * Instantiates a new Desk from a SQL ResultSet.
     *
     * @param deskRs the desk rs
     */
    public Desk(ResultSet deskRs) {
        super(deskRs);
        boolean legs = false;
        boolean drawers = false;
        boolean cabinet = false;
        try {
            legs = deskRs.getString("Legs").equals("Y");
            drawers = deskRs.getString("Drawers").equals("Y");
            cabinet = deskRs.getString("Cabinet").equals("Y");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.legs = legs;
        this.drawers = drawers;
        this.cabinet = cabinet;
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

    /**
     * Get query string string.
     *
     * @return the string
     */
    public static String getQueryString() {
        return Desk.queryString;
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(
                entry("legs", this.legs),
                entry("drawers", this.drawers),
                entry("cabinet", this.cabinet)
        ));
    }
}
