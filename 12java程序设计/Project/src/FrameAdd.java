import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;

public class FrameAdd extends JFrame {
    private JTextField idField;
    private JTextField typeField;
    private JTextField stemField;
    private JTextField optionAField;
    private JTextField optionBField;
    private JTextField optionCField;
    private JTextField optionDField;
    private JTextField answerField;
    private JTextField solutionField;
    private JLabel imageLabel;
    private byte[] imageData;


    public FrameAdd() {
        super("添加题目");
        addUI();
    }

    private void addUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(255, 255, 255));

        // 输入内容
        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(650, 320));
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new GridLayout(10, 2, 20, 10));
        inputPanel.add(new JLabel("题目编号："));
        idField = new JTextField(10);
        idField.setToolTipText("请输入题目编号");
        inputPanel.add(idField);
        inputPanel.add(new JLabel("题目类型："));
        typeField = new JTextField(10);
        typeField.setToolTipText("请输入题目类型");
        inputPanel.add(typeField);
        inputPanel.add(new JLabel("题目内容："));
        stemField = new JTextField(200);
        stemField.setToolTipText("请输入题目内容");
        inputPanel.add(stemField);
        inputPanel.add(new JLabel("选项A："));
        optionAField = new JTextField(30);
        optionAField.setToolTipText("请输入选项A");
        inputPanel.add(optionAField);
        inputPanel.add(new JLabel("选项B："));
        optionBField = new JTextField(30);
        optionBField.setToolTipText("请输入选项B");
        inputPanel.add(optionBField);
        inputPanel.add(new JLabel("选项C："));
        optionCField = new JTextField(30);
        optionCField.setToolTipText("请输入选项C");
        inputPanel.add(optionCField);
        inputPanel.add(new JLabel("选项D："));
        optionDField = new JTextField(30);
        optionDField.setToolTipText("请输入选项D");
        inputPanel.add(optionDField);
        inputPanel.add(new JLabel("答案："));
        answerField = new JTextField(10);
        answerField.setToolTipText("请输入答案");
        inputPanel.add(answerField);
        inputPanel.add(new JLabel("解析："));
        solutionField = new JTextField(200);
        solutionField.setToolTipText("请输入解析");
        inputPanel.add(solutionField);

        // 上传图片
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        imagePanel.setOpaque(false);
        JButton uploadButton = new JButton("上传图片");
        uploadButton.setBackground(new Color(46, 134, 193));
        uploadButton.setForeground(new Color(255, 255, 255));
        uploadButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        uploadButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    imageData = Files.readAllBytes(selectedFile.toPath());
                    ImageIcon imageIcon = new ImageIcon(imageData);
                    imageLabel.setIcon(imageIcon);
                } catch (IOException exception) {
                    showError(exception.getMessage());
                }
            }
        });
        imagePanel.add(uploadButton);
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(46, 134, 193), 1));
        imagePanel.add(imageLabel);

        // 提交和取消按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton confirmButton = new JButton("确认");
        confirmButton.setBackground(new Color(46, 134, 193));
        confirmButton.setForeground(new Color(255, 255, 255));
        confirmButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        confirmButton.addActionListener(e -> {
            confirmAction();
        });
        buttonPanel.add(confirmButton);
        JButton cancelButton = new JButton("取消");
        cancelButton.setBackground(new Color(220, 220, 220));
        cancelButton.setForeground(new Color(68, 68, 68));
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelButton.addActionListener(e -> {
            dispose();
        });
        buttonPanel.add(cancelButton);

        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setOpaque(false);
        formPanel.setPreferredSize(new Dimension(700, 460));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.add(inputPanel, BorderLayout.CENTER);
        formPanel.add(imagePanel, BorderLayout.EAST);

        // 标题栏
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("添加题目");
        titleLabel.setForeground(new Color(46, 134, 193));
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 整个界面
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.setPreferredSize(new Dimension(800, 580));
        add(contentPanel);

        pack();
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void confirmAction() {
        // 获取输入值
        String id = idField.getText().trim();
        String type = typeField.getText().trim();
        String stem = stemField.getText().trim();
        String optionA = optionAField.getText().trim();
        String optionB = optionBField.getText().trim();
        String optionC = optionCField.getText().trim();
        String optionD = optionDField.getText().trim();
        String answer = answerField.getText().trim();
        String solution = solutionField.getText().trim();

        // 检查输入值是否为空
        if(type.isEmpty()){
            showError("请填写完整信息");
            return;
        }
        if(type.equals("单选题")||type.equals("多选题") ){
            if (id.isEmpty() || stem.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty() || answer.isEmpty() || solution.isEmpty()) {
                showError("请填写完整信息");
                return;
            }
        } else if (type.equals("判断题")) {
            if (id.isEmpty() || stem.isEmpty()  || answer.isEmpty() || solution.isEmpty()) {
                showError("请填写完整信息");
                return;
            }

        }else {
            showError("请填写正确的题型：单选题，多选题，判断题。");
        }

        // 创建 PreparedStatement 对象，执行 SQL 插入语句
        PreparedStatement stmt1 = null;
        Connection conn = null;
        int result = -100;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
            if(type.equals("单选题")){
                if (imageData == null) {
                    stmt1 = conn.prepareStatement(
                            "INSERT INTO allquestion (id, type, stem, a, b, c, d, answer, solution) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                } else {
                    stmt1 = conn.prepareStatement(
                            "INSERT INTO allquestion (id, type, stem, a, b, c, d, answer, solution,image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
                    stmt1.setBytes(10, imageData);

                }
                stmt1.setString(1, id);
                stmt1.setString(2, type);
                stmt1.setString(3, stem);
                stmt1.setString(4, optionA);
                stmt1.setString(5, optionB);
                stmt1.setString(6, optionC);
                stmt1.setString(7, optionD);
                stmt1.setString(8, answer);
                stmt1.setString(9, solution);
                result = stmt1.executeUpdate();
            } else if (type.equals("多选题")) {
                if (imageData == null) {
                    stmt1 = conn.prepareStatement(
                            "INSERT INTO allquestion (id, type, stem, a, b, c, d, answer, solution) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                } else {
                    stmt1 = conn.prepareStatement(
                            "INSERT INTO allquestion (id, type, stem, a, b, c, d, answer, solution,image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
                    stmt1.setBytes(10, imageData);
                }

                stmt1.setString(1, id);
                stmt1.setString(2, type);
                stmt1.setString(3, stem);
                stmt1.setString(4, optionA);
                stmt1.setString(5, optionB);
                stmt1.setString(6, optionC);
                stmt1.setString(7, optionD);
                stmt1.setString(8, answer);
                stmt1.setString(9, solution);
                result = stmt1.executeUpdate();
            } else if (type.equals("判断题")) {
                if (imageData == null) {
                    stmt1 = conn.prepareStatement(
                            "INSERT INTO allquestion (id, type, stem, answer, solution) VALUES (?, ?, ?, ?, ?)");
                } else {
                    stmt1 = conn.prepareStatement(
                            "INSERT INTO allquestion (id, type, stem, answer, solution,image) VALUES (?, ?, ?, ?, ?, ?)");
                    stmt1.setBytes(6, imageData);
                }

                stmt1.setString(1, id);
                stmt1.setString(2, type);
                stmt1.setString(3, stem);
                stmt1.setString(4, answer);
                stmt1.setString(5, solution);
                result = stmt1.executeUpdate();
            }else{
                showError("请填写正确的题型：单选题，多选题，判断题。");
            }

            if (result > 0) {
                showInfo();
                idField.setText("");
                typeField.setText("");
                stemField.setText("");
                optionAField.setText("");
                optionBField.setText("");
                optionCField.setText("");
                optionDField.setText("");
                answerField.setText("");
                solutionField.setText("");
                imageLabel.setIcon(null);
            } else {
                showError("添加失败！");
            }

        } catch (SQLException e) {
            showError(e.getMessage());
        } finally {
            try {
                if (stmt1 != null) {
                    stmt1.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                showError(e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo() {
        JOptionPane.showMessageDialog(this, "添加成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}