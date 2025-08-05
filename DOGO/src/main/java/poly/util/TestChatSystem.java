package poly.util;

import poly.dao.ChatSessionDAO;
import poly.dao.ChatMessageDAO;
import poly.dao.impl.ChatSessionDAOImpl;
import poly.dao.impl.ChatMessageDAOImpl;
import poly.entity.ChatSession;
import poly.entity.ChatMessage;
import poly.entity.User;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Test class cho hệ thống chat
 */
public class TestChatSystem {
    
    public static void main(String[] args) {
        System.out.println("=== TEST CHAT SYSTEM ===");
        
        try {
            ChatSessionDAO chatSessionDAO = new ChatSessionDAOImpl();
            ChatMessageDAO chatMessageDAO = new ChatMessageDAOImpl();
            
            // Test 1: Tạo phiên chat mới
            System.out.println("\n1. Tạo phiên chat mới...");
            Integer sessionId = chatSessionDAO.createSession(1); // User ID = 1
            System.out.println("Phiên chat được tạo với ID: " + sessionId);
            
            // Test 2: Lấy phiên chat
            System.out.println("\n2. Lấy thông tin phiên chat...");
            ChatSession session = chatSessionDAO.selectById(sessionId);
            System.out.println("Phiên chat: " + session);
            
            // Test 3: Gửi tin nhắn từ khách hàng
            System.out.println("\n3. Gửi tin nhắn từ khách hàng...");
            ChatMessage customerMsg = new ChatMessage();
            customerMsg.setSessionId(sessionId);
            customerMsg.setSenderType("customer");
            customerMsg.setSenderId(1);
            customerMsg.setMessageText("Xin chào! Tôi cần hỗ trợ về sản phẩm.");
            customerMsg.setMessageType("text");
            customerMsg.setSentAt(LocalDateTime.now());
            
            Integer messageId = chatMessageDAO.insert(customerMsg);
            System.out.println("Tin nhắn được gửi với ID: " + messageId);
            
            // Test 4: Gán agent cho phiên chat
            System.out.println("\n4. Gán agent cho phiên chat...");
            boolean assigned = chatSessionDAO.assignAgent(sessionId, 2); // Agent ID = 2
            System.out.println("Gán agent thành công: " + assigned);
            
            // Test 5: Gửi tin nhắn từ agent
            System.out.println("\n5. Gửi tin nhắn từ agent...");
            ChatMessage agentMsg = new ChatMessage();
            agentMsg.setSessionId(sessionId);
            agentMsg.setSenderType("agent");
            agentMsg.setSenderId(2);
            agentMsg.setMessageText("Xin chào! Tôi là nhân viên hỗ trợ. Tôi sẽ giúp bạn ngay bây giờ.");
            agentMsg.setMessageType("text");
            agentMsg.setSentAt(LocalDateTime.now());
            
            Integer agentMessageId = chatMessageDAO.insert(agentMsg);
            System.out.println("Tin nhắn agent được gửi với ID: " + agentMessageId);
            
            // Test 6: Lấy tin nhắn của phiên chat
            System.out.println("\n6. Lấy tất cả tin nhắn của phiên chat...");
            List<ChatMessage> messages = chatMessageDAO.selectWithDetails(sessionId);
            System.out.println("Số tin nhắn: " + messages.size());
            for (ChatMessage msg : messages) {
                System.out.println("[" + msg.getSentAt() + "] " + msg.getSenderType() + ": " + msg.getMessageText());
            }
            
            // Test 7: Lấy phiên chat đang chờ
            System.out.println("\n7. Lấy phiên chat đang chờ...");
            List<ChatSession> waitingSessions = chatSessionDAO.selectWaitingSessions();
            System.out.println("Số phiên chat đang chờ: " + waitingSessions.size());
            
            // Test 8: Đóng phiên chat
            System.out.println("\n8. Đóng phiên chat...");
            boolean closed = chatSessionDAO.closeSession(sessionId, 5, "Rất hài lòng với dịch vụ!");
            System.out.println("Đóng phiên chat thành công: " + closed);
            
            System.out.println("\n=== TEST HOÀN THÀNH ===");
            
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 