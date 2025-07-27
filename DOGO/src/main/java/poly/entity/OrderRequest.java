package poly.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity để lưu thông tin đặt hàng từ giao diện DatHang
 * @author Nghia
 */
public class OrderRequest {
    private Integer orderId;
    private Integer userId;
    private String customerName;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String paymentMethod;
    private String couponId;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Integer deliveryAddressId;
    private List<OrderRequestItem> items;

    public OrderRequest() {
        this.orderDate = LocalDateTime.now();
        this.orderStatus = "Pending";
    }

    public OrderRequest(Integer orderId, Integer userId, String customerName, String phone, 
                       String address, String city, String country, String paymentMethod, 
                       String couponId, BigDecimal subtotal, BigDecimal shippingFee, 
                       BigDecimal discount, BigDecimal totalAmount, LocalDateTime orderDate, 
                       String orderStatus, Integer deliveryAddressId, List<OrderRequestItem> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.paymentMethod = paymentMethod;
        this.couponId = couponId;
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.deliveryAddressId = deliveryAddressId;
        this.items = items;
    }

    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Integer deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public List<OrderRequestItem> getItems() {
        return items;
    }

    public void setItems(List<OrderRequestItem> items) {
        this.items = items;
    }

    // Helper methods
    public void calculateTotal() {
        if (subtotal != null) {
            BigDecimal total = subtotal;
            if (shippingFee != null) {
                total = total.add(shippingFee);
            }
            if (discount != null) {
                total = total.subtract(discount);
            }
            this.totalAmount = total;
        } else {
            this.totalAmount = BigDecimal.ZERO;
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