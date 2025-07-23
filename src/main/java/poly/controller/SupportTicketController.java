package poly.controller;

import java.util.List;
import poly.dao.SupportTicketDAO;
import poly.dao.impl.SupportTicketDAOImpl;
import poly.entity.SupportTicket;
import poly.util.CurrentUserUtil;

/**
 * Controller cho SupportTicket
 * Xử lý logic nghiệp vụ liên quan đến hỗ trợ khách hàng
 */
public class SupportTicketController {
    
    private final SupportTicketDAO supportTicketDAO;
    
    public SupportTicketController() {
        this.supportTicketDAO = new SupportTicketDAOImpl();
    }
    
    /**
     * Tạo ticket hỗ trợ mới từ khách hàng
     * @param subject Chủ đề yêu cầu
     * @param content Nội dung yêu cầu
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createSupportTicket(String subject, String content) {
        try {
            // Lấy userID của user hiện tại
            Integer currentUserID = CurrentUserUtil.getCurrentUserId();
            if (currentUserID == null) {
                return false; // Chưa đăng nhập
            }
            
            // Tạo ticket mới
            SupportTicket ticket = new SupportTicket(currentUserID, subject, content);
            return supportTicketDAO.insert(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy danh sách tickets của user hiện tại
     * @return List<SupportTicket> danh sách tickets
     */
    public List<SupportTicket> getCurrentUserTickets() {
        try {
            Integer currentUserID = CurrentUserUtil.getCurrentUserId();
            if (currentUserID == null) {
                return null;
            }
            return supportTicketDAO.findByUserID(currentUserID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy ticket theo ID
     * @param ticketID ID của ticket
     * @return SupportTicket nếu tìm thấy, null nếu không tìm thấy
     */
    public SupportTicket getTicketById(int ticketID) {
        try {
            return supportTicketDAO.findById(ticketID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Cập nhật trạng thái ticket (cho admin)
     * @param ticketID ID của ticket
     * @param status Trạng thái mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTicketStatus(int ticketID, String status) {
        try {
            Integer currentUserID = CurrentUserUtil.getCurrentUserId();
            if (currentUserID == null) {
                return false;
            }
            return supportTicketDAO.updateStatus(ticketID, status, currentUserID);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Thêm phản hồi của admin cho ticket
     * @param ticketID ID của ticket
     * @param response Nội dung phản hồi
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addAdminResponse(int ticketID, String response) {
        try {
            Integer currentUserID = CurrentUserUtil.getCurrentUserId();
            if (currentUserID == null) {
                return false;
            }
            return supportTicketDAO.addAdminResponse(ticketID, response, currentUserID);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy tất cả tickets (cho admin)
     * @return List<SupportTicket> danh sách tất cả tickets
     */
    public List<SupportTicket> getAllTickets() {
        try {
            return supportTicketDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy tickets theo trạng thái (cho admin)
     * @param status Trạng thái cần lọc
     * @return List<SupportTicket> danh sách tickets theo trạng thái
     */
    public List<SupportTicket> getTicketsByStatus(String status) {
        try {
            return supportTicketDAO.findByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy tickets chưa xử lý (cho admin)
     * @return List<SupportTicket> danh sách tickets pending
     */
    public List<SupportTicket> getPendingTickets() {
        try {
            return supportTicketDAO.findPendingTickets();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Đếm số lượng tickets theo trạng thái
     * @param status Trạng thái cần đếm
     * @return Số lượng tickets
     */
    public int countTicketsByStatus(String status) {
        try {
            return supportTicketDAO.countByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Xóa ticket (cho admin)
     * @param ticketID ID của ticket cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteTicket(int ticketID) {
        try {
            return supportTicketDAO.delete(ticketID);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra xem user hiện tại có quyền xem ticket không
     * @param ticket SupportTicket cần kiểm tra
     * @return true nếu có quyền, false nếu không có quyền
     */
    public boolean canViewTicket(SupportTicket ticket) {
        try {
            Integer currentUserID = CurrentUserUtil.getCurrentUserId();
            if (currentUserID == null) {
                return false;
            }
            
            // Tạm thời cho phép tất cả user xem (có thể thay đổi logic sau)
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra xem user hiện tại có quyền chỉnh sửa ticket không
     * @param ticket SupportTicket cần kiểm tra
     * @return true nếu có quyền, false nếu không có quyền
     */
    public boolean canEditTicket(SupportTicket ticket) {
        try {
            Integer currentUserID = CurrentUserUtil.getCurrentUserId();
            if (currentUserID == null) {
                return false;
            }
            
            // Tạm thời cho phép tất cả user chỉnh sửa tickets của mình
            return ticket.getUserID() == currentUserID && "Pending".equals(ticket.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 