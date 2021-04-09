/**
 * This class acts as a data container for filing cabinet furniture types
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
 * The type Filing.
 */
public class Filing extends Furniture {
    public static final ArrayList<String> TYPES = new ArrayList<>() {
        {
            add("SMALL");
            add("MEDIUM");
            add("LARGE");
        }
    };
    public static final ArrayList<Manufacturer> MANUFACTURERS = new ArrayList<>() {
        {
            add(new Manufacturer("002", "Office Furnishings", "587-890-4387", "AB"));
            add(new Manufacturer("004", "Furniture Goods", "306-512-5508", "SK"));
            add(new Manufacturer("005", "Fine Office Supplies", "403-980-9876", "AB"));
        }
    };
    /**
     * The Rails.
     */
    private boolean rails;
    /**
     * The Drawers.
     */
    private boolean drawers;
    /**
     * The Cabinet.
     */
    private boolean cabinet;
    /**
     * The constant queryString.
     */
    private static String queryString = "SELECT * FROM FILING";

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

    /**
     * Get query string string.
     *
     * @return the string
     */
    @Override
    public String getQueryString() {
        return Filing.queryString;
    }

    @Override
    public HashMap<String, Integer> getCountingMap() {
        return new HashMap<String, Integer>(Map.ofEntries(entry("rails", 0), entry("drawers", 0), entry("cabinet", 0)));
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(entry("rails", this.rails), entry("drawers", this.drawers),
                entry("cabinet", this.cabinet)));
    }
}
