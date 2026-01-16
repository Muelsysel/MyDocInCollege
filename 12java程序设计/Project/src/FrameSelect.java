import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class FrameSelect extends JFrame {
    private JTextField idField;
    private JLabel  imageLabel;

    public FrameSelect() {
        super("查询题目");
        initUI();
    }

    private void initUI() {
        // 创建主面板
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setPreferredSize(new Dimension(400, 50));
        JLabel idLabel = new JLabel("题目编号：", SwingConstants.RIGHT);
        idLabel.setFont(new Font("宋体", Font.PLAIN, 16));
        inputPanel.add(idLabel);
        idField = new JTextField(15);
        idField.setToolTipText("请输入题目编号");
        inputPanel.add(idField);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton confirmButton = new JButton("确认");
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(28, 132, 198));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        confirmButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        confirmButton.addActionListener(e -> confirmAction());
        buttonPanel.add(confirmButton);
        JButton cancelButton = new JButton("取消");
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(28, 132, 198));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        // 组装面板
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel);

        pack();
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void confirmAction() {
        // 获取输入值
        String id = idField.getText().trim();

        // 检查输入值是否为空
        if (id.isEmpty()) {
            showError("请填写需要查询题目的id");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt1 = null;
        try {
            // 建立数据库连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");

            // 查询题目
            stmt1 = conn.prepareStatement("SELECT * FROM allquestion WHERE id = ?");
            stmt1.setString(1, id);
            ResultSet rs1 = stmt1.executeQuery();

            // 如果找到题目，则显示题目信息和图片；否则提示用户“查询失败”
            if (rs1.next()) {
                showInfo("查询题目成功！\n\n编号：" + rs1.getLong("id") + "\n类型：" + rs1.getString("type") + "\n题目："
                        + rs1.getString("stem") + "\n选项A:" + rs1.getString("a") + "\n选项B:" + rs1.getString("b")
                        + "\n选项C:" + rs1.getString("c") + "\n选项D:" + rs1.getString("d") + "\n答案："
                        + rs1.getString("answer") + "\n解释:" + rs1.getString("solution"));

                // 显示图片
                byte[] imageData = rs1.getBytes("image");
                if (imageData != null) {
                    ImageIcon imageIcon = new ImageIcon(imageData);
                    imageLabel.setIcon(imageIcon);
                } else {
                    imageLabel.setIcon(null);
                }
            } else {
                showError("查询题目失败！请重试。");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭数据库连接
            try {
                if (stmt1 != null) {
                    stmt1.close();
                }
            } catch (SQLException e) {
                showError(e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}