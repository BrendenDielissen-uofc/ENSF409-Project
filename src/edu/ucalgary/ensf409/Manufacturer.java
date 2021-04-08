package edu.ucalgary.ensf409;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Manufacturer.
 */
public class Manufacturer {
    /**
     * The Manu id.
     */
    public final String manuId;
    /**
     * The Name.
     */
    public final String name;
    /**
     * The Phone.
     */
    public final String phone;
    /**
     * The Province.
     */
    public final String province;
    /**
     * The Query string.
     */
    public static final String queryString = "SELECT * FROM MANUFACTURER";

    /**
     * Instantiates a new Manufacturer from a SQL ResultSet.
     *
     * @param manufacturerRs the manufacturer rs
     */
    public Manufacturer(ResultSet manufacturerRs) {
        String manuId = null;
        String name = null;
        String phone = null;
        String province = null;
        try {
            manuId = manufacturerRs.getString("ManuID");
            name = manufacturerRs.getString("Name");
            phone = manufacturerRs.getString("Phone");
            province = manufacturerRs.getString("Province");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.manuId = manuId;
        this.name = name;
        this.phone = phone;
        this.province = province;
    }

    /**
     * Instantiates a new Manufacturer.
     *
     * @param manuId   the manu id
     * @param name     the name
     * @param phone    the phone
     * @param province the province
     */
    public Manufacturer(String manuId, String name, String phone, String province) {
        this.manuId = manuId;
        this.name = name;
        this.phone = phone;
        this.province = province;
    }
}
