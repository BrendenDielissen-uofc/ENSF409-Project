/**
 * This class acts as a data container for lamp furniture types
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
 * The type Lamp.
 */
public class Lamp extends Furniture {
    public static ArrayList<String> LAMP_TYPES = new ArrayList<>() {
        {
            add("DESK");
            add("STUDY");
            add("SWING ARM");
        }
    };
    public static ArrayList<Manufacturer> MANUFACTURERS = new ArrayList<>() {
        {
            add(new Manufacturer("002", "Office Furnishings", "587-890-4387", "AB"));
            add(new Manufacturer("004", "Furniture Goods", "306-512-5508", "SK"));
            add(new Manufacturer("005", "Fine Office Supplies", "403-980-9876", "AB"));
        }
    };
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
    public Lamp() {
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
    public boolean hasBase() {
        return this.base;
    }

    /**
     * Has bulb boolean.
     *
     * @return the boolean
     */
    public boolean hasBulb() {
        return this.bulb;
    }

    /**
     * Gets query string.
     *
     * @return the query string
     */
    @Override
    public String getQueryString() {
        return Lamp.queryString;
    }

    @Override
    public HashMap<String, Integer> getCountingMap() {
        return new HashMap<String, Integer>(Map.ofEntries(entry("base", 0), entry("bulb", 0)));
    }

    @Override
    public HashMap<String, Boolean> getComponents() {
        return new HashMap<>(Map.ofEntries(entry("base", this.base), entry("bulb", this.bulb)));
    }
}