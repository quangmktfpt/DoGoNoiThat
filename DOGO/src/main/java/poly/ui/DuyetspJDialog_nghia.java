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

/**
 *
 * @author Nghia
 */
public class DuyetspJDialog_nghia extends javax.swing.JDialog implements ProductController_nghia {

    /**
     * Creates new form DuyetspJDialog
     */
    public DuyetspJDialog_nghia(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        // Đảm bảo AI trả lời luôn xuống dòng, không kéo ngang
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        
        // Cấu hình TextArea nhập liệu hiển thị theo chiều dọc
        jTextArea2.setLineWrap(true);
        jTextArea2.setWrapStyleWord(true);
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
                    jButton1.doClick();
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
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String userMessage = jTextArea2.getText().trim();
                if (userMessage.isEmpty()) return;
                jTextArea1.append("\n[Bạn]: " + userMessage + "\n[AI]: ");
                jTextArea2.setText("");
                jButton1.setEnabled(false);
                jTextArea2.setEnabled(false);
                
                // Sử dụng streaming response
                if (currentProductInChat != null) {
                    String productContext = String.format(
                        "Ngữ cảnh: Bạn đang hỏi về sản phẩm '%s' với thông tin:\n" +
                        "- Mô tả: %s\n" +
                        "- Giá: %s\n" +
                        "- Kích thước: %s\n" +
                        "- Tồn kho: %s\n\n" +
                        "Câu hỏi của bạn: %s",
                        currentProductInChat.getProductName() != null ? currentProductInChat.getProductName() : "Không có tên",
                        currentProductInChat.getDescription() != null ? currentProductInChat.getDescription() : "Không có mô tả",
                        currentProductInChat.getUnitPrice() != null ? currentProductInChat.getUnitPrice().toString() + " VNĐ" : "Không có giá",
                        currentProductInChat.getKichThuoc() != null ? currentProductInChat.getKichThuoc() : "Không có thông tin",
                        currentProductInChat.getQuantity() != null ? currentProductInChat.getQuantity().toString() + " sản phẩm" : "Không có thông tin",
                        userMessage
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
                                jButton1.setEnabled(true);
                                jTextArea2.setEnabled(true);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                                jTextArea2.requestFocusInWindow();
                            });
                        },
                        (error) -> {
                            // Lỗi
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                jTextArea1.append("[Lỗi]: " + error + "\n");
                                jButton1.setEnabled(true);
                                jTextArea2.setEnabled(true);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                            });
                        }
                    );
                } else {
                    poly.util.OpenAIClient.getAIResponseStream(userMessage, 
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
                                jButton1.setEnabled(true);
                                jTextArea2.setEnabled(true);
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                                jTextArea2.requestFocusInWindow();
                            });
                        },
                        (error) -> {
                            // Lỗi
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                jTextArea1.append("[Lỗi]: " + error + "\n");
                                jButton1.setEnabled(true);
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
        jButton1 = new javax.swing.JButton();
        btnXoaNguCanh = new javax.swing.JButton();
        btnXoaDoanChat = new javax.swing.JButton();
        btnThongTinAI = new javax.swing.JButton();
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

        jButton1.setText("Gửi");

        btnXoaNguCanh.setText("Xoá ngữ cảnh");
        btnXoaNguCanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNguCanhActionPerformed(evt);
            }
        });

        btnXoaDoanChat.setText("Xoá đoạn chat");
        btnXoaDoanChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaDoanChatActionPerformed(evt);
            }
        });

        btnThongTinAI.setText("AI");
        btnThongTinAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongTinAIActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaNguCanh, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaDoanChat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThongTinAI, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnXoaNguCanh)
                    .addComponent(btnXoaDoanChat)
                    .addComponent(btnThongTinAI)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(325, 325, 325))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(productScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
                .addContainerGap(89, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtTonKho, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtTenSanPham)
                                    .addComponent(txtGiaSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKichThuoc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(311, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(339, 339, 339))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnMoveFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMovePrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMoveNext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMoveLast, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(440, 440, 440)
                .addComponent(btnThemVaoGio)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(101, 101, 101)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtTonKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMoveFirst)
                    .addComponent(btnMovePrevious)
                    .addComponent(btnMoveNext)
                    .addComponent(btnMoveLast))
                .addGap(4, 4, 4)
                .addComponent(btnThemVaoGio)
                .addContainerGap(312, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Chi tiết", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
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

    private void btnXoaNguCanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNguCanhActionPerformed
        currentProductInChat = null;
        jTextArea1.append("\n[AI]: Đã xóa ngữ cảnh sản phẩm. Bạn có thể chat chung với tôi.\n");
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }//GEN-LAST:event_btnXoaNguCanhActionPerformed

    private void btnXoaDoanChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaDoanChatActionPerformed
        jTextArea1.setText("");
        currentProductInChat = null;
        jTextArea1.append("[AI]: Đã xóa toàn bộ đoạn chat. Bạn có thể bắt đầu cuộc trò chuyện mới.\n");
    }//GEN-LAST:event_btnXoaDoanChatActionPerformed

    private void btnThongTinAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongTinAIActionPerformed
        if (currentProductInChat != null) {
            // Hỏi AI về sản phẩm đang được chọn
            askAIAboutProduct(currentProductInChat);
        } else {
            // Nếu chưa chọn sản phẩm nào
            jTabbedPane1.setSelectedIndex(0);
            jTextArea1.append("\n[AI]: Vui lòng chọn một sản phẩm trước khi hỏi tôi về nó.\n");
            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        }
    }//GEN-LAST:event_btnThongTinAIActionPerformed

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
            java.util.logging.Logger.getLogger(DuyetspJDialog_nghia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DuyetspJDialog_nghia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DuyetspJDialog_nghia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DuyetspJDialog_nghia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DuyetspJDialog_nghia dialog = new DuyetspJDialog_nghia(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnMoveFirst;
    private javax.swing.JButton btnMoveLast;
    private javax.swing.JButton btnMoveNext;
    private javax.swing.JButton btnMovePrevious;
    private javax.swing.JButton btnThemVaoGio;
    private javax.swing.JButton btnThongTinAI;
    private javax.swing.JButton btnTimGia;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTimLoai;
    private javax.swing.JButton btnXoaDoanChat;
    private javax.swing.JButton btnXoaNguCanh;
    private javax.swing.JComboBox<String> cboLoaiSanPham;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel lblHinhAnh;
    private javax.swing.JPanel productGridPanel;
    private javax.swing.JScrollPane productScrollPane;
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
    private int currentRow = -1;
    private List<Product_Nghia> productList;
    private List<Product_Nghia> currentTypeList = new java.util.ArrayList<>();
    private int currentTypeIndex = -1;
    private Product_Nghia currentProductInChat = null; // Lưu sản phẩm đang được chat

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
    
    // Phương thức hỏi AI về thông tin sản phẩm
    private void askAIAboutProduct(Product_Nghia product) {
        // Lưu sản phẩm hiện tại để AI nhớ ngữ cảnh
        currentProductInChat = product;
        
        // Chuyển sang tab chat AI
        jTabbedPane1.setSelectedIndex(0);
        
        // Tạo prompt cho AI
        String productName = product.getProductName() != null ? product.getProductName() : "";
        String productDescription = product.getDescription() != null ? product.getDescription() : "";
        String productPrice = product.getUnitPrice() != null ? product.getUnitPrice().toString() : "";
        String productSize = product.getKichThuoc() != null ? product.getKichThuoc() : "";
        String productStock = product.getQuantity() != null ? product.getQuantity().toString() : "";
        
        // Hiển thị thông báo đang hỏi AI
        jTextArea1.append("\n[AI]: Phân tích sản phẩm '" + productName + "': ");
        
        // Sử dụng streaming response
        poly.util.OpenAIClient.getProductInfoAIStream(productName, productDescription, productPrice, productSize, productStock,
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
                // Lỗi
                javax.swing.SwingUtilities.invokeLater(() -> {
                    jTextArea1.append("[Lỗi]: " + error + "\n");
                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                });
            }
        );
    }
}
