package com.Bean;

import java.util.List;

/**
 * Created by bwelco on 2016/8/15.
 */
public class WuLiuBean {


    /**
     * Time : 2016-08-14 10:37:54
     * Location : 深圳
     */

    private List<LogisticsBean> Logistics;

    public List<LogisticsBean> getLogistics() {
        return Logistics;
    }

    public void setLogistics(List<LogisticsBean> Logistics) {
        this.Logistics = Logistics;
    }

    public static class LogisticsBean {
        private String Time;
        private String Location;

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getLocation() {
            return Location;
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }
    }
}
