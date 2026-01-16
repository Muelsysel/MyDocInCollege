import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FrameModify extends JFrame {
    private JTextField idField;
    private JTextField newidField;
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

    public FrameModify() {
        super("修改题目");
        modifyUI();
    }

    private void modifyUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245)); // 设置背景色为淡灰色

        // 标题栏
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(250, 250, 250)); // 设置背景色为白色
        JLabel titleLabel = new JLabel("修改题目");
        titleLabel.setForeground(new Color(46, 134, 193)); // 设置前景色为蓝色
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 输入内容面板
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(245, 245, 245));
        inputPanel.setBorder(new EmptyBorder(20, 100, 20, 20)); // 设置边框留白
        inputPanel.setLayout(new GridLayout(10, 2, 10, 10)); // 设置网格布局和组件之间的行距和列距为10
        inputPanel.add(new JLabel("需要修改的题目编号："));
        idField = new JTextField(5);
        idField.setToolTipText("请输入需要修改题目编号");
        inputPanel.add(idField);

        inputPanel.add(new JLabel("修改后的题目编号："));
        newidField = new JTextField(5);
        newidField.setToolTipText("修改后的题目编号");
        inputPanel.add(newidField);

        inputPanel.add(new JLabel("修改后的题目类型："));
        typeField = new JTextField(10);
        typeField.setToolTipText("请输入修改后的题目类型");
        inputPanel.add(typeField);

        inputPanel.add(new JLabel("修改后的题目内容："));
        stemField = new JTextField(200);
        stemField.setToolTipText("请输入修改后的题目内容");
        inputPanel.add(stemField);

        inputPanel.add(new JLabel("修改后的选项A："));
        optionAField = new JTextField(15);
        optionAField.setToolTipText("请输入修改后的选项A");
        inputPanel.add(optionAField);

        inputPanel.add(new JLabel("修改后的选项B："));
        optionBField = new JTextField(15);
        optionBField.setToolTipText("请输入修改后的选项B");
        inputPanel.add(optionBField);

        inputPanel.add(new JLabel("修改后的选项C："));
        optionCField = new JTextField(15);
        optionCField.setToolTipText("请输入修改后的选项C");
        inputPanel.add(optionCField);

        inputPanel.add(new JLabel("修改后的选项D："));
        optionDField = new JTextField(15);
        optionDField.setToolTipText("请输入修改后的选项D");
        inputPanel.add(optionDField);

        inputPanel.add(new JLabel("修改后的答案："));
        answerField = new JTextField(10);
        answerField.setToolTipText("请输入修改后的答案");
        inputPanel.add(answerField);

        inputPanel.add(new JLabel("修改后的解析："));
        solutionField = new JTextField(200);
        solutionField.setToolTipText("请输入修改后的解析");
        inputPanel.add(solutionField);

        // 上传图片面板
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        imagePanel.setBackground(new Color(245, 245, 245));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // 设置边框留白
        JButton uploadButton = new JButton("上传图片");
        uploadButton.setBackground(new Color(46, 134, 193)); // 设置背景色为蓝色
        uploadButton.setForeground(new Color(255, 255, 255)); // 设置前景色为白色
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

        // 提交和取消按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));
        JButton confirmButton = new JButton("确认");
        confirmButton.setBackground(new Color(46, 134,193));
        confirmButton.setForeground(new Color(255, 255, 255));
        confirmButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        confirmButton.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                showError("需要修改的题目编号不能为空");
                return;
            }
            // 查询该题目的原始内容
            String querySQL = "SELECT * FROM allquestion WHERE id = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
                 PreparedStatement queryPS = conn.prepareStatement(querySQL)) {
                queryPS.setString(1, id);
                ResultSet rs = queryPS.executeQuery();

                if (rs.next()) {
                    String newId = rs.getString("id");
                    String type = rs.getString("type");
                    String stem = rs.getString("stem");
                    String a = rs.getString("a");
                    String b = rs.getString("b");
                    String c = rs.getString("c");
                    String d = rs.getString("d");
                    String answer = rs.getString("answer");
                    String solution = rs.getString("solution");
                    byte[] imageData = rs.getBytes("image");


                    // 根据用户的输入更新对应的字段
                    String newIdUserInput = newidField.getText().trim();
                    if (!newIdUserInput.isEmpty()) {
                        newId = newIdUserInput;
                    }

                    String typeUserInput = typeField.getText().trim();
                    if (!typeUserInput.isEmpty()) {
                        type = typeUserInput;
                    }

                    String stemUserInput = stemField.getText().trim();
                    if (!stemUserInput.isEmpty()) {
                        stem = stemUserInput;
                    }

                    String optionAUserInput = optionAField.getText().trim();
                    if (!optionAField.getText().isEmpty()) {
                        a = optionAUserInput;
                    }

                    String optionBUserInput = optionAField.getText().trim();
                    if (!optionAField.getText().isEmpty()) {
                        b = optionBUserInput;
                    }
                    String optionCUserInput = optionAField.getText().trim();
                    if (!optionAField.getText().isEmpty()) {
                        c = optionCUserInput;
                    }
                    String optionDUserInput = optionAField.getText().trim();
                    if (!optionAField.getText().isEmpty()) {
                        d = optionDUserInput;
                    }
                    String answerUserInput = answerField.getText().trim();
                    if (!answerUserInput.isEmpty()) {
                        answer = answerUserInput;
                    }

                    String solutionUserInput = solutionField.getText().trim();
                    if (!solutionUserInput.isEmpty()) {
                        solution = solutionUserInput;
                    }

                    // 执行更新操作
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE allquestion SET id=?, type=?, stem=?, a=?,b=?,c=?,d=?, answer=?, solution=?, image=? WHERE id=?")) {

                        ps.setString(1, newId);
                        ps.setString(2, type);
                        ps.setString(3, stem);
                        ps.setString(4, a);
                        ps.setString(5, b);
                        ps.setString(6, c);
                        ps.setString(7, d);
                        ps.setString(8, answer);
                        ps.setString(9, solution);

                        // 如果用户上传了图片，则将图片数据存储到数据库中
                        if (imageData != null) {
                            ps.setBinaryStream(10, new ByteArrayInputStream(imageData));
                        } else {
                            ps.setNull(10, Types.BLOB);
                        }

                        ps.setString(11, id);

                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(this, "修改成功！");
                            // 清空所有输入框和图片
                            idField.setText("");
                            newidField.setText("");
                            typeField.setText("");
                            stemField.setText("");
                            optionAField.setText("");
                            optionBField.setText("");
                            optionCField.setText("");
                            optionDField.setText("");
                            answerField.setText("");
                            solutionField.setText("");
                            imageLabel.setIcon(null);
                            imageData = null;
                        } else {
                            showError("修改失败，请检查题目编号是否正确");
                        }

                    } catch (SQLException exception) {
                        showError(exception.getMessage());
                    }
                } else {
                    showError("需要修改的题目不存在！");
                    return;
                }
            }catch (SQLException exception) {
                showError(exception.getMessage());
            }
        });
        buttonPanel.add(confirmButton);
        JButton cancelButton = new JButton("取消");
        cancelButton.setBackground(new Color(193, 193, 193));
        cancelButton.setForeground(new Color(255, 255, 255));
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        // 将组件添加到内容面板中
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(imagePanel, BorderLayout.EAST);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(contentPanel);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }
}