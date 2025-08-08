/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.ui;

import poly.controller.ProductController;
import poly.entity.Product;
import poly.entity.Category;
import poly.dao.impl.ProductDAOImpl;
import poly.dao.impl.CategoryDAOImpl;
import poly.dao.ShoppingCartDAO;
import poly.dao.impl.ShoppingCartDAOImpl;
import poly.entity.ShoppingCart;
import poly.entity.CartItem;
import poly.util.CurrentUserUtil;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.util.List;
import java.math.BigDecimal;
import poly.controller.ProductController_nghia;
import poly.dao.impl.ProductDAOImpl_Nghia;
import poly.entity.Product_Nghia;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Component;
import poly.util.WrapLayout;
import poly.dao.ProductReviewDAO;
import poly.dao.impl.ProductReviewDAOImpl;
import poly.entity.ProductReview;
import javax.swing.table.DefaultTableModel;
import poly.ui.DanhGiaJDialog1;

/**
 *
 * @author Nghia
 */
public class DuyetspJDialog_nghia1 extends javax.swing.JDialog implements ProductController_nghia {

    /**
     * Creates new form DuyetspJDialog
     */
    public DuyetspJDialog_nghia1(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        // Đảm bảo AI trả lời luôn xuống dòng, không kéo ngang
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        // Đặt font hỗ trợ tiếng Việt rõ ràng
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        
        // Cấu hình TextArea nhập liệu hiển thị theo chiều dọc
        jTextArea2.setLineWrap(true);
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jTextArea2.setRows(3); // Hiển thị 3 dòng
        // Hiển thị sản phẩm dạng lưới 3 cột, khoảng cách 16px
        productGridPanel.setLayout(new java.awt.GridLayout(0, 3, 16, 16));
        productScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // Sự kiện Enter gửi chat
        jTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && !evt.isShiftDown()) {
                    evt.consume();
                    btnGui.doClick();
                }
            }
        });
        // 1. Đổi màu nền panel (phối màu mới)
        jPanel1.setBackground(new java.awt.Color(227, 242, 253)); // Xanh nhạt pastel
        jPanel2.setBackground(new java.awt.Color(240, 244, 195)); // Xám xanh nhạt
        // 2. Đổi màu tiêu đề
        jLabel1.setForeground(new java.awt.Color(25, 118, 210)); // Xanh dương đậm
        jLabel9.setForeground(new java.awt.Color(56, 142, 60));  // Xanh lá đậm
        // 3. Đổi màu header bảng
        // 4. Thêm icon cho nút (chỉ dùng icon có sẵn trong thư mục icon)
        // btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/icon/search.png")));
//        btnThemVaoGio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/icon/add.png")));
        // btnMoveFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/icon/first.png")));
        //btnMovePrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/icon/back.png")));
        //btnMoveNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/icon/next.png")));
        // btnMoveLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/icon/last.png")));
        // 5. Thêm hiệu ứng hover cho nút
        addHoverEffect(btnTimKiem, new java.awt.Color(41, 128, 185));
        addHoverEffect(btnThemVaoGio, new java.awt.Color(39, 174, 96));
        addHoverEffect(btnMoveFirst, new java.awt.Color(127, 140, 141));
        addHoverEffect(btnMovePrevious, new java.awt.Color(127, 140, 141));
        addHoverEffect(btnMoveNext, new java.awt.Color(127, 140, 141));
        addHoverEffect(btnMoveLast, new java.awt.Color(127, 140, 141));
        addHoverEffect(btnTimLoai, new java.awt.Color(241, 196, 15));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        btnGui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String userMessage = jTextArea2.getText().trim();
                if (userMessage.isEmpty()) return;
                jTextArea1.append("\n[Bạn]: " + userMessage + "\n[AI]: Đang trả lời...\n");
                jTextArea2.setText("");
                btnGui.setEnabled(false);
                jTextArea2.setEnabled(false);
                
                // Sử dụng streaming response
                if (currentProductInChat != null) {
                    // Lấy đầy đủ thông tin sản phẩm hiện tại
                    String productName = currentProductInChat.getProductName() != null ? currentProductInChat.getProductName() : "";
                    String productPrice = currentProductInChat.getUnitPrice() != null ? currentProductInChat.getUnitPrice().toString() : "";
                    String productDescription = currentProductInChat.getDescription() != null ? currentProductInChat.getDescription() : "";
                    String productSize = currentProductInChat.getKichThuoc() != null ? currentProductInChat.getKichThuoc() : "";
                    String productStock = currentProductInChat.getQuantity() != null ? currentProductInChat.getQuantity().toString() : "";
                    String productCategory = getCategoryName(currentProductInChat.getCategoryId());
                    String categoryId = currentProductInChat.getCategoryId();
                    
                    // Lấy tất cả sản phẩm cùng danh mục để so sánh
                    List<Product_Nghia> sameCategoryProducts = searchByCategory(categoryId);
                    StringBuilder categoryProductsInfo = new StringBuilder();
                    
                    if (sameCategoryProducts.size() > 1) {
                        categoryProductsInfo.append("\nCác sản phẩm cùng danh mục ").append(productCategory).append(":\n");
                        for (Product_Nghia p : sameCategoryProducts) {
                            String pName = p.getProductName() != null ? p.getProductName() : "";
                            String pPrice = p.getUnitPrice() != null ? p.getUnitPrice().toString() : "";
                            String pSize = p.getKichThuoc() != null ? p.getKichThuoc() : "";
                            String pStock = p.getQuantity() != null ? p.getQuantity().toString() : "";
                            
                            categoryProductsInfo.append(String.format(
                                "- %s: %s VNĐ, Kích thước: %s, Tồn kho: %s\n",
                                pName, pPrice, pSize, pStock
                            ));
                        }
                    }
                    
                    String productContext = String.format(
                        "Thông tin sản phẩm hiện tại:\n" +
                        "- Tên: %s\n" +
                        "- Danh mục: %s\n" +
                        "- Giá: %s VNĐ\n" +
                        "- Kích thước: %s\n" +
                        "- Tồn kho: %s sản phẩm\n" +
                        "- Mô tả: %s%s\n\n" +
                        "Câu hỏi của bạn: %s",
                        productName, productCategory, productPrice, productSize, productStock, productDescription, 
                        categoryProductsInfo.toString(), userMessage
                    );
                    
                    poly.util.OpenAIClient.getAIResponseStream(productContext, 
                        (chunk) -> {
                            // Hiển thị từng chunk
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                jTextArea1.append(chunk);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                            });
                        },
                        (complete) -> {
                            // Hoàn thành
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                jTextArea1.append("\n");
                                btnGui.setEnabled(true);
                                jTextArea2.setEnabled(true);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                                jTextArea2.requestFocusInWindow();
                            });
                        },
                        (error) -> {
                            // Lỗi - sử dụng fallback
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                StringBuilder fallbackResponse = new StringBuilder();
                                fallbackResponse.append(String.format(
                                    "Xin chào! Tôi thấy bạn đang hỏi về sản phẩm '%s' thuộc danh mục %s.\n" +
                                    "Thông tin chi tiết:\n" +
                                    "- Giá: %s VNĐ\n" +
                                    "- Kích thước: %s\n" +
                                    "- Tồn kho: %s sản phẩm\n" +
                                    "- Mô tả: %s\n\n",
                                    productName, productCategory, productPrice, productSize, productStock, productDescription
                                ));
                                
                                // Thêm thông tin so sánh nếu có nhiều sản phẩm cùng danh mục
                                if (sameCategoryProducts.size() > 1) {
                                    fallbackResponse.append("Các sản phẩm cùng danh mục để so sánh:\n");
                                    for (Product_Nghia p : sameCategoryProducts) {
                                        String pName = p.getProductName() != null ? p.getProductName() : "";
                                        String pPrice = p.getUnitPrice() != null ? p.getUnitPrice().toString() : "";
                                        String pSize = p.getKichThuoc() != null ? p.getKichThuoc() : "";
                                        fallbackResponse.append(String.format("- %s: %s VNĐ, Kích thước: %s\n", pName, pPrice, pSize));
                                    }
                                    fallbackResponse.append("\nBạn có thể so sánh các sản phẩm này để chọn phù hợp nhất!\n");
                                }
                                
                                fallbackResponse.append("Đây là một sản phẩm nội thất chất lượng cao. Bạn có muốn biết thêm thông tin gì không?");
                                
                                jTextArea1.append("[AI]: " + fallbackResponse.toString() + "\n");
                                btnGui.setEnabled(true);
                                jTextArea2.setEnabled(true);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                            });
                        }
                    );
                } else {
                    // Khi không có sản phẩm được chọn, AI có thể tư vấn dựa trên tất cả sản phẩm
                    List<Product_Nghia> allProducts = productDAO.selectAll();
                    StringBuilder allProductsInfo = new StringBuilder();
                    
                    // Phân loại sản phẩm theo danh mục
                    java.util.Map<String, java.util.List<Product_Nghia>> productsByCategory = new java.util.HashMap<>();
                    for (Product_Nghia p : allProducts) {
                        String categoryName = getCategoryName(p.getCategoryId());
                        productsByCategory.computeIfAbsent(categoryName, k -> new java.util.ArrayList<>()).add(p);
                    }
                    
                    // Tạo thông tin tất cả sản phẩm
                    allProductsInfo.append("\nTất cả sản phẩm trong cửa hàng:\n");
                    for (java.util.Map.Entry<String, java.util.List<Product_Nghia>> entry : productsByCategory.entrySet()) {
                        String categoryName = entry.getKey();
                        java.util.List<Product_Nghia> products = entry.getValue();
                        
                        allProductsInfo.append("\n").append(categoryName).append(":\n");
                        for (Product_Nghia p : products) {
                            String pName = p.getProductName() != null ? p.getProductName() : "";
                            String pPrice = p.getUnitPrice() != null ? p.getUnitPrice().toString() : "";
                            String pSize = p.getKichThuoc() != null ? p.getKichThuoc() : "";
                            String pStock = p.getQuantity() != null ? p.getQuantity().toString() : "";
                            
                            allProductsInfo.append(String.format(
                                "- %s: %s VNĐ, Kích thước: %s, Tồn kho: %s\n",
                                pName, pPrice, pSize, pStock
                            ));
                        }
                    }
                    
                    String fullContext = String.format(
                        "Bạn là trợ lý AI của cửa hàng nội thất. Dựa trên thông tin sản phẩm sau, hãy tư vấn cho khách hàng:\n%s\n\n" +
                        "Câu hỏi của khách hàng: %s\n\n" +
                        "Hãy đưa ra tư vấn chi tiết, bao gồm:\n" +
                        "1. Sản phẩm phù hợp với ngân sách (nếu có đề cập)\n" +
                        "2. So sánh các lựa chọn\n" +
                        "3. Khuyến nghị cụ thể\n" +
                        "4. Thông tin bổ sung về sản phẩm",
                        allProductsInfo.toString(), userMessage
                    );
                    
                    poly.util.OpenAIClient.getAIResponseStream(fullContext, 
                        (chunk) -> {
                            // Hiển thị từng chunk
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                jTextArea1.append(chunk);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                            });
                        },
                        (complete) -> {
                            // Hoàn thành
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                jTextArea1.append("\n");
                                btnGui.setEnabled(true);
                                jTextArea2.setEnabled(true);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                                jTextArea2.requestFocusInWindow();
                            });
                        },
                        (error) -> {
                            // Lỗi - sử dụng fallback thông minh
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                StringBuilder fallbackResponse = new StringBuilder();
                                fallbackResponse.append("Xin chào! Tôi là trợ lý AI của cửa hàng nội thất.\n\n");
                                
                                // Phân tích câu hỏi để đưa ra tư vấn phù hợp
                                String lowerMessage = userMessage.toLowerCase();
                                
                                // Tìm số tiền trong câu hỏi
                                java.util.regex.Pattern pricePattern = java.util.regex.Pattern.compile("(\\d+)\\s*(nghìn|k|vnđ|đồng|vnd)?", java.util.regex.Pattern.CASE_INSENSITIVE);
                                java.util.regex.Matcher matcher = pricePattern.matcher(lowerMessage);
                                
                                if (matcher.find()) {
                                    String numberStr = matcher.group(1);
                                    String unit = matcher.group(2);
                                    int budget = Integer.parseInt(numberStr);
                                    
                                    // Chuyển đổi đơn vị
                                    if (unit != null) {
                                        if (unit.toLowerCase().contains("nghìn") || unit.toLowerCase().contains("k")) {
                                            budget = budget * 1000;
                                        }
                                    }
                                    
                                    // Tìm tất cả sản phẩm phù hợp với ngân sách
                                    java.util.List<Product_Nghia> affordableProducts = new java.util.ArrayList<>();
                                    for (Product_Nghia p : allProducts) {
                                        if (p.getUnitPrice() != null && p.getUnitPrice().compareTo(new java.math.BigDecimal(budget)) <= 0) {
                                            affordableProducts.add(p);
                                        }
                                    }
                                    
                                    if (!affordableProducts.isEmpty()) {
                                        fallbackResponse.append(String.format("Với ngân sách %d VNĐ, bạn có thể xem xét các sản phẩm sau:\n\n", budget));
                                        
                                        // Sắp xếp theo giá từ thấp đến cao
                                        affordableProducts.sort((p1, p2) -> {
                                            if (p1.getUnitPrice() == null) return 1;
                                            if (p2.getUnitPrice() == null) return -1;
                                            return p1.getUnitPrice().compareTo(p2.getUnitPrice());
                                        });
                                        
                                        for (Product_Nghia p : affordableProducts) {
                                            String pName = p.getProductName() != null ? p.getProductName() : "";
                                            String pPrice = p.getUnitPrice() != null ? p.getUnitPrice().toString() : "";
                                            String pCategory = getCategoryName(p.getCategoryId());
                                            String pSize = p.getKichThuoc() != null ? p.getKichThuoc() : "";
                                            String pStock = p.getQuantity() != null ? p.getQuantity().toString() : "";
                                            
                                            fallbackResponse.append(String.format(
                                                "- %s (%s): %s VNĐ, Kích thước: %s, Tồn kho: %s\n",
                                                pName, pCategory, pPrice, pSize, pStock
                                            ));
                                        }
                                        
                                        fallbackResponse.append(String.format("\nTổng cộng có %d sản phẩm phù hợp với ngân sách của bạn.\n", affordableProducts.size()));
                                        fallbackResponse.append("Bạn có muốn biết thêm chi tiết về sản phẩm nào không?\n");
                                    } else {
                                        fallbackResponse.append(String.format("Với ngân sách %d VNĐ, hiện tại không có sản phẩm nào phù hợp.\n", budget));
                                        fallbackResponse.append("Bạn có thể xem xét tăng ngân sách hoặc liên hệ với chúng tôi để được tư vấn thêm.\n");
                                    }
                                } else if (lowerMessage.contains("rẻ") || lowerMessage.contains("giá thấp") || lowerMessage.contains("rẻ nhất")) {
                                    fallbackResponse.append("Các sản phẩm có giá tốt nhất:\n");
                                    java.util.List<Product_Nghia> sortedByPrice = new java.util.ArrayList<>(allProducts);
                                    sortedByPrice.sort((p1, p2) -> {
                                        if (p1.getUnitPrice() == null) return 1;
                                        if (p2.getUnitPrice() == null) return -1;
                                        return p1.getUnitPrice().compareTo(p2.getUnitPrice());
                                    });
                                    
                                    for (int i = 0; i < Math.min(10, sortedByPrice.size()); i++) {
                                        Product_Nghia p = sortedByPrice.get(i);
                                        String pName = p.getProductName() != null ? p.getProductName() : "";
                                        String pPrice = p.getUnitPrice() != null ? p.getUnitPrice().toString() : "";
                                        String pCategory = getCategoryName(p.getCategoryId());
                                        String pSize = p.getKichThuoc() != null ? p.getKichThuoc() : "";
                                        
                                        fallbackResponse.append(String.format(
                                            "- %s (%s): %s VNĐ, Kích thước: %s\n",
                                            pName, pCategory, pPrice, pSize
                                        ));
                                    }
                                } else {
                                    fallbackResponse.append("Tôi có thể tư vấn cho bạn về:\n");
                                    fallbackResponse.append("- Sản phẩm theo ngân sách (ví dụ: 'tôi có 500k thì mua gì?')\n");
                                    fallbackResponse.append("- So sánh giá cả\n");
                                    fallbackResponse.append("- Khuyến nghị theo không gian\n");
                                    fallbackResponse.append("- Thông tin chi tiết sản phẩm\n\n");
                                    fallbackResponse.append("Bạn có thể hỏi cụ thể hơn về nhu cầu của mình!");
                                }
                                
                                jTextArea1.append("[AI]: " + fallbackResponse.toString() + "\n");
                                btnGui.setEnabled(true);
                                jTextArea2.setEnabled(true);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                            });
                        }
                    );
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txtTimKiemSanPham = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cboLoaiSanPham = new javax.swing.JComboBox<>();
        btnTimLoai = new javax.swing.JButton();
        productScrollPane = new javax.swing.JScrollPane();
        productGridPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtGiaTu = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtGiaDen = new javax.swing.JTextField();
        btnTimGia = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        btnGui = new javax.swing.JButton();
        btnXoaNguCanh = new javax.swing.JButton();
        btnXoaChat = new javax.swing.JButton();
        btnAI = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblHinhAnh = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnMoveFirst = new javax.swing.JButton();
        btnMovePrevious = new javax.swing.JButton();
        btnMoveNext = new javax.swing.JButton();
        btnMoveLast = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaMoTa = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        btnThemVaoGio = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtTenSanPham = new javax.swing.JTextField();
        txtGiaSanPham = new javax.swing.JTextField();
        txtKichThuoc = new javax.swing.JTextField();
        txtTonKho = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDanhGia = new javax.swing.JTable();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên người đặt", "Số lượng", "Đánh giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtTimKiemSanPham.setPreferredSize(new java.awt.Dimension(300, 25));

        jLabel6.setText("Tìm Kiếm");

        btnTimKiem.setText("Tìm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Tổng Quan Sản Phẩm");

        cboLoaiSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTimLoai.setText("Tìm Loại");
        btnTimLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimLoaiActionPerformed(evt);
            }
        });

        productScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        productGridPanel.setLayout(new java.awt.GridBagLayout());
        productScrollPane.setViewportView(productGridPanel);

        jLabel2.setText("Từ");

        jLabel11.setText("Đến");

        btnTimGia.setText("Tìm Giá");
        btnTimGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimGiaActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        btnGui.setText("Gửi");
        btnGui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiActionPerformed(evt);
            }
        });

        btnXoaNguCanh.setText("Xoá ngữ cảnh");
        btnXoaNguCanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNguCanhActionPerformed(evt);
            }
        });

        btnXoaChat.setText("Xoá đoạn chat");
        btnXoaChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaChatActionPerformed(evt);
            }
        });

        btnAI.setText("AI");
        btnAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAIActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnGui, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaNguCanh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaChat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAI, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGui)
                    .addComponent(btnXoaNguCanh)
                    .addComponent(btnXoaChat)
                    .addComponent(btnAI)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKiemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTimLoai)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGiaTu, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGiaDen, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimGia)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(productScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(325, 325, 325))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTimKiem)
                    .addComponent(txtTimKiemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cboLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimLoai)
                    .addComponent(jLabel2)
                    .addComponent(txtGiaTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtGiaDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimGia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(productScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(249, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tổng quan", jPanel1);

        lblHinhAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Tên Sản Phẩm");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Giá");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Mô tả");

        btnMoveFirst.setText("|<");
        btnMoveFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveFirstActionPerformed(evt);
            }
        });

        btnMovePrevious.setText("<<");
        btnMovePrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovePreviousActionPerformed(evt);
            }
        });

        btnMoveNext.setText(">>");
        btnMoveNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveNextActionPerformed(evt);
            }
        });

        btnMoveLast.setText(">|");
        btnMoveLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveLastActionPerformed(evt);
            }
        });

        txtAreaMoTa.setEditable(false);
        txtAreaMoTa.setColumns(30);
        txtAreaMoTa.setLineWrap(true);
        txtAreaMoTa.setRows(5);
        txtAreaMoTa.setText("\n");
        txtAreaMoTa.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtAreaMoTa);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("Thông Tin Chi Tiết");

        btnThemVaoGio.setText("Thêm vào giỏ");
        btnThemVaoGio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemVaoGioActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Kích Thước");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Tồn Kho");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel16.setText("Đánh giá sản phẩm");

        tblDanhGia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên người đặt", "Số lượng", "Đánh giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblDanhGia);
        
        // Thêm sự kiện click vào bảng đánh giá để hiển thị chi tiết
        tblDanhGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    showReviewDetails();
                }
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(397, 397, 397))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(399, 399, 399))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtTenSanPham)
                                    .addComponent(txtGiaSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(txtKichThuoc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(btnMoveFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnMovePrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnMoveNext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnMoveLast, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(29, 29, 29)))
                    .addComponent(txtTonKho, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemVaoGio))
                .addContainerGap(158, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(txtGiaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(txtTonKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemVaoGio)
                    .addComponent(btnMoveFirst)
                    .addComponent(btnMovePrevious)
                    .addComponent(btnMoveNext)
                    .addComponent(btnMoveLast))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );

        jTabbedPane1.addTab("Chi tiết", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMoveFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveFirstActionPerformed
        if (currentTypeList != null && !currentTypeList.isEmpty()) {
            currentTypeIndex = 0;
            setForm(currentTypeList.get(currentTypeIndex));
        }
    }//GEN-LAST:event_btnMoveFirstActionPerformed

    private void btnMovePreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovePreviousActionPerformed
        if (currentTypeList != null && currentTypeIndex > 0) {
            currentTypeIndex--;
            setForm(currentTypeList.get(currentTypeIndex));
        }
    }//GEN-LAST:event_btnMovePreviousActionPerformed

    private void btnMoveNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveNextActionPerformed
        if (currentTypeList != null && currentTypeIndex < currentTypeList.size() - 1) {
            currentTypeIndex++;
            setForm(currentTypeList.get(currentTypeIndex));
        }
    }//GEN-LAST:event_btnMoveNextActionPerformed

    private void btnMoveLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveLastActionPerformed
        if (currentTypeList != null && !currentTypeList.isEmpty()) {
            currentTypeIndex = currentTypeList.size() - 1;
            setForm(currentTypeList.get(currentTypeIndex));
        }
    }//GEN-LAST:event_btnMoveLastActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        searchProducts();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnThemVaoGioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemVaoGioActionPerformed
        if (currentTypeIndex >= 0 && currentTypeList != null && currentTypeIndex < currentTypeList.size()) {
            Product_Nghia selectedProduct = currentTypeList.get(currentTypeIndex);
            String input = javax.swing.JOptionPane.showInputDialog(this, "Nhập số lượng muốn thêm vào giỏ:", "Số lượng", javax.swing.JOptionPane.QUESTION_MESSAGE);
            if (input == null) return; // Bấm Cancel
            input = input.trim();
            if (input.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int soLuong = Integer.parseInt(input);
                int tonKho = selectedProduct.getQuantity() != null ? selectedProduct.getQuantity() : 0;
                if (soLuong <= 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (soLuong > tonKho) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Số lượng vượt quá tồn kho sẵn có! (Tồn kho: " + tonKho + ")", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Lấy thông tin người dùng đang đăng nhập
                Integer userId = CurrentUserUtil.getCurrentUserId();
                if (userId == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Thêm sản phẩm vào giỏ hàng
                ShoppingCartDAO shoppingCartDAO = new ShoppingCartDAOImpl();
                shoppingCartDAO.addProductToCart(userId, selectedProduct.getProductId(), soLuong);

                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Đã thêm " + soLuong + " sản phẩm '" + selectedProduct.getProductName() + "' vào giỏ hàng!", 
                    "Thông báo", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một sản phẩm trước khi thêm vào giỏ hàng!", 
                "Thông báo", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnThemVaoGioActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        open();
    }//GEN-LAST:event_formWindowOpened

    private void btnTimGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimGiaActionPerformed
        // TODO add your handling code here:
        String giaTuStr = txtGiaTu.getText().trim();
        String giaDenStr = txtGiaDen.getText().trim();
        if (giaTuStr.isEmpty() && giaDenStr.isEmpty()) {
            renderProductGrid(productDAO.selectAll());
            return;
        }
        try {
            java.math.BigDecimal giaTu = giaTuStr.isEmpty() ? java.math.BigDecimal.ZERO : new java.math.BigDecimal(giaTuStr);
            java.math.BigDecimal giaDen = giaDenStr.isEmpty() ? new java.math.BigDecimal("999999999") : new java.math.BigDecimal(giaDenStr);
            if (giaTu.compareTo(giaDen) > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Giá từ phải nhỏ hơn hoặc bằng giá đến!", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Product_Nghia> result = searchByPriceRange(giaTu, giaDen);
            renderProductGrid(result);
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ cho giá!", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnTimGiaActionPerformed

    private void btnDanhGiaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDanhGiaSanPhamActionPerformed
        // Kiểm tra xem có sản phẩm được chọn không
        if (currentTypeIndex < 0 || currentTypeList == null || currentTypeIndex >= currentTypeList.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm trước khi đánh giá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Product_Nghia selectedProduct = currentTypeList.get(currentTypeIndex);
        
        // Lấy thông tin người dùng hiện tại
        Integer currentUserId = CurrentUserUtil.getCurrentUserId();
        if (currentUserId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập để đánh giá sản phẩm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Kiểm tra xem người dùng có thể đánh giá sản phẩm không
        boolean canReview = productReviewDAO.canUserReviewProduct(selectedProduct.getProductId(), currentUserId);
        boolean hasReviewed = productReviewDAO.hasUserReviewed(selectedProduct.getProductId(), currentUserId);
        
        if (hasReviewed) {
            JOptionPane.showMessageDialog(this, "Bạn đã đánh giá sản phẩm này rồi!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (!canReview) {
            JOptionPane.showMessageDialog(this, "Bạn chỉ có thể đánh giá sản phẩm sau khi đã mua và nhận hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy ảnh sản phẩm
        ImageIcon productImage = getProductImage(selectedProduct.getImagePath());
        
        // Tạo callback để refresh bảng đánh giá sau khi gửi đánh giá
        Runnable refreshCallback = () -> {
            loadProductReviews(selectedProduct.getProductId());
        };
        
        // Mở dialog đánh giá
        java.awt.Frame parentFrame = null;
        if (this.getOwner() instanceof java.awt.Frame) {
            parentFrame = (java.awt.Frame) this.getOwner();
        } else {
            // Nếu không có parent frame, tạo một frame tạm thời
            parentFrame = new java.awt.Frame();
        }
        
        DanhGiaJDialog1 reviewDialog = new DanhGiaJDialog1(
            parentFrame, 
            selectedProduct.getProductId(), 
            currentUserId, 
            selectedProduct.getProductName(), 
            productImage, 
            true, 
            refreshCallback
        );
        reviewDialog.setVisible(true);
    }//GEN-LAST:event_btnDanhGiaSanPhamActionPerformed

    private void btnGuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuiActionPerformed

    private void btnAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAIActionPerformed
        if (currentProductInChat != null) {
            // Hỏi AI về sản phẩm đang được chọn
            askAIAboutProduct(currentProductInChat);
        } else {
            // Nếu chưa chọn sản phẩm nào
            jTabbedPane1.setSelectedIndex(0);
            jTextArea1.append("\n[AI]: Vui lòng chọn một sản phẩm trước khi hỏi tôi về nó.\n");
            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        }
    }//GEN-LAST:event_btnAIActionPerformed

    private void btnXoaNguCanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNguCanhActionPerformed
        currentProductInChat = null;
        jTextArea1.append("\n[AI]: Đã xóa ngữ cảnh sản phẩm. Bạn có thể chat chung với tôi.\n");
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }//GEN-LAST:event_btnXoaNguCanhActionPerformed

    private void btnXoaChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaChatActionPerformed
        jTextArea1.setText("");
        currentProductInChat = null;
        jTextArea1.append("[AI]: Đã xóa toàn bộ đoạn chat. Bạn có thể bắt đầu cuộc trò chuyện mới.\n");
    }//GEN-LAST:event_btnXoaChatActionPerformed

    private void btnTimLoaiActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = cboLoaiSanPham.getSelectedIndex();
        if (selectedIndex == 0) { // Nếu chọn Tất cả
            renderProductGrid(productDAO.selectAll());
            currentRow = -1;
            return;
        }
        if (selectedIndex > 0) {
            List<Category> categories = loadAllCategories();
            String selectedCategoryId = categories.get(selectedIndex - 1).getCategoryId();
            List<Product_Nghia> filteredProducts = searchByCategory(selectedCategoryId);
            renderProductGrid(filteredProducts);
            currentRow = -1;
        }
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
            java.util.logging.Logger.getLogger(DuyetspJDialog_nghia1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DuyetspJDialog_nghia1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DuyetspJDialog_nghia1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DuyetspJDialog_nghia1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DuyetspJDialog_nghia1 dialog = new DuyetspJDialog_nghia1(new javax.swing.JFrame(), true);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAI;
    private javax.swing.JButton btnGui;
    private javax.swing.JButton btnMoveFirst;
    private javax.swing.JButton btnMoveLast;
    private javax.swing.JButton btnMoveNext;
    private javax.swing.JButton btnMovePrevious;
    private javax.swing.JButton btnThemVaoGio;
    private javax.swing.JButton btnTimGia;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTimLoai;
    private javax.swing.JButton btnXoaChat;
    private javax.swing.JButton btnXoaNguCanh;
    private javax.swing.JComboBox<String> cboLoaiSanPham;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel lblHinhAnh;
    private javax.swing.JPanel productGridPanel;
    private javax.swing.JScrollPane productScrollPane;
    private javax.swing.JTable tblDanhGia;
    private javax.swing.JTextArea txtAreaMoTa;
    private javax.swing.JTextField txtGiaDen;
    private javax.swing.JTextField txtGiaSanPham;
    private javax.swing.JTextField txtGiaTu;
    private javax.swing.JTextField txtKichThuoc;
    private javax.swing.JTextField txtTenSanPham;
    private javax.swing.JTextField txtTimKiemSanPham;
    private javax.swing.JTextField txtTonKho;
    // End of variables declaration//GEN-END:variables

    // ========== PRODUCT CONTROLLER IMPLEMENTATION ==========
    private ProductDAOImpl_Nghia productDAO = new ProductDAOImpl_Nghia();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
    private ProductReviewDAO productReviewDAO = new ProductReviewDAOImpl();
    private int currentRow = -1;
    private List<Product_Nghia> productList;
    private List<Product_Nghia> currentTypeList = new java.util.ArrayList<>();
    private int currentTypeIndex = -1;
    private Product_Nghia currentProductInChat = null; // Lưu sản phẩm đang được chat
    private List<ProductReview> currentProductReviews = new java.util.ArrayList<>(); // Lưu danh sách đánh giá hiện tại

    @Override
    public void open() {
        renderProductGrid(productDAO.selectAll());
        clear();
        setEditable(false); // Mặc định tắt quyền chỉnh sửa ở tab 2
        loadCategoriesToComboBox();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void setForm(Product_Nghia entity) {
        if (entity != null) {
            txtTenSanPham.setText(entity.getProductName() != null ? entity.getProductName() : "");
            txtGiaSanPham.setText(entity.getUnitPrice() != null ? entity.getUnitPrice().toString() : "");
         //   txtGiaKichThuoc.setText(entity.getUnitPrice() != null ? entity.getUnitPrice().toString() : "");
            txtTonKho.setText(entity.getQuantity() != null ? entity.getQuantity().toString() : "");
            txtAreaMoTa.setText(entity.getDescription() != null ? entity.getDescription().toString() : "");
            txtKichThuoc.setText(entity.getKichThuoc() != null ? entity.getKichThuoc().toString() : "");
            
            // Debug log để kiểm tra dữ liệu
            System.out.println("DEBUG DUYET SP: ProductID=" + entity.getProductId() + 
                             ", Quantity=" + entity.getQuantity() + 
                             ", ProductName=" + entity.getProductName());
            
            // Hiển thị ảnh sản phẩm
            if (entity.getImagePath() != null && !entity.getImagePath().isEmpty()) {
                ImageIcon icon = getProductImage(entity.getImagePath());
                if (icon != null) {
                    // Scale the image to fit the JLabel dimensions
                    Image image = icon.getImage();
                    int labelWidth = lblHinhAnh.getWidth();
                    int labelHeight = lblHinhAnh.getHeight();
                    if (labelWidth > 0 && labelHeight > 0) {
                        Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
                        lblHinhAnh.setIcon(new ImageIcon(scaledImage));
                    } else {
                        // Default dimensions if label size is not available
                        Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        lblHinhAnh.setIcon(new ImageIcon(scaledImage));
                    }
                }
            } else {
                lblHinhAnh.setIcon(null);
            }
            
            // Load đánh giá vào bảng
            loadProductReviews(entity.getProductId());
        }
    }

    @Override
    public Product_Nghia getForm() {
        // Không cần implement cho màn hình duyệt sản phẩm
        return null;
    }

    @Override
    public void fillToTable() {
        // Không có bảng, không cần fillToTable
    }

    @Override
    public void edit() {
        // Không có bảng, không có chức năng này
    }

    @Override
    public void create() {
        // Không cần implement cho màn hình duyệt sản phẩm
    }

    @Override
    public void update() {
        // Không cần implement cho màn hình duyệt sản phẩm
    }

    @Override
    public void delete() {
        // Không cần implement cho màn hình duyệt sản phẩm
    }

    @Override
    public void clear() {
        txtTenSanPham.setText("");
        txtGiaSanPham.setText("");
        txtTonKho.setText("");
        txtKichThuoc.setText("");
      //  txtGiaKichThuoc.setText("");
        txtAreaMoTa.setText("");
        lblHinhAnh.setIcon(null);
        currentRow = -1;
        
        // Clear bảng đánh giá
        DefaultTableModel emptyModel = new DefaultTableModel();
        emptyModel.addColumn("Tên người đánh giá");
        emptyModel.addColumn("Số sao");
        emptyModel.addColumn("Đánh giá");
        emptyModel.addColumn("Ngày đánh giá");
        tblDanhGia.setModel(emptyModel);
    }

    @Override
    public void setEditable(boolean editable) {
        txtTenSanPham.setEditable(editable);
        txtGiaSanPham.setEditable(editable);
        txtTonKho.setEditable(editable);
        txtKichThuoc.setEditable(editable);
       // txtGiaKichThuoc.setEditable(editable);
        txtAreaMoTa.setEditable(editable);
    }

    @Override
    public void checkAll() {
        // Không cần implement cho màn hình duyệt sản phẩm
    }

    @Override
    public void uncheckAll() {
        // Không cần implement cho màn hình duyệt sản phẩm
    }

    @Override
    public void deleteCheckedItems() {
        // Không cần implement cho màn hình duyệt sản phẩm
    }

    @Override
    public void moveFirst() {
        // Không có bảng, không có chức năng này
    }

    @Override
    public void movePrevious() {
        // Không có bảng, không có chức năng này
    }

    @Override
    public void moveNext() {
        // Không có bảng, không có chức năng này
    }

    @Override
    public void moveLast() {
        // Không có bảng, không có chức năng này
    }

    @Override
    public void moveTo(int rowIndex) {
        // Không có bảng, không có chức năng này
    }

    @Override
    public List<Category> loadAllCategories() {
        return categoryDAO.selectAll();
    }

    @Override
    public String uploadImage(String localImagePath) {
        // Không cần implement cho màn hình duyệt sản phẩm
        return null;
    }

    @Override
    public ImageIcon getProductImage(String imagePath) {
        return productDAO.getProductImage(imagePath);
    }

    @Override
    public List<Product_Nghia> searchByName(String name) {
        return productDAO.searchByName(name);
    }

    @Override
    public List<Product_Nghia> searchByPriceRange(BigDecimal min, BigDecimal max) {
        return productDAO.searchByPriceRange(min, max);
    }

    @Override
    public List<Product_Nghia> searchByCategory(String categoryId) {
        return productDAO.searchByCategory(categoryId);
    }

    // Phương thức hỗ trợ
    private String getCategoryName(String categoryId) {
        List<Category> categories = loadAllCategories();
        for (Category category : categories) {
            if (category.getCategoryId().equals(categoryId)) {
                return category.getCategoryName();
            }
        }
        return "Không xác định";
    }

    // Phương thức tìm kiếm sản phẩm
    private void searchProducts() {
        String searchText = txtTimKiemSanPham.getText().trim();
        if (searchText.isEmpty()) {
            renderProductGrid(productDAO.selectAll());
        } else {
            List<Product_Nghia> searchResults = searchByName(searchText);
            renderProductGrid(searchResults);
        }
    }

    private void loadCategoriesToComboBox() {
        cboLoaiSanPham.removeAllItems();
        cboLoaiSanPham.addItem("Tất cả"); // Thêm mục Tất cả
        List<Category> categories = loadAllCategories();
        for (Category c : categories) {
            cboLoaiSanPham.addItem(c.getCategoryName());
        }
    }

    private void renderProductGrid(List<Product_Nghia> products) {
        productGridPanel.removeAll();
        for (Product_Nghia product : products) {
            JPanel card = createProductCard(product);
            productGridPanel.add(card);
        }
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    private JPanel createProductCard(Product_Nghia product) {
        JPanel card = new JPanel();
        card.setPreferredSize(new java.awt.Dimension(160, 220));
        card.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY, 1));
        card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));
        card.setBackground(java.awt.Color.WHITE);

        // Ảnh sản phẩm (căn giữa, bo góc nhẹ)
        ImageIcon icon = getProductImage(product.getImagePath());
        JLabel imgLabel = new JLabel();
        imgLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(140, 90, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
            imgLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        }
        card.add(javax.swing.Box.createVerticalStrut(8));
        card.add(imgLabel);

        // Tên sản phẩm (căn giữa, font to, đậm, tối đa 2 dòng, không tràn)
        String name = product.getProductName() != null ? product.getProductName() : "";
        JLabel nameLabel = new JLabel("<html><div style='text-align:center; color:#1a237e; font-weight:bold; font-size:14px; max-width:140px; overflow:hidden; display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical; white-space:normal;'>" + name + "</div></html>");
        nameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        card.add(javax.swing.Box.createVerticalStrut(6));
        card.add(nameLabel);

        // Giá sản phẩm (căn giữa, font to, màu nổi bật, không tràn)
        String price = product.getUnitPrice() != null ? product.getUnitPrice().toString() : "";
        JLabel priceLabel = new JLabel("<html><div style='text-align:center; color:#d32f2f; font-weight:bold; font-size:16px; max-width:140px; overflow:hidden;'>" + price + " ₫</div></html>");
        priceLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        card.add(javax.swing.Box.createVerticalStrut(6));
        card.add(priceLabel);

        card.add(javax.swing.Box.createVerticalGlue());

        // Sự kiện click: chỉ chọn sản phẩm, không hỏi AI
        card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    // Bỏ hiệu ứng ở các card khác
                    for (Component comp : productGridPanel.getComponents()) {
                        if (comp instanceof JPanel) {
                            ((JPanel) comp).setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY, 1));
                        }
                    }
                    // Đổi border card này
                    card.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 118, 210), 2));
                    
                    // Lưu sản phẩm được chọn để nút AI sử dụng
                    currentProductInChat = product;
                } else if (evt.getClickCount() == 2) {
                    setForm(product);
                    // Lấy danh sách cùng loại
                    String typeId = product.getCategoryId();
                    currentTypeList = searchByCategory(typeId);
                    // Tìm index của sản phẩm đang chọn trong danh sách này
                    currentTypeIndex = -1;
                    for (int i = 0; i < currentTypeList.size(); i++) {
                        if (currentTypeList.get(i).getProductId().equals(product.getProductId())) {
                            currentTypeIndex = i;
                            break;
                        }
                    }
                    jTabbedPane1.setSelectedIndex(1);
                }
            }
        });

        return card;
    }

    // Thêm phương thức hỗ trợ hiệu ứng hover cho nút
    private void addHoverEffect(javax.swing.JButton button, java.awt.Color hoverColor) {
        java.awt.Color original = button.getBackground();
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(original);
            }
        });
    }

    // Thêm sự kiện cho btnTimGia
    private void timGiaActionPerformed(java.awt.event.ActionEvent evt) {
        
    }
    
    // Phương thức load đánh giá sản phẩm vào bảng
    private void loadProductReviews(String productId) {
        try {
            // Lấy danh sách đánh giá từ database
            List<ProductReview> reviews = productReviewDAO.getReviewsByProduct(productId);
            
            // Lưu danh sách đánh giá hiện tại
            currentProductReviews.clear();
            currentProductReviews.addAll(reviews);
            
            // Tạo model cho bảng
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Tên người đánh giá");
            model.addColumn("Số sao");
            model.addColumn("Đánh giá");
            model.addColumn("Ngày đánh giá");
            
            // Thêm dữ liệu vào bảng
            for (ProductReview review : reviews) {
                String userName = review.getUserName() != null ? review.getUserName() : "Không xác định";
                String starRating = getStarRating(review.getRating());
                
                // Sửa lỗi font cho comment
                String comment = review.getComment() != null ? review.getComment() : "";
                try {
                    // Thử chuyển đổi encoding nếu có vấn đề
                    byte[] bytes = comment.getBytes("ISO-8859-1");
                    comment = new String(bytes, "UTF-8");
                } catch (Exception e) {
                    // Nếu lỗi, giữ nguyên text gốc
                    comment = review.getComment() != null ? review.getComment() : "";
                }
                
                String reviewDate = review.getReviewDate() != null ? 
                    review.getReviewDate().toString().substring(0, 19) : "";
                
                model.addRow(new Object[]{userName, starRating, comment, reviewDate});
            }
            
            // Cập nhật bảng
            tblDanhGia.setModel(model);
            
            // Hiển thị thông tin tổng quan
            double avgRating = productReviewDAO.getAverageRating(productId);
            int reviewCount = productReviewDAO.getReviewCount(productId);
            
            // Có thể hiển thị thông tin này ở đâu đó trong UI
            System.out.println("Sản phẩm: " + productId + 
                             " - Đánh giá trung bình: " + String.format("%.1f", avgRating) + 
                             " - Số lượng đánh giá: " + reviewCount);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi load đánh giá: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Phương thức chuyển đổi số sao thành ký tự sao
    private String getStarRating(Byte rating) {
        if (rating == null) return "☆☆☆☆☆";
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("★"); // Sử dụng ký tự Unicode sao đặc
        }
        // Thêm sao rỗng cho đủ 5 sao
        for (int i = rating; i < 5; i++) {
            stars.append("☆"); // Sử dụng ký tự Unicode sao rỗng
        }
        return stars.toString();
    }
    
    // Phương thức hỏi AI về thông tin sản phẩm
    private void askAIAboutProduct(Product_Nghia product) {
        // Lưu sản phẩm hiện tại để AI nhớ ngữ cảnh
        currentProductInChat = product;
        
        // Chuyển sang tab chat AI
        jTabbedPane1.setSelectedIndex(0);
        
        // Lấy đầy đủ thông tin sản phẩm
        String productName = product.getProductName() != null ? product.getProductName() : "";
        String productPrice = product.getUnitPrice() != null ? product.getUnitPrice().toString() : "";
        String productDescription = product.getDescription() != null ? product.getDescription() : "";
        String productSize = product.getKichThuoc() != null ? product.getKichThuoc() : "";
        String productStock = product.getQuantity() != null ? product.getQuantity().toString() : "";
        String productCategory = getCategoryName(product.getCategoryId());
        
        // Hiển thị thông báo đang hỏi AI
        jTextArea1.append("\n[AI]: Đang phân tích sản phẩm '" + productName + "'...\n");
        
        // Sử dụng streaming response
        String prompt = String.format(
            "Phân tích chi tiết sản phẩm:\n" +
            "- Tên: %s\n" +
            "- Danh mục: %s\n" +
            "- Giá: %s VNĐ\n" +
            "- Kích thước: %s\n" +
            "- Tồn kho: %s sản phẩm\n" +
            "- Mô tả: %s\n\n" +
            "Hãy đưa ra nhận xét chi tiết về sản phẩm này, bao gồm:\n" +
            "1. Đánh giá về chất lượng và thiết kế\n" +
            "2. Phù hợp với không gian nào\n" +
            "3. So sánh giá cả với thị trường\n" +
            "4. Khuyến nghị cho khách hàng",
            productName, productCategory, productPrice, productSize, productStock, productDescription
        );
        
        poly.util.OpenAIClient.getAIResponseStream(prompt, 
            (chunk) -> {
                // Hiển thị từng chunk
                javax.swing.SwingUtilities.invokeLater(() -> {
                    jTextArea1.append(chunk);
                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                });
            },
            (complete) -> {
                // Hoàn thành
                javax.swing.SwingUtilities.invokeLater(() -> {
                    jTextArea1.append("\n");
                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                });
            },
            (error) -> {
                // Lỗi - sử dụng fallback
                javax.swing.SwingUtilities.invokeLater(() -> {
                    String fallbackResponse = String.format(
                        "Sản phẩm '%s' thuộc danh mục %s có giá %s VNĐ.\n\n" +
                        "Thông tin chi tiết:\n" +
                        "- Kích thước: %s\n" +
                        "- Tồn kho: %s sản phẩm\n" +
                        "- Mô tả: %s\n\n" +
                        "Đây là một sản phẩm nội thất chất lượng cao, phù hợp cho không gian hiện đại. " +
                        "Với kích thước này, sản phẩm sẽ phù hợp cho phòng khách hoặc văn phòng. " +
                        "Bạn có muốn biết thêm thông tin về cách bảo quản hoặc tư vấn mua hàng không?",
                        productName, productCategory, productPrice, productSize, productStock, productDescription
                    );
                    jTextArea1.append("[AI]: " + fallbackResponse + "\n");
                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                });
            }
        );
    }
    
    // Phương thức hiển thị chi tiết đánh giá
    private void showReviewDetails() {
        int selectedRow = tblDanhGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đánh giá để xem chi tiết!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (selectedRow >= currentProductReviews.size()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin đánh giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ProductReview selectedReview = currentProductReviews.get(selectedRow);
        
        // Tạo dialog hiển thị chi tiết đánh giá
        javax.swing.JDialog detailDialog = new javax.swing.JDialog(this, "Chi tiết đánh giá", true);
        detailDialog.setLayout(new java.awt.BorderLayout());
        detailDialog.setSize(500, 400);
        detailDialog.setLocationRelativeTo(this);
        
        // Panel chính
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Thông tin người đánh giá
        javax.swing.JPanel userPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        javax.swing.JLabel userLabel = new javax.swing.JLabel("Người đánh giá:");
        userLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        javax.swing.JLabel userNameLabel = new javax.swing.JLabel(selectedReview.getUserName() != null ? selectedReview.getUserName() : "Không xác định");
        userNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
        userPanel.add(userLabel);
        userPanel.add(userNameLabel);
        
        // Số sao - Sửa lỗi hiển thị sao
        javax.swing.JPanel ratingPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        javax.swing.JLabel ratingLabel = new javax.swing.JLabel("Số sao:");
        ratingLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        
        // Tạo label hiển thị số sao bằng ký tự Unicode
        String starRating = "";
        if (selectedReview.getRating() != null) {
            int rating = selectedReview.getRating();
            for (int i = 0; i < rating; i++) {
                starRating += "★"; // Sử dụng ký tự Unicode sao đặc
            }
            // Thêm sao rỗng cho đủ 5 sao
            for (int i = rating; i < 5; i++) {
                starRating += "☆"; // Sử dụng ký tự Unicode sao rỗng
            }
        } else {
            starRating = "☆☆☆☆☆"; // 5 sao rỗng nếu không có đánh giá
        }
        
        javax.swing.JLabel starLabel = new javax.swing.JLabel(starRating);
        starLabel.setFont(new java.awt.Font("Segoe UI", 0, 18));
        starLabel.setForeground(new java.awt.Color(255, 193, 7)); // Màu vàng cho sao
        ratingPanel.add(ratingLabel);
        ratingPanel.add(starLabel);
        
        // Ngày đánh giá
        javax.swing.JPanel datePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        javax.swing.JLabel dateLabel = new javax.swing.JLabel("Ngày đánh giá:");
        dateLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        String reviewDate = selectedReview.getReviewDate() != null ? 
            selectedReview.getReviewDate().toString().substring(0, 19) : "Không xác định";
        javax.swing.JLabel dateValueLabel = new javax.swing.JLabel(reviewDate);
        dateValueLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
        datePanel.add(dateLabel);
        datePanel.add(dateValueLabel);
        
        // Nội dung đánh giá - Sửa lỗi font
        javax.swing.JPanel commentPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JLabel commentLabel = new javax.swing.JLabel("Nội dung đánh giá:");
        commentLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        commentLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        javax.swing.JTextArea commentArea = new javax.swing.JTextArea();
        String commentText = selectedReview.getComment() != null ? selectedReview.getComment() : "Không có nội dung đánh giá";
        
        // Sửa lỗi font bằng cách chuyển đổi encoding
        try {
            // Thử chuyển đổi từ UTF-8 nếu có vấn đề encoding
            byte[] bytes = commentText.getBytes("ISO-8859-1");
            commentText = new String(bytes, "UTF-8");
        } catch (Exception e) {
            // Nếu lỗi, giữ nguyên text gốc
            commentText = selectedReview.getComment() != null ? selectedReview.getComment() : "Không có nội dung đánh giá";
        }
        
        commentArea.setText(commentText);
        commentArea.setEditable(false);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setFont(new java.awt.Font("Segoe UI", 0, 14));
        commentArea.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
        
        javax.swing.JScrollPane commentScrollPane = new javax.swing.JScrollPane(commentArea);
        commentScrollPane.setPreferredSize(new java.awt.Dimension(450, 150));
        
        commentPanel.add(commentLabel, java.awt.BorderLayout.NORTH);
        commentPanel.add(commentScrollPane, java.awt.BorderLayout.CENTER);
        
        // Thêm các panel vào panel chính
        mainPanel.add(userPanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(15));
        mainPanel.add(ratingPanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(15));
        mainPanel.add(datePanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(20));
        mainPanel.add(commentPanel);
        
        // Nút đóng
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        javax.swing.JButton closeButton = new javax.swing.JButton("Đóng");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailDialog.dispose();
            }
        });
        buttonPanel.add(closeButton);
        
        // Thêm vào dialog
        detailDialog.add(mainPanel, java.awt.BorderLayout.CENTER);
        detailDialog.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        
        // Hiển thị dialog
        detailDialog.setVisible(true);
    }
}
