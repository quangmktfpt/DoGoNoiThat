package poly.dao.impl;

import poly.dao.ChatSessionDAO;
import poly.entity.ChatSession;
import poly.util.XJdbc;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation của ChatSessionDAO
 */
public class ChatSessionDAOImpl implements ChatSessionDAO {
    
    @Override
    public Integer createSession(Integer userId) {
        String sql = "INSERT INTO ChatSessions (UserID, Status) VALUES (?, 'waiting')";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, userId);
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
    public ChatSession selectById(Integer sessionId) {
        String sql = "SELECT * FROM ChatSessions WHERE SessionID = ?";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChatSession(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public ChatSession getActiveSessionByUserId(Integer userId) {
        String sql = "SELECT * FROM ChatSessions WHERE UserID = ? AND Status IN ('waiting', 'active') ORDER BY CreatedAt DESC";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChatSession(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<ChatSession> selectByUserId(Integer userId) {
        String sql = "SELECT * FROM ChatSessions WHERE UserID = ? ORDER BY CreatedAt DESC";
        List<ChatSession> sessions = new ArrayList<>();
        
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToChatSession(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
    
    @Override
    public List<ChatSession> selectWaitingSessions() {
        String sql = """
            SELECT cs.*, 
                   u1.FullName AS CustomerName,
                   u2.FullName AS AgentName
            FROM ChatSessions cs
            LEFT JOIN Users u1 ON cs.UserID = u1.UserID
            LEFT JOIN Users u2 ON cs.AgentID = u2.UserID
            WHERE cs.Status = 'waiting' 
            ORDER BY cs.CreatedAt ASC
        """;
        List<ChatSession> sessions = new ArrayList<>();
        
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                sessions.add(mapResultSetToChatSessionWithDetails(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
    
    @Override
    public List<ChatSession> selectActiveSessionsByAgentId(Integer agentId) {
        String sql = """
            SELECT cs.*, 
                   u1.FullName AS CustomerName,
                   u2.FullName AS AgentName
            FROM ChatSessions cs
            LEFT JOIN Users u1 ON cs.UserID = u1.UserID
            LEFT JOIN Users u2 ON cs.AgentID = u2.UserID
            WHERE cs.AgentID = ? AND cs.Status = 'active' 
            ORDER BY cs.StartedAt ASC
        """;
        List<ChatSession> sessions = new ArrayList<>();
        
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, agentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToChatSessionWithDetails(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
    
    @Override
    public boolean updateStatus(Integer sessionId, String status) {
        String sql = "UPDATE ChatSessions SET Status = ? WHERE SessionID = ?";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, sessionId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean assignAgent(Integer sessionId, Integer agentId) {
        String sql = "UPDATE ChatSessions SET AgentID = ?, Status = 'active', StartedAt = SYSDATETIME() WHERE SessionID = ?";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, agentId);
            ps.setInt(2, sessionId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean closeSession(Integer sessionId, Integer rating, String comment) {
        String sql = "UPDATE ChatSessions SET Status = 'closed', ClosedAt = SYSDATETIME(), CustomerRating = ?, CustomerComment = ? WHERE SessionID = ?";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setObject(1, rating);
            ps.setString(2, comment);
            ps.setInt(3, sessionId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public int getWaitingSessionCount() {
        String sql = "SELECT COUNT(*) FROM ChatSessions WHERE Status = 'waiting'";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public int getActiveSessionCountByAgent(Integer agentId) {
        String sql = "SELECT COUNT(*) FROM ChatSessions WHERE AgentID = ? AND Status = 'active'";
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, agentId);
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
    public ChatSession selectWithDetails(Integer sessionId) {
        String sql = """
            SELECT cs.*, 
                   u1.FullName AS CustomerName,
                   u2.FullName AS AgentName
            FROM ChatSessions cs
            LEFT JOIN Users u1 ON cs.UserID = u1.UserID
            LEFT JOIN Users u2 ON cs.AgentID = u2.UserID
            WHERE cs.SessionID = ?
        """;
        
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChatSessionWithDetails(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<ChatSession> selectWithDetails(Integer userId, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cs.*, ");
        sql.append("u1.FullName AS CustomerName, ");
        sql.append("u2.FullName AS AgentName ");
        sql.append("FROM ChatSessions cs ");
        sql.append("LEFT JOIN Users u1 ON cs.UserID = u1.UserID ");
        sql.append("LEFT JOIN Users u2 ON cs.AgentID = u2.UserID ");
        sql.append("WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        
        if (userId != null) {
            sql.append("AND cs.UserID = ? ");
            params.add(userId);
        }
        
        if (status != null) {
            sql.append("AND cs.Status = ? ");
            params.add(status);
        }
        
        sql.append("ORDER BY cs.CreatedAt DESC");
        
        List<ChatSession> sessions = new ArrayList<>();
        
        try (Connection conn = XJdbc.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToChatSessionWithDetails(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
    
    /**
     * Map ResultSet thành ChatSession object
     */
    private ChatSession mapResultSetToChatSession(ResultSet rs) throws SQLException {
        ChatSession session = new ChatSession();
        session.setSessionId(rs.getInt("SessionID"));
        session.setUserId(rs.getInt("UserID"));
        
        Integer agentId = rs.getObject("AgentID", Integer.class);
        session.setAgentId(agentId);
        
        session.setStatus(rs.getString("Status"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            session.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp startedAt = rs.getTimestamp("StartedAt");
        if (startedAt != null) {
            session.setStartedAt(startedAt.toLocalDateTime());
        }
        
        Timestamp closedAt = rs.getTimestamp("ClosedAt");
        if (closedAt != null) {
            session.setClosedAt(closedAt.toLocalDateTime());
        }
        
        Integer rating = rs.getObject("CustomerRating", Integer.class);
        session.setCustomerRating(rating);
        
        session.setCustomerComment(rs.getString("CustomerComment"));
        
        return session;
    }
    
    /**
     * Map ResultSet thành ChatSession object với thông tin chi tiết
     */
    private ChatSession mapResultSetToChatSessionWithDetails(ResultSet rs) throws SQLException {
        ChatSession session = mapResultSetToChatSession(rs);
        
        // Thêm thông tin chi tiết
        session.setCustomerName(rs.getString("CustomerName"));
        session.setAgentName(rs.getString("AgentName"));
        
        return session;
    }
} 