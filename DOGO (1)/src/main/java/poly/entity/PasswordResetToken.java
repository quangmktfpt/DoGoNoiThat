package poly.entity;

import java.util.Date;

public class PasswordResetToken {
    private int tokenID;
    private int userID;
    private String token;
    private Date expiryTime;
    private boolean isUsed;
    private Date createdTime;

    public PasswordResetToken() {}

    public PasswordResetToken(int tokenID, int userID, String token, Date expiryTime, boolean isUsed, Date createdTime) {
        this.tokenID = tokenID;
        this.userID = userID;
        this.token = token;
        this.expiryTime = expiryTime;
        this.isUsed = isUsed;
        this.createdTime = createdTime;
    }

    public int getTokenID() {
        return tokenID;
    }

    public void setTokenID(int tokenID) {
        this.tokenID = tokenID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
} 