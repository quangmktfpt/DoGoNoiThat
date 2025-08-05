package poly.entity;

import java.time.LocalDateTime;

/**
 * Entity class cho ChatSession
 * Quản lý phiên chat giữa khách hàng và nhân viên hỗ trợ
 */
public class ChatSession {
    private Integer sessionId;
    private Integer userId;
    private Integer agentId;
    private String status; // waiting, active, closed, transferred
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime closedAt;
    private Integer customerRating;
    private String customerComment;
    
    // Thông tin bổ sung (không lưu trong DB)
    private String customerName;
    private String agentName;
    private Integer unreadCount;
    
    public ChatSession() {
    }
    
    public ChatSession(Integer sessionId, Integer userId, Integer agentId, String status) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.agentId = agentId;
        this.status = status;
    }
    
    // Getters and Setters
    public Integer getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getAgentId() {
        return agentId;
    }
    
    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalDateTime getClosedAt() {
        return closedAt;
    }
    
    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
    
    public Integer getCustomerRating() {
        return customerRating;
    }
    
    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }
    
    public String getCustomerComment() {
        return customerComment;
    }
    
    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    
    public Integer getUnreadCount() {
        return unreadCount;
    }
    
    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }
    
    @Override
    public String toString() {
        return "ChatSession{" +
                "sessionId=" + sessionId +
                ", userId=" + userId +
                ", agentId=" + agentId +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", customerName='" + customerName + '\'' +
                ", agentName='" + agentName + '\'' +
                '}';
    }
} 