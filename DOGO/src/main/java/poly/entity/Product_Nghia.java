/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author quang
 */
public class Product_Nghia {
    private String productId;
    private String categoryId;
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal gianhap;

    public BigDecimal getGianhap() {
        return gianhap;
    }

    public void setGianhap(BigDecimal gianhap) {
        this.gianhap = gianhap;
    }
    private Integer quantity;    // tồn kho hiện tại
    private String imagePath;
    private LocalDateTime createdDate;
    private String description; // mô tả sản phẩm
    private String kichThuoc;   // kích thước sản phẩm

    public Product_Nghia(String productId, String categoryId, String productName, BigDecimal unitPrice, BigDecimal gianhap, Integer quantity, String imagePath, LocalDateTime createdDate, String description, String kichThuoc) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.gianhap = gianhap;
        this.quantity = quantity;
        this.imagePath = imagePath;
        this.createdDate = createdDate;
        this.description = description;
        this.kichThuoc = kichThuoc;
    }

 

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getKichThuoc() {
        return kichThuoc;
    }
    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public Product_Nghia() {
    }
}
