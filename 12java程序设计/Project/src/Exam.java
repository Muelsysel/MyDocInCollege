import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Exam extends JFrame {
    private String id;
    public Exam(String id) {
        super("C1小型汽车科目四模拟考试系统");
        this.id=id;
        // 设置窗口大小和布局
        setSize(400, 300);
        setLayout(new GridLayout(2, 2, 10, 10));
        setResizable(false);

        // 添加“模拟考试”按钮
        JButton examBtn = new JButton("模拟考试");
        examBtn.setBackground(new Color(97, 174, 239));
        examBtn.setForeground(Color.WHITE);
        examBtn.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        examBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        examBtn.addActionListener(e -> enterExam());
        add(examBtn);
        // 添加“历史成绩”按钮
        JButton historyBtn = new JButton("历史成绩");
        historyBtn.setBackground(new Color(255, 202, 40));
        historyBtn.setForeground(Color.WHITE);
        historyBtn.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        historyBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        historyBtn.addActionListener(e -> showHistory());
        add(historyBtn);

        // 设置窗口居中显示并可见
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 进入模拟考试
    private void enterExam() {
        new PracticeExam(id);
    }

    // 显示历史成绩
    private void showHistory() {
        new FrameHistory(id);
    }
}
class FrameHistory extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private String id;

    public FrameHistory(String id) {
        this.id = id;

        // 设置窗口大小和位置
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 创建表格模型和表格组件
        model = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "时间", "分数"});
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 创建滚动面板，并将表格添加到其中
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 500));

        // 将滚动面板添加到内容面板中央
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 设置窗口可见性
        setVisible(true);

        // 刷新表格数据
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);

        // 使用 try-with-resource 语句，自动关闭连接和声明
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
             PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM grade WHERE id = ?")) {

            // 设置查询参数
            stmt1.setString(1, id);

            // 执行查询
            ResultSet rs = stmt1.executeQuery();

            // 添加数据库中所有的记录到表格中
            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getString("id"),
                        rs.getTimestamp("time"),
                        rs.getString("grade")
                };
                model.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

