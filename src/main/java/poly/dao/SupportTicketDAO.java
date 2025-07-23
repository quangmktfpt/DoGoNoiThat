package poly.dao;

import java.util.List;
import poly.entity.SupportTicket;

/**
 * Interface DAO cho SupportTicket
 * Định nghĩa các phương thức thao tác với bảng SupportTickets
 */
public interface SupportTicketDAO {
    
    /**
     * Thêm một ticket hỗ trợ mới
     * @param ticket SupportTicket cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean insert(SupportTicket ticket);
    
    /**
     * Cập nhật thông tin ticket
     * @param ticket SupportTicket cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean update(SupportTicket ticket);
    
    /**
     * Xóa ticket theo ID
     * @param ticketID ID của ticket cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean delete(int ticketID);
    
    /**
     * Tìm ticket theo ID
     * @param ticketID ID của ticket cần tìm
     * @return SupportTicket nếu tìm thấy, null nếu không tìm thấy
     */
    SupportTicket findById(int ticketID);
    
    /**
     * Lấy tất cả tickets
     * @return List<SupportTicket> danh sách tất cả tickets
     */
    List<SupportTicket> findAll();
    
    /**
     * Lấy tickets theo trạng thái
     * @param status Trạng thái cần lọc (Pending, InProgress, Resolved, Closed)
     * @return List<SupportTicket> danh sách tickets theo trạng thái
     */
    List<SupportTicket> findByStatus(String status);
    
    /**
     * Lấy tickets theo userID
     * @param userID ID của user
     * @return List<SupportTicket> danh sách tickets của user
     */
    List<SupportTicket> findByUserID(int userID);
    
    /**
     * Lấy tickets chưa được xử lý (Pending)
     * @return List<SupportTicket> danh sách tickets pending
     */
    List<SupportTicket> findPendingTickets();
    
    /**
     * Cập nhật trạng thái ticket
     * @param ticketID ID của ticket
     * @param status Trạng thái mới
     * @param adminID ID của admin xử lý
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateStatus(int ticketID, String status, int adminID);
    
    /**
     * Thêm phản hồi của admin cho ticket
     * @param ticketID ID của ticket
     * @param response Nội dung phản hồi
     * @param adminID ID của admin
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean addAdminResponse(int ticketID, String response, int adminID);
    
    /**
     * Đếm số lượng tickets theo trạng thái
     * @param status Trạng thái cần đếm
     * @return Số lượng tickets
     */
    int countByStatus(String status);
} 