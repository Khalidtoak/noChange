package com.group1.swepproject.user.nochange.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Payment {
    private String customerName, item, amount, type, imageUrl, phoneNumber, userId;
    private Date mTimestamp;


    public Payment(){}

    public Payment(String customerName, String item, String amount, String type, String imageUrl, String phoneNumber, String userId) {
        this.customerName = customerName;
        this.item = item;
        this.amount = amount;
        this.type = type;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }
}
