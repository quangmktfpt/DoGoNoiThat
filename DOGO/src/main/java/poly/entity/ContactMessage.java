package poly.entity;

import java.time.LocalDateTime;

/**
 * Entity để lưu thông tin tin nhắn liên hệ từ khách hàng
 * @author Nghia
 */
public class ContactMessage {
    private Integer messageId;
    private Integer userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String subject;
    private String message;
    private LocalDateTime sentDate;
    private Boolean isRead;
    private String adminResponse;
    private LocalDateTime responseDate;
    private Integer adminUserId;

    public ContactMessage() {
        this.sentDate = LocalDateTime.now();
        this.isRead = false;
    }

    public ContactMessage(Integer messageId, Integer userId, String customerName, 
                        String customerEmail, String customerPhone, String subject, 
                        String message, LocalDateTime sentDate, Boolean isRead, 
                        String adminResponse, LocalDateTime responseDate, Integer adminUserId) {
        this.messageId = messageId;
        this.userId = userId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.subject = subject;
        this.message = message;
        this.sentDate = sentDate;
        this.isRead = isRead;
        this.adminResponse = adminResponse;
        this.responseDate = responseDate;
        this.adminUserId = adminUserId;
    }

    // Getters and Setters
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public Integer getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Integer adminUserId) {
        this.adminUserId = adminUserId;
    }
} 