package poly.entity;

import java.math.BigDecimal;

/**
 * Entity để lưu thông tin từng sản phẩm trong đơn hàng
 * @author Nghia
 */
public class OrderRequestItem {
    private Integer itemId;
    private Integer orderId;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String city;
    private String country;
    private String customerName;
    private String address;
    private String phone;
    private String paymentMethod;

    public OrderRequestItem() {
    }

    public OrderRequestItem(Integer itemId, Integer orderId, String productId, String productName, 
                           Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice,
                           String city, String country, String customerName, 
                           String address, String phone, String paymentMethod) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.city = city;
        this.country = country;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Helper methods
    public void calculateTotalPrice() {
        if (quantity != null && unitPrice != null) {
            try {
                this.totalPrice = unitPrice.multiply(new BigDecimal(quantity));
            } catch (Exception e) {
                this.totalPrice = BigDecimal.ZERO;
            }
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (address != null && !address.trim().isEmpty()) {
            sb.append(address);
        }
        if (city != null && !city.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }
        if (country != null && !country.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(country);
        }
        return sb.toString();
    }
} 