/**
 * This class acts a data container for all furniture types
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
import java.util.HashMap;


/**
 * The type Furniture.
 */
public abstract class Furniture {
    /**
     * The Id.
     */
    private String id;
    /**
     * The Type.
     */
    private String type;
    /**
     * The Price.
     */
    private Integer price;
    /**
     * The Manu id.
     */
    private String manuId;

    /**
     * Default constructor for Furniture.
     */
    public Furniture() {
        this.id = null;
        this.price = null;
        this.type = null;
        this.manuId = null;
    }

    /**
     * Instantiates a new Furniture from a SQL ResultSet.
     *
     * @param furnitureRs the furniture rs
     */
    public Furniture(ResultSet furnitureRs) {
        String id = null;
        String type = null;
        Integer price = null;
        String manuId = null;
        try {
            id = furnitureRs.getString("ID");
            type = furnitureRs.getString("Type");
            price = furnitureRs.getInt("Price");
            manuId = furnitureRs.getString("ManuID");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.id = id;
        this.type = type;
        this.price = price;
        this.manuId = manuId;
    }

    /**
     * Get id string.
     *
     * @return the string
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public Integer getPrice() {
        return this.price;
    }

    /**
     * Get manu id string.
     *
     * @return the string
     */
    public String getManuId() {
        return this.manuId;
    }

    /**
     * Gets query string.
     *
     * @return the query string
     */
    public abstract String getQueryString();

    /**
     * Gets counting map.
     *
     * @return the counting map
     */
    public abstract HashMap<String, Integer> getCountingMap();

    /**
     * Gets components.
     *
     * @return the components
     */
    public abstract HashMap<String, Boolean> getComponents();
}
