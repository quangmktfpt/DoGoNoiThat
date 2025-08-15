package poly.dao.impl;

import poly.dao.ChatMessageDAO;
import poly.entity.ChatMessage;
import poly.util.XJdbc;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation của ChatMessageDAO
 */
public class ChatMessageDAOImpl implements ChatMessageDAO {
    
    @Override
    public Integer insert(ChatMessage message) {
        String sql = "INSERT INTO ChatMessages (SessionID, SenderType, SenderID, MessageText, MessageType, FilePath) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, message.getSessionId());
            ps.setString(2, message.getSenderType());
            ps.setInt(3, message.getSenderId());
            ps.setString(4, message.getMessageText());
            ps.setString(5, message.getMessageType());
            ps.setString(6, message.getFilePath());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public ChatMessage selectById(Integer messageId) {
        String sql = "SELECT * FROM ChatMessages WHERE MessageID = ?";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, messageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChatMessage(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<ChatMessage> selectBySessionId(Integer sessionId) {
        String sql = "SELECT * FROM ChatMessages WHERE SessionID = ? ORDER BY SentAt ASC";
        List<ChatMessage> messages = new ArrayList<>();
        
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    messages.add(mapResultSetToChatMessage(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    @Override
    public List<ChatMessage> selectUnreadBySessionId(Integer sessionId, Integer userId) {
        String sql = "SELECT * FROM ChatMessages WHERE SessionID = ? AND SenderID != ? AND IsRead = 0 ORDER BY SentAt ASC";
        List<ChatMessage> messages = new ArrayList<>();
        
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sessionId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    messages.add(mapResultSetToChatMessage(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    @Override
    public boolean markAsRead(Integer messageId) {
        String sql = "UPDATE ChatMessages SET IsRead = 1 WHERE MessageID = ?";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, messageId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean markAllAsRead(Integer sessionId, Integer userId) {
        String sql = "UPDATE ChatMessages SET IsRead = 1 WHERE SessionID = ? AND SenderID != ? AND IsRead = 0";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sessionId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public ChatMessage getLastMessage(Integer sessionId) {
        String sql = "SELECT TOP 1 * FROM ChatMessages WHERE SessionID = ? ORDER BY SentAt DESC";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChatMessage(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public int getUnreadCount(Integer userId) {
        String sql = "SELECT COUNT(*) FROM ChatMessages cm " +
                    "JOIN ChatSessions cs ON cm.SessionID = cs.SessionID " +
                    "WHERE cs.UserID = ? AND cm.SenderID != ? AND cm.IsRead = 0";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public List<ChatMessage> selectWithDetails(Integer sessionId) {
        String sql = """
            SELECT cm.*, 
                   COALESCE(u.FullName, 
                           CASE 
                               WHEN cm.SenderType = 'agent' THEN 'Nhân viên hỗ trợ'
                               WHEN cm.SenderType = 'customer' THEN 'Khách hàng'
                               WHEN cm.SenderType = 'bot' THEN 'Hệ thống'
                               ELSE 'Người dùng'
                           END) AS SenderName
            FROM ChatMessages cm
            LEFT JOIN Users u ON cm.SenderID = u.UserID
            WHERE cm.SessionID = ?
            ORDER BY cm.SentAt ASC
        """;
        
        List<ChatMessage> messages = new ArrayList<>();
        
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    messages.add(mapResultSetToChatMessageWithDetails(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    @Override
    public boolean delete(Integer messageId) {
        String sql = "DELETE FROM ChatMessages WHERE MessageID = ?";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, messageId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean deleteBySessionId(Integer sessionId) {
        String sql = "DELETE FROM ChatMessages WHERE SessionID = ?";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sessionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet thành ChatMessage object
     */
    private ChatMessage mapResultSetToChatMessage(ResultSet rs) throws SQLException {
        ChatMessage message = new ChatMessage();
        message.setMessageId(rs.getInt("MessageID"));
        message.setSessionId(rs.getInt("SessionID"));
        message.setSenderType(rs.getString("SenderType"));
        message.setSenderId(rs.getInt("SenderID"));
        message.setMessageText(rs.getString("MessageText"));
        message.setMessageType(rs.getString("MessageType"));
        message.setFilePath(rs.getString("FilePath"));
        
        Timestamp sentAt = rs.getTimestamp("SentAt");
        if (sentAt != null) {
            message.setSentAt(sentAt.toLocalDateTime());
        }
        
        message.setIsRead(rs.getBoolean("IsRead"));
        
        return message;
    }
    
    /**
     * Map ResultSet thành ChatMessage object với thông tin chi tiết
     */
    private ChatMessage mapResultSetToChatMessageWithDetails(ResultSet rs) throws SQLException {
        ChatMessage message = mapResultSetToChatMessage(rs);
        
        // Thêm thông tin chi tiết
        message.setSenderName(rs.getString("SenderName"));
        
        return message;
    }
} 