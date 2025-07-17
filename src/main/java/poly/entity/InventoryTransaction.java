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
public class InventoryTransaction {
        private Integer transactionId;
    private String productId;
    private LocalDateTime transactionDate;
    private String transactionType; // PurchaseIn, SaleOut, Adjustment
    private Integer quantityChange;
    private String referenceId;
    private String notes;

    private Integer userId;

    // getters and setters

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

<<<<<<< HEAD
    public InventoryTransaction(Integer transactionId, String productId, LocalDateTime transactionDate, String transactionType, Integer quantityChange, String referenceId, String notes) {
=======
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public InventoryTransaction(Integer transactionId, String productId, LocalDateTime transactionDate, String transactionType, Integer quantityChange, String referenceId, String notes, Integer userId) {
>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
        this.transactionId = transactionId;
        this.productId = productId;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.quantityChange = quantityChange;
        this.referenceId = referenceId;
        this.notes = notes;
<<<<<<< HEAD
=======
        this.userId = userId;
>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
    }

    public InventoryTransaction() {
    }
    
}
