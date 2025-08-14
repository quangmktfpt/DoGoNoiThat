/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.entity;

import java.time.LocalDateTime;

/**
 *
 * @author quang
 */
public class Address {
       private Integer addressId;
    private Integer userId;
    private String addressLine1;
    private String city;
    private String country;
    private String phone;
    private String customerName;
    private Boolean isDefault;
    private String couponId;
    private Integer orderId;
    private LocalDateTime createdDate;

    public Address(Integer addressId, Integer userId, String addressLine1, String city, String country, 
                   String phone, String customerName, Boolean isDefault, String couponId, Integer orderId, LocalDateTime createdDate) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.customerName = customerName;
        this.isDefault = isDefault;
        this.couponId = couponId;
        this.orderId = orderId;
        this.createdDate = createdDate;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Address() {
    }
    
}
