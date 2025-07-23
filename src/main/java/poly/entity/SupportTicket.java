package poly.entity;

import java.util.Date;

/**
 * Entity cho bảng SupportTickets
 * Lưu trữ các yêu cầu hỗ trợ từ khách hàng
 */
public class SupportTicket {
    private int ticketID;
    private int userID;
    private String subject;
    private String content;
    private String status; // Pending, InProgress, Resolved, Closed
    private Date createdDate;
    private Date resolvedDate;
    private String adminResponse;
    private int adminID; // ID của admin xử lý ticket
    
    // Constructor
    public SupportTicket() {
    }
    
    public SupportTicket(int userID, String subject, String content) {
        this.userID = userID;
        this.subject = subject;
        this.content = content;
        this.status = "Pending";
        this.createdDate = new Date();
    }
    
    // Getters and Setters
    public int getTicketID() {
        return ticketID;
    }
    
    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }
    
    public int getUserID() {
        return userID;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getResolvedDate() {
        return resolvedDate;
    }
    
    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
    
    public String getAdminResponse() {
        return adminResponse;
    }
    
    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }
    
    public int getAdminID() {
        return adminID;
    }
    
    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }
    
    @Override
    public String toString() {
        return "SupportTicket{" + "ticketID=" + ticketID + ", userID=" + userID + 
               ", subject=" + subject + ", status=" + status + ", createdDate=" + createdDate + '}';
    }
} 