package poly.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import poly.dao.SupportTicketDAO;
import poly.entity.SupportTicket;
import poly.util.XJdbc;

/**
 * Implementation của SupportTicketDAO
 * Thực hiện các thao tác với bảng SupportTickets
 */
public class SupportTicketDAOImpl implements SupportTicketDAO {
    
    @Override
    public boolean insert(SupportTicket ticket) {
        String sql = "INSERT INTO SupportTickets (UserID, Subject, Content, Status, CreatedDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ticket.getUserID());
            ps.setString(2, ticket.getSubject());
            ps.setString(3, ticket.getContent());
            ps.setString(4, ticket.getStatus());
            ps.setTimestamp(5, new Timestamp(ticket.getCreatedDate().getTime()));
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(SupportTicket ticket) {
        String sql = "UPDATE SupportTickets SET Subject=?, Content=?, Status=?, ResolvedDate=?, AdminResponse=?, AdminID=? WHERE TicketID=?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ticket.getSubject());
            ps.setString(2, ticket.getContent());
            ps.setString(3, ticket.getStatus());
            
            if (ticket.getResolvedDate() != null) {
                ps.setTimestamp(4, new Timestamp(ticket.getResolvedDate().getTime()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            
            ps.setString(5, ticket.getAdminResponse());
            ps.setInt(6, ticket.getAdminID());
            ps.setInt(7, ticket.getTicketID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int ticketID) {
        String sql = "DELETE FROM SupportTickets WHERE TicketID=?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ticketID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public SupportTicket findById(int ticketID) {
        String sql = "SELECT * FROM SupportTickets WHERE TicketID=?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ticketID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSupportTicket(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<SupportTicket> findAll() {
        String sql = "SELECT * FROM SupportTickets ORDER BY CreatedDate DESC";
        List<SupportTicket> tickets = new ArrayList<>();
        
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                tickets.add(mapResultSetToSupportTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    @Override
    public List<SupportTicket> findByStatus(String status) {
        String sql = "SELECT * FROM SupportTickets WHERE Status=? ORDER BY CreatedDate DESC";
        List<SupportTicket> tickets = new ArrayList<>();
        
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                tickets.add(mapResultSetToSupportTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    @Override
    public List<SupportTicket> findByUserID(int userID) {
        String sql = "SELECT * FROM SupportTickets WHERE UserID=? ORDER BY CreatedDate DESC";
        List<SupportTicket> tickets = new ArrayList<>();
        
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                tickets.add(mapResultSetToSupportTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    @Override
    public List<SupportTicket> findPendingTickets() {
        return findByStatus("Pending");
    }
    
    @Override
    public boolean updateStatus(int ticketID, String status, int adminID) {
        String sql = "UPDATE SupportTickets SET Status=?, AdminID=?, ResolvedDate=? WHERE TicketID=?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, adminID);
            
            if ("Resolved".equals(status) || "Closed".equals(status)) {
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            
            ps.setInt(4, ticketID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean addAdminResponse(int ticketID, String response, int adminID) {
        String sql = "UPDATE SupportTickets SET AdminResponse=?, AdminID=?, Status='Resolved', ResolvedDate=? WHERE TicketID=?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, response);
            ps.setInt(2, adminID);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setInt(4, ticketID);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM SupportTickets WHERE Status=?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Map ResultSet thành đối tượng SupportTicket
     */
    private SupportTicket mapResultSetToSupportTicket(ResultSet rs) throws SQLException {
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketID(rs.getInt("TicketID"));
        ticket.setUserID(rs.getInt("UserID"));
        ticket.setSubject(rs.getString("Subject"));
        ticket.setContent(rs.getString("Content"));
        ticket.setStatus(rs.getString("Status"));
        ticket.setCreatedDate(rs.getTimestamp("CreatedDate"));
        ticket.setResolvedDate(rs.getTimestamp("ResolvedDate"));
        ticket.setAdminResponse(rs.getString("AdminResponse"));
        ticket.setAdminID(rs.getInt("AdminID"));
        return ticket;
    }
} 