package poly.entity;

import java.time.LocalDateTime;

/**
 * Entity class cho ChatMessage
 * Quản lý tin nhắn trong phiên chat
 */
public class ChatMessage {
    private Integer messageId;
    private Integer sessionId;
    private String senderType; // customer, agent, bot
    private Integer senderId;
    private String messageText;
    private String messageType; // text, image, file, system
    private String filePath;
    private LocalDateTime sentAt;
    private Boolean isRead;
    
    // Thông tin bổ sung (không lưu trong DB)
    private String senderName;
    private String senderAvatar;
    
    public ChatMessage() {
    }
    
    public ChatMessage(Integer sessionId, String senderType, Integer senderId, String messageText) {
        this.sessionId = sessionId;
        this.senderType = senderType;
        this.senderId = senderId;
        this.messageText = messageText;
        this.messageType = "text";
        this.isRead = false;
    }
    
    // Getters and Setters
    public Integer getMessageId() {
        return messageId;
    }
    
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
    
    public Integer getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getSenderType() {
        return senderType;
    }
    
    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }
    
    public Integer getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public LocalDateTime getSentAt() {
        return sentAt;
    }
    
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
    
    public Boolean getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public String getSenderAvatar() {
        return senderAvatar;
    }
    
    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }
    
    /**
     * Kiểm tra xem tin nhắn có phải từ khách hàng không
     */
    public boolean isFromCustomer() {
        return "customer".equals(senderType);
    }
    
    /**
     * Kiểm tra xem tin nhắn có phải từ nhân viên không
     */
    public boolean isFromAgent() {
        return "agent".equals(senderType);
    }
    
    /**
     * Kiểm tra xem tin nhắn có phải từ bot không
     */
    public boolean isFromBot() {
        return "bot".equals(senderType);
    }
    
    /**
     * Kiểm tra xem tin nhắn có phải là file không
     */
    public boolean isFileMessage() {
        return "file".equals(messageType);
    }
    
    /**
     * Kiểm tra xem tin nhắn có phải là hình ảnh không
     */
    public boolean isImageMessage() {
        return "image".equals(messageType);
    }
    
    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageId=" + messageId +
                ", sessionId=" + sessionId +
                ", senderType='" + senderType + '\'' +
                ", senderId=" + senderId +
                ", messageText='" + messageText + '\'' +
                ", messageType='" + messageType + '\'' +
                ", sentAt=" + sentAt +
                ", senderName='" + senderName + '\'' +
                '}';
    }
} 