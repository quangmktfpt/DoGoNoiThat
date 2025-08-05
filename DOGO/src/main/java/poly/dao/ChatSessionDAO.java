package poly.dao;

import poly.entity.ChatSession;
import java.util.List;

/**
 * DAO interface cho ChatSession
 * Quản lý các thao tác CRUD với bảng ChatSessions
 */
public interface ChatSessionDAO {
    
    /**
     * Tạo phiên chat mới
     * @param userId ID của khách hàng
     * @return ID của phiên chat mới được tạo
     */
    Integer createSession(Integer userId);
    
    /**
     * Lấy phiên chat theo ID
     * @param sessionId ID của phiên chat
     * @return ChatSession object
     */
    ChatSession selectById(Integer sessionId);
    
    /**
     * Lấy phiên chat đang hoạt động của khách hàng
     * @param userId ID của khách hàng
     * @return ChatSession object hoặc null nếu không có
     */
    ChatSession getActiveSessionByUserId(Integer userId);
    
    /**
     * Lấy tất cả phiên chat của khách hàng
     * @param userId ID của khách hàng
     * @return List các ChatSession
     */
    List<ChatSession> selectByUserId(Integer userId);
    
    /**
     * Lấy tất cả phiên chat đang chờ xử lý
     * @return List các ChatSession có status = 'waiting'
     */
    List<ChatSession> selectWaitingSessions();
    
    /**
     * Lấy tất cả phiên chat đang hoạt động của nhân viên
     * @param agentId ID của nhân viên
     * @return List các ChatSession
     */
    List<ChatSession> selectActiveSessionsByAgentId(Integer agentId);
    
    /**
     * Cập nhật trạng thái phiên chat
     * @param sessionId ID của phiên chat
     * @param status Trạng thái mới
     * @return true nếu cập nhật thành công
     */
    boolean updateStatus(Integer sessionId, String status);
    
    /**
     * Gán nhân viên cho phiên chat
     * @param sessionId ID của phiên chat
     * @param agentId ID của nhân viên
     * @return true nếu cập nhật thành công
     */
    boolean assignAgent(Integer sessionId, Integer agentId);
    
    /**
     * Đóng phiên chat
     * @param sessionId ID của phiên chat
     * @param rating Đánh giá của khách hàng (1-5)
     * @param comment Bình luận của khách hàng
     * @return true nếu đóng thành công
     */
    boolean closeSession(Integer sessionId, Integer rating, String comment);
    
    /**
     * Lấy số lượng phiên chat đang chờ
     * @return Số lượng phiên chat có status = 'waiting'
     */
    int getWaitingSessionCount();
    
    /**
     * Lấy số lượng phiên chat đang hoạt động của nhân viên
     * @param agentId ID của nhân viên
     * @return Số lượng phiên chat
     */
    int getActiveSessionCountByAgent(Integer agentId);
    
    /**
     * Lấy phiên chat với thông tin chi tiết (bao gồm tên khách hàng, nhân viên)
     * @param sessionId ID của phiên chat
     * @return ChatSession với thông tin đầy đủ
     */
    ChatSession selectWithDetails(Integer sessionId);
    
    /**
     * Lấy danh sách phiên chat với thông tin chi tiết
     * @param userId ID của khách hàng (có thể null để lấy tất cả)
     * @param status Trạng thái (có thể null để lấy tất cả)
     * @return List các ChatSession với thông tin đầy đủ
     */
    List<ChatSession> selectWithDetails(Integer userId, String status);
} 