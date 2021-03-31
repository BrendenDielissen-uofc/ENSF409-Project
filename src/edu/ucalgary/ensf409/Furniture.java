package edu.ucalgary.ensf409;

public class Furniture {
    private String id;
    private String type;
    private Integer price;
    private String manuID;
    private String queryString;

    public Furniture(String id, String type, Integer price, String manuID, String queryString){
        this.id = id;
        this.type = type;
        this.price = price;
        this.manuID = manuID;
        this.queryString = queryString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getManuID() {
        return manuID;
    }

    public void setManuID(String manuID) {
        this.manuID = manuID;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
