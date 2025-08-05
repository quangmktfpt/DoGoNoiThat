package poly.entity;

import java.time.LocalDateTime;

/**
 * Entity class cho Notification
 * Quản lý thông báo cho người dùng
 */
public class Notification {
    private Integer notificationId;
    private Integer userId;
    private String type; // email, in_app, chat, order_update, support_response
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private Integer relatedId;
    private String relatedType;
    
    // Thông tin bổ sung (không lưu trong DB)
    private String userName;
    private String userEmail;
    
    public Notification() {
    }
    
    public Notification(Integer userId, String type, String title, String message) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.isRead = false;
    }
    
    // Getters and Setters
    public Integer getNotificationId() {
        return notificationId;
    }
    
    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Boolean getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getReadAt() {
        return readAt;
    }
    
    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
    
    public Integer getRelatedId() {
        return relatedId;
    }
    
    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }
    
    public String getRelatedType() {
        return relatedType;
    }
    
    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    /**
     * Kiểm tra xem thông báo có phải là email không
     */
    public boolean isEmailNotification() {
        return "email".equals(type);
    }
    
    /**
     * Kiểm tra xem thông báo có phải là in-app không
     */
    public boolean isInAppNotification() {
        return "in_app".equals(type);
    }
    
    /**
     * Kiểm tra xem thông báo có phải là chat không
     */
    public boolean isChatNotification() {
        return "chat".equals(type);
    }
    
    /**
     * Kiểm tra xem thông báo có phải là cập nhật đơn hàng không
     */
    public boolean isOrderUpdateNotification() {
        return "order_update".equals(type);
    }
    
    /**
     * Kiểm tra xem thông báo có phải là phản hồi hỗ trợ không
     */
    public boolean isSupportResponseNotification() {
        return "support_response".equals(type);
    }
    
    /**
     * Lấy thời gian đã tạo dưới dạng chuỗi ngắn gọn
     */
    public String getTimeAgo() {
        if (createdAt == null) {
            return "";
        }
        
        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(createdAt, now).getSeconds();
        
        if (seconds < 60) {
            return seconds + " giây trước";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " phút trước";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " giờ trước";
        } else {
            long days = seconds / 86400;
            return days + " ngày trước";
        }
    }
    
    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
} 