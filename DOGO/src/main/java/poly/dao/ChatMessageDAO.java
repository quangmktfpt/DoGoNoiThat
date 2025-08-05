package poly.dao;

import poly.entity.ChatMessage;
import java.util.List;

/**
 * DAO interface cho ChatMessage
 * Quản lý các thao tác CRUD với bảng ChatMessages
 */
public interface ChatMessageDAO {
    
    /**
     * Thêm tin nhắn mới
     * @param message ChatMessage object
     * @return ID của tin nhắn mới được tạo
     */
    Integer insert(ChatMessage message);
    
    /**
     * Lấy tin nhắn theo ID
     * @param messageId ID của tin nhắn
     * @return ChatMessage object
     */
    ChatMessage selectById(Integer messageId);
    
    /**
     * Lấy tất cả tin nhắn của một phiên chat
     * @param sessionId ID của phiên chat
     * @return List các ChatMessage
     */
    List<ChatMessage> selectBySessionId(Integer sessionId);
    
    /**
     * Lấy tin nhắn chưa đọc của một phiên chat
     * @param sessionId ID của phiên chat
     * @param userId ID của người dùng (để lọc tin nhắn không phải của họ)
     * @return List các ChatMessage chưa đọc
     */
    List<ChatMessage> selectUnreadBySessionId(Integer sessionId, Integer userId);
    
    /**
     * Đánh dấu tin nhắn đã đọc
     * @param messageId ID của tin nhắn
     * @return true nếu cập nhật thành công
     */
    boolean markAsRead(Integer messageId);
    
    /**
     * Đánh dấu tất cả tin nhắn của phiên chat đã đọc
     * @param sessionId ID của phiên chat
     * @param userId ID của người dùng
     * @return true nếu cập nhật thành công
     */
    boolean markAllAsRead(Integer sessionId, Integer userId);
    
    /**
     * Lấy tin nhắn cuối cùng của phiên chat
     * @param sessionId ID của phiên chat
     * @return ChatMessage object hoặc null
     */
    ChatMessage getLastMessage(Integer sessionId);
    
    /**
     * Lấy số lượng tin nhắn chưa đọc của một người dùng
     * @param userId ID của người dùng
     * @return Số lượng tin nhắn chưa đọc
     */
    int getUnreadCount(Integer userId);
    
    /**
     * Lấy tin nhắn với thông tin chi tiết (bao gồm tên người gửi)
     * @param sessionId ID của phiên chat
     * @return List các ChatMessage với thông tin đầy đủ
     */
    List<ChatMessage> selectWithDetails(Integer sessionId);
    
    /**
     * Xóa tin nhắn
     * @param messageId ID của tin nhắn
     * @return true nếu xóa thành công
     */
    boolean delete(Integer messageId);
    
    /**
     * Xóa tất cả tin nhắn của một phiên chat
     * @param sessionId ID của phiên chat
     * @return true nếu xóa thành công
     */
    boolean deleteBySessionId(Integer sessionId);
} 