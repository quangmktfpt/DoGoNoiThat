-- =====================================================
-- MỞ RỘNG DATABASE CHO CHAT SYSTEM & NOTIFICATION
-- =====================================================

USE Storedogo2;
GO

-- =====================================================
-- 1. BẢNG CHAT SYSTEM
-- =====================================================

-- Bảng quản lý phiên chat
CREATE TABLE ChatSessions (
    SessionID      INT           IDENTITY(1,1) PRIMARY KEY,
    UserID         INT           NOT NULL,
    AgentID        INT           NULL,                    -- NULL = chưa có nhân viên
    Status         VARCHAR(20)   NOT NULL DEFAULT 'waiting' 
                   CHECK (Status IN ('waiting', 'active', 'closed', 'transferred')),
    CreatedAt      DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
    StartedAt      DATETIME2     NULL,                    -- Khi nhân viên bắt đầu chat
    ClosedAt       DATETIME2     NULL,                    -- Khi kết thúc chat
    CustomerRating TINYINT       NULL CHECK (CustomerRating BETWEEN 1 AND 5),
    CustomerComment NVARCHAR(500) NULL,
    CONSTRAINT FK_ChatSessions_Users FOREIGN KEY (UserID)
        REFERENCES Users(UserID),
    CONSTRAINT FK_ChatSessions_Agents FOREIGN KEY (AgentID)
        REFERENCES Users(UserID)
);

-- Bảng tin nhắn chat
CREATE TABLE ChatMessages (
    MessageID      INT           IDENTITY(1,1) PRIMARY KEY,
    SessionID      INT           NOT NULL,
    SenderType     VARCHAR(20)   NOT NULL 
                   CHECK (SenderType IN ('customer', 'agent', 'bot')),
    SenderID       INT           NOT NULL,                -- UserID của người gửi
    MessageText    NVARCHAR(1000) NOT NULL,
    MessageType    VARCHAR(20)   NOT NULL DEFAULT 'text'
                   CHECK (MessageType IN ('text', 'image', 'file', 'system')),
    FilePath       NVARCHAR(500) NULL,                    -- Đường dẫn file nếu có
    SentAt         DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
    IsRead         BIT           NOT NULL DEFAULT 0,
    CONSTRAINT FK_ChatMessages_Sessions FOREIGN KEY (SessionID)
        REFERENCES ChatSessions(SessionID),
    CONSTRAINT FK_ChatMessages_Users FOREIGN KEY (SenderID)
        REFERENCES Users(UserID)
);

-- Bảng nhân viên hỗ trợ (agents)
CREATE TABLE SupportAgents (
    AgentID        INT           PRIMARY KEY,
    IsOnline       BIT           NOT NULL DEFAULT 0,
    CurrentSessionID INT         NULL,
    MaxSessions    INT           NOT NULL DEFAULT 5,      -- Số phiên chat tối đa
    Specialization NVARCHAR(200) NULL,                    -- Chuyên môn
    CONSTRAINT FK_SupportAgents_Users FOREIGN KEY (AgentID)
        REFERENCES Users(UserID),
    CONSTRAINT FK_SupportAgents_Sessions FOREIGN KEY (CurrentSessionID)
        REFERENCES ChatSessions(SessionID)
);

-- =====================================================
-- 2. BẢNG NOTIFICATION SYSTEM
-- =====================================================

-- Bảng thông báo
CREATE TABLE Notifications (
    NotificationID INT           IDENTITY(1,1) PRIMARY KEY,
    UserID         INT           NOT NULL,
    Type           VARCHAR(50)   NOT NULL 
                   CHECK (Type IN ('email', 'in_app', 'chat', 'order_update', 'support_response')),
    Title          NVARCHAR(200) NOT NULL,
    Message        NVARCHAR(1000) NOT NULL,
    IsRead         BIT           NOT NULL DEFAULT 0,
    CreatedAt      DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
    ReadAt         DATETIME2     NULL,
    RelatedID      INT           NULL,                    -- ID liên quan (OrderID, MessageID, etc.)
    RelatedType    VARCHAR(50)   NULL,                    -- Loại ID liên quan
    CONSTRAINT FK_Notifications_Users FOREIGN KEY (UserID)
        REFERENCES Users(UserID)
);

-- Bảng cài đặt thông báo của user
CREATE TABLE UserNotificationSettings (
    UserID         INT           PRIMARY KEY,
    EmailEnabled   BIT           NOT NULL DEFAULT 1,
    InAppEnabled   BIT           NOT NULL DEFAULT 1,
    ChatEnabled    BIT           NOT NULL DEFAULT 1,
    OrderUpdates   BIT           NOT NULL DEFAULT 1,
    SupportUpdates BIT           NOT NULL DEFAULT 1,
    CONSTRAINT FK_UserNotificationSettings_Users FOREIGN KEY (UserID)
        REFERENCES Users(UserID)
);

-- =====================================================
-- 3. BẢNG FAQ SYSTEM
-- =====================================================

-- Bảng câu hỏi thường gặp
CREATE TABLE FAQs (
    FAQID          INT           IDENTITY(1,1) PRIMARY KEY,
    Category       VARCHAR(100)  NOT NULL,
    Question       NVARCHAR(500) NOT NULL,
    Answer         NVARCHAR(2000) NOT NULL,
    IsActive       BIT           NOT NULL DEFAULT 1,
    SortOrder      INT           NOT NULL DEFAULT 0,
    CreatedAt      DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
    UpdatedAt      DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

-- Bảng thống kê FAQ
CREATE TABLE FAQStatistics (
    FAQID          INT           NOT NULL,
    ViewCount      INT           NOT NULL DEFAULT 0,
    HelpfulCount   INT           NOT NULL DEFAULT 0,
    NotHelpfulCount INT          NOT NULL DEFAULT 0,
    LastViewed     DATETIME2     NULL,
    CONSTRAINT FK_FAQStatistics_FAQs FOREIGN KEY (FAQID)
        REFERENCES FAQs(FAQID)
);

-- =====================================================
-- 4. INDEXES CHO HIỆU SUẤT
-- =====================================================

-- Indexes cho ChatSessions
CREATE INDEX IX_ChatSessions_UserID ON ChatSessions(UserID);
CREATE INDEX IX_ChatSessions_AgentID ON ChatSessions(AgentID);
CREATE INDEX IX_ChatSessions_Status ON ChatSessions(Status);
CREATE INDEX IX_ChatSessions_CreatedAt ON ChatSessions(CreatedAt);

-- Indexes cho ChatMessages
CREATE INDEX IX_ChatMessages_SessionID ON ChatMessages(SessionID);
CREATE INDEX IX_ChatMessages_SenderID ON ChatMessages(SenderID);
CREATE INDEX IX_ChatMessages_SentAt ON ChatMessages(SentAt);
CREATE INDEX IX_ChatMessages_IsRead ON ChatMessages(IsRead);

-- Indexes cho Notifications
CREATE INDEX IX_Notifications_UserID ON Notifications(UserID);
CREATE INDEX IX_Notifications_Type ON Notifications(Type);
CREATE INDEX IX_Notifications_IsRead ON Notifications(IsRead);
CREATE INDEX IX_Notifications_CreatedAt ON Notifications(CreatedAt);

-- Indexes cho FAQs
CREATE INDEX IX_FAQs_Category ON FAQs(Category);
CREATE INDEX IX_FAQs_IsActive ON FAQs(IsActive);
CREATE INDEX IX_FAQs_SortOrder ON FAQs(SortOrder);

-- =====================================================
-- 5. STORED PROCEDURES CHO CHAT SYSTEM
-- =====================================================

-- Procedure tạo phiên chat mới
CREATE PROCEDURE sp_CreateChatSession
    @UserID INT,
    @SessionID INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    
    INSERT INTO ChatSessions (UserID, Status)
    VALUES (@UserID, 'waiting');
    
    SET @SessionID = SCOPE_IDENTITY();
    
    -- Tạo tin nhắn chào mừng từ bot
    INSERT INTO ChatMessages (SessionID, SenderType, SenderID, MessageText, MessageType)
    VALUES (@SessionID, 'bot', 1, N'Xin chào! Tôi là trợ lý ảo của StoreDoGo. Bạn cần hỗ trợ gì?', 'text');
END;

-- Procedure lấy tin nhắn của phiên chat
CREATE PROCEDURE sp_GetChatMessages
    @SessionID INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        cm.MessageID,
        cm.SessionID,
        cm.SenderType,
        cm.SenderID,
        u.FullName AS SenderName,
        cm.MessageText,
        cm.MessageType,
        cm.FilePath,
        cm.SentAt,
        cm.IsRead
    FROM ChatMessages cm
    JOIN Users u ON cm.SenderID = u.UserID
    WHERE cm.SessionID = @SessionID
    ORDER BY cm.SentAt ASC;
END;

-- Procedure gửi tin nhắn
CREATE PROCEDURE sp_SendChatMessage
    @SessionID INT,
    @SenderID INT,
    @SenderType VARCHAR(20),
    @MessageText NVARCHAR(1000),
    @MessageType VARCHAR(20) = 'text',
    @FilePath NVARCHAR(500) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    INSERT INTO ChatMessages (SessionID, SenderID, SenderType, MessageText, MessageType, FilePath)
    VALUES (@SessionID, @SenderID, @SenderType, @MessageText, @MessageType, @FilePath);
    
    -- Cập nhật trạng thái phiên chat nếu cần
    UPDATE ChatSessions 
    SET Status = 'active', StartedAt = CASE WHEN StartedAt IS NULL THEN SYSDATETIME() ELSE StartedAt END
    WHERE SessionID = @SessionID AND Status = 'waiting';
END;

-- Procedure tìm nhân viên hỗ trợ phù hợp
CREATE PROCEDURE sp_FindAvailableAgent
    @AgentID INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT TOP 1 @AgentID = sa.AgentID
    FROM SupportAgents sa
    WHERE sa.IsOnline = 1 
      AND (sa.CurrentSessionID IS NULL OR 
           (SELECT COUNT(*) FROM ChatSessions cs WHERE cs.AgentID = sa.AgentID AND cs.Status = 'active') < sa.MaxSessions)
    ORDER BY (SELECT COUNT(*) FROM ChatSessions cs WHERE cs.AgentID = sa.AgentID AND cs.Status = 'active');
    
    IF @AgentID IS NULL
        SET @AgentID = 0; -- Không có nhân viên nào
END;

-- =====================================================
-- 6. STORED PROCEDURES CHO NOTIFICATION SYSTEM
-- =====================================================

-- Procedure tạo thông báo
CREATE PROCEDURE sp_CreateNotification
    @UserID INT,
    @Type VARCHAR(50),
    @Title NVARCHAR(200),
    @Message NVARCHAR(1000),
    @RelatedID INT = NULL,
    @RelatedType VARCHAR(50) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Kiểm tra cài đặt thông báo của user
    IF EXISTS (SELECT 1 FROM UserNotificationSettings WHERE UserID = @UserID)
    BEGIN
        DECLARE @ShouldNotify BIT = 0;
        
        SELECT @ShouldNotify = CASE 
            WHEN @Type = 'email' AND EmailEnabled = 1 THEN 1
            WHEN @Type = 'in_app' AND InAppEnabled = 1 THEN 1
            WHEN @Type = 'chat' AND ChatEnabled = 1 THEN 1
            WHEN @Type = 'order_update' AND OrderUpdates = 1 THEN 1
            WHEN @Type = 'support_response' AND SupportUpdates = 1 THEN 1
            ELSE 0
        END
        FROM UserNotificationSettings 
        WHERE UserID = @UserID;
        
        IF @ShouldNotify = 1
        BEGIN
            INSERT INTO Notifications (UserID, Type, Title, Message, RelatedID, RelatedType)
            VALUES (@UserID, @Type, @Title, @Message, @RelatedID, @RelatedType);
        END
    END
    ELSE
    BEGIN
        -- Nếu chưa có cài đặt, tạo thông báo mặc định
        INSERT INTO Notifications (UserID, Type, Title, Message, RelatedID, RelatedType)
        VALUES (@UserID, @Type, @Title, @Message, @RelatedID, @RelatedType);
    END
END;

-- Procedure lấy thông báo chưa đọc
CREATE PROCEDURE sp_GetUnreadNotifications
    @UserID INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        NotificationID,
        Type,
        Title,
        Message,
        CreatedAt,
        RelatedID,
        RelatedType
    FROM Notifications
    WHERE UserID = @UserID AND IsRead = 0
    ORDER BY CreatedAt DESC;
END;

-- Procedure đánh dấu thông báo đã đọc
CREATE PROCEDURE sp_MarkNotificationAsRead
    @NotificationID INT
AS
BEGIN
    SET NOCOUNT ON;
    
    UPDATE Notifications 
    SET IsRead = 1, ReadAt = SYSDATETIME()
    WHERE NotificationID = @NotificationID;
END;

-- =====================================================
-- 7. INSERT DỮ LIỆU MẪU
-- =====================================================

-- Thêm nhân viên hỗ trợ
INSERT INTO SupportAgents (AgentID, IsOnline, MaxSessions, Specialization)
VALUES 
(1, 1, 5, N'Hỗ trợ đặt hàng và thanh toán'),
(2, 1, 3, N'Hỗ trợ kỹ thuật và sản phẩm');

-- Thêm cài đặt thông báo cho users
INSERT INTO UserNotificationSettings (UserID, EmailEnabled, InAppEnabled, ChatEnabled, OrderUpdates, SupportUpdates)
SELECT UserID, 1, 1, 1, 1, 1
FROM Users;

-- Thêm FAQ mẫu
INSERT INTO FAQs (Category, Question, Answer, SortOrder)
VALUES 
(N'Đặt hàng', N'Làm thế nào để đặt hàng online?', N'Bạn có thể đặt hàng bằng cách: 1) Chọn sản phẩm 2) Thêm vào giỏ hàng 3) Thanh toán', 1),
(N'Thanh toán', N'Có những phương thức thanh toán nào?', N'Chúng tôi chấp nhận: Tiền mặt khi nhận hàng, Chuyển khoản ngân hàng, Thẻ tín dụng', 1),
(N'Giao hàng', N'Thời gian giao hàng là bao lâu?', N'Thời gian giao hàng từ 2-5 ngày làm việc tùy thuộc vào địa điểm giao hàng', 1),
(N'Đổi trả', N'Chính sách đổi trả như thế nào?', N'Bạn có thể đổi trả trong vòng 7 ngày kể từ ngày nhận hàng với điều kiện sản phẩm còn nguyên vẹn', 1);

-- Thêm thống kê FAQ
INSERT INTO FAQStatistics (FAQID, ViewCount, HelpfulCount, NotHelpfulCount)
SELECT FAQID, 0, 0, 0 FROM FAQs;

-- Thêm thông báo mẫu
INSERT INTO Notifications (UserID, Type, Title, Message, RelatedID, RelatedType)
VALUES 
(2, 'order_update', N'Cập nhật đơn hàng', N'Đơn hàng #1 của bạn đã được xử lý', 1, 'order'),
(3, 'support_response', N'Phản hồi hỗ trợ', N'Chúng tôi đã phản hồi tin nhắn hỗ trợ của bạn', 1, 'contact_message');

-- =====================================================
-- 8. TRIGGERS
-- =====================================================

-- Trigger tự động tạo cài đặt thông báo cho user mới
CREATE TRIGGER trg_CreateNotificationSettingsForNewUser
ON Users
AFTER INSERT
AS
BEGIN
    INSERT INTO UserNotificationSettings (UserID, EmailEnabled, InAppEnabled, ChatEnabled, OrderUpdates, SupportUpdates)
    SELECT UserID, 1, 1, 1, 1, 1 FROM inserted;
END;

-- Trigger tự động tạo thống kê FAQ cho FAQ mới
CREATE TRIGGER trg_CreateFAQStatisticsForNewFAQ
ON FAQs
AFTER INSERT
AS
BEGIN
    INSERT INTO FAQStatistics (FAQID, ViewCount, HelpfulCount, NotHelpfulCount)
    SELECT FAQID, 0, 0, 0 FROM inserted;
END;

PRINT 'Database extension for Chat System and Notification System completed!';
PRINT 'Added tables: ChatSessions, ChatMessages, SupportAgents, Notifications, UserNotificationSettings, FAQs, FAQStatistics';
PRINT 'Added stored procedures for chat and notification management';
PRINT 'Added sample data for testing';
GO 