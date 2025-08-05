package poly.ui.manager;

import poly.dao.ChatSessionDAO;
import poly.dao.ChatMessageDAO;
import poly.dao.impl.ChatSessionDAOImpl;
import poly.dao.impl.ChatMessageDAOImpl;
import poly.entity.ChatSession;
import poly.entity.ChatMessage;
import poly.entity.User;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Giao diện chat cho nhân viên hỗ trợ
 */
public class AgentChatWindow extends JDialog {
    
    private ChatSessionDAO chatSessionDAO;
    private ChatMessageDAO chatMessageDAO;
    private User currentAgent;
    private ChatSession currentSession;
    
    // UI Components
    private JList<ChatSession> sessionList;
    private DefaultListModel<ChatSession> sessionListModel;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton closeSessionButton;
    private JButton refreshButton;
    private JLabel statusLabel;
    private JLabel customerInfoLabel;
    private Timer messageTimer;
    
    public AgentChatWindow(JFrame parent) {
        super(parent, "Agent Chat Support", false);
        initComponents();
        initData();
        startMessagePolling();
    }
    
    private void initComponents() {
        setSize(1000, 700);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Main layout
        setLayout(new BorderLayout());
        
        // Left panel - Session list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Danh sách phiên chat"));
        
        sessionListModel = new DefaultListModel<>();
        sessionList = new JList<>(sessionListModel);
        sessionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sessionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ChatSession selected = sessionList.getSelectedValue();
                if (selected != null) {
                    loadSession(selected);
                }
            }
        });
        
        JScrollPane sessionScrollPane = new JScrollPane(sessionList);
        leftPanel.add(sessionScrollPane, BorderLayout.CENTER);
        
        // Session control buttons
        JPanel sessionButtonPanel = new JPanel(new FlowLayout());
        refreshButton = new JButton("🔄 Làm mới");
        refreshButton.addActionListener(e -> refreshSessions());
        sessionButtonPanel.add(refreshButton);
        
        leftPanel.add(sessionButtonPanel, BorderLayout.SOUTH);
        
        // Right panel - Chat area
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        headerPanel.setBackground(new Color(240, 240, 240));
        
        statusLabel = new JLabel("Chưa chọn phiên chat");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(statusLabel, BorderLayout.WEST);
        
        customerInfoLabel = new JLabel("");
        customerInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerPanel.add(customerInfoLabel, BorderLayout.EAST);
        
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        rightPanel.add(chatScrollPane, BorderLayout.CENTER);
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        messageField = new JTextField();
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        
        sendButton = new JButton("Gửi");
        sendButton.addActionListener(e -> sendMessage());
        
        closeSessionButton = new JButton("Đóng phiên");
        closeSessionButton.addActionListener(e -> closeCurrentSession());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(sendButton);
        buttonPanel.add(closeSessionButton);
        
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        rightPanel.add(inputPanel, BorderLayout.SOUTH);
        
        // Add panels to main layout
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        
        // Disable chat controls initially
        setChatControlsEnabled(false);
    }
    
    private void initData() {
        chatSessionDAO = new ChatSessionDAOImpl();
        chatMessageDAO = new ChatMessageDAOImpl();
        
        // Tạo agent dummy - KIỂM TRA LẠI
        currentAgent = new User();
        currentAgent.setUserId(1);
        currentAgent.setFullName("Nhân viên hỗ trợ");
        currentAgent.setUsername("support");
        
        System.out.println("Current Agent: " + currentAgent.getFullName()); // Debug
        
        refreshSessions();
    }
    
    private void refreshSessions() {
        sessionListModel.clear();
        
        try {
            // Lấy phiên đang chờ
            List<ChatSession> waitingSessions = chatSessionDAO.selectWaitingSessions();
            System.out.println("Waiting sessions: " + waitingSessions.size()); // Debug
            
            for (ChatSession session : waitingSessions) {
                sessionListModel.addElement(session);
            }
            
            // Lấy phiên đang hoạt động của agent này
            List<ChatSession> activeSessions = chatSessionDAO.selectActiveSessionsByAgentId(currentAgent.getUserId());
            System.out.println("Active sessions: " + activeSessions.size()); // Debug
            
            for (ChatSession session : activeSessions) {
                sessionListModel.addElement(session);
            }
            
            updateStatusLabel();
            
        } catch (Exception e) {
            System.err.println("Error refreshing sessions: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadSession(ChatSession session) {
        try {
            currentSession = session;
            
            // Update UI
            if (session.getStatus().equals("waiting")) {
                statusLabel.setText("🟡 Phiên chờ - " + session.getCustomerName());
                statusLabel.setForeground(new Color(255, 140, 0));
            } else if (session.getStatus().equals("active")) {
                statusLabel.setText("🟢 Phiên đang hoạt động - " + session.getCustomerName());
                statusLabel.setForeground(new Color(0, 128, 0));
            } else {
                statusLabel.setText("🔴 Phiên đã đóng - " + session.getCustomerName());
                statusLabel.setForeground(Color.RED);
            }
            
            customerInfoLabel.setText("Khách hàng: " + session.getCustomerName());
            
            // Load messages
            loadMessages();
            
            // Enable chat controls if session is active
            setChatControlsEnabled(session.getStatus().equals("active"));
            
            // If session is waiting, offer to accept it
            if (session.getStatus().equals("waiting")) {
                int choice = JOptionPane.showConfirmDialog(this,
                    "Bạn có muốn nhận phiên chat này không?",
                    "Nhận phiên chat",
                    JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    acceptSession(session);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải phiên chat: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void acceptSession(ChatSession session) {
        try {
            // Update session status to active and assign agent
            session.setStatus("active");
            session.setAgentId(currentAgent.getUserId());
            session.setStartedAt(LocalDateTime.now());
            
            chatSessionDAO.assignAgent(session.getSessionId(), currentAgent.getUserId());
            
            // Refresh sessions list
            refreshSessions();
            
            // Load the updated session
            loadSession(session);
            
            // Send welcome message
            ChatMessage welcomeMsg = new ChatMessage();
            welcomeMsg.setSessionId(session.getSessionId());
            welcomeMsg.setSenderType("agent");
            welcomeMsg.setSenderId(currentAgent.getUserId());
            welcomeMsg.setMessageText("Xin chào! Tôi là " + currentAgent.getFullName() + ". Tôi sẽ hỗ trợ bạn ngay bây giờ.");
            welcomeMsg.setMessageType("text");
            welcomeMsg.setSentAt(LocalDateTime.now());
            
            chatMessageDAO.insert(welcomeMsg);
            
            // Reload messages to show welcome message
            loadMessages();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi nhận phiên chat: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadMessages() {
        if (currentSession == null) return;
        
        try {
            chatArea.setText("");
            List<ChatMessage> messages = chatMessageDAO.selectWithDetails(currentSession.getSessionId());
            
            for (ChatMessage message : messages) {
                appendMessage(message);
            }
            
            // Scroll to bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải tin nhắn: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void appendMessage(ChatMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = message.getSentAt().format(formatter);
        
        String senderName = message.getSenderName() != null ? message.getSenderName() : "Unknown";
        String prefix = message.getSenderType().equals("customer") ? "👤 " : "🛡️ ";
        
        String messageText = String.format("[%s] %s%s: %s\n", 
            time, prefix, senderName, message.getMessageText());
        
        chatArea.append(messageText);
    }
    
    private void sendMessage() {
        if (currentSession == null || messageField.getText().trim().isEmpty()) {
            return;
        }
        
        try {
                    ChatMessage message = new ChatMessage();
        message.setSessionId(currentSession.getSessionId());
        message.setSenderType("agent");
        message.setSenderId(currentAgent.getUserId());
        message.setMessageText(messageField.getText().trim());
        message.setMessageType("text");
        message.setSentAt(LocalDateTime.now());
            
            chatMessageDAO.insert(message);
            
            // Add message to chat area
            appendMessage(message);
            
            // Clear input field
            messageField.setText("");
            
            // Scroll to bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi gửi tin nhắn: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void closeCurrentSession() {
        if (currentSession == null) return;
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đóng phiên chat này không?",
            "Đóng phiên chat",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            try {
                            currentSession.setStatus("closed");
            currentSession.setClosedAt(LocalDateTime.now());
            
            chatSessionDAO.updateStatus(currentSession.getSessionId(), "closed");
            
            // Send system message
            ChatMessage systemMsg = new ChatMessage();
            systemMsg.setSessionId(currentSession.getSessionId());
            systemMsg.setSenderType("bot"); // ✅ Đúng với database constraint
            systemMsg.setSenderId(0);
            systemMsg.setMessageText("Phiên chat đã được đóng bởi nhân viên hỗ trợ.");
            systemMsg.setMessageType("text");
            systemMsg.setSentAt(LocalDateTime.now());
                
                chatMessageDAO.insert(systemMsg);
                
                // Refresh sessions list
                refreshSessions();
                
                // Clear chat area
                chatArea.setText("");
                statusLabel.setText("Chưa chọn phiên chat");
                customerInfoLabel.setText("");
                setChatControlsEnabled(false);
                currentSession = null;
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi đóng phiên chat: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void setChatControlsEnabled(boolean enabled) {
        messageField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
        closeSessionButton.setEnabled(enabled);
    }
    
    private void updateStatusLabel() {
        int waitingCount = 0;
        int activeCount = 0;
        
        for (int i = 0; i < sessionListModel.size(); i++) {
            ChatSession session = sessionListModel.getElementAt(i);
            if (session.getStatus().equals("waiting")) {
                waitingCount++;
            } else if (session.getStatus().equals("active")) {
                activeCount++;
            }
        }
        
        String status = String.format("Tổng: %d | Chờ: %d | Đang hoạt động: %d", 
            sessionListModel.size(), waitingCount, activeCount);
        
        // Update window title
        setTitle("Agent Chat Support - " + status);
    }
    
    private void startMessagePolling() {
        messageTimer = new Timer();
        messageTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (currentSession != null) {
                        checkNewMessages();
                    }
                    refreshSessions();
                });
            }
        }, 3000, 3000); // Check every 3 seconds
    }
    
    private void checkNewMessages() {
        if (currentSession == null) return;
        
        try {
            List<ChatMessage> unreadMessages = chatMessageDAO.selectUnreadBySessionId(currentSession.getSessionId(), currentAgent.getUserId());
            
            for (ChatMessage message : unreadMessages) {
                if (!message.getSenderType().equals("agent")) {
                    appendMessage(message);
                }
            }
            
            // Mark messages as read
            if (!unreadMessages.isEmpty()) {
                chatMessageDAO.markAllAsRead(currentSession.getSessionId(), currentAgent.getUserId());
            }
            
        } catch (Exception e) {
            // Silent error for polling
            System.err.println("Error checking new messages: " + e.getMessage());
        }
    }
    
    @Override
    public void dispose() {
        if (messageTimer != null) {
            messageTimer.cancel();
        }
        super.dispose();
    }
} 