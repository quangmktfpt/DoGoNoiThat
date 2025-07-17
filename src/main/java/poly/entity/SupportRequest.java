package poly.entity;

import java.sql.Timestamp;

public class SupportRequest {
    private int id;
    private String subject;
    private String content;
    private Timestamp createdDate;
    private String status;

    public SupportRequest() {}

    public SupportRequest(int id, String subject, String content, Timestamp createdDate, String status) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.createdDate = createdDate;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 