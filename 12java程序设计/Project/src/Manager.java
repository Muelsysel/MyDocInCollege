import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public Manager() {
        super("C1小型汽车科目四模拟考试题库管理系统");
        initComponents();
        refreshTable();
        setVisible(true);
    }

    private void initComponents() {
        // 数据预览
        model = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "类型", "题目内容", "选项A", "选项B", "选项C", "选项D", "答案","解释"});
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1000, 450));

        // 增删改查导入导出等按钮
        JButton addButton = new JButton("增加");
        JButton deleteButton = new JButton("删除");
        JButton updateButton = new JButton("修改");
        JButton searchButton = new JButton("查询");
        JButton importButton = new JButton("导入");
        JButton exportButton = new JButton("导出");
        JButton paperButton = new JButton("试卷管理");
        //刷新按钮
        JButton refreshButton = new JButton("刷新");


        // 添加按钮监听器，点击后打开相应的子界面
        addButton.addActionListener(e -> {
            new FrameAdd();
        });
        deleteButton.addActionListener(e -> {
            new FrameDelete();
        });
        updateButton.addActionListener(e -> {
            new FrameModify();
        });
        searchButton.addActionListener(e -> {
            new FrameSelect();
        });
        importButton.addActionListener(e -> {
            new FrameImport();
        });
        exportButton.addActionListener(e -> {
            new FrameExport();
        });
        paperButton.addActionListener(e->{
                new FramePaper();
        });
        refreshButton.addActionListener(e->{
            refreshTable();
        });

        // 将按钮和数据预览添加到窗口中
        JPanel contentPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(refreshButton);
        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel);

        // 添加题目预览区域
        JTextArea previewArea = new JTextArea(10, 60);
        JScrollPane previewScrollPane = new JScrollPane(previewArea);
        previewScrollPane.setPreferredSize(new Dimension(1000, 200));
        contentPanel.add(previewScrollPane, BorderLayout.SOUTH);


        // 在表格上添加行选择监听器，当用户点击某一行时，在预览区域显示该行的题目内容
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String stem = (String) model.getValueAt(selectedRow, 2);
                String A =(String) model.getValueAt(selectedRow, 3);
                String B =(String) model.getValueAt(selectedRow, 4);
                String C =(String) model.getValueAt(selectedRow, 5);
                String D =(String) model.getValueAt(selectedRow, 6);
                String E =(String) model.getValueAt(selectedRow, 7);
                String F =(String) model.getValueAt(selectedRow, 8);
                String text = "题目：" + stem + "\n" +
                        "A：" + A + "\n" +
                        "B：" + B + "\n" +
                        "C：" + C + "\n" +
                        "D：" + D + "\n" +
                        "答案："+ E +"\n" +
                        "解析:"+ F;
                previewArea.setText(text);
            }
        });

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // 初始化主窗口，显示数据库中所有数据
    private void refreshTable() {
        model.setRowCount(0);
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM allquestion")
        ) {
            // 添加数据库中所有的记录到表格中
            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("stem"),
                        rs.getString("a"),
                        rs.getString("b"),
                        rs.getString("c"),
                        rs.getString("d"),
                        rs.getString("answer"),
                        rs.getString("solution")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public DefaultTableModel getModel() {
        return model;
    }
}