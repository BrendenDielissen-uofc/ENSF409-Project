package edu.ucalgary.ensf409;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static java.util.Map.entry;

/**
 * The type Desk.
 */
class Desk extends Furniture {
    /**
     * The Legs.
     */
    public final boolean legs;
    /**
     * The Drawers.
     */
    public final boolean drawers;
    /**
     * The Cabinet.
     */
    public final boolean cabinet;
    /**
     * The constant queryString.
     */
    public static final String queryString = "SELECT * FROM DESK";

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
     * Instantiates a new Desk.
     *
     * @param id      the id
     * @param type    the type
     * @param price   the price
     * @param manuId  the manu id
     * @param legs    the legs
     * @param drawers the drawers
     * @param cabinet the cabinet
     */
    public Desk(String id, String type, Integer price, String manuId, boolean legs, boolean drawers, boolean cabinet) {
        super(id, type, price, manuId);
        this.legs = legs;
        this.drawers = drawers;
        this.cabinet = cabinet;
    }

    @Override
    public Map<String, Boolean> getComponents() {
        return Map.ofEntries(
                entry("legs", this.legs),
                entry("drawers", this.drawers),
                entry("cabinet", this.cabinet)
        );
    }

}
