/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author quang
 */
public class PurchaseOrder {
     private Integer purchaseOrderId;
    private String supplierId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PurchaseOrder() {
    }

    public PurchaseOrder(Integer purchaseOrderId, String supplierId, LocalDateTime orderDate, BigDecimal totalAmount) {
        this.purchaseOrderId = purchaseOrderId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }
}
