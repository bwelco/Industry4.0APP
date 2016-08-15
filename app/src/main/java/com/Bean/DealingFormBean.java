package com.Bean;

/**
 * Created by bwelco on 2016/8/15.
 */
public class DealingFormBean {


    /**
     * OrderState : 订单已提交
     * OrderProgress : 0%
     * ProductNumber : 10
     * SuccessNumber : 0
     * StartTime : null
     */

    private String OrderState;
    private String OrderProgress;
    private String ProductNumber;
    private String SuccessNumber;
    private Object StartTime;

    public String getOrderState() {
        return OrderState;
    }

    public void setOrderState(String OrderState) {
        this.OrderState = OrderState;
    }

    public String getOrderProgress() {
        return OrderProgress;
    }

    public void setOrderProgress(String OrderProgress) {
        this.OrderProgress = OrderProgress;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public void setProductNumber(String ProductNumber) {
        this.ProductNumber = ProductNumber;
    }

    public String getSuccessNumber() {
        return SuccessNumber;
    }

    public void setSuccessNumber(String SuccessNumber) {
        this.SuccessNumber = SuccessNumber;
    }

    public Object getStartTime() {
        return StartTime;
    }

    public void setStartTime(Object StartTime) {
        this.StartTime = StartTime;
    }
}
