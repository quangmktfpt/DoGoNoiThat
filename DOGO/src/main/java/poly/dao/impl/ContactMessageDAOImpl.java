package poly.dao.impl;

import poly.dao.ContactMessageDAO;
import poly.entity.ContactMessage;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Implementation của ContactMessageDAO
 * @author Nghia
 */
public class ContactMessageDAOImpl implements ContactMessageDAO {
    
    private final String INSERT_SQL = 
        "INSERT INTO ContactMessages (UserID, CustomerName, CustomerEmail, CustomerPhone, Subject, Message, SentDate, IsRead) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private final String UPDATE_SQL = 
        "UPDATE ContactMessages SET UserID=?, CustomerName=?, CustomerEmail=?, CustomerPhone=?, Subject=?, Message=?, SentDate=?, IsRead=?, AdminResponse=?, ResponseDate=?, AdminUserID=? WHERE MessageID=?";
    
    private final String DELETE_SQL = 
        "DELETE FROM ContactMessages WHERE MessageID=?";
    
    private final String SELECT_ALL_SQL = 
        "SELECT * FROM ContactMessages ORDER BY SentDate DESC";
    
    private final String SELECT_BY_ID_SQL = 
        "SELECT * FROM ContactMessages WHERE MessageID=?";
    
    private final String SELECT_BY_USER_SQL = 
        "SELECT * FROM ContactMessages WHERE UserID=? ORDER BY SentDate DESC";
    
    private final String SELECT_UNREAD_SQL = 
        "SELECT * FROM ContactMessages WHERE IsRead=0 ORDER BY SentDate DESC";
    
    private final String SELECT_READ_SQL = 
        "SELECT * FROM ContactMessages WHERE IsRead=1 ORDER BY SentDate DESC";
    
    private final String MARK_AS_READ_SQL = 
        "UPDATE ContactMessages SET IsRead=1 WHERE MessageID=?";
    
    private final String MARK_ALL_AS_READ_SQL = 
        "UPDATE ContactMessages SET IsRead=1 WHERE IsRead=0";
    
    private final String RESPOND_SQL = 
        "UPDATE ContactMessages SET AdminResponse=?, ResponseDate=?, AdminUserID=? WHERE MessageID=?";
    
    private final String COUNT_UNREAD_SQL = 
        "SELECT COUNT(*) FROM ContactMessages WHERE IsRead=0";
    
    private final String SEARCH_SQL = 
        "SELECT * FROM ContactMessages WHERE CustomerName LIKE ? OR CustomerEmail LIKE ? OR Subject LIKE ? OR Message LIKE ? ORDER BY SentDate DESC";

    @Override
    public void insert(ContactMessage message) {
        XJdbc.executeUpdate(INSERT_SQL,
            message.getUserId(),
            message.getCustomerName(),
            message.getCustomerEmail(),
            message.getCustomerPhone(),
            message.getSubject(),
            message.getMessage(),
            message.getSentDate(),
            message.getIsRead()
        );
    }

    @Override
    public void update(ContactMessage message) {
        XJdbc.executeUpdate(UPDATE_SQL,
            message.getUserId(),
            message.getCustomerName(),
            message.getCustomerEmail(),
            message.getCustomerPhone(),
            message.getSubject(),
            message.getMessage(),
            message.getSentDate(),
            message.getIsRead(),
            message.getAdminResponse(),
            message.getResponseDate(),
            message.getAdminUserId(),
            message.getMessageId()
        );
    }

    @Override
    public void delete(Integer messageId) {
        XJdbc.executeUpdate(DELETE_SQL, messageId);
    }

    @Override
    public List<ContactMessage> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public ContactMessage selectById(Integer messageId) {
        List<ContactMessage> list = selectBySql(SELECT_BY_ID_SQL, messageId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<ContactMessage> selectByUserId(Integer userId) {
        return selectBySql(SELECT_BY_USER_SQL, userId);
    }

    @Override
    public List<ContactMessage> selectUnreadMessages() {
        return selectBySql(SELECT_UNREAD_SQL);
    }

    @Override
    public List<ContactMessage> selectReadMessages() {
        return selectBySql(SELECT_READ_SQL);
    }

    @Override
    public void markAsRead(Integer messageId) {
        XJdbc.executeUpdate(MARK_AS_READ_SQL, messageId);
    }

    @Override
    public void markAllAsRead() {
        XJdbc.executeUpdate(MARK_ALL_AS_READ_SQL);
    }

    @Override
    public void respondToMessage(Integer messageId, String response, Integer adminUserId) {
        XJdbc.executeUpdate(RESPOND_SQL, response, LocalDateTime.now(), adminUserId, messageId);
    }

    @Override
    public int countUnreadMessages() {
        try (ResultSet rs = XJdbc.executeQuery(COUNT_UNREAD_SQL)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<ContactMessage> searchMessages(String keyword) {
        String searchPattern = "%" + keyword + "%";
        return selectBySql(SEARCH_SQL, searchPattern, searchPattern, searchPattern, searchPattern);
    }

    private List<ContactMessage> selectBySql(String sql, Object... args) {
        List<ContactMessage> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql, args)) {
            while (rs.next()) {
                ContactMessage message = new ContactMessage();
                message.setMessageId(rs.getInt("MessageID"));
                message.setUserId(rs.getInt("UserID"));
                message.setCustomerName(rs.getString("CustomerName"));
                message.setCustomerEmail(rs.getString("CustomerEmail"));
                message.setCustomerPhone(rs.getString("CustomerPhone"));
                message.setSubject(rs.getString("Subject"));
                message.setMessage(rs.getString("Message"));
                message.setSentDate(rs.getTimestamp("SentDate").toLocalDateTime());
                message.setIsRead(rs.getBoolean("IsRead"));
                message.setAdminResponse(rs.getString("AdminResponse"));
                if (rs.getTimestamp("ResponseDate") != null) {
                    message.setResponseDate(rs.getTimestamp("ResponseDate").toLocalDateTime());
                }
                if (rs.getObject("AdminUserID") != null) {
                    message.setAdminUserId(rs.getInt("AdminUserID"));
                }
                list.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
} 