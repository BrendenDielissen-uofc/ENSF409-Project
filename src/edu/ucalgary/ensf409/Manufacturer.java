/**
 * This class acts as a data container for furniture manufacturer information
 *
 * @author Brenden Dielissen
 * @author Maria Martine Baclig
 * @author Nafisa Tabassum
 * @author Ronn Delos Reyes
 * @version 1.0
 */
package edu.ucalgary.ensf409;

/**
 * The type Manufacturer.
 */
public class Manufacturer {
    /**
     * The Manu id.
     */
    public String manuId;
    /**
     * The Name.
     */
    public String name;
    /**
     * The Phone.
     */
    public String phone;
    /**
     * The Province.
     */
    public String province;

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
