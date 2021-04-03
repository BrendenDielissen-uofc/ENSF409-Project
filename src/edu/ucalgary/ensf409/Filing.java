package edu.ucalgary.ensf409;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

/**
 * The type Filing.
 */
public class Filing extends Furniture {
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
     * Instantiates a new Filing.
     *
     * @param id          the id
     * @param type        the type
     * @param price       the price
     * @param manuId      the manu id
     * @param queryString the query string
     * @param rails       the rails
     * @param drawers     the drawers
     * @param cabinet     the cabinet
     */
    public Filing(String id, String type, Integer price, String manuId, String queryString, boolean rails, boolean drawers, boolean cabinet) {
        super(id, type, price, manuId);
        this.rails = rails;
        this.drawers = drawers;
        this.cabinet = cabinet;
    }

    /**
     * Get rails boolean.
     *
     * @return the boolean
     */
    public boolean hasRails(){ return this.rails;}

    /**
     * Get drawers boolean.
     *
     * @return the boolean
     */
    public boolean hasDrawers(){ return this.drawers;}

    /**
     * Get cabinet boolean.
     *
     * @return the boolean
     */
    public boolean hasCabinet(){ return this.cabinet;}

    /**
     * Get query string string.
     *
     * @return the string
     */
    public static String getQueryString(){ return Filing.queryString;}

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(
                entry("rails", this.rails),
                entry("drawers", this.drawers),
                entry("cabinet", this.cabinet)
        ));
    }
}
