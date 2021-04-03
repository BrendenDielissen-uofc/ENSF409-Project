package edu.ucalgary.ensf409;

import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Furniture.
 */
public abstract class Furniture {
    /**
     * The Id.
     */
    private final String id;
    /**
     * The Type.
     */
    private final String type;
    /**
     * The Price.
     */
    private final Integer price;
    /**
     * The Manu id.
     */
    private final String manuId;

    /**
     * Instantiates a new Furniture from a SQL ResultSet.
     *
     * @param furnitureRs the furniture rs
     */
    public Furniture(ResultSet furnitureRs){
        String id = null;
        String type = null;
        Integer price = null;
        String manuId = null;
        try{
            id = furnitureRs.getString("ID");
            type = furnitureRs.getString("Type");
            price = furnitureRs.getInt("Price");
            manuId = furnitureRs.getString("ManuID");
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        this.id = id;
        this.type = type;
        this.price = price;
        this.manuId = manuId;
    }

    /**
     * Instantiates a new Furniture.
     *
     * @param id     the id
     * @param type   the type
     * @param price  the price
     * @param manuId the manu id
     */
    public Furniture(String id, String type, Integer price, String manuId) {
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
    public String getId(){ return this.id;}

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() { return this.type;}

    /**
     * Gets price.
     *
     * @return the price
     */
    public Integer getPrice() {return this.price;}

    /**
     * Get manu id string.
     *
     * @return the string
     */
    public String getManuId(){ return this.manuId;}

    /**
     * Gets furniture components in an easily iterable format.
     *
     * @return the components
     */
    public abstract Map<String, Boolean> getComponents();
}

