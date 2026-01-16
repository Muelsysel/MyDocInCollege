import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FramePaper {
    private JFrame frame;
    private JButton createExamBtn;
    private JButton deleteExamBtn;
    private JPanel previewPanel;
    private JLabel[] examLabels;


    private ExamManager examManager;

    public FramePaper() {
        examManager = new ExamManager();
        initializeUI();
        this.show();
    }

    private void initializeUI() {
        frame = new JFrame("试卷管理");
        frame.setBounds(100, 100, 350, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        //添加按钮
        JPanel btnPanel = new JPanel(new FlowLayout());
        createExamBtn = new JButton("新建试卷");
        createExamBtn.addActionListener(new CreateExamListener());
        deleteExamBtn = new JButton("删除试卷");
        deleteExamBtn.addActionListener(new DeleteExamListener());
        btnPanel.add(createExamBtn);
        btnPanel.add(deleteExamBtn);

        previewPanel = new JPanel(new GridLayout(10, 1));
        previewPanel.setBorder(BorderFactory.createTitledBorder("试卷列表"));
        previewPanel.setPreferredSize(new Dimension(300, 550));
        examLabels = new JLabel[10];
        for (int i = 0; i < examLabels.length; i++) {
            examLabels[i] = new JLabel("");
            previewPanel.add(examLabels[i]);
        }
        initExamList();

        JScrollPane scrollPane = new JScrollPane(previewPanel);
        scrollPane.setPreferredSize(new Dimension(300, 550));

        mainPanel.add(btnPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setLocation(700,250);
    }

    private void show() {
        frame.setVisible(true);
    }

    private void initExamList() {
        try {
            String[] examList = examManager.getExamList();
            for (int i = 0; i < examLabels.length; i++) {
                if (i < examList.length) {
                    examLabels[i].setText(examList[i]);
                } else {
                    examLabels[i].setText("");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "获取试卷列表失败：" + e.getMessage());
        }
    }

    private class CreateExamListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String examName = JOptionPane.showInputDialog(frame, "请输入试卷名称：");
            if (examName != null && !examName.trim().isEmpty()) {
                try {
                    examManager.createExam(examName.trim());
                    initExamList();
                    JOptionPane.showMessageDialog(frame, "新建试卷成功！");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "新建试卷失败：" + ex.getMessage());
                }
            }
        }
    }

    private class DeleteExamListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String examName = JOptionPane.showInputDialog(frame, "请输入试卷名称：");
            if (examName != null && !examName.trim().isEmpty()) {
                int confirmResult = JOptionPane.showConfirmDialog(frame, "确定要删除试卷 " + examName + " 吗？", "删除试卷", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    try {
                        examManager.deleteExam(examName.trim());
                        initExamList();
                        JOptionPane.showMessageDialog(frame, "删除试卷成功！");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "删除试卷失败：" + ex.getMessage());
                    }
                }
            }
        }
    }

class ExamManager {
    private Connection conn;

    public ExamManager() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 抽取试卷
    public void createExam(String examName) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE ");
        sqlBuilder.append(examName).append(" (id CHAR(5) PRIMARY KEY,");
        sqlBuilder.append("type CHAR(10), stem CHAR(200), a CHAR(40), b CHAR(40), c CHAR(40), d CHAR(40), answer CHAR(5), solution CHAR(200));");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sqlBuilder.toString());
        PreparedStatement pstmt;
        try {
            // 加载所有非多选题
            Statement zstmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet  rs = zstmt.executeQuery("SELECT * FROM allquestion WHERE type!='多选题' ORDER BY RAND() LIMIT 45");
            while (rs.next()) {
                    String id = rs.getString("id");
                    String type = rs.getString("type");
                    String stem = rs.getString("stem");
                    String a = rs.getString("a");
                    String b = rs.getString("b");
                    String c = rs.getString("c");
                    String d = rs.getString("d");
                    String answer = rs.getString("answer");
                    String solution = rs.getString("solution");
                    addQuestion(examName,id, type, stem, a, b, c, d, answer, solution);
            }
                // 加载多选题
                rs = zstmt.executeQuery("SELECT * FROM allquestion WHERE type='多选题' ORDER BY RAND() LIMIT 5");
                while (rs.next()) {
                    String id = rs.getString("id");
                    String type = rs.getString("type");
                    String stem = rs.getString("stem");
                    String a = rs.getString("a");
                    String b = rs.getString("b");
                    String c = rs.getString("c");
                    String d = rs.getString("d");
                    String answer = rs.getString("answer");
                    String solution = rs.getString("solution");
                    addQuestion(examName,id, type, stem, a, b, c, d, answer, solution);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }


    // 增加试题
    public void addQuestion(String tableName, String id, String type, String stem, String a, String b, String c, String d, String answer, String solution) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO " + tableName + " (id, type, stem, a, b, c, d, answer, solution) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
        pstmt.setString(1, id);
        pstmt.setString(2, type);
        pstmt.setString(3, stem);
        pstmt.setString(4, a);
        pstmt.setString(5, b);
        pstmt.setString(6, c);
        pstmt.setString(7, d);
        pstmt.setString(8, answer);
        pstmt.setString(9, solution);
        pstmt.executeUpdate();
    }


    // 删除试卷
    public void deleteExam(String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DROP TABLE " + tableName + ";");
    }

    // 获取试卷列表
    public String[] getExamList() throws SQLException {
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery("SHOW TABLES;");
        rs.last();
        int rowCount = rs.getRow();
        rs.beforeFirst();

        String[] examList = new String[rowCount];
        int index = 0;
        while (rs.next()) {
            String tableName = rs.getString(1);
            if (!tableName.equals("allquestion")) {
                examList[index++] = tableName;
            }
        }
        return examList;
    }


}
}
