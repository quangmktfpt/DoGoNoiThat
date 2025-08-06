package poly.ui;

import poly.dao.ChatMessageDAO;
import poly.dao.ChatSessionDAO;
import poly.dao.impl.ChatMessageDAOImpl;
import poly.dao.impl.ChatSessionDAOImpl;
import poly.entity.ChatMessage;
import poly.entity.ChatSession;
import poly.entity.User;
import poly.util.CurrentUserUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CustomerChatWindowJDialog - Cửa sổ chat hỗ trợ trực tuyến cho khách hàng
 */
public class CustomerChatWindowJDialog extends javax.swing.JDialog {

    private ChatSessionDAO chatSessionDAO;
    private ChatMessageDAO chatMessageDAO;
    private ChatSession currentSession;
    private User currentUser;

    // UI Components
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton closeButton;
    private JLabel statusLabel;
    private JLabel agentLabel;

    /**
     * Creates new form CustomerChatWindow
     */
    public CustomerChatWindowJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        // Khởi tạo DAOs
        chatSessionDAO = new ChatSessionDAOImpl();
        chatMessageDAO = new ChatMessageDAOImpl();

        // Lấy user hiện tại
        currentUser = getCurrentUser();

        initComponents();
        initChat();

        setSize(500, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("💬 Chat Hỗ Trợ Trực Tuyến");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        statusLabel = new JLabel("Đang kết nối...");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerPanel.add(statusLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(new Color(248, 249, 250));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setPreferredSize(new Dimension(0, 60));

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.addActionListener(e -> sendMessage());
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Gửi");
        sendButton.setBackground(new Color(0, 123, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sendButton.setPreferredSize(new Dimension(80, 30));
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(5, 15, 5, 15));
        bottomPanel.setPreferredSize(new Dimension(0, 40));

        agentLabel = new JLabel("Nhân viên: Chưa được phân công");
        agentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bottomPanel.add(agentLabel, BorderLayout.WEST);

        closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> closeChat());
        bottomPanel.add(closeButton, BorderLayout.EAST);

        // Tạo panel chứa cả bottom và input
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(bottomPanel, BorderLayout.NORTH);
        southPanel.add(inputPanel, BorderLayout.CENTER);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void initChat() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng đăng nhập để sử dụng chat!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // Kiểm tra phiên chat hiện tại
        currentSession = chatSessionDAO.getActiveSessionByUserId(currentUser.getUserId());

        if (currentSession == null) {
            // Tạo phiên chat mới
            Integer sessionId = chatSessionDAO.createSession(currentUser.getUserId());
            if (sessionId != null) {
                currentSession = chatSessionDAO.selectById(sessionId);
                statusLabel.setText("Đang chờ nhân viên...");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không thể tạo phiên chat!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
        } else {
            // Cập nhật trạng thái
            updateStatus();
        }

        // Load tin nhắn
        loadMessages();

        // Timer để cập nhật tin nhắn mới
        Timer timer = new Timer(3000, e -> checkNewMessages());
        timer.start();
    }

    private void loadMessages() {
        if (currentSession == null) {
            return;
        }

        List<ChatMessage> messages = chatMessageDAO.selectWithDetails(currentSession.getSessionId());
        chatArea.setText("");

        for (ChatMessage message : messages) {
            appendMessage(message);
        }

        // Scroll xuống cuối
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void appendMessage(ChatMessage message) {
        String timeStr = message.getSentAt() != null
                ? message.getSentAt().toLocalTime().toString().substring(0, 5) : "";

        String senderName = message.getSenderName() != null
                ? message.getSenderName() : "Hệ thống";

        String prefix = message.isFromCustomer() ? "👤 "
                : message.isFromAgent() ? "👨‍💼 " : "🤖 ";

        String formattedMessage = String.format("[%s] %s%s: %s\n",
                timeStr, prefix, senderName, message.getMessageText());

        chatArea.append(formattedMessage);
    }

    private void sendMessage() {
        String messageText = messageField.getText().trim();
        if (messageText.isEmpty() || currentSession == null) {
            return;
        }

        try {
            // Tạo tin nhắn mới
            ChatMessage message = new ChatMessage();
            message.setSessionId(currentSession.getSessionId());
            message.setSenderType("customer");
            message.setSenderId(currentUser.getUserId());
            message.setMessageText(messageText);
            message.setMessageType("text");
            message.setSentAt(LocalDateTime.now());

            Integer messageId = chatMessageDAO.insert(message);
            if (messageId != null) {
                message.setMessageId(messageId);
                message.setSenderName(currentUser.getFullName());

                appendMessage(message);
                messageField.setText("");

                // Gửi thông báo cho nhân viên
                notifyAgent();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không thể gửi tin nhắn!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi gửi tin nhắn: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkNewMessages() {
        if (currentSession == null) {
            return;
        }

        // Lấy tin nhắn chưa đọc
        List<ChatMessage> unreadMessages = chatMessageDAO.selectUnreadBySessionId(
                currentSession.getSessionId(),
                currentUser.getUserId()
        );

        for (ChatMessage message : unreadMessages) {
            appendMessage(message);
            chatMessageDAO.markAsRead(message.getMessageId());
        }

        // Cập nhật trạng thái
        updateStatus();
    }

    private void updateStatus() {
        if (currentSession == null) {
            return;
        }

        ChatSession updatedSession = chatSessionDAO.selectWithDetails(currentSession.getSessionId());
        if (updatedSession != null) {
            currentSession = updatedSession;

            switch (currentSession.getStatus()) {
                case "waiting":
                    statusLabel.setText("Đang chờ nhân viên...");
                    agentLabel.setText("Nhân viên: Chưa được phân công");
                    break;
                case "active":
                    statusLabel.setText("Đang chat với nhân viên");
                    if (currentSession.getAgentName() != null) {
                        agentLabel.setText("Nhân viên: " + currentSession.getAgentName());
                    }
                    break;
                case "closed":
                    statusLabel.setText("Phiên chat đã kết thúc");
                    break;
            }
        }
    }

    private void notifyAgent() {
        // TODO: Implement real-time notification
        // Có thể sử dụng WebSocket hoặc polling
        System.out.println("Thông báo cho nhân viên: Có tin nhắn mới từ " + currentUser.getFullName());
    }

    private void closeChat() {
        if (currentSession != null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Bạn có muốn đóng phiên chat này không?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                // Đánh giá phiên chat
                showRatingDialog();
            }
        }
        dispose();
    }

    private void showRatingDialog() {
        String[] options = {"1 ⭐", "2 ⭐⭐", "3 ⭐⭐⭐", "4 ⭐⭐⭐⭐", "5 ⭐⭐⭐⭐⭐"};
        int rating = JOptionPane.showOptionDialog(this,
                "Vui lòng đánh giá chất lượng hỗ trợ:",
                "Đánh giá",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[4]);

        if (rating >= 0) {
            String comment = JOptionPane.showInputDialog(this,
                    "Nhận xét của bạn (có thể để trống):",
                    "Nhận xét");

            chatSessionDAO.closeSession(currentSession.getSessionId(), rating + 1, comment);
        }
    }

    private User getCurrentUser() {
        try {
            Integer userId = CurrentUserUtil.getCurrentUserId();
            if (userId != null) {
                // TODO: Implement UserDAO to get user by ID
                // For now, create a dummy user
                User user = new User();
                user.setUserId(userId);
                user.setFullName("Khách hàng");
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerChatWindowJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerChatWindowJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerChatWindowJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerChatWindowJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CustomerChatWindowJDialog dialog = new CustomerChatWindowJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
}
