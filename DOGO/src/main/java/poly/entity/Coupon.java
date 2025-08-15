/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author quang
 */
public class Coupon {
     private String couponId;
    private String description;
    private String discountType;  // Percent or Fixed
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;  // Hoạt động or Không hoạt động

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Coupon(String couponId, String description, String discountType, BigDecimal discountValue, LocalDate startDate, LocalDate endDate) {
        this.couponId = couponId;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Hoạt động"; // Mặc định là hoạt động
    }
    
    public Coupon(String couponId, String description, String discountType, BigDecimal discountValue, LocalDate startDate, LocalDate endDate, String status) {
        this.couponId = couponId;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Coupon() {
    }
}
