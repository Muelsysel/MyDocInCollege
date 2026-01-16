import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class FrameImport extends JFrame {

    private JTextField pathField;
    private byte[] fileData;

    public FrameImport() {
        super("导入题库");
        initUI();
    }

    private void initUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 文件路径
        JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel pathLabel = new JLabel("文件路径：");
        pathField = new JTextField(25);
        pathField.setEditable(false);
        pathPanel.add(pathLabel);
        pathPanel.add(pathField);

        // 上传文件按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton uploadButton1 = new JButton("上传文件");
        uploadButton1.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel文件", "xls"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    fileData = Files.readAllBytes(selectedFile.toPath());
                    pathField.setText(selectedFile.getAbsolutePath());
                } catch (IOException exception) {
                    showError(exception.getMessage());
                }
            }
        });

        buttonPanel.add(uploadButton1);
        // 确认和取消按钮
        JPanel buttonContainerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("确认导入");
        confirmButton.addActionListener(e -> {
            importAction();
        });
        buttonContainerPanel.add(confirmButton);
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> {
            dispose();
        });
        buttonContainerPanel.add(cancelButton);

        // 添加各个面板到内容面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 1));
        centerPanel.add(buttonPanel);
        contentPanel.add(centerPanel, BorderLayout.NORTH);
        contentPanel.add(pathPanel, BorderLayout.CENTER);
        contentPanel.add(buttonContainerPanel, BorderLayout.SOUTH);
        add(contentPanel);

        pack();
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void importAction() {
        // 检查输入值是否为空
        if (fileData == null) {
            showError("请选择要导入的文件");
            return;
        }

        // 创建 PreparedStatement 对象，执行 SQL 插入语句
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");

            // 定义 SQL 插入语句
            String sql = "INSERT INTO allQuestion (id,type, stem, a, b, c, d, answer, solution, image) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // 创建 PreparedStatement 对象，并设置占位符参数的值
            stmt = conn.prepareStatement(sql);
            // 解析 Excel 文件，将题目信息插入数据库
            Workbook workbook = Workbook.getWorkbook(new ByteArrayInputStream(fileData));
            Sheet sheet = workbook.getSheet(0); // 假设所有数据都在第一个 sheet 中
            int rows = sheet.getRows();
            for (int i = 1; i < rows; i++) { // 第一行是表头，跳过
                Cell[] cells = sheet.getRow(i);

                // 获取题目信息
                String id =cells[0].getContents().trim();
                String type =cells[1].getContents().trim();
                String stem = cells[2].getContents().trim();
                String a = cells[3].getContents().trim();
                String b = cells[4].getContents().trim();
                String c = cells[5].getContents().trim();
                String d = cells[6].getContents().trim();
                String answer = cells[7].getContents().trim();
                String solution = cells[8].getContents().trim();
                byte[] image = null;
                // 假设照片数据存储在第七列，且文件格式为 JPG
                if (cells.length > 9 && cells[9].getType() == CellType.LABEL) {
                    File imageFile = new File(cells[7].getContents().trim());
                    if (imageFile.exists() && imageFile.isFile()) {
                        image = Files.readAllBytes(imageFile.toPath());
                    }
                }

                // 将题目信息插入数据库
                stmt.setString(1, id);
                stmt.setString(2, type); // 假设导入的是单选题，题目类型编号为 1
                stmt.setString(3, stem);
                stmt.setString(4, a);
                stmt.setString(5, b);
                stmt.setString(6, c);
                stmt.setString(7, d);
                stmt.setString(8, answer);
                stmt.setString(9, solution);
                if (image != null) {
                    stmt.setBytes(10, image);
                } else {
                    stmt.setNull(10, Types.BLOB);
                }
                stmt.addBatch();
            }

            // 执行插入操作
            stmt.executeBatch();

            showInfo("导入成功");
        } catch (SQLException e) {
            showError(e.getMessage());
        } catch (IOException | BiffException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                showError(e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误提示", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
