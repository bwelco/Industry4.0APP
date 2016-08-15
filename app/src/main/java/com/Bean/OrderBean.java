package com.Bean;

import java.util.List;

/**
 * Created by bwelco on 2016/8/15.
 */
public class OrderBean {


    /**
     * ID : 1
     * ProductClass : 一号规格
     */

    private List<ProductClassBean> ProductClass;
    /**
     * ID : 1
     * Location : 北京
     */

    private List<LocationBean> Location;

    public List<ProductClassBean> getProductClass() {
        return ProductClass;
    }

    public void setProductClass(List<ProductClassBean> ProductClass) {
        this.ProductClass = ProductClass;
    }

    public List<LocationBean> getLocation() {
        return Location;
    }

    public void setLocation(List<LocationBean> Location) {
        this.Location = Location;
    }

    public static class ProductClassBean {
        private String ID;
        private String ProductClass;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getProductClass() {
            return ProductClass;
        }

        public void setProductClass(String ProductClass) {
            this.ProductClass = ProductClass;
        }
    }

    public static class LocationBean {
        private String ID;
        private String Location;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getLocation() {
            return Location;
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }
    }
}
