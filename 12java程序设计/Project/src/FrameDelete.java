import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FrameDelete extends JFrame{

    private JTextField idField;

    public FrameDelete() {
        super("删除题目");
        deleteUI();
    }

    private void deleteUI() {
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
        if(id.isEmpty()){
            showError("请填写需要删除题目的id");
            return;
        }

        // 从数据库中删除对应的题目
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
            stmt1 = conn.prepareStatement("SELECT * FROM allquestion WHERE id = ?");
            stmt1.setString(1, id);
            ResultSet rs1 = stmt1.executeQuery();
            if (!rs1.next()) {
                showError("没有找到该题目");
                return;
            }

            stmt2 = conn.prepareStatement("DELETE FROM allquestion WHERE id = ?");
            stmt2.setString(1, id);
            int result = stmt2.executeUpdate();

            if(result > 0) {
                showInfo("删除题目成功！\n\n编号：" + rs1.getLong("id") +
                        "\n类型：" + rs1.getString("type") +
                        "\n题目：" + rs1.getString("stem") +
                        "\n答案：" + rs1.getString("answer"));
            } else {
                showError("删除题目失败！请重试。");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (stmt1 != null) {
                    stmt1.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
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