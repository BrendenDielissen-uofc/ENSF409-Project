package edu.ucalgary.ensf409;

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
