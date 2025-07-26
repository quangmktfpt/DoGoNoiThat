-- Tạo bảng ShoppingCarts (theo schema của user)
CREATE TABLE ShoppingCarts (
    CartID        INT         IDENTITY(1,1) PRIMARY KEY,
    UserID        INT         NOT NULL UNIQUE,
    CreatedDate   DATETIME2   NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT FK_Carts_Users FOREIGN KEY (UserID)
        REFERENCES Users(UserID)
);

-- Tạo bảng CartItems (theo schema của user)
CREATE TABLE CartItems (
    CartItemID    INT         IDENTITY(1,1) PRIMARY KEY,
    CartID        INT         NOT NULL,
    ProductID     VARCHAR(20) NOT NULL,
    Quantity      INT         NOT NULL,
    CONSTRAINT FK_CartItems_Carts    FOREIGN KEY (CartID)
        REFERENCES ShoppingCarts(CartID),
    CONSTRAINT FK_CartItems_Products FOREIGN KEY (ProductID)
        REFERENCES Products(ProductID),
    CONSTRAINT UQ_Cart_Product UNIQUE (CartID, ProductID)
);

-- Tạo index để tối ưu hiệu suất
CREATE INDEX IX_ShoppingCarts_UserID ON ShoppingCarts(UserID);
CREATE INDEX IX_CartItems_CartID ON CartItems(CartID);
CREATE INDEX IX_CartItems_ProductID ON CartItems(ProductID); 