package com.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bwelco on 2016/8/15.
 */
public class OrderListBean implements Serializable {


    /**
     * OrderID : 36
     * OrderStateCode : -1
     * OrderState : 订单已提交
     * description : 草
     * ProductClass : 一号规格
     * ProductNumber : 10
     * Location : 北京
     * CreateTime : 2016-08-14 11:10:02
     * StartTime : null
     * FinishTime : null
     */

    private List<OrderInfoBean> OrderInfo;

    public List<OrderInfoBean> getOrderInfo() {
        return OrderInfo;
    }

    public void setOrderInfo(List<OrderInfoBean> OrderInfo) {
        this.OrderInfo = OrderInfo;
    }

    public static class OrderInfoBean implements Serializable {
        private String OrderID;
        private String OrderStateCode;
        private String OrderState;
        private String description;
        private String ProductClass;
        private String ProductNumber;
        private String Location;
        private String CreateTime;
        private Object StartTime;
        private Object FinishTime;

        public String getOrderID() {
            return OrderID;
        }

        public void setOrderID(String OrderID) {
            this.OrderID = OrderID;
        }

        public String getOrderStateCode() {
            return OrderStateCode;
        }

        public void setOrderStateCode(String OrderStateCode) {
            this.OrderStateCode = OrderStateCode;
        }

        public String getOrderState() {
            return OrderState;
        }

        public void setOrderState(String OrderState) {
            this.OrderState = OrderState;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getProductClass() {
            return ProductClass;
        }

        public void setProductClass(String ProductClass) {
            this.ProductClass = ProductClass;
        }

        public String getProductNumber() {
            return ProductNumber;
        }

        public void setProductNumber(String ProductNumber) {
            this.ProductNumber = ProductNumber;
        }

        public String getLocation() {
            return Location;
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public Object getStartTime() {
            return StartTime;
        }

        public void setStartTime(Object StartTime) {
            this.StartTime = StartTime;
        }

        public Object getFinishTime() {
            return FinishTime;
        }

        public void setFinishTime(Object FinishTime) {
            this.FinishTime = FinishTime;
        }
    }
}
